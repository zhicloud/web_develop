<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import = "java.math.BigInteger "%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));	
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String userId = loginInfo.getUserId();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- uploaded_file_manage.jsp -->
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 		
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/global.css" media="all"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/user/css/mycloud.css" media="all"/>
 
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/big.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/unslider.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/goup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/plugin.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/common.js"></script>

<script type="text/javascript">

window.name = "selfWin";

var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
ajax.async = false;
var size = 0;
var totalSize = 0;


$(document).ready(function(){ 
	initstep(1);
	
	setCurrentFileSize();
	
});


// 布局初始化
$("#uploaded_file_datagrid").height( $(document.body).height());

function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.reload();
}

function setCurrentFileSize(){
	var userId = "<%=userId%>";
	ajax.remoteCall("bean://cloudUserService:getCurrentFileSize",
			[userId], 
			function(reply) {
				if(reply.result.status == "success") {
					size = reply.result.properties.size;
					totalSize = reply.result.properties.totalSize;
					var sizeFormated = CapacityUtil.toCapacityLabel(size,0);
					var totalSizeFormated = CapacityUtil.toCapacityLabel(totalSize,0);
					$("#size").html(sizeFormated);
					$("#total_size").html(totalSizeFormated);
				}
			}
		); 
}


function dataGrid(){
	var pager = $('#uploaded_file_datagrid').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		showRefresh: false,
		displayMsg: '',
		beforePageText: '',
		afterPageText: ''
	 });            
}

// 显示对话框，提供给iframe使用
	function showSingleDialog(param)
	{
		var container = $("#single_dialog_dynamic_container");
		if( container.length==0 )
		{
			container = $("<div id='single_dialog_dynamic_container' style='display:none;'></div>").appendTo(document.body);
		}
		if( param.params==null )
		{
			param.params = {};
		}
		container.load(
			param.url, 
			param.params,
			function(){
				$.parser.parse(container.get(0));
				container.prop("_data_", param);
			}
		);
	}

function formatSize(val)	
{   
	return CapacityUtil.toCapacityLabel(val,0);
}  

function operateColumnFormatter(value, row, index)
{
	return "<div row_index='"+index+"'>\
	<a href='javascript:void(0);' class='datagrid_row_linkbutton download_btn'><img src='<%=request.getContextPath()%>/image/button_download_i.png' width='24' height='24' alt=' ' title='下载' /></a>\
	<a href='javascript:void(0);' class='datagrid_row_linkbutton delete_btn'><img src='<%=request.getContextPath()%>/image/button_trash_i.png' width='24' height='24' alt=' ' title='删除' /></a>\
</div>";
} 

function timeFormat(val)	
{   
	return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
}  


//查询结果为空
 function createView(){
	return $.extend({},$.fn.datagrid.defaults.view,{
	    onAfterRender:function(target){
	        $.fn.datagrid.defaults.view.onAfterRender.call(this,target);
	        var opts = $(target).datagrid('options');
	        var vc = $(target).datagrid('getPanel').children('div.datagrid-view');
	        vc.children('div.datagrid-empty').remove();
	        if (!$(target).datagrid('getRows').length){
	            var d = $('<div class="datagrid-empty"></div>').html( '没有相关记录').appendTo(vc);
	            d.css({
	                position:'absolute',
	                left:0,
	                top:50,
	                width:'100%',
	                textAlign:'center'
	            });
	        }
	    }
    });
} 
	
function onLoadSuccess()
{
	
	dataGrid();
	
	// 每一行的'下载'按钮
	$("a.download_btn").hover(function() { 
		$(this).find("img").attr("src",a+"/image/button_download_h.png");  
		}, function() {
		$(this).find("img").attr("src",a+"/image/button_download_i.png");   
		});
	$("a.download_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#uploaded_file_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		window.location.href= "<%=request.getContextPath()%>/fileDownload.do?fileId="+encodeURIComponent(id)+"&id=id";
	});
		// 每一行的'删除'按钮
		 
		$("a.delete_btn").hover(function() { 
			$(this).find("img").attr("src",a+"/image/button_trash_h.png");  
			}, function() {
			$(this).find("img").attr("src",a+"/image/button_trash_i.png");   
			}); 
		$("a.delete_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#uploaded_file_datagrid").datagrid("getData");
			var ids = data.rows[rowIndex].id;
			top.$.messager.confirm("确认", "确定要删除吗?", function (r) {  
		        if (r) {    
						ajax.remoteCall("bean://cloudUserService:deleteUploadedFileByIds",
							[ [ids] ], 
							function(reply) {
								if (reply.status == "exception") {
									if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
										top.$.messager.alert("警告","会话超时，请重新登录","warning");
									}else{
										top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
											top.location.reload();
										});
									}
								} else {
									$('#uploaded_file_datagrid').datagrid('reload');
									setCurrentFileSize();
								}
							}
						);
		        }  
		    });   
		});
}

$(function(){
	// 查询
	$("#query_uploaded_file_btn").click(function(){
		var queryParams = {};
		queryParams.fileName = $("#fileName").val().trim();
		$('#uploaded_file_datagrid').datagrid({
			"queryParams": queryParams
		});
		dataGrid();
	});
	// 上传文件
	$("#add_upload_file_btn").click(function(){
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudUserService&method=uploadFilePage",
			onClose: function(data){
				location.reload(false);
				setCurrentFileSize();
			}
		});
	});
	// 批量删除文件
	$("#del_uploaded_files_btn").click(function() {
		var rows = $('#uploaded_file_datagrid').datagrid('getSelections');
		if (rows == null || rows.length == 0)
		{
			top.$.messager.alert("警告","未选择删除项");
			return;
		}
		var ids = rows.joinProperty("id");
		top.$.messager.confirm("确认", "确定要删除吗?", function (r) {  
	        if (r) {    
					ajax.remoteCall("bean://cloudUserService:deleteUploadedFileByIds",
						[ ids ], 
						function(reply) {
							if (reply.status == "exception") {
								if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
									top.$.messager.alert("警告","会话超时，请重新登录","warning");
								}else{
									top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
										top.location.reload();
									});
								}
							} else {
								location.reload(false);
								setCurrentFileSize();
							}
						}
					);
	        }  
	    });   
	});
	
// 	// 删除文件
// 	$("#del_uploaded_file_btn").click(function(ids) {
		
// 	});
	
	
	$('#fileName').bind('keypress',function(event){
        if(event.keyCode == "13")    
        {
        	$("#query_uploaded_file_btn").click();
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
	<body>
	<div class="page">
  		<div class="pageleft">
    		<jsp:include page="/src/common/tpl/u_header.jsp"></jsp:include>
		<div class="main">
			<div class="wrap"><jsp:include page="/src/common/tpl/u_mcslider.jsp"></jsp:include></div>
    		<div class="titlebar">
		<div class="l" style="font-size:12px;">总容量：<b><span id="total_size"></span></b>　已使用：<b><span id="size"></span></b></div>
        		<div class="r">文件夹</div>
      		</div> <div class="titlebar" style="padding-top:30px">
      		<a href="javascript:void(0);" id="add_upload_file_btn" class="bluebutton l" style="margin-right:10px;width:110px"><img src="<%=request.getContextPath()%>/image/icon_upload.png" width="20" height="20" style="vertical-align:middle;margin-right:10px" />上传文件</a>
      		<a href="javascript:void(0);" id="del_uploaded_files_btn" class="bluebutton l" style="margin-right:10px;width:110px"><img src="<%=request.getContextPath()%>/image/button_delete.png" width="20" height="20" style="vertical-align:middle;" />全部删除</a>
      		 <div class="r"><input id = "fileName" name="fileName" type="text" class="textbox" style="height:28px; width:108px;margin-right:10px;background:transparent"/>
      		 	<a href="javascript:void(0);" id="query_uploaded_file_btn"><img src="<%=request.getContextPath()%>/image/button_search.png" width="30" height="30" style="vertical-align:middle;" /></a></div>
      		</div>
      		<div class="box">
			<table id="uploaded_file_datagrid" class="easyui-datagrid"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=cloudUserService&method=queryMyFile',
					queryParams: {},
					toolbar: '#toolbar',
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageSize: 10, 
 					onLoadSuccess: onLoadSuccess
					">
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
              			<th data-options="field:'fileName',width:380" >文件名称</th>
              			<th data-options="field:'size',width:100" formatter="formatSize">文件大小</th>
             			<th data-options="field:'uploadTime',width:150"  formatter = "timeFormat">上传时间</th>
             			<th data-options="field:'operate',width:100"  formatter="operateColumnFormatter">&nbsp;</th>
					</tr>
				</thead>
			</table>
			</div>
      <div class="clear">&nbsp;</div>
    </div>
        		<jsp:include page="/src/common/tpl/u_footer.jsp"></jsp:include>

  </div>
  <div class="pageright">
    <iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div>
</div>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
$(function(){
	navHighlight("umc","umul"); 
});
</script>
</body>
</html>