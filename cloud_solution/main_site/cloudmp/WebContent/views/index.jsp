<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>${productName}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8" />

    <link rel="icon" type="image/ico" href="<%=request.getContextPath()%>/assets/images/favicon.ico" />
    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap-checkbox.css">

    <link href="<%=request.getContextPath()%>/assets/css/zhicloud.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="assets/js/html5shiv.js"></script>
      <script src="assets/js/respond.min.js"></script>
    <![endif]-->
  </head>
  <body class="bg-1">
 

    <!-- Wrap all page content here -->
    <div id="wrap">
      <!-- Make page fluid -->
      <div class="row">
        <!-- Page content -->
        <div id="content" class="col-md-12 full-page login">


          <div class="inside-block">
            <img src="<%=request.getContextPath()%>/assets/images/logo.png" alt class="logo">
            <h1><strong>致云</strong> 云平台管理中心</h1>
            <h5>V1.0版本</h5>

            <form   class="form-signin"  action="<%=request.getContextPath() %>/user/login" method="post" id="loginform" onSubmit = "return (checkUsername() && checkPassword());">
              <section>
                <div class="input-group">
                  <input type="text" class="form-control" name="username" placeholder="用 户 名">
                  <div class="input-group-addon"><i class="fa fa-user"></i></div>
                </div>
                <div class="input-group">
                  <input type="password" class="form-control" name="password" placeholder="密 码">
                  <div class="input-group-addon"><i class="fa fa-key"></i></div>
                </div>
              </section>
              <section class="controls">
                <div class="checkbox check-transparent">
                  <input type="checkbox" value="1" id="remember"  >
                  <label for="remember">记住密码</label>
                </div>
                <a href="#">忘记密码?</a>
              </section>
              <section class="log-in">
                <button class="btn btn-primary"  onclick="login();">登 录</button>
                <span></span>
                <button class="btn btn-slategray">重 置</button>
              </section>
            </form>
          </div>


        </div>
        <!-- /Page content -->  
      </div>
    </div>
    <!-- Wrap all page content end -->
  </body>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/plugins/jquery-1.7.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/plugins/jquery-ui-1.8.16.custom.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/plugins/jquery.cookie.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/plugins/jquery.uniform.min.js"></script> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>
	
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/custom/general.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath() %>/js/custom/index.js"></script>
	<script type="text/javascript">
	  var path = '<%=request.getContextPath() %>';
	   //如果已经登录。页面跳转，
	  //如果未登录，body显示
	  jQuery(document).ready(function(){
		  jQuery.ajax({
		      url: path+'/main/checklogin',
		      type: 'post', 
		      dataType: 'json',
		      timeout: 10000,
		      async: true,
		      error: function()
		      {
		          alert('Error!');
		      },
		      success: function(result)
		      {
		      	if(result.status == "success"){
			    	top.document.location.href = path+"/main/menu"; 
		        }else{
		        	jQuery("#loginbody").attr("display","block");
		        }
		      }
		   });
		  
	  }); 
	   
	  jQuery(document).ready(function() { 
		    if (jQuery.cookie("remember") == "true") {
		        jQuery("#remember").click(); 
		        jQuery("#username").val(jQuery.cookie("username"));
		        jQuery("#password").val(jQuery.cookie("password"));
		    }
		});
		//保存用户信息 
		function saveUserInfo() {
		    if (jQuery("#remember").attr("checked") == "checked") {
		        var username = jQuery("#username").val();
		        var password = jQuery("#password").val();
		        jQuery.cookie("remember", "true", {
		            expires: 7
		        }); // 存储一个带7天期限的 cookie 
		        jQuery.cookie("username", username, {
		            expires: 7
		        }); // 存储一个带7天期限的 cookie 
		        jQuery.cookie("password", password, {
		            expires: 7
		        }); // 存储一个带7天期限的 cookie 
		    } else {
		        jQuery.cookie("remember", "false", {
		            expires: -1
		        });
		        jQuery.cookie("username", '', {
		            expires: -1
		        });
		        jQuery.cookie("password", '', {
		            expires: -1
		        });
		    }
		}
	</script>
</html>
      

