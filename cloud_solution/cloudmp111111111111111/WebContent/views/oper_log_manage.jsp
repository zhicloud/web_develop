<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- oper_log_manage.jsp -->
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
            

            <h2><i class="fa fa-book"></i> 操作日志管理</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              
              <!-- col 6 -->
          <div class="col-md-12">

				  <section class="tile color transparent-black">


                  <!-- /tile header -->
					
                    <div class="tile-header">
                      
                    <!-- <div class="col-sm-3">
                    	<div class="input-group table-options">
                    	<span class="input-group-btn">
                   			<input id="username" type="text" name=username value="" />
                   		</span>	
                   		
                   		<span class="input-group-btn">
                          <select id="oper_level" class="chosen-select form-control" style="width:140px;">
                            <option value="">操作等级(全部)</option>
                            <option value="1">一般</option>
                            <option value="2">敏感</option>
                            <option value="3">高危</option>
                          </select>
                   		</span>
                   		<span class="input-group-btn">
                          <select id="oper_status" class="chosen-select form-control" style="width:140px;">
                            <option value="">操作结果(全部)</option>
                            <option value="1">成功</option>
                            <option value="2">失败</option>
                            <option value="3">异步</option>
                          </select>
                   		</span>
                          <span class="input-group-btn">
                            <button id="search_btn" class="btn btn-default" type="button">查看</button>
                          </span>
                        </div>
                    </div> -->
                            <button type="button" class="btn btn-green delete" id="report_log">
                              <i class="fa fa-download"></i>
                              <span>导出日志 </span>
                    		</button> 
                    <div class="controls">
                    
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                   
                  </div>
                       <!-- start -->
                    <div class="tile-widget bg-transparent-black-2">
                    <div class="row">
                      
                      <div class="col-sm-6 col-xs-6" style="z-index: 100;">
                        <div class="input-group table-options">
                        <span class="input-group-btn">
                   			<input id="username" type="text" name=username value=""/>
                   		</span>
                        <span class="input-group-btn">
                          <select id="oper_level" class="chosen-select form-control">
                            <option value="">操作等级(全部)</option>
                            <option value="1">一般</option>
                            <option value="2">敏感</option>
                            <option value="3">高危</option>
                          </select>
                          <select id="oper_status" class="chosen-select form-control">
                            <option value="">操作结果(全部)</option>
                            <option value="1">成功</option>
                            <option value="2">失败</option>
                            <option value="3">异步</option>
                          </select>
                          </span>
                          <span class="input-group-btn">
                            <button id="search_btn" class="btn btn-default" type="button">查看</button>
<!--                             <button id="reset_btn" class="btn btn-default" type="button">重置</button> -->
                          </span>
                        </div>
                      </div>

                      <!-- <div class="col-sm-8 col-xs-6 text-right">
                        
                         <div class="btn-group btn-group-xs table-options">
                          <button type="button" class="btn btn-default">Day</button>
                          <button type="button" class="btn btn-default">Week</button>
                          <button type="button" class="btn btn-default">Month</button>
                        </div>

                      </div> -->


                    </div>
                  </div>
                   <!-- -----over----- -->
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
                            <th class="sort-alpha">操作模块</th>
                            <th class="sort-alpha">操作用户</th>
                            <th class="sort-alpha">用户IP</th>
                            <th class="sort-alpha">操作内容</th>
							<th class="sort-alpha">操作时间</th>
                            <th class="sort-alpha">操作状态</th>  
                            <th class="sort-alpha">敏感度</th>  
                            <th class="no-sort">操作</th>
                          </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${operLogList}" var="operLog">
                        
                          <tr class="odd gradeX">
						          <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" name="idcheck" value="${operLog.id}" id="${operLog.id}">
									  <label for="${operLog.id}"></label>
									</div>
                                 </td>
                                  <td>${operLog.module}</td>
                                  <td>
                                  <c:if test="${operLog.username != null}">
                                  ${operLog.username}
                                  </c:if>
                                  <c:if test="${operLog.username == null}">
                                  ${operLog.userId}
                                  </c:if>
                                  </td>
                                  <td>${operLog.userIp}</td>
								  <td class="cut">${operLog.content }</td>
								  <td>${operLog.getOperTimeFormat() }</td> 
								  <td>${operLog.getStatusFormat() }</td> 
								  <td>${operLog.getLevelFormat() }</td> 
                                  <td> 
                                  	<div class="btn-group">
                                  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                                    操作 <span class="caret"></span>
                                  </button>
                                  <ul class="dropdown-menu" role="menu">
		                            <li><a href="javascript:void(0);" onclick="deleteLogBtn('${operLog.id}');">删除</a></li>                        	
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
					<a href="#modalForm" id="mform" role="button"   data-toggle="modal"> </a>
                    
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
    var curStatus = "";
    var curName = "";
    var curLevel = "";
    var curIds = [];
    var page = parseInt('${page}');
    var count = parseInt('${count}');
    var _name = "${name}"
    var _level = "${level}"
    var _status = "${status}"
    var flag = 0;

    $(function(){
      if(_name!=""){
      	$("#username").val(_name);
      }
      if(_level!=""){
        $("#oper_level").val(_level);
      }
      if(_status!=""){
        $("#oper_status").val(_status);
      }
      
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
        "aaSorting": [ [5,'desc']],
        "aoColumnDefs": [
                         { 'bSortable': false, 'aTargets': [ "no-sort" ] }
                       ], 
        "fnInitComplete": function(oSettings, json) { 
          $('.dataTables_filter').hide();
          $('.dataTables_filter').css("text-align","right");
          $('.dataTables_filter').css("width","300px");   
          $('.dataTables_filter').css("margin-top","-40px");
          $('.dataTables_filter').css("margin-bottom","0px");   
          $('.dataTables_filter').css("margin-left","465px");   
          $('.dataTables_filter input').attr("placeholder", "Search");
        }
      });
 
 

      /* Get the rows which are currently selected */
      function fnGetSelected(oTable01Local){
        return oTable01Local.$('tr.row_selected');
      };
	    $("div[class='col-md-4 text-right sm-center']").attr("class","tile-footer text-right");
	  $("#report_log").click(function(){
		  
		  var username = $("#username").val();
		  var operLevel = $("#oper_level").val();
		  var operStatus = $("#oper_status").val();
// 		  curName = username;
// 		  curStatus = operStatus;
// 		  curLevel = operLevel;
		  location.href = path + "/operlog/download?name="+username+"&level="+operLevel+"&status="+operStatus;
// 		  $("#confirmcontent").html("确定要导出操作日志吗？");
// 	      $("#confirm_btn").attr("onclick","reportLog();");
// 	      $("#con").click();
 	  });
	  
	  jQuery("#save_oper").click(function(){
		  	var option = jQuery("#oper_select").val();
		  	if(option=="del"){
				 var ids = [];
				 var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
				 $(datatable).each(function(){
					ids.push(jQuery(this).val());
				 });
		        if(ids.length==0){
		        	$("#tipscontent").html("请选择要删除的操作日志");
					$("#dia").click();
		       	 	return;
		        } 
				curIds = ids;
				$("#confirmcontent").html("确定要删除所选操作日志吗？");
		    	$("#confirm_btn").attr("onclick","deleteMultLog();");
		    	$("#con").click();
		  	}
	     });


	  
	  $("#search_btn").click(function(){
		  var username = $("#username").val();
		  var operLevel = $("#oper_level").val();
		  var operStatus = $("#oper_status").val();

		  window.location.href = path + "/operlog/all?page="+page+"&name="+username+"&level="+operLevel+"&status="+operStatus+"&flag="+flag;
	  });
	  $("#reset_btn").click(function(){
		  $("#username").val("");
		  $("#oper_level option[text='操作等级(全部)']").attr("select",true);
		  $("#oper_status option[text='操作结果(全部)']").attr("select",true);
	  });

        $("#username").bind("propertychange input",function(){
            flag = 1;
        });

//        var ie = !!window.ActiveXObject;
//        if(ie){
//            $("#username").attr('onpropertychange', 'flagChange');
//        }else{
//            $("#username").attr('oninput', 'flagChange');
//        }

    })

//    function flagChange(){
//        flag = 1;
//        console.info(flag);
//
//    }

    //删除日志
    function deleteLogBtn(id){
    	currentId = id;
    	$("#confirmcontent").html("确定要删除该条操作日志吗？");
    	$("#confirm_btn").attr("onclick","deleteLog();");
    	$("#con").click();
    }
    //导出日志
    function reportLog(){
    	location.href = path + "/operlog/download?name="+curName+"&level="+curLevel+"&status="+curStatus;
    }
    //置空
    function setNullBtn(){
    	 $("#cmount_input").val("");
    }
      function deleteLog(){
    	jQuery.get(path + "/operlog/"+currentId+"/delete",function(data){
			if(data.status == "success"){   
	    		location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function deleteMultLog(){
    	jQuery.ajax({
	        url: '<%=request.getContextPath()%>/operlog/deleteLogByIds',
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
	    			location.reload();
	        	}
	        	else{
		        	$("#tipscontent").html(result.message);
					$("#dia").click();
	        	}
	        }
	        
	     });  
    }

    $(function () {
        var prev = page - 1;
        var next = page + 1;
        var pageContent = '';

        var flag = false;

        if (_name != null || _status != null || _level != null) {
            flag = true;
        }

        if (prev > 0) {
            if (flag) {
                pageContent = '<li class="prev"><a href='+path+"/operlog/all?page="+prev+"&name="+_name+"&level="+_level+"&status="+_status+'><i class="fa fa-angle-double-left"></i></a></li>';

            } else {
                pageContent = '<li class="prev"><a href='+path+"/operlog/all?page="+prev+'><i class="fa fa-angle-double-left"></i></a></li>';
            }
        } else {
            pageContent = '<li class="prev disabled"><a href="#"><i class="fa fa-angle-double-left"></i></a></li>';
        }
        var i = 1;
        var length = (count > 5?5:count);

        var n = parseInt(page / 5);


        if (page > 5 * n && (i + 5) < count ) {
            i += 5 * n;
            length += 5 * n;
        } else if (page == 5 * ((n>1)?n:0) && (i + 5) < count) {
            i += 5 * ((n>1)?(n - 1):1);
            length += 5 * ((n>1)?(n - 1):1);
        }
        for (; i <= length; i++) {
                if (i > count) {
                    break;
                }
                if (i == page) {
                    if (flag) {
                        pageContent += '<li class="active"><a href=' + path + "/operlog/all?page=" + i + "&name="+_name+"&level="+_level+"&status="+_status+ '>' + i + '</a></li>';
                    } else {
                        pageContent += '<li class="active"><a href=' + path + "/operlog/all?page=" + i + '>' + i + '</a></li>';
                    }
                } else {
                    if (flag) {
                        pageContent += '<li class=""><a href=' + path + "/operlog/all?page=" + i + "&name="+_name+"&level="+_level+"&status="+_status+ '>' + i + '</a></li>';

                    } else {
                        pageContent += '<li class=""><a href=' + path + "/operlog/all?page=" + i + '>' + i + '</a></li>';

                    }
                }
        }


        if (next <= count) {
            if (flag) {
                pageContent = pageContent + '<li class="next"><a href='+path+"/operlog/all?page="+next+ "&name="+_name+"&level="+_level+"&status="+_status+ '><i class="fa fa-angle-double-right"></i></a></li>';
            } else {
                pageContent = pageContent + '<li class="next"><a href='+path+"/operlog/all?page="+next+'><i class="fa fa-angle-double-right"></i></a></li>';
            }
        } else {
            pageContent = pageContent + '<li class="next disabled"><a href="#"><i class="fa fa-angle-double-right"></i></a></li>';
        }
        $(".bootstrap ul")[0].innerHTML = pageContent;

    });
    
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
      

