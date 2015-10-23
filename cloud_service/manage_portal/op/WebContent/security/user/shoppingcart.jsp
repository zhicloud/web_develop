<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.UserOrderVO"%>
<%@page import="com.zhicloud.op.vo.ShoppingCartVO"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.vo.DiskPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.BandwidthPackageOptionVO"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.math.BigInteger"%>
<%@page import="com.zhicloud.op.vo.SysDiskImageVO"%>
<%@page import="com.zhicloud.op.vo.MemoryPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.CpuPackageOptionVO"%>
<%@ page import="java.util.*"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	List<ShoppingCartVO> detail = (List<ShoppingCartVO>)request.getAttribute("detail");
    ShoppingCartVO cart = (ShoppingCartVO)request.getAttribute("cart");
    List<CpuPackageOptionVO> cpuOptions = (List<CpuPackageOptionVO>)request.getAttribute("cpuOptions");
    List<MemoryPackageOptionVO> memoryOptions = (List<MemoryPackageOptionVO>)request.getAttribute("memoryOptions");
    Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType); 
	DiskPackageOptionVO diskOption = (DiskPackageOptionVO)request.getAttribute("diskOption");
	BandwidthPackageOptionVO bandwidthOption = (BandwidthPackageOptionVO)request.getAttribute("bandwidthOption");
	List<SysDiskImageVO> sysDiskImageOptions = (List<SysDiskImageVO>)request.getAttribute("sysDiskImageOptions");
	String exsit = (String)request.getAttribute("exsit");
	if(exsit==null){
		exsit = "false";
	}
	BigDecimal i = new BigDecimal("1073741824");
	BigDecimal j = new BigDecimal("1000000");
	BigInteger k = new BigInteger("1073741824");
%>
<!DOCTYPE html>
<!-- shoppingcart.jsp -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<meta http-equiv="Content-Type" content="textml; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
<title>试用 - 云端在线</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css"> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/metro/linkbutton.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/metro/menu.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/metro/menubutton.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 
</head>

<body>
	<div class="page" >
		<div class="pagebox">
			<div class="header">
				<div class="headerbox">
					<div class="headerlogo l">
						<a href="<%=request.getContextPath()%>/"><img src="<%=request.getContextPath()%>/images/logo_green_light_145_29.gif" width="145" height="29" alt="云端在线" /></a>
					</div>
					<%
						if(loginInfo!=null){
					%>
					<div class="headerregister r">
						<a href="javascript:void(0);" id="logout_link">注销</a>
					</div>
					<div class="headerlogin r">
						 <%=loginInfo.getAccount()%> 
					</div>
					<%
						} else{
					%>
					<div class="headerregister r">
						<a href="<%=request.getContextPath()%>/user/register.do">注册</a>
					</div>
					<div class="headerlogin r">
						<a href="<%=request.getContextPath()%>/user.do?url=<%=request.getContextPath()%>/user/buy.do">登录</a>
					</div>
					<%
						}
					%>
				</div>
			</div>
			<div class="nav">
				<div class="navbox">
					<div class="navbutton l">
						<a href="<%=request.getContextPath()%>/">首页</a>
					</div>
					<div class="navsplitter l">&nbsp;</div>
					<div class="navbutton l"  ><a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#productmenu'">产品</a></div>
                    <div class="navsplitter l">&nbsp;</div>
					<div class="navbutton l"  >
						<a href="<%=request.getContextPath()%>/user/buy.do">定制云主机</a> 
					</div> 
					<div class="navcontrol r">
						<a href="<%=request.getContextPath()%>/user.do"><span>我的云端</span></a>
						<input id="url" type="hidden" value="<%=request.getContextPath()%>"/>
					</div>
					
				</div>
				<div id="productmenu" style="width:150px;">
			        <div><a href="<%=request.getContextPath()%>/user/cloudsever.do">云主机</a></div>
			      <div><a href="<%=request.getContextPath()%>/user/cloudstorage.do">云存储</a></div>
			      <div><a href="<%=request.getContextPath()%>/user/banancing.do">负载均衡</a></div>
			      <div><a href="<%=request.getContextPath()%>/user/database.do">云数据库</a></div>
			      <div><a href="<%=request.getContextPath()%>/user/sdn.do">SDN</a></div>
			      </div>
			</div>
			    <div class="main">
		      <div class="fullcontainer">
		        <div class="fulltitlebar">
		          <div class="tab tabactive">购物车</div>
		        </div> 
		        <%
 		        	if(detail==null||detail.size()==0){
 		        %>
		        <div class="fullcontent" id="cart1">
				<div class="fullcenter"><img src="<%=request.getContextPath()%>/images/image_emptycart.gif" width="98" height="134" alt="购物车还没有商品" /><br /><br /><br />
				  立即前往<a href="<%=request.getContextPath()%>/user/buy.do">定制云主机</a><br /><br /><br /><br /></div>
		        </div>
		        <%
		        	}else{
		        %>
		        <div class="fulltable">
		          <form action="account.do" id="cart_form" method="post">
		          <input name="detailPrice" id="detailPrice" type="hidden"/>
		          <input name="ids" id="ids" type="hidden"/>
		          <input name="cartId" id="cartId" type="hidden" value="<%=cart.getId()%>"/>
		          <table class="paytable" cellspacing="0">
		            <tr class="firstrow">
		              <td width="196"><input id="sall" name="getall" checked="checked"   type="checkbox" value="" onchange="getAll(this)" value="" /> <label for="sall">全选</label></td>
		              <td>详细</td>
		              <td width="56">购买数量</td>
		              <td width="56">付费方式</td>
		              <td width="84">价格</td>
		              <td width="16">&nbsp;</td>
		            </tr>
		            <%
		            	for( ShoppingCartVO vo : detail)
		            		               {
		            		            	   boolean flag = true;
		            %>
		            <tr class="evenrow">
		              <td><input   name="configIds" type="checkbox" checked="checked" onchange="checkCheckBox();" id="<%=vo.getPrice() %>" value="<%=vo.getId()%>"/> <label for="s1">  
			              <%
			                  
			               	 if(vo.getSysImageId()!=null&&vo.getSysImageId().length()>0)
								{
									for( SysDiskImageVO sysDiskImageOption : sysDiskImageOptions )
									{
										if(vo.getSysImageId().equals(sysDiskImageOption.getId()))
										{ 
											flag = false; 
											out.println(sysDiskImageOption.getName());
											}
										}
									}else{ 
										out.println("自定义云主机");
										}                 
		                	 
			              %>
			              </label>
		              </td>
		              <td>
		              CPU<%=vo.getCpuCore() %>核 ,内存<%= vo.getMemory().divide(i) %>G,
		              <%
		                 if(flag==true){   
		              %>
		                                                 系统磁盘<%=vo.getSysDisk().divide(i)%>G
		              <%
		                 }
		              %> 
		                                                         数据磁盘 <%= vo.getDataDisk().divide(i) %>G,网络带宽<%= vo.getBandwidth().divide(j) %>M
		             </td>
		              <td>1台</td>
		              <td>
		              <%
						 if(1==vo.getType()){
							 out.println("包月付费");
						 }else if(2==vo.getType()){
							 out.println("按量付费");
						 }else if(3==vo.getType()){
							 out.println("试用");
						 }
					  %>                                     
		             </td>
		              <td><b>
		              <%
						 if(1==vo.getType()){
							 out.println(vo.getPrice());
						 }else if(2==vo.getType()){
							 out.println("<b>0.8</b>元/GB");
						 }else if(3==vo.getType()){
							 out.println(vo.getPrice());
						 }
					  %>
		              </b></td>
		              <td class="remove" width="16"><a href="javascript:void(0);" onclick="deleteDetail('<%=vo.getId()%>','<%=vo.getPrice() %>');"><img src="<%=request.getContextPath()%>/images/button_remove_gray_16_16.png" width="16" height="16" alt="删除" /></a></td>
		            </tr>
		            <%
		              } 
		            %> 
		            <tr class="lastrow">
		              <td colspan="2" class="tools"><input id="sall2" checked="checked" name="getall" type="checkbox" value="" onchange="getAll(this)" /> <label for="sall2">全选</label><a id="removebutton" class="removebutton" href="javascript:void(0);" onclick="deleteDetail();">删除</a> </td>
		              <td colspan="2" class="total">已选择<b id="sizecount"><%=detail.size() %></b>台云主机</td>
		              <td colspan="2" class="total">合计：<b id="totalPrice"><%=cart.getTotalPrice() %></b>元</td>
		            </tr>
		          </table>
                  <input type="button" id="account" class="cartbutton" onclick="goAccount();" value="结　算"/><div class="clear">&nbsp;</div>
		          </form>
		        </div>
		       <%} %>
		      </div>
    </div>
  </div>
  <div class="pagefooter">
    <div class="footer">
      <div class="footerbox">
        <div class="footercopyright">Copyright &copy; 2014 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1</div>
      </div>
    </div>
  </div> 
	</div>
</body>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/big.min.js"></script>
<script type="text/javascript">
window.name = "selfWin";

var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do");
ajax.async = false;
var _terminal_user_add_dlg_scope_ = new function(){
	
	var self = this;
	 
	
	// 保存
	self.save = function() {
		var sysImageId = $("#sysImageId").val();
		if(sysImageId==""){
			$.messager.alert('警告','请选择镜像','warning');
		}else{
			
			var formData = $.formToBean(cart_form);
			ajax.remoteCall("bean://paymentService:addCart", 
				[ formData ],
				function(reply) {
					if (reply.status == "exception") {
						$.messager.alert('警告',reply.exceptionMessage,'warning');
					} else if (reply.result.status == "success") {
						// 注册成功
						$("<div class=\"datagrid-mask\"></div>").css({
							display:"block",
							width:"100%",
							height:"100%"
						}).appendTo("body"); 
						$("<div class=\"datagrid-mask-msg\"></div>").html("添加成功").appendTo("body").css({
							display:"block",
							left:($(document.body).outerWidth(true) - 190) / 2,
							top:($(window).height() - 45) / 2
						});
						// 跳转页面
						window.setTimeout(function(){
							window.location.href="<%=request.getContextPath()%>/user/buy.do"; 
						}, 500);
					} else {
						$.messager.alert('警告',reply.result.message,'warning');
					}
				}
			);
		}
	};
	

	// 关闭
	self.close = function() {
		$("#terminal_user_add_dlg").dialog("close");
	};

	//--------------------------
	
	// 初始化
	$(document).ready(function(){
		// 保存
		$("#put_into_cart").click(function() {
			self.save();
		}); 
		$("#toLogin").click(function() { 
			var url = encodeURIComponent($("#url").val()+"/user/buy.do"); 
			window.location.href="<%=request.getContextPath()%>/user.do?url="+url;
		}); 
		// 注销
		$("#logout_link").click(function(){
			logout();
		}); 
	});
	$(document).ready(function(){
		$(".paytable td").mouseover(function(){
			$(this).parent().addClass("selectedrow");
			$(this).parent().find(".remove").find("img").attr("src","<%=request.getContextPath()%>/images/button_remove_red_16_16.png");
		});
		$(".paytable td").mouseout(function(){
			$(this).parent().removeClass("selectedrow");
			$(this).parent().find(".remove").find("img").attr("src","<%=request.getContextPath()%>/images/button_remove_gray_16_16.png");
		});
	}); 
};

	// 结算
	function goAccount() { 
		checkCheckBox();
			$("#cart_form").submit(); 
	}
	function checkCheckBox(){
		var check=document.getElementsByName("configIds");
        var flag = 1;
        var count = 0;
        var totalPrice = Number(0.00);
        var ids = "";
		for(var i=0;i<check.length;i++){
           
		  if(check[i].checked==true){
			  ids = ids+check[i].value+",";
			  count = count+1;
		     flag = 2;
		     totalPrice = Number(totalPrice)+Number(check[i].id);

		    }else{
		    	
		   var getall=document.getElementsByName("getall");
		   
			for(var i=0;i<getall.length;i++){
	           
				getall[i].checked = false ;
			}
		    }
		}
		$("#totalPrice").empty(); 
		$("#totalPrice").append(totalPrice.toFixed(2));
		$("#sizecount").empty(); 
		$("#sizecount").append(count);
		$("#ids").val(ids);
		
		if(flag==1){
			$("#account").attr("class","cartbutton cartbuttondisabled");
			$("#account").attr("disabled","disabled");
			$("#removebutton").attr("class","removebutton disabled");
			$("#removebutton").attr("disabled","disabled");
		}else{ 
			$("#account").attr("class","cartbutton");
			$("#account").attr("disabled","disabled");
			$("#removebutton").attr("class","removebutton");
			$("#removebutton").attr("disabled","disabled");
			
			//
		}
		
	}
   function getAll(self){
	   var getall=document.getElementsByName("getall");
	   
		for(var i=0;i<getall.length;i++){
           
			getall[i].checked = self.checked ;
		}
	   if( self.checked==true){
			var check=document.getElementsByName("configIds"); 
			for(var i=0;i<check.length;i++){
	           
			   check[i].checked = true ;
			}
		   
	   }else{
			var check=document.getElementsByName("configIds"); 
			for(var i=0;i<check.length;i++){
	           
			   check[i].checked = false ;
			}
		   
	   }
	   checkCheckBox();
   }
   function deleteDetail(id,price){
	   top.$.messager.confirm("确认", "确定要删除吗?", function (r) {  
	        if (r) {    
				   if(id==""){
					   checkCheckBox();
					   var check=document.getElementsByName("configIds"); 
				       var totalPrice = Number(0.00);
						for(var i=0;i<check.length;i++){
				          
						  if(check[i].checked==true){ 
						     totalPrice = Number(totalPrice)+Number(check[i].id);
				
						    }
						} 
					   
				  		$("#detailPrice").val(totalPrice);
				   }else{
				  		$("#detailPrice").val(price);
				  		$("#ids").val(id);
					   
				   }
				   var formData = $.formToBean(cart_form);
				   ajax.remoteCall("bean://paymentService:deleteDetailAndConfig", 
							[ formData],
							function(reply) {
								if (reply.status == "exception") {
									$.messager.alert('警告',reply.exceptionMessage,'warning');
								} else if (reply.result.status == "success") { 
									window.location.href="<%=request.getContextPath()%>/user/shoppingcart.do";  						 
								} else {
									$.messager.alert('警告',reply.result.message,'warning');
								}
							}
						);
	        }  
	    }); 
   } 
// 注销
   function logout(){
	ajax.remoteCall("bean://sysUserService:logout4",
			[],
			function(reply)
			{
				if( reply.status=="exception" )
				{
					$.messager.alert('警告',reply.exceptionMessage,'warning');
				} 
				else if( reply.result.status=="success" )
				{
					// 登录成功
					$("<div class=\"datagrid-mask\"></div>").css({
						display:"block",
						width:"100%",
						height:"100%"
					}).appendTo("body"); 
					$("<div class=\"datagrid-mask-msg\"></div>").html("成功退出，正在跳转页面。。。").appendTo("body").css({
						display:"block",
						left:($(document.body).outerWidth(true) - 190) / 2,
						top:($(window).height() - 45) / 2
					});
					// 跳转页面
					window.setTimeout(function(){
						top.location.href = "<%=request.getContextPath()%>/user.do";
					}, 500);
				}
				else
				{
					$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	}
	function addPort(){
		var port = $("#port").val();
		var flag = true;
		$('*[name="ports"]').each(function(){
			 if($(this).val()==port){
					$.messager.alert('警告','端口已存在','warning'); 
					flag = false;
				 
			 }
		});
		if(flag==true){
			
			$("#showports").append("<a href='javascript:void(0);' onclick='deletePort($(this));'>"+port+ "</a>");
			var val = $("#ports").val();
		    val = val + port+",";
		    $("#ports").val(val);
			$("#port").val(""); 
		}
	} 
	
	function deletePort(self){
		var port = self.text();
		self.remove();
		var ports = $("#ports").val(); 
		ports = ports.replace(port+",","");
		$("#ports").val(ports);
	}
</script>
</html>
