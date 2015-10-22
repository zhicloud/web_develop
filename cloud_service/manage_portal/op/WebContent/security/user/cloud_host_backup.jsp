<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	String uuid  = (String)request.getAttribute("uuid");
	Integer region = (Integer)request.getAttribute("region");
	Integer backupFlag = (Integer)request.getAttribute("backup_flag");
	Integer resumeFlag = (Integer)request.getAttribute("resume_flag");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- cloud_host_backup.jsp -->
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
var region = '<%=region%>';
var backupFlag = '<%=backupFlag%>';
var resumeFlag = '<%=resumeFlag%>';

ajax.async = false;

	$(document).ready(function() {
		init(10, 2);
		if (name != '') {
			inituser(name, 0);
		} else {

			inituser();
		}
		initstep(1);
		queryBackup();
		statusCheck();
	});
	
	function statusCheck() {
		if(backupFlag == 1) {
			refreshBackupProgress();
			hideElement("main_section");
			hideElement("choice_section");
			
		}
		if(resumeFlag == 1) {
			refreshResumeProgress();
			hideElement("main_section");
			hideElement("choice_section");
			hideElement("backup_progressbar_section");

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
	
	
	
	function cloud_host_backup() {
		top.$.messager.confirm('提示',"备份将删除原有备份，确定现在备份？",function(r){
			if (r){
			var formData = $.formToBean(cloud_host_backup_form);
			ajax.remoteCall("bean://cloudHostService:backupHost", [ formData ],
					function(reply) {
						if (reply.status == "exception") {
							top.$.messager.alert('警告', reply.exceptionMessage,
									'warning');
						} else if (reply.result.status == "success") {
							$("#backup_progress").html("备份中");
							$("#backup_progressbar_id").show();
							refreshBackupProgress();
							nextstep();
						} else {
							top.$.messager.alert('警告', reply.result.message,
									'warning');
						}
					});
			}
		});
	};
	
	function cloud_host_resume() {
		top.$.messager.confirm('提示',"确定使用该备份恢复？",function(r){
		if (r){
				var formData = $.formToBean(cloud_host_resume_form);
				ajax.remoteCall("bean://cloudHostService:resumeHostBackup", [ formData ],
						function(reply) {
							if (reply.status == "exception") {
								top.$.messager.alert('警告', reply.exceptionMessage,
										'warning');
							} else if (reply.result.status == "success") {
								$("#resume_progress").html("恢复中");
								$("#resume_progressbar_id").show();
								refreshResumeProgress();
 								hideElement("choice_section");
								hideElement("backup_progressbar_section");
								nextstep();
								
							} else {
								top.$.messager.alert('警告', reply.result.message,
										'warning');
							}
						});
		    }
		});
		
	};
	
	function queryBackup() {
		var formData = $.formToBean(cloud_host_query_form);
		var timestamp;
        var mode;
        var disk;
        var avaliable;
		ajax.remoteCall("bean://cloudHostService:queryHostBackup", [formData],
				function(reply) {
					if (reply.status == "exception") {
						top.$.messager.alert('警告', reply.exceptionMessage,
								'warning');
					} else if (reply.result.status == "success") {

                        avaliable = reply.result.properties.avaliable;
                        if (avaliable){
                            timestamp = reply.result.properties.timestamp;
                            mode = reply.result.properties.mode;
                            disk = reply.result.properties.disk;

                            $("#resume_mode").val(mode);
                            $("#resume_disk").val(disk);

                            if(mode == 0){
                                $("#query_backup").html("<a href='javascript:void(0);' onclick='cloud_host_resume();'>"+timestamp+"－全备份</a>");
                            } else {
                                if(disk == 0) {
                                    $("#query_backup").html("<a href='javascript:void(0);' onclick='cloud_host_resume();'>"+timestamp+"－系统盘备份</a>");
                                } else {
                                    $("#query_backup").html("<a href='javascript:void(0);' onclick='cloud_host_resume();'>"+timestamp+"－数据盘备份</a>");

                                }
                            }
                        }

					} else {
						$("#query_backup").html(reply.result.message);
					}
				});
//        window.setTimeout(self.queryBackup, 10000);
	}; 
	
	function refreshBackupProgress(){ 
			ajax.remoteCall("bean://cloudHostService:getBackupHostResult", 
					[ uuid ],
					function(reply) {
						if( reply.status == "exception" ) 
						{ 
							$("#backup_progress").html("备份失败");
							$("#backup_progressbar_id").hide(); 
							showElement("back_from_backup_to_main");
						} 
						else if(reply.result.status == "success")
						{
							$('#backup_progressbar').progressbar('setValue', parseInt(reply.result.properties.progress));

							if( reply.result.properties.backup_status=="pending" )
							{
								window.setTimeout(self.refreshBackupProgress, 1000);
							}
							else if( reply.result.properties.backup_status=="false" )
							{
								$("#backup_progress").html("备份失败");
								$("#backup_progressbar_id").hide();
								showElement("back_from_backup_to_main");
							}
							else if( reply.result.properties.backup_status=="true" )
							{
								$("#backup_progress").html("备份成功");
								$("#backup_progressbar_id").hide();
								showElement("back_from_backup_to_main");
							}
                            $('#backup_progressbar').progressbar('setValue', parseInt(reply.result.properties.progress));

                        }
						else 
						{ 
							// 云主机尚未开始备份，继续获取信息
							window.setTimeout(self.refreshBackupProgress, 1000);
						}
					}
				); 
			
	}
	
	function refreshResumeProgress(){ 
			ajax.remoteCall("bean://cloudHostService:getResumeHostResult", 
					[ uuid ],
					function(reply) {
						if( reply.status == "exception" ) 
						{ 
							$("#resume_progress").html("恢复失败");
							$("#resume_progressbar_id").hide(); 
							showElement("back_from_resume_to_main");
						} 
						else if(reply.result.status == "success") 
						{
							$('#resume_progressbar').progressbar('setValue', parseInt(reply.result.properties.progress));
                            if( self.refreshResumeProgress==null )
							{
								return ;
							}
							if( reply.result.properties.resume_status==null )
							{
								window.setTimeout(self.refreshResumeProgress, 1000);
							}
							else if( reply.result.properties.resume_status==false )
							{ 								
								$("#resume_progress").html("恢复失败");
								$("#resume_progressbar_id").hide(); 
								showElement("back_from_resume_to_main");

							}
							else if( reply.result.properties.resume_status==true )
							{
								$("#resume_progress").html("恢复成功");
								$("#resume_progressbar_id").hide();
								showElement("back_from_resume_to_main");

							}
						} 
						else 
						{ 
							// 云主机尚未开始恢复，继续获取信息
							window.setTimeout(self.refreshResumeProgress, 1000);
						}
					}
				); 
			
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
  height: 250px;
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
				<form id="cloud_host_query_form" method="post">
					<input type="hidden" name="uuid" value="<%=uuid%>" />
				</form>
				
				<form id="cloud_host_resume_form" method="post">
					<input type="hidden" name="uuid" value="<%=uuid%>" />
                    <input id="resume_mode" type="hidden" name="mode" value=""/>
                    <input id="resume_disk" type="hidden" name="disk" value=""/>
                    <input type="hidden" name="region" value="<%=region%>" />
				</form>
			
				<form id="cloud_host_backup_form" method="post" class="wizard" >
 				<ul id="wizardbox" style="width:4000px;heigth:200px">
          			<li id="main_section">
					<div class="box"
						style="width: 720px; height: 210px; margin: 0 auto 0 auto">
						<table id="porttable" cellpadding="0" cellspacing="0" border="0"
							width="100%">
							<tr class="headtr">
								<td style="width: 231px; border-style:none none solid none"><div class="datagrid-cell"><center>云主机备份还原</center></div></td>
							</tr>
							<tr class="bodytr">
								<td style="width: 231px;high:250px; border-style:none none solid none">
									<br/>
								1.由于可能存在文件所定的情况，我们不承诺100%成功，若有非常重要的数据，请您自己备份。<br/>
								2.云主机还原功能将用以前的备份的虚拟硬盘VHD文件覆盖现有虚拟硬盘文件。<br/>
								3.由于虚拟硬盘VHD文件往往达到几十个G，因此备份及还原都需要一个小时或更长的时间才能完成。<br/>
									<br/>
								</td>
							</tr>
							
							
							<tr class="footertr">
								
								<td style="border-style:none none solid none">
								<div class="buttonbar" >
									上一次的备份&nbsp;&nbsp;<span id="query_backup"></span> <a class="bluebutton r" href="javascript:void(0);" onclick="nextstep()">马上备份</a><a class="graybutton r" href="javascript:void(0);" onclick="window.history.back()">返回</a>
								</div>
								</td>
								
							</tr>
							
						</table>
					</div>
				</li>
 				<li id="choice_section">
						<input type="hidden" name="uuid" value="<%=uuid%>" />
						<input type="hidden" name="region" value="<%=region%>" />
						<div class="l" style="width:600px;">
							<div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">备份类型</span></div>
						<table>
							<tr>
								<td>
									<input name="mode" id="mode1" class="radio" type="radio" value="0" checked="checked"/>
									<label for="mode1" class="smalllabel l" onclick="hideElement('disk_type')">全备份</label>
								</td>
								
								<td>
									<input name="mode" id="mode2" class="radio" type="radio" value="1" />
									<label for="mode2" class="smalllabel l" onclick="showElement('disk_type')">部分备份</label>
								</td>
							</tr>
						</table>
						</div>
						<div id="disk_type" style="width:600px;display:none;">
						<div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">备份盘选择</span></div>
              				<div id="option">
              				<table>
              					<tr>
              						<td>
              							<input name="disk" id="disk1" class="radio" type="radio" value="0" checked="checked"/>
										<label for="disk1" class="smalllabel l">系统盘</label>
									</td>
									<td>
										<input name="disk" id="disk2" class="radio" type="radio" value="1" />
										<label for="disk2" class="smalllabel l">数据盘</label>
									</td>
								</tr>
							</table>
              				</div>
              			</div>
						<div class="buttonbar" >
 							<a class="bluebutton r" href="javascript:void(0);" onclick="cloud_host_backup()">确认</a>
							<a class="graybutton r" href="javascript:void(0);" onclick="prevstep()">返回</a>
						</div>
					</li>
					<li id="backup_progressbar_section">
						  <div class="infoicon" style="font-size:16px; font-weight:bold" id="backup_progress">备份中</div>
            			  <div id="backup_progressbar_id">
                          	<div id="backup_progressbar" class="easyui-progressbar" value="0"  style="width:200px; margin-left:300px; margin:0 auto 0 auto;"></div>
                          </div>
                          <div class="buttonbar" style="margin:60px">
                          	<a class="bluebutton r" href="<%=request.getContextPath() %>/bean/page.do?userType=4&bean=cloudUserService&method=myCloudHostPage" onclick="self.location=document.referrer;">返回主机列表</a>
 							<a id="back_from_backup_to_main" class="graybutton r " href="javascript:void(0);" onclick="window.location.reload();" style="display:none">返回</a>
                          </div>
					</li>
					<li id="resume_progressbar_section">
						  <div class="infoicon" style="font-size:16px; font-weight:bold" id="resume_progress">恢复中</div>
            			  <div id="resume_progressbar_id">
                          	<div id="resume_progressbar" class="easyui-progressbar" value="0"  style="width:200px; margin-left:300px; margin:0 auto 0 auto;"></div>
                          </div>
                           <div class="buttonbar" style="margin:60px">
                          	<a class="bluebutton r" href="<%=request.getContextPath() %>/bean/page.do?userType=4&bean=cloudUserService&method=myCloudHostPage" onclick="self.location=document.referrer;">返回主机列表</a>
 							<a id="back_from_resume_to_main" class="graybutton r" href="javascript:void(0);" onclick="window.location.reload();" style="display:none">返回</a>
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
