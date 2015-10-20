<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/style.default.css" type="text/css" /> 

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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/tables.js"></script>    
<script type="text/javascript">
 
var path = '<%=request.getContextPath()%>';  

function disassociationHost(id){
	jConfirm('确定要解除该用户与该主机的关联吗？', '确认', function(r) {
 		if(r == true){
    		jQuery.get(path + "/cloudhost/"+id+"/disassociation",function(data){
    			if(data.status == "success"){   
    				jAlert(data.message,'成功', function(r) {
		    			location.href = path + "/cloudhost/${host.id}/detail";
 	    			});
    			}else{  
    				jAlert(data.message,'提示'); 	    				
    			}
    		});
			
		}
	});
}
function startHost(id){
	jConfirm('确定要开启该主机吗？', '确认', function(r) {
 		if(r == true){
    		jQuery.get(path + "/cloudhost/"+id+"/start",function(data){
    			if(data.status == "success"){   
    				jAlert(data.message,'成功', function(r) {
		    			location.href = path + "/cloudhost/${host.id}/detail";
 	    			});
    			}else{  
    				jAlert(data.message,'提示'); 	    				
    			}
    		});
			
		}
	});
}
function shutdownHost(id){
	jConfirm('确定要关闭该主机吗？', '确认', function(r) {
 		if(r == true){
    		jQuery.get(path + "/cloudhost/"+id+"/shutdown",function(data){
    			if(data.status == "success"){   
    				jAlert(data.message,'成功', function(r) {
		    			location.href = path + "/cloudhost/${host.id}/detail";
 	    			});
    			}else{  
    				jAlert(data.message,'提示'); 	    				
    			}
    		});
			
		}
	});
}

function  restartHost(id){
	jConfirm('确定要重启该主机吗？', '确认', function(r) {
 		if(r == true){
    		jQuery.get(path + "/cloudhost/"+id+"/restart",function(data){
    			if(data.status == "success"){   
    				jAlert(data.message,'成功', function(r) {
		    			location.href = path + "/cloudhost/${host.id}/detail";
 	    			});
    			}else{  
    				jAlert(data.message,'提示'); 	    				
    			}
    		});
			
		}
	});
}
			
</script>
<div class="centercontent_block tables"   id="mainpage">
        <div id="contentwrapper_1" class="contentwrapper tables">  
            <div class="contenttitle2">
                	<h3>主机详情</h3>
             </div><!--contenttitle-->
             	
             <table cellpadding="0" cellspacing="0" border="0" class="stdtable" style="font-size:12px;width:700px;">
                 <colgroup>
                     <col class="con0" />
                     <col class="con1" />
                     <col class="con0" />
                     <col class="con1" />
                     <col class="con0" />
                 </colgroup> 
                 <tbody>
                     <tr >
                         <td style="border-top: 1px solid #ddd;align:center;" colspan="2">
                             <c:if test="${host.status==2}">
                                  <c:if test="${host.runningStatus==1}">
	                              <a href="javascript:void(0);"onclick="startHost('${host.id }');" cur_id="${host.id }" class="btn btn_start btn3 start_host_btn" title="开机"></a>&nbsp;&nbsp;&nbsp;&nbsp;
                                </c:if>
                                <c:if test="${host.runningStatus==2}">
	                              <a href="javascript:void(0);" onclick="shutdownHost('${host.id }');" cur_id="${host.id }" class="btn btn_shutdown btn3 shutdown_host_btn" title="关机"></a>&nbsp;&nbsp;&nbsp;&nbsp;
	                              <a href="javascript:void(0);" onclick="restartHost('${host.id }');" cur_id="${host.id }" class="btn btn_restart btn3 restart_host_btn" title="重启"></a>
                                 </c:if>
                             </c:if>  
                             <c:if test="${host.userId!=null }">
	                              <a href="javascript:void(0);" onclick="disassociationHost('${host.id }');" cur_id="${host.id }" class="btn btn_disassociation btn3 delete_one_host_btn" title="取消关联"></a>&nbsp;&nbsp;&nbsp;&nbsp;                           	
                             </c:if>
                         </td>
                      </tr>
                     <tr >
                         <td >主机名：${host.displayName}</td>
                         <td>真实ID：${host.realHostId}</td>
                      </tr> 
                     <tr >
                         <td>CPU：${host.cpuCore}核       </td>
                         <td>当前利用率：${realdata.cpuUsage }%</td>
                      </tr>
                     <tr >
                         <td>内存：${host.getMemoryText(0) }</td>
                         <td>当前利用率：${realdata.memoryUsage }%</td>
                      </tr>
                     <tr >
                         <td>硬盘：${host.getDataDiskText(0) }</td>
                         <td>当前利用率：${realdata.dataDiskUsage }%</td>
                      </tr>
                     <tr>
                         <td colspan="2">带宽：${host.getBandwidthText(0) }</td> 
                      </tr>
                     <tr>
                         <td colspan="2">
                                                                                          创建时间:
                        <fmt:formatDate value="${host.curCreateDate }" pattern="yyyy-MM-dd HH:mm:ss"/>                 
                         </td>
                      </tr>
                      <c:if test="${host.userId!=null }"> 
                         <tr>
                             <td>分配状态:已分配</td> 
	                         <td>所属用户:${host.userAccount }</td>
	                      </tr>
                     	</c:if>
                     	<c:if test="${host.userId==null }">
                     	 <tr>
                           <td colspan="2">分配状态:未分配</td>
                         </tr>
                     	</c:if>
                     <tr>
                         <td colspan="2">运行状态:${host.getSummarizedStatusText() }</td>
                      </tr>  
                      
                 </tbody>
             </table> 
             
              
               
                <br>
                <br>
        	 
        
        </div><!--contentwrapper-->
</div>
         
        <br clear="all" />
