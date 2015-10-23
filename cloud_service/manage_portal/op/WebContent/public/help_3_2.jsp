<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
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
  <div class="bread"><span>帮助中心</span><img src="<%=request.getContextPath() %>/image/help_arrow.png" width="7" height="12" /><span>云主机</span><img src="<%=request.getContextPath() %>/image/help_arrow.png" width="7" height="12" /><span class="active">连接云主机</span></div>
  <ul>
    <li><h3>1.&#8220;致云客户端&#8221;连接方式：</h3></li>
    <li>下载<a href="https://www.zhicloud.com/download/<%=AppProperties.getValue("client_version_name", "")%>">&#8220;致云客户端&#8221;</a>并安装；</li>
    <li></li>
  </ul>
  <p >运行&#8220;致云客户端&#8221;，输入用户名和密码（Zhicloud.com登录用户名和密码），点击&#8220;登录&#8221;进入；</p>
  <p >&nbsp;</p>
  <p ><img src="<%=request.getContextPath() %>/image/help_3_2_wpsA57B.tmp.png" alt="" width="554" height="282" />&nbsp;</p>
  <p >&nbsp;</p>
  <p >登录后进入我的云主机列表，选择一个云主机，点击&#8220;进入&#8221;即可。 </p>
  <h3 >&nbsp;</h3>
  <ul>
    <li></li>
    <li></li>
    <li><h3>2.windows远程桌面连接（RDP）方式：</h3></li>
    <li>点击&#8220;开始&#8221;-&#8220;运行&#8221;，输入&#8220;mstsc&#8221;回车确定； </li>
    <li></li>
  </ul>
  <p ><img src="<%=request.getContextPath() %>/image/help_3_2_wpsA557.tmp.png" alt="" width="413" height="237" />&nbsp;</p>
  <p >&nbsp;</p>
  <ul>
    <li>打开&#8220;远程桌面连接&#8221;后，输入云主机的公网IP地址，点击&#8220;连接&#8221;。 </li>
    <li></li>
  </ul>
  <p ><img src="<%=request.getContextPath() %>/image/help_3_2_wpsA558.tmp.png" alt="" width="489" height="328" />&nbsp;</p>
  <p >&nbsp;</p>
  <ul>
    <li>输入云主机默认用户名：administrator，默认密码：zhicloud@123（请及时更改系统默认密码）。 </li>
    <li></li>
  </ul>
  <p ><img src="<%=request.getContextPath() %>/image/help_3_2_wpsA569.tmp.png" alt="" width="554" height="473" />&nbsp;</p>
  <p >&nbsp;</p>
  <ul>
    <li></li>
    <li><h3>3.Linux SSH远程连接方式 ：</h3></li>
  </ul>
  <p >&#9;打开SSH工具，如&#8220;SSH&nbsp;Secure&nbsp;Shell&#8221;，输入云主机公网IP、用户名（默认root）、密码（默认zhicloud@123）后进入云主机系统。 </p>
  <p >&nbsp;</p>
  <p ><img src="<%=request.getContextPath() %>/image/help_3_2_wpsA58D.tmp.png" alt="" width="398" height="140" />&nbsp;</p>
  <p >&nbsp;</p>
  <p >&nbsp;</p>
  <p >&nbsp;</p>
  <p >&nbsp;</p>
  <p >&nbsp;</p>
  <p >&nbsp;</p>
  <p >&nbsp;</p>
</div>
</body>
</html>

