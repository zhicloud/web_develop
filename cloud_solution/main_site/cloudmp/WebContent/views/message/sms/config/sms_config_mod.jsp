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
    <link rel="stylesheet" href="<%=request.getContextPath() %>/editor/themes/default/default.css" />

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


                <h2><i class="fa fa-inbox"></i> 修改短信配置</h2>


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
                                <h3><a href="<%=request.getContextPath() %>/message/sms/config/list"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>输入短信配置信息</h3>
                            </div>
                            <!-- /tile header -->

                            <!-- tile body -->
                            <div class="tile-body">

                                <form class="form-horizontal" id="sms_config_form" role="form" action="${pageContext.request.contextPath }/message/sms/config/mod" method="post">
                                   <input type="hidden" id="id" name="id" value="${sms_config_vo.id}">
                                    <div class="form-group">
                                        <label for="config_name" class="col-sm-2 control-label" >短信配置名称*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="config_name" name="configName" value="${sms_config_vo.configName}" oldname="${sms_config_vo.configName}"  parsley-trigger="change" parsley-required="true" parsley-minlength="2" parsley-maxlength="50" parsley-checksmsconfigname="true">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="sms_id" class="col-sm-2 control-label">短信配置ID*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="sms_id" name="smsId" value="${sms_config_vo.smsId}" parsley-trigger="change" parsley-required="true" parsley-type="nochinese" parsley-minlength="2" parsley-maxlength="50">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="service_url" class="col-sm-2 control-label">服务端地址*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="service_url" name="serviceUrl" value="${sms_config_vo.serviceUrl}" parsley-trigger="change"  parsley-required="true" parsley-type="nochinese" >
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="name" class="col-sm-2 control-label">发送人账户*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="name" name="name" value="${sms_config_vo.name}" parsley-trigger="change" parsley-required="true" >
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="password" class="col-sm-2 control-label">密码*</label>
                                        <div class="col-sm-4">
                                            <input type="password" class="form-control" id="password" name="password" value="${sms_config_vo.password}"  parsley-trigger="change" parsley-required="true"  parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="confirm_password" class="col-sm-2 control-label">密码确认*</label>
                                        <div class="col-sm-4">
                                            <input type="password" class="form-control" id="confirm_password" name="confirm_password" value="${sms_config_vo.password}"  parsley-trigger="change" parsley-required="true"  parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1" parsley-equalto="#password">
                                        </div>
                                    </div>


                                    <div class="form-group form-footer footer-white">
                                        <div class="col-sm-offset-4 col-sm-8">
                                            <button type="button" class="btn btn-greensea" onclick="saveForm();"><i class="fa fa-plus"></i>
                                                <span> 保 存 </span></button>
                                            <button type="reset" class="btn btn-red"><i class="fa fa-refresh"></i>
                                                <span> 重 置 </span></button>
                                        </div>
                                    </div>


                                </form>

                                <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
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
                            <!-- /tile body -->

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

    });

    function saveForm(){
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
                    var options = {
                        success:function result(data){
                            if(data.status == "fail"){
                                $("#tipscontent").html(data.message);
                                $("#dia").click();
                            }else{
                                location.href = path + "/message/sms/config/list";
                            }
                        },
                        dataType:'json',
                        timeout:10000
                    };
                    var form = jQuery("#sms_config_form");
                    form.parsley('validate');
                    if(form.parsley('isValid')){
                        jQuery("#sms_config_form").ajaxSubmit(options);
                    }
                }
            }
        });

    }

</script>

</body>
</html>
      
