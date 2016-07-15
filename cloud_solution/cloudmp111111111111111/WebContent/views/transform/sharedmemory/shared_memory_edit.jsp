<%@ page pageEncoding="utf-8"%>
<!-- shared_memory_edit.jsp -->
  <head>
    <title>共享存储信息编辑 </title>
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
var temptype = "${type}";
// 保存表单信息
function saveForm(){
	var form = $(".form-horizontal")	
	form.parsley('validate');
	if(form.parsley('isValid')){
		var name = $("#name").val();
		var url = $("#url").val();
		var username = $("#username").val();
		var password = $("#password").val();
		var secretkey = $("#secretkey").val();
		var data = new Object();
		data["name"] = name;
		data["url"] = url;
		data["username"] = username;
		data["password"] = password;
		data["secretkey"] = secretkey;
		data["type"] = temptype;
		data["id"] = "${sharedmeomory.id}";
		var url = "<%=request.getContextPath()%>/storageresourcepool/sharedmemory/save"
		jQuery.ajax({
	  	 	type: "POST",
	  	 	async:false,
	   		url: url,
	   		dataType : "json",
	  		data:{data:JSON.stringify(data)},
	  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
	   		success: function(result){
	     	if(result.status=="success"){
	     		backhome();
	     	}else{
	     		$("#tipscontent").html(result.message);
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
	window.location.href = "<%=request.getContextPath() %>/storageresourcepool/sharedmemory/manage";
}
</script>
<%@include file="/views/common/common_menus.jsp" %>
    <!-- Make page fluid -->
    <div class="row">
<!-- Page content -->
        <div id="content" class="col-md-12">

          <!-- page header -->
          <div class="pageheader">
            
            <h2><i class="fa fa-group"></i> 
            <c:if test="${empty sharedmeomory}">新增外部存储信息</c:if>
            <c:if test="${!empty sharedmeomory}">修改外部存储信息</c:if>
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

                  <!-- tile widget -->
                  <div class="tile-widget color transparent-black rounded-top-corners nopadding nobg">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs tabdrop">
                      <li class="active"><a href="#users-tab" data-toggle="tab" onclick="window.location.href='<%=request.getContextPath() %>/storageresourcepool/sharedmemory/manage';">外部存储管理</a></li>
                      <li><a href="#orders-tab" onclick="window.location.href='<%=request.getContextPath() %>/storageresourcepool/all';" data-toggle="tab">本地存储管理</a></li>
 	                      <div id="space"></div>
                      
                     </ul>
                    <!-- / Nav tabs -->
                  </div>
                  <!-- /tile widget -->
                  <!-- tile header -->
                  <div class="tile-header">
                    <h3><a href="<%=request.getContextPath() %>/storageresourcepool/sharedmemory/manage"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>
                        <c:if test="${empty sharedmeomory}">输入外部存储信息</c:if>
                        <c:if test="${!empty sharedmeomory}">修改外部存储信息</c:if></h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body" style="padding-bottom:0px;margin-bottom:-45px;">
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations">
                     <c:if test="${empty sharedmeomory}">
                     
                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">路径名称*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="name" parsley-required="true"  parsley-maxlength="50">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="url" class="col-sm-2 control-label">路径URL*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="url" parsley-required="true"  parsley-maxlength="100" parsley-type="sharedurl">
                        </div>
                      </div>   
                      <div class="form-group">
                        <label for="username" class="col-sm-2 control-label">用户名*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="username" parsley-required="true"  parsley-maxlength="50">
                        </div>
                      </div>  
                      <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">密码*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="password" parsley-required="true"  parsley-maxlength="50" parsley-type="nochinese">
                        </div>
                      </div>  
                      <div class="form-group">
                        <label for="secretkey" class="col-sm-2 control-label">秘钥*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="secretkey" parsley-required="true"  parsley-maxlength="50" parsley-type="nochinese">
                        </div>
                      </div>                                                                                         
                      </c:if>
                     <c:if test="${!empty sharedmeomory}">
                     
                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">路径名称*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="name" parsley-required="true"  parsley-maxlength="50" value="${sharedmeomory.name }">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="url" class="col-sm-2 control-label">路径URL*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="url" parsley-required="true"  parsley-maxlength="100" value="${sharedmeomory.url }" parsley-type="sharedurl">
                        </div>
                      </div>   
                      <div class="form-group">
                        <label for="username" class="col-sm-2 control-label">用户名*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="username" parsley-required="true"  parsley-maxlength="50" value="${sharedmeomory.username }">
                        </div>
                      </div>  
                      <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">密码*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="password" parsley-required="true"  parsley-maxlength="50" value="${sharedmeomory.password }" parsley-type="nochinese">
                        </div>
                      </div>  
                      <div class="form-group">
                        <label for="secretkey" class="col-sm-2 control-label">秘钥*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="secretkey" parsley-required="true"  parsley-maxlength="50" value="${sharedmeomory.secretkey }" parsley-type="nochinese">
                        </div>
                      </div>
                      </c:if>                      
                      <!-- modify -->
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
                <!-- /tile -->
                 <!-- /tile body -->
				<div class="tile-body">

                    <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                    
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

    </div>
    <!-- Wrap all page content end -->

    <section class="videocontent" id="video"></section>
    
    <script>
    $(function(){
      //chosen select input
      $(".chosen-select").chosen({disable_search_threshold: 10});
      $("#space").width($("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop").width()
 			 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).width()
 			 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(1).width()
 			 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(2).width()
 			 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(3).width()
 			 -1).height(
 			  $("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).height());
 	$(window).resize(function(){
 		 $("#space").width($("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop").width()
 				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).width()
 				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(1).width()
 				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(2).width()
 				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(3).width()
 				 -1);
 	});
      
    })
      
    </script>
    
  </body>
