<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="java.util.*"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType    = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	
	HashMap<String,Object> results = (HashMap<String,Object>)request.getAttribute("statement");
%>
<!DOCTYPE html>
<!-- statement_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		<title>运营商 - 统计报表</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		
		</style>
	</head>
	<body>
		<div>
			<div style="
	 				background-color:#fdfdfd;  
					margin:1px; 
					padding:1px;
				">
				<table style="width:280px; height:80px; padding:5px;">
					<tr>
						<td>当月新开用户:</td><td><%=results.get("newUserCount") %>位/<%=results.get("userCount") %>位</td>
					</tr>
					<tr>
						<td>总收入:</td><td><%=results.get("incomeCount") %>元</td>
					</tr>
					<tr>
						<td>其中:&nbsp;包月付费:&nbsp;<%=results.get("incomeCountForMonthly") %>元</td><td>按量付费:&nbsp;<%=results.get("incomeCountForDosage") %>元</td>
					</tr>
				</table>
			</div>
			
			<hr style="width:99%"/>
			
			<div style="height:50px;width: 800px; margin-left: 6px;float: left;">
				<table style="width:400px; height:50px;margin-bottom: 10px;">
					<tr>
						<td>当前云主机:&nbsp;<%=results.get("cloudHostCount") %>&nbsp;台</td><td></td>
					</tr>
				</table>
			</div>
			
			<hr style="width:99%; margin-bottom:10px;"/>
			
			<div style="height:250px;">
				<div id="main_pie_1" style="
					height:250px;
					border-right:1px solid #ccc;
					width: 250px; 
					margin-left: 20px;
					float: left;"></div>
				<div id="main_pie_2" style="
					height:250px;
					border-right:1px solid #ccc;
					width: 250px; 
					margin-left: 0px; 
					float: left;"></div> 
				<div id="main_pie_3" style="
					height:250px;
					width: 250px; 
					margin-left: 0px; 
					float: left;"></div>
			</div>
		</div>
	</body>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/big.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/esl.js"></script>
	<script type="text/javascript">
		
	window.name = "selfWin";
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	
	$(document).ready(function(){
	    require.config({
	        paths:{
	            'echarts':'<%=request.getContextPath()%>/js/echarts',
	            'echarts/chart/pie' : '<%=request.getContextPath()%>/js/echarts'
	        }
	  });
	    require(
	            [
	                'echarts',
	                'echarts/chart/pie'
	            ],
	            function(ec) {
	                var mainChart1 = ec.init(document.getElementById('main_pie_1'));
	                var mainChart2 = ec.init(document.getElementById('main_pie_2'));
	               	var mainChart3 = ec.init(document.getElementById('main_pie_3'));
	                var option1 = {
	                    tooltip : {
				show:false,
	                        trigger: 'item',
	                        formatter: "{a} <br/>{b} : {c} ({d}%)"
	                    },
	                    legend: {
	                        orient : 'vertical',
	                        x : 'right',
	                        y : 'bottom',
	                        data:['停止','告警','故障','运行']
	                    },
	                    toolbox: {
	                        show : true,
	                        feature : {
	                            mark : false,
	                            dataView : {readOnly: false},
	                            restore : true,
	                            saveAsImage : false
	                        }
	                    },
	                    calculable : false,
	                    title : {
	                        text: '宿主机',
	                        subtext: '',
	                        x:'center',
	                        textStyle:{
	                            fontSize: 8,
	                            fontWeight: 'bolder',
	                            color: '#999'
	                        }
	                    },
	                    series : [
	                        {
	                            name:'宿主机',
	                            type:'pie',
	                            radius : [40, 70],
	                            center: ['42%', '50%'],
	                            data:[
	                                {value:400,name:'停止',itemStyle:{
	                                    normal:{
	                                        color:'rgba(128,128,128,1)',
	                                        labelLine : { 
			                                	show : false
		                                	},
		                                	label : { 
			                                	show : false
		                                	}
	                                    },
	                                    emphasis : {
	                                        label : {
	                                            show : true,
	                                            position : 'center',
	                                            formatter:"{c}",
	                                            textStyle : {
	                                                fontSize : '15',
	                                                fontWeight : 'bold'
	                                            }
	                                        }
	                                    }
	                                }},
	                                {value:1124.39,name:'告警',itemStyle:{
	                                    normal:{
	                                        color:'rgba(255,165,0,1)',
	                                        labelLine : { 
			                                	show : false
		                                	},
		                                	label : { 
			                                	show : false
		                                	}
	                                    },
	                                    emphasis : {
	                                        label : {
	                                            show : true,
	                                            position : 'center',
	                                            formatter:"{c}",
	                                            textStyle : {
	                                                fontSize : '15',
	                                                fontWeight : 'bold'
	                                            }
	                                        }
	                                    }
	                                }},
	                                {value:1124.39,name:'故障',itemStyle:{
	                                    normal:{
	                                        color:'rgba(255,0,0,1)',
	                                        labelLine : { 
			                                	show : false
		                                	},
		                                	label : { 
			                                	show : false
		                                	}
	                                    },
	                                    emphasis : {
	                                        label : {
	                                            show : true,
	                                            position : 'center',
	                                            formatter:"{c}",
	                                            textStyle : {
	                                                fontSize : '15',
	                                                fontWeight : 'bold'
	                                            }
	                                        }
	                                    }
	                                }},
	                                {value:1124.39,name:'运行',itemStyle:{
	                                    normal:{
	                                        color:'rgba(70,255,70,1)',
	                                        labelLine : { 
			                                	show : false
		                                	},
		                                	label : { 
			                                	show : false
		                                	}
	                                    },
	                                    emphasis : {
	                                        label : {
	                                            show : true,
	                                            position : 'center',
	                                            formatter:"{c}",
	                                            textStyle : {
	                                                fontSize : '15',
	                                                fontWeight : 'bold'
	                                            }
	                                        }
	                                    }
	                                }}
	                            ]
	                        }
	                    ]
	                }; 
	
	               var option2 = {
	                    title : {
	                        text: '虚拟机',
	                        subtext: '',
	                        x:'center',
	                        textStyle:{
	                            fontSize: 8,
	                            fontWeight: 'bolder',
	                            color: '#999'
	                        }
	                    },
	                    tooltip : {
				show:false,
	                        trigger: 'item',
	                        formatter: "{a} <br/>{b} : {c} ({d}%)"
	                    },
	                    legend: {
	                        orient : 'vertical',
	                        x : 'right',
	                        y : 'bottom',
	                        data:['停止','告警','故障','运行']
	                    },
	                    toolbox: {
	                        show : true,
	                        feature : {
	                            mark : false,
	                            dataView : {readOnly: false},
	                            restore : true,
	                            saveAsImage : false
	                        }
	                    },
	                    calculable : false,
	                    series : [
	                        {
	                            name:'虚拟机',
	                            type:'pie',
	                            radius : [40, 70],
	                            center: ['42%', '50%'],
	                            data:[
	                                {value:1300, name:'停止',itemStyle:{
	                                    normal:{
	                                        color:'rgba(128,128,128,1)',
	                                        labelLine : { 
			                                	show : false
		                                	},
		                                	label : { 
			                                	show : false
		                                	}
	                                    },
	                                    emphasis : {
	                                        label : {
	                                            show : true,
	                                            position : 'center',
	                                            formatter:"{c}",
	                                            textStyle : {
	                                                fontSize : '15',
	                                                fontWeight : 'bold'
	                                            }
	                                        }
	                                    }
	                                }},
	                                {value:3672, name:'告警',itemStyle:{
	                                    normal:{
	                                        color:'rgba(255,165,0,1)',
	                                        labelLine : { 
			                                	show : false
		                                	},
		                                	label : { 
			                                	show : false
		                                	}
	                                    },
	                                    emphasis : {
	                                        label : {
	                                            show : true,
	                                            position : 'center',
	                                            formatter:"{c}",
	                                            textStyle : {
	                                                fontSize : '15',
	                                                fontWeight : 'bold'
	                                            }
	                                        }
	                                    }
	                                }},
	                                {value:3672, name:'故障',itemStyle:{
	                                    normal:{
	                                        color:'rgba(255,0,0,1)',
	                                        labelLine : { 
			                                	show : false
		                                	},
		                                	label : { 
			                                	show : false
		                                	}
	                                    },
	                                    emphasis : {
	                                        label : {
	                                            show : true,
	                                            position : 'center',
	                                            formatter:"{c}",
	                                            textStyle : {
	                                                fontSize : '15',
	                                                fontWeight : 'bold'
	                                            }
	                                        }
	                                    }
	                                }},
	                                {value:3672, name:'运行',itemStyle:{
	                                    normal:{
	                                        color:'rgba(70,255,70,1)',
	                                        labelLine : { 
			                                	show : false
		                                	},
		                                	label : { 
			                                	show : false
		                                	}
	                                    },
	                                    emphasis : {
	                                        label : {
	                                            show : true,
	                                            position : 'center',
	                                            formatter:"{c}",
	                                            textStyle : {
	                                                fontSize : '15',
	                                                fontWeight : 'bold'
	                                            }
	                                        }
	                                    }
	                                }}
	                            ]
	                        }
	                    ]
	                };
	               var option3 = {
	            		   title : {
		                        text: '磁盘空间',
		                        subtext: '',
		                        x:'center',
		                        textStyle:{
		                            fontSize: 8,
		                            fontWeight: 'bolder',
		                            color: '#999'
		                        }
		                    },
		                    tooltip : {
					show:false,
		                        trigger: 'item',
		                        formatter: "{a} <br/>{b} : {c} ({d}%)"
		                    },
		                    legend: {
		                        orient : 'vertical',
		                        x : 'right',
		                        y : 'bottom',
		                        data:['空闲','运行']
		                    },
		                    toolbox: {
		                        show : true,
		                        feature : {
		                            mark : false,
		                            dataView : {readOnly: false},
		                            restore : true,
		                            saveAsImage : false
		                        }
		                    },
		                    calculable : false,
		                    series : [
		                        {
		                            name:'磁盘空间',
		                            type:'pie',
		                            radius : [40, 70],
		                            center: ['42%', '50%'],
		                            data:[
		                                {value:600, name:'空闲',itemStyle:{
		                                    normal:{
		                                        color:'rgba(255,235,205,1)',
		                                        labelLine : { 
				                                	show : false
			                                	},
			                                	label : { 
				                                	show : false
			                                	}
		                                    },
		                                    emphasis : {
		                                        label : {
		                                            show : true,
		                                            position : 'center',
		                                            formatter:"{c}PB",
		                                            textStyle : {
		                                                fontSize : '15',
		                                                fontWeight : 'bold'
		                                            }
		                                        }
		                                    }
		                                }},
		                                {value:3672, name:'运行',itemStyle:{
		                                    normal:{
		                                        color:'rgba(70,255,70,1)',
		                                        labelLine : { 
				                                	show : false
			                                	},
			                                	label : { 
				                                	show : false
			                                	}
		                                    },
		                                    emphasis : {
		                                        label : {
		                                            show : true,
		                                            position : 'center',
		                                            formatter:"{c}PB",
		                                            textStyle : {
		                                                fontSize : '15',
		                                                fontWeight : 'bold'
		                                            }
		                                        }
		                                    }
		                                }}
		                            ]
		                        }
		                    ]
	            		};
	               mainChart1.setOption(option1,true); 
	               mainChart2.setOption(option2,true); 
	               mainChart3.setOption(option3,true);
	           }
	    );
	});
	</script>
</html>