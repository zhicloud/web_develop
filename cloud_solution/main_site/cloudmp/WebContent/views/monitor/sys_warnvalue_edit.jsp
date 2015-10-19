<%@ page pageEncoding="utf-8"%>
  <head>
    <title>规则内容编辑</title>
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
var tempruleid = "${ruleid}";
//返回
function backhome(){
	window.location.href = "<%=request.getContextPath() %>/syswarn/rulelist";
}
//检查表格里面内容
function checkTable(){ 
	var restr = "";
	var returnflag = true;
	var floatreg = /^[1-9]\d*(\.[0-9]{1,2})?|0\.[0-9]{1,2}$/;
	var wordreg  = /^[a-zA-Z]+(_)?[a-zA-Z]*$/;
	var betweenreg = /^([1-9]\d*(\.[0-9]{1,2})?|0\.[0-9]{1,2})-([1-9]\d*(\.[0-9]{1,2})?|0\.[0-9]{1,2})$/;
	var len = $("#unchoosetable tbody tr").length;
	for(var i=1;i<=len;i++){
		var name = $("#name"+i).val();
		var code = $("#code"+i).val();
		var value = $("#value"+i).val();
		var operator = $("#operator"+i).val();
		var contact = $("#contact"+i).val();
		if(name.length>50){
			restr = "第 "+i+" 行名称长度超出定义的20个字符";
			break;
		}
		if(code.length>50){
			restr = "第 "+i+" 行编码长度超出定义的20个字符";
			break;
		}
		if(!wordreg.test(code)){
			restr = "第 "+i+" 行编码格式不正确";
			break;
		}
		if(value.leng>20){
			restr = "第 "+i+" 行值长度超出定义的20个字符";
			break;
		}
		if(operator=="between"){
			if(!betweenreg.test(value)){
				restr = "第 "+i+" 行阀值格式不正确,请录入[0.00-0.00]的格式";
				break;
			}else{
				if(parseFloat(value.split("-")[1])<parseFloat(value.split("-")[0])){
					restr = "第 "+i+" 行阀值的值后面的值不能小于前面的值";
					break;
				}
			}
		}else{
			if(!floatreg.test(value)){
				restr = "第 "+i+" 行阀值格式不正确";
				break;
			}
		}
		if((i!=len)&&contact==""){
			restr = "第 "+i+" 行连接符不能为空";
			break;
		}
	}
	
	return restr;
}
//保存数据
function saveData(){
	var str = checkTable();
	if(str!=""){
 		$("#tipscontent").html(str);
 		$("#dia").click();
 		return;
	}else{
		var values = new Array();
		var len = $("#unchoosetable tbody tr").length;
		for(var i=1;i<=len;i++){
			var object = new Object();
			object["name"] = $("#name"+i).val();
			object["code"] = $("#code"+i).val();
			object["operator"] = $("#operator"+i).val();	
			object["value"] = $("#value"+i).val();
			object["contact"] = $("#contact"+i).val();
			object["sort"] = i;
			values.push(object);
		}
	    jQuery.ajax({
	        url: '<%=request.getContextPath()%>/syswarn/savevalue',
	        type: 'post',
	        dataType: 'json',
	        data:{ruleid:tempruleid,datas:JSON.stringify(values)},
	        async: false,
	        timeout: 10000,
	        error: function()
	        {
	        },
	        success: function(result)
	        {
	            if(result.status=="success"){
	                location.href = "<%=request.getContextPath()%>/syswarn/rulelist";
	            }else{
	                $("#tipscontent").html(result.message);
	                $("#dia").click();
	                return;
	            }
	        }

	    });
		
	}
}
//增加表格行数
function addRow(){
	var len = $("#unchoosetable tbody tr").length+1;
	var operator_special_div = $("#operator_special_div").html();
	var trhtml = "<tr><td><input type=\"text\"  class=\"form-control\" id=\"name"+len+"\" ><\/td>"+
	 "<td><input type=\"text\"  class=\"form-control\" id=\"code"+len+"\" ><\/td>"+
	 "<td><select class=\"chosen-select chosen-transparent form-control\"  id=\"operator"+len+"\">"+
     "<option value=\"equal\">等于<\/option>"+  
     "<option value=\"high\" >大于<\/option>"+ 
     "<option value=\"low\"  >小于<\/option>"+ 
     "<option value=\"between\" >在...之间<\/option><\/select><\/td>"+
	 "<td><input type=\"text\" class=\"form-control\" id=\"value"+len+"\" ><\/td>"+
	 "<td><select class=\"chosen-select chosen-transparent form-control\"  id=\"contact"+len+"\">"+
     "<option value=\"\">空<\/option>"+  
     "<option value=\"and\">并且<\/option>"+
     "<option value=\"or\" >或者<\/option><\/select></\td>"+
	  "<\/tr>";
$("#unchoosetable tbody").append(trhtml);
$("#operator"+len).chosen({disable_search:true});
$("#contact"+len).chosen({disable_search:true});
}
//删除表格行
function deleteRow(){
	var len = $("#unchoosetable tbody tr").length;
	if(len>1){
		$("#unchoosetable tbody").find("tr:last").remove();
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
            	规则内容编辑
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
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations" style="margin-top:0px;">
					<div id="operator_special_div" style="display:none;">
					<select class="chosen-select chosen-transparent form-control" name="operator" id="operator${status.count}">
			                            <option value="equal" >等于</option>  
			                            <option value="high" >大于</option> 
			                            <option value="low" >小于</option> 
			                            <option value="between" >在...之间</option> 
		             </select>
		             </div>
					<table  class="table table-datatable table-custom" id="unchoosetable" style="font-size: 9px;">
                      <thead>
                          <tr>
						  <th style="width:20%;">名称</th>
						  <th style="width:20%;">编码</th>
						  <th style="width:20%;">操作符</th>
						  <th style="width:20%;">阀值</th>
						  <th style="width:20%;">连接符</th>
                          </tr>
                        </thead>
						<tbody>       
						<c:if test="${!empty valueVOS}">
	                     	<c:forEach items="${valueVOS }" var="valuevo" varStatus="status">
	                     		<tr>
	                     		<td><input type="text" value="${valuevo.name }" class="form-control" id="name${status.count}" ></td>
								<td><input type="text" value="${valuevo.code }" class="form-control" id="code${status.count}" ></td>
								<td>
								<select class="chosen-select chosen-transparent form-control" name="operator" id="operator${status.count}">
			                            <option value="equal" <c:if test="${valuevo.operator=='equal' }">selected</c:if> >等于</option>  
			                            <option value="high" <c:if test="${valuevo.operator=='high' }">selected</c:if> >大于</option> 
			                            <option value="low" <c:if test="${valuevo.operator=='low' }">selected</c:if> >小于</option> 
			                            <option value="between" <c:if test="${valuevo.operator=='between' }">selected</c:if> >在...之间</option> 
		                        </select>
								</td>
								<td><input type="text" value="${valuevo.value }" class="form-control" id="value${status.count}" ></td>
								<td>
								<select class="chosen-select chosen-transparent form-control" name="contact" id="contact${status.count}">
		                            <option value="" <c:if test="${valuevo.contact=='' }">selected</c:if>>空</option>  
		                            <option value="and" <c:if test="${valuevo.contact=='and' }">selected</c:if>>并且</option> 
		                            <option value="or" <c:if test="${valuevo.contact=='or' }">selected</c:if>>或者</option> 
	                            </select>
								</td>
								</tr>
	                     </c:forEach>
	                     </c:if>   
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
