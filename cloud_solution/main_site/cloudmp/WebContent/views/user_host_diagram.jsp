<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
 <html>
  <head>
    <title>控制台-${productName}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8" />

    <link rel="icon" type="image/ico" href="<%=request.getContextPath()%>/assets/images/favicon.ico" />
    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/vendor/animate/animate.css">
    <link type="text/css" rel="stylesheet" media="all" href="<%=request.getContextPath()%>/assets/js/vendor/mmenu/css/jquery.mmenu.all.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/videobackground/css/jquery.videobackground.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap-checkbox.css">

    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen-bootstrap.css">

    <link href="<%=request.getContextPath()%>/assets/css/zhicloud.css" rel="stylesheet">
    <!-- ======================== -->

    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/css/rickshaw.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/morris/css/morris.css">
	<link href="<%=request.getContextPath()%>/assets/css/minimal.css" rel="stylesheet">
	
    

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->
  </head>
  <body class="bg-1">
     <!-- Preloader -->
    <div class="mask"><div id="loader"></div></div>
    <!--/Preloader -->
    <!-- Wrap all page content here -->
    <div id="wrap">
      <!-- Make page fluid -->
      <div class="row">
        <%@include file="/views/common/common_menus.jsp" %>
        <!-- Page content -->
        <div id="content" class="col-md-12">
          <!-- page header -->
          <div class="pageheader">
            <h2><i class="fa fa-cogs"></i> 资源监控</h2>
          </div>
          <!-- /page header -->
          <!-- content main container -->
          <div class="main">
            <!-- row -->
            <div class="row">
              <!-- col 8 -->
              <div class="col-md-10">
              
              
                <!-- tile -->
                <section class="tile color transparent-black" style="width:1075px;height: 210px;">
                  <!-- tile header -->
                  <div class="tile-header">
                    
                    <h1><a href="javascript:history.back();"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a> 主机信息</h1>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                      <a href="#" class="remove"><i class="fa fa-times"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->
                  <!-- tile body -->
                  <div class="tile-body">
                    <div class="clear"></div>
        <table width="720" border="0" cellspacing="0" cellpadding="0"	>
          <tr>
            <td>
           	 主机名：${server.displayName }
           	 
            </td>
            <td></td>
            <td>uuid：${realId}</td>
          </tr>
          <tr>
            <td>状态：${server.getSummarizedStatusText() }</td>
            <td></td>
            <td>镜像名：${server.sysImageName }</td>
          </tr>
          <tr>
            <td>系统用户名：${server.account }</td>
            <td></td>
            <td>系统密码：${server.password }</td>
          </tr>
          <tr>
            <td>外网IP：${server.outerIp }    
            </td>
            <td></td>
            <td>外网端口：${server.outerPort }</td>
          </tr>
          <tr>
            <td>内网IP：${server.innerIp }    
            </td>
            <td></td>
            <td>内网端口：${server.innerPort }</td>
          </tr>
          <tr>
            <td>创建时间：<fmt:formatDate value="${server.curCreateDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
            </td>
            <td></td>
            <td>真实主机名：${server.hostName }</td>
          </tr>
        </table>
      </div>
      </section>
      </div>
      <div class="col-md-6">
      <section class="tile color transparent-black" style="height: 290px;">
                    <div class="row">
                      
                      <div class="col-md-3" style="width:540px;padding-left:30px;">
                        
                        <h5><font color="#FFFFFF">CPU利用率</font></h5>
<!--                         <div id="line-cpu" style="height: 180px;width:690px;"></div> -->
						 <div id="main_line_1" style="height:248px;width: 495px;"></div>
                      </div>
                      </div>
                      </section>
                      </div>
                      <div class="col-md-6">
                      <section class="tile color transparent-black" style="height: 290px;">
                      <div class="row">
                       <div class="col-md-3" style="width:540px;margin-left:5px;">
                        
                        <h5><font color="#FFFFFF">内存利用率</font></h5>
						 <div id="main_line_3" style="height:248px;width: 495px;"></div>
                      </div>
                      
                    </div>
                    </section>
                    </div>
                    <div class="col-md-6">
                    <section class="tile color transparent-black" style="height: 290px;">
                    <div class="row">
                      <div class="col-md-3" style="width:540px;padding-left:30px;">
                        
                        <h5><font color="#FFFFFF">磁盘吞吐量</font></h5>
						 <div id="main_line_2" style="height:248px;width: 495px;"></div>
                      </div>
                      </div>
                      </section>
                      </div>
                      <div class="col-md-6">
                      <section class="tile color transparent-black" style="height: 290px;">
                       <div class="row">
                      <div class="col-md-3" style="width:540px;margin-left:5px;">
                        
                        <h5><font color="#FFFFFF">网络吞吐量</font></h5>
						 <div id="main_line_4" style="height:248px;width: 495px;"></div>
                      </div>
                    </div>
					</section>
                  </div>
                  <!-- /tile body -->
                <!-- /tile -->
			</div>
            </div>
            <!-- /row -->
          </div>
          <!-- /content container -->
        </div>
        <!-- Page content end -->
      </div>
      <!-- Make page fluid-->
    <!-- Wrap all page content end -->
    <section class="videocontent" id="video"></section>

	<script type="text/javascript" src="<%=request.getContextPath()%>/js/esl.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/echarts.js"></script>
      <script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>

    <script>
    
    var path = '<%=request.getContextPath()%>'; 
    var serverId = "${realId}";
      
   $(document).ready(function(){
	var cpuArray         = ['0','0','0','0','0','0','0'];
	var memoryArray      = ['0','0','0','0','0','0','0'];
	var diskIORead       = ['0','0','0','0','0','0','0'];
	var diskIOWrite      = ['0','0','0','0','0','0','0'];
	var networkIOReceive = ['0','0','0','0','0','0','0'];
	var networkIOSend    = ['0','0','0','0','0','0','0'];
	refreshData();
	self.si = window.setInterval(refreshData,2000);
	function refreshData(){ 
		jQuery.ajax({
	        url: '<%=request.getContextPath()%>/user/refreshData',
	        type: 'post', 
	        dataType: 'json',
	        async:false,
	        data:"id="+serverId,
	        timeout: 10000,
	        error: function()
	        { 
	        },
	        success: function(result)	        
	        {
	        	for(var i=0;i<6;i++){
					cpuArray[i] = cpuArray[i+1];
				}
				var cpu_usage = result.cpuUsage * 100;
				cpuArray[6] = cpu_usage.toFixed(0);
				
				
				for(var i=0;i<6;i++){
					memoryArray[i] = memoryArray[i+1];
				}
				var memory_usage = result.memoryUsage * 100;
				memoryArray[6] = memory_usage.toFixed(0);
				
				
				for(var i=0;i<6;i++){
					diskIORead[i] = diskIORead[i+1];
				}
				var diskRead = result.diskIOReadByte / 1024 /1024;
				diskIORead[6] = diskRead.toFixed(0);
				
				
				for(var i=0;i<6;i++){
					diskIOWrite[i] = diskIOWrite[i+1];
				}
				var diskWrite = result.diskIOWriteByte / 1024 /1024;
				diskIOWrite[6] = diskWrite.toFixed(0);
				
				
				for(var i=0;i<6;i++){
					networkIOReceive[i] = networkIOReceive[i+1];
				}
				var networkReceive = result.networkIOReceiveByte / 1024;
				networkIOReceive[6] = networkReceive.toFixed(0);
				
				
				for(var i=0;i<6;i++){
					networkIOSend[i] = networkIOSend[i+1];
				}
				var networkSend = result.networkIOSendByte / 1024;
				networkIOSend[6] = networkSend.toFixed(0);
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
			                var mainChart2 = ec.init(document.getElementById('main_line_4'));
				            var mainChart3 = ec.init(document.getElementById('main_line_2'));
			                var mainChart4 = ec.init(document.getElementById('main_line_3'));
			                var option1 = {
			                	   color:['#CC99CC','#99FF00'],
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
			            		        formatter:'{a0}:{c0}</br>{a1}:{c1}'
			            		    },
			            		    legend: {
			            		        data:['磁盘读','磁盘写'],
			            		        x:350,
			            		        y:'bottom',
			            		        textStyle:{
			            		        	color:'#FFF'
			            		        }
			            		    },
			            		    grid: {
			            		    	x:'30',
			            		    	y:'40',
			            	            width: '450',
			            	            height: '160',
			            	            backgroundColor: 'rgba(0,0,0,0)',
			            	            borderWidth: 0,
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
			            		                    color: '#FFFFFF',
			            		                    fontFamily: 'sans-serif',
			            		                    fontSize: 12,
			            		                    fontStyle: 'normal',
			            		                    fontWeight: 'normal'
			            		                }
				            	            },
				            	            axisLine : {
				            	            	show:true,
			            		            	lineStyle:{
			            		            		width:1,
			            		            		color:'#ccc'
			            		            	}
			            		            },
				            	            splitLine : {
				            	            	show:false
				            	            }
			            		        }
			            		    ],
			            		    yAxis : [
			            		        {
			            		        	show:true,
			            		            type : 'value',
			            		            axisLine : {
			            		            	show:true
			            		            },
			            		            axisLabel : {
			            		                formatter: '{value}',
			            		                margin:4,
			            		                textStyle: {
			            		                    color: '#FFFFFF',
			            		                    fontFamily: 'sans-serif',
			            		                    fontSize: 12,
			            		                    fontStyle: 'normal',
			            		                    fontWeight: 'normal'
			            		                }
			            		            },
			            		            axisLine : {
				            	            	show:true,
			            		            	lineStyle:{
			            		            		width:1,
			            		            		color:'#ccc'
			            		            	}
			            		            },
			            		            splitArea : {show : false}
			            		        }
			            		    ],
			            		    series : [
			            		        {
			            		            name:'磁盘读',
			            		            type:'line',
			            		            itemStyle: {
			            		                normal: {
			            		                    lineStyle: {
			            		                    	width:3,
			            		                        shadowColor : 'rgba(0,0,0,0)',
			            		                        shadowBlur: 0,
			            		                        shadowOffsetX: 0,
			            		                        shadowOffsetY: 0
			            		                    }
			            		                }
			            		            },
				            		        data:diskIORead
			            		        },
			            		        {
			            		            name:'磁盘写',
			            		            type:'line',
			            		            itemStyle: {
			            		                normal: {
			            		                    lineStyle: {
			            		                    	width:3,
			            		                        shadowColor : 'rgba(0,0,0,0)',
			            		                        shadowBlur: 0,
			            		                        shadowOffsetX: 0,
			            		                        shadowOffsetY: 0
			            		                    }
			            		                }
			            		            },
				            		        data:diskIOWrite
			            		        },
			            		    ]
			            		}; 
			               var option3 = {
			            		   color:['#CC99CC'],
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
			            		        trigger: 'item',
			            		        formatter:'{a}:{c}%'
			            		    },
			            		    legend: {
			            		        data:['']
			            		    },
			            		    grid: {
			            		    	x:'50',
			            		    	y:'40',
			            		    	width: '430',
			            	            height: '170',
			            	            backgroundColor: 'rgba(0,0,0,0)',
			            	            borderWidth: 0,
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
			            		                    color: '#FFFFFF',
			            		                    fontFamily: 'sans-serif',
			            		                    fontSize: 12,
			            		                    fontStyle: 'normal',
			            		                    fontWeight: 'normal'
			            		                }
				            	            },
				            	            axisLine : {
				            	            	show:true,
			            		            	lineStyle:{
			            		            		width:1,
			            		            		color:'#ccc'
			            		            	}
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
			            		                    color: '#FFFFFF',
			            		                    fontFamily: 'sans-serif',
			            		                    fontSize: 12,
			            		                    fontStyle: 'normal',
			            		                    fontWeight: 'normal'
			            		                }
			            		            },
			            		            axisLine : {
				            	            	show:true,
			            		            	lineStyle:{
			            		            		width:1,
			            		            		color:'#ccc'
			            		            	}
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
			            		                    	width:3,
			            		                        shadowColor : 'rgba(0,0,0,0)',
			            		                        shadowBlur: 0,
			            		                        shadowOffsetX: 0,
			            		                        shadowOffsetY: 0
			            		                    }
			            		                }
			            		            },
			            		            data:cpuArray
			            		        }
			            		    ]
			            		};
			               var option4 = {
			            		   color:['#CC99CC','#99FF00'],
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
			            		        formatter:'{a0}:{c0}</br>{a1}:{c1}'
			            		    },
			            		    legend: {
			            		        data:['网络读','网络写'],
			            		        x:350,
			            		        y:'bottom',
			            		        textStyle:{
			            		        	color:'#FFF'
			            		        }
			            		    },
			            		    grid: {
			            		    	x:'30',
			            		    	y:'40',
			            	            width: '450',
			            	            height: '160',
			            	            backgroundColor: 'rgba(0,0,0,0)',
			            	            borderWidth: 0,
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
			            		                    color: '#FFFFFF',
			            		                    fontFamily: 'sans-serif',
			            		                    fontSize: 12,
			            		                    fontStyle: 'normal',
			            		                    fontWeight: 'normal'
			            		                }
				            	            },
				            	            axisLine : {
				            	            	show:true,
			            		            	lineStyle:{
			            		            		width:1,
			            		            		color:'#ccc'
			            		            	}
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
			            		                formatter: '{value}',
			            		                textStyle: {
			            		                    color: '#FFFFFF',
			            		                    fontFamily: 'sans-serif',
			            		                    fontSize: 12,
			            		                    fontStyle: 'normal',
			            		                    fontWeight: 'normal'
			            		                }
			            		            },
			            		            axisLine : {
				            	            	show:true,
			            		            	lineStyle:{
			            		            		width:1,
			            		            		color:'#ccc'
			            		            	}
			            		            },
			            		            splitArea : {show : false}
			            		        }
			            		    ],
			            		    series : [
			            		        {
			            		            name:'网络读',
			            		            type:'line',
			            		            itemStyle: {
			            		                normal: {
			            		                    lineStyle: {
			            		                    	width:3,
			            		                        shadowColor : 'rgba(0,0,0,0.4)',
			            		                        shadowBlur: 0,
			            		                        shadowOffsetX: 0,
			            		                        shadowOffsetY: 0
			            		                    }
			            		                }
			            		            },
			            		            data:networkIOReceive
			            		        },
			            		        {
			            		            name:'网络写',
			            		            type:'line',
			            		            itemStyle: {
			            		                normal: {
			            		                    lineStyle: {
			            		                    	width:3,
			            		                        shadowColor : 'rgba(0,0,0,0.4)',
			            		                        shadowBlur: 0,
			            		                        shadowOffsetX: 0,
			            		                        shadowOffsetY: 0
			            		                    }
			            		                }
			            		            },
			            		            data:networkIOSend
			            		        }
			            		    ]
			            		};
			               var option5 = {
			            		   color:['#CC99CC'],
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
			            		        trigger: 'item',
			            		        formatter:'{a}:{c}%'
			            		    },
			            		    legend: {
			            		        data:['']
			            		    },
			            		    grid: {
			            		    	x:'50',
			            		    	y:'40',
			            		    	width: '430',
			            	            height: '170',
			            	            backgroundColor: 'rgba(0,0,0,0)',
			            	            borderWidth: 0,
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
			            		                    color: '#FFFFFF',
			            		                    fontFamily: 'sans-serif',
			            		                    fontSize: 12,
			            		                    fontStyle: 'normal',
			            		                    fontWeight: 'normal'
			            		                }
				            	            },
				            	            axisLine : {
				            	            	show:true,
			            		            	lineStyle:{
			            		            		width:1,
			            		            		color:'#ccc'
			            		            	}
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
			            		                    color: '#FFFFFF',
			            		                    fontFamily: 'sans-serif',
			            		                    fontSize: 12,
			            		                    fontStyle: 'normal',
			            		                    fontWeight: 'normal'
			            		                }
			            		            },
			            		            axisLine : {
				            	            	show:true,
			            		            	lineStyle:{
			            		            		width:1,
			            		            		color:'#ccc'
			            		            	}
			            		            },
			            		            splitArea : {show : false}
			            		        }
			            		    ],
			            		    series : [
			            		        {
			            		            name:'内存使用率',
			            		            type:'line',
			            		            itemStyle: {
			            		                normal: {
			            		                    lineStyle: {
			            		                    	width:3,
			            		                        shadowColor : 'rgba(0,0,0,0)',
			            		                        shadowBlur: 0,
			            		                        shadowOffsetX: 0,
			            		                        shadowOffsetY: 0
			            		                    }
			            		                }
			            		            },
			            		            data:memoryArray
			            		        }
			            		    ]
			            		};
			               mainChart1.setOption(option3,true); 
			               mainChart2.setOption(option4,true); 
				           mainChart3.setOption(option1,true); 
			               mainChart4.setOption(option5,true); 
			           }
			    	);
	        }
		});
	}
});
    </script>
  </body>
</html>
      

      

      
 
