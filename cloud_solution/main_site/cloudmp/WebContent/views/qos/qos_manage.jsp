<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
  <head>
    <title>控制台-${productName}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8" />

    <link rel="icon" type="image/ico" href="<%=request.getContextPath()%>/assets/images/favicon.ico" />
    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/vendor/animate/animate.min.css">
    <link type="text/css" rel="stylesheet" media="all" href="<%=request.getContextPath()%>/assets/js/vendor/mmenu/css/jquery.mmenu.all.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/videobackground/css/jquery.videobackground.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap-checkbox.css">

    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/css/rickshaw.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/morris/css/morris.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/tabdrop/css/tabdrop.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/summernote/css/summernote.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/summernote/css/summernote-bs3.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen-bootstrap.css">

    <link href="<%=request.getContextPath()%>/assets/css/zhicloud.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="<%=request.getContextPath()%>/assets/js/html5shiv.js"></script>
      <script src="<%=request.getContextPath()%>/assets/js/respond.min.js"></script>
    <![endif]-->
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
            

            <h2><i class="fa fa-hdd-o"></i> QoS规则管理</h2>
            

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
                          <button type="button" class="btn btn-greensea add" id="qos_add">
                              <i class="fa fa-plus"></i>
                              <span> 新增QoS规则 </span>
                          </button>
	                    <button type="button" class="btn btn-green file-excel-o" onclick="exportData('/export/qosdata')">
	                              <i class="fa fa-file-excel-o"></i>
	                              <span>导出数据</span>
	                    </button>
                      </div>
                      <%--<div class="tile-widget bg-transparent-black-2">--%>
                          <%--<div class="row">--%>
                              <%--<div class="col-sm-6 col-xs-6">--%>
                                  <%--<div class="input-group table-options">--%>
                          <%--<span class="input-group-btn">--%>
                                <%--<input id="param" type="text" name="param" value="${parameter == null?"":parameter}"/>--%>
                            <%--</span>--%>
                          <%--<span class="input-group-btn">--%>
                              <%--<select id="status" class="chosen-select form-control" style="width: 150px;">--%>
                                  <%--<option value="">分配状态(全部)</option>--%>
                                  <%--<c:if test="${status == 0}">--%>
                                      <%--<option value="0" selected="selected">未分配</option>--%>
                                  <%--</c:if>--%>
                                  <%--<c:if test="${status != 0}">--%>
                                      <%--<option value="0">未分配</option>--%>
                                  <%--</c:if>--%>
                                  <%--<c:if test="${status == 1}">--%>
                                      <%--<option value="1" selected="selected">已分配</option>--%>
                                  <%--</c:if>--%>
                                  <%--<c:if test="${status != 1}">--%>
                                      <%--<option value="1">已分配</option>--%>
                                  <%--</c:if>--%>
                              <%--</select>--%>
                              <%--&lt;%&ndash;</span>&ndash;%&gt;--%>
                           <%--&lt;%&ndash;<span class="input-group-btn">&ndash;%&gt;--%>
                                <%--<button id="search_btn" class="btn btn-default" type="button">查看</button>--%>
                              <%--</span>--%>
                                  <%--</div>--%>
                              <%--</div>--%>
                              <%--</div>--%>
                          <%--</div>--%>
                  <%--<!-- /tile header -->--%>

                  <div class="tile-body no-vpadding">

                    <div class="table-responsive">
                    
                      <table  class="table table-datatable table-custom table-sortable" id="basicDataTable">
                        <thead>
                          <tr>
						    <th class="no-sort">
                            <div class="checkbox check-transparent">
                              <input type="checkbox" value="1" id="allchck">
                              <label for="allchck"></label>
                            </div>
                          </th>
                              <th class="sortable sort-alpha">名称</th>
                              <th class="sortable sort-amount">云主名</th>
                              <th class="sortable sort-amount">云主机IP</th>
                              <th class="sortable sort-amount">所属服务器</th>
                              <th class="sortable sort-amount">带宽</th>
                              <th class="sortable sort-amount">硬盘</th>
                              <th class="sortable sort-amount">VCPU优先级</th>
                              <th class="sortable sort-amount">创建时间</th>
                          </tr>
                        </thead>
                        <tbody>
                          <c:forEach items="${qos_vo_list}" var="qos_vos">
                    		<tr class="gradeX">
                    		<td>
								<div class="checkbox check-transparent">
								  <input type="checkbox" value="${qos_vos.uuid}" name="checkboxid" id="chck${qos_vos.id}" >
								  <label for="chck${qos_vos.id}"></label>
								</div>
                                </td>
                                <td class="cut">${qos_vos.name}</td>
                                <td class="cut">${qos_vos.hostName}</td>
                                <td class="cut">${qos_vos.ip}</td>
                                <td class="cut">${qos_vos.serverIp}</td>
                                <td class="cut">
                                    <span class="label label-greensea">上行：${qos_vos.outboundBandwidth}MB</span>/
                                    <span class="label label-greensea">下行：${qos_vos.inboundBandwidth}MB</span>
                                </td>
                                <td>
                                    <span class="label label-greensea">IOPS：${qos_vos.maxIops}</span>
                                </td>
                                <td class="cut">
                                    <c:if test="${qos_vos.priority == 0}">
                                        <span class="label label-success">高优先级</span>
                                    </c:if>
                                    <c:if test="${qos_vos.priority == 1}">
                                        <span class="label label-orange">中优先级</span>
                                    </c:if>
                                    <c:if test="${qos_vos.priority == 2}">
                                        <span class="label label-danger">低优先级</span>
                                    </c:if>
                                </td>
                                <td class="cut"><fmt:formatDate
                                        value="${qos_vos.createTimeDate}"
                                        pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                </td>
                        </tr>
                    	</c:forEach>
                           
                        </tbody>
                      </table>
                      
                    </div>
                    
                    
                    
                  </div>
                   <div class="col-sm-2" style="margin-top: -40px;">
                        <div class="input-group table-options">
                          <select class="chosen-select form-control" id="oper_select">
                            <option value="">批量操作</option> 
                            <option value="delete">删除</option>
                          </select>
                          <span class="input-group-btn">
                            <button class="btn btn-default" type="button" id="submit_oper">提交</button>
                          </span>
                        </div>
                      </div>  
                    <div class="tile-body">

                    <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                    <a href="#modalConfirm" id="con" role="button"   data-toggle="modal"> </a>

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
                                <label style="align:center;" id="confirmcontent">确定要删除该QoS规则吗？</label>
                               </div>

                            </form>
                          </div>
                            <div class="modal-footer">
                                <button class="btn btn-green" id="confirm_btn" onclick="" data-dismiss="modal" aria-hidden="true">确定
                                </button>
                                <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->    
                    

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



     
     
    

    <script>
        var path = "<%=request.getContextPath()%>";
        var curId = "";
        var ids = [];
        var type = parseInt("${type}");
        var add_url;
        var list_url;

        if (type == 1) {
            add_url="desktopqos/add";
            list_url="desktopqos/all";
        }
        if (type == 2) {
            add_url="serverqos/add";
            list_url="serverqos/all";
        }
   
    
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
        "aaSorting": [ [7,'desc']],
        "aoColumnDefs": [
                         { 'bSortable': false, 'aTargets': [ "no-sort" ] }
                       ], 
        "fnInitComplete": function(oSettings, json) {
            $('.dataTables_filter').css("text-align","right");
            $('.dataTables_filter').css("margin-top","-40px");
            $('.dataTables_filter').css("margin-bottom","0px");
            $('.dataTables_filter input').attr("placeholder", "Search");        }
      });
      
      
 
 

      /* Get the rows which are currently selected */
      function fnGetSelected(oTable01Local){
        return oTable01Local.$('tr.row_selected');
      };
 	  $("div[class='col-md-4 text-right sm-center']").attr("class","tile-footer text-right"); 

       
      
    })
    
     $(function(){

         jQuery("#qos_add").click(function(){
             window.location.href = "${pageContext.request.contextPath}/"+add_url;
         });

         jQuery("#submit_oper").click(function(){
             var option = jQuery("#oper_select").val();
             if(option=="delete"){
                 var myids = [];
                 var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
                 $(datatable).each(function(){
                     myids.push(jQuery(this).val());
                 });
                 if(myids.length < 1){
                     $("#tipscontent").html("请选择要删除的QoS规则");
                     $("#dia").click();
                     return;
                 }
                 ids = myids;
                 $("#confirmcontent").html("确定要删除所选QoS规则吗？");
                 $("#confirm_btn").attr("onclick","deleteMultQoS();");
                 $("#con").click();
             }
         });

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
        });

//         $("#search_btn").click(function(){
//             var param = $("#param").val();
//             var status = $("#status").val();
//             window.location.href = encodeURI(path + "/box/list?param="+param+"&status="+status);
//
//         });

//         $('#param').bind('keypress',function(event){
//             if(event.keyCode == "13")
//             {
//                 $("#search_btn").click();
//             }
//         });

      });

        <%--function mod_box(id) {--%>
            <%--window.location.href = "${pageContext.request.contextPath}/box/"+id+"/modify";--%>
        <%--}--%>

        <%--function del_box(id) {--%>
            <%--$("#confirmcontent").html("确定要删除该QoS规则吗？");--%>
            <%--curId = id;--%>
            <%--$("#confirm_btn").attr("onclick","deleteTerminalBox();");--%>
            <%--$("#con").click();--%>
        <%--}--%>

        <%--function deleteTerminalBox(){--%>
            <%--jQuery.get("${pageContext.request.contextPath}/box/"+curId+"/delete",--%>
                    <%--function(data){--%>
                        <%--if(data.status=="success"){--%>
                            <%--location.href = "${pageContext.request.contextPath}/qos/all?type=1";--%>
                        <%--}else{--%>
                            <%--jQuery("#tipscontent").html(data.message);--%>
                            <%--jQuery("#dia").click();--%>
                        <%--}--%>
                    <%--});--%>

        <%--}--%>

        function deleteMultQoS(){
            jQuery.ajax({
                url: '<%=request.getContextPath()%>/qos/delete',
                type: 'post',
                dataType: 'json',
                data:{ids:ids,type:type},
                async: false,
                timeout: 10000,
                error: function()
                {
                },
                success: function(result)
                {
                    if(result.status=="success"){
                        location.href = path + "/"+list_url;
                    }else{
                        $("#tipscontent").html(result.message);
                        $("#dia").click();
                    }
                }

            });
        }
      	//通用导出数据方法
        function exportData(url){
            if (type == 1) {
            	url = "/export/desktopqosdata";
            }
            if (type == 2) {
            	url = "/export/serverqosdata";
            }
        	window.location.href = "<%=request.getContextPath() %>"+url;
        }
    </script>
    
  </body>
</html>
      
