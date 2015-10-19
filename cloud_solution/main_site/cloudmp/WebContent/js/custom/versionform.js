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
var isGetFile = false;
var filetype = "";
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
    
    ///// 镜像表单创建表单验证 /////
	validator = jQuery("#imageform").validate({ 
        rules: {
        	    versionNumber: {
                  required: true, 
                  checkNameNoChinese:true 
                },  
                updateInfo: {
                 maxlength:100,
                },
               },
     messages: { 
    	 versionNumber:{
    	        	required:"请输入版本号"
    	        },  
                updateInfo:{
                 maxlength: jQuery.validator.format("描述最多{0}个字符")
                }
              }
 	});
	
	jQuery("#hostType").change(function(){ 
		getHostList();		
	});
});
function beforeSubmit(){
	if(isGetFile == false){
		jAlert('未选择文件','提示');
		return false;
	}
	if(filetype != "l"){
		jAlert('文件格式不正确','提示');
		return false;
		
	}
}
function saveForm(){ 
	
	if(validator.form()){
		var options = {
				success:function result(data){ 
					if(data.status == "success"){
						jAlert('保存成功','提示', function(r) {
 							location.href = path + "/version/list";
						});						
					}else{
						jAlert(data.message,'提示');
					} 
				},
				dataType:'json',
				timeout:60000
		};
		jQuery("#imageform").ajaxForm(options);
	}
}

function resetForm(){
	validator.resetForm();
}

 

 

//不包括汉字
jQuery.validator.addMethod("checkNameNoChinese", function(value, element) {
return this.optional(element) || (/^[a-zA-Z0-9,.,&,*,(,),!,~,#,$,%,^,-,_'"]*$/.test(value)&&value.length>=2&&value.length<=50) ;
}, "版本号名称由字母、数字和特殊字符组成,长度为2-50字符");
 

 
/**
 * 根据id删除版本
 * @param id
 */
function deleteVersion(id){ 
	jQuery.ajax({
        url: path+'/main/checklogin',
        type: 'post', 
        dataType: 'json',
        timeout: 10000,
        async: true,
        error: function()
        {
            alert('Error!');
        },
        success: function(result)
        {
        	if(result.status == "fail"){
        		jAlert('登录或以超时，请重新登录','提示', function(r) {
	    			top.document.location.href = path;
	    			});
	        	}else{ 
		        		jConfirm('确定要删除该版本吗？', '确认', function(r) {
	 	            		if(r == true){
	 	       	    		jQuery.get(path + "/version/"+id+"/delete",function(data){
	 	       	    			if(data.status == "success"){   
	 	       	    				jAlert('删除成功','成功', function(r) {
	 	       			    			location.href = path + "/version/list";
	 	       	 	    			});
	 	       	    			}else{  
	 	       	    				jAlert(data.message,'提示'); 	    				
	 	       	    			}
	 	       	    		});
	 	           			
	 	           		}
	 	       		});
	        	} 
        }
     });
	
 }
