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
            

            <h2><i class="fa fa-list-alt"></i> 定时备份记录</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              
              <!-- col 6 -->
          <div class="col-md-12">

				  <section class="tile color transparent-black">
				  
				  <!-- tile widget -->
                  <div class="tile-widget color transparent-black rounded-top-corners nopadding nobg">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs tabdrop">
                      <li class="active"><a href="#users-tab" data-toggle="tab" onclick="window.location.href='<%=request.getContextPath() %>/backresume/detailmanage';">备份记录</a></li>
                      <li><a href="#orders-tab" onclick="window.location.href='<%=request.getContextPath() %>/backresume/dtsettimebackup/manage';" data-toggle="tab">定时备份管理</a></li>
                      <li><a href="#messages-tab" onclick="window.location.href='<%=request.getContextPath() %>/backresume/desktopbackuptimer/manage';" data-toggle="tab">定时备份主机管理</a></li>
                      <li><a href="#tasks-tab" data-toggle="tab" onclick="window.location.href='<%=request.getContextPath() %>/backresume/manualbackup';">手动备份</a></li>
                      <div id="space"></div>
                      
                     </ul>
                    <!-- / Nav tabs -->
                  </div>
                  <!-- /tile widget -->


 
                  <!-- tile header -->
                      <div class="tile-header">  
	                    <button type="button" class="btn btn-green file-excel-o" onclick="exportData('/export/backresumedata')">
	                              <i class="fa fa-file-excel-o"></i>
	                              <span>导出数据</span>
	                    </button>
                          <div class="controls">
                              <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                          </div>

                      </div>
                  <!-- /tile header -->

                  <div class="tile-body no-vpadding">

                    <div class="table-responsive">
                    
                      <table  class="table table-datatable table-custom table-sortable" id="basicDataTable">
                        <thead>
                          <tr> 
                              <th class="sortable sort-alpha">主机名</th>
                              <th class="sortable sort-alpha">所属用户</th>
                              <th class="sortable sort-alpha">备份模式</th>
                              <th class="sortable sort-alpha">备份磁盘</th>
                              <th class="sortable sort-amount">备份完成时间</th>
                              <th class="sortable sort-amount">备份状态</th> 
                              <th class="sortable sort-amount">操作</th> 
                          </tr>
                        </thead>
                        <tbody>
                          <c:forEach items="${detailList}" var="detail">
                    		<tr class="gradeX">
                    		 
                                <td class="cut">${detail.displayName}</td>
                                <td class="cut">${detail.userName}</td>
                                <td class="cut">${detail.modeFormat()}</td>
                                <td class="cut">${detail.diskFormat()}</td>
                                 <td class="cut"><fmt:formatDate
                                        value="${detail.backUpTimeDate}"
                                        pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                 
                                <td>
                                    <c:if test="${detail.status == 1}">
                                        <span class="label label-greensea">成功</span>
                                    </c:if>
                                    <c:if test="${detail.status == 2}">
                                        <span class="label label-danger">失败</span>
                                    </c:if>
                                    <c:if test="${detail.status == 3}">
                                        <span class="label label-danger">已过期</span>
                                    </c:if>
                                    <c:if test="${detail.status == 4}">
                                        <span class="label label-danger">正在备份</span>
                                    </c:if>
                                    <c:if test="${detail.status == 5}">
                                        <span class="label label-danger">正在恢复</span>
                                    </c:if>
                                    <c:if test="${detail.status == 6}">
                                        <span class="label label-danger">正在备份其他版本</span>
                                    </c:if>
                                </td>
                                
                                <td>
                                    <c:if test="${detail.status == 1}">
                                       <button type="button" class="btn btn-primary btn-xs" onclick="$('#resumehostid').val('${detail.hostId}');$('#resumedisk').val('${detail.disk}');$('#resumemode').val('${detail.mode}');$('#con').click();">恢复</button>
                                        
                                     </c:if>
                                    <c:if test="${detail.status == 2}">
                                    				     无 
                                    </c:if>
                                    <c:if test="${detail.status == 3}">
                                    				     无 
                                    </c:if>
                                    <c:if test="${detail.status == 4}">
                                    				     无 
                                    </c:if>
                                    <c:if test="${detail.status == 5}">
                                    				     无 
                                    </c:if>
                                    <c:if test="${detail.status == 6}">
                                    				     无 
                                    </c:if>
                                </td>
                                 
                        </tr>
                    	</c:forEach>
                           
                        </tbody>
                      </table>
                      
                    </div>
                    
                    
                    
                  </div>
                  
                  <div class="tile-body">

                    <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                    <a href="#modalConfirm" id="con" role="button"   data-toggle="modal"> </a>

                    <div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>确认</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form role="form">   
                              <input type="hidden" id="resumehostid" value=""/>
                              <input type="hidden" id="resumemode" value=""/>
                              <input type="hidden" id="resumedisk" value=""/>
                              
                              <div class="form-group">
                                <label style="align:center;" id="confirmcontent">确定要恢复成该版本吗？</label>
                               </div>

                            </form>
                          </div>
                            <div class="modal-footer">
                                <button class="btn btn-green" id="confirm_btn" onclick="resume();" data-dismiss="modal" aria-hidden="true">确定
                                </button>
                                <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->     
                    
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

       
      
    })
    
     $(function(){

         
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
        $("#space").width($("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop").width()
				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).width()
				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(1).width()
				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(2).width()
				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(3).width()
				 -1).height(
				  $("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).height());
		$(window).resize(function(){
			 $("#space").width($("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop").width()
					 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).width()
					 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(1).width()
					 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(2).width()
					 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(3).width()
					 -1);
		});
        
        
       });
    
    function resume(){
    	var id = $("#resumehostid").val();
    	var mode = $("#resumemode").val();
    	var disk = $("#resumedisk").val();
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
	        		  $("#tipscontent").html("登录超时，请重新登录");
	     		      $("#dia").click();
		        	}else{  
		        		jQuery.get(path + "/backresume/"+id+"/"+mode+"/"+disk+"/resume",function(data){
		        			if(data.status == "success"){	        					
		        					window.location.reload(); 
		        			}else{
		        				$("#tipscontent").html(data.message);
		           		        $("#dia").click();
		        			}
		        		});
		        	} 
	        }
	     }); 
    	
    }
    
    $(window).resize(function() {
    });

        
      
    </script>
    
  </body>
</html>
      
 