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
var isNameUseable = true;
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
    
    ///// 主机类型创建表单验证 /////
	validator = jQuery("#hosttypeform").validate({ 
        rules: {
        	    name: {
                  required: true,
                  maxlength:30,
                  typeNameUseable:true
                },
                allocationType: "required",
                cpuCore: "required",
                memory: "required",
                dataDisk: "required",
                diskdiy: {
                 required: true,
                 min:1,
                 max:1000
                },
                bandwidth: "required",
                bandwidthdiy: {
                 required: true,
                 min:1,
                 max:1000
                },
                sysImageId: {
                 required: true
                },
                password: {
                 required: true,
                 minlength: 5
                },
                confirm_password: {
                 required: true,
                 minlength: 5,
                 equalTo: "#password"
                },
                description: {
                 maxlength:50,
                },
               },
     messages: {
    	 		name: {
                 required: "请输入类型名称",
                 maxlength: "类型名称不能大于30个字符"
                },
                allocationType: {
                 required: "请选择常用配置"
                },
                cpuCore: {
                 required: "请选择cpu"
                },
                memory: {
                 required: "请选择内存"
                },
                dataDisk: {
                 required: "请选择硬盘"
                },
                diskdiy:{
                 required: "请输入自定义硬盘大小",
                 max:"硬盘不能大于1000G",
                 min:"硬盘不能小于1G"
                },
                bandwidth: {
                 required: "请选择带宽"
                },
                bandwidthdiy:{
                 required: "请输入自定义带宽大小",
                 max:"带宽不能大于1000M",
                 min:"带宽不能小于1M"
                },
                sysImageId:{
                 required:"请选择镜像"
                },
                email: {
                 required: "请输入Email地址",
                 email: "请输入正确的email地址"
                },
                password: {
                 required: "请输入密码",
                 minlength: jQuery.validator.format("密码不能小于{0}个字 符")
                },
                confirm_password: {
                 required: "请输入确认密码",
                 minlength: "确认密码不能小于5个字符",
                 equalTo: "两次输入密码不一致不一致"
                },
                description:{
                 maxlength: jQuery.validator.format("描述最多{0}个字符")
                }
              }
 	});
	
///// 镜像表单创建表单验证 /////
	imagevalidator = jQuery("#imageform").validate({ 
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
                 required: "请选择主机类型"
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
	jQuery.ajax({
        url: path+'/main/checklogin',
        type: 'post', 
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
        		jAlert('登录或以超时，请重新登录','提示', function(r) {
	    			top.document.location.href = path;
	    			});
	        	}else{
	        		if(validator.form()){
	        			var options = {
	        					success:function result(data){
	        						location.href = path + "/chcm/all";
	        					},
	        					dataType:'json',
	        					timeout:10000
	        			};
	        			jQuery("#hosttypeform").ajaxForm(options);
	        		}
	        	} 
        }
     }); 
	
}
function saveImageForm(){
	jQuery.ajax({
        url: path+'/main/checklogin',
        type: 'post', 
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
        		jAlert('登录或以超时，请重新登录','提示', function(r) {
	    			top.document.location.href = path;
	    			});
	        	}else{
	        		if(imagevalidator.form()){
	        			var options = {
	        					success:function result(data){ 
	        						jQuery('#showCreateType').click();
//	        						jQuery("#image_list option[value='fOption']").after("<option value="+data.properties.id+">"+data.properties.name+"</option>");
//	        						jQuery("#image_list option[value="+data.properties.id+"]").attr("selected",true);
	        					},
	        					dataType:'json',
	        					timeout:10000
	        			};
	        			jQuery("#imageform").ajaxForm(options);
	        		}
	        	} 
        }
     }); 
	
}

function resetForm(){
	validator.resetForm();
}
function imageresetForm(){
	imagevalidator.resetForm();
}
function getHostList(){
	var warehouseId = jQuery("#hostType option:selected").val();
	if(warehouseId == ""){
		jQuery("#fromHostId").html("");
		return;
	}
	jQuery.ajax({
        url: path+'/image/gethost',
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
        		if(item.userId != ""){       			
        			options = options + "<option value='"+item.id+"'>用户 -"+item.userName+" 主机-"+item.displayName+"</option>";
        		}
            }); 
        	jQuery("#fromHostId").html(options);
        }
     });
}

function checkName(name){
 	if(name == ""){
		return;
	}
 	else{
 		if(jQuery("#oldtypename").val() == name){
 			return;
 		}
 	}
	jQuery.ajax({
        url: path+'/chcm/checktypename',
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
return this.optional(element) || /^\w{2,50}$/.test(value) ;
}, "镜像名称由字母、数字和下划线组成,长度为2-50字符");
//镜像名是否可用
jQuery.validator.addMethod("typeNameUseable", function(value, element) {
	checkName(value);
 	return this.optional(element) || isNameUseable;
}, "该类型名已经存在");
/**
 * 根据id更新主机类型
 * @param id
 */
function updateType(id){
	jQuery.ajax({
        url: path+'/main/checklogin',
        type: 'post', 
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
        		jAlert('登录或以超时，请重新登录','提示', function(r) {
	    			top.document.location.href = path;
	    			});
	        	}else{
	        		validator.form();
	        		jQuery.blockUI({ message: jQuery("#newType"),  
	        		    css: {border:'3px solid #aaa',
	        		          backgroundColor:'#FFFFFF',
	        		          overflow: 'hide',
	        		          width: '80%', 
	        		          height: 'auto', 
	        		          left:'100px',
	        		          top:'40px'} 
	        		    }); 
	        		 	jQuery("#newType h1:eq(0) span").html("主机类型修改");
	        		 	jQuery.get(path+"/chcm/"+id+"/update",function(data){
	        				jQuery("#hosttypeform #typename").val(data.name);
	        				jQuery("#oldtypename").val(data.name);
	        				jQuery("#hosttypeform #is_diy").attr("checked","checked");
	        				jQuery("#hosttypeform #is_diy").click();
	        				jQuery("#hosttypeform #cpu option[value="+data.cpuCore+"]").attr("selected",true);
	        				jQuery("#hosttypeform #cur_memory option[value="+data.memoryText+"]").attr("selected",true);
	        				jQuery("#hosttypeform #disk_radio").attr("checked","checked");
	        				jQuery("#hosttypeform #disk_radio").click();
	        				jQuery("#hosttypeform #diskdiy").val(data.dataDiskText);
	        				jQuery("#hosttypeform #bandwidth_radio").attr("checked","checked");
	        				jQuery("#hosttypeform #bandwidth_radio").click();
	        				jQuery("#hosttypeform #bandwidthdiy").val(data.bandwidthText);
	        				jQuery("#hosttypeform #image_list").val(data.sysImageId);
	        				jQuery("#hosttypeform").attr("action",path + "/chcm/"+data.id+"/update");
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
function deleteType(id){ 
	jQuery.ajax({
        url: path+'/main/checklogin',
        type: 'post', 
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
        		jAlert('登录或以超时，请重新登录','提示', function(r) {
	    			top.document.location.href = path;
	    			});
	        	}else{ 
	        		
	        		jConfirm('确定要删除该类型吗？', '确认', function(r) {
 	            		if(r == true){
 	            			jQuery.get(path + "/chcm/"+id+"/delete",function(data){
 		        				if(data.status == "success"){	        					
 	 	        					location.href = path+"/chcm/all"; 
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
