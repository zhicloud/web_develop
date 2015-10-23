<%@page import="com.zhicloud.op.app.pool.CloudHostPoolManager"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.vo.CloudHostOpenPortVO"%>
<%@page import="com.zhicloud.op.vo.CloudDiskVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	CloudDiskVO cloudDisk = (CloudDiskVO)request.getAttribute("cloudDisk");
	List<CloudHostOpenPortVO> ports = (List<CloudHostOpenPortVO>)request.getAttribute("ports");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<%-- <script src="<%=request.getContextPath() %>/javascript/jquery.min.js"></script>  --%>
<script src="<%=request.getContextPath() %>/javascript/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/esl.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
var userName = "<%=loginInfo.getAccount()%>";
$(document).ready(function(){
	init(10,3);
	inituser(userName,0);
	//--------------
	
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
                var mainChart2 = ec.init(document.getElementById('main_pie_1'));
//                 var mainChart4 = ec.init(document.getElementById('main_line_3'));
                var option1 = {
                	animation:false,
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c}</br>({d}%)"
                    },
                    legend: {
                        orient : 'vertical',
                        x : 'left',
                        data:['已使用','未使用']
                    },
                    toolbox: {
                        show : false,
                        feature : {
                            mark : false,
                            dataView : {readOnly: false},
                            restore : true,
                            saveAsImage : false
                        }
                    },
                    calculable : false,
                    title : {
                        text: '磁盘占用率',
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
                            name:'磁盘占用率',
                            type:'pie',
                            radius : [0, 50],
                            center: ['50%', '50%'],
                            data:[
                                {value:50,name:'已使用',itemStyle:{
                                    normal:{
                                        color:'rgba(255,192,0,1)',
                                        labelLine : { 
		                                	show : false
	                                	},
	                                	label : { 
		                                	show : false
	                                	}
                                    }
                                }},
                                {value:180,name:'未使用',itemStyle:{
                                    normal:{
                                        color:'rgba(0,176,240,1)',
                                        labelLine : { 
		                                	show : false 
	                                	},
	                                	label : { 
		                                	show : false
	                                	}
                                    }
                                }}
                            ]
                        }
                    ]
                }; 
//                var option5 = {
//             		   animation:false,
//             		   title : {
//             		        text: '内存使用量',
//             		        x:'center',
// 	                        textStyle:{
// 	                            fontSize: 8,
// 	                            fontWeight: 'bolder',
// 	                            color: '#999'
// 	                        }
//             		    },
//             		    tooltip : {
//             		        trigger: 'axis',
//             		        formatter:'{a}:{c}'
//             		    },
//             		    legend: {
//             		        data:['']
//             		    },
//             		    grid: {
//             		    	x:'50',
//             		    	y:'40',
//             	            width: '330',
//             	            height: '150',
//             	            backgroundColor: 'rgba(0,0,0,0)',
//             	            borderWidth: 1,
//             	            borderColor: '#ccc'
//             	        },
//             		    toolbox: {
//             		        show : true,
//             		        feature : {
//             		            mark : {show: false},
//             		            dataView : {show: false, readOnly: false},
//             		            magicType : {show: false, type: ['line', 'bar']},
//             		            restore : {show: false},
//             		            saveAsImage : {show: false}
//             		        }
//             		    },
//             		    calculable : false,
//             		    xAxis : [
//             		        {
//             		            type : 'category',
//             		            boundaryGap : false,
// //				            		            data : ['2014/06/01','2014/06/02','2014/06/03','2014/06/04','2014/06/05','2014/06/06','2014/06/07'],
//             		            data : ['','','','','','',''],
//             		            axisTick : {    // 轴标记
// 	            	                show:false
// 	            	            },
// 	            	            axisLabel : {
// 	            	            	show:false
// 	            	            },
// 	            	            splitLine : {
// 	            	            	show:false
// 	            	            }
//             		        }
//             		    ],
//             		    yAxis : [
//             		        {
//             		            type : 'value',
//             		            axisLabel : {
//             		                formatter: '{value}M'
//             		            },
//             		            splitArea : {show : false}
//             		        }
//             		    ],
//             		    series : [
//             		        {
//             		            name:'内存使用量',
//             		            type:'line',
//             		            itemStyle: {
//             		                normal: {
//             		                    lineStyle: {
//             		                        shadowColor : 'rgba(0,0,0,0.4)',
//             		                        shadowBlur: 5,
//             		                        shadowOffsetX: 3,
//             		                        shadowOffsetY: 3
//             		                    }
//             		                }
//             		            },
//             		            data:memoryArray,
//             		            markPoint : {
//             		                data : [
//             		                    {type : 'max', name: '最大值'},
//             		                    {type : 'min', name: '最小值'}
//             		                ]
//             		            }
//             		        }
//             		    ]
//             		};
               mainChart2.setOption(option1,true); 
//                mainChart4.setOption(option5,true); 
           }
    	);
});
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
  <div class="pageleft">
    <div class="header"></div>
    <div class="main">
      <div class="titlebar"><a href="javascript:void(0);" onclick="window.history.back()"><img src="<%=request.getContextPath()%>/image/button_back.png" width="22" height="30" alt="返回" /></a>
        <div class="r">资源监控</div>
      </div>
<!--       <div class="box" style="text-align:center; margin:15px auto">创建时间：<b>2014-08-05　16:45:31</b>　　使用时间：<b>43天12小时23分56秒</b></div> -->
      <div class="box" style="width:960px">
        <div style="width:450px; height:215px; padding:15px; margin:0 auto;">
          <div class="diagramtitle">
            <div class="l"><b>磁盘占用率</b></div>
          </div>
          <div id="main_pie_1" class="diagramcontent"></div>
        </div>
        <div style="width:450px; height:215px; padding:15px; margin:0 auto;">
          <div class="diagramtitle">
            <div class="l"><b>磁盘读写量</b></div>
            <div class="r" style="color:#2a8cf6">写次数</div>
            <div class="r diagramdot" style="background:#2a8cf6">&nbsp;</div>
            <div class="r" style="color:#67aefb">读次数</div>
            <div class="r diagramdot" style="background:#67aefb">&nbsp;</div>
          </div>
          <div class="diagramcontent"><img src="<%=request.getContextPath()%>/image/t3.png" width="395" height="152" alt=" " /></div>
        </div>
      </div>
    </div>
    <div class="clear"></div>
    <div class="footer"></div>
  </div>
  <div class="pageright">
    <iframe id="loginiframe" src="<%=request.getContextPath() %>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath() %>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div>
</div>
</body>
</html>