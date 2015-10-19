jQuery(document).ready(function(){
	
	(function(jQuery){
		jQuery.AutoiFrame = function(_o){
			
			var _o_=new Function("return "+_o)(); 
			var OsObject = "";  
			   if(navigator.userAgent.indexOf("MSIE")>0) {  
				   jQuery('#'+_o).ready(function(){jQuery('#'+_o).height(_o_.document.body.scrollHeight+20)});
 			   }  
			   else if(isFirefox=navigator.userAgent.indexOf("Firefox")>0){  
				   jQuery('#'+_o).load(function(){jQuery('#'+_o).height(_o_.document.body.scrollHeight+40)});
 			   }  
			   else if(window.navigator.userAgent.indexOf("Chrome") !== -1) {  
				   jQuery('#'+_o).load(function(){jQuery('#'+_o).height(_o_.document.body.scrollHeight-10)});
 			   }   
			   else if(isSafari=navigator.userAgent.indexOf("Safari")>0) {  
				   jQuery('#'+_o).load(function(){jQuery('#'+_o).height(_o_.document.body.scrollHeight+20)});
			   }   
			   else if(isSafari=navigator.userAgent.indexOf("Safari")>0) {  
				   jQuery('#'+_o).load(function(){jQuery('#'+_o).height(_o_.document.body.scrollHeight+20)});
 			   }   
			   else if(isCamino=navigator.userAgent.indexOf("Camino")>0){  
				   jQuery('#'+_o).load(function(){jQuery('#'+_o).height(_o_.document.body.scrollHeight+20)});
 			   }  
			   else if(isMozilla=navigator.userAgent.indexOf("Gecko/")>0){  
 			        jQuery('#'+_o).load(function(){jQuery('#'+_o).height(_o_.document.body.scrollHeight+20)});
			   }  
			   else{
				   jQuery('#'+_o).load(function(){jQuery('#'+_o).height(_o_.document.body.scrollHeight+20)});				   
			   }
			
	    }
	})(jQuery);
	jQuery(function(){
		jQuery.AutoiFrame('content_frame');
		window.setInterval(function(){jQuery.AutoiFrame('content_frame')},100);
	});
	  //默认选中
    jQuery("#host_manage").click();  
    jQuery("#content_frame").load(function(){
        if(jQuery(this).contents().find("#mainpage").height()<520){
            jQuery(this).height(520);
        }else{
        	if(jQuery(this).contents().find("#mainpage").height()-40 >= 520){        		
        		jQuery(this).height(520);
        	}else{        		
        		jQuery(this).height(jQuery(this).contents().find("#mainpage").height()-40); 
        	}
        }
        
     });
    
    jQuery.validator.addMethod("checkPasswordEqual", function(value, element) {
    	var $new_password = jQuery("#new_password").val();
    	return this.optional(element) || value == $new_password ;
    }, "两次输入的密码不一致");
    //====================
    jQuery.validator.addMethod("checkOldPassword",function(value,element){
		var flag = false;
		jQuery.ajax({
			type:"post",
			url:path+"/user/checkOldPassword",
			data:{oldPassword:value},
			async:false,
			success:function(data){
				if(data.status=="success"){
					flag = true;
				}
			}
		});
		return flag;
	},"原密码输入错误");
    //====================
    myValidator = jQuery("#update_password_form").validate({ 
        rules: {
        	    oldPassword: {
                  required: true,
                  minlength:6,
                  maxlength:30,
                  checkOldPassword:true
                },
                newPassword: {
                  required: true,
                  minlength:6,
                  maxlength:30
                },
                newPasswordConfig: {
                  required: true,
                  minlength:6,
                  maxlength:30,
                  checkPasswordEqual:true
                }
               },
     messages: {
    	 		oldPassword: {
                 required: "请输入原密码",
                 minlength: "密码长度不能小于6个字符",
                 maxlength: "密码长度不能大于30个字符"
                },
                newPassword: {
                 required: "请输新密码",
                 minlength: "密码长度不能小于6个字符",
                 maxlength: "新密码长度不能大于30个字符"
                },
                newPasswordConfig: {
                 required: "请确认新密码",
                 minlength: "密码长度不能小于6个字符",
                 maxlength: "密码长度不能大于30个字符"
                },
              }
 	});
    
    jQuery("#logout_btn").click(function(){
     	jQuery.get(path+"/user/logout",function(data){
    		if(data.status == "success"){
				jAlert('注销成功','提示', function(r) {
					top.document.location.href = path + "/main/menu";
				});						
			}else{
				jAlert(data.message,'提示');
			} 
    	});
    });
});  

    function saveUpdatePwdForm(){
    	if(myValidator.form()){
    		var options = {
    				success:function result(data){
     					if(data.status == "success"){
    						jAlert('密码修改成功','提示', function(r) {
    							top.document.location.href = path + "/main/menu";
    						});						
    					}else{
    						jAlert(data.message,'提示');
    					}
     				},
    				dataType:'json',
    				timeout:10000
    		};
    		jQuery("#update_password_form").ajaxSubmit(options);
    	}
    } 
    
    