<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- terminal_user_manage.jsp -->
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
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/webupload/webuploader.css" />


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


 
                  <div class="tile-header">
                     <button type="button" class="btn btn-red delete" onclick="addType();">
                              <i class="fa fa-plus"></i>
                              <span> 新增用户 </span>
                    </button>  
                    
                     <button type="button" class="btn btn-blue delete" onclick="$('#upload').click();">
                              <i class="fa fa-file-excel-o"></i>
                              <span> 导入Excel </span>
                    </button>  
                     <button type="button" class="btn btn-green delete" onclick="location.href ='${pageContext.request.contextPath}/user/download/demo';">
                              <i class="fa fa-download"></i>
                              <span> 导入模板下载 </span>
                    </button>  
                    <button type="button" class="btn btn-green file-excel-o" onclick="exportData('/export/terminaluserdata')">
                              <i class="fa fa-file-excel-o"></i>
                              <span>导出数据</span>
                    </button>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                    
                    <div class="bodywrapper" style="display:none;" id="importUser"> 
     
				     <div class="formtitle">
				            <div class="left">
				                <h1 class="logo"><span>导入用户</span></h1> 
								<br clear="all" />
				            </div>
				        <div id = "upload_file_div">
				    		<form id = "upload_file_form" action="${pageContext.request.contextPath}/user/import" method="POST" enctype="multipart/form-data">
				     			<div class="form-group">
				                        <label for="file" class="col-sm-2 control-label">版本上传*</label>
				                        <div class="col-sm-4">
				                          <div class="input-group">
				                          <span class="input-group-btn">
				                            <span class="btn btn-primary btn-file">
				                              <i class="fa fa-upload"></i><input type="file" id="file"  name="attach"  multiple="">
				                            </span>
				                          </span>
				                          <input type="text" class="form-control" readonly="">
				                        </div>
				                        </div>
				                      </div>
				    	</form>
				</div>
         	
         	<div id="loader">
         		<img src="${pageContext.request.contextPath}/images/loaders/loader10.gif" alt="">
         	</div>
         
       </div>
       
       </div>
                  </div>

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
                                      <option value="1" selected="selected">禁用</option>
                                  </c:if>
                                  <c:if test="${status != 1}">
                                      <option value="1">禁用</option>
                                  </c:if>
                              </select>
                               <select id="usb_status" class="chosen-select form-control" style="width: 150px;">
                                   <option value="">USB状态(全部)</option>
                                   <c:if test="${usb_status == 0}">
                                       <option value="0" selected="selected">未开启</option>
                                   </c:if>
                                   <c:if test="${usb_status != 0}">
                                       <option value="0">未开启</option>
                                   </c:if>
                                   <c:if test="${usb_status == 1}">
                                       <option value="1" selected="selected">开启</option>
                                   </c:if>
                                   <c:if test="${usb_status != 1}">
                                       <option value="1">开启</option>
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
                    
                      <table  class="table table-datatable table-custom table-sortable" id="basicDataTable">
                        <thead>
                          <tr> 
                            <th class="no-sort">
                            <div class="checkbox check-transparent">
                              <input type="checkbox" value="1" id="allchck">
                              <label for="allchck"></label>
                            </div>
                          </th>
                            <th class="sortable sort-alpha">用户名</th>
                            <%--<th class="sortable sort-alpha">别名</th>--%>
                            <th class="sortable sort-alpha">显示名</th>
                             <th class="sortable sort-alpha">所属分组</th>  
                             <th class="sortable sort-alpha">邮箱</th>  
                             <th class="sortable sort-alpha">电话</th>
                              <th class="sortable sort-alpha">地区</th>
                              <th class="sortable sort-alpha">行业</th>
                              <th class="sortable sort-alpha">用户状态</th>
                             <th class="sortable sort-alpha">USB权限</th>  
                             <th class="sortable sort-alpha">已分配云主机</th>  
                             <th class="no-sort">操作</th>
                          </tr>
                        </thead>
                        <tbody>
                          <c:forEach items="${terminal_user_list}" var="terminal_user">
                    		<tr class="gradeX">
                          	<td>
								<div class="checkbox check-transparent">
								  <input type="checkbox" value="${terminal_user.id}" name="checkboxid" username="${terminal_user.username}" id="chck${terminal_user.id}" >
								  <label for="chck${terminal_user.id}"></label>
								</div>
                                </td>
                            <td class="cut">${terminal_user.username}</td>
                            <%--<td class="cut">${terminal_user.alias}</td>--%>
                            <td class="cut">${terminal_user.name}</td>
                            <td class="cut">
                            <c:if test="${terminal_user.groupName == null}">
                            &nbsp;                                                                        
                            </c:if>
                            ${terminal_user.groupName}</td>
                            <td class="cut">${terminal_user.email}</td>
                            <td class="cut">${terminal_user.phone}</td>
                                <td class="cut">${terminal_user.region}</td>
                                <td class="cut">${terminal_user.industry}</td>
                                <td class="cut">
                                <c:if test="${terminal_user.status == 0}">
                                    <span class="label label-greensea">正常</span>
                                </c:if>
                                <c:if test="${terminal_user.status == 1}">
                                    <span class="label label-danger">禁用</span>
                                </c:if>
                                </td>
                            <td class="cut">
                                <c:if test="${terminal_user.usbStatus == 0}">
                                    <span class="label label-slategray">未开启</span>
                                </c:if>
                                <c:if test="${terminal_user.usbStatus == 1}">
                                    <span class="label label-blue">开启</span>
                                </c:if>
                            </td>
                            <td class="cut">${terminal_user.cloudHostAmount}</td>
                            <td> 
                              <div class="btn-group">
                                  <button type="button" class="btn btn-default btn-xs dropdown-toggle" data-toggle="dropdown"> 操作 <span class="caret"></span>
                                  </button>
                                  <ul class="dropdown-menu" role="menu"> 
									<c:if test="${terminal_user.boxId==null }">
										<li><a href="javascript:void(0);" onclick="bindBoxBtn('${terminal_user.id}');" >分配盒子</a></li>
									</c:if>
									<c:if test="${terminal_user.boxId!=null }">
										<li><a href="javascript:void(0);" onclick="unboundBoxBtn('${terminal_user.id}');">回收盒子</a></li> 
									</c:if>
                                    <li><a href="javascript:void(0);" onclick="$('#allocateHostsIds').val('${terminal_user.id}');$('#allocat').click();">分配主机</a></li>
                                    <li><a href="javascript:void(0);" onclick="$('#ids_password').val('${terminal_user.id}');$('#usernames').val('${terminal_user.username}');$('#pass').click();">重置密码</a></li> 
                                    <li><a href="javascript:void(0);" onclick="$('input[name=usbStatus][value=${terminal_user.usbStatus}]').attr('checked','checked');$('#ids_usb').val('${terminal_user.id}');$('#usb').click();">修改USB权限</a></li> 
                                    <li><a href="javascript:void(0);" onclick ="$('input[name=status][value=${terminal_user.status}]').attr('checked','checked');$('#ids_status').val('${terminal_user.id}');$('#userstatus').click();">修改用户状态</a></li> 
                                    <li><a href="javascript:void(0);" onclick="updateUser('${terminal_user.id}');">修改</a></li> 
                                    <li><a href="javascript:void(0);" onclick="userDetail('${terminal_user.id}');">详情</a></li> 
                                    <li class="divider"></li>
                                    <li><a href="javascript:void(0);" onclick="deleteUser('${terminal_user.id}');">删除</a></li>
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
                          <select class="chosen-select form-control" id="operselect">
                            <option value="">批量操作</option> 
                            <option value="reset">重置密码</option>
                            <option value="usb">USB权限</option>
                            <option value="userstatus">用户状态</option>
                            <option value="allocat">分配主机</option>
                            <option value="delete">删除</option>
                          </select>
                          <span class="input-group-btn">
                            <button class="btn btn-default" type="button" onclick="toOper();">提交</button>
                          </span>
                        </div>
                      </div> 
                      
                      <div class="tile-body">

                          <a href="#modalUpload" id="upload" role="button"   data-toggle="modal"> </a>
                          <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                          <a href="#modalConfirm" id="con" role="button"   data-toggle="modal"> </a>
                          <a href="#modalpassword" id="pass" role="button"   data-toggle="modal"> </a>
                          <a href="#modalusb" id="usb" role="button"   data-toggle="modal"> </a>
                          <a href="#modaluserstatus" id="userstatus" role="button"   data-toggle="modal"> </a>
                          <a href="#modaluserallocate" id="allocat" role="button"   data-toggle="modal"> </a>

                          <div class="modal fade" id="modalUpload" tabindex="-1" role="dialog" aria-labelledby="modalUploadLabel" aria-hidden="true"  >
                              <div class="modal-dialog">
                                  <div class="modal-content">
                                      <div class="modal-header">
                                          <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="location.href = path + '/user/list';">Close</button>
                                          <h3 class="modal-title" id="modalConfirmLabel"><strong>导入用户数据</strong></h3>
                                      </div>
                                      <div class="modal-body">

                                          <form id="uploadform" name="uploadform"  class="form-horizontal" role="form" parsley-validate id="basicvalidations_uploadimage"  method="post"  enctype="multipart/form-data">
                                              <div class="form-group">
                                                  <label for="attach" class="col-sm-3 control-label"></label>
                                                  <div id="path" class="col-sm-8 btns">
                                                      <div style="float:left;width:75%;">
                                                          <input id="attach" type="text" style="width:95%;" disabled="disabled" class="form-control" parsley-required="true">
                                                      </div>
                                                      <div id="picker" style="width:25%;float: left;">选择文件</div>
                                                  </div>
                                              </div>
                                              <div class="form-group">
                                                  <label for="attach" class="col-sm-2 control-label"></label>
                                                  <div class="col-sm-8">
                                                      <div id="graphbox">
                                                          <div class="graph" style="width:100%;background:darkgrey; border-radius:25px;position: relative;visibility:hidden;">
                                                              <span class="green" style="display: inline-block;text-align:center;width:0%; height: 20px;background: lightseagreen;border-radius:25px;"></span>
                                                              <label class="num" style="position: absolute;top: 0;left: 50%;width: 36px;line-height: 20px;margin-left: -18px;display: inline-block;text-align: center;"></label>
                                                          </div>
                                                      </div>
                                                  </div>

                                              </div>
                                              <div class="form-group">
                                                  <label for="attach" class="col-sm-2 control-label"></label>
                                                  <div class="col-sm-8">
                                                      <div id="chooseinfo" style="display:none;color:cadetblue;text-align: center;"></div>
                                                  </div>
                                              </div>
                                              </form>
                                          </form>
                                      </div>

                                      <div class="modal-footer" style="margin-top:-10px;">
                                          <button id="ctlBtn" class="btn btn-default">开始上传</button>
                                          <button id="closebtn" class="btn btn-red" data-dismiss="modal" aria-hidden="true" onclick="location.href = path + '/user/list';">关闭</button>
                                      </div>
                                  </div><!-- /.modal-content -->
                              </div><!-- /.modal-dialog -->
                          </div><!-- /.modal -->

                          <!-- 上传成功提示框 -->
                          <div class="modal fade" id="successDialog" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                              <div class="modal-dialog">
                                  <div class="modal-content" style="width:60%;margin-left:20%;">
                                      <div class="modal-header">
                                          <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                                          <h3 class="modal-title" id="modalConfirmLabel">提示</h3>
                                      </div>
                                      <div class="modal-body">
                                          <h4>上传成功</h4>
                                      </div>
                                      <div class="modal-footer">
                                          <!--                             <button class="btn btn-green" onclick="uploadAfter()"   data-dismiss="modal" aria-hidden="true">确定</button>
                                           -->                            <button class="btn btn-red"   onclick="uploadAfter()" data-dismiss="modal" aria-hidden="true">关闭</button>
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
                                <label style="align:center;" id="confirmcontent">确定要删除该用户吗？</label>
                               </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green" id="confirm_btn"  onclick="toDelete();" data-dismiss="modal" aria-hidden="true">确定</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->

                    <div class="modal fade" id="modalpassword" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>密码重置</strong></h3>
                          </div>
                          <div class="modal-body">
                            <form class="form-horizontal" role="form" parsley-validate id="basicvalidations_password" action="<%=request.getContextPath() %>/user/password/reset" method="post"   >
                            	<input id = "ids_password" name="ids" type="hidden">
            					<input id = "usernames" name="usernames" type="hidden">
                              
                              <div class="form-group">
		                        <label for="password" class="col-sm-2 control-label">密码*</label>
		                        <div class="col-sm-8">
		                          <input type="password" class="form-control" id="password" name="password"  parsley-trigger="change" parsley-required="true"  parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1">
		                        </div>
		                      </div> 
		                      <div class="form-group">
		                        <label for="confirm_password" class="col-sm-2 control-label">密码确认*</label>
		                        <div class="col-sm-8">
		                          <input type="password" class="form-control" id="confirm_password" name="confirm_password"  parsley-trigger="change" parsley-required="true"  parsley-minlength="2" parsley-maxlength="50" parsley-validation-minlength="1" parsley-equalto="#password">
		                        </div>
		                      </div> 

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green" onclick="savePassword();">保存</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">关闭</button>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->                     
                    <div class="modal fade" id="modalusb" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>USB权限</strong></h3>
                          </div>
                          <div class="modal-body">
                            <form class="form-horizontal" role="form" parsley-validate id="basicvalidations_usb" action="<%=request.getContextPath() %>/user/change/usb" method="post"   >
                            	<input id = "ids_usb" name="ids" type="hidden">
                               
                              <div class="form-group">
		                        <label for="optionsRadios11" class="col-sm-2 control-label">USB权限</label>
		                        <div class="col-sm-8">
		                          <div class="radio   col-md-4">
		                            <input type="radio" name="usbStatus"   id="optionsRadios11" value="1" checked>
		                            <label for="optionsRadios11">启用</label>
		                          </div>
		                          <div class="radio  col-md-4">
		                            <input type="radio" name="usbStatus"   id="optionsRadios12" value="0">
		                            <label for="optionsRadios12">禁用</label>
		                          </div> 
		                        </div>
		                      </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green" onclick="saveUsb();">保存</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">关闭</button>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->                     
                    <div class="modal fade" id="modaluserstatus" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>用户状态</strong></h3>
                          </div>
                          <div class="modal-body">
                            <form class="form-horizontal" role="form" parsley-validate id="basicvalidations_status" action="<%=request.getContextPath() %>/user/change/status" method="post"   >
                            	<input id = "ids_status" name="ids" type="hidden">
                               
                              <div class="form-group">
		                        <label for="optionsRadios13" class="col-sm-2 control-label">用户状态</label>
		                        <div class="col-sm-8">
		                          <div class="radio   col-md-4">
		                            <input type="radio" name="status"   id="optionsRadios13" value="0" checked>
		                            <label for="optionsRadios13">正常</label>
		                          </div>
		                          <div class="radio  col-md-4">
		                            <input type="radio" name="status"   id="optionsRadios14" value="1">
		                            <label for="optionsRadios14">禁用</label>
		                          </div> 
		                        </div>
		                      </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green" onclick="saveStatus();">保存</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">关闭</button>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->                     
                    <div class="modal fade" id="modaluserallocate" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>分配主机</strong></h3>
                          </div>
                          <div class="modal-body">
                            <form class="form-horizontal" role="form" parsley-validate id="basicvalidations_allocate" action="<%=request.getContextPath() %>/user/allocat" method="post"   >
                            	<input id="allocateHostsIds" name="allocateHostsIds" type="hidden" value=""/>
                               
                              <div class="form-group">
		                        <label for="warehouseId" class="col-sm-2 control-label">仓库选择*</label>
		                        <div class="col-sm-8" id="selectbox">
		                          <select class="chosen-select   form-control" name="warehouseId"id="warehouseId" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectbox">
		                            <option value="">请选择仓库</option> 
		                            <c:forEach items="${warelist }" var="sdi">
 		                                 <option value="${sdi.id }">${sdi.name}</option>
 		                             </c:forEach>   
		                          </select>
		                        </div>
		                      </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green" onclick="saveAllocate();">保存</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">关闭</button>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->  
                    
                    <div class="modal fade" id="modalDialog" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="location.href = path + '/user/list';">Close</button>
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
    <script type="text/javascript" src="<%=request.getContextPath()%>/webupload/webuploader.js"></script>
    <%--<script src="<%=request.getContextPath() %>/webupload/jquery.js"></script>--%>
    <script src="<%=request.getContextPath() %>/webupload/import.js"></script>

    <script>
    var path = '<%=request.getContextPath()%>';
    var operid = "";
    var oper = "";
    var usernames = "";
    var ids = ";"




    $(function(){

      // Add custom class to pagination div
      $.fn.dataTableExt.oStdClasses.sPaging = 'dataTables_paginate pagination-sm paging_bootstrap paging_custom';  
 
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
      
      
 
 

      /* Get the rows which are currently selected */
      function fnGetSelected(oTable01Local){
        return oTable01Local.$('tr.row_selected');
      };
 	  $("div[class='col-md-4 text-right sm-center']").attr("class","tile-footer text-right"); 

       
      
    })
    
     $(function(){

        //check all checkboxes
        $('table thead input[type="checkbox"]').change(function () {
          $(this).parents('table').find('tbody input[type="checkbox"]').prop('checked', $(this).prop('checked'));
        });

        // sortable table
        $('.table.table-sortable th.sortable').click(function() {
          var o = $(this).hasClass('sort-asc') ? 'sort-desc' : 'sort-asc';
          $(this).parents('table').find('th.sortable').removeClass('sort-asc').removeClass('sort-desc');
          $(this).addClass(o);
        });

        //chosen select input
        $(".chosen-select").chosen({disable_search_threshold: 10});

        //check toggling
        $('.check-toggler').on('click', function(){
          $(this).toggleClass('checked');
        });

//        jQuery("#file").change(function(){
//        	$("#loader").delay(500).fadeOut(300);
//
//        	 jQuery("#upload_file_form").ajaxSubmit(function(e){
//        		 $(".mask").delay(800).fadeOut(300,
//        		            function() {
//        		                widthLess1024();
//        		                widthLess768()
//        		            });
//        		 if(e.status == "success"){
//                     $("#tipscontent").html(e.message);
//                     $("#dia").click();
//
//        		 }else{
//        			 $("#tipscontent").html(e.message);
//          		      $("#dia").click();
//        		 }
//
//        		});
//
//        });
//        $("#warehouseId_chosen").css("width","250px");
        
      });

//    $(function(){
//
//        var $list=$("#thelist");
//        var $btn =$("#ctlBtn");   //开始上传
//
//        var uploader = WebUploader.create({
//
//            auto: false,
//
//            // swf文件路径
//            swf: path + '/webupload/Uploader.swf',
//
//            // 文件接收服务端。
//            server: path + '/user/import',
//
//            // 选择文件的按钮。可选。
//            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
//            pick: '#picker',
//
//            // 不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
//            resize: false,
//
//            // 只允许选择excel文件。
//            accept: {
//                title: 'Excel',
//                extensions: 'xls,xlsx',
//                mimeTypes: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
//            },
//
//            method: 'POST'
//        });
//
//        // 当有文件被添加进队列的时候
//        uploader.on( 'fileQueued', function( file ) {
//            console.info("queue");
//            $list.append( '<div id="' + file.id + '" class="item">' +
//                    '<h4 class="info">' + file.name + '</h4>' +
//                    '<p class="state">等待上传...</p>' +
//                    '</div>' );
//        });
//
//        // 文件上传过程中创建进度条实时显示。
//        uploader.on( 'uploadProgress', function( file, percentage ) {
//            console.info("progress");
//            var $li = $( '#'+file.id ),
//                    $percent = $li.find('.progress .progress-bar');
//
//            // 避免重复创建
//            if ( !$percent.length ) {
//                $percent = $('<div class="progress progress-striped active">' +
//                        '<div class="progress-bar" role="progressbar" style="width: 0%">' +
//                        '</div>' +
//                        '</div>').appendTo( $li ).find('.progress-bar');
//            }
//
//            $li.find('p.state').text('上传中');
//
//            $percent.css( 'width', percentage * 100 + '%' );
//        });
//
//        uploader.on( 'uploadSuccess', function( file ) {
//            console.info("succes");
//            $( '#'+file.id ).find('p.state').text('已上传');
//        });
//
//        uploader.on( 'uploadError', function( file ) {
//            console.info("error");
//            $( '#'+file.id ).find('p.state').text('上传出错');
//        });
//
//        uploader.on( 'uploadComplete', function( file ) {
//            console.info("complete");
//            $( '#'+file.id ).find('.progress').fadeOut();
//        });
//
//
//    });



    $(function(){
        $("#search_btn").click(function(){
            var param = $("#param").val();
            var status = $("#status").val();
            var usb_status = $("#usb_status").val();

            window.location.href = encodeURI(path + "/user/list?param="+param+"&status="+status+"&usb_status="+usb_status);

        });

        $('#param').bind('keypress',function(event){
            if(event.keyCode == "13")
            {
                $("#search_btn").click();
            }
        });
    });

    function deleteUser(id){
    	operid = id;
    	oper = 1;
		jQuery.ajax({
	        url: '<%=request.getContextPath()%>/main/checklogin',
	        type: 'post', 
	        dataType: 'json',
	        timeout: 10000,
	        async: false,
	        error: function()
	        {
	            alert('Error!');
	        },
	        success: function(result)
	        {
	        	if(result.status == "fail"){
	      		  $("#tipscontent").html("登录超时，请重新登录");
	   		      $("#dia").click();
		        	}else{ 	        		  
				       $("#con").click();		        		 	        	 
	           } 
	        }
	     });
	
 }
    
    
    function toDelete(){
    	jQuery.get(path + "/user/"+operid+"/delete",function(data){
			if(data.status == "success"){	        					
					location.href = path+"/user/list"; 
			}else{
				$("#tipscontent").html("删除失败");
   		        $("#dia").click();
			}
		});
    } 
    function addType(){          					
	      location.href = path+"/user/addpage";  
	
    }
    function updateUser(id){
    	  location.href = path+"/user/"+id+"/update"; 
    }
    //分配盒子
    function bindBoxBtn(id){
    	  location.href = path+"/user/"+id+"/bindbox"; 
    }
    //回收盒子
    function unboundBoxBtn(id){
        curId = id;
    	$("#confirmcontent").html("确定回收该用户的盒子吗？");
        $("#confirm_btn").attr("onclick","unboundBox();");
        $("#con").click();
    	  
    }
    function unboundBox(){
    	jQuery.ajax({
            url: path + '/user/unboundbox',
            type: 'post',
            dataType: 'json',
            data:{id:curId},
            async: false,
            timeout: 10000,
            error: function()
            {
            },
            success: function(result)
            {
                if(result.status=="success"){
                    window.location.reload();
                }else{
                    $("#tipscontent").html(result.message);
                    $("#dia").click();
                }
            }

        });
    }
    
    function deleteIds(){
    	if($("#operselect").val() == "delete"){
	    	operid = "";
	    	$("input[name='checkboxid']:checked").each(function(){  
	                operid += $(this).val()+","
	        });
	        if(operid == ""){
	        	$("#tipscontent").html("请选择版本");
			       $("#dia").click();
	        }else{      	
		    	$("#con").click();
	        }
    		
    	}else{
    		$("#tipscontent").html("请选择操作");
		       $("#dia").click();
    	}
    }
    
    function getUsernamesAndIds(){
    	var idArray = document.getElementsByName('checkboxid');
    	ids = new Array();
    	usernames = new Array();
    	var j=0;
    	for (var i = 0; i < idArray.length; i++) {
    		if(idArray[i].checked){
    			usernames[j] = idArray[i].getAttribute("username");
    			ids[j] = idArray[i].value;
    			j++;
    		}
    	}
     }
    function toOper(){
    	oper = $("#operselect").val();
    	operid = "";
    	$("input[name='checkboxid']:checked").each(function(){  
                operid += $(this).val()+","
        });
        if(operid == ""){
        	$("#tipscontent").html("请选择用户");
		    $("#dia").click();
        }
        else{       	
	    	if(oper == "reset"){
	    		getUsernamesAndIds();
	    		$("#usernames").val(usernames);
	    		$("#ids_password").val(ids);
	    		$("#pass").click();
	    	}
	    	
	    	else if(oper == "delete"){   	
			    $("#con").click(); 
	    		
	    	}else if(oper == "usb"){
	    		$("#optionsRadios11").click();;
	    		getUsernamesAndIds();
 	    		$("#ids_usb").val(ids);
			    $("#usb").click(); 
	    		
	    	}else if(oper == "userstatus"){
	    		$("#optionsRadios13").click();;
	    		getUsernamesAndIds();
 	    		$("#ids_status").val(ids);
			    $("#userstatus").click(); 
	    		
	    	}else if(oper == "allocat"){
 	    		getUsernamesAndIds();
 	    		$("#allocateHostsIds").val(ids);
			    $("#allocat").click(); 
	    		
	    	}else{
	    		$("#tipscontent").html("请选择操作");
			       $("#dia").click();
	    	}
        }
    }
    
    function savePassword(){
		jQuery.ajax({
	        url: path+'/main/checklogin',
	        type: 'post', 
	        dataType: 'json',
	        timeout: 10000,
	        async: true,
	        error: function()
	        {
	            alert('Error!');
	        },
	        success: function(result)
	        {
	        	if(result.status == "fail"){ 
	        		  $("#tipscontent").html("登录超时，请重新登录");
	     		      $("#dia").click();
		        	}else{ 
   		        			var options = {
   		        					success:function result(data){
   		        						if(data.status == "fail"){
							        		  $("#tipscontent").html("创建失败");
							     		      $("#dia").click();  		        							
   		        						}else{  		        							
	   		        						location.href = path + "/user/list";
   		        						}
   		        					},
   		        					dataType:'json',
   		        					timeout:10000
   		        			};
   		        			var form = jQuery("#basicvalidations_password");
   		        			form.parsley('validate');
   		        			if(form.parsley('isValid')){  		        				
			        			jQuery("#basicvalidations_password").ajaxSubmit(options); 
   		        			}
 		        	} 
	        }
	     }); 
		
	}
    function saveUsb(){
		jQuery.ajax({
	        url: path+'/main/checklogin',
	        type: 'post', 
	        dataType: 'json',
	        timeout: 10000,
	        async: true,
	        error: function()
	        {
	            alert('Error!');
	        },
	        success: function(result)
	        {
	        	if(result.status == "fail"){ 
	        		  $("#tipscontent").html("登录超时，请重新登录");
	     		      $("#dia").click();
		        	}else{ 
   		        			var options = {
   		        					success:function result(data){
   		        						if(data.status == "fail"){
							        		  $("#tipscontent").html("修改失败");
							     		      $("#dia").click();  		        							
   		        						}else{  		        							
	   		        						location.href = path + "/user/list";
   		        						}
   		        					},
   		        					dataType:'json',
   		        					timeout:10000
   		        			};
   		        			var form = jQuery("#basicvalidations_usb");
   		        			form.parsley('validate');
   		        			if(form.parsley('isValid')){  		        				
			        			jQuery("#basicvalidations_usb").ajaxSubmit(options); 
   		        			}
 		        	} 
	        }
	     }); 
		
	}
    function saveStatus(){
		jQuery.ajax({
	        url: path+'/main/checklogin',
	        type: 'post', 
	        dataType: 'json',
	        timeout: 10000,
	        async: true,
	        error: function()
	        {
	            alert('Error!');
	        },
	        success: function(result)
	        {
	        	if(result.status == "fail"){ 
	        		  $("#tipscontent").html("登录超时，请重新登录");
	     		      $("#dia").click();
		        	}else{ 
   		        			var options = {
   		        					success:function result(data){
   		        						if(data.status == "fail"){
							        		  $("#tipscontent").html("修改失败");
							     		      $("#dia").click();  		        							
   		        						}else{  		        							
	   		        						location.href = path + "/user/list";
   		        						}
   		        					},
   		        					dataType:'json',
   		        					timeout:10000
   		        			};
   		        			var form = jQuery("#basicvalidations_status");
   		        			form.parsley('validate');
   		        			if(form.parsley('isValid')){  		        				
			        			jQuery("#basicvalidations_status").ajaxSubmit(options); 
   		        			}
 		        	} 
	        }
	     }); 
		
	}
    function saveAllocate(){
		jQuery.ajax({
	        url: path+'/main/checklogin',
	        type: 'post', 
	        dataType: 'json',
	        timeout: 10000,
	        async: true,
	        error: function()
	        {
	            alert('Error!');
	        },
	        success: function(result)
	        {
	        	if(result.status == "fail"){ 
	        		  $("#tipscontent").html("登录超时，请重新登录");
	     		      $("#dia").click();
		        	}else{ 
   		        			var options = {
   		        					success:function result(data){
   		        						if(data.status == "fail"){
							        		  $("#tipscontent").html(data.message);
							     		      $("#dia").click();  		        							
   		        						}else{  		        							
	   		        						location.href = path + "/user/list";
   		        						}
   		        					},
   		        					dataType:'json',
   		        					timeout:10000
   		        			};
   		        			var form = jQuery("#basicvalidations_allocate");
   		        			form.parsley('validate');
   		        			if(form.parsley('isValid')){  		        				
			        			jQuery("#basicvalidations_allocate").ajaxSubmit(options); 
   		        			}
 		        	} 
	        }
	     }); 
		
	}
    
    function userDetail(id){  
    	 location.href = path+"/user/"+id+"/detail"; 
   }
     
    
    
      
    </script>
    
  </body>
</html>
      
