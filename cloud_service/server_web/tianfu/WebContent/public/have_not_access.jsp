<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云代理商管理平台</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<script src="<%=request.getContextPath() %>/javascript/jquery.min.js"></script>
<script src="<%=request.getContextPath() %>/javascript/jquery.easyui.min.js"></script>
<script src="<%=request.getContextPath() %>/javascript/page.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$(".swapimage").parent().hover(function(event){
		if(!$(this).hasClass("active")){
			$(this).find("img").attr("src","<%=request.getContextPath() %>/image/"+$(this).find("img").attr("id")+"_h.png");
		}
	},
	function(event){
		if(!$(this).hasClass("active")){
			$(this).find("img").attr("src","<%=request.getContextPath() %>/image/"+$(this).find("img").attr("id")+"_i.png");
		}
	});
});
</script>
<!--[if IE 6]>
<script src="javascript/DD_belatedPNG.js"></script>
<script type="text/javascript">
	DD_belatedPNG.fix("*");
</script>
<![endif]-->
</head>

<body style="background:#f7f7f7;">
<table width="100%" height="100%" border="0" cellspacing="0" cellpadding="0" style="background:#f7f7f7;">
  <tr>
    <td align="center" valign="middle"><div style="width:129px;height:22px;background:url(<%=request.getContextPath() %>/image/norights.jpg);padding:480px 259px 121px 259px"><a href="javascript:history.go(-1);"><img id="backbtn" class="swapimage" src="<%=request.getContextPath() %>/image/backbtn_i.png" width="129" height="22" /></a></div></td>
  </tr>
</table>

</body>
</html>
