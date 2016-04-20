<%@ page pageEncoding="utf-8"%>
<!-- system_user_updatepassword.jsp -->
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
// 保存表单信息
function saveForm(){
	var form = $(".form-horizontal")	
	form.parsley('validate');
	if(form.parsley('isValid')){
		var oldpassword = $("#oldpassword").val();
		var newpassword = $("#newpassword").val();
		var confirmpassword = $("#confirmpassword").val();
		if(newpassword!=confirmpassword){
     		$("#tipscontent").html("两次输入的密码不一致,请重新输入");
     		$("#dia").click();
     		return;	
		}
		var param = "oldpassword="+oldpassword+"&newpassword="+newpassword;
		var url = "<%=request.getContextPath()%>/transform/admin/updatepassword";
		jQuery.ajax({
	  	 	type: "POST",
	  	 	async:false,
	   		url: url,
	  		data: param,
	  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
	   		success: function(result){
	   		  var obj = eval("("+result+")");
	     	if(obj.status=="success"){
	     		window.location.href= "<%=request.getContextPath()%>";
	     	}else{
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
function reset(){
	$("#oldpassword").val("");
	$("#newpassword").val("");
	$("#confirmpassword").val("");	
}
</script>
<%@include file="/views/common/common_menus.jsp" %>
<!-- Page content -->
        <div id="content" class="col-md-12">

          <!-- page header -->
          <div class="pageheader">
            
            <h2><i class="fa fa-inbox"></i> 修改密码
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
                    <h3>修改密码</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body" style="padding-bottom:0px;margin-bottom:-45px;">
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations">
                     
                      <div class="form-group">
                        <label for="oldpassword" class="col-sm-2 control-label">旧密码*</label>
                        <div class="col-sm-4">
                          <input type="password" class="form-control" id="oldpassword" parsley-required="true"  parsley-maxlength="40">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="newpassword" class="col-sm-2 control-label">新密码*</label>
                        <div class="col-sm-4">
                          <input type="password" class="form-control" id="newpassword" parsley-required="true"  parsley-maxlength="40">
                        </div>
                      </div>                      
                      <div class="form-group">
                        <label for="confirmpassword" class="col-sm-2 control-label">确认密码*</label>
                        <div class="col-sm-4">
                          <input type="password" class="form-control" id="confirmpassword" parsley-required="true" parsley-maxlength="40">
                        </div>
                      </div>
                      <!-- modify -->
                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8">
                          <button type="button" class="btn btn-greensea" onclick="saveForm()"><i class="fa fa-plus"></i>
                              <span> 保 存 </span></button>
                          <button type="button" class="btn btn-red" onclick="reset()"><i class="fa fa-refresh"></i>
                              <span> 重置</span></button>
                        </div>
                      </div>
                            
                    </form>

                  </div>
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
                </section>
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

    </div>
    <!-- Wrap all page content end -->

    <section class="videocontent" id="video"></section>
    
    <script>
    $(function(){
      //chosen select input
      $(".chosen-select").chosen({disable_search_threshold: 10});
      
    })
      
    </script>
    
  </body>
