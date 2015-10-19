<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>云桌面管理中心</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.default.css" type="text/css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery-1.7.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.uniform.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.flot.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.flot.resize.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.slimscroll.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/main.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.tagsinput.min.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/chosen.jquery.min.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.blockUI.js"></script>
 <script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.alerts.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/forms.js"></script> 
<script type="text/javascript">   
    var path = '<%=request.getContextPath()%>';
    jQuery(function(){
    	//===============================
        jQuery("#update_password_btn").click(function(){
            jQuery.blockUI({ message: jQuery("#update_password_div"),  
            css: {border:'3px solid #aaa',
                  backgroundColor:'#FFFFFF',
                  overflow: 'hide',
                   width: '70%', 
                  height: 'auto', 
                  left:'200px',
                  top:'200px'} 
            });
          jQuery('.blockOverlay').attr('title','单击关闭').click(jQuery.unblockUI); 
    	  });
    });
  
</script> 
 
<!--[if lte IE 8]><script language="javascript" type="text/javascript" src="js/plugins/excanvas.min.js"></script><![endif]-->
<!--[if IE 9]>
    <link rel="stylesheet" media="screen" href="css/style.ie9.css"/>
<![endif]-->
<!--[if IE 8]>
    <link rel="stylesheet" media="screen" href="css/style.ie8.css"/>
<![endif]-->
<!--[if lt IE 9]>
	<script src="js/plugins/css3-mediaqueries.js"></script>
<![endif]-->
</head>

<body class="withvernav" style="overflow-x:hidden;overflow-y:hidden">
<div class="bodywrapper">
     
     <div class="topheader">
        <div class="left">
            <h1 class="logo"><img src="<%=request.getContextPath()%>/images/LOGO.png"/></h1> 
            
            <br clear="all" />
            
        </div><!--left-->
        
        <div class="right"> 
            <div class="userinfo">
            	<img src="<%=request.getContextPath()%>/images/thumbs/avatar.png" alt="" />
                <span>${loginInfo.username}</span>
            </div><!--userinfo-->
            
            <div class="userinfodrop">
            	<div class="avatar">
                	<a href=""><img src="<%=request.getContextPath()%>/images/thumbs/avatarbig.png" alt="" /></a> 
                </div><!--avatar-->
				<div class="userdata">
                	<h4>${loginInfo.username}</h4>
                    <span class="email">${loginInfo.username}</span>
                    <ul>
                    	<li><a href="javascript:void(0);" id="update_password_btn">修改密码</a></li> 
                        <li><a href="javascript:void(0);" id="logout_btn">注销</a></li>
                    </ul>
                </div><!--userdata-->
            </div><!--userinfodrop-->
        </div><!--right-->
    </div><!--topheader-->
    
    
    <div class="header">
    	<ul class="headermenu">
        	<li ><a href="#"><span class="icon icon-ztjk"></span>状态监控</a></li> 
        	<li ><a href="#"><span class="icon icon-ptjg"></span>平台架构</a></li> 
        	<li ><a href="#"><span class="icon icon-zyc"></span>资源池</a></li> 
        	<li class="current"><a href="#"><span class="icon icon-flatscreen"></span>云桌面</a></li> 
        	<li ><a href="#"><span class="icon icon-ycc"></span>云存储</a></li> 
<!--         	<li ><a href="#"><span class="icon icon-xnwl"></span>虚拟网络</a></li>  -->
        	<li ><a href="#"><span class="icon icon-tjbb"></span>统计报表</a></li> 
        	<li ><a href="#"><span class="icon icon-ptpz"></span>平台配置</a></li> 
        </ul> 
        
    </div><!--header-->
     
    
    <div class="vernav2 iconmenu">
    	<ul>
        	<li><a href="#hostmanage" class="host" id="host_manage">主机管理</a>
            	<span class="arrow"></span>
            	<ul id="hostmanage">
               		<li class="current"><a id="host_type_manage" href="javascript:void(0);">主机类型管理</a></li>
                    <li><a id="image_manage_menu" href="javascript:void(0);">镜像管理</a></li>
                    <li><a id="cloud_host_ware" href="javascript:void(0);">主机仓库管理</a></li> 
                </ul>
            </li> 
			<li><a href="#usermanage" class="user">用户管理</a>
            	<span class="arrow"></span>
            	<ul id="usermanage">
               		<li><a id="sys_group_manage" href="javascript:void(0);">群组管理</a></li>
                    <li><a id="terminal_user_manage" href="javascript:void(0);">用户列表</a></li>
                     
                </ul>
            </li> 
            <li>           
            <a href="#manage"  class="elements">平台配置</a>
            <span class="arrow"></span>
            <ul id="manage">
               		<li><a id="version_record_manage" href="javascript:void(0);">版本管理</a></li>
                      
                </ul>
            </li> 
        </ul>
        <a class="togglemenu"></a>
        <br /><br />
    </div><!--leftmenu-->
        
    <div class="centercontent">  
        
        	 <iframe id="content_frame" scrolling="yes" frameborder="0" hspace="0" vspace="0"  style="width:100%;height:100%;" src="<%=request.getContextPath() %>/chcm/all" >              </iframe> 
        
        <br clear="all" />
        
	</div><!-- centercontent -->
    
    
</div><!--bodywrapper-->
<div class="bodywrapper" style="display:none;" id="update_password_div"> 

            <div class="centercontent_block" style="margin-left:0">

                 <div class="formtitle">
		            <div class="left">
		                <h1 class="logo"><span>修改密码</span></h1> 
		
		
		                <br clear="all" />
		
		            </div><!--left--> 
		        
		         
		       </div>

                <div   class="contentwrapper">

                    <div id="imageform" class="subcontent"> 

                            <form class="stdform" action="<%=request.getContextPath() %>/user/updateAdminPwd" method="post" id="update_password_form">
                                <p>
                                    <label>原密码</label>
                                    <span class="field"><input type="password" name="oldPassword" id="oldPassword"   class="password" maxlength="30"/></span>
                                    <small class="desc"></small>
                                </p>
                                <p>
                                    <label>新密码</label>
                                    <span class="field"><input id="new_password" type="password" name="newPassword"  class="password" maxlength="30"/></span>
                                    <small class="desc"></small>
                                </p>
                                <p>
                                    <label>确认</label>
                                    <span class="field"><input type="password" name="newPasswordConfig"   class="password" maxlength="30"/></span>
 
                                    <small class="desc"></small>
                                </p>
                                <p class="stdformbutton">
                                    <button type="button" class="submit radius2" onclick="saveUpdatePwdForm();">保存</button>
                                    <input type="reset" onclick="jQuery.unblockUI();" class="reset radius2" value="取消" />
                                </p>


                            </form>

                            <br /> 

                    </div><!--subcontent-->

                </div><!--contentwrapper-->
                
            </div><!-- centercontent -->

        </div><!--bodywrapper-->
</body>
</html>