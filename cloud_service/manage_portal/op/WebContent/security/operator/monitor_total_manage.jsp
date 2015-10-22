<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	
	<title>资源监控 - 系统监控</title>
	<link type="image/x-icon" rel="shortcut icon" href="favicon.ico"/>
	<link type="text/css" rel="stylesheet"  href="<%=request.getContextPath()%>/css/global.css"/>
	<link type="text/css" rel="stylesheet"  href="<%=request.getContextPath()%>/css/glbmonitor.css"/>
<style type="text/css">
.pie_div{
width:220px;height:220px; background: #fff;
border-bottom: 1px solid #f5f9fb;
margin-bottom:15px;
}
.pie_div2{
width:380px;height:220px; background: #fff;
border-bottom: 1px solid #f5f9fb;
margin-bottom:15px;
}
</style>
</head>
<body class="no-js">
<div class="main">
	<section class="m-right">
		<div class="r-cont">
			<div class="c-ware">
				<div class="c-hardware">
					<ul>
						<li><div id="server_pie" class="pie_div"></div></li>
						<li><div id="host_pie" class="pie_div"></div></li>
					</ul>
				</div>
				<div class="c-software-list">
					<ul class="p-iteam">
						<li><div id="cpu_usage" class="pie_div2"></div></li>
						<li><div id="disk_operator" class="pie_div2"></div></li>
					</ul>
					<ul class="p-iteam">
						<li><div id="memory_usage" class="pie_div2"></div></li>
						<li><div id="net_operator" class="pie_div2"></div></li>
					</ul>
				</div>
			</div>
		</div>
	</section>
</div>
<!--JavaScript-->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/glbmonitor.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script src="<%=request.getContextPath()%>/js/echarts-all.js"></script>
<script type="text/javascript">
//初始化饼状图	
function init(obj){
	$.ajax({
 	 	type: "POST",
 	 	async:false,
  		url: "<%=request.getContextPath()%>/monitor/querymonitordata.do",
 		data: "type="+obj,
 		contenttype :"application/x-www-form-urlencoded;charset=utf-8", 
  		success: function(msg){
  			var re = eval("("+msg+")");
  			if(re.result == "success"){
  	  			//折线图
  	  			if(obj=="server"){
  	  				init_pie("server_pie",re);
  	  				init_line("cpu_usage",re);
  	  				init_line("memory_usage",re);
  	  			}
  	  			if(obj=="host"){
  	  				init_pie("host_pie",re);
  	  				init_bar("disk_operator",re);
  	  				init_bar("net_operator",re);
  	  			}	
  			}
  	}
 });
}
//初始化饼状图
function init_pie(obj,data){
	//内圈和外圈半径
	var pie_radius = [50, 60];
	//提示框
	var pie_tooltip = {
	        trigger: 'item',
	        formatter: "{a} <br/>{b} : {c} ({d}%)"
	};
	var pie_legend = {
	        orient : 'horizontal',
	        x : 'center',
	        y : '195',
	        itemWidth : 10,
	        itemHeight : 13,
	        data:['正常','告警','故障','屏蔽']
	};
	//饼状图样式
	var pie_itemStyle = {
	        normal : {
	            label : {
	                show : false
	            },
	            labelLine : {
	                show : false
	            },
	            borderwidth:'0'
	        },
	        emphasis : {
	            label : {
	                show : true,
	                position : 'center',
	                textStyle : {
	                    fontSize : '20',
	                    fontWeight : 'bold'
	                }
	            }
	        }
	};
	//饼状图数据
	var pie_data = [];
	var pie_series = [{
		name:'节点状态',
	    type:'pie',
	    radius : pie_radius,
	    itemStyle : pie_itemStyle,
	    data:pie_data
	}];
	//标题 
	var pie_title ={
	    text: '',
	    subtext: '',
	    x: 'right'
	};
	var pie_option = {
			backgroundColor:'fff',
		    tooltip :pie_tooltip,
		    legend: pie_legend,
		    title:pie_title,
		    calculable : true,
		    series : pie_series
		};	
	if(obj=="server_pie"){
  			pie_series[0].name = "服务器状态";
  			pie_title.text = "服务器";
		}
	if(obj=="host_pie"){
 			pie_series[0].name = "云主机状态";
 			pie_title.text = "云主机";
	}
	pie_data[0] = {value:data.normal, name:'正常',itemStyle:{normal:{color:'#99FF66'}}};
	pie_data[1] = {value:data.warn, name:'告警',itemStyle:{normal:{color:'#FFCC66'}}};
	pie_data[2] = {value:data.error, name:'故障',itemStyle:{normal:{color:'#FF0000'}}};
	pie_data[3] = {value:data.stop, name:'屏蔽',itemStyle:{normal:{color:'#666666'}}};
	var myChart1 = echarts.init(document.getElementById(obj));
	myChart1.setOption(pie_option,true);
}
//数据相关
var cpu_data = [0, 0, 0, 0, 0, 0, 0];
var memory_data = [0, 0, 0, 0, 0, 0, 0];
//初始化折线图
function init_line(obj,data){
var line_title = {
    text: '',
    subtext: '',
    x: 'right',
    textStyle:{
    	color:'#ccc',
    	fontSize:12
    }
};
//工具栏选项
var line_toolbox = {
    show : false,
    feature : {
        mark : {show: true},
        dataView : {show: true, readOnly: false},
        magicType : {show: true, type: ['line', 'bar']},
        restore : {show: true},
        saveAsImage : {show: true}
    }
};
//横坐标
var line_xAxis = [
                 {
                     type : 'category',
                     boundaryGap : false,
                     data : ['','','','','','',''],
                     splitLine : {//分割线
    	                show:false
    	            },
		            axisTick : {    // 轴标记
    	                show:false
    	            }
                 }
];
//纵坐标
var line_yAxis = [
                 {
                     type : 'value',
                     axisLabel : {
                         formatter: '{value}%'
                     }
                 }
];
var line_series = [
          {
              name:'',
              type:'line',
              smooth:true,
              data:[]
          }
];
var line_option = {
		animation:false,
		backgroundColor:'fff',
	    title :line_title,
	    tooltip : {
	        trigger: 'axis',
	        formatter:'{a}:{c}%'
	    },
	    grid: {
	    	x:'60',
	    	y:'40',
            width: '300',
            height: '160',
            backgroundColor: 'rgba(0,0,0,0)',
            borderWidth: 1,
            borderColor: '#ccc'
        },
	    toolbox: line_toolbox,
	    calculable : false,
	    xAxis : line_xAxis,
	    yAxis : line_yAxis,
	    series : line_series
};
if("cpu_usage"==obj){
	line_title.text = "cpu利用率";
	line_series[0].name = "cpu利用率";
	for(var i=0;i<6;i++){
		cpu_data[i] = cpu_data[i+1];
	}
	cpu_data[6] = data.cpu_usage==undefined?0:data.cpu_usage;
	line_series[0].data = cpu_data;
}
if("memory_usage"==obj){
	line_title.text = "内存利用率";
	line_series[0].name = "内存利用率";
	for(var i=0;i<6;i++){
		memory_data[i] = memory_data[i+1];
	}
	memory_data[6] = data.memory_usage==undefined?0:data.memory_usage;
	line_series[0].data = memory_data;
}
	var myChart1 = echarts.init(document.getElementById(obj));
	myChart1.setOption(line_option,true);
}
//柱形图相关数据初始化
var read_time = [0, 0, 0, 0, 0, 0];
var write_time = [0, 0, 0, 0, 0, 0];
var reseive_num = [0, 0, 0, 0, 0, 0];
var send_num = [0, 0, 0, 0, 0, 0];
//柱形图
function init_bar(obj,data){
var bar_title = {
       text: '',
       subtext: '',
       x:'left',
       textStyle:{
    	   fontSize:12,
    	   fontFamily:'sans-serif'
       }
   };
var bar_legend = {
    data:['',''],
    x:'right',
    textStyle:{
    	color:'#ccc'
    }
};
//工具栏
var bar_toolbox = {
    show : false,
    feature : {
        mark : {show: true},
        dataView : {show: true, readOnly: false},
        magicType : {show: true, type: ['line', 'bar']},
        restore : {show: true},
        saveAsImage : {show: true}
    }
};
//横坐标
var bar_xAxis = [
                 {
                     type : 'category',
                     data : ['','','','','',''],
                     splitLine : {//分割线
     	                show:false
     	            },
 		            axisTick : {    // 轴标记
     	                show:false
     	            }
                 }
];
//纵坐标
var bar_yAxis = [
         {
             type : 'value'
         }
];
//数据
var bar_series = [
          {
              name:'蒸发量',
              type:'bar',
              data:[]
          },
          {
              name:'降水量',
              type:'bar',
              data:[]
          }
];
var bar_option = {
		animation:false,
		backgroundColor:'fff',
	    title : bar_title,
	    tooltip : {
	        trigger: 'axis'
	    },
	    grid: {
	    	x:'60',
	    	y:'40',
            width: '300',
            height: '160',
            backgroundColor: 'rgba(0,0,0,0)',
            borderWidth: 1,
            borderColor: '#ccc'
        },
	    legend: bar_legend,
	    toolbox: bar_toolbox,
	    calculable : true,
	    xAxis : bar_xAxis,
	    yAxis : bar_yAxis,
	    series : bar_series
	};
	if("disk_operator"==obj){
		//更新读写次数的数组集合
		for(var i=0;i<5;i++){
			read_time[i] = read_time[i+1];
			write_time[i] = write_time[i+1];
		}
		read_time[5] = data.readtimes==undefined?0:data.readtimes;
		write_time[5] = data.writetimes==undefined?0:data.writetimes;
		bar_title.text = "磁盘操作";
		bar_legend.data = ["读次数","写次数"];
		bar_series[0].name = "读次数";
		bar_series[0].data = read_time;
		bar_series[1].name = "写次数";
		bar_series[1].data = write_time;
	}
	if("net_operator"==obj){
		//更新收包和发包数的数组集合
		for(var i=0;i<5;i++){
			reseive_num[i] = reseive_num[i+1];
			send_num[i] = send_num[i+1];
		}
		reseive_num[5] = data.reseivenum==undefined?0:data.reseivenum;
		send_num[5] = data.sendnum==undefined?0:data.sendnum;
		bar_title.text = "网络操作";
		bar_legend.data = ["收包数","发包数"];
		bar_series[0].name = "收包数";
		bar_series[0].data = reseive_num;
		bar_series[1].name = "发包数";
		bar_series[1].data = send_num;
	}
	var myChart1 = echarts.init(document.getElementById(obj));
	myChart1.setOption(bar_option,true);
}
$(function(){
	init("server");
	init("host");
	self.setInterval("init(\"server\")",10000);
	self.setInterval("init(\"host\")",10000);
})
</script>
<!--/JavaScript-->
</body>
</html>