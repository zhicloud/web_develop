<%@ page pageEncoding="utf-8"%>
<%@ include file="/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<script type="text/javascript">
function tohome(){
	jQuery.ajax({
  	 	type: "POST",
  	 	async:false,
   		url: "<%=request.getContextPath()%>/transform/logout",
  		data: null,
  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
   		success: function(result){
   			window.location.href = "<%=request.getContextPath()%>";
   	}
	});		
	//window.history.go(-1);
}
</script>
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- error.jsp -->
 <body class="bg-1">
    <!-- Wrap all page content here -->
    <div id="wrap">
      <!-- Make page fluid -->
      <div class="row">
        <!-- Page content -->
        <div id="content" class="col-md-12 full-page error">
          <div class="inside-block">
            <img src="<%=request.getContextPath()%>/assets/images/logo-big.png" alt class="logo">
          
            <h1 class="error">Error 404</h1>
            <p class="lead bold">该用户没有分配权限，请联系管理员</p>

            <div class="controls">
              <button class="btn btn-greensea" onclick="tohome()"><i class="fa fa-home"></i> 返回</button>
            </div>

          </div>

        </div>
        <!-- /Page content --> 

      </div> 
      
    </div>
    <!-- Wrap all page content end -->
  </body>
  </html>