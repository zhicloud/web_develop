<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.InvoiceAddressVO"%>
<%@page import="com.zhicloud.op.vo.AccountBalanceDetailVO"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	String userId  = loginInfo.getUserId();
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	List<InvoiceAddressVO> invoiceAddressList = (List<InvoiceAddressVO>)request.getAttribute("invoice_address_list");
	List<AccountBalanceDetailVO> accountBalanceDetailList = (List<AccountBalanceDetailVO>)request.getAttribute("account_balance_detail_list");

%>


<!--invoice_add.jsp -->

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" /> 
 
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>

<script type="text/javascript">

var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
ajax.async = false;
var data = new Array();
var amount = 0;
var ids = new Array();
var myradio=document.getElementsByName("rh");
var myCheckbox = document.getElementsByName("amount");
var invoiceTitleFlag = false;
var addressFlag = false;
var recipientsFlag = false;
var phoneFlag = false;

function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.reload();
}

function checkSum(){
	amount = 0;
	for(i = 0;i<myCheckbox.length; i++) {
		if(myCheckbox[i].checked){
			amount = numAdd(amount, myCheckbox[i].value); 
		}
	}
	$(".amount_sum").html(amount); 
}

function numAdd(num1, num2) {
    var baseNum, baseNum1, baseNum2;
    try {
        baseNum1 = num1.toString().split(".")[1].length;
    } catch (e) {
        baseNum1 = 0;
    }
    try {
        baseNum2 = num2.toString().split(".")[1].length;
    } catch (e) {
        baseNum2 = 0;
    }
    baseNum = Math.pow(10, Math.max(baseNum1, baseNum2));
    return (numMulti(num1,baseNum) + numMulti(num2,baseNum)) / baseNum;
};

function numMulti(num1, num2) {
    var baseNum = 0;
    try {
        baseNum += num1.toString().split(".")[1].length;
    } catch (e) {
    }
    try {
        baseNum += num2.toString().split(".")[1].length;
    } catch (e) {
    }
    return Number(num1.toString().replace(".", "")) * Number(num2.toString().replace(".", "")) / Math.pow(10, baseNum);
};

function radioShow(){
	 var div=document.getElementById("c").getElementsByTagName("div");
	 for(i=0;i<div.length;i++){
	  if(myradio[i].checked){
	   div[i].style.display="block";
	   var child =  div[i].getElementsByTagName("input");
	   data[0] = child[0].value;
	   data[1] = child[1].value;
	   data[2] = child[2].value;
	   data[3] = child[3].value;
	   data[4] = child[4].value;
	   data[5] = child[5].value;
	   }
	   else{
	   div[i].style.display="none";
	   }
	 }
}

function check(){ 
	for(i=0;i<myCheckbox.length;i++){ 
		if(myCheckbox[i].checked){ 
			if(amount < 100){
				$("#next").attr("disabled",true); 
				return; 
			}
			$("#next").attr("disabled",false); 
			return; 
		} 
	} 
// 	window.alert('一个也没有选！'); 	
		$("#next").attr("disabled",true); 
	
} 

function submitDisabled(){
	$("#add_invoice_dlg_submit_btn").attr("disabled",true);
}

function submitEnabled(){
	$("#add_invoice_dlg_submit_btn").attr("disabled",false);
}

function insertInvoiceTitle(){
	data[2] = document.getElementById("invoiceTitle").value;
}

function insertAddress(){
	data[3] = document.getElementById("address").value;
}

function insertRecipients(){
	data[4] = document.getElementById("recipients").value;
}


function insertPhone(){
	data[5] = document.getElementById("phone").value;
}

function checkInvoiceTitle(){
	var invoiceTitle = data[2];
	if(invoiceTitle==null || invoiceTitle==""){
		$("#tip-invoiceTitle").html("<font color=red>发票抬头不能为空</font>");
		invoiceTitleFlag = false;
		return false;
	}
	if(invoiceTitle.length > 50){
		$("#tip-invoiceTitle").html("<font color=red>发票抬头长度不能超过50个字符</font>");
		invoiceTitleFlag = false;
		return false;
	}
	$("#tip-invoiceTitle").html("");
	invoiceTitleFlag = true;
	return true;
}

function checkAddress(){
	var address = data[3];
	if(address==null || address==""){
		$("#tip-address").html("<font color=red>寄送地址不能为空</font>");
		addressFlag = false;
		return false;
	}
	if(address.length > 50){
		$("#tip-address").html("<font color=red>寄送地址长度不能超过50个字符</font>");
		addressFlag = false;
		return false;
	}
	$("#tip-address").html("");
	addressFlag = true;
	return true;
}

function checkRecipients(){
	var recipients = data[4];
	if(recipients==null || recipients==""){
		$("#tip-recipients").html("<font color=red>收件人不能为空</font>");
		recipientsFlag = false;
		return false;
	}
	if(recipients.length > 10){
		$("#tip-recipients").html("<font color=red>收件人长度不能超过10个字符</font>");
		recipientsFlag = false;
		return false;
	}
	$("#tip-recipients").html("");
	recipientsFlag = true;
	return true;
}

function checkPhone(){
	var phone = data[5];
	if(phone==null || phone==""){
		$("#tip-phone").html("<font color=red>联系电话不能为空</font>");
		phoneFlag = false;
		return false;
	}
 	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))) { 
		if(!(/^((0\d{2,3})-)(\d{7,8})(-(\d{3,}))?$/.test(phone))) {
	 		$("#tip-phone").html("<font color=red>请输入正确的电话号码(固话：区号-电话号码)</font>");
			phoneFlag = false;
			return false;
	 	}
	}
 	
	$("#tip-phone").html("");
	phoneFlag = true;
	return true;
}

function submitCheck(){
	if(invoiceTitleFlag && addressFlag && recipientsFlag && phoneFlag) {
		$("#add_invoice_dlg_submit_btn").attr("disabled",false); 
	}else {
		$("#add_invoice_dlg_submit_btn").attr("disabled",true);	
	}
}

function go_to_page(page_num){
	var show_per_page = parseInt($('#show_per_page').val());
	start_from = page_num * show_per_page;
	end_on = start_from + show_per_page;
	$('#receiptselect').children().css('display', 'none').slice(start_from, end_on).css('display', 'block');
	$('.num[longdesc=' + page_num +']').addClass('active').siblings('.active').removeClass('active');
	$('#current_page').val(page_num);
	if(page_num<=0){
		$('.pagebar .page .arrow:first-child img').attr("src","<%=request.getContextPath()%>/image/button_prev_disabled.gif");
		$('.pagebar .page .arrow:last-child img').attr("src","<%=request.getContextPath()%>/image/button_next.gif");
	}else if (parseInt($('#number_of_pages').val())-1 <= page_num){
		$('.pagebar .page .arrow:first-child img').attr("src","<%=request.getContextPath()%>/image/button_prev.gif");
		$('.pagebar .page .arrow:last-child img').attr("src","<%=request.getContextPath()%>/image/button_next_disabled.gif")
	}else{
		$('.pagebar .page .arrow:first-child img').attr("src","<%=request.getContextPath()%>/image/button_prev.gif");
		$('.pagebar .page .arrow:last-child img').attr("src","<%=request.getContextPath()%>/image/button_next.gif")
	}
}	

function previous(){
	new_page = parseInt($('#current_page').val()) - 1;
	if($('.pagebar .page .active').prev('.num').length==true){
		go_to_page(new_page);
	}
}
function next(){
	new_page = parseInt($('#current_page').val()) + 1;
	//if there is an item after the current active link run the function
	if($('.pagebar .page .active').next('.num').length==true){
		go_to_page(new_page);
	}
}



var _add_invoice_dlg_scope_ = new function(){

var self = this;

$(document).ready(function() {
	
	check();
	
	myradio[0].checked = true;
	
	if(<%=invoiceAddressList.size()%> > 0) {
		$("#add_invoice_dlg_submit_btn").attr("disabled",false); 
	} else {
		$("#add_invoice_dlg_submit_btn").attr("disabled",true); 
	}
	
	radioShow();
	
	init(10,4); 
	if(name!= ''){ 
	   inituser(name,0);
	}else{
		
	   inituser();
	}   
	initstep(1);
	
	// 提交
	$("#add_invoice_dlg_submit_btn").click(function() {
		self.save();
	});
	// 关闭
	$("#add_invoice_dlg_close_btn").click(function() {
		self.close();
	});
	
	$("#base_info").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=toConsumptionRecordPage";
	});
	$("#recharge").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=toRechargePage";
	}); 
	var show_per_page = 18; 
	var number_of_items = $('#receiptselect').children().size();
	var number_of_pages = Math.ceil(number_of_items/show_per_page);
	$('#current_page').val(0);
	$('#show_per_page').val(show_per_page);
	$('#number_of_pages').val(number_of_pages);
	if(number_of_pages>1){
	var navigation_html = '<a class="arrow" onclick="javascript:previous()"><img src="<%=request.getContextPath()%>/image/button_prev_disabled.gif" width="16" height="16" alt="上一页" /></a>';
	var current_link = 0;
	while(number_of_pages > current_link){
		navigation_html += '<a class="num" onclick="javascript:go_to_page(' + current_link +')" longdesc="' + current_link +'">'+ (current_link + 1) +'</a>';
		current_link++;
	}
	navigation_html += '<a class="arrow" onclick="javascript:next()"><img src="<%=request.getContextPath()%>/image/button_next.gif" width="16" height="16" alt="下一页" /></a>';
	$('.pagebar .page').html(navigation_html);
	$('.pagebar .page .num:first').addClass('active');
	$('#receiptselect').children().css('display', 'none');
	$('#receiptselect').children().slice(0, show_per_page).css('display', 'block');	
	}
});


// 提交
self.save = function() {
	var myCheckbox = document.getElementsByName("amount");
	for(i = 0;i<myCheckbox.length; i++) {
		if(myCheckbox[i].checked){
				ids.push($(myCheckbox[i]).attr("ids"));
		}
	}
	var formData = $.formToBean(invoice_add_dlg_form);
	formData.ids = ids;
	formData.amount = amount+'';
	formData.id = data[0];
	formData.addressId = data[1];
	formData.invoiceTitle = data[2];
	formData.address = data[3];
	formData.recipients = data[4];
	formData.phone = data[5];
	ajax.remoteCall("bean://invoiceService:addInvoice", 
		[ formData ],
		function(reply) {
			if (reply.status == "exception") {
				top.$.messager.alert('警告',reply.exceptionMessage,'warning');
			} else if (reply.result.status == "success") {
// 					var data = $("#add_invoice_dlg_container").parent().prop("_data_");
// 					$("#add_invoice_dlg").dialog("close");
// 					data.onClose(reply.result);
			} else {
				top.$.messager.alert('警告',reply.result.message,'warning');
			}
		}
	);
	
};


// 关闭
self.close = function() {
	$("#add_invoice_dlg").dialog("close");
};

//--------------------------

};

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
    <div class="header"> 
	   <div class="top"> 
	    <a class="logo l" href="<%=request.getContextPath()%>/"><img src="<%=request.getContextPath()%>/image/logo_tf.png" width="184" height="34" alt="天府软件园创业场" /></a> 
	    <div id="beforelogin" class="user r"> 
	     <a id="loginlink" href="javascript:void(0);" class="graylink">登录</a>
	     <span>|</span> 
	     <a id="reglink" href="javascript:void(0);">注册</a> 
	    </div> 
	    <div id="afterlogin" class="user r" style="display:none;">
	     <img class="reddot" src="<%=request.getContextPath()%>/image/reddot.png" width="6" height="6" alt=" " />
	     <a id="logoutlink" href="javascript:void(0);">注销</a>
	     <span>|</span>
	     <a href="<%=request.getContextPath()%>/user.do" class="bluelink">我的云端</a>
	    </div>
	    <div class="nav r">
	     <a href="<%=request.getContextPath()%>/" style="background:transparent;"><img id="nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/nav_1_i.png" width="20" height="20" alt="首页" style="padding:8px 0" /> </a>
	     <a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a>
	     <a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
	     <a href="<%=request.getContextPath()%>/solution.do">解决方案</a>
	     <a href="<%=request.getContextPath()%>/help.do">帮助中心</a>
	     <a href="<%=request.getContextPath()%>/aboutus.do">关于我们</a>
	     <a href="#" style="display:none"></a>
	     <a href="<%=request.getContextPath()%>/user.do?flag=login" style="display:none"></a>
	     <a href="#" style="display:none"></a>
	     <a href="#" style="display:none">我的云端</a>
	    </div>
	   </div>
	   <div class="subnav">
	    <div class="box">
	     1
	    </div>
	    <div class="box">
	     2
	    </div>
	    <div class="box">
	     3
	    </div>
	    <div class="box">
	     4
	    </div>
	    <div class="box">
	     5
	    </div>
	    <div class="box">
	     6
	    </div>
	    <div class="box">
	     7
	    </div>
	    <div class="box">
	     8
	    </div>
	    <div class="box">
	     9
	    </div>
	    <div class="box">
	     <a id="overview" onclick="onSwitch(this);" href="#"><img id="nav_10_1" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_1_i.png" width="24" height="24" alt="概览" /><br />概览</a>
	     <a id="my_cloud_host_link" onclick="onSwitch(this);" href="#"><img id="nav_10_2" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_2_i.png" width="24" height="24" alt="我的云主机" /><br />我的云主机</a>
	     <a href="#" id="my_cloud_disk_link" onclick="onSwitch(this);"><img id="nav_10_3" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_3_i.png" width="24" height="24" alt="我的云硬盘" /><br />我的云硬盘</a>
	     <a href="#" id="recharge_record" onclick="onSwitch(this);"><img id="nav_10_4" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_4_i.png" width="24" height="24" alt="我的账户" /><br />我的账户</a>
	     <a href="#" id="oper_log" onclick="onSwitch(this);"><img id="nav_10_5" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_5_i.png" width="24" height="24" alt="操作日志" /><br />操作日志</a>
	     <a href="#" id="suggestion" onclick="onSwitch(this);"><img id="nav_10_6" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_6_i.png" width="24" height="24" alt="意见反馈" /><br />意见反馈</a>
	     <a href="#" id="my_uploaded_file_link" onclick="onSwitch(this);"><img id="nav_10_7" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_7_i.png" width="24" height="24" alt="文件夹" /><br />文件夹</a>
	    </div>
	   </div> 
	</div>
    <div class="main">
      <div class="titlebar">
        <div class="tabbar l"><a href="#" id="base_info">基本信息</a>　｜　<a href="#" id="recharge">账户充值</a>　｜　　<a href="<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=invoiceService&method=managePage" class="active">发票管理</a></div>
      </div>
      <ul class="steps" style="width:360px;">
        <li class="l">选择充值金额<span>1</span></li>
        <li class="l">选择开票方式<span>2</span></li>
        <li class="l">提交<span>3</span></li>
      </ul>
      <form id="invoice_add_dlg_form" class="wizard" method="post">
        <ul style="width:3000px;">
          <li>
            <div id="receiptselect" >
            <%
            if(accountBalanceDetailList.size() == 0) {
            	%>
            	<center><strong>暂无可索取的发票记录</strong></center>
            <%
        	}
				for(int i = 0; i < accountBalanceDetailList.size(); i++){
					AccountBalanceDetailVO accountBalanceDetailVO = accountBalanceDetailList.get(i); 
 			%> 
              <input name="amount"  id="rg<%=i%>" class="checkbox"  type="checkbox"   ids = "<%=accountBalanceDetailVO.getId()%>"  value="<%=accountBalanceDetailVO.getAmount()%>"  onclick="checkSum(),check()"/>
              <label for="rg<%=i %>" class="biglabel l">
              <strong class="l"><%=accountBalanceDetailVO.getAmount()%>元</strong>
              <div class="r"><%=StringUtil.formatDateString(accountBalanceDetailVO.getChangeTime(), "yyyyMMddHHmmssSSS", "yyyy-MM-dd")%></div>
              <div class="clear"></div>
              <div class="l" ><%=accountBalanceDetailVO.getPayType() == 1?"支付宝":"银联"%></div>
               <div class="r"><%=StringUtil.formatDateString(accountBalanceDetailVO.getChangeTime(), "yyyyMMddHHmmssSSS", "HH:mm:ss")%></div>
              </label>
              <%
            }
              %>
              <div class="clear"></div>
             </div>
             <div class="pagebar">
              <div class="page"></div>
              <input type='hidden' id='current_page' />
              <input type='hidden' id='show_per_page' />
              <input type='hidden' id='number_of_pages' />
            </div>
            <div class="clear"></div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><input id="next" style="cursor:pointer;" type="button" class="bluebutton r" value="下一步" onclick="nextstep();" disabled = "disabled"/><i>合计：<strong><span class = "amount_sum">0</span>元</strong></i><br />
              请注意：仅当年度充值金额可以索取发票，已索取发票金额不能退款。金额超过<strong>100元</strong>才能索取发票。</div>
          </li>
          <li>
            <div id="addressselect">
          <% 
          if(invoiceAddressList.size() > 0){
        		for(int i = 0; i < invoiceAddressList.size(); i++){
            %>
              <input name="rh" id="rh<%=i+1 %>" class="radio" type="radio" value="" onclick="radioShow(),submitEnabled()" />
              <label for="rh<%=i+1 %>" class="biglabel l">地址 <%=i+1 %></label>
             
  			<%
        		}
        	}
              %> 
              <input name="rh" id="rh0" class="radio" type="radio" value=""  onclick="radioShow(),submitDisabled()" />
              <label for="rh0" class="biglabel l">新地址</label>
              <div class="clear"></div>

              <div id="c">
                <%
        	for(int i = 0; i < invoiceAddressList.size(); i++){
        		InvoiceAddressVO invoiceAddressVO = invoiceAddressList.get(i);
            %>
              <div id="addr1" style="width: 690px;padding: 15px 0;margin:0 auto;line-height:30px;">
                <table width="670" border="0" cellspacing="0" cellpadding="5">
                <input id="userId" name="userId"  type="hidden" value = "<%=userId %>" />
                <input id="addressId" name="addressId"  type="hidden" value = "<%=invoiceAddressVO.getId() %>" />
                  <tr>
                    <td width="105">发票抬头</td>
                    <td width="545"><input name="invoiceTitle" type="hidden" value = "<%=invoiceAddressVO.getInvoiceTitle() %>" /><%=invoiceAddressVO.getInvoiceTitle() %></td>
                  </tr>
                  <tr>
                    <td>寄送地址</td>
                    <td><input name="address" type="hidden" value = "<%=invoiceAddressVO.getAddress() %>" /><%=invoiceAddressVO.getAddress() %></td>
                  </tr>
                  <tr>
                    <td>收件人</td>
                    <td><input name="recipients" type="hidden"  value = "<%=invoiceAddressVO.getRecipients() %>"/><%=invoiceAddressVO.getRecipients() %></td>
                  </tr>
                  <tr>
                    <td>联系电话</td>
                    <td><input name="phone" type="hidden" value = "<%=invoiceAddressVO.getPhone() %>" /><%=invoiceAddressVO.getPhone() %></td>
                  </tr>
                </table>
              </div>
              	<%
        	}
              %> 
                 <div id="newaddr" style="width: 690px;padding: 15px 0;margin:0 auto;line-height:30px;">
                <table width="690" border="0" cellspacing="0" cellpadding="5">
                  <input name="userId" type="hidden" value = "<%=userId %>" />
                  <input id="addressId" name="addressId"  type="hidden"  />
                  <tr>
                    <td width="105">发票抬头</td>
                    <td width="565"><input id = "invoiceTitle" name="invoiceTitle" type="text"  onchange = "insertInvoiceTitle()" onblur="checkInvoiceTitle(),submitCheck()" class="textbox" style="height:28px; width:335px"/>
                    <span class="tip" id="tip-invoiceTitle"></span>
                    </td>
                  </tr>
                  <tr>
                    <td>寄送地址</td>
                    <td><input id = "address" name="address" type="text" onchange = "insertAddress()"  onblur="checkAddress(),submitCheck()" class="textbox" style="height:28px; width:335px"/>
                    <span class="tip" id="tip-address"></span>
                    </td>
                  </tr>
                  <tr>
                    <td>收件人</td>
                    <td><input id = "recipients" name="recipients" type="text" onchange = "insertRecipients()" onblur="checkRecipients(),submitCheck()" class="textbox" style="height:28px; width:335px"/>
                    <span class="tip" id="tip-recipients"></span>
                    </td>
                  </tr>
                  <tr>
                    <td>联系电话</td>
                    <td><input id = "phone" name="phone" type="text" onchange = "insertPhone(),submitCheck()" onblur="checkPhone(),submitCheck()" class="textbox" style="height:28px; width:335px"/>
                    <span class="tip" id="tip-phone"></span>
                    </td>
                  </tr>
                </table>
              </div>
            </div>
              </div>
              <div class="clear"></div>
           
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><input id="add_invoice_dlg_submit_btn" style="cursor:pointer;" type="button" class="bluebutton r" value="提交" onclick="nextstep();" disabled = "disabled"/><a class="graybutton r" href="javascript:void(0);" style="cursor:pointer;" onclick="prevstep();">上一步</a><i>合计：<strong><span class = "amount_sum">0</span>元</strong></i><br />
              请注意：仅当年度充值金额可以索取发票，已索取发票金额不能退款。金额超过<strong>100元</strong>才能索取发票。</div>
          </li>
          <li>
            <div class="infoicon" style="background:url(<%=request.getContextPath() %>/image/progress.gif) no-repeat;">审核中</div>
            <div class="info" style="padding:0 0 30px 0">您可以随时前往“我的账户 - 发票管理”查询发票开具情况</div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=invoiceService&method=managePage"">完成</a></div>
          </li>
        </ul>
      </form>
    </div>
    <div class="clear"></div>
    <div class="footer">
		<div class="box">
			<div class="sitemap">
				产品<br />
				<a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a><br />
				<a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
			</div>
			<div class="sitemap">
				解决方案<br />
				<a href="<%=request.getContextPath()%>/solution.do">云管理平台</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云存储</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云桌面</a>
			</div>
			<div class="sitemap">
				帮助中心<br />
				<a href="<%=request.getContextPath()%>/help.do">常见问题</a><br />
				<a href="<%=request.getContextPath()%>/help.do">账户相关指南</a><br />
				<a href="<%=request.getContextPath()%>/help.do">云主机指南</a>
			</div>
			<div class="sitemap">
				关于我们<br />
				<a href="<%=request.getContextPath()%>/aboutus.do">关于我们</a><br /> 
			</div>
			<div class="sitemap" style="width: 100px;">
				关注我们<br />
				<a href="javascript:void(0);">微信公众号</a><br />
				<img src="<%=request.getContextPath()%>/image/weixin.gif" width="70" height="70" />
			</div>
			<div class="sitemap">
				&nbsp;<br />
				<a href="http://weibo.com/zhicloud" target="_blank">新浪微博</a><br />
				<img src="<%=request.getContextPath()%>/image/weibo.gif" width="70" height="70" />
			</div>
			<div class="hotline">
				<img src="<%=request.getContextPath()%>/image/tel.png" width="30" height="30"
					style="vertical-align: middle" /> 客服热线<br />
				<span style="font-size: 22px; color: #595959;">4000-212-999</span><br />
				<span>客服服务时间：7X24小时</span>
			</div>
			<div class="clear"></div>
			<div class="copyright">
				Copyright &copy; 2014 <a href="http://www.tianfusoftwarepark.com" target="_blank">成都天府软件园有限公司</a>, All rights reserved.
				蜀ICP备11001370号-3
			</div>
		</div> 
	</div>
  </div>
  <div class="pageright">
      <iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div>
</div>
</body>
</html>




