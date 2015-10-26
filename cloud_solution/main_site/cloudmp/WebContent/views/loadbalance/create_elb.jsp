<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>创建ELB</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8" />
  </head>
<script type="text/javascript">
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
		$("#hiddendiv").css("display","block");
	}else{
		$("#hiddendiv").css("display","none");
	}
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
                      <li><a href="#tab2" data-toggle="tab">配置健康检查</a></li>
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

                      <div class="tab-pane" id="tab2">

                        <form class="form-horizontal form2" role="form" parsley-validate id="contact" style="width:60%;">
                         <p>负载均衡器将自动对ECS进行健康检查，它只将网络访问指向通过了健康检查的ECS。如果一个ECS的健康检查失败，负载均衡器将不会将网络访问指向这个ECS。请根据您的具体需求自定义健康检查的具体指标。</p>
                          <div class="form-group">
                            <label for="checkprotocol" class="col-sm-3 control-label">检查协议</label>
                            <div class="col-sm-2" id="tab2group1">
								<select class="chosen-select chosen-transparent form-control" style="width:100px;" id="checkprotocol" parsley-trigger="change" parsley-required="true" parsley-error-container="#tab2group1">
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
                            <div class="col-sm-4" id="tab2group2" style="width:230px;">
                              <input type="text" class="form-control" id="timeout" parsley-trigger="change" parsley-minlength="4" parsley-type="number" parsley-error-container="#tab2group2" parsley-validation-minlength="1">
                            </div>
                          </div>

                          <div class="form-group">
                            <label for="healthycheck" class="col-sm-3 control-label">健康检查间隔</label>
                            <div class="col-sm-4" id="tab2group3" style="width:230px;">
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
                        </form>

                      </div>
                      
                      <div class="tab-pane" id="tab3" style="height:500px;">
                        <div style="width:70%;float: left;">
                        	<span>定义负债均衡</span>
                        	<p>请定义您要创建的负载均衡器名称及监听端口等。</p>
                        	<form class="form-horizontal form3" role="form" parsley-validate id="tab3form">
	                          <div class="form-group">
	                            <label for="elb_name" class="col-sm-3 control-label">ELB名称</label>
	                            <div class="col-sm-4" id="tab3group1" style="width:230px;">
	                              <input type="text" class="form-control" id="elb_name" parsley-trigger="change"   parsley-error-container="#tab3group1">
	                            </div>
	                          </div>
	                         <div class="form-group">
	                            <label for="tab3group2" class="col-sm-3 control-label">最大并发连接数</label>
	                            <div class="col-sm-6" id="tab3group2" style="width:480px;float: left;">
	                            	<div style="float:left;">
	                            	<button type="button" id="maxcontact1" onclick="checkmaxcontact(this)" class="btn btn-default btn-sm margin-bottom-20">5,000</button>
	                            	<button type="button" id="maxcontact2" onclick="checkmaxcontact(this)" class="btn btn-default btn-sm margin-bottom-20">10,000</button>
	                            	<button type="button" id="maxcontact3" onclick="checkmaxcontact(this)" class="btn btn-default btn-sm margin-bottom-20">20,000</button>
	                            	<button type="button" id="maxcontact4" onclick="checkmaxcontact(this)" class="btn btn-default btn-sm margin-bottom-20">40,000</button>
	                            	</div>
	                          </div>
	                          </div>
		                      <div class="form-group">
		                        <label for="elb_name" class="col-sm-3 control-label">是否保持回话</label>
		                        <div class="col-sm-8">
		                          <div class="radio radio-transparent">
		                            <input type="radio" name="optionsRadios" id="optionsRadios1" value="option1" onclick="displaydiv(true)">
		                            <label for="optionsRadios1">是</label>
		                            <input type="radio" name="optionsRadios" id="optionsRadios2" value="option2" onclick="displaydiv(false)" checked>
		                            <label for="optionsRadios2">否</label>
		                          </div>
		                        </div>
		                      </div>
		                      <div class="form-group" id="hiddendiv" style="display:none;">
			                        <label for="sessionkeep" class="col-sm-3 control-label">保持回话时长</label>
		                            <div class="col-sm-4" id="tab3group4" style="width:230px;">
		                              <input type="text" class="form-control" id="sessionkeep" parsley-trigger="change"   parsley-error-container="#tab3group4">
		                            </div>
		                      </div>		                      
		                      <div class="form-group" style="margin-left:0px;">
		                        <p style="padding-left:20px;">编辑服务端口</p>
		                        <div class="col-sm-8" style="padding-left:20px;height:150px;" id="portdiv">
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
		                        </div>
		                       <div class="form-group" style="margin-bottom:5px;position: relative;margin-top:5px;margin-left:0px;">
			                        <div class="col-sm-8">
			                          <button type="button" class="btn btn-greensea" onclick="addRow()"><i class="fa fa-plus"></i>
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
		$("#ecsdiv,#portdiv,#confirmecs_table,#confirmelb_table").niceScroll({
			cursoropacitymin:0.5,
			cursorcolor:"#424242",  
			cursoropacitymax:0.5,  
			touchbehavior:false,  
			cursorwidth:"8px",  
			cursorborder:"0",  
			cursorborderradius:"7px" ,
		});
		$("#checkprotocol").chosen({disable_search:true,width:'200px'});
		$("#nothealthy").chosen({disable_search:true,width:'200px'});
		$("#thealthyvalue").chosen({disable_search:true,width:'200px'});
		
		$("#price").chosen({disable_search_threshold: 10,width:'100px'});
		$("#elb_protocol").chosen({disable_search_threshold: 10,width:'100px'});
		$("#instance_protocol").chosen({disable_search_threshold: 10,width:'100px'});
      })
    </script>
    
  </body>
</html>
      
