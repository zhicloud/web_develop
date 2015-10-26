<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.UserOrderVO"%>
<%@page import="com.zhicloud.op.vo.ShoppingCartVO"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.vo.DiskPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.BandwidthPackageOptionVO"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.zhicloud.op.vo.SysDiskImageVO"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
    LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	List<ShoppingCartVO> detail = (List<ShoppingCartVO>)request.getAttribute("detail");
    UserOrderVO order = (UserOrderVO)request.getAttribute("order");
	List<ShoppingCartVO> orderdetail = (List<ShoppingCartVO>)request.getAttribute("orderdetail");
	ShoppingCartVO cart = (ShoppingCartVO)request.getAttribute("cart");
	DiskPackageOptionVO diskOption = (DiskPackageOptionVO)request.getAttribute("diskOption");
	BandwidthPackageOptionVO bandwidthOption = (BandwidthPackageOptionVO)request.getAttribute("bandwidthOption");
	List<SysDiskImageVO> sysDiskImageOptions = (List<SysDiskImageVO>)request.getAttribute("sysDiskImageOptions");
%>
<!DOCTYPE html>
<!-- pay.jsp -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<meta http-equiv="Content-Type" content="textml; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
<title>结算 - 云端在线</title> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script> 
<script type="text/javascript">
<!--
$(document).ready(function(){

});
-->
</script>
</head>

<body>
<div class="page">
  <div class="pagebox">
    <div class="header">
      <div class="headerbox">
        <div class="headerlogo l"><a href="#"><img src="<%=request.getContextPath()%>/images/logo_green_light_145_29.gif" width="145" height="29" alt="云端在线" /></a></div>
        <div class="headerlogout r"><a href="#" id="logout_link">注销</a></div>
        <div class="headerusername r"><a href="<%=request.getContextPath()%>/user.do"><%=loginInfo.getAccount()%></a></div>
      </div>
    </div>
    <div class="nav">
      <div class="navbox">
        <div class="navbutton l"><a href="<%=request.getContextPath()%>/">首页</a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l"><a href="#">定制云主机</a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l"><a href="#">相关下载</a></div>
        <div class="navcontrol r"><a href="#"><span>我的云端</span></a></div>
      </div>
    </div>
    <div class="main">
      <div class="fullcontainer">
        <div class="fulltitlebar">
          <div class="tab tabactive">订　　单</div>
        </div>
        <div class="fulltable">
          <table class="paytable" cellspacing="0">
            <tr class="firstrow">
              <td width="196">云主机</td>
              <td>详细</td>
              <td width="56">购买数量</td>
              <td width="56">付费方式</td>
              <td width="84">价格</td>
            </tr>
            <%
            	for( ShoppingCartVO vo : orderdetail)
                           {
                        	   boolean flag = true;
            %>
            <tr class="evenrow">
              <td>
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
                	 BigDecimal i = new BigDecimal("1073741824");
                	 BigDecimal j = new BigDecimal("1000000");
                	 
	              %>
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
            </tr>
            <%
              } 
            %>
            <tr class="lastrow">
             <td colspan="5">合计：<b><%=order.getTotalPrice() %></b>元</td>
           </tr>
            
          </table>
          <form id="alipay" action="<%=request.getContextPath()%>/security/user/alipayapi.jsp?userType=4" method="post" target="_blank">
          <input name="WIDseller_email" value="mali@zhicloud.com" type="hidden"/>
          <input name="WIDout_trade_no" value="<%=order.getId() %>" type="hidden"/>
          <input name="WIDsubject" value="云主机" type="hidden"/>
          <input name="WIDtotal_fee" value="<%=order.getTotalPrice() %>" type="hidden"/>
          </form>
          <form id="unionpay" action="<%=request.getContextPath()%>/security/user/unionpayapi.jsp?userType=4" method="post" target="_blank">
          <input name="WIDseller_email" value="mali@zhicloud.com" type="hidden"/>
          <input name="WIDout_trade_no" value="<%=order.getId() %>" type="hidden"/>
          <input name="WIDsubject" value="云主机" type="hidden"/>
          <input name="WIDtotal_fee" value="<%=order.getTotalPrice() %>" type="hidden"/>
          </form>
        </div>
      </div>
      <div class="fullcontainer">
        <div class="fulltitlebar">
          <div class="tab tabactive">选择支付方式</div>
        </div>
        <div class="fullcontent paymethod">
          <div class="l">
            <input id="pay_unionpay" name="paymethod" type="radio" value="1" />
            <label for="pay_unionpay"><img src="<%=request.getContextPath()%>/images/logo_unionpay.gif" width="96" height="60" alt="银联" /></label>
          </div>
          <div class="l">
            <input id="pay_alipay" name="paymethod" type="radio" value="2" />
            <label for="pay_alipay"><img src="<%=request.getContextPath()%>/images/logo_alipay.gif" width="138" height="40" alt="支付宝" /></label>
          </div>
          <div class="tip r"><img src="<%=request.getContextPath()%>/images/button_caution_red_24_24.gif" width="24" height="24" alt="注意"/>请选择支付方式</div>
        </div>
      </div><input type="button" class="paybutton" onclick="gotoPay();" value="支付订单"/><div class="clear">&nbsp;</div>
    </div>
  </div>
  <div class="pagefooter">
    <div class="sitemap">
      <div class="sitemapbox">
        <div class="sitemapsocial">
          <div class="sitemaplogo l"><a href="#"><img src="<%=request.getContextPath()%>/images/logo_green_dark_40_29.gif" width="40" height="29" alt="云端在线" /></a></div>
          <div class="sitemapweixin r"><a href="#">微信扫二维码<br />
            关注云端在线</a></div>
          <div class="sitemapweibo r"><a href="#">新浪<br />
            微博</a></div>
        </div>
        <ul>
          <li><a href="<%=request.getContextPath()%>/">首页</a></li>
        </ul>
        <ul>
          <li><a href="#">定制云主机</a></li>
          <li><a href="#">试用</a></li>
          <li><a href="#">包月试用</a></li>
          <li><a href="#">按量付费</a></li>
        </ul>
        <ul>
          <li><a href="#">相关下载</a></li>
        </ul>
        <ul>
          <li><a href="#">我的云端</a></li>
          <li><a href="#">我的云主机</a></li>
          <li><a href="#">财务中心</a></li>
          <li><a href="#">用户中心</a></li>
        </ul>
        <ul>
          <li><a href="#">账户注册</a></li>
          <li><a href="#">账户登录</a></li>
        </ul>
        <ul>
          <li><a href="#">关于我们</a></li>
          <li><a href="#">联系我们</a></li>
        </ul>
      </div>
    </div>
    <div class="footer">
      <div class="footerbox">
        <div class="footercopyright l">Copyright &copy; 2014 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.</div>
        <div class="footercopyright r">蜀ICP备14004217号-1</div>
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

var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false; 
var self = this; 

 function gotoPay(){  
	 var val=$('input:radio[name="paymethod"]:checked').val();
	 if(val == null){
		 alert("请选择支付方式！");
	 }else if(val == '1'){
	 $("#unionpay").submit();
		 
	 }else if(val == '2'){		 
	 $("#alipay").submit();
	 }
 } 
 var _terminal_user_add_dlg_scope_ = new function(){ 
		
		// 初始化
		$(document).ready(function(){ 
			// 注销
			$("#logout_link").click(function(){
				logout();
			});
		});
	}; 
	// 注销
	   function logout(){
		ajax.remoteCall("bean://sysUserService:logout",
				[],
				function(reply)
				{
					if( reply.status=="exception" )
					{
						top.$.messager.alert('警告',reply.exceptionMessage,'warning');
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
						top.$.messager.alert('警告',reply.result.message,'warning');
					}
				}
			);
		}
 
</script>
</html>
