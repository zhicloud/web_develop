<%@page import="java.math.BigDecimal"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.app.pool.CloudHostData"%>
<%@page import="com.zhicloud.op.app.pool.CloudHostPoolManager"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.vo.CloudHostOpenPortVO"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	CloudHostVO cloudHost = (CloudHostVO)request.getAttribute("cloudHost");
	BigDecimal hourPrice = cloudHost.getMonthlyPrice().divide(new BigDecimal("30"),BigDecimal.ROUND_HALF_UP).divide(new BigDecimal("24"),BigDecimal.ROUND_HALF_UP);
	List<CloudHostOpenPortVO> ports = (List<CloudHostOpenPortVO>)request.getAttribute("ports");
	CloudHostData cloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云代理商管理平台</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/agent.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/javascript/agent.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<%-- <script src="<%=request.getContextPath() %>/javascript/jquery.min.js"></script>  --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/esl.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/echarts.js"></script>
<script type="text/javascript">
var region = "<%=cloudHost.getRegion()%>";
var realHostId = "<%=cloudHost.getRealHostId()%>";
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=3");
var userName = "<%=loginInfo.getAccount()%>";
$(document).ready(function(){
	init(2);
	inituser(userName,0);
	//--------------
	
	var cpuArray         = ['0','0','0','0','0','0','0'];
	var memoryArray      = ['0','0','0','0','0','0','0'];
	var diskIORead       = ['0','0','0','0','0','0','0'];
	var diskIOWrite      = ['0','0','0','0','0','0','0'];
	var networkIOReceive = ['0','0','0','0','0','0','0'];
	var networkIOSend    = ['0','0','0','0','0','0','0'];
	refreshData();
	self.si = window.setInterval(refreshData,2000);
	function refreshData(){ 
		var cloudHostId = "<%=cloudHost.getRealHostId()%>";
		ajax.remoteCall("bean://cloudHostService:refreshData", 
				[ cloudHostId ],
				function(reply) {
					for(var i=0;i<6;i++){
						cpuArray[i] = cpuArray[i+1];
					}
					var cpu_usage = reply.result.cpuUsage;
					cpuArray[6] = cpu_usage.toFixed(0);
					
					console.info('memoryUsage:'+reply.result.memoryUsage);
					for(var i=0;i<6;i++){
						memoryArray[i] = memoryArray[i+1];
					}
					var memory_usage = reply.result.memoryUsage * 100;
					memoryArray[6] = memory_usage.toFixed(0);
					
					
					for(var i=0;i<6;i++){
						diskIORead[i] = diskIORead[i+1];
					}
					var diskRead = reply.result.diskIOReadByte / 1024 /1024;
					diskIORead[6] = diskRead.toFixed(0);
					
					
					for(var i=0;i<6;i++){
						diskIOWrite[i] = diskIOWrite[i+1];
					}
					var diskWrite = reply.result.diskIOWriteByte / 1024 /1024;
					diskIOWrite[6] = diskWrite.toFixed(0);
					
					
					for(var i=0;i<6;i++){
						networkIOReceive[i] = networkIOReceive[i+1];
					}
					var networkReceive = reply.result.networkIOReceiveByte / 1024;
					networkIOReceive[6] = networkReceive.toFixed(0);
					
					
					for(var i=0;i<6;i++){
						networkIOSend[i] = networkIOSend[i+1];
					}
					var networkSend = reply.result.networkIOSendByte / 1024;
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
// 				                var mainChart5 = ec.init(document.getElementById('main_pie_2'));
	//				                var mainChart6 = ec.init(document.getElementById('main_line_4'));
				                var option1 = {
				                	   color:['#4FA6F6','#58CB81'],
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
				            		        x:170,
				            		        y:'bottom',
				            		    },
				            		    grid: {
				            		    	x:'30',
				            		    	y:'40',
				            	            width: '270',
				            	            height: '160',
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
				            		        	show:false,
				            		            type : 'value',
				            		            axisLine : {
				            		            	show:false
				            		            },
				            		            axisLabel : {
				            		                formatter: '{value}',
				            		                margin:4,
				            		                textStyle: {
				            		                    color: '#9a9a9a',
				            		                    fontFamily: 'sans-serif',
				            		                    fontSize: 12,
				            		                    fontStyle: 'normal',
				            		                    fontWeight: 'normal'
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
				            		                    	width:1,
				            		                        shadowColor : 'rgba(0,0,0,0)',
				            		                        shadowBlur: 0,
				            		                        shadowOffsetX: 0,
				            		                        shadowOffsetY: 0
				            		                    }
				            		                }
				            		            },
 				            		            data:diskIORead,
//				            		            data:[12,34,22,11,5,31,20],
				            		            markPoint : {
				            		                data : [
				            		                    {type : 'max', name: '最大值'},
				            		                    {type : 'min', name: '最小值'}
				            		                ]
				            		            }
				            		        },
				            		        {
				            		            name:'磁盘写',
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
 				            		            data:diskIOWrite,
//				            		            data:[3,5,7,32,22,6,10],
				            		            markPoint : {
				            		                data : [
				            		                    {type : 'max', name: '最大值'},
				            		                    {type : 'min', name: '最小值'}
				            		                ]
				            		            }
				            		        },
				            		    ]
				            		}; 
				               var option3 = {
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
				               var option4 = {
				            		   color:['#4FA6F6','#58CB81'],
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
				            		        x:170,
				            		        y:'bottom'
				            		    },
				            		    grid: {
				            		    	x:'30',
				            		    	y:'40',
				            	            width: '270',
				            	            height: '160',
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
				            		                formatter: '{value}',
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
				            		                    	width:1,
				            		                        shadowColor : 'rgba(0,0,0,0.4)',
				            		                        shadowBlur: 0,
				            		                        shadowOffsetX: 0,
				            		                        shadowOffsetY: 0
				            		                    }
				            		                }
				            		            },
				            		            data:networkIOReceive,
				            		            markPoint : {
				            		                data : [
				            		                    {type : 'max', name: '最大值'},
				            		                    {type : 'min', name: '最小值'}
				            		                ]
				            		            }
				            		        },
				            		        {
				            		            name:'网络写',
				            		            type:'line',
				            		            itemStyle: {
				            		                normal: {
				            		                    lineStyle: {
				            		                    	width:1,
				            		                        shadowColor : 'rgba(0,0,0,0.4)',
				            		                        shadowBlur: 0,
				            		                        shadowOffsetX: 0,
				            		                        shadowOffsetY: 0
				            		                    }
				            		                }
				            		            },
				            		            data:networkIOSend,
				            		            markPoint : {
				            		                data : [
				            		                    {type : 'max', name: '最大值'},
				            		                    {type : 'min', name: '最小值'}
				            		                ]
				            		            }
				            		        }
				            		    ]
				            		};
				               var option5 = {
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
				               /* var option6 = {
				            		   animation:false,
				            		   title : {
				            		        text: '网络写',
				            		        x:'center',
					                        textStyle:{
					                            fontSize: 8,
					                            fontWeight: 'bolder',
					                            color: '#999'
					                        }
				            		    },
				            		    tooltip : {
				            		        trigger: 'axis'
				            		    },
				            		    legend: {
				            		        data:['']
				            		    },
				            		    grid: {
				            		    	x:'40',
				            		    	y:'80',
				            	            width: '165',
				            	            height: '110',
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
				            		            data : ['2014/06/01','2014/06/02','2014/06/03','2014/06/04','2014/06/05','2014/06/06','2014/06/07'],
				            		            axisTick : {    // 轴标记
					            	                show:false
					            	            },
					            	            axisLabel : {
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
				            		                formatter: '{value}Kb/s'
				            		            },
				            		            splitArea : {show : false}
				            		        }
				            		    ],
				            		    series : [
				            		        {
				            		            name:'网络写',
				            		            type:'line',
				            		            itemStyle: {
				            		                normal: {
				            		                    lineStyle: {
				            		                        shadowColor : 'rgba(0,0,0,0.4)',
				            		                        shadowBlur: 5,
				            		                        shadowOffsetX: 3,
				            		                        shadowOffsetY: 3
				            		                    }
				            		                }
				            		            },
				            		            data:[11, 11, 15, 13, 12, 13, 10],
				            		            markPoint : {
				            		                data : [
				            		                    {type : 'max', name: '最大值'},
				            		                    {type : 'min', name: '最小值'}
				            		                ]
				            		            }
				            		        }
				            		    ]
				            		}; */
				               mainChart1.setOption(option3,true); 
				               mainChart2.setOption(option4,true); 
					           mainChart3.setOption(option1,true); 
				               mainChart4.setOption(option5,true); 
// 				               mainChart5.setOption(option2,true); 
//				               mainChart6.setOption(option6,true); 
				           }
				    	);
				}
			);
	}
});
var old_host_name = "<%=cloudHost.getDisplayName()%>";
var current_host_id = "<%=cloudHost.getId()%>";
function changeHostName(){
	var new_host_name = $("#t1").val();
	$("#displayName").html(new_host_name);
	if(old_host_name == new_host_name){
		return;
	}
	if(new_host_name == ''||new_host_name.length>30){ 
		top.$.messager.alert("提示","主机名必须为1-30个字符","info",function(){
			window.location.reload();
		});
		return ;
	}
	
	if(current_host_id!='' && new_host_name!=''){
		ajax.remoteCall("bean://cloudHostService:updateHostNameById", 
			[ current_host_id,new_host_name],
			function(reply) {
				if (reply.status == "exception") 
				{
					if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
					{
						top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
							window.location.reload();
						});
					}
					else 
					{
						top.$.messager.alert("警告", "修改失败", "warning",function(){
							window.location.reload();
						});
					}
				} 
				else if (reply.result.status == "success") 
				{
					
					top.$.messager.alert('提示',reply.result.message,'info',function(){
						$("#displayName").html(new_host_name);
					}); 
				} 
				else 
				{
					top.$.messager.alert('警告',reply.result.message,'warning',function(){
						
					});
				}
			}
		);
		
	}else{
		 window.location.reload();
	}
}
//-----------
  function fclose() 
  { 
	  ajax.remoteCall("bean://cloudHostService:stopMyMonitor", 
				[realHostId],
				function(reply) {
					if (reply.status == "exception") 
					{
						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
						{
							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
								window.location.reload();
							});
						}
						else 
						{
						}
					} 
					else if (reply.result.status == "success") 
					{
					} 
					else 
					{
					}
				}
			);  
  } 

  function fload() 
  { 
	  ajax.remoteCall("bean://cloudHostService:startMonitoring", 
				[ region,realHostId],
				function(reply) {
					if (reply.status == "exception") 
					{
						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
						{
							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
								window.location.reload();
							});
						}
						else 
						{
						}
					} 
					else if (reply.result.status == "success") 
					{
					} 
					else 
					{
					}
				}
			); 
  } 

  function bfunload() 
  { 
  }
</script>
<!--[if IE 6]>
<script src="javascript/DD_belatedPNG.js"></script>
<script type="text/javascript">
	DD_belatedPNG.fix("*");
</script>
<![endif]-->
</head>

<body> 
<div class="page">
    <div class="header">
     <div class="top">
	   <a class="logo l" href="#"><img src="<%=request.getContextPath()%>/image/agent_logo.png" width="188" height="25" alt="致云代理商管理平台" /></a>
	   <div class="nav l">
	    <a href="#" id="business"><img id="agent_nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_1_i.png" width="21" height="21" />业务信息</a>
	    <a href="#" id="user_manage"><img id="agent_nav_2" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_2_i.png" width="21" height="21" />用户管理</a>
	    <a href="#" id="my_account"><img id="agent_nav_3" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_3_i.png" width="21" height="21" />我的账户</a>
	    <a href="#" id="oper_log"><img id="agent_nav_4" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_4_i.png" width="21" height="21" />操作日志</a>
	   </div>
	   <div class="user l">
	    <img class="reddot" src="<%=request.getContextPath()%>/image/reddot.png" width="6" height="6" alt=" " />
	    <a id="logoutlink" href="javascript:void(0);">注销</a>
	    <span>|</span>
	    <a id="userlink" href="javascript:void(0);"></a>
	   </div>
	   <div class="clear"></div>
	  </div>
  </div>
    <div class="main">
      <div class="titlebar"><a href="javascript:void(0);" onclick="self.location=document.referrer;"><img src="<%=request.getContextPath()%>/image/button_back.png" width="22" height="30" alt="返回" /></a>
        <div class="r">主机详情</div>
      </div>
      <div class="box" style="margin:15px auto;font-size:20px; height:30px;position:relative;">
<%--         <input id="t1" editable="false" name="" type="text" value="<%=cloudHost.getDisplayName() %>" class="dinput"/> --%>
<!--         <label for="t1" editable="false" class="dbutton"> -->
        <div id="displayName" class="l"><%=cloudHost.getDisplayName() %></div>
<%--         <div class="l"><img src="<%=request.getContextPath()%>/image/dedit.png" width="30" height="30" /></div> --%>
<!--         </label> -->
      </div>
      <div class="box"  style="border-bottom:solid 1px #d6d3d3; padding-bottom:15px;">
<%--         <div class="msg"><img src="<%=request.getContextPath()%>/image/msgicon.png" width="20" height="20" style="vertical-align:middle;" /> 主机磁盘空间不足</div> --%>
<%--         <div class="msg"><img src="<%=request.getContextPath()%>/image/msgicon.png" width="20" height="20" style="vertical-align:middle;" /> 余额不足</div> --%>
        <div class="clear"></div>
        <table width="720" border="0" cellspacing="0" cellpadding="0" class="dtable">
          <tr>
            <td>
            <%if("运行".equals(cloudHost.getSummarizedStatusText())){ %>
            	运行中
            <%}else if("关机".equals(cloudHost.getSummarizedStatusText())){ %>
            	已关机
            <%}else{%>
            	<%=cloudHost.getSummarizedStatusText()%>
            <%} %>
            </td>
            <td>创建时间：<%=StringUtil.dateToString(StringUtil.stringToDate(cloudHost.getCreateTime(),"yyyyMMddHHmmssSSS"),"yyyy-MM-dd HH:mm:ss") %></td>
            <td>暂无备案信息</td>
          </tr>
          <tr>
            <td><%=cloudHost.getSysImageNameOld()==null?"无系统信息":cloudHost.getSysImageNameOld() %></td>
            <td></td>
            <td></td>
          </tr>
          <tr>
            <td>IP：<%=cloudHost.getOuterIp() %></td>
            <td></td>
            <td><%=hourPrice %>元/小时　约合：<%=cloudHost.getMonthlyPrice() %>元/月</td>
          </tr>
        </table>
      </div>
      <div class="box" style="margin:15px auto">
        <div class="dbox l">
          <div class="title l">CPU</div>
          <div class="title r"><%=cloudHost.getCpuCore() %>核</div>
          <div class="clear"></div>
          <div class="unit">利用率</div>
         	 <div id="main_line_1" style="height:248px;border:1px solid #ccc;width: 319px;"></div></div>
          <div class="dbox r">
          <div class="title l">内存</div>
          <div class="title r"><%=cloudHost.getMemoryText(0) %></div>
          <div class="clear"></div>
          <div class="unit">利用率</div>
          	<div id="main_line_3" style="height:248px;border:1px solid #ccc;width: 319px;"></div></div>
          <div class="dbox l">
          <div class="title l">磁盘</div>
          <div class="title r">总量:<%=CapacityUtil.toGB(cloudHost.getSysDisk().add(cloudHost.getDataDisk()),0) %></div>
          <div class="clear"></div>
          <div class="unit">KB/S 吞吐量</div>
          	<div id="main_line_2" style="height:248px;border:1px solid #ccc;width: 319px;"></div></div>
          <div class="dbox r">
          <div class="title l">网络</div>
          <div class="title r"><%=cloudHost.getBandwidthText(0)%></div>
          <div class="clear"></div>
          <div class="unit">KB/S 吞吐量</div>
          	<div id="main_line_4" style="height:248px;border:1px solid #ccc;width: 319px;"></div></div>
        <div class="clear"></div>
      </div>
    <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
  </div>
</div>
</body>
</html>
