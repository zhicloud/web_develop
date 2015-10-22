<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@include file="/views/common/common.jsp" %>

<%
	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>服务器运行状态</title>
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
			<div class="sys-list">
				<div class="iteam-left">
					<h3>服务器</h3>
					<div class="top-cont"><img src="../images/sys_server.jpg" alt="服务器"/></div>
					<div class="bottom-cont">
						<img id="imgshutdown" src="../images/sys_img1.png" style="display:inline-block;cursor:pointer;margin-bottom:4px;margin-left:5px;vertical-align:middle;" alt="关闭服务器" />
						<img id="imgreboot"   src="../images/sys_img2.png" style="display:inline-block;cursor:pointer;margin-bottom:4px;margin-left:5px;vertical-align:middle;" alt="重启服务器" />
					</div>
				</div>
				<div class="iteam-right">
					<div class="detlinfo-cont">
						<h3>详细信息</h3>
						<div class="detlinfo-list">
							<div class="detlinfo-iteam">
								<img class="di-name" src="../images/di_name.png" alt="名称" style="width: 44px;height: 63px;"/>
								<div class="di-cont">
									<input type="text" name="name" id="ma_edit_name" style="width:165px;" disabled/>
									<a class="btn_ma_edit mda-sty" href="javascript:;">编辑</a>
									<a class="btn_ma_save mda-sty mda-save-sty" id="editHostname" href="javascript:;" style="display: none;">保存</a>
								</div>
							</div>
							<div class="detlinfo-iteam">
								<img class="di-time" src="../images/di_time.png" alt="时间" style="width: 43px;height: 56px;"/>
								<div class="di-cont">
									<div id="div_select_datetime" style="display:none" >
										<input class="easyui-datetimebox" id="newtime" style="height:25px;" data-options="editable:false"/>
										<a class="btn_da_save mda-sty mda-save-sty" id="editDatetime" href="javascript:;" style="display:inline-block;">保存</a>
									</div>
									<div id="div_disable_datetime">
										<input type="text" name="smtime" id="sm_time"  style="width:165px;" class="sm-time" disabled readonly/>
										<a class="btn_da_edit mda-sty" href="javascript:;">编辑</a>
									</div>
									
								</div>
							</div>
							<div class="detlinfo-iteam">
								<img class="di-version" src="../images/di_version.png" alt="版本" style="width: 38px;height: 66px;"/>
								<div class="di-info">
									<p class="t-box"><label>固件版本：</label><span>2015.1.4</span></p>
									<p class="btn-box">
										<a class="upgrade" href="javascript:;">升级</a>
										<span>&nbsp;&nbsp;|&nbsp;&nbsp;</span>
										<a class="sermodule" href="javascript:;">服务模块版本升级</a>
									</p>
									<!-- 固件版本升级操作框 -->
									<div class="upg-popup" id="div_firewareupdate">
										<h5>固件版本升级</h5>
										<ul>
											<li><label>当前版本：</label><input type="text" name="ver-current" id="ver_current" class="ver-sty" /></li>
											<li><label>最新版本：</label><input type="text" name="ver-latest" id="ver_latest" class="ver-sty" /></li>
										</ul>
										<p>
											<a href="javascript:;" class="manu-upgrade" id="update_fireware">手动升级固件版本</a>
											<a href="javascript:;" class="upg-save upg-sty" id="update_save">保存</a>
											<a href="javascript:;" class="upg-cancel upg-sty" id="update_cancel">取消</a>
										</p>
									</div>
									<!-- 具体升级对话框 -->									
									<div class="upg-mmu-popu">									
										<form id="form_upload" action="<%=request.getContextPath() %>/sysmanage/uploadFireware" method="post">
											<h5>固件版本手动升级</h5>
											<div class="upgmmu-inpt">
												<input type="text" name="upgselectfile" id="upgselectfile" class="upgselectfile" disabled />
												<a class="btn-selectfile" href="javascript:;"  id="btn_selectfile">选择文件</a>
											</div>											
											<p id="tipscontent"></p>
											<div class="upgmmu-btn">
												<a class="btn-upgmmu-upgrade summ-sty sty-bgblue" href="javascript:;" id="upload_save" onclick="saveFormFireware();">升级</a>
												<a class="btn-upgmmu-cancel summ-sty" href="javascript:;">取消</a>
											</div>												
											<div style="display:none" >
												<input type="file" name="file_fireware" id="file_fireware" />
											</div>									
										</form>
									</div>
									<div class="sermodule-popup">
										<p class="tab-upgrade">
											<a class="online-upgrade active" href="javascript:;">在线升级</a>
											<label>&nbsp;&nbsp;|&nbsp;&nbsp;</label>
											<a class="manual-upgrade" href="javascript:;">手动升级</a>
										</p>
										<div class="ou-cont">
											<ul>
												<li><input type="checkbox" name="dataServer" id="data_server"/><label for="data_server">Data Server</label></li>
												<li><input type="checkbox" name="nodeClient" id="node_client"/><label for="node_client">Node Client</label></li>
												<li><input type="checkbox" name="controlServer" id="control_server"/><label for="control_server">Control Server</label></li>
												<li><input type="checkbox" name="statisticSetver" id="statistic_setver"/><label for="statistic_setver">Statistic Setver</label></li>
												<li><input type="checkbox" name="intelligentRouter" id="intelligent_router"/><label for="intelligent_router">Intelligent Router</label></li>
												<li><input type="checkbox" name="intelligentRoute" id="intelligent_route"/><label for="intelligent_route">Intelligent Router</label></li>
												<li><input type="checkbox" name="managerTerminal" id="manager_terminal"/><label for="manager_terminal">Manager Terminal</label></li>
												<li><input type="checkbox" name="managerTermina" id="manager_termina"/><label for="manager_termina">Manager Terminal</label></li>
												<li><input type="checkbox" name="intelligentRout" id="intelligent_rout"/><label for="intelligent_rout">Intelligent Router</label></li>
												<li><input type="checkbox" name="intelligentRou" id="intelligent_rou"/><label for="intelligent_rou">Intelligent Router</label></li>
												<li><input type="checkbox" name="storageServer" id="storage_server"/><label for="storage_server">Storage Server</label></li>
												<li><input type="checkbox" name="storageServe" id="storage_serve"/><label for="storage_serve">Storage Server</label></li>
											</ul>
											<div class="ou-btn">
												<a class="ou-btn-upgrade sty-upgrade sty-bgblue" href="javascript:;">升级</a>
												<a class="ou-btn-upgrade-cancel sty-upgrade" href="javascript:;">取消</a>
											</div>
										</div>
										<div class="mu-cont">
											<ul>
												<li><input type="checkbox" name="muDataServer" id="mu_data_server"/><label for="mu_data_server">Data Server</label></li>
												<li><input type="checkbox" name="muNodeClient" id="mu_node_client"/><label for="mu_node_client">Node Client</label></li>
												<li><input type="checkbox" name="muControlServer" id="mu_control_server"/><label for="mu_control_server">Control Server</label></li>
												<li><input type="checkbox" name="muStatisticSetver" id="mu_statistic_setver"/><label for="mu_statistic_setver">Statistic Setver</label></li>
												<li><input type="checkbox" name="muIntelligentRouter" id="mu_intelligent_router"/><label for="mu_intelligent_router">Intelligent Router</label></li>
												<li><input type="checkbox" name="muIntelligentRoute" id="mu_intelligent_route"/><label for="mu_intelligent_route">Intelligent Router</label></li>
												<li><input type="checkbox" name="muManagerTerminal" id="mu_manager_terminal"/><label for="mu_manager_terminal">Manager Terminal</label></li>
												<li><input type="checkbox" name="muManagerTermina" id="mu_manager_termina"/><label for="mu_manager_termina">Manager Terminal</label></li>
												<li><input type="checkbox" name="muIntelligentRout" id="mu_intelligent_rout"/><label for="mu_intelligent_rout">Intelligent Router</label></li>
												<li><input type="checkbox" name="muIntelligentRou" id="mu_intelligent_rou"/><label for="mu_intelligent_rou">Intelligent Router</label></li>
												<li><input type="checkbox" name="muStorageServer" id="mu_storage_server"/><label for="mu_storage_server">Storage Server</label></li>
												<li><input type="checkbox" name="muStorageServe" id="mu_storage_serve"/><label for="mu_storage_serve">Storage Server</label></li>
											</ul>
											<div class="mu-btn">
												<a class="mu-btn-upgrade sty-upgrade sty-bgblue" href="javascript:;">上传</a>
												<a class="mu-btn-upgrade-cancel sty-upgrade" href="javascript:;">取消</a>
											</div>
										</div>
									</div>
									<div class="ser-mmu-popu">
										<form id="form_upload_component" action="<%=request.getContextPath() %>/sysmanage/uploadComponent" method="post">
											<h5>服务器模块手动升级</h5>
											<div class="smmu-inpt">
												<input type="text" name="selectcomponentfile" id="selectcomponentfile" class="selectfile" disabled/>
												<a class="btn-selectfile" href="javascript:;" id="btn_selectcomponentfile">选择文件</a>
											</div>
											<div class="smmu-btn">
												<a class="btn-smmu-upgrade summ-sty sty-bgblue" href="javascript:;" onclick="saveFormComponent();">升级</a>
												<a class="btn-smmu-cancel summ-sty" href="javascript:;">取消</a>
											</div>																					
											<div style="display:none" >
												<input type="file" name="file_component" id="file_component" />
											</div>
										</form>	
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="g-ft"></div>
</div>
<script type="text/javascript" src="../common/js/calendar.min.js"></script>
<script type="text/javascript" src="../common/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/plugins/jquery.form.js"></script>
<script type="text/javascript">
$(function(){	
	$('#imgshutdown').click(function(){	
		$.messager.confirm("确认", "确定要关闭服务器吗?", function (r){
			if(r){
				$.ajax({
			        type:"POST",
			        url:"<%=request.getContextPath() %>/sysmanage/shutdownServer",
			        datatype: "json",
			        success:function(data){
			        	var re = eval(data);
		        		$.messager.alert("系统提示", "已经执行关闭服务器！", "info");						
			        },
			        complete: function(XMLHttpRequest, textStatus){
			        },
			        error: function(){
			        	$.messager.alert("系统提示", "已经执行关闭服务器！", "info");
			        }         
			     });
			}
		});	
	});
	
	$('#imgreboot').click(function(){
		$.messager.confirm("确认", "确定要重启服务器吗?", function (r){
			if(r){
				$.ajax({
			        type:"POST",
			        url:"<%=request.getContextPath() %>/sysmanage/rebootServer",
			        datatype: "json",
			        success:function(data){
			        	var re = eval(data);
			        	$.messager.alert("系统提示", "已经执行重启服务器！", "info");
			        },
			        complete: function(XMLHttpRequest, textStatus){
			        },
			        error: function(){
			        	$.messager.alert("系统提示", "已经执行重启服务器！", "当局者迷");
			        }         
			    });
			}
		});
	});	
	
	$('#editHostname').click(function(){
		var hostname = $("#ma_edit_name").val().trim();
		if(hostname.length == 0){
			$.messager.alert("系统提示", "服务器名不能为空！", "error");
			return;
		}
		
		$.messager.confirm("确认", "确定要修改服务器名吗?", function (r){
			if(r){
				$.ajax({
			        type:"POST",
			        url:"<%=request.getContextPath() %>/sysmanage/updateHostname",
			        data:{"newHostname":hostname},
			        datatype: "json",
			        success:function(data){
			        	var re = eval(data);
		        		$.messager.alert("系统提示", "执行修改服务器名！", "info");
		        		
		        		$(".btn_ma_save").css("display","none");
		        		$(".btn_ma_edit").css("display","inline-block");
		        		$("#ma_edit_name").val("");
		        		$("#ma_edit_name").attr("display","none");
		        		$("#ma_edit_name").attr("disabled","true");
						/*
			        	if(re.result == "success"){
			        		$.messager.alert("系统提示", "执行修改服务器名成功！", "info");
			        	}else{
			        		$.messager.alert("系统提示", "修改服务器名失败！", "error");
			        	}
						*/
			        },
			        complete: function(XMLHttpRequest, textStatus){
			        },
			        error: function(){
			        	$.messager.alert("系统提示", "修改服务器名出错！", "error");
			        }         
			     });

				$(this).css("display","none");
				$(".btn_ma_save").css("display","inline-block");
				$("#ma_edit_name").attr("disabled",false);
			}
		});
	});
	
	$('#editDatetime').click(function(){
		var newdatetime = $("#newtime").datetimebox("getValue");
		if(newdatetime.length == 0){
			$.messager.alert("系统提示", "时间不能为空！", "error");
			return;
		}
		$.messager.confirm("确认", "确定要修改服务器时间吗?", function (r){
			if(r){		
				$.ajax({
			        type:"POST",
			        url:"<%=request.getContextPath() %>/sysmanage/updateDatetime",
			        data:{"newDatetime":newdatetime},
			        datatype: "json",
			        success:function(data){
			        	var re = eval(data);
			        	$.messager.alert("系统提示", "执行修改系统时间成功！", "info");
			        	$("#newtime").datetimebox("setValue", "");
			    		$("#div_select_datetime").hide();
			    		$("#div_disable_datetime").show();
			        },
			        complete: function(XMLHttpRequest, textStatus){
			        },
			        error: function(){
			        	$.messager.alert("系统提示", "修改系统时间出错！", "error");
			        } 
			    });
			}
		});
	});
	
	$(".upgrade").on("click",function(){
		$(".sermodule").removeClass("current");
		$(this).addClass("current");
		$(".sermodule-popup").css("display","none");
		$(".ser-mmu-popu").css("display","none");
		//$(".upg-popup").css("display","block");
		$(".upg-mmu-popu").css("display","block");
	})
	$(".upg-cancel").on("click",function(){
		$(".upg-popup").css("display","none");
		$(".upgrade").removeClass("current");
	})
	$(".sermodule").on("click",function(){
		$(".upgrade").removeClass("current");
		$(this).addClass("current");
		$(".upg-popup").css("display","none");
		$(".upg-mmu-popu").css("display","none");
		//$(".sermodule-popup").css("display","block"); // 选择界面
		$(".ser-mmu-popu").css("display","block");    // 手动升级界面
	})
	$(".ou-btn-upgrade-cancel,.mu-btn-upgrade-cancel").on("click",function(){
		$(".sermodule-popup").css("display","none");
		$(".sermodule").removeClass("current");
	})
	
	$(".online-upgrade").on("click",function(){
		$(".manual-upgrade").removeClass("active");
		$(this).addClass("active");
		$(".mu-cont").css("display","none");
		$(".ou-cont").css("display","block");
	})
	$(".manual-upgrade").on("click",function(){
		$(".online-upgrade").removeClass("active")
		$(this).addClass("active");
		$(".ou-cont").css("display","none");
		$(".mu-cont").css("display","block");
	})
	$(".mu-btn-upgrade").on("click",function(){
		$(".sermodule-popup").css("display","none");
		$(".ser-mmu-popu").css("display","block");
	})
	$(".btn-smmu-cancel").on("click",function(){
		$(".ser-mmu-popu").css("display","none");
		$(".sermodule").removeClass("current");
	})

	$(".btn_ma_edit").on("click",function(){
		$(this).css("display","none");
		$(".btn_ma_save").css("display","inline-block");
		$("#ma_edit_name").attr("disabled",false);
	});
	$(".btn_da_edit").on("click",function(){
		$("#div_select_datetime").show();
		$("#div_disable_datetime").hide();		
	}); 	

	/*******
	$("#update_fireware").click(function(){
		$("#div_firewareupdate").css("display","none");
		$("#div_uploadfile").css("display","block");
		//location.href = "<%=request.getContextPath() %>/sysmanage/updateFireware";		
	});
	
	$("#upload_cancel").click(function(){
		$("#div_firewareupdate").css("display","none");
		$("#div_uploadfile").css("display","none");
	});
	*/
	
	// 平台固件
	$("#btn_selectfile").click(function(){
		$("#file_fireware").trigger("click");
	});
	
	$("#file_fireware").live("change",function(){ 
		var path = $("#file_fireware").val();
		$("#upgselectfile").val(path.substring(path.lastIndexOf("\\") + 1));
	}); 
	
	// 平台组件
	$("#btn_selectcomponentfile").click(function(){
		$("#file_component").trigger("click");
	});
	
	$("#file_component").live("change",function(){ 
		var path = $("#file_component").val();
		$("#selectcomponentfile").val(path.substring(path.lastIndexOf("\\") + 1));
	}); 
	
	$(".manu-upgrade").on('click',function(){
		$(".upg-popup").css("display","none");
		$(".upg-mmu-popu").css("display","block");
	})
	$(".btn-upgmmu-cancel").on("click",function(){
		$(".upg-mmu-popu").css("display","none");
		$(".upgrade").removeClass("current");
	})
});

// 固件升级
function saveFormFireware(){
	if(beforeSubmit()){	
		var options = {
			success:function result(data){
				if(data.status == "fail"){
					$.messager.alert("错误", "上传文件失败。");					
				}else{ 
					$.messager.confirm("提示", "上传文件成功。确定要重启WEB服务吗?", function (r){
						if(r){
							$.ajax({
						        type:"POST",
						        url:"<%=request.getContextPath() %>/sysmanage/rebootTomcat",
						        datatype: "json",
						        success:function(data){
						        	var re = eval(data);
					        		$(".upg-popup").css("display","none");
					        		$.messager.alert("系统提示", "已经执行重启WEB服务！", "info");
						        },
						        complete: function(XMLHttpRequest, textStatus){
						        },
						        error: function(){
						        	$(".upg-popup").css("display","none");
						        	$.messager.alert("系统提示", "已经执行重启WEB服务！", "info");
						        }         
						     });
						}
					});	
				}
			},
			dataType:'json',
			timeout:1000000
		};			
		jQuery("#form_upload").ajaxSubmit(options); 
	}
}

function beforeSubmit(){
	var filename = $("#upgselectfile").val();
	if(filename.length == 0){
		$.messager.alert("错误", "还未选择要升级的固件程序。");
		return false;
	}
 	if(filename.indexOf(".war") < 0){
		$.messager.alert("错误", "文件格式不正确，请重新选择。");
		$("#upgselectfile").val("");
		return false;		
	}
	return true;
}

// 组件升级
function saveFormComponent(){
	var filename = $("#selectcomponentfile").val();
	if(filename.length == 0){
		$.messager.alert("错误", "还未选择要升级的组件程序。");
		return false;
	}
 	if(filename.indexOf(".") >= 0){
		$.messager.alert("错误", "文件格式不正确，请重新选择。");
		$("#selectcomponentfile").val("");
		return false;		
	}
	
	var options = {
		success:function result(data){
			if(data.status == "fail"){
				$.messager.alert("错误", "组件程序上传失败。", "error");					
			}else{ 
				$.messager.confirm("提示", "组件程序上传成功。确定要重启组件服务吗?", function (r){
					if(r){
						$.ajax({
					        type:"POST",
					        url:"<%=request.getContextPath() %>/sysmanage/restartComponent",
					        data:{name:filename},
					        datatype: "json",
					        success:function(data){
					        	var re = eval(data);
					        	if(re.status == "success"){
					        		$(".ser-mmu-popu").css("display","none");
					        		$.messager.alert("系统提示", "重启组件：" + filename + "成功！", "info");
					        	}
					        	else{
				        			$.messager.alert("系统提示", "重启组件：" + filename + "失败！", "error");
					        	}
					        },
					        complete: function(XMLHttpRequest, textStatus){
					        },
					        error: function(){
					        	$.messager.alert("系统提示", "重启组件：" + filename + "出错！", "error");
					        }         
					     });
					}
				});	
			}
		},
		dataType:'json',
		timeout:1000000
	};			
	jQuery("#form_upload_component").ajaxSubmit(options);
}
</script>
</body>
</html>