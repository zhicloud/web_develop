<%@page import="com.zhicloud.op.app.pool.CloudHostData"%>
<%@page import="com.zhicloud.op.app.pool.CloudHostPoolManager"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.vo.CloudHostOpenPortVO"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	CloudHostVO cloudHost = (CloudHostVO)request.getAttribute("cloudHost");
	List<CloudHostOpenPortVO> ports = (List<CloudHostOpenPortVO>)request.getAttribute("ports");
	CloudHostData cloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
%>
<!-- cloud_host_view_detail.jsp -->
<div id="cloud_host_view_detail_dlg_container">
	<div id="cloud_host_view_detail_dlg" class="easyui-dialog" title="云主机详情-[<%=cloudHost.getHostName() %>]"
		style="width:800px; height:600px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			modal: true,
			onMove: _cloud_host_view_detail_dlg_scope_.onMove ,
			onClose: _cloud_host_view_detail_dlg_scope_.myOnClose ,
			onDestroy: function(){
 				delete _cloud_host_view_detail_dlg_scope_; 
			}
		">
		<form id="cloud_host_view_detail_dlg_form" method="post">
			
			<table border="0" style="width:90%; margin:0 auto 0 auto;">
				<tr>
					<td style="padding:5px;">云主机：<%=cloudHost.getHostName()%></td>
					<td style="padding:5px;">状态：<%=cloudHost.getSummarizedStatusText()%></td>
				</tr>
				<tr>
					<td style="padding:5px;">真实主机名：<%=cloudHost.getHostName()%></td>
					<td style="padding:5px;">真实ID：<%=cloudHost.getRealHostId()%></td>
				</tr>
				<tr>
					<td style="padding:5px;">spice用户名：<%=cloudHost.getUserAccount()%></td>
					<td style="padding:5px;">密码：<%=cloudHost.getPassword()%></td>
				</tr> 
				<tr>
					<td style="padding:5px;">CPU：<%=cloudHost.getCpuCore()%>核</td>
					<td style="padding:5px;">内存：<%=cloudHost.getMemoryText(0)%></td>
				</tr>
				<tr>
					<td style="padding:5px;">系统磁盘：<%=cloudHost.getSysDiskText(0)%></td>
					<td style="padding:5px;">数据磁盘：<%=cloudHost.getDataDiskText(0)%></td>
				</tr>
				<tr>
					<td style="padding:5px;">网络带宽：<%=cloudHost.getBandwidthText(2)%></td>
					<td style="padding:5px;">所属用户：<%=cloudHost.getUserAccount()%></td>
				</tr>
				<tr>
					<td style="padding:5px;">内网监控地址：
					<%
					  if(cloudHost.getInnerIp()!=null)
						  out.println(cloudHost.getInnerIp());
					  else{
						  out.println("无");
						  
					  }
					
					%>
					</td>
					<td style="padding:5px;">外网监控地址：
					<%
					  if(cloudHost.getOuterIp()!=null){
						  out.println(cloudHost.getOuterIp());
					      if(cloudHost.getRegion() == 2){
							  out.println(": "+cloudHost.getOuterPort());
					    	  
					      }
						  
					  }
					  else{
						  out.println("无");
						  
					  }
					
					%>
					</td>
				</tr>
				<tr>
					<td colspan="99" style="padding:5px;">
						开放端口：
						<table  style="margin-left:50px;">
						<%
							for( int i=0; i<ports.size(); i++ )
							{
								CloudHostOpenPortVO port = ports.get(i);
								%>
								<tr>
								<td>
								<%
								if(port.getProtocol()==0){
								out.print("所有协议：");
									
								}else if(port.getProtocol()==1){
								out.print("TCP：");
									
								}else if(port.getProtocol()==2){
									out.print("UDP：");
									
								}
								out.print(port.getPort());
								%>
								</td>
								<td>
								<%
								out.print("宿主机端口：");
								out.print(port.getServerPort());
								%>
								</td>
								<td>
								<%
								out.print("外网端口：");
								out.print(port.getOuterPort());
								%>
								</td> 
								</tr>
								<%
							}
						%>
						
						</table>
					</td>
				</tr>
				<tr>
					<td style="padding:10px;" colspan="2">备注：<%=cloudHost.getDescription()%></td> 
				</tr>
				<tr>
					<td colspan="99"><hr /></td>
				<!-- </tr>
				<tr>
					<td style="padding:5px;">磁盘读总流量：0</td>
					<td style="padding:5px;">磁盘写总流量：0</td>
				</tr>
				<tr>
					<td style="padding:5px;">网络读总流量：100G</td>
					<td style="padding:5px;">网络写总流量：100G</td>
				</tr>
				<tr>
					<td colspan="99"><hr /></td>
				</tr> -->
				
			</table>
			
		</form>
		<div id="main_line_1" style="height:220px;border:1px solid #ccc;width: 425px; margin-left: 40px;float: left;"></div>
		<div id="main_pie_1" style="height:220px;border-top:1px solid #ccc;border-right:1px solid #ccc;border-bottom:1px solid #ccc;width: 240px; margin-left: 0px; float: left;"></div> 
<!-- 		<div id="main_line_2" style="height:220px;border-top:1px solid #ccc;border-right:1px solid #ccc;border-bottom:1px solid #ccc;width: 240px; margin-left: 0px; float: left;"></div> -->
		<div id="main_line_3" style="height:220px;border:1px solid #ccc;width: 425px; margin-left: 40px;float: left;"></div>
		<div id="main_pie_2" style="height:220px;border-top:1px solid #ccc;border-right:1px solid #ccc;border-bottom:1px solid #ccc;width: 240px; margin-left: 0px; float: left;"></div> 
<!-- 		<div id="main_line_4" style="height:220px;border-top:1px solid #ccc;border-right:1px solid #ccc;border-bottom:1px solid #ccc;width: 240px; margin-left: 0px; float: left;"></div>  -->
	</div>

</div>


<script type="text/javascript" src="<%=request.getContextPath()%>/js/esl.js"></script>
<script type="text/javascript">


//-----------------------------

//var si = null;

var _cloud_host_view_detail_dlg_scope_ = new function(){
	
	var self = this;
	
	self.si = null;
	
	self.myOnClose = function(){
		window.clearInterval(self.si);
	    $("#cloud_host_view_detail_dlg").dialog('destroy');
		return true; 
	};
	
	self.onMove = function(){
		var thisId = "#cloud_host_view_detail_dlg";
		var topValue = $(thisId).offset().top;
		var leftValue = $(thisId).offset().left;
		if(topValue==0){
			topValue = 30;
		}
		if(topValue<30){
			$(thisId).dialog('move',{
				left:leftValue,
				top:30
			});
			return;
		}
		if(leftValue>1315){
			$(thisId).dialog('move',{
				left:1300,
				top:topValue
			});
			return;
		}
		if(topValue>600){
			$(thisId).dialog('move',{
				left:leftValue,
				top:570
			});
			return;
		}
	}
	
	// 保存
	self.save = function() {
		var formData = $.formToBean(cloud_host_view_detail_dlg_form);
		ajax.remoteCall("bean://cloudHostService:addCloudHost", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					var data = $("#cloud_host_view_detail_dlg_container").parent().prop("_data_");
					$("#cloud_host_view_detail_dlg").dialog("close");
					data.onClose(reply.result);
				} else {
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};

	// 关闭
	self.close = function() {
		$("#cloud_host_view_detail_dlg").dialog("close");
	};
	
	//--------------------------
	$(document).ready(function() {
		var cpuArray = ['0','0','0','0','0','0','0'];
		var memoryArray = ['0','0','0','0','0','0','0'];
		refreshData();
		self.si = window.setInterval(refreshData,10000);
		function refreshData(){ 
			var cloudHostId = "<%=cloudHost.getRealHostId()%>";
			ajax.remoteCall("bean://cloudHostService:refreshData", 
					[ cloudHostId ],
					function(reply) {
						for(var i=0;i<6;i++){
							cpuArray[i] = cpuArray[i+1];
						}
						cpuArray[6] = reply.result.cpuUsage == 0 ? 0 : reply.result.cpuUsage / reply.result.cpu;
						for(var i=0;i<6;i++){
							memoryArray[i] = memoryArray[i+1];
						}
						var _memoryUsage = reply.result.memoryUsage;
						var mu = _memoryUsage * 100; 
						memoryArray[6] = mu.toFixed(0);
						
						var sysDiskUsage = new Number(reply.result.sysDiskUsage);
						var sysDisk = new Number(reply.result.sysDisk);
						var dataDiskUsage = new Number(reply.result.dataDiskUsage);
						var dataDisk = new Number(reply.result.dataDisk);
						var _sysDiskUsage = (sysDiskUsage / 1024 / 1024 / 1024).toFixed(0);	
						var _sysDisk = (sysDisk / 1024 / 1024 / 1024).toFixed(0);	
						var _dataDiskUsage = (dataDiskUsage / 1024 / 1024 / 1024).toFixed(0);	
						var _dataDisk = (dataDisk / 1024 / 1024 / 1024).toFixed(0);	
						require.config({
					        paths:{
					            'echarts':'<%=request.getContextPath()%>/js/echarts',
					            'echarts/chart/bar' : '<%=request.getContextPath()%>/js/echarts',
					            'echarts/chart/pie' : '<%=request.getContextPath()%>/js/echarts',
					            'echarts/chart/line' : '<%=request.getContextPath()%>/js/echarts'
					        }
					    });
					    require(
					            [
					                'echarts',
					                'echarts/chart/bar',
					                'echarts/chart/pie',
					                'echarts/chart/line'
					            ],
					            function(ec) {
					                var mainChart1 = ec.init(document.getElementById('main_line_1'));
					                var mainChart2 = ec.init(document.getElementById('main_pie_1'));
		//				                var mainChart3 = ec.init(document.getElementById('main_line_2'));
					                var mainChart4 = ec.init(document.getElementById('main_line_3'));
					                var mainChart5 = ec.init(document.getElementById('main_pie_2'));
		//				                var mainChart6 = ec.init(document.getElementById('main_line_4'));
					                var option1 = {
					                	animation:false,
					                    tooltip : {
					                        trigger: 'item',
					                        formatter: "{a} <br/>{b} : {c}G</br>({d}%)"
					                    },
					                    legend: {
					                        orient : 'vertical',
					                        x : 'left',
					                        data:['已使用','未使用']
					                    },
					                    toolbox: {
					                        show : false,
					                        feature : {
					                            mark : false,
					                            dataView : {readOnly: false},
					                            restore : true,
					                            saveAsImage : false
					                        }
					                    },
					                    calculable : false,
					                    title : {
					                        text: '系统磁盘占用',
					                        subtext: '',
					                        x:'center',
					                        textStyle:{
					                            fontSize: 8,
					                            fontWeight: 'bolder',
					                            color: '#999'
					                        }
					                    },
					                    series : [
					                        {
					                            name:'系统磁盘占用',
					                            type:'pie',
					                            radius : [0, 50],
					                            center: ['50%', '50%'],
					                            data:[
					                                {value:_sysDiskUsage,name:'已使用',itemStyle:{
					                                    normal:{
					                                        color:'rgba(255,192,0,1)',
					                                        labelLine : { 
							                                	show : false
						                                	},
						                                	label : { 
							                                	show : false
						                                	}
					                                    }
					                                }},
					                                {value:_sysDisk - _sysDiskUsage,name:'未使用',itemStyle:{
					                                    normal:{
					                                        color:'rgba(0,176,240,1)',
					                                        labelLine : { 
							                                	show : false 
						                                	},
						                                	label : { 
							                                	show : false
						                                	}
					                                    }
					                                }}
					                            ]
					                        }
					                    ]
					                }; 
					
					               var option2 = {
					            		animation:false,
					                    title : {
					                        text: '数据磁盘占用',
					                        subtext: '',
					                        x:'center',
					                        textStyle:{
					                            fontSize: 8,
					                            fontWeight: 'bolder',
					                            color: '#999'
					                        }
					                    },
					                    tooltip : {
											show:true,
					                        trigger: 'item',
					                        formatter: "{a} <br/>{b} : {c}G</br>({d}%)"
					                    },
					                    legend: {
					                        orient : 'vertical',
					                        x : 'left',
					                        data:['已使用','未使用']
					                    },
					                    toolbox: {
					                        show : true,
					                        feature : {
					                            mark : false,
					                            dataView : {readOnly: false},
					                            restore : true,
					                            saveAsImage : false
					                        }
					                    },
					                    calculable : false,
					                    series : [
					                        {
					                            name:'数据占用磁盘',
					                            type:'pie',
					                            radius : [0, 50],
					                            left: ['50%', '50%'],
					                            data:[
					                                {value:_dataDiskUsage, name:'已使用',itemStyle:{
					                                    normal:{
					                                        color:'rgba(247,150,70,1)',
					                                        labelLine : { 
							                                	show : false 
						                                	},
						                                	label : { 
							                                	show : false
						                                	}
					                                    }
					                                }},
					                                {value:_dataDisk - _dataDiskUsage, name:'未使用',itemStyle:{
					                                    normal:{
					                                        color:'rgba(146,208,80,1)',
					                                        labelLine : { 
							                                	show : false 
						                                	},
						                                	label : { 
							                                	show : false
						                                	}
					                                    }
					                                }}
					                            ]
					                        }
					                    ]
					                };
					               var option3 = {
					            		   animation:false,
					            		    title : {
					            		        text: 'CPU使用率',
					            		        x:'center',
						                        textStyle:{
						                            fontSize: 8,
						                            fontWeight: 'bolder',
						                            color: '#999'
						                        }
					            		    },
					            		    tooltip : {
					            		        trigger: 'axis',
					            		        formatter:'{a}:{c}%'
					            		    },
					            		    legend: {
					            		        data:['']
					            		    },
					            		    grid: {
					            		    	x:'50',
					            		    	y:'40',
					            	            width: '330',
					            	            height: '150',
					            	            backgroundColor: 'rgba(0,0,0,0)',
					            	            borderWidth: 1,
					            	            borderColor: '#ccc'
					            	        },
					            		    toolbox: {
					            		        show : true,
					            		        feature : {
					            		            mark : {show: false},
					            		            dataView : {show: false, readOnly: false},
					            		            magicType : {show: false, type: ['line', 'bar']},
					            		            restore : {show: false},
					            		            saveAsImage : {show: false}
					            		        }
					            		    },
					            		    calculable : false,
					            		    xAxis : [
					            		        {
					            		            type : 'category',
					            		            boundaryGap : false,
		//				            		            data : ['2014/06/01','2014/06/02','2014/06/03','2014/06/04','2014/06/05','2014/06/06','2014/06/07'],
				 	            		            data : ['','','','','','',''],
					            		            axisTick : {    // 轴标记
						            	                show:false
						            	            },
						            	            axisLabel : {
						            	            	show:false
						            	            },
						            	            splitLine : {
						            	            	show:false
						            	            }
					            		        },
					            		    ],
					            		    yAxis : [
					            		        {
					            		            type : 'value',
					            		            axisLabel : {
					            		                formatter: '{value}%'
					            		            },
					            		            splitArea : {show : false},
					            		            min:0,
					            		            max:100
					            		        }
					            		    ],
					            		    series : [
					            		        {
					            		            name:'CPU使用率',
					            		            type:'line',
					            		            itemStyle: {
					            		                normal: {
					            		                    lineStyle: {
					            		                        shadowColor : 'rgba(0,0,0,0.4)',
					            		                        shadowBlur: 5,
					            		                        shadowOffsetX: 3,
					            		                        shadowOffsetY: 3
					            		                    }
					            		                }
					            		            },
					            		            data:cpuArray,
					            		            markPoint : {
					            		                data : [
					            		                    {type : 'max', name: '最大值'},
					            		                    {type : 'min', name: '最小值'}
					            		                ]
					            		            }
					            		        }
					            		    ]
					            		};
					               /* var option4 = {
					            		   animation:false,
					            		   title : {
					            		        text: '网络读',
					            		        x:'center',
						                        textStyle:{
						                            fontSize: 8,
						                            fontWeight: 'bolder',
						                            color: '#999'
						                        }
					            		    },
					            		    tooltip : {
					            		        trigger: 'axis'
					            		    },
					            		    legend: {
					            		        data:['']
					            		    },
					            		    grid: {
					            		    	x:'40',
					            		    	y:'80',
					            	            width: '165',
					            	            height: '110',
					            	            backgroundColor: 'rgba(0,0,0,0)',
					            	            borderWidth: 1,
					            	            borderColor: '#ccc'
					            	        },
					            		    toolbox: {
					            		        show : true,
					            		        feature : {
					            		            mark : {show: false},
					            		            dataView : {show: false, readOnly: false},
					            		            magicType : {show: false, type: ['line', 'bar']},
					            		            restore : {show: false},
					            		            saveAsImage : {show: false}
					            		        }
					            		    },
					            		    calculable : false,
					            		    xAxis : [
					            		        {
					            		            type : 'category',
					            		            boundaryGap : false,
					            		            data : ['2014/06/01','2014/06/02','2014/06/03','2014/06/04','2014/06/05','2014/06/06','2014/06/07'],
					            		            axisTick : {    // 轴标记
						            	                show:false
						            	            },
						            	            axisLabel : {
						            	            	show:false
						            	            },
						            	            splitLine : {
						            	            	show:false
						            	            }
					            		        }
					            		    ],
					            		    yAxis : [
					            		        {
					            		            type : 'value',
					            		            axisLabel : {
					            		                formatter: '{value}Kb/s'
					            		            },
					            		            splitArea : {show : false}
					            		        }
					            		    ],
					            		    series : [
					            		        {
					            		            name:'网络读',
					            		            type:'line',
					            		            itemStyle: {
					            		                normal: {
					            		                    lineStyle: {
					            		                        shadowColor : 'rgba(0,0,0,0.4)',
					            		                        shadowBlur: 5,
					            		                        shadowOffsetX: 3,
					            		                        shadowOffsetY: 3
					            		                    }
					            		                }
					            		            },
					            		            data:[11, 11, 15, 13, 12, 13, 10],
					            		            markPoint : {
					            		                data : [
					            		                    {type : 'max', name: '最大值'},
					            		                    {type : 'min', name: '最小值'}
					            		                ]
					            		            }
					            		        }
					            		    ]
					            		}; */
					               var option5 = {
					            		   animation:false,
					            		   title : {
					            		        text: '内存使用率',
					            		        x:'center',
						                        textStyle:{
						                            fontSize: 8,
						                            fontWeight: 'bolder',
						                            color: '#999'
						                        }
					            		    },
					            		    tooltip : {
					            		        trigger: 'axis',
					            		        formatter:'{a}:{c}'
					            		    },
					            		    legend: {
					            		        data:['']
					            		    },
					            		    grid: {
					            		    	x:'50',
					            		    	y:'40',
					            	            width: '330',
					            	            height: '150',
					            	            backgroundColor: 'rgba(0,0,0,0)',
					            	            borderWidth: 1,
					            	            borderColor: '#ccc'
					            	        },
					            		    toolbox: {
					            		        show : true,
					            		        feature : {
					            		            mark : {show: false},
					            		            dataView : {show: false, readOnly: false},
					            		            magicType : {show: false, type: ['line', 'bar']},
					            		            restore : {show: false},
					            		            saveAsImage : {show: false}
					            		        }
					            		    },
					            		    calculable : false,
					            		    xAxis : [
					            		        {
					            		            type : 'category',
					            		            boundaryGap : false,
		//				            		            data : ['2014/06/01','2014/06/02','2014/06/03','2014/06/04','2014/06/05','2014/06/06','2014/06/07'],
					            		            data : ['','','','','','',''],
					            		            axisTick : {    // 轴标记
						            	                show:false
						            	            },
						            	            axisLabel : {
						            	            	show:false
						            	            },
						            	            splitLine : {
						            	            	show:false
						            	            }
					            		        }
					            		    ],
					            		    yAxis : [
					            		        {
					            		            type : 'value',
					            		            axisLabel : {
					            		                formatter: '{value}%'
					            		            },
					            		            splitArea : {show : false}
					            		        }
					            		    ],
					            		    series : [
					            		        {
					            		            name:'内存使用率',
					            		            type:'line',
					            		            itemStyle: {
					            		                normal: {
					            		                    lineStyle: {
					            		                        shadowColor : 'rgba(0,0,0,0.4)',
					            		                        shadowBlur: 5,
					            		                        shadowOffsetX: 3,
					            		                        shadowOffsetY: 3
					            		                    }
					            		                }
					            		            },
					            		            data:memoryArray,
					            		            markPoint : {
					            		                data : [
					            		                    {type : 'max', name: '最大值'},
					            		                    {type : 'min', name: '最小值'}
					            		                ]
					            		            }
					            		        }
					            		    ]
					            		};
					               /* var option6 = {
					            		   animation:false,
					            		   title : {
					            		        text: '网络写',
					            		        x:'center',
						                        textStyle:{
						                            fontSize: 8,
						                            fontWeight: 'bolder',
						                            color: '#999'
						                        }
					            		    },
					            		    tooltip : {
					            		        trigger: 'axis'
					            		    },
					            		    legend: {
					            		        data:['']
					            		    },
					            		    grid: {
					            		    	x:'40',
					            		    	y:'80',
					            	            width: '165',
					            	            height: '110',
					            	            backgroundColor: 'rgba(0,0,0,0)',
					            	            borderWidth: 1,
					            	            borderColor: '#ccc'
					            	        },
					            		    toolbox: {
					            		        show : true,
					            		        feature : {
					            		            mark : {show: false},
					            		            dataView : {show: false, readOnly: false},
					            		            magicType : {show: false, type: ['line', 'bar']},
					            		            restore : {show: false},
					            		            saveAsImage : {show: false}
					            		        }
					            		    },
					            		    calculable : false,
					            		    xAxis : [
					            		        {
					            		            type : 'category',
					            		            boundaryGap : false,
					            		            data : ['2014/06/01','2014/06/02','2014/06/03','2014/06/04','2014/06/05','2014/06/06','2014/06/07'],
					            		            axisTick : {    // 轴标记
						            	                show:false
						            	            },
						            	            axisLabel : {
						            	            	show:false
						            	            },
						            	            splitLine : {
						            	            	show:false
						            	            }
					            		        }
					            		    ],
					            		    yAxis : [
					            		        {
					            		            type : 'value',
					            		            axisLabel : {
					            		                formatter: '{value}Kb/s'
					            		            },
					            		            splitArea : {show : false}
					            		        }
					            		    ],
					            		    series : [
					            		        {
					            		            name:'网络写',
					            		            type:'line',
					            		            itemStyle: {
					            		                normal: {
					            		                    lineStyle: {
					            		                        shadowColor : 'rgba(0,0,0,0.4)',
					            		                        shadowBlur: 5,
					            		                        shadowOffsetX: 3,
					            		                        shadowOffsetY: 3
					            		                    }
					            		                }
					            		            },
					            		            data:[11, 11, 15, 13, 12, 13, 10],
					            		            markPoint : {
					            		                data : [
					            		                    {type : 'max', name: '最大值'},
					            		                    {type : 'min', name: '最小值'}
					            		                ]
					            		            }
					            		        }
					            		    ]
					            		}; */
					               mainChart1.setOption(option3,true); 
					               mainChart2.setOption(option1,true); 
		//				               mainChart3.setOption(option4,true); 
					               mainChart4.setOption(option5,true); 
					               mainChart5.setOption(option2,true); 
		//				               mainChart6.setOption(option6,true); 
					           }
					    	);
					}
				);
		}
		
		// “从镜像创建”、“空白系统”的选择切换
		$(':radio[name=check1]').change(function() {
			var checkval = $(this).val();
			$('#disk1').prop('disabled', checkval == '1');
			if(checkval == 1) {
				$('#disk2').slider("enable");
			}else{
				$('#disk2').slider("disable");
			}
		});
		// 是否使用数据磁盘
		$(':checkbox[name=check2]').change(function() {
			var checkval = $(this).prop("checked");
			if(checkval == true) {
				$('#disk3').slider("enable");
			}else{
				$('#disk3').slider("disable");
			}
		});
		// 保存
		$("#cloud_host_view_detail_dlg_save_btn").click(function() {
			self.save();
		});
		// 关闭
		$("#cloud_host_view_detail_dlg_close_btn").click(function() {
			self.close();
		});
	});
	
};
	
	
</script>



