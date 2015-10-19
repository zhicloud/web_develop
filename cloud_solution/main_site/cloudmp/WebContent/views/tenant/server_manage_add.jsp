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
            

            <h2><i class="fa fa-cogs"></i> 创建云服务器</h2>
            

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
                    <h3><a href="<%=request.getContextPath() %>/tenant/${id}/host"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>输入服务器配置信息</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <form class="form-horizontal" role="form" parsley-validate id="cloudserveraddform" action="<%=request.getContextPath() %>/tenant/${id}/host/add" method="post"   >
                      <input id="ports" value="" type="hidden" name="ports"/>
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">服务器资源池 *</label>
                        <div class="col-sm-4" id="selectpool">
							<select class="chosen-select chosen-transparent form-control" name="poolId" id="poolId" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectpool">
	                            <option value="">请选择资源池</option>  
	                            <c:forEach items="${computerPool }" var="sdi">
 	                                 <option value="${sdi.uuid }">${sdi.name }</option>
 	                             </c:forEach>  
	                          </select>                       
	                     </div>
                      </div>
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">配置类型 *</label>
                        <div class="col-sm-4" id="optiontype">
							<select class="chosen-select chosen-transparent form-control" name="chcmId" id="chcmId" parsley-trigger="change" parsley-required="true" parsley-error-container="#optiontype">
	                            <option value="">请选择配置类型</option>  
	                            <c:forEach items="${optionType }" var="ot">
 	                                 <option value="${ot.id }">${ot.name }</option>
 	                             </c:forEach>  
	                          </select>                       
	                     </div>
                      </div>
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">服务器名 *</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="displayName" name="displayName"  parsley-trigger="change" parsley-required="true" parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">开放端口</label>
                        <div class="col-sm-10" > 
 	                            <div class="col-sm-2"  style="padding:0 5px;">
	                            <select class="chosen-select chosen-transparent form-control" name="protocol" id="protocol" >
		                            <option>所有协议</option>
		                            <option>TCP</option>
		                            <option>UDP</option>
	                            </select> 
                          </div>
                          <div class="col-sm-2" style="padding:0 5px;">
							<input type="text" class="form-control" name="port" id="port" placeholder="端口号">  
  							</div> 
 							<div class="col-sm-2" style="padding:0 5px;">
                             <button class="btn btn-default" type="button" onclick="addPort();">添加</button>
 							</div>      
                          
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label class="col-sm-2 control-label"> </label>
                        <div class="col-sm-8" id="myproperlabel"> 

                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label class="col-sm-2 control-label">其他</label>
                        <div class="col-sm-8">
                          <div class="checkbox check-transparent">
                            <input type="checkbox" value="1" id="is_auto_startup" name="isAutoStartup" value="1" checked>
                            <label for="is_auto_startup">创建完自动开机</label>
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
 

      <script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>
      <script src="<%=request.getContextPath()%>/assets/js/vendor/no-ui-slider/jquery.nouislider.all.js"></script> 

    <script>
    
    var path = '<%=request.getContextPath()%>'; 

    $(function(){

      //chosen select input
      $(".chosen-select").chosen({disable_search_threshold: 10});
      
    //check all checkboxes
      $('table thead input[type="checkbox"]').change(function () {
        $(this).parents('table').find('tbody input[type="checkbox"]').prop('checked', $(this).prop('checked'));
      });
    
    //initialize slider
      $('#slider').noUiSlider({
    	  range: {
    		  'min': 10,
    		  'max': 500
    		},
        start: [10],  
        handles: 1,
        format: wNumb({
    		mark: ',',
    		decimals: 0
    	}),
        step:10
      });
    
      $('#slider').Link('lower').to($('#emptyDisk'));
      $('#slider').Link('lower').to($('#now'), 'html');
    
      
      
      
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
							        		  $("#tipscontent").html(data.message);
							     		      $("#dia").click();  		        							
 		        						}else{  		        							
// 	   		        						location.href = path + "/chcm/all";
 		        							window.location.href= path +"/tenant/${id}/host";  
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
    
  
  function addPort(){
	  var protocolText = $("#protocol").find("option:selected").text();
	    
		var protocol ;
		if(protocolText == '所有协议'){
			protocol = 0;
		}else if(protocolText == 'TCP'){
			protocol = 1
		}else if(protocolText == 'UDP'){
			protocol = 2
		}
		var port = $("#port").val();
		 
		if( port =="" )
		{
			return ;
		}
		if(!(/^[1-9][0-9]*$/.test(port))){
			$("#port").val("");
			$("#tipscontent").html("端口必须为数字");
		      $("#dia").click(); 
		      return;
		}
		// 判断是否已经有那个端口
		if( $("#myproperlabel").find("span.port_text[port='"+protocol + ":" + port+"']").size()==0 )
		{
			var item = $("<span style='padding:0 20px 0 0; white-space:nowrap; display:inline-block;'>\
				<span class='port_text' port='"+protocol + ":" + port+"'>" + protocolText + "：" + port + "</span><i class='fa fa-times'  onclick='$(this).parent().remove();'> </i>\
				</span>\
			"); 
			// 插入dom树
			item.appendTo("#myproperlabel");
			// 删除事件响应
			item.find("a.delete_port").click(function(){
				var portElement = this;
				top.$.messager.confirm('操作确认', '确定要删除吗?', function(result){
	                if( result ) {
	                	$(portElement).parent().remove();
	                }
	            });
			});
			$("#port").val(""); 
		}else{
			$("#tipscontent").html("端口已经存在");
		      $("#dia").click(); 
		}
  }
  
	//资源监控
	  function serverDiagramBtn(id){
	  	window.location.href = path+"/tenant/"+id+"/diagram";
	  }
      
    </script>
  </body>
</html>
      

      

      
 
