<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.default.css" type="text/css" /> 
<link rel="stylesheet" href="<%=request.getContextPath()%>/themes/default/easyui.css" type="text/css" /> 
<link rel="stylesheet" href="<%=request.getContextPath()%>/themes/icon.css" type="text/css" /> 

<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery-1.7.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.uniform.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.tagsinput.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/charCount.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/ui.spinner.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/chosen.jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/cloudhostform.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.uniform.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.blockUI.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.alerts.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.easyui.min.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/tables.js"></script>    
<script type="text/javascript">
 
 
var path = '<%=request.getContextPath()%>'; 
var warehouseId = '${warehouseId}';
var hostIds = [];
var oneCloudHostId = [];
var sysImageName = '${warehouse.sysImageName}';
jQuery(document).ready(function(){
	  //默认选中
    var select = '<a href="#" id="showAddMore" class="btn btn_add radius50"><span>增加该类型云主机</span></a>';
 
      jQuery("#dyntable2_length").append(select);
      jQuery("#showAddMore").after("&nbsp;&nbsp;<a href='#' id='delete_more' class='btn btn_trash_big radius50'><span>批量删除</span></a>");
      jQuery("#delete_more").after("&nbsp;&nbsp;<a href='#' id='assign_to_user' class='btn btn_dispach_big radius50'><span>分配至用户</span></a>");
      jQuery("#showAddMore").click(function(){
    	var $this = jQuery(this);
    	if(sysImageName==null || sysImageName=="" || sysImageName==undefined){
    		jAlert('主机类型已失效，无法增加该类型数量','提示', function(r) {
    		});
    		return;
    	}
        jQuery.blockUI({ message: jQuery("#add_more_cloud_host_div"),  
        css: {border:'3px solid #aaa',
              backgroundColor:'#FFFFFF',
              overflow: 'hide',
              width: '80%', 
              height: 'auto', 
              left:'100px',
              top:'50px'} 
        });
        jQuery('.blockOverlay').attr('title','单击关闭').click(jQuery.unblockUI); 
      });
    jQuery("#showCreateNewImage").click(function(){  
        jQuery.blockUI({ message: jQuery("#newImage"),  
        	css: {border:'3px solid #aaa',
                backgroundColor:'#FFFFFF',
                overflow: 'hide',
                width: '80%', 
                height: 'auto', 
                left:'100px',
                top:'50px'} 
        }); 
       }); 
    //删除云主机
    jQuery(".delete_one_host_btn").click(function(){
    	var id = jQuery(this).attr("cur_id");
    	jConfirm('确定要删除该主机吗？', '确认', function(r) {
     		if(r == true){
	    		jQuery.get(path + "/cloudhost/"+id+"/delete",function(data){
	    			if(data.status == "success"){   
	    				jAlert(data.message,'成功', function(r) {
			    			location.href = path + "/cloudhost/${warehouseId}/all";
	 	    			});
	    			}else{  
	    				jAlert(data.message,'提示'); 	    				
	    			}
	    		});
    			
    		}
		}); 
    });
    //启动云主机
    jQuery(".start_host_btn").click(function(){
    	var id = jQuery(this).attr("cur_id");
    	jConfirm('确定要开启该主机吗？', '确认', function(r) {
     		if(r == true){
	    		jQuery.get(path + "/cloudhost/"+id+"/start",function(data){
	    			if(data.status == "success"){   
	    				jAlert(data.message,'成功', function(r) {
			    			location.href = path + "/cloudhost/${warehouseId}/all";
	 	    			});
	    			}else{  
	    				jAlert(data.message,'提示'); 	    				
	    			}
	    		});
    			
    		}
		});  
    });
  //关机云主机
    jQuery(".shutdown_host_btn").click(function(){
    	var id = jQuery(this).attr("cur_id");
    	jConfirm('确定要关闭该主机吗？', '确认', function(r) {
     		if(r == true){
	    		jQuery.get(path + "/cloudhost/"+id+"/shutdown",function(data){
	    			if(data.status == "success"){   
	    				jAlert(data.message,'成功', function(r) {
			    			location.href = path + "/cloudhost/${warehouseId}/all";
	 	    			});
	    			}else{  
	    				jAlert(data.message,'提示'); 	    				
	    			}
	    		});
    			
    		}
		});  
    });
    //重启云主机
    jQuery(".restart_host_btn").click(function(){
    	var id = jQuery(this).attr("cur_id");
    	jConfirm('确定要重启该主机吗？', '确认', function(r) {
     		if(r == true){
	    		jQuery.get(path + "/cloudhost/"+id+"/restart",function(data){
	    			if(data.status == "success"){   
	    				jAlert(data.message,'成功', function(r) {
			    			location.href = path + "/cloudhost/${warehouseId}/all";
	 	    			});
	    			}else{  
	    				jAlert(data.message,'提示'); 	    				
	    			}
	    		});
    			
    		}
		});  
    });
    //分配给用户
    jQuery("#assign_to_user").click(function(){
    	jQuery("#sava_assign_form_btn").attr("onclick","saveAssignToUserTwo();");
    	var ids = [];
    	var flag = false;
		 jQuery("[name='idcheck'][checked='checked']:checkbox").each(function(){ 
            if(jQuery(this).attr("isAssigned")!='' || jQuery(this).attr("realHostId")==''){
            	alert('请选择有效的云主机进行分配');
            	flag = true;
            	return false;
            }
            ids.push(jQuery(this).val());
        });
		if(flag){
			return;
		}
        if(ids.length<1){
        	jAlert('请至少选择一台主机进行分配','提示'); 
        	return;
        }else{
        	hostIds = ids;
        	jQuery.blockUI({ message: jQuery("#assign_to_user_div_two"),  
                css: {border:'3px solid #aaa',
                      backgroundColor:'#FFFFFF',
                      overflow: 'hide',
                      width: '45%', 
                      height: '50%', 
                      left:'280px',
                      top:'50px'}
                });
              jQuery('.blockOverlay').attr('title','单击关闭').click(jQuery.unblockUI); 
        }
    }); 
    
  //分配给单个用户
    jQuery(".assign_to_one_user").click(function(){
    	jQuery("#sava_assign_form_btn").attr("onclick","saveAssignToOneUser();");
    	oneCloudHostId.push(jQuery(this).attr("cur_id"));
       	jQuery.blockUI({ message: jQuery("#assign_to_user_div_two"),  
            css: {border:'3px solid #aaa',
                  backgroundColor:'#FFFFFF',
                  overflow: 'hide',
                  width: '45%', 
                  height: '50%', 
                  left:'280px',
                  top:'50px'} 
   		});
   		jQuery('.blockOverlay').attr('title','单击关闭').click(jQuery.unblockUI);
    });
    jQuery("#deleteAll").click(function(){
    	
    	if(jQuery(this).is(":checked")){
    		jQuery(".idcheck:checkbox").each(function(){
    			jQuery(this).attr("checked","checked");
    		});
    		jQuery(".paginate_active").click();
    	}else{
    		jQuery(".idcheck:checkbox").each(function(){
    			jQuery(this).removeAttr("checked");
    		});
    		jQuery(".paginate_active").click();
    	}
    });
    jQuery("#delete_more").click(function(){
		 var ids = "";
		 jQuery("input[name='idcheck']:checkbox").each(function(){ 
            if(jQuery(this).attr("checked")){
                ids += jQuery(this).val()+","
            }
        })
        if(ids == ""){
       	 jAlert("请选择主机",'提示'); 
       	 return;
        }   
		jQuery.ajax({
	        url: '<%=request.getContextPath()%>/cloudhost/deletehosts',
	        type: 'post', 
	        dataType: 'json',
	        data:"ids="+ids,
	        timeout: 10000,
	        error: function()
	        { 
	        },
	        success: function(result)	        
	        {  
	        	jAlert(result.message,'成功', function(r) {
	    			location.href = path + "/cloudhost/${warehouseId}/all";
	    			});
	        }
	        
	     });  
     });  
   
});
function toDetail(id){
	location.href = path + "/cloudhost/"+id+"/detail";
}		
</script>
<div class="centercontent_block tables" id="mainpage">
        <div id="contentwrapper_1" class="contentwrapper tables"> 
            <div class="contenttitle2">
                	<h3>云主机管理</h3>
                </div>
                <table cellpadding="0" cellspacing="0" border="0" class="stdtable" id="dyntable2" style="font-size:12px;">
                    <colgroup>
                        <col class="con0" style="width: 4%" />
                        <col class="con1" />
                        <col class="con0" />
                        <col class="con1" />
                        <col class="con0" />
                    </colgroup>
                    <thead>
                        <tr> 
   
                          <th class="head0"><input id="deleteAll" type="checkbox"/></th>
                             <th class="head0 nosort">云主机名称</th>
                            <th class="head1 nosort">类型</th>
                            <th class="head0 nosort">配置</th>
                            <th class="head1 nosort">分配状态</th>
                            <th class="head0 nosort">分配时间</th>
                            <th class="head1 nosort">运行状态</th>
                            <th class="head0 nosort">所属用户</th>
                            <th class="head1 nosort">操作</th>
                        </tr>
                    </thead> 
                    <tbody>
                    	<c:forEach items="${cloudHostList}" var="hostList">
                    		<tr class="gradeX">
                          	<td align="center"><span class="center">
                        	<input type="checkbox" class = "idcheck" name="idcheck" style="opacity: 1;" value="${hostList.id}" isAssigned="${hostList.userId}" realHostId="${hostList.realHostId }"/>
                         	</span></td>
                            <td class="cut">${hostList.displayName}</td>
                            <td class="cut">${hostList.sysImageName}</td>
                            <td class="cut">${hostList.cpuCore}核/${hostList.getMemoryText(0) }/${hostList.getDataDiskText(0) }/${hostList.getBandwidthText(0) }</td>
                            <td class="cut">
                            	<c:if test="${hostList.userId!=null }">
                            		已分配
                            	</c:if>
                            	<c:if test="${hostList.userId==null }">
                            		未分配
                            	</c:if>
                            </td>
                            <td class="center">
                            	<c:if test="${hostList.getAssignDate()!=null }">
                            		<fmt:formatDate value="${hostList.getAssignDate() }" pattern="yyyy-MM-dd HH:mm:ss"/>
                            	</c:if>
                            	<c:if test="${hostList.getAssignDate()==null }">
                            		无
                            	</c:if>
                            </td>
                            <td class="cut">${hostList.getSummarizedStatusText() }</td>
                            <td class="cut">${hostList.userAccount }</td>
                            <td class="operatoricon"> 
                            <c:if test="${hostList.status==0 or hostList.status == 3}">
                              <a href="javascript:void(0);" cur_id="${hostList.id }" class="btn btn_trash btn3 delete_one_host_btn" title="删除"></a>&nbsp;&nbsp;&nbsp;&nbsp;                           	
                            </c:if> 
                            <c:if test="${hostList.status==1}">
                                         &nbsp;&nbsp;&nbsp;&nbsp;无
                              </c:if> 
                            <c:if test="${hostList.status==2}">
                           		<a href="javascript:void(0);" cur_id="${hostList.id }" onclick="toDetail('${hostList.id }');" class="btn btn3 btn_info2 host_detail_btn" title="详细"></a>&nbsp;&nbsp;&nbsp;&nbsp; 
                            	<c:if test="${hostList.userId==null && hostList.realHostId!=null}">
                              		<a href="javascript:void(0);" cur_id="${hostList.id }" class="btn btn3 btn_dispach assign_to_one_user" title="分配"></a>&nbsp;&nbsp;&nbsp;&nbsp; 
                            	</c:if>
                                 <c:if test="${hostList.runningStatus==1}">
	                              <a href="javascript:void(0);" cur_id="${hostList.id }" class="btn btn_start btn3 start_host_btn" title="开机"></a>&nbsp;&nbsp;&nbsp;&nbsp;
                                </c:if>
                                <c:if test="${hostList.runningStatus==2}">
	                              <a href="javascript:void(0);" cur_id="${hostList.id }" class="btn btn_shutdown btn3 shutdown_host_btn" title="关机"></a>&nbsp;&nbsp;&nbsp;&nbsp;
	                              <a href="javascript:void(0);" cur_id="${hostList.id }" class="btn btn_restart btn3 restart_host_btn" title="重启"></a>
                                 </c:if>
                              <a href="javascript:void(0);" cur_id="${hostList.id }" class="btn btn_trash btn3 delete_one_host_btn" title="删除"></a>&nbsp;&nbsp;&nbsp;&nbsp;                           	
                             </c:if> 
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
        <!-- ======================================= -->
	<div class="bodywrapper" style="display:none;" id="add_more_cloud_host_div" style="width:100px;"> 

            <div class="centercontent_block" style="margin-left:0">

                <div class="formtitle">
		            <div class="left">
		                <h1 class="logo"><span>增加该类型云主机</span></h1> 
		
		
		                <br clear="all" />
		
		            </div><!--left--> 
		        
		         
		       </div> 
  
                <div   class="contentwrapper">

                    <div id="imageform" class="subcontent"> 

                            <form class="stdform" action="<%=request.getContextPath() %>/warehouse/addAmount" method="post" id="addAmountForm">
                            	<input id="amount_form_input" type="hidden" value="${warehouseId }" name="id"/>
                                <p>
                                    <label>增加数量</label>
                                    <span class="field"><input type="text" name="addAmount" style="width: 50px;" class="smallinput" maxlength="4" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>&nbsp;&nbsp;台</span>
                                    <small class="desc"></small>
                                </p>
                                 <p class="stdformbutton" style="margin-left:200px;"> 
                                    <button class="submit radius2" onclick="saveAddAmount();">保存</button>
                                    <input type="reset" onclick="jQuery.unblockUI();" class="reset radius2" value="取消" />
                                </p>


                            </form>

                            <br /> 

                    </div><!--subcontent-->

                </div><!--contentwrapper-->
                
            </div><!-- centercontent -->

 
        </div><!--bodywrapper-->
        <!-- ============================================ -->
		<div class="bodywrapper" style="display:none;" id="assign_to_user_div_two" style="width:100px;"> 

            <div class="centercontent_block" style="margin-left:0">

                <div class="formtitle">
		            <div class="left">
		                <h1 class="logo"><span>选择分配的用户</span></h1> 
		
		
		                <br clear="all" />
		
		            </div><!--left--> 
		        
		         
		       </div> 
  
                <div   class="contentwrapper" style="width:100%;height:80%;overflow:scroll;">

                    <div id="imageform" align="left" class="subcontent"> 
                         <form class="stdform" action="<%=request.getContextPath() %>/warehouse/assigntouser" method="post" id="assignToUserFormTwo">
	                         	<ul id="users_tree_two" class="easyui-tree" data-options="
									url: '${pageContext.request.contextPath }/warehouse/tree', 
									method: 'get',
									lines: false,
									checkbox: true,
									">
								</ul>
					 		<p class="stdformbutton" style="margin-left:200px;"> 
                                 <button id="sava_assign_form_btn" type="button" class="submit radius2" onclick="saveAssignToUserTwo();">保存</button>
                                 <input type="reset" onclick="jQuery.unblockUI();" class="reset radius2" value="取消" />
                            </p>
                         </form>

                         <br /> 

                    </div><!--subcontent-->

                </div><!--contentwrapper-->
                
            </div><!-- centercontent -->

        </div><!--bodywrapper-->
 
