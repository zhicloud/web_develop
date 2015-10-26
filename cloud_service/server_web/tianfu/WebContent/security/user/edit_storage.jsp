<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.CloudDiskVO"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%> 
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	CloudDiskVO cloudDiskVO = (CloudDiskVO) request.getAttribute("cloudDiskVO");
%>

<!-- my_cloud_disk_mod.jsp -->

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
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/user/refreshprice.js"></script>
<%-- <script src="<%=request.getContextPath() %>/javascript/jquery.min.js"></script>  --%>
<script src="<%=request.getContextPath() %>/javascript/common.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
$(document).ready(function(){
	var userName = "<%=loginInfo.getAccount()%>";
	init(10,3);
	inituser(userName,0);
	//-----------
	var defaultDisk = "<%=CapacityUtil.toGBValue(cloudDiskVO.getDisk(),0)%>";
	$("#disk"+defaultDisk).click();
	//-----------
	$("input[name='disk']").click(function(){ 
		$("#disk_other").next("label").html("自定义(G)");
		$("#disk_other").next("label").removeClass("checked");
		var disk = $(this).val();
		$("#my_disk").html(disk);
	});
	$("#disk_other").focus(function(){
		$("input[name='disk']:checked").next("label").removeClass("checked");
		$("input[name='disk']:checked").removeAttr("checked");
		
	});
	$("#disk_other").blur(function(){
		var disk = $(this).val();
		if(disk == null || disk == "" || disk == undefined){
			$("#disk"+defaultDisk).click();
			disk = defaultDisk;
		}
		$("#my_disk").html(disk);
	});
	$("#save_btn").click(function(){
		save();
	});
});
//-----------------------------
function save() {
		var formData = $.formToBean(mod_cloud_disk_form);
		ajax.remoteCall("bean://cloudDiskService:updateCloudDiskById", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception")
				{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				}
				else if (reply.result.status == "success")
				{
					top.$.messager.alert('提示','修改成功','info',function(){ 
						
					});
				}
				else
				{
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};
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
      <div class="titlebar"><a href="javascript:void(0);" onclick="self.location=document.referrer;"><img src="<%=request.getContextPath()%>/image/button_back.png" width="22" height="30" alt="返回" /></a>
        <div class="r">修改配置</div>
      </div>
      <form id="mod_cloud_disk_form" method="post">
      <input id = "id" name = "id" type="hidden" value="<%=cloudDiskVO.getId()%>">
        <div style="width:720px; margin:30px auto 0 auto;">
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 30px 0">硬盘</span></div>
            <div class="l" style="width:600px;">
              <input name="disk" id="disk20" class="radio" type="radio" value="20" checked="checked" />
              <label for="disk20" class="smalllabel l">20G</label>
              <input name="disk" id="disk50" class="radio" type="radio" value="50" />
              <label for="disk50" class="smalllabel l">50G</label>
              <input name="disk" id="disk100" class="radio" type="radio" value="100" />
              <label for="disk100" class="smalllabel l">100G</label>
              <input name="disk" id="disk200" class="radio" type="radio" value="200" />
              <label for="disk200" class="smalllabel l">200G</label>
              <input name="disk" id="disk500" class="radio" type="radio" value="500" />
              <label for="disk500" class="smalllabel l">500G</label>
              <div class="custom smalllabel l" style="border:none; margin:0;">
                <input id="disk_other" name="diskDIY" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" type="text" class="smalllabel" title="G"/>
                <label for="disk_other" class="smalllabel l">自定义(G)</label>
              </div>
            </div>
            <div class="clear"></div>
             <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a id="save_btn" class="bluebutton r" href="javascript:void(0);">确认修改</a><a class="graybutton r" href="javascript:void(0);" onclick="self.location=document.referrer;">返回</a>
<!--              <i>单价：<strong>0.1元/小时</strong>　约合：<strong>72元/月</strong></i><br/> -->
              地域:<% if(cloudDiskVO.getRegion()==1){%>广州<%}else if(cloudDiskVO.getRegion()==4){ %>香港<% }%>　硬盘:<span id="my_disk"><%=CapacityUtil.toGBValue(cloudDiskVO.getDisk(),0) %></span>GB</div>
          </div>
      </form>
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
