<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String region = request.getParameter("region");
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - CPU配置列表</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
	</head>
	<body>
		<form id="package_form"  method="post">
			<table id="package_datagrid" class="easyui-datagrid" 
				style="height:300px;"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=packageOptionService&method=queryPackagePrice',
					queryParams: {
						type:5,
						region:<%=region%>
					},
					loadMeg:'数据加载中，请稍等...',
					toolbar: '#toolbar_package',
					rownumbers: true,
					striped: true,
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 20,
					view: createView(),
					onDblClickRow:dblClickRow,
					onLoadSuccess:function(data){
						onLoadSuccess();
						checkSize();
					},
					onBeforeEdit:function(index,row){  
				        row.editing = true;  
				        $('#package_datagrid').datagrid('refreshRow', index);  
				    },  
				    onAfterEdit:afterEdit,  
				    onCancelEdit:function(index,row){  
				        row.editing = false;  
				        $('#package_datagrid').datagrid('refreshRow', index);  
				    } 
				">
				<thead>
					<tr>
						<th data-options="field:'ck',checkbox:true"></th>
						<th data-options="field:'cpuCore',width:20,sortable:true,
							editor:{  
				                type:'combobox',  
				                options:{
				                	url:'<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=packageOptionService&method=getOption&region='+encodeURIComponent(<%=region %>)+'&type='+encodeURIComponent(1),  
				                    valueField:'value',  
				                    textField:'text',  
				                    required:true  
				                }  
				            }
						">CPU核数</th>
						<th data-options="field:'memory',width:20,
				            editor:{  
				                type:'combobox',  
				                options:{  
				                    url:'<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=packageOptionService&method=getOption&region='+encodeURIComponent(<%=region %>)+'&type='+encodeURIComponent(2),  
				                    valueField:'value',  
				                    textField:'text',  
				                    required:true  
				                }  
				            }">内存(G)</th>
				        <th data-options="field:'dataDisk',width:20,
				            editor:{  
				                type:'combobox',  
				                options:{  
				                    url:'<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=packageOptionService&method=getOption&region='+encodeURIComponent(<%=region %>)+'&type='+encodeURIComponent(3),  
				                    valueField:'value',  
				                    textField:'text',  
				                    required:true  
				                }  
				            }">硬盘(G)</th>
				        <th data-options="field:'bandwidth',width:20,
				            editor:{  
				                type:'combobox',  
				                options:{  
				                    url:'<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=packageOptionService&method=getOption&region='+encodeURIComponent(<%=region %>)+'&type='+encodeURIComponent(4),  
				                    valueField:'value',  
				                    textField:'text',  
				                    required:true  
				                }  
				            }">带宽(M)</th>
						<th data-options="field:'price',
							editor:{
								type:'numberbox',
								options:{
									min:0,
									length:[1,8],
									precision:2,
									required:true
								},
							},
							width:20
						">价格(元/月)</th>
						<th data-options="field:'description',
							editor:{
								type:'text',
								options:{
								length:[0,100]
								}
							},
							formatter:descColumnFormatter,
							width:40
						">描述</th>
						<th data-options="field:'operate',
							formatter:packageColumnFormatter,
							width:20
						">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar_package" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add_package_btn">添加</a> 
					</div>
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-edit" plain="true" id="update_package_btn">修改</a> 
					</div>
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="cancel_package_btn">取消编辑</a> 
					</div>
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-save" plain="true" id="save_package_btn">保存</a> 
					</div>
				</div>
			</div>
		</form>
		<script type="text/javascript">
        var editRow = undefined; //定义全局变量：当前编辑的行
		var _region = "<%=region%>";
		var packageListSize = '0';
		// 添加
			$("#add_package_btn").click(function(){
				if(packageListSize>=8){
					top.$.messager.alert("提示","套餐显示项不能超过8项","info");
					return;
				}
				if(editRow!=undefined){
					$("#package_datagrid").datagrid("endEdit",editRow);
				}
				
				//添加时如果没有正在编辑的行，则在datagrid的第一行插入一行
                if (editRow == undefined) {
                	$("#package_datagrid").datagrid("insertRow", {
                        index: 0, // index start with 0
                        row: {

                        }
                    });
                    //将新插入的那一行开户编辑状态
                    $("#package_datagrid").datagrid("beginEdit", 0);
                    //给当前编辑的行赋值
                    editRow = 0;
                }
			});
			function checkSize(){
				var d = $("#package_datagrid").datagrid("getData");
				var t = d.total;
				packageListSize = t;
			}
			// 修改
			$("#update_package_btn").click(function(){
				var rows = $("#package_datagrid").datagrid("getSelections");
				if(rows.length == 1){
					if (editRow != undefined) {
						$("#package_datagrid").datagrid("endEdit", editRow);
                    }
                    //当无编辑行时
                    if (editRow == undefined) {
                        //获取到当前选择行的下标
                        var index = $("#package_datagrid").datagrid("getRowIndex", rows[0]);
                        //开启编辑
                        $("#package_datagrid").datagrid("beginEdit", index);
                        //把当前开启编辑的行赋值给全局变量editRow
                        editRow = index;
                        //当开启了当前选择行的编辑状态之后，
                        //应该取消当前列表的所有选择行，要不然双击之后无法再选择其他行进行编辑
                        $("#package_datagrid").datagrid("unselectAll");
                    }
				}else{
					top.$.messager.alert("警告","请选择一行进行修改","warning",function(){
					});
				}
			});
			//双击修改
			function dblClickRow(rowIndex, rowData){
				if (editRow != undefined) {
					$("#package_datagrid").datagrid("endEdit", editRow);
                }
				if (editRow == undefined) {
                    //开启编辑
                    $("#package_datagrid").datagrid("beginEdit", rowIndex);
                    //把当前开启编辑的行赋值给全局变量editRow
                    editRow = rowIndex;
                    //当开启了当前选择行的编辑状态之后，
                    //应该取消当前列表的所有选择行，要不然双击之后无法再选择其他行进行编辑
                    $("#package_datagrid").datagrid("unselectAll");
                }
			}
			// 取消
			$("#cancel_package_btn").click(function(){
				editRow = undefined;
				$("#package_datagrid").datagrid("rejectChanges");
				$("#package_datagrid").datagrid("unselectAll");
			});
			// 保存
			$("#save_package_btn").click(function(){
				$("#package_datagrid").datagrid("endEdit", editRow);
				editRow = undefined;
				$("#package_datagrid").datagrid("reload");
			});
			function afterEdit(index,row){
				var inserted = $('#package_datagrid').datagrid('getChanges','inserted');
				var updated = $('#package_datagrid').datagrid('getChanges','updated');
				row.type = "5";
				row.region = _region;
				row.editing = false;  
		        $('#package_datagrid').datagrid('refreshRow', index);
		        if(inserted.length == 1){
			        ajax.remoteCall("bean://packageOptionService:addPackagePrice", 
		    			[ row ],
		    			function(reply) {
		    				if (reply.status == "exception") {
		    					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
		    				} else if (reply.result.status == "success") {
		    					$("#package_datagrid").datagrid("acceptChanges");
		    					$('#package_datagrid').datagrid("reload");
		    				} else {
		    					top.$.messager.alert('警告',reply.result.message,'warning');
		    				}
		    			}
		    		);
		        }
		        if(updated.length == 1){
		        	var data = $("#package_datagrid").datagrid("getData");
		    		var id = data.rows[index].id;
		    		row.id = id;
		        	ajax.remoteCall("bean://packageOptionService:modPackagePrice", 
	        			[ row ],
	        			function(reply) {
	        				if (reply.status == "exception") {
	        					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
	        				} else if (reply.result.status == "success") {
	        					$("#package_datagrid").datagrid("acceptChanges");
	        					$('#package_datagrid').datagrid("reload");
	        				} else {
	        					top.$.messager.alert('警告',reply.result.message,'warning');
	        				}
	        			}
	        		);
		        }
			}
		</script>
	</body>
</html> 