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
                <c:if test="${rack_name != null}">
                    <h2><i class="fa fa-hdd-o"></i> ${rack_name }的服务器清单管理</h2>
                </c:if>
                <c:if test="${rack_name == null}">
                    <h2><i class="fa fa-hdd-o"></i> 服务器清单管理</h2>
                </c:if>
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

                                <div class="controls">
                                    <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                                </div>

                            </div>
                            <!-- /tile header -->

                            <div class="tile-body no-vpadding">

                                <div class="table-responsive">

                                    <table  class="table table-datatable table-custom table-sortable" id="basicDataTable">
                                        <thead>
                                        <tr>
                                            <%--<th class="no-sort">--%>
                                            <%--<div class="checkbox check-transparent">--%>
                                            <%--<input type="checkbox" value="1" id="allchck">--%>
                                            <%--<label for="allchck"></label>--%>
                                            <%--</div>--%>
                                            <%--</th>--%>
                                            <th class="sortable sort-alpha">服务器名</th>
                                            <th class="sortable sort-amount">CPU核心数</th>
                                            <th class="sortable sort-amount">CPU利用率</th>
                                            <th class="sortable sort-amount">内存利用率</th>
                                            <th class="sortable sort-amount">磁盘利用率</th>
                                            <th class="sortable sort-amount">IP</th>
                                            <th>状态</th>
                                            <th>操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <c:forEach items="${servers}" var="server">
                                            <tr class="gradeX">
                                                    <%--<td>--%>
                                                    <%--<div class="checkbox check-transparent">--%>
                                                    <%--<input type="checkbox" value="${server.uuid}" name="checkboxid" id="chck${server.uuid}" >--%>
                                                    <%--<label for="chck${server.uuid}"></label>--%>
                                                    <%--</div>--%>
                                                    <%--</td>--%>
                                                <td class="cut">${server.name}</td>
                                                <td class="cut">${server.cpuCount}</td>
                                                <td class="cut"><fmt:formatNumber value="${server.cpuUsage}" type="percent"/></td>
                                                <td class="cut"><fmt:formatNumber value="${server.memoryUsage}" type="percent"/></td>
                                                <td class="cut"><fmt:formatNumber value="${server.diskUsage}" type="percent"/></td>
                                                        <td class="cut">${server.ip}</td>
                                                        <td>
                                                    <c:if test="${server.status == 0}">
                                                        <span class="label label-greensea">正常</span>
                                                    </c:if>
                                                    <c:if test="${server.status == 1}">
                                                        <span class="label label-warning">告警</span>
                                                    </c:if>
                                                    <c:if test="${server.status == 2}">
                                                        <span class="label label-danger">故障</span>
                                                    </c:if>
                                                    <c:if test="${server.status == 3}">
                                                        <span class="label label-dutch">停止</span>
                                                    </c:if>
                                                </td>
                                                        <td>
                                                        <div class="btn-group">
                                                            <button type="button"
                                                                    class="btn btn-default btn-xs dropdown-toggle"
                                                                    data-toggle="dropdown">
                                                                操作 <span class="caret"></span>
                                                            </button>
                                                            <ul class="dropdown-menu" role="menu">
                                                                <li><a href="#" onclick="show_network_divice('${room.uuid }', '${room.name}')">网络接口详情</a></li>
                                                            </ul>
                                                        </div>
                                                        </td>
                                            </tr>
                                        </c:forEach>

                                        </tbody>
                                    </table>

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
    var path = "<%=request.getContextPath()%>";

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
            "aaSorting": [ [6,'desc']],
            "aoColumnDefs": [
                { 'bSortable': false, 'aTargets': [ "no-sort" ] }
            ],
            "fnInitComplete": function(oSettings, json) {
                $('.dataTables_filter').css("text-align","right");
                $('.dataTables_filter').css("margin-top","-20px");
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
        })

    });

    function show_network_divice(uuid, name) {
        window.location.href = "${pageContext.request.contextPath}/rack/"+uuid+"/"+name+"/list";
    }

</script>

</body>
</html>
      
