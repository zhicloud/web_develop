<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<!-- set_time_startup_host_manage.jsp -->
<html>
  <head>
    <title>控制台-${productName} </title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  </head>
<%@include file="/views/common/common_menus.jsp" %>
<style>
 body #content .tile table.table-custom > tbody > tr > th,
 body #content .tile table.table-custom > tbody > tr > td{
 font-size:9px;
 }
</style>  
<script type="text/javascript">
var menuid = "${menuid}";
//返回子菜单页面
function backhome(){
	window.location.href = "<%=request.getContextPath() %>/transform/menuadmin/children?parentid=${parentid}";	
}
//选择开机云主机
function chooseStartupHost(){
    var array = new Array();
    $("input[name=startup_host_unchoosecheck]:checkbox").each(function(i){
        if($(this).get(0).checked){
            array[i] = $(this).val();
        }
    });
    if(array.length==0){
        $("#startup_host_tipscontent").html("请至少选择一条未关联数据");
        $("#startup_host_dia").click();
    }else{
        var temphtml = "";
        $("input[name=startup_host_unchoosecheck]:checkbox").each(function(i){
            if($(this).get(0).checked){
                $(this).attr("name","startup_host_choosecheck");
                $(this).attr("checked","");
                temphtml +="<tr>"+$(this).parents("tr").html()+"</tr>";
                $(this).parents("tr").remove();
            }
        });
        if(temphtml!=""){
            $("#startup_host_choosetable").find("tbody").append(temphtml);
        }
    }
}
//取消选择开机云主机
function unchooseStartupHost(){
    var array = new Array();
    $("input[name=startup_host_choosecheck]:checkbox").each(function(i){
        if($(this).get(0).checked){
            array[i] = $(this).val();
        }
    });
    if(array.length==0){
        $("#startup_host_tipscontent").html("请至少选择一条已关联数据");
        $("#startup_host_dia").click();
    }else{
        var temphtml = "";
        $("input[name=startup_host_choosecheck]:checkbox").each(function(i){
            if($(this).get(0).checked){
                $(this).attr("name","startup_host_unchoosecheck");
                $(this).attr("checked","checked");
                temphtml +="<tr>"+$(this).parents("tr").html()+"</tr>";
                $(this).parents("tr").remove();
            }
        });
        if(temphtml!=""){
            $("#startup_host_unchoosetable").find("tbody").append(temphtml);
        }
    }
}

//保存选择开机云主机信息
function saveStartupHostInfo(){
    var array = new Array();
    jQuery("input[name=startup_host_choosecheck]:checkbox").each(function(i){
        array[i] = jQuery(this).val();
    });
    var hostId = array.join(",");
    var key = "startup_timer";
    jQuery.ajax({
        type: "POST",
        async:false,
        url: "<%=request.getContextPath() %>/dtsettimeoperation/update",
        data: {"hostId":hostId, "key":key},
        contenttype :"application/x-www-form-urlencoded;charset=utf-8",
        success: function(result){
            if(result.status=="success"){
                window.location.reload();
            }else{
                $("#startup_host_tipscontent").html(result.message);
                $("#startup_host_dia").click();
            }
        }
    });
}
</script>
        <!-- Page content -->
        <div id="content" class="col-md-12">
          


          <!-- page header -->
          <div class="pageheader">


            <h2><i class="fa fa-list-ol"></i> 主机定时操作管理</h2>


          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              
              <!-- col 6 -->
          <div class="col-md-12">

				  <section class="tile color transparent-black" style="height:470px;">

                      <!-- tile widget -->
                      <div class="tile-widget color transparent-black rounded-top-corners nopadding nobg">
                          <!-- Nav tabs -->
                          <ul class="nav nav-tabs tabdrop">
                              <li class=""><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/startup/manage');">定时开机管理</a></li>
                              <li class="active"><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/startup/host/manage');">定时开机主机管理</a></li>
                              <li class=""><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/shutdown/manage');">定时关机管理</a></li>
                              <li class=""><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/shutdown/host/manage');">定时关机主机管理</a></li>
                              <li class=""><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/detail/manage');">定时操作记录</a></li>
                              <div id="space"></div>

                          </ul>
                          <!-- / Nav tabs -->
                      </div>
                      <!-- /tile widget -->

                      <!-- tile header -->
                      <div class="tile-header">
                          <span class="label label-default">未定时开机主机</span>
                          <span class="label label-success" style="margin-left:52%;">已定时开机主机</span>
                      </div>
                      <!-- /tile header -->

                      <div id="startup_host_divscroll" class="tile-body no-vpadding" style="height:420px;">
                          <div id="startup_host_unchoosediv" style="width:40%;height:80%;float: left;">
                              <table  class="table table-datatable table-custom" id="startup_host_unchoosetable" style="hegith:80%;font-size: 9px;width:99%;">
                                  <thead>
                                  <tr>
                                      <th class="no-sort">
                                          <div class="checkbox check-transparent">
                                              <input type="checkbox"  id="startup_host_unallchck">
                                              <label for="startup_host_unallchck"></label>
                                          </div>
                                      </th>
                                      <th class="no-sort">主机名</th>
                                      <th class="no-sort">所属用户</th>
                                  </tr>
                                  </thead>
                                  <tbody>

                                  <c:forEach items="${startup_not_in_timer}" var="cloudHost">
                                      <tr class="odd gradeX">
                                          <td>
                                              <div class="checkbox check-transparent">
                                                  <input type="checkbox" name="startup_host_unchoosecheck" value="${cloudHost.id }" id="${cloudHost.id }">
                                                  <label for="${cloudHost.id }"></label>
                                              </div>
                                          </td>
                                          <td class="cut">${cloudHost.displayName }</td>
                                          <td class="cut">${cloudHost.userAccount }</td>
                                      </tr>
                                  </c:forEach>
                                  </tbody>
                              </table>

                          </div>
                          <div style="width:10%;text-align: center;float: left;margin-left:40px;margin-top:100px;">
                              <button type="button" class="btn btn-cyan add" onclick="chooseStartupHost()">选择&gt;&gt;</button><br><br>
                              <button type="button" class="btn btn-cyan add" onclick="unchooseStartupHost()">&lt;&lt;取消</button>
                          </div>
                          <div id="startup_host_choosediv" class="table-responsive" style="width:40%;height:80%;;float: right;">
                              <table  class="table table-datatable table-custom" id="startup_host_choosetable" style="width:99%;">
                                  <thead>
                                  <tr>
                                      <th class="no-sort">
                                          <div class="checkbox check-transparent">
                                              <input type="checkbox"  id="startup_host_allchck">
                                              <label for="startup_host_allchck"></label>
                                          </div>
                                      </th>
                                      <th class="no-sort">主机名</th>
                                      <th class="no-sort">所属用户</th>
                                  </tr>
                                  </thead>
                                  <tbody>

                                  <c:forEach items="${startup_in_timer}" var="cloudHost">
                                      <tr class="odd gradeX">
                                          <td>
                                              <div class="checkbox check-transparent">
                                                  <input type="checkbox" name="startup_host_choosecheck" value="${cloudHost.id }" id="startup_${cloudHost.id }">
                                                  <label for="startup_${cloudHost.id }"></label>
                                              </div>
                                          </td>
                                          <td class="cut">${cloudHost.displayName }</td>
                                          <td class="cut">${cloudHost.userAccount }</td>
                                      </tr>
                                  </c:forEach>
                                  </tbody>
                              </table>

                          </div>
                          <div style="width:100%;text-align: center;float: left;">
                              <button type="button" class="btn btn-primary add" onclick="saveStartupHostInfo()">保存</button>
                              <!-- 				<button type="button" class="btn btn-default add" onclick="backhome()">返回</button> -->
                          </div>

                      </div>
                      <!-- /tile body -->
                      <%--<div class="tile-body">--%>
                      <a href="#modalDialog" id="startup_host_dia" role="button"  data-toggle="modal"> </a>

                      <div class="modal fade" id="startup_host_modalDialog" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
                          <div class="modal-dialog">
                              <div class="modal-content" style="width:60%;margin-left:20%;">
                                  <div class="modal-header">
                                      <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                                      <h3 class="modal-title" id="startup_host_modalDialogLabel"><strong>提示</strong></h3>
                                  </div>
                                  <div class="modal-body">
                                      <p id="startup_host_tipscontent"></p>
                                  </div>
                              </div><!-- /.modal-content -->
                          </div><!-- /.modal-dialog -->
                      </div><!-- /.modal -->

                      <%--</div>--%>
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
<script type="text/javascript">


    $(function(){
	//check all checkboxes
    $('#startup_host_unchoosetable thead input[type="checkbox"]').change(function () {
        $(this).parents('table').find('tbody input[type="checkbox"]').prop('checked', $(this).prop('checked'));
    });

    $('#startup_host_choosetable thead input[type="checkbox"]').change(function () {
        $(this).parents('table').find('tbody input[type="checkbox"]').prop('checked', $(this).prop('checked'));
    });
    $("#startup_host_unchoosediv").niceScroll({
        cursoropacitymin:0.5,
        cursorcolor:"#424242",
        cursoropacitymax:0.5,
        touchbehavior:false,
        cursorwidth:"8px",
        cursorborder:"0",
        cursorborderradius:"7px" ,
    });

    $("#startup_host_choosediv").niceScroll({
        cursoropacitymin:0.5,
        cursorcolor:"#424242",
        cursoropacitymax:0.5,
        touchbehavior:false,
        cursorwidth:"8px",
        cursorborder:"0",
        cursorborderradius:"7px" ,
    });

});

    $(function(){
        $("#space").width($("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop").width()
                -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).width()
                -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(1).width()
                -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(2).width()
                -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(3).width()
                -119).height(
                $("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).height());
        $(window).resize(function(){
            $("#space").width($("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop").width()
                    -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).width()
                    -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(1).width()
                    -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(2).width()
                    -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(3).width()
                    -119);
        });
    });

function redirect(url) {
    window.location.href = "${pageContext.request.contextPath}"+url;
}

</script>    
  </body>
</html>
      
