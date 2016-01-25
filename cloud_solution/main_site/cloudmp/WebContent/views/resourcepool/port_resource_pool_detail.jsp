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
            

            <h2><i class="fa fa-life-buoy"></i> 端口资源管理</h2>
            

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
                      <li ><a href="#users-tab" data-toggle="tab" onclick="window.location.href='<%=request.getContextPath() %>/networkpool/ipresourcepool/all';">地址资源池管理 </a></li>
                      <li class="active"><a href="#orders-tab" onclick="window.location.href='<%=request.getContextPath() %>/networkpool/portresourcepool/all';" data-toggle="tab">端口资源池管理</a></li>
		                      <div id="space"></div>
                      
                     </ul>
                    <!-- / Nav tabs -->
                  </div>
                  <!-- /tile widget -->


                  <!-- tile header -->
                  <div class="tile-header">
                     <a href="<%=request.getContextPath()%>/networkpool/portresourcepool/all"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>
                     <button type="button" class="btn btn-success delete" id="create_resource_node">
                              <i class="fa fa-plus"></i>
                              <span> 新增端口资源 </span>
                            </button>
<!--              <div class="search  text-left" id="main-search">-->
<!--              <i class="fa fa-search"></i> <input id="search_input" type="text" placeholder="端口资源节点名称">-->
<!--            </div>-->
<!--            -->
            		<div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">

                  <div class="row">
				  <c:forEach items="${portPoolDetailList}" varStatus="i" var="cp">
						<div class="col-lg-3 col-md-3" style="width:330px;">
                       
                        
                        <div class="panel panel-greensea">
                          <div class="panel-heading title">
                            <h3 class="panel-title">端口资源${i.count}</h3>
                          </div>
                          
                   <div class="panel-body">
                    <p><strong>起始IP：${cp.ip }</strong></p>
                    <p><strong>运行状态：${cp.getStatusText() }</strong></p>
                    
					<div class="progress-list">
						<div class="details">
							端口
							<small> 共${cp.count[1] }个/可用${cp.count[0]}个</small>
						</div>
						<div class="pull-right label label-blue margin-top-10">
							
						       <span class="animate-number"
<%-- 								data-value="使用率${cp.getPortUsageFormat() }" --%>
								data-animation-duration="1500">使用率${cp.getPortUsageFormat()
								}</span>%
						</div>  
						<div class="clearfix"></div>
						<div class="progress progress-little no-radius">
							<div
								class="progress-bar progress-bar-red animate-progress-bar"
								data-percentage="${cp.getPortUsageFormat()}%"
								style="width: ${cp.getPortUsageFormat()}%;"></div>
						</div>
					</div>
                    
                    <div class="btn-group">
                    <button type="button" class="btn btn-greensea dropdown-toggle" data-toggle="dropdown">
                      
                       	 操作 <span class="caret"></span>
                      </button>
                      <ul class="dropdown-menu" role="menu">
<!--                       	<li><a href="#">新增资源节点</a></li> -->
                        <li><a href="javascript:void(0);" cur_ip="${cp.ip}" class="delete_resource_node">删除该端口资源节点</a></li>
<!--                         <li><a href="#">编辑该资源池</a></li> -->
                      </ul>
                    </div>
								
                          </div>
                        </div>

                      </div>
					</c:forEach>

                    </div>

                  </div>

                </section>
                <!-- /tile -->
           		<div class="tile-body">
                   <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                   <a href="#modalConfirm" id="con" role="button"  data-toggle="modal"> </a>
                   
                   
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
	                               <label style="align:center;" id="confirmcontent">确定要删除该端口资源吗？</label>
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
	    var poolId = "${poolId}";
	    var cur_ip = "";	
	    $(function(){
	    	//添加资源节点(后台判断权限)
	    	$("#create_resource_node").click(function(){
	    		window.location.href ="<%=request.getContextPath()%>/networkpool/portresourcepool/"+poolId+"/an";
	    	});
	    	//删除资源节点
		    $(".delete_resource_node").click(function(){
		    	var ip = $(this).attr("cur_ip");
		    	cur_ip = ip;
		    	$("#confirmcontent").html("确定要删除端口资源资源节点吗？");
		    	$("#confirm_btn").attr("onclick","deleteResourceNode();");
		    	$("#con").click();
		    });
	    });
	    
	    function deleteResourceNode(){
		   	jQuery.ajax({
		        url: '<%=request.getContextPath()%>/networkpool/portresourcepool/dn',
		        type: 'post', 
		        dataType: 'json',
		        data:{ip:cur_ip,poolId:poolId},
		        timeout: 10000,
		        error: function(){
		        	alert('Error!');
		        },
		        success: function(result)	        
		        {  
		        	if(result.status=="success"){
		        		location.href = "<%=request.getContextPath()%>/networkpool/portresourcepool/"+poolId+"/qn";
		        	}
		        	else{
			        	$("#tipscontent").html(result.message);
						$("#dia").click();
		        	}
		        }	        
		     });  
	    }
	    $(function(){  
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
      

