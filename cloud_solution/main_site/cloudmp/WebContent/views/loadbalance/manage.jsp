<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
  <head>
    <title>负载均衡管理</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8" />
  </head>
  <script type="text/javascript">
  var temptabstr = "详情";
  //更改标签样式
  function changeCss(obj){
  	$("#tabgroup").find("a").each(function(){
  		if($(this).html()==obj){
  			$(this).attr("class","btn btn-success");
  		}else{
  			$(this).attr("class","btn btn-default");
  		}
  	});
  }
  //标签切换
  function changeTab(obj){
  	var tabstr = $(obj).html();
  	if(temptabstr==tabstr){
  		return;
  	}else{
  		if(tabstr=="详情"){
  			detaillable();
  		}
  		if(tabstr=="监听"){
  			listenlable();
  		}
  		if(tabstr=="实例"){
  			instancelable();
  		}
  		if(tabstr=="健康检查"){
  			healthylable();
  		}
  		if(tabstr=="标签"){
  			lablelable();
  		}
  		temptabstr = tabstr;
  		changeCss(tabstr);
  	}
  }
  //详情标签切换
  function detaillable(){
	  var detaillablehtml = "<div style=\"width:49%;float: left;\">";
	  	  detaillablehtml += "<p>ID:ELB_123456</p><p>VIP：10.10.0.100</p>";
	  	  detaillablehtml += "<p>负载均衡端口：HTTP：80</p><p>是否保持会话：是</p><p>健康检查：HTTP：80</p>";
	  	  detaillablehtml += "</div><div style=\"width:49%;float: left;\">";
	  	  detaillablehtml += "<p>名称：test_ELB</p><p>绑定的EIP：112.1.1.2</p><p>实例端口：HTTP:80</p><p>关联的ECS:2ECS</p></div>";
  	  $("#labelcontent").html(detaillablehtml);
  }
  //监听标签切换
  function listenlable(){
	  var listenlablehtml = "<p>下面是目前已配置给这个负载均衡器的监听端口:</p><table  class=\"table table-datatable table-custom\" style=\"width:98%;\">";
	  	  listenlablehtml += "<thead><tr><th class=\"no-sort\">负载均衡器协议</th><th class=\"no-sort\">负载均衡器端口</th>";
	  	  listenlablehtml += "<th class=\"no-sort\">实例协议</th><th class=\"no-sort\">实例端口</th></tr></thead>";
	  	  listenlablehtml += "<tbody>";
	  	  //塞数据
	  	  listenlablehtml += "<tr class=\"odd gradeX\"><td class=\"cut\">HTTP</td>";
	  	  listenlablehtml += "<td class=\"cut\">HTTP</td><td class=\"cut\">80</td></tr>";
	  	  
	  	  listenlablehtml += "</tbody></table>";
	  	$("#labelcontent").html(listenlablehtml);
  }
  //实例标签切换
  function instancelable(){
	  var instancelablehtml = "<p>下面是目前已关联给这个负载均衡器的ECS:</p><table  class=\"table table-datatable table-custom\" style=\"width:98%;\">";
	      instancelablehtml += "<thead><tr> <th class=\"no-sort\">ID</th><th class=\"no-sort\">IP</th><th class=\"no-sort\">状态</th></tr></thead>";
	      instancelablehtml += "<tbody>";
	      //塞数据
	      instancelablehtml += "<tr class=\"odd gradeX\"><td class=\"cut\">ECS_123456</td><td class=\"cut\">192.168.1.11</td><td class=\"cut\">运行中</td></tr>";
	      instancelablehtml += "<tr class=\"odd gradeX\"><td class=\"cut\">ECS_798</td><td class=\"cut\">192.168.1.11</td><td class=\"cut\">运行中</td></tr>";
	      
	      instancelablehtml += "</tbody></table>";
	      $("#labelcontent").html(instancelablehtml);
  }
  //健康检查标签切换
  function healthylable(){
	  var healthylablehtml = "<div style=\"width:30%;float: left;\">";
	  
	      healthylablehtml += "<p>检查协议: HTTP</p><p>响应超时时间: 5S</p><p>健康检查间隔: 30S</p>";
	      healthylablehtml += "<p>不健康阈值: 2</p><p>健康阈值: 10</p>";
	      
	      healthylablehtml += "</div>";
	      $("#labelcontent").html(healthylablehtml);
  }
  //标签切换
  function lablelable(){
	  var lablehtml = "<table  class=\"table table-datatable table-custom\" style=\"width:98%;\">";
	  	  lablehtml += "<thead><tr><th class=\"no-sort\">关键字</th><th class=\"no-sort\">值</th></tr></thead>";
	  	  
	  	  lablehtml += "<tr class=\"odd gradeX\"><td class=\"cut\">TAG_KEY1</td><td class=\"cut\">TAG_VALUE1</td></tr>";
	  	  lablehtml += "<tr class=\"odd gradeX\"><td class=\"cut\">TAG_KEY1</td><td class=\"cut\">TAG_VALUE1</td></tr>";
	  	  
	  	  lablehtml += "</tbody></table>";
	  	$("#labelcontent").html(lablehtml);
  }
  function createelb(){
	  window.location.href = "<%=request.getContextPath()%>/loadbalance/createelb";
  }
  function gotopage(obj){
	  window.location.href = "<%=request.getContextPath()%>/loadbalance/operator/"+obj;
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
            

            <h2><i class="fa fa-trophy"></i> 负载均衡</h2>
            

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
                     <button type="button" class="btn btn-red add" onclick="createelb()">
                              <i class="fa fa-plus"></i>
                              <span>创建ELB</span>
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
                            <th class="no-sort">ID</th>
                            <th class="no-sort">名称</th>
                            <th class="no-sort">VIP</th>
                            <th class="no-sort">绑定的EIP</th>
                            <th class="no-sort">负载均衡端口</th>
                            <th class="no-sort">实例端口</th>
                            <th class="no-sort">是否保持会话</th>
                            <th class="no-sort">关联的ECS</th>
                            <th class="no-sort">健康检查</th>
                            <th class="no-sort">操作</th>                          
                          </tr>
                        </thead>
                        <tbody>
                        <tr class="odd gradeX">
                        	<td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" value="testid" id="testid">
									  <label for="testid"></label>
									</div>
                             </td>
                             <td class="cut">ELB_123456</td>
                             <td class="cut">test_ELB</td>
                             <td class="cut">10.10.0.100</td>
                             <td class="cut">112.1.1.2</td>
                             <td class="cut">HTTP:80</td>
                             <td class="cut">HTTP:80</td>
                             <td class="cut">是</td>
                             <td class="cut">2 ECS</td>
                             <td class="cut">健康</td>
                             <td>
                              <div class="btn-group">
                                  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                                    操作 <span class="caret"></span>
                                  </button>
                                  <ul class="dropdown-menu" role="menu"> 
                                    <li><a href="#" onclick="gotopage('port_edit')">编辑监听端口</a></li>
                                    <li><a href="#" onclick="gotopage('bind_eip')">绑定EIP</a></li>
                                    <li><a href="#" onclick="gotopage('unbind_eip')">解绑定EIP</a></li>
                                    <li><a href="#" onclick="gotopage('ecs_edit')">编辑关联的ECS</a></li>
                                    <li><a href="#" onclick="gotopage('healthy_edit')">编辑健康检查</a></li>
                                    <li><a href="#">删除ELB</a></li>
                                  </ul>
                              </div>
                             </td>
                        </tr>
                        </tbody>
                      </table>
                      
                    </div>
            <div class="row">
            	<hr style="margin-top:15px;margin-bottom:0px;width:98%;">
            </div>        
            <div class="row">
            	<div class="btn-group" id="tabgroup" style="margin-left:10px;margin-top:10px;">
                   <a class="btn btn-success" role="button" onclick="changeTab(this)" style="width:120px;">详情</a>
                   <a class="btn btn-default" role="button" onclick="changeTab(this)" style="width:120px;">监听</a>
                   <a class="btn btn-default" role="button" onclick="changeTab(this)" style="width:120px;">实例</a>
                   <a class="btn btn-default" role="button" onclick="changeTab(this)" style="width:120px;">健康检查</a>
<!--                    <a class="btn btn-default" role="button" onclick="changeTab(this)" style="width:100px;">标签</a> -->
           		</div>
            </div> 
            <!-- 标签内容 -->
            <div class="row" style="padding-left:10px;padding-top:5px;height:200px;width:60%;" id="labelcontent">
	            <div style="width:49%;float: left;">
	              	<p>ID:ELB_123456</p>
	              	<p>VIP：10.10.0.100</p>
	              	<p>负载均衡端口：HTTP：80</p>
	              	<p>是否保持会话：是</p>
	              	<p>健康检查：HTTP：80</p>
	            </div>  
	            <div style="width:49%;float: left;">
	              	<p>名称：test_ELB</p>
	              	<p>绑定的EIP：112.1.1.2</p>
	              	<p>实例端口：HTTP:80</p>
	              	<p>关联的ECS:2ECS</p>
	            </div>  	
            </div>                       
         </div>
                  
                  <!-- /tile body -->
<!-- 		<div class="col-sm-2" style="margin-top: -80px;">
	                     <div class="input-group table-options">
	                       <select class="chosen-select form-control" id="multyselect">
	                         <option value="opp">批量操作</option> 
	                         <option value="delete">删除</option>
	                         <option value="usbstatus">修改用户USB权限</option>
	                       </select>
	                       <span class="input-group-btn">
	                         <button class="btn btn-default" type="button" onclick="multyopp()">提交</button>
	                       </span>
	                     </div>
	    </div>  -->
            
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
		$("#labelcontent").niceScroll({
			cursoropacitymin:0.5,
			cursorcolor:"#424242",  
			cursoropacitymax:0.5,  
			touchbehavior:false,  
			cursorwidth:"8px",  
			cursorborder:"0",  
			cursorborderradius:"7px" ,
		});
    })
    </script>
    
  </body>
</html>
      
