<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="java.util.*"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType    = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	
%>
<!DOCTYPE html>
<!-- database_monitor.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		<title>运营商 - 数据库监控</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/style.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		
		</style>
	</head>
	<body>
			
	<div class="box" style="margin:15px auto">
        <div class="dbox l">
          <div class="title l">CPU</div>
         <div class="clear"></div>
          <div class="unit">利用率</div>
         	 <div id="main_line_1" style="height:248px;border:1px solid #ccc;width: 300px;"></div></div>
          <div class="dbox l">
          <div class="title l">内存</div>
          <div class="title r"></div>
          <div class="clear"></div>
          <div class="unit">利用率</div>
          	<div id="main_line_2" style="height:248px;border:1px solid #ccc;width: 300px;"></div></div>
         <div class="dbox l">
          <div class="title l">硬盘</div>
          <div class="title r"></div>
          <div class="clear"></div>
          <div class="unit">利用率</div>
          	<div id="main_line_3" style="height:248px;border:1px solid #ccc;width: 300px;"></div></div>
        <div class="clear"></div>
      </div>
	</body>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/big.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/esl.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/echarts.js"></script>
	<script type="text/javascript">
		
	window.name = "selfWin";
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	
	var cpuArray        	 = ['0','0','0','0','0','0','0'];
	var memoryArray     	 = ['0','0','0','0','0','0','0'];
	var mysqlDataArray       = ['0','0','0','0','0','0','0'];
	var diskUsed     	 	 = '0';
	var diskAvaliabled 		 = '0';
	var diskUsedPercent		 = '0';
	
	var cpu_mem = window.setInterval(getCpuAndMemData,2000);
	getDiskData();
  	var disk = window.setInterval(getDiskData,300000);
 
 	function getCpuAndMemData(){ 
		ajax.remoteCall("bean://statementService:getCpuAndMemData", 
		[],
		function(reply) {

					if(reply.result.status == "fail") {
						top.$.messager.alert("警告","服务器繁忙，请稍后重试","warning");
						clearInterval(cpu_mem);
						return;
					}
					for(var i=0;i<6;i++){
						cpuArray[i] = cpuArray[i+1];
					}
					var cpu_load = reply.result.properties.mysql_cpu;
 					cpuArray[6] = cpu_load;
 					
					
					for(var i=0;i<6;i++){
						memoryArray[i] = memoryArray[i+1];
					}
					var memory_load = reply.result.properties.mysql_mem;
 					memoryArray[6] = memory_load;
 					
 					require.config({
				        paths:{
				            'echarts':'<%=request.getContextPath()%>/js/echarts',
				            'echarts/chart/bar' : '<%=request.getContextPath()%>/js/echarts',
				            'echarts/chart/pie' : '<%=request.getContextPath()%>/js/echarts',
				            'echarts/chart/line' : '<%=request.getContextPath()%>/js/echarts'
				        }
				    });
				    require(
				            [
				                'echarts',
				                'echarts/chart/bar',
				                'echarts/chart/pie',
				                'echarts/chart/line'
				            ],
				            function(ec) {
				                var mainChart1 = ec.init(document.getElementById('main_line_1'));
				                var mainChart2 = ec.init(document.getElementById('main_line_2'));

				                var option1 = {
				            		   color:['#4FA6F6'],
				            		   animation:false,
				            		    title : {
				            		        text: '',
				            		        x:'center',
					                        textStyle:{
					                            fontSize: 8,
					                            fontWeight: 'bolder',
					                            color: '#999'
					                        }
				            		    },
				            		    tooltip : {
				            		        trigger: 'axis',
				            		        formatter:'{a}:{c}%'
				            		    },
				            		    legend: {
				            		        data:['']
				            		    },
				            		    grid: {
				            		    	x:'50',
				            		    	y:'40',
				            		    	width: '240',
				            	            height: '170',
				            	            backgroundColor: 'rgba(0,0,0,0)',
				            	            borderWidth: 1,
				            	            borderColor: '#ccc'
				            	        },
				            		    toolbox: {
				            		        show : true,
				            		        feature : {
				            		            mark : {show: false},
				            		            dataView : {show: false, readOnly: false},
				            		            magicType : {show: false, type: ['line', 'bar']},
				            		            restore : {show: false},
				            		            saveAsImage : {show: false}
				            		        }
				            		    },
				            		    calculable : false,
				            		    xAxis : [
				            		        {
				            		            type : 'category',
				            		            boundaryGap : false,
			 	            		            data : ['0s','','','','','','12s'],
				            		            axisTick : {    // 轴标记
					            	                show:false
					            	            },
					            	            axisLabel : {
					            	            	show:true,
					            	            	margin:4,
					            	            	textStyle: {
				            		                    color: '#9a9a9a',
				            		                    fontFamily: 'sans-serif',
				            		                    fontSize: 12,
				            		                    fontStyle: 'normal',
				            		                    fontWeight: 'normal'
				            		                }
					            	            },
					            	            axisLine : {
				            		            	show:false
				            		            },
					            	            splitLine : {
					            	            	show:false
					            	            }
				            		        },
				            		    ],
				            		    yAxis : [
				            		        {
				            		            type : 'value',
				            		            axisLabel : {
				            		                formatter: '{value}%',
				            		                textStyle: {
				            		                    color: '#9a9a9a',
				            		                    fontFamily: 'sans-serif',
				            		                    fontSize: 12,
				            		                    fontStyle: 'normal',
				            		                    fontWeight: 'normal'
				            		                }
				            		            },
				            		            axisLine : {
				            		            	show:false
				            		            },
				            		            splitArea : {show : false},
				            		            min:0,
				            		            max:100
				            		        }
				            		    ],
				            		    series : [
				            		        {
				            		            name:'CPU使用率',
				            		            type:'line',
				            		            itemStyle: {
				            		                normal: {
				            		                    lineStyle: {
				            		                    	width:1,
				            		                        shadowColor : 'rgba(0,0,0,0)',
				            		                        shadowBlur: 0,
				            		                        shadowOffsetX: 0,
				            		                        shadowOffsetY: 0
				            		                    }
				            		                }
				            		            },
				            		            data:cpuArray,
				            		            markPoint : {
				            		                data : [
				            		                    {type : 'max', name: '最大值'},
				            		                    {type : 'min', name: '最小值'}
				            		                ]
				            		            }
				            		        }
				            		    ]
				            		};
				               var option2 = {
				            		   color:['#4FA6F6'],
				            		   animation:false,
				            		   title : {
				            		        text: '',
				            		        x:'center',
					                        textStyle:{
					                            fontSize: 8,
					                            fontWeight: 'bolder',
					                            color: '#999'
					                        }
				            		    },
				            		    tooltip : {
				            		        trigger: 'axis',
				            		        formatter:'{a}:{c}'
				            		    },
				            		    legend: {
				            		        data:['']
				            		    },
				            		    grid: {
				            		    	x:'50',
				            		    	y:'40',
				            		    	width: '240',
				            	            height: '170',
				            	            backgroundColor: 'rgba(0,0,0,0)',
				            	            borderWidth: 1,
				            	            borderColor: '#ccc'
				            	        },
				            		    toolbox: {
				            		        show : true,
				            		        feature : {
				            		            mark : {show: false},
				            		            dataView : {show: false, readOnly: false},
				            		            magicType : {show: false, type: ['line', 'bar']},
				            		            restore : {show: false},
				            		            saveAsImage : {show: false}
				            		        }
				            		    },
				            		    calculable : false,
				            		    xAxis : [
				            		        {
				            		            type : 'category',
				            		            boundaryGap : false,
				            		            data : ['0s','','','','','','12s'],
				            		            axisTick : {    // 轴标记
					            	                show:false
					            	            },
					            	            axisLabel : {
					            	            	show:true,
					            	            	margin:4,
					            	            	textStyle: {
				            		                    color: '#9a9a9a',
				            		                    fontFamily: 'sans-serif',
				            		                    fontSize: 12,
				            		                    fontStyle: 'normal',
				            		                    fontWeight: 'normal'
				            		                }
					            	            },
					            	            axisLine : {
				            		            	show:false
				            		            },
					            	            splitLine : {
					            	            	show:false
					            	            }
				            		        }
				            		    ],
				            		    yAxis : [
				            		        {
				            		            type : 'value',
				            		            axisLabel : {
				            		                formatter: '{value}%',
				            		                textStyle: {
				            		                    color: '#9a9a9a',
				            		                    fontFamily: 'sans-serif',
				            		                    fontSize: 12,
				            		                    fontStyle: 'normal',
				            		                    fontWeight: 'normal'
				            		                }
				            		            },
				            		            axisLine : {
				            		            	show:false
				            		            },
				            		            splitArea : {show : false},
				            		            min:0,
				            		            max:100
				            		        }
				            		    ],
				            		    series : [
				            		        {
				            		            name:'内存使用率',
				            		            type:'line',
				            		            itemStyle: {
				            		                normal: {
				            		                    lineStyle: {
				            		                    	width:1,
				            		                        shadowColor : 'rgba(0,0,0,0)',
				            		                        shadowBlur: 0,
				            		                        shadowOffsetX: 0,
				            		                        shadowOffsetY: 0
				            		                    }
				            		                }
				            		            },
				            		            data:memoryArray,
				            		            markPoint : {
				            		                data : [
				            		                    {type : 'max', name: '最大值'},
				            		                    {type : 'min', name: '最小值'}
				            		                ]
				            		            }
				            		        }
				            		    ]
				            		};
				          
				               mainChart1.setOption(option1,true); 
				               mainChart2.setOption(option2,true); 
				           }
				    	);
				}
			);
	};
	
	function getDiskData(){ 
		ajax.remoteCall("bean://statementService:getDiskData", 
		[],
		function(reply) {
			
				if(reply.result.status == "fail") {
					clearInterval(disk);
					return;
				}
					var disk_data_used = reply.result.properties.disk_data_used;
					var disk_data_avaliabled = reply.result.properties.disk_data_avaliabled;
					
 					require.config({
				        paths:{
				            'echarts':'<%=request.getContextPath()%>/js/echarts',
				            'echarts/chart/bar' : '<%=request.getContextPath()%>/js/echarts',
				            'echarts/chart/pie' : '<%=request.getContextPath()%>/js/echarts',
				            'echarts/chart/line' : '<%=request.getContextPath()%>/js/echarts'
				        }
				    });
				    require(
				            [
				                'echarts',
				                'echarts/chart/bar',
				                'echarts/chart/pie',
				                'echarts/chart/line'
				            ],
				            function(ec) {
				                var mainChart3 = ec.init(document.getElementById('main_line_3'));

				            		option3 = {
				            			    title : {
				            			        text: '',
				            			        x:'center'
				            			    },
				            			    tooltip : {
				            			        trigger: 'item',
				            			        formatter: "{a} <br/>{b} : {c} ({d}%)"
				            			    },
				            			    legend: {
				            			        orient : 'vertical',
				            			        x : 'right',
				            			        data:['已使用','可使用']
				            			    },
				            			    calculable : true,
				            			    series : [
				            			        {
				            			            name:'磁盘空间利用率(单位：字节)',
				            			            type:'pie',
				            			            radius : '55%',
				            			            center: ['50%', '60%'],
				            			            data:[
				            			                {value:disk_data_used, name:'已使用'},
				            			                {value:disk_data_avaliabled, name:'可使用'}
				            			            ]
				            			        }
				            			    ]
				            			};

				               
				          
				               mainChart3.setOption(option3,true); 
				           }
				    	);
				}
			);
	};
	
	
	
	</script>
</html>