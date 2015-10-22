<%@ page pageEncoding="utf-8"  contentType="text/html; charset=utf-8" %>

<!DOCTYPE html>
<html>
	<body>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
		
		<select id="select_1">
			<option value=""></option>
		</select>
		
		<select id="select_2">
			<option value="1" region="1">1</option>
			<option value="2" region="2">2</option>
			<option value="3" region="3">3</option>
			<option value="4" region="4">4</option>
			<option value="5" region="1">5</option>
			<option value="6" region="2">6</option>
		</select>
		
		<script type="text/javascript">
		$(function(){
			
			$("#select_2").find("> option[region=1]").appendTo("#select_1");
			
		});
		</script>
	</body>
</html>
