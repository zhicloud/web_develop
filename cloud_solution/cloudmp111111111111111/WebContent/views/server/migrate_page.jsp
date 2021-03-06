<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- migrate_page.jsp -->
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
            

            <h2><i class="fa fa-cogs"></i> 主机${host.displayName }迁移</h2>
            

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
                    <h3><a href="<%=request.getContextPath() %>/cloudserver/all"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>输入迁移信息</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <form class="form-horizontal" role="form" parsley-validate id="cloudserveraddform" action="<%=request.getContextPath() %>/cloudserver/migrate" method="post"   >
                      <input id="hostId" value="${host.id }" type="hidden" name="hostId"/>
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">迁移说明</label>
                        <div class="col-sm-4" id="selectpool">
							 ${description }                    
	                     </div>
                      </div>
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">目标资源*</label>
                        <div class="col-sm-4" id="selectpool">
							<select class="chosen-select chosen-transparent form-control" name="nodeName" id="nodeName" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectpool">
	                            <option value="">请选择nc</option>  
	                            <c:forEach items="${cList }" var="sdi">
 	                                 <option value="${sdi.name }">${sdi.name }</option>
 	                             </c:forEach>  
	                          </select>                       
	                     </div>
                      </div>
                       
                         
                    
                      
                       
                      

                       

                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8">
                          <button type="button" class="btn btn-greensea" onclick="saveForm();"><i class="fa fa-plus"></i>
                              <span> 迁  移 </span></button>
                          <button type="reset" class="btn btn-red" onclick="window.location.reload();"><i class="fa fa-refresh"></i>
                              <span> 重 置 </span></button>
                        </div>
                      </div>
                            
                    </form>
                    
                    <div class="progress-list" id="progress_div">
                      <div class="details">
                        <div class="title"><strong id="tips"></strong></div>
                       </div>
                      <div class="status pull-right">
                        <span class="animate-number" id="progress1"  data-value="0" data-animation-duration="1500">0</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little no-radius">
                        <div class="progress-bar progress-bar-orange animate-progress-bar" id="progress2" data-percentage="0%"></div>
                      </div>
                    </div>

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
 

      <script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>
      <script src="<%=request.getContextPath()%>/assets/js/vendor/no-ui-slider/jquery.nouislider.all.js"></script> 

    <script>
    
    var path = '<%=request.getContextPath()%>'; 
    var isMigrate = '${isMigrate}';

    $(function(){
    	
      //chosen select input
      $(".chosen-select").chosen({disable_search_threshold: 10});
      if(isMigrate == 'true') {
			refreshMigrateProgress();
			hideElement("cloudserveraddform"); 
		}else{
			hideElement("progress_div");
		}
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
 		        		var myports=new Array();
		        		var ports = "";
		        		myports = $("#myproperlabel").find("span.port_text");
		        		for(i=0;i<myports.length;i++){
		        			if(i != myports.length-1){
		        				ports = ports + $(myports[i]).attr("port")+",";
		        			}else{
		        				ports = ports + $(myports[i]).attr("port");
		        			}
		        			
		        		}
		        		$("#ports").val(ports);
 		        			var options = {
 		        					success:function result(data){
 		        						console.info(data);
 		        						if(data.status == "fail"){
							        		  $("#tipscontent").html("创建失败");
							     		      $("#dia").click();  		        							
 		        						}else{  		        							
// 	   		        						location.href = path + "/chcm/all";
 		        							window.location.href= path +"/cloudserver/all";  
 		        						}
 		        					},
 		        					 error: function(data){  
 		        						$("#tipscontent").html("创建失败");
						     		      $("#dia").click();//失败时的处理方法  
 		        					     },
 		        					dataType:'json',
 		        					timeout:10000
 		        			};
 		        			var form = jQuery("#cloudserveraddform");
 		        			form.parsley('validate');
 		        			if(form.parsley('isValid')){  		        				
			        			jQuery("#cloudserveraddform").ajaxSubmit(options); 
 		        			}
		        	} 
	        }
	     }); 
		
	}
    
    function refreshMigrateProgress(){
    	jQuery.ajax({
    		url: path+'/cloudserver/getmigrateprogress',
    		type: 'get',
    		data: 'uuid=' + '${host.id}',
    		dataType: 'json', 
    		async: false,
    		error: function(result)
    		{ 
    		},
    		success: function(result)
    		{  
    			 if(result.status == "success") 
    				{    
   						if(result.properties.progress <= 100){ 
  							   $("#progress2").attr("data-percentage",result.properties.progress+"%"); 
  							   $("#progress2").css("width",result.properties.progress+"%"); 
  							   $("#progress1").html(result.properties.progress+""); 
  							   $("#progress1").attr("data-value",result.properties.progress);
  							   window.setTimeout(self.refreshMigrateProgress, 5000);  							 
   						 }    				
    				}
    			 else  {
    				 window.location.href = path+"/cloudserver/all";
    			 }
    		}
    	}); 
    		
    }
    
    function hideElement(targetid){
        if (document.getElementById){
            target=document.getElementById(targetid);
            target.style.display="none";
        }
    }
    
  
   
      
    </script>
  </body>
</html>
      

      

      
 
