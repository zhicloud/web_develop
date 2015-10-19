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
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/vendor/animate/animate.css">
    <link type="text/css" rel="stylesheet" media="all" href="<%=request.getContextPath()%>/assets/js/vendor/mmenu/css/jquery.mmenu.all.css" />
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/videobackground/css/jquery.videobackground.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/css/vendor/bootstrap-checkbox.css">

    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen.min.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/assets/js/vendor/chosen/css/chosen-bootstrap.css">

    <link href="<%=request.getContextPath()%>/assets/css/zhicloud.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
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
            

            <h2><i class="fa fa-server"></i> 备份与恢复</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            


            <!-- row -->
            <div class="row">
              
              <div class="col-md-12">



                <!-- tile -->
                <section class="tile color transparent-black">


 

                  <!-- tile body -->
                  <div class="tile-body">

                     
                    <h5 class="filled greensea">说明</h5>
                    <p class="filled greensea"> 
                      1.由于可能存在文件所定的情况，我们不承诺100%成功，若有非常重要的数据，请您自己备份。<br><br>
					  2.云主机还原功能将用以前的备份的虚拟硬盘VHD文件覆盖现有虚拟硬盘文件。<br><br>
					  3.由于虚拟硬盘VHD文件往往达到几十个G，因此备份及还原都需要一个小时或更长的时间才能完成。<br><br>				                      
                    </p>
                    <div id="situation">
                      	<h5 class="filled slategray">备份情况</h5>
	                    <p class="filled slategray"> 
	                  	  上次备份时间：<span id="lasttime">正在获取备份信息。。。</span>  &nbsp;&nbsp;&nbsp;  <button type="button" class="btn btn-orange btn-sm margin-bottom-30" onclick="$('#bu').click();">马上备份</button>       
	                    </p>
                    </div>
                    
                    
                     <div class="progress-list" id="progress_div">
                      <div class="details">
                        <div class="title"><strong id="tips"></strong></div>
                       </div>
                      <div class="status pull-right">
                        <span class="animate-number" id="progress1"  data-value="0" data-animation-duration="1500">0</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little no-radius">
                        <div class="progress-bar progress-bar-orange animate-progress-bar" id="progress2" data-percentage="0%"></div>
                      </div>
                    </div>
                    

                     
                  </div>
                  <!-- /tile body -->
                  
                


                </section>
                <!-- /tile -->
                
                <div class="tile-body">

                    <a href="#modalDialog" id="dia" role="button"  data-toggle="modal"> </a>
                    <a href="#modalConfirm" id="con" role="button"   data-toggle="modal"> </a>
                     <a href="#backup" id="bu" role="button"   data-toggle="modal"> </a>

                    
                    

                    <div class="modal fade" id="modalConfirm" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true"  >
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:70%;margin-left:20%;">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>确认</strong> </h3>
                          </div>
                          <div class="modal-body">
                            <form role="form">   

                              <div class="form-group">
                                <label style="align:center;" id="confirmcontent">确定要删除该镜像吗？</label>
                               </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green"   onclick="cloud_host_resume();" data-dismiss="modal" aria-hidden="true">确定</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">取消</button>
                            
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal --> 
                    
                    <div class="modal fade" id="backup" tabindex="-1" role="dialog" aria-labelledby="modalConfirmLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">Close</button>
                            <h3 class="modal-title" id="modalConfirmLabel"><strong>新增备份</strong></h3>
                          </div>
                          <div class="modal-body">
                            <form class="form-horizontal" role="form"  id="basicvalidations_password" action="<%=request.getContextPath() %>/cloudserver/addbackup" method="post"   >
                            	<input id = "hostId" name="hostId" value="${host.realHostId}" type="hidden">
                               
                              <div class="form-group">
		                        <label for="input01" class="col-sm-4 control-label">备份类型</label>
		                        <div class="col-sm-8">
		                          <div class="radio   col-md-4">
		                            <input type="radio" name="mode"   id="mode1" value="0" onclick="$('#choose_disk').hide();">
		                            <label for="mode1">全备份</label>
		                          </div>
		                          <div class="radio  col-md-6">
		                            <input type="radio" name="mode"   id="mode2" value="1" onclick="$('#choose_disk').show();" checked>
		                            <label for="mode2">部分备份</label>
		                          </div> 
		                        </div>
		                      </div>
		                      
		                      <div class="form-group" id="choose_disk">
		                        <label for="input01" class="col-sm-4 control-label">备份盘选择</label>
		                        <div class="col-sm-8">
		                          <div class="radio   col-md-4">
		                            <input type="radio" name="disk"   id="disk1" value="0" >
		                            <label for="disk1">系统盘</label>
		                          </div>
		                          <div class="radio  col-md-6">
		                            <input type="radio" name="disk"   id="disk2" value="1" checked>
		                            <label for="disk2">数据盘</label>
		                          </div> 
		                        </div>
		                      </div>

                            </form>
                          </div>
                          <div class="modal-footer">
                            <button class="btn btn-green" onclick="saveForm();">备份</button>
                            <button class="btn btn-red" data-dismiss="modal" aria-hidden="true">关闭</button>
                          </div>
                        </div><!-- /.modal-content -->
                      </div><!-- /.modal-dialog -->
                    </div><!-- /.modal -->   
                    
                    <div class="modal fade" id="modalDialog" tabindex="-1" role="dialog" aria-labelledby="modalDialogLabel" aria-hidden="true">
                      <div class="modal-dialog">
                        <div class="modal-content" style="width:60%;margin-left:20%;">
                          <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true" onclick="window.location.reload();">Close</button>
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


 
          <script type="text/javascript" src="<%=request.getContextPath()%>/js/plugins/jquery.form.js"></script>
    
    <script>
    
    var path = '<%=request.getContextPath()%>'; 
    var sessionId = '${sessionId}';
    var backupFlag = '${backupFlag}';
    var resumeFlag = '${resumeFlag}';
    var uuid = '${host.realHostId}';
    var mode = 0;
    var disk = 0;
    var isCommited = false;
    var timestamp = "";

    $(function(){
      
     

      //chosen select input
      $(".chosen-select").chosen({disable_search_threshold: 10});
      
      //查询备份信息
      queryBackup(); 
	  statusCheck(); 
      
      
      
    }); 
    
    function statusCheck() {
		if(backupFlag == 1) {
			refreshBackupProgress();
			hideElement("situation");
			$("#tips").html("正在备份。。。")
 			
		}
		if(resumeFlag == 1) {
			refreshResumeProgress();
			hideElement("situation");
			$("#tips").html("正在恢复。。。")
		}
		if(resumeFlag == 0 && backupFlag == 0){
			hideElement("progress_div");
		}
	}  
    function saveForm(){
		if(isCommited){
     		return false;
		 } 
		isCommited=true;
    	
    	
		jQuery.ajax({
	        url: path+'/main/checklogin',
	        type: 'post', 
	        dataType: 'json',
	        timeout: 10000,
	        async: true,
	        error: function()
	        { 
	        	isCommited = false;
	        },
	        success: function(result)
	        {
	        	if(result.status == "fail"){ 
	        		  isCommited = false;
	        		  $("#tipscontent").html("登录超时，请重新登录");
	     		      $("#dia").click();
		        	}else{ 
   		        			var options = {
   		        					success:function result(data){
   		        						if(data.status == "fail"){
   		        							  isCommited = false;
							        		  $("#tipscontent").html("备份失败");
							     		      $("#dia").click();  		        							
   		        						}else{  		        							
	   		        						window.setTimeout(refreshPage(),2000);
   		        						}
   		        					},
   		        					dataType:'json',
   		        					timeout:10000
   		        			};
   		        			var form = jQuery("#basicvalidations_password");   		        				
			        		jQuery("#basicvalidations_password").ajaxSubmit(options); 
 		        	} 
	        }
	     });
    } 
function refreshBackupProgress(){
	console.log("start"+new Date());
	jQuery.ajax({
		url: path+'/cloudserver/getbackupprogress',
		type: 'get',
		data: 'uuid=' + '${host.realHostId}',
		dataType: 'json', 
		async: false,
		error: function(result)
		{ 
		},
		success: function(result)
		{  
			 if(result.status == "success") 
				{   
				 if( result.properties.backup_status==true )
					{
					 window.setTimeout(refreshPage(),2000);
					} else{
						if(result.properties.isfail == true){
							  $("#tipscontent").html("备份失败");
			     		      $("#dia").click();  
						 }else{
							 $("#progress2").attr("data-percentage",result.properties.progress+"%"); 
							   $("#progress2").css("width",result.properties.progress+"%"); 
							   $("#progress1").html(result.properties.progress+""); 
							   $("#progress1").attr("data-value",result.properties.progress);
							   window.setTimeout(self.refreshBackupProgress, 5000);
						 }
						 
					}
				
				}
			 else if(result.properties.backup_status==null )
				{
					
					window.setTimeout(self.refreshBackupProgress, 5000);
				}
				else if(result.properties.backup_status==false )
				{ 								
					$("#backup_progress").html("备份失败");
					$("#backup_progressbar_id").hide();
					showElement("back_from_backup_to_main");
				}
				
			else 
			{ 
				// 云主机尚未开始备份，继续获取信息
				window.setTimeout(self.refreshBackupProgress, 5000);
			}
		}
	}); 
}
 

function confirmResum(){
	$("#confirmcontent").html("您确定要将主机恢复到"+timestamp+"吗？");
	$("#con").click();
}

function cloud_host_resume() {
	jQuery.ajax({
		url: path+'/cloudserver/resumhost',
		type: 'post',
		data: 'hostId=' + '${host.realHostId}&mode='+mode+'&disk='+disk,
		dataType: 'json', 
		async: false,
		error: function(result)
		{
			alert('Error!');
		},
		success: function(result)
		{  
			 if(result.status == "success") 
				{   
				 window.setTimeout(refreshPage(),2000);
 				}
				 
			else 
			{ 
				 $("#tipscontent").html("恢复失败");
    		      $("#dia").click(); 
			}
		}
	});  
	
};

function refreshPage(){
	window.location.href=window.location.href;

}

function queryBackup() { 
	jQuery.ajax({
        url: path+'/cloudserver/querybackinfo',
        type: 'post', 
        data:"id="+'${host.id}',
        dataType: 'json',
        timeout: 10000,
        async: true,
        error: function(result)
        { 
        },
        success: function(result)
        { 
        	if (result.status == "exception") {
				top.$.messager.alert('警告', reply.exceptionMessage,'warning');
			} else if (result.status == "success") {
				timestamp = result.properties.timestamp;
				mode = result.properties.mode;
				disk = result.properties.disk;
				$("#lasttime").html("<a href='javascript:void(0);' onclick='confirmResum();'>"+timestamp+"</a>");
			} else {
				$("#lasttime").html(result.message);
			}
        }
     }); 
}; 
 

function refreshResumeProgress(){
	jQuery.ajax({
		url: path+'/cloudserver/getresumprogress',
		type: 'get',
		data: 'uuid=' + '${host.realHostId}',
		dataType: 'json', 
		async: false,
		error: function(result)
		{
//			alert('Error!');
		},
		success: function(result)
		{  
			 if(result.status == "success") 
				{   
				 if( result.properties.resume_status==true )
					{
						window.location.reload();
					} else{
						 if(result.properties.isfail == true){
							  $("#tipscontent").html("恢复失败");
			     		      $("#dia").click();  
						 }else{
							 if (typeof(result.properties.progress) == "undefined") { 
								 $("#tipscontent").html("恢复失败");
				     		      $("#dia").click();
				     		      return ;
								}  
							 $("#progress2").attr("data-percentage",result.properties.progress+"%"); 
							   $("#progress2").css("width",result.properties.progress+"%"); 
							   $("#progress1").html(result.properties.progress+""); 
							   $("#progress1").attr("data-value",result.properties.progress);
							   window.setTimeout(self.refreshResumeProgress, 5000);
						 }
						 
					}
				
				}
			 else if(result.properties.backup_status==null )
				{
					
					window.setTimeout(self.refreshResumeProgress, 5000);
				}
				else if(result.properties.backup_status==false )
				{ 								
					$("#backup_progress").html("恢复失败");
					$("#backup_progressbar_id").hide();
					showElement("back_from_backup_to_main");
				}
				
			else 
			{ 
				// 云主机尚未开始备份，继续获取信息
				window.setTimeout(self.refreshResumeProgress, 5000);
			}
		}
	}); 
		
}ni

function showElement(targetid){
    if (document.getElementById){
        target=document.getElementById(targetid);
        target.style.display="block";
    }
}

function hideElement(targetid){
    if (document.getElementById){
        target=document.getElementById(targetid);
        target.style.display="none";
    }
}

      
    </script>
  </body>
</html>
      

      

      
 