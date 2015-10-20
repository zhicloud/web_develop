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
            

            <h2><i class="fa fa-users"></i> 修改分组信息</h2>
            

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
                    <h3><a href="<%=request.getContextPath() %>/group/all"    style="color:#FAFAFA;cursor:pointer;padding-right:10px;"> <i class="fa fa-reply"></i></a>输入分组信息</h3>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <form class="form-horizontal" id="sys_group_form_update" role="form" action="${pageContext.request.contextPath }/group/${group.id}/update" method="post">
                      
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">分组名*</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" name="groupName" oldname="${group.groupName }" value="${group.groupName }" id="group_name" parsley-trigger="change" parsley-required="true" parsley-checkdesktopgroupname="true" parsley-minlength="1" parsley-maxlength="50" parsley-validation-minlength="1">
                        </div>
                      </div>
                        
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">上级分组</label>
                        <div class="col-sm-4" id="selectbox"> 
                            <div class="col-sm-16" >
                            	<select name="parentId" class="chosen-select chosen-transparent form-control" id="parentId" parsley-error-container="#selectbox">
                                <option value="" selected="selected">请选择</option>
                                <c:forEach items="${groupList }" var="group">
                                	<option value="${group.id }">${group.groupName }</option>
                                </c:forEach>
                                </select> 
                            </div> 
                          
                        </div>
                      </div>
                        
                      <div class="form-group">
                        <label for="input01" class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-4">
                          <textarea class="form-control" value="${group.description }" name="description" id="description" parsley-maxlength="100"></textarea>
                        </div>
                      </div>
                    
                       

                     <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8">
                          <button type="button" class="btn btn-greensea" id="save_btn"><i class="fa fa-plus"></i>
                              <span> 保 存 </span></button>
                          <button type="reset" class="btn btn-red"><i class="fa fa-refresh"></i>
                              <span> 重 置 </span></button>
                        </div>
                      </div>
                            
                    </form>

                  </div>
                  <!-- /tile body -->
                  
                


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
	
    <script type="text/javascript">
    var path = "<%=request.getContextPath()%>";
    var parentId = "${group.parentId}";
    var desc = "${group.description }";
    $(function(){
    	if(parentId!=0){
     		 $("#parentId").val(parentId);
    	}
    	$("#description").val(desc);
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
	  
      jQuery("#save_btn").click(function(){
      	var options = {
  				success:function result(data){
  					location.href = "${pageContext.request.contextPath}/group/all";
  				},
  				dataType:'json',
  				timeout:10000
  		};
      	var form = jQuery("#sys_group_form_update");
		form.parsley('validate');
		if(form.parsley('isValid')){  		        				
			jQuery("#sys_group_form_update").ajaxSubmit(options); 
		}
       });
    })
      
    </script>
    
  </body>
</html>
      

