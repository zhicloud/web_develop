<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- warehouse_manage.jsp -->
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
  <script type="text/javascript">
  //最大并发创建数设置页面跳转
  function beforeConcurrent(){
  	window.location.href = "<%=request.getContextPath()%>/warehouse/beforeconcurrent";
  }
  </script>
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
            

            <h2><i class="fa fa-desktop"></i> 云桌面主机仓库管理</h2>
            

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
					<button type="button" class="btn btn-red delete" id="add_warehouse"">
                              <i class="fa fa-plus"></i>
                              <span> 新增主机仓库 </span>
                    </button>
                    <button type="button" class="btn btn-green file-excel-o" onclick="exportData('/export/warehousedata')">
                              <i class="fa fa-file-excel-o"></i>
                              <span>导出数据</span>
                    </button>
                    <button type="button" class="btn btn-blue add" onclick="beforeConcurrent()">
                              <i class="fa fa-gear"></i>
                              <span>设置最大并发创建数</span>
                    </button>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                   
                  </div>
                  <!-- /tile header -->

                        
                    
                       
                  <div class="tile-body no-vpadding"> 
                   
                    <div class="table-responsive">
                    
                      <table  class="table table-datatable table-custom" id="basicDataTable">
                        <thead>
                          <tr>
						    <th class="no-sort">
                            <div class="checkbox check-transparent">
                              <input type="checkbox" value="1" id="allchck">
                              <label for="allchck"></label>
                            </div>
                          </th>
                            <th class="sort-alpha">仓库名称</th>
                            <th class="sort-alpha">主机类型</th>
                            <th class="sort-numeric">数量</th>
                            <th class="sort-numeric">已分配</th>
							<th class="sort-numeric">未分配</th>
                            <th class="sort-alpha">创建时间</th>  
                            <th class="no-sort">操作</th>
                          </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${cloudHostWarehouseList}" var="chw">
                        
                          <tr class="odd gradeX">
						          <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" value="${chw.id }" id="${chw.id }">
									  <label for="${chw.id }"></label>
									</div>
                                 </td>
                                  <td class="cut">
                                  	<c:if test="${chw.name == null}">
		                            &nbsp;                                                                        
		                            </c:if>
		                            &nbsp;<a hrep="javascript:void(0);" onclick="toDetail('${chw.id }');" style="color:#323232;cursor:pointer">${chw.name}</a>
                                  </td>
                                  <td class="cut">
                                  	<c:if test="${chw.hostTypeName == null}">
		                            &nbsp;                                                                        
		                            </c:if>
		                            ${chw.hostTypeName }
                                  </td>
                                  <td class="cut">${chw.totalAmount }</td>
                                  <td class="cut">${chw.assignedAmount }</td>
								  <td class="cut">${chw.remainAmount }</td>
								  <td class="cut"><fmt:formatDate value="${chw.curCreateDate }" pattern="yyyy-MM-dd HH:mm:ss"/></td> 
                                  <td> 
                              <div class="btn-group">
                                  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                                    操作 <span class="caret"></span>
                                  </button>
                                  <ul class="dropdown-menu" role="menu"> 
                                    <li><a href="${pageContext.request.contextPath }/warehouse/${chw.id}/assign">分配</a></li>
			                        <li><a href="javascript:void(0);" onclick="toDetail('${chw.id }');">详细</a></li> 
			                        <li><a href="javascript:void(0);" onclick="updateWare('${chw.id }','${chw.totalAmount }');">修改</a></li> 
			                        <li><a href="javascript:void(0);" onclick="deleteWare('${chw.id }');">删除</a></li> 
                                    <li class="divider"></li>
                                    <li><a href="javascript:void(0);" onclick="checkManageBtn('${chw.id }');">最小库存管理</a></li> 
                                    <li><a href="javascript:void(0);" onclick="addHosts('${chw.id }','${chw.hostTypeName}','${chw.fileType} }');">增加主机数</a></li> 
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
					<div class="col-sm-2" style="margin-top: -40px;">
                        <div class="input-group table-options">
                          <select id="oper_select" class="chosen-select form-control">
                            <option value="">批量操作</option> 
                            <option value="del">删除</option>
                          </select>
                          <span class="input-group-btn">
                            <button class="btn btn-default" type="button" id="save_oper">提交</button>
                          </span>
                        </div>
                      </div>
 					 <div class="tile-body">

                    <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                    <a href="#modalConfirm" id="con" role="button"   data-toggle="modal"> </a>
                    <a href="#modalForm" id="mform" role="button"   data-toggle="modal"> </a>
                    <a href="#modalForm2" id="mform2" role="button"   data-toggle="modal"> </a>

                    
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
                                <label style="align:center;" id="confirmcontent">确定要删除该主机配置吗？</label>
                               </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green"  id="confirm_btn" onclick="" data-dismiss="modal" aria-hidden="true">确定</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->
                    
                    <div class="modal fade" id="modalForm" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button onclick="setNullBtn();" type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>库存信息</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form class="form-horizontal" id="addAmountForm1" role="form" action="<%=request.getContextPath() %>/warehouse/addAmount" method="post" onsubmit="return false">
		                      <input type="hidden" id="warehouse_id" name="id" value="">
		                      <div class="form-group">
		                        <label for="input01" class="col-sm-2 control-label" style="width:150px;">服务器资源池 </label>
		                        <div class="col-sm-4" id="selectpool1" style="width:160px;">
									<select class="form-control" name="poolId" id="poolId" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectpool1">
			                            <option value="">请选择资源池</option>  
			                            <c:forEach items="${computerPool }" var="sdi">
		 	                                 <option value="${sdi.uuid }">${sdi.name }</option>
		 	                             </c:forEach>  
			                          </select>                       
			                     </div>
		                      </div>
		                      <div class="form-group">
		                        <label for="input01" class="col-sm-2 control-label" style="width:150px;">增加库存数量</label>
		                        <div class="col-sm-4" style="width:160px;">
		                          <input id="cmount_input" type="text" class="form-control" name="addAmount" maxlength="4" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"  parsley-required="true" parsley-max="100" parsley-min="1">
		                        </div>
		                      </div>
                    </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green"  id="form_btn" onclick="saveAddAmount(1);">确定</button>
                            <button onclick="setNullBtn();" class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->   
                    
                    <div class="modal fade" id="modalForm2" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button onclick="setNullBtn();" type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>库存信息</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form class="form-horizontal" id="addAmountForm2" role="form" action="<%=request.getContextPath() %>/warehouse/addAmount" method="post">
		                      <input type="hidden" id="warehouse_id" name="id" value="">
		                      <div class="form-group">
		                        <label for="input01" class="col-sm-2 control-label" style="width:150px;">服务器资源池 </label>
		                        <div class="col-sm-4" id="selectpool2" style="width:160px;">
									<select class="form-control" name="poolId" id="poolId" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectpool2">
			                            <option value="">请选择资源池</option>  
			                            <c:forEach items="${computerPool }" var="sdi">
			                                 <c:if test="${sdi.getMode2() == 1}">
			                                    <option value="${sdi.uuid }">${sdi.name }</option>			                                 
			                                 </c:if>
		 	                             </c:forEach>  
			                          </select>                       
			                     </div>
		                      </div>
		                      <div class="form-group">
		                        <label for="input01" class="col-sm-2 control-label" style="width:150px;">增加库存数量</label>
		                        <div class="col-sm-4" style="width:160px;">
		                          <input id="cmount_input" type="text" class="form-control" name="addAmount" maxlength="4" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"  parsley-required="true" parsley-max="100" parsley-min="1">
		                        </div>
		                      </div>
                    </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green"  id="form_btn" onclick="saveAddAmount(2);">确定</button>
                            <button onclick="setNullBtn();" class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
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
    var path = "<%=request.getContextPath()%>";
    var curId = "";
    var ids = [];
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
	    
	  //==========
	  $("#add_warehouse").click(function(){
 		  window.location.href = "${pageContext.request.contextPath}/warehouse/add";
 	  });
      jQuery("#save_oper").click(function(){
		  	var option = jQuery("#oper_select").val();
		  	if(option=="del"){
				 var myids = [];
				 var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
				 $(datatable).each(function(){
					 myids.push(jQuery(this).val());
				 });
		        if(myids.length < 1){
		        	$("#tipscontent").html("请选择要删除的仓库");
					$("#dia").click();
		       	 	return;
		        } 
		        ids = myids;
				$("#confirmcontent").html("确定要删除所选仓库吗？");
		    	$("#confirm_btn").attr("onclick","deleteMultWarehouse();");
		    	$("#con").click();
		  	}	
	     });
      
    })
      function deleteWarehouse(){
    	jQuery.get(path + "/warehouse/"+curId+"/delete",function(data){
			if(data.status=="success"){
				location.href = path + "/warehouse/all";
			}else{
				jQuery("#tipscontent").html(data.message);
   		        jQuery("#dia").click();
				return;
			}
		});
    }
    function deleteMultWarehouse(){
   		jQuery.ajax({
   	        url: '<%=request.getContextPath()%>/warehouse/deleteIds',
   	        type: 'post', 
   	        dataType: 'json',
   	        data:{ids:ids},
   	     	async: false,
   	        timeout: 10000,
   	        error: function()
   	        { 
   	        },
   	        success: function(result)	        
   	        {  
   	        	if(result.status=="success"){
	   	    		location.href = path + "/warehouse/all";
   	        	}else{
   	        		$("#tipscontent").html(result.message);
	   				$("#dia").click();
   	        	}
   	        }
   	        
   	     });
    }
    //最小库存管理
    function checkManageBtn(id){
    	window.location.href = path + "/warehouse/" + id + "/checkpage";
    }
    //置空
    function setNullBtn(){
    	 $("#cmount_input").val("");
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
      

