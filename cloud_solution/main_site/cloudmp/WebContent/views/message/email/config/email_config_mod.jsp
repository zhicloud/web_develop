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


                <h2><i class="fa fa-inbox"></i> 修改邮件配置</h2>


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
                                <h3><a href="<%=request.getContextPath() %>/message/email/config/list" style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>输入邮件配置信息</h3>
                            </div>
                            <!-- /tile header -->

                            <!-- tile body -->
                            <div class="tile-body">

                                <form class="form-horizontal" id="email_config_form" role="form" action="${pageContext.request.contextPath }/message/email/config/mod" method="post">
                                    <input type="hidden" id="id" name="id" value="${mail_config_vo.id}">
                                    <div class="form-group">
                                        <label for="name" class="col-sm-2 control-label" >邮件配置名称*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="name" name="name" value="${mail_config_vo.name}" oldname="${mail_config_vo.name}" parsley-trigger="change" parsley-required="true" parsley-minlength="5" parsley-maxlength="50" parsley-checkemailconfigname="true">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="protocol" class="col-sm-2 control-label" >协议*</label>
                                        <div class="col-sm-4" id="selectprotocol">
                                            <select class="chosen-select chosen-transparent form-control" id = "protocol" name="protocol" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectprotocol">
                                                <option value="">请选择邮件服务协议</option>
                                                <c:if test="${mail_config_vo.protocol eq 'smtp'}">
                                                    <option value="smtp" selected="selected">smtp</option>
                                                    <option value="pop3">pop3</option>
                                                    <option value="imap">imap</option>
                                                </c:if>
                                                <c:if test="${mail_config_vo.protocol eq 'pop3'}">
                                                    <option value="smtp">smtp</option>
                                                    <option value="pop3" selected="selected">pop3</option>
                                                    <option value="imap">imap</option>
                                                </c:if>
                                                <c:if test="${mail_config_vo.protocol eq 'imap'}">
                                                    <option value="smtp">smtp</option>
                                                    <option value="pop3">pop3</option>
                                                    <option value="imap" selected="selected">imap</option>
                                                </c:if>

                                            </select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="host" class="col-sm-2 control-label" >邮件服务器*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="host" name="host" value="${mail_config_vo.host}" parsley-trigger="change" parsley-required="true" parsley-minlength="5" parsley-maxlength="50">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="port" class="col-sm-2 control-label" >服务端口*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="port" name="port" value="${mail_config_vo.port}" parsley-trigger="change" parsley-required="true" parsley-minlength="1" parsley-maxlength="5">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="is_auth0" class="col-sm-2 control-label" >是否要求身份验证*</label>
                                        <div class="col-sm-8">
                                            <div class="radio radio-transparent col-md-2">
                                                <input type="radio" name="isAuth" id="is_auth0" value="0">
                                                <label for="is_auth0">否</label>
                                            </div>
                                            <div class="radio radio-transparent col-md-2">
                                                <input type="radio" name="isAuth" id="is_auth1" value="1" checked="checked">
                                                <label for="is_auth1">是</label>
                                            </div>
                                        </div>
                                    </div>


                                    <div class="form-group">
                                        <label for="sender" class="col-sm-2 control-label">发件人*</label>
                                        <div class="col-sm-4">
                                            <input type="text" class="form-control" id="sender" name="sender" value="${mail_config_vo.sender}" parsley-trigger="change" parsley-required="true" parsley-minlength="5" parsley-maxlength="50">
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="senderAddress" class="col-sm-2 control-label">发件邮箱*</label>
                                        <div class="col-sm-4">
                                            <input type="email" class="form-control" id="senderAddress" name="senderAddress" value="${mail_config_vo.senderAddress}" parsley-trigger="change"   parsley-type="email"  >
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="password" class="col-sm-2 control-label">密码*</label>
                                        <div class="col-sm-4">
                                            <input type="password" class="form-control" id="password" name="password"  value="${mail_config_vo.password}" parsley-trigger="change" parsley-required="true"  parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="confirm_password" class="col-sm-2 control-label">密码确认*</label>
                                        <div class="col-sm-4">
                                            <input type="password" class="form-control" id="confirm_password" name="confirm_password" value="${mail_config_vo.password}" parsley-trigger="change" parsley-required="true"  parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1" parsley-equalto="#password">
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

        $("#is_auth"+'${mail_config_vo.isAuth}').click();

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
                                $("#tipscontent").html("添加失败");
                                $("#dia").click();
                            }else{
                                location.href = path + "/message/email/config/list";
                            }
                        },
                        dataType:'json',
                        timeout:10000
                    };
                    var form = jQuery("#email_config_form");
                    form.parsley('validate');
                    if(form.parsley('isValid')){
                        jQuery("#email_config_form").ajaxSubmit(options);
                    }
                }
            }
        });

    }

</script>

</body>
</html>
      
