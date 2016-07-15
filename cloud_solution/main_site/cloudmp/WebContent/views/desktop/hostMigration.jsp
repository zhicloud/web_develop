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
.img{
    position: absolute;
    left: 0px;
    top: 0px;
    width: 160px;
    height: 50px;
}
.imghead{cursor: pointer;}
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
.divMag{
    margin-left: 95px;
    height: 35px;
}

.div444{margin-left: 515px;}

.divAline{margin-left: 160px;margin-top: 30px;}
.divDistance{padding-left: 5px;}
.divDistance1{margin-left: 10px;}
.divDistance2{margin-left: 5px;}
.divBut{width: 200px;height: 59px;background-color: #717171;border: 0px;}
.divBut111{width: 45%;height: 2px;background-color: #717171;border: 0px;margin-left: 160px;}
.divTop{height: 51px;padding-top: 25px;}
.textarea{width: 50%;}
.div331{position: absolute;margin-left: 23px;}
.div333{text-align: center;flood-color: inherit;color: beige;margin-left: 35px;margin-top: -24px;}
.div334{margin-top: 15px;margin-left: 220px;}
.divFloat{float: left;}
.quxiao{margin-left: 34px;margin-top: -25px;color: beige;}
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

								<div class="tile-body no-vpadding divMag" style="margin-left: 157px;">
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
								<div class="divAline">迁移至</div>
								<div><button class="divBut111"/></div>
								<div style="margin-left: 160px;">
									<div class="divTop">选择目的NC</div>
									<div>
										<select class="textarea" style="height: 300px;font-size: 25px;" id="migrationNC" name="migrationNC" multiple=multiple size="6" >
										    <option value="${poolUuid}" selected="selected">系统分配</option>
										    <c:forEach items="${computerPool }" varStatus="i" var="cp">
										    	<option value="${poolUuid}">${cp.name }</option>
									    	</c:forEach>
										</select>
									</div>
									
									
									<input id="realHostId" type="hidden" value="${cloudHost.getRealHostId()}"/>
									<input id="displayName" type="hidden" value="${cloudHost.getDisplayName()}"/>
									<input id="innerIp" type="hidden" value="${cloudHost.getInnerIp()}"/>
									<input id='hostType' type='hidden' value='${cloudHost.getType()}'/>
									<input id='uid' type='hidden' value='${cloudHost.id}'/>
									
									
									<div class="div334">
										<div id="migrationBut" class="ax_01 imghead divFloat" data-label="确定">
									        <div class="div330"><img id="u106_img" class="" src="<%=request.getContextPath()%>/images/hostMigration/u106.png"/></div>
									        <div id="u107" class="div331 div333">
									          <p><span>确定</span></p>
									        </div>
									    </div>
										<div id="migrationBut" class="ax_01 imghead" style="margin-left: 137px;" data-label="取消">
									        <div class=""><img id="u106_img" class="" src="<%=request.getContextPath()%>/images/hostMigration/u106.png"/></div>
									        <div id="u107" class="quxiao">
									          <p><span>取消</span></p>
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

									<div class="modal fade" id="modalAddDesktop" tabindex="-1"
										role="dialog" aria-labelledby="modalConfirmLabel"
										aria-hidden="true">
										<div class="modal-dialog">
											
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
    var nameList =  '${nameList}' ;
    var curIds;
    $(function(){
	  var typeString = $("#typeStringValue").val();
	  if(typeString !='1'){
		  //alert(typeString);
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
			location.href=path +"/cdrpm/"+id+"/queryHostMigration"; 
		});
	  
    })
    
		//迁移提交按钮
		jQuery("#migrationBut")
				.click(
						function() {
							var realHostId = $("#realHostId").val();
							if(realHostId == '' || realHostId == null){
								$("#tipscontent").html("请检查主机是否存在");
								$("#dia").click();
								return false;
							}
							var displayName = $("#displayName").val();//主机名
							var innerIp = jQuery("#innerIp").text();//源NC
							var type = $("#hostType").val();//主机类型
							
							var id = $("#migrationNC  option:selected").val();//目的NC
							var nodeName = $("#migrationNC  option:selected").text();
							
							//下拉列表长度
							//var optionLenfth =$('#migrationNC option').length;
							var optionSelValue = systemDistribution();//随机获取下拉列表节点值
							
							if('系统分配' == nodeName){
								nodeName = optionSelValue;
							}
							
							//return false;
							jQuery
									.ajax({
										url : path + '/cdrpm/toMigration',
										type : 'post',
										data : {
											id : id,
											realHostId:realHostId,
											innerIp :innerIp,
											nodeName:nodeName,
											uuid:$("#uid").val()
										},
										dataType : 'json',
										timeout : 10000,
										async : false,
										success : function(data) {
											if (data.status == "success") {
												//location.href=path +"/cdrpm/"+realHostId+"/getostMigrationPree"; 
												location.href=path +"/cdrpm/0/queryHostMigration";
											} else if (data.status == "fail") {
												return;
											}
										}
									});
						})

		//主机迁移（批量）
		function hostMigrations(ids) {
			//post表单提交到后台调整页面
		}

		//主机迁移（批量）
		function hostMigration(id) {

		}
		
		 function getSelectedText(id){
             var obj=document.getElementById(id);      //获取属性
             var index=obj.selectedIndex;    //将出现在文本框中的option的索引保存到变量index中
             var c=obj.options[index].text;       //通过保存在index中的索引获取字符串
             return c;
         }
		 
		 function systemDistribution(){   
	        var text=getSelectedText("migrationNC");
		    if(text=="系统分配"){
	             return breakfirst();
		    }
		}
		function breakfirst(){
			 var lists=[];
			 if(nameList.indexOf(",")){
				 var arry = nameList.split(",");
				 for(var i=0;i<arry.length;i++){
					 lists.push(arry[i]);
				 }
			 }else{
				 lists.push(nameList);
			 }
		    
		    return lists[selectFrom(0,lists.length-1)];
		}

	    function selectFrom(lowerValue,upperValue){ // 传入两个应该返回的最小值和最大值
	        var choice=upperValue-lowerValue+1;            //得到可能值的总数并保存在变量choice中
	        return Math.floor(Math.random()*choice+lowerValue);        //返回数值  
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