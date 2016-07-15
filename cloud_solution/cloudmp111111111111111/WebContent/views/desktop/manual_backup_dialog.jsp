<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- login.jsp -->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>系统提示</title>
<script src="<%=request.getContextPath()%>/assets/js/jquery.js"></script>
<script src="<%=request.getContextPath() %>/js/plugins/artDialog/artDialog.js?skin=chrome"></script>
<script src="<%=request.getContextPath() %>/js/plugins/artDialog/jquery.artDialog.source.js"></script>
<style type="text/css">
html {padding-top: 0;}
.inside-block{width:650px;}
.center{text-align: center;margin-top: 50px;background-color:rgba(240, 245, 248, 1.0)}
.body #content.div{height: 25px;margin-top:10px;}
.top{height: 25px;margin-top: 20px;}
.but{
background: #b977ff;
display: inline-block;
height: 37px;
line-height: 38px;
width: 83px;
color: #fcf7f5;
padding: 0;
border: none;
font-size: 16px;
}
.textColor{
color:gray;
}
.input{
height: 25px;
}


body #content.div input:focus, #content.full-page .inside-block form .input-group input:focus + .input-group-addon {background-color: rgba(0, 0, 0, 0.3) !important;-webkit-box-shadow: none !important;box-shadow: none !important;}
body #content.div input::-webkit-input-placeholder {color: rgba(255, 255, 255, 0.6);}
body #content.div input:-moz-placeholder {color: rgba(255, 255, 255, 0.6);}
body #content.div input::-moz-placeholder {color: rgba(255, 255, 255, 0.6);}
body #content.div input:-ms-input-placeholder {color: rgba(255, 255, 255, 0.6);}

</style>
</head>

<body class="center">
<div id="content">
	<div class="div"><h3 style="margin-top:0;margin-bottom:5px;">${backupsTitle}</h3></div>
	<c:if test="${runningStatus == '1'}">
		<div class="top">${content}</div>
		<div class="top">
			<button class="btn btn-slategray but" onclick="go()">确认</button>
			<button class="but" onclick="reset()">取 消</button>
		</div>
	</c:if>
	<c:if test="${runningStatus != '1'}">
		<div class="top">${content}</div>
	</c:if>
</div>
</body>
</html>
<script type="text/javascript">
var path = '<%=request.getRequestURI()%>';
function go(){
	window.parent.saveForm();//修改成功调用父页面方法跳转登录页面
	window.parent.window.art.dialog({ id: 'dialogBack' }).close();
}

//取消
function reset(){
	window.parent.window.art.dialog({ id: 'dialogBack' }).close();
}
</script>
