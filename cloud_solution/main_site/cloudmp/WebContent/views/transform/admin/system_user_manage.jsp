<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
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
            

            <h2><i class="fa fa-user"></i> 用户管理</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              
              <!-- col 6 -->
          <div class="col-md-12">

				  <section class="tile color transparent-black">


 
                  <!-- tile header -->
                  <div class="tile-header"> 
                     <button type="button" class="btn btn-red add" onclick="updateuser('add',null)">
                              <i class="fa fa-plus"></i>
                              <span> 新增用户</span>
                    </button>
                    <button type="button" class="btn btn-green file-excel-o" onclick="exportData('/export/userdata')">
                              <i class="fa fa-file-excel-o"></i>
                              <span>导出数据</span>
                    </button>
                  </div>
                  <!-- /tile header -->
                      <div class="tile-widget bg-transparent-black-2">
                          <div class="row">
                      <div class="col-sm-6 col-xs-6" style="z-index: 100;">
                          <div class="input-group table-options">
                          <span class="input-group-btn">
                                <input id="param" type="text" name="param" value="${parameter == null?"":parameter}"/>
                            </span>
                          <span class="input-group-btn">
                              <select id="status" class="chosen-select form-control" style="width: 150px;">
                                  <option value="">状态(全部)</option>
                                  <c:if test="${status == 0}">
                                      <option value="0" selected="selected">正常</option>
                                  </c:if>
                                  <c:if test="${status != 0}">
                                      <option value="0">正常</option>
                                  </c:if>
                                  <c:if test="${status == 1}">
                                      <option value="1" selected="selected">停用</option>
                                  </c:if>
                                  <c:if test="${status != 1}">
                                      <option value="1">停用</option>
                                  </c:if>
                              </select>
                              <select id="userType" class="chosen-select form-control" style="width: 150px;">
                                  <option value="">用户类型(全部)</option>
                                  <c:if test="${userType == 0}">
                                      <option value="0" selected="selected">管理员用户</option>
                                  </c:if>
                                  <c:if test="${userType != 0}">
                                      <option value="0">管理员用户</option>
                                  </c:if>
                                  <c:if test="${userType == 1}">
                                      <option value="1" selected="selected">租户管理员用户</option>
                                  </c:if>
                                  <c:if test="${userType != 1}">
                                      <option value="1">租户管理员用户</option>
                                  </c:if>
                              </select>
                              <%--</span>--%>
                           <%--<span class="input-group-btn">--%>
                                <button id="search_btn" class="btn btn-default" type="button">查看</button>
                              </span>
                          </div>
                      </div>
                    </div>
                  </div>
                       
                  <div class="tile-body no-vpadding"> 
                   
                    <div class="table-responsive">
                    
                      <table  class="table table-datatable table-custom" id="basicDataTable">
                        <thead>
                          <tr>
						    <th class="no-sort">
                            <div class="checkbox check-transparent">
                              <input type="checkbox"  id="allchck">
                              <label for="allchck"></label>
                            </div>
                          </th>
                            <th>用户账号</th>
                            <th>显示名称</th>
                            <th>用户类型</th>                            
                            <th>邮箱</th>
                            <th>联系电话</th>
                            <th>状态</th>
                            <th>创建时间</th>
                            <th class="no-sort">操作</th>                            
                          </tr>
                        </thead>
                        <tbody>
                        
					<c:forEach items="${systemUserList}" var="systemUser">
                    		<tr class="odd gradeX">
						    <td>
									<div class="checkbox check-transparent">
									  <input type="checkbox" value="${systemUser.billid }" id="${systemUser.billid }">
									  <label for="${systemUser.billid }"></label>
									</div>
                             </td>
                            <td class="cut">${systemUser.usercount }</td>
                            <td class="cut">${systemUser.displayname }</td>
                           <td class="cut">
                            <c:if test="${systemUser.userType==0}">管理员用户</c:if>
                            <c:if test="${systemUser.userType==1}">租户管理员用户</c:if>
                            </td>                          
                            <td class="cut">${systemUser.email }</td>
                            <td class="cut">${systemUser.telphone }</td>
                            <td class="cut">
                            <c:if test="${systemUser.status==0}">正常</c:if>
                            <c:if test="${systemUser.status==1}">禁用</c:if>
                            </td>
                            <td class="cut">${systemUser.insert_date }</td>
							<td> 
                              <div class="btn-group">
                                  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown">
                                    		操作 <span class="caret"></span>
                                  </button>
                                  <ul class="dropdown-menu" role="user"> 
                                    <li><a href="#"  onclick="updateuser('modify','${systemUser.billid}')">修改基本信息</a></li>
                                    <li><a href="#" role="button" data-toggle="modal" onclick="beforeresetpassword('${systemUser.billid}', '${systemUser.email }')">重置用户密码</a></li>
                                    <li><a href="#" onclick="deletedataone('${systemUser.billid}')">删除用户信息</a></li>
                                    <li><a href="#" onclick="setuserstatus('${systemUser.billid}','${systemUser.status }')">设置用户状态</a></li>
                                    <li><a href="#" onclick="getUserRoleGroup('${systemUser.billid}')">查看用户授权</a></li>
                                   <%--  <li><a href="#" onclick="setuserusbstatus('${systemUser.billid}','${systemUser.usb_status }')">设置USB权限</a></li> --%>
                                  </ul>
                              </div>
                            </td>                                                        
                        </tr> 
                    	</c:forEach>                         
                        </tbody>
                      </table>
                      
                    </div>
                    
                  </div>
                  <!-- /tile body -->
                    <div class="col-sm-2" style="margin-top: -40px;">
                        <div class="input-group table-options">
                          <select class="chosen-select form-control" id="multyselect">
                            <option value="opp">批量操作</option> 
                            <option value="delete">删除</option>
                            <option value="status">用户状态</option>
                          </select>
                          <span class="input-group-btn">
                            <button class="btn btn-default" type="button" onclick="multyopp()">提交</button>
                          </span>
                        </div>
                      </div>    
                      
                     <!-- 修改密码弹出框 -->    
                    <div class="modal fade" id="userpassword" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel">修改用户密码</h3>
                          </div>
                          <div class="modal-body">
                            <form role="form" id="userpassform" parsley-validate>
                              
                              <div class="form-group">
                                <label for="oldpassword">旧密码:</label>
                                <input type="password" class="form-control" id="oldpassword" parsley-required="true" parsley-maxlength="40">
                              </div>

                              <div class="form-group">
                                <label for="newpassword">新密码:</label>
                                <input type="password" class="form-control" id="newpassword" parsley-required="true" parsley-maxlength="40">
                              </div>

                              <div class="form-group">
                                <label for="confirmpassword">确认新密码:</label>
                                <input type="password" class="form-control" id="confirmpassword" parsley-required="true" parsley-maxlength="40">
                              </div>
                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">关闭</button>
                            <button class="btn btn-green" onclick="saveuserpassword()">保存</button>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div>  
                    
                <!-- 设置用户状态弹出框 -->    
				<div class="modal fade" id="userstatus" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabeltwo">设置用户状态</h3>
                          </div>
                          <div class="modal-body">
                            <form role="form" id="userstatusform" class="form-horizontal">
                              
                              <div class="form-group" style="margin-left:40%;float:left;width:20%;">
		                            <input type="radio" name="userstatusinput" value="0" id="userstatusinput1">
		                            <label for="userstatusinput1">正常</label>
                              </div>
                              <div class="form-group" style="margin:0;float:left;width:20%;">
		                            <input type="radio" name="userstatusinput" value="1" id="userstatusinput2">
		                            <label for="userstatusinput2">禁用</label>
                              </div>
                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">关闭</button>
                            <button class="btn btn-green" onclick="saveuserstatus()">保存</button>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div>  
				<!-- 批量设置用户状态弹出框 -->    
				<div class="modal fade" id="userstatuss" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabeltwos">设置用户状态</h3>
                          </div>
                          <div class="modal-body">
                            <form role="form" id="userstatusforms" class="form-horizontal">
                              
                              <div class="form-group" style="margin-left:40%;float:left;width:20%;">
		                            <input type="radio" name="userstatusinputs" value="0" id="userstatusinput1s">
		                            <label for="userstatusinput1s">正常</label>
                              </div>
                              <div class="form-group" style="margin:0;float:left;width:20%;">
		                            <input type="radio" name="userstatusinputs" value="1" id="userstatusinput2s">
		                            <label for="userstatusinput2s">禁用</label>
                              </div>
                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">关闭</button>
                            <button class="btn btn-green" onclick="saveuserstatuss()">保存</button>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div>                      
				<!-- 设置用户USB权限弹出框 -->    
				<div class="modal fade" id="userusbstatus" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabelthree">设置用户USB权限</h3>
                          </div>
                          <div class="modal-body">
                            <form role="form" id="userusbstatusform" class="form-horizontal">
                              
                              <div class="form-group" style="margin-left:40%;float:left;width:20%;">
		                            <input type="radio" name="userusbstatusinput" value="0" id="userusbstatusinput1">
		                            <label for="userusbstatusinput1">未开启</label>
                              </div>
                              <div class="form-group" style="margin:0;float:left;width:20%;">
		                            <input type="radio" name="userusbstatusinput" value="1" id="userusbstatusinput2">
		                            <label for="userusbstatusinput2">开启</label>
                              </div>
                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">关闭</button>
                            <button class="btn btn-green" onclick="saveuserusbstatus()">保存</button>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div>                     
				<!-- 批量设置用户USB权限弹出框 -->    
				<div class="modal fade" id="userusbstatuss" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabelthrees">设置用户USB权限</h3>
                          </div>
                          <div class="modal-body">
                            <form role="form" id="userusbstatusforms" class="form-horizontal">
                              
                              <div class="form-group" style="margin-left:40%;float:left;width:20%;">
		                            <input type="radio" name="userusbstatusinputs" value="0" id="userusbstatusinput1s">
		                            <label for="userusbstatusinput1s">未开启</label>
                              </div>
                              <div class="form-group" style="margin:0;float:left;width:20%;">
		                            <input type="radio" name="userusbstatusinputs" value="1" id="userusbstatusinput2s">
		                            <label for="userusbstatusinput2s">开启</label>
                              </div>
                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">关闭</button>
                            <button class="btn btn-green" onclick="saveuserusbstatuss()">保存</button>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div>                      
                    
                                                           
				<div class="tile-body">

                    <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                    <a href="#modalConfirm" id="con" role="button"   data-toggle="modal"> </a>
                    <a href="#modalConfirmone" id="one" role="button"   data-toggle="modal"> </a>
                    <a href="#modalConfirmtwo" id="confirmtwo" role="button"   data-toggle="modal"> </a>
                    <a href="#userstatus" id="two" role="button"   data-toggle="modal"> </a>
                    <a href="#userusbstatus" id="three" role="button"   data-toggle="modal"> </a>
                    <a href="#userstatuss" id="twos" role="button"   data-toggle="modal"> </a>
                    <a href="#userusbstatuss" id="threes" role="button"   data-toggle="modal"> </a>
                    
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
                                <label style="align:center;" id="confirmcontent">确定要删除所选数据？</label>
                               </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green"   onclick="confirmreturn()" data-dismiss="modal" aria-hidden="true">确定</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->    
                                        
					<div class="modal fade" id="modalConfirmone" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabelone"><strong>确认</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form role="form">   

                              <div class="form-group">
                                <label style="align:center;" id="confirmcontentone">确定要删除该条数据？</label>
                               </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green"   onclick="confirmreturnone()" data-dismiss="modal" aria-hidden="true">确定</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal --> 
                    <!-- 重置密码 -->
                    <div class="modal fade" id="modalConfirmtwo" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabeltwo"><strong>确认</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form role="form">   

                              <div class="form-group" id="randomdiv">
                                <label style="align:center;" id="confirmcontenttwo">确定要重置用户密码？</label>
                               </div>
							  <div class="form-group" style="display:none;" id="manualdiv">
                                <label for="manualpass">新密码:</label>
                                <input type="text" class="form-control" id="manualpass" parsley-required="true" parsley-maxlength="6"  placeholder="请输入1-6位字符">
                              </div>
                            </form>
                          </div>
                          <div class="modal-footer" id="resetpassdiv">
                            <button class="btn btn-green" id = "pass_manual_btn"  onclick="beforemanual()"  aria-hidden="true">手动输入</button>
                            <button class="btn btn-green" id = "pass_confirm_btn"  onclick="confirmreturntwo()" data-dismiss="modal" aria-hidden="true">随机</button>
                            <button class="btn btn-red" id="cancel_btn" data-dismiss="modal" aria-hidden="true">取消</button>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal --> 
                    </div>
                </section>
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


	<script type="text/javascript">
	var tempuserid = "";
    var email="";
	//修改信息页面跳转
	function updateuser(obj,billid){
		window.location.href = "<%=request.getContextPath()%>/transform/useradmin/beforeedit?type="+obj+"&billid="+billid;
	}
	//删除数据
	function confirmreturn(){
		var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
		var billids = new Array();
		$(datatable).each(function(){
				billids.push($(this).attr("value"));
		});	
		jQuery.ajax({
	  	 	type: "POST",
	  	 	async:false,
	   		url: "<%=request.getContextPath() %>/transform/useradmin/deleteuser",
	  		data: "billids="+billids.join(","),
	  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
	   		success: function(result){
	   		  var obj = eval("("+result+")");
	     	if(obj.status=="success"){
	     		window.location.reload();
	     	}else{
	     		$("#tipscontent").html(obj.result);
	     		$("#dia").click();
	     	}
	   	}
		});		
	}
	//删除数据
	function deletedata(){
		var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
		if(datatable.length==0){
	 		$("#tipscontent").html("请选择用户");
	 		$("#dia").click();
			return;
		}
		$("#con").click();
	}
	//删除单个数据
	function confirmreturnone(){
		jQuery.ajax({
	  	 	type: "POST",
	  	 	async:false,
	   		url: "<%=request.getContextPath() %>/transform/useradmin/deleteuser",
	  		data: "billids="+tempuserid,
	  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
	   		success: function(result){
	   		  var obj = eval("("+result+")");
	     	if(obj.status=="success"){
	     		window.location.reload();
	     	}else{
	     		$("#tipscontent").html(obj.result);
	     		$("#dia").click();
	     	}
	   	}
		});		
	}
	//单个删除数据
	function deletedataone(billid){
		tempuserid = billid;
		$("#one").click();
	}
	//设置用户状态
	function setuserstatus(billid,status){
		tempuserid = billid;
		if(status==0){
			$("#userstatusinput1").get(0).checked = true;
		}
		if(status==1){
			$("#userstatusinput2").get(0).checked = true;
		}
		$("#two").click();
	}
	//批量设置用户状态
	function setuserstatuss(){
		var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
		if(datatable.length==0){
	 		$("#tipscontent").html("请选择用户");
	 		$("#dia").click();
			return;
		}	
		$("#twos").click();
	}
	//设置用户USB权限
	function setuserusbstatus(billid,status){
		tempuserid = billid;
		if(status==0){
			$("#userusbstatusinput1").get(0).checked = true;
		}
		if(status==1){
			$("#userusbstatusinput2").get(0).checked = true;
		}
		$("#three").click();
	}
	//批量设置用户USB权限
	function setuserusbstatuss(){
		var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
		if(datatable.length==0){
	 		$("#tipscontent").html("请选择用户");
	 		$("#dia").click();
			return;
		}	
		$("#threes").click();
	}
	//保存用户状态
	function saveuserstatus(){
		var value = $("input[name=userstatusinput]:checked").val();
		jQuery.ajax({
	  	 	type: "POST",
	  	 	async:false,
	   		url: "<%=request.getContextPath() %>/transform/useradmin/updateuserstatus",
	  		data: "billids="+tempuserid+"&value="+value+"&statusflag=status",
	  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
	   		success: function(result){
	   		  var obj = eval("("+result+")");
	     	if(obj.status=="success"){
	     		window.location.reload();
	     	}else{
	     		$("#tipscontent").html(obj.result);
	     		$("#dia").click();
	     	}
	   	}
		});	
	}
	//批量保存用户状态
	function saveuserstatuss(){
		var value = $("input[name=userstatusinputs]:checked").val();
		var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
		var billids = new Array();
		$(datatable).each(function(){
				billids.push($(this).attr("value"));
		});		
		jQuery.ajax({
	  	 	type: "POST",
	  	 	async:false,
	   		url: "<%=request.getContextPath() %>/transform/useradmin/updateuserstatus",
	  		data: "billids="+billids+"&value="+value+"&statusflag=status",
	  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
	   		success: function(result){
	   		  var obj = eval("("+result+")");
	     	if(obj.status=="success"){
	     		window.location.reload();
	     	}else{
	     		$("#tipscontent").html(obj.result);
	     		$("#dia").click();
	     	}
	   	}
		});	
	}
	//保存用户USB权限
	function saveuserusbstatus(){
		var value = $("input[name=userusbstatusinput]:checked").val();
		jQuery.ajax({
	  	 	type: "POST",
	  	 	async:false,
	   		url: "<%=request.getContextPath() %>/transform/useradmin/updateuserstatus",
	  		data: "billids="+tempuserid+"&value="+value+"&statusflag=usb_status",
	  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
	   		success: function(result){
	   		  var obj = eval("("+result+")");
	     	if(obj.status=="success"){
	     		window.location.reload();
	     	}else{
	     		$("#tipscontent").html(obj.result);
	     		$("#dia").click();
	     	}
	   	}
		});	
	}
	//批量保存用户USB权限
	function saveuserusbstatuss(){
		var value = $("input[name=userusbstatusinputs]:checked").val();
		var datatable = $("#basicDataTable").find("tbody tr input[type=checkbox]:checked");
		var billids = new Array();
		$(datatable).each(function(){
				billids.push($(this).attr("value"));
		});			
		jQuery.ajax({
	  	 	type: "POST",
	  	 	async:false,
	   		url: "<%=request.getContextPath() %>/transform/useradmin/updateuserstatus",
	  		data: "billids="+billids+"&value="+value+"&statusflag=usb_status",
	  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
	   		success: function(result){
	   		  var obj = eval("("+result+")");
	     	if(obj.status=="success"){
	     		window.location.reload();
	     	}else{
	     		$("#tipscontent").html(obj.result);
	     		$("#dia").click();
	     	}
	   	}
		});	
	}
	function getUserRoleGroup(userid){
		jQuery.ajax({
	  	 	type: "POST",
	  	 	async:false,
	   		url: "<%=request.getContextPath() %>/transform/useradmin/getUserRoleGroup",
	  		data: "billids="+userid,
	  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
	   		success: function(result){
	   		var obj = eval("("+result+")");
	     	if(obj.status=="success"){
	     		$("#tipscontent").html(obj.result);
	     		$("#dia").click();
	     	}
	   	}
		});	
	}
	
	
	
	//批量操作
	function multyopp(){
		var op = $("#multyselect").val();
		if(op=="delete"){
			deletedata();
		}
		if(op=="status"){
			setuserstatuss();
		}
		if(op=="usbstatus"){
			setuserusbstatuss();
		}	
		
	}
	//修改密码保存
	function saveuserpassword(){
		var form = $("#userpassform");	
		form.parsley('validate');
		if(form.parsley("isValid")){
			var oldpassword = $("#oldpassword").val();
			var newpassword = $("#newpassword").val();
			var confirmpassword = $("#confirmpassword").val();
			if(newpassword!=confirmpassword){
				$("#tipscontent").html("两次输入的密码不一致,请重新输入");
				$("#dia").click();
				return;
			}
			var param = "billid="+tempuserid+"&oldpassword="+oldpassword+"&newpassword="+oldpassword;
			var url = "<%=request.getContextPath()%>/transform/useradmin/updatepassword";
			jQuery.ajax({
		  	 	type: "POST",
		  	 	async:false,
		   		url: url,
		  		data: param,
		  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
		   		success: function(result){
		   		  var obj = eval("("+result+")");
		     	if(obj.status=="success"){
		     		window.location.reload();
		     	}else{
		     		$("#tipscontent").html(obj.result);
		     		$("#dia").click();
		     		return;
		     	}
		   	}
			});		
			
		}else{
			return;
		}
	}
	//弹出框前设置用户的ID
	function beforeopendialog(obj){
		tempuserid = obj;
	}
	//重置密码之前
	function beforeresetpassword(obj, email){
		tempuserid = obj;
        this.email = email;
		$("#randomdiv").css("display","block");
		$("#manualdiv").css("display","none");
		var pass_confirm_btn = $("#pass_confirm_btn");
		if(pass_confirm_btn==null||pass_confirm_btn==undefined||pass_confirm_btn.length==0){
			var temphtml = "<button class=\"btn btn-green\" id = \"pass_confirm_btn\"  onclick=\"confirmreturntwo()\" data-dismiss=\"modal\" aria-hidden=\"true\">随机<\/button>";
			var temphtml2 = "<button class=\"btn btn-green\" id = \"pass_manual_btn\"  onclick=\"beforemanual()\"  aria-hidden=\"true\">手动输入<\/button>";
			$("#cancel_btn").before(temphtml2);
			$("#cancel_btn").before(temphtml);
			$("#save_confirm_btn").remove();
		}
		
		$("#confirmtwo").click();
	}
	//重置密码
	function confirmreturntwo(){
			jQuery.ajax({
		  	 	type: "POST",
		  	 	async:true,
		   		url: "<%=request.getContextPath() %>/transform/useradmin/resetpassword",
		  		data: "billid="+tempuserid,
		  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
		   		success: function(result){
		   		  var obj = eval("("+result+")");
		     	if(obj.status=="success"){
		     		$("#pass_confirm_btn").attr("disabled","");
		     		$("#tipscontent").html(obj.result);
		     		$("#dia").click();
		     		return;
		     	}else{
		     		$("#pass_confirm_btn").attr("disabled","");
		     		$("#tipscontent").html(obj.result);
		     		$("#dia").click();
		     		return;
		     	}
		   	}
			});	
	 		$("#tipscontent").html("正在重置密码，请稍后...");
	 		$("#dia").click();
	}
	//手动重置密码
	function beforemanual(){
		$("#randomdiv").css("display","none");
		$("#manualdiv").css("display","block");
		var temphtml = "<button class=\"btn btn-green\" id = \"save_confirm_btn\"  onclick=\"savepassword()\" aria-hidden=\"true\">保存<\/button>";
		$("#pass_confirm_btn").remove();
		$("#pass_manual_btn").remove();
		$("#cancel_btn").before(temphtml);
	}
	//保存手动输入的密码
	function savepassword(){
		var manualpass = $("#manualpass");
		manualpass.parsley('validate');
		if(manualpass.parsley('isValid')){
			var param = "billid="+tempuserid+"&manualpass="+$(manualpass).val()+"&email="+email;
			var url = "<%=request.getContextPath()%>/transform/admin/manualpassword";
			jQuery.ajax({
		  	 	type: "POST",
		  	 	async:false,
		   		url: url,
		  		data: param,
		  		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
		   		success: function(result){
		   		  var obj = eval("("+result+")");
		     	if(obj.status=="success"){
		     		$("#cancel_btn").click();
		     		$("#tipscontent").html(obj.result);
		     		$("#dia").click();
		     	}else{
		     		$("#tipscontent").html(obj.result);
		     		$("#dia").click();
		     		return;
		     	}
		   	}
			});	
		}
	}
	</script>

    <script>

        var path = "<%=request.getContextPath()%>";


        $(function(){
       //check all checkboxes
       $('table thead input[type="checkbox"]').change(function () {
         $(this).parents('table').find('tbody input[type="checkbox"]').prop('checked', $(this).prop('checked'));
       });
      // Add custom class to pagination div
      $.fn.dataTableExt.oStdClasses.sPaging = 'dataTables_paginate pagination-sm paging_bootstrap paging_custom';
	  var oper = '';
      /*************************************************/
      /**************** BASIC DATATABLE ****************/
      /*************************************************/

      /* Define two custom functions (asc and desc) for string sorting */
      jQuery.fn.dataTableExt.oSort['string-case-asc']  = function(x,y) {
          return ((x < y) ? -1 : ((x > y) ?  1 : 0));
      };
       
      jQuery.fn.dataTableExt.oSort['string-case-desc'] = function(x,y) {
          return ((x < y) ?  1 : ((x > y) ? -1 : 0));
      };
 

      /* Build the DataTable with third column using our custom sort functions */
      var oTable01 = $('#basicDataTable').dataTable({
        "sDom":
          "R<'row'<'col-md-6'l><'col-md-6'f>r>"+
          "t"+
          "<'row'<'col-md-4 sm-center'i><'col-md-4'><'col-md-4 text-right sm-center'p>>",
        "oLanguage": {
          "sSearch": "搜索"
        },
        "aaSorting": [],
        "aoColumnDefs": [
                         { 'bSortable': false, 'aTargets': [ "no-sort" ] }
                       ], 
        "fnInitComplete": function(oSettings, json) {
            $('.dataTables_filter').css("text-align","right");
            $('.dataTables_filter').css("margin-top","-40px");
            $('.dataTables_filter').css("margin-bottom","0px");
            $('.dataTables_filter input').attr("placeholder", "Search");
        }

      });

        $(function(){

            $("#search_btn").click(function(){
                var param = $("#param").val();
                var status = $("#status").val();
                var userType = $("#userType").val();
                window.location.href = encodeURI(path + "/transform/useradmin/index?param="+param+"&status="+status+"&userType="+userType);

            });

            $('#param').bind('keypress',function(event){
                if(event.keyCode == "13")
                {
                    $("#search_btn").click();
                }
            });
        });
 

      /* Get the rows which are currently selected */
      function fnGetSelected(oTable01Local){
        return oTable01Local.$('tr.row_selected');
      };
	  $("div[class='col-md-4 text-right sm-center']").prepend(oper);
	    $("div[class='col-md-4 text-right sm-center']").attr("class","tile-footer text-right");
      //chosen select input
      $(".chosen-select").chosen({disable_search_threshold: 10});
    })
   
    </script>
    
  </body>
</html>
