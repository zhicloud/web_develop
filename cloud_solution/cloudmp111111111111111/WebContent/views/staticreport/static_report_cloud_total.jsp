<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- static_report_cloud_summary.jsp -->
<html>
  <head>
    <title>云主机-统计报表</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8" />
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


            <h2><i class="fa fa-area-chart"></i> 统计报表</h2>


          </div>
          <!-- /page header -->


          <!-- content main container -->
          <div class="main">

            <!-- row -->
            <div class="row">

              <!-- col 6 -->
          <div class="col-md-12">

				  <section class="tile color transparent-black">
                      <div class="tile-widget color transparent-black rounded-top-corners nopadding nobg">
                      <!-- Nav tabs -->
                      <ul class="nav nav-tabs tabdrop">
                          <li class="active" style="width:10%;text-align: center;"><a href="#" data-toggle="tab">云主机</a></li>
                          <li class="" style="width:90%;"><a href="#" data-toggle="tab" onclick="gotoPage('/staticreport/serverindex');">宿主机</a></li>
                      </ul>
                      <!-- / Nav tabs -->
                    </div>

                  <div class="tile-body no-vpadding" style="margin-top:-10px;">
                    <div class="table-responsive">
                    <hr>
                    <div class="row">
                      <div class="col-md-8 text-left">
						<div style="width:100px;float: left;">所有云主机</div><br>
                        <div style="height:20px;"></div>                      
                        <div style="width:100px;float: left;">云主机个数:${total_num }</div>
                        <div style="width:100px;float: left;padding-left:5px;">正常:${statusdata.normal }</div>
                        <div style="width:100px;float: left;">故障:${statusdata.error }</div>
                        <div style="width:100px;float: left;">告警:${statusdata.warning }</div>
                        <div style="width:100px;float: left;">屏蔽:${statusdata.stop }</div><br>
                        <div style="height:20px;"></div>
                        <div style="width:100px;">CPU总核数:${cpu_total }</div>
                      </div>
                        <div class="col-sm-4" id="cloudsearch">
							<select class="chosen-select chosen-transparent form-control" id="cloudid" parsley-trigger="change" parsley-required="true" parsley-error-container="#cloudsearch" onchange="querycloud(this)">
	                            <option value="">云主机搜索</option>  
	                            <c:forEach items="${cloudlists }" var="cloud">
	                            	<option value="${cloud.id }">${cloud.name }</option>
	                            </c:forEach>
	                          </select>  
	                     </div>
                    </div>
                    <div class="row">
                      <div class="col-md-4 text-center">
                        <h5 class="text-left"><strong>内存</strong></h5>
                        <div style="text-align: left;padding:2px;">
                        <div style="float:left;width:80%;"><span class="label label-success">未使用</span></div>
                        <div style="float:left;width:80%;margin-top:5px;"><span class="label label-danger">已使用</span></div>
                        </div>
                        <span id="memory_pie"></span>
                      </div>
                      <div class="col-md-4 text-center">
                        <h5 class="text-left"><strong>磁盘</strong></h5>
                        <div style="text-align: left;padding:2px;">
                        	<div style="float:left;width:80%;"><span class="label label-success">未使用</span></div>
                        	<div style="float:left;width:80%;margin-top:5px;"><span class="label label-danger">已使用</span></div>
                        </div>
                        <span id="disk_pie"></span>
                      </div>
                    </div>
                      
                    </div>

                  </div>
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

    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/mmenu/js/jquery.mmenu.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/sparkline/jquery.sparkline.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/nicescroll/jquery.nicescroll.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/animate-numbers/jquery.animateNumbers.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/videobackground/jquery.videobackground.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/blockui/jquery.blockUI.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.categories.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.pie.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.stack.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/graphtable/jquery.graphTable-0.3.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.tooltip.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/raphael-min.js"></script> 
    <script src="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/d3.v2.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/rickshaw.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/morris/morris.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/easypiechart/jquery.easypiechart.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/justgage/justgage.js"></script>    


    <script>
     $(function(){
        //chosen select input
        $(".chosen-select").chosen({disable_search_threshold: 10});
        $('#memory_pie').sparkline([${memory_canuse},${memory_used}], {
            type: 'pie',
            width: 'auto',
            height: '150px',
            tagOptionPrefix:'spark',
            sliceColors: ['rgb(92, 184, 92)','rgb(217, 83, 79)']
          });
        $('#disk_pie').sparkline([${disk_canuse},${disk_used}], {
            type: 'pie',
            width: 'auto',
            height: '150px',
            enableTagOptions:true,
            tagOptionPrefix:'spark',
            sliceColors: ['rgb(92, 184, 92)','rgb(217, 83, 79)']
          });
      });
    //云主机概览页面跳转 
	function querycloud(obj){
		var id = $(obj).val();
		if(id==undefined||id==null||id==""){
			return;
		}
		window.location.href = "<%=request.getContextPath()%>/staticreport/cloudhost/"+id;
	}
    //页面跳转
    function gotoPage(url) {
        window.location.href = "<%=request.getContextPath()%>"+url;
    }
    </script>
    
  </body>
</html>
      
