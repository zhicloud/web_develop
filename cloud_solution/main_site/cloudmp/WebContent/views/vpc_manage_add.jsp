<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- vpc_manage_add.jsp -->
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
            

            <h2><i class="fa fa-cloud"></i> 添加专属云</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">
            <!-- row -->
            <div class="row">

              <!-- col 12 -->
              <div class="col-md-12">
                <!-- tile -->
                <section id="rootwizard" class="tabbable transparent tile">
                  <!-- tile header -->
                  <div class="tile-header transparent">
                    <h1>输入专属云信息</h1>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                      <a href="#" class="remove"><i class="fa fa-times"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile widget -->
                  <div class="tile-widget nopadding color transparent-black rounded-top-corners">
                    <ul>
                      <li><a href="#tab1" data-toggle="tab">基本信息</a></li>
                      <li><a href="#tab2" data-toggle="tab">主机选择</a></li>
                      <li><a href="#tab3" data-toggle="tab">创建进度</a></li>
<!--                       <li><a href="#tab3" data-toggle="tab">EULA</a></li> -->
                    </ul>
                  </div>
                  <!-- /tile widget -->

                  <!-- tile body -->
                  <div class="tile-body" style="background-color: rgb(225, 163, 155);">
                    
                    <div id="bar" class="progress progress-striped active">
                      <div class="progress-bar progress-bar-cyan animate-progress-bar"></div>
                    </div>

                    <div class="tab-content">
                      
                      <div class="tab-pane" id="tab1">
                        <form class="form-horizontal form1" role="form" parsley-validate>
                      
                          <div class="form-group">
                            <label for="displayName" class="col-sm-2 control-label">专属云名称 *</label>
                            <div class="col-sm-10">
                              <input type="text" class="form-control" id="displayName" parsley-trigger="change" parsley-required="true" parsley-minlength="4" parsley-validation-minlength="1">
                            </div>
                          </div>

                          <div class="form-group">
                            <label for="ipAmount" class="col-sm-2 control-label">申请IP个数 *</label>
                            <div class="col-sm-10">
                              <input type="text" class="form-control" id="ipAmount" name="ipAmount" parsley-trigger="change" parsley-required="true" parsley-max="100" parsley-minlength="1" parsley-type="number" parsley-validation-minlength="1">
                            </div>
                          </div>

                            <div class="form-group">
                                <label for="group_id" class="col-sm-2 control-label">所属分组</label>
                                <div class="col-sm-4" id="selectbox">
                                    <select class="chosen-select chosen-transparent form-control" id = "group_id" name="groupId" parsley-error-container="#selectbox">
                                        <option value="">请选择群组</option>
                                        <c:forEach items="${sys_group_list }" var="sys_groups">
                                            <option value="${sys_groups.id}">${sys_groups.groupName}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                            </div>

                          <div class="form-group">
                            <label for="description" class="col-sm-2 control-label">描述 </label>
                            <div class="col-sm-10">
                              <input type="text" class="form-control" id="description" name="description" parsley-trigger="change" parsley-minlength="0" parsley-validation-minlength="1">
                            </div>
                          </div>

                        </form>
                      </div>

                      <div class="tab-pane" id="tab2">

                        <form class="form-horizontal form2" role="form" parsley-validate id="contact">
                         
                          <div class="form-group">
                            <label for="email" class="col-sm-2 control-label">选择云主机*</label>
                            <div class="col-sm-10">
                              <ul class="nolisttypes inlineSelect rowSelect green">
                          		<li class="">
									<button type="button" class="btn btn-red delete" id="add_host">
	                              		<i class="fa fa-plus"></i>
	                              		<span>添加新主机 </span>
	                    			</button>
								</li>
                        	</ul>

                            </div>
                          </div>
                          </form>
                       <!-- --------隐藏内容-------- -->
                     <form class="form-horizontal form2" role="form" parsley-validate id="host_options">
                      <div class="form-group">
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
                      
                      <div class="form-group">
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
                      
                      <div class="form-group">
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
                      
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">带宽 *</label>
                        <div class="col-sm-8"> 
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="bandwidth" id="optionsRadios43" value="1" checked onclick="$('#bandwidthdiy').attr('disabled','disabled');$('#bandwidthdiy').val('')">
                            <label for="optionsRadios43">1M</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="bandwidth" id="optionsRadios38" value="2" onclick="$('#bandwidthdiy').attr('disabled','disabled');$('#bandwidthdiy').val('')">
                            <label for="optionsRadios38">2M</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="bandwidth" id="optionsRadios39" value="4" onclick="$('#bandwidthdiy').attr('disabled','disabled');$('#bandwidthdiy').val('')">
                            <label for="optionsRadios39">4M</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="bandwidth" id="optionsRadios40" value="6" onclick="$('#bandwidthdiy').attr('disabled','disabled');$('#bandwidthdiy').val('')">
                            <label for="optionsRadios40">6M</label>
                          </div>
                          <div class="radio radio-transparent col-md-4">
                            <input type="radio" name="bandwidth" id="optionsRadios41" value="10" onclick="$('#bandwidthdiy').attr('disabled','disabled');$('#bandwidthdiy').val('')">
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
                         
                    <div class="form-group">
                        <label for="input07" class="col-sm-2 control-label">镜像 *</label>
                        <div class="col-sm-4" id="selectbox">
                          <select class="chosen-select chosen-transparent form-control" name="sysImageId" id="input07" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox">
                            <option value="">请选择镜像</option> 
                            <c:forEach items="${imageList }" var="sdi">
                                 	<c:if test="${sdi.realImageId!=null }">
                                 		<option value="${sdi.id }">${sdi.displayName }</option>
                                 	</c:if>
                             </c:forEach>
                          </select>
                        </div>
                      </div>
                      <div class="form-group">
                            <label for="hostAmount" class="col-sm-2 control-label">创建数量 *</label>
                            <div class="col-sm-3">
                              <input type="text" class="form-control" id="hostAmount" name="hostAmount" parsley-trigger="change" parsley-required="true" parsley-minlength="1" parsley-max="15" parsley-type="integer" parsley-validation-minlength="1" value="1">
                            </div>
                       </div>  
                       <div class="form-group form-footer footer-white" style="background-color: #C78585;">
                        <div class="col-sm-offset-4 col-sm-8">
                          <button id="create_btn" type="button" class="btn btn-greensea" onclick="addHost();"><i class="fa fa-plus"></i>
                              <span> 添 加 </span></button>
                          <button id="reset_btn" type="reset" class="btn btn-red"><i class="fa fa-refresh"></i>
                              <span> 重 置 </span></button>
                        </div>
                      </div>
                    </form>
					<!-- 隐藏内容结束 -->

                      </div>
                      
                      <div class="tab-pane" id="tab3">
                        
                        <form class="form-horizontal form3" role="form" parsley-validate id="eula">
                         
                          <div class="form-group" align="center">
			                    <h4><strong><font color="azure" id="status_text">创建成功</font></strong></h1>
                            <!-- <div class="col-sm-12">
                              <div class="checkbox">
                                <input type="checkbox" value="1" id="opt06" parsley-trigger="change" parsley-required="true" name="eula" parsley-group="two">
                                <label for="opt06">EULA acceptation *</label>
                              </div>
                              <div class="checkbox">
                                <input type="checkbox" value="1" id="opt07" name="newsletter" parsley-group="two">
                                <label for="opt07">Receive newsletter</label>
                              </div>
                            </div> -->
                          </div>

                        </form>

                      </div>

                    </div>

                  </div>
                  <!-- /tile body -->

                  <!-- tile footer -->
                  <div class="tile-footer border-top color white rounded-bottom-corners nopadding">
                    <ul class="pager pager-full wizard">
                      <li class="previous"><a href="javascript:;"><i class="fa fa-reply fa-lg"></i></a></li>
                      <li class="next"><a href="javascript:;"><i class="fa fa-arrow-right fa-lg"></i></a></li>
                      <li class="next finish" style="display:none;"><a href="javascript:;"><i class="fa fa-check fa-lg"></i></a></li>
                    </ul>
                  </div>
                  <!-- /tile footer -->
                  
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
                                <label style="align:center;" id="confirmcontent">确定要删除该主机配置吗？</label>
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
              <!-- /col 12 -->
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
        var isCommited = false;
    $(function(){
    	
    	$("#host_options").hide();
//    	  	$("#ip_pool").show();
   	 	$("#host_options").attr("disabled",true);
   	    $("#add_host").click(function(){
   	    	$("#host_options").show();
   	   	 	$("#host_options").attr("disabled",false);
   	    });
   	  
    	//initialize form wizard
        $('#rootwizard').bootstrapWizard({

          'tabClass': 'nav nav-tabs tabdrop',
          onTabShow: function(tab, navigation, index) {
            var $total = navigation.find('li').not('.tabdrop').length;
            var $current = index+1;
            var $percent = ($current/$total) * 100;
            $('#rootwizard').find('#bar .progress-bar').css({width:$percent+'%'});

            // If it's the last tab then hide the last button and show the finish instead
            if($current >= $total) {
              $('#rootwizard').find('.pager .next').hide();
              $('#rootwizard').find('.pager .finish').show();
              $('#rootwizard').find('.pager .finish').removeClass('disabled');
            } else {
              $('#rootwizard').find('.pager .next').show();
              $('#rootwizard').find('.pager .finish').hide();
            }  
          },

          onNext: function(tab, navigation, index) {

            var form = $('.form' + index);
            if(index==2){
            	var total_checked = $("#contact :checkbox:checked").length;
            	if(total_checked<2 || total_checked>15){
            		$("#tipscontent").html("请选择2~15个云主机");
         		    $("#dia").click();
         		    return false;
            	}else{
            		var display_name = $("#displayName").val();
            		var ip_amount = $("#ipAmount").val();
                    var group_id = $("#group_id").val();
            		var description = $("#description").val();
            		var checked = $("#contact :checkbox:checked");
            		var hosts = [];
            		$(checked).each(function(){
   						hosts.push($(this).val());
   				 	});
            		
            		if(isCommited){
                 		return false;
            		} 
            		isCommited = true;
            		
            		jQuery.ajax({
						url: path+'/vpc/add',
						type: 'post', 
						data:{displayName:display_name,ipAmount:ip_amount,groupId:group_id,description:description,hosts:hosts},
						dataType: 'json',
						timeout: 10000,
						async: false,
						success:function(data){
							isCommited = false;
							if(data.status == "success"){
								return true;
							}else if(data.status == "fail"){
								$("#tipscontent").html("创建失败");
			         		    $("#dia").click();
			         		    $("#status_text").html("创建失败");
								return false;
							}
						}
					});
            	}
            }
            if(index==3){
            	window.location.href = path + "/vpc/all";
            }
            form.parsley('validate');

            if(form.parsley('isValid')) {
            	
              tab.addClass('success');       
            } else {
              return false;
            }

          },

          onTabClick: function(tab, navigation, index) {

            var form = $('.form' + (index+1))

            form.parsley('validate');

            if(form.parsley('isValid')) {
              tab.addClass('success');  
            } else {
              return false;
            }

          }

        });

        // Initialize tabDrop
        
        $('.tabdrop').tabdrop({text: '<i class="fa fa-th-list"></i>'});
    	//----------------------
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
      //$(".chosen-select").chosen({disable_search_threshold: 10});

      //check toggling
      $('.check-toggler').on('click', function(){
        $(this).toggleClass('checked');
      });
	  
    })
    function addHost(){
		var form = jQuery("#host_options");
		form.parsley('validate');
		if(form.parsley('isValid')){
			var cpu_core = $("input[name='cpuCore']:checked").val();
			var memory = $("input[name='memory']:checked").val();
			var data_disk = $("input[name='dataDisk']:checked").val();
			var data_disk_diy = $("#diskdiy").val();
			var bandwidth = $("input[name='bandwidth']:checked").val();
			var bandwidth_diy = $("#bandwidthdiy").val();
			var host_amount = $("#hostAmount").val();
			var image = $("#input07").val();
			//防止多选框的ID重复
			var total_check = $("#contact .checkbox").length;
			if(data_disk==""){
				data_disk = data_disk_diy;
			}
			if(bandwidth==""){
				bandwidth = bandwidth_diy;
			}
			var value = cpu_core+'/'+memory+'/'+data_disk+'/'+bandwidth+'/'+image;
			var value_text = cpu_core+'核/'+memory+'G/'+data_disk+'G/'+bandwidth+'M';
			for(var i=0;i<host_amount;i++){
				var cur_index = total_check + i + 1;
				var cur_id = 'opt'+cur_index;
				var opt = "<li><div class='checkbox'><input type='checkbox' value="+value+" id="+cur_id+" parsley-group='one'><label for="+cur_id+">"+value_text+"</label></div></li>";
				$("#contact li:first").after(opt);
			}
			$("#reset_btn").click();
			$("#host_options").hide();
   	   	 	$("#host_options").attr("disabled",true);
		}
		        	
    }
    </script>
    
  </body>
</html>
      

