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
            

            <h2><i class="fa fa-cogs"></i>创建主机资源池</h2>
            

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
                    <h3><a href="<%=request.getContextPath() %>/csrpm/all"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>输入主机资源池信息</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations" action="<%=request.getContextPath() %>/csrpm/add" method="post"   >
                      <input name="prefixion" type="hidden" value="server_pool_"/>
                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">资源池名 *</label>
                         <div class="col-sm-4">
                             <input type="text" class="form-control" id="name" name="name"  parsley-trigger="change" parsley-type="nochinese" parsley-required="true" parsley-checkdesktoprecomputersourcepoolname="true" parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1"/>
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="optionsRadios1" class="col-sm-2 control-label">网络类型 *</label>
                        <div class="col-sm-10">
                          <div class="radio radio-transparent col-sm-3">
                            <input type="radio" name="networkType" id="optionsRadios1" value="0" checked>
                            <label for="optionsRadios1">私有云</label>
                          </div>
                          <div class="radio radio-transparent col-sm-3">
                            <input type="radio" name="networkType" id="optionsRadios2" value="1">
                            <label for="optionsRadios2">独享公网地址(IP)</label>
                          </div> 
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="optionsRadios3" class="col-sm-2 control-label"></label>
                        <div class="col-sm-10">
                           
                          <div class="radio radio-transparent col-sm-3">
                            <input type="radio" name="networkType" id="optionsRadios3" value="2">
                            <label for="optionsRadios3">共享公网地址(端口)</label>
                          </div>
                          <div class="radio radio-transparent col-sm-3">
                            <input type="radio" name="networkType" id="optionsRadios4" value="3">
                            <label for="optionsRadios4">直连</label>
                          </div>
                        </div>
                      </div>
                      <input id="no_pool" type="hidden" name="network" value="">
                      <div class="form-group" id="ip_pool">
                        <label for="input07" class="col-sm-2 control-label">IP资源池*</label>
                        <div class="col-sm-4" id="selectbox_ip">
                          <select class="chosen-select chosen-transparent form-control" name="network" id="input07" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox_ip" >
                            <option value="">请选择资源池</option> 
                            <c:forEach items="${ipList }" var="ip">
                             		<option value="${ip.uuid }">${ip.name }</option>
                             </c:forEach>
                          </select>
                        </div>
                      </div>
                      <div class="form-group" id="port_pool">
                        <label for="input007" class="col-sm-2 control-label">端口资源池*</label>
                        <div class="col-sm-4" id="selectbox">
                          <select class="chosen-select chosen-transparent form-control" name="network" id="input007" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox" >
                            <option value="">请选择资源池</option> 
                            <c:forEach items="${portList }" var="port">
                             		<option value="${port.uuid }">${port.name }</option>
                             </c:forEach>  
                          </select>
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="optionsRadios10" class="col-sm-2 control-label">磁盘模式*</label>
                        <div class="col-sm-8"> 
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="diskType" id="optionsRadios10" value="0" onclick="$('#divNas').removeAttr('show');$('#divNas').attr('class','hidden');" checked>
                            <label for="optionsRadios10">本地</label>
                          </div>
                          <div class="radio radio-transparent col-md-3">
                            <input type="radio" name="diskType" id="optionsRadios11" value="1" onclick="$('#divNas').removeAttr('show');$('#divNas').attr('class','hidden');">
                            <label for="optionsRadios11">云存储</label>
                          </div>                          
                          <div class="radio radio-transparent col-md-3">
                            <input type="radio" name="diskType" id="optionsRadios12" value="2" onclick="$('#divNas').removeAttr('hidden');$('#divNas').attr('class','show');">
                            <label for="optionsRadios12">nas磁盘</label>
                          </div>                          
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="diskType" id="optionsRadios13" value="3" onclick="$('#divNas').removeAttr('show');$('#divNas').attr('class','hidden');">
                            <label for="optionsRadios13">ip san</label>
                          </div>                          
                        </div>
                      </div>
                      
                      <div id="divNas" class="hidden">
	                      <div class="form-group">
	                        <label for="path" class="col-sm-2 control-label">存储路径</label>
	                         <div class="col-sm-4">
	                             <input type="text" class="form-control" id="path" name="path" value="${path}" readonly="readonly"/>
	                        </div>
	                      </div>                      
	                      <%--<div class="form-group">--%>
	                        <%--<label for="path" class="col-sm-2 control-label">存储路径</label>--%>
	                         <%--<div class="col-sm-4">--%>
	                             <%--<input type="text" class="form-control" id="path" name="diskSource" />--%>
	                        <%--</div>--%>
	                      <%--</div>   --%>
                      </div>
                      <%--<div class="form-group">--%>
                         <%--<label for="optionsRadios10" class="col-sm-2 control-label">开启高可用</label>--%>
                         <%--<div class="col-sm-16">--%>
                             <%--<div class="radio radio-transparent col-md-2">--%>
                                 <%--<input type="radio" name="mode0" id="mode00" value="0" checked="checked">--%>
                                 <%--<label for="mode00">否</label>--%>
                             <%--</div>--%>
                             <%--<div class="radio radio-transparent col-md-2">--%>
                                 <%--<input type="radio" name="mode0" id="mode01" value="1">--%>
                                 <%--<label for="mode01">是</label>--%>
                             <%--</div>--%>
                         <%--</div>--%>
                      <%--</div>--%>

                      <div class="form-group">
                         <label for="optionsRadios10" class="col-sm-2 control-label">开启自动QoS调整</label>
                         <div class="col-sm-16">
                             <div class="radio radio-transparent col-md-2">
                                 <input type="radio" name="mode1" id="mode10" value="0" checked="checked">
                                 <label for="mode10">否</label>
                             </div>
                             <div class="radio radio-transparent col-md-2">
                                 <input type="radio" name="mode1" id="mode11" value="1" >
                                 <label for="mode11">是</label>
                             </div>
                         </div>
                      </div>
                     
                      <div class="form-group">
                         <label for="optionsRadios10" class="col-sm-2 control-label">开启thin provioning</label>
                         <div class="col-sm-16">
                             <div class="radio radio-transparent col-md-2">
                                 <input type="radio" name="mode2" id="mode20" value="0" checked="checked">
                                 <label for="mode20">否</label>
                             </div>
                             <div class="radio radio-transparent col-md-2">
                                 <input type="radio" name="mode2" id="mode21" value="1" >
                                 <label for="mode21">是</label>
                             </div>
                         </div>
					 </div>

                     <%--<div class="form-group">--%>
                         <%--<label for="optionsRadios10" class="col-sm-2 control-label">开启backing image</label>--%>
                         <%--<div class="col-sm-16">--%>
                             <%--<div class="radio radio-transparent col-md-2">--%>
                                 <%--<input type="radio" name="mode3" id="mode30" value="0" checked="checked">--%>
                                 <%--<label for="mode30">否</label>--%>
                             <%--</div>--%>
                             <%--<div class="radio radio-transparent col-md-2">--%>
                                 <%--<input type="radio" name="mode3" id="mode31" value="1" >--%>
                                 <%--<label for="mode31">是</label>--%>
                             <%--</div>--%>
                         <%--</div>--%>
					<%--</div>                         --%>
                    <input type="hidden" name="diskId" value="">
<!--                        <div class="form-group"> -->
<!--                         <label for="input07" class="col-sm-2 control-label">存储资源池*</label> -->
<!--                         <div class="col-sm-4" id="selectbox"> -->
<!--                           <select class="chosen-select chosen-transparent form-control" name="sysImageId"id="input07" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox"> -->
<!--                             <option value="">请选择资源池</option>  -->
<%--                             <c:forEach items="${imageList }" var="sdi"> --%>
<%--                                  	<c:if test="${sdi.realImageId!=null }"> --%>
<%--                                  		<option value="${sdi.id }">${sdi.displayName }</option> --%>
<%--                                  	</c:if> --%>
<%--                              </c:forEach>   --%>
<!--                           </select> -->
<!--                         </div> -->
<!--                       </div> -->

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
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="window.history.go(-1)">Close</button>
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

    <script>
    
    var path = '<%=request.getContextPath()%>'; 
    var isCommited = false;
    
    $(function(){
      $(".chosen-select").chosen({disable_search_threshold: 10});
      $("#port_pool").hide();
      $("#ip_pool").hide();
      $("#input07").attr("disabled",true);
	  $("#input007").attr("disabled",true);
      $("#optionsRadios1").click(function(){
    	  $("#port_pool").hide();
     	  $("#ip_pool").hide();
     	  $("#no_pool").attr("disabled",false);
     	  $("#input07").attr("disabled",true);
  	      $("#input007").attr("disabled",true);
       });
      $("#optionsRadios2,#optionsRadios4").click(function(){
    	  $("#port_pool").hide();
     	  $("#ip_pool").show();
     	  $("#no_pool").attr("disabled",true);
     	  $("#input07").attr("disabled",false);
   	      $("#input007").attr("disabled",true);
       });
      $("#optionsRadios3").click(function(){
    	  $("#port_pool").show();
    	  $("#ip_pool").hide(); 
    	  $("#no_pool").attr("disabled",true);
    	  $("#input07").attr("disabled",true);
    	  $("#input007").attr("disabled",false);
      });
      //chosen select input
    });
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
							        		  $("#tipscontent").html(data.message);
							     		      $("#dia").click();  		        							
 		        						}else{
 		        							$("#tipscontent").html("创建成功，将在5秒后刷新结果，请稍等...<br/>(若结果未刷新，请手动刷新页面)");
 		        			     		    $("#dia").click();
 		        			     		    window.setTimeout(function(){
 		        			     		    	location.href = path + "/csrpm/all";
 		        			     		    }, 5000);
// 	   		        						location.href = path + "/csrpm/all";
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
      

      

      
 
