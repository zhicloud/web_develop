<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>资源监控 - 资源管理</title>
		<link type="image/x-icon" rel="shortcut icon" href="favicon.ico"/>
		<link type="text/css" rel="stylesheet"  href="<%=request.getContextPath()%>/css/global.css"/>
		<link type="text/css" rel="stylesheet"  href="<%=request.getContextPath()%>/css/glbmonitor.css"/>
	</head>
<body class="no-js" style="overflow:hidden;">
<div class="main">
	<section class="m-right" id="r_info">
		<div class="s-nav m-tabs">
			<ul>
				<li class="current"><a class="s-area" href="javascript:;" onclick="changeTab(this,'all')">全部</a></li>
				<li><a class="s-area" href="javascript:;" onclick="changeTab(this,'1')">广州</a></li>
				<li><a class="s-area" href="javascript:;" onclick="changeTab(this,'4')">香港</a></li>
				<li><a class="s-area" href="javascript:;" onclick="changeTab(this,'2')">成都</a></li>
			</ul>
		</div>
		<div class="clear"></div>
		<div class="m-container">
			<div class="m-content current">
				<div class="c-list" id="all_list">
				</div>
			</div>
			<!-- <div class="m-content">
				<div class="c-list" id="gz_list">
				</div>
			</div>
			<div class="m-content">
				<div class="c-list" id="xg_list">
				</div>
			</div>
			<div class="m-content">
				<div class="c-list" id="cd_list">
				</div>
			</div> -->
		</div>
	</section>
</div>
<!--JavaScript-->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/glbmonitor.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script src="<%=request.getContextPath()%>/js/echarts-all.js"></script>
<script type="text/javascript">
var all_interval;
var gz_interval;
var xg_intervak;
var cd_interval;
window.name = "selfWin";

var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;
//初始化数据
function initPieData(obj){
	var divid = "all_list";
	ajax.remoteCall("bean://monitorService:resourcePoolQuery",
			[obj], 
			function(reply) {
				if (reply.status == "exception") {
					if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
						top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
							top.location.reload();
						});
					}
				}
				$("#"+divid).html("");
				if(reply.result!=undefined&&reply.result.length>0){
					for(var i=0;i<reply.result.length;i++){
						var disname = reply.result[i].areaname+"-"+reply.result[i].poolname;
						var pie_id = "pie"+i;						
						var item_html = "<div class=\"c-iteam\"><div class=\"t\"><div class=\"t-left\">"+
						"<h3>资源池</h3><span>"+disname+"</span></div><div class=\"t-right\">"+
						<%-- "<a class=\"sourcecjk\" href=\"javascript:;\"><img src=\"<%=request.getContextPath()%>/img/sourcecjk.png\" alt=\"监控\"/></a>"+ --%>
						"<a class=\"sourcelist\" href=\"javascript:;\" onclick=\"ToHost(\'"+reply.result[i].poolid+"\',\'"+reply.result[i].poolname+"\')\"><img src=\"<%=request.getContextPath()%>/img/sourcelist.png\" alt=\"列表\"/></a>"+
						"</div></div><div class=\"s\"><div id=\""+pie_id+"\" style=\"height:220px;width:280px;\"></div></div></div>";						
						$("#"+divid).append(item_html);
						var myChart = echarts.init(document.getElementById(pie_id));
						var option = {
					    	    tooltip : {
					    	        trigger: 'item',
					    	        formatter: "{a} <br/>{b} : {c} ({d}%)"
					    	    },
					    	    legend: {
					    	        orient : 'horizontal',
					    	        x : 'center',
					    	        y : '200',
					    	        data:['正常','告警','故障','屏蔽']
					    	    },
					    	    calculable : true,
					    	    series : [
					    	        {
					    	            name:'节点状态',
					    	            type:'pie',
					    	            radius : [60, 70],
					    	            itemStyle : {
					    	                normal : {
					    	                    label : {
					    	                        show : false
					    	                    },
					    	                    labelLine : {
					    	                        show : false
					    	                    }
					    	                },
					    	                emphasis : {
					    	                    label : {
					    	                        show : true,
					    	                        position : 'center',
					    	                        textStyle : {
					    	                            fontSize : '20',
					    	                            fontWeight : 'bold'
					    	                        }
					    	                    }
					    	                }
					    	            },
					    	            data:[
					    	                {value:reply.result[i].normal, name:'正常',itemStyle:{normal:{color:'#99FF66'}}},
					    	                {value:reply.result[i].warn, name:'告警',itemStyle:{normal:{color:'#FFCC66'}}},
					    	                {value:reply.result[i].error, name:'故障',itemStyle:{normal:{color:'#FF0000'}}},
					    	                {value:reply.result[i].stop, name:'屏蔽',itemStyle:{normal:{color:'#666666'}}}
					    	            ]
					    	        }
					    	    ]
					    	};
						myChart.setOption(option,true);
					}
				}
			}
	); 
}
//标签切换
function changeTab(tab,obj){
	$("#r_info li").each(function(){
		$(this).removeAttr("class");
	});
	$(tab).parent().attr("class","current");
	initPieData(obj);
	if(obj=="all"){
		all_interval = self.setInterval("initPieData(\""+obj+"\")",10000);
		window.clearInterval(gz_interval);
		window.clearInterval(xg_interval);
		window.clearInterval(cd_interval);
	}
	if(obj=="1"){
		gz_interval = self.setInterval("initPieData(\""+obj+"\")",10000);
		window.clearInterval(all_interval);
		window.clearInterval(xg_interval);
		window.clearInterval(cd_interval);
	}
	if(obj=="4"){
		xg_interval = self.setInterval("initPieData(\""+obj+"\")",10000);
		window.clearInterval(all_interval);
		window.clearInterval(gz_interval);
		window.clearInterval(cd_interval);
	}
	if(obj=="2"){
		cd_interval = self.setInterval("initPieData(\""+obj+"\")",10000);
		window.clearInterval(all_interval);
		window.clearInterval(gz_interval);
		window.clearInterval(xg_interval);
	}
	//self.setInterval("initPieData("+obj+")",5000);
}
//跳转主机列表
function ToHost(obj,poolname){
	window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=monitorService&method=resourceToHostPage&poolid="+obj+"&poolname="+poolname+"&menuflag=resourcetohost";
}
$(function(){
	//绑定选项卡
/*     $('#r_info').zcloud_tab({
        tab : ['.m-tabs','li'],
        content : ['.m-container','.m-content'],
        current : 'current',
        classname : "current"
    }); */
    initPieData("all");
    all_interval = self.setInterval("initPieData(\"all\")",10000);
})
</script>
<!--/JavaScript-->
</body>
</html>