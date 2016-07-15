<%@ page pageEncoding="utf-8"%>
<!-- monitor_view_detail.jsp -->
  <head>
    <title>查看详情 </title>
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
//返回
function backserver(){
	var type = "${objectdata.type}";
	if(type=="server"){
		window.location.href = window.location.href = "<%=request.getContextPath()%>/monitor/serverquery";
	}
	if(type=="host"){
		window.location.href = window.location.href = "<%=request.getContextPath()%>/monitor/hostquery";
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
            
            <h2><i class="fa fa-signal"></i> 
            	<c:if test="${objectdata.type=='server' }">查看服务器详情</c:if>
            	<c:if test="${objectdata.type=='host' }">查看云主机详情</c:if>
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
                  	    <button type="button" class="btn btn-success delete" onclick="backserver()">
	                              <i class="fa fa-step-backward"></i>
	                              <span> 
					            	<c:if test="${objectdata.type=='server' }">返回服务器</c:if>
					            	<c:if test="${objectdata.type=='host' }">返回云主机</c:if>
	                              </span>
	                    </button>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body" style="padding-bottom:0px;margin-bottom:-15px;">
                    <form class="form-horizontal" role="form" parsley-validate id="basicvalidations">
                      
                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">云主机名称:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control"  id="name"  disabled="true" value="${objectdata.hostName }">
                        </div>
                        <label for="code" class="col-sm-2 control-label">uuid:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="code" disabled="true" value="${objectdata.uuid }">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">真实主机名:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control"  id="name"  disabled="true" value="${objectdata.realHostId }">
                        </div>
                        <label for="code" class="col-sm-2 control-label">系统镜像:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="code" disabled="true" value="${objectdata.sysImageName }">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">系统用户名:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control"  id="name"  disabled="true" value="${objectdata.account }">
                        </div>
                        <label for="code" class="col-sm-2 control-label">系统密码:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="code" disabled="true" value="${objectdata.password }">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">内网IP:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control"  id="name"  disabled="true" value="${objectdata.innerIp }">
                        </div>
                        <label for="code" class="col-sm-2 control-label">内网端口:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="code" disabled="true" value="${objectdata.innerPort }">
                        </div>
                      </div>
                      
                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">外网IP:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control"  id="name"  disabled="true" value="${objectdata.outerIp }">
                        </div>
                        <label for="code" class="col-sm-2 control-label">外网端口:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="code" disabled="true" value="${objectdata.outerPort }">
                        </div>
                      </div>
                      
					 <%-- <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">名称:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control"  id="name"  disabled="true" value="${objectdata.name }">
                        </div>
                        <label for="code" class="col-sm-2 control-label">状态:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="code" disabled="true" value="${objectdata.status }">
                        </div>
                      </div> --%>

                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">CPU:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control"  id="name"  disabled="true" value="${objectdata.cpu_count }">
                        </div>
                        <label for="code" class="col-sm-2 control-label">CPU利用率:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="code" disabled="true" value="${objectdata.cpu_usage }">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">内存:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control"  id="name"  disabled="true" value="${objectdata.memory }">
                        </div>
                        <label for="code" class="col-sm-2 control-label">内存利用率:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="code" disabled="true" value="${objectdata.memory_usage }">
                        </div>
                      </div>
                      <div class="form-group">
                        <label for="name" class="col-sm-2 control-label">磁盘:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control"  id="name"  disabled="true" value="${objectdata.disk }">
                        </div>
                        <label for="code" class="col-sm-2 control-label">磁盘利用率:</label>
                        <div class="col-sm-4">
                          <input type="text" class="form-control" id="code" disabled="true" value="${objectdata.disk_usage }">
                        </div>
                      </div>
                      <div class="form-group form-footer footer-white">
                        <div class="col-sm-offset-4 col-sm-8">
                        </div>
                      </div>
                      <!-- modify -->
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
