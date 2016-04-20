<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- set_time_back_up_manage.jsp -->
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
            

            <h2><i class="fa fa-clock-o"></i> 定时备份管理</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              <div class="col-md-12">



                <!-- tile -->
                <section class="tile color transparent-black">

                  <!-- tile widget -->
	                  <div class="tile-widget color transparent-black rounded-top-corners nopadding nobg">
	                    <!-- Nav tabs -->
	                    <ul class="nav nav-tabs tabdrop">
	                      <li ><a href="#users-tab" data-toggle="tab" onclick="window.location.href='<%=request.getContextPath() %>/csbackresume/detailmanage';">备份记录</a></li>
	                      <li class="active"><a href="#orders-tab" onclick="window.location.href='<%=request.getContextPath() %>/csbackresume/dtsettimebackup/manage';" data-toggle="tab">定时备份管理</a></li>
	                      <li><a href="#messages-tab" onclick="window.location.href='<%=request.getContextPath() %>/csbackresume/desktopbackuptimer/manage';" data-toggle="tab">定时备份主机管理</a></li>
	                      <li><a href="#tasks-tab" data-toggle="tab" onclick="window.location.href='<%=request.getContextPath() %>/csbackresume/manualbackup';">手动备份</a></li>
	                      <div id="space"></div>
	                      
	                     </ul>
	                    <!-- / Nav tabs -->
	                  </div>
	                  <!-- /tile widget -->

                  <!-- tile header -->
                  <div class="tile-header">
                    <h3>输入定时信息</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <form class="form-horizontal" role="form" parsley-validate id="timerform" action="<%=request.getContextPath() %>/cssettimebackup/updatecheck" method="post"   >
                       <input type="hidden" name="id" value="${timer.id}"/>
                       <input type="hidden" name="key" value="server_back_up"/>
                       <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">定时器状态*</label>
                        <div class="col-sm-10">
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="status" id="status1" value="1" checked onclick="$('#timerinfo').show();$('#timerinfo').find('input[type=text]').removeAttr('disabled');">
                            <label for="status1">启用</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="status" id="status2" value="2" onclick="$('#timerinfo').find('input[type=text]').attr('disabled','disabled');$('#timerinfo').hide();">
                            <label for="status2">禁用</label>
                          </div>
                        </div>
                      </div>
                      <div id="timerinfo">
                      
	                      <div class="form-group">
	                        <label for="input01" class="col-sm-2 control-label">模式 *</label>
	                        <div class="col-sm-10">
	                          <div class="radio radio-transparent col-md-2">
	                            <input type="radio" name="type" id="type1" value="1" checked onclick="$('#everymonth').show();$('#everymonth').find('input').removeAttr('disabled');$('#everyweek').find('input').attr('disabled','disabled');$('#everyweek').hide();">
	                            <label for="type1">月</label>
	                          </div>
	                          <div class="radio radio-transparent col-md-2">
	                            <input type="radio" name="type" id="type2" value="2" onclick="$('#everymonth').find('input').attr('disabled','disabled');$('#everymonth').hide();$('#everyweek').show();$('#everyweek').find('input').removeAttr('disabled');">
	                            <label for="type2">周</label>
	                          </div>
	                          <div class="radio radio-transparent col-md-2">
	                            <input type="radio" name="type" id="type3" value="3" onclick="$('#everymonth').find('input').attr('disabled','disabled');$('#everyweek').find('input').attr('disabled','disabled');$('#everymonth').hide();$('#everyweek').hide();">
	                            <label for="type3">日</label>
	                          </div>
	                        </div>
	                      </div>
	                      <div class="form-group" id="everymonth">
	                        
	                        <label for="input01" class="col-sm-2 control-label">每月*</label>
	                        <div class="col-sm-4">
	                          <input type="text" class="form-control" value="${timer.day}" id="day" name="day" parsley-type="number" maxlength="2" parsley-trigger="change" value="${hour }" parsley-required="true" parsley-max="31" parsley-min="1">
	                        </div>
	                      </div>
	                      <div class="form-group" id="everyweek">
	                        <label for="input01" class="col-sm-2 control-label">每周*</label>
	                        <div class="col-sm-4">
	                          <input type="text" class="form-control" value="${timer.week}"  id="week" name="week" parsley-type="number" maxlength="2" parsley-trigger="change" value="${hour }" parsley-required="true" parsley-max="7" parsley-min="1">
	                        </div>
	                      </div>
	                      <div class="form-group">
	                        <label for="input01" class="col-sm-2 control-label">时*</label>
	                        <div class="col-sm-4">
	                          <input type="text" class="form-control" value="${timer.hour}"  id="hour" name="hour" parsley-type="number" maxlength="2" parsley-trigger="change" value="${hour }" parsley-required="true" parsley-max="23" parsley-min="1">
	                        </div>
	                      </div> 
	                      
	                      <div class="form-group">
	                        <label for="input01" class="col-sm-2 control-label">分*</label>
	                        <div class="col-sm-4">
	                          <input type="text" class="form-control" id="minute" value="${timer.minute}"  name="minute"  parsley-type="number" maxlength="2" parsley-trigger="change" value="${min }" parsley-required="true" parsley-max="59" parsley-min="0">
	                        </div>
	                      </div> 
<!-- 	                      <div class="form-group"> -->
<!-- 	                        <label for="input01" class="col-sm-2 control-label">秒*</label> -->
<!-- 	                        <div class="col-sm-4"> -->
<%-- 	                          <input type="text" class="form-control" id="second" value="${timer.second}"  name="second"  parsley-type="number" maxlength="2" parsley-trigger="change" value="${sec }" parsley-required="true" parsley-max="59" parsley-min="0"> --%>
<!-- 	                        </div> -->
<!-- 	                      </div> -->
	                      <div class="form-group">
	                        <label for="input01" class="col-sm-2 control-label">备份类型</label>
	                        <div class="col-sm-10">
	                          <div class="radio radio-transparent col-md-2">
	                            <input type="radio" name="mode"   id="mode0" value="0" onclick="$('#choose_disk').hide();">
	                            <label for="mode0">全备份</label>
	                          </div>
	                          <div class="radio radio-transparent col-md-2">
	                            <input type="radio" name="mode"   id="mode1" value="1" onclick="$('#choose_disk').show();" checked>
	                            <label for="mode1">部分备份</label>
	                          </div> 
	                        </div>
	                      </div>
			                      
	                      <div class="form-group" id="choose_disk">
	                        <label for="input01" class="col-sm-2 control-label">备份盘选择</label>
	                        <div class="col-sm-10">
	                          <div class="radio radio-transparent col-md-2">
	                            <input type="radio" name="disk"   id="disk0" value="0" >
	                            <label for="disk0">系统盘</label>
	                          </div>
	                          <div class="radio radio-transparent col-md-2">
	                            <input type="radio" name="disk"   id="disk1" value="1" checked>
	                            <label for="disk1">数据盘</label>
	                          </div> 
	                        </div>
	                      </div>  
                      </div>
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
      <script src="<%=request.getContextPath()%>/assets/js/vendor/no-ui-slider/jquery.nouislider.all.js"></script> 

    <script>
    
    var path = '<%=request.getContextPath()%>'; 
    var isCommited = false;
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
      if('${timer.type}' == ''){
    	  $("#type1").click();
      }else{
    	  $("#type"+'${timer.type}').click();
      }      
      $("#mode"+'${timer.mode}').click();
      $("#disk"+'${timer.disk}').click();
      $("#status"+'${timer.status}').click();
      
      $("#timerinfo").find("input[type='text']").each(function(){
    	  if($(this).is(":visible") == false){
    		  $(this).attr("disabled","disabled");
    	  }
      });
      
      $("#space").width($("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop").width()
				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).width()
				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(1).width()
				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(2).width()
				 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(3).width()
				 -1).height(
				  $("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).height());
		$(window).resize(function(){
			 $("#space").width($("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop").width()
					 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(0).width()
					 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(1).width()
					 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(2).width()
					 -$("body #content .tile .tile-widget.color.transparent-black.nobg .tabdrop li").eq(3).width()
					 -1);
		});
    
      
      
      
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
	            isCommited = false;
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
 		        						console.info(data);
 		        						if(data.status == "fail"){
 		        							  isCommited = false;
							        		  $("#tipscontent").html("保存失败");
							     		      $("#dia").click();  		        							
 		        						}else{  		        							
// 	   		        						location.href = path + "/chcm/all";
 		        							window.location.reload(); 
 		        						}
 		        					},
 		        					 error: function(data){ 
 		        						isCommited = false;
 		        						$("#tipscontent").html(data.message);
						     		     $("#dia").click();//失败时的处理方法  
 		        					     },
 		        					dataType:'json',
 		        					timeout:10000
 		        			};
 		        			var form = jQuery("#timerform");
 		        			form.parsley('validate');
 		        			if(form.parsley('isValid')){  		        				
			        			jQuery("#timerform").ajaxSubmit(options); 
 		        			} 
		        	} 
	        }
	     }); 
		
	}
    
  
   
    </script>
  </body>
</html>
      

      

      
 
