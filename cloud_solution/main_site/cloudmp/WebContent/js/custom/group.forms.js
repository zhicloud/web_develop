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
/*jQuery(document).ready(function(){
	
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
    
    ///// 群组表单验证 /////
	validator = jQuery("#sys_group_form").validate({ 
        rules: {
        	groupName: {
                required: true,
                checkAvailable: true,
                minlength: 2,
                maxlength: 50
               },
               description: {
                maxlength: 100
               }
        },
     messages: {
    	 groupName: {
             required: "请输入群组名",
             minlength: jQuery.validator.format("群组名不能小于{0}个字符"),
             maxlength: jQuery.validator.format("群组名不能大于{0}个字符")
            },
            description: {
             maxlength: jQuery.validator.format("描述不能大于{0}个字符")
            }
         }
 	});

});*/


/*function resetForm(){
	validator.resetForm();
}

jQuery.validator.addMethod("checkAvailable",function(value,element){
	var oldname = jQuery("#group_name").attr("oldname");
	if(value == oldname){
		flag = true;
	}else {
		flag = checkGroupName();
	}
	return this.optional(element) || flag;
},"该群组名已存在，请重新输入");

function checkGroupName() {
	var group_name = jQuery("#group_name").val();
	if(group_name == ""){
		return;
	}
	jQuery.ajax({
        url: path+'/group/checkAvaliable',
        type: 'post',
        data: 'groupName=' + group_name,
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
}*/
