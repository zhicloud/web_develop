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
	validator = jQuery("#addAmountForm").validate({ 
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
});

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


function saveAddAmount(){
	if(validator.form()){
		var options = {
				success:function result(data){
					if(data.status=="success"){
						jAlert(data.message,'提示', function(r) {
							location.href=path +"/warehouse/all"; 
						});
					}else{
						jAlert(data.message,'提示', function(r) {
							return;
						});
					}
				},
				dataType:'json',
				timeout:10000
		};
		jQuery("#addAmountForm").ajaxForm(options);
	}
}
/**
 * 分配主机给用户
 * @param id
 */
function saveAssignToUserTwo(){ 
	var nodes = jQuery("#users_tree_two").tree("getChecked");
	var checkedNodes = [];
	for(var i=0;i<nodes.length;i++){
		if(nodes[i].iconCls=="icon-user"){
			 checkedNodes.push(nodes[i].id);
		}
	}
	if(checkedNodes.length < 1){
		jAlert('您尚未选择要分配的用户','提示', function(r) {
			return;
		});
	}else if(checkedNodes.length > hostIds.length){
		jAlert('请最多选择'+hostIds.length+'位用户进行分配','提示', function(r) {
			return;
		});
	}else{
		jQuery.ajax({
			url: path+'/warehouse/assigntouserstwo',
			type: 'post', 
			data:{warehouseId:warehouseId,allNodes:checkedNodes,hostIds:hostIds},
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
/**
 * 分配主机给单个用户
 * @param id
 */
function saveAssignToOneUser(){ 
	var nodes = jQuery("#users_tree_two").tree("getChecked");
	var checkedNodes = [];
	for(var i=0;i<nodes.length;i++){
		if(nodes[i].iconCls=="icon-user"){
			 checkedNodes.push(nodes[i].id);
		}
	}
	if(checkedNodes.length < 1){
		jAlert('您尚未选择要分配的用户','提示', function(r) {
			return;
		});
	}else if(checkedNodes.length > 1){
		jAlert('请选择1位用户进行分配','提示', function(r) {
			return;
		});
	}else{
		jQuery.ajax({
			url: path+'/warehouse/assigntouserstwo',
			type: 'post', 
			data:{warehouseId:warehouseId,allNodes:checkedNodes,hostIds:oneCloudHostId},
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