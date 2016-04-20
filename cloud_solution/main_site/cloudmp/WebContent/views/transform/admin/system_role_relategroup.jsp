<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<!-- system_role_relategroup.jsp -->
<html>
  <head>
    <title>关联角色组 </title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  </head>
<%@include file="/views/common/common_menus.jsp" %>
<style>
  body #content .tile table.table-custom > tbody > tr > th,
  body #content .tile table.table-custom > tbody > tr > td{
  font-size:12px;
  }
</style>  
<script type="text/javascript">
var temproleid = "${roleid}";
//修改信息页面跳转
function backhome(){
	window.location.href = "<%=request.getContextPath()%>/transform/roleadmin/index";
}
//保存角色和角色组关联关系
function saveRoleGroup(){
	var array = new Array();
	$("#unchoosetable").find("input[name=unchoosecheck]:checkbox").each(function(i){
		if($(this).get(0).checked==true){
			array[i] = $(this).val();
		}
	});
	var urldata = "groupids="+array.join(",")+"&roleid="+temproleid;
	jQuery.ajax({
  	 	type: "POST",
  	 	async:false,
   		url: "<%=request.getContextPath() %>/transform/roleadmin/saverolegroup",
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
            

            <h2><i class="fa fa-meh-o"></i> 关联角色组</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              
              <!-- col 6 -->
          <div class="col-md-12">

				  <section class="tile color transparent-black" style="height:500px;">


 
                  <!-- tile header -->
                  <div class="tile-header"> 
                    <button type="button" class="btn btn-success delete" onclick="window.location.href='<%=request.getContextPath()%>/transform/roleadmin/index';">
                              <i class="fa fa-step-backward"></i>
                              <span> 返回上级</span>
                    </button>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                   
                  </div>
                  <!-- /tile header -->
                       
                  <div class="tile-body no-vpadding" style="height:440px;"> 
                   
                    <div id="scrolldiv" class="table-responsive" style="width:100%;height:80%;float: left;overflow: auto;">
                    
                      <table  class="table table-datatable table-custom" id="unchoosetable" style="hegith:80%;font-size: 9px;">
                        <thead>
                          <tr>
						    <th>
                            <div class="checkbox check-transparent">
                              <input type="checkbox"  id="allchck">
                              <label for="allchck"></label>
                            </div>
                          </th>
                            <th>角色组名称</th>
                            <th>创建时间</th>
                          </tr>
                        </thead>
                        <tbody>
                        
					<c:forEach items="${groupList}" var="systemGroup">
                    		<tr class="odd gradeX">
						    <td>
									<div class="checkbox check-transparent">
									<c:if test="${empty systemGroup.code }">
										<input type="checkbox" name="unchoosecheck" value="${systemGroup.billid }" id="${systemGroup.billid }">
									</c:if>
									<c:if test="${!empty systemGroup.code }">
										<input type="checkbox" name="unchoosecheck" value="${systemGroup.billid }" id="${systemGroup.billid }" checked>
									</c:if>									
									  <label for="${systemGroup.billid }"></label>
									</div>
                             </td>
                            <td class="cut">${systemGroup.name }</td>
                            <td class="cut">${systemGroup.insert_date }</td>
                        </tr> 
                    	</c:forEach>                         
                        </tbody>
                      </table>

                    </div>
			<div style="width:100%;text-align: center;float: left;margin-top:48px;">
				<button type="button" class="btn btn-primary add" onclick="saveRoleGroup()">保存</button>
				<button type="button" class="btn btn-default add" onclick="backhome()">返回</button>
			</div>                    
                    
                  </div>
                  <!-- /tile body -->
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
	$("#scrolldiv").niceScroll({
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
      
