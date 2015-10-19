<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>关联用户 </title>
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
var temproleid = "${roleid}";
//修改信息页面跳转
function backhome(){
	window.location.href = "<%=request.getContextPath()%>/transform/roleadmin/index";
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
function saveUserInfo(){
	var array = new Array();
	jQuery("input[name=choosecheck]:checkbox").each(function(i){
			array[i] = jQuery(this).val();
	});
	var urldata = "userids="+array.join(",")+"&roleid="+temproleid;
	jQuery.ajax({
  	 	type: "POST",
  	 	async:false,
   		url: "<%=request.getContextPath() %>/transform/roleadmin/saveuserrole",
  		data: urldata,
  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
   		success: function(result){
   		  var obj = eval("("+result+")");
     	if(obj.status=="success"){
     		backhome();
     	}else{
     		$("#tipscontent").html(obj.result);
     		$("#dia").click();
     	}
   	}
	});	
}
</script>
    <!-- Make page fluid -->
    <div class="row">
        <!-- Page content -->
        <div id="content" class="col-md-12">
          


          <!-- page header -->
          <div class="pageheader">
            

            <h2><i class="fa fa-meh-o"></i> 关联用户</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              
              <!-- col 6 -->
          <div class="col-md-12">

				  <section class="tile color transparent-black" style="height:470px;">


 
                  <!-- tile header -->
                  <div class="tile-header"> 

                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  <button type="button" class="btn btn-success delete" onclick="window.location.href = '<%=request.getContextPath()%>/transform/groupadmin/index';">
                              <i class="fa fa-step-backward"></i>
                              <span> 返回上级</span>
                    </button>
                  <span class="label label-default">未关联用户</span> 
                  <span class="label label-success" style="margin-left:52%;">已关联用户</span>
                  </div>
                  <!-- /tile header -->
                       
                  <div id="divscroll" class="tile-body no-vpadding" style="height:440px;"> 
                   
                    <div id="unchoosediv" class="table-responsive" style="width:40%;height:80%;float: left;">
                      <table  class="table table-datatable table-custom" id="unchoosetable" style="hegith:80%;font-size: 9px;width:99%;">
                        <thead>
                          <tr>
						    <th>
                            <div class="checkbox check-transparent">
                              <input type="checkbox"  id="unallchck">
                              <label for="unallchck"></label>
                            </div>
                          </th>
                            <th>用户账号</th>
                            <th>显示名称</th>
                            <th>用户邮箱</th>
                          </tr>
                        </thead>
                        <tbody>
                        
					<c:forEach items="${notexistsUsers}" var="systemUser">
                    		<tr class="odd gradeX">
						    <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" name="unchoosecheck" value="${systemUser.billid }" id="${systemUser.billid }">
									  <label for="${systemUser.billid }"></label>
									</div>
                             </td>
                            <td class="cut">${systemUser.usercount }</td>
                            <td class="cut">${systemUser.displayname }</td>
                            <td class="cut">${systemUser.email }</td>
                            
                        </tr> 
                    	</c:forEach>                         
                        </tbody>
                      </table>

                    </div>
			<div style="width:10%;text-align: center;float: left;margin-left:80px;margin-top:100px;">
			<button type="button" class="btn btn-cyan add" onclick="chooseUser()">选择&gt;&gt;</button><br><br>
			<button type="button" class="btn btn-cyan add" onclick="unchooseUser()">&lt;&lt;取消</button>
			</div>
			<div id="choosediv" class="table-responsive" style="width:40%;height:80%;;float: right;">
                      <table  class="table table-datatable table-custom" id="choosetable" style="width:99%;">
                        <thead>
                          <tr>
						    <th>
                            <div class="checkbox check-transparent">
                              <input type="checkbox"  id="allchck">
                              <label for="allchck"></label>
                            </div>
                          </th>
                            <th>用户账号</th>
                            <th>显示名称</th>
                            <th>用户邮箱</th>
                          </tr>
                        </thead>
                        <tbody>
                        
					<c:forEach items="${existsUsers}" var="systemUser">
                    		<tr class="odd gradeX">
						    <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" name="choosecheck" value="${systemUser.billid }" id="${systemUser.billid }">
									  <label for="${systemUser.billid }"></label>
									</div>
                             </td>
                            <td class="cut">${systemUser.usercount }</td>
                            <td class="cut">${systemUser.displayname }</td>
                            <td class="cut">${systemUser.email }</td>
                        </tr> 
                        </tr> 
                    	</c:forEach>                         
                        </tbody>
                      </table>
                      
                    </div>
			<div style="width:100%;text-align: center;float: left;">
				<button type="button" class="btn btn-primary add" onclick="saveUserInfo()">保存</button>
				<button type="button" class="btn btn-default add" onclick="backhome()">返回</button>
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
});

</script>    
  </body>
</html>
      
