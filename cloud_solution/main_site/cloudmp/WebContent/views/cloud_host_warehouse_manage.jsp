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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.col-md-4rm.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.uniform.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.tagsinput.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/charCount.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/ui.spinner.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/chosen.jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/hostwarehouseform.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.uniform.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.blockUI.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.alerts.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.easyui.min.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/tables.js"></script>    
<script type="text/javascript">
var path = "<%=request.getContextPath()%>";
var cur_warehouse_id = "";
var cur_warehouse_remain_amount = 0;
jQuery(document).ready(function(){
	  //默认选中
    var select = '<a href="#" id="add_hostwarehouse_btn" class="btn btn_add radius50"><span>新增主机仓库</span></a>';
      jQuery("#dyntable_length").append(select);
      jQuery("#add_hostwarehouse_btn").click(function(){
    	var $this = jQuery(this);
    	jQuery("#old_amount_input").val("0");
        jQuery.blockUI({ message: jQuery("#newHostWarehouse"),  
        css: {border:'3px solid #aaa',
              backgroundColor:'#FFFFFF',
              overflow: 'hide',
              width: '80%', 
              height: 'auto', 
              left:'100px',
              top:'50px'} 
        });
        jQuery('.blockOverlay').attr('title','单击关闭').click(jQuery.unblockUI); 
        /* jQuery.post("${pageContext.request.contextPath}/sysimage/all",function(data){
			if(false){
				
			}
		}); */
      });
      //===============================
      jQuery("#create_new_host_type").click(function(){
          jQuery.blockUI({ message: jQuery("#newType"),  
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
    //==============增加主机数量=================
      jQuery(".add_more_host").click(function(){
    	  alert("qian");
          jQuery.blockUI({ message: jQuery("#add_more_cloud_host"),  
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
    //==============分配主机=================
      
      jQuery(".assign_ware").click(function(){
    	  cur_warehouse_id = jQuery(this).attr("cur_id");
    	  cur_warehouse_remain_amount = jQuery(this).attr("cur_remain_amount");
          jQuery.blockUI({ message: jQuery("#assign_to_user_div"),  
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
    //==================================  
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
});
    
function saveForm(){
	if(validator2.form()){
		var options = {
				success:function result(data){
					if(data.status=="success"){
						jQuery("#add_hostwarehouse_btn").click();
						jQuery("#cloudHostConfigModelId").prepend("<option value="+data.properties.id+">"+data.properties.name+"</option>");
						jQuery("#cloudHostConfigModelId option[value="+data.properties.id+"]").attr("selected",true);
					}
				},
				dataType:'json',
				timeout:10000
		};
		jQuery("#hosttypeform").ajaxForm(options);
	}
}
    // 添加运营商
			
</script>
<div class="centercontent_block tables" id="mainpage">
        <div id="contentwrapper_1" class="contentwrapper tables"> 
            <div class="contenttitle2">
                	<h3>云主机仓库管理</h3>
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
                            <th class="head1">主机仓库名称</th>
                            <th class="head0">主机类型</th>
                            <th class="head1">数量</th>
                            <th class="head0">已分配</th>
                            <th class="head1">未分配</th>
                            <th class="head0">创建时间</th>
                            <th class="head1">操作</th>
                        </tr>
                    </thead> 
                    <tbody>
                    	 <c:forEach items="${cloudHostWarehouseList}" var="chw">
                    		<tr class="gradeX">
                            <td>
                            <c:if test="${chw.name == null}">
                            &nbsp;                                                                        
                            </c:if>
                            &nbsp;${chw.name}</td>
                            <td>
                            <c:if test="${chw.sysImageName == null}">
                            &nbsp;                                                                        
                            </c:if>
                            ${chw.sysImageName }</td>
                            <td> 
                            ${chw.totalAmount }</td>
                            <td>${chw.assignedAmount }</td>
                            <td>${chw.remainAmount }</td>
                            <td class="center">
                            <fmt:formatDate value="${chw.curCreateDate }" pattern="yyyy-MM-dd HH:mm:ss"/>
                            </td>
                            <td class="operatoricon"> 
                              <a href="javascript:void(0);" cur_id="${chw.id }" cur_remain_amount="${chw.remainAmount }" class="btn btn3 btn_dispach assign_ware" title="分配"></a>&nbsp;&nbsp;&nbsp;&nbsp;
                              <a href="javascript:void(0);" cur_id="${chw.id }" onclick="addHosts('${chw.id }','${chw.sysImageName}');"  class="btn btn_add_little btn3" title="增加主机数"></a>&nbsp;&nbsp;&nbsp;&nbsp;
                              <a href="javascript:void(0);" my_index_detail="${chw.id }" onclick="toDetail('${chw.id }');"  class="btn btn3 btn_info2 hostwarehouse_detail_btn" title="详细"></a>&nbsp;&nbsp;&nbsp;&nbsp;
                              <a href="javascript:void(0);" my_index="${chw.id }" onclick="updateWare('${chw.id }','${chw.totalAmount }');" class="btn btn3 btn_pencil update_hostwarehouse_btn" title="修改"></a>&nbsp;&nbsp;&nbsp;&nbsp;
                              <a href="javascript:void(0);" my_index_delete="${chw.id }" onclick="deleteWare('${chw.id }');" class="btn btn3 btn_trash delete_hostwarehouse_btn" title="删除"></a>
                             </td>
                        </tr>
                    	</c:forEach> 
                          
                        <!--<tr class="gradeC">
                          <td align="center"><span class="center">
                            <input type="checkbox" />
                          </span></td>
                            <td>Trident</td>
                            <td>Internet Explorer 5.0</td>
                            <td>Win 95+</td>
                            <td class="center">5</td>
                            <td class="center">C</td> 
                            <td class="operatoricon">
                              <a href="" class="btn btn3 btn_pencil"></a>
                              <a href="" class="btn btn3 btn_trash"></a>
                            </td>
                        </tr>-->
                         
                    </tbody>
                </table>
                <br>
                <br>
        	 
        
        </div><!--contentwrapper-->
</div>
        
        <br clear="all" />
    <div class="bodywrapper" style="display:none;" id="newHostWarehouse">  
    
        <div class="formtitle">
            <div class="left">
                <h1 class="logo"><span>主机仓库新增</span></h1> 


                <br clear="all" />

            </div><!--left--> 
        
         
       </div>
        <div class="centercontent_block" style="margin-left:0;"> 

            <div  class="contentwrapper" >
               
                <div  class="subcontent">  

                        <form class="stdform" action="${pageContext.request.contextPath }/warehouse/add" method="post" id="hostwarehouseform"  >
							<input id="old_amount_input" type="hidden" value="0" name="oldAmount"/>
                            <p>
                                <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">仓库名称</label>
                                <span class="field"><input type="text" oldName="" name="name" id="warehouse_name" class="smallinput "  style="width: 310px;"/></span>
                                <small class="desc"></small>
                            </p>
                            <p>
                                <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">主机类型</label>
                                <span class="formwrapper"> 
                                    <select name="cloudHostConfigModelId" class="bigselect" id="cloudHostConfigModelId" style="width:200px">
                                    <option value="" selected="selected">请选择</option>
                                    <c:forEach items="${cloudHostConfigModeList }" var="chcm">
                                    	<option value="${chcm.id }">${chcm.name }</option>
                                    </c:forEach>
                                    </select> 
                                    <a href="#" id="create_new_host_type" class="btn btn_add radius50"><span>新增主机类型</span></a>
                                </span>
                            </p>
                            <p>
                                <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">数量</label>
                                <span class="formwrapper"> 
                                    <input id="totalAmount" type="text" name="totalAmount" value="10" style="width: 60px;" maxlength="4" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
                                </span>
                            </p>

                            <p class="stdformbutton">
                                <button type="button" class="submit radius2" onclick="saveWarehouseForm();" >保存</button>
                                <input  onclick="jQuery.unblockUI();" type="reset" class="reset radius2" value="取消" />
                            </p>


                        </form>

                        <br /> 

                </div><!--subcontent-->

            </div><!--contentwrapper-->

        </div><!-- centercontent -->
<!-- ===============newCloudHostType================== -->
<div class="bodywrapper" style="display:none;" id="newType">  
    
        <div class="formtitle">
            <div class="left">
                <h1 class="logo"><span>主机类型新增</span></h1> 


                <br clear="all" />

            </div><!--left--> 
        
         
       </div>
        <div class="centercontent_block" style="margin-left:0;"> 

            <div  class="contentwrapper" >
               
                <div  class="subcontent">  

                        <form class="stdform" action="<%=request.getContextPath() %>/chcm/add" method="post" id="hosttypeform"  >
								<input type="hidden" name="oldname" id="oldtypename" class="smallinput "  value=""/>
                            <p>
                                <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">类型名称</label>
                                <span class="field"><input type="text" name="name" id="typename" class="smallinput "  /></span>
                                <small class="desc"></small>
                            </p>
                            <p>
                                <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">配置</label>
                                <span class="formwrapper"> 
                                    <input id="not_diy" type="radio" name="isdiy" checked="checked" onclick="hideAndAbled('diy','commoncollocation')" value="notdiy"/> 常用配置 &nbsp; &nbsp;
                                    <select name="allocationType" class="bigselect" id="commoncollocation" style="width:200px">
                                    <option value="" selected="selected">请选择</option>
                                    <option value="2-2-20-2">入门级：2核/2G/20G/2M</option>
                                    <option value="2-4-50-2">标准级：2核/4G/50G/2M</option>
                                    <option value="4-4-100-4">商务级：4核/4G/100G/4M</option>
                                    <option value="8-8-200-6">企业级：8核/8G/200G/6M</option>
                                    <option value="12-12-500-10">专业级：12核/12G/500G/10M</option>
                                    </select> 
                                    <input id="is_diy" type="radio" name="isdiy" onclick="showAndDisabled('diy','commoncollocation');" value="diy"/> 自定义
                                </span>
                            </p>
                            <p>
                                <label></label>
                                <span class="formwrapper"> 
                                </span>
                            </p>
                            <div id="diy" style="display:none;margin-left:60px;" >
                                <p>
                                <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">CPU</label>
                                <span class="formwrapper">  
                                    <select name="cpuCore" class="bigselect" id="cpu" style="width:200px">
                                    <option value="">请选择</option>
                                    <option value="1">1核</option> 
                                    <option value="2">2核</option> 
                                    <option value="4">4核</option>
                                    <option value="8">8核</option>
                                    <option value="12">12核</option>
                                    <option value="16">16核</option>
                                    </select> 
                                </span>
                                </p>
                                <p>
                                    <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">内存</label>
                                    <span class="formwrapper">  
                                        <select name="memory" class="bigselect" id="cur_memory" style="width:200px">
                                        <option value="">请选择</option> 
                                        <option value="1">1G</option>
                                        <option value="2">2G</option>
                                        <option value="4">4G</option>
                                        <option value="6">6G</option>
                                        <option value="8">8G</option>
                                        <option value="12">12G</option>
                                        <option value="16">16G</option>
                                        <option value="32">32G</option>
                                        <option value="64">64G</option>
                                        </select> 

                                    </span>
                                </p>

                                <p>
                                    <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">硬盘</label>
                                    <span class="formwrapper"> 
                                        <input type="radio" name="diskcheck" checked="checked" onclick="abledAndDiabled('disk','diskdiy');"/>
                                        <select id="disk" name="dataDisk" class="bigselect" style="width:200px">
                                        <option value="">请选择</option> 
                                        <option value="20">20G</option> 
                                        <option value="50">50G</option>
                                        <option value="100">100G</option>
                                        <option value="200">200G</option>
                                        <option value="500">500G</option>
                                        </select> 
                                        <input id="disk_radio" type="radio" name="diskcheck"  onclick="abledAndDiabled('diskdiy','disk');"/> 
                                        <input type="text" disabled="disabled" id="diskdiy" name="diskdiy" class="width50 noradiusright" />
                                        &nbsp;G 

                                    </span>


                                </p>
                                <p>
                                    <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">带宽</label>
                                    <span class="formwrapper"> 
                                        <input type="radio" name="bandwidthcheck" checked="checked" value="1" onclick="abledAndDiabled('bandwidth','bandwidthdiy');"/>
                                        <select name="bandwidth" class="bigselect" id="bandwidth" style="width:200px">
                                        <option value="">请选择</option> 
                                        <option value="1">1M</option>
                                        <option value="2">2M</option>
                                        <option value="4">4M</option>
                                        <option value="6">6M</option>
                                        <option value="10">10M</option>
                                        </select> 
                                        <input id="bandwidth_radio" type="radio" name="bandwidthcheck" value="2" onclick="abledAndDiabled('bandwidthdiy','bandwidth');"/>
                                        <input type="text" disabled="disabled" id="bandwidthdiy"  name="bandwidthdiy" class="width50 noradiusright" />&nbsp;M

                                    </span>
                                </p>
                            </div>

                            <p>
                                <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">选择镜像</label>
                                <span class="field">
                                <select id="image_list" name="sysImageId" class="bigselect" style="width:270px;">
                                    <option value="">请选择</option>
                                    <c:forEach items="${sdiList }" var="sdi">
                                    	<c:if test="${sdi.realImageId!=null }">
                                    		<option value="${sdi.id }">${sdi.displayName }</option>
                                    	</c:if>
                                    </c:forEach> 
                                </select> 
                                <a href="#" id="showCreateNewImage"  class="btn btn_add radius50"><span>新增镜像</span></a>
                                </span>
                            </p>

                            <p>
                                <label>描述</label>
                                <span class="field"><textarea cols="80" name="description" rows="3" class="longinput"></textarea></span> 
                            </p>

                            <p class="stdformbutton">
                                <button class="submit radius2" id="add_type_btn" onclick="saveForm();">保存</button>
                                <input  onclick="jQuery.unblockUI();" type="reset" class="reset radius2" value="取消" />
                            </p>


                        </form>

                        <br /> 

                </div><!--subcontent-->

            </div><!--contentwrapper-->

        </div><!-- centercontent -->


    </div>
<!-- ================================================= -->
	
    </div> 

        <div class="bodywrapper" style="display:none;" id="newImage"> 



            <div class="centercontent_block" style="margin-left:0">

                <div class="formtitle">
		           <div class="left">
		               <h1 class="logo"><span>镜像新增</span></h1> 
		
		
		               <br clear="all" />
		
		           </div><!--left--> 
		       
		        
		       </div>  

                <div   class="contentwrapper">

                    <div   class="subcontent"> 

                            <form class="stdform" action="<%=request.getContextPath() %>/image/add" method="post" id="imageform"  >

	                            <p>
	                                <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">镜像名称</label>
	                                <span class="field"><input type="text" name="name" id="name" class="smallinput "  /></span>
	                                <small class="desc"></small>
	                            </p>
	                            <p>
	                                <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">镜像名称</label>
	                                <span class="field"><input type="text" name="displayName" id="displayName" class="smallinput "  /></span>
	                                <small class="desc"></small>
	                            </p>
	                            <div id="notforedit">
		                            <p>
		                               <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">类型</label>
		                               <span class="formwrapper">  
		                                   <select name="type" class="bigselect" id="type">
		                                   <option value="1">从云主机创建</option> 
		                                   </select> 
		                               </span>
		                             </p>                          
		                             <p>
		                                <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">主机仓库</label>
		                                <span class="formwrapper">  
		                                    <select name="hostType" class="bigselect" id="hostType">
		                                    <option value="">请选择</option>  
		                                    <c:forEach items="${cloudHostWarehouseList}" var="warehouse"> 
		                                      <option value="${warehouse.id}">${warehouse.name}  </option>  
					                    	</c:forEach>
		                                    </select> 
		
		                                </span>
		                            </p>
		                             <p>
		                                <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">选择需要创建镜像的主机</label>
		                                <span class="formwrapper">  
		                                    <select name="fromHostId" class="bigselect" id="fromHostId">
		                                    <option value="">请选择</option>  
		                                    </select> 	
		                                </span>
		                            </p>
	                            </div>
	                            
	                            <p>
	                                <label>描述</label>
	                                <span class="field"><textarea cols="80" name="description" id="description" rows="3" class="longinput"></textarea></span> 
	                            </p>
	                            <p class="stdformbutton">
	                                <button class="submit radius2" onclick="saveImageForm();" >保存</button>
	                                <input  onclick="imageresetForm();jQuery('#create_new_host_type').click();" type="reset" class="reset radius2" value="取消" />
	                            </p>
	
	
	                        </form>

                            <br /> 

                    </div><!--subcontent-->

                </div><!--contentwrapper-->
                
            </div><!-- centercontent -->
            
        </div><!--bodywrapper-->
		 
        <!-- ============================================ -->
		<div class="bodywrapper" style="display:none;" id="add_more_cloud_host" style="width:100px;"> 

            <div class="centercontent_block" style="margin-left:0">

                <div class="formtitle">
		            <div class="left">
		                <h1 class="logo"><span>增加仓库库存</span></h1> 
		
		
		                <br clear="all" />
		
		            </div><!--left--> 
		        
		         
		       </div> 
  
                <div   class="contentwrapper">

                    <div id="imageform" class="subcontent"> 

                            <form class="stdform" action="<%=request.getContextPath() %>/warehouse/addAmount" method="post" id="addAmountForm">
                            	<input id="amount_form_input" type="hidden" value="" name="id"/>
                                <p>
                                    <label><img alt="" src="<%=request.getContextPath() %>/images/required.png">增加数量</label>
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
		<div class="bodywrapper" style="display:none;" id="assign_to_user_div" style="width:100px;"> 

            <div class="centercontent_block" style="margin-left:0">

                <div class="formtitle">
		            <div class="left">
		                <h1 class="logo"><span>选择分配的用户</span></h1> 
		
		
		                <br clear="all" />
		
		            </div><!--left--> 
		        
		         
		       </div> 
  
                <div   class="contentwrapper" style="width:100%;height:80%;overflow:scroll;">

                    <div id="imageform" align="left" class="subcontent"> 
                         <form class="stdform" action="<%=request.getContextPath() %>/warehouse/assigntouser" method="post" id="assignToUserForm">
                         		<ul id="users_tree" class="easyui-tree" data-options="
									url: '${pageContext.request.contextPath }/warehouse/tree', 
									method: 'get',
									lines: false,
									checkbox: true,
									">
								</ul>
					 		<p class="stdformbutton" style="margin-left:200px;"> 
                                 <button type="button" class="submit radius2" onclick="saveAssignToUser();">保存</button>
                                 <input type="reset" onclick="jQuery.unblockUI();" class="reset radius2" value="取消" />
                            </p>
                         </form>

                         <br /> 

                    </div><!--subcontent-->

                </div><!--contentwrapper-->
                
            </div><!-- centercontent -->

        </div><!--bodywrapper-->
      
	
