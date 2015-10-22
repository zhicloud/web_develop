function init(level1,level2){
	var feature;
	var featurecount;
// 	$(".header").html('<div class="top"><a class="logo l" href="'+a+'/"><img src="'+a+'/image/logo_big.png" width="153" height="33" alt="致云 ZhiCloud" /></a><div id="beforelogin" class="user r"><a id="loginlink" href="javascript:void(0);" class="graylink">登录</a><span>|</span><a id="reglink" href="javascript:void(0);">注册</a></div><div id="afterlogin" class="user r" style="display:none;"><img class="reddot" src="'+a+'/image/reddot.png" width="6" height="6" alt=" " /><a id="logoutlink" href="javascript:void(0);">注销</a><span>|</span><a href="user.do" class="bluelink">我的云端</a></div><div class="nav r"><a href="'+a+'/" style="background:transparent;"><img id="nav_1" class="swapimage" src="'+a+'/image/nav_1_i.png" width="20" height="20" alt="首页" style="padding:8px 0" />  </a><a href="'+a+'/cloudsever.do">云主机</a><a href="'+a+'/cloudstorage.do">云硬盘</a><a href="'+a+'/solution.do">解决方案</a><a href="'+a+'/help.do">帮助中心</a><a href="'+a+'/aboutus.do">关于我们</a><a href="#" style="display:none"></a><a href="user.do?flag=login"  style="display:none"></a><a href="#" style="display:none"></a><a   href="#" style="display:none">我的云端</a></div></div><div class="subnav"><div class="box">1</div><div class="box">2</div><div class="box">3</div><div class="box">4</div><div class="box">5</div><div class="box">6</div><div class="box">7</div><div class="box">8</div><div class="box">9</div><div class="box"><a href="#"><img id="nav_10_1" class="swapimage" src="image/nav_10_1_i.png" width="24" height="24" alt="概览" /><br/>概览</a><a href="#"><img id="nav_10_2" class="swapimage" src="image/nav_10_2_i.png" width="24" height="24" alt="我的云主机" /><br/>我的云主机</a><a href="#"><img id="nav_10_3" class="swapimage" src="image/nav_10_3_i.png" width="24" height="24" alt="我的云硬盘" /><br/>我的云硬盘</a><a href="#"><img id="nav_10_4" class="swapimage" src="image/nav_10_4_i.png" width="24" height="24" alt="我的账户" /><br/>我的账户</a><a href="#"><img id="nav_10_5" class="swapimage" src="image/nav_10_5_i.png" width="24" height="24" alt="操作日志" /><br/>操作日志</a><a href="#"><img id="nav_10_6" class="swapimage" src="image/nav_10_6_i.png" width="24" height="24" alt="意见反馈" /><br/>意见反馈</a><a href="#"><img id="nav_10_7" class="swapimage" src="image/nav_10_7_i.png" width="24" height="24" alt="文件夹" /><br/>文件夹</a></div></div>');
	if(level1==10){
		$(".header .top .user .greenlink").attr("class","bluelink");
	}
// 	$(".footer").html('<div class="box"><div class="sitemap">产品<br /><a href="'+a+'/cloudsever.do">云主机</a><br /><a href="'+a+'/cloudstorage.do">云硬盘</a></div><div class="sitemap">解决方案<br /><a href="'+a+'/solution.do">云管理平台</a><br /><a href="'+a+'/solution.do">云存储</a><br /><a href="'+a+'/solution.do">云桌面</a></div><div class="sitemap">帮助中心<br /><a href="'+a+'/help.do">常见问题</a><br /><a href="'+a+'/help.do">账户相关指南</a><br /><a href="'+a+'/help.do">云主机指南</a></div><div class="sitemap">关于我们<br /><a href="'+a+'/aboutus.do">关于致云</a><br /><a href="'+a+'/job.do">加入我们</a><br /><a href="'+a+'/aboutus.do">联系我们</a></div><div class="sitemap" style="width:100px;">关注我们<br /><a href="javascript:void(0);">微信公众号</a><br /><img src="'+a+'/image/weixin.gif" width="70" height="70" /></div><div class="sitemap">&nbsp;<br /><a href="http://weibo.com/zhicloud" target="_blank">新浪微博</a><br /><img src="'+a+'/image/weibo.gif" width="70" height="70" /></div><div class="hotline"><img src="'+a+'/image/tel.png" width="30" height="30" style="vertical-align:middle" /> 客服热线<br /><span style="font-size:22px;color:#595959;">4000-212-999</span><br /><span>客服服务时间：7X24小时</span></div><div class="clear"></div><div class="copyright">Copyright &copy; 2014 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1</div></div>');	
	
	$("body").append('<div class="loadingbg"></div><div class="loading"><img id="bluedot" src="'+a+'/image/bluedot.png" width="18" height="18" /><img id="greendot" src="'+a+'/image/greendot.png" width="18" height="18" /></div>');
	$(".header .top .nav a:eq("+(level1-1)+")").addClass("active");
	$(".header .top .nav .active .swapimage").attr("src","image/nav_"+level1+"_a.png");
	if(level2!=undefined){
		$(".header .subnav").css("display","block");
		$(".header .subnav .box:eq("+(level1-1)+")").css("display","block");
		$(".header .subnav .box:eq("+(level1-1)+") a:eq("+(level2-1)+")").addClass("active");
		$(".header .subnav .box .active .swapimage").attr("src","image/nav_"+level1+"_"+level2+"_a.png");
	}
	$(".swapimage").parent().hover(function(event){
		if(!$(this).hasClass("active")){
			$(this).find("img").attr("src","image/"+$(this).find("img").attr("id")+"_h.png");
		}
	},
	function(event){
		if(!$(this).hasClass("active")){
			$(this).find("img").attr("src","image/"+$(this).find("img").attr("id")+"_i.png");
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
	if($('.pagewhite').css('margin-left')!='0px'){
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
		$('.pagewhite').css('margin-left','-340px');
		$('.pageright').css('margin-left','-340px');
		$('.pagewhite').bind("click",function(){
			slideright();
		});
	}
}
function slideright(){
	$('.pagewhite').css('margin-left','0');
	$('.pageright').css('margin-left','0');
	$('.pagewhite').unbind("click");
}
//轮播
function initfeature(s,c){
	feature=s;
	featurecount=c;
	$(".feature ul").css("left",(feature-1)*(-100)+"%");
	$(".feature ul").addClass("anime");
	$(".feature ul").css("display","block");
}
function nextfeature(){
	if(feature>=featurecount){
		feature=1;
		$(".feature ul").removeClass("anime");
		$(".feature ul").css("left","0");
		feature++;
		setTimeout(function(){$(".feature ul").addClass("anime");$(".feature ul").css("left",(feature-1)*(-100)+"%");},100);
	}else{
		feature++;
		setTimeout(function(){$(".feature ul").addClass("anime");$(".feature ul").css("left",(feature-1)*(-100)+"%");},100);
	}
}
function prevfeature(){
	if(feature<=1){
		feature=featurecount;
		$(".feature ul").removeClass("anime");
		$(".feature ul").css("left",(feature-1)*(-100)+"%");
		feature--;
		setTimeout(function(){$(".feature ul").addClass("anime");$(".feature ul").css("left",(feature-1)*(-100)+"%");},100);
	}else{
		feature--;
		setTimeout(function(){$(".feature ul").addClass("anime");$(".feature ul").css("left",(feature-1)*(-100)+"%");},100);
	}
}

function gotosolution(x,y){
	$("#m"+x+" a").removeClass("active");
	if(y!=1){
		$("#m"+x+" a:eq("+(y-1)+")").addClass("active");
	}
	$("#s"+x).css("left",(y-1)*(-100)+"%");
}
function scrollto(x){
	$('html,body').animate({scrollTop:x},700); 
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
			}
			else
			{
				top.$.messager.alert('警告',reply.result.message,'warning');
			}
		}
	);
}
function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message); 
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
