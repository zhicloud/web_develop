<%@ page pageEncoding="utf-8"%>
<!-- bind_eip.jsp -->
  <head>
    <title>绑定EIP</title>
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
            	绑定EIP
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
                  	<h3>选择要绑定给ELB的公网IP</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body" style="padding-bottom:0px;margin-bottom:-45px;">
                  <p style="margin-top:-20px;">可用的公网IP:</p>
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations" style="margin-top:0px;">
					  <table  class="table table-datatable table-custom" id="porttable">
			                      <thead>
			                          <tr>
						              <th class="no-sort">选择</th>
									  <th class="no-sort">ID</th>
									  <th class="no-sort">公网IP地址</th>
			                          </tr>
			                        </thead>
									<tbody>   
									<tr>
									<td>
									  <div class="radio radio-transparent">
			                            <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" style="height:20px;">
			                            <label for="optionsRadios1">&nbsp;&nbsp;</label>
			                          </div>
									</td>
									<td class="cut">EIP_123456</td>
									<td class="cut">112.1.1.2</td>
									</tr>  
									<tr>
									<td>
									  <div class="radio radio-transparent">
			                            <input type="radio" name="optionsRadios" id="optionsRadios2" value="option2" style="height:20px;">
			                            <label for="optionsRadios2">&nbsp;&nbsp;</label>
			                          </div>
									</td>
									<td class="cut">EIP_123879</td>
									<td class="cut">112.1.1.2</td>
									</tr>  									  
			                      </tbody>
			           </table>          
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
