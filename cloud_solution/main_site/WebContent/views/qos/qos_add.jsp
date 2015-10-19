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


                <h2><i class="fa fa-cloud"></i> 创建QoS规则</h2>


            </div>
            <!-- /page header -->


            <!-- content main container -->
            <div class="main">
                <!-- row -->
                <div class="row">

                    <!-- col 12 -->
                    <div class="col-md-12">
                        <!-- tile -->
                        <section>
                            <!-- tile header -->
                            <div class="tile-header">
                                <c:if test="${type == 1}">
                                    <h4><a href="<%=request.getContextPath() %>/desktopqos/all" style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a><span style="color: rgb(250, 250, 250);">输入QoS规则</span></h4>

                                </c:if>

                                <c:if test="${type == 2}">
                                    <h4><a href="<%=request.getContextPath() %>/serverqos/all" style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a><span style="color: rgb(250, 250, 250);">输入QoS规则</span></h4>

                                </c:if>

                            </div>
                            <!-- /tile header -->
                        </section>
                        <section id="rootwizard" class="tabbable transparent tile color transparent-black">


                            <!-- tile widget -->
                            <div class="tile-widget nopadding color transparent-black rounded-top-corners">
                                <ul>
                                    <li class="li_tab"><a class="tab" href="#tab1" data-toggle="tab">主机选择</a></li>
                                    <li class="li_tab"><a class="tab" href="#tab2" data-toggle="tab">带宽设置</a></li>
                                    <li class="li_tab"><a class="tab" href="#tab3" data-toggle="tab">硬盘IOPS设置</a></li>
                                    <li class="li_tab"><a class="tab" href="#tab4" data-toggle="tab">VCPU设置</a></li>
                                    <li><a href="#tab5" data-toggle="tab">创建进度</a></li>
                                </ul>
                            </div>
                            <!-- /tile widget -->

                            <!-- tile body -->
                            <div class="tile-body">

                                <div id="bar" class="progress progress-striped active">
                                    <div class="progress-bar progress-bar-cyan animate-progress-bar"></div>
                                </div>

                                <div class="tab-content">

                                    <div class="tab-pane" id="tab1">
                                        <form class="form-horizontal form1" role="form" parsley-validate onkeydown="if(event.keyCode==13){return false;}">

                                            <div class="form-group">
                                                <label for="name" class="col-sm-2 control-label">规则名称 *</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control" id="name" name="name" parsley-trigger="change" parsley-required="true" parsley-minlength="1" parsley-maxlength="45" parsley-checkqosname ="true" parsley-validation-minlength="1">
                                                </div>
                                            </div>

                                            <div class="form-group">
                                                <label for="uuid" class="col-sm-2 control-label">选择云主机</label>
                                                <div class="col-sm-4">
                                                    <div class="col-sm-16" id="select_host">
                                                        <select class="chosen-select chosen-transparent form-control" id = "uuid" name="uuid" parsley-trigger="change" parsley-required="true" parsley-error-container="#select_host">
                                                            <option value="">请选择云主机</option>
                                                            <c:forEach items="${cloud_host_vo_list }" var="cloud_host_vo">
                                                                <option value="${cloud_host_vo.realHostId}">${cloud_host_vo.displayName}</option>
                                                            </c:forEach>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>

                                        </form>
                                    </div>

                                    <div class="tab-pane" id="tab2">

                                        <form class="form-horizontal form2" role="form" parsley-validate onkeydown="if(event.keyCode==13){return false;}">

                                            <div class="form-group">
                                                <label for="outbound_bandwidth" class="col-sm-2 control-label">最大上行带宽*</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control" id="outbound_bandwidth" name="outboundandwidth" parsley-trigger="change" parsley-required="true" parsley-max="10000" parsley-validation-minlength="1">
                                                </div>
                                                <label for="outbound_bandwidth" class="col-sm-1 control-label">MB</label>

                                            </div>

                                            <div class="form-group">
                                                <label for="inbound_bandwidth" class="col-sm-2 control-label">最大下行带宽*</label>
                                                <div class="col-sm-4">
                                                    <input type="text" class="form-control" id="inbound_bandwidth" name="inboundandwidth" parsley-trigger="change" parsley-required="true" parsley-max="10000" parsley-validation-minlength="1">
                                                </div>
                                                <label for="inbound_bandwidth" class="col-sm-1 control-label">MB</label>

                                            </div>
                                        </form>

                                    </div>

                                    <div class="tab-pane" id="tab3">

                                        <form class="form-horizontal form3" role="form" parsley-validate onkeydown="if(event.keyCode==13){return false;}">
                                            <div class="form-group">
                                                <label for="max_iops" class="col-sm-2 control-label">硬盘盘类型选择*</label>
                                                <div class="col-sm-4">
                                                    <div class="col-sm-16" id="select_iops">
                                                        <select class="chosen-select chosen-transparent form-control" id = "max_iops" name="maxIops" parsley-trigger="change" parsley-required="true" parsley-error-container="#select_iops">
                                                            <option value="4000">高速磁盘</option>
                                                            <option value="100">普通磁盘</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>

                                    </div>

                                    <div class="tab-pane" id="tab4">

                                        <form class="form-horizontal form4" role="form" parsley-validate onkeydown="if(event.keyCode==13){return false;}">
                                            <div class="form-group">
                                                <label for="priority" class="col-sm-2 control-label">VCPU设置*</label>
                                                <div class="col-sm-4">
                                                    <div class="col-sm-16" id="select_vcpu">
                                                        <select class="chosen-select chosen-transparent form-control" id = "priority" name="priority" parsley-trigger="change" parsley-required="true" parsley-error-container="#select_vcpu">
                                                            <option value="0">高优先级</option>
                                                            <option value="1">中优先级</option>
                                                            <option value="2">低优先级</option>
                                                        </select>
                                                    </div>
                                                </div>
                                            </div>
                                        </form>

                                    </div>

                                    <div class="tab-pane" id="tab5">

                                        <form class="form-horizontal form5" role="form" parsley-validate id="eula" onkeydown="if(event.keyCode==13){return false;}">

                                            <div class="form-group" align="center">
                                                <h4><strong><font color="azure" id="status_text"></font></strong></h4>
                                            </div>

                                        </form>

                                    </div>

                                </div>

                            </div>
                            <!-- /tile body -->

                            <!-- tile footer -->
                            <div class="tile-footer border-top color white rounded-bottom-corners nopadding">
                                <ul id="result" class="pager pager-full wizard">
                                    <li class="previous"><a href="javascript:;"><i class="fa fa-reply fa-lg"></i></a></li>
                                    <li class="next"><a href="javascript:;"><i class="fa fa-arrow-right fa-lg"></i></a></li>
                                    <li class="next finish" style="display:none;"><a href="javascript:;"><i class="fa fa-check fa-lg"></i></a></li>
                                </ul>
                            </div>
                            <!-- /tile footer -->

                        </section>
                        <!-- /tile -->
                        <%--<div class="tile-body">--%>

                            <%--<a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>--%>
                            <%--<a href="#modalConfirm" id="con" role="button"   data-toggle="modal"> </a>--%>


                            <%--<div class="modal fade" id="modalDialog" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">--%>
                                <%--<div class="modal-dialog">--%>
                                    <%--<div class="modal-content" style="width:60%;margin-left:20%;">--%>
                                        <%--<div class="modal-header">--%>
                                            <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>--%>
                                            <%--<h3 class="modal-title" id="modalDialogLabel"><strong>提示</strong></h3>--%>
                                        <%--</div>--%>
                                        <%--<div class="modal-body">--%>
                                            <%--<p id="tipscontent"></p>--%>
                                        <%--</div>--%>
                                    <%--</div><!-- /.modal-content -->--%>
                                <%--</div><!-- /.modal-dialog -->--%>
                            <%--</div><!-- /.modal -->--%>

                            <%--<div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >--%>
                                <%--<div class="modal-dialog">--%>
                                    <%--<div class="modal-content" style="width:60%;margin-left:20%;">--%>
                                        <%--<div class="modal-header">--%>
                                            <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>--%>
                                            <%--<h3 class="modal-title" id="modalConfirmLabel"><strong>确认</strong> </h3>--%>
                                        <%--</div>--%>
                                        <%--<div class="modal-body">--%>
                                            <%--<form role="form">--%>

                                                <%--<div class="form-group">--%>
                                                    <%--<label style="align:center;" id="confirmcontent">确定要删除该主机配置吗？</label>--%>
                                                <%--</div>--%>

                                            <%--</form>--%>
                                        <%--</div>--%>
                                        <%--<div class="modal-footer">--%>
                                            <%--<button class="btn btn-green"   onclick="toDelete();" data-dismiss="modal" aria-hidden="true">确定</button>--%>
                                            <%--<button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>--%>

                                        <%--</div>--%>
                                    <%--</div><!-- /.modal-content -->--%>
                                <%--</div><!-- /.modal-dialog -->--%>
                            <%--</div><!-- /.modal -->--%>

                        <%--</div>--%>
                    </div>
                    <!-- /col 12 -->
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

<script type="text/javascript">
    var path = "<%=request.getContextPath()%>";
    var isCommited = false;
    var type = parseInt("${type}");
    var add_url;
    var list_url;
    var flag_index = 0;

    if (type == 1) {
        list_url="desktopqos/all";
    }
    if (type == 2) {
        list_url="serverqos/all";
    }


    $(function() {


        //initialize form wizard
        $('#rootwizard').bootstrapWizard({

            'tabClass': 'nav nav-tabs tabdrop',
            onTabShow: function(tab, navigation, index) {
                var $total = navigation.find('li').not('.tabdrop').length;
                var $current = index+1;
                var $percent = ($current/$total) * 100;
                $('#rootwizard').find('#bar .progress-bar').css({width:$percent+'%'});

                // If it's the last tab then hide the last button and show the finish instead
                if($current >= $total) {
                    $('#rootwizard').find('.pager .next').hide();
                    $('#rootwizard').find('.pager .finish').show();
                    $('#rootwizard').find('.pager .finish').removeClass('disabled');
                } else {
                    $('#rootwizard').find('.pager .next').show();
                    $('#rootwizard').find('.pager .finish').hide();
                }
            },

            onNext: function(tab, navigation, index) {

                var form = $('.form' + index);

                form.parsley('validate');

                if(form.parsley('isValid')) {
                    tab.addClass('success');
                } else {
                    return false;
                }

                if(index==4){

                    var uuid = $("#uuid").val();
                    var name = $("#name").val();
                    var outbound_bandwidth = $("#outbound_bandwidth").val();
                    var inbound_bandwidth = $("#inbound_bandwidth").val();
                    var max_iops = $("#max_iops").val();
                    var priority = $("#priority").val();

                    if(isCommited){
                        return false;
                    }
                    isCommited = true;
                    jQuery.ajax({
                        url: path+'/qos/add',
                        type: 'post',
                        data:{uuid:uuid,type:type,name:name,outboundBandwidth:outbound_bandwidth,inboundBandwidth:inbound_bandwidth,maxIops:max_iops,priority:priority},
                        dataType: 'json',
                        timeout: 20000,
                        async: false,
                        success:function(data){
                            isCommited = false;
                            $("#status_text").html(data.message);
                            return;
                        }
                    });

                    $("#result li").eq(0).hide();
                    $(".tab").attr("href", "#");
                    return true;
                }

                if(index==5){
                    window.location.href = path + "/"+list_url;
                }

            },
            onTabClick: function(tab, navigation, index) {

                var form = $('.form' + (index + 1));

                form.parsley('validate');

                if (flag_index < index) {
                    flag_index = index;
                }

                if (flag_index == 4) {
                    return false;
                }

                if(form.parsley('isValid')) {
                    $("#result li").eq(0).show();
                    tab.addClass('success');

                } else {
                    return false;
                }

            }

        });

        // Initialize tabDrop

        $('.tabdrop').tabdrop({text: '<i class="fa fa-th-list"></i>'});
        //----------------------
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
        //$(".chosen-select").chosen({disable_search_threshold: 10});

        //check toggling
        $('.check-toggler').on('click', function(){
            $(this).toggleClass('checked');
        });

    })
</script>

</body>
</html>
      

