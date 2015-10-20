<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>控制台-共享存储管理</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8" />
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
            

            <h2><i class="fa fa-jsfiddle"></i> 共享存储管理</h2>
            

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
                     <button type="button" class="btn btn-red add" onclick="edit('add',null)">
                              <i class="fa fa-plus"></i>
                              <span>新增共享存储</span>
                    </button>  
<!--                     <button type="button" class="btn btn-green file-excel-o" onclick="exportData('/export/rolegroupdata')">
                              <i class="fa fa-file-excel-o"></i>
                              <span>导出数据</span>
                    </button> -->
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
                            <th>路径名称</th>
                            <th>路径URL</th>
                            <th>创建时间</th>
                            <th>是否可用</th>
                            <th class="no-sort">操作</th>                           
                          </tr>
                        </thead>
                        <tbody>
                        
					<c:forEach items="${datas}" var="data">
                    		<tr class="odd gradeX">
						    <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" value="${data.id }" id="${data.id }">
									  <label for="${data.id }"></label>
									</div>
                             </td>
                            <td class="cut">${data.name }</td>
                            <td class="cut">${data.url }</td>
                            <td class="cut">${data.insert_date }</td>
                            <td>
								<c:if test="${data.available==null }">
								否
								</c:if>
								<c:if test="${data.available!=null }">
								是
								</c:if>
							</td>
                            <td> 
                            <button type="button" class="btn btn-primary btn-xs" onclick="edit('edit','${data.id}')">修改</button>
                            <c:if test="${data.available==null }">
                            <button type="button" class="btn btn-primary btn-xs" onclick="setAvailable('${data.id}')">设为可用</button>
                            </c:if>
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
    //批量操作
	function multyopp(){
		var op = $("#multyselect").val();
		if(op=="delete"){
			deletedata();
		}
	}
  	//修改信息页面跳转
	function edit(type,id){
		if(type=="add"){
			id = "32f7876413a74afb840814a7976c1xxx";
		}
		window.location.href = "<%=request.getContextPath()%>/sharedmemory/"+type+"/"+id;
	}
	//删除数据
	function confirmreturn(){
		var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
		var billids = new Array();
		$(datatable).each(function(){
				billids.push($(this).attr("value"));
		});
		var data = new Object();
			data.ids = billids.join(",");
			jQuery.ajax({
		  	 	type: "POST",
		  	 	async:false,
		   		url: "<%=request.getContextPath() %>/sharedmemory/delete",
		  		data: {data:JSON.stringify(data)},
		  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
		   		success: function(result){
		     	if(result.status=="success"){
		     		window.location.reload();
		     	}else{
		     		$("#tipscontent").html(result.message);
		     		$("#dia").click();
		     	}
		   	}
			});		
	}
	//删除数据
	function deletedata(){
		var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
		if(datatable.length==0){
	 		$("#tipscontent").html("请选择数据");
	 		$("#dia").click();
			return;
		}
		$("#con").click();
		
	}
	function setAvailable(id){
		jQuery.get(path + "/sharedmemory/"+id+"/available",function(data){
			if(data.status=="success"){
				window.location.reload();
			}else{
				jQuery("#tipscontent").html(data.message);
   		        jQuery("#dia").click();
				return;
			}
		});
	}
    </script>
    
  </body>
</html>
      
