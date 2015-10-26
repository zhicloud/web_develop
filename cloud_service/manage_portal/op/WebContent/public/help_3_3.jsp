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
  <div class="bread"><span>帮助中心</span><img src="<%=request.getContextPath() %>/image/help_arrow.png" width="7" height="12" /><span>云主机</span><img src="<%=request.getContextPath() %>/image/help_arrow.png" width="7" height="12" /><span class="active">使用数据盘</span></div>
  <h3 >Windows系统 </h3>
  <p >&nbsp;</p>
  <p >以win2003系统参考，进入云主机后，右键&#8220;我的电脑&#8221;-&gt;&#8220;管理&#8221;&#8212;&gt;&#8220;磁盘管理&#8221;，系统会提示需初始化磁盘，根据向导提示初始化及格式化数据盘。 </p>
  <p >&nbsp;</p>
  <p ><img src="<%=request.getContextPath() %>/image/help_3_3_wps10EB.tmp.png" alt="" width="554" height="346" />&nbsp;</p>
  <p >&nbsp;</p>
  <h3 >Linux系统 </h3>
  <p >&nbsp;</p>
  <p >1）通过ssh登录后，执行命令&#8220;fdisk&nbsp;-l&#8221;查看数据盘信息 </p>
  <p >&nbsp;</p>
  <p ><img src="<%=request.getContextPath() %>/image/help_3_3_wps10EC.tmp.png" alt="" width="527" height="286" />&nbsp;</p>
  <p >2）格式化数据盘，如sdb，执行命令&#8220;mkfs.ext4&nbsp;/dev/sdb&#8221; </p>
  <p >&nbsp;</p>
  <p ><img src="<%=request.getContextPath() %>/image/help_3_3_wps10ED.tmp.png" alt="" width="554" height="312" />&nbsp;</p>
  <p >&nbsp;</p>
  <p >3）新建一个目录用于挂载数据盘，如：&#8221;mkdir&nbsp;/data&#8221; </p>
  <p >&nbsp;</p>
  <p ><img src="<%=request.getContextPath() %>/image/help_3_3_wps10FE.tmp.png" alt="" width="380" height="51" />&nbsp;</p>
  <p >&nbsp;</p>
  <p >4）挂载数据盘，如：&#8220;mount&nbsp;/dev/sdb&nbsp;/data&#8221; </p>
  <p >&nbsp;</p>
  <p ><img src="<%=request.getContextPath() %>/image/help_3_3_wps10FF.tmp.png" alt="" width="501" height="128" />&nbsp;</p>
</div>
</body>
</html>