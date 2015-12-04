<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@include file="/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>日志清单</title>
	<style type="text/css">
	.pagination table{
		margin:0px;
	}
	.queryspan{
		margin-left:5px;
		margin-right:5px;
	}
	</style>
</head>
<body>
	<div class="g-mn" id="gmn">
		<div class="container" id="container">
			<div class="loglist-wrap">
				<h3>日志列表</h3>
				<form id="big_form"  method="post">
			<div id="toolbar">
				<div style="display: table; width: 100%;padding-top:5px;padding-bottom:5px;">
					<div style="margin-bottom:5px;">
					<span class="queryspan" style="margin-left:0px;">开始日期</span><input class="easyui-datetimebox" id="start_time" style="height:25px;" data-options="editable:false"/>
					<span class="queryspan">至</span><input class="easyui-datetimebox" id="end_time" style="height:25px;" data-options="editable:false"/>
					<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_btn">查询</a>
					<a href="#" class="easyui-linkbutton" iconCls="icon-clear" id="clear_btn">清除</a>
					</div>
				</div>
			</div>		
					<table id="list_data" class="easyui-datagrid" data-options="url: '<%=request.getContextPath() %>/log/querydata',
					queryParams: {},
					border:false,
					singleSelect:true,
					scrollbarSize:0,
					pagination:true,
					remoteSort:false,
					fitColumns: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 20,
					onLoadSuccess: onLoadSuccess,
					onLoadError:onLoadError">
						<thead>
							<tr>
								<th field="username" width="100">用户名</th>
								<th field="actiondesc" width="400">动作</th>
								<th field="operatetime" width="200">时间</th>
							</tr>
						</thead>
					</table> 
					</form>
					<div class="clear">&nbsp;</div>
			</div>
		</div>
	</div>
	<div class="g-ft"></div>
<script type="text/javascript">
// 布局初始化
$("#list_data").height(400);

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
	$("body").css({
		"visibility":"visible"
	});
}
function onLoadError(data){
	
}
//比较时间大小
function compareTo(beginTime,endTime){  
    var beginTimes = beginTime.substring(0,10).split('-');  
    var endTimes   =  endTime.substring(0,10).split('-');  
      
    beginTime = beginTimes[1]+'-'+beginTimes[2]+'-'+beginTimes[0]+' '+beginTime.substring(10,19);  
    endTime    = endTimes[1]+'-'+endTimes[2]+'-'+endTimes[0]+' '+endTime.substring(10,19);  
    var a =(Date.parse(endTime)-Date.parse(beginTime))/3600/1000;  
    if(a<0){ 
    	return -1;//开始日期大
    }else if (a>0){  
    	return 1;//截止日期大
    }else if (a==0){  
        return 0;
    }  
}  

$(function(){
	// 查询
	$("#query_btn").click(function(){
		var queryParams = {};
		var re = compareTo($("#start_time").datetimebox("getValue"),$("#end_time").datetimebox("getValue"));
		if(re==-1){
			jQuery.messager.alert('提示:','开始日期不能大于截止日期','info'); 
			return; 
		}
		queryParams.start_time = $("#start_time").datetimebox("getValue");
		queryParams.end_time = $("#end_time").datetimebox("getValue");
		
		$('#list_data').datagrid({
			"queryParams": queryParams
		});
	});
	//清除
	$("#clear_btn").click(function(){
		$("#start_time").datetimebox("setValue","");
		$("#end_time").datetimebox("setValue","");
		
	});	
});
</script>

</body>
</html>