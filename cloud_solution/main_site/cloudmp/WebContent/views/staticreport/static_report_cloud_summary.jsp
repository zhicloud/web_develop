<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<!-- static_report_cloud_summary.jsp -->
<html>
  <head>
    <title>云主机概览</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8" />
  </head>
<script type="text/javascript">
var tempcloudid = "${cloudid}";
var temptabstr1 = null;
var temptabstr2 = null;
var singlebar = null;
var multybar = null;
var customqueryflag1 = false;
var customqueryflag2 = false;
var cloudname = "cloudhost";
//更新数据
function updateData(obj){
	cloudname = obj.name;
	$("#host_name").html(obj.name);
	$("#cpu_count").html(obj.cpu_count);
	$("#host_memory").html(obj.memorydata);
	$("#host_disk").html(obj.diskdata);
	var str = "";
	var color = "";
	if(obj.status=="normal"){
		str = "正常";
		color = "green";
	}else if(obj.status=="warning"){
		str = "告警";
		color = "yellow";
	}else if(obj.status=="error"){
		str = "故障";
		color = "red";
	}else if(obj.status=="stop"){
		str = "屏蔽";
		color = "gray";		
	}
	$("#host_status").attr("color",color);
	$("#host_status").html(str);
}
function createtablehtml(){
	var obj = singlebar
	var temphtml = "<table id=\"bar-chart\" class=\"flot-chart\" data-type=\"bars\" data-bar-width=\"0.4\" data-tool-tip=\"show\" data-width=\"50%\" data-height=\"250px\" data-font-color=\"rgba(255,255,255,.8)\" data-legend=\"hidden\" data-tick-color=\"rgba(255,255,255,.3)\">";
	temphtml += "<thead><tr><th></th><th style=\"color : #fff;\">在线时长</th></tr></thead>";
	temphtml += "<tbody>";
	for(var i=0;i<obj.length;i++){
		temphtml += "<tr>";
		temphtml +="<th>"+obj[i].name+"</th>";
		temphtml +="<td>"+obj[i].value+"</td>";
		temphtml +="</tr>";
	}
	temphtml +="</tbody></table>";
	$("#singlebardiv").html(temphtml);
	initializeFlot();
}
//从后台获取数据
function getData(obj,startdate,enddate,type){
	$.ajax({
        type:"POST",
        url:"<%=request.getContextPath()%>/staticreport/gethostdata",
        data:{id:obj,startdate:startdate,enddate:enddate,type:type},
        datatype: "json",
        success:function(data){
        	var re = eval(data);
       		if(re.status == "success"){
       			if(type=="all"){
           			//updateData(re.data);
           			singlebar = re.singlebar;
           			multybar = re.multybar;
           			createtablehtml();
           			initMorris();
       			}else if(type=="cpu"){
       				singlebar = re.singlebar;
       				createtablehtml();
       			}else if(type=="multy"){
       				multybar = re.multybar;
       				initMorris();
       			}
       			
       		}else{
       			$("#tipscontent").html(re.message);
         		$("#dia").click();
       		}       
        },
        complete: function(XMLHttpRequest, textStatus){
        },
        error: function(){
        }         
     });
		
}
//柱形图初始化
function initializeFlot(){
	var bars = false;
	var el = $('table.flot-chart');

	  el.each(function(){
	    var data = $(this).data();
	    var colors = [];
	    var gridColor = data.tickColor || 'rgba(0,0,0,.1)';

	    $(this).find('thead th:not(:first)').each(function() {
	      colors.push($(this).css('color'));
	    });

	    if(data.type){
	      bars = data.type.indexOf('bars') != -1;
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
	        bars: {
	          show: bars,
	          barWidth: data.barWidth || 0.5,
	          fill: data.fill || 1,
	          align: 'center'
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
	          size: 12
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
	      tooltipOpts: { content: ('<b>%s</b> :  %y') }
	    });
	  });
}
//复杂柱形图初始化
function initMorris(){
	$("#multy_bar").html("");
	var data = multybar;
	//barColors:['#ff4a43','#1693A5','white','black']
    Morris.Bar({
        element: 'multy_bar',
        data:data,
        xkey: 'y',
        ykeys: ['read', 'write','send','receive'],
        labels: ['读', '写','发送','接收'],
        barColors:['blue','green','red','black']
      });
}
//小标签切换
function changeTab(obj,i){
	var tabstr = $(obj).html();
	if(i=="1"&&temptabstr1==tabstr){
		return;
	}
	if(i=="2"&&temptabstr2==tabstr){
		return;
	}
	if(tabstr=="最近5天"){
		$("#fiveday"+i).attr("class","btn btn-success");
		$("#custom"+i).attr("class","btn btn-default");
		$("#betweendate"+i).css("display","none");
		$("#exportdiv"+i).css("margin-left","350px")
		if(i=="1"){
			//自定义点击了查询按钮以后才重新更新数据
			if(customqueryflag1){
				getData(tempcloudid,null,null,"cpu");
			}
		} else if(i=="2"){
			//自定义点击了查询按钮以后才重新更新数据
			if(customqueryflag2){
				getData(tempcloudid,null,null,"multy");
			}
		}

	}
	if(tabstr=="自定义"){
		$("#fiveday"+i).attr("class","btn btn-default");
		$("#custom"+i).attr("class","btn btn-success");
		$("#betweendate"+i).css("display","block");
		$("#exportdiv"+i).css("margin-left","5px")
	}
	if(i=="1"){
		temptabstr1 = tabstr;
	}
	if(i=="2"){
		temptabstr2 = tabstr;
	}
	
}
//初始化日期控件
function initDateTimePicker(id){
	$('#'+id).datetimepicker({
        icons: {
          time: "fa fa-clock-o",
          date: "fa fa-calendar",
          up: "fa fa-arrow-up",
          down: "fa fa-arrow-down"
        },
        language:'zh-cn'
      });
/* 	$('.bootstrap-datetimepicker-widget:eq(1)').css("margin-top","360px");
	$('.bootstrap-datetimepicker-widget:eq(2)').css("margin-top","430px");
	$('.bootstrap-datetimepicker-widget:eq(3)').css("margin-top","430px"); */
      $('#'+id).on("dp.show",function (e) {
    	  if(id=="startdate1"){
    	        var newtop = $('.bootstrap-datetimepicker-widget:eq(0)').position().top - 45;      
    	        $('.bootstrap-datetimepicker-widget:eq(0)').css('top', newtop + 'px');
    	  }else if(id=="enddate1"){
	  	        var newtop = $('.bootstrap-datetimepicker-widget:eq(1)').position().top - 45;      
		        $('.bootstrap-datetimepicker-widget:eq(1)').css('top', newtop + 'px');    		  
    	  }else if(id=="startdate2"){
	  	        var newtop = $('.bootstrap-datetimepicker-widget:eq(2)').position().top - 45;      
		        $('.bootstrap-datetimepicker-widget:eq(2)').css('top', newtop + 'px');    		  
    	  }
    	  else if(id=="enddate2"){
	  	        var newtop = $('.bootstrap-datetimepicker-widget:eq(3)').position().top - 45;      
		        $('.bootstrap-datetimepicker-widget:eq(3)').css('top', newtop + 'px');    		  
  	  }

      });
}
//自定义查询
function customQuery1(){
	var startdate = $("#startdate1").val();
	if(startdate==undefined||startdate==null||startdate==""){
			$("#tipscontent").html("开始日期不能为空");
     		$("#dia").click();
     		return;
	}
	var enddate = $("#enddate1").val();
	if(enddate==undefined||enddate==null||enddate==""){
		$("#tipscontent").html("截止日期不能为空");
 		$("#dia").click();
 		return;
	}
	getData(tempcloudid,startdate,enddate,'cpu');
	customqueryflag1 = true;
}
function customQuery2(){
	var startdate = $("#startdate2").val();
	if(startdate==undefined||startdate==null||startdate==""){
			$("#tipscontent").html("开始日期不能为空");
     		$("#dia").click();
     		return;
	}
	var enddate = $("#enddate2").val();
	if(enddate==undefined||enddate==null||enddate==""){
		$("#tipscontent").html("截止日期不能为空");
 		$("#dia").click();
 		return;
	}
	getData(tempcloudid,startdate,enddate,'multy');
	customqueryflag2 = true;
}
//导出统计报表数据
function exportStaticData(obj){
	window.location.href = "<%=request.getContextPath()%>/staticreport/exportdata/"+cloudname+"/"+obj;
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





        <%@include file="/views/common/common_menus.jsp" %>


        <!-- Page content -->
        <div id="content" class="col-md-12">



          <!-- page header -->
          <div class="pageheader">


            <h2><i class="fa fa-area-chart"></i> 云主机概览</h2>


          </div>
          <!-- /page header -->


          <!-- content main container -->
          <div class="main">

            <!-- row -->
            <div class="row">

              <!-- col 6 -->
          <div class="col-md-12">

				  <section class="tile color transparent-black">
                      <div class="tile-widget color transparent-black rounded-top-corners nopadding nobg">
                      <!-- Nav tabs -->
                      <ul class="nav nav-tabs tabdrop">
                          <li class="active" style="width:10%;text-align: center;"><a href="#" data-toggle="tab">云主机</a></li>
                          <li class="" style="width:90%;"><a href="#" data-toggle="tab" onclick="gotoPage('/staticreport/serverindex');">宿主机</a></li>
                      </ul>
                      <!-- / Nav tabs -->
                    </div>

                  <div class="tile-body no-vpadding" style="margin-top:-10px;">
                    <div class="table-responsive">
                    <hr>
                    <div class="row">
                      <div class="col-md-8 text-left">
						<div style="float: left;" >云主机:<font id="host_name"></font> 概览</div><br>
                        <div style="height:20px;"></div>                      
                        <div style="width:70px;float: left;">状态:<font color="green" id="host_status">正常</font></div>
                        <div style="width:90px;float: left;" >CPU核数:<font id="cpu_count">10</font></div>
                        <div style="width:80px;float: left;" >内存:<font id="host_memory">80G</font></div>
                        <div style="width:220px;float: left;">磁盘总空间/已用空间:<font id="host_disk">100G/30G</font></div>
                        <div style="width:60px;float: left;">
                        	<span class="label label-default" style="cursor:pointer;" onclick="backhome()">返回上级</span>
                        </div>
                        <div style="width:60px;float: left;margin-left:2px;">
                        	<span class="label label-success" style="cursor:pointer;" onclick="viewcloudmore('1')">更多</span>
                        </div>
                      </div>
                        <div class="col-sm-4" id="cloudsearch">
							<select class="chosen-select chosen-transparent form-control" id="cloudid" parsley-trigger="change" parsley-required="true" parsley-error-container="#cloudsearch" onchange="querycloud(this)">
	                            <option value="">云主机切换</option>  
	                            <c:forEach items="${cloudlists }" var="cloud">
	                            	<option value="${cloud.id }">${cloud.name }</option>
	                            </c:forEach>
	                          </select>  
	                     </div>
                    </div>
                    <div class="row" style="padding-left:15px;padding-top:10px;">
                          <div class="btn-group btn-group-sm" style="float:left;" id="tabdiv1">
                            <button type="button" class="btn btn-success" id="fiveday1" onclick="changeTab(this,'1')">最近5天</button>
                            <button type="button" class="btn btn-default" id="custom1" onclick="changeTab(this,'1')">自定义</button>
                          </div>
                          <div style="float:left;display:none;width:40%;" id="betweendate1">
	                        <div class="col-sm-4" style="padding-left:5px;">
	                          <input type="text" class="form-control" id="startdate1" style="min-height: 30px;" placeholder="开始日期" data-date-format="YYYY-MM-DD">
	                        </div>
	                        <div class="col-sm-4" style="padding-left:5px;padding-right:5px;">
	                          <input type="text" class="form-control" id="enddate1" style="min-height: 30px;" placeholder="截止日期" data-date-format="YYYY-MM-DD">
	                        </div>
	                        <button type="button" class="btn btn-blue btn-sm" onclick="customQuery1()">查询</button>
                          </div>
                          <div style="width: 200px;float:left;margin-left:350px;" id="exportdiv1">
                          <button type="button" class="btn btn-green btn-sm" onclick="exportStaticData('cscpu')">导出Excel表</button>
                          </div>
                    </div>
                    <div class="row">
	                    <div class="tile-body" id="singlebardiv">
	
	                  </div>
                    </div>
                    <div class="row" style="padding-left:15px;padding-top:10px;">
                          <div class="btn-group btn-group-sm" style="float:left;" id="tabdiv2">
                            <button type="button" class="btn btn-success" id="fiveday2" onclick="changeTab(this,'2')">最近5天</button>
                            <button type="button" class="btn btn-default" id="custom2" onclick="changeTab(this,'2')">自定义</button>
                          </div>
                          <div style="float:left;display:none;width:40%;" id="betweendate2">
	                        <div class="col-sm-4" style="float:left;padding-left:5px;">
	                          <input type="text" class="form-control" id="startdate2" style="min-height: 30px;" placeholder="开始日期" data-date-format="YYYY-MM-DD">
	                        </div>
	                        <div class="col-sm-4" style="float:left;padding-left:5px;padding-right:5px;">
	                          <input type="text" class="form-control" id="enddate2" style="min-height: 30px;" placeholder="截止日期" data-date-format="YYYY-MM-DD">
	                        </div>
	                        <button type="button" class="btn btn-blue btn-sm" onclick="customQuery2()">查询</button>
                          </div>
                          <div style="width: 200px;float:left;margin-left:350px;" id="exportdiv2">
                          <button type="button" class="btn btn-green btn-sm" onclick="exportStaticData('cswrite')">导出Excel表</button>
                          </div>
                    </div>                    
					<div class="row">
	                   <div id="multy_bar" style="height: 250px;width:50%;"></div>
                    </div>
                                        
                    </div>

                  </div>
                  
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
	<script src="<%=request.getContextPath()%>/assets/js/minimal.min.js"></script>
    <script src="<%=request.getContextPath()%>/assets/js/vendor/momentjs/moment-with-langs.min.js"></script> 
    <script src="<%=request.getContextPath()%>/assets/js/vendor/datepicker/bootstrap-datetimepicker.min.js"></script>
    

    

    <script>
     $(function(){
    	 $(".chosen-select").chosen({disable_search_threshold: 10});
    	 //初始化
    	 getData("${cloudid}",null,null,"all");
    	 initDateTimePicker("startdate1");
    	 initDateTimePicker("enddate1");
    	 initDateTimePicker("startdate2");
    	 initDateTimePicker("enddate2");
       })
    function backhome(){
    	window.location.href = "<%=request.getContextPath()%>/staticreport/cloudindex";
    } 
    function viewcloudmore(){
    	window.location.href = "<%=request.getContextPath()%>/staticreport/cloudhost/detail/"+tempcloudid;
    }
    //云主机切换
	function querycloud(obj){
    	if(tempcloudid==$(obj).val()) return;
		getData($(obj).val());
		tempcloudid = $(obj).val();
	}
    //页面跳转
    function gotoPage(url) {
        window.location.href = "<%=request.getContextPath()%>"+url;
    }

    </script>
    
  </body>
</html>
      
