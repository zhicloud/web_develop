<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云 ZhiCloud</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/style.css" />
<!--[if IE 6]>
<script src="javascript/DD_belatedPNG.js"></script>
<script type="text/javascript">
	DD_belatedPNG.fix("*");
</script>
<![endif]-->
</head>

<body>
<div class="help">
  <div class="bread"><span>帮助中心</span><img src="<%=request.getContextPath() %>/image/help_arrow.png" width="7" height="12" /><span>云主机</span><img src="<%=request.getContextPath() %>/image/help_arrow.png" width="7" height="12" /><span class="active">上传文件</span></div>
  <p >一.&nbsp;通过windows&nbsp;RDP或者linux&nbsp;SSH方式连接主机后，直接上传文件。 </p>
  <p >&nbsp;</p>
  <p >二.&nbsp;通过FTP上传 </p>
  <p >&nbsp;</p>
  <p >云主机默认开启FTP服务，可以通过FTP上传或下载，默认用户名/密码：admin/zhicloud123 </p>
  <p >ftp://IP来访问或下载ftp客户端访问连接 </p>
  <p >&nbsp;</p>
  <p >三.&nbsp;通过Zhicloud.com网站 </p>
  <p >&nbsp;</p>
  <p >每个用户在我的云端&#8212;&#8212;文件夹中可上传300M资源 </p>
  <ol>
    <li>登录&#8220;我的云端&#8221;，点击&#8220;文件夹&#8221;功能，上传所需文件； </li>
    <li>进入云主机后，登录&#8220;我的云端&#8221;，点击&#8220;文件夹&#8221;功能，下载文件。 </li>
    <li></li>
  </ol>
  <p ><img src="<%=request.getContextPath() %>/image/help_3_6_wpsCD45.tmp.png" alt="" width="554" height="201" />&nbsp;</p>
  <p >&nbsp;</p>
  <p >四.Linux通过主机面板 </p>
  <p >&nbsp;</p>
  <p >每个Linux云主机默认已打开 </p>
  <p >通过访问http://IP:10000来访问 </p>
  <p >输入用户名/密码：root/zhicloud@123 </p>
  <p >&nbsp;</p>
  <p ><img src="<%=request.getContextPath() %>/image/help_3_6_wpsCD46.tmp.png" alt="" width="554" height="334" /></p>
</div>
</body>
</html>