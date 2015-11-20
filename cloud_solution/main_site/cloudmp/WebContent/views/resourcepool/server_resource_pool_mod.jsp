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
                      <h3><a href="<%=request.getContextPath() %>/csrpm/all"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-arrow-left"></i></a>输入主机资源池信息</h3>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations" action="${pageContext.request.contextPath }/csrpm/mod" method="post">
                      <input name="uuid" type="hidden" value="${computeInfoExt.uuid}"/>
                      
                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">资源池名 *</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="name" name="name" value="${computeInfoExt.name}" oldname="${computeInfoExt.name}"  parsley-trigger="change" parsley-required="true" parsley-checkdesktoprecomputersourcepoolname="true" parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1">
                        </div>
                      </div>
                      
                      <%--<div class="form-group">--%>
                        <%--<label for="optionsRadios1" class="col-sm-2 control-label">网络类型 *</label>--%>
                        <%--<div class="col-sm-16">--%>
                          <%--<div class="radio radio-transparent col-md-3">--%>
                              <%--<input type="radio" name="networkType" id="optionsRadios0" value="0" onclick="networkCheck('optionsRadios0')">--%>
                            <%--<label for="optionsRadios0">私有云</label>--%>
                          <%--</div>--%>
                          <%--<div class="radio radio-transparent col-md-3">--%>
                              <%--<input type="radio" name="networkType" id="optionsRadios1" value="1" onclick="networkCheck('optionsRadios1')">--%>
                              <%--<label for="optionsRadios1">独享公网地址(IP)</label>--%>
                          <%--</div>--%>
                        <%--</div>--%>
                      <%--</div>--%>

                        <%--<div class="form-group">--%>
                            <%--<label for="optionsRadios3" class="col-sm-2 control-label"></label>--%>
                            <%--<div class="col-sm-10">--%>

                          <%--<div class="radio radio-transparent col-md-3">--%>
                              <%--<input type="radio" name="networkType" id="optionsRadios2" value="2" onclick="networkCheck('optionsRadios2')">--%>
                              <%--<label for="optionsRadios2">共享公网地址(端口)</label>--%>
                          <%--</div>--%>
                            <%--<div class="radio radio-transparent col-md-3">--%>
                                <%--<input type="radio" name="networkType" id="optionsRadios3" value="3" onclick="networkCheck('optionsRadios0')">--%>
                                <%--<label for="optionsRadios3">直连</label>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                      <%--</div>--%>


                        <div class="form-group">
                            <label for="optionsRadios1" class="col-sm-2 control-label">网络类型 *</label>
                            <div class="col-sm-10">
                                <div class="radio radio-transparent col-sm-3">
                                    <input type="radio" name="networkType" id="optionsRadios0" value="0" onclick="networkCheck('optionsRadios0')">
                                    <label for="optionsRadios0">私有云</label>
                                </div>
                                <div class="radio radio-transparent col-sm-3">
                                    <input type="radio" name="networkType" id="optionsRadios1" value="1" onclick="networkCheck('optionsRadios1')">
                                    <label for="optionsRadios1">独享公网地址(IP)</label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="optionsRadios3" class="col-sm-2 control-label"></label>
                            <div class="col-sm-10">

                                <div class="radio radio-transparent col-sm-3">
                                    <input type="radio" name="networkType" id="optionsRadios2" value="2" onclick="networkCheck('optionsRadios2')">
                                    <label for="optionsRadios2">共享公网地址(端口)</label>
                                </div>
                                <div class="radio radio-transparent col-sm-3">
                                    <input type="radio" name="networkType" id="optionsRadios3" value="3" onclick="networkCheck('optionsRadios3')">
                                    <label for="optionsRadios3">直连</label>
                                </div>
                            </div>
                        </div>

                      <input id="no_pool" type="hidden" name="networkId" value="">
                      <div class="form-group" id="ip_pool">
                        <label for="network_ip" class="col-sm-2 control-label">IP资源池*</label>
                        <div class="col-sm-4" id="ip_box">
                          <select class="chosen-select chosen-transparent form-control" name="network" id="network_ip" parsley-trigger="change" parsley-required="true" parsley-error-container="#ip_box" >
                            <option value="">请选择资源池</option>
                            <c:forEach items="${ipList }" var="ip">
                                <c:if test="${computeInfoExt.network == ip.uuid}">
                                    <option value="${ip.uuid }" selected="selected">${ip.name }</option>
                                </c:if>
                                <c:if test="${computeInfoExt.network != ip.uuid}">
                                    <option value="${ip.uuid }">${ip.name }</option>
                                </c:if>
                            </c:forEach>
                          </select>
                        </div>
                      </div>
                      <div class="form-group" id="port_pool">
                        <label for="network_port" class="col-sm-2 control-label">端口资源池*</label>
                        <div class="col-sm-4" id="port_box">
                          <select class="chosen-select chosen-transparent form-control" name="network" id="network_port" parsley-trigger="change" parsley-required="true" parsley-error-container="#port_box" >
                            <option value="">请选择资源池</option>
                            <c:forEach items="${portList }" var="port">
                                <c:if test="${computeInfoExt.network == port.uuid}">
                                    <option value="${port.uuid }" selected="selected">${port.name }</option>
                                </c:if>
                                <c:if test="${computeInfoExt.network != port.uuid}">
                                    <option value="${port.uuid }">${port.name }</option>
                                </c:if>
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
							     <input type="text" class="form-control" id="path" name="path" value="${path}" readonly="readonly" />
							</div>
						</div>                      
						<%--<div class="form-group">--%>
							<%--<label for="crypt" class="col-sm-2 control-label">连接信息</label>--%>
							<%--<div class="col-sm-4">--%>
							     <%--<input type="text" class="form-control" id="crypt" name="crypt" value="${computeInfoExt.crypt}" parsley-trigger="change" parsley-type="nochinese" parsley-maxlength="50" />--%>
							<%--</div>--%>
						<%--</div>   --%>
					</div>
					
                    <input id="no_disk" type="hidden" name="diskId" value="">
                        <%--<div class="form-group" id="disk_pool">--%>
                         <%--<label for="sys_image_id" class="col-sm-2 control-label">存储资源池*</label>--%>
                         <%--<div class="col-sm-4" id="selectbox">--%>
                           <%--<select class="chosen-select chosen-transparent form-control" name="diskId" id="sys_image_id" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox">--%>
                             <%--<option value="">请选择资源池</option>--%>
                             <%--<c:forEach items="${imageList }" var="sdi">--%>
                                  	<%--<c:if test="${sdi.realImageId == computeInfoExt.diskSource }">--%>
                                  		<%--<option value="${sdi.id }" selected="selected">${sdi.displayName }</option>--%>
                                  	<%--</c:if>--%>
                                 <%--<c:if test="${sdi.realImageId != computeInfoExt.diskSource }">--%>
                                     <%--<option value="${sdi.id }">${sdi.displayName }</option>--%>
                                 <%--</c:if>--%>
                              <%--</c:forEach>--%>
                           <%--</select>--%>
                         <%--</div>--%>
                      <%--</div>--%>

                        <%--<div class="form-group">--%>
                            <%--<label for="optionsRadios10" class="col-sm-2 control-label">开启高可用</label>--%>
                            <%--<div class="col-sm-16">--%>
                                <%--<div class="radio radio-transparent col-md-2">--%>
                                    <%--<input type="radio" name="mode0" id="mode00" value="0">--%>
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
                                    <input type="radio" name="mode1" id="mode10" value="0" >
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
                                    <input type="radio" name="mode2" id="mode20" value="0" >
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
                                    <%--<input type="radio" name="mode3" id="mode30" value="0" >--%>
                                    <%--<label for="mode30">否</label>--%>
                                <%--</div>--%>
                                <%--<div class="radio radio-transparent col-md-2">--%>
                                    <%--<input type="radio" name="mode3" id="mode31" value="1" >--%>
                                    <%--<label for="mode31">是</label>--%>
                                <%--</div>--%>
                            <%--</div>--%>
						<%--</div>--%>

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
                                            location.href = path + "/csrpm/all";
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
      

      

      
 
