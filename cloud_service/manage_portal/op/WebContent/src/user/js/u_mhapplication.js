var flag = true;
$(function(){
	//公司全称
	$('#mh_incubatorname').bind('focus',function () {
		$(this).removeClass('error');
		$("#mh_tip_incubatorname").addClass("optional").html('不超过25个汉字字符');
	}).bind('blur', function () {
		var incubatorName = $.trim($(this).val());
		$("#mh_tip_incubatorname").removeClass("optional");
		if(incubatorName==null || incubatorName==""){
			$(this).addClass('error');
			$("#mh_tip_incubatorname").html("公司全称不能为空");
			flag = false;
			return false;
		}else{
			$(this).removeClass('error');
			$("#mh_tip_incubatorname").html("");
			return true;
		}
	});
	//联系人姓名
	$('#mh_contactname').bind('focus',function () {
		$(this).removeClass('error');
		$("#mh_tip_contactname").addClass("optional").html('不超过10个汉字字符');
	}).bind('blur', function () {
		var contactName = $.trim($(this).val());
		$("#mh_tip_contactname").removeClass("optional");
		if(contactName==null || contactName==""){
			$(this).addClass('error');
			$("#mh_tip_contactname").html("联系人不能为空");
			flag = false;
			return false;
		}else{
			$(this).removeClass('error');
			$("#mh_tip_contactname").html("");
			return true;
		}
	});
	
	//联系人电话	
	$('#mh_contacttel').bind('focus',function () {
		$(this).removeClass('error');
		$("#mh_tip_contacttel").addClass("optional").html('必须是数字或“-”');
	}).bind('blur', function () {
		var phone = $.trim($(this).val());
		var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
		var isMob= /^1[3|4|5|8][0-9]\d{8,8}$/;
		$("#mh_tip_contacttel").removeClass("optional");
		
		if(phone==null || phone==""){
			$(this).addClass('error');
			$("#mh_tip_contacttel").html("固话或手机号码不能为空");
			flag = false;
			return false;
		}else if(!(isMob.test(phone)) && !(isPhone.test(phone))){ 
			$(this).addClass('error');
			$("#mh_tip_contacttel").html("请输入正确的固话或手机号码");
			flag = false;
			return false;
		} else{
			$(this).removeClass('error');
			$("#mh_tip_contacttel").html("");
			return true;
		}
	});
		
	//联系人QQ或微信
	$('#mh_contactqqwx').bind('focus',function () {
		$(this).removeClass('error');
		$("#mh_tip_contactqqwx").addClass("optional").html('例：QQ：123456789');
	}).bind('blur', function () {
		var contactQQWX = $.trim($(this).val());
		$("#mh_tip_contactqqwx").removeClass("optional");
		if(contactQQWX==null || contactQQWX==""){
			$(this).addClass('error');
			$("#mh_tip_contactqqwx").html("联系人QQ或微信不能为空");
			flag = false;
			return false;
		}else{
			$(this).removeClass('error');
			$("#mh_tip_contactqqwx").html("");
			return true;
		}
	});
	
	//公司简介
	$('#mh_incubatorprof').bind('focus',function () {
		$(this).removeClass('error');
		$("#mh_tip_incubatorprof").addClass("optional").html('不超过500个汉字字符');
	}).bind('blur', function () {
		var incubatorProf = $.trim($(this).val());
		$("#mh_tip_incubatorprof").removeClass("optional");
		if(incubatorProf==null || incubatorProf==""){
			$(this).addClass('error');
			$("#mh_tip_incubatorprof").html("公司简介不能为空");
			flag = false;
			return false;
		}else{
			$(this).removeClass('error');
			$("#mh_tip_incubatorprof").html("");
			return true;
		}
	});
	
	//验证码检测
	$('#mh_identifying').bind('focus',function () {
		$(this).removeClass('error');
		$("#mh_tip_code").html("");
	}).bind('blur', function () {
		var identifyingCode = $.trim($('#mh_identifying').val());
		if(identifyingCode==null || identifyingCode==""){
			$(this).addClass('error');
			$("#mh_tip_code").html("验证码不能为空");
			flag = false;
			return false;
		}else{
			$(this).removeClass('error');
			$("#mh_tip_code").html("");
			return true;
		}
	});
	
	//点击事件
	$('#mh_sub_apply').on('click', function () {
		flag = true;
		incubatorName();
		contactName();
		contacttel();
		contactQQWX();
		incubatorProf();
		identifyingCode();
		if(flag){ 
			application();
		}
	})
})

//孵化器/园区全称
function incubatorName(){
	var incubatorName = $.trim($("#mh_incubatorname").val());
	$("#mh_tip_incubatorname").removeClass("optional");
	if(incubatorName==null || incubatorName==""){
		$("#mh_incubatorname").addClass('error');
		$("#mh_tip_incubatorname").html("公司全称不能为空");
		flag = false;
	}else{
		$("#mh_incubatorname").removeClass('error');
		$("#mh_tip_incubatorname").html("");
		return true;
	}
}

//联系人姓名
function contactName(){
	var contactName = $.trim($("#mh_contactname").val());
	$("#mh_tip_contactname").removeClass("optional");
	if(contactName==null || contactName==""){
		$("#mh_contactname").addClass('error');
		$("#mh_tip_contactname").html("联系人不能为空");
		flag = false;
	}else{
		$("#mh_contactname").removeClass('error');
		$("#mh_tip_contactname").html("");
		return true;
	}
}

//联系人电话	
function contacttel(){
	var contacttel = $.trim($("#mh_contacttel").val());
	var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
	var isMob= /^1[3|4|5|8][0-9]\d{8,8}$/;
	$("#mh_tip_contacttel").removeClass("optional");
	if(contacttel==null || contacttel==""){
		$("#mh_contacttel").addClass('error');
		$("#mh_tip_contacttel").html("固话或手机号码不能为空");
		flag = false;
	}else if(!(isMob.test(contacttel)) && !(isPhone.test(contacttel))){ 
		$("#mh_contacttel").addClass('error');
		$("#mh_tip_contacttel").html("请输入正确的固话或手机号码");
		flag = false;
	} else{
		$("#mh_contacttel").removeClass('error');
		$("#mh_tip_contacttel").html("");
		return true;
	}
}

//联系人QQ或微信
function contactQQWX(){
	var contactQQWX = $.trim($("#mh_contactqqwx").val());
	$("#mh_tip_contactqqwx").removeClass("optional");
	if(contactQQWX==null || contactQQWX==""){
		$("#mh_contactqqwx").addClass('error');
		$("#mh_tip_contactqqwx").html("联系人QQ或微信不能为空");
		flag = false;
	}else{
		$("#mh_contactqqwx").removeClass('error');
		$("#mh_tip_contactqqwx").html("");
		return true;
	}
}

//公司简介
function incubatorProf(){
	var incubatorProf = $.trim($("#mh_incubatorprof").val());
	$("#mh_tip_incubatorprof").removeClass("optional");
	if(incubatorProf==null || incubatorProf==""){
		$("#mh_incubatorprof").addClass('error');
		$("#mh_tip_incubatorprof").html("公司简介不能为空");
		flag = false;
	}else{
		$("#mh_incubatorprof").removeClass('error');
		$("#mh_tip_incubatorprof").html("");
		return true;
	}
}

//验证码
function identifyingCode(){
	var identifyingCode = $.trim($('#mh_identifying').val());
		if(identifyingCode==null || identifyingCode==""){
			$('#mh_identifying').addClass('error');
			$("#mh_tip_code").html("验证码不能为空");
			flag = false; 
		}else{
			$('#mh_identifying').removeClass('error');
			$("#mh_tip_code").html("");  
		}
}
