<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- vpc_manage_host.jsp -->
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
            

            <h2><i class="fa fa-desktop"></i> 云桌面主机管理</h2>
            

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
                     <button type="button" class="btn btn-red delete" id="add_host">
                              <i class="fa fa-plus"></i>
                              <span>创建主机 </span>
                    </button>  
<!-- 					<button type="button" class="btn btn-red delete" id="add_host"> -->
<!--                               <i class="fa fa-plus"></i> -->
<!--                               <span>增加该类型云主机 </span> -->
<!--                     </button> -->
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
                            <th class="sort-alpha">云主机名称</th>
                            <th class="sort-alpha">CPU核数</th>
                            <th class="sort-alpha">内存</th>
                            <th class="sort-alpha">硬盘</th>
                            <th class="sort-alpha">带宽</th>
                            <th class="sort-alpha">外网IP</th>
                            <th class="sort-alpha">外网端口</th>
                            <th class="sort-alpha">运行状态</th>  
                            <th class="no-sort">操作</th>
                          </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${hostList}" var="hostList">
                        
                          <tr class="odd gradeX">
						          <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" name="idcheck" value="${hostList.id}" id="${hostList.id}" isAssigned="${hostList.userId}" realHostId="${hostList.realHostId }" status="${hostList.status }">
									  <label for="${hostList.id}"></label>
									</div>
                                 </td>
                                  <td class="cut">${hostList.displayName}</td>
                                  <td class="cut">${hostList.cpuCore}核</td>
                                  <td class="cut">${hostList.getMemoryText(0) }</td>
                                  <td class="cut">${hostList.getDataDiskText(0) }</td>
                                  <td class="cut">${hostList.getBandwidthText(0) }</td>
                                  <td class="cut">${hostList.outerIp }</td>
                                  <td class="cut">${hostList.outerPort }</td>
								  <td class="cut">${hostList.getSummarizedStatusText() }</td> 
                                  <td> 
                                  <c:choose>
                                  	<c:when test="${hostList.realHostId==null and hostList.status==1}">
                                  		无
                                  	</c:when>
                                  	<c:otherwise>
                                  		<div class="btn-group">
                                  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                                    操作 <span class="caret"></span>
                                  </button>
                                  <ul class="dropdown-menu" role="menu">
                                  	<c:if test="${hostList.status==0 or hostList.status == 3}">
		                              <li><a href="javascript:void(0);" onclick="unboundBtn('${hostList.id }');">解除绑定</a></li>                        	
		                            </c:if> 
		                            <c:if test="${hostList.status==2}">
<%-- 		                           		<li><a href="javascript:void(0);" onclick="toDetail('${hostList.id }');" title="详细"></a></li> --%>
		                                 <c:if test="${hostList.runningStatus==1}">
			                              <li><a href="javascript:void(0);" onclick="startHostBtn('${hostList.id }');" >开机</a></li>
			                              <li><a href="javascript:void(0);" onclick="updateHostBtn('${hostList.id }','1');">配置修改</a></li>
			                              <li><a href="javascript:void(0);" onclick="unboundBtn('${hostList.id }');">解除绑定</a></li>
		                                </c:if>
		                                <c:if test="${hostList.runningStatus==2}">
			                              <li><a href="javascript:void(0);" onclick="shutdownHostBtn('${hostList.id }');" >关机</a></li>
			                              <li><a href="javascript:void(0);" onclick="restartHostBtn('${hostList.id }');" >重启</a></li>
			                              <li><a href="javascript:void(0);" onclick="haltHostBtn('${hostList.id }');" >强制关机</a></li>
			                              <li><a href="javascript:void(0);" onclick="updateHostBtn('${hostList.id }','2');">配置修改</a></li>
			                              <li><a href="javascript:void(0);" onclick="unboundBtn('${hostList.id }');">解除绑定</a></li>
		                                 </c:if>
		                             </c:if>  
                                  </ul>
                              </div>
                                	</c:otherwise>
                                </c:choose>
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
					
					
					 <div class="modal fade" id="modalForm" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button onclick="setNullBtn();" type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>主机管理</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form class="form-horizontal" id="addAmountForm" role="form" action="<%=request.getContextPath() %>/warehouse/addAmount" method="post">
		                      <input type="hidden" id="warehouse_id" name="id" value="">
		                      <div class="form-group">
		                        <label for="input01" class="col-sm-2 control-label" style="width:150px;">服务器资源池 </label>
		                        <div class="col-sm-4" id="selectpool" style="width:160px;">
									<select class="form-control" name="poolId" id="poolId" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectpool">
			                            <option value="">请选择资源池</option>  
			                            <c:forEach items="${computerPool }" var="sdi">
		 	                                 <option value="${sdi.uuid }">${sdi.name }</option>
		 	                             </c:forEach>  
			                          </select>                       
			                     </div>
		                      </div>
		                      <div class="form-group">
		                        <label for="input01" class="col-sm-2 control-label" style="width:150px;">增加主机数量</label>
		                        <div class="col-sm-4" style="width:160px;">
		                          <input type="text" id="cmount_input" class="form-control" name="addAmount" maxlength="4" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"  parsley-required="true" parsley-max="100" parsley-min="1">
		                        </div>
		                      </div>
                    		</form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green"  id="form_btn" onclick="saveAddAmount();">确定</button>
                            <button onclick="setNullBtn();" class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
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
    var vpcId = '${vpcId}';
    var hostIds = [];
    var oneCloudHostId = [];
    var currentId = "";
    var runStatus = "";
    var curIds = "";
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
	    
	  $("#add_host").click(function(){
		  window.location.href = path + "/vpc/" + vpcId + "/addVpcHost";
 	  });
	  
    })
    //解除绑定
    function unboundBtn(id){
    	currentId = id;
    	$("#confirmcontent").html("主机解绑之后主机将被删除，无法恢复<br/>确定解除绑定吗？");
    	$("#confirm_btn").attr("onclick","unboundHost();");
    	$("#con").click();
    }
    //启动主机
    function startHostBtn(id){
    	currentId = id;
    	$("#confirmcontent").html("确定要启动该主机吗？");
    	$("#confirm_btn").attr("onclick","startHost();");
    	$("#con").click();
    }
    //关闭主机
    function shutdownHostBtn(id){
    	currentId = id;
    	$("#confirmcontent").html("确定要关闭该主机吗？");
    	$("#confirm_btn").attr("onclick","shutdownHost();");
    	$("#con").click();
    }
    //重启主机
    function restartHostBtn(id){
    	currentId = id;
    	$("#confirmcontent").html("确定要重启该主机吗？");
    	$("#confirm_btn").attr("onclick","restartHost();");
    	$("#con").click();
    }
    //强制关机
    function haltHostBtn(id){
    	currentId = id;
    	$("#confirmcontent").html("确定要强制关闭该主机吗？");
    	$("#confirm_btn").attr("onclick","haltHost();");
    	$("#con").click();
    }
    //修改配置
    function updateHostBtn(id,run_status){
    	currentId = id;
    	runStatus = run_status;
    	if(run_status=="2"){
    		$("#confirmcontent").html("未关机的云主机不能修改CPU，确定继续？");
	    	$("#confirm_btn").attr("onclick","updateHost();");
	    	$("#con").click();
    	}else{
    		window.location.href = path+"/vpc/"+currentId+"/"+runStatus+"/updateHost";
    	}
    }
    //置空
    function setNullBtn(){
    	 $("#cmount_input").val("");
    }
    function unboundHost(){
    	jQuery.get(path + "/vpc/"+currentId+"/unbound",function(data){
			if(data.status == "success"){   
	    		location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function updateHost(){
    	window.location.href = path+"/vpc/"+currentId+"/"+runStatus+"/updateHost";
    }
    function startHost(){
    	jQuery.get(path + "/vpc/"+currentId+"/start",function(data){
			if(data.status == "success"){   
	    		location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function restartHost(){
    	jQuery.get(path + "/vpc/"+currentId+"/restartHost",function(data){
			if(data.status == "success"){   
				location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function shutdownHost(){
    	jQuery.get(path + "/vpc/"+currentId+"/shutdownHost",function(data){
			if(data.status == "success"){   
				location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function haltHost(){
    	jQuery.get(path + "/vpc/"+currentId+"/haltHost",function(data){
			if(data.status == "success"){   
				location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
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
        })
        
      });
    </script>
  </body>
</html>
      

