<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>快照管理</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  </head>
 <script type="text/javascript">

</script>
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
            

            <h2><i class="fa fa-user"></i> 快照管理</h2>
            

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
                     <button type="button" class="btn btn-red add" onclick="createSnapshot()">
                              <i class="fa fa-plus"></i>
                              <span>新快照</span>
                    </button>  
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <div class="tile-body no-vpadding"> 
                    <div class="row">
                     	<div class="col-sm-2">
	                         <div class="input-group table-options">
	                              <select id="idxId" class="chosen-select form-control" onchange="reloadWindow();">
	                                   <option value=""  <c:if test="${idx==''}"> selected="selected"</c:if>>全主机磁盘</option>
	                                   <c:forEach items="${diskList}" varStatus="i" var="cp">
	                                   	<option value="${i.count}" <c:if test="${idx==i.count}"> selected="selected"</c:if>>指定磁盘${i.count}</option>
	                                   </c:forEach>
	                              </select>	                          
	                          <span class="input-group-btn">
	                            <button class="btn btn-default" type="button" onclick="reloadWindow();">刷新</button>
	                          </span>
	                        </div>
                        </div>
                        
	                    <div class="col-sm-4">
	                        <c:if test="${runing !=null}" >
	                        	<div class="pull-right label label-blue margin-top-10">							
							       <span class="animate-number"
									data-animation-duration="1500">
										<c:if test="${runing.command=='create'}"> 该主机正在创建快照...${runing.level}%</c:if>
										<c:if test="${runing.command=='resume'}"> 该主机正在恢复快照...${runing.level}%</c:if>
									 </span>
								</div>  
							</c:if>
	                    </div>
	                </div>    
                    <div class="table-responsive">
                        <table  class="table table-datatable table-custom" id="basicDataTable">
                        <thead>
                        	<tr>
							    <th class="no-sort">
	                            <div class="checkbox check-transparent">
	                              <input type="checkbox"  id="allchck">
	                              <label for="allchck"></label>
	                            </div>
	                          	</th>
	                            <th>快照ID</th>
	                            <th>时间戳</th>
	                            <th class="no-sort">操作</th>                           
                            </tr>
                        </thead>
                        <tbody>
                        
					 <c:forEach items="${snapshotList}" var="sps">
                    		<tr class="odd gradeX">
						    <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" value="${sps.id}" id="${sps.id }">
									  <label for="${sps.id}"></label>
									</div>
                             </td>
                            <td class="cut">${sps.id}</td>
                            <td class="cut">${sps.timestamp }</td>
                            <td> 
	                            <button type="button" class="btn btn-primary btn-xs" onclick="deleteSnapshot('${sps.id}');">删除</button>
	                            <button type="button" class="btn btn-primary btn-xs" onclick="resumeSnapshot('${sps.id}');">恢复</button>
                            </td>                           
                        </tr> 
                    	</c:forEach>                         
                        </tbody>
                      </table>
                      
                    </div>
                    
                  </div>
                  <!-- /tile body -->
                    <div class="col-sm-2" style="margin-top: -40px;">
                        <div class="input-group table-options">
                          <select class="chosen-select form-control" id="multyselect">
                            <option value="opp">批量操作</option> 
                            <option value="delete">删除</option>
                          </select>
                          <span class="input-group-btn">
                            <button class="btn btn-default" type="button" onclick="multyopp()">提交</button>
                          </span>
                        </div>
                      </div> 
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

                    <div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>确认</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form role="form">   

                              <div class="form-group">
                                <label style="align:center;" id="confirmcontent">确定要删除所选数据？</label>
                               </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green" id="confirm_btn" data-dismiss="modal" aria-hidden="true">确定</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
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


 



    <section class="videocontent" id="video"></section>


    <script>
    $(function(){
       //check all checkboxes
       $('table thead input[type="checkbox"]').change(function () {
         $(this).parents('table').find('tbody input[type="checkbox"]').prop('checked', $(this).prop('checked'));
       });
      // Add custom class to pagination div
      $.fn.dataTableExt.oStdClasses.sPaging = 'dataTables_paginate pagination-sm paging_bootstrap paging_custom';
	  var oper = '';
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
        "aaSorting": [],
        "aoColumnDefs": [
                         { 'bSortable': false, 'aTargets': [ "no-sort" ] }
                       ], 
        "fnInitComplete": function(oSettings, json) { 
            $('.dataTables_filter').css("text-align","right");
            $('.dataTables_filter').css("margin-top","-40px");
            $('.dataTables_filter').css("margin-bottom","0px");   
            $('.dataTables_filter input').attr("placeholder", "Search");
        }
      });
 

      /* Get the rows which are currently selected */
      function fnGetSelected(oTable01Local){
        return oTable01Local.$('tr.row_selected');
      };
	  $("div[class='col-md-4 text-right sm-center']").prepend(oper);
	    $("div[class='col-md-4 text-right sm-center']").attr("class","tile-footer text-right");
	  $(".chosen-select").chosen({disable_search_threshold: 10});
    })
   
    </script>
    <script type="text/javascript">
        var snapshotId = '';
        var target = "${realId}";    
        var mode = ${mode};
        var index = "";
    	var snapshotIds;
    	var isRuning = false;
        //批量操作
        function multyopp(){
        	var op = $("#multyselect").val();
        	if(op=="delete"){
        		deleteMutil();
        	}
        }       
        //批量删除数据
        function deleteMutil(){
        	checkTasking();
        	if (isRuning){
	    		$("#tipscontent").html("该主机目前有快照任务执行中，暂不能做批量删除快照操作!");
	 		    $("#dia").click();
	 		    return false;
        	}
        	
        	var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
        	if(datatable.length==0){
         		$("#tipscontent").html("请选择快照！");
         		$("#dia").click();
        		return;
        	}
	    	snapshotIds = new Array();
        	$(datatable).each(function(){
        		snapshotIds.push($(this).attr("value"));
        	});	
        	if (mode=="0"){
	    		$("#confirmcontent").html("确定要删除选中的全磁盘快照？");
        	} else{
    	    	$("#confirmcontent").html("确定要删除选中的磁盘快照？");       		
        	}
	    	$("#confirm_btn").attr("onclick","delMutiSnap();");
	    	$("#con").click();      	
        }       

        function delMutiSnap(){
   	     	jQuery.ajax({
   	        url: '<%=request.getContextPath()%>/snapshot/delMutil',
   	        type: 'post', 
   	        dataType: 'json',
   	        data:{'target': target, 'snapshotId': snapshotIds, 'mode': mode},
   	        timeout: 10000,
   	        error: function(){ 
   	        },
   	        success: function(result){  
   	        	if(result.status=="success"){
   	        		$("#tipscontent").html("删除成功!");
   	     		    $("#dia").click();
   	     		   window.location.reload();
   	        	}
   	        	else{
   		        	$("#tipscontent").html(result.message);
   					$("#dia").click();
   				}
	   	    }
   	        });     	
        }
        
        function reloadWindow(){
        	window.location.href= "<%=request.getContextPath()%>/snapshot/"+target+"/query?idx="+$("#idxId").val();
        }
        
        function createSnapshot(id){
        	snapshotId = id;
        	index = $("#idxId").val();
        	checkTasking();
        	if (isRuning){
	    		$("#tipscontent").html("该主机目前有快照任务执行中，暂不能做创建快照操作!");
	 		    $("#dia").click();
	 		    return false;
        	}
        	if (mode=="0"){
	    		$("#confirmcontent").html("确定创建全磁盘快照？");
        	} else{
    	    	$("#confirmcontent").html("确定创建该磁盘快照？");       		
        	}
	    	$("#confirm_btn").attr("onclick","createSnap();");	    	
	    	$("#con").click();  	
        }

        function createSnap(){   
        	jQuery.ajax({
    	        url: '<%=request.getContextPath()%>/snapshot/create',
    	        type: 'post', 
    	        dataType: 'json',
    	        data:{'target': target, 'index': index, 'mode': mode},
    	        timeout: 10000,
    	        error: function(){ 
    	        },
    	        success: function(result)	        
    	        {  
    	        	if(result.status=="success"){
    	        		$("#tipscontent").html("创建成功!");
    	     		    $("#dia").click();
    	     		   window.location.reload();
    	        	}
    	        	else{
    		        	$("#tipscontent").html(result.message);
    					$("#dia").click();
    	        	}
    	        }
    	     });     	
        }       
        
        function deleteSnapshot(id){
        	snapshotId = id;
        	checkTasking();
        	if (isRuning){
	    		$("#tipscontent").html("该主机目前有快照任务执行中，暂不能做删除操作!");
	 		    $("#dia").click();
	 		    return false;
        	}
	    	$("#confirmcontent").html("确定要删除该磁盘快照？");
	    	$("#confirm_btn").attr("onclick","deleteSnap();");
	    	$("#con").click();  	
        }

        function deleteSnap(){   
        	jQuery.ajax({
    	        url: '<%=request.getContextPath()%>/snapshot/del',
    	        type: 'post', 
    	        dataType: 'json',
    	        data:{'target': target, 'snapshotId': snapshotId, 'mode': mode},
    	        timeout: 10000,
    	        error: function(){ 
    	        },
    	        success: function(result)	        
    	        {  
    	        	if(result.status=="success"){
    	        		$("#tipscontent").html("删除成功!");
    	     		    $("#dia").click();
    	     		   window.location.reload();
    	        	}
    	        	else{
    		        	$("#tipscontent").html(result.message);
    					$("#dia").click();
    	        	}
    	        }
    	        
    	     });     	
        }

        
        function resumeSnapshot(id){
        	snapshotId = id;
        	checkTasking();
        	if (isRuning){
	    		$("#tipscontent").html("该主机目前有快照任务执行中，暂不能做恢复快照操作!");
	 		    $("#dia").click();
	 		    return false;
        	}        	
	    	$("#confirmcontent").html("确定要恢复该磁盘快照？");
	    	$("#confirm_btn").attr("onclick","resumeSnap();");
	    	$("#con").click();  	
        }
        
        function resumeSnap(){   
        	jQuery.ajax({
    	        url: '<%=request.getContextPath()%>/snapshot/resume',
    	        type: 'post', 
    	        dataType: 'json',
    	        data:{'target': target, 'snapshotId': snapshotId, 'mode': mode},
    	        timeout: 10000,
    	        error: function(){ 
    	        },
    	        success: function(result){	  
    	        	if(result.status=="success"){
    	        		$("#tipscontent").html("恢复磁盘成功!");
    	     		    $("#dia").click();
    	     		   window.location.reload();
    	        	}
    	        	else{
    		        	$("#tipscontent").html(result.message);
    					$("#dia").click();
    	        	}
    	        }
    	     });     	
        }

        function checkTasking(){
        	jQuery.ajax({
    	        url: '<%=request.getContextPath()%>/snapshot/runing',
    	        type: 'post', 
    	        dataType: 'json',
    	        data:{'target': target},
    	        async : false,
    	        timeout: 10000,
    	        error: function(){ 
    	        },
    	        success: function(result){
    	        	isRuning = result.status=="success";
    	        	return;
    	        }
    	     });         	
        }
        
    </script>
  </body>
</html>
      
