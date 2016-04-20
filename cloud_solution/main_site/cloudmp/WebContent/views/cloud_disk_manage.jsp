<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- cloud_disk_manage.jsp -->
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
            

            <h2><i class="fa fa-desktop"></i> 云硬盘管理</h2>
            

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
                     <button type="button" class="btn btn-red delete" id="add_disk">
                              <i class="fa fa-plus"></i>
                              <span>创建云硬盘 </span>
                    </button>  
                    <button type="button" class="btn btn-green file-excel-o" onclick="exportData('/export/cloudDiskdata')">
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
	                          <th class="no-sort">
	                            <div class="checkbox check-transparent">
	                              <input type="checkbox" id="allchck">
	                              <label for="allchck"></label>
	                            </div>
	                          </th>
	                            <th class="sortable sort-alpha">云硬盘名</th>
	                            <th class="sortable sort-amount">硬盘大小</th>
	                            <th class="sortable sort-amount">磁盘类型</th>
	                            <th class="sortable sort-amount">用户</th>
	                            <th class="sortable sort-amount">创建时间</th>
<!--	                            <th>内网IP及端口</th>-->
<!--	                            <th>外网IP及端口</th>-->
	                            <th class="sortable sort-amount">运行状态</th>
	                            <th>使用状态</th>
	                            <th>操作</th>
                          </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${cloudDiskList}" var="disk">
                        
                          <tr class="odd gradeX">
						          <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" name="idcheck" value="${disk.id}" id="${disk.id}" status="${disk.status }">
									  <label for="${disk.id}"></label>
									</div>
                                 </td>
                            <td>${disk.name}</td>
                            <td>${disk.disk/1024/1024/1024}GB</td>
                            <td><c:choose>
								  	<c:when test="${disk.type==0}">
								  		普通磁盘
								  	</c:when>                            
								  	<c:when test="${disk.type==1}">
								  		高速磁盘
								  	</c:when>
								</c:choose>
								
								</td>
                            <td>${disk.account}</td>
                            <td>${disk.createTime}</td>
<!--                            <td>${disk.innerIp} ${disk.innerPort}</td>-->
<!--                            <td>${disk.outerIp} ${disk.outerPort}</td>-->
                            <td>
                            	<c:choose>
								  	<c:when test="${disk.runningStatus==1}">
								  		<span class="label label-danger">关机</span>
								  	</c:when>
								  	<c:when test="${disk.runningStatus==2}">
								  		<span class="label label-greensea">启动</span>
								  	</c:when>
								  	<c:when test="${disk.runningStatus==3}">
								  		<span class="label label-danger">故障</span>
								  	</c:when>
								  	<c:when test="${disk.runningStatus==4}">
								  		<span class="label label-danger">停机</span>
								  	</c:when>
                                </c:choose>
                            
                            </td>
                            
                            <td>                        
                            	<c:choose>
								  	<c:when test="${disk.status==1}">
								  		<span class="label label-greensea">正常</span>
								  	</c:when>
								  	<c:when test="${disk.status==2}">
								  		<span class="label label-danger">停机</span>
								  	</c:when>
								  	<c:when test="${disk.status==3}">
								  		<span class="label label-danger">欠费</span>
								  	</c:when>
								  	<c:when test="${disk.status==4}">
								  		<span class="label label-danger">永久停机</span>
								  	</c:when>
                                </c:choose></td>
                             <td><div class="btn-group">
                      <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                        操作 <span class="caret"></span>
                      </button>
                      <ul class="dropdown-menu" role="menu">
						<c:choose>
							<c:when test="${disk.status==2}">                      
                            	<li><a href="javascript:void(0);" diskid="${disk.id}"  status="${disk.status}" class="start_data_disk">启用</a></li>
                            </c:when>
                            <c:when test="${disk.status==1}">     
                        		<li><a href="javascript:void(0);" diskid="${disk.id}" status="${disk.status}" class="stop_data_disk">停用</a></li>
                        	</c:when>
                        </c:choose>
                        <li><a href="javascript:void(0);" diskid="${disk.id}" class="update_data_disk">修改</a></li>
                        <li class="divider"></li>
                        <li><a href="javascript:void(0);" diskid="${disk.id}" class="delete_data_disk">删除</a></li>
                      </ul>
                    </div></td>
                          
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
                                <label style="align:center;" id="confirmcontent">确定要删除该硬盘吗？</label>
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
    var curIds = "";
    var status = "";
    $(function(){
      //删除云硬盘
	  jQuery(".delete_data_disk").click(function(){
	    	curIds = jQuery(this).attr("diskid");
			$("#confirmcontent").html("确定要删除所选硬盘吗？");
	    	$("#confirm_btn").attr("onclick","deleteDisk();");
	    	$("#con").click();
	  }); 
	  
      //修改云硬盘
	  jQuery(".update_data_disk").click(function(){
          window.location.href = "<%=request.getContextPath()%>/cloudDisk/update?id="+jQuery(this).attr("diskid");
	  }); 
	  	  
	  //停用云硬盘
	  jQuery(".stop_data_disk").click(function(){
	    	curIds = jQuery(this).attr("diskid");
	    	status = jQuery(this).attr("status");  	
			$("#confirmcontent").html("确定要停用所选硬盘吗？");
	    	$("#confirm_btn").attr("onclick","onOffDisk();");
	    	$("#con").click();
	  });  

	  //启用云硬盘
	  jQuery(".start_data_disk").click(function(){
		  curIds = jQuery(this).attr("diskid");
          status = jQuery(this).attr("status");
		  $("#confirmcontent").html("确定要启用所选硬盘吗？");
	      $("#confirm_btn").attr("onclick","onOffDisk();");
	      $("#con").click();
	  }); 
	  
	  //添加云硬盘
	  $("#add_disk").click(function(){
		  window.location.href = "<%=request.getContextPath()%>/cloudDisk/add";
 	  });
	  
	  //批量操作
	  $("#save_oper").click(function(){
		  	var option = jQuery("#oper_select").val();
		  	if(option=="del"){
				 var ids = "";
				 var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
				 $(datatable).each(function(){
					 if(!(jQuery(this).attr("id")=="" && jQuery(this).attr("status")==1)){
						ids += jQuery(this).val()+","
					 }
				 });
		        if(ids == ""){
		        	$("#tipscontent").html("请选择要删除的硬盘");
					$("#dia").click();
		       	 	return;
		        } 
				curIds = ids;
				$("#confirmcontent").html("确定要删除所选硬盘吗？");
		    	$("#confirm_btn").attr("onclick","deleteDisk();");
		    	$("#con").click();
		    	
		    	
		    	
		  	}
	     });	    
	    
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
	   $("div[class='col-md-4 text-right sm-center']").attr("class","tile-footer text-right");
    });
    
       /* Get the rows which are currently selected */
    function fnGetSelected(oTable01Local){
        return oTable01Local.$('tr.row_selected');
    };   
    
    //删除硬盘
    function deleteDisk(){
    	jQuery.ajax({
	        url: '<%=request.getContextPath()%>/cloudDisk/del',
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
	    			location.href = "<%=request.getContextPath()%>/cloudDisk/all";
	        	}
	        	else{
		        	$("#tipscontent").html(result.message);
					$("#dia").click();
	        	}
	        }
	        
	     });  
    }
    
    //重启或停用硬盘
    function onOffDisk(){
    	jQuery.ajax({
	        url: '<%=request.getContextPath()%>/cloudDisk/onoff',
	        type: 'post', 
	        dataType: 'json',
	        data:"id="+curIds+"&status="+status,
	        timeout: 10000,
	        error: function()
	        { 
	        },
	        success: function(result)	        
	        {  
	        	if(result.status=="success"){
	    			location.href = "<%=request.getContextPath()%>/cloudDisk/all";
	        	}
	        	else{
		        	$("#tipscontent").html(result.message);
					$("#dia").click();
	        	}
	        }
	        
	     });  
    }

    </script>
  </body>
</html>
      

