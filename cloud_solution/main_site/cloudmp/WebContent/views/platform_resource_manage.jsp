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
            

            <h2><i class="fa fa-tachometer"></i> 全局实时状态</h2>
            

          </div>
          <!-- /page header -->
          

          <!-- content main container -->
          <div class="main">



            <!-- cards -->
            <div class="row cards">
              
              <div class="card-container col-lg-3 col-sm-6 col-sm-12">
                <div class="card card-redbrown hover">
                  <div class="front"> 

                    <div class="media">        
                      <span class="pull-left">
                        <i class="fa fa-server media-object"></i>
                      </span>

                      <div class="media-body">
                        <big>云服务器</big>
                        <h2 class="media-heading animate-number" data-value="${totalServer }" data-animation-duration="1500">0</h2>
                      </div>
                    </div> 

                    <div class="progress-list">
                      <div class="details">
                        <div class="title">CPU利用率</div>
                      </div>
                      <div class="status pull-right bg-transparent-black-1">
                        <span class="animate-number" data-value="${sCpuUsage }" data-animation-duration="1500">0</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little progress-transparent-black">
                        <div class="progress-bar animate-progress-bar" data-percentage="${sCpuUsage }%"></div>
                      </div>
                    </div>
                    <div class="progress-list">
                      <div class="details">
                        <div class="title">内存利用率</div>
                      </div>
                      <div class="status pull-right bg-transparent-black-1">
                        <span class="animate-number" data-value="${sMemoryUsage }" data-animation-duration="1500">0</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little progress-transparent-black">
                        <div class="progress-bar animate-progress-bar" data-percentage="${sMemoryUsage }%"></div>
                      </div>
                    </div>
                    <div class="progress-list">
                      <div class="details">
                        <div class="title">硬盘利用率</div>
                      </div>
                      <div class="status pull-right bg-transparent-black-1">
                        <span class="animate-number" data-value="${sDiskUsage }" data-animation-duration="1500">0</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little progress-transparent-black">
                        <div class="progress-bar animate-progress-bar" data-percentage="${sDiskUsage }%"></div>
                      </div>
                    </div>

                  </div>
                  <div class="back" style="height:177px;">
                    <a href="<%=request.getContextPath() %>/cloudserver/all" style="padding-top:25px;">
                      <i class="fa fa-bar-chart-o fa-4x"></i>
                      <span>查看云服务器详情</span>
                    </a>  
                  </div>
                </div>
              </div>


              <div class="card-container col-lg-3 col-sm-6 col-sm-12">
                <div class="card card-blue hover">
                  <div class="front">        
                    
                    <div class="media">                  
                      <span class="pull-left">
                        <i class="fa fa-database media-object"></i>
                      </span>

                      <div class="media-body">
                        <big>云磁盘</big>
                        <h2 class="media-heading animate-number" data-value="0" data-animation-duration="1500">0</h2>
<%--                         <h2 class="media-heading animate-number" data-value="${totalDisk }" data-animation-duration="1500">0</h2> --%>
                      </div>
                    </div> 

                    <div class="progress-list">
                      <div class="details">
                        <div class="title">CPU利用率</div>
                      </div>
                      <div class="status pull-right bg-transparent-black-1">
                        <span class="animate-number" data-value="0" data-animation-duration="1500">0</span>%
<%--                         <span class="animate-number" data-value="${dCpuUsage }" data-animation-duration="1500">0</span>% --%>
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little progress-transparent-black">
                        <div class="progress-bar animate-progress-bar" data-percentage="0%"></div>
<%--                         <div class="progress-bar animate-progress-bar" data-percentage="${dCpuUsage }%"></div> --%>
                      </div>
                    </div>
                    <div class="progress-list">
                      <div class="details">
                        <div class="title">内存利用率</div>
                      </div>
                      <div class="status pull-right bg-transparent-black-1">
                        <span class="animate-number" data-value="0" data-animation-duration="1500">0</span>%
<%--                         <span class="animate-number" data-value="${dMemoryUsage }" data-animation-duration="1500">0</span>% --%>
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little progress-transparent-black">
<%--                         <div class="progress-bar animate-progress-bar" data-percentage="${dMemoryUsage }%"></div> --%>
                        <div class="progress-bar animate-progress-bar" data-percentage="0%"></div>
                      </div>
                    </div>
                    <div class="progress-list">
                      <div class="details">
                        <div class="title">硬盘利用率</div>
                      </div>
                      <div class="status pull-right bg-transparent-black-1">
                        <span class="animate-number" data-value="0" data-animation-duration="1500">0</span>%
<%--                         <span class="animate-number" data-value="${dDiskUsage }" data-animation-duration="1500">0</span>% --%>
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little progress-transparent-black">
                        <div class="progress-bar animate-progress-bar" data-percentage="0%"></div>
<%--                         <div class="progress-bar animate-progress-bar" data-percentage="${dDiskUsage }%"></div> --%>
                      </div>
                    </div>

                  </div>
                  <div class="back" style="height:177px;">
                    <a href="#" style="padding-top:25px;">
                      <i class="fa fa-bar-chart-o fa-4x"></i>
                      <span>查看云磁盘详情</span>
                    </a>
                  </div>
                </div>
              </div>



              <div class="card-container col-lg-3 col-sm-6 col-sm-12">
                <div class="card card-greensea hover">
                  <div class="front">        
                    
                    <div class="media">
                      <span class="pull-left">
                        <i class="fa fa-cloud media-object"></i>
                      </span>

                      <div class="media-body">
                        <big>专属云</big>
                        <h2 class="media-heading animate-number" data-value="0" data-animation-duration="1500">0</h2>
                      </div>
                    </div>

                    <div class="progress-list">
                      <div class="details">
                        <div class="title">CPU利用率</div>
                      </div>
                      <div class="status pull-right bg-transparent-black-1">
                        <span class="animate-number" data-value="0" data-animation-duration="1500">0</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little progress-transparent-black">
                        <div class="progress-bar animate-progress-bar" data-percentage="0%"></div>
                      </div>
                    </div>
                    <div class="progress-list">
                      <div class="details">
                        <div class="title">内存利用率</div>
                      </div>
                      <div class="status pull-right bg-transparent-black-1">
                        <span class="animate-number" data-value="0" data-animation-duration="1500">0</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little progress-transparent-black">
                        <div class="progress-bar animate-progress-bar" data-percentage="0%"></div>
                      </div>
                    </div>
                    <div class="progress-list">
                      <div class="details">
                        <div class="title">硬盘利用率</div>
                      </div>
                      <div class="status pull-right bg-transparent-black-1">
                        <span class="animate-number" data-value="0" data-animation-duration="1500">0</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little progress-transparent-black">
                        <div class="progress-bar animate-progress-bar" data-percentage="0%"></div>
                      </div>
                    </div>

                  </div>
                  <div class="back" style="height:177px;">
                    <a href="#" style="padding-top:25px;">
                      <i class="fa fa-bar-chart-o fa-4x"></i>
                      <span>查看专属云详情</span>
                    </a>
                  </div>
                </div>
              </div> 


              <div class="card-container col-lg-3 col-sm-6 col-xs-12">
                <div class="card card-slategray hover">
                  <div class="front"> 

                    <div class="media">                   
                      <span class="pull-left">
                        <i class="fa fa-desktop media-object"></i>
                      </span>

                      <div class="media-body">
                        <big>云桌面</big>
                        <h2 class="media-heading animate-number" data-value="${totalTop }" data-animation-duration="1500">0</h2>
                      </div>
                    </div> 

                    <div class="progress-list">
                      <div class="details">
                        <div class="title">CPU利用率</div>
                      </div>
                      <div class="status pull-right bg-transparent-black-1">
                        <span class="animate-number" data-value="${tCpuUsage }" data-animation-duration="1500">0</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little progress-transparent-black">
                        <div class="progress-bar animate-progress-bar" data-percentage="${tCpuUsage }%"></div>
                      </div>
                    </div>
                    <div class="progress-list">
                      <div class="details">
                        <div class="title">内存利用率</div>
                      </div>
                      <div class="status pull-right bg-transparent-black-1">
                        <span class="animate-number" data-value="${tMemoryUsage }" data-animation-duration="1500">0</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little progress-transparent-black">
                        <div class="progress-bar animate-progress-bar" data-percentage="${tMemoryUsage }%"></div>
                      </div>
                    </div>
                    <div class="progress-list">
                      <div class="details">
                        <div class="title">硬盘利用率</div>
                      </div>
                      <div class="status pull-right bg-transparent-black-1">
                        <span class="animate-number" data-value="${tDiskUsage }" data-animation-duration="1500">0</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little progress-transparent-black">
                        <div class="progress-bar animate-progress-bar" data-percentage="${tDiskUsage }%"></div>
                      </div>
                    </div>

                  </div>
                  <div class="back" style="height:177px;">
                    <a href="<%=request.getContextPath() %>/warehouse/all" style="padding-top:25px;">
                      <i class="fa fa-bar-chart-o fa-4x"></i>
                      <span>查看云桌面详情</span>
                    </a>
                  </div>
                </div>
              </div>


            </div>
            <!-- /cards -->
            


            <!-- row -->
            <div class="row">
              
              
              <!-- col 6 -->
              <div class="col-md-8">



                <!-- tile -->
                <section class="tile color transparent-black">



                  <!-- tile header -->
                  <div class="tile-header">
                    <h1>CPU监控</h1>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body text-center">
                    
                    
                     <div class="easypiechart inline" style="width: 140px;height: 150px;line-height: 140px;">
                      <div class="percentage" id="platformcpu" data-percent="0" data-size="140" data-line-cap="round" data-line-width="10" data-scale-color="false" data-bar-color="#16a085" data-track-color="rgba(0,0,0,.2)"><span>0</span>%</div>
                      <div class="label">全局CPU占用率</div>
                    </div>
                    
                    <div class="easypiechart inline" style="width: 120px;height: 150px;line-height: 120px;">
                      <div class="percentage" id="computecpu" data-percent="0" data-size="120" data-scale-color="false" data-bar-color="#FF9900" data-track-color="rgba(0,0,0,.2)"><span>0</span>%</div>
                      <div class="label">计算节点CPU占用率</div>
                    </div>
                      
                

                    <div class="easypiechart inline" style="width: 120px;height: 150px;line-height: 120px;">
                      <div class="percentage" id="storagecpu" data-percent="0" data-size="120" data-scale-color="false" data-bar-color="#cc3399" data-track-color="rgba(0,0,0,.2)"><span>0</span>%</div>
                      <div class="label">存储节点CPU占用率</div>
                    </div>

                   

                  </div>
                  <!-- /tile body -->

                </section>
                <!-- /tile -->
                
                <section class="tile color transparent-black">



                  <!-- tile header -->
                  <div class="tile-header">
                    <h1>内存监控</h1>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                   <!-- tile body -->
                  <div class="tile-body">
                    
                    <div class="tile-body text-center">
                      <div id="gauge01" class="inline-block" style="width:180px; height: 160px;"></div>
                      <div id="gauge02" class="inline-block" style="width:180px; height: 160px;"></div>
                      <div id="gauge03" class="inline-block" style="width:180px; height: 160px;"></div>
                    </div>

                  </div>
                  <!-- /tile body -->

                </section>


              </div>
              <!-- /col 6 -->
                           
              
              <div class="col-md-4">
              <!-- tile -->
                <section class="tile color transparent-black">



                  <!-- tile header -->
                  <div class="tile-header">
                    <h1>磁盘监控</h1>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile widget -->
                  <div class="tile-widget">

                    <div class="progress-list with-heading">
                      <div class="details">
                        <div class="title"><h2><i class="fa fa-hdd-o"></i> <span class="animate-number" data-value="${pDiskRemain }" data-animation-duration="1600">0</span> GB</h2></div>
                      </div>
                      <div class="status pull-right bg-transparent-black-1">
                        <span class="animate-number" data-value="${pDiskUsage }" data-animation-duration="1500">0</span>%
                      </div>
                      <div class="clearfix"></div>
                      <div class="progress progress-little progress-transparent-black" style="margin-bottom: 5px">
                        <div class="progress-bar animate-progress-bar" data-percentage="${pDiskUsage }%"></div>
                      </div>
                    </div>  
                    <p class="description"><strong>已使用${pDiskRemain }GB</strong> 共 <strong class="white-text">${pDisk }GB</strong></p>
                  </div>
                  <!-- /tile widget -->


                  <!-- tile body -->
                  <div class="tile-body paddingtop">
                    <div id="rickshaw-chart"></div>
                  </div>
                  <!-- /tile body -->
                  
                


                </section>
                <!-- /tile -->
                
                
                <!-- tile -->
                <section class="tile color transparent-black">



                  <!-- tile header -->
                  <div class="tile-header">
                    <h1>网络IO监控</h1>
                    <div class="controls">
                      <a href="#" class="refresh"><i class="fa fa-refresh"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">    
                    
                    <div id="realtime-chart" style="height: 195px;"></div>

                  </div>
                  <!-- /tile body -->
                  
                


                </section>
                <!-- /tile -->



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




    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.time.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.selection.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.animator.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.orderBars.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/easypiechart/jquery.easypiechart.min.js"></script>

    <script src="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/raphael-min.js"></script> 
    <script src="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/d3.v2.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/rickshaw.min.js"></script>

    <script src="<%=request.getContextPath()%>/assets/js/vendor/graphtable/jquery.graphTable-0.3.js"></script>
	<script src="<%=request.getContextPath()%>/assets/js/vendor/justgage/justgage.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.categories.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.pie.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.stack.min.js"></script>
    
    <script src="<%=request.getContextPath()%>/assets/js/vendor/morris/morris.min.js"></script>

    <script src="<%=request.getContextPath()%>/assets/js/vendor/tabdrop/bootstrap-tabdrop.min.js"></script>

    <script src="<%=request.getContextPath()%>/assets/js/vendor/summernote/summernote.min.js"></script>


    <script src="<%=request.getContextPath()%>/assets/js/vendor/chosen/chosen.jquery.min.js"></script>

    <script src="<%=request.getContextPath()%>/assets/js/minimal.min.js"></script>
    <script type="text/javascript">
    var path = "<%=request.getContextPath()%>";
    $(function(){
    	//-------获取平台资源数据-------
    	var platform_cpu = new String("0.00%");
    	var compute_cpu = new String("0.00");
    	var storage_cpu = new String("0.00");
    	var platform_memory = new String("0.00%");
    	var compute_memory = new String("0.00");
    	var storage_memory = new String("0.00");
    	var read_speed = new String("0KB");//磁盘读速度
    	var write_speed = new String("0KB");//磁盘写速度
    	var send_speed = new String("0KB");//网络发送速度
    	var receive_speed = new String("0KB");//网络接收速度
    	window.setInterval(function() {
    		jQuery.ajax({
    	        url: '<%=request.getContextPath()%>/paltform/getpr',
    	        type: 'post', 
    	        dataType: 'json',
    	        async:true,
    	        timeout: 10000,
    	        error: function()
    	        { 
    	        },
    	        success: function(result)	        
    	        {
    	        	platform_cpu = result.cpuUsage;
    	        	platform_memory = result.memoryUsage;
    	        	read_speed = result.speed[0];
    	        	write_speed = result.speed[1];
    	        	receive_speed = result.speed[2];
    	        	send_speed = result.speed[3];
    	        }
    		});
    	}, 2000);
    	window.setInterval(function() {
    		jQuery.ajax({
    	        url: '<%=request.getContextPath()%>/paltform/getcr',
    	        type: 'post', 
    	        dataType: 'json',
    	        async:true,
    	        timeout: 10000,
    	        error: function()
    	        { 
    	        },
    	        success: function(result)	        
    	        {
    	        	compute_cpu = result.cpuUsage;
    	        	compute_memory = result.memoryUsage;
    	        }
    		});
    		jQuery.ajax({
    	        url: '<%=request.getContextPath()%>/paltform/getsr',
    	        type: 'post', 
    	        dataType: 'json',
    	        async:true,
    	        timeout: 10000,
    	        error: function()
    	        { 
    	        },
    	        success: function(result)	        
    	        {
    	        	storage_cpu = result.cpuUsage;
    	        	storage_memory = result.memoryUsage;
    	        }
    		});
        }, 5000);
    	//--------------------------
    	// Initialize card flip 鼠标移上出现翻转
        $('.card.hover').hover(function(){
          $(this).addClass('flip');
        },function(){
          $(this).removeClass('flip');
        });
    	
        // 网络IO图表
        var realTimeData = [];
        var totalPoints = 30;
        var updateInterval = 3000;
		var maxY = 0;
        function getData() {
          realTimeData.shift();

          while (realTimeData.length<totalPoints) {     
            var y = parseFloat(receive_speed.substring(0, receive_speed.length-2));
            if(y>maxY){
            	maxY = y;
            }
            var temp = [];
            realTimeData.push(y);
          }

          var temp = [];
          for (var i = 0; i<realTimeData.length; ++i) {
            temp.push([i, realTimeData[i]])
          }
          return temp;

        }

        var plot = $.plot('#realtime-chart', [getData()], 
        {
          colors: ['#3399cc'],
          series: {
            lines: { 
              show: true,
              fill: 0.1 
            },
            shadowSize: 0
          },
          yaxis: { 
            tickColor: 'rgba(255,255,255,.2)',
            min: 0,
            max: maxY,
            font :{
              color: 'rgba(255,255,255,.8)'
            }
          },  
          grid: { 
            borderWidth: {
              top: 0,
              right: 0,
              bottom: 1,
              left: 1
            },
            color :  'rgba(255,255,255,.2)' 
          },
          tooltip: false,
          xaxis: { 
            show: false
          }
        });

        function update() {
          plot.setData([getData()]);
          plot.draw();
          setTimeout(update, updateInterval);
        };

        update(); 

        //存储占用图表
        var graph;
        var time_base = Math.floor((new Date).getTime() / 1e3);
        var seriesData = [[],[]];
        var random = new Rickshaw.Fixtures.RandomData(6);
        for (var i = 0; i < 6; i++) {
        	var readObj = {};
        	var writeObj = {};
        	readObj.x = time_base * 6 * i;
        	readObj.y = 0;
        	readObj.y0 = 0;
        	writeObj.x = time_base * 6 * i;
        	writeObj.y = 0;
        	writeObj.y0 = readObj.y;
        	seriesData[0].push(readObj);
        	seriesData[1].push(writeObj);
        }

        graph = new Rickshaw.Graph( {
          element: document.querySelector("#rickshaw-chart"),
          height: 112,
          renderer: 'area',
          series: [
            {
              data: seriesData[0],
              color: '#6e6e6e',
              name:'磁盘读取操作(KB)'
            },{
              data: seriesData[1],
              color: '#fff',
              name:'磁盘写入操作(KB)'
            }
          ]
        } );

        var hoverDetail = new Rickshaw.Graph.HoverDetail( {
          graph: graph,
        });

        setInterval( function() {
          var x_v = Math.floor((new Date).getTime() / 1e3) * 6 * 6;
          var y_v = parseFloat(read_speed.substring(0, read_speed.length-2));
          var y1_v = parseFloat(write_speed.substring(0, write_speed.length-2));
          random.removeData(seriesData);
          seriesData[0][5] = {x:x_v,y:y_v,y0:0};
          seriesData[1][5] = {x:x_v,y:y1_v,y0:y_v};
          graph.update();

        },2000);


        // CPU占用图表
        var chartAll = $('#platformcpu');
        var chartCompute = $('#computecpu');
        var chartStorage = $('#storagecpu');
        chartAll.easyPieChart({
          animate: 2000,
          onStart: function(value) {
            $(this.el).find('span').animateNumbers(parseFloat(platform_cpu.substring(0, platform_cpu.length-1)));
          }
        });
        chartCompute.easyPieChart({
            animate: 2000,
            onStart: function(value) {
              $(this.el).find('span').animateNumbers(parseFloat(compute_cpu));
            }
          });
        chartStorage.easyPieChart({
            animate: 2000,
            onStart: function(value) {
              $(this.el).find('span').animateNumbers(parseFloat(storage_cpu));
            }
          });
        //update instance every 5 sec
        window.setInterval(function() {

          // refresh easy pie chart
       	  chartAll.data('easyPieChart').update(parseFloat(platform_cpu.substring(0, platform_cpu.length-1)));
       	  chartCompute.data('easyPieChart').update(parseFloat(compute_cpu));
       	  chartStorage.data('easyPieChart').update(parseFloat(storage_cpu));

        }, 5000);

        // 内存占用图表

        var g01 = new JustGage({
          id: "gauge01", 
          value: parseFloat(platform_memory.substring(0, platform_memory.length-1)), 
          min: 0,
          max: 100,
          title: "全局内存占用率(%)",
          titleFontColor : "rgba(255,255,255,.6)",
          valueFontColor:  "rgba(255,255,255,.8)"
        }); 

        var g02 = new JustGage({
          id: "gauge02",
          value : parseFloat(compute_memory),
          title : "计算节点内存占用率(%)",
          min: 0,
          max: 100,
          gaugeWidthScale: .4,
          titleFontColor : "rgba(255,255,255,.6)",
          valueFontColor:  "rgba(255,255,255,.8)"
        }); 

        var g03 = new JustGage({
          id: "gauge03",
          value : parseFloat(storage_memory),
          title : "存储节点内存占用率(%)",
          min: 0,
          max: 100,
          gaugeWidthScale: .4,
          titleFontColor : "rgba(255,255,255,.6)",
          valueFontColor:  "rgba(255,255,255,.8)"
        });

   

        //update instance every 5 sec
        window.setInterval(function() {

          // refresh justGage charts
          g01.refresh(parseFloat(platform_memory.substring(0, platform_memory.length-1)));
          g02.refresh(parseFloat(compute_memory));
          g03.refresh(parseFloat(storage_memory));

        }, 5000);
        
      })
    </script>
    
  </body>
</html>
      

