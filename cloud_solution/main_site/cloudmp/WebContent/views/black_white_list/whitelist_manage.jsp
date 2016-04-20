<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- whitelist_manage.jsp -->
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
            

            <h2><i class="fa fa-file-text-o"></i> 白名单管理</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              
              <!-- col 6 -->
          <div class="col-md-12">

				  <section class="tile color transparent-black">
                  <!-- tile widget -->
                  <div class="tile-widget color transparent-black rounded-top-corners nopadding nobg">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs tabdrop">
                      <li ><a href="#users-tab" data-toggle="tab" onclick="window.location.href='<%=request.getContextPath() %>/networkrule/blacklist/all';">黑名单</a></li>
                      <li class="active" ><a href="#orders-tab" onclick="window.location.href='<%=request.getContextPath() %>/networkrule/whitelist/all';" data-toggle="tab">白名单</a></li>
                      <li><a href="#messages-tab" onclick="window.location.href='<%=request.getContextPath() %>/networkrule/desktopqos/all';" data-toggle="tab">QOS规则设置</a></li>
                      <li><a href="#tasks-tab" data-toggle="tab" onclick="window.location.href='<%=request.getContextPath() %>/networkrule/rule/all';">智能路由例外配置 </a></li>
                      <div id="space"></div>
                      
                     </ul>
                    <!-- / Nav tabs -->
                  </div>
                  <!-- /tile widget -->

                  <!-- /tile header -->

                        
                    <div class="tile-header">
                     <button type="button" class="btn btn-red delete" id="add_whitelist">
                              <i class="fa fa-plus"></i>
                              <span>增加白名单 </span>
                    </button>  
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                   
                  </div>
                       
                  <div class="tile-body no-vpadding"> 
                   
                    <div class="table-responsive">
                    
                      <table  class="table table-datatable table-custom" id="basicDataTable">
                        <thead>
                          <tr>
						    <th class="no-sort">
                            <div class="checkbox check-transparent">
                              <input type="checkbox" value="1" id="allchck">
                              <label for="allchck"></label>
                            </div>
                          </th>
                            <th class="sort-alpha">规则名</th>
                            <th class="sort-alpha">IP</th>
                            <th class="sort-alpha">描述</th>
                            <th class="no-sort">操作</th>
                          </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${whitelistList}" var="whitelist">
                        
                          <tr class="odd gradeX">
						          <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" name="idcheck" value="${whitelist.ruleId}" id="${whitelist.ruleId}" >
									  <label for="${whitelist.ruleId}"></label>
									</div>
                                 </td>
                                  <td class="cut">${whitelist.ruleName}</td>
                                  <td class="cut">${whitelist.ruleIp}</td>
                                  <td class="cut">${whitelist.description}</td>
								  <td>
                                  		<div class="btn-group">
                                  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                                    操作 <span class="caret"></span>
                                  </button>
                                  <ul class="dropdown-menu" role="menu">
		                              <li><a href="javascript:void(0);" onclick="deleteBtn('${whitelist.ruleId }');" >删除</a></li>
		                              <li><a href="javascript:void(0);" onclick="updateBtn('${whitelist.ruleId }');" >修改</a></li>
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
                          <select id="oper_select" class="chosen-select form-control">
                            <option value="">批量操作</option> 
                            <option value="del">删除</option>
                          </select>
                          <span class="input-group-btn">
                            <button class="btn btn-default" type="button" id="save_oper">提交</button>
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
                                <label style="align:center;" id="confirmcontent">确定要删除该云主机吗？</label>
                               </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green" id="confirm_btn"  onclick="toDelete();" data-dismiss="modal" aria-hidden="true">确定</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->                        
					
                    <div class="modal fade" id="modalDialog" tabindex="1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
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
    var path = '<%=request.getContextPath()%>'; 
    var currentId = "";
    var curIds = [];
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
	  $("#add_whitelist").click(function(){
		  window.location.href = path+"/networkrule/whitelist/add";
 	  });
	  
	  
	  
	  jQuery("#save_oper").click(function(){
		  	var option = jQuery("#oper_select").val();
		  	if(option=="del"){
				 var ids = [];
				 var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
				 $(datatable).each(function(){
					ids.push($(this).val());
				 });
// 				 jQuery("input[name='idcheck']:checkbox").each(function(){ 
// 		            if(jQuery(this).attr("checked")){
// 		                ids += jQuery(this).val()+","
// 		            }
// 		        })
		        if(ids.length<1){
		        	$("#tipscontent").html("请选择要删除的白名单");
					$("#dia").click();
		       	 	return;
		        } 
				curIds = ids;
				$("#confirmcontent").html("确定要删除所选白名单吗？");
		    	$("#confirm_btn").attr("onclick","deleteMultWhitelist();");
		    	$("#con").click();
		  	}
	     });
    })
    
    
    //删除黑名单
    function deleteBtn(id){
    	currentId = id;
    	$("#confirmcontent").html("确定要删除该白名单吗？");
    	$("#confirm_btn").attr("onclick","deleteWhitelist();");
    	$("#con").click();
    }
    //修改黑名单
    function updateBtn(id){
    	window.location.href = path+"/networkrule/whitelist/"+id+"/update";
    }
          
    //置空
    function setNullBtn(){
    	 $("#cmount_input").val("");
    }
    function deleteWhitelist(){
    	jQuery.get(path + "/networkrule/whitelist/"+currentId+"/delete",function(data){
			if(data.status == "success"){   
	    		location.href = path + "/networkrule/whitelist/all";
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    
    function deleteMultWhitelist(){
    	jQuery.ajax({
	        url: '<%=request.getContextPath()%>/networkrule/whitelist/deletemore',
	        type: 'post', 
	        dataType: 'json',
	        data:{ids:curIds},
	        timeout: 10000,
	        error: function()
	        { 
	        },
	        success: function(result)	        
	        {  
	        	if(result.status=="success"){
	    			location.href = path + "/networkrule/whitelist/all";
	        	}
	        	else{
		        	$("#tipscontent").html(result.message);
					$("#dia").click();
	        	}
	        }
	        
	     });  
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
      

