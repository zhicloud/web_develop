<%@ page pageEncoding="utf-8" %>
<%@ page import="com.zhicloud.ms.transform.constant.TransformConstant" %>
<%@ page import="com.zhicloud.ms.transform.util.TransFormLoginInfo" %>
<%@ page import="com.zhicloud.ms.transform.util.TransFormLoginHelper" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!-- common_menus.jsp -->
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
<link rel="icon" type="image/ico" href="<%=request.getContextPath()%>/assets/images/favicon.ico" />
<link href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/font-awesome/css/font-awesome.css" rel="stylesheet">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/vendor/animate/animate.min.css">
<link type="text/css" rel="stylesheet" media="all" href="<%=request.getContextPath()%>/assets/js/vendor/mmenu/css/jquery.mmenu.all.css" />
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/videobackground/css/jquery.videobackground.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap-checkbox.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/css/rickshaw.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/morris/css/morris.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/tabdrop/css/tabdrop.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/summernote/css/summernote.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/summernote/css/summernote-bs3.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen.min.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen-bootstrap.css">
<link href="<%=request.getContextPath()%>/assets/css/zhicloud.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/assets/js/jquery.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/vendor/bootstrap/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/mmenu/js/jquery.mmenu.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/sparkline/jquery.sparkline.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/nicescroll/jquery.nicescroll.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/animate-numbers/jquery.animateNumbers.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/videobackground/jquery.videobackground.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/blockui/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/vendor/tabdrop/bootstrap-tabdrop.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/vendor/chosen/chosen.jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/vendor/parsley/parsley.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/vendor/wizard/jquery.bootstrap.wizard.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/vendor/datatables/jquery.dataTables.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/vendor/datatables/ColReorderWithResize.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/vendor/datatables/colvis/dataTables.colVis.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/vendor/datatables/tabletools/ZeroClipboard.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/vendor/datatables/tabletools/dataTables.tableTools.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/vendor/datatables/dataTables.bootstrap.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/vendor/chosen/chosen.jquery.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/minimal.min.js"></script>
<script src="<%=request.getContextPath()%>/assets/js/vendor/parsley/parsley.min.js"></script>
<script>
var windowtemptimeoutcount = 0 ;
var windowtemptimeflag = false;
var windowintervalid;

function logout(){
	jQuery.ajax({
		type: "POST",
		async:false,
		url: "<%=request.getContextPath()%>/transform/logout",
		data: null,
		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
		success: function(result){
			var obj = eval("("+result+")");
			if(obj.status=="success"){
				window.location.href = "<%=request.getContextPath()%>";
			}else{
				return;
			}
		}
	});		
}

function ieclosewithlogout(){
	jQuery.ajax({
		type: "POST",
		async:false,
		url: "<%=request.getContextPath() %>/transform/logout",
		data: null,
		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
		success: function(result){}
	});
}

//定时更新后台服务器时间
function updateServeTime(){
	if(!windowtemptimeflag){
		jQuery.ajax({
			type: "POST",
			async:false,
			url: "<%=request.getContextPath() %>/transform/updateuserlogin",
			data: null,
			contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
			success: function(result){},
			error:function(result){
				windowtemptimeoutcount++;
				if(windowtemptimeoutcount>=4){
					windowtemptimeflag = true;
					clearInterval(windowintervalid);
				}
			}
		}); 
	}
}

window.onload = function(){
	updateServeTime();
	windowintervalid = window.setInterval(updateServeTime,5000);
}

//通用导出数据方法
function exportData(url){
	window.location.href = "<%=request.getContextPath() %>"+url;
}

//检测客户端IP是否可用
function checkIPAvailable(){
	var returnval = false;
	jQuery.ajax({
		type: "GET",
		async:false,
		url: "<%=request.getContextPath()%>/blacklist/checkIpAvailable",
		data: null,
		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
		success: function(result){
			if(result.success){ returnval = true; }
		}
	});	
	return returnval;
}

//更新镜像缓存数据
function updateMemoryData(obj){
	var returnval = false;
	jQuery.ajax({
  	 	type: "GET",
  	 	async:false,
   		url: "<%=request.getContextPath()%>/image/update",
  		data: {type:obj},
  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
   		success: function(result){
     		if(result.success){ returnval = true; }
   		}
	});	
	return returnval;
}

//检测登录超时
function checkLoginOut(){
	var returnval = true;
	jQuery.ajax({
		type: "post",
		async:false,
		url: "<%=request.getContextPath()%>/main/checklogin",
		data: null,
		success: function(result){
			if(result.status == "fail"){ 
				$("#tipscontent").html("登录超时，请重新登录");
				$("#dia").click();
				returnval = false;
			}
		}
	});
	return returnval;
}

//判断是否具有权限
function uploadHasPrivilege(obj){
	var returnval = false;
	jQuery.ajax({
  	 	type: "GET",
  	 	async:false,
   		url: "<%=request.getContextPath()%>/image/hasprivilege",
  		data: {type:obj},
  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
   		success: function(result){
     		if(result.success==true){ returnval = true; }
   		}
	});	
	return returnval;
}

//获取SS可用地址
function getAvailableAdress(){
	var returnval = "";
	jQuery.ajax({
  	 	type: "GET",
  	 	async:false,
   		url: "<%=request.getContextPath()%>/image/getavailableadress",
  		data: null,
   		success: function(result){
   			returnval = result;
   		}
	});	
	return returnval;
}
</script>

  <body class="bg-1">
    <!-- Preloader -->
    <div class="mask"><div id="loader"></div></div>
    <!--/Preloader -->

    <!-- Wrap all page content here -->
    <div id="wrap">

      <!-- Make page fluid -->
      <div class="row">

        <!-- Fixed navbar -->
        <div class="navbar navbar-default navbar-fixed-top navbar-transparent-black mm-fixed-top" role="navigation" id="navbar">
			<!-- Branding -->
			<div class="navbar-header col-md-2">
				<a class="navbar-brand" href="index.html"><strong>致云</strong>${productName}</a>
				<div class="sidebar-collapse"><a href="#"><i class="fa fa-bars"></i></a></div>
			</div>
			<!-- Branding end -->

			<!-- .nav-collapse -->
			<div class="navbar-collapse">
				<!-- Page refresh -->
				<ul class="nav navbar-nav refresh"><li class="divided"><a href="#" class="page-refresh"><i class="fa fa-refresh"></i></a></li></ul>
				<!-- /Page refresh -->
				
				<!-- Search -->
				<!-- <div class="search" id="main-search"><i class="fa fa-search"></i> <input type="text" placeholder="关键字"></div> -->
				<!-- Search end -->

				<!-- Quick Actions -->
				<ul class="nav navbar-nav quick-actions">
<!--               <li class="dropdown divided"> -->
<!--                 <a class="dropdown-toggle button" data-toggle="dropdown" href="#"> -->
<!--                   <i class="fa fa-envelope"></i> -->
<!--                   <span class="label label-transparent-black"></span> -->
<!--                 </a> -->
<!--                 <ul class="dropdown-menu wider arrow nopadding messages"> -->
<!--                   <li><h1>您有 <strong>0</strong> 个新的短消息</h1></li> -->
<!--                   <li> -->
<!--                     <a class="cyan" href="#"> -->
<!--                       <div class="profile-photo"> -->
<%--                         <img src="<%=request.getContextPath()%>/assets/images/profile-photo.png" alt /> --%>
<!--                       </div> -->
<!--                       <div class="message-info"> -->
<!--                         <span class="sender">董孔明</span> -->
<!--                         <span class="time">12 分钟前</span> -->
<!--                         <div class="message-content">172.168.1.2云服务器网络访问很慢，请看看时什么原因？谢谢</div> -->
<!--                       </div> -->
<!--                     </a> -->
<!--                   </li> -->

<!--                   <li> -->
<!--                     <a class="green" href="#"> -->
<!--                       <div class="profile-photo"> -->
<%--                         <img src="<%=request.getContextPath()%>/assets/images/minimal-logo.png" alt /> --%>
<!--                       </div> -->
<!--                       <div class="message-info"> -->
<!--                         <span class="sender">系统消息</span> -->
<!--                         <span class="time">1 小时前</span> -->
<!--                         <div class="message-content">自动检测并加入一台宿主机节点，自动分配IP 172.168.1.239</div> -->
<!--                       </div> -->
<!--                     </a> -->
<!--                   </li> -->

<!--                   <li class="topborder"><a href="#">查看全部短消息 <i class="fa fa-angle-right"></i></a></li> -->
<!--                 </ul> -->

<!--               </li> -->

<!--               <li class="dropdown divided"> -->
                
<!--                 <a class="dropdown-toggle button" data-toggle="dropdown" href="#"> -->
<!--                   <i class="fa fa-bell"></i> -->
<!--                   <span class="label label-transparent-black"></span> -->
<!--                 </a> -->

<!--                 <ul class="dropdown-menu wide arrow nopadding bordered"> -->
<!--                   <li><h1>你有 <strong>0</strong> 个新告警消息</h1></li> -->
                  

<!--                   <li> -->
<!--                     <a href="#"> -->
<!--                       <span class="label label-red"><i class="fa fa-power-off"></i></span> -->
<!--                       NC节点：172.168.1.239停止服务. -->
<!--                       <span class="small">27 分钟前</span> -->
<!--                     </a> -->
<!--                   </li> -->

<!--                   <li> -->
<!--                     <a href="#"> -->
<!--                       <span class="label label-orange"><i class="fa fa-power-off"></i></span> -->
<!--                       DS节点：172.168.1.219停止服务. -->
<!--                       <span class="small">1 小时前</span> -->
<!--                     </a> -->
<!--                   </li> -->


<!--                    <li><a href="#">查看全部告警消息 <i class="fa fa-angle-right"></i></a></li> -->
<!--                 </ul> -->

<!--               </li> -->

              <li class="dropdown divided user" id="current-user">
                <div class="profile-photo">
                  <img src="<%=request.getContextPath()%>/assets/images/profile-photo.png" alt />
                </div>
                <a class="dropdown-toggle options" data-toggle="dropdown" href="#">
                  ${sessionScope.displayname } <i class="fa fa-caret-down"></i>
                </a>
                
                <ul class="dropdown-menu arrow settings">

                  <li>
                    <h3>更换主题风格:</h3>
                    <ul id="color-schemes">
                      <li><a href="#" class="bg-1"></a></li>
                      <li><a href="#" class="bg-2"></a></li>
                      <li><a href="#" class="bg-3"></a></li>
                      <li><a href="#" class="bg-4"></a></li>
                      <li><a href="#" class="bg-5"></a></li>
                      <li><a href="#" class="bg-6"></a></li>
                    </ul>

                  </li>

                  <li class="divider"></li>

                  <li>
                    <a href="<%=request.getContextPath()%>/transform/baseinfo/beforeupdateuser"><i class="fa fa-user"></i> 账号管理</a>
                  </li>


                  <li>
                    <a href="<%=request.getContextPath()%>/transform/updatepass/beforeupdatepassword"><i class="fa fa-lock"></i> 修改密码  </a>
                  </li>

                  <li class="divider"></li>

                  <li>
                    <a href="#" onclick="logout()"><i class="fa fa-power-off"></i> 退出登录</a>
                  </li>
                </ul>
              </li>

           
            </ul>
            <!-- /Quick Actions -->
            
		<!-- Sidebar -->
		<ul class="nav navbar-nav side-nav" id="sidebar">
		  	<li class="collapsed-content"> 
				<ul><li class="search"><!-- Collapsed search pasting here at 768px --></li></ul>
			</li>
			<li class="navigation" id="navigation">
			  	<a href="#" class="sidebar-toggle" data-toggle="#navigation">平 台 控 制 面 板<i class="fa fa-angle-up"></i></a>
				<ul class="menu">${sessionScope.menuhtml }</ul>
			</li>
			<li class="settings" id="general-settings">
				<a href="#" class="sidebar-toggle underline" data-toggle="#general-settings">数据同步配置 <i class="fa fa-angle-up"></i></a>
				<div class="form-group">
					<label class="col-xs-8 control-label">实时数据显示</label>
					<div class="col-xs-4 control-label">
						<div class="onoffswitch greensea">
							<input type="checkbox" name="onoffswitch" class="onoffswitch-checkbox" id="switch-on" checked="">
							<label class="onoffswitch-label" for="switch-on">
								<span class="onoffswitch-inner"></span>
								<span class="onoffswitch-switch"></span>
							</label>
						</div>
					</div>
				</div>         
			</li>
		</ul>
		<!-- Sidebar end -->
		<div class="header-bg"></div>
	</div>
	<!--/.nav-collapse -->
</div>
<!-- Fixed navbar end -->
