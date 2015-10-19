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
    
    ///// 重置表单验证 /////
	validator = jQuery("#reset_password_form").validate({ 
        rules: {
        	password: {
                required: true,
                minlength: 5
               },
               confirm_password: {
                required: true,
                minlength: 5,
                checkResetPasswordEqual:true
               },
        },
     messages: {
    	 password: {
             required: "请输入密码",
             minlength: jQuery.validator.format("密码不能小于{0}个字 符")
            },
            confirm_password: {
             required: "请输入确认密码",
             minlength: "确认密码不能小于5个字符"
            },
         }
 	});
	
//	jQuery("#hostType").change(function(){ 
//		getHostList();		
//	});
});


function resetForm(){
	validator.resetForm();
}
//function getHostList(){
//	var warehouseId = jQuery("#hostType option:selected").val();
//	if(warehouseId == ""){
//		jQuery("#fromHostId").html("");
//		return;
//	}
//	jQuery.ajax({
//        url: '../image/gethost',
//        type: 'post',
//        data: 'warehouseId=' + warehouseId,
//        dataType: 'json',
//        timeout: 1000,
//        error: function()
//        {
//            alert('Error!');
//        },
//        success: function(result)
//        {
//        	var options = "<option value=''>请选择</option>  ";
//        	jQuery.each(result.properties.hostList,function(idx,item){ //循环对象取值
//        		if(item.userId != ""){       			
//        			options = options + "<option value='"+item.id+"'>用户 -"+item.userName+" 主机-"+item.displayName+"</option>";
//        		}
//            }); 
//        	jQuery("#fromHostId").html(options);
//        }
//     });
//}

//function checkImageName(){
//	var name = jQuery("#name").val();
//	if(name == ""){
//		return;
//	}
//	jQuery.ajax({
//        url: '../image/checkimagename',
//        type: 'post',
//        data: 'name=' + name,
//        dataType: 'json',
//        timeout: 1000,
//        async: false,
//        error: function()
//        {
//            alert('Error!');
//        },
//        success: function(result)
//        {
//        	if(result.status == "fail"){
//        		isNameUseable =  false;
//        	}else{
//        		isNameUseable =   true;       		
//        	}
//        }
//     });
//}
//
////不包括汉字
//jQuery.validator.addMethod("checkNameNoChinese", function(value, element) {
//return this.optional(element) || /^\w{2,50}$/.test(value) ;
//}, "镜像名称由字母、数字和下划线组成,长度为2-50字符");
////镜像名是否可用
//jQuery.validator.addMethod("imageNameUseable", function(value, element) {
//	checkImageName();
// 	return this.optional(element) || isNameUseable;
//}, "该镜像名已经存在");
jQuery.validator.addMethod("checkResetPasswordEqual", function(value, element) {
	var $confirm_password = jQuery("#reset_password_form #password").val();
	return this.optional(element) || value == $confirm_password;
}, "两次输入的密码不一致");