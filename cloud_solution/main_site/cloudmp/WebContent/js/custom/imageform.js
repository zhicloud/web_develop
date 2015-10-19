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
    
    ///// 镜像表单创建表单验证 /////
	validator = jQuery("#imageform").validate({ 
        rules: {
        	    name: {
                  required: true, 
                  checkNameNoChinese:true,
                  imageNameUseable:true
                },
                displayName: {
	              required: true,
	              maxlength:50,
	              minlength:2
                 },
                type: "required",
                hostType: "required",
                fromHostId: "required",
                description: {
                 maxlength:100,
                },
               },
     messages: { 
    	        name:{
    	        	required:"请输入镜像名称"
    	        },
    	        displayName:{
    	        	required:"请输入镜像显示名称",
    	        	maxlength:jQuery.validator.format("显示名称不能大于{0}个字 符"),
    	        	minlength:jQuery.validator.format("显示名称不能小于{0}个字 符")
    	        }, 
                type: {
                 required: "请选择类型"
                },
                hostType: {
                 required: "请选择仓库"
                },
                fromHostId: {
                 required: "请选择需要创建镜像的主机"
                },
                description:{
                 maxlength: jQuery.validator.format("描述最多{0}个字符")
                }
              }
 	});
	
	jQuery("#hostType").change(function(){ 
		getHostList();		
	});
});
function saveForm(){
	if(validator.form()){
		var options = {
				success:function result(data){ 
					if(data.status == "success"){
						jAlert('保存成功','提示', function(r) {
 							location.href = path + "/image/imagelist";
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

function getHostList(){
	var warehouseId = jQuery("#hostType option:selected").val();
	if(warehouseId == ""){
		jQuery("#fromHostId").html("");
		return;
	}
	jQuery.ajax({
        url: '../image/gethost',
        type: 'post',
        data: 'warehouseId=' + warehouseId,
        dataType: 'json',
        timeout: 10000,
        error: function()
        {
            alert('Error!');
        },
        success: function(result)
        {
        	var options = "<option value=''>请选择</option>  ";
        	jQuery.each(result.properties.hostList,function(idx,item){ //循环对象取值
        		if(item.realHostId != "" && item.userId !=null &&item.userId != ""){       			
        			options = options + "<option value='"+item.id+"'>用户 -"+item.userName+" 主机-"+item.displayName+"</option>";
        		}
            }); 
        	jQuery("#fromHostId").html(options);
        }
     });
}

function checkImageName(){
	var name = jQuery("#name").val();
	if(name == ""){
		return;
	}
	jQuery.ajax({
        url: '../image/checkimagename',
        type: 'post',
        data: 'name=' + name,
        dataType: 'json',
        timeout: 10000,
        async: false,
        error: function()
        {
            alert('Error!');
        },
        success: function(result)
        {
        	if(result.status == "fail"){
        		isNameUseable =  false;
        	}else{
        		isNameUseable =   true;       		
        	}
        }
     });
}

//不包括汉字
jQuery.validator.addMethod("checkNameNoChinese", function(value, element) {
	return this.optional(element) || (/^[a-zA-Z0-9,.,&,*,(,),!,~,#,$,%,^,-,_'"]*$/.test(value)&&value.length>=2&&value.length<=50) ;
}, "镜像名称由字母、数字和特殊字符组成,长度为2-50字符");
 
//镜像名是否可用
jQuery.validator.addMethod("imageNameUseable", function(value, element) {
	checkImageName();
 	return this.optional(element) || isNameUseable;
}, "该镜像名已经存在");

/**
 * 根据id更新主机类型
 * @param id
 */
function updateImage(id){
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
 	     	   	        jQuery.blockUI({ message: jQuery("#newImage"),  
	     	   	        css: {border:'3px solid #aaa',
	     	   	              backgroundColor:'#FFFFFF',
	     	   	              overflow: 'hide',
	     	   	              width: '80%', 
	     	   	              height: 'auto', 
	     	   	              left:'100px',
	     	   	              top:'50px'} 
	     	   	        }); 
     	   	    		jQuery("#newImage h1:eq(0) span").html("镜像修改");
     	   	   			jQuery("#imageform #name").attr("disabled","disabled");
      	   	    		jQuery.get(path + "/image/"+id+"/getimage",function(data){
     	   	     			jQuery("#imageform #name").val(data.name);
     	   	     			jQuery("#imageform #displayName").val(data.displayName);
     	   	    			jQuery("#notforedit").hide();   			 
     	   	    			jQuery("#imageform #description").val(data.description);
     	   	    			jQuery("#imageform").attr("action",path + "/image/"+data.id+"/update");
     	   	    		}); 
	     	   	        jQuery('.blockOverlay').attr('title','单击关闭').click(jQuery.unblockUI);  
	        	} 
        }
     });
    
}
/**
 * 根据id删除主机类型
 * @param id
 */
function deleteImage(id){ 
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
		        		jConfirm('确定要删除该镜像吗？', '确认', function(r) {
	 	            		if(r == true){
	 	       	    		jQuery.get(path + "/image/"+id+"/delete",function(data){
	 	       	    			if(data.status == "success"){   
	 	       	    				jAlert(data.message,'成功', function(r) {
	 	       			    			location.href = path + "/image/imagelist";
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
