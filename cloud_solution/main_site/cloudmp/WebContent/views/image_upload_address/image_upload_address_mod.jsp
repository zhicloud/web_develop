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


                <h2><i class="fa fa-inbox"></i> 修改上传地址</h2>


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
                                <h3><a href="<%=request.getContextPath() %>/image_upload_address/all"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>输入上传地址信息</h3>
                                <div class="controls">
                                    <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                                </div>
                            </div>
                            <!-- /tile header -->

                            <!-- tile body -->
                            <div class="tile-body">

                                <form class="form-horizontal" id="image_upload_address_form" role="form" action="${pageContext.request.contextPath }/image_upload_address/mod" method="POST">
                                    <div class="form-group">
                                        <label for="service_name" class="col-sm-2 control-label" >服务名*</label>
                                        <div class="col-sm-4">
                                            <div class="col-sm-16" id="selectbox">
                                                <select class="chosen-select chosen-transparent form-control" id = "service_name" name="serviceName" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox">
                                                    <option value="">从获取到的服务选择</option>
                                                    <c:forEach items="${image_upload_address_list}" var="address">
                                                        <c:choose>
                                                            <c:when test="${image_upload_address.serviceName eq address.serviceName}">
                                                                <option value="${address.serviceName} " localIp = "${address.localIp}" localPort = "${address.localPort}" selected="selected">${address.serviceName} </option>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <option value="${address.serviceName} " localIp = "${address.localIp}" localPort = "${address.localPort}">${address.serviceName} </option>
                                                            </c:otherwise>
                                                        </c:choose>

                                                    </c:forEach>
                                                </select>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="local_ip" class="col-sm-2 control-label">内网IP*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="local_ip" name="localIp" value="" parsley-trigger="change" parsley-required="true"  readonly="readonly">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="local_port" class="col-sm-2 control-label">内网端口*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="local_port" name="localPort" value="" parsley-trigger="change" parsley-required="true"readonly="readonly">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="public_ip" class="col-sm-2 control-label">公网IP</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="public_ip" name="publicIp" value="${image_upload_address.publicIp}" parsley-trigger="change"  parsley-ip="true">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="public_port" class="col-sm-2 control-label">公网端口</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="public_port" name="publicPort" value="${image_upload_address.publicPort == 0 ? "" : image_upload_address.publicPort}"  parsley-trigger="change"  parsley-port="true">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="service_enable_0" class="col-sm-2 control-label">启用该地址</label>
                                        <div class="col-sm-16">
                                            <div class="radio radio-transparent col-md-2">
                                                <input type="radio" name="serviceEnable" id="service_enable_0" value="0">
                                                <label for="service_enable_0">否</label>
                                            </div>
                                            <div class="radio radio-transparent col-md-2">
                                                <input type="radio" name="serviceEnable" id="service_enable_1" value="1" >
                                                <label for="service_enable_1">是</label>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="description" class="col-sm-2 control-label">描述</label>
                                        <div class="col-sm-4">
                                            <textarea class="form-control" name="description" id="description" rows="6" parsley-maxlength="100" parsley-validation-minlength="1">${image_upload_address.description}</textarea>
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
                        </section>
                            <!-- /tile body -->

                            <div class="tile-body">
                                <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>

                                <div class="modal fade" id="modalDialog" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content" style="width:60%;margin-left:20%;">
                                            <div class="modal-header">
                                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="location.reload()">Close</button>
                                                <h3 class="modal-title" id="modalDialogLabel"><strong>提示</strong></h3>
                                            </div>
                                            <div class="modal-body">
                                                <p id="tipscontent"></p>
                                            </div>
                                        </div><!-- /.modal-content -->
                                    </div><!-- /.modal-dialog -->

                                </div><!-- /.modal -->

                            </div>


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
    var isCommited = false;
    var serviceEnable = "${image_upload_address.serviceEnable}";

    function check(){
        $("#service_enable_"+serviceEnable).click();
    }

    $(function(){

        check();

        changeLocal();

        jQuery("#save_btn").click(function(){

            // 空值处理
            var publicIp = $("#public_ip").val();
            var publicPort = $("#public_port").val();

            if (publicIp == "") {
                $("#public_ip").removeAttr("name");
            }

            if (publicPort == "") {
                $("#public_port").removeAttr("name");
            }

            var options = {
                success:function result(data){
                    if (data.status == "success") {
                        location.href = "${pageContext.request.contextPath}/image_upload_address/all";
                    } else {
                        $("#tipscontent").html(data.message);
                        $("#dia").click();
                    }
                },
                dataType:'json',
                timeout:10000
            };
            var form = jQuery("#image_upload_address_form");
            form.parsley('validate');
            if(form.parsley('isValid')){
                if(isCommited){
                    return false;
                }
                isCommited=true;
                jQuery("#image_upload_address_form").ajaxSubmit(options);
            }
        });

        $('#service_name').change(function(){
            changeLocal();
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

    function changeLocal() {
        var localIp = $('#service_name').children('option:selected').attr("localIp");
        var localPort = $('#service_name').children('option:selected').attr("localPort");
        $("#local_ip").val(localIp);
        $("#local_port").val(localPort);
    }

</script>

</body>
</html>

