<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@include file="/views/common/common.jsp" %>
<%@page import="com.zhicloud.ms.vo.ResUsageVO"%>
<%@page import="com.zhicloud.ms.vo.NetVO"%>
<%@page import="com.zhicloud.ms.vo.BcastVO"%>
<%@page import="com.zhicloud.ms.vo.ServiceVO"%>
<%@page import="com.zhicloud.ms.vo.ServerVO"%>
<%@page import="java.util.List"%>
<%
	ServerVO server = (ServerVO) request.getAttribute("server");
	ResUsageVO usage = (ResUsageVO) request.getAttribute("usage");
	
	List<NetVO> netlist = (List<NetVO>) request.getAttribute("netList");
	List<ServiceVO> servicelist = (List<ServiceVO>) request.getAttribute("serviceList");
	List<BcastVO> bcastlist = (List<BcastVO>) request.getAttribute("bcastList");	
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>服务器运行状态</title>
	<style type="text/css">
	.pagination table{
		margin:0px;
	}
	.queryspan{
		margin-left:5px;
		margin-right:5px;
	}
	</style>
</head>
<body>
	<div class="g-mn" id="gmn">
		<div class="container" id="container">
			<div class="mn-hostinfo">
	    		<h3>服务器&nbsp;&nbsp;[<%=server.getName() %>]</h3>
	    		<div class="mn-paraminfo">
	    			<label>操作系统：</label><span><%=server.getType() %></span>
	    			<span>&nbsp;|&nbsp;</span>
	    			<label>类型：</label><span><%=server.getModel() %></span>
	    			<span>&nbsp;|&nbsp;</span>
	    			<label>IP：</label><span><%=server.getIp() %></span>
	    			<span>&nbsp;|&nbsp;</span>
	    			<label>内存：</label><span><%=server.getMem() %></span>
	    			<span>&nbsp;|&nbsp;</span>
	    			<label>CPU核数：</label><span><%=server.getCpucore() %></span>
	    			<span>&nbsp;|&nbsp;</span>
	    			<label>磁盘大小：</label><span><%=server.getDisk() %></span>
	    		</div>
		    </div>		    
		    <div class="mn-curresource">
		    	<div class="cr-headinfo">
					<h2 class="cr-tit" style="display:inline-block;">当前资源使用情况</h2>
					<img id="refresh" src="../images/refresh.png" alt="刷新" style="display:inline-block;cursor:pointer;margin-bottom:4px;margin-left:5px;vertical-align:middle;" />
				</div>
	    		<div class="cr-list">
	    			<div class="cr-iteam">
	    				<div class="s-left-info cr-internal"></div>
	    				<div class="s-right-info"><h3 id="cpuper"><%=usage.getCpuper() %>%</h3><p>CPU使用率</p></div>
	    			</div>
	    			<div class="cr-iteam">
	    				<div class="s-left-info cr-cpu"></div>
	    				<div class="s-right-info"><h3 id="memper"><%=usage.getMemper() %>%</h3><p>内存使用率</p></div>
	    			</div>
	    			<div class="cr-iteam">
	    				<div class="s-left-info cr-disk"></div>
	    				<div class="s-right-info"><h3 id="diskper"><%=usage.getDiskper() %>%</h3><p>硬盘使用率</p></div>
	    			</div>
	    			<div class="cr-iteam">
	    				<div class="s-left-info cr-puff"></div>
	    				<div class="s-right-info"><h3 id="datacap"><%=usage.getData() %>KB</h3><p>数据吞吐量（KB/s)</p></div>
	    			</div>
	    		</div>
		    </div>
		    <div class="mn-recresource">
	    		<h2 class="rr-tit">近期资源使用</h2>
	    		<div class="rr-search">
	    			查询日期<!-- <i class="icon-calendar"></i> -->
	    			<input type="text" name="stime" id="c_stime" class="c-stime" readonly/>	    			
	    			<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_btn">查询</a>
	    		</div>
	    		<div class="rr-list">
	    			<div class="rr-iteam"><div class="rr-iteam-inner" id="main_pie_1"></div></div>  <!-- <div class="rr-iteam-inner"></div> -->
	    			<div class="rr-iteam"><div class="rr-iteam-inner" id="main_pie_2"></div></div>
	    			<div class="rr-iteam"><div class="rr-iteam-inner" id="main_pie_3"></div></div>
	    			<div class="rr-iteam"><div class="rr-iteam-inner" id="main_pie_4"></div></div>
	    		</div>
		    </div>
		    <div class="mn-oteinfolist" id="oteinfolist">
				<div class="m-tabs">
					<ul>
						<li class="current" style="margin-right: 21px;">网络信息</li>
						<li style="margin-left: 20px;margin-right: 20px;">服务状态</li>
						<li style="margin-left: 21px;">组播地址</li>
					</ul>
				</div>
				<div class="f-cb">&nbsp;</div>
				<div class="m-container">
					<div class="m-content current">
						<div class="datagrid-wrap">
							<table class="datagrid-view" id="netinfolist">
								<thead>
									<tr>
										<th>序号</th>
										<th>IP</th>
										<th>子网掩码</th>
										<th>速率</th>
										<th>状态</th>
									</tr>
								</thead>
								<tbody>
									<%	for(NetVO netvo : netlist){
										    %>
										    <tr>
											    <td><%=netvo.getName() %></td>
											    <td><%=netvo.getAddress()%></td>
											    <td><%=netvo.getMask() %></td>
											    <td><%=netvo.getSpeed() %></td>
										    <% if(netvo.getStatus() == "0"){ %>
										    	<td><i class="icon-none-state"></i>DOWN</td>
										    <% }else {
										    %>
										    	<td><i class="icon-state"></i>UP</td>
										    <% } 
										    %>
										    </tr>
									<%} %>
								</tbody>
							</table>
						</div>
					</div>
					<div class="m-content">
						<div class="datagrid-wrap">
							<table class="datagrid-view" id="servicestatuslist">
								<thead>
									<tr>
										<th>序号</th>
										<th>服务</th>
										<th>运行时间</th>
										<th>状态</th>
									</tr>
								</thead>
								<tbody>
									<% for(ServiceVO servicevo: servicelist){
									    %>
									    <tr>
											<td><%=servicevo.getId() %></td>
											<td><%=servicevo.getName() %></td>
											<td><%=servicevo.getRuntime() %></td>
											<%
												if(servicevo.getStatus() == "0"){
											%>
												<td><i class="icon-state"></i>正常</td>
											<% }else {
											%>
												<td><i class="icon-none-state"></i>停止</td>
											<% }%>
										</tr>
									    <%
									}
									%>
								</tbody>
							</table>
						</div>
					</div>
					<div class="m-content">
						<div class="datagrid-wrap">
							<table class="datagrid-view" id="netbcastlist">
								<thead>
									<tr>
										<th>序号</th>
										<th>组播地址</th>
										<th>状态</th>
									</tr>
								</thead>
								<tbody>								
									<% for(BcastVO bcastvo: bcastlist){
									    %>
									    <tr>
									    	<td><%=bcastvo.getName() %></td>
											<td><%=bcastvo.getBcastaddr() %></td>
										<%
											if(bcastvo.getStatus() == "0"){
										%>
											<td><i class="icon-none-state"></i>DOWN</td>
										<% }else {
										%>
											<td><i class="icon-state"></i>UP</td>
										<% }
										%>
										</tr>
									    <%
									}
									%>									
								</tbody>
							</table>				
						</div>
					</div>
				</div>
		    </div>
		</div>
	</div>
	<div class="g-ft"></div>
	
<script type="text/javascript" src="<%=request.getContextPath() %>/js/calendar.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/plugin.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/js/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/esl.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/echarts.js"></script>
<script type="text/javascript">
$(function(){
    $('#c_stime').bind('click',function(){
        $.calendar({btnBar: false});
    });
    $("#refresh").click(function(){
    	refreshUsage();
    });
    
    //绑定选项卡
    $('#oteinfolist').zc_tab({
        tab : ['.m-tabs','li'],
        content : ['.m-container','.m-content'],
        current : 'current',
        classname : "current"
    });
    
    $('#query_btn').click(function(){
    	var selectDate = $('#c_stime').val();
    	if(selectDate.length < 1){
    		alert("请选择要查询的日期。");
    	}else{
    		//alert(selectDate);
        	showCharts(selectDate);
    	}
    });

    function onLoadSuccess()
    {
    	$("body").css({
    		"visibility":"visible"
    	});
    }
    function onLoadError(data){
    	alert();
    }
    
    // 图表部分
    require.config({
        paths:{
            'echarts':'<%=request.getContextPath()%>/js/echarts',
            'echarts/chart/pie' : '<%=request.getContextPath()%>/js/echarts'
        }
  	});
    
    // 默认显示当前的使用情况 
    // alert(getdate());
    $('#c_stime').val(getdate());
    showCharts(getdate());
    
});

// 取当天日期
function getdate()
{
	var now=new Date()
	y=now.getFullYear()
	m=now.getMonth()+1
	d=now.getDate()
	m=m<10?"0"+m:m
	d=d<10?"0"+d:d
	return y+"-"+m+"-"+d
}

// 显示图表信息
function showCharts(rq){		
   	var cpu1, mem1, data1, disk1;
   	var cpu2, mem2, data2, disk2;
   	$.ajax({
        type:"POST",
        url:"<%=request.getContextPath() %>/state/queryshowdata",
        data: {"c_stime":rq},  
        datatype: "json",
        async: false,
        success:function(data){
        	var re = eval(data);
        	cpu1 = re.cpu1;
        	cpu2 = re.cpu2;
        	
        	mem1 = re.mem1;
        	mem2 = re.mem2;
        	
        	data1 = re.data1;
        	data2 = re.data2;
        	
        	disk1 = re.disk1;
        	disk2 = re.disk2;
        },
        complete: function(XMLHttpRequest, textStatus){
        },
        error: function(){
        	alert("获取数据失败。");
        }         
     });

   	//cpu2 = [];
   	//disk1 = [];
   	/*
   	if(cpu1 == null || cpu1 == undefined){
   		alert("未取到值，返回。");
   		return;
   	}
   	
   	alert(cpu1);
   	alert(cpu2);
   	alert(mem1);
   	alert(mem2);
   	alert(disk1);
   	alert(disk2);
   	alert(data1);
   	alert(data2);
   	*/
   	
    require(
            [
                'echarts',
                'echarts/chart/pie'
            ],
            function(ec) {
                var mainChart1 = ec.init(document.getElementById('main_pie_1'));
                var mainChart2 = ec.init(document.getElementById('main_pie_2'));
               	var mainChart3 = ec.init(document.getElementById('main_pie_3'));
               	var mainChart4 = ec.init(document.getElementById('main_pie_4'));
               	
                var option1 = {
            		   title: {
            		       text: "CPU使用率",
            		       subtext: '单位:%',
            		       x: "left"
            		   },
            		   tooltip: {
            		       trigger: "item",
            		       formatter: "{b}时{c}%"
            		   },
            		   legend: {
            		       x: 'center',
            		       data: [rq]
            		   },
            		   xAxis: [
            		       {
            		           type: "category",
            		           name: "小时",
            		           splitLine: {show: false},
            		           data: cpu1
            		       }
            		   ],
            		   yAxis: [
            		       {
            		           type: "log",
            		           name: "百分比"
            		       }
            		   ],
	                    toolbox: {
	                        show : true,
	                        feature : {
	                            mark : false,
	                            dataView : {readOnly: false},
	                            restore : true,
	                            saveAsImage : false
	                        }
	                    },
            		   calculable: true,
            		   series: [
            		       {
            		           name: "CPU percent",
            		           type: "line",
            		           data: cpu2

            		       }
            		   ]
            		};

               var option2 = {
            		   title: {
            		       text: "内存使用率",
            		       subtext: '单位:%',
            		       x: "left"
            		   },
            		   tooltip: {
            		       trigger: "item",
            		       formatter: "{b}时{c}%"
            		   },
            		   legend: {
            		       x: 'center',
            		       data: [rq]
            		   },
            		   xAxis: [
            		       {
            		           type: "category",
            		           name: "小时",
            		           splitLine: {show: false},
            		           data: mem1
            		       }
            		   ],
            		   yAxis: [
            		       {
            		           type: "log",
            		           name: "百分比"
            		       }
            		   ],
	                    toolbox: {
	                        show : true,
	                        feature : {
	                            mark : false,
	                            dataView : {readOnly: false},
	                            restore : true,
	                            saveAsImage : false
	                        }
	                    },
            		   calculable: true,
            		   series: [
            		       {
            		           name: "mem percent",
            		           type: "line",
            		           data: mem2
            		       }
            		   ]
            		};
               
               var option3 = {
            		   title: {
            		       text: "硬盘使用率",
            		       subtext: '单位:%',
            		       x: "left"
            		   },
            		   tooltip: {
            		       trigger: "item",
            		       formatter: "{b}时{c}%"
            		   },
            		   legend: {
            		       x: 'center',
            		       data: [rq]
            		   },
            		   xAxis: [
            		       {
            		           type: "category",
            		           name: "小时",
            		           splitLine: {show: false},
            		           data: disk1
            		       }
            		   ],
            		   yAxis: [
            		       {
            		           type: "log",
            		           name: "百分比"
            		       }
            		   ],
	                    toolbox: {
	                        show : true,
	                        feature : {
	                            mark : false,
	                            dataView : {readOnly: false},
	                            restore : true,
	                            saveAsImage : false
	                        }
	                    },
            		   calculable: true,
            		   series: [
            		       {
            		           name: "disk percent",
            		           type: "line",
            		           data: disk2

            		       }
            		   ]
            		};
               
               var option4 = {
            		   title: {
            		       text: "数据吞吐量",
            		       subtext: '单位:KB/s',
            		       x: "left"
            		   },
            		   tooltip: {
            		       trigger: "item",
            		       formatter: "{b}时{c}KB"
            		   },
            		   legend: {
            		       x: 'center',
            		       data: [rq]
            		   },
            		   xAxis: [
            		       {
            		           type: "category",
            		           name: "小时",
            		           splitLine: {show: false},
            		           data: data1
            		       }
            		   ],
            		   yAxis: [
            		       {
            		           type: "log",
            		           name: "KB/s"
            		       }
            		   ],
	                    toolbox: {
	                        show : true,
	                        feature : {
	                            mark : false,
	                            dataView : {readOnly: false},
	                            restore : true,
	                            saveAsImage : false
	                        }
	                    },
            		   calculable: true,
            		   series: [
            		       {
            		           name: "bandwidth",
            		           type: "line",
            		           data: data2

            		       }
            		   ]
            		};
               
              	if(cpu1 == null || cpu1 == undefined){
               		alert("未取到CPU使用率的值！");
               	}
              	else{
               		mainChart1.setOption(option1,true); 
               }
               if(mem1 == null || mem2 == undefined){
               	alert("未取到内存使用率的值！");
               }
              	else{
               		mainChart2.setOption(option2,true); 
              	}
               if(cpu1 == null || cpu1 == undefined){
              		alert("未取到磁盘使用率的值！");
              	}
             	else{
               		mainChart3.setOption(option3,true);
             	}
               if(cpu1 == null || cpu1 == undefined){
              		alert("未取到数据吞吐量的值！");
              	}
             	else{
               		mainChart4.setOption(option4,true);
             	}
               
               /*
               mainChart1.setOption(option1,true); 
               mainChart2.setOption(option2,true); 
               mainChart3.setOption(option3,true);
               mainChart4.setOption(option4,true);
               */
           }
    );
}

// 定时器，3秒获取一次。
// setInterval( "refreshUsage()", 3000 );
function refreshUsage(){
	$.ajax({
        type:"POST",
        url:"<%=request.getContextPath() %>/state/queryusage",
        datatype: "json",
        success:function(data){
        	var re = eval(data);
        	$("#cpuper").html(re.cpu+"%");
        	$("#memper").html(re.mem+"%");
        	$("#diskper").html(re.disk+"%");
        	$("#datacap").html(re.data+"KB");
        	// alert("状态值：" + re.cpu + ":" + re.mem + ":" + re.disk + ":" + re.data);
        },
        complete: function(XMLHttpRequest, textStatus){
        },
        error: function(){
        }         
     });
}
</script>
</body>
</html>