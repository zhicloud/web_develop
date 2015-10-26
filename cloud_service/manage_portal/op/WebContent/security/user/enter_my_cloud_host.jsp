<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>

<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));	
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String ip = (String) request.getAttribute("ip");
	String port = (String) request.getAttribute("port");
	String password = (String) request.getAttribute("password");

%>
<!doctype html>
<html>
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 		
<script src="<%=request.getContextPath()%>/javascript/client/spicearraybuffer.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/enums.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/atKeynames.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/utils.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/png.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/lz.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/quic.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/bitmap.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/spicedataview.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/spicetype.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/spicemsg.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/wire.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/spiceconn.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/display.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/main.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/inputs.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/simulatecursor.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/cursor.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/thirdparty/jsbn.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/thirdparty/rsa.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/thirdparty/prng4.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/thirdparty/rng.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/thirdparty/sha1.js"></script>
<script src="<%=request.getContextPath()%>/javascript/client/ticket.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/big.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/spice.css" />

<script>
	var host = document.location.hostname;
	var port = "<%=port%>";
	var scheme = "ws://";
	var uri = scheme + host + ":" + port;
	var sc;
	var password = "<%=password%>";
	var path;
	
	var a = '<%= request.getContextPath()%>';
	
	var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
	
	function sendPort(){
		ajax.remoteCall("bean://cloudHostService:sendPort", 
				[port],
				function(){
			
		}); 
	}
	//关闭 client 检测
	var thisPage = false;
	window.onbeforeunload = function checkLeave(e) {
		var evt = e ? e : (window.event ? window.event : null); //此方法为了在firefox中的兼容
		if (!thisPage) {
			sendPort();
			evt.returnValue="确定离开当前页面？";
		}

	}

	//每5秒定时执行
	function timedCount() {
		ajax.remoteCall("bean://cloudHostService:keepAlive", [ port ],
				function() {

				});
		t = setTimeout("timedCount()", 5000);
	}

	timedCount();
	


/* 	function spice_set_cookie(name, value, days) {
		var date, expires;
		date = new Date();
		date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000));
		expires = "; expires=" + date.toGMTString();
		document.cookie = name + "=" + value + expires + "; path=/";
	};

	function spice_query_var(name, defvalue) {
		var match = RegExp('[?&]' + name + '=([^&]*)').exec(
				window.location.search);
		return match ? decodeURIComponent(match[1].replace(/\+/g, ' '))
				: defvalue;
	} */

	function spice_error(e) {
		disconnect();
	}

	function connect() {
		//                var host, port, password, scheme = "ws://", uri;

		// By default, use the host and port of server that served this file
		//                host = spice_query_var('host', window.location.hostname);
		//				host = "10.211.55.3";
		// Note that using the web server port only makes sense
		//  if your web server has a reverse proxy to relay the WebSocket
		//  traffic to the correct destination port.
		/*                 var default_port = window.location.port;
		 if (!default_port) {
		 if (window.location.protocol == 'http:') {
		 default_port = 80;
		 }
		 else if (window.location.protocol == 'https:') {
		 default_port = 443;
		 }
		 } 
		 */
		// If a token variable is passed in, set the parameter in a cookie.
		// This is used by nova-spiceproxy.
		/* token = spice_query_var('token', null);
		if (token) {
			spice_set_cookie('token', token, 1)
		} */

		/*                 password = spice_query_var('password', '');
		 path = spice_query_var('path', 'websockify'); */

		//                if ((!host) || (!port)) {
		//                    console.log("must specify host and port in URL");
		//                    return;
		//                }
		if (sc) {
			sc.stop();
		}

		//                uri = scheme + host + ":" + port;

		try {
			sc = new SpiceMainConn({
				uri : uri,
				screen_id : "spice-screen",
			/* 	dump_id : "debug-div",
				message_id : "message-div", */
				password : password,
				onerror : spice_error
			});
		} catch (e) {
			alert(e.toString());
			disconnect();
		}

	}

	function disconnect() {
		console.log(">> disconnect");
		if (sc) {
			sc.stop();
		}
		console.log("<< disconnect");
	}

	connect();
	

	/* function refreshOnce(){
		if(location.href.indexOf("refresh=1") === -1) {
		       setTimeout(function() {
			     location = location.href + "?refresh=1"
		       },1)
		}
	 }
	
	refreshOnce(); */

	
</script>

</head>

<body>


	<!--        <div id="login">
            <span class="logo">SPICE</span>
        </div> -->

	<div id="spice-area">
		<div id="spice-screen" class="spice-screen"></div>
	</div>

<!-- 	<div id="message-div" class="spice-message"></div>
 -->
	<div id="debug-div">
		<!-- If DUMPXXX is turned on, dumped images will go here -->
	</div>
</body>
</html>
