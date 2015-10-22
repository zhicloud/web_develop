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
            

            <h2><i class="fa fa-life-buoy"></i> 计算资源节点磁盘挂载(NC: ${nodename})</h2>
            

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
                     
                     <button type="button" class="btn btn-success delete" 
                          <c:if test="${formPageFlag==0}">onclick="window.location.href='<%=request.getContextPath()%>/cdrpm/${poolId}/rd';"</c:if>
                          <c:if test="${formPageFlag==1}">onclick="window.location.href='<%=request.getContextPath()%>/computeresourcepool/${poolId}/nc';"</c:if>
                          >
                              <i class="fa fa-step-backward"></i>
                              <span> 返回上级</span>
                    </button>

                    <button type="button" class="btn btn-success delete" id="create_resource_node" onclick="$('#mform').click();">
                              <i class="fa fa-plus"></i>
                              <span> 挂载共享存储 </span>
                    </button>
<!--                     <div class="search  text-left" id="main-search"> -->
<!--               			<i class="fa fa-search"></i> <input id="search_input" type="text" placeholder="磁盘名称"> -->
<!--             		</div> -->
            
            		<div class="controls">
                      	<a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                    
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">

                    <div class="row">
					<c:forEach items="${deviceList}" varStatus="i" var="disk">
						<div class="col-lg-3 col-md-3" style="width:330px;">
                       
                        
                        <div class="panel panel-greensea">
                          <div class="panel-heading title">
                            <h3 class="panel-title">磁盘${i.count}</h3>
                          </div>
                          
                          <div class="panel-body">
                          <p>序号：${disk.index}</p>
                          <p>目标：${disk.target}</p>
                          <p>挂载类型：${disk.getDiskTypeText()}</p>
                          <c:if test="${disk.status==1}"><p>挂载状态：${disk.getStatusText() }</p></c:if>
                          <c:if test="${disk.status==0}"><p>挂载状态：<font color="red">${disk.getStatusText() }</font></p></c:if>
                          <p>可用性：${disk.getAvailableText() }</p>
					      <div class="progress-list">
								<div class="details">
									存储空间：
									<small> 已用${disk.disk_used/1024/1024/1204}GB/总计${disk.disk_volume/1024/1024/1204}GB</small>
								</div>
								<br/>
								<div class="pull-right label label-blue margin-top-10">
									
								       <span class="animate-number"
										data-value="使用率${disk.getDiskUsageFormat() }"
										data-animation-duration="1500">${disk.getDiskUsageFormat()
										}</span>%
								</div>  
								<div class="clearfix"></div>
								<div class="progress progress-little no-radius">
									<div
										class="progress-bar progress-bar-red animate-progress-bar"
											data-percentage="${disk.getDiskUsageFormat()}%"
											style="width: ${disk.getDiskUsageFormat()}%;">
									</div>
								</div>
							</div>


		                    <div class="btn-group">
		                     	<button type="button" class="btn btn-greensea dropdown-toggle" data-toggle="dropdown">
		                     		 操作 <span class="caret"></span>
		                      	</button>
		                      	<ul class="dropdown-menu" role="menu">
			                      	<c:if test="${disk.status==1}"><!-- 1 已经挂载 -->
										<li><a href="javascript:void(0);" nodename="${nodename}" disk_type="${disk.disk_type}" vindex="${disk.index}" onclick="unmountDiskClick(this);">取消磁盘挂载</a></li>  
			                        </c:if>                      

			                        <c:if test="${disk.disk_type==0}"><!-- 0 本地磁盘 -->
				                      	<c:if test="${disk.status==0}">
											<li><a href="javascript:void(0);" nodename="${nodename}" vindex="${disk.index}"  disk_type="${disk.disk_type}" onclick="mountDiskClick(this);">挂载本地磁盘</a></li>  
				                        </c:if> 
				                      	<c:if test="${disk.available==1}"><!-- 1 可用 -->
											<li><a href="javascript:void(0);" nodename="${nodename}" vindex="${disk.index}" onclick="disableDiskClick(this);">禁用本地磁盘</a></li>  
				                        </c:if>                      
				                      	<c:if test="${disk.available==0}">
											<li><a href="javascript:void(0);" nodename="${nodename}" vindex="${disk.index}" onclick="enableDiskClick(this);">启用本地磁盘</a></li>  
				                        </c:if> 
                                    </c:if> 
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
                                <label style="align:center;" id="confirmcontent">确定挂载此硬盘吗？</label>
                               </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green" id="confirm_btn"  onclick="toMountDisk();" data-dismiss="modal" aria-hidden="true">确定</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->                        
					
					
					 <div class="modal fade" id="modalForm" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:80%;margin-left:10%;">
                          <div class="modal-header">
                            <button id="my_close" type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>挂载共享存储</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form class="form-horizontal" id="addShareDiskForm" role="form"  method="post"
                                    <c:if test="${formPageFlag==0}"> action="<%=request.getContextPath() %>/cdrpm/addShareDisk"</c:if>
                                    <c:if test="${formPageFlag==1}"> action="<%=request.getContextPath() %>/computeresourcepool/addShareDisk"</c:if> >
								<input type="hidden" name="nodename" value="${nodename}">		                        
                            	<input type="hidden" name="disk_type" value="2"><!-- 2=共享存储/NAS -->	
                           	 
			                    <div class="form-group">
			                        <label for="input01" class="col-sm-2 control-label" style="width:180px;">路径标识名*</label>
			                        <div class="col-sm-4" style="width:200px;">
			                            <input type="text" name="name"  parsley-trigger="change" parsley-required="true" parsley-checkdesktoprecomputersourcepoolname="true" parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1"/>
			                        </div>
			                    </div>
		                      
			                    <div class="form-group">
			                        <label for="input01" class="col-sm-2 control-label" style="width:180px;">共享存储路径*</label>
			                        <div class="col-sm-4" style="width:200px;">	                        
			                            <input type="text"  name="path"  parsley-trigger="change" parsley-required="true" parsley-checkdesktoprecomputersourcepoolname="true" parsley-minlength="2" parsley-maxlength="100" parsley-validation-minlength="1"/>
			                        </div>
			                    </div>

			                    <div class="form-group">
			                        <label for="input01" class="col-sm-2 control-label" style="width:180px;">共享链接信息*</label>
			                        <div class="col-sm-4" style="width:200px;">	                        
			                            <input type="text"  name="crypt"  parsley-trigger="change" parsley-required="true" parsley-checkdesktoprecomputersourcepoolname="true" parsley-minlength="2" parsley-maxlength="100" parsley-validation-minlength="1"/>
			                        </div>
			                    </div>
                    		</form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green"  id="form_btn" onclick="saveForm();">确定</button>
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
   	<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/hostwarehouseform.js"></script>
    <script>
	    var path = '<%=request.getContextPath()%>'; 
	    var nodename = "";
	    var disk_type = "";
	    var index = "";
	    var formPageFlag = ${formPageFlag};
	    var isCommited = false;
	    
	    function unmountDiskClick(a){
	    	nodename = jQuery(a).attr("nodename");
	    	index = jQuery(a).attr("vindex");
	    	disk_type = jQuery(a).attr("disk_type");
	    	$("#confirmcontent").html("确定要取消挂载此硬盘吗？");
	    	if (formPageFlag==1){
	    		$("#confirm_btn").attr("onclick","unmountDiskPool();");
	    	} else{
	    		$("#confirm_btn").attr("onclick","unmountDiskServer();");
	    	}
	    	$("#con").click();    	
	    }
	    
	    function unmountDiskServer(){

	    	jQuery.ajax({
		        url: '<%=request.getContextPath()%>/cdrpm/unmount',
		        type: 'post', 
		        dataType: 'json',
		        data:{'nodename':nodename,'index':index,'disk_type':disk_type},
		        timeout: 10000,
		        error: function(){ 
		        },
		        success: function(result){  
		        	if(result.status=="success"){
		        		location.href = location.href;
		        	}
		        	else{
			        	$("#tipscontent").html(result.message);
						$("#dia").click();
		        	}
		        }
		        
		     });  
	    } 
	    
	    function unmountDiskPool(){

	    	jQuery.ajax({
		        url: '<%=request.getContextPath()%>/computeresourcepool/unmount',
		        type: 'post', 
		        dataType: 'json',
		        data:{'nodename':nodename,'index':index,'disk_type':disk_type},
		        timeout: 10000,
		        error: function(){ 
		        },
		        success: function(result){  
		        	if(result.status=="success"){
		        		location.href = location.href;
		        	}
		        	else{
			        	$("#tipscontent").html(result.message);
						$("#dia").click();
		        	}
		        }
		        
		     });  
	    }
	    
	    function mountDiskClick(a){
	    	nodename = jQuery(a).attr("nodename");
	    	disk_type = jQuery(a).attr("disk_type");
	    	index = jQuery(a).attr("vindex");    	
	    	$("#confirmcontent").html("确定要挂载此硬盘吗？");
	    	if (formPageFlag==1){
	    		$("#confirm_btn").attr("onclick","mountDiskPool();");
	    	} else{
	    		$("#confirm_btn").attr("onclick","mountDiskServer();");
	    	}
	    	$("#con").click();   
	
	    }  
    
	    function mountDiskServer(){
	    	jQuery.ajax({
		        url: '<%=request.getContextPath()%>/cdrpm/mount',
		        type: 'post', 
		        dataType: 'json',
		        data:{'nodename':nodename,'index':index,'disk_type':disk_type},
		        timeout: 10000,
		        error: function(){ 
		        },
		        success: function(result){  
		        	if(result.status=="success"){
		        		location.href = location.href;
		        	}
		        	else{
			        	$("#tipscontent").html(result.message);
						$("#dia").click();
		        	}
		        }
		     });  
	    } 

	    function mountDiskPool(){
	    	jQuery.ajax({
		        url: '<%=request.getContextPath()%>/computeresourcepool/mount',
		        type: 'post', 
		        dataType: 'json',
		        data:{'nodename':nodename,'index':index,'disk_type':disk_type},
		        timeout: 10000,
		        error: function(){ 
		        },
		        success: function(result){  
		        	if(result.status=="success"){
		        		location.href = location.href;
		        	}
		        	else{
			        	$("#tipscontent").html(result.message);
						$("#dia").click();
		        	}
		        }
		     });  
	    } 
	    
	    function enableDiskClick(a){
	    	index = jQuery(a).attr("vindex");
	    	nodename = jQuery(a).attr("nodename");	
	    	$("#confirmcontent").html("确定要启用此硬盘吗？");
	    	if (formPageFlag==1){
	    		$("#confirm_btn").attr("onclick","enableDiskPool();");
	    	} else{
	    		$("#confirm_btn").attr("onclick","enableDiskServer();");
	    	}
	    	$("#con").click();   	    	

	    }
	    
	    function enableDiskServer(){
	    	jQuery.ajax({
		        url: '<%=request.getContextPath()%>/cdrpm/enableDisk',
		        type: 'post', 
		        dataType: 'json',
		        data:{'nodename':nodename,'index':index},
		        timeout: 10000,
		        error: function(){ 
		        },
		        success: function(result){  
		        	if(result.status=="success"){
		        		location.href = location.href;
		        	}
		        	else{
			        	$("#tipscontent").html(result.message);
						$("#dia").click();
		        	}
		        }
		     });  
	    } 

	    function enableDiskPool(){
	    	jQuery.ajax({
		        url: '<%=request.getContextPath()%>/computeresourcepool/enableDisk',
		        type: 'post', 
		        dataType: 'json',
		        data:{'nodename':nodename,'index':index},
		        timeout: 10000,
		        error: function(){ 
		        },
		        success: function(result){  
		        	if(result.status=="success"){
		        		location.href = location.href;
		        	}
		        	else{
			        	$("#tipscontent").html(result.message);
						$("#dia").click();
		        	}
		        }
		     });  
	    } 	    

	    function disableDiskClick(a){
	    	index = jQuery(a).attr("vindex");
	    	nodename = jQuery(a).attr("nodename");	    	
	    	$("#confirmcontent").html("确定要禁用此硬盘吗？");
	    	if (formPageFlag==1){
	    		$("#confirm_btn").attr("onclick","disableDiskPool();");
	    	} else{
	    		$("#confirm_btn").attr("onclick","disableDiskServer();");
	    	}
	    	$("#con").click();   	    	
	    }	    
	    
	    function disableDiskServer(){
	    	jQuery.ajax({
		        url: '<%=request.getContextPath()%>/cdrpm/disableDisk',
		        type: 'post', 
		        dataType: 'json',
		        data:{'nodename':nodename,'index':index},
		        timeout: 10000,
		        error: function(){ 
		        },
		        success: function(result){  
		        	if(result.status=="success"){
		        		location.href = location.href;
		        	}
		        	else{
			        	$("#tipscontent").html(result.message);
						$("#dia").click();
		        	}
		        }
		     });  
	    } 


	    function disableDiskPool(){
	    	jQuery.ajax({
		        url: '<%=request.getContextPath()%>/computeresourcepool/disableDisk',
		        type: 'post', 
		        dataType: 'json',
		        data:{'nodename':nodename,'index':index},
		        timeout: 10000,
		        error: function(){ 
		        },
		        success: function(result){  
		        	if(result.status=="success"){
		        		location.href = location.href;
		        	}
		        	else{
			        	$("#tipscontent").html(result.message);
						$("#dia").click();
		        	}
		        }
		     });  
	    } 
	    
	    function saveForm(){
			if(isCommited){
	     		return false;
			 } 
			isCommited=true;
	    	
			jQuery.ajax({
		        url: path+'/main/checklogin',
		        type: 'post', 
		        dataType: 'json',
		        timeout: 10000,
		        async: true,
		        error: function()
		        {
		            alert('Error!');
		        },
		        success: function(result)
		        {
		        	if(result.status == "fail"){ 
		        		  isCommited = false;
		        		  $("#tipscontent").html("登录超时，请重新登录");
		     		      $("#dia").click();
			        	}else{ 
	 		        			var options = {
	 		        					success:function result(data){
	 		        						if(data.status == "fail"){
	 		        							 isCommited = false;
	 		        							 $("#modalForm").modal('hide');
								        		 $("#tipscontent").html(data.message);
								     		     $("#dia").click();  
	 		        						}else{  
	 		        							location.href = location.href;
	 		        						}
	 		        					},
	 		        					dataType:'json',
	 		        					timeout:10000
	 		        			};
	 		        			var form = jQuery("#addShareDiskForm");
	 		        			form.parsley('validate');
	 		        			if(form.parsley('isValid')){  		        				
				        			jQuery("#addShareDiskForm").ajaxSubmit(options); 
	 		        			}
			        	} 
		        }
		     }); 
			
		}	    
    </script>
  </body>
</html>
      

