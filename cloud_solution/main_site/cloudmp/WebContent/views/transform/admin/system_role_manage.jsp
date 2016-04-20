<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<!-- system_role_manage.jsp -->
<html>
  <head>
    <title>角色管理 </title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  </head>
 <script type="text/javascript">
var tempbillid = "";
//修改信息页面跳转
function updaterole(obj,billid){
	window.location.href = "<%=request.getContextPath()%>/transform/roleadmin/beforeedit?type="+obj+"&billid="+billid;
}
//关联用户信息页面跳转
function relateuser(billid){
	window.location.href = "<%=request.getContextPath()%>/transform/roleadmin/beforerelateuser?billid="+billid;
}
//关联权限信息页面跳转
function relateright(billid){
	window.location.href = "<%=request.getContextPath()%>/transform/roleadmin/beforerelateright?billid="+billid;
}
//关联角色组信息页面跳转
function relategroup(billid){
	window.location.href = "<%=request.getContextPath()%>/transform/roleadmin/beforerelategroup?billid="+billid;
}
//关联菜单信息页面跳转
function relatemenu(billid){
	window.location.href = "<%=request.getContextPath()%>/transform/roleadmin/beforerelatemenu?billid="+billid;
}
//确认删除数据
function confirmreturn(){
	var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
	var billids = new Array();
	$(datatable).each(function(){
			billids.push($(this).attr("value"));
	});	
	jQuery.ajax({
  	 	type: "POST",
  	 	async:false,
   		url: "<%=request.getContextPath() %>/transform/roleadmin/deleterole",
  		data: "billids="+billids.join(","),
  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
   		success: function(result){
   		  var obj = eval("("+result+")");
     	if(obj.status=="success"){
     		window.location.reload();
     	}else{
     		$("#tipscontent").html(obj.result);
     		$("#dia").click();
     	}
   	}
	});			
}
//删除数据
function deletedata(){
	var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
	if(datatable.length==0){
 		$("#tipscontent").html("请选择角色");
 		$("#dia").click();
		return;
	}
	$("#con").click();
	
}
//批量操作
function multyopp(){
	var op = $("#multyselect").val();
	if(op=="delete"){
		deletedata();
	}
}
//删除单条数据
function deleterole(obj){
	tempbillid = obj;
	$("#con1").click();
}
//确认删除数据
function confirmreturn1(){
	jQuery.ajax({
  	 	type: "POST",
  	 	async:false,
   		url: "<%=request.getContextPath() %>/transform/roleadmin/deleterole",
  		data: "billids="+tempbillid,
  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
   		success: function(result){
   		  var obj = eval("("+result+")");
     	if(obj.status=="success"){
     		window.location.reload();
     	}else{
     		$("#tipscontent").html(obj.result);
     		$("#dia").click();
     	}
   	}
	});			
}
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
            

            <h2><i class="fa fa-user"></i> 角色管理</h2>
            

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
                     <button type="button" class="btn btn-red add" onclick="updaterole('add',null)">
                              <i class="fa fa-plus"></i>
                              <span> 新增角色</span>
                    </button>  
                    <button type="button" class="btn btn-green file-excel-o" onclick="exportData('/export/roledata')">
                              <i class="fa fa-file-excel-o"></i>
                              <span>导出数据</span>
                    </button>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                   
                  </div>
                  <!-- /tile header -->

                        
                    
                       
                  <div class="tile-body no-vpadding"> 
                   
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
                            <th>角色名称</th>
                            <th>角色编码</th>
                            <th>创建时间</th>
                            <th class="no-sort">操作</th>                           
                          </tr>
                        </thead>
                        <tbody>
                        
					<c:forEach items="${systemRoleList}" var="systemRole">
                    		<tr class="odd gradeX">
						    <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" value="${systemRole.billid }" id="${systemRole.billid }">
									  <label for="${systemRole.billid }"></label>
									</div>
                             </td>
                            <td class="cut">${systemRole.name }</td>
                            <td class="cut">${systemRole.code }</td>
                            <td class="cut">${systemRole.insert_date }</td>
                            <td> 
                            <button type="button" class="btn btn-primary btn-xs" onclick="updaterole('modify','${systemRole.billid}')">修改</button>
                              <div class="btn-group">
                                  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                                    操作 <span class="caret"></span>
                                  </button>
                                  <ul class="dropdown-menu" role="menu"> 
                                  	<li><a href="#"  onclick="deleterole('${systemRole.billid}')">删除角色</a></li>
                                    <li><a href="#"  onclick="relateuser('${systemRole.billid}')">关联用户</a></li>
                                    <li><a href="#" onclick="relategroup('${systemRole.billid}')">关联角色组</a></li>
                                    <li><a href="#" onclick="relatemenu('${systemRole.billid}')">设置菜单权限</a></li>
                                    <li><a href="#" onclick="relateright('${systemRole.billid}')">设置功能权限</a></li>
                                    
                                  </ul>
                              </div>
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
                            <!-- <option value="usbstatus">修改用户USB权限</option> -->
                          </select>
                          <span class="input-group-btn">
                            <button class="btn btn-default" type="button" onclick="multyopp()">提交</button>
                          </span>
                        </div>
                      </div> 
					<div class="tile-body">

                    <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                    <a href="#modalConfirm" id="con" role="button"   data-toggle="modal"> </a>
                    <a href="#modalConfirm1" id="con1" role="button"   data-toggle="modal"> </a>
                    
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
                            <button class="btn btn-green"   onclick="confirmreturn()" data-dismiss="modal" aria-hidden="true">确定</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->    
                    
 					<div class="modal fade" id="modalConfirm1" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>确认</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form role="form">   

                              <div class="form-group">
                                <label style="align:center;" id="confirmcontent1">确定要删除所选数据？</label>
                               </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green"   onclick="confirmreturn1()" data-dismiss="modal" aria-hidden="true">确定</button>
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
    
  </body>
</html>
      
