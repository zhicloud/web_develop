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
jQuery.ajaxSetup({
	async:false
});
/*jQuery(document).ready(function(){
	jQuery.validator.addMethod("checkName",function(value,element){
		var flag = false;
		var $oldName = jQuery("#old_name").val();
		if(value == $oldName){
			flag = true;
		}else{
			jQuery.ajax({
				type:"post",
				url:path + "/warehouse/checkname",
				data:{name:value},
				async:false,
				success:function(data){
					if(data.status=="success"){
						flag = true;
					}
				}
			});
		}
		return flag;
	},"该名称已存在，请重新输入");
	jQuery.validator.addMethod("checkAmount",function(value,element){
		var oldAmount = jQuery("#old_amount_input").val();
		if(oldAmount>value){
			return false;
		}else{
			return true;
		}
	},"修改的数量不能少于原有数量");
	
	validator2 = jQuery("#hosttypeform").validate({ 
        rules: {
        	    name: {
                  required: true,
                  maxlength:30
                },
                allocationType: "required",
                cpuCore: "required",
                memory: "required",
                dataDisk: "required",
                diskdiy: {
                 required: true,
                 min:1,
                 max:100
                },
                bandwidth: "required",
                bandwidthdiy: {
                 required: true,
                 min:1,
                 max:10
                },
                sysImageName: {
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
                 max:"硬盘小于100G",
                 min:"硬盘需要大于1G"
                },
                bandwidth: {
                 required: "请选择带宽"
                },
                bandwidthdiy:{
                 required: "请输入自定义带宽大小",
                 max:"带宽小于10M",
                 min:"带宽需要大于1M"
                },
                sysImageName:{
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
	
	validator3 = jQuery("#addAmountForm").validate({ 
		        rules: {
		                addAmount: {
		                 required: true,
		                 max:100,
		                 min:1
		                }
		               },
		     messages: {
		                addAmount: {
		                 required: "请输入增加的主机数",
		                 max:"最多增加100个主机",
		                 min:"最少增加1台云主机"
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
    	        	required:"请输入主机名称"
    	        },
    	        displayName:{
    	        	required:"请输入显示名称",
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
});*/



function saveWarehouseForm(){
		var options = {
				success:function result(data){
					if(data.status=="success"){
						location.href = path+"/warehouse/all";
					}else{
						jQuery("#tipscontent").html(data.message);
		   		        jQuery("#dia").click();
						return;
					}
				},
				dataType:'json',
				timeout:10000
		};
		var form = jQuery("#hostwarehouseform");
		form.parsley('validate');
		if(form.parsley('isValid')){ 
			$("#create_btn").attr("disabled",true);
			jQuery("#hostwarehouseform").ajaxSubmit(options); 
		}
}
function saveAddAmount(){
	var options = {
			success:function result(data){
				if(data.status=="success"){
					location.href = path+"/warehouse/all";
				}else{
					jQuery("#my_reset").click();
					jQuery("#tipscontent").html(data.message);
	   		        jQuery("#dia").click();
					return;
				}
			},
			dataType:'json',
			timeout:10000
	};
	var form = jQuery("#addAmountForm");
	form.parsley('validate');
	if(form.parsley('isValid')){  		        				
		jQuery("#addAmountForm").ajaxSubmit(options); 
	}
}
/*function saveImageForm(){
	if(imagevalidator.form()){
		var options = {
				success:function result(data){ 
					jQuery('#showCreateType').click();
//					jQuery("#image_list option[value='fOption']").after("<option value="+data.properties.id+">"+data.properties.name+"</option>");
//					jQuery("#image_list option[value="+data.properties.id+"]").attr("selected",true);
				},
				dataType:'json',
				timeout:10000
		};
		jQuery("#imageform").ajaxForm(options);
	}
}

function imageresetForm(){
	imagevalidator.resetForm();
}*/
/*function getHostList(){
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

function checkImageName(){
	var name = jQuery("#name").val();
	if(name == ""){
		return;
	}
	jQuery.ajax({
        url: path+'/image/checkimagename',
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
}*/

//不包括汉字
/*jQuery.validator.addMethod("checkNameNoChinese", function(value, element) {
return this.optional(element) || /^\w{2,50}$/.test(value) ;
}, "镜像名称由字母、数字和下划线组成,长度为2-50字符");
//镜像名是否可用
jQuery.validator.addMethod("imageNameUseable", function(value, element) {
	checkImageName();
 	return this.optional(element) || isNameUseable;
}, "该镜像名已经存在");*/


/**
 * 根据id更新主机类型
 * @param id
 */
function updateWare(id,totalAmount){
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
        			alert("登录超时，请重新登录");
	    			top.document.location.href = path;
	        	}else{
 		            window.location.href = path+"/warehouse/"+id+"/update"; 
	        	} 
        }
     });
    
}
/**
 * 根据id删除仓库
 * @param id
 */
function deleteWare(id){ 
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
        		jQuery("#tipscontent").html("登录超时，请重新登录");
   		        jQuery("#dia").click();
	    		top.document.location.href = path;
	        	}else{
	        		curId = id;
	        		jQuery("#confirmcontent").html("确定要删除该仓库吗？");
	    	    	jQuery("#confirm_btn").attr("onclick","deleteWarehouse();");
	    	    	jQuery("#con").click();
	        	} 
        }
     });
	
 }
/**
 * 通过仓库id新增仓库库存数
 * @param id
 */
function addHosts(id,hostType,fileType){ 
	if(hostType==null || hostType=="" || hostType==undefined){
		$("#tipscontent").html('主机类型已失效，无法增加该类型数量');
		$("#dia").click();
		return;
	}
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
				$("#tipscontent").html('登录或以超时，请重新登录');
				$("#dia").click();
				top.document.location.href = path;
			}else{ 
				//增加云主机
				$("#warehouse_id").val(id);
				if(fileType == 1){
					$("#mform2").click();
				}else{
	 				$("#mform").click();
				}
				return;
//				window.location.href = path+"/warehouse/"+id+"/addAmount";
 			} 
		}
	});
	
}
/**
 * 分配主机给用户
 * @param id
 */
function assignWare(id){ 
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
				//分配主机
		    	jQuery.blockUI({ message: jQuery("#assign_to_user_div"),  
		            css: {border:'3px solid #aaa',
		                  backgroundColor:'#FFFFFF',
		                  overflow: 'hide',
		                  width: '40%', 
		                  height: 'auto', 
		                  left:'400px',
		                  top:'100px'} 
		            });
		          jQuery('.blockOverlay').attr('title','单击关闭').click(jQuery.unblockUI);
 			} 
		}
	});
	
}
/**
 * 查看仓库详情
 * @param id
 */
function toDetail(id){ 
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
				alert("登录超时，请重新登录");
				top.document.location.href = path;
			}else{ 
				location.href=path +"/warehouse/cloudhost/"+id+"/all"; 
			} 
		}
	});
	
}
/**
 * 分配主机给用户
 * @param id
 */
function saveAssignToUser(){ 
	var nodes = jQuery("#users_tree").tree("getChecked");
	var checkedNodes = [];
	for(var i=0;i<nodes.length;i++){
		if(nodes[i].iconCls=="icon-user"){
			 checkedNodes.push(nodes[i].id);
		}
	}
	if(cur_warehouse_remain_amount == 0){
		jAlert('该仓库无可分配云主机','提示', function(r) {
			return;
		});
	}else if(checkedNodes.length < 1){
		jAlert('您尚未选择要分配的用户','提示', function(r) {
			return;
		});
	}else if(checkedNodes.length > cur_warehouse_remain_amount){
		jAlert('请最多选择'+cur_warehouse_remain_amount+'位用户进行分配','提示', function(r) {
			return;
		});
	}else{
		jQuery.ajax({
			url: path+'/warehouse/assigntousers',
			type: 'post', 
			data:{warehouseId:cur_warehouse_id,allNodes:checkedNodes},
			dataType: 'json',
			timeout: 10000,
			async: false,
			success:function(data){
				if(data.status == "success"){
					jAlert(data.message,'提示', function(r) {
						location.href=path +"/warehouse/all"; 
					});
				}else if(data.status == "fail"){
					jAlert(data.message,'提示', function(r) {
						return;
					});
				}
			}
		});
	}
}
