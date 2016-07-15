<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- set_time_operation_detail_manage.jsp -->
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
            

            <h2><i class="fa fa-list-alt"></i> 主机定时操作管理</h2>
            

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
                              <li class=""><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/startup/manage');">定时开机管理</a></li>
                              <li class=""><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/startup/host/manage');">定时开机主机管理</a></li>
                              <li class=""><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/shutdown/manage');">定时关机管理</a></li>
                              <li class=""><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/shutdown/host/manage');">定时关机主机管理</a></li>
                              <li class="active"><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/detail/manage');">定时操作记录</a></li>
                              <div id="space"></div>

                          </ul>
                          <!-- / Nav tabs -->
                          <!-- tile header -->
                          </div>
                      <div class="tile-header">  
	                    <button type="button" class="btn btn-green file-excel-o" onclick="exportData('/export/settimeoperationdata')">
	                              <i class="fa fa-file-excel-o"></i>
	                              <span>导出数据</span>
	                    </button>
                      </div>    
                      <div class="tile-body no-vpadding">

                          <div class="table-responsive">

                              <table  class="table table-datatable table-custom table-sortable" id="basicDataTable">
                                  <thead>
                                  <tr>
                                      <th class="sortable sort-alpha">主机名</th>
                                      <th class="sortable sort-amount">操作时间</th>
                                      <th class="sortable sort-amount">操作</th>
                                      <th class="sortable sort-amount">状态</th>
                                  </tr>
                                  </thead>
                                  <tbody>
                                  <c:forEach items="${detailList}" var="operation_list">
                                      <tr class="gradeX">
                                          <td class="cut">${operation_list.displayName}</td>
                                          <td class="cut">
                                              <fmt:formatDate
                                                      value="${operation_list.createTimeDate}"
                                                      pattern="yyyy-MM-dd HH:mm:ss"/>
                                          </td>
                                          <td class="cut">
                                              <c:if test="${operation_list.type == 1}">
                                                  <span class="label label-greensea">开机</span>
                                              </c:if>
                                              <c:if test="${operation_list.type == 2}">
                                                  <span class="label label-danger">关机</span>
                                              </c:if>
                                          </td>
                                          <td>
                                              <c:if test="${operation_list.status == 1}">
                                                  <span class="label label-greensea">成功</span>
                                              </c:if>
                                              <c:if test="${operation_list.status == 2}">
                                                  <span class="label label-danger">失败</span>
                                              </c:if>
                                          </td>
                                      </tr>
                                  </c:forEach>

                                  </tbody>
                              </table>

                          </div>



                      </div>

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


      });

    $(function(){
        $("#space").width($("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop").width()
                -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).width()
                -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(1).width()
                -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(2).width()
                -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(3).width()
                -119).height(
                $("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).height());
        $(window).resize(function(){
            $("#space").width($("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop").width()
                    -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).width()
                    -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(1).width()
                    -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(2).width()
                    -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(3).width()
                    -119);
        });
    });

    function redirect(url) {
        window.location.href = "${pageContext.request.contextPath}"+url;
    }
      
    </script>
    
  </body>
</html>
      
