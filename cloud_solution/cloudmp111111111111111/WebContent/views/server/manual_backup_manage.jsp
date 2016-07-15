<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- manual_backup_manage.jsp -->
 <html>
  <head>
    <title>控制台-${productName}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8" />

    <link rel="icon" type="image/ico" href="<%=request.getContextPath()%>/assets/images/favicon.ico" />
    <!-- Bootstrap -->
    <link href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap/bootstrap.min.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/vendor/animate/animate.css">
    <link type="text/css" rel="stylesheet" media="all" href="<%=request.getContextPath()%>/assets/js/vendor/mmenu/css/jquery.mmenu.all.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/videobackground/css/jquery.videobackground.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap-checkbox.css">

    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen-bootstrap.css">

    <link href="<%=request.getContextPath()%>/assets/css/zhicloud.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
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
            

            <h2><i class="fa fa-server"></i> 手动备份</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              <div class="col-md-12">



                <!-- tile -->
                <section class="tile color transparent-black">

				<!-- tile widget -->
                  <div class="tile-widget color transparent-black rounded-top-corners nopadding nobg">
                    <!-- Nav tabs -->
                    <ul class="nav nav-tabs tabdrop">
                      <li ><a href="#users-tab" data-toggle="tab" onclick="window.location.href='<%=request.getContextPath() %>/csbackresume/detailmanage';">备份记录</a></li>
                      <li><a href="#orders-tab" onclick="window.location.href='<%=request.getContextPath() %>/csbackresume/dtsettimebackup/manage';" data-toggle="tab">定时备份管理</a></li>
                      <li><a href="#messages-tab" onclick="window.location.href='<%=request.getContextPath() %>/csbackresume/desktopbackuptimer/manage';" data-toggle="tab">定时备份主机管理</a></li>
                      <li class="active"><a href="#tasks-tab" data-toggle="tab" onclick="window.location.href='<%=request.getContextPath() %>/csbackresume/manualbackup';">手动备份</a></li>
                      <div id="space"></div>
                      
                     </ul>
                    <!-- / Nav tabs -->
                  </div>
                  <!-- /tile widget -->

                  <!-- tile header -->
                  <div class="tile-header">
                    <h3> 输入备份信息</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations" action="<%=request.getContextPath() %>/csbackresume/manualbackup" method="post"   >
                      
                       
                       
                        
                      
                      <div class="form-group">
                        <label for="input07" class="col-sm-2 control-label">主机选择*</label>
                        <div class="col-sm-4" id="selectbox">
                          <select class="chosen-select chosen-transparent form-control" name="id" id="fromHostId" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox">
                            <option value="">请选择主机</option> 
                            <c:forEach items="${hostList }" var="sdi">
                                 	<c:if test="${sdi.realHostId!=null }">
                                 		<option value="${sdi.realHostId }">${sdi.displayName }</option>
                                 	</c:if>
                             </c:forEach>   
                          </select>
                        </div>
                      </div>
                     <div class="form-group">
	                        <label for="input01" class="col-sm-2 control-label">备份类型</label>
	                        <div class="col-sm-10">
	                          <div class="radio radio-transparent col-md-2">
	                            <input type="radio" name="mode"   id="mode0" value="0" onclick="$('#choose_disk').hide();">
	                            <label for="mode0">全备份</label>
	                          </div>
	                          <div class="radio radio-transparent col-md-2">
	                            <input type="radio" name="mode"   id="mode1" value="1" onclick="$('#choose_disk').show();" checked>
	                            <label for="mode1">部分备份</label>
	                          </div> 
	                        </div>
	                      </div>
			                      
	                      <div class="form-group" id="choose_disk">
	                        <label for="input01" class="col-sm-2 control-label">备份盘选择</label>
	                        <div class="col-sm-10">
	                          <div class="radio radio-transparent col-md-2">
	                            <input type="radio" name="disk"   id="disk0" value="0" >
	                            <label for="disk0">系统盘</label>
	                          </div>
	                          <div class="radio radio-transparent col-md-2">
	                            <input type="radio" name="disk"   id="disk1" value="1" checked>
	                            <label for="disk1">数据盘</label>
	                          </div> 
	                        </div>
	                      </div> 
                      
                     
                      
                       
                      

                       

                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8">
                          <button type="button" class="btn btn-greensea" onclick="saveForm();"><i class="fa fa-plus"></i>
                              <span> 创 建 </span></button>
                          <button type="reset" class="btn btn-red" onclick="window.location.reload();"><i class="fa fa-refresh"></i>
                              <span> 重 置 </span></button>
                        </div>
                      </div>
                            
                    </form>

                  </div>
                  <!-- /tile body -->
                  
                  
                  
                


                </section>
                <!-- /tile -->
                
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
                                <label style="align:center;" id="confirmcontent">确定要删除该镜像吗？</label>
                               </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green"   onclick="toDelete();" data-dismiss="modal" aria-hidden="true">确定</button>
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


 

    <script>
    
    var path = '<%=request.getContextPath()%>'; 

    $(function(){

      //chosen select input
      $(".chosen-select").chosen({disable_search_threshold: 10});
      
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
    function saveForm(){
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
	        		  $("#tipscontent").html("登录超时，请重新登录");
	     		      $("#dia").click();
		        	}else{ 
   		        			var options = {
   		        					success:function result(data){
   		        						if(data.status == "fail"){
							        		  $("#tipscontent").html("创建失败");
							     		      $("#dia").click();  		        							
   		        						}else{  		        							
	   		        						window.location.href=path+"/csbackresume/detailmanage";
   		        						}
   		        					},
   		        					dataType:'json',
   		        					timeout:10000
   		        			};
   		        			var form = jQuery("#basicvalidations");
   		        			form.parsley('validate');
   		        			if(form.parsley('isValid')){  		        				
			        			jQuery("#basicvalidations").ajaxSubmit(options); 
   		        			}
 		        	} 
	        }
	     }); 
		
	}
      
    </script>
  </body>
</html>
      

      

      
 