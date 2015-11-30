<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>告警规则管理 </title>
  </head>
<%@include file="/views/common/common_menus.jsp" %>
<script type="text/javascript">
//规则信息编辑页面跳转
function updaterule(obj,id){
	window.location.href = "<%=request.getContextPath()%>/syswarn/ruleedit?type="+obj+"&id="+id;
}
//规则内容编辑页面跳转
function editvalue(id){
	window.location.href = "<%=request.getContextPath()%>/syswarn/valueedit?ruleid="+id;
}
//批量操作
function multyopp(){
	var op = $("#multyselect").val();
	if(op=="delete"){
		beforedeletedatas();
	}
	
}
function beforedeletedatas(){
	var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
	if(datatable.length==0){
 		$("#tipscontent").html("请选择规则");
 		$("#dia").click();
		return;
	}
	$("#con").click();
}
//删除记录
function deleterules(){
	var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
	var billids = new Array();
	$(datatable).each(function(){
			billids.push($(this).attr("value"));
	});	
    jQuery.ajax({
        url: '<%=request.getContextPath()%>/syswarn/deleterule',
        type: 'post',
        dataType: 'json',
        data:{ids:billids},
        async: false,
        timeout: 10000,
        error: function()
        {
        },
        success: function(result)
        {
            if(result.status=="success"){
                location.href = "<%=request.getContextPath()%>/syswarn/rulelist";
            }else{
                $("#tipscontent").html(result.message);
                $("#dia").click();
                return;
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
            <h2><i class="fa fa-warning"></i> 告警规则管理</h2>
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
                     <button type="button" class="btn btn-red add" onclick="updaterule('add',null)">
                              <i class="fa fa-plus"></i>
                              <span> 新增</span>
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
                            <th class="no-sort">规则名称</th>
                            <!-- <th class="no-sort">规则标示符</th> -->
                            <th class="no-sort">规则类型</th>
                            <th class="no-sort" width="300">规则内容</th>
                            <!-- <th class="no-sort">采样次数</th> -->
                            <th class="no-sort">是否发送通知</th>
                            <th class="no-sort">通知类型</th>
                            <th class="no-sort">定时发送时间</th>
                            <!-- <th class="no-sort">通知邮件</th> -->
                            <th class="no-sort">操作</th>
                          </tr>
                        </thead>
                        <tbody>
                        
					<c:forEach items="${sysWarnRuleList}" var="sysWarnRuleVO">
                    		<tr class="odd gradeX">
						    <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" value="${sysWarnRuleVO.id }" id="${sysWarnRuleVO.id }">
									  <label for="${sysWarnRuleVO.id }"></label>
									</div>
                             </td>
                            <td class="cut">${sysWarnRuleVO.name }</td>
                            <%-- <td class="cut">${sysWarnRuleVO.code }</td> --%>
                            <td class="cut">
	                            <c:if test="${sysWarnRuleVO.ruletype==1}">故障</c:if>
	                            <c:if test="${sysWarnRuleVO.ruletype==0}">告警</c:if>
                            </td>
                            <td class="cut" style="word-wrap: break-word;">
                            <%-- ${sysWarnRuleVO.frequency }秒 --%>
                            ${sysWarnRuleVO.content }
                            </td>
                            <%-- <td class="cut">${sysWarnRuleVO.sampletime }</td> --%>
                            <td class="cut">
	                            <c:if test="${sysWarnRuleVO.isnotify==1}">是</c:if>
	                            <c:if test="${sysWarnRuleVO.isnotify==0}">否</c:if>                            
                            </td>
                            <td class="cut">
	                            <c:if test="${sysWarnRuleVO.realtime==1}">定时</c:if>
	                            <c:if test="${sysWarnRuleVO.realtime==2}">实时</c:if>                             
                            </td>
                            <td class="cut">${sysWarnRuleVO.sendtime }</td>
                            <%-- <td class="cut">${sysWarnRuleVO.notify_email }</td>      --%>                       
							<td> 
                              <div class="btn-group" style="width:90px;">
                                  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                                    		操作 <span class="caret">
                                   </span>
                                  </button>
                                  <ul class="dropdown-menu" role="menu"> 
                                    <li><a href="#"  onclick="updaterule('modify','${sysWarnRuleVO.id}')">修改</a></li>
                                    <li><a href="#"  onclick="editvalue('${sysWarnRuleVO.id}')">编辑规则</a></li>
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
                            <button class="btn btn-green"   onclick="deleterules()" data-dismiss="modal" aria-hidden="true">确定</button>
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
      //chosen select input
      $(".chosen-select").chosen({disable_search_threshold: 10});
    })
   
    </script>
    
  </body>
</html>
      
