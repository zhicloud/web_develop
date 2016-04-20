<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- tenant_modify.jsp -->
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
    
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/no-ui-slider/css/jquery.nouislider.min.css">

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
            

            <h2><i class="fa fa-cogs"></i>修改配额</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              <div class="col-md-12">



                <!-- tile -->
                <section class="tile color transparent-black">


                  <!-- tile body -->
                  <div class="tile-body">
                    
                     <form class="form-horizontal" role="form" parsley-validate id="basicvalidations" action="<%=request.getContextPath() %>/tenant/modify" method="post"   >
                    
                     <div class="form-group">
                         <label for="input01" class="col-sm-2 control-label">租户名称 *</label>
                         <div class="col-sm-4">
                             <input type="hidden" id="id" name="id" value="${tenant.id}" >
                             <input type="text" class="form-control" id="name" name="name" value="${tenant.name}" parsley-trigger="change" parsley-required="true" parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1">
                         </div>
                     </div>  
                                            
                     <div class="form-group">
                         <label for="input01" class="col-sm-2 control-label">描述 *</label>
                         <div class="col-sm-4">
                         	<input type="text" class="form-control" id="remark" name="remark" value="${tenant.remark}"  parsley-trigger="change" parsley-required="true" parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1">
                         </div>
                     </div>  
                     
                     <div class="form-group">
                         <label for="input01" class="col-sm-2 control-label">VCPU*</label>
                         <div class="col-sm-4">
                             <input type="text" class="form-control" id="cpu" name="cpu" value="${tenant.cpu}" parsley-trigger="change" parsley-required="true" parsley-type="integer" parsley-max="100" parsley-validation-minlength="1">
                         </div>
                     </div>    
                                        
                     <div class="form-group">
                         <label for="input01" class="col-sm-2 control-label">内存(GB)*</label>
                         <div class="col-sm-4">
                             <input type="text" class="form-control" id="mem" name="mem" value="${tenant.memStr}" parsley-trigger="change" parsley-required="true" parsley-type="integer" parsley-max="1000" parsley-validation-minlength="1">
                         </div>
                     </div>              

                     <div class="form-group">
                         <label for="input01" class="col-sm-2 control-label">磁盘(TB)*</label>
                         <div class="col-sm-4">
                             <input type="text" class="form-control" id="disk" name="disk" value='${tenant.diskStr}'  parsley-trigger="change" parsley-required="true" parsley-type="number" parsley-max="1000" parsley-validation-minlength="1">
                         </div>
                     </div>   
                     
                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8">
                          <button type="button" class="btn btn-greensea" onclick="saveForm();"><i class="fa fa-edit"></i>
                              <span> 修 改</span></button>
                          <button type="reset" class="btn btn-default" onclick="backhome();"><i class="fa fa-refresh"></i>
                              <span> 返 回</span></button>
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


      <script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>

    <script>
        var isCommited = false;
 	    //返回
	    function backhome(){
	    	window.location.href = "<%=request.getContextPath()%>/tenant/all";
	    }
    
	    $(function(){
		      //chosen select input
		     $(".chosen-select").chosen({disable_search_threshold: 10});
	    });
	     
	    function saveForm(){
			if(isCommited){
	     		return false;
			} 
			isCommited = true;
	    	
	    	
			jQuery.ajax({
		        url: '<%=request.getContextPath()%>/main/checklogin',
		        type: 'post', 
		        dataType: 'json',
		        timeout: 10000,
		        async: true,
		        error: function(){
		            alert('Error!');
		        },
		        success: function(result){ 
		        	if(result.status == "fail"){ 
		        		  $("#tipscontent").html("登录超时，请重新登录");
		     		      $("#dia").click();
		     		     isCommited = false;
			        	}else{ 
	 		        			var options = {
	 		        					success:function result(data){
	 		        						if(data.status == "fail"){
								        		  $("#tipscontent").html(data.message);
								     		      $("#dia").click();
								     		     isCommited = false;
	 		        						}else{  		        							
		   		        						location.href = "<%=request.getContextPath()%>/tenant/all";
	 		        						}
	 		        					},
	 		        					dataType:'json',
	 		        					timeout:10000
	 		        			};
				        		jQuery("#basicvalidations").ajaxSubmit(options); 
			        	} 
		        }
		     }); 
			
		}
      
    </script>
  </body>
</html>
      

      

      
 
