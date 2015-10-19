/*
 * 	Additional function for forms.html
 *	Written by ThemePixels	
 *	http://themepixels.com/
 *
 *	Copyright (c) 2012 ThemePixels (http://themepixels.com)
 *	
 *	Built for Amanda Premium Responsive Admin Template
 *  http://themeforest.net/category/site-templates/admin-templates
 */
var validator;
jQuery(document).ready(function(){
	
	///// FORM TRANSFORMATION /////
	jQuery('input:checkbox, input:radio, select.uniformselect, input:file').uniform();


	///// DUAL BOX /////
	var db = jQuery('#dualselect').find('.ds_arrow .arrow');	//get arrows of dual select
	var sel1 = jQuery('#dualselect select:first-child');		//get first select element
	var sel2 = jQuery('#dualselect select:last-child');			//get second select element
	
	sel2.empty(); //empty it first from dom.
	
	db.click(function(){
		var t = (jQuery(this).hasClass('ds_prev'))? 0 : 1;	// 0 if arrow prev otherwise arrow next
		if(t) {
			sel1.find('option').each(function(){
				if(jQuery(this).is(':selected')) {
					jQuery(this).attr('selected',false);
					var op = sel2.find('option:first-child');
					sel2.append(jQuery(this));
				}
			});	
		} else {
			sel2.find('option').each(function(){
				if(jQuery(this).is(':selected')) {
					jQuery(this).attr('selected',false);
					sel1.append(jQuery(this));
				}
			});		
		}
	});
    
    ///// 终端用户表单验证 /////
	validator = jQuery("#terminal_user_form").validate({ 
        rules: {
        	    username: {
                  required: true,
                  checkNameNoChinese:true,
                  checkAvailable:true,
//                  maxlength:50
            
                },
                name: {
                    required: true,
                    minlength: 2,
                    maxlength:45
                  },
                password: {
                 required: true,
                 minlength: 5
                },
                confirm_password: {
                 required: true,
                 minlength: 5,
                 checkPasswordEqual:true
                },
                email: { 
                    checkEmail: true
                },
                phone: { 
                    checkPhone: true
                },
               },
     messages: {
    	 		username: {
                 required: "请输入用户名",
                },
                name: {
                    required: "请输入显示名",
                    minlength: jQuery.validator.format("显示名不能小于{0}个字符"),
                    maxlength: jQuery.validator.format("显示名不能大于{0}个字符")
                  },
                password: {
                 required: "请输入密码",
                 minlength: jQuery.validator.format("密码不能小于{0}个字符")
                },
                confirm_password: {
                 required: "请输入确认密码",
                 minlength: "确认密码不能小于{0}个字符"
                }, 
              }
 	});
	
});


//不包括汉字

jQuery.validator.addMethod("checkNameNoChinese", function(value, element) {
return this.optional(element) || /^\w{2,50}$/.test(value) ;
}, "用户名称由字母、数字和下划线组成,长度为2-50字符");

jQuery.validator.addMethod("checkPhone", function(value, element) {
	return this.optional(element) || /^1[3|4|5|8][0-9]\d{8,8}$/.test(value) ;
	}, "请输入正确的手机号码");

jQuery.validator.addMethod("checkPasswordEqual", function(value, element) {
	var $confirm_password = jQuery("#terminal_user_form #password").val();
	return this.optional(element) || value == $confirm_password;
}, "两次输入的密码不一致");

jQuery.validator.addMethod("checkEmail", function(value, element) {
	return this.optional(element) || /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/.test(value) ;
}, "请输入正确的邮箱");

jQuery.validator.addMethod("checkAvailable",function(value,element){
	var oldname = jQuery("#username").attr("oldname");
	if(value == oldname){
		flag = true;
	}else {
		flag = checkUsername();
	}
	return this.optional(element) || flag;
},"该用户名已存在，请重新输入");

function checkUsername() {
	var username = jQuery("#username").val();
	if(username == ""){
		return;
	}
	jQuery.ajax({
        url: path+'/user/checkAvaliable',
        type: 'post',
        data: 'username=' + username,
        dataType: 'json',
        timeout: 10000,
        async: false,
        error: function()
        {
        },
        success: function(result)
        {
        	if(result.status == "fail"){
        		flag =  false;
        	}else{
        		flag =   true;       		
        	}
        }
     });
	return flag;
}
