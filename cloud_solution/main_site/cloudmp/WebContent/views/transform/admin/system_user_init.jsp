<%@ page pageEncoding="utf-8"%>
<%@page import="com.zhicloud.ms.transform.constant.TransformConstant"%>
<%@page import="com.zhicloud.ms.transform.util.TransFormLoginInfo"%>
<%@page import="com.zhicloud.ms.transform.util.TransFormLoginHelper"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- system_user_init.jsp -->
  <head>
    <title>用户信息编辑</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  </head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
  body #content .tile table > tbody > tr td .checkbox,
  body #content .tile table > tbody > tr th .checkbox,
  body #content .tile table > tfoot > tr td .checkbox,
  body #content .tile table > tfoot > tr th .checkbox{
    padding-top: 15px;
    margin: 0;
    min-height: 10px; 
  }
  .pagination-sm > li > a > i{
  	padding:5px;
  }
</style> 

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
            <a class="navbar-brand" href="index.html">
              <strong>致云</strong>${productName}
            </a>
            <div class="sidebar-collapse">
              <a href="#">
                <i class="fa fa-bars"></i>
              </a>
            </div>
          </div>
          <!-- Branding end -->


          <!-- .nav-collapse -->
          <div class="navbar-collapse">
                        
            <!-- Page refresh -->
            <ul class="nav navbar-nav refresh">
              <li class="divided">
                <a href="#" class="page-refresh"><i class="fa fa-refresh"></i></a>
              </li>
            </ul>
            <!-- /Page refresh -->

            <!-- Search -->
            <div class="search" id="main-search">
              <i class="fa fa-search"></i> <input type="text" placeholder="关键字">
            </div>
            <!-- Search end -->

            <!-- Quick Actions -->
            <ul class="nav navbar-nav quick-actions">
              

              <li class="dropdown divided">
                
                <a class="dropdown-toggle button" data-toggle="dropdown" href="#">
                  <i class="fa fa-envelope"></i>
                  <span class="label label-transparent-black">2</span>
                </a>

                <ul class="dropdown-menu wider arrow nopadding messages">
                  <li><h1>您有 <strong>2</strong> 个新的短消息</h1></li>
                  <li>
                    <a class="cyan" href="#">
                      <div class="profile-photo">
                        <img src="<%=request.getContextPath()%>/assets/images/profile-photo.png" alt />
                      </div>
                      <div class="message-info">
                        <span class="sender">董孔明</span>
                        <span class="time">12 分钟前</span>
                        <div class="message-content">172.168.1.2云服务器网络访问很慢，请看看时什么原因？谢谢</div>
                      </div>
                    </a>
                  </li>

                  <li>
                    <a class="green" href="#">
                      <div class="profile-photo">
                        <img src="<%=request.getContextPath()%>/assets/images/minimal-logo.png" alt />
                      </div>
                      <div class="message-info">
                        <span class="sender">系统消息</span>
                        <span class="time">1 小时前</span>
                        <div class="message-content">自动检测并加入一台宿主机节点，自动分配IP 172.168.1.239</div>
                      </div>
                    </a>
                  </li>

                  <li class="topborder"><a href="#">查看全部短消息 <i class="fa fa-angle-right"></i></a></li>
                </ul>

              </li>

              <li class="dropdown divided">
                
                <a class="dropdown-toggle button" data-toggle="dropdown" href="#">
                  <i class="fa fa-bell"></i>
                  <span class="label label-transparent-black">2</span>
                </a>

                <ul class="dropdown-menu wide arrow nopadding bordered">
                  <li><h1>你有 <strong>2</strong> 个新告警消息</h1></li>
                  

                  <li>
                    <a href="#">
                      <span class="label label-red"><i class="fa fa-power-off"></i></span>
                      NC节点：172.168.1.239停止服务.
                      <span class="small">27 分钟前</span>
                    </a>
                  </li>

                  <li>
                    <a href="#">
                      <span class="label label-orange"><i class="fa fa-power-off"></i></span>
                      DS节点：172.168.1.219停止服务.
                      <span class="small">1 小时前</span>
                    </a>
                  </li>


                   <li><a href="#">查看全部告警消息 <i class="fa fa-angle-right"></i></a></li>
                </ul>

              </li>

               

           
            </ul>
            <!-- /Quick Actions -->

            <!-- Sidebar -->
             
            <!-- Sidebar end -->
          </div>
          <!--/.nav-collapse -->
        </div>
    <!-- Make page fluid -->
    <div class="row">
<!-- Page content -->
        <div id="content" class="col-md-12">

          <!-- page header -->
          <div class="pageheader">
            
            <h2><i class="fa fa-user"></i> 
            <c:if test="${empty systemUser}">创建用户</c:if>
            <c:if test="${!empty systemUser}">修改用户</c:if>	
            </h2>

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">

            <!-- row -->
            <div class="row">
              
              <div class="col-md-12">


                <!-- tile -->
                <section class="tile color transparent-black" style="width:80%;">


                  <!-- tile header -->
                  <div class="tile-header">
                    <h3>
					 编辑用户信息</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body" style="padding-bottom:0px;margin-bottom:-45px;">
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations">
                     <c:if test="${empty systemUser}">
                     
                      <div class="form-group">
                        <label for="usercount" class="col-sm-2 control-label">用户账号*</label>
                        <div class="col-sm-4">
                            <input type="text" class="form-control" id="usercount" parsley-trigger="change" parsley-type="nochinese" parsley-required="true"  parsley-minlength="4" parsley-maxlength="50" parsley-validation-minlength="4">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="displayname" class="col-sm-2 control-label">显示名称*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="displayname" parsley-required="true"  parsley-maxlength="50">
                        </div>
                      </div>                      
                      <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">用户密码*</label>
                        <div class="col-sm-4">
                          <input type="password" class="form-control" id="password" parsley-required="true"  parsley-maxlength="20">
                        </div>
                      </div>                      
                      <div class="form-group">
                        <label for="email" class="col-sm-2 control-label">用户邮箱*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="email" parsley-required="true" parsley-type="email" parsley-maxlength="100">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="telphone" class="col-sm-2 control-label">联系电话*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="telphone" parsley-required="true" parsley-type="phone">
                        </div>
                      </div>

                      <div class="form-group" style="display:none;">
                        <label for="userType" class="col-sm-2 control-label">用户类型</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" id="userTypebox">
                            <select class="chosen-select chosen-transparent form-control" id="userType" parsley-required="true" parsley-error-container="#userTypebox">
                             <option value="0">管理员用户</option>   
                          </select></div> 
                        </div>
                      </div>  
                      
                      <div class="form-group" style="display:none;">
                        <label for="status" class="col-sm-2 control-label">状态 *</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" id="selectbox">
                            <select class="chosen-select chosen-transparent form-control" id="status" parsley-required="true" parsley-error-container="#selectbox">
                             
                            <option value="0" checked >正常</option>   
                          </select></div> 
                        </div>
                      </div>  
                      
                      </c:if>
                      <!-- modify -->
                      <c:if test="${!empty systemUser }">
                      <div class="form-group">
                        <label for="usercount" class="col-sm-2 control-label">用户账号*</label>
                        <div class="col-sm-4">
                            <input value="${systemUser.usercount }" type="text" class="form-control" id="usercount" parsley-trigger="change" parsley-type="nochinese" parsley-required="true"  parsley-minlength="4" parsley-maxlength="50" parsley-validation-minlength="4">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="displayname" class="col-sm-2 control-label">显示名称*</label>
                        <div class="col-sm-4">
                          <input value="${systemUser.displayname }" type="text" class="form-control" id="displayname" parsley-required="true"  parsley-maxlength="100">
                        </div>
                      </div>                        
                      <div class="form-group">
                        <label for="email" class="col-sm-2 control-label">用户邮箱*</label>
                        <div class="col-sm-4">
                          <input value="${systemUser.email }" type="text" class="form-control" id="email" parsley-required="true" parsley-type="email" parsley-maxlength="100">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="telphone" class="col-sm-2 control-label">联系电话*</label>
                        <div class="col-sm-4">
                          <input value="${systemUser.telphone }" type="text" class="form-control" id="telphone" parsley-required="true" parsley-type="phone">
                        </div>
                      </div>
                             
                      
                      <div class="form-group" style="display:none;">
                        <label for="userType" class="col-sm-2 control-label">用户类型</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" id="selectbox">
                            <select class="chosen-select chosen-transparent form-control" id="userType" parsley-required="true" parsley-error-container="#selectbox">
                             <option value="0"  selected >管理员用户</option>  
                           </select></div> 
                        </div>
                      </div>                  
                      
                      <div class="form-group" style="display:none;">
                        <label for="status" class="col-sm-2 control-label">状态 *</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" id="selectbox">
                            <select class="chosen-select chosen-transparent form-control" id="status" parsley-required="true" parsley-error-container="#selectbox">
                            <option value="0"  selected  >正常</option>  
                           </select></div> 
                        </div>
                      </div> 
                      
                      </c:if>      
                                   
                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8">
                          <button type="button" class="btn btn-greensea" onclick="saveForm()"><i class="fa fa-plus"></i>
                              <span> 保 存 </span></button>
                           
                        </div>
                      </div>
                            
                    </form>

                  </div>
                  
                </section>
                
                <!-- /tile body -->
				<div class="tile-body">

                    <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                    <a href="#modalConfirm" id="con" role="button"   data-toggle="modal"> </a>
                    
                    <div class="modal fade" id="modalDialog" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalDialogLabel"><strong>提示</strong></h3>
                          </div>
                          <div class="modal-body">
                            <p id="tipscontent"></p>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->

                    </div>  
                <!-- /tile -->

              </div>
              <!-- /col 6 -->
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
    
    <script>
    $(function(){
      //chosen select input
      $(".chosen-select").chosen({disable_search_threshold: 10});
      
    })
      
    </script>
    
    <script>

var isCommited = false;
var tempbillid = "${systemUser.billid}";
// 保存表单信息
function saveForm(){
	var form = $(".form-horizontal")	
	form.parsley('validate');
	if(form.parsley('isValid')){
		
		if(isCommited){
     		return false;
		 } 
		isCommited=true;
		
		var usercount = $("#usercount").val();
		var email = $("#email").val();
		var telphone = $("#telphone").val();
		var status = $("#status").val();
		var password = $("#password").val();
		var displayname = $("#displayname").val();
		var userType =  $("#userType").val();
		var param = "usercount="+usercount+"&email="+email+"&telphone="+telphone+"&status="+status+"&password="+password+"&displayname="+displayname+"&userType="+userType;
		var url = "<%=request.getContextPath()%>/transform/useradmin/saveuser";
		jQuery.ajax({
	  	 	type: "POST",
	  	 	async:false,
	   		url: url,
	  		data: "billid="+tempbillid+"&modflag="+${modflag}+"&"+param,
	  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
	   		success: function(result){
	   		  var obj = eval("("+result+")");
	     	if(obj.status=="success"){
	     		window.location.href = "<%=request.getContextPath()%>/transform/useradmin/index";
	     	}else{
	     		isCommited= false;
	     		$("#tipscontent").html(obj.result);
	     		$("#dia").click();
	     		return;
	     	}
	   	}
		});		
		
	}else{
		return;
	}
}
//返回
function backhome(){
	window.location.href = "<%=request.getContextPath() %>/transform/useradmin/index";
}
</script>
    
  </body>
  </html>
