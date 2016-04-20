<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<!-- create_elb.jsp -->
<html>
  <head>
    <title>创建ELB</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8" />
    <style>
    .test {
    background-image: url(../assets/images/noise.png), url(../assets/images/backgrounds/1.jpg);
    background-repeat: repeat, no-repeat;
    background-position: left top;
    background-size: auto, cover; }
    .testinput{
   	background-color : rgba(0, 0, 0, 0.3);
   	border:0px;
   	color:rgba(255,255,255,0.8);
    }
    .modal-footer {
      border-top-color:red;
      bordor-top-width:0px;
     }
    </style>
  </head>
<script type="text/javascript">
//增加表格行数
function addRow(){
	var len = $("#porttable tbody tr").length+1;
	var trhtml = "<tr><td>http</td><td>8080</td><td>udp</td><td>25</td><td>40,000</td>";
		trhtml += "<td>是</td><td>10分钟</td><td>2次</td><td>10次</td><td><button type=\"button\" class=\"btn btn-primary btn-xs\" onclick=\"addWeight()\">设置</button></td></tr>";
	$("#porttable tbody").append(trhtml);
	$("#loadbalance").find("button[class='close']").click();
}
//删除表格行
function deleteRow(){
	var len = $("#porttable tbody tr").length;
	if(len>1){
		$("#porttable tbody").find("tr:last").remove();
	}
}
function checkmaxcontact(obj){
	$("button[id*='maxcontact']").each(function(){
		if(this==obj){
			$(this).attr("class","btn btn-success btn-sm margin-bottom-20");
		}else{
			$(this).attr("class","btn btn-default btn-sm margin-bottom-20");
		}
		
	});
}

function displaydiv(obj){
	if(obj==true){
		$("#keepsession1").attr("class","btn btn-success btn-sm margin-bottom-20");
		$("#keepsession2").attr("class","btn btn-default btn-sm margin-bottom-20");
		$("#hiddendiv").css("display","block");
	}else{
		$("#keepsession2").attr("class","btn btn-success btn-sm margin-bottom-20");
		$("#keepsession1").attr("class","btn btn-default btn-sm margin-bottom-20");
		$("#hiddendiv").css("display","none");
	}
}
function addLoadBalance(){
	$("#con").click();
}
function addWeight(){
	$("#we").click();
}
function saveWeight(){
	$("#weight").find("button[class='close']").click();
}
</script>  
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
            

            <h2><i class="fa fa-trophy"></i> 负载均衡</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              <!-- col 6 -->
          <div class="col-md-12">
            <section>
                <div class="tile-header">
                  <h4><a href="<%=request.getContextPath() %>/loadbalance/manage" style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a><span style="color: rgb(250, 250, 250);">创建ELB</span></h4>
                </div>
            </section>
			<section id="rootwizard" class="tabbable transparent tile color transparent-black">

                  <!-- tile widget -->
                  <div class="tile-widget nopadding color transparent-black rounded-top-corners">
                    <ul>
                      <li><a href="#tab1" data-toggle="tab">添加ECS</a></li>
                      <!-- <li><a href="#tab2" data-toggle="tab">配置健康检查</a></li> -->
                      <li><a href="#tab3" data-toggle="tab">定义负载均衡</a></li>
                      <li><a href="#tab4" data-toggle="tab">确认提交</a></li>
                    </ul>
                  </div>
                  <!-- /tile widget -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <div id="bar" class="progress progress-striped active">
                      <div class="progress-bar progress-bar-cyan animate-progress-bar"></div>
                    </div>

                    <div class="tab-content">
                      
                      <div class="tab-pane" id="tab1">
	                      <div style="width:60%;">
	                      <P> 
							下表列出了您所有正在运行的，未关联给其它负载均衡器的ECS。
							请选择您想添加到这个ELB的ECS
	                      </P>
	                      </div>
	                      <div style="width:60%;height:150px;" id="ecsdiv">
	                        <table  class="table table-datatable table-custom" id="basicDataTable" style="width:98%;">
	                        <thead>
	                          <tr>
							    <th class="no-sort">
	                            <div class="checkbox check-transparent">
	                              <input type="checkbox"  id="allchck">
	                              <label for="allchck"></label>
	                            </div>
	                          </th>
	                            <th class="no-sort">ID</th>
	                            <th class="no-sort">名称</th>
	                            <th class="no-sort">状态</th>
	                          </tr>
	                        </thead>
	                        <tbody>
	                        <tr class="odd gradeX">
	                        	<td>
										<div class="checkbox check-transparent">
										  <input type="checkbox" value="testid" id="testid1">
										  <label for="testid1"></label>
										</div>
	                             </td>
	                             <td class="cut">ELB_123456</td>
	                             <td class="cut">test_ELB</td>
	                             <td class="cut">运行中</td>
	                        </tr>
	                        <tr class="odd gradeX">
	                        	<td>
										<div class="checkbox check-transparent">
										  <input type="checkbox" value="testid" id="testid2">
										  <label for="testid2"></label>
										</div>
	                             </td>
	                             <td class="cut">ELB_123456</td>
	                             <td class="cut">test_ELB</td>
	                             <td class="cut">运行中</td>
	                        </tr>
	                        </tbody>
	                      </table>
	                      </div>
	                      <div style="width:60%;">
	                     <hr style="margin-top:5px;margin-bottom:5px;width:100%;float: left">
	                     </div>
	                    <div  style="width:60%;">
	                     <span>已选择的ECS:ECS_123456，ECS_789</span>
	                    </div>
                      </div>

                      
                      <div class="tab-pane" id="tab3" style="height:300px;">
                        <div style="width:98%;float: left;">
                        	<span>定义负债均衡</span>
                        	<p>请定义您要创建的负载均衡器名称及监听端口等。</p>
                        	<form class="form-horizontal form3" role="form" parsley-validate id="tab3form">
		                      <div class="form-group" style="margin-left:0px;">
		                        <div class="col-sm-12" style="height:150px;" id="portdiv">
					              <table  class="table table-datatable table-custom" id="porttable" style="font-size: 6px;">
			                      <thead>
			                          <tr>
									  <th style="width:12%;">负载均衡器协议</th>
									  <th style="width:12%;">负载均衡器端口</th>
									  <th style="width:10%;">实例协议</th>
									  <th style="width:10%;">实例端口</th>
									  <th style="width:12%;">最大并发连接数</th>
									  <th style="width:10%;">是否保持会话</th>
									  <th style="width:10%;">保持会话时长</th>
<!-- 									  <th style="width:10%;">响应超时时间</th>
									  <th style="width:10%;">健康检查间隔</th> -->
									  <th style="width:10%;">不健康阀值</th>
									  <th style="width:10%;">健康阀值</th>
									  <th>操作</th>
			                          </tr>
			                        </thead>
									<tbody>   
									<tr>
									<td>http</td>
									<td>8080</td>
									<td>udp</td>
									<td>25</td>
									<td>40,000</td>
									<td>是</td>
									<td>10分钟</td>
<!-- 									<td>5S</td>
									<td>30S</td> -->
									<td>2次</td>
									<td>10次</td>
									<td>
									<button type="button" class="btn btn-primary btn-xs" onclick="addWeight()">设置</button>
									</td>
									</tr>  
			                      </tbody>
			                       </table>    	
		                        </div>
		                       <div class="form-group" style="margin-bottom:5px;position: relative;margin-top:5px;margin-left:0px;">
			                        <div class="col-sm-8">
			                          <button type="button" class="btn btn-greensea" onclick="addLoadBalance()"><i class="fa fa-plus"></i>
			                              <span> 增加 </span></button>
			                          <button type="button" class="btn btn-greensea" onclick="deleteRow()"><i class="fa fa-remove"></i>
                              			  <span> 删除 </span></button>    
			                        </div>
                      			   </div>	
		                      </div>
                        	</form>
                        </div>
                        

                      </div>
                      
                      
                      <div class="tab-pane" id="tab4" style="height:650px;">
                        <div style="width:70%;float: left;">
                        	<p>请确认您即将要创建的ELB的相关信息是否完全正确。</p>
                        	<div>
                        	<span><b>关联实例:</b></span>
                        	<div id="confirmecs_table" style="width:80%;height:150px;">
	                        <table  class="table table-datatable table-custom" id="basicDataTable1" style="width:98%;">
	                        <thead>
	                          <tr>
	                            <th class="no-sort">ID</th>
	                            <th class="no-sort">名称</th>
	                            <th class="no-sort">状态</th>
	                          </tr>
	                        </thead>
	                        <tbody>
	                        <tr class="odd gradeX">
	                             <td class="cut">ELB_123456</td>
	                             <td class="cut">test_ELB</td>
	                             <td class="cut">运行中</td>
	                        </tr>
	                        <tr class="odd gradeX">
	                             <td class="cut">ELB_123456</td>
	                             <td class="cut">test_ELB</td>
	                             <td class="cut">运行中</td>
	                        </tr>
	                        </tbody>
	                      </table>
	                      </div>
	                      </div>
	                       <hr style="margin-top:5px;margin-bottom:5px;width:80%;float: left">
	                      <div style="width:80%;height:170px;margin-top:15px;" id="confirmhealthy">
                        	<span><b>健康检查:</b></span>
	                        	<p>检查协议: HTTP</p>
								<p>响应超时时间: 5 S</p>
								<p>健康检查间隔: 30 S</p>
								<p>不健康阈值: 2</p>
								<p>健康阈值: 10</p>
	                      </div>
	                      <hr style="margin-top:5px;margin-bottom:5px;width:80%;float: left">
	                      <div style="width:80%;height:250px;margin-top:20px;" id="confirmelb">
                        	<span><b>负载均衡定义:</b></span><br>
	                        <span>ELB名称: test_ELB</span><br>
	                        <span>
	                        	<font>是否保持回话: 是</font>
	                        	<font>保持回话时长: 10分钟</font>
	                        </span><br>
	                        <span><b>监听端口:</b></span>
	                        <div id="confirmelb_table" style="width:80%;height:150px;margin-top:10px;">
		                        <table  class="table table-datatable table-custom" id="basicDataTable2" style="width:98%;">
		                        <thead>
		                          <tr>
		                            <th class="no-sort">负载均衡器协议</th>
		                            <th class="no-sort">负载均衡器端口</th>
		                            <th class="no-sort">实例协议</th>
		                            <th class="no-sort">实例端口</th>
		                          </tr>
		                        </thead>
		                        <tbody>
		                        <tr class="odd gradeX">
		                             <td class="cut">HTTP</td>
		                             <td class="cut">80</td>
		                             <td class="cut">HTTP</td>
		                             <td class="cut">80</td>
		                        </tr>
		                        <tr class="odd gradeX">
		                             <td class="cut">UDP</td>
		                             <td class="cut">25</td>
		                             <td class="cut">UDP</td>
		                             <td class="cut">25</td>
		                        </tr>
		                        </tbody>
		                      </table>
	                      </div>
	                      </div>
	            		<hr style="margin-top:5px;margin-bottom:5px;width:80%;float: left">           
                        </div>
                        

                      </div>
                      
                    </div>

                  </div>
                  <!-- /tile body -->

                  <!-- tile footer -->
                  <div class="tile-footer border-top color white rounded-bottom-corners nopadding" style="margin-bottom:5px;">
                    <ul class="pager pager-full wizard">
                      <li class="previous"><a href="javascript:;"><i class="fa fa-arrow-left fa-lg"></i></a></li>
                      <li class="next"><a href="javascript:;"><i class="fa fa-arrow-right fa-lg"></i></a></li>
                      <li class="next finish" style="display:none;"><a href="javascript:;"><i class="fa fa-check fa-lg"></i></a></li>
                    </ul>
                  </div>
                  <!-- /tile footer -->
                  <div class="tile-body" style="padding:0px;">

                    <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                    <a href="#loadbalance" id="con" role="button"   data-toggle="modal"> </a>
                    <a href="#weight" id="we" role="button"   data-toggle="modal"> </a>
                    
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

                    <div class="modal fade" id="loadbalance" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true">
                       <div class="modal-dialog">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabelthrees">增加负载均衡</h3>
                          </div>
                          <div class="modal-body" id="loadbalance_div" style="height:300px;overflow:auto;">
                            <form role="form" id="userusbstatusforms" class="form-horizontal">
                             
                          <div class="form-group">
                            <label for="balance_protocol" class="col-sm-3 control-label">负载均衡器协议</label>
                            <div class="col-sm-4" id="tab2group5">
								<select class="chosen-select chosen-transparent form-control" id="balance_protocol" parsley-trigger="change" parsley-required="true" parsley-error-container="#tab2group5">
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
                            <label for="timeout" class="col-sm-3 control-label">负载均衡器端口</label>
                            <div class="col-sm-4" id="tab3group1">
                              <input type="text" class="form-control" style="" id="balance_port" parsley-trigger="change" parsley-minlength="4" parsley-type="number" parsley-error-container="#tab3group1">
                            </div>
                          </div>
                          <div class="form-group">
                            <label for="instance_protocol" class="col-sm-3 control-label">实例协议</label>
                            <div class="col-sm-4" id="tab3group2">
								<select class="chosen-select chosen-transparent form-control" id="instance_protocol" parsley-trigger="change" parsley-required="true" parsley-error-container="#tab3group2">
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
                            <label for="instance" class="col-sm-3 control-label">实例端口</label>
                            <div class="col-sm-4" id="tab3group3">
                              <input type="text" class="form-control" id="instance_port" parsley-trigger="change" parsley-minlength="4" parsley-type="number" parsley-error-container="#tab3group3">
                            </div>
                          </div>  
                          <div class="form-group">
                            <label for="maxcontact" class="col-sm-3 control-label">最大并发连接数</label>
                            <div class="col-sm-8" id="tab3group4">
                            	    <button type="button" id="maxcontact1" onclick="checkmaxcontact(this)" class="btn btn-default btn-sm margin-bottom-20">5,000</button>
	                            	<button type="button" id="maxcontact2" onclick="checkmaxcontact(this)" class="btn btn-default btn-sm margin-bottom-20">10,000</button>
	                            	<button type="button" id="maxcontact3" onclick="checkmaxcontact(this)" class="btn btn-default btn-sm margin-bottom-20">20,000</button>
	                            	<button type="button" id="maxcontact4" onclick="checkmaxcontact(this)" class="btn btn-default btn-sm margin-bottom-20">40,000</button>
                            </div>
                          </div>                            
                          <div class="form-group">
                            <label for="maxcontact" class="col-sm-3 control-label">是否保持会话</label>
                            <div class="col-sm-4">
	                                <button type="button" id="keepsession1" onclick="displaydiv(true)" class="btn btn-default btn-sm margin-bottom-20">是</button>
	                            	<button type="button" id="keepsession2" onclick="displaydiv(false)" class="btn btn-default btn-sm margin-bottom-20">否</button>
                            </div>
                          </div>                              
                          <div class="form-group" id="hiddendiv" style="display:none;">
	                        <label for="sessionkeep" class="col-sm-3 control-label">保持回话时长</label>
                            <div class="col-sm-4" id="tab3group5">
                              <input type="text" class="form-control" id="sessionkeep" parsley-trigger="change"   parsley-error-container="#tab3group5">
                            </div>
		                   </div>	
                         <div class="form-group">
                            <label for="timeout" class="col-sm-3 control-label">响应超时时间</label>
                            <div class="col-sm-4" id="tab3group6">
                              <input type="text" class="form-control" id="timeout" parsley-trigger="change" parsley-minlength="4" parsley-type="number" parsley-error-container="#tab3group6">
                            </div>
                          </div>        
                          <div class="form-group">
                            <label for="healthycheck" class="col-sm-3 control-label">健康检查间隔</label>
                            <div class="col-sm-4" id="tab3group7">
                              <input type="text" class="form-control" id="healthycheck" parsley-trigger="change" parsley-type="number"  parsley-error-container="#tab3group7">
                            </div>
                          </div>  
                          <div class="form-group">
                            <label for="nothealthy" class="col-sm-3 control-label">不健康阀值</label>
                            <div class="col-sm-4" id="tab3group8">
								<select class="chosen-select chosen-transparent form-control" id="nothealthy" parsley-trigger="change" parsley-required="true" parsley-error-container="#tab3group8">
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
                            <div class="col-sm-4" id="tab3group9">
								<select class="chosen-select chosen-transparent form-control" id="thealthyvalue" parsley-trigger="change" parsley-required="true" parsley-error-container="#tab3group9">
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
                                  
                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">关闭</button>
                            <button class="btn btn-green" onclick="addRow()">确定</button>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->    
                    <div class="modal fade" id="weight" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
                    	<div class="modal-dialog">
                        <div class="modal-content">
                          <div class="modal-header test">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabelthrees">设置权重</h3>
                          </div>
                          <div class="test" id="weight_div" style="height:200px;overflow:auto;background-color:rgba(0, 0, 0, 0.3);">
		                     <section class="tile color transparent-black">

                  <!-- tile body -->
                  <div class="tile-body">
                    <table  class="table table-datatable table-custom" id="weightTable" style="width:98%;">
	                        <thead>
	                          <tr>
	                            <th class="no-sort">ID</th>
	                            <th class="no-sort">名称</th>
	                            <th class="no-sort">状态</th>
	                            <th class="no-sort">权重</th>
	                          </tr>
	                        </thead>
	                        <tbody>
	                        <tr class="odd gradeX">
	                             <td class="cut">ELB_123456</td>
	                             <td class="cut">test_ELB</td>
	                             <td class="cut">运行中</td>
	                             <td class="cut">
	                             <input type="text" class="form-control" id="weight_input" parsley-trigger="change" parsley-minlength="4" parsley-type="number">
	                             </td>
	                        </tr>
	                        <tr class="odd gradeX">
	                             <td class="cut">ELB_123456</td>
	                             <td class="cut">test_ELB</td>
	                             <td class="cut">运行中</td>
	                             <td class="cut">
	                             <input type="text" class="form-control" id="weight_input2" parsley-trigger="change" parsley-minlength="4" parsley-type="number">
	                             </td>
	                        </tr>
	                        </tbody>
	                      </table>

                  </div>
                  <!-- /tile body -->

                </section>																																				
                          </div>
                          <div class="modal-footer test" style="margin-top:0px;">
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">关闭</button>
                            <button class="btn btn-green" onclick="saveWeight()">确定</button>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->                    
                    </div>
                

                </section>
                <!-- /tile -->
				  

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

    <script>
    $(function(){
        //check all checkboxes
        $('table thead input[type="checkbox"]').change(function () {
          $(this).parents('table').find('tbody input[type="checkbox"]').prop('checked', $(this).prop('checked'));
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
	       	  if(index=="1"){
	       		  tab.addClass('success');
	       	  }else{
	           var form = $('.form' + index)
	
	           form.parsley('validate');
	
	           if(form.parsley('isValid')) {
	             tab.addClass('success');       
	           } else {
	             return false;
	           }
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
		$("#ecsdiv,#portdiv,#confirmecs_table,#confirmelb_table,#loadbalance,#loadbalance_div,#weight_div").niceScroll({
			cursoropacitymin:0.5,
			cursorcolor:"#424242",  
			cursoropacitymax:0.5,  
			touchbehavior:false,  
			cursorwidth:"8px",  
			cursorborder:"0",  
			cursorborderradius:"7px" ,
		});
		var tempwidth = $("#balance_port").css("width");
		$("#balance_protocol").chosen({disable_search_threshold: 10,width:tempwidth});
		$("#instance_protocol").chosen({disable_search_threshold: 10,width:tempwidth});
		$("#nothealthy").chosen({disable_search_threshold: 10,width:tempwidth});
		$("#thealthyvalue").chosen({disable_search_threshold: 10,width:tempwidth});
      })
    </script>
    
  </body>
</html>
      
