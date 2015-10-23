

function init(level1,level2){
	var step;
	var loadinganime;
	var loadingbganime;
	var brief;
	$(".header").css("padding-bottom","67px");
// 	$(".header").html('<div class="top"><a class="logo l" href="'+a+'/"><img src="'+a+'/image/logo_big.png" width="153" height="33" alt="致云 ZhiCloud" /></a><div id="beforelogin" class="user r"><a id="loginlink" href="javascript:void(0);" class="graylink">登录</a><span>|</span><a id="reglink" href="javascript:void(0);">注册</a></div><div id="afterlogin" class="user r" style="display:none;"><img class="reddot" src="'+a+'/image/reddot.png" width="6" height="6" alt=" " /><a id="logoutlink" href="javascript:void(0);">注销</a><span>|</span><a href="'+a+'/user.do" class="bluelink">我的云端</a></div><div class="nav r"><a href="'+a+'/" style="background:transparent;"><img id="nav_1" class="swapimage" src="'+a+'/image/nav_1_i.png" width="20" height="20" alt="首页" style="padding:8px 0" /> </a><a href="'+a+'/cloudsever.do">云主机</a><a href="'+a+'/cloudstorage.do">云硬盘</a><a href="'+a+'/solution.do">解决方案</a><a href="'+a+'/help.do">帮助中心</a><a href="'+a+'/aboutus.do">关于我们</a><a href="#" style="display:none"></a><a href="'+a+'/user.do?flag=login"  style="display:none"></a><a href="#" style="display:none"></a><a   href="#" style="display:none">我的云端</a></div></div><div class="subnav"><div class="box">1</div><div class="box">2</div><div class="box">3</div><div class="box">4</div><div class="box">5</div><div class="box">6</div><div class="box">7</div><div class="box">8</div><div class="box">9</div><div class="box"><a id="overview" onclick="onSwitch(this);" href="#"><img id="nav_10_1" class="swapimage" src="'+a+'/image/nav_10_1_i.png" width="24" height="24" alt="概览" /><br/>概览</a><a id="my_cloud_host_link" onclick="onSwitch(this);" href="#"><img id="nav_10_2" class="swapimage" src="'+a+'/image/nav_10_2_i.png" width="24" height="24" alt="我的云主机" /><br/>我的云主机</a><a href="#" id="my_cloud_disk_link" onclick="onSwitch(this);"><img id="nav_10_3" class="swapimage" src="'+a+'/image/nav_10_3_i.png" width="24" height="24" alt="我的云硬盘" /><br/>我的云硬盘</a><a href="#" id="recharge_record" onclick="onSwitch(this);"><img id="nav_10_4" class="swapimage" src="'+a+'/image/nav_10_4_i.png" width="24" height="24" alt="我的账户" /><br/>我的账户</a><a href="#" id="oper_log" onclick="onSwitch(this);"><img id="nav_10_5" class="swapimage" src="'+a+'/image/nav_10_5_i.png" width="24" height="24" alt="操作日志" /><br/>操作日志</a><a href="#" id="suggestion" onclick="onSwitch(this);"><img id="nav_10_6" class="swapimage" src="'+a+'/image/nav_10_6_i.png" width="24" height="24" alt="意见反馈" /><br/>意见反馈</a><a href="#" id="my_uploaded_file_link" onclick="onSwitch(this);"><img id="nav_10_7" class="swapimage" src="'+a+'/image/nav_10_7_i.png" width="24" height="24" alt="文件夹" /><br/>文件夹</a></div></div>');
 	if(level1==10){
		$(".header .top .user .greenlink").attr("class","bluelink");
	} 
// 	$(".footer").html('<div class="box"><div class="sitemap">产品<br /><a href="'+a+'/cloudsever.do">云主机</a><br /><a href="'+a+'/cloudstorage.do">云硬盘</a></div><div class="sitemap">解决方案<br /><a href="'+a+'/solution.do">云管理平台</a><br /><a href="'+a+'/solution.do">云存储</a><br /><a href="'+a+'/solution.do">云桌面</a></div><div class="sitemap">帮助中心<br /><a href="'+a+'/help.do">常见问题</a><br /><a href="'+a+'/help.do">账户相关指南</a><br /><a href="'+a+'/help.do">云主机指南</a></div><div class="sitemap">关于我们<br /><a href="'+a+'/aboutus.do">关于致云</a><br /><a href="'+a+'/job.do">加入我们</a><br /><a href="'+a+'/aboutus.do">联系我们</a></div><div class="sitemap" style="width:100px;">关注我们<br /><a href="javascript:void(0);">微信公众号</a><br /><img src="'+a+'/image/weixin.gif" width="70" height="70" /></div><div class="sitemap">&nbsp;<br /><a href="http://weibo.com/zhicloud" target="_blank">新浪微博</a><br /><img src="'+a+'/image/weibo.gif" width="70" height="70" /></div><div class="hotline"><img src="'+a+'/image/tel.png" width="30" height="30" style="vertical-align:middle" /> 客服热线<br /><span style="font-size:22px;color:#595959;">4000-212-999</span><br /><span>客服服务时间：7X24小时</span></div><div class="clear"></div><div class="copyright">Copyright &copy; 2014 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1</div></div>');	
 	$("body").append('<div class="loadingbg"></div><div class="loading"><img id="bluedot" src="'+a+'/image/bluedot.png" width="18" height="18" /><img id="greendot" src="'+a+'/image/greendot.png" width="18" height="18" /></div>');
	$(".header .top .nav a:eq("+(level1-1)+")").addClass("active");
	$(".header .top .nav .active .swapimage").attr("src",a+"/image/nav_"+level1+"_a.png");
	if(level2!=undefined){
		$(".header .subnav").css("display","block");
		$(".header .subnav .box:eq("+(level1-1)+")").css("display","block");
		$(".header .subnav .box:eq("+(level1-1)+") a:eq("+(level2-1)+")").addClass("active");
		$(".header .subnav .box .active .swapimage").attr("src",a+"/image/nav_"+level1+"_"+level2+"_a.png");
	}
	$(".swapimage").parent().hover(function(event){
		//alert((this).find("img").attr("id"));
		if(!$(this).hasClass("active")){
			$(this).find("img").attr("src",a+"/image/"+$(this).find("img").attr("id")+"_h.png");
		}
	},
	function(event){
		if(!$(this).hasClass("active")){
			$(this).find("img").attr("src",a+"/image/"+$(this).find("img").attr("id")+"_i.png");
		}
	});
	 
	$("#reglink").click(function(event){
		slideleft(0);
		event.stopPropagation();
	});
	$("#loginlink").click(function(event){
		slideleft(1);
		event.stopPropagation();
	});
	$("#logoutlink").click(function(event){
		logout();
	});
	$("#my_clouduan").click(function(event){
		toClouduan();
	});
	$(".radio").change(function(){
		$("#"+$(this).attr("name")).next("label").html("自定义("+$("#"+$(this).attr("name")).attr("title")+")");
		$("#"+$(this).attr("name")).next("label").removeClass("checked");
		radiochange();
	});
	$(".checkbox").change(function(){
		checkboxchange();
	});
	radiochange();
	checkboxchange();
	$(".custom input").focus(function(){
		$(".radio[name='"+$(this).attr('id')+"']").removeAttr("checked");
		$(".radio[name='"+$(this).attr('id')+"']").next("label").removeClass("checked");
		$(this).next("label").css("left","-99999px");
		$(this).next("label").addClass("checked");
		$(this).css("left","0");
		
	});
	$(".custom input").blur(function(){
		$(this).css("left","-99999px");
		$(this).next("label").css("left","0");
		if($(this).val()==""){
			$(this).next("label").html("自定义("+$(this).attr("title")+")");
			$(this).next("label").removeClass("checked");
		}else{
			$(this).next("label").html($(this).val()+$(this).attr("title"));
		}
	});
	$(".main .listleft .listicon .text input").focus(function(){
		$(this).next("label").css("left","-99999px");
		
	});
	$(".main .listleft .listicon .text input").blur(function(){
		$(this).next("label").css("left","0");
	});
	$(".einput").focus(function(){
		$(".ebutton").css("left","-99999px");
		$(".einput").css("left","0");
		
	});
	$(".einput").blur(function(){
		$(".ebutton").css("left","0");
		$(".einput").css("left","-99999px");
		
	});
	$(".dinput").focus(function(){
		$(".dbutton").css("left","-99999px");
		$(".dinput").css("left","0");
		
	});
	$(".dinput").blur(function(){
		$(".dbutton").css("left","0");
		$(".dinput").css("left","-99999px");
		
	});
}
function inituser(username,message){
	if(username!=undefined){
		$("#beforelogin").css("display","none");
		$("#afterlogin").css("display","block");
		$(".header .top .user span").attr("title",username);
		if(message>0){
			$(".header .top .user .reddot").css("display","block");
			$("#title_message").css("background","url(image/title_message.png) no-repeat");
			$("#title_message").html(message);
		}
	}else{
		$("#afterlogin").css("display","none");
		$("#beforelogin").css("display","block");
		$(".header .top .user .reddot").css("display","none");
	}
}
//右栏
function slideleft(page){
	if($('.pageleft').css('margin-left')!='0px'){
		if(page==1){
			$('.pageright #loginiframe').css("display","block");
			$('.pageright #regiframe').css("display","none");
		}else if(page==0){
			$('.pageright #loginiframe').css("display","none");
			$('.pageright #regiframe').css("display","block");
		}
	}else{
		if(page==1){
			$('.pageright #loginiframe').css("display","block");
			$('.pageright #regiframe').css("display","none");
		}else if(page==0){
			$('.pageright #loginiframe').css("display","none");
			$('.pageright #regiframe').css("display","block");
		}
		$('.pageleft').css('margin-left','-340px');
		$('.pageright').css('margin-left','-340px');
		$('.pageleft').bind("click",function(){
			slideright();
		});
	}
}
function slideright(){
	$('.pageleft').css('margin-left','0');
	$('.pageright').css('margin-left','0');
	$('.pageleft').unbind("click");
}
//表单
function inputfocus(label){
	$("#"+label+"label").css("z-index","-1");
}
function inputblur(label){
	if($("#input"+label).val()==""){
		$("#"+label+"label").css("z-index","1");
	}else{
		$("#"+label+"label").css("z-index","-1");
	}
}
function radiochange(){
	$(".radio").next("label").removeClass("checked");
	$(".radio:checked").next("label").addClass("checked");
}
function checkboxchange(){
	$(".checkbox").next("label").removeClass("checked");
	$(".checkbox:checked").next("label").addClass("checked");
}
//向导
function initstep(s){
	step=s;
	$(".steps li:eq("+(step-1)+")").addClass("active");
	$(".wizard ul").css("left",(step-1)*(-1000)+"px");
	$(".wizard ul").addClass("anime");
	$(".wizard ul").css("display","block");
}
function nextstep(){
	step++;
	$(".steps li").removeClass("active");
	$(".steps li:eq("+(step-1)+")").addClass("active");
	$(".wizard ul").css("left",(step-1)*(-1000)+"px");
}
function prevstep(){
	step--;
	$(".steps li").removeClass("active");
	$(".steps li:eq("+(step-1)+")").addClass("active");
	$(".wizard ul").css("left",(step-1)*(-1000)+"px");
}

function initbrief(b){
	brief=b;
}
function nextbrief(){
	brief++;
	$(".briefpages").css("margin-left",(brief-1)*(-1000)+"px");
}
function prevbrief(){
	brief--;
	$(".briefpages").css("margin-left",(brief-1)*(-1000)+"px");
}
function hidebrief(){
	$(".briefbg").css("display","none");
	brief=1;
	$(".briefpages").css("margin-left","0");
}
function showbrief(){
	$(".briefbg").css("display","block");
	brief=1;
	$(".briefpages").css("margin-left","0");
}

function loadingbegin(){
	$("#bluedot").css("margin-left","-38px");
	$("#bluedot").animate({"margin-left":"20px"},480);
	$("#greendot").css("margin-left","20px");
	$("#greendot").animate({"margin-left":"-38px"},480);
	$(".loading").css("display","block");
	loadinganime=setInterval(function(){
		if($("#bluedot").css("margin-left")=="-38px"){
			$("#bluedot").animate({"margin-left":"20px"},480);
		}else{
			$("#bluedot").animate({"margin-left":"-38px"},480);
		}
		if($("#greendot").css("margin-left")=="-38px"){
			$("#greendot").animate({"margin-left":"20px"},480);
		}else{
			$("#greendot").animate({"margin-left":"-38px"},480);
		}
	},500);
	loadingbganime=setTimeout(function(){
		$(".loadingbg").fadeIn();
	},500);
}
function loadingend(){
	$(".loading").css("display","none");
	$(".loadingbg").fadeOut(200);
	clearInterval(loadinganime);
	clearInterval(loadingbganime);
}

//注销
function logout()
{ 
	ajax.remoteCall("bean://sysUserService:logout",
		[],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert('提示','会话超时，请重新登录','info',function(){
					//window.location.reload();
					slideright();
					
				});
			} 
			else if( reply.result.status=="success" )
			{

				inituser();
				// 注销成功 
				// 跳转页面
				slideright();
				//跳转到登录页面
				window.location.href=a+"/user.do";
			}
			else
			{
				top.$.messager.alert('警告',reply.result.message,'warning');
			}
		}
	);
}

function onSwitch(target)
{
	// 基本信息
	if( target.id=="base_info_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudUserService&method=baseInfoPage");
	}
	// 概览
	else if( target.id=="overview" )
	{
		window.location.href = a+"/user.do";
	}
	// 我的云硬盘
	else if( target.id=="my_cloud_disk_link" )
	{
		window.location.href = a+"/bean/page.do?userType=4&bean=cloudDiskService&method=managePage";
	}
	// 我的云主机
	else if( target.id=="my_cloud_host_link" )
	{
		window.location.href = a+"/bean/page.do?userType=4&bean=cloudUserService&method=myCloudHostPage";
	}
	// 我的文件
	else if( target.id=="my_uploaded_file_link" )
	{
		window.location.href = a+"/bean/page.do?userType=4&bean=cloudUserService&method=myFileManagePage";
	}
	// 我的账单
	else if( target.id=="my_bill_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=billService&method=managePage");
	}
	// 历史订单
	else if( target.id=="historical_order_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudUserService&method=myHistoryOrderPage");
	}
	// 充值
	else if( target.id=="recharge_link" )
	{
	}
	// 修改密码
	else if( target.id=="change_password_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudUserService&method=changePasswordPage");
	}
	// 邀请码
	else if( target.id=="invite_code_link")
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=inviteCodeService&method=terminalInviteCodePage");
	}
	// 意见反馈
	else if( target.id=="suggestion" )
	{
		window.location.href = a+"/bean/page.do?userType=4&bean=suggestionService&method=managePage";
	}
	// 充值记录
//	else if( target.id=="recharge_record" )
//	{
//		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=toRechargeRecordPage");
//	}
	else if( target.id=="recharge_record" )
	{
		window.location.href = a+"/bean/page.do?userType=4&bean=accountBalanceService&method=toConsumptionRecordPage";
	}
	// 充值
	else if( target.id=="recharge" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=toRechargePage");
	}
	// 发票管理
	else if( target.id=="invoice" )
	{
		window.location.href = a+"/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=managePage";
	}
	// 操作日志
	else if( target.id=="oper_log" )
	{
		window.location.href = a+"/bean/page.do?userType=4&bean=operLogService&method=managePage";
	}
}

function toClouduan(){
	if(name!=''){
		window.location.href=a+"/user.do";
	}else{
		slideleft(1);
		event.stopPropagation();
		
	}
}
