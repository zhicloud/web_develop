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
    String balance = (String)request.getAttribute("balance");
    String amount = (String)request.getAttribute("amount");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/global.css" media="all"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/user/css/mycloud.css" media="all"/>
 
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=<%=userType%>");
var amount = '<%=amount%>';
$(document).ready(function(){
	init(10,5);
	inituser("<%=loginInfo.getAccount()%>",0);
	$("#toPay").click(function(){
		gotoPay();
	});
	$("#base_info").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=accountBalanceService&method=toConsumptionRecordPage";
	});
	$("#recharge").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=accountBalanceService&method=toRechargePage";
	});
	$("#invoice_manage").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=invoiceService&method=managePage";
	});
	$("#cash_coupon").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=cashCouponService&method=cashCouponPage";
	});
	$("input[name='total_fee']").click(function(){ 
		$("#re").next("label").html("其他金额");
		$("#re").next("label").removeClass("checked");
		$(this).next("label").addClass("checked"); 
		var gift =parseInt($(this).val()/1000);
		if(gift<=0){
			$("#info").html("充值"+$(this).val()+"元");
		}else{
			$("#info").html("充值"+$(this).val()+"元，赠送"+parseInt($(this).val()/1000)*200+"元");
			
		}
	});
	$("#re").click(function(){
		$("#re1").removeAttr("checked");
		$("#re1").next("label").removeClass("checked");
		$("#re2").removeAttr("checked");
		$("#re2").next("label").removeClass("checked");
		$("#re3").removeAttr("checked");
		$("#re3").next("label").removeClass("checked");
		$("#re4").removeAttr("checked");
		$("#re4").next("label").removeClass("checked");
		$("#re5").removeAttr("checked");
		$("#re5").next("label").removeClass("checked");
	});
	$("#re").blur(function(){
		if($(this).val()!=''){	
			if(checkMoney()){				
				$("#rh1").next("label").removeClass("checked");
				$("#rh2").next("label").removeClass("checked");
				$("#rh3").next("label").removeClass("checked");  
				$("#rh").next("label").addClass("checked");
				var gift =parseInt($(this).val()/1000);
				if(gift<=0){
					$("#info").html("充值"+$(this).val()+"元");
				}else{
					$("#info").html("充值"+$(this).val()+"元，赠送"+parseInt($(this).val()/1000)*200+"元");
					
				}
			}
		}else{
			$("#re1").click();
			$("#re1").next("label").addClass("checked");
		}
		
	}); 
	if(amount != ''){
		
		if(amount == '200' || amount == '500' || amount == '1000' || amount == '2000' || amount == '5000'){
			$("input[name=total_fee][value = "+amount+"]").click();
			$("input[name=total_fee][value = "+amount+"]").next("label").addClass("checked");
		}else{
			$("#re").val(amount);
			$("#re").next("label").addClass("checked");
			$("#re").click();
			$("#re").blur();
		}
	}
});

function checkMoney(){  
	if($("#re").next("label").attr("class").indexOf('checked')>0){
		var patrn=/^\d+$/;
		if(!(patrn.test($("#re").val()))){
			top.$.messager.alert('警告','请输入整数','warning'); 
			return false;
		}else if($("#re").val()<10){
			top.$.messager.alert('警告','充值金额应大于10','warning'); 
			return false;
			
		}else if($("#re").val()>50000){
			top.$.messager.alert('警告','充值金额应小于50000','warning');	 
			return false;
		}else{
			return true;
		} 
		
	}else{
		return true;
	}
} 

var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");


 function gotoPay(){  
	 var val=$('input:radio[name="payType"]:checked').val();
	 if(val == null){
		 top.$.messager.alert('警告','请选择支付方式！','warning');
	 } else{
		 if(checkMoney()){
			 
		  $("#pay_form").submit();
		 }
	 }
	 
 }
 function getLoginInfo(name,message,userId){
		slideright();
		inituser(name,message);
		window.location.reload();
		
	}
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
			<div class="titlebar"><div class="tabbar l"><a href="javascript:void(0);" id="base_info">基本信息</a>　｜　<a href="javascript:void(0);" class="active" id="recharge">账户充值</a>　｜　<a href="javascript:void(0);" id="cash_coupon">现金券</a>　｜　<a href="javascript:void(0);" id="invoice_manage">发票管理</a></div></div>
			<form id="pay_form" action="<%=request.getContextPath()%>/bean/page.do" method="post"  target="_blank">
			<div class="box" style="padding:60px 0 120px;overflow:hidden;">
			<div class="l" style="width:198px; height:197px; margin-right:30px;"><img src="<%=request.getContextPath()%>/image/recharge.png" width="198" height="197" alt=" " /></div>
    				<div class="l" style="width:492px;overflow:hidden;">
					<div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 25px 0">余额</span></div>
					<div class="l" style="width:372px; margin:0 0 25px 0; line-height:30px"><%=balance %></>元</div>
					<div class="l" style="width:115px;"><span class="bluebutton" style="width:105px;margin:0 10px 25px 0">充值金额</span></div>
         				<div class="l" style="width:377px; margin:0 0 10px 0;">
						<div class="l" style="width:377px; margin:0 0 10px 0;">
							<input name="total_fee" id="re1" class="radio" type="radio" value="200" checked="checked" style="width:53px"/>
							<label for="re1" class="smalllabel l" style="width:53px">200元</label>
							<input name="total_fee" id="re2" class="radio" type="radio" value="500"  style="width:53px"/>
							<label for="re2" class="smalllabel l" style="width:53px">500元</label>
							<input name="total_fee" id="re3" class="radio" type="radio" value="1000"  style="width:53px"/>
							<label for="re3" class="smalllabel l" style="width:53px">1000元</label>
							<input name="total_fee" id="re4" class="radio" type="radio" value="2000"  style="width:54px"/>
							<label for="re4" class="smalllabel l" style="width:54px">2000元</label>
							<input name="total_fee" id="re5" class="radio" type="radio" value="5000"  style="width:54px"/>
							<label for="re5" class="smalllabel l" style="width:54px">5000元</label>
							<div class="custom smalllabel l" style="border:none; margin:0; width:68px">
								<input onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')" maxlength="5" id="re" name="total_fee_diy" type="text" class="smalllabel" title="元" style="width:68px"/>
								<label for="re" class="smalllabel l" style="width:68px">其他金额</label>
							</div>
						</div>
						<input name="email" value="mali@zhicloud.com" type="hidden"/> 
						<input name="WIDsubject" value="云主机" type="hidden"/> 
						<input name="userId" value="<%=loginInfo.getUserId()%>" type="hidden"/> 
						<input name="userType" value="4" type="hidden"/>
						<input name="bean" value="paymentService" type="hidden"/>
						<input name="method" value="toAccountPage" type="hidden"/> 
					</div>
         				<div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 27px 0">充值方式</span></div>
					<div class="l" style="width:372px; margin:0 0 25px 0; line-height:30px">
						<input checked="checked" id="pm1" name="payType" type="radio" value="1" style="vertical-align:middle" />
						<label for="pm1"><img src="<%=request.getContextPath()%>/image/alipay.png" width="70" height="30" alt="支付宝"  style="vertical-align:middle" /></label>
						<input id="pm2" name="payType" type="radio" value="2" style="vertical-align:middle" />
						<label for="pm2"><img src="<%=request.getContextPath()%>/image/yinlian.png" width="60" height="30" alt="银联"  style="vertical-align:middle" /></label>
					</div>
	          		<a href="javascript:void(0);" class="bluelinebutton r" id="toPay">前往支付</a>
	          		<div class="r" style="line-height:30px;padding-right:15px"><b id="info">充值200元</b></div>
        			</div>
      		</div> 
          	</form>
			<div class="clear">&nbsp;</div>
		</div>
		<jsp:include page="/src/common/tpl/u_footer.jsp"></jsp:include>
	</div>
	<div class="pageright">
		<iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
		<iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
	</div>
</div>
<!-- JavaScript -->
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/goup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/common.js"></script>
<script  type="text/javascript">
$(function(){
	navHighlight("umc","uma");
})
</script>
</body>
</html>