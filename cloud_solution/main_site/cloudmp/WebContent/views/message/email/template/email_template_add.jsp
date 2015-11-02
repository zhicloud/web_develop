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


                <h2><i class="fa fa-inbox"></i> 新增邮件模板</h2>


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
                                <h3><a href="<%=request.getContextPath() %>/message/email/template/list"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>输入邮件模板信息</h3>
                            </div>
                            <!-- /tile header -->

                            <!-- tile body -->
                            <div class="tile-body">

                                <form class="form-horizontal" id="email_template_form" role="form" action="${pageContext.request.contextPath }/message/email/template/add" method="post">

                                    <div class="form-group">
                                        <label for="config_id" class="col-sm-2 control-label">邮件服务器</label>
                                        <div class="col-sm-4">
                                            <div class="col-sm-16" id="selectbox">
                                            <select class="chosen-select chosen-transparent form-control" id = "config_id" name="config_id" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox">
                                                <option value="">请选择邮件服务器</option>
                                                <c:forEach items="${email_config_list }" var="email_configs">
                                                    <option value="${email_configs.id}">${email_configs.name}</option>
                                                </c:forEach>
                                            </select>
                                           </div>
                                        </div>
                                    </div>


                                    <div class="form-group">
                                        <label for="name" class="col-sm-2 control-label" >邮件模板名称*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="name" name="name" value="" parsley-trigger="change" parsley-required="true" parsley-minlength="5" parsley-maxlength="50" parsley-checkemailtemplatename="true">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="code" class="col-sm-2 control-label">代码*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="code" name="code" value="" parsley-trigger="change" parsley-required="true" parsley-minlength="2" parsley-maxlength="50" parsley-checkemailtemplatecode="true">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="subject" class="col-sm-2 control-label">邮件主题*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="subject" name="subject" parsley-trigger="change"   parsley-required="true" parsley-minlength="5" parsley-maxlength="50">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="recipient" class="col-sm-2 control-label">收件人*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="recipient" name="recipient"  parsley-trigger="change" parsley-required="true"  parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1">
                                        </div>
                                    </div>

                                    <div class="form-group transparent-editor">
                                        <label for="email_content_check" class="col-sm-2 control-label">邮件正文*</label>
                                        <div class="col-sm-4" style="width:600px;">
                                            <div class="form-control" id="email_content"></div>
                                            <textarea class="note-editable" id="email_content_check" name="content" style="visibility:hidden" parsley-trigger="change" parsley-required="true" parsley-minlength="2" parsley-maxlength="1000" parsley-validation-minlength="1"></textarea>
                                        </div>
                                    </div>

                                    <div class="form-group form-footer footer-white">
                                        <div class="col-sm-offset-4 col-sm-8">
                                            <button type="button" class="btn btn-greensea" onclick="saveForm();"><i class="fa fa-plus"></i>
                                                <span> 创 建 </span></button>
                                            <button type="reset" class="btn btn-red"><i class="fa fa-refresh"></i>
                                                <span> 重 置 </span></button>
                                        </div>
                                    </div>


                                </form>

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
<script charset="utf-8" src="<%=request.getContextPath() %>/editor/kindeditor-min.js" type="text/javascript"></script>
<script charset="utf-8" src="<%=request.getContextPath() %>/editor/zh_CN.js" type="text/javascript"></script>
<script charset="utf-8" src="<%=request.getContextPath() %>/assets/js/vendor/summernote/summernote.min.js" type="text/javascript"></script>

<script>
    var path = "<%=request.getContextPath()%>";

    $(function(){

        //load wysiwyg editor
        $('#email_content').summernote({
            toolbar: [
                //['style', ['style']], // no style button
                ['style', ['bold', 'italic', 'underline', 'clear']],
                ['fontsize', ['fontsize']],
                ['color', ['color']],
                ['para', ['ul', 'ol', 'paragraph']],
                ['height', ['height']],
                //['insert', ['picture', 'link']], // no insert buttons
                //['table', ['table']], // no table button
                //['help', ['help']] //no help button
            ],
            height: 200   //set editable area's height
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

    });

    function contentCheck(e) {
        var min = $("#email_content_check").attr("parsley-minlength");
        var max = $("#email_content_check").attr("parsley-maxlength");

        if (e.length < min) {
            $("#email_content_check").val("");
            $(".note-editable").css("background-color","rgba(255, 74, 67, 0.2)");
        }
        if (e.length > max) {
            $(".note-editable").css("background-color","rgba(255, 74, 67, 0.2)");

        }
    }

    function saveForm(){

        $("#email_content_check").val($(".note-editable")[0].innerText);

        var e = $("#email_content_check").val();
        contentCheck(e);

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
                                location.href = path + "/message/email/template/list";
                            }
                        },
                        dataType:'json',
                        timeout:10000
                    };
                    var form = jQuery("#email_template_form");
                    form.parsley('validate');
                    if(form.parsley('isValid')){
                        $("#email_content_check").val($(".note-editable")[0].innerHTML);
                        jQuery("#email_template_form").ajaxSubmit(options);
                    }
                }
            }
        });

    }

</script>

</body>
</html>
      
