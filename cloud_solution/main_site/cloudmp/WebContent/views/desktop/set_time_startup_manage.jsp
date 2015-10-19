<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
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
//选择用户
function chooseUser(){
	var array = new Array();
	$("input[name=unchoosecheck]:checkbox").each(function(i){
		if($(this).get(0).checked){
			array[i] = $(this).val();
		}
	});
	if(array.length==0){
 		$("#tipscontent").html("请至少选择一条未关联数据");
 		$("#dia").click();
	}else{
		var temphtml = "";
		$("input[name=unchoosecheck]:checkbox").each(function(i){
			if($(this).get(0).checked){
				$(this).attr("name","choosecheck");
				$(this).attr("checked","");
				temphtml +="<tr>"+$(this).parents("tr").html()+"</tr>";
				$(this).parents("tr").remove();
			}
		});
		if(temphtml!=""){
			$("#choosetable").find("tbody").append(temphtml);
		}
	}
}
//取消选择用户
function unchooseUser(){
	var array = new Array();
	$("input[name=choosecheck]:checkbox").each(function(i){
		if($(this).get(0).checked){
			array[i] = $(this).val();
		}
	});
	if(array.length==0){
 		$("#tipscontent").html("请至少选择一条已关联数据");
 		$("#dia").click();
	}else{
		var temphtml = "";
		$("input[name=choosecheck]:checkbox").each(function(i){
			if($(this).get(0).checked){
				$(this).attr("name","unchoosecheck");
				$(this).attr("checked","checked");
				temphtml +="<tr>"+$(this).parents("tr").html()+"</tr>";
				$(this).parents("tr").remove();
			}
		});
		if(temphtml!=""){
			$("#unchoosetable").find("tbody").append(temphtml);
		}
	}
}
//保存角色和用户关联信息
function saveHostInfo(){
	var array = new Array();
	jQuery("input[name=choosecheck]:checkbox").each(function(i){
			array[i] = jQuery(this).val();
	});
	var urldata = "hostId="+array.join(",");
	jQuery.ajax({
  	 	type: "POST",
  	 	async:false,
   		url: "<%=request.getContextPath() %>/desktopbackuptimer/update",
  		data: urldata,
  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
   		success: function(result){
      	if(result.status=="success"){
     		window.location.reload();
     	}else{
     		$("#tipscontent").html(obj.result);
     		$("#dia").click();
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

				  <section class="tile color transparent-black">

                      <!-- tile widget -->
                      <div class="tile-widget color transparent-black rounded-top-corners nopadding nobg">
                          <!-- Nav tabs -->
                          <ul class="nav nav-tabs tabdrop">
                              <li class="active"><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/startup/manage');">定时开机管理</a></li>
                              <li class=""><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/startup/host/manage');">定时开机主机管理</a></li>
                              <li class=""><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/shutdown/manage');">定时关机管理</a></li>
                              <li class=""><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/shutdown/host/manage');">定时关机主机管理</a></li>
                              <li class=""><a href="#" data-toggle="tab" onclick="redirect('/dtsettimeoperation/detail/manage');">定时操作记录</a></li>
                              <div id="space"></div>


                          </ul>
                          <!-- / Nav tabs -->
                      </div>
                      <!-- /tile widget -->
                      <div class="tile-header">
                          <h3>输入定时信息</h3>
                      </div>
                      <!-- tile body -->
                      <div class="tile-body" >

                          <form class="form-horizontal" role="form" parsley-validate id="startup_timer_form" action="<%=request.getContextPath() %>/dtsettimeoperation/updatecheck" method="post"   >
                              <input type="hidden" name="id" value="${timer.id}"/>
                              <input type="hidden" name="key" value="startup_timer"/>
                              <div class="form-group">
                                  <label for="status1" class="col-sm-2 control-label">定时器状态*</label>
                                  <div class="col-sm-10">
                                      <div class="radio radio-transparent col-md-2">
                                          <input type="radio" name="status" id="status1" value="1" checked onclick="$('#timerinfo').show();$('#timerinfo').find('input[type=text]').removeAttr('disabled'); $('#type1').click();">
                                          <label for="status1">启用</label>
                                      </div>
                                      <div class="radio radio-transparent col-md-2">
                                          <input type="radio" name="status" id="status2" value="2" onclick="$('#timerinfo').find('input[type=text]').attr('disabled','disabled');$('#timerinfo').hide();">
                                          <label for="status2">禁用</label>
                                      </div>
                                  </div>
                              </div>
                              <div id="timerinfo">

                                  <div class="form-group">
                                      <label for="type1" class="col-sm-2 control-label">模式 *</label>
                                      <div class="col-sm-10">
                                          <div class="radio radio-transparent col-md-2">
                                              <input type="radio" name="type" id="type1" value="1" checked onclick="$('#everymonth').show();$('#everymonth').find('input').removeAttr('disabled');$('#everyweek').find('input').attr('disabled','disabled');$('#everyweek').hide();">
                                              <label for="type1">月</label>
                                          </div>
                                          <div class="radio radio-transparent col-md-2">
                                              <input type="radio" name="type" id="type2" value="2" onclick="$('#everymonth').find('input').attr('disabled','disabled');$('#everymonth').hide();$('#everyweek').show();$('#everyweek').find('input').removeAttr('disabled');">
                                              <label for="type2">周</label>
                                          </div>
                                          <div class="radio radio-transparent col-md-2">
                                              <input type="radio" name="type" id="type3" value="3" onclick="$('#everymonth').find('input').attr('disabled','disabled');$('#everyweek').find('input').attr('disabled','disabled');$('#everymonth').hide();$('#everyweek').hide();">
                                              <label for="type3">日</label>
                                          </div>
                                      </div>
                                  </div>
                                  <div class="form-group" id="everymonth">

                                      <label for="day" class="col-sm-2 control-label">每月*</label>
                                      <div class="col-sm-4">
                                          <input type="text" class="form-control" value="${timer.day}" id="day" name="day" parsley-type="number" maxlength="2" parsley-trigger="change" value="${hour }" parsley-required="true" parsley-max="31" parsley-min="1">
                                      </div>
                                  </div>
                                  <div class="form-group" id="everyweek">
                                      <label for="week" class="col-sm-2 control-label">每周*</label>
                                      <div class="col-sm-4">
                                          <input type="text" class="form-control" value="${timer.week}"  id="week" name="week" parsley-type="number" maxlength="2" parsley-trigger="change" value="${hour }" parsley-required="true" parsley-max="7" parsley-min="1">
                                      </div>
                                  </div>
                                  <div class="form-group">
                                      <label for="hour" class="col-sm-2 control-label">时*</label>
                                      <div class="col-sm-4">
                                          <input type="text" class="form-control" value="${timer.hour}"  id="hour" name="hour" parsley-type="number" maxlength="2" parsley-trigger="change" value="${hour }" parsley-required="true" parsley-max="23" parsley-min="1">
                                      </div>
                                  </div>
                                  <div class="form-group">
                                      <label for="minute" class="col-sm-2 control-label">分*</label>
                                      <div class="col-sm-4">
                                          <input type="text" class="form-control" id="minute" value="${timer.minute}"  name="minute"  parsley-type="number" maxlength="2" parsley-trigger="change" value="${min }" parsley-required="true" parsley-max="59" parsley-min="0">
                                      </div>
                                  </div>
                              </div>
                              <div class="form-group form-footer footer-white">
                                  <div class="col-sm-offset-4 col-sm-8">
                                      <button type="button" class="btn btn-greensea" onclick="saveStartupForm();"><i class="fa fa-plus"></i>
                                          <span> 保 存 </span></button>
                                      <button type="reset" class="btn btn-red" onclick="window.location.reload();"><i class="fa fa-refresh"></i>
                                          <span> 重 置 </span></button>
                                  </div>
                              </div>

                          </form>

                      </div>

                      </section>
                      <%--<!-- /tile body -->--%>
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

    var path = '<%=request.getContextPath()%>';

    $(function(){
	//check all checkboxes
	$('#unchoosetable thead input[type="checkbox"]').change(function () {
	  $(this).parents('table').find('tbody input[type="checkbox"]').prop('checked', $(this).prop('checked'));
	});	

	//check all checkboxes
	$('#choosetable thead input[type="checkbox"]').change(function () {
	  $(this).parents('table').find('tbody input[type="checkbox"]').prop('checked', $(this).prop('checked'));
	});	
	$("#unchoosediv").niceScroll({
		cursoropacitymin:0.5,
		cursorcolor:"#424242",  
		cursoropacitymax:0.5,  
		touchbehavior:false,  
		cursorwidth:"8px",  
		cursorborder:"0",  
		cursorborderradius:"7px" ,
	});
	$("#choosediv").niceScroll({
		cursoropacitymin:0.5,
		cursorcolor:"#424242",  
		cursoropacitymax:0.5,  
		touchbehavior:false,  
		cursorwidth:"8px",  
		cursorborder:"0",  
		cursorborderradius:"7px" ,
	});

        $("#status"+'${timer.status}').click();

        if('${timer.type}' == ''){
            $("#type1").click();
        }else{
            $("#type"+'${timer.type}').click();
        }

        $("#timerinfo").find("input[type='text']").each(function(){
            if($(this).is(":visible") == false){
                $(this).attr("disabled","disabled");
            }
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

function saveStartupForm(){
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
                            window.location.reload();
                        }
                    },
                    error: function(data){
                        $("#tipscontent").html(data.message);
                        $("#dia").click();//失败时的处理方法
                    },
                    dataType:'json',
                    timeout:10000
                };
                var form = jQuery("#startup_timer_form");
                form.parsley('validate');
                if(form.parsley('isValid')){
                    jQuery("#startup_timer_form").ajaxSubmit(options);
                }
            }
        }
    });

}

    </script>
  </body>
</html>
      
