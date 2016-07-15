<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<!-- host_pool_detail.jsp -->
<head>
<title>控制台-${productName}</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="UTF-8" />

<link rel="icon" type="image/ico"
	href="<%=request.getContextPath()%>/assets/images/favicon.ico" />
<!-- Bootstrap -->
<link
	href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap/bootstrap.min.css"
	rel="stylesheet">
<link
	href="<%=request.getContextPath()%>/font-awesome/css/font-awesome.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/css/vendor/animate/animate.min.css">
<link type="text/css" rel="stylesheet" media="all"
	href="<%=request.getContextPath()%>/assets/js/vendor/mmenu/css/jquery.mmenu.all.css" />
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/js/vendor/videobackground/css/jquery.videobackground.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap-checkbox.css">

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/css/rickshaw.min.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/js/vendor/morris/css/morris.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/js/vendor/tabdrop/css/tabdrop.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/js/vendor/summernote/css/summernote.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/js/vendor/summernote/css/summernote-bs3.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen.min.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen-bootstrap.css">

<link href="<%=request.getContextPath()%>/assets/css/zhicloud.css"
	rel="stylesheet">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
      <script src="<%=request.getContextPath()%>/assets/js/html5shiv.js"></script>
      <script src="<%=request.getContextPath()%>/assets/js/respond.min.js"></script>
    <![endif]-->
<style type="text/css">
.butCss {
	width: 82px;
}
.tdiv1{
    position: absolute;
    left: 160px;
    width: 160px;
    height: 50px;
    font-family: 'Arial Negreta', 'Arial';
    font-weight: 700;
    font-style: normal;
    font-size: 18px;
}
.tdvi2{
    position: absolute;
    left: 491px;
    width: 160px;
    height: 50px;
    font-family: 'Arial Negreta', 'Arial';
    font-weight: 700;
    font-style: normal;
    font-size: 18px;
    color: #FFFFFF;
}
.img{
    position: absolute;
    left: 0px;
    top: 0px;
    width: 160px;
    height: 50px;
}
.imghead{cursor: pointer;}
.img1{
    position: absolute;
    left: 0px;
    top: 10px;
    width: 346px;
    height: 1px;
}
.img10{
    position: absolute;
    left: 0px;
    top: 10px;
    width: 870px;
    height: 1px;
}
.text{
	position: absolute;
    left: 2px;
    top: 14px;
    width: 156px;
    word-wrap: break-word;
    text-align: center;
    font-size: 20px;
    height: 37px;
}
.text1{
    left: 2px;
    top: 14px;
    width: 150px;
    word-wrap: break-word;
    text-align: center;
    font-size: 20px;
    height: 37px;
}
.div99{position: absolute;margin-top: -15px;}
.div100{position: absolute;margin-left: 40px;margin-top: -6px;}
.div101{position: absolute;margin-left: 23px;margin-top: -6px;}
.div330{position: absolute;margin-top: -15px;}
.div331{position: absolute;margin-left: 23px;margin-top: -6px;}
.div334{margin-top: 110px;margin-left: 476px;}
.div335{position: absolute;left: 95px;width: 719px;height: 10px}
.text2{
    left: 2px;
    top: 14px;
    width: 150px;
    word-wrap: break-word;
    font-size: 20px;
    text-align:left;
    height: 37px;
}
.divMag{
    margin-left: 95px;
    height: 35px;
}
.divSelect{
    height: 30px;
    margin-top: 41px;
    text-align: left;
    margin-left: 593px;
}
.div12{
	position: absolute;
    margin-left: 103px;
    margin-top: -21px;
}
.ax_o{
  font-family:'Arial Normal', 'Arial';
  font-weight:400;
  font-style:normal;
  font-size:13px;
  color:#333333;
  text-align:center;
  line-height:normal;
  margin-left: 0x;
}
.contentDiv111{margin-left: -50px;}
.spandiv11{margin-left: 12px;}

.ax_o1{
  font-family:'Arial Normal', 'Arial';
  font-weight:400;
  font-style:normal;
  font-size:13px;
  color:#333333;
  line-height:normal;
  margin-left: 395px;
}
.ax_o2{
  font-family:'Arial Normal', 'Arial';
  font-weight:400;
  font-style:normal;
  font-size:13px;
  color:#333333;
  line-height:normal;
  position: absolute;
  top: 432px;
  left: 472px;
  font-size: 15px;
}
.divtopleft{
    margin-left: 120px;
}
.divleft{
    margin-left: -111px;
}
.span1{
    margin-left: 21px;
}
.div80{
	position: absolute;
    left: 95px;
    width: 319px;
    height: 10px
}
.div88{
	position: absolute;
    left: 95px;
    width: 319px;
    height: 10px
}
.div880{
	position: absolute;
    left: 95px;
    width: 319px;
    height: 10px
}
.div81{
	position: absolute;
    left: 554px;
    top: 232px;
    width: 319px;
    height: 10px
}
.contentDiv{
	margin-top: 35px;
}
.selectDiv11{margin-top: -22px;padding-left: 8px;}
.div1201{
	margin-top: -36px;
	margin-left: 62px;
}
.div22{height: 60px;}
.div23{
	width: 60px;
    height: 30px;
    color: #FFFFFF;
}
.rightContent{
	margin-top: -172px;
    margin-left: 460px;
}

.div220{position: absolute;}
.div221{
    margin-left: 515px;
    margin-top: -262px;
}
.typeString{margin-top: -187px;}
.div222{
    text-align: center;
    flood-color: inherit;
    color: beige;
    margin-left: 8px;
    margin-top: 5px;
}
.div333{
    text-align: center;
    flood-color: inherit;
    color: beige;
    margin-left: 30px;
    margin-top: -9px;
}
.div444{margin-left: 515px;}

.table1{margin-left: 95px;}
.table2{
	width: 60px;
    margin-top: -52px;
    margin-left: 495px;
}
</style>
</head>
<body class="bg-1">
	<!-- Preloader -->
	<div class="mask">
		<div id="loader"></div>
	</div>
	<!--/Preloader -->

	<!-- Wrap all page content here -->
	<div id="wrap">
		<!-- Make page fluid -->
		<div class="row">
			<%@include file="/views/common/common_menus.jsp"%>
			<!-- Page content -->
			<div id="content" class="col-md-12">
				<!-- page header -->
				<div class="pageheader">
					<h2>
						<i class="fa fa-desktop"></i> 主机迁移
					</h2>
				</div>
				<!-- /page header -->
				<!-- content main container -->
				<div class="main">
					<!-- row -->
					<div class="row">
						<!-- col 6 -->
						<div class="col-md-12">
							<section class="tile color transparent-black">
								<!-- /tile header -->
								<div class="tile-header">
									<button type="button" class="btn btn-success delete"
										onclick="javascript:history.back(-1);">
										<i class="fa fa-step-backward"></i> <span> 返回上级</span>
									</button>
									<i class="fa"></i>

								</div>

								<div class="tile-body no-vpadding divMag">
									<div class="">
										<div id="migrationHost" class="tdiv1">
											<img id="u112_img" class="img " src="<%=request.getContextPath()%>/images/hostMigration/u110.png" tabindex="0">
											<div class="text"><p><span>主机迁移</span></p></div>
										</div>
										<div id="migrationLocalHost" class="tdiv2">
											<img id="u112_img" class="img imghead" src="<%=request.getContextPath()%>/images/hostMigration/u112.png" tabindex="0">
											<div class="text"><p><span>迁移记录</span></p></div>
										</div>										
									</div>
								</div>
								
								<!-- 主机迁移  leftstart-->
								<div class="divSelect">
									<div id="u120" class="ax_下拉列表框">
								        <select id="migrationNC">
								          <option value="选择NC">选择NC</option>
								          <c:forEach items="${computerPool }" varStatus="i" var="cp">
									          <c:if test="${cp.getAllNode()>0}">
									          	<option value="${cp.uuid }">${cp.name }</option>
									          </c:if>
								          </c:forEach>
								          <option value="系统分配NC">系统分配NC</option>
								        </select>&nbsp;可选择目的NC  
							         </div>
								</div>
								<br>
								<div>
									<div>
										<table class="table1" width="360px" style="text-align: center;">
											<tr height="76px">
												<td width="30px"><input type="checkbox" name="leftCheckBoxAll" id="leftCheckBoxAll" /></td>
												<td width="126px">
													<div class="div99"><img id="u90_img" class="text1" src="<%=request.getContextPath()%>/images/hostMigration/u64.png"/></div>
													<div class="div100">主机名</div>
												</td>
												<td width="126px">
													<div class="div99"><img id="u114_img" class="text1" src="<%=request.getContextPath()%>/images/hostMigration/u64.png"/></div>
													<div class="div100">源NC</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="div80">
											        <img id="u80_line" class="img1" src="<%=request.getContextPath()%>/images/hostMigration/u70_line.png" alt="u80_line"/>
												</div>
												</td>
											</tr><input id="typeStringValue" type="hidden" value="${typeString}"/>
											<c:if test="${typeString == '1'}">
												<tr height="76px">
													<td width="30px"><input type="checkbox" name="leftCheckBox" id="leftCheckBox" /></td>
													<td width="126px">
														<div class="div99"><img id="u90_img" class="text1" src="<%=request.getContextPath()%>/images/hostMigration/u64.png"/></div>
														<div class="div101">${cloudHost.getDisplayName()}</div><input id="realHostId" type="hidden" value="${realHostId}"/>
													</td>
													<td width="126px">
														<div class="div99"><img id="u114_img" class="text2" src="<%=request.getContextPath()%>/images/hostMigration/u64.png"/></div>
											        	<div class="div101">${cloudHost.getInnerIp()}</div>
													</td>
												</tr>
												
												<tr>
													<td>
														<div class="div80">
												        <img id="u80_line" class="img1" src="<%=request.getContextPath()%>/images/hostMigration/u70_line.png" alt="u80_line"/>
													</div>
													</td>
												</tr>
											</c:if>
										</table>
									</div>
									
									<div class="table2">
										<table width="60px" style="text-align: center;">
											<tr id="rightLt" height="76px">
												<td>
													<div class="div220"><img id="u84_img" class="div23" src="<%=request.getContextPath()%>/images/hostMigration/u84.png" /></div>
													<div class="div220 div222">选择&gt;&gt;</div>
												</td>
											</tr>
											<tr><td><p></p></td></tr>
											<tr id="leftGt" height="76px">
												<td>
													<div class="div220"><img id="u84_img" class="div23" src="<%=request.getContextPath()%>/images/hostMigration/u84.png" /></div>
													<div class="div220 div222">&lt;&lt;取消</div>
												</td>
											</tr>
										</table>
									</div>
									
									<!-- leftEnd -->
									<div id="typeString" class="div221">
										<table class="table1" width="360px" style="text-align: center;">
											<tr height="76px">
												<td width="30px"><input type="checkbox" name="rightCheckBoxAll" id="rightCheckBoxAll" /></td>
												<td width="126px">
													<div class="div99"><img id="u90_img" class="text1" src="<%=request.getContextPath()%>/images/hostMigration/u64.png"/></div>
													<div class="div100">主机名</div>
												</td>
												<td width="126px">
													<div class="div99"><img id="u114_img" class="text1" src="<%=request.getContextPath()%>/images/hostMigration/u64.png"/></div>
													<div class="div100">源NC</div>
												</td>
											</tr>
											<tr>
												<td>
													<div class="div80">
											        <img id="u80_line" class="img1 div444" src="<%=request.getContextPath()%>/images/hostMigration/u70_line.png" alt="u80_line"/>
												</div>
												</td>
											</tr>
											<!-- js动态添加 
											<form role="form" id="basicvalidations" action="<%=request.getContextPath() %>/cdrpm/toMigration" method="post"   >-->
											<tr id="rightDivContent" height="76px">
												<%-- <td width="30px"><input type="checkbox" name="leftCheckBox" id="leftCheckBox" /></td>
												<td width="126px">
													<div class="div99"><img id="u90_img" class="text1" src="<%=request.getContextPath()%>/images/hostMigration/u64.png"/></div>
													<div class="div101">${cloudHost.getDisplayName()}</div>
												</td>
												<td width="126px">
													<div class="div99"><img id="u114_img" class="text2" src="<%=request.getContextPath()%>/images/hostMigration/u64.png"/></div>
										        	<div class="div101">${cloudHost.getInnerIp()}</div>
												</td> --%>
											</tr>
											
											<tr id="rightDivContent1">
												<%-- <td>
													<div class="div80">
											        <img id="u80_line" class="img1 div444" src="<%=request.getContextPath()%>/images/hostMigration/u70_line.png" alt="u80_line"/>
													</div>
												</td> --%>
											</tr>
										</table>
									</div>
									<div class="div334">
										<br>
										<div class="div335">
									        <img id="u80_line" class="img10" src="<%=request.getContextPath()%>/images/hostMigration/u70_line.png" alt="u80_line"/>
										</div>
										<br><br><br>
										<div id="migrationBut" class="ax_01 imghead" data-label="确定">
									        <div class="div330"><img id="u106_img" class="" src="<%=request.getContextPath()%>/images/hostMigration/u106.png"/></div>
									        <div id="u107" class="div331 div333">
									          <p><span>确定</span></p>
									        </div>
									      </div>
									</div>
								</div>
								
								<!-- /tile body -->

								<div class="tile-body">
									
									<a href="#modalDialog" id="dia" role="button"
										data-toggle="modal"> </a> <a href="#modalConfirm" id="con"
										role="button" data-toggle="modal"> </a> <a href="#modalForm"
										id="mform" role="button" data-toggle="modal"> </a> <a
										href="#modalAddDesktop" id="adddesktop" role="button"
										data-toggle="modal"> </a>

									<div class="modal fade" id="modalDialog" tabindex="-1"
										role="dialog" aria-labelledby="modalDialogLabel"
										aria-hidden="true">
										<div class="modal-dialog">
											<div class="modal-content"
												style="width: 60%; margin-left: 20%;">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal"
														aria-hidden="true">Close</button>
													<h3 class="modal-title" id="modalDialogLabel">
														<strong>提示</strong>
													</h3>
												</div>
												<div class="modal-body">
													<p id="tipscontent"></p>
												</div>
											</div>
											<!-- /.modal-content -->
										</div>
										<!-- /.modal-dialog -->
									</div>
									<!-- /.modal -->

									<div class="modal fade" id="modalConfirm" tabindex="-1"
										role="dialog" aria-labelledby="modalConfirmLabel"
										aria-hidden="true">
										<div class="modal-dialog">
											<div class="modal-content"
												style="width: 60%; margin-left: 20%;">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal"
														aria-hidden="true">Close</button>
													<h3 class="modal-title" id="modalConfirmLabel">
														<strong>确认</strong>
													</h3>
												</div>
												<div class="modal-body">
													<form role="form">

														<div class="form-group">
															<label style="align: center;" id="confirmcontent">确定要删除该云主机吗？</label>
														</div>

													</form>
												</div>
												<div class="modal-footer">
													<button class="btn btn-green" id="confirm_btn"
														onclick="toDelete();" data-dismiss="modal"
														aria-hidden="true">确定</button>
													<button class="btn btn-red" data-dismiss="modal"
														aria-hidden="true">取消</button>
												</div>
											</div>
											<!-- /.modal-content -->
										</div>
										<!-- /.modal-dialog -->
									</div>
									<!-- /.modal -->
									<div class="modal fade" id="modalForm" tabindex="-1"
										role="dialog" aria-labelledby="modalConfirmLabel"
										aria-hidden="true">
										<div class="modal-dialog">
											<div class="modal-content"
												style="width: 60%; margin-left: 20%;">
												<div class="modal-header">
													<button id="my_close" type="button" class="close"
														data-dismiss="modal" aria-hidden="true">Close</button>
													<h3 class="modal-title" id="modalConfirmLabel">
														<strong>主机管理</strong>
													</h3>
												</div>
												<div class="modal-body">
													<form class="form-horizontal" id="addAmountForm"
														role="form"
														action="<%=request.getContextPath() %>/warehouse/addAmount"
														method="post">
														<input type="hidden" id="warehouse_id" name="id" value="">
														<div class="form-group">
															<label for="input01" class="col-sm-2 control-label"
																style="width: 150px;">增加主机数量</label>
															<div class="col-sm-4" style="width: 160px;">
																<input type="text" id="cmount_input"
																	class="form-control" name="addAmount" maxlength="4"
																	onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
																	onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"
																	parsley-required="true" parsley-max="100"
																	parsley-min="1">
															</div>
														</div>
													</form>
												</div>
												<div class="modal-footer">
													<button class="btn btn-green" id="form_btn"
														onclick="saveAddAmount();">确定</button>
													<button id="my_reset" class="btn btn-red"
														data-dismiss="modal" aria-hidden="true">取消</button>

												</div>
											</div>
											<!-- /.modal-content -->
										</div>
										<!-- /.modal-dialog -->
									</div>
									<!-- /.modal -->

									<div class="modal fade" id="modalAddDesktop" tabindex="-1"
										role="dialog" aria-labelledby="modalConfirmLabel"
										aria-hidden="true">
										<div class="modal-dialog">
											<div class="modal-content">
												<div class="modal-header">
													<button type="button" class="close" data-dismiss="modal"
														aria-hidden="true">Close</button>
													<h3 class="modal-title" id="modalConfirmLabel">
														<strong>添加主机</strong>
													</h3>
												</div>
												<div class="modal-body">
													<form class="form-horizontal" id="addDesktop" role="form"
														action="<%=request.getContextPath() %>/warehouse/addAmount"
														method="post">
														<div class="form-group">
															<label for="input01" class="col-sm-2 control-label"
																style="width: 150px;">仓库 </label>
															<div class="col-sm-4" id="selectbox">
																<select class="chosen-select   form-control"
																	style="width: 300px;" name="wareHostId" id="wareHostId"
																	parsley-trigger="change" parsley-required="true"
																	parsley-error-container="#selectbox">
																	<option value="">请选择仓库</option>
																	<c:forEach items="${wareHoseList }" var="sdi">
																		<option value="${sdi.id }">${sdi.name}</option>
																	</c:forEach>
																</select>
															</div>
														</div>
													</form>

													<form class="form-horizontal" id="hostmove" role="form"
														action="<%=request.getContextPath() %>/cdrpm/${sdi.id }/hostMigration"
														method="post">
														<div class="form-group">
															<label for="input01" class="col-sm-2 control-label"
																style="width: 150px;">仓库 </label>
															<div class="col-sm-4" id="selectbox">
																<select class="chosen-select   form-control"
																	style="width: 300px;" name="wareHostId" id="wareHostId"
																	parsley-trigger="change" parsley-required="true"
																	parsley-error-container="#selectbox">
																	<option value="">请选择仓库</option>
																	<c:forEach items="${wareHoseList }" var="sdi">
																		<option value="${sdi.id }">${sdi.name}</option>
																	</c:forEach>
																</select>
															</div>
														</div>
													</form>

												</div>
												<div class="modal-footer">
													<button class="btn btn-green" id="form_btn"
														onclick="addDesktop();">确定</button>
													<button class="btn btn-red" data-dismiss="modal"
														aria-hidden="true">取消</button>

												</div>
											</div>
											<!-- /.modal-content -->
										</div>
										<!-- /.modal-dialog -->
									</div>
									<!-- /.modal -->
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
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/hostwarehouseform.js"></script>
	<script>
    var path = '<%=request.getContextPath()%>'; 
    var uuid =  '${uuid}' ;
    var curIds;
    $(function(){
	  var typeString = $("#typeStringValue").val();
	  if(typeString !='1'){
		  alert(typeString);
		  $(".div221").addClass("typeString");
	  }
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
      

      /* Get the rows which are currently selected */
      function fnGetSelected(oTable01Local){
        return oTable01Local.$('tr.row_selected');
      };
	    $("div[class='col-md-4 text-right sm-center']").attr("class","tile-footer text-right");
	  $("#add_host").click(function(){
		  addHosts(warehouseId,sysImageName);
 	  });
	  
		//迁移记录
		$("#migrationLocalHost").click(function() {
			var id = 0;
			location.href=path +"/cdrpm/{"+id+"}/queryHostMigration"; 
		});
	  
	  //全选
	jQuery("#leftCheckBoxAll").click(function() {
		if($('#leftCheckBoxAll').is(':checked')){
			$("[name='leftCheckBox']:checkbox").attr("checked", true);
		}else{
			$("[name='leftCheckBox']:checkbox").attr("checked", false);
		}
	  });
    })
	  //del绑定click 事件
	  $("#rightLt").click(function() {
		  //判断主机是否选中
		  if($('#leftCheckBox').is(':checked')){
			  //需要获取选择的CheckBox的值
			  var rightDivContent = document.getElementById("rightDivContent");
			  rightDivContent.innerHTML="<td width='30px'><input type='checkbox' name='rightCheckBox' id='rightCheckBox' /></td><td width='126px'><div class='div99'><img id='u90_img' class='text1' src='<%=request.getContextPath()%>/images/hostMigration/u64.png'/></div><div class='div101'>${cloudHost.getDisplayName()}</div></td><td width='126px'><div class='div99'><img id='u114_img' class='text2' src='<%=request.getContextPath()%>/images/hostMigration/u64.png'/></div><div id='innerIp' class='div101'>${cloudHost.getInnerIp()}</div></td><input id='hostType' type='hidden' value='${cloudHost.getType()}'> <input id='displayName' type='hidden' value='${cloudHost.getDisplayName()}'>";
			  var rightDivContent1 = document.getElementById("rightDivContent1");
			  rightDivContent1.innerHTML="<td><div class='div80'><img id='u80_line' class='img1 div444' src='<%=request.getContextPath()%>/images/hostMigration/u70_line.png' alt='u80_line'/></div></td>";
							} else {
								$("#tipscontent").html("请选择要迁移的主机");
								$("#dia").click();
							}

						});
		//去掉要选择的主机
		jQuery("#leftGt").click(function() {
			document.getElementById("rightDivContent").innerHTML = "";
			document.getElementById("rightDivContent1").innerHTML = "";
		})

		//迁移提交按钮
		jQuery("#migrationBut")
				.click(
						function() {
							if ($('#rightCheckBox').is(':checked')) {
								var displayName = $("#displayName").val();//主机名
								var innerIp = jQuery("#innerIp").text();//源NC
								var type = $("#hostType").val();//主机类型
								var id = $("#migrationNC  option:selected").val();//目的NC
								var realHostId = $("#realHostId").val();//目的NC
								var nodeName = $("#migrationNC  option:selected").text();
								
								if('选择NC' == id){
									$("#tipscontent").html("请选择目的NC");
									$("#dia").click();
									return false;
								}
								
								jQuery
										.ajax({
											url : path + '/cdrpm/toMigration',
											type : 'post',
											data : {
												id : id,
												realHostId:realHostId,
												displayName : displayName,
												type : type,
												innerIp :innerIp,
												nodeName:nodeName
											},
											dataType : 'json',
											timeout : 10000,
											async : false,
											success : function(data) {
												if (data.status == "success") {
													location.href=path +"/cdrpm/{"+realHostId+"}/getostMigrationPree"; 
												} else if (data.status == "fail") {
													return;
												}
											}
										});
							} else {
								$("#tipscontent").html("请选择要迁移的主机");
								$("#dia").click();
							}
						})

		//主机迁移（批量）
		function hostMigrations(ids) {
			//post表单提交到后台调整页面
		}

		//主机迁移（批量）
		function hostMigration(id) {

		}

		$(function() {

			//check all checkboxes
			$('table thead input[type="checkbox"]').change(
					function() {
						$(this).parents('table').find(
								'tbody input[type="checkbox"]').prop('checked',
								$(this).prop('checked'));
					});

			// sortable table
			$('.table.table-sortable th.sortable').click(
					function() {
						var o = $(this).hasClass('sort-asc') ? 'sort-desc'
								: 'sort-asc';
						$(this).parents('table').find('th.sortable')
								.removeClass('sort-asc').removeClass(
										'sort-desc');
						$(this).addClass(o);
					});

			//chosen select input
			$(".chosen-select").chosen({
				disable_search_threshold : 10
			});

			//check toggling
			$('.check-toggler').on('click', function() {
				$(this).toggleClass('checked');
			})

		});
	</script>
</body>
</html>


