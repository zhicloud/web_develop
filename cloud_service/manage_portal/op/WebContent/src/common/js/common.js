/**
 * Created by songgk on 2015/5/15.
 */
var url = window.location.href;
$(function(){
//	var docHeight = $(".g-doc").height();
//	$("body").height(docHeight);
	
	//返回顶部	
	$.goup({
        trigger: 100,
        bottomOffset: 50,
        locationOffset: 25,
        title: '返回顶部',
        titleAsText: true
    });
	//注册
	$(".reglink").click(function(e){ 
		slideleft(0);
		e.stopPropagation();
	});
	//登录
	$(".loginlink").click(function(e){
		slideleft(1);
		e.stopPropagation();
	});
	//注销
	$(".logoutlink").click(function(){
		logout();
	});
	//点击空白区
	$('.g-doc,.btn-back').unbind("click");
	$('.g-doc,.btn-back').bind("click",function(e){
		slideright();
		e.stopPropagation();
	});
	//下拉菜单
	dropMenu(".n-iteam");
	
	//我的云端
	$("a.m-tab").hover(function(event){
		if(!$(this).hasClass("active")){
 			$(this).find("img").attr("src",a+"/src/user/img/"+$(this).find("img").attr("class")+"_h.png");
		}
	},function(event){
		if(!$(this).hasClass("active")){
			$(this).find("img").attr("src",a+"/src/user/img/"+$(this).find("img").attr("class")+"_i.png");
		}else{
			$(this).find("img").attr("src",a+"/src/user/img/"+$(this).find("img").attr("class")+"_h.png");
		}
	});
	
	//登录验证
	/*$("#login_submit").click(function(){
		var myEmail = $.trim($('#log_ipteml').val());
		var myPassword = $.trim($('#log_iptpwd').val());
		var verificationCode = $.trim($('#log_iptcode').val());
		var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
			
		if(myEmail==null || myEmail==""){
			layer.tips('邮箱不能为空','#log_ipteml',{
	            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
	        });
	        return false;
		}else if(!myreg.test(myEmail)){
			layer.tips('邮箱格式不正确','#log_ipteml',{
	            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
	        });
			return false;
		}else if(myPassword==null || myPassword==""){
			layer.tips('密码不能为空','#log_iptpwd',{
	            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
	        });
			return false;
		}else if(myPassword.length<6||myPassword.length>20){ 
			layer.tips('密码长度为6-20个字符','#log_iptpwd',{
	            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
	        });
			return false;
		}else if(verificationCode==null || verificationCode==""){
			layer.tips('验证码不能为空','#log_iptcode',{
	            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
	        });
			return false;
		}
	})*/
	
	//注册验证
	/*var regisiter = new zCloudRegister();
	$('#reg_submit').on('click', function () {
	    regisiter.checkdata();
	});*/
	
	//滚动字幕
//	ScrollText($('#scrollText'),24,1190,'<i>重要通知：</i>机房迁移！','left',1,20);
});

function navHighlight(doParam,viewParam){
	if(doParam == "uh"){
		$('.nav .nav-list .n-iteam:eq(0) a.lev1').addClass('current').parent().siblings().find('a').removeClass('current');
	}else if(doParam == "upro"){
		$('.nav .nav-list .n-iteam:eq(1) a.lev1').addClass('current').parent().siblings().find('a').removeClass('current');
		if(viewParam == "ucs"){
			$('.pro-submenu ul li:eq(0) a.lev2:eq(0)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}else if(viewParam == "ucshp"){
			$('.pro-submenu ul li:eq(0) a.lev2:eq(1)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}else if(viewParam == "uccdn"){
			$('.pro-submenu ul li:eq(0) a.lev2:eq(2)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}else if(viewParam == "ulb"){
			$('.pro-submenu ul li:eq(0) a.lev2:eq(3)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}else if(viewParam == "ucm"){
			$('.pro-submenu ul li:eq(0) a.lev2:eq(4)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}else if(viewParam == "uec"){
			$('.pro-submenu ul li:eq(1) a.lev2:eq(0)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}else if(viewParam == "ucd"){
			$('.pro-submenu ul li:eq(1) a.lev2:eq(1)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}else if(viewParam == "ucdb"){
			$('.pro-submenu ul li:eq(1) a.lev2:eq(2)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}else if(viewParam == "umh"){
			$('.pro-submenu ul li:eq(1) a.lev2:eq(3)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}
	}else if(doParam == "uep"){
		$('.nav .nav-list .n-iteam:eq(2) a.lev1').addClass('current').parent().siblings().find('a').removeClass('current');
	}else if(doParam == "us"){
		$('.nav .nav-list .n-iteam:eq(3) a.lev1').addClass('current').parent().siblings().find('a').removeClass('current');
	}else if(doParam == "uts"){
		$('.nav .nav-list .n-iteam:eq(4) a.lev1').addClass('current').parent().siblings().find('a').removeClass('current');
		if(viewParam == "uscc"){
			$('.ts-submenu ul li:eq(0) a.lev2:eq(0)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}/*else if(viewParam == "usol"){
			$('.ts-submenu ul li:eq(0) a.lev2:eq(1)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}*/else if(viewParam == "uscs"){
			$('.ts-submenu ul li:eq(0) a.lev2:eq(1)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}else if(viewParam == "uhc"){
			$('.ts-submenu ul li:eq(1) a.lev2:eq(0)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}/*else if(viewParam == "uad"){
			$('.ts-submenu ul li:eq(1) a.lev2:eq(1)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}*/else if(viewParam == "uas"){
			$('.ts-submenu ul li:eq(1) a.lev2:eq(1)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}
	}else if(doParam == "uau"){
		$('.nav .nav-list .n-iteam:eq(5) a.lev1').addClass('current').parent().siblings().find('a').removeClass('current');
		if(viewParam == "ucf"){
			$('.au-submenu ul li:eq(0) a.lev2:eq(0)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}else if(viewParam == "uju"){
			$('.au-submenu ul li:eq(0) a.lev2:eq(1)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}else if(viewParam == "und"){
			$('.au-submenu ul li:eq(1) a.lev2:eq(0)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}else if(viewParam == "ucu"){
			$('.au-submenu ul li:eq(1) a.lev2:eq(1)').addClass('active').siblings("a.lev2").removeClass('active').parent().siblings().find('a').removeClass('active');
		}
	}else if(doParam == "umc"){
		$('.afterlog .mycloud').addClass('current').parent().next().find('a').removeClass('current');
		if(viewParam == "umov"){
			$('.mc-tabsinfo a:eq(0)').addClass('active').siblings("a").removeClass('active');
			$('.mc-tabsinfo a:eq(0)').find("img").attr("src",a+"/src/user/img/"+$('.mc-tabsinfo a:eq(0)').find("img").attr("class")+"_h.png");
		}else if(viewParam == "umcs"){
			$('.mc-tabsinfo a:eq(1)').addClass('active').siblings("a").removeClass('active');
			$('.mc-tabsinfo a:eq(1)').find("img").attr("src",a+"/src/user/img/"+$('.mc-tabsinfo a:eq(1)').find("img").attr("class")+"_h.png");
		}else if(viewParam == "umcd"){
			$('.mc-tabsinfo a:eq(2)').addClass('active').siblings("a").removeClass('active');
			$('.mc-tabsinfo a:eq(2)').find("img").attr("src",a+"/src/user/img/"+$('.mc-tabsinfo a:eq(2)').find("img").attr("class")+"_h.png");
		}else if(viewParam == "umec"){
			$('.mc-tabsinfo a:eq(3)').addClass('active').siblings("a").removeClass('active');
			$('.mc-tabsinfo a:eq(3)').find("img").attr("src",a+"/src/user/img/"+$('.mc-tabsinfo a:eq(3)').find("img").attr("class")+"_h.png");
		}else if(viewParam == "uma"){
			$('.mc-tabsinfo a:eq(4)').addClass('active').siblings("a").removeClass('active');
			$('.mc-tabsinfo a:eq(4)').find("img").attr("src",a+"/src/user/img/"+$('.mc-tabsinfo a:eq(4)').find("img").attr("class")+"_h.png");
		}else if(viewParam == "umol"){
			$('.mc-tabsinfo a:eq(5)').addClass('active').siblings("a").removeClass('active');
			$('.mc-tabsinfo a:eq(5)').find("img").attr("src",a+"/src/user/img/"+$('.mc-tabsinfo a:eq(5)').find("img").attr("class")+"_h.png");
		}else if(viewParam == "umfb"){
			$('.mc-tabsinfo a:eq(6)').addClass('active').siblings("a").removeClass('active');
			$('.mc-tabsinfo a:eq(6)').find("img").attr("src",a+"/src/user/img/"+$('.mc-tabsinfo a:eq(6)').find("img").attr("class")+"_h.png");
		}else if(viewParam == "umul"){
			$('.mc-tabsinfo a:eq(7)').addClass('active').siblings("a").removeClass('active');
			$('.mc-tabsinfo a:eq(7)').find("img").attr("src",a+"/src/user/img/"+$('.mc-tabsinfo a:eq(7)').find("img").attr("class")+"_h.png");
		}else if(viewParam == "umfs"){
			$('.mc-tabsinfo a:eq(8)').addClass('active').siblings("a").removeClass('active');
			$('.mc-tabsinfo a:eq(8)').find("img").attr("src",a+"/src/user/img/"+$('.mc-tabsinfo a:eq(8)').find("img").attr("class")+"_h.png");
		}
	}else{
		$('.nav .nav-list .n-iteam a.lev1').removeClass('current');
	}
}
//左滑动
function slideleft(page){
	if($('.g-doc').css('margin-left') != '0px'){
		if(page==1){
			$('.g-log .u-login').css("display","block");
			$('.g-log .u-register').css("display","none");
		}else if(page==0){
			$('.g-log .u-login').css("display","none");
			$('.g-log .u-register').css("display","block");
		}
	}else{
		if(page==1){
			$('.g-log .u-login').css("display","block");
			$('.g-log .u-register').css("display","none");
		}else if(page==0){
			$('.g-log .u-login').css("display","none");
			$('.g-log .u-register').css("display","block");
		}
		$('.g-doc').css('margin-left','-170px');
		$('.g-log').css('margin-left','-340px');
		$('.goup-container').css('right','365px');
		$('.goup-text').css('right','345px');
		$('p.arrows .prev').css('left','190px');
		$('p.arrows .next').css('right','190px');
	}
	if(page==1){
		// 换一个验证码 
		$("#verification_code_img").click(); 
		
	}else{
		$("#verification_code_register_img").click();
	}
}
//右滑动
function slideright(){ 
	$('.g-doc').css('margin-left','0');
	$('.g-log').css('margin-left','0');
	$('p.arrows .prev').css({
		'left':'20px',
		'-webkit-transition': 'left .5s',
		'-moz-transition': 'left .5s',
		'-ms-transition': 'left .5s',
		'-o-transition': 'left .5s',
		'transition': 'left .5s'
	});
	$('p.arrows .next').css({
		'right':'20px',
		'-webkit-transition': 'right .5s',
		'-moz-transition': 'right .5s',
		'-ms-transition': 'right .5s',
		'-o-transition': 'right .5s',
		'transition': 'right .5s'
	});
	$('.goup-container').css({
		'right':'25px',
		'-webkit-transition': 'right .5s',
		'-moz-transition': 'right .5s',
		'-ms-transition': 'right .5s',
		'-o-transition': 'right .5s',
		'transition': 'right .5s'
	});
	$('.goup-text').css({
		'right':'5px',
		'-webkit-transition': 'right .5s',
		'-moz-transition': 'right .5s',
		'-ms-transition': 'right .5s',
		'-o-transition': 'right .5s',
		'transition': 'right .5s'
	});
}

// 注销
function logout(){
	
}
// 下拉菜单
function dropMenu(obj){
	$(obj).each(function(){
		var theMenu = $(this).find(".submenu"),
            tarHeight = theMenu.height();
		theMenu.css({
            height:0,
            opacity:0
        });
        $(this).hover(function(){
            $(this).addClass("selected");
            theMenu.stop().show().animate({height:tarHeight,opacity:1},400);
        },function(){
            $(this).removeClass("selected");
            theMenu.stop().animate({height:0,opacity:0},400,function(){
                $(this).css({display:"none"});
            });
        });
	});
}

function onclickSwitch(target){
	if( target.id=="myoverview" ){ // 概览
		window.location.href = a+"/user.do";
	}else if( target.id=="mycloudser" ){ // 我的云主机
		window.location.href = a+"/bean/page.do?userType=4&bean=cloudUserService&method=myCloudHostPage";
	}else if( target.id=="myclouddrive" ){ // 我的云硬盘
		window.location.href = a+"/bean/page.do?userType=4&bean=cloudDiskService&method=managePage";
	}else if( target.id=="myexclucloud" ){ // 我的专属云
		window.location.href = a+"/bean/page.do?userType=4&bean=vpcService&method=managePage";
	}else if( target.id=="myaccount" ){ // 我的账户
		window.location.href = a+"/bean/page.do?userType=4&bean=accountBalanceService&method=toConsumptionRecordPage";
	}else if( target.id=="myoperalog" ){ // 操作日志
		window.location.href = a+"/bean/page.do?userType=4&bean=operLogService&method=managePage";
	}else if( target.id=="myfeedback" ){ // 意见反馈
		window.location.href = a+"/bean/page.do?userType=4&bean=suggestionService&method=managePage";
	}else if( target.id=="myuploadfile" ){ // 上传文件
		window.location.href = a+"/bean/page.do?userType=4&bean=cloudUserService&method=myFileManagePage";
	}else if( target.id=="myfilingsys" ){ // ICP备案系统
		window.location.href = a+"/src/user/u_myfilingsys.jsp";
	}
}

/**
 * 邮箱验证
 */
/*function checkEmail() {
	var myEmail = $.trim($('#log_ipteml').val());
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	
	if(myEmail==null || myEmail==""){
		layer.tips('邮箱不能为空','#log_ipteml',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
        return false;
	}else if(!myreg.test(myEmail)){
		layer.tips('邮箱格式不正确','#log_ipteml',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}
}*/
/**
 * 密码验证
 */
/*function checkPassword(){
	var myPassword = $.trim($('#log_iptpwd').val());
	
	if(myPassword==null || myPassword==""){
		layer.tips('密码不能为空','#log_iptpwd',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}else if(myPassword.length<6||myPassword.length>20){ 
		layer.tips('密码长度为6-20个字符','#log_iptpwd',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}
}*/
/**
 * 验证码验证
 */
/*function checkVcode(){
	var verificationCode = $.trim($('#log_iptcode').val());
	
	if(verificationCode==null || verificationCode==""){
		layer.tips('验证码不能为空','#log_iptcode',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}else if(verificationCode.length>2){
		layer.tips('验证码不正确','#log_iptcode',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}
}*/

/**
 * 注册时邮箱验证
 */
/*function checkRegEmail() {
	var myEmail = $.trim($('#reg_ipteml').val());
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	
	if(myEmail==null || myEmail==""){
		layer.tips('邮箱不能为空','#reg_ipteml',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 30,closeBtn:[0, false]
        });
        return false;
	}else if(!myreg.test(myEmail)){
		layer.tips('邮箱格式不正确','#reg_ipteml',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}else{
		var flag = false;
		var formData = $.formToBean(terminal_user_add_dlg_form);
		ajax.remoteCall("bean://terminalUserService:checkAccount", [ formData ],function(reply) {
			if (reply.status == "exception") {
				alert(reply.exceptionMessage);
			}else if(reply.result.status=="fail"){
				layer.tips('邮箱已经注册，请更换邮箱','#reg_ipteml',{
		            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
		        });
			}else{
				flag = true;
			}
		});
		return flag;
	}
}*/

/**
 * 注册时密码验证
 */
/*function checkRegPassword(){
	var myPassword = $.trim($('#reg_iptpwd').val());
	
	if(myPassword==null || myPassword==""){
		layer.tips('密码不能为空','#reg_iptpwd',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}else if(myPassword.length<6||myPassword.length>20){ 
		layer.tips('密码长度为6-20个字符','#reg_iptpwd',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}
}*/

/**
 * 注册时密码确认验证
 */
/*function checkRegPasswordConf(){
	var iptPassword = $.trim($('#reg_iptpwd').val());
	var cfmPassword = $.trim($('#reg_cfmpwd').val());
	
	if(cfmPassword==null || cfmPassword==""){
		layer.tips('密码不能为空','#reg_cfmpwd',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}else if(cfmPassword.length<6||cfmPassword.length>20){ 
		layer.tips('密码长度为6-20个字符','#reg_cfmpwd',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}else if(iptPassword!=cfmPassword){
		layer.tips('两次输入的密码不一致','#reg_cfmpwd',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}
}*/

/**
 * 注册时手机验证
 */
/*function checkPhone(){
	var myphone = $.trim($('#reg_iptnum').val());
	var myreg = /^1[3|4|5|8][0-9]\d{8,8}$/;
	
	if(myphone==null || myphone==""){
		layer.tips('手机号码不能为空','#reg_iptnum',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}else if(!(myreg.test(myphone))){ 
		layer.tips('请输入正确的手机号码','#reg_iptnum',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}
}*/

/**
 * 注册时验证码验证
 */
/*function checkRegVcode(){
	var verificationCode = $.trim($('#reg_iptcode').val());
	
	if(verificationCode==null || verificationCode==""){
		layer.tips('验证码不能为空','#reg_iptcode',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}else if(verificationCode.length>2){
		layer.tips('验证码不正确','#reg_iptcode',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}
}*/

/**
 * 注册时短信验证码验证
 */
/*function checkMessage(){
	var messageCode = $.trim($('#reg_iptmsg').val());
	
	if(messageCode==null||messageCode==""){
		layer.tips('短息验证码不能为空','#reg_iptmsg',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}else if(messageCode.length>10){ 
		layer.tips('短息验证码长度不能超过10位','#reg_iptmsg',{
            style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
        });
		return false;
	}
}*/

/*var zCloudRegister = function () {
    this.data = {
    		regeml: '#reg_ipteml',
    		regipwd: '#reg_iptpwd',
    		regcpwd: '#reg_cfmpwd',
        regnum: '#reg_iptnum',
        regcode: '#reg_iptcode',
        regmsg: '#reg_iptmsg',
        regRule: '#rule',
        sendbtn: '#reg_sendcode',
        submitbtn: '#reg_submit'
    };
    this.require = false;
};
zCloudRegister.prototype = {
    //检测
    checkdata: function () {
        var d = this.data;
	    	var myEmailReg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	    	var myNumReg = /^1[3|4|5|8][0-9]\d{8,8}$/;
	    	
	    	if($(d.regeml).val()==null || $(d.regeml).val()==""){
	    		layer.tips('邮箱不能为空',d.regeml,{
                style: ['position:fixed;background-color:#219eff;color:#fff','#219eff'],time: 30,closeBtn:[0, false]
            });
	    		this.require = false;
	    	}else if(!myEmailReg.test($(d.regeml).val())){
	    		layer.tips('邮箱格式不正确',d.regeml,{
                style: ['position:fixed;background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
            });
	    		this.require = false;
	    	}else if($(d.regipwd).val()==null || $(d.regipwd).val()==""){
	    		layer.tips('密码不能为空',d.regipwd,{
                style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
            });
	    		this.require = false;
	    	}else if($(d.regipwd).val().length<6||$(d.regipwd).val().length>20){ 
	    		layer.tips('密码长度为6-20个字符',d.regipwd,{
                style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
            });
	    		this.require = false;
	    	}else if($(d.regcpwd).val()==null || $(d.regcpwd).val()==""){
	    		layer.tips('密码不能为空',d.regcpwd,{
                style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
            });
	    		this.require = false;
	    	}else if($(d.regcpwd).val().length<6||$(d.regcpwd).val().length>20){ 
	    		layer.tips('密码长度为6-20个字符',d.regcpwd,{
                style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
            });
	    		this.require = false;
	    	}else if($(d.regcpwd).val()!=$(d.regcpwd).val()){
	    		layer.tips('两次输入的密码不一致',d.regcpwd,{
                style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
            });
	    		this.require = false;
	    	}else if($(d.regnum).val()==null || $(d.regnum).val()==""){
	    		layer.tips('手机号码不能为空','#reg_iptnum',{
                style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
            });
	    		this.require = false;
	    	}else if(!(myNumReg.test($(d.regnum).val()))){ 
	    		layer.tips('请输入正确的手机号码','#reg_iptnum',{
                style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
            });
	    		this.require = false;
	    	}else if($(d.regcode).val()==null || $(d.regcode).val()==""){
	    		layer.tips('验证码不能为空',d.regcode,{
                style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
            });
	    		this.require = false;
	    	}else if($(d.regcode).val().length>2){
	    		layer.tips('验证码不正确',d.regcode,{
                style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
            });
	    		this.require = false;
	    	}else if($(d.regmsg).val()==null||$(d.regmsg).val()==""){
	    		layer.tips('短息验证码不能为空',d.regmsg,{
                style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
            });
	    		this.require = false;
	    	}else if($(d.regmsg).val().length>10){ 
	    		layer.tips('短息验证码长度不能超过10位',d.regmsg,{
                style: ['background-color:#219eff;color:#fff','#219eff'],time: 3,closeBtn:[0, false]
            });
	    		this.require = false;
	    	}else if ($(d.regRule).prop("checked") == false) {
            layer.tips('请勾选同意协议！', d.regRule, {
                style: ['background-color:#ff7d00;color:#fff', '#ff7d00'], time: 3, closeBtn: [0, false]
            });
            this.require = false;
        } else {
            this.require = true;
        }   
    }
};*/

function inituser(username,message){
//	if(username!=undefined){
//		$("#beforelogin").css("display","none");
//		$("#afterlogin").css("display","block");
//		$(".header .top .user span").attr("title",username);
//		if(message>0){
//			$(".header .top .user .reddot").css("display","block");
//			$("#title_message").css("background","url(image/title_message.png) no-repeat");
//			$("#title_message").html(message);
//		}
//	}else{
//		$("#afterlogin").css("display","none");
//		$("#beforelogin").css("display","block");
//		$(".header .top .user .reddot").css("display","none");
//	}
}

function loaduser(username,message){
	if(username!=undefined){
		$("#beforelogin").css("display","none");
		$("#afterlogin").css("display","block"); 
	}else{
		$("#afterlogin").css("display","none");
		$("#beforelogin").css("display","block"); 
	}
}
//注销
function commonlogout(){  
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
				//跳转到登录页面
				window.location.reload();
			}
			else
			{
				top.$.messager.alert('警告',reply.result.message,'warning');
			}
		}
	);
}


var ScrollTime;
function ScrollAutoPlay(contID,scrolldir,showwidth,textwidth,steper){
	var PosInit,currPos;
	with($('#'+contID)){
		currPos = parseInt(css('margin-left'));
		if(scrolldir=='left'){
			if(currPos<0 && Math.abs(currPos)>textwidth){
				css('margin-left',showwidth);
			}else{
				css('margin-left',currPos-steper);
			}
		}else{
			if(currPos>showwidth){
				css('margin-left',(0-textwidth));
			}
			else{
				css('margin-left',currPos-steper);
		    }
		}
	}
}

/**
 * AppendToObj：显示位置（目标对象）
 * ShowHeight：显示高度
 * ShowWidth：显示宽度
 * ShowText：显示信息
 * ScrollDirection：滚动方向（值：left、right）
 * Steper：每次移动的间距（单位：px；数值越小，滚动越流畅，建议设置为1px）
 * Interval：每次执行运动的时间间隔（单位：毫秒；数值越小，运动越快）
 */
function ScrollText(AppendToObj,ShowHeight,ShowWidth,ShowText,ScrollDirection,Steper,Interval){
	var TextWidth,PosInit,PosSteper;
	with(AppendToObj){
		html('');
		css('overflow','hidden');
		css('height',ShowHeight+'px');
		css('line-height',ShowHeight+'px');
		css('width',ShowWidth+'px');
	}
	if (ScrollDirection=='left'){
		PosInit = ShowWidth;
		PosSteper = Steper;
	}else{
		PosSteper = 0 - Steper;
	}
	//每次移动间距超出限制(单位:px)
	if(Steper<1 || Steper>ShowWidth){Steper = 1}
	//每次移动的时间间隔（单位：毫秒）
	if(Interval<1){Interval = 10}
	
	var Container = $('<div></div>');
	var ContainerID = 'ContainerTemp';
	var i = 0;
	while($('#'+ContainerID).length>0){
		ContainerID = ContainerID + '_' + i;
		i++;
	}
	with(Container){
		attr('id',ContainerID);
		css('float','left');
		css('cursor','default');
		appendTo(AppendToObj);
		html(ShowText);
		TextWidth = width();
		if(isNaN(PosInit)){PosInit = 0 - TextWidth;}
		css('margin-left',PosInit);
		mouseover(function(){
			clearInterval(ScrollTime);
		});
		mouseout(function(){
			ScrollTime = setInterval("ScrollAutoPlay('"+ContainerID+"','"+ScrollDirection+"',"+ShowWidth+','+TextWidth+","+PosSteper+")",Interval);
		});
	}
	ScrollTime = setInterval("ScrollAutoPlay('"+ContainerID+"','"+ScrollDirection+"',"+ShowWidth+','+TextWidth+","+PosSteper+")",Interval);
}
