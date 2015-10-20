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
            

            <h2><i class="fa fa-cogs"></i>编辑主机资源池</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              <div class="col-md-12">



                <!-- tile -->
                <section class="tile color transparent-black">



                  <!-- tile header -->
                  <div class="tile-header">
                      <h3><a href="<%=request.getContextPath() %>/mypaltform/service"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-arrow-left"></i></a>输入主机资源池信息</h3>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations" action="${pageContext.request.contextPath }/mypaltform/service/mod" method="post">

                      <div class="form-group">
                        <label for="target" class="col-sm-2 control-label">服务名 *</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="target" name="target" value="${service_info.name}" parsley-trigger="change" parsley-required="true" parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1">
                        </div>
                      </div>
                      
					<div class="form-group">
					    <label for="optionsRadios10" class="col-sm-2 control-label">磁盘模式*</label>
					    <div class="col-sm-8">  
					        <div class="radio radio-transparent col-md-2">
					        	<input type="radio" name="diskType" id="optionsRadios10" value="0" onclick="$('#divNas').removeAttr('show');$('#divNas').attr('class','hidden');" checked>
					       		<label for="optionsRadios10">本地</label>
					     	</div>
					     	<div class="radio radio-transparent col-md-2">
					       		<input type="radio" name="diskType" id="optionsRadios11" value="1" onclick="$('#divNas').removeAttr('show');$('#divNas').attr('class','hidden');">
					       		<label for="optionsRadios11">共享存储</label>
					     	</div>                          
					   	</div>
					</div>
                      
					<div id="divNas" class="hidden">
						<div class="form-group">
						<label for="path" class="col-sm-2 control-label">存储路径</label>
							<div class="col-sm-4">
							     <input type="text" class="form-control" id="path" name="path" value="${computeInfoExt.path}" parsley-trigger="change" parsley-type="nochinese" parsley-maxlength="50" />
							</div>
						</div>
					</div>
					
                    <input id="no_disk" type="hidden" name="diskId" value="">

                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8">
                          <button type="button" class="btn btn-greensea" onclick="saveForm();"><i class="fa fa-plus"></i>
                              <span> 保 存 </span></button>
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
    
    var path = '<%=request.getContextPath()%>';

    $(function(){
      //chosen select input
        $(".chosen-select").chosen({disable_search_threshold: 10});

    });

    $(function(){
        $("#port_pool").hide();
        $("#ip_pool").hide();
        $("#network_ip").attr("disabled",true);
        $("#network_port").attr("disabled",true);
        $("#disk_pool").hide();
        $("#optionsRadios"+${computeInfoExt.networkType}).click();
        $("#optionsRadios1"+${computeInfoExt.diskType}).click();
        $("#mode0"+${computeInfoExt.mode0}).attr("checked", true);
        $("#mode1"+${computeInfoExt.mode1}).attr("checked", true);
        $("#mode2"+${computeInfoExt.mode2}).attr("checked", true);
        $("#mode3"+${computeInfoExt.mode3}).attr("checked", true);
        if($('#optionsRadios12').is(':checked')) {
        	$("#divNas").show();
        }
        <%--$("#option"+${computeInfoExt.option}).attr("checked", true);--%>
    });

    function networkCheck(id) {
        if (id == 'optionsRadios0') {
            $("#port_pool").hide();
            $("#ip_pool").hide();
            $("#no_pool").attr("disabled",false);
            $("#network_ip").attr("disabled",true);
            $("#network_port").attr("disabled",true);
        }
        if (id == 'optionsRadios1' ) {
            $("#port_pool").hide();
            $("#ip_pool").show();
            $("#no_pool").attr("disabled",true);
            $("#network_ip").attr("disabled",false);
            $("#network_port").attr("disabled",true);
        }
        if (id == 'optionsRadios2') {
            $("#port_pool").show();
            $("#ip_pool").hide();
            $("#no_pool").attr("disabled",true);
            $("#network_ip").attr("disabled",true);
            $("#network_port").attr("disabled",false);
        }
    }

    function diskCheck(id) {
        if (id == 'optionsRadios10') {
            $("#disk_pool").hide();
            $("#no_pool").attr("disabled",false);
            $("#sys_image_id").attr("disabled",true);
        }
        if (id == 'optionsRadios11' ) {
            $("#disk_pool").show();
            $("#no_pool").attr("disabled",true);
            $("#sys_image_id").attr("disabled",false);
        }
    }

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
                                            $("#tipscontent").html(data.message);
							     		      $("#dia").click();  		        							
 		        						}else{
                                            location.href = path + "/cdrpm/all";
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
      

      

      
 
