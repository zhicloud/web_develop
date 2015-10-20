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
    <link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/css/ligerui-tree.css" />
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
            

            <h2><i class="fa fa-desktop"></i> 分配终端盒子</h2>
            

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
                    <h3><a href="javascript:void(0);" onclick="history.go(-1);"   style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>分配给用户</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body no-vpadding" style="height:460px;"> 
                   
                    <div class="table-responsive" style="width:100%;height:80%;float: left;overflow: auto;">
                    	<div id="assigntree" style="width:100%"></div>

                    </div>
			<div style="width:100%;text-align: center;float: left;margin-top:50px;">
				<button type="button" class="btn btn-primary add" onclick="saveMenu()">保存</button>
				<button type="button" class="btn btn-default add" onclick="backhome()">返回</button>
			</div>                    
                    
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

                    <%--<div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >--%>
                      <%--<div class="modal-dialog">--%>
                        <%--<div class="modal-content" style="width:60%;margin-left:20%;">--%>
                          <%--<div class="modal-header">--%>
                            <%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>--%>
                            <%--<h3 class="modal-title" id="modalConfirmLabel"><strong>确认</strong> </h3>--%>
                          <%--</div>--%>
                          <%--<div class="modal-body">--%>
                            <%--<form role="form">   --%>

                              <%--<div class="form-group">--%>
                                <%--<label style="align:center;" id="confirmcontent">确定要删除该镜像吗？</label>--%>
                               <%--</div>--%>

                            <%--</form>--%>
                          <%--</div>--%>
                          <%--<div class="modal-footer">--%>
                            <%--<button class="btn btn-green"   onclick="toDelete();" data-dismiss="modal" aria-hidden="true">确定</button>--%>
                            <%--<button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>--%>
                            <%----%>
                          <%--</div>--%>
                        <%--</div><!-- /.modal-content -->--%>
                      <%--</div><!-- /.modal-dialog -->--%>
                    <%--</div><!-- /.modal -->                        --%>

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

	<script type="text/javascript" src="<%=request.getContextPath()%>/js/base.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/ligerTree.js"></script>
    <script type="text/javascript">
    var path = "<%=request.getContextPath()%>";
    var curBoxId = "${terminal_box.id}";
    $(function(){
      setMenu();

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

    })
    function setMenu(){
    $("#assigntree").html("");

	jQuery.ajax({
 	 	type: "POST",
 	 	async:false,
  		url: "<%=request.getContextPath() %>/box/assignTree",
 		contenttype :"application/x-www-form-urlencoded;charset=utf-8",
  		success: function(result){
  		  var obj = eval("("+result+")");
  		  console.info(obj);
    	if(obj.length>0){

    		jQuery("#assigntree").ligerTree({  
    	        data: obj
    	        });
    		jQuery("#assigntree").css("width","100%");
    		

    	}else{
    		alert(obj.result);
    	}
  	}
	});	  
}
//保存角色和菜单关联信息
function saveMenu(){
		var manager = $("#assigntree").ligerGetTreeManager();
		var note = manager.getChecked();
		var menuids = new Array();
		for(var i=0;i<note.length;i++){
			if(note[i].data.level=="user"){
				menuids.push(note[i].data.id);
			}
		}
        if(menuids.length < 1){
			$("#tipscontent").html("您尚未选择要分配的用户");
		    $("#dia").click();
			return;
		}else if(menuids.length > 1){
			$("#tipscontent").html("请最多选择"+1+"位用户进行分配");
		    $("#dia").click();
			return;
		}else{
			jQuery.ajax({
				url: path+'/box/allocate',
				type: 'post', 
				data:{boxId:curBoxId,userId:menuids[0]},
				dataType: 'json',
				timeout: 10000,
				async: false,
				success:function(data){
					if(data.status == "success"){
						$("#tipscontent").html(data.message);
					    $("#dia").click();
						location.href=path +"/box/list"; 
					}else if(data.status == "fail"){
						$("#tipscontent").html(data.message);
					    $("#dia").click();
						return;
					}
				}
			});
		}
}
function backhome(){
	window.location.href = "<%=request.getContextPath()%>/box/list";
}
    </script>
    
  </body>
</html>
      

