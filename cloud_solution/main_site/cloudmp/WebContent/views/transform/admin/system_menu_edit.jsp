<%@ page pageEncoding="utf-8"%>
  <head>
    <title>功能菜单编辑</title>
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
var tempbillid = "${systemMenu.billid}";
var parentid = "${parentid}";
var isCommited = false;
// 保存表单信息
function saveForm(){
	var form = $(".form-horizontal")	
	form.parsley('validate');
	if(form.parsley('isValid')){
		if(isCommited){
     		return false;
		} 
		isCommited = true;		
		
		var menuname = $("#menuname").val();
		var linkname = $("#linkname").val();
		var sort = $("#sort").val();
		var cssname = $("#cssname").val();
		var status = $("#status").val();
		var remark = $("#remark").val();
		var param = "menuname="+menuname+"&linkname="+linkname+"&sort="+sort+"&status="+status+"&remark="+remark+"&parentid="+parentid+"&cssname="+cssname;
		var url = "<%=request.getContextPath()%>/transform/menuadmin/savemenu";
		jQuery.ajax({
	  	 	type: "POST",
	  	 	async:false,
	   		url: url,
	  		data: "billid="+tempbillid+"&modflag="+${modflag}+"&"+param,
	  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
	   		success: function(result){
	   		  var obj = eval("("+result+")");
	     	if(obj.status=="success"){
	     		window.location.href = "<%=request.getContextPath()%>/transform/menuadmin/index";
	     	}else{
	     		$("#tipscontent").html(obj.result);
	     		$("#dia").click();
	     		isCommited = false;
	     		return;
	     	}
	   	}
		});		
		
	}else{
		return;
	}
}
//返回
function backhome(){
	window.location.href = "<%=request.getContextPath() %>/transform/menuadmin/index";
}
</script>
<%@include file="/views/common/common_menus.jsp" %>
    <!-- Make page fluid -->
    <div class="row">
<!-- Page content -->
        <div id="content" class="col-md-12">

          <!-- page header -->
          <div class="pageheader">
            
            <h2><i class="fa fa-chain-broken"></i> 
            <c:if test="${empty systemMenu}">创建菜单</c:if>
            <c:if test="${!empty systemMenu}">修改菜单</c:if>	
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
                    <h3> 
                       <c:if test="${parentid == null || parentid ==''}">
                          <a href="<%=request.getContextPath() %>/transform/menuadmin/index"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>                      
                       </c:if>
                       <c:if test="${parentid != null}">
                          <a href="<%=request.getContextPath() %>/transform/menuadmin/children?parentid=${parentid}"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>                      
                       </c:if>
                      
                      编辑菜单信息</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body" style="padding-bottom:0px;margin-bottom:-45px;">
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations">
                     <c:if test="${empty systemMenu}">
                     
                      <div class="form-group">
                        <label for="menuname" class="col-sm-2 control-label">菜单名称*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="menuname" parsley-required="true"  parsley-maxlength="40">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="linkname" class="col-sm-2 control-label">连接路径</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="linkname"  parsley-maxlength="200">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="sort" class="col-sm-2 control-label">显示顺序*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="sort" parsley-required="true" parsley-type="number">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="cssname" class="col-sm-2 control-label">图标样式</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="cssname" parsley-maxlength="100">
                        </div>
                      </div>                      
                      <div class="form-group">
                        <label for="status" class="col-sm-2 control-label">状态 *</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" id="selectbox">
                            <select class="chosen-select chosen-transparent form-control" id="status" parsley-required="true" parsley-error-container="#selectbox">
                            <option value="">请选择</option>  
                            <option value="0">正常</option>  
                            <option value="1">禁用</option> 
                          </select></div> 
                        </div>
                      </div> 
                      
                      <div class="form-group">
                        <label for="remark" class="col-sm-2 control-label">备注信息</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="remark" parsley-maxlength="200">
                        </div>
                      </div>                       
                      </c:if>
                      <!-- modify -->
                      <c:if test="${!empty systemMenu }">
                      <div class="form-group">
                        <label for="menuname" class="col-sm-2 control-label">菜单名称*</label>
                        <div class="col-sm-4">
                          <input value="${systemMenu.menuname }" type="text" class="form-control" id="menuname" parsley-required="true"  parsley-maxlength="40">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="linkname" class="col-sm-2 control-label">连接路径</label>
                        <div class="col-sm-4">
                          <input value="${systemMenu.linkname }" type="text" class="form-control" id="linkname"  parsley-maxlength="200">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="sort" class="col-sm-2 control-label">显示顺序*</label>
                        <div class="col-sm-4">
                          <input value="${systemMenu.sort }" type="text" class="form-control" id="sort" parsley-required="true" parsley-type="number">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="cssname" class="col-sm-2 control-label">图标样式</label>
                        <div class="col-sm-4">
                          <input value="${systemMenu.cssname }" type="text" class="form-control" id="cssname" parsley-maxlength="100">
                        </div>
                      </div>                      
                      <div class="form-group">
                        <label for="status" class="col-sm-2 control-label">状态 *</label>
                        <div class="col-sm-4"> 
                            <div class="col-sm-16" id="selectbox">
                            <select class="chosen-select chosen-transparent form-control" id="status" parsley-required="true" parsley-error-container="#selectbox">
                            <option value="0" <c:if test="${systemMenu.status==0 }">selected</c:if> >正常</option>  
                            <option value="1" <c:if test="${systemMenu.status==1 }">selected</c:if> >禁用</option> 
                          </select></div> 
                        </div>
                      </div>  
                      <div class="form-group">
                        <label for="remark" class="col-sm-2 control-label">备注信息</label>
                        <div class="col-sm-4">
                          <input value="${systemMenu.remark }" type="text" class="form-control" id="remark" parsley-maxlength="200">
                        </div>
                      </div>                                            
                      </c:if>      
                                   
                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8">
                          <button type="button" class="btn btn-greensea" onclick="saveForm()"><i class="fa fa-plus"></i>
                              <span> 保 存 </span></button>
                          <button type="button" class="btn btn-default" onclick="backhome()"><i class="fa fa-backward"></i>
                              <span> 返回 </span></button>
                        </div>
                      </div>
                            
                    </form>

                  </div>
                  
                </section>
                <!-- /tile -->
                
                <!-- /tile body -->
				<div class="tile-body">

                    <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                    
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
