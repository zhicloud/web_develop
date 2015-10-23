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
  <div class="bread"><span>帮助中心</span><img src="<%=request.getContextPath() %>/image/help_arrow.png" width="7" height="12" /><span>云主机</span><img src="<%=request.getContextPath() %>/image/help_arrow.png" width="7" height="12" /><span class="active">修改密码</span></div>
  <p >1.云主机密码修改 </p>
  <p >windows系统云主机，通过控制面板-用户账户-修改密码 </p>
  <p >Linux系统通过命令passwd&nbsp;用户名&nbsp;修改用户名密码 </p>
  <p >&nbsp;</p>
  <p >2.ftp服务器密码修改 </p>
  <p >window系统云主机，通过运行Filezilla&nbsp;server修改用户密码 </p>
  <p >&nbsp;</p>
  <p ><img src="<%=request.getContextPath() %>/image/help_3_5_wpsA41A.tmp.png" alt="" width="554" height="347" />&nbsp;</p>
  <p >&nbsp;</p>
  <p >Linux系统云主机，执行脚本来修改密码 </p>
  <p >&nbsp;</p>
  <p >脚本名称：vsftpd_virtualuser_update.sh </p>
  <p >执行：sh&nbsp;/opt/vsftpd_shell/vsftpd_virtualuser_update.sh </p>
  <p >&nbsp;</p>
  <p >示例： </p>
  <p >&nbsp;Enter&nbsp;Username&nbsp;(lowercase)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;admin&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; </p>
  <p >&nbsp;Enter&nbsp;Password&nbsp;(case&nbsp;sensitive)&nbsp;:&nbsp;zhicloud123 </p>
  <p >&nbsp;Enter&nbsp;Comment(user's&nbsp;full&nbsp;name)&nbsp;:&nbsp;admin </p>
  <p >&nbsp;Account&nbsp;disabled&nbsp;?&nbsp;(y/N)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;n </p>
</div>
</body>
</html>