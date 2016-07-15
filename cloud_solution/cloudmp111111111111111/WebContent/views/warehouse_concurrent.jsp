<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<!-- warehouse_concurrent.jsp -->
<html>
  <head>
    <title>控制台-${productName}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8" />

    <link rel="icon" type="image/ico" href="<%=request.getContextPath()%>/assets/images/favicon.ico" />
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
            

            <h2><i class="fa fa-desktop"></i> 最大并发创建数设置</h2>
            

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
                    <button type="button" class="btn btn-success delete" onclick="window.location.href='<%=request.getContextPath()%>/warehouse/all';">
                              <i class="fa fa-step-backward"></i>
                              <span> 返回上级</span>
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
<!-- 						    <th class="no-sort"> -->
<!--                             <div class="checkbox check-transparent"> -->
<!--                               <input type="checkbox" value="1" id="allchck"> -->
<!--                               <label for="allchck"></label> -->
<!--                             </div> -->
<!--                           </th> -->
                            <th class="no-sort">资源池ID</th>
                            <th class="no-sort">资源池名称</th>
                            <th class="no-sort">最大并发创建数</th>
                            <th class="no-sort" style="width:120px;">操作</th>
                          </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${lists}" var="data">

                            <tr class="odd gradeX">
                                <!-- 						          <td> -->
                                <!-- 									<div class="checkbox check-transparent"> -->
                                <!-- 									  <input type="checkbox" value="${data.uuid }" id="${data.uuid }"> -->
                                <!-- 									  <label for="${data.uuid }"></label> -->
                                <!-- 									  <input type="hidden" name="hiddenval" value="${data.max_creating }"> -->
                                <!-- 									</div> -->
                                <!--                                  </td> -->
                                <td class="cut">
                                        ${data.uuid }
                                </td>
                                <td class="cut">
                                    <input type="hidden" name="hiddenval" value="${data.max_creating }">
                                        ${data.name }
                                </td>
                                <td class="cut">
                                        ${data.max_creating }
                                </td>
                                <td>
                                    <button type="button" class="btn btn-primary btn-xs" onclick="edit(this)">编辑</button>
                                    <!--                                   	<button type="button" class="btn btn-success btn-xs" onclick="saveInfo(this)">保存</button>
                                     -->                            	  </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                      </table>
                      
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



	<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/hostwarehouseform.js"></script>

    <script>
    var path = "<%=request.getContextPath()%>";
    var curId = "";
    var ids = [];
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
	  $("div[class='col-md-4 text-right sm-center']").attr("class","tile-footer text-right");
    });
	  //页面嵌入input框
	  function edit(obj){
		  //如果原来存在数值
		  var val = $.trim($(obj).parents("td").prev().html());
		  $(obj).parents("td").prev().html("<input type=\"text\" class=\"input-sm\" value=\""+val+"\" name=\"max_creating\"  style=\"min-height:18px;line-height:15px;padding:1px;width:100px;\">");
		  $(obj).parents("td").html("<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"synchronizedata(this)\">取消编辑</button><button type=\"button\" style=\"margin-left:4px;\" class=\"btn btn-success btn-xs\" onclick=\"saveInfo(this)\">保存</button>");
	  }
	  //同步数据
	  function synchronizedata(obj){
		  var val = $(obj).parents("tr").find("td:eq(1)").find("input[type=hidden]").val();
		  $(obj).parents("td").prev().html(val);
		  //$(obj).parents("td").prev().html("<input type=\"text\" class=\"form-control\" value=\""+val+"\" name=\"name\" id=\"warehouse_name\" style=\"min-height:25px;padding:2px;width:100px;\">");
		  $(obj).parents("td").html("<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"edit(this)\">编辑</button>"+
				  "");
	  }
	  //保存信息
	  function saveInfo(obj){
				var max_creating = $(obj).parents("td").prev().find("input").val().trim();
				var reg = /^[1-9]{1}[0-9]{0,1}$/;
				if(!reg.test(max_creating)){
		     		$("#tipscontent").html("请输入【1-99】之间的数字");
		     		$("#dia").click();
		     		return;
				}
				var pool_name = $(obj).parents("td").prev().prev().html().trim();
				var pool_id = $(obj).parents("td").prev().prev().prev().html().trim();
				var data = new Object();
				data["pool_id"] = pool_id;
				data["pool_name"] = pool_name;
				data["max_creating"] = max_creating;
				var url = "<%=request.getContextPath()%>/warehouse/savemaxconcurrent";
				$("#tipscontent").html("保存中,请稍后");
	     		$("#dia").click();
	     		$("button[class='close']").get(0).disabled = true;
	     		setTimeout(function(){
					jQuery.ajax({
				  	 	type: "POST",
				  	 	async:false,
				   		url: url,
				   		dataType : "json",
				  		data:{data:JSON.stringify(data)},
				  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
				   		success: function(result){
				     	if(result.status=="success"){
				  		  $(obj).parents("tr").find("td:eq(0)").find("input[type=hidden]").val(max_creating);
						  $(obj).parents("td").prev().html(max_creating);
						  $(obj).parents("td").html("<button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"edit(this)\">编辑</button>"+
								  "");
			     		  $("#tipscontent").html("保存成功");
			     		 $("button[class='close']").get(0).disabled = false;
				     	}else{
				     		$("#tipscontent").html(result.message);
				     		$("button[class='close']").get(0).disabled = false;
				     		return;
				     	}
				   	}
					});	
	     		},1000)
		}
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
    </script>
    
  </body>
</html>
      

