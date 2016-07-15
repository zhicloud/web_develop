<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!-- sys_group_manage.jsp -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.default.css" type="text/css" /> 
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/jquery.treetable.css" type="text/css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery-1.7.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.uniform.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.tagsinput.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/charCount.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/ui.spinner.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/chosen.jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/group.forms.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.uniform.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.blockUI.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/tables.js"></script>  
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.alerts.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.treetable.js"></script>
  
<script type="text/javascript">
var path = "<%=request.getContextPath()%>";

jQuery(document).ready(function(){
	  //默认选中
    var select = '<a href="#" id="showCreateGroup" class="btn btn_add radius50"><span>新增分组</span></a>';
    
      jQuery("#dyntable_length").append(select);
      
/*       jQuery("#dyntable").treetable({ expandable: true });
 */      
      jQuery("#showCreateGroup").click(function(){
    	var $this = jQuery(this);
        jQuery.blockUI({ message: jQuery("#newGroup"),  
        css: {border:'3px solid #aaa',
              backgroundColor:'#FFFFFF',
              overflow: 'hide',
              width: '85%', 
              height: 'auto', 
              left:'100px',
              top:'50px'} 
        });
        jQuery("#newGroup h1:eq(0) span").html("新增分组");
        jQuery("#sys_group_form").attr("action","${pageContext.request.contextPath}/group/add");
        jQuery.get("${pageContext.request.contextPath}/group/list",function(data){
        	var options = "<option value=''>请选择</option>  ";
        	jQuery.each(data.properties.sys_group_list,function(idx,item){ //循环对象取值
    	 		options = options + "<option value='"+item.id+"'>"+item.groupName+"</option>";
    	     }); 
    		jQuery("#group_list").html(options);
        })
        jQuery("#sys_group_form #group_list option[value='']").attr("selected", true);
        jQuery('.blockOverlay').attr('title','单击关闭').click(jQuery.unblockUI); 
        /* jQuery.post("${pageContext.request.contextPath}/sysimage/all",function(data){
			if(false){
				
			}
		}); */
      });
      
  
    
    jQuery("#save_btn").click(function(){
    	var options = {
				success:function result(data){
					location.href = "${pageContext.request.contextPath}/group";
				},
				dataType:'json',
				timeout:10000
		};
		jQuery("#sys_group_form").ajaxForm(options);
     });
    
    jQuery("#save_member_btn").click(function(){
    	var group_id = jQuery("#manage_member_form #group_id").val();
    	var user_id_out = new Array();
    	var user_id_in = new Array();
    	
    	var user_in;
    	
    	var i = 0;
    	jQuery("#manage_member_form #terminal_ids_out option").each(function(){
    		user_id_out[i] = jQuery(this).val();
   			i++;
    	});
    	var user_out = "";  
    	for(var i = 0;i<user_id_out.length;i++){

    	     if(i!=user_id_out.length-1){

    	          user_out=user_out+user_id_out[i]+",";

    	     }else{

    	    	 user_out=user_out+user_id_out[i];

    	     }

    	}
    	
    	var j = 0;
    	jQuery("#manage_member_form #terminal_ids_in option").each(function(){
    		
    		user_id_in[j] = jQuery(this).val();
    		j++;
    	});
    	var user_in = "";
    	for(var i = 0;i<user_id_in.length;i++){

   	     if(i!=user_id_in.length-1){

   	          user_in=user_in+user_id_in[i]+",";

   	     }else{

   	    	user_in=user_in+user_id_in[i];

   	     }

   		}

		jQuery.get("${pageContext.request.contextPath}/group/item/manage", 
				{"groupId":group_id, "userIdOut":user_out, "userIdIn":user_in},
				function(data){
					location.href = "${pageContext.request.contextPath}/group";					
				});
    });
    
//     jQuery.get("${pageContext.request.contextPath}/group/amount", function(data){
//     	jQuery.each(data.properties.amount_list,function(idx,item){ //循环对象取值
//      		jQuery("#"+item.id+"_amount").html(item.amount);
//      		jQuery("#"+item.id+"_cloud_host_amount").html(item.cloudHostAmount);
//          }); 
//     });
    
});

function getCloudHostAmount(){
	jQuery.get("${pageContext.request.contextPath}/group/amount", function(data){
    	jQuery.each(data.properties.amount_list,function(idx,item){ //循环对象取值
     		jQuery("#"+item.id+"_amount").html(item.amount);
     		jQuery("#"+item.id+"cloud_host_amount").html(item.cloudHostAmount);
         }); 
    });
};

function updateGroup(id){
	var $this = jQuery(this);
    jQuery.blockUI({ message: jQuery("#newGroup"),  
    css: {border:'3px solid #aaa',
          backgroundColor:'#FFFFFF',
          overflow: 'hide',
          width: '80%', 
          height: 'auto', 
          left:'100px',
          top:'50px'} 
    });
	jQuery("#newGroup h1:eq(0) span").html("分组修改");
	jQuery.get("${pageContext.request.contextPath}/group/"+id+"/update",function(data){
		
		jQuery("#sys_group_form #group_name").val(data.properties.sys_group.groupName);
		jQuery("#sys_group_form #group_name").attr("oldname",data.properties.sys_group.groupName);
	
		var options = "<option value=''>请选择</option>  ";
		jQuery.each(data.properties.parent_id_list,function(idx,item){ //循环对象取值
	 		options = options + "<option value='"+item.id+"'>"+item.groupName+"</option>";
	     }); 
		jQuery("#group_list").html(options);
		
		jQuery("#sys_group_form #group_list option[value="+data.properties.sys_group.parentId+"]").attr("selected", true);
		
		jQuery("#sys_group_form #description").val(data.properties.sys_group.description);
		
		jQuery("#sys_group_form").attr("action","${pageContext.request.contextPath}/group/"+data.properties.sys_group.id+"/update");
	});
}

function deleteGroup(id){
	jConfirm('确定要删除该群组吗？', '确认', function(r) {
 		if(r == true){
 			jQuery.get("${pageContext.request.contextPath}/group/"+id+"/delete",
 					function(data){
 						location.href = "${pageContext.request.contextPath}/group";
 					});	           			
			}
	});
}

function manageItem(id){
	var $this = jQuery(this);
    jQuery.blockUI({ message: jQuery("#manageMember"),  
    css: {border:'3px solid #aaa',
          backgroundColor:'#FFFFFF',
          overflow: 'scroll',
          width: '80%', 
          height: '80%', 
          left:'100px',
          top:'90px'} 
    });
    jQuery.get("${pageContext.request.contextPath}/group/"+id+"/item", function(data){
    	jQuery.each(data, function(n, value){
    		jQuery("#terminal_ids_in").append("<option value="+value.id+">"+value.username+"</option>");
    	});
    });
    jQuery("#manage_member_form #group_id").val(id);
}
    
</script>
<div class="centercontent_block tables" id="mainpage">
        <div id="contentwrapper_1" class="contentwrapper tables"> 
            <div class="contenttitle2">
                	<h3>分组管理</h3>
                </div>
                <table cellpadding="0" cellspacing="0" border="0" class="stdtable" id="dyntable" style="font-size:12px;">
                    <colgroup>
                        <col class="con0" />
                        <col class="con1" />
                        <col class="con0" />
                        <col class="con1" />
                        <col class="con0" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th class="head1">分组名</th>
                            <th class="head0">成员数</th>
                            <th class="head1">分配云主机数</th>
                            <th class="head0">描述</th>
                            <th class="head1 nosort">操作</th>
                        </tr>
                    </thead> 
                    <tbody>
                    	<c:forEach items="${sys_group_list}" var="sys_groups">
<%--                     	<tr class="gradeX" data-tt-id="${sys_groups.id}" data-tt-parent-id="${sys_groups.parentId}">
 --%>                       <tr class="gradeX">
 							<td>${sys_groups.groupName}</td>
                            <td>${sys_groups.amount}</td>
                            <td>${sys_groups.cloudHostAmount}</td>
                            <td>${sys_groups.description}</td>
                            <td class="operatoricon">
                              <a group_id="${sys_groups.id }" onclick="manageItem('${sys_groups.id }');" href="#" class="btn btn3 btn_dispach manage_member_btn" title="成员管理"></a>
                              <a update_id="${sys_groups.id }" onclick="updateGroup('${sys_groups.id }');" href="#" class="btn btn3 btn_pencil update_group_btn" title="更新"></a>
                              <a delete_id="${sys_groups.id }" onclick="deleteGroup('${sys_groups.id }');" href="#" class="btn btn3 btn_trash delete_group_btn" title="删除"></a>
                        	</td>
                        </tr>
                    	</c:forEach>
                    </tbody>
                </table>
                <br>
                <br>
        	 
        
        </div><!--contentwrapper-->
</div>
        
        <br clear="all" />
    <div class="bodywrapper" style="display:none;" id="newGroup">  
    
        <div class="formtitle">
            <div class="left">
                <h1 class="logo"><span>新增分组</span></h1> 


                <br clear="all" />

            </div><!--left--> 
        
         
       </div>
        <div class="centercontent_block" style="margin-left:0;"> 

            <div  class="contentwrapper" >
               
                <div  class="subcontent">  

                        <form class="stdform" action = "<%=request.getContextPath() %>/group/add" method = "post" id="sys_group_form"  >

                            <p>
                                <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">分组名</label>
                                <span class="field"><input type="text" name="groupName" id="group_name" class="smallinput " oldname = "" /></span>
                                <small class="desc"></small>
                            </p>
                         
                            <p>
                                <label>上级分组</label>
                                <span class="field">
                                <select id="group_list" name="parentId" class="bigselect" style="width:270px;">
                                </select> 
                                </span>
                            </p>

                            <p>
                                <label>描述</label>
                                <span class="field"><textarea cols="4" id ="description" name="description" rows="3" class="longinput"></textarea></span> 
                            </p>



                            <p class="stdformbutton">
                                <button id="save_btn" class="submit radius2">保存</button>
                                <input  onclick="jQuery.unblockUI();" type="reset" class="reset radius2" value="取消" />
                            </p>


                        </form>

                        <br /> 

                </div><!--subcontent-->

            </div><!--contentwrapper-->

        </div><!-- centercontent -->


    </div> 
    
         <br clear="all" />
    <div class="bodywrapper" style="display:none;" id="manageMember">  
    
        <div class="formtitle">
            <div class="left">
                <h1 class="logo"><span>成员管理</span></h1> 


                <br clear="all" />

            </div><!--left--> 
        
         
       </div>
        <div class="centercontent_block" style="margin-left:0;"> 

            <div  class="contentwrapper" >
               
                <div  class="subcontent">  

                        <form class="stdform" id="manage_member_form"  >
								<input type="hidden" id = "group_id" value="" />
                            <p>
                        	<label>成员管理</label>
                            <span id="dualselect" class="dualselect">
                            	<select id = "terminal_ids_out" class="uniformselect" name="terminal_ids_out" multiple="multiple" size="10">
                                    <c:forEach items="${terminal_user_list_out}" var="terminal_user_out">
                                    	<option value="${terminal_user_out.id}">${terminal_user_out.username}</option>
                    				</c:forEach>
                                </select>
                                <span class="ds_arrow">
                                	<span class="arrow ds_prev">&laquo;</span>
                                    <span class="arrow ds_next">&raquo;</span>
                                </span>
                                
                                <select id = "terminal_ids_in" name="terminal_ids_in" multiple="multiple" size="10" >
                                
                                </select>
                                
                            </span>
                        </p>



                            <p class="stdformbutton">
                                <button id="save_member_btn" class="submit radius2">保存</button>
                                <input  onclick="jQuery.unblockUI();" type="reset" class="reset radius2" value="取消" />
                            </p>


                        </form>

                        <br /> 

                </div><!--subcontent-->

            </div><!--contentwrapper-->

        </div><!-- centercontent -->


    </div>
