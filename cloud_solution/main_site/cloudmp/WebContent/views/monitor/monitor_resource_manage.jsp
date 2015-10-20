<%@ page pageEncoding="utf-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title>资源监控——资源池信息监控 </title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
  </head>
<%@include file="/views/common/common_menus.jsp" %>
<style>
 body #content .tile .tile-body{
 	padding-top:0px;
 	padding-bottom:0px;
 }
 .custom-labels{
 	margin-top:5px;
 }
 .col-md-3{
 margin:5px;
 }
</style>
<script type="text/javascript">
//查看机房页面跳转
function hostquery(id){
	var obj = new Object();
	obj["areaid"] = "";
	obj["roomid"] = "";
	obj["rackid"] = "";
	obj["serverid"] = "";
	obj["type"] = "";
	obj["poolid"] = id;
	window.location.href = "<%=request.getContextPath()%>/monitor/resourcetohost?data="+JSON.stringify(obj);
}
</script>
  <body class="bg-1">

 

    <!-- Preloader -->
    <div class="mask"><div id="loader"></div></div>
    <!--/Preloader -->

    <!-- Wrap all page content here -->
    <div id="wrap">
    <!-- Make page fluid -->
    <div class="row">
        <!-- Page content -->
        <div id="content" class="col-md-12">
          <!-- page header -->
          <div class="pageheader">
            <h2><i class="fa fa-signal"></i> 资源池信息监控</h2>
          </div>
          <!-- /page header -->
          <!-- content main container -->
          <div class="main">
            <!-- row -->
            <div class="row">
              <!-- col 6 -->
          <c:forEach items="${resourcedata }" var="resource">
          
          <div class="col-md-3">
			<section class="tile color transparent-white">
                  <!-- tile header -->
                  <div class="tile-header">
                    <%-- <h1>${resource.areaname}</h1> --%>
                    <span class="note">${resource.resourcename }</span>
                    <div class="controls">
                      <a href="#" class="list" onclick="hostquery('${resource.resourceid}')"><i class="fa fa-list"></i></a>
                      <a href="#" class="remove"><i class="fa fa-times"></i></a>
                    </div>
                  </div>
                  <!-- /tile header -->

                  <!-- tile body -->
                  <div class="tile-body">
                    
                    <table id="pie-chart02" class="flot-chart" data-type="pie" data-inner-radius="0.8" data-pie-label="hidden" data-width="100%" data-height="200px" data-tool-tip="show">
                      <thead>
                        <tr>
                          <th></th>
                          <th style="color : green;">正常</th>
                          <th style="color : yellow;">告警</th>                                       
                          <th style="color : red;">故障</th>
                          <th style="color : gray;">屏蔽</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <th></th>
                          <td>${resource.normal }</td>
                          <td>${resource.warning }</td>
                          <td>${resource.error }</td>
                          <td>${resource.stop }</td>
                        </tr>
                      </tbody>
                    </table>

                    <div class="custom-labels" data-target-flot="#pie-chart02"></div>

                  </div>
                  <!-- /tile body -->
                </section>          
          
              </div>
            </c:forEach>    
 
               
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


    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/mmenu/js/jquery.mmenu.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/sparkline/jquery.sparkline.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/nicescroll/jquery.nicescroll.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/animate-numbers/jquery.animateNumbers.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/videobackground/jquery.videobackground.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/assets/js/vendor/blockui/jquery.blockUI.js"></script>

    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.categories.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.pie.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.stack.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/graphtable/jquery.graphTable-0.3.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/flot/jquery.flot.tooltip.min.js"></script>

    <script src="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/raphael-min.js"></script> 
    <script src="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/d3.v2.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/rickshaw/rickshaw.min.js"></script>

    <script src="<%=request.getContextPath()%>/assets/js/vendor/morris/morris.min.js"></script>

    <script src="<%=request.getContextPath()%>/assets/js/vendor/easypiechart/jquery.easypiechart.min.js"></script>

    <script src="<%=request.getContextPath()%>/assets/js/vendor/justgage/justgage.js"></script>


    <script>
    $(function(){

        // Line Chart

        var bars = false;
        var lines = true;
        var pie = false;

        var initializeFlot = function(){
          
          var el = $('table.flot-chart');

          el.each(function(){
            var data = $(this).data();
            var colors = [];
            var gridColor = data.tickColor || 'rgba(0,0,0,.1)';

            $(this).find('thead th:not(:first)').each(function() {
              colors.push($(this).css('color'));
            });

            if(data.type){
              pie = data.type.indexOf('pie') != -1;
            }

            $(this).graphTable({
              series: 'columns',
              position: 'replace',
              colors: colors,
              width: data.width,
              height: data.height
            },
            {
              series: { 
                stack: data.stack,
                pie: {
                  show: pie,
                  innerRadius: data.innerRadius || 0,
                  label:{ 
                    show: data.pieLabel=='show' ? true:false
                  }
                },
                shadowSize: 0,
                points: {
                  radius: 4
                }
              },
              xaxis: {
                mode: 'categories',
                tickLength: 0,
                font :{
                  lineHeight: 24,
                  weight: '300',
                  color: data.fontColor,
                  size: 14
                } 
              },
              yaxis: { 
                tickColor: gridColor,
                tickFormatter: function number(x) {  var num; if (x >= 1000) { num=(x/1000)+'k'; }else{ num=x; } return num; },
                max: data.yMax,
                font :{
                  lineHeight: 13,
                  weight: '300',
                  color: data.fontColor
                }
              },  
              grid: { 
                borderWidth: {
                  top: 0,
                  right: 0,
                  bottom: 1,
                  left: 1
                },
                borderColor: gridColor,
                margin: 13,
                minBorderMargin:0,              
                labelMargin:20,
                hoverable: true,
                clickable: true,
                mouseActiveRadius:6
              },
              legend: { show: data.legend=='show' ? true:false },
              tooltip: data.toolTip=='show' ? true:false,
              tooltipOpts: { content: (pie ? '%p.0%, %s':'<b>%s</b> :  %y') }
            });
          });
        };   

        // Pie Chart custom labels

        $('.custom-labels').each(function () {
          var el= $(this);
          var data = el.data();
          var colors = [];
          var info = [];
          var item = '';

          $(data.targetFlot).find('thead th:not(:first)').each(function() {
            colors.push($(this).css('color'));
            info.push($(this).text());
          });

          for(var i=0;i<info.length;i++){
            item += '<li><span class="badge badge-outline" style="border-color:' + colors[i] + '"></span>' + info[i] + ' '  + '</li>';
          }

          el.append('<ul class="nolisttypes chart-legend">'+item+'</ul>');
          
        }); 

        initializeFlot();

        //update instance every 5 sec
        window.setInterval(function() {

/*           // refresh justGage charts
          g01.refresh(getRandomInt(0, 100));
          g02.refresh(getRandomInt(0, 100));
          g03.refresh(getRandomInt(0, 100));
          g04.refresh(getRandomInt(0, 100)); */

        }, 5000);
        
      })
   
    </script>
    
  </body>
</html>
      
