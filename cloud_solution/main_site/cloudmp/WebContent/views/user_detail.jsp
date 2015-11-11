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
            

            <h2><i class="fa fa-desktop"></i> 用户详情</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              
              <!-- col 6 -->
          <div class="col-md-12">

				  <section class="tile color transparent-black">


 
                  <!-- tile header -->
                  <div class="tile-header"> 
                    <button type="button" class="btn btn-success delete" onclick="window.location.href='<%=request.getContextPath()%>/user/list';">
                              <i class="fa fa-step-backward"></i>
                              <span> 返回上级</span>
                    </button>
                    <button type="button" class="btn btn-green file-excel-o" onclick="exportData('/export/terminaluserdetaildata/${user.id}')">
                              <i class="fa fa-file-excel-o"></i>
                              <span>导出数据</span>
                    </button>                    
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                   
                  </div> 
                       
                  <div class="tile-body no-vpadding"> 
                   
                    <div class="table-responsive">
                    
                      <table  class="table table-datatable table-custom" id="basicDataTable">
                        <thead>
                          <tr> 
                            <th class="sort-alpha">云主机名称</th>
                            <th class="sort-alpha">类型</th>
                            <th class="sort-amount">配置</th>
                            <th class="sort-numeric">内网ip</th>
                            <th class="sort-numeric">外网ip</th>
                            <th class="sort-numeric">分配状态</th>
							<th class="sort-amount">分配时间</th>
                            <th class="sort-alpha">运行状态</th>  
                             <th class="no-sort">操作</th>
                          </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${hosts}" var="hostList">
                        
                          <tr class="odd gradeX">
						           
                                  <td>
                                  	${hostList.displayName}
                                  </td>
                                  <td>
                                  	${hostList.sysImageName}
                                  </td>
                                  <td>${hostList.cpuCore}核/${hostList.getMemoryText(0) }/${hostList.getDataDiskText(0) }/${hostList.getBandwidthText(0) }</td>
                                  <td>${hostList.getInnerIp() }:${hostList.getInnerPort() } 
                                  
                                  </td> 
                                  <td>
                                          ${hostList.getOuterIpAndPort()}                                     
                                   
                                  </td>
                                  <td>
                                  	<c:if test="${hostList.userId!=null }">
                            		已分配
	                            	</c:if>
	                            	<c:if test="${hostList.userId==null }">
                            		未分配
                            	</c:if>
                                  </td>
								  <td>
								  	<c:if test="${hostList.getAssignDate()!=null }">
                            		<fmt:formatDate value="${hostList.getAssignDate() }" pattern="yyyy-MM-dd HH:mm:ss"/>
	                            	</c:if>
	                            	<c:if test="${hostList.getAssignDate()==null }">
                            		无
                            	</c:if>
								  </td>
								  <td>${hostList.getSummarizedStatusText() }</td> 
                                   <td> 
                              <div class="btn-group">
                                  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                                    操作 <span class="caret"></span>
                                  </button>
                                  <ul class="dropdown-menu" role="menu">
                                  	<c:if test="${hostList.status==0 or hostList.status == 3}">
		                              <li><a href="javascript:void(0);" cur_id="${hostList.id }" class="delete_one_host_btn">删除</a></li>                        	
		                            </c:if> 
		                            <c:if test="${hostList.realHostId==null}">
		                              <li><a href="javascript:void(0);">无</a></li>
		                            </c:if> 
		                            <c:if test="${hostList.status==2}">
		                           		<li><a href="javascript:void(0);" cur_id="${hostList.id }" onclick="toDetail('${hostList.id }');" title="详细"></a></li>
		                            	<c:if test="${hostList.userId==null && hostList.realHostId!=null}">
		                              		<li><a href="javascript:void(0);" cur_id="${hostList.id }" class="assign_one_user">分配</a></li>
		                            	</c:if>
		                                 <c:if test="${hostList.runningStatus==1}">
			                              <li><a href="javascript:void(0);" cur_id="${hostList.id }" class="start_host_btn" >开机</a></li>
		                                </c:if>
		                                <c:if test="${hostList.runningStatus==2}">
			                              <li><a href="javascript:void(0);" cur_id="${hostList.id }" class="shutdown_host_btn" >关机</a></li>
			                              <li><a href="javascript:void(0);" cur_id="${hostList.id }" class="restart_host_btn" >重启</a></li>
		                                 </c:if>
		                              <li><a href="javascript:void(0);" cur_id="${hostList.id }" class="delete_one_host_btn" >解除绑定</a></li>                          	
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
					<div class="tile-body">

                    <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                    <a href="#modalConfirm" id="con" role="button"   data-toggle="modal"> </a>

                    
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
    var warehouseId = '${user.id}';
    var hostIds = [];
    var oneCloudHostId = [];
    var sysImageName = '${warehouse.sysImageName}';
    var currentId = "";
    var curIds = "";
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
        "aaSorting": [ [6,'desc']],
        "aoColumnDefs": [
                         { 'bSortable': false, 'aTargets': [ "no-sort" ] }
                       ], 
        "fnInitComplete": function(oSettings, json) { 
          $('.dataTables_filter').css("text-align","right");
          $('.dataTables_filter').css("margin-top","-25px");
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
						ids += jQuery(this).val()+","
				 });
// 				 jQuery("input[name='idcheck']:checkbox").each(function(){ 
// 		            if(jQuery(this).attr("checked")){
// 		                ids += jQuery(this).val()+","
// 		            }
// 		        })
		        if(ids == ""){
		        	$("#tipscontent").html("请选择要删除主机");
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
	//删除云主机
	    jQuery(".delete_one_host_btn").click(function(){
	    	var id = jQuery(this).attr("cur_id");
	    	currentId = id;
	    	$("#confirmcontent").html("确定要解除该主机与用户的绑定吗？");
	    	$("#confirm_btn").attr("onclick","deleteHost();");
	    	$("#con").click();
	    });
	    //启动云主机
	    jQuery(".start_host_btn").click(function(){
	    	var id = jQuery(this).attr("cur_id");
	    	currentId = id;
	    	$("#confirmcontent").html("确定要启动该主机吗？");
	    	$("#confirm_btn").attr("onclick","startHost();");
	    	$("#con").click();
	    });
	  //关机云主机
	    jQuery(".shutdown_host_btn").click(function(){
	    	var id = jQuery(this).attr("cur_id");
	    	currentId = id;
	    	$("#confirmcontent").html("确定要关闭该主机吗？");
	    	$("#confirm_btn").attr("onclick","shutdownHost();");
	    	$("#con").click();  
	    });
	    //重启云主机
	    jQuery(".restart_host_btn").click(function(){
	    	var id = jQuery(this).attr("cur_id");
	    	currentId = id;
	    	$("#confirmcontent").html("确定要重启该主机吗？");
	    	$("#confirm_btn").attr("onclick","restartHost();");
	    	$("#con").click();  
	    });
       
    })
      function deleteHost(){
    	jQuery.get(path + "/warehouse/cloudhost/"+currentId+"/disassociation",function(data){
			if(data.status == "success"){   
	    		location.href = path + "/user/${user.id}/detail";
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function startHost(){
    	jQuery.get(path + "/warehouse/cloudhost/"+currentId+"/start",function(data){
			if(data.status == "success"){   
	    		location.href = path + "/user/${user.id}/detail";
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function restartHost(){
    	jQuery.get(path + "/warehouse/cloudhost/"+currentId+"/restart",function(data){
			if(data.status == "success"){   
	    		location.href = path + "/user/${user.id}/detail";
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function shutdownHost(){
    	jQuery.get(path + "/cloudhost/"+currentId+"/shutdown",function(data){
			if(data.status == "success"){   
	    		location.href = path + "/user/${user.id}/detail";
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function deleteMultHost(){
    	jQuery.ajax({
	        url: '<%=request.getContextPath()%>/cloudhost/deletehosts',
	        type: 'post', 
	        dataType: 'json',
	        data:"ids="+curIds,
	        timeout: 10000,
	        error: function()
	        { 
	        },
	        success: function(result)	        
	        {  
	        	$("#tipscontent").html(result.message);
				$("#dia").click();
	    		location.href = path + "/user/${user.id}/detail";
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
      

