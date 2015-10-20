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
            

            <h2><i class="fa fa-hdd-o"></i> 终端盒子实时情况</h2>
            

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
                    <button type="button" class="btn btn-green file-excel-o" onclick="exportData('/export/boxrealinfodata')">
                              <i class="fa fa-file-excel-o"></i>
                              <span>导出数据</span>
                    </button>
                      </div>
                  <!-- /tile header -->
                      <!-- start -->
                      <div class="tile-widget bg-transparent-black-2">
                          <div class="row">
                      <div class="col-sm-6 col-xs-6" style="z-index: 100;">
                          <div class="input-group table-options">
                          <span class="input-group-btn">
                                <input id="param" type="text" name="param" value="${parameter == null?"":parameter}"/>
                            </span>
                          <span class="input-group-btn">
                              <select id="status" class="chosen-select form-control" style="width: 150px;">
                                  <option value="">状态(全部)</option>
                                  <c:if test="${status == 0}">
                                      <option value="0" selected="selected">离线</option>
                                  </c:if>
                                  <c:if test="${status != 0}">
                                      <option value="0">离线</option>
                                  </c:if>
                                  <c:if test="${status == 1}">
                                      <option value="1" selected="selected">在线</option>
                                  </c:if>
                                  <c:if test="${status != 1}">
                                      <option value="1">在线</option>
                                  </c:if>
                              </select>
                              <%--</span>--%>
                           <%--<span class="input-group-btn">--%>
                                <button id="search_btn" class="btn btn-default" type="button">查看</button>
                              </span>
                          </div>
                      </div>
                              </div>
                          </div>

                  <div class="tile-body no-vpadding">

                    <div class="table-responsive">
                    
                      <table  class="table table-datatable table-custom table-sortable" id="basicDataTable">
                        <thead>
                          <tr>
<!-- 						    <th class="no-sort"> -->
<!--                             <div class="checkbox check-transparent"> -->
<!--                               <input type="checkbox" value="1" id="allchck"> -->
<!--                               <label for="allchck"></label> -->
<!--                             </div> -->
<!--                           </th> -->
                              <th class="sortable sort-alpha">用户名</th>
                              <th class="sortable sort-amount">盒子编号</th>
                              <th class="sortable sort-amount">ip</th>
                              <th class="sortable sort-amount">网关</th>
                              <th class="sortable sort-amount">盒子mac</th>
                              <th class="sortable sort-amount">软件版本</th>
                              <th class="sortable sort-amount">硬件版本</th>
                              <th class="sortable sort-amount">上次登录时间</th>
                              <th class="sortable sort-amount">上次Alive时间</th>
                              <th class="sortable sort-amount">累计在线时长</th>
                              <th class="sortable sort-amount">当前状态</th>
                           </tr>
                        </thead>
                        <tbody>
                          <c:forEach items="${infoList}" var="terminal_boxes">
                    		<tr class="gradeX">
<!--                     		<td> -->
<!-- 								<div class="checkbox check-transparent"> -->
<%-- 								  <input type="checkbox" value="${terminal_boxes.userName}" name="checkboxid" id="chck${terminal_boxes.userName}" > --%>
<%-- 								  <label for="chck${terminal_boxes.userName}"></label> --%>
<!-- 								</div> -->
<!--                                 </td> -->
                                <td class="cut">${terminal_boxes.userName}</td>
                                <td class="cut">${terminal_boxes.serialNumber}</td>
                                <td class="cut">${terminal_boxes.ip}</td>
                                <td class="cut">${terminal_boxes.gateway}</td>
                                <td class="cut">${terminal_boxes.mac}</td>
                                <td class="cut">${terminal_boxes.softwareVersion}</td>
                                <td class="cut">${terminal_boxes.hardwareVersion}</td>
                                <td class="cut"><fmt:formatDate
                                        value="${terminal_boxes.lastLoginDate}"
                                        pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td class="cut"><fmt:formatDate
                                        value="${terminal_boxes.lastAliveDate}"
                                        pattern="yyyy-MM-dd HH:mm:ss"/>
                                </td>
                                <td class="cut">${terminal_boxes.getCumulativeOnlineTimeText()}</td>       
                                <td class="cut">${terminal_boxes.caculateRealStatus()}</td>                                 
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

                              <div class="form-group">
                                <label style="align:center;" id="confirmcontent">确定要删除该终端盒子吗？</label>
                               </div>

                            </form>
                          </div>
                            <div class="modal-footer">
                                <button class="btn btn-green" id="confirm_btn" onclick="" data-dismiss="modal" aria-hidden="true">确定
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
        "aaSorting": [ [9,'asc']],
        "aoColumnDefs": [
                         { 'bSortable': false, 'aTargets': [ "no-sort" ] }
                       ], 
        "fnInitComplete": function(oSettings, json) {
            $('.dataTables_filter').css("text-align","right");
            $('.dataTables_filter').css("margin-top","-40px");
            $('.dataTables_filter').css("margin-bottom","0px");
            $('.dataTables_filter input').attr("placeholder", "Search");        }
      });
      
      
 
 

      /* Get the rows which are currently selected */
      function fnGetSelected(oTable01Local){
        return oTable01Local.$('tr.row_selected');
      };
 	  $("div[class='col-md-4 text-right sm-center']").attr("class","tile-footer text-right"); 

       
      
    })
    
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
        });

         $("#search_btn").click(function(){
             var param = $("#param").val();
             var status = $("#status").val();
             window.location.href = encodeURI(path + "/boxrealinfo/list?param="+param+"&status="+status);

         });

         $('#param').bind('keypress',function(event){
             if(event.keyCode == "13")
             {
                 $("#search_btn").click();
             }
         });
        
      }); 
      
    </script>
    
  </body>
</html>
      
