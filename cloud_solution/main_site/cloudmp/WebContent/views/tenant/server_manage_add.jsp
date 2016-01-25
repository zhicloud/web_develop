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
							<select class="chosen-select chosen-transparent form-control" name="poolId" id="poolId" onChange="check();" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectpool">
	                            <option value="">请选择资源池</option>  
	                            <c:forEach items="${computerPool }" var="sdi">
 	                                 <option value="${sdi.uuid }">${sdi.name }</option>
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
                      <div class="form-group" id="allType">
                        <label for="input01" class="col-sm-2 control-label">配置类型 *</label>
                        <div class="col-sm-4" id="optiontype">
							<select class="chosen-select chosen-transparent form-control" style="width:230px;" name="chcmId" id="chcmId1" parsley-trigger="change" parsley-required="true" parsley-error-container="#optiontype">
	                            <option value="">请选择配置类型</option>  
	                            <c:forEach items="${optionType }" var="ot"> 
 	                                 <c:if test="${ot.fileType != 1 }">
 	                                 <option value="${ot.id }">${ot.name }</option>
 	                                 </c:if>
 	                             </c:forEach>  
	                          </select>  
	                          <a href="#" id="create_new_option_type1" flag="1" class="btn btn-greensea"><span>自定义配置</span></a>                     
	                     </div>
                      </div>
                      <div class="form-group" id="qcw2" >
                        <label for="input01" class="col-sm-2 control-label">配置类型 *</label>
                        <div class="col-sm-4" id="optiontype2">
							<select class="chosen-select chosen-transparent form-control" style="width:230px;" name="chcmId" id="chcmId2" parsley-trigger="change" parsley-required="true" parsley-error-container="#optiontype2en">
	                            <option value="">请选择配置类型</option>  
	                            <c:forEach items="${optionType }" var="ot">
	                                 <c:if test="${ot.fileType == 1 }">
 	                                 <option value="${ot.id }">${ot.name }</option>
 	                                 </c:if>
 	                             </c:forEach>  
	                          </select>  
	                          <a href="#" id="create_new_option_type2" flag="1" class="btn btn-greensea"><span>自定义配置</span></a>                     
	                     </div>
                      </div>
                      <div id="custom">
                      	<div class="form-group my_div">
                        <label for="input01" class="col-sm-2 control-label">CPU核心数 *</label>
                        <div class="col-sm-8">
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="cpuCore" id="optionsRadios1" value="1" checked>
                            <label for="optionsRadios1">1核</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="cpuCore" id="optionsRadios2" value="2">
                            <label for="optionsRadios2">2核</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="cpuCore" id="optionsRadios3" value="4">
                            <label for="optionsRadios3">4核</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="cpuCore" id="optionsRadios4" value="8">
                            <label for="optionsRadios4">8核</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="cpuCore" id="optionsRadios5" value="16">
                            <label for="optionsRadios5">16核</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="cpuCore" id="optionsRadios6" value="32">
                            <label for="optionsRadios6">32核</label>
                          </div> 
                        </div>
                      </div>
                      
                      <div class="form-group my_div">
                        <label for="input01" class="col-sm-2 control-label">内存 *</label>
                        <div class="col-sm-8"> 
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="memory" id="optionsRadios12" value="1" checked>
                            <label for="optionsRadios12">1GB</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="memory" id="optionsRadios13" value="2">
                            <label for="optionsRadios13">2GB</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="memory" id="optionsRadios14" value="4">
                            <label for="optionsRadios14">4GB</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="memory" id="optionsRadios15" value="8">
                            <label for="optionsRadios15">8GB</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="memory" id="optionsRadios16" value="16">
                            <label for="optionsRadios16">16GB</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="memory" id="optionsRadios17" value="32">
                            <label for="optionsRadios17">32GB</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="memory" id="optionsRadios18" value="64">
                            <label for="optionsRadios18">64GB</label>
                          </div>

                           
                        </div>
                      </div>
                      
                      <div class="form-group my_div">
                        <label for="input01" class="col-sm-2 control-label">系统磁盘</label>
                        <div class="col-sm-10">
                          <div class="radio radio-transparent col-md-8">
	                            <input type="radio" id="create_from_img" name="sysDiskType" value="from_sys_image" checked onclick="$('#sysImageId').removeAttr('disabled');">
	                            <label for="create_from_img" style="float:left">从镜像创建</label>
	                            <div class="col-sm-6" id="selectbox">
	                            <div id="image_divall">
	                            
	                            
	                            <select class="chosen-select chosen-transparent form-control" name="sysImageId" id="sysImageId1" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox">
	                            <option value="">请选择镜像</option> 
	                            <c:forEach items="${imageList }" var="sdi">
	                                 	<c:if test="${sdi.realImageId!=null }">
	                                 		<option value="${sdi.id }">${sdi.displayName }</option>
	                                 	</c:if>
	                             </c:forEach>   
	                           </select>
	                           </div>
	                           <div id="image_divthin">
	                           <select class="chosen-select chosen-transparent form-control" name="sysImageId" id="sysImageId2" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox">
	                            <option value="">请选择镜像</option> 
	                            <c:forEach items="${imageList }" var="sdi">
	                                 	<c:if test="${sdi.realImageId!=null && sdi.fileType == 1 }">
	                                 		<option value="${sdi.id }">${sdi.displayName }</option>
	                                 	</c:if>
	                             </c:forEach>   
	                           </select>
	                           </div>
                               </div> 
                          </div> 
                          
                        </div>
                      </div>
                        
                      
                      
                      
                      <div class="form-group my_div">
                        <label for="input01" class="col-sm-2 control-label"> </label>
                        <div class="col-sm-10">
                          
                          <div class="radio radio-transparent col-md-8">
                            <input type="hidden" name="emptyDisk" id="emptyDisk" value="10"/>
                            <input type="radio" id="empty_system" name="sysDiskType" value="from_empty" onclick="$('#sysImageId').attr('disabled','disabled');">
                            <label for="empty_system" style="float:left">空白系统</label>
                            <div class="col-sm-8" >10GB<div class="noUiSlider"  style="margin-left: 40px;"      id="slider"    ></div></div>500GB
                            <br><span id="now">10</span>GB
                          </div>
                          
                        </div>
                      </div>
                      
                      <div class="form-group my_div">
                        <label for="input01" class="col-sm-2 control-label">数据磁盘 *</label>
                        <div class="col-sm-8"> 
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="dataDisk" id="optionsRadios31" value="10" checked onclick="$('#diskdiy').attr('disabled','disabled');$('#diskdiy').val('')">
                            <label for="optionsRadios31">10G</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="dataDisk" id="optionsRadios32" value="20" onclick="$('#diskdiy').attr('disabled','disabled');$('#diskdiy').val('')">
                            <label for="optionsRadios32">20G</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="dataDisk" id="optionsRadios33" value="50" onclick="$('#diskdiy').attr('disabled','disabled');$('#diskdiy').val('')">
                            <label for="optionsRadios33">50G</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="dataDisk" id="optionsRadios34" value="100" onclick="$('#diskdiy').attr('disabled','disabled');$('#diskdiy').val('')">
                            <label for="optionsRadios34">100G</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="dataDisk" id="optionsRadios35" value="200" onclick="$('#diskdiy').attr('disabled','disabled');$('#diskdiy').val('')">
                            <label for="optionsRadios35">200G</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="dataDisk" id="optionsRadios36" value="500" onclick="$('#diskdiy').attr('disabled','disabled');$('#diskdiy').val('')">
                            <label for="optionsRadios36">500G</label>
                          </div>
                          <div class="radio radio-transparent col-md-2"  >
                            <input type="radio" name="dataDisk" id="optionsRadios37" value="" onclick="$('#diskdiy').removeAttr('disabled')">
                            <label for="optionsRadios37" style="float:left;min-width:64px;">自定义</label>
                          
                        
                          </div>
                       <div class="col-sm-3">
                          <input type="text" class="form-control" id="diskdiy" name="diskdiy" disabled="disabled" parsley-trigger="change" parsley-required="true"  parsley-type="integer" parsley-max="1000" parsley-validation-minlength="1">
                        </div>		                   
                        </div>
                      </div>
                      
                      <div class="form-group my_div">
                        <label for="input01" class="col-sm-2 control-label">带宽 *</label>
                        <div class="col-sm-8"> 
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="bandwidth" id="optionsRadios43" value="1" checked onclick="$('#bandwidthdiy').attr('disabled','disabled');$('#diskdiy').val('')">
                            <label for="optionsRadios43">1M</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="bandwidth" id="optionsRadios38" value="2" onclick="$('#bandwidthdiy').attr('disabled','disabled');$('#diskdiy').val('')">
                            <label for="optionsRadios38">2M</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="bandwidth" id="optionsRadios39" value="4" onclick="$('#bandwidthdiy').attr('disabled','disabled');$('#diskdiy').val('')">
                            <label for="optionsRadios39">4M</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="bandwidth" id="optionsRadios40" value="6" onclick="$('#bandwidthdiy').attr('disabled','disabled');$('#diskdiy').val('')">
                            <label for="optionsRadios40">6M</label>
                          </div>
                          <div class="radio radio-transparent col-md-4">
                            <input type="radio" name="bandwidth" id="optionsRadios41" value="10" onclick="$('#bandwidthdiy').attr('disabled','disabled');$('#diskdiy').val('')">
                            <label for="optionsRadios41">10M</label>
                          </div> 
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="bandwidth" id="optionsRadios42" value="" onclick="$('#bandwidthdiy').removeAttr('disabled')">
                            <label for="optionsRadios42" style="float:left;min-width:64px;">自定义</label>                         
                          </div>
                          <div class="col-sm-3">
                          <input type="text" class="form-control" id="bandwidthdiy" name=bandwidthdiy disabled="disabled" parsley-trigger="change" parsley-required="true"  parsley-type="integer" parsley-max="1000" parsley-validation-minlength="1">
                        </div>
                        </div>
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
                        <label for="input01" class="col-sm-2 control-label">是否支持H264 *</label>
                        <div class="col-sm-8"> 
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="supportH264" id="supportH264_1" value="0" checked>
                            <label for="supportH264_1">否</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="supportH264" id="supportH264_2" value="1">
                            <label for="supportH264_2">是</label>
                          </div>
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
    var diy_id = '';

    $(function(){

      //chosen select input
       $(".chosen-select").chosen({disable_search_threshold: 10});
      $("#custom").attr("disabled",true);
      $(".my_div").attr("disabled",true);
      $("#sysImageId").attr("disabled",true);
      $("#custom").hide();
      
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
      
      $("#create_new_option_type1").click(function(){
    	  var flag = $(this).attr("flag");
    	  if(flag=="1"){
			$("#chcmId1_chosen").css("display","none");
			$("#chcmId1").attr("disabled",true);
			$("#custom").attr("disabled",false);
			$(".my_div").attr("disabled",false);
			$("#sysImageId").attr("disabled",false);
			$("#custom").show();
			$("#create_new_option_type1").attr("flag","2");
			$("#create_new_option_type1").html("<span>取消自定义<span>");
			$("#image_divall").find("select").removeAttr("disabled");
			diy_id = "create_new_option_type1";
    	  }else{
    		$("#chcmId1_chosen").attr("style","width:230px;");
    		$("#chcmId1").attr("disabled",false);
  			$("#custom").attr("disabled",true);
  			$(".my_div").attr("disabled",true);
  			$("#sysImageId").attr("disabled",true);
  			$("#custom").hide();
  			$("#create_new_option_type1").attr("flag","1");
  			$("#create_new_option_type1").html("<span>自定义配置<span>");
  			$("#image_divall").find("select").attr("disabled","disabled");
  			diy_id = "";
    	  }
//   	  location.href = path+"/cscm/addserverpage";
    });
      
      $("#create_new_option_type2").click(function(){
    	  var flag = $(this).attr("flag");
    	  if(flag=="1"){
			$("#chcmId2_chosen").css("display","none");
			$("#chcmId2").attr("disabled",true);
			$("#custom").attr("disabled",false);
			$(".my_div").attr("disabled",false);
			$("#sysImageId").attr("disabled",false);
			$("#custom").show();
			$("#create_new_option_type2").attr("flag","2");
			$("#create_new_option_type2").html("<span>取消自定义<span>");
			$("#image_divthin").find("select").removeAttr("disabled");
			diy_id = "create_new_option_type2";
    	  }else{
    		$("#chcmId2_chosen").attr("style","width:230px;");
    		$("#chcmId2").attr("disabled",false);
  			$("#custom").attr("disabled",true);
  			$(".my_div").attr("disabled",true);
  			$("#sysImageId").attr("disabled",true);
  			$("#custom").hide();
  			$("#create_new_option_type2").attr("flag","1");
  			$("#create_new_option_type2").html("<span>自定义配置<span>");
      		$("#image_divthin").find("select").attr("disabled","disabled");
  			diy_id = "";
    	  }
//   	  location.href = path+"/cscm/addserverpage";
    });
      
      $("#allType").find("select").removeAttr("disabled");
		$("#allType").show();
		$("#qcw2").find("select").attr("disabled","disabled");
		$("#qcw2").hide(); 
		$("#image_divthin").find("select").attr("disabled","disabled");
		$("#image_divthin").hide();
		$("#image_divall").find("select").removeAttr("disabled");
		$("#image_divall").show();
    
      
      
      
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
	        	console.warn("can't get right infomation~try again")
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
	
	  function check(){
		  	var poolId = $("#poolId").val();
		  	if(poolId != ""){
		  		jQuery.ajax({
		              url: path+'/cloudserver/checkpoolisthin',
		              type: 'post',
		              data: 'id=' + poolId,
		              dataType: 'json', 
		              async: false,
		              error: function()
		              {
		                  alert('Error!');
		              },
		              success: function(result)
		              {
		            	  if(result.status == "success"){
		                		$("#qcw2").find("select").removeAttr("disabled");
		                		$("#qcw2").show();
		                		$("#allType").find("select").attr("disabled","disabled");
		                		$("#allType").hide();  
		                		$("#image_divall").find("select").attr("disabled","disabled");
		                		$("#image_divall").hide();
		                		$("#image_divthin").find("select").attr("disabled","disabled");
		                		$("#image_divthin").show();
		                	}else{
		                		$("#allType").find("select").removeAttr("disabled");
		                		$("#allType").show();
		                		$("#qcw2").find("select").attr("disabled","disabled");
		                		$("#qcw2").hide(); 
		                		$("#image_divthin").find("select").attr("disabled","disabled");
		                		$("#image_divthin").hide();
		                		$("#image_divall").find("select").attr("disabled","disabled");
		                		$("#image_divall").show();
		                	}
		              	$("#"+diy_id).click(); 
		              }
		           }); 
		  	}
		  	
		  }
      
    </script>
  </body>
</html>
      

      

      
 
