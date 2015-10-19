<%@ page pageEncoding="utf-8"%>
  <head>
    <title>告警规则信息编辑</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  </head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
  body #content .tile table > tbody > tr td .checkbox,
  body #content .tile table > tbody > tr th .checkbox,
  body #content .tile table > tfoot > tr td .checkbox,
  body #content .tile table > tfoot > tr th .checkbox{
    padding-top: 15px;
    margin: 0;
    min-height: 10px; 
  }
  .pagination-sm > li > a > i{
  	padding:5px;
  }
</style>
<script type="text/javascript">
//返回
function backhome(){
	window.location.href = "<%=request.getContextPath() %>/syswarn/rulelist";
}
//初始化radio
function initdata(){
	var type = "${type}";
	if(type=="modify"){
		$("input[name='isnotify'][value='${ruleVO.isnotify}']").attr("checked","checked");
	}
}
</script>
<%@include file="/views/common/common_menus.jsp" %>
    <!-- Make page fluid -->
    <div class="row">
<!-- Page content -->
        <div id="content" class="col-md-12">

          <!-- page header -->
          <div class="pageheader">
            
            <h2><i class="fa fa-warning"></i> 
            <c:if test="${empty ruleVO}">新增告警规则</c:if>
            <c:if test="${!empty ruleVO}">修改告警规则</c:if>	
            </h2>

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
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body" style="padding-bottom:0px;margin-bottom:-45px;">
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations" action="${pageContext.request.contextPath }/syswarn/saverule" method="post">
                     <c:if test="${empty ruleVO}">
                     <input type="hidden" name="edittype" value="add"/>
                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">规则名称</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" name="name" id="name" parsley-required="true"  parsley-maxlength="20">
                        </div>
                        <label for="code" class="col-sm-2 control-label">规则标示符</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" name="code" id="code" parsley-required="true"  parsley-maxlength="20">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="ruletype" class="col-sm-2 control-label">规则类型</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" for="ruletype">
                            <select class="chosen-select chosen-transparent form-control" name="ruletype" id="ruletype" parsley-required="true" parsley-error-container="#selectbox">
                            <option value="">请选择</option>  
                            <option value="0">告警</option>  
                            <option value="1">故障</option> 
                          </select></div> 
                        </div>
                        
                        <label for="realtime" class="col-sm-2 control-label">通知类型</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" for="realtime">
                            <select class="chosen-select chosen-transparent form-control" name="realtime" id="realtime" parsley-required="true" parsley-error-container="#selectbox">
                            <option value="">请选择</option>  
                            <option value="1">定时</option>  
                            <option value="2">实时</option> 
                          </select></div> 
                        </div>  
                                              
                      </div>    
                                          
                      <div class="form-group">
                        <label for="frequency" class="col-sm-2 control-label">采样频率</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" name="frequency" id="frequency" parsley-required="true" parsley-type="digits"  parsley-maxlength="20">
                        </div>
                        <label for="sampletime" class="col-sm-2 control-label">采样次数</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" name="sampletime" id="sampletime" parsley-required="true" parsley-type="digits"  parsley-maxlength="9">
                        </div>
                      </div>  
                      <div class="form-group">
                        <label for="notify_phone" class="col-sm-2 control-label">通知电话</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" name="notify_phone" id="notify_phone" parsley-required="true"  parsley-type="phone">
                        </div>
                        
                        <label for="notify_email" class="col-sm-2 control-label">通知邮件</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" name="notify_email" id="notify_email" parsley-required="true" parsley-type="email" parsley-maxlength="100">
                        </div>
                      </div>  
                      <div class="form-group" id="lastgroup">
                       <label for="isnotify" class="col-sm-2 control-label">是否发送通知</label>
                        <div class="col-sm-4">
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="isnotify" id="isnotify1" value="1" checked>
                            <label for="isnotify1">是</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="isnotify" id="isnotify2" value="0">
                            <label for="isnotify2">否</label>
                          </div>
                        </div>
                        
                      </div> 
                      </c:if>
                      <!-- modify -->
					<c:if test="${!empty ruleVO}">
                     <input type="hidden" name="edittype" value="modify"/>
                     <input type="hidden" name="id" value="${ruleVO.id }"/>
                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">规则名称</label>
                        <div class="col-sm-4">
                          <input type="text" value="${ruleVO.name }" class="form-control" name="name" id="name" parsley-required="true"  parsley-maxlength="20">
                        </div>
                        <label for="code" class="col-sm-2 control-label">规则标示符</label>
                        <div class="col-sm-4">
                          <input type="text" value="${ruleVO.code }" class="form-control" name="code" id="code" parsley-required="true"  parsley-maxlength="20">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="ruletype" class="col-sm-2 control-label">规则类型</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" for="ruletype">
                            <select class="chosen-select chosen-transparent form-control" name="ruletype" id="ruletype" parsley-required="true" parsley-error-container="#selectbox">
                            <option value="0" <c:if test="${ruleVO.ruletype==0 }">selected</c:if> >告警</option>  
                            <option value="1" <c:if test="${ruleVO.ruletype==1 }">selected</c:if> >故障</option> 
                          </select></div> 
                        </div>
                        
                        <label for="realtime" class="col-sm-2 control-label">通知类型</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" for="realtime">
                            <select class="chosen-select chosen-transparent form-control" name="realtime" id="realtime" parsley-required="true" parsley-error-container="#selectbox">
                            <option value="1" <c:if test="${ruleVO.realtime==1 }">selected</c:if>>定时</option>  
                            <option value="2" <c:if test="${ruleVO.realtime==2 }">selected</c:if>>实时</option> 
                          </select></div> 
                        </div>  
                                              
                      </div>    
                                          
                      <div class="form-group">
                        <label for="frequency" class="col-sm-2 control-label">采样频率</label>
                        <div class="col-sm-4">
                          <input type="text" value="${ruleVO.frequency}" class="form-control" name="frequency" id="frequency" parsley-type="digits" parsley-required="true"  parsley-maxlength="20">
                        </div>
                        <label for="sampletime" class="col-sm-2 control-label">采样次数</label>
                        <div class="col-sm-4">
                          <input type="text" value="${ruleVO.sampletime}" class="form-control" name="sampletime" id="sampletime" parsley-type="digits" parsley-required="true"  parsley-maxlength="9">
                        </div>
                      </div>  
                      <div class="form-group">
                        <label for="notify_phone" class="col-sm-2 control-label">通知电话</label>
                        <div class="col-sm-4">
                          <input type="text" value="${ruleVO.notify_phone}" class="form-control" name="notify_phone" id="notify_phone" parsley-required="true"  parsley-type="phone">
                        </div>
                        
                        <label for="notify_email" class="col-sm-2 control-label">通知邮件</label>
                        <div class="col-sm-4">
                          <input type="text" value="${ruleVO.notify_email}" class="form-control" name="notify_email" id="notify_email" parsley-required="true" parsley-type="email" parsley-maxlength="100">
                        </div>
                      </div>  
                      <div class="form-group" id="lastgroup">
                       <label for="isnotify" class="col-sm-2 control-label">是否发送通知</label>
                        <div class="col-sm-4">
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="isnotify" id="isnotify1" value="1">
                            <label for="isnotify1">是</label>
                          </div>
                          <div class="radio radio-transparent col-md-2">
                            <input type="radio" name="isnotify" id="isnotify2" value="0">
                            <label for="isnotify2">否</label>
                          </div>
                        </div>
                        <c:if test="${ruleVO.realtime==1 }">
                        <label for="sendtime" class="col-sm-2 control-label" id="sendtimelable">定时发送时间</label>
                        <div class="col-sm-4" id="sendtimediv">
                        	<input type="text" value="${ruleVO.sendtime }" class="form-control" name="sendtime"  parsley-required="true" parsley-type="sendtime" parsley-maxlength="8">
                        </div>
                        </c:if>
                      </div> 
                      </c:if>                      
                      
                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8" style="z-index: 100;">
                          <button type="button" class="btn btn-greensea" id="save_btn"><i class="fa fa-plus"></i>
                              <span> 保 存 </span></button>
                          <button type="button" class="btn btn-default" onclick="backhome()"><i class="fa fa-backward"></i>
                              <span> 返回 </span></button>
                        </div>
                      </div>
                            
                    </form>

                  </div>
                  <!-- /tile body -->
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
                </section>
                <!-- /tile -->

              </div>
              <!-- /col 6 -->
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
    
    <script>
    $(function(){
      //chosen select input
      $(".chosen-select").chosen({disable_search_threshold: 10});
      var lablehtml  = "<label for=\"sendtime\" class=\"col-sm-2 control-label\" id=\"sendtimelable\">定时发送时间</label>";
      var divhtml = "<div class=\"col-sm-4\" id=\"sendtimediv\"><input type=\"text\" class=\"form-control\" name=\"sendtime\" id=\"sendtime\" parsley-required=\"true\"  parsley-type=\"sendtime\" parsley-maxlength=\"8\"></div>";
   	  $("#realtime").change(function(){ 
   		  if($(this).val()=="1"){
   			$("#lastgroup").append(lablehtml+divhtml);
   		  }else{
   			$("input[name=sendtime]").val("24:00:00");
   			$("#sendtimelable").remove();
   			$("#sendtimediv").remove();
   		  }
   	  });
      $("#save_btn").click(function(){
        	var options = {
    				success:function result(data){
    					if(data.status == "success"){
    						location.href = "${pageContext.request.contextPath}/syswarn/rulelist";
    					}else{
    			     		$("#tipscontent").html(data.message);
    			     		$("#dia").click();
    			     		return;
    					}
    				},
    				dataType:'json',
    				timeout:10000
    		};
        	var form = jQuery(".form-horizontal");
        	var sendtime = jQuery("input[name=sendtime]");
        	var realtime = jQuery("#realtime").val();
	  		form.parsley('validate');
	  		sendtime.parsley('validate');
	  		if(form.parsley('isValid')){  	
	  			if(realtime=="1"&&sendtime.parsley('isValid')){
	  				$(".form-horizontal").ajaxSubmit(options);
	  			}
	  			if(realtime=="2"){
	  				$(".form-horizontal").ajaxSubmit(options); 
	  			}
	  		}
       });
      initdata();
    })
    </script>
    
  </body>
