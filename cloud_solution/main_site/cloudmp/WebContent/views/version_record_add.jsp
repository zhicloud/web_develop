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
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/summernote/css/summernote.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/summernote/css/summernote-bs3.css">
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
            

            <h2><i class="fa fa-book"></i> 发布升级版本</h2>
            

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
                    <h3><a href="<%=request.getContextPath() %>/version/list"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>输入版本信息</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations" action="<%=request.getContextPath() %>/version/add" method="post"    >
                      
                      <div class="form-group">
                        <label for="versionNumber" class="col-sm-2 control-label">版本名称 *</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" name="versionNumber" id="versionNumber"  parsley-trigger="change" parsley-required="true" parsley-checkversionname="true" parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1">
                           <span style="font-size:50%;">
                                                                版本名称规则：<br>
                                                                  客户缩写(如ZS)_数字版本号_CPU架构(如ARM,X86)_硬件厂家名称缩写（如SD，XH）共四部分组成;
						  <br>   
						     例：ZS_1.1.1_X86_SD，不能有中文和特殊符号，英文字母均为全大写。
                           </span> 
						
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="platformType" class="col-sm-2 control-label">平台类型*</label>
                        <div class="col-sm-4" id="selectbox">
                          <select class="chosen-select chosen-transparent form-control" name="platformType" id="platformType" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox">
                            <option value="">请选择平台类型</option>  
                            <option value="X86">X86</option>
                            <option value="ARM">ARM</option>
                               
                          </select>
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="hardwareCompany" class="col-sm-2 control-label">硬件产商*</label>
                        <div class="col-sm-4" id="selectbox1">
                          <select class="chosen-select chosen-transparent form-control" name="hardwareCompany" id="hardwareCompany" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox1">
                            <option value="">请选择硬件产商</option>  
                            <option value="XH">小海XH</option>
                            <option value="SD">思叠SD</option>
                               
                          </select>
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="filename" class="col-sm-2 control-label">版本上传*</label>
                        <div class="col-sm-4">
                          <div class="input-group">
                          <span class="input-group-btn">
                            <span class="btn btn-primary btn-file">
                              <i class="fa fa-upload"></i><input id="filename" type="file" name="filename"  multiple="*.tgz">
                            </span>
                          </span>
                          <input type="text" class="form-control" readonly="">
                        </div>
                        </div>
                      </div>
                         
                     <div class="form-group">
                        <label for="updateInfo" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-4">
                          <textarea class="form-control" name="updateInfo" id="updateInfo" rows="6" parsley-maxlength="100"></textarea>
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


 
    
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>
    
    <script src="<%=request.getContextPath()%>/assets/js/vendor/summernote/summernote.min.js"></script>

    <script>
    
    var path = '<%=request.getContextPath()%>'; 
    var filetype = "";
    var isGetFile = false;
    var isCommited = false;
    $(document)
    .on('change', '.btn-file :file', function() {
      var input = $(this),
      numFiles = input.get(0).files ? input.get(0).files.length : 1,
      label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
      input.trigger('fileselect', [numFiles, label]);
      filetype = this.value.substr(this.value.lastIndexOf('.')).toLowerCase();
      isGetFile = true;
  });

    $(function(){

      //chosen select input
      $(".chosen-select").chosen({disable_search_threshold: 10});
      $('.btn-file :file').on('fileselect', function(event, numFiles, label) {
          
          var input = $(this).parents('.input-group').find(':text'),
              log = numFiles > 1 ? numFiles + ' files selected' : label;

              console.log(log);
          
          if( input.length ) {
            input.val(log);
          } else {
            if( log ) alert(log);
          }
          
        });
      
      
      
    });
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
	            alert('Error!');
	        },
	        success: function(result)
	        {
	        	if(result.status == "fail"){ 
	        		isCommited = false;
	        		  $("#tipscontent").html("登录超时，请重新登录");
	     		      $("#dia").click();
		        	}else{
		        		  if(beforeSubmit()){		        			  
	   		        			var options = {
	   		        					success:function result(data){
	   		        						if(data.status == "fail"){
	   		        							isCommited = false;
								        		  $("#tipscontent").html("创建失败");
								     		      $("#dia").click();  		        							
	   		        						}else{  		        							
		   		        						location.href = path + "/version/list";
	   		        						}
	   		        					},
	   		        					dataType:'json',
	   		        					timeout:1000000
	   		        			};
	   		        			var form = jQuery("#basicvalidations");
	   		        			form.parsley('validate');
	   		        			if(form.parsley('isValid')){  		        				
				        			jQuery("#basicvalidations").ajaxSubmit(options); 
	   		        			}
		        		  }
 		        	} 
	        }
	     }); 
		
	}
    
    function beforeSubmit(){
    	if(isGetFile == false){
    		isCommited = false;
    		$("#tipscontent").html("未选择文件");
		      $("#dia").click();
    		return false;
    	}
     	if(filetype != ".tgz"){
     		isCommited = false;
    		$("#tipscontent").html("文件格式不正确");
		      $("#dia").click();
    		return false;
    		
    	}
    	
    	return true;
    }
      
    </script>
  </body>
</html>
      

      

      
 
