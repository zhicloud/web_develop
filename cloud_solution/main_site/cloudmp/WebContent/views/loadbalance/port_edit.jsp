<%@ page pageEncoding="utf-8"%>
  <head>
    <title>编辑端口</title>
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
</style>
<script type="text/javascript">
//返回
function backhome(){
	window.location.href = "<%=request.getContextPath() %>/loadbalance/manage";
}
//增加表格行数
function addRow(){
	var len = $("#porttable tbody tr").length+1;
	var trhtml = "<tr><td><select class=\"chosen-select chosen-transparent form-control\" name=\"elb_protocol\" id=\"elb_protocol"+len+"\">";
		trhtml += "<option>HTTP</option><option>ARP</option><option>FTP</option><option>TCP</option><option>UDP</option><option>HTTPS</option><option>DNS</option><option>TCPS</option>";
		trhtml += "</select></td><td><input type=\"text\"  class=\"form-control\" id=\"elb_port"+len+"\" ></td>";
		trhtml += "<td><select class=\"chosen-select chosen-transparent form-control\" name=\"instance_protocol\" id=\"instance_protocol"+len+"\">";
		trhtml += "<option>HTTP</option><option>ARP</option><option>FTP</option><option>TCP</option><option>UDP</option><option>HTTPS</option><option>DNS</option><option>TCPS</option>";
		trhtml += "</select></td><td><input type=\"text\"  class=\"form-control\" id=\"instance_port"+len+"\" ></td>";
		trhtml += "</tr>";

$("#porttable tbody").append(trhtml);
$("#elb_protocol"+len).chosen({disable_search:true});
$("#instance_protocol"+len).chosen({disable_search:true});
}
//删除表格行
function deleteRow(){
	var len = $("#porttable tbody tr").length;
	if(len>1){
		$("#porttable tbody").find("tr:last").remove();
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
            
            <h2><i class="fa fa-trophy"></i> 
            	编辑端口
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
                  <p>下面是当前已配置给这个负载均衡器的端口:</p>
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations" style="margin-top:0px;">
					  <table  class="table table-datatable table-custom" id="porttable" style="font-size: 6px;">
			                      <thead>
			                          <tr>
									  <th style="width:30%;">负载均衡器协议</th>
									  <th style="width:30%;">负载均衡器端口</th>
									  <th style="width:20%;">实例协议</th>
									  <th style="width:20%;">实例端口</th>
			                          </tr>
			                        </thead>
									<tbody>   
									<tr>
									<td>
										<select class="chosen-select chosen-transparent form-control" name="elb_protocol" id="elb_protocol">
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
									</td>
									<td><input type="text"  class="form-control" id="elb_port" ></td>
									<td>
										<select class="chosen-select chosen-transparent form-control" name="instance_protocol" id="instance_protocol">
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
									</td>
									<td><input type="text"  class="form-control" id="instance_port" ></td>
									</tr>    
			                      </tbody>
			                       </table>          
                     <div class="form-group form-footer footer-white" style="margin-bottom:5px;position: relative;">
                        <div class="col-sm-offset-4 col-sm-8" style="z-index: 100;">
                          <button type="button" class="btn btn-greensea" onclick="addRow()"><i class="fa fa-plus"></i>
                              <span> 增加 </span></button>
                          <button type="button" class="btn btn-greensea" onclick="deleteRow()"><i class="fa fa-remove"></i>
                              <span> 删除 </span></button>                        
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
