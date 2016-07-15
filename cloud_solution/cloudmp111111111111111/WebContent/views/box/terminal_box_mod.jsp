<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- terminal_box_mod.jsp -->
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


        <h2><i class="fa fa-inbox"></i> 修改云终端</h2>


      </div>
      <!-- /page header -->


      <!-- content main container -->
      <div class="main">






        <!-- row -->
        <div class="row">

          <div class="col-md-12">



            <!-- tile -->
            <section class="tile color transparent-black">



              <!-- tile header -->
              <div class="tile-header">
                <h3><a href="<%=request.getContextPath() %>/box/list"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>输入云终端信息</h3>
                <div class="controls">
                  <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                </div>
              </div>
              <!-- /tile header -->

              <!-- tile body -->
              <div class="tile-body">

                <form class="form-horizontal" id="terminal_box_form" role="form" action="${pageContext.request.contextPath }/box/modify" method="post">

                    <input type="hidden" name="id" value="${terminal_box.id}">

                                        <div class="form-group">
                                        <label for="name" class="col-sm-2 control-label">状态</label>
                                        <div class="col-sm-4">
                                           <c:if test="${terminal_box.status == 0}">
                                                                                                           未分配
                                             <button type="button" class="btn btn-red delete"  onclick="allocate_box('${terminal_box.id}')">
                                                  <i class="fa fa-plus"></i>
                                                  <span> 分配 </span>
                                              </button>
                                            </c:if>
                                            <c:if test="${terminal_box.status == 1}">
                                                                                                           已分配
                                             <button type="button" class="btn btn-red delete" onclick="$('#con').click();">
                                                  <i class="fa fa-chain-broken"></i>
                                                  <span> 回收</span>
                                              </button>
                                            </c:if>
                                         </div>
                                      </div>
                                      <c:if test="${terminal_box.status == 1}">
                                       <div class="form-group">
                                        <label for="name" class="col-sm-2 control-label">分配用户</label>
                                        <div class="col-sm-4">
                                               ${terminal_box.allocateUser}
                                          </div>
                                      </div>
                                      </c:if>

                    <div class="form-group">
                    <label for="serial_number" class="col-sm-2 control-label">云终端编号*</label>
                    <div class="col-sm-4">
                      <input type="text" class="form-control" id="serial_number" name="serialNumber" value="${terminal_box.serialNumber}" oldname="${terminal_box.serialNumber}" parsley-checkdboxserial="true" parsley-trigger="change" parsley-required="true" parsley-minlength="5" parsley-maxlength="50">
                    </div>
                  </div>

                  <div class="form-group">
                    <label for="name" class="col-sm-2 control-label">云终端名称*</label>
                    <div class="col-sm-4">
                      <input type="text" class="form-control" id="name" name="name" value="${terminal_box.name}" oldname="${terminal_box.name}" parsley-trigger="change" parsley-required="true" parsley-minlength="5" parsley-maxlength="50">
                    </div>
                  </div>
                  
                  
                  <div class="form-group form-footer footer-white">
                    <div class="col-sm-offset-4 col-sm-8">
                      <button type="button" class="btn btn-greensea" id="save_btn"><i class="fa fa-plus"></i>
                        <span> 保 存 </span></button>
                      <button type="reset" class="btn btn-red"><i class="fa fa-refresh"></i>
                        <span> 重 置 </span></button>
                    </div>
                  </div>

                </form>

              </div>
              <!-- /tile body -->
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
                                <label style="align:center;" id="confirmcontent">确定要回收该云终端吗？</label>
                               </div>

                            </form>
                          </div>
                            <div class="modal-footer">
                                <button class="btn btn-green" id="confirm_btn" onclick="release_box('${terminal_box.id}')" data-dismiss="modal" aria-hidden="true">确定
                                </button>
                                <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->    
                    
                    <%--<div class="modal fade" id="modalimagetype" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true">--%>
                      <%--<div class="modal-dialog">--%>
                        <%--<div class="modal-content">--%>
                          <%--<div class="modal-header">--%>
                            <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>--%>
                            <%--<h3 class="modal-title" id="modalConfirmLabel"><strong>镜像用途</strong></h3>--%>
                          <%--</div>--%>
                          <%--<div class="modal-body">--%>
                            <%--<form class="form-horizontal" role="form" parsley-validate id="basicvalidations_imagetype" action="<%=request.getContextPath() %>/image/change/imagetype" method="post"   >--%>
                            	<%--<input id = "ids_type" name="ids" type="hidden"> --%>
                              <%--<div class="form-group">--%>
		                        <%--<label for="input01" class="col-sm-2 control-label">镜像用途</label>--%>
		                        <%--<div class="col-sm-16">--%>
		                          <%--<div class="radio   col-md-4">--%>
		                            <%--<input type="radio" name="imageType"   id="optionsRadios11" value="1" checked>--%>
		                            <%--<label for="optionsRadios11">通用镜像</label>--%>
		                          <%--</div>--%>
		                          <%--<div class="radio  col-md-4">--%>
		                            <%--<input type="radio" name="imageType"   id="optionsRadios12" value="2">--%>
		                            <%--<label for="optionsRadios12">桌面云专用镜像</label>--%>
		                          <%--</div> --%>
		                           <%----%>
		                        <%--</div>--%>
		                      <%--</div>--%>
		                      <%--<div class="form-group">--%>
		                        <%--<label for="input01" class="col-sm-2 control-label"></label>--%>
		                        <%--<div class="col-sm-16"> --%>
		                          <%--<div class="radio  col-md-4">--%>
		                            <%--<input type="radio" name="imageType"   id="optionsRadios13" value="3">--%>
		                            <%--<label for="optionsRadios13">云主机专用镜像</label>--%>
		                          <%--</div>--%>
		                          <%--<div class="radio  col-md-4">--%>
		                            <%--<input type="radio" name="imageType"   id="optionsRadios14" value="4">--%>
		                            <%--<label for="optionsRadios14">专属云专用镜像</label>--%>
		                          <%--</div>--%>
		                        <%--</div>--%>
		                      <%--</div>--%>

                            <%--</form>--%>
                          <%--</div>--%>
                          <%--<div class="modal-footer">--%>
                            <%--<button class="btn btn-green" onclick="saveImageType();">保存</button>--%>
                            <%--<button class="btn btn-red" data-dismiss="modal" aria-hidden="true">关闭</button>--%>
                          <%--</div>--%>
                        <%--</div><!-- /.modal-content -->--%>
                      <%--</div><!-- /.modal-dialog -->--%>
                    <%--</div><!-- /.modal -->    --%>
                    
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
            <!-- /tile -->

          </div>
          <!-- /col 6 -->

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

  $(function(){
    jQuery("#save_btn").click(function(){
      var options = {
        success:function result(data){
          location.href = "${pageContext.request.contextPath}/box/list";
        },
        dataType:'json',
        timeout:10000
      };
      var form = jQuery("#terminal_box_form");
      form.parsley('validate');
      if(form.parsley('isValid')){
        jQuery("#terminal_box_form").ajaxSubmit(options);
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
    });






  })
  
  function allocate_box(id) {
            window.location.href = "${pageContext.request.contextPath}/box/"+id+"/allocate";
        }

  function release_box(id) {
      jQuery.ajax({
          url: '<%=request.getContextPath()%>/box/release',
          type: 'post',
          dataType: 'json',
          data:{id:id}, 
          async: false,
          timeout: 10000,
          error: function()
          {
          },
          success: function(result)
          {
              if(result.status=="success"){
                  window.location.reload();
              }else{
                  $("#tipscontent").html(result.message);
                  $("#dia").click();
              }
          }

      });
  }

</script>

</body>
</html>
      
