<%@ page pageEncoding="utf-8"%>
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
var timecount = 0;
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
// 保存表单信息
function saveForm(){
	var form = $(".form-horizontal")	
	form.parsley('validate');
	if(form.parsley('isValid')){
		var email = $("#email").val();
		var telphone = $("#telphone").val();
		var usercount = $("#usercount").val();
		var displayname = $("#displayname").val();
		var param = "email="+email+"&telphone="+telphone+"&modflag=1"+"&billid=${userbaseinfo.billid}&usercount="+usercount+"&displayname="+displayname;
		var url = "<%=request.getContextPath()%>/transform/useradmin/saveuser";
		jQuery.ajax({
	  	 	type: "POST",
	  	 	async:false,
	   		url: url,
	  		data: param,
	  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
	   		success: function(result){
	   		  var obj = eval("("+result+")");
	     	if(obj.status=="success"){
	     		$("#tipscontent").html(obj.result+",<span id=\"timecount\" style=\"color:red;\">5</span>秒以后将自动跳转到登录页面,请重新登录");
	     		$("#dia").click();
	     		setInterval(function(){
	     			timecount++;
	     			$("#timecount").html(5-timecount);
		     		if(timecount==5){
		     			logout();
		     		}
	     		},1000);
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
//返回
function backhome(){
	window.location.href = "<%=request.getContextPath() %>/transform/useradmin/index";
}
function resetval(){
	$("#email").val("");
	$("#telphone").val("");
}
</script>
<%@include file="/views/common/common_menus.jsp" %>
    <!-- Make page fluid -->
    <div class="row">
<!-- Page content -->
        <div id="content" class="col-md-12">

          <!-- page header -->
          <div class="pageheader">
            
            <h2><i class="fa fa-inbox"></i>基本信息
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
                    <h3>基本信息</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body" style="padding-bottom:0px;margin-bottom:-45px;">
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations">
                      <div class="form-group">
                        <label for="usercount" class="col-sm-2 control-label">用户账号*</label>
                        <div class="col-sm-4">
                          <input value="${userbaseinfo.usercount }" type="text" class="form-control" id="usercount" parsley-required="true"  parsley-maxlength="100">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="displayname" class="col-sm-2 control-label">显示名称*</label>
                        <div class="col-sm-4">
                          <input value="${userbaseinfo.displayname }" type="text" class="form-control" id="displayname" parsley-required="true"  parsley-maxlength="100">
                        </div>
                      </div>                        
                      <div class="form-group">
                        <label for="email" class="col-sm-2 control-label">用户邮箱*</label>
                        <div class="col-sm-4">
                          <input value="${userbaseinfo.email }" type="text" class="form-control" id="email" parsley-required="true" parsley-type="email" parsley-maxlength="100">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="telphone" class="col-sm-2 control-label">联系电话*</label>
                        <div class="col-sm-4">
                          <input value="${userbaseinfo.telphone }" type="text" class="form-control" id="telphone" parsley-required="true" parsley-type="phone">
                        </div>
                      </div>
<!--                       <div class="form-group"> -->
<!--                         <label for="email" class="col-sm-2 control-label">用户邮箱*</label> -->
<!--                         <div class="col-sm-4"> -->
<%--                           <input type="text" class="form-control" id="email" parsley-required="true" parsley-type="email" parsley-maxlength="100" value="${userbaseinfo.email }"> --%>
<!--                         </div> -->
<!--                       </div> -->
<!--                       <div class="form-group"> -->
<!--                         <label for="telphone" class="col-sm-2 control-label">联系电话*</label> -->
<!--                         <div class="col-sm-4"> -->
<%--                           <input type="text" class="form-control" id="telphone" parsley-required="true" parsley-type="phone" value="${userbaseinfo.telphone }"> --%>
<!--                         </div> -->
<!--                       </div> -->
                      <!-- modify -->
                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8" style="text-align: left;">
                          <button type="button" class="btn btn-greensea" onclick="saveForm()"><i class="fa fa-plus"></i>
                              <span> 保 存 </span></button>
                          <button type="button" class="btn btn-red" onclick="resetval()"><i class="fa fa-refresh"></i>
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
