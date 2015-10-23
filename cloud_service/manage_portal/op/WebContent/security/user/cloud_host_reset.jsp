<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.vo.SysDiskImageVO"%>
<%@page import="java.util.List"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	String uuid  = (String)request.getAttribute("uuid");
	Integer region  = (Integer)request.getAttribute("region");
	Integer resetFlag  = (Integer)request.getAttribute("reset_flag");
	List<SysDiskImageVO> sysDiskImageList = (List<SysDiskImageVO>)request.getAttribute("sysDiskImageList");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- cloud_host_reset.jsp -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/global.css" media="all"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/user/css/mycloud.css" media="all"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/unslider.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/goup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/plugin.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/common.js"></script>
<script type="text/javascript">
window.name = "selfWin";

var a = '<%= request.getContextPath() %>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
var uuid = '<%=uuid%>';
var resetFlag = '<%=resetFlag%>';


ajax.async = false;

	$(document).ready(function() {
		init(10, 2);
		if (name != '') {
			inituser(name, 0);
		} else {

			inituser();
		}
		initstep(1);
		setChecked();
        statusCheck();
	});
	
	function statusCheck() {
		if(resetFlag == 1) {
            refreshResetProgress();
			hideElement("main_section");
			
		}
	}

	function getLoginInfo(name, message, userId) {
		slideright();
		inituser(name, message);
		window.location.reload();
	};
	
	function showElement(targetid){
	    if (document.getElementById){
	        target=document.getElementById(targetid);
	        target.style.display="block";
	    }
	}
	
	function hideElement(targetid){
	    if (document.getElementById){
	        target=document.getElementById(targetid);
	        target.style.display="none";
	    }
	}
	
	
	
	function cloud_host_reset() {
		top.$.messager.confirm('提示',"确定使用该镜像重装系统？",function(r){
			if (r){
			var formData = $.formToBean(cloud_host_reset_form);
			ajax.remoteCall("bean://cloudHostService:resetHost", [ formData ],
					function(reply) {
						if (reply.status == "exception") {
							top.$.messager.alert('警告', reply.exceptionMessage,
									'warning');
						} else if (reply.result.status == "success") {
 							$("#reset_progress").html("安装中");
							$("#reset_progressbar_id").show();
							refreshResetProgress();
							nextstep();
						} else {
							top.$.messager.alert('警告', reply.result.message,
									'warning');
						}
					});
			}
		});
	};
	
	function refreshResetProgress(){
			ajax.remoteCall("bean://cloudHostService:getResetHostResult", 
					[ uuid ],
					function(reply) {
						if( reply.status == "exception" ) 
						{ 
							$("#reset_progress").html("安装失败");
							$("#reset_progressbar_id").hide(); 
							showElement("back_from_reset_to_main");
						} 
						else if(reply.result.status == "success") 
						{  
							$('#reset_progressbar').progressbar('setValue', parseInt(reply.result.properties.progress));
							if( reply.result.properties.reset_status==null )
							{
								window.setTimeout(self.refreshResetProgress, 1000);
							}
							else if( reply.result.properties.reset_status==false )
							{ 								
								$("#reset_progress").html("安装失败");
								$("#reset_progressbar_id").hide();
								showElement("back_from_reset_to_main");
							}
							else if( reply.result.properties.reset_status==true )
							{
								$("#reset_progress").html("安装成功");
								$("#reset_progressbar_id").hide();
								showElement("back_from_reset_to_main");
							}
						} 
						else 
						{ 
							// 云主机尚未开始安装，继续获取信息
							window.setTimeout(self.refreshResetProgress, 1000);
						}
					}
			);
			
	}
	
	/**
	 * 显示镜像描述
	 * @param description
	 */
	function showDescription(description){
		if(description!=''){		
			$("#isotitle").empty();
			$("#isotitle").append("版本信息："+description);
		}else{
			var chkObjs = document.getElementsByName("image_id"); 
		    for(var i=0;i<chkObjs.length;i++){
		        if(chkObjs[i].checked==true){  
		        	$("#isotitle").empty();
		        	$("#isotitle").append("版本信息："+$(chkObjs[i]).attr("imgdescription"));
		        	break;
		        }
		    } 
		}
	}
	
	function setChecked() {
		$("input[type='radio']").eq(0).prop("checked", true);
	}
	
</script>
<style type="text/css">
#porttable td {
	width: 60px;
	border-width: 0 1px 1px 0;
	border-style: dotted;
	margin: 0;
	padding: 0;
}

#porttable .headtr td {
	width: 60px;
	border-width: 0 1px 1px 0;
	border-style: dotted;
	margin: 0;
	padding: 0;
	background: #ddedef;
}

#porttable .nochange .datagrid-cell {
	color: #a2a2a2;
}

.main .wizard {
  height: 500px;
}
</style>
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
			<jsp:include page="/src/common/tpl/u_header.jsp"></jsp:include>
     <div class="main">
     <div class="wrap"><jsp:include page="/src/common/tpl/u_mcslider.jsp"></jsp:include></div>
				<form id="cloud_host_reset_form" method="post" class="wizard" >
				<input type="hidden" name="uuid" value="<%=uuid%>" />
				<input type="hidden" name="region" value="<%=region%>" />
 				<ul id="wizardbox" style="width:4000px;heigth:200px">
          			<li id="main_section">
					<div class="box"
						style="width: 720px; height: 210px; margin: 0 auto 0 auto">
						<table id="porttable" cellpadding="0" cellspacing="0" border="0"
							width="100%">
							<tr class="headtr">
								<td style="width: 231px; border-style:none none solid none"><div class="datagrid-cell"><center>云主机安装操作系统</center></div></td>
							</tr>
							<tr class="bodytr">
								<td style="width: 231px;high:250px; border-style:none none solid none">
									<div id="isotitle" style="text-align:center;"></div>
            							<div id="isoselect">
            								<% 
            									int flag = 0;
												for( SysDiskImageVO sysDiskImageOption : sysDiskImageList )
												{
													if (flag == 0) {
												%> 
				
				    				<input imgdescription="<%=sysDiskImageOption.getDescription() %>"  name="image_id" id="<%=sysDiskImageOption.getName()%>" class="radio" type="radio" value="<%=sysDiskImageOption.getRealImageId()%>" checked="checked"/>
 				 					<label onmouseout="showDescription('');" onmouseover="showDescription('<%=sysDiskImageOption.getDescription() %>');" for="<%=sysDiskImageOption.getName()%>" class="biglabel l"><%=sysDiskImageOption.getName()%></label> 
 				 					
 											<%
														flag++;
														continue;
													}

											%>

												<input imgdescription="<%=sysDiskImageOption.getDescription() %>"  name="image_id" id="<%=sysDiskImageOption.getName()%>" class="radio" type="radio" value="<%=sysDiskImageOption.getRealImageId()%>"/>
											<label onmouseout="showDescription('');" onmouseover="showDescription('<%=sysDiskImageOption.getDescription() %>');" for="<%=sysDiskImageOption.getName()%>" class="biglabel l"><%=sysDiskImageOption.getName()%></label>
											<%
											}
											%>
<!-- 									<div class="clear"></div>
              						<div class="easyui-pagination" data-options="showPageList: false,showRefresh: false,displayMsg: '',beforePageText: '',afterPageText: ''"></div> -->
            					</div>
								</td>
							</tr>
							<tr class="bodytr">
								<td style="border-style:none none solid none">
									<br/>
									1.重装操作系统将会格式化系统盘所有数据，其他盘不受影响！<br/>
									2.重装系统大概需要3-15分钟时间完成，请耐心等候！<br/>
									3.默认操作系统初始密码为：<strong>zhicloud@123</strong><br/>
									<br/>
								</td>
							</tr>
							<tr class="footertr">
								<td style="border-style:none none solid none">
								<div class="buttonbar" >
									<a class="bluebutton r" href="javascript:void(0);" onclick="cloud_host_reset()">马上安装操作系统</a><a class="graybutton r" href="javascript:void(0);" onclick="window.history.back()">返回</a>
								</div>
								</td>
								
							</tr>
							
						</table>
					</div>
				</li>
				<li id="reset_progressbar_section">
						  <div class="infoicon" style="font-size:16px; font-weight:bold" id="reset_progress">安装中</div>
            			  <div id="reset_progressbar_id">
                          	<div id="reset_progressbar" class="easyui-progressbar" value="0"  style="width:200px; margin-left:300px; margin:0 auto 0 auto;"></div>
                          </div>
                          <div class="buttonbar" style="margin:60px">
                          	<a class="bluebutton r" href="<%=request.getContextPath() %>/bean/page.do?userType=4&bean=cloudUserService&method=myCloudHostPage" onclick="self.location=document.referrer;">返回主机列表</a>
 							<a id="back_from_reset_to_main" class="graybutton r " href="javascript:void(0);" onclick="window.location.reload();" style="display:none">返回</a>
                          </div>
				</li>
 				</ul>
 				</form>
 					
			</div>
			<div class="clear"></div>
			    <jsp:include page="/src/common/tpl/u_footer.jsp"></jsp:include>

		</div>
		<div class="pageright">
			<iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp"frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
			<iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
		</div>
	</div>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
$(function(){
	navHighlight("umc","umcs");
});
</script>
</body>
</html>
