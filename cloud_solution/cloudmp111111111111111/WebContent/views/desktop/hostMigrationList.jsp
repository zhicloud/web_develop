<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<!-- host_pool_detail.jsp -->
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
    <style type="text/css">
    .butCss{width:82px;}
    
.img{
	position: absolute;
	left: 0px;
	top: 0px;
	width: 160px;
	height: 50px;
}
.imghead{cursor: pointer;}
.img1{
    position: absolute;
    left: 0px;
    top: 10px;
    width: 346px;
    height: 1px;
}

.tdiv1{
    position: absolute;
    left: 160px;
    width: 160px;
    height: 50px;
    font-family: 'Arial Negreta', 'Arial';
    font-weight: 700;
    font-style: normal;
    font-size: 18px;
}
.tdvi2{
    position: absolute;
    left: 491px;
    width: 160px;
    height: 50px;
    font-family: 'Arial Negreta', 'Arial';
    font-weight: 700;
    font-style: normal;
    font-size: 18px;
    color: #FFFFFF;
}
.text{
	position: absolute;
    left: 2px;
    top: 14px;
    width: 156px;
    word-wrap: break-word;
    text-align: center;
    font-size: 20px;
    height: 37px;
}
.text1{
    left: 2px;
    top: 14px;
    width: 150px;
    word-wrap: break-word;
    text-align: center;
    font-size: 20px;
    height: 37px;
}
    </style>
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
            

            <h2><i class="fa fa-desktop"></i> 资源监控主机列表</h2>
            

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
	                    <button type="button" class="btn btn-success delete" onclick="window.location.href='javascript:history.back(-1);';">
		                    <i class="fa fa-step-backward"></i>
		                    <span> 返回上级</span>
	                    </button>
                    </div>
                    <br><br>
                    
                   	<div class="tile-body no-vpadding divMag" style="margin: auto;margin-left: 100px;">
						<div id="migrationHost" class="tdiv1">
							<img id="u112_img" class="img imghead" src="<%=request.getContextPath()%>/images/hostMigration/u112.png" tabindex="0">
							<div class="text"><p><span>主机迁移</span></p></div>
						</div>
						<div id="migrationLocalHost" class="tdiv2">
							<img id="u112_img" class="img " src="<%=request.getContextPath()%>/images/hostMigration/u110.png" tabindex="0">
							<div class="text"><p><span>迁移记录</span></p></div>
						</div>
					</div>
                     <br><br><br><br>
                    
                  <div class="tile-body no-vpadding"> 
                   
                    <div class="table-responsive">
                    
                      <table  class="table table-datatable table-custom" id="basicDataTable">
                        <thead>
                          <tr>
                            <th class="sort-alpha">主机名</th>
                            <th class="sort-alpha">源NC</th>
                            <th class="sort-alpha">目的NC</th>
                            <th class="sort-alpha">迁移状态</th>
							<th class="sort-alpha">迁移完成时间</th>
                            <th class="no-sort">操作</th> 
                          </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${hmlist}" var="hm">
                          <tr class="odd gradeX">
                                  <td class="cut">${hm.getHostName()}</td>
                                  <td class="cut">${hm.getLocalhostNC()}</td>
                                  <td class="cut">${hm.getToNC()}</td>
                                  <td class="cut">
                                 	 <c:if test="${hm.getStatus()==2}">迁移完成</c:if>
	                            	 <c:if test="${hm.getStatus()==3}">迁移失败</c:if>
                                  </td>
								  <td class="cut">
								  	 ${hm.getTime()}
								  </td>
								  <td> 
		                              <div class="btn-group">
		                                  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown"> 操作 <span class="caret"></span>
		                                  </button>
		                                  <ul class="dropdown-menu" role="menu" style="left:-122px;"> 
		                                  	<li><a href="#"  onclick="shutdownHost('${hm.getRealHostId()}');">关机</a></li> 
	                                    	<li><a href="#"  onclick="delMigration('${hm.getId()}');">删除记录</a></li>
	                                    	<li><a href="#"  onclick="deleteHost('${hm.getRealHostId()}');">删除主机</a></li>
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
                  
                  <!-- <div class="col-sm-2" style="margin-top: -40px;width:100%;">
	                  <p>批量操作</p>
	                  <button class="btn btn-default butCss" type="button" id="shutdown_oper">关机</button>
	                  <button class="btn btn-default butCss" type="button" id="start_oper">开机</button>
	                  <button class="btn btn-default butCss" type="button" id="restart_oper">重启</button>
	                  <button class="btn btn-default butCss" type="button" id="hostRome_oper">主机迁移</button>
	                  <button class="btn btn-default butCss" type="button" id="del_oper">删除</button>
                 </div> -->
				 <div class="tile-body">

                    <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                    <a href="#modalConfirm" id="con" role="button"   data-toggle="modal"> </a>
					<a href="#modalForm" id="mform" role="button"   data-toggle="modal"> </a>
					<a href="#modalAddDesktop" id="adddesktop" role="button"   data-toggle="modal"> </a>
                    
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
                            <button id="my_close" type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>主机管理</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form class="form-horizontal" id="addAmountForm" role="form" action="<%=request.getContextPath() %>/warehouse/addAmount" method="post">
		                      <input type="hidden" id="warehouse_id" name="id" value="">
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
                            <button id="my_reset" class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->
                    
                    <div class="modal fade" id="modalAddDesktop" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" >
                          <div class="modal-header">
                            <button  type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>添加主机</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form class="form-horizontal" id="addDesktop" role="form" action="<%=request.getContextPath() %>/warehouse/addAmount" method="post">
 		                      <div class="form-group">
		                        <label for="input01" class="col-sm-2 control-label" style="width:150px;">仓库 </label>
		                        <div class="col-sm-4"  id="selectbox">
										<select class="chosen-select   form-control" style="width:300px;" name="wareHostId"id="wareHostId" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox">
			                            <option value="">请选择仓库</option> 
			                            <c:forEach items="${wareHoseList }" var="sdi">
	 		                                 <option value="${sdi.id }">${sdi.name}</option>
	 		                             </c:forEach>   
			                          </select>                       
			                     </div>
		                      </div> 
                    		</form>
                    		
                    		<form class="form-horizontal" id="hostmove" role="form" action="<%=request.getContextPath() %>/cdrpm/${sdi.id }/hostMigration" method="post">
 		                      <div class="form-group">
		                        <label for="input01" class="col-sm-2 control-label" style="width:150px;">仓库 </label>
		                        <div class="col-sm-4"  id="selectbox">
										<select class="chosen-select   form-control" style="width:300px;" name="wareHostId"id="wareHostId" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox">
			                            <option value="">请选择仓库</option> 
			                            <c:forEach items="${wareHoseList }" var="sdi">
	 		                                 <option value="${sdi.id }">${sdi.name}</option>
	 		                             </c:forEach>   
			                          </select>                       
			                     </div>
		                      </div> 
                    		</form>
                    		
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green"  id="form_btn" onclick="addDesktop();">确定</button>
                            <button   class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
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
    var uuid =  '${uuid}' ;
    var curIds;
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
          "R<'row'<'col-md-6'l>r>"+
          "t"+
          "<'row'<'col-md-4 sm-center'i><'col-md-4'><'col-md-4 text-right sm-center'p>>",
        "aaSorting": [],
        "aoColumnDefs": [
                         { 'bSortable': false, 'aTargets': [ "no-sort" ] }
                       ]
      });
      

      /* Get the rows which are currently selected */
      function fnGetSelected(oTable01Local){
        return oTable01Local.$('tr.row_selected');
      };
	    $("div[class='col-md-4 text-right sm-center']").attr("class","tile-footer text-right");
	  $("#add_host").click(function(){
		  addHosts(warehouseId,sysImageName);
 	  });
	  
	//主机迁移绑定click 事件 
	jQuery("#migrationHost").click(function() {
		var id = 0;
		location.href=path +"/cdrpm/{"+id+"}/hostMigration"; 
    });

	  $("#my_close,#my_reset").click(function() {
	      $("#cmount_input").val("");
	  });
    })
    
      function deleteHost(id){
    	jQuery.get(path + "/cloudhost/"+id+"/delete",function(data){
			if(data.status == "success"){
				alert('操作成功');
	    		window.location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    
    function delMigration(id){
    	jQuery.get(path + "/cdrpm/"+id+"/delMigration",function(data){
			if(data.status == "success"){   
	    		window.location.reload();
			}else{  
				$("#tipscontent").html(data.message);
				$("#dia").click();
			}
		});
    }
    function shutdownHost(id){
    	jQuery.get(path + "/cdrpm/"+id+"/stop",function(data){
			if(data.status == "success"){   
	    		window.location.reload();
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
      

