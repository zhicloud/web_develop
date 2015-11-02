<%@ page pageEncoding="utf-8"%>
  <head>
    <title>配置健康检查</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  </head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style>
  body #content .tile table > tbody > tr td .checkbox,
  body #content .tile table > tbody > tr th .checkbox,
  body #content .tile table > tfoot > tr td .checkbox,
  body #content .tile table > tfoot > tr th .checkbox{
    padding-top: 1px;
    margin: 0;
    min-height: 5px; 
  }
  .pagination-sm > li > a > i{
  	padding:5px;
  }
  #porttable > tbody > tr > td {
  	color:#fafafa;
  }
</style>
<script type="text/javascript">
//返回
function backhome(){
	window.location.href = "<%=request.getContextPath() %>/loadbalance/manage";
}

</script>
<%@include file="/views/common/common_menus.jsp" %>
    <!-- Make page fluid -->
    <div class="row">
<!-- Page content -->
        <div id="content" class="col-md-12">

          <!-- page header -->
          <div class="pageheader">
            
            <h2><i class="fa fa-trophy"></i> 
            	配置健康检查
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
                  	<h3>配置健康检查:</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body" style="padding-bottom:0px;margin-bottom:-45px;">
                  <p style="margin-top:-20px;">负载均衡器将自动对ECS进行健康检查，它只将网络访问指向通过了健康检查的ECS。如果一个ECS的健康检查失败，负载均衡器将不会将网络访问指向这个ECS。请根据您的具体需求自定义健康检查的具体指标</p>
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations" style="margin-top:0px;">
					    <div class="form-group">
                            <label for="checkprotocol" class="col-sm-3 control-label">检查协议</label>
                            <div class="col-sm-4" id="tab2group1">
								<select class="chosen-select chosen-transparent form-control" id="checkprotocol" parsley-trigger="change" parsley-required="true" parsley-error-container="#tab2group1">
		                            <option>ping</option>  
		                            <option>HTTP</option> 
		                            <option>ARP</option>
		                            <option>FTP</option>
		                            <option>TCP</option>
		                            <option>UDP</option>
		                            <option>UDP</option>
		                            <option>HTTPS</option>
		                            <option>DNS</option>
		                            <option>TCPS</option>
		                        </select>  
	                        </div>
                          </div>

                          <div class="form-group">
                            <label for="timeout" class="col-sm-3 control-label">响应超时时间</label>
                            <div class="col-sm-4" id="tab2group2">
                              <input type="text" class="form-control" id="timeout" parsley-trigger="change" parsley-minlength="4" parsley-type="number" parsley-error-container="#tab2group2" parsley-validation-minlength="1">
                            </div>
                          </div>

                          <div class="form-group">
                            <label for="healthycheck" class="col-sm-3 control-label">健康检查间隔</label>
                            <div class="col-sm-4" id="tab2group3">
                              <input type="text" class="form-control" id="healthycheck" parsley-trigger="change" parsley-type="number" parsley-validation-minlength="0" placeholder="30" parsley-error-container="#tab2group3">
                            </div>
                          </div>  
                          
                          <div class="form-group">
                            <label for="nothealthy" class="col-sm-3 control-label">不健康阀值</label>
                            <div class="col-sm-4" id="tab2group4">
								<select class="chosen-select chosen-transparent form-control" id="nothealthy" parsley-trigger="change" parsley-required="true" parsley-error-container="#tab2group4">
		                            <option>2</option>  
		                            <option>3</option> 
		                            <option>4</option>
		                            <option>5</option>
		                            <option>6</option>
		                            <option>7</option>
		                            <option>8</option>
		                            <option>9</option>
		                            <option>10</option>
		                        </select>  
	                        </div>
                          </div>
                          <div class="form-group">
                            <label for="thealthyvalue" class="col-sm-3 control-label">健康阀值</label>
                            <div class="col-sm-4" id="tab2group5">
								<select class="chosen-select chosen-transparent form-control" id="thealthyvalue" parsley-trigger="change" parsley-required="true" parsley-error-container="#tab2group5">
		                            <option>2</option>  
		                            <option>3</option> 
		                            <option>4</option>
		                            <option>5</option>
		                            <option>6</option>
		                            <option>7</option>
		                            <option>8</option>
		                            <option>9</option>
		                            <option>10</option>
		                        </select>  
	                        </div>
                          </div>         
                     <div class="form-group form-footer footer-white" style="margin-bottom:5px;position: relative;">
                        <div class="col-sm-offset-4 col-sm-8" style="z-index: 100;">
                          <button type="button" class="btn btn-greensea" onclick="saveData()"><i class="fa fa-save"></i>
                              <span> 保 存 </span></button>
                          <button type="button" class="btn btn-greensea" onclick="backhome()"><i class="fa fa-backward"></i>
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
    })
      
    </script>
    
  </body>
