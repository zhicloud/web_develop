var flag = true;
$(function(){
	//验证
//	$('.ep-plan').bind('focus',function () {
//	    $(this).removeClass('error').nextAll().removeClass('block');
//	}).bind('blur', function () {
//	    var required = $(this).attr("required");
//	    if (required != undefined && required != false) {
//	        if ($.trim($(this).val()) == null || $.trim($(this).val()) == '') {
//	            $(this).addClass('error').nextAll().addClass('block');
//	            return false;
//	        } else {
//	            $(this).removeClass('error').nextAll().removeClass('block');
//	            return true;
//	        }
//	    }
//	});
	
	//孵化器/园区全称
	$('#ep_incubatorname').bind('focus',function () {
		$(this).removeClass('error');
		$("#ep_tip_incubatorname").addClass("optional").html('不超过25个汉字字符');
	}).bind('blur', function () {
		var incubatorName = $.trim($(this).val());
		$("#ep_tip_incubatorname").removeClass("optional");
		if(incubatorName==null || incubatorName==""){
			$(this).addClass('error');
			$("#ep_tip_incubatorname").html("孵化器/园区全称不能为空");
			flag = false;
			return false;
		}else{
			$(this).removeClass('error');
			$("#ep_tip_incubatorname").html("");
			return true;
		}
	});
	//联系人姓名
	$('#ep_contactname').bind('focus',function () {
		$(this).removeClass('error');
		$("#ep_tip_contactname").addClass("optional").html('不超过10个汉字字符');
	}).bind('blur', function () {
		var contactName = $.trim($(this).val());
		$("#ep_tip_contactname").removeClass("optional");
		if(contactName==null || contactName==""){
			$(this).addClass('error');
			$("#ep_tip_contactname").html("联系人不能为空");
			flag = false;
			return false;
		}else{
			$(this).removeClass('error');
			$("#ep_tip_contactname").html("");
			return true;
		}
	});
	
	//联系人电话	
	$('#ep_contacttel').bind('focus',function () {
		$(this).removeClass('error');
		$("#ep_tip_contacttel").addClass("optional").html('必须是数字或“-”');
	}).bind('blur', function () {
		var phone = $.trim($(this).val());
		var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
		var isMob= /^1[3|4|5|8][0-9]\d{8,8}$/;
		$("#ep_tip_contacttel").removeClass("optional");
		
		if(phone==null || phone==""){
			$(this).addClass('error');
			$("#ep_tip_contacttel").html("固话或手机号码不能为空");
			flag = false;
			return false;
		}else if(!(isMob.test(phone)) && !(isPhone.test(phone))){ 
			$(this).addClass('error');
			$("#ep_tip_contacttel").html("请输入正确的固话或手机号码");
			flag = false;
			return false;
		} else{
			$(this).removeClass('error');
			$("#ep_tip_contacttel").html("");
			return true;
		}
	});
		
	//联系人QQ或微信
	$('#ep_contactqqwx').bind('focus',function () {
		$(this).removeClass('error');
		$("#ep_tip_contactqqwx").addClass("optional").html('例：QQ：123456789');
	}).bind('blur', function () {
		var contactQQWX = $.trim($(this).val());
		$("#ep_tip_contactqqwx").removeClass("optional");
		if(contactQQWX==null || contactQQWX==""){
			$(this).addClass('error');
			$("#ep_tip_contactqqwx").html("联系人QQ或微信不能为空");
			flag = false;
			return false;
		}else{
			$(this).removeClass('error');
			$("#ep_tip_contactqqwx").html("");
			return true;
		}
	});
	
	//简介或运营情况
	$('#ep_incubatorprof').bind('focus',function () {
		$(this).removeClass('error');
		$("#ep_tip_incubatorprof").addClass("optional").html('不超过500个汉字字符');
	}).bind('blur', function () {
		var incubatorProf = $.trim($(this).val());
		$("#ep_tip_incubatorprof").removeClass("optional");
		if(incubatorProf==null || incubatorProf==""){
			$(this).addClass('error');
			$("#ep_tip_incubatorprof").html("简介或运营情况不能为空");
			flag = false;
			return false;
		}else{
			$(this).removeClass('error');
			$("#ep_tip_incubatorprof").html("");
			return true;
		}
	});
	
	//验证码检测
	$('#ep_identifying').bind('focus',function () {
		$(this).removeClass('error');
		$("#tip_code").html("");
	}).bind('blur', function () {
		var identifyingCode = $.trim($('#ep_identifying').val());
		if(identifyingCode==null || identifyingCode==""){
			$(this).addClass('error');
			$("#ep_tip_code").html("验证码不能为空");
			flag = false;
			return false;
		}else{
			$(this).removeClass('error');
			$("#ep_tip_code").html("");
			return true;
		}
	});
	// 换一个验证码 
	$("#verification_code_application").click(function(){
		$("#verification_code_application").attr("src", a+"/public/verificationCode/new.do?userType=4&ts="+Math.random());
	});
	
	//点击事件
	$('#ep_sub_apply').on('click', function () {
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
	});
})


//孵化器/园区全称
function incubatorName(){
	var incubatorName = $.trim($("#ep_incubatorname").val());
	$("#ep_tip_incubatorname").removeClass("optional");
	if(incubatorName==null || incubatorName==""){
		$("#ep_incubatorname").addClass('error');
		$("#ep_tip_incubatorname").html("孵化器/园区全称不能为空");
		flag = false;
	}else{
		$("#ep_incubatorname").removeClass('error');
		$("#ep_tip_incubatorname").html("");
		return true;
	}
}

//联系人姓名
function contactName(){
	var contactName = $.trim($("#ep_contactname").val());
	$("#ep_tip_contactname").removeClass("optional");
	if(contactName==null || contactName==""){
		$("#ep_contactname").addClass('error');
		$("#ep_tip_contactname").html("联系人不能为空");
		flag = false;
	}else{
		$("#ep_contactname").removeClass('error');
		$("#ep_tip_contactname").html("");
		return true;
	}
}

//联系人电话	
function contacttel(){
	var contacttel = $.trim($("#ep_contacttel").val());
	var isPhone = /^([0-9]{3,4}-)?[0-9]{7,8}$/;
	var isMob= /^1[3|4|5|8][0-9]\d{8,8}$/;
	$("#ep_tip_contacttel").removeClass("optional");
	if(contacttel==null || contacttel==""){
		$("#ep_contacttel").addClass('error');
		$("#ep_tip_contacttel").html("固话或手机号码不能为空");
		flag = false;
	}else if(!(isMob.test(contacttel)) && !(isPhone.test(contacttel))){ 
		$("#ep_contacttel").addClass('error');
		$("#ep_tip_contacttel").html("请输入正确的固话或手机号码");
		flag = false;
	} else{
		$("#ep_contacttel").removeClass('error');
		$("#ep_tip_contacttel").html("");
		return true;
	}
}

//联系人QQ或微信
function contactQQWX(){
	var contactQQWX = $.trim($("#ep_contactqqwx").val());
	$("#ep_tip_contactqqwx").removeClass("optional");
	if(contactQQWX==null || contactQQWX==""){
		$("#ep_contactqqwx").addClass('error');
		$("#ep_tip_contactqqwx").html("联系人QQ或微信不能为空");
		flag = false;
	}else{
		$("#ep_contactqqwx").removeClass('error');
		$("#ep_tip_contactqqwx").html("");
		return true;
	}
}


//简介或运营情况
function incubatorProf(){
	var incubatorProf = $.trim($("#ep_incubatorprof").val());
	$("#ep_tip_incubatorprof").removeClass("optional");
	if(incubatorProf==null || incubatorProf==""){
		$("#ep_incubatorprof").addClass('error');
		$("#ep_tip_incubatorprof").html("简介或运营情况不能为空");
		flag = false;
	}else{
		$("#ep_incubatorprof").removeClass('error');
		$("#ep_tip_incubatorprof").html("");
		return true;
	}
}


//验证码
function identifyingCode(){
	var identifyingCode = $.trim($('#ep_identifying').val());
		if(identifyingCode==null || identifyingCode==""){
			$('#ep_identifying').addClass('error');
			$("#ep_tip_code").html("验证码不能为空");
			flag = false; 
		}else{
			$('#ep_identifying').removeClass('error');
			$("#ep_tip_code").html("");  
		}
}

function application(){ 
	var formData = $.formToBean(applicationForm);
	ajax.remoteCall("bean://eggPlanService:addEggPlan", [ formData ],function(reply) {
		   $("#verification_code_application").click();
			if (reply.status == "exception") { 
				top.$.messager.alert("警告", "申请失败", "warning", function(){
					window.location.reload();
				});
 			} else if (reply.result.status == "success") {
 				top.$.messager.alert("提示", "申请成功", "warning", function(){
					window.location.reload();
				});
 			}else {  
 				top.$.messager.alert("警告", reply.result.message, "warning", function(){
					window.location.reload();
				});
 			} 
		}
	);
}


