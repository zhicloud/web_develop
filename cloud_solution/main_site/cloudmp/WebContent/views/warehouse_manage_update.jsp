<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- warehouse_manage_update.jsp -->
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
            

            <h2><i class="fa fa-desktop"></i> 修改主机仓库信息</h2>
            

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
                    <h3><a href="<%=request.getContextPath() %>/warehouse/all"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>输入仓库信息</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <form class="form-horizontal" id="hostwarehouseform" role="form" action="${pageContext.request.contextPath }/warehouse/${warehouse.id }/update" method="post">
                      <input type="hidden" id="old_amount_input" value="${warehouse.totalAmount }">
                      <input type="hidden" id="old_name" value="${warehouse.name }">
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">仓库名称*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" name="name" oldname="${warehouse.name }" id="warehouse_name" value="${warehouse.name }" parsley-trigger="change" parsley-required="true" parsley-checkdesktopwarehousename="true" parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1">
                        </div>
                      </div>
                        
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">主机类型选择*</label>
                        <div class="col-sm-4" id="selectbox"> 
                            <div class="col-sm-16" >
                            	<select name="cloudHostConfigModelId" class="chosen-select chosen-transparent form-control" id="cloudHostConfigModelId" style="width:216px;" parsley-required="true" parsley-error-container="#selectbox">
                                <option value="" selected="selected">请选择</option>
                                <c:forEach items="${cloudHostConfigModeList }" var="chcm">
                                	<option value="${chcm.id }">${chcm.name }</option>
                                </c:forEach>
                                </select> 
                                <a href="#" id="create_new_host_type" class="btn btn-greensea"><span>新增主机类型</span></a>
                            </div> 
                          
                        </div>
                      </div>
                        
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">库存个数*</label>
                        <div class="col-sm-4">
                          <input type="text" value="${warehouse.totalAmount }" class="form-control" name="totalAmount" id="host_name" maxlength="4" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" parsley-required="true" parsley-max="100" parsley-min="${warehouse.totalAmount }">
                        </div>
                      </div>
                    
                       

                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8">
                          <button type="button" class="btn btn-greensea" onclick="saveWarehouseForm();"><i class="fa fa-plus"></i>
                              <span> 修 改</span></button>
                          <button type="reset" class="btn btn-red"><i class="fa fa-refresh"></i>
                              <span> 重 置 </span></button>
                        </div>
                      </div>
                            
                    </form>

                  </div>
                  <!-- /tile body -->
                  
                


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
	
    <script type="text/javascript">
    var path = "<%=request.getContextPath()%>";
    var sys_image_id = "${warehouse.cloudHostConfigModelId}";
    $(function(){
    	if(sys_image_id!="0"){
    		$("#cloudHostConfigModelId").val(sys_image_id);
    	}
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
	  
      $("#create_new_host_type").click(function(){
    	  location.href = path+"/chcm/addpage";
      });
    })
      
    </script>
    
  </body>
</html>
      

