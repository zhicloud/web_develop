<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.vo.UserOrderVO"%> 
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	List<UserOrderVO> detail = (List<UserOrderVO>)request.getAttribute("detail"); 
	String orderId = (String)request.getAttribute("orderId"); 
	String createTime = (String)request.getAttribute("createTime");
	String isPaid = (String)request.getAttribute("isPaid");
	String totalPrice = (String)request.getAttribute("totalPrice");
	BigDecimal i = new BigDecimal("1073741824");
	BigDecimal k = new BigDecimal("1000000");
%>

<div id="cloud_host_view_detail_dlg_container">
	<div id="cloud_host_view_detail_dlg" class="easyui-dialog" title="订单详情"
		style="width:700px; height:550px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			modal: true,
			onMove:_cloud_host_view_detail_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _cloud_host_view_detail_dlg_scope_;
			}
		">
		<form id="cloud_host_view_detail_dlg_form" method="post">
		
			<table border="0" style="width:90%; margin:0 auto 0 auto;">
			<tr>
			<td>订购时间：<%=createTime%></td>
			<td>订单编号：<%=orderId%></td>
			<td>总价：<%=totalPrice%>元</td>
			</tr>
			</table> <hr/>
			
			<%
							for( UserOrderVO vo : detail)
												{
						%>
			<table style="width:95%; margin:0 auto 0 auto;">
				<tr>
 					<td style="padding:4px;">付费方式：
 					<%
 					if(vo.getType()==1)
 					{out.println("包月使用");
 					%>
 					<td style="padding:4px;">订购时段：<%=vo.getDuration() %>个月</td>
 					<td style="padding:4px;">总价：<%=vo.getPrice()%>元</td>
 					<td style="padding:4px;">状态：  <%=isPaid %></td>
 					<%
 					}
 					else if(vo.getType()==2){
 						out.println("按量付费");
 					}else if(vo.getType()==3){
 						out.println("试用");
 					%>
 					<%-- <td style="padding:4px;">试用时段：<%=vo.getDuration()/12 %>年</td> --%>
 					<td style="padding:4px;">总价：<%=vo.getPrice()%>元</td>
 					<%-- <td style="padding:4px;">状态：  <%=isPaid %></td> --%>
 					<%
 					}
 					%>
 					</td>
 					 <td style="padding:4px;">镜像：  <%=vo.getName() %></td>  
				</tr>
				<tr>
 					<td style="padding:4px;">CPU：<%=vo.getCpuCore()%>核</td>
 					<td style="padding:4px;">内存：<%=vo.getMemory().divide(i)%>G</td>
 					<td style="padding:4px;">系统磁盘：<%=vo.getSysDisk().divide(i)%>G</td>
 					<td style="padding:4px;">数据磁盘：<%=vo.getDataDisk().divide(i)%>G</td>
 					<td style="padding:4px;">带宽： <%=vo.getBandwidth().divide(k)%>Mbps</td>
				</tr>
				 
				
			</table> <hr/>
							<%
								}
							%>
			
		</form>
	</div>

</div>


<script type="text/javascript">

var _cloud_host_view_detail_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#cloud_host_view_detail_dlg";
		var topValue = $(thisId).offset().top;
		var leftValue = $(thisId).offset().left;
		if(topValue==0){
			topValue = 30;
		}
		if(topValue<30){
			$(thisId).dialog('move',{
				left:leftValue,
				top:30
			});
			return;
		}
		if(leftValue>1315){
			$(thisId).dialog('move',{
				left:1300,
				top:topValue
			});
			return;
		}
		if(topValue>600){
			$(thisId).dialog('move',{
				left:leftValue,
				top:570
			});
			return;
		}
	};
	// 保存
	self.save = function() {
		var formData = $.formToBean(cloud_host_view_detail_dlg_form);
		ajax.remoteCall("bean://cloudHostService:addCloudHost", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					var data = $("#cloud_host_view_detail_dlg_container").parent().prop("_data_");
					$("#cloud_host_view_detail_dlg").dialog("close");
					data.onClose(reply.result);
				} else {
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};

	// 关闭
	self.close = function() {
		$("#cloud_host_view_detail_dlg").dialog("close");
	};

	//--------------------------
	
	$(document).ready(function() {
		// “从镜像创建”、“空白系统”的选择切换
		$(':radio[name=check1]').change(function() {
			var checkval = $(this).val();
			$('#disk1').prop('disabled', checkval == '1');
			if(checkval == 1) {
				$('#disk2').slider("enable");
			}else{
				$('#disk2').slider("disable");
			}
		});
		// 是否使用数据磁盘
		$(':checkbox[name=check2]').change(function() {
			var checkval = $(this).prop("checked");
			if(checkval == true) {
				$('#disk3').slider("enable");
			}else{
				$('#disk3').slider("disable");
			}
		});
		// 保存
		$("#cloud_host_view_detail_dlg_save_btn").click(function() {
			self.save();
		});
		// 关闭
		$("#cloud_host_view_detail_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
	
	
</script>



