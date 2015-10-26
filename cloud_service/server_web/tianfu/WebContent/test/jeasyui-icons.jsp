<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		
		<title>运营商 - 云主机管理</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
	</head>
	<body>
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-blank'">icon-blank</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-add'">icon-add</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">icon-edit</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-remove'">icon-remove</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-save'">icon-save</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cut'">icon-cut</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-ok'">icon-ok</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-no'">icon-no</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">icon-cancel</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-reload'">icon-reload</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">icon-search</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-print'">icon-print</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-help'">icon-help</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-undo'">icon-undo</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-redo'">icon-redo</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-back'">icon-back</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-sum'">icon-sum</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-tip'">icon-tip</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-filter'">icon-filter</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-mini-add'">icon-mini-add</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-mini-edit'">icon-mini-edit</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-mini-refresh'">icon-mini-refresh</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-large-picture'">icon-large-picture</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-large-clipart'">icon-large-clipart</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-large-shapes'">icon-large-shapes</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-large-smartart'">icon-large-smartart</a><br />
		<a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-large-chart'">icon-large-chart</a><br />
	</body>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
</html>