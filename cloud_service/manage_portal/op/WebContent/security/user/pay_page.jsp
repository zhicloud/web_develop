<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<!-- pay_page.jsp -->
<html>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
<title>网上支付商户模拟器</title>
<style type="text/css">
<!--
body {
	font-size: 9pt;
	font-style: normal;
	line-height: 20px;
	font-weight: normal;
	font-variant: normal;
	color: #000099;
}
.smallfont {
	font-size: 9pt;
	font-weight: bold;
	color: #FF0000;
}
.bigFont {
	font-size: 16pt;
	font-style: normal;
	font-weight: bolder;
	font-variant: normal;
	color: #000000;
}
.text {
	font-size: 9pt;
	font-style: normal;
	line-height: normal;
	color: #000000;
	background-color: #6699FF;
	letter-spacing: normal;
	word-spacing: normal;
	width: auto;
	border: 1px dotted #CCCCCC;
}
.text1 {
	font-size: 14pt;
	font-weight: bold;
	color: #330099;
	border-top-width: 1px;
	border-right-width: 1px;
	border-bottom-width: 1px;
	border-left-width: 1px;
	border-top-style: none;
	border-right-style: none;
	border-bottom-style: dotted;
	border-left-style: none;
	border-top-color: #FF0000;
	border-right-color: #FF0000;
	border-bottom-color: #FF0000;
	border-left-color: #FF0000;
}
-->
</style>
<script language="JavaScript" src="<%=request.getContextPath()%>/js/md5.js">
</script>
<script language="JavaScript"  src="<%=request.getContextPath()%>/js/pay.js">
</script>
</head>

<body bgcolor="#CCCCCC" onLoad="setValue()">
<table width="760" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
				 
				  <input type="hidden" name="INTER_FLAG" id="INTER_FLAG" onchange="DOTYPE_onclick()">
				   
			 
                 
    <td><form action="https://ibsbjstar.ccb.com.cn/app/ccbMain?" method="post" name="MD5form" id="MD5form" target="_blank">
        <table width="80%" border="1" align="center" cellpadding="0" cellspacing="0" bordercolor="#000000">
          
          <tr> 
            <td bordercolor="#CCCCCC" bgcolor="#FFFFFF">
			<table width="80%" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr> 
                  <td width="26%" height="20" nowrap>商户代码【MERCHANTID】</td>
                  <td width="74%" height="20"><input name="MERCHANTID" type="text" class="text1" id="MERCHANTID"></td>
                </tr>
                <tr> 
                  <td height="20">商户柜台代码【POSID】</td>
                  <td height="20"><input name="POSID" type="text" class="text1" id="POSID"></td>
                </tr>
                <tr> 
                  <td height="20">分行代码【BRANCHID】</td>
                  <td height="20"><input name="BRANCHID" type="text" class="text1" id="BRANCHID"></td>
                </tr>
				
                <tr> 
                  <td height="20">定单号【ORDERID】</td>
                  <td height="20"><input name="ORDERID" type="text" class="text1" id="ORDERID"></td>
                </tr>
                <tr> 
                  <td height="20">付款金额【PAYMENT】</td>
                  <td height="20"><input name="PAYMENT" type="text" class="text1" id="PAYMENT"></td>
                </tr>
				<tr> 
                  <td height="20">币种【CURCODE】</td>
                  <td height="20"><input name="CURCODE" type="text" class="text1" id="CURCODE"></td>
                </tr>
                <tr> 
                  <td height="20">交易码【TXCODE】</td>
                  <td height="20"><input name="TXCODE" type="text" class="text1" id="TXCODE"></td>
                </tr>
                <tr> 
                  <td height="20">备注1【REMARK1】</td>
                  <td height="20"><input name="REMARK1" type="text" class="text1" id="REMARK1"></td>
                </tr>
                <tr>
                  <td height="20">备注2【REMARK2】</td>
                  <td height="20"><input name="REMARK2" type="text" class="text1" id="REMARK2"></td>
                </tr>
                <tr>
                  <td height="20">MAC2【REMARK2】</td>
                  <td height="20"> <input type="text" name="MAC" value="" id="MAC"></td>
                </tr>
                <tr id="PUB32TR" > 
                  <td height="20">公钥前30位【PUB32】</td>
                  <td height="20"><input name="PUB32" type="text" class="text1" id="PUB32"></td>
                </tr>
				 <tr id="PUB32TR1" > 
                  <td height="20">公钥后30位【PUB】</td>
                  <td height="20"><input name="PUB32TR2" type="text" class="text1" id="PUB32TR2" value="56b70d563d8e997e0b48ee5b020111"></td>
                </tr>
                </tr>
				<tr id='GATEWAYTR' style='display:none'> 
                  <td height="20">网关类型【GATEWAY】</td>
                  <td height="20"><input name="GATEWAY" type="text" class="text1" id="GATEWAY"></td>
				</tr>
				<tr id='CLIENTIPTR' style='display:none'> 
                  <td height="20">客户端IP【CLIENTIP】</td>
                  <td height="20"><input name="CLIENTIP" type="text" class="text1" id="CLIENTIP" value ="128.128.0.1"></td>
				</tr>

				<tr id='REGINFOTR'  > 
                  <td height="20">注册信息【REGINFO】</td>
                  <td height="20"><input name="REGINFO" type="text" class="text1" id="REGINFO" value="xiaofeixia"></td>
				</tr>

				<tr id='PROINFOTR'  > 
                  <td height="20">商品信息【PROINFO】</td>
                  <td height="20"><input name="PROINFO" type="text" class="text1" id="PROINFO" value="digital"></td>
				</tr>
				
				<tr id='MER_REFERERTR'  > 
                  <td height="20">商户域名【REFERER】</td>
                  <td height="20"><input name="MER_REFERER" type="text" class="text1" id="MER_REFERER" value=""></td>
				</tr>
				                <tr> 
                  <td height="20">银行网址【URL】</td>
                  <td height="20">
				  <select name="bankURL" id="bankURL">
 			     
			     </select>
				  <br>
                     </td>
                </tr> 
                <tr> 
                  <td height="20" colspan="2"><div align="center"> 
                      <input   type="button" id="sendB2" onClick="go()" value="   立即支付 ">
                    </div></td>
                </tr>
              </table>
			  </td>
          </tr>
        </table> 
	    </form></td>
  </tr>
</table>
</body>
</html>
<script>

</script>