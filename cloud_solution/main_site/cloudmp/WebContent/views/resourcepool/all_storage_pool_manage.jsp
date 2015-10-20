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
            

            <h2><i class="fa fa-cube"></i> 存储资源池管理</h2>
            

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
<!--                      <button type="button" class="btn btn-success delete"> -->
<!--                               <i class="fa fa-plus"></i> -->
<!--                               <span> 新增资源池 </span> -->
<!--                             </button> -->
                            <div class="search  text-left" id="main-search">
<!--               <i class="fa fa-search"></i> <input type="text" placeholder="资源池名称"> -->
            </div>
            
            <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                    
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">

                    <div class="row">
					<c:forEach items="${computerPool }" varStatus="i" var="cp">
						<div class="col-lg-3 col-md-3" style="width:330px;">
                       
                        
                        <div class="panel panel-greensea">
                          <div class="panel-heading title">
                            <h3 class="panel-title">资源池${i.count }</h3>
                          </div>
                          
                          <div class="panel-body">
                          <p><strong>UUID：<br>${cp.uuid }</strong></p>
                          <p><strong>存储节点：</strong><a href="javascript:void(0);"  cur_id="${cp.uuid }" class="query_detail"><span class="badge badge-greensea">${cp.getAllNode()}</span></a>节点</p>
                          <p><strong>资源池名：</strong>${cp.name }</p>
                           <p><strong>运行状态：${cp.getStatusText() }</strong></p>
                           <p><strong>云磁盘数量：</strong><a href="javascript:void(0);" cur_id="${cp.uuid }" class="query_host_detail"><span class="badge badge-greensea">${cp.getAllDisk()}</span></a>台</p>
                           
                           <div class="progress-list">
                      <div class="details">
                        CPU<small> 共${cp.cpuCount }核</small>
                      </div>
                      <div class="pull-right label label-red margin-top-10">
                        <span class="animate-number" data-value="${cp.getCpuUsageFormat() }" data-animation-duration="1500">${cp.getCpuUsageFormat() }</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little no-radius">
                        <div class="progress-bar progress-bar-red animate-progress-bar" data-percentage="${cp.getCpuUsageFormat() }%" style="width: ${cp.getCpuUsageFormat() }%;"></div>
                      </div>
                    </div>
                    
                    <div class="progress-list">
                      <div class="details">
                        内存<small> 已用${cp.getUsedMemoryText() }G/共${cp.getAllMemoryText() }G</small>
                      </div>
                      <div class="pull-right label label-green margin-top-10">
                        <span class="animate-number" data-value="${cp.getMemoryUsageFormat() }" data-animation-duration="1500">${cp.getMemoryUsageFormat() }</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little no-radius">
                        <div class="progress-bar progress-bar-green animate-progress-bar" data-percentage="${cp.getMemoryUsageFormat() }%" style="width: ${cp.getMemoryUsageFormat() }%;"></div>
                      </div>
                    </div>
                    
                    <div class="progress-list">
                      <div class="details">
                        磁盘<small> 已用${cp.getUsedDiskText() }G/共${cp.getAllDiskText() }G</small>
                      </div>
                      <div class="pull-right label label-blue margin-top-10">
                        <span class="animate-number" data-value="${cp.getDiskUsageFormat() }" data-animation-duration="1500">${cp.getDiskUsageFormat() }</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little no-radius">
                        <div class="progress-bar progress-bar-blue animate-progress-bar" data-percentage="${cp.getDiskUsageFormat() }%" style="width: ${cp.getDiskUsageFormat() }%;"></div>
                      </div>
                    </div>
                    
                          <div class="btn-group">
                      <button type="button" class="btn btn-greensea dropdown-toggle" data-toggle="dropdown">
                        操作 <span class="caret"></span>
                      </button>
                      <ul class="dropdown-menu" role="menu">
<!--                       	<li><a href="#">新增资源节点</a></li> -->
                      	<li><a href="javascript:void(0);" cur_id="${cp.uuid }" class="query_detail">查看资源节点</a></li>
<!--                         <li><a href="#">编辑该资源池</a></li>
<!--                         <li><a href="#">删除该资源池</a></li> -->
                      </ul>
                    </div>
								
                          </div>
                        </div>

                      </div>
					</c:forEach>

                    </div>

                  </div>
                  <!-- /tile body -->
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
    <script type="text/javascript">
    var path = '<%=request.getContextPath()%>'; 

    $(function(){
    	jQuery(".query_detail").click(function(){
	    	var uuid = jQuery(this).attr("cur_id");
	    	window.location.href = path+"/storageresourcepool/"+uuid+"/datanode";
	    });	 
 	  });
    </script>



 
     
     
  </body>
</html>
      

