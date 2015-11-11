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
            

            <h2><i class="fa fa-compress"></i> 添加智能路由例外规则</h2>
            

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
                    <h3><a href="<%=request.getContextPath() %>/rule/all"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>输入规则信息</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <form class="form-horizontal" id="rule" role="form" action="${pageContext.request.contextPath }/rule/add" method="post"> 
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">智能路由*</label>
                        <div class="col-sm-4" id="selectmark">
							<select class="chosen-select chosen-transparent form-control" name="markId" id="markId" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectmark">
	                            <option value="">请选择智能路由</option>  
	                            <c:forEach items="${targetarray }" var="sdi">
 	                                 <option value="${sdi.name }">${sdi.name }</option>
 	                             </c:forEach>  
	                          </select>                       
	                     </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">规则*</label>
                        <div class="col-sm-4" id="selectpool">
							<select class="chosen-select chosen-transparent form-control" name="mode" id="mode" parsley-trigger="change" parsley-required="true" parsley-error-container="#selectpool">
	                            <option value="0" checked="checked">INPUT规则</option>   
	                          </select>                       
	                     </div>
                      </div>
                         
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">目的IP </label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="ip1" name="ip1"  parsley-trigger="change"  parsley-ip="true"   parsley-maxlength="50" parsley-validation-minlength="1">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">源IP </label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="ip2" name="ip2"  parsley-trigger="change"  parsley-ip="true"  parsley-maxlength="50" parsley-validation-minlength="1">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">端口 *</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="port1" name="port1"  parsley-trigger="change" parsley-required="true" parsley-type="integer"   parsley-maxlength="50" parsley-validation-minlength="1">
                        </div>
                      </div>
                    
                       

                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8">
                          <button id="create_btn" type="button" class="btn btn-greensea" onclick="saveRuleForm();"><i class="fa fa-plus"></i>
                              <span> 创 建 </span></button>
                          <button type="reset" class="btn btn-red"><i class="fa fa-refresh"></i>
                              <span> 重 置 </span></button>
                        </div>
                      </div>
                            
                    </form>

                  </div>
                  <!-- /tile body -->
                  
                


                </section>

                <!-- /tile -->
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






<%-- 	<script type="text/javascript" src="<%=request.getContextPath()%>/js/custom/rule.js"></script> --%>
	
    <script type="text/javascript">
    var path = "<%=request.getContextPath()%>";
    var isCommited= false;
    
    $(function(){
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
    
    function saveRuleForm(){
		if(isCommited){
     		return false;
		 } 
		isCommited=true; 
		
		var options = {
				success:function result(data){ 
					if(data.status=="success"){
						location.href = path+"/rule/all";
					}else{
						isCommited= false;
						$("#create_btn").attr("disabled",false);
						jQuery("#tipscontent").html(data.message);
		   		        jQuery("#dia").click();
						return;
					}
				},
				error:function result(data){ 
					isCommited= false;
					$("#create_btn").attr("disabled",false);
					jQuery("#tipscontent").html(data.message);
	   		        jQuery("#dia").click();
					return; 
				},
				dataType:'json',
				timeout:10000
		};
		var form = jQuery("#rule");
		form.parsley('validate');
		if(form.parsley('isValid')){ 
			$("#create_btn").attr("disabled",true);
			jQuery("#rule").ajaxSubmit(options); 
		}
}
      
    </script>
    
  </body>
</html>
      

