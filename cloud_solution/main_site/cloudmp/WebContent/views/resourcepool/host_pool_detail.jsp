<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
  <head>
    <title>控制台-${productName}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8" />

    <link rel="icon" type="image/ico" href="<%=request.getContextPath()%>/assets/images/favicon.ico" />
    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/vendor/animate/animate.min.css">
    <link type="text/css" rel="stylesheet" media="all" href="<%=request.getContextPath()%>/assets/js/vendor/mmenu/css/jquery.mmenu.all.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/videobackground/css/jquery.videobackground.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap-checkbox.css">

    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/css/rickshaw.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/morris/css/morris.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/tabdrop/css/tabdrop.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/summernote/css/summernote.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/summernote/css/summernote-bs3.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen-bootstrap.css">

    <link href="<%=request.getContextPath()%>/assets/css/zhicloud.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="<%=request.getContextPath()%>/assets/js/html5shiv.js"></script>
      <script src="<%=request.getContextPath()%>/assets/js/respond.min.js"></script>
    <![endif]-->
  </head>
  <body class="bg-1">

 

    <!-- Preloader -->
    <div class="mask"><div id="loader"></div></div>
    <!--/Preloader -->

    <!-- Wrap all page content here -->
    <div id="wrap">

      


      <!-- Make page fluid -->
      <div class="row">
        




        <%@include file="/views/common/common_menus.jsp" %>

        
        <!-- Page content -->
        <div id="content" class="col-md-12">
          


          <!-- page header -->
          <div class="pageheader">
            

            <h2><i class="fa fa-desktop"></i> 资源监控主机列表</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              
              <!-- col 6 -->
          <div class="col-md-12">

				  <section class="tile color transparent-black">


                  <!-- /tile header -->

                        
                    <div class="tile-header">
                       <button type="button" class="btn btn-success delete" onclick="window.location.href='<%=request.getContextPath()%>/computeresourcepool/all';">
                              <i class="fa fa-step-backward"></i>
                              <span> 返回上级</span>
                    </button>
<!--                      <button type="button" class="btn btn-red delete" id="add_host"> -->
<!--                               <i class="fa fa-plus"></i> -->
<!--                               <span>增加该类型云主机 </span> -->
<!--                     </button>   -->
<!-- 					<div class="search  text-left" id="main-search"> -->
              			<i class="fa"></i>
<!--            			 </div> -->
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                   
                  </div>
                       
                  <div class="tile-body no-vpadding"> 
                   
                    <div class="table-responsive">
                    
                      <table  class="table table-datatable table-custom" id="basicDataTable">
                        <thead>
                          <tr>
<!-- 						    <th class="no-sort"> -->
<!--                             <div class="checkbox check-transparent"> -->
<!--                               <input type="checkbox" value="1" id="allchck"> -->
<!--                               <label for="allchck"></label> -->
<!--                             </div> -->
<!--                           </th> -->
                            <th class="sort-alpha">云主机名称</th>
                            <th class="sort-alpha">CPU核数</th>
                            <th class="sort-alpha">CPU使用率</th>
                            <th class="sort-alpha">内存[使用/总量]</th>
							<th class="sort-alpha">内存使用率</th>
                            <th class="sort-alpha">磁盘[使用/总量]</th>  
                            <th class="sort-alpha">磁盘使用率</th>  
                            <th class="sort-alpha">IP地址[宿主机IP/公网IP]</th>  
                            <th class="sort-alpha">状态</th>
                            <th class="sort-alpha">类型</th>  
                            <th class="no-sort">操作</th> 
                          </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${hostPoolDetail}" var="hostList">
                        
                          <tr class="odd gradeX">
<!-- 						          <td> -->
<!-- 									<div class="checkbox check-transparent"> -->
<%-- 									  <input type="checkbox" name="idcheck" value="${hostList.id}" id="${hostList.id}"> --%>
<%-- 									  <label for="${hostList.id}"></label> --%>
<!-- 									</div> -->
<!--                                  </td> -->
                                  <td class="cut">
                                  	${hostList.displayName}
                                  </td>
                                  <td class="cut">
                                  	${hostList.cpuCore}
                                  </td>
                                  <td class="cut">${hostList.getUsageFormat(1)}</td>
                                  <td class="cut">
                                 	 ${hostList.memoryUsage}G/${hostList.getMemoryText(0)}
                                  </td>
								  <td class="cut">
								  ${hostList.getUsageFormat(2) }
								  </td>
								  <td class="cut">${hostList.diskUsage}G/${hostList.getSysDiskText(0) }</td> 
								  <td class="cut">${hostList.getUsageFormat(3) }</td> 
								  <td class="cut">${hostList.getInnerIp() }/${hostList.getOuterIp() }</td> 
								  <td class="cut">${hostList.getStatusTextFromComputePool()}</td> 
								  <td class="cut">
                                      <c:if test="${hostList.type == 1}">桌面主机</c:if>
	                                   <c:if test="${hostList.type == 2}">云服务器</c:if>
	                                   <c:if test="${hostList.type == 3}">专属云主机</c:if> 
	                                   <c:if test="${hostList.type != 1&&hostList.type != 2&&hostList.type != 3}">无归属</c:if> 
                                  </td>
								  <td> 
	                              <div class="btn-group">
	                                  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown"> 操作 <span class="caret"></span>
	                                  </button>
	                                  <ul class="dropdown-menu" role="menu" style="left:-122px;"> 
	<!--                                     <li><a href="#">分配盒子</a></li>  -->
	                                    <c:if test="${hostList.status==3}">
	                                    	<li><a href="#" onclick="operatorHost('${hostList.realHostId}','1');">开机</a></li> 
	                                    </c:if>
	                                    <c:if test="${hostList.status==0}">
	                                    	<li><a href="#"  onclick="operatorHost('${hostList.realHostId}','2');">关机</a></li> 
	                                    	<li><a href="#"  onclick="operatorHost('${hostList.realHostId}','3');">重启</a></li>
	                                    	<li><a href="#"  onclick="operatorHost('${hostList.realHostId}','4');">强制关机</a></li> 
	                                    </c:if>
	                                    <c:if test="${poolType==1 && hostList.type == null}">
	                                        <li><a href="#" onclick="operatorHost('${hostList.realHostId}','5');">设置成云服务器</a></li> 	                                        
	                                    </c:if>
	                                    <c:if test="${poolType==2 && hostList.type == null}">
	                                        <li><a href="#" onclick="operatorHost('${hostList.realHostId}','6');">设置成桌面主机</a></li> 	                                        
	                                    </c:if>
	                                  </ul>
	                              </div>
	                            </td>
                          </tr>
                        </c:forEach>
                        </tbody>
                      </table>
                      
                    </div>
                    
                    
                    
                  </div>
                  <!-- /tile body -->
                  
                  <!-- <div class="col-sm-2" style="margin-top: -40px;">
                        <div class="input-group table-options">
                          <select id="oper_select" class="chosen-select form-control">
                            <option value="">批量操作</option> 
                            <option value="del">删除</option>
                            <option value="assign">分配</option>
                          </select>
                          <span class="input-group-btn">
                            <button class="btn btn-default" type="button" id="save_oper">提交</button>
                          </span>
                        </div>
                      </div> -->
				 <div class="tile-body">

                    <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                    <a href="#modalConfirm" id="con" role="button"   data-toggle="modal"> </a>
					<a href="#modalForm" id="mform" role="button"   data-toggle="modal"> </a>
					<a href="#modalAddDesktop" id="adddesktop" role="button"   data-toggle="modal"> </a>
                    
                    <div class="modal fade" id="modalDialog" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalDialogLabel"><strong>提示</strong></h3>
                          </div>
                          <div class="modal-body">
                            <p id="tipscontent"></p>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->

                    <div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>确认</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form role="form">   

                              <div class="form-group">
                                <label style="align:center;" id="confirmcontent">确定要删除该云主机吗？</label>
                               </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green" id="confirm_btn"  onclick="toDelete();" data-dismiss="modal" aria-hidden="true">确定</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->                        
					
					
					 <div class="modal fade" id="modalForm" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button id="my_close" type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>主机管理</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form class="form-horizontal" id="addAmountForm" role="form" action="<%=request.getContextPath() %>/warehouse/addAmount" method="post">
		                      <input type="hidden" id="warehouse_id" name="id" value="">
		                      <div class="form-group">
		                        <label for="input01" class="col-sm-2 control-label" style="width:150px;">增加主机数量</label>
		                        <div class="col-sm-4" style="width:160px;">
		                          <input type="text" id="cmount_input" class="form-control" name="addAmount" maxlength="4" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"  parsley-required="true" parsley-max="100" parsley-min="1">
		                        </div>
		                      </div>
                    		</form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green"  id="form_btn" onclick="saveAddAmount();">确定</button>
                            <button id="my_reset" class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->
                    
                    <div class="modal fade" id="modalAddDesktop" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" >
                          <div class="modal-header">
                            <button  type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>添加主机</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form class="form-horizontal" id="addDesktop" role="form" action="<%=request.getContextPath() %>/warehouse/addAmount" method="post">
 		                      <div class="form-group">
		                        <label for="input01" class="col-sm-2 control-label" style="width:150px;">仓库 </label>
		                        <div class="col-sm-4"  id="selectbox">
										<select class="chosen-select   form-control" style="width:300px;" name="wareHostId"id="wareHostId" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox">
			                            <option value="">请选择仓库</option> 
			                            <c:forEach items="${wareHoseList }" var="sdi">
	 		                                 <option value="${sdi.id }">${sdi.name}</option>
	 		                             </c:forEach>   
			                          </select>                       
			                     </div>
		                      </div> 
                    		</form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green"  id="form_btn" onclick="addDesktop();">确定</button>
                            <button   class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->
                  </div> 
 
                </section>


                
                
              </div>
           
                           



            </div>
            <!-- /row -->


            



          </div>
          <!-- /content container -->






        </div>
        <!-- Page content end -->




      </div>
      <!-- Make page fluid-->




    </div>
    <!-- Wrap all page content end -->



    <section class="videocontent" id="video"></section>



   	<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/hostwarehouseform.js"></script>

    <script>
    var path = '<%=request.getContextPath()%>'; 
    $(function(){

      // Add custom class to pagination div
      $.fn.dataTableExt.oStdClasses.sPaging = 'dataTables_paginate pagination-sm paging_bootstrap paging_custom';
 	  
      /*************************************************/
      /**************** BASIC DATATABLE ****************/
      /*************************************************/

      /* Define two custom functions (asc and desc) for string sorting */
      jQuery.fn.dataTableExt.oSort['string-case-asc']  = function(x,y) {
          return ((x < y) ? -1 : ((x > y) ?  1 : 0));
      };
       
      jQuery.fn.dataTableExt.oSort['string-case-desc'] = function(x,y) {
          return ((x < y) ?  1 : ((x > y) ? -1 : 0));
      };
 

      /* Build the DataTable with third column using our custom sort functions */
      var oTable01 = $('#basicDataTable').dataTable({
        "sDom":
          "R<'row'<'col-md-6'l><'col-md-6'f>r>"+
          "t"+
          "<'row'<'col-md-4 sm-center'i><'col-md-4'><'col-md-4 text-right sm-center'p>>",
        "oLanguage": {
          "sSearch": "搜索"
        },
        "aaSorting": [],
        "aoColumnDefs": [
                         { 'bSortable': false, 'aTargets': [ "no-sort" ] }
                       ], 
        "fnInitComplete": function(oSettings, json) { 
          $('.dataTables_filter').css("text-align","right");
          $('.dataTables_filter').css("margin-top","-40px");
          $('.dataTables_filter').css("margin-bottom","0px");   
          $('.dataTables_filter input').attr("placeholder", "Search");
        }
      });
      
     
 
 

      /* Get the rows which are currently selected */
      function fnGetSelected(oTable01Local){
        return oTable01Local.$('tr.row_selected');
      };
	    $("div[class='col-md-4 text-right sm-center']").attr("class","tile-footer text-right");
	  $("#add_host").click(function(){
		  addHosts(warehouseId,sysImageName);
 	  });
	  
	  jQuery("#save_oper").click(function(){
		  	var option = jQuery("#oper_select").val();
		  	if(option=="del"){
				 var ids = "";
				 var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
				 $(datatable).each(function(){
					 if(!(jQuery(this).attr("realHostId")=="" && jQuery(this).attr("status")==1)){
						ids += jQuery(this).val()+","
					 }
				 });
// 				 jQuery("input[name='idcheck']:checkbox").each(function(){ 
// 		            if(jQuery(this).attr("checked")){
// 		                ids += jQuery(this).val()+","
// 		            }
// 		        })
		        if(ids == ""){
		        	$("#tipscontent").html("请选择要删除的主机(创建中的主机无法删除)");
					$("#dia").click();
		       	 	return;
		        } 
				curIds = ids;
				$("#confirmcontent").html("确定要删除所选主机吗？");
		    	$("#confirm_btn").attr("onclick","deleteMultHost();");
		    	$("#con").click();
		  	}else if(option=="assign"){
		  		var ids = [];
		    	var flag = false;
		    	var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
				 $(datatable).each(function(){
					 if(jQuery(this).attr("isAssigned")!='' || jQuery(this).attr("realHostId")==''){
			            	$("#tipscontent").html('请选择有效的云主机进行分配');
							$("#dia").click();
			            	flag = true;
			            	return false;
			            }
			            ids.push(jQuery(this).val());
				 });
				if(flag){
					return;
				}
		        if(ids.length<1){
		        	$("#tipscontent").html('请至少选择一台主机进行分配');
					$("#dia").click();
		        	return;
		        }else{
		        	hostIds = ids;
		        	jQuery.ajax({
						url: path+'/warehouse/assign',
						type: 'post', 
						data:{warehouseId:warehouseId,ids:ids},
						dataType: 'json',
						timeout: 10000,
						async: false,
						success:function(data){
							if(data.status == "success"){
								location.href=path +"/views/warehouse_manage_assign.jsp"; 
							}else if(data.status == "fail"){
								return;
							}
						}
					});
		        }
		  	}
	     });
	  	//分配给单个用户
	  	jQuery(".assign_one_user").click(function(){
	  		var id = jQuery(this).attr("cur_id");
	  		var ids = [];
	  		ids.push(id);
	  		jQuery.ajax({
				url: path+'/warehouse/assign',
				type: 'post', 
				data:{warehouseId:warehouseId,ids:ids},
				dataType: 'json',
				timeout: 10000,
				async: false,
				success:function(data){
					if(data.status == "success"){
						location.href=path +"/views/warehouse_manage_assign.jsp"; 
					}else if(data.status == "fail"){
						return;
					}
				}
			});
	  		return;
	  	});
	
	    $("#my_close,#my_reset").click(function(){
	    	  $("#cmount_input").val("");
	      });
	    
	    
       
    })
    function operatorHost(id,type){
    	currentId = id;
    	if(type == 1){
 	    	$("#confirmcontent").html("确定要启动该主机吗？");
	    	$("#confirm_btn").attr("onclick","startHost();");
	    	$("#con").click();
    	}else if(type == 2){   		
	    	$("#confirmcontent").html("确定要关闭该主机吗？");
	    	$("#confirm_btn").attr("onclick","shutdownHost();");
	    	$("#con").click();  
    	}else if(type == 3){   		
    		$("#confirmcontent").html("确定要重启该主机吗？");
	    	$("#confirm_btn").attr("onclick","restartHost();");
	    	$("#con").click();
    	}else if(type == 4){   		
    		$("#confirmcontent").html("确定要强制关闭该主机吗？");
	    	$("#confirm_btn").attr("onclick","haltHost();");
	    	$("#con").click();
    	}else if(type == 5){   		
    		$("#confirmcontent").html("确定要将该主机添加进云服务器吗？");
	    	$("#confirm_btn").attr("onclick","addServer();");
	    	$("#con").click();
    	}else if(type == 6){
     		$("#adddesktop").click();
     		 $("#wareHostId_chosen").css("width","250px");
    	}
    	
    }
     
	    
      function deleteHost(){
    	jQuery.get(path + "/warehouse/cloudhost/"+currentId+"/delete",function(data){
			if(data.status == "success"){   
	    		window.location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function startHost(){
    	jQuery.get(path + "/computeresourcepool/"+currentId+"/start",function(data){
			if(data.status == "success"){   
	    		window.location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function restartHost(){
    	jQuery.get(path + "/computeresourcepool/"+currentId+"/restart",function(data){
			if(data.status == "success"){   
	    		window.location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function shutdownHost(){
    	jQuery.get(path + "/computeresourcepool/"+currentId+"/stop",function(data){
			if(data.status == "success"){   
	    		window.location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    
    function haltHost(){
    	jQuery.get(path + "/computeresourcepool/"+currentId+"/halt",function(data){
			if(data.status == "success"){   
	    		window.location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function addServer(){
    	jQuery.get(path + "/computeresourcepool/"+currentId+"/addserver",function(data){
			if(data.status == "success"){   
	    		window.location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function addDesktop(){
    	var form = jQuery("#addDesktop");
		form.parsley('validate');
		if(form.parsley('isValid')){  		        				
			var wareHostId = $("#wareHostId").val();
	    	jQuery.get(path + "/computeresourcepool/"+currentId+"/"+wareHostId+"/adddesktop",function(data){
				if(data.status == "success"){   
		    		window.location.reload();
				}else{  
					$("#tipscontent").html(data.message);
					$("#dia").click();
				}
			});
		}
    	
    }
    function deleteMultHost(){
    	jQuery.ajax({
	        url: '<%=request.getContextPath()%>/warehouse/cloudhost/deletehosts',
	        type: 'post', 
	        dataType: 'json',
	        data:"ids="+curIds,
	        timeout: 10000,
	        error: function()
	        { 
	        },
	        success: function(result)	        
	        {  
	        	if(result.status=="success"){
	    			window.location.reload();
	        	}
	        	else{
		        	$("#tipscontent").html(result.message);
					$("#dia").click();
	        	}
	        }
	        
	     });  
    }
    
    $(function(){

        //check all checkboxes
        $('table thead input[type="checkbox"]').change(function () {
          $(this).parents('table').find('tbody input[type="checkbox"]').prop('checked', $(this).prop('checked'));
        });

        // sortable table
        $('.table.table-sortable th.sortable').click(function() {
          var o = $(this).hasClass('sort-asc') ? 'sort-desc' : 'sort-asc';
          $(this).parents('table').find('th.sortable').removeClass('sort-asc').removeClass('sort-desc');
          $(this).addClass(o);
        });

        //chosen select input
        $(".chosen-select").chosen({disable_search_threshold: 10});

        //check toggling
        $('.check-toggler').on('click', function(){
          $(this).toggleClass('checked');
        })
        
      });
    </script>
  </body>
</html>
      

