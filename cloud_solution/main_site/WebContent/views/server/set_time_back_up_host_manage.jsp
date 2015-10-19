<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>初始化菜单功能权限 </title>
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
   		url: "<%=request.getContextPath() %>/serverbackuptimer/update",
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
            

            <h2><i class="fa fa-list-ol"></i> 主机定时备份管理</h2>
            

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
	                      <li ><a href="#users-tab" data-toggle="tab" onclick="window.location.href='<%=request.getContextPath() %>/csbackresume/detailmanage';">备份记录</a></li>
	                      <li><a href="#orders-tab" onclick="window.location.href='<%=request.getContextPath() %>/csbackresume/dtsettimebackup/manage';" data-toggle="tab">定时备份管理</a></li>
	                      <li class="active"><a href="#messages-tab" onclick="window.location.href='<%=request.getContextPath() %>/csbackresume/desktopbackuptimer/manage';" data-toggle="tab">定时备份主机管理</a></li>
	                      <li><a href="#tasks-tab" data-toggle="tab" onclick="window.location.href='<%=request.getContextPath() %>/csbackresume/manualbackup';">手动备份</a></li>
	                      <div id="space"></div>
	                      
	                     </ul>
	                    <!-- / Nav tabs -->
	                  </div>
	                  <!-- /tile widget -->

 
                  <!-- tile header -->
                  <div class="tile-header"> 

                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  <span class="label label-default">未定时备份主机</span> 
                  <span class="label label-success" style="margin-left:52%;">已定时备份主机</span> 
                  </div>
                  <!-- /tile header -->
                       
                  <div id="divscroll" class="tile-body no-vpadding" style="height:420px;"> 
                    <div id="unchoosediv" style="width:40%;height:80%;float: left;">
                      <table  class="table table-datatable table-custom" id="unchoosetable" style="hegith:80%;font-size: 9px;width:99%;">
                        <thead>
                          <tr>
						    <th class="no-sort">
                            <div class="checkbox check-transparent">
                              <input type="checkbox"  id="unallchck">
                              <label for="unallchck"></label>
                            </div>
                          </th>
                            <th class="no-sort">主机名</th>
                            <th class="no-sort">所属用户</th>
                          </tr>
                        </thead>
                        <tbody>
                        
					<c:forEach items="${notInTimer}" var="cloudHost">
                    		<tr class="odd gradeX">
						    <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" name="unchoosecheck" value="${cloudHost.id }" id="${cloudHost.id }">
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
			<button type="button" class="btn btn-cyan add" onclick="chooseUser()">选择&gt;&gt;</button><br><br>
			<button type="button" class="btn btn-cyan add" onclick="unchooseUser()">&lt;&lt;取消</button>
			</div>
			<div id="choosediv" class="table-responsive" style="width:40%;height:80%;;float: right;">
                      <table  class="table table-datatable table-custom" id="choosetable" style="width:99%;">
                        <thead>
                          <tr>
						    <th class="no-sort">
                            <div class="checkbox check-transparent">
                              <input type="checkbox"  id="allchck">
                              <label for="allchck"></label>
                            </div>
                          </th>
                            <th class="no-sort">主机名</th>
                            <th class="no-sort">所属用户</th>
                          </tr>
                        </thead>
                        <tbody>
                        
					<c:forEach items="${inTimer}" var="cloudHost">
                    		<tr class="odd gradeX">
						    <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" name="choosecheck" value="${cloudHost.id }" id="${cloudHost.id }">
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
			<div style="width:100%;text-align: center;float: left;">
				<button type="button" class="btn btn-primary add" onclick="saveHostInfo()">保存</button>
<!-- 				<button type="button" class="btn btn-default add" onclick="backhome()">返回</button> -->
			</div>                    
                    
                  </div>
                  <!-- /tile body -->
				<div class="tile-body">
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
	$("#space").width($("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop").width()
			 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).width()
			 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(1).width()
			 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(2).width()
			 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(3).width()
			 -1).height(
			  $("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).height());
	$(window).resize(function(){
		 $("#space").width($("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop").width()
				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).width()
				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(1).width()
				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(2).width()
				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(3).width()
				 -1);
	});
});

</script>    
  </body>
</html>
      
