<%@ page pageEncoding="utf-8"%>
<%@ include file="/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- main.jsp -->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>云平台管理中心</title>
</head>
<body class="bg-1">

 
    <!-- Preloader -->
<!--     <div class="mask"><div id="loader"></div></div> -->
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
              <strong>致云</strong>云平台管理中心
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
                        <img src="assets/images/profile-photo.png" alt />
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
                        <img src="assets/images/minimal-logo.png" alt />
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

              <li class="dropdown divided user" id="current-user">
                <div class="profile-photo">
                  <img src="<%=request.getContextPath() %>/assets/images/profile-photo.png" alt />
                </div>
                <a class="dropdown-toggle options" data-toggle="dropdown" href="#">
                  平台管理员 <i class="fa fa-caret-down"></i>
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
                    <a href="#"><i class="fa fa-user"></i> 账号管理</a>
                  </li>


                  <li>
                    <a href="#"><i class="fa fa-envelope"></i> 短消息 <span class="badge badge-red" id="user-inbox">2</span></a>
                  </li>

                  <li class="divider"></li>

                  <li>
                    <a href="login.html"><i class="fa fa-power-off"></i> 退出登录</a>
                  </li>
                </ul>
              </li>

           
            </ul>
            <!-- /Quick Actions -->

            <!-- Sidebar -->
            <ul class="nav navbar-nav side-nav" id="sidebar">
              
              <li class="collapsed-content"> 
                <ul>
                  <li class="search"><!-- Collapsed search pasting here at 768px --></li>
                </ul>
              </li>
              <li class="navigation" id="navigation">
                <a href="#" class="sidebar-toggle" data-toggle="#navigation">平 台 控 制 面 板<i class="fa fa-angle-up"></i></a>
                <ul class="menu">
                  	${menuhtml}
                </ul>
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
          </div>
          <!--/.nav-collapse -->


        </div>
        <!-- Fixed navbar end -->

        
        <!-- Page content -->
        <div id="content" class="col-md-12">
        </div>
        <!-- Page content end -->
      </div>
      <!-- Make page fluid-->


    </div>
    <!-- Wrap all page content end -->
    <section class="videocontent" id="video"></section>
  </body>
</html>