<%@ page pageEncoding="utf-8"%>
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
<script type="text/javascript">
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
<%@include file="/views/common/common_menus.jsp" %>
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
                <section class="tile color transparent-black">


                  <!-- tile header -->
                  <div class="tile-header">
                    <h3>
					<a href="<%=request.getContextPath() %>/transform/useradmin/index"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>编辑用户信息</h3>
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

                      <div class="form-group">
                        <label for="userType" class="col-sm-2 control-label">用户类型</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" id="userTypebox">
                            <select class="chosen-select chosen-transparent form-control" id="userType" parsley-required="true" parsley-error-container="#userTypebox">
<!--                             <option value="">请选择</option>   -->
                            <option value="0">管理员用户</option>  
<!--                             <option value="1">租户管理员用户</option>  -->
                          </select></div> 
                        </div>
                      </div>  
                      
                      <div class="form-group">
                        <label for="status" class="col-sm-2 control-label">状态 *</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" id="selectbox">
                            <select class="chosen-select chosen-transparent form-control" id="status" parsley-required="true" parsley-error-container="#selectbox">
                            <option value="">请选择</option>  
                            <option value="0">正常</option>  
                            <option value="1">禁用</option> 
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
                             
                      
                      <div class="form-group">
                        <label for="userType" class="col-sm-2 control-label">用户类型</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" id="selectbox">
                            <select class="chosen-select chosen-transparent form-control" id="userType" parsley-required="true" parsley-error-container="#selectbox">
                            <option value="">请选择</option>  
                            <option value="0" <c:if test="${systemUser.userType==0 }">selected</c:if>>管理员用户</option>  
                            <option value="1" <c:if test="${systemUser.userType==1 }">selected</c:if>>租户管理员用户</option> 
                          </select></div> 
                        </div>
                      </div>                  
                      
                      <div class="form-group">
                        <label for="status" class="col-sm-2 control-label">状态 *</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" id="selectbox">
                            <select class="chosen-select chosen-transparent form-control" id="status" parsley-required="true" parsley-error-container="#selectbox">
                            <option value="0" <c:if test="${systemUser.status==0 }">selected</c:if> >正常</option>  
                            <option value="1" <c:if test="${systemUser.status==1 }">selected</c:if> >禁用</option> 
                          </select></div> 
                        </div>
                      </div> 
                      
                      </c:if>      
                                   
                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8">
                          <button type="button" class="btn btn-greensea" onclick="saveForm()"><i class="fa fa-plus"></i>
                              <span> 保 存 </span></button>
                          <button type="button" class="btn btn-default" onclick="backhome()"><i class="fa fa-backward"></i>
                              <span> 返回 </span></button>
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
    
  </body>
