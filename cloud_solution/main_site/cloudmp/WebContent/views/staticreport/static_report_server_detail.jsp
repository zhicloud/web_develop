<%@ page pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
  <head>
    <title>服务器明细</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/datepicker/WdatePicker.js"></script>
    <style type="text/css">
    .round_memory{width:16px;height:16px;display: block;font-size:20px;line-heigth:16px;text-align:center;color:#16a085;text-decoration:none}
    .round_disk{width:16px;height:16px;display:block;font-size:20px;line-heigth:16px;text-align:center;color:#FF0066;text-decoration:none}
    </style>
  </head>
<script type="text/javascript">
var tempcloudid = "${serverid}";
var temptabstr = "服务状态";
var temptabstr2 = "最近5天";
var server_status = null;
var cpu_online_time = null;
var memory_disk = null;
var read_write = null;
var receive_data = null;
var sent_data = null;
var customqueryflag = false;
var cloudname = "server";
//返回概览 
function backhome(obj){
	window.location.href = "<%=request.getContextPath()%>/staticreport/server/"+obj;
}
//云主机切换
function queryserver(obj){
	if(tempcloudid==$(obj).val()) return;
	$("#tabgroup2").find("button:first").click();
	getData($(obj).val(),null,null);
	tempcloudid = $(obj).val();
}
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
//初始化表格效果
function initTable(){
    var oTable01 = $('#basicDataTable').dataTable({
        "sDom":
          "R<'row'<'col-md-6'l><'col-md-6'f>r>"+
          "t"+
          "<'row'<'col-md-4 sm-center'i><'col-md-4'><'col-md-4 text-right sm-center'p>>",
        "aaSorting": [],
        "aoColumnDefs": [
                         { 'bSortable': false, 'aTargets': [ "no-sort" ] }
                       ], 
        "fnInitComplete": function(oSettings, json) { 
        	$('.dataTables_filter').hide();
            $('.dataTables_filter').css("text-align","right");
            $('.dataTables_filter').css("margin-top","-40px");
            $('.dataTables_filter').css("margin-bottom","0px");   
            $('.dataTables_filter input').attr("placeholder", "Search");
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
//曲线图初始化
function initLineArea(){
    Morris.Area({
        element: 'memoryanddisk',
        data: memory_disk,
        xkey: 'y',
        ykeys: ['memory_use', 'disk_use'],
        labels: ['内存使用率', '磁盘使用率'],
        lineColors:['#16a085','#FF0066'],
        lineWidth:'0',
        grid: false,
        fillOpacity:'0.3'
      });
}
//生成服务状态表格
function createServerStatus(){
	var obj = server_status;
	$("#detailcontent").html("");
	var temphtml = "<table  class=\"table table-datatable\" id=\"basicDataTable\">";
	temphtml += "<thead><tr><th>时间</th><th class=\"no-sort\">状态</th></tr></thead>";
	temphtml += "<tbody>";
	for(var i=0;i<obj.length;i++){
		temphtml += "<tr class=\"odd gradex\">";
		temphtml +="<td>"+obj[i].name+"</td>";
		temphtml +="<td>"+obj[i].status+"</td>";
		temphtml +="</tr>";
	}
	temphtml +="</tbody></table>";
	$("#detailcontent").html(temphtml);
	initTable();
}
//CPU在线时长
function cpuOnlineTime(){
	var obj = cpu_online_time;
	$("#detailcontent").html("");
	var temphtml = "<table id=\"bar-chart\" class=\"flot-chart\" data-type=\"bars\" data-bar-width=\"0.4\" data-tool-tip=\"show\" data-width=\"100%\" data-height=\"250px\" data-font-color=\"rgba(255,255,255,.8)\" data-legend=\"hidden\" data-tick-color=\"rgba(255,255,255,.3)\">";
	temphtml += "<thead><tr><th></th><th style=\"color : #fff;\">在线时长</th></tr></thead>";
	temphtml += "<tbody>";
	for(var i=0;i<obj.length;i++){
		temphtml += "<tr>";
		temphtml +="<th>"+obj[i].name+"</th>";
		temphtml +="<td>"+obj[i].value+"</td>";
		temphtml +="</tr>";
	}
	temphtml +="</tbody></table>";
	$("#detailcontent").html(temphtml);
	initializeFlot();
}
//内存磁盘数据
function memoryAndDisk(){
	var temphtml = "<div><div style=\"float: left;\">";
		temphtml += "<span class=\"label label-default\" style=\"background-color: #16a085;opacity:0.3;\">内存使用率</span>";
		temphtml += "<span class=\"label label-default\" style=\"background-color: #FF0066;opacity:0.3;\">磁盘使用率</span>";
		temphtml += "</div><div style=\"float: left;\"></div></div>";
		temphtml += "<div id=\"memoryanddisk\" style=\"height: 250px;\"></div>";
		$("#detailcontent").html("");
		$("#detailcontent").html(temphtml);
		initLineArea();
}
//生成读写数据表格
function createReadWriteData(){
	var obj = read_write;
	$("#detailcontent").html("");
	var temphtml = "<table  class=\"table table-datatable\" id=\"basicDataTable\">";
	temphtml += "<thead><tr><th>时间</th><th class=\"no-sort\">读的数据(M)</th>";
	temphtml += "<th class=\"no-sort\">写的数据(M)</th><th class=\"no-sort\">读的次数</th>";
	temphtml += "<th class=\"no-sort\">写的次数</th><th class=\"no-sort\">读的速度</th>";
	temphtml += "<th class=\"no-sort\">写的速度</th></tr></thead>";
	temphtml += "<tbody>";
	for(var i=0;i<obj.length;i++){
		temphtml += "<tr class=\"odd gradex\">";
		temphtml +="<td>"+obj[i].timestamp+"</td>";
		temphtml +="<td>"+obj[i].read_byte+"</td>";
		temphtml +="<td>"+obj[i].write_byte+"</td>";
		temphtml +="<td>"+obj[i].read_request+"</td>";
		temphtml +="<td>"+obj[i].write_request+"</td>";
		temphtml +="<td>"+obj[i].read_speed+"</td>";
		temphtml +="<td>"+obj[i].write_speed+"</td>";
		temphtml +="</tr>";
	}
	temphtml +="</tbody></table>";
	$("#detailcontent").html(temphtml);
	initTable();
}
//生成接收数据表格
function createReceiveData(){
	var obj = receive_data;
	$("#detailcontent").html("");
	var temphtml = "<table  class=\"table table-datatable\" id=\"basicDataTable\">";
	temphtml += "<thead><tr><th>时间</th><th class=\"no-sort\">接收数据(M)</th>";
	temphtml += "<th class=\"no-sort\">接收数据包</th><th class=\"no-sort\">出错数</th>";
	temphtml += "<th class=\"no-sort\">丢包数</th><th class=\"no-sort\">接收速度</th>";
	temphtml += "</tr></thead>";
	temphtml += "<tbody>";
	for(var i=0;i<obj.length;i++){
		temphtml += "<tr class=\"odd gradex\">";
		temphtml +="<td>"+obj[i].timestamp+"</td>";
		temphtml +="<td>"+obj[i].received_bytes+"</td>";
		temphtml +="<td>"+obj[i].received_packets+"</td>";
		temphtml +="<td>"+obj[i].received_errors+"</td>";
		temphtml +="<td>"+obj[i].received_drop+"</td>";
		temphtml +="<td>"+obj[i].received_speed+"</td>";
		temphtml +="</tr>";
	}
	temphtml +="</tbody></table>";
	$("#detailcontent").html(temphtml);
	initTable();
}
//生成发送数据表格
function createSentData(){
	var obj = sent_data;
	$("#detailcontent").html("");
	var temphtml = "<table  class=\"table table-datatable\" id=\"basicDataTable\">";
	temphtml += "<thead><tr><th>时间</th><th class=\"no-sort\">发送数据(M)</th>";
	temphtml += "<th class=\"no-sort\">发送数据包</th><th class=\"no-sort\">出错数</th>";
	temphtml += "<th class=\"no-sort\">丢包数</th><th class=\"no-sort\">发送速度</th>";
	temphtml += "</tr></thead>";
	temphtml += "<tbody>";
	for(var i=0;i<obj.length;i++){
		temphtml += "<tr class=\"odd gradex\">";
		temphtml +="<td>"+obj[i].timestamp+"</td>";
		temphtml +="<td>"+obj[i].sent_bytes+"</td>";
		temphtml +="<td>"+obj[i].sent_packets+"</td>";
		temphtml +="<td>"+obj[i].sent_errors+"</td>";
		temphtml +="<td>"+obj[i].sent_drop+"</td>";
		temphtml +="<td>"+obj[i].sent_speed+"</td>";
		temphtml +="</tr>";
	}
	temphtml +="</tbody></table>";
	$("#detailcontent").html(temphtml);
	initTable();
}
//从后台获取数据
function getData(obj,startdate,enddate){
	$.ajax({
        type:"POST",
        url:"<%=request.getContextPath()%>/staticreport/getserverdetaildata",
        data:{id:obj,startdate:startdate,enddate:enddate},
        datatype: "json",
        success:function(data){
        	var re = eval(data);
       		if(re.status == "success"){
       			//updateData(re.data);
       			server_status = re.server_status;
       			cpu_online_time = re.cpu_online_time;
       			memory_disk = re.memory_disk;
       			read_write = re.read_write;
       			receive_data = re.receive_data;
       			sent_data = re.sent_data;
       			if(temptabstr=="服务状态"){
       				createServerStatus();
       			}
       			if(temptabstr=="CPU"){
       				cpuOnlineTime();
       			}
       			if(temptabstr=="内存/磁盘"){
       				memoryAndDisk();
       			}
       			if(temptabstr=="读/写"){
       				createReadWriteData();
       			}
       			if(temptabstr=="接收"){
       				createReceiveData();
       			}
       			if(temptabstr=="发送"){
       				createSentData();
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
//更改标签样式
function changeCss(obj){
	$("#tabgroup").find("a").each(function(){
		if($(this).html()==obj){
			$(this).attr("class","btn btn-success");
		}else{
			$(this).attr("class","btn btn-default");
		}
	});
}
//标签切换
function changeTab(obj){
	var tabstr = $(obj).html();
	if(temptabstr==tabstr){
		return;
	}else{
		if(tabstr=="服务状态"){
			createServerStatus();
		}
		if(tabstr=="CPU"){
			cpuOnlineTime();
		}
		if(tabstr=="内存/磁盘"){
			memoryAndDisk();
		}
		if(tabstr=="读/写"){
			createReadWriteData();
		}
		if(tabstr=="接收"){
			createReceiveData();
		}
		if(tabstr=="发送"){
			createSentData();
		}
		temptabstr = tabstr;
		changeCss(tabstr);
	}
}
//小标签切换
function changeTab2(obj){
	var tabstr = $(obj).html();
	if(temptabstr2==tabstr){
		return;
	}else{
		if(tabstr=="最近5天"){
			$("#fiveday").attr("class","btn btn-success");
			$("#custom").attr("class","btn btn-default");
			$("#betweendate").css("display","none");
			$("#exportdiv").css("margin-left","350px")
			//自定义点击了查询按钮以后才重新更新数据
			if(customqueryflag){
				getData(tempcloudid,null,null);
			}
		}
		if(tabstr=="自定义"){
			$("#fiveday").attr("class","btn btn-default");
			$("#custom").attr("class","btn btn-success");
			$("#betweendate").css("display","block");
			$("#exportdiv").css("margin-left","5px");
		}
		temptabstr2 = tabstr;
	}
}
//自定义查询
function customQuery(){
	var startdate = $("#startdate").val();
	var enddate = $("#enddate").val();
	if(startdate==undefined||startdate==null||startdate==""){
			$("#tipscontent").html("开始日期不能为空");
     		$("#dia").click();
     		return;
	}
	if(enddate==undefined||enddate==null||enddate==""){
		$("#tipscontent").html("截止日期不能为空");
 		$("#dia").click();
 		return;
	}
	getData(tempcloudid,startdate,enddate);
	customqueryflag = true;
}
//导出统计报表数据
function exportStaticData(){
	var obj = "";
	if(temptabstr=="服务状态"){
		obj = "sdserver";
	}else if(temptabstr=="CPU"){
		obj = "sdonline";
	}else if(temptabstr=="内存/磁盘"){
		obj = "sdmemory";
	}else if(temptabstr=="读/写"){
		obj = "sdwrite";
	}else if(temptabstr=="接收"){
		obj = "sdreceive";
	}else if(temptabstr=="发送"){
		obj = "sdsent";
	}
	if(obj!=""){
		window.location.href = "<%=request.getContextPath()%>/staticreport/exportdata/"+cloudname+"/"+obj;
	}
}
//页面跳转
function gotoPage(url) {
    window.location.href = "<%=request.getContextPath()%>"+url;
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


            <h2><i class="fa fa-area-chart"></i> 服务器明细</h2>


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
                          <li class="" style="width:10%;text-align: center;"><a href="#" data-toggle="tab" onclick="gotoPage('/staticreport/cloudindex');">云主机</a></li>
                          <li class="active" style="width:90%;"><a href="#" data-toggle="tab">宿主机</a></li>
                      </ul>
                      <!-- / Nav tabs -->
                    </div>

                  <div class="tile-body no-vpadding" style="margin-top:-10px;">
                    <div class="table-responsive">
                    <hr>
                    <div class="row">
                      <div class="col-md-8 text-left">
						<div style="float: left;" >服务器:<font id="host_name"></font></div><br>
                        <div style="height:20px;"></div>                      
                        <div style="width:70px;float: left;">状态:<font color="green" id="host_status">正常</font></div>
                        <div style="width:90px;float: left;" >CPU核数:<font id="cpu_count">10</font></div>
                        <div style="width:80px;float: left;" >内存:<font id="host_memory">80G</font></div>
                        <div style="width:220px;float: left;">磁盘总空间/已用空间:<font id="host_disk">100G/30G</font></div>
                        <div style="width:80px;float: left;">
                        	<span class="label label-default" style="cursor:pointer;" onclick="backhome('${serverid}')">返回概览</span>
                        </div>
                      </div>
                        <div class="col-sm-4" id="cloudsearch">
							<select class="chosen-select chosen-transparent form-control" id="cloudid" parsley-trigger="change" parsley-required="true" parsley-error-container="#cloudsearch" onchange="queryserver(this)">
	                            <option value="">服务器切换</option>  
	                            <c:forEach items="${serverlists }" var="server">
	                            	<option value="${server.id }">${server.name }</option>
	                            </c:forEach>
	                          </select>  
	                     </div>
                    </div>
                    <div class="row">
	                   <div class="btn-group" id="tabgroup" style="margin-left:10px;margin-top:10px;">
	                          <a class="btn btn-success" role="button" onclick="changeTab(this)">服务状态</a>
	                          <a class="btn btn-default" role="button" onclick="changeTab(this)">CPU</a>
	                          <a class="btn btn-default" role="button" onclick="changeTab(this)">内存/磁盘</a>
	                          <a class="btn btn-default" role="button" onclick="changeTab(this)">读/写</a>
	                          <a class="btn btn-default" role="button" onclick="changeTab(this)">接收</a>
	                          <a class="btn btn-default" role="button" onclick="changeTab(this)">发送</a>
<!-- 	                          <a class="btn btn-default" role="button" onclick="changeTab(this)">IO出错</a>
 -->	                   </div>
                    </div>
                    <div class="row" style="padding-left:10px;padding-top:5px;">
                          <div class="btn-group btn-group-sm" id="tabgroup2" style="float:left;">
                            <button type="button" id="fiveday" class="btn btn-success" onclick="changeTab2(this)">最近5天</button>
                            <button type="button" id="custom" class="btn btn-default" onclick="changeTab2(this)">自定义</button>
                          </div>
                          <div style="float:left;display:none;width:40%;" id="betweendate">
	                        <div class="col-sm-4" style="float:left;padding-left:5px;">
	                          <input type="text" class="form-control" id="startdate" style="min-height: 30px;" placeholder="开始日期" data-date-format="YYYY-MM-DD">
	                        </div>
	                        <div class="col-sm-4" style="float:left;padding-left:5px;padding-right:5px;">
	                          <input type="text" class="form-control" id="enddate" style="min-height: 30px;" placeholder="截止日期" data-date-format="YYYY-MM-DD">
	                        </div>
	                        <button type="button" class="btn btn-blue btn-sm" onclick="customQuery()">查询</button>
                          </div>
                          <div style="width: 100px;float:left;margin-left:350px;" id="exportdiv">
                          	<button type="button" class="btn btn-green btn-sm" onclick="exportStaticData()">导出Excel表</button>
                          </div>
                    </div>
                    <div class="row" style="padding-left:10px;padding-top:5px;">
                     <div class="tile-body rounded-bottom-corners" style="width:60%;" id="detailcontent">
<!-- 	                     <div>
	                      <div style="float: left;">
	                      <span class="label label-default" style="background-color: #16a085;opacity:0.3;">内存使用率</span>
	                      <span class="label label-default" style="background-color: #FF0066;opacity:0.3;">磁盘使用率</span>
	                      </div>
	                      <div style="float: left;"></div>
	                     </div>
	                    <div id="line-area-example" style="height: 250px;"></div> -->
                  	 </div>
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
    <script src="<%=request.getContextPath()%>/assets/js/vendor/momentjs/moment-with-langs.min.js"></script> 
    <script src="<%=request.getContextPath()%>/assets/js/vendor/datepicker/bootstrap-datetimepicker.min.js"></script>


    <script>
     $(function(){
        //chosen select input
        $(".chosen-select").chosen({disable_search_threshold: 10});
        //initialize datepicker
        $('#startdate').datetimepicker({
          icons: {
            time: "fa fa-clock-o",
            date: "fa fa-calendar",
            up: "fa fa-arrow-up",
            down: "fa fa-arrow-down"
          },
          language:'zh-cn'
        });

        $("#startdate").on("dp.show",function (e) {
          var newtop = $('.bootstrap-datetimepicker-widget:eq(0)').position().top-45;      
          $('.bootstrap-datetimepicker-widget:eq(0)').css('top', newtop + 'px');
        });
        
        $('#enddate').datetimepicker({
            icons: {
              time: "fa fa-clock-o",
              date: "fa fa-calendar",
              up: "fa fa-arrow-up",
              down: "fa fa-arrow-down"
            },
            language:'zh-cn'
          });

          $("#enddate").on("dp.show",function (e) {
            var newtop = $('.bootstrap-datetimepicker-widget:eq(1)').position().top-45;      
            $('.bootstrap-datetimepicker-widget:eq(1)').css('top', newtop + 'px');
          });

          getData(tempcloudid,null,null);
      });
    </script>
    
  </body>
</html>
      
