
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.zhicloud.op.pay.unionpay.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<!-- unionpayapi.jsp -->
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>支付宝即时到账交易接口</title>
	</head>
	<% 
		

		/* //支付类型
		String payment_type = "1";
		//必填，不能修改
		//服务器异步通知页面路径
		String notify_url = "http://182.138.102.39:12657/op/public/user/notify_url.jsp";
		//需http://格式的完整路径，不能加?id=123这类自定义参数 
	
		//卖家支付宝帐户
		String seller_email = new String(request.getParameter("WIDseller_email").getBytes("ISO-8859-1"),"UTF-8");
		//必填
	
		//商户订单号
		String out_trade_no = new String(request.getParameter("WIDout_trade_no").getBytes("ISO-8859-1"),"UTF-8");
		//商户网站订单系统中唯一订单号，必填
	
		//订单名称
		String subject = new String(request.getParameter("WIDsubject").getBytes("ISO-8859-1"),"UTF-8");
		//必填
	*/
	    //订单
		String out_trade_no =  (String)request.getAttribute("orderId");
		//付款金额
		String total_fee =  (String)request.getAttribute("totalFee"); 
		
		
		//////////////////////////////////////////////////////////////////////////////////
		
		//把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();   
		/* sParaTemp.put("seller_email", seller_email);
		sParaTemp.put("subject", subject);*/
		sParaTemp.put("out_trade_no", out_trade_no);
		sParaTemp.put("total_fee", total_fee);   
		////////////////////////////////////////////////////////////////////////////////// 
		
		//建立请求
		String sHtmlText = QuickPaySampleServLet.service(sParaTemp);
		out.println(sHtmlText);
	%>
	<body>
	</body>
</html>
