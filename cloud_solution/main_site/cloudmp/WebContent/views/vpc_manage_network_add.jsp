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
            

            <h2><i class="fa fa-cloud"></i> 添加端口配置</h2>
            

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
                    <h3>输入配置信息</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <form class="form-horizontal" id="networkform" role="form" action="${pageContext.request.contextPath }/vpc/addNetwork" method="post">
                      
                      <input type="hidden" name="vpcId" value="${vpcId }" />
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">外网IP*</label>
                        <div class="col-sm-4" id="selectouterip">
							<select class="chosen-select chosen-transparent form-control" name="outerIp" id="outerIp" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectouterip">
	                            <option value="">请选择外网IP</option>  
	                            <c:forEach items="${outerIpList }" var="oip">
 	                                 <option value="${oip.ip }">${oip.ip }</option>
 	                             </c:forEach>  
	                          </select>                       
	                     </div>
                      </div>
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">外网端口*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" name="outerPort" id="outerPort" parsley-trigger="change" parsley-required="true" parsley-type="integer" parsley-minlength="1" parsley-maxlength="5" parsley-validation-minlength="1">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">主机 *</label>
                        <div class="col-sm-4" id="selecthost">
							<select class="chosen-select chosen-transparent form-control" name="hostId" id="hostId" parsley-trigger="change" parsley-required="true" parsley-error-container="#selecthost">
	                            <option value="">请选择主机</option>  
	                            <c:forEach items="${hostList }" var="host">
 	                                 <option value="${host.id }">${host.displayName }</option>
 	                             </c:forEach>  
	                          </select>                       
	                     </div>
                      </div>
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">主机端口*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" name="port" id="port" parsley-trigger="change" parsley-required="true" parsley-type="integer" parsley-minlength="1" parsley-maxlength="5" parsley-validation-minlength="1">
                        </div>
                      </div>  
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">协议*</label>
                        <div class="col-sm-4" id="selectbox"> 
                           	<select name="protocol" class="chosen-select chosen-transparent form-control" id="protocol" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox">
                               <option value="0" selected="selected">全部</option>
                               <option value="1" >TCP</option>
                               <option value="2" >UDP</option>
                            </select> 
                        </div>
                      </div>
                        
                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8">
                          <button id="create_btn" type="button" class="btn btn-greensea" onclick="saveForm();"><i class="fa fa-plus"></i>
                              <span> 添 加 </span></button>
                          <button type="reset" class="btn btn-red"><i class="fa fa-refresh"></i>
                              <span> 重 置 </span></button>
                        </div>
                      </div>
                            
                    </form>

                  </div>
                  <!-- /tile body -->
                  
                


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
	
    <script type="text/javascript">
    var path = "<%=request.getContextPath()%>";
    var vpcId = "${vpcId}";
    var isCommited = false;
    $(function(){
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
	  
    })
      function saveForm(){
		if(isCommited){
     		return false;
		} 
		isCommited = true; 
		
		jQuery.ajax({
	        url: path+'/main/checklogin',
	        type: 'post', 
	        dataType: 'json',
	        timeout: 10000,
	        async: true,
	        error: function()
	        {
	        	isCommited = false;
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
							        		  $("#tipscontent").html(data.message);
							     		      $("#dia").click();  		        							
		        						}else{  
		        							window.location.href = path + "/vpc/" + vpcId +"/networkManage";
		        						}
 		        					},
 		        					dataType:'json',
 		        					timeout:10000
 		        			};
 		        			var form = jQuery("#networkform");
 		        			form.parsley('validate');
 		        			if(form.parsley('isValid')){  		        				
			        			jQuery("#networkform").ajaxSubmit(options); 
 		        			}
		        	} 
	        }
	     }); 
		
	}
    </script>
    
  </body>
</html>
      

