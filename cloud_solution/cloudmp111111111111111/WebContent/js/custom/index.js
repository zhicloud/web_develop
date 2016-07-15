jQuery(document).ready(function(){
	///// TRANSFORM CHECKBOX /////							
	jQuery('input:checkbox').uniform();
	
	///// LOGIN FORM SUBMIT /////
//	jQuery('#login').submit(function(){
//		var username = jQuery('#username').val()+"";
//		
//		if(username.replace(/[ ]/g,"") == "") {
//			jQuery('.nousername').fadeIn();
//			jQuery('.nousername .loginmsg').html("<font color=red>用户名不能为空</font>");
			
//			jQuery('.nopassword').fadeIn();
//			return false;	
//		}
//		if(jQuery('#username').val() != '' && jQuery('#password').val() == '') {
//			jQuery('.nopassword').fadeIn().find('.userlogged h4, .userlogged a span').text(jQuery('#username').val());
//			jQuery('.nousername,.username').hide();
//			return false;;
//		}
//	});
	
	///// ADD PLACEHOLDER /////
//	jQuery('#username').attr('placeholder','Username');
//	jQuery('#password').attr('placeholder','Password');
});
function checkUsername(){
	var username = jQuery('#username').val()+"";
	if(username.replace(/[ ]/g,"") == ""){
		jQuery('.nousername').fadeIn();
		jQuery('.nousername .loginmsg').html("<font color=red>用户名不能为空</font>");
		return false;
	}else{
		jQuery('.nousername').hide();
		return true;
	}
}
function checkPassword(){
	var password = jQuery('#password').val()+"";
	if(password.replace(/[ ]/g,"") == ""){
		jQuery('.nousername').fadeIn();
		jQuery('.nousername .loginmsg').html("<font color=red>密码不能为空</font>");
		return false;
	}else{
		jQuery('.nousername').hide();
		return true;
	}
}
function login(){
	if(checkUsername() && checkPassword()){   
		var username = jQuery('#username').val()+"";
		var password = jQuery('#password').val()+"";
		var options = {
				success:function result(data){ 
					if(data.status == "success"){
						saveUserInfo();
						location.href = path+"/main/menu";
					}else{
						jQuery('.nousername').fadeIn();
						jQuery('.nousername .loginmsg').html("<font color=red>"+data.message+"</font>");
					}
				},
				dataType:'json',
				timeout:10000
		};
		jQuery("#loginform").ajaxForm(options); 
	}
	return;
}