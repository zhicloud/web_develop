function init(level1){
	var step;
	var brief;
	var loadinganime;
	var loadingbganime;
//	$(".header").html('<div class="top"><a class="logo l" href="#"><img src="'+a+'/image/agent_logo.png" width="188" height="25" alt="致云代理商管理平台" /></a><div class="nav l"><a href="#" id="business"><img id="agent_nav_1" class="swapimage" src="'+a+'/image/agent_nav_1_i.png" width="21" height="21"/>业务信息</a><a href="#" id="user_manage"><img id="agent_nav_2" class="swapimage" src="'+a+'/image/agent_nav_2_i.png" width="21" height="21"/>用户管理</a><a href="#" id="my_account"><img id="agent_nav_3" class="swapimage" src="'+a+'/image/agent_nav_3_i.png" width="21" height="21"/>我的账户</a><a href="#" id="oper_log"><img id="agent_nav_4" class="swapimage" src="'+a+'/image/agent_nav_4_i.png" width="21" height="21"/>操作日志</a></div><div class="user l"><img class="reddot" src="'+a+'/image/reddot.png" width="6" height="6" alt=" " /><a id="logoutlink" href="javascript:void(0);">注销</a><span>|</span><a id="userlink" href="javascript:void(0);"></a></div><div class="clear"></div></div>');
//	$(".footer").html('Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1');
	//$("body").append('<div class="loadingbg"></div><div class="loading"><img id="bluedot" src="image/bluedot.png" width="18" height="18" /><img id="greendot" src="image/greendot.png" width="18" height="18" /></div>');
	$(".header .top .nav a:eq("+(level1-1)+")").addClass("active");
	$(".header .top .nav .active .swapimage").attr("src",a+"/image/agent_nav_"+level1+"_a.png");
	$(".swapimage").parent().hover(function(event){
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
		event.stopPropagation()
	});
	$("#loginlink").click(function(event){
		slideleft(1);
		event.stopPropagation()
	});
	$("#logoutlink").click(function(event){
		logout();
//		inituser();
	});
	//业务信息
	$("#business").click(function(event){
		window.location.href = a+"/bean/page.do?userType=3&bean=statementService&method=businessGraphicsPage";
	});
	//我的账户
	$("#my_account,#userlink").click(function(event){
		window.location.href = a+"/bean/page.do?userType=3&bean=accountBalanceService&method=toConsumptionRecordPage";
	});
	//操作日志
	$("#oper_log").click(function(event){
		window.location.href = a+"/bean/page.do?userType=3&bean=operLogService&method=managePageForAgent";
	});
	//用户管理
	$("#user_manage").click(function(event){
		window.location.href = a+"/bean/page.do?userType=3&bean=terminalUserService&method=managePageForAgent";
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
	$(".header .top .user #userlink").html(username);
	if(message>0){
		$(".header .top .user .reddot").css("display","block");
	}
}
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
function isoa(x){
	$("#isotitle a").removeClass("active");
	$("#isoa"+x).addClass("active");
	if(x==0){
		$("#isos").css("display","none");
	$("#isos"+0).css("display","block");
	}else{
		$("#isos"+0).css("display","none");
		$("#isos").css("display","block");
		$(".isog").css("display","none");
		$("#isog"+x).css("display","block");
	}
}
function isob(x){
	$("#isos .l a").removeClass("active");
	$("#isob"+x).addClass("active");
	$(".isos").css("display","none");
	$(".isos"+x).css("display","block");
	
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
					window.location.reload();
				});
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
					top.location.href = a+"/agent.do";
				}, 500);
			}
			else
			{
				top.$.messager.alert('警告',reply.result.message,'warning');
			}
		}
	);
}