/// 初始化
var id='';
var region ;
var amount_recharge = 100;
$(document).ready(function(){ 
	// 
	$("#item6").click(function(){
		$("#diy").css("display","block");
	}); 
	$("#item0").click(function(){
		$("#diy").css("display","none");
	}); 
	$("#item1").click(function(){
		$("#diy").css("display","none");
	}); 
	$("#item2").click(function(){
		$("#diy").css("display","none");
	}); 
	$("#item3").click(function(){
		$("#diy").css("display","none");
	}); 
	$("#item4").click(function(){
		$("#diy").css("display","none");
	}); 
	$("input[name=item][id!=item6]").click(function(){
		$("#diy").css("display","none");
	}); 
	$("#item5").click(function(){
		$("#diy").css("display","none");
	});
	$("#region1").click(function(){
		$("#cpuPackage1").css("display","block");
		$("#cpuPackage2").css("display","none");
		$("#cpuPackage4").css("display","none");
		$("#cpuRegion1_1").click();
		$("#memoryPackage1").css("display","block");
		$("#memoryPackage2").css("display","none");
		$("#memoryPackage4").css("display","none");
		$("#memoryRegion1_1").click();
		$("#diskPackage1").css("display","block");
		$("#diskPackage2").css("display","none");
		$("#diskPackage4").css("display","none");
		$("#diskRegion1_1").click();
		$("#bandwidthPackage1").css("display","block");
		$("#bandwidthPackage2").css("display","none");
		$("#bandwidthPackage4").css("display","none");
		$("#bandwidthRegion1_1").click();
		$("#package1").css("display","block");
		$("#package2").css("display","none");
		$("#package4").css("display","none");
		$("#host_region_1").css("display","block");
		$("#host_region_2").css("display","none");
		$("#host_region_4").css("display","none");
		$("input[name='hostId']").each(function(){
			   $(this).attr("checked",false);
	    });  
		$("#item1_1").click();
		if($("#host_region_1").children().length>0){
			$('#hostSelect').show();
			setports(1);
		}else{
			setports(0);
			$('#hostSelect').hide();
			
		}
		$(".checkbox").next("label").removeClass("checked");
		$(".checkbox:checked").next("label").addClass("checked");
		dynamicImage("1");
		getPriceInfo();
		if(region!=1){			
			$("#createHostSelected").empty();
			$("#createHostSelected").append('<a  class="biglabel l" href="javascript:void(0);"     onclick="showCreateHosts();">创建主机</a>');
			$("#hostPage").hide();
		}
		
		region = 1;
	});
	$("#region1").click();
	$("#region2").click(function(){
		$("#cpuPackage1").css("display","none");
		$("#cpuPackage4").css("display","none");
		$("#cpuPackage2").css("display","block");
		$("#cpuRegion2_1").click();
		$("#memoryPackage1").css("display","none");
		$("#memoryPackage4").css("display","none");
		$("#memoryPackage2").css("display","block");
		$("#memoryRegion2_1").click(); 
		$("#diskPackage1").css("display","none");
		$("#diskPackage4").css("display","none");
		$("#diskPackage2").css("display","block");
		$("#diskRegion2_1").click(); 
		$("#bandwidthPackage1").css("display","none");
		$("#bandwidthPackage4").css("display","none");
		$("#bandwidthPackage2").css("display","block");
		$("#bandwidthRegion2_1").click(); 
		$("#package1").css("display","none");
		$("#package4").css("display","none");
		$("#package2").css("display","block");
		$("#host_region_1").css("display","none");
		$("#host_region_4").css("display","none");
		$("#host_region_2").css("display","block");
		$("input[name='hostId']").each(function(){
			   $(this).attr("checked",false);
	    });  
		$("#item2_1").click(); 
		if($("#host_region_2").children().length>0){
			$('#hostSelect').show();
			setports(1);
		}else{
			setports(0);
			$('#hostSelect').hide();
			
		}
		$(".checkbox").next("label").removeClass("checked");
		$(".checkbox:checked").next("label").addClass("checked");
		dynamicImage("2");
		getPriceInfo();
		if(region!=2){			
			$("#createHostSelected").empty();
			$("#createHostSelected").append('<a  class="biglabel l" href="javascript:void(0);"     onclick="showCreateHosts();">创建主机</a>');
			$("#hostPage").hide();
		}
		
		region = 2;
	});
	$("#region4").click(function(){
		$("#cpuPackage1").css("display","none");
		$("#cpuPackage2").css("display","none");
		$("#cpuPackage4").css("display","block");
		$("#cpuRegion4_1").click();
		$("#memoryPackage1").css("display","none");
		$("#memoryPackage2").css("display","none");
		$("#memoryPackage4").css("display","block");
		$("#memoryRegion4_1").click(); 
		$("#diskPackage1").css("display","none");
		$("#diskPackage2").css("display","none");
		$("#diskPackage4").css("display","block");
		$("#diskRegion4_1").click(); 
		$("#bandwidthPackage1").css("display","none");
		$("#bandwidthPackage2").css("display","none");
		$("#bandwidthPackage4").css("display","block");
		$("#bandwidthRegion4_1").click(); 
		$("#package1").css("display","none");
		$("#package2").css("display","none");
		$("#package4").css("display","block");
		$("#host_region_1").css("display","none");
		$("#host_region_2").css("display","none");
		$("#host_region_4").css("display","block");
		$("input[name='hostId']").each(function(){
			   $(this).attr("checked",false);
			   $(this).next("lable").attr("class","biglabel l");
	    });  
		$("#item4_1").click(); 
		if($("#host_region_4").children().length>0){
			$('#hostSelect').show();
			setports(1);
		}else{
			setports(0);
			$('#hostSelect').hide();
			
		}
		$(".checkbox").next("label").removeClass("checked");
		$(".checkbox:checked").next("label").addClass("checked");
		dynamicImage("4");
		getPriceInfo();
		if(region!=4){			
			$("#createHostSelected").empty();
			$("#createHostSelected").append('<a  class="biglabel l" href="javascript:void(0);"     onclick="showCreateHosts();">创建主机</a>');
			$("#hostPage").hide();
		}
		
		region = 4;
	});
	$(":radio").click(function(){
		if($("bandwidth").val()==""&&$("#bandwidth").attr("title")=='M'){ 
			
		} else{
			
			var region = document.getElementsByName("region"); 
			var region_val = " "; 
			for(var i=0;i<region.length;i++){
				if(region[i].checked){
					region_val = region[i].value;
					break;
				}
			} 
		} 
		getDescriptionAndPrice();
	});
	$("#goRechargeRightNow").click(function(){  
		var html = '<form id="gift_form" action="'+a+'/bean/page.do" method="post">'+
		'<input type="hidden" name="bean" value="agentService">'+          
		'<input type="hidden" name="method" value="rechargePage">'+          
		'<input type="hidden" name="userType" value="3">'+          
		'<input type="hidden" name="total_fee" value="'+amount_recharge+'">'+          
		'</form>';
		$("#month_form").html(html);
		$("#gift_form").submit();
  	}); 
	$("input[name='amount']").click(function(){ 
		$("#rh").next("label").html("自定义(元)");
		$("#rh").next("label").removeClass("checked");
		$(this).next("label").addClass("checked"); 
		$("#price").html($(this).val()+"元");
		amount_recharge=$(this).val();
		 
		
	});
	$("#rh").click(function(){
		
		$("#rh1").next("label").removeClass("checked");
		$("#rh2").next("label").removeClass("checked");
		$("#rh3").next("label").removeClass("checked");
	});
	$("#rh").blur(function(){
		if($(this).val()!=''){			
			$("#rh1").next("label").removeClass("checked");
			$("#rh2").next("label").removeClass("checked");
			$("#rh3").next("label").removeClass("checked");  
			$("#rh").next("label").addClass("checked"); 
 			$("#price").html($(this).val()+"元");
			amount=$(this).val();
		}else{
			$("#rh3").click();
			$("#rh3").next("label").addClass("checked");
		}
		
	});
	$("#dataDisk").blur(function(){
        
		if($(this).val()==""&&($(this).attr("title"))=="G"){
			var region = document.getElementsByName("region"); 
			var region_val = " "; 
			for(var i=0;i<region.length;i++){
				if(region[i].checked){
					region_val = region[i].value;
					break;
				}
			}
			if(region_val=='1'){
				
				$("#diskRegion1_1").click();
				$("#diskRegion1_1").next("label").addClass("checked");
			}else{
				$("#diskRegion4_1").click();
				$("#diskRegion4_1").next("label").addClass("checked");
				
			} 
		}
		if($(this).val()!=""&&($(this).attr("title"))=="G"){			
			if(parseInt($(this).val())>parseInt(dataDiskMax)){
				$(this).val(dataDiskMax);
				top.$.messager.alert('警告','最大支持'+dataDiskMax+'G','warning'); 
			}
			if(parseInt($(this).val())<parseInt(dataDiskMin)){
				$(this).val(dataDiskMin);
				top.$.messager.alert('警告','最小支持'+dataDiskMin+'G','warning'); 
			} 
		}
		getDescriptionAndPrice();
	});
	$("#bandwidth").blur(function(){
		if($(this).val()==""&&$(this).attr("title")=='M'){
			
			var region = document.getElementsByName("region"); 
			var region_val = " "; 
			for(var i=0;i<region.length;i++){
				if(region[i].checked){
					region_val = region[i].value;
					break;
				}
			}
			if(region_val=='1'){				
				$("#bandwidthRegion1_1").click();
				$("#bandwidthRegion1_1").next("label").addClass("checked");
			}else{
				$("#bandwidthRegion4_1").click();				
				$("#bandwidthRegion4_1").next("label").addClass("checked");
			}  
			
		} 
		var region = document.getElementsByName("region"); 
	    var region_val = " "; 
	    for(var i=0;i<region.length;i++){
	        if(region[i].checked){
	        	region_val = region[i].value;
	            break;
	        }
	    } 
	    
	    if($(this).val()!=""&&$(this).attr("title")=='M'){
	    	
	    	if(region_val=="1"){ 
	    		if(parseInt($(this).val())>parseInt(bandwidthMax_1)){
	    			$(this).val(bandwidthMax_1);
	    			top.$.messager.alert('警告','广州带宽最大支持'+bandwidthMax_1+'M','warning'); 
	    		}
	    		if(parseInt($(this).val())<parseInt(bandwidthMin_1)){
	    			$(this).val(bandwidthMin_1);
	    			top.$.messager.alert('警告','广州带宽最小支持'+bandwidthMin_1+'M','warning'); 
	    		}
	    	}else if(region_val=="2"){ 
	    		if(parseInt($(this).val())>parseInt(bandwidthMax_2)){
	    			$(this).val(bandwidthMax_2);
	    			top.$.messager.alert('警告','成都带宽最大支持'+bandwidthMax_2+'M','warning'); 
	    		}
	    		if(parseInt($(this).val())<parseInt(bandwidthMin_2)){
	    			$(this).val(bandwidthMin_2);
	    			top.$.messager.alert('警告','成都带宽最小支持'+bandwidthMin_2+'M','warning'); 
	    		}
	    		
	    	}else if(region_val=="4"){ 
	    		if(parseInt($(this).val())>parseInt(bandwidthMax_4)){
	    			$(this).val(bandwidthMax_4);
	    			top.$.messager.alert('警告','香港带宽最大支持'+bandwidthMax_4+'M','warning'); 
	    		}
	    		if(parseInt($(this).val())<parseInt(bandwidthMin_4)){
	    			$(this).val(bandwidthMin_4);
	    			top.$.messager.alert('警告','香港带宽最小支持'+bandwidthMin_4+'M','warning'); 
	    		}
	    		
	    	}
	    }
		getDescriptionAndPrice();
	}); 
	if(name!=''){		 
	}else{ 
		$("#goCreate").click(function(){ 
			slideleft(1);
			
			if (event.stopPropagation) { 
				// this code is for Mozilla and Opera 
				event.stopPropagation(); 
				} 
				else if (window.event) { 
				// this code is for IE 
				window.event.cancelBubble = true; 
				}
	//		event.stopPropagation();
			
		});
		
	}
	getDescriptionAndPrice();
});  
/**
 * 显示镜像描述
 * @param description
 */
function showDescription(description){
	if(description!=''){		
		$("#isotitle").empty();
		$("#isotitle").append("版本信息："+description);
	}else{
		var chkObjs = document.getElementsByName("sysImageId"); 
	    for(var i=0;i<chkObjs.length;i++){
	        if(chkObjs[i].checked==true){  
	        	$("#isotitle").empty();
	        	$("#isotitle").append("版本信息："+$(chkObjs[i]).attr("imgdescription"));
	        	break;
	        }
	    } 
	}
}
function getDescriptionAndPrice(){
	var iteminfo="";
	var sysImageName="";
	var ports = "";
	var chkObjs = document.getElementsByName("item");
    var item = "";
    var bandwidth = "";
    var memory = "";
    var cpu = "";
    var dataDisk = "";
    var monthlyprice = 0;
    var hourprice = "";
    var flag = true;  
    var free = "";
    var region = document.getElementsByName("region"); 
    var region_val = " "; 
    for(var i=0;i<region.length;i++){
    	if(region[i].checked){
    		region_val = region[i].value;
    		break;
    	}
    } 
    var region_info; 
    if(region_val=="1"){
    	region_info = "广州";
    }else if(region_val=="2"){
    	region_info = "成都";
    	
    }else if(region_val=="4"){
    	region_info = "香港";
    	
    } 
    for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
        	if(chkObjs[i].value==""){
        		break;
        	}
        	item = chkObjs[i];       	 
        	cpu = $(item).attr("cpu");
        	memory = $(item).attr("memory");
        	dataDisk = $(item).attr("disk");
        	bandwidth = $(item).attr("bandwidth");    	 
        	monthlyprice = $(item).attr("price");  
        	flag = false;
            break;
        }
    }
    if(flag){  
    	chkObjs = document.getElementsByName("cpu");
    	var cpu = "";
    	for(var i=0;i<chkObjs.length;i++){
    		if(chkObjs[i].checked){
    			cpu = chkObjs[i].value;
    			break;
    		}
    	}
    	chkObjs = document.getElementsByName("memory");
    	var memory = "";
    	for(var i=0;i<chkObjs.length;i++){
    		if(chkObjs[i].checked){
    			memory = chkObjs[i].value;
    			break;
    		}
    	}
    	var diy = "true";
    	chkObjs = document.getElementsByName("dataDisk");
    	for(var i=0;i<chkObjs.length;i++){
    		if(chkObjs[i].checked){
    			dataDisk = chkObjs[i].value;
    			diy = "false";
    			break;
    		}
    	} 
    	if(diy=="true"){
    		dataDisk = $("#dataDisk").val().replace("G","");
    	}
    	diy = "true";
    	chkObjs = document.getElementsByName("bandwidth");
    	for(var i=0;i<chkObjs.length;i++){
    		if(chkObjs[i].checked){
    			bandwidth = chkObjs[i].value;
    			diy = "false";
    			break;
    		}
    	}
    	if(diy=="true"){
    		bandwidth = $("#bandwidth").val().replace("M","");
    	}
    }
    iteminfo = "CPU:"+cpu+"核     内存:"+memory+"G   硬盘:"+dataDisk+"G   带宽:"+bandwidth+"M"; 
    var chkObjs = document.getElementsByName("sysImageId"); 
    for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked==true){  
        	sysImageName = chkObjs[i].id;
        	break;
        }
    } 
    
    
    iteminfo = "地域:"+region_info+"  "+iteminfo;  
    if(monthlyprice==0){  
    	ajax.async = false; 	
    	monthlyprice = refreshPrice(region_val,"2",cpu,memory,dataDisk,bandwidth); 
     	ajax.async = true; 
    	if(flag==false){
    		free = "(推广期免费，2015-03-31止)";
    		if(region_val == '2'){
    			free = "(2015-12-31前免费)";   			
    		}
    	}
    } 
    monthlyprice = monthlyprice*$("#hostAmount").val();
	hourprice = (monthlyprice*$("#hostAmount").val()/30/24).toFixed(3);  
	var info = "<i>单价:<strong>"+hourprice+"元/小时</strong> 约合:<strong>"+monthlyprice+"元/月</strong>"+free+"</i><br/>"+iteminfo
	
//	$("#description_1").empty();
//	$("#description_1").append(info);
	$("#description_2").empty();
	$("#description_2").append(info);
//	$("#description_3").empty();
//	$("#description_3").append('<i style="line-height:40px;">单价:<strong>'+hourprice+'元/小时</strong>　约合:<strong>'+monthlyprice+'元/月</strong>'+free+'</i>');
//     var allPrice = ((+totalPrice)+(+monthlyprice)).toFixed(2); 
//    $("#description_4").empty();
//    $("#description_4").append('当前余额：<strong>'+balance+'元</strong>，您的主机还可使用：<strong style="color:#4cda64">'+getDays(balance,allPrice)+'</strong>');
//    $("#monthlyPrice").val(allPrice);
//    $("#description_5").html(info);
    
}

 
 
function goCreate(){  
 	var formData = $.formToBean(cloud_host_config_form);
 	loadingbegin();
	ajax.remoteCall("bean://vpcService:agentAddVpcBaseInfo", 
		[ formData ],
		function(reply) { 
		loadingend();
			if (reply.status == "exception") {
 				if(reply.errorCode=="RMC_1"){
						top.$.messager.alert("警告","登陆超时，请重新登录","warning",function(){
							slideleft(1);
					});
				}else{ 
					top.$.messager.alert('警告','创建失败','warning'); 
				}
			} else if (reply.result.status == "fail") {
 				top.$.messager.alert('警告',reply.result.message,'warning'); 
// 				$("#cloudhostcreateprogress").html("创建失败");
// 				$("#progressbar_id").hide();
				
			} else {   
				// 刷新progress   
				nextstep(); 
			}
		}
	);  
}
//获取剩余时间
function getDays(balance,totalPrice){
	var dayprice = totalPrice/30;
	var days = parseInt(balance/dayprice);
	if(days<30){
		if(days<1){
			return "不足一天";
		}
		return days+"天";
	}else if(days<365){
		return parseInt(days/30)+"个月"+days%30+"天";
	}else{
		var month=parseInt(parseInt(days%365)/30);
		if(month>0){
			month = month+"月";
		}else{
			month = "";
		}
		var day = parseInt(days%365)%30;
		if(day>0){
			day = day+"天";
		}else{
			day = "";
		}
		return parseInt(days/365)+"年"+month+day;
	}
	return "";
}

function getLoginInfo(username,message,id){
	slideright();
	inituser(username,message); 
	getDescriptionAndPrice();
	$("#goCreate").unbind("click"); 
	$("#goCreate").empty();
	$("#goCreate").append("确认生成");
	$("#my_clouduan").unbind("click");
	$("#my_clouduan").click(function(){
		window.location.href = a+"/user.do";
	});
	userId = id;
	getCreateInfo();
	getDescriptionAndPrice();
	showDescription();
	
}

function getCreateInfo(){  
	var region = document.getElementsByName("region"); 
    var region_val = " "; 
    for(var i=0;i<region.length;i++){
    	if(region[i].checked){
    		region_val = region[i].value;
    		break;
    	}
    } 
	ajax.remoteCall("bean://vpcService:getCreateInfo", 
		[userId],
		function(reply) {
			if (reply.status == "exception") {
				if(reply.errorCode=="RMC_1"){
//						top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
//							slideleft(1);
//					});
				}else{
					
					top.$.messager.alert('警告','获取信息失败','warning'); 
			  }
		    }
			else if (reply.result.status == "success") 
			{ 
				balance = reply.result.properties.balance;
 				vpcName = reply.result.properties.name;
				$("#vpcname").val(vpcName);
				$("#vpcname_display").html(vpcName);
				$("#description_4").html('当前余额：<strong>'+balance+'元</strong>')
			} 
			else 
			{
				top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		); 
}
// 通过地域选择显示不同镜像
function checkRegion(){
	var region = document.getElementsByName("region"); 
    var region_val = " "; 
    for(var i=0;i<region.length;i++){
    	if(region[i].checked){
    		region_val = region[i].value;
    		break;
    	}
    } 
    var region_info;     
    if(region_val=="1"){
    	$("#image_region_4+ .combo").hide();
    	$("#image_region_2+ .combo").hide();
    	$("#image_region_1+ .combo").show();
    	 
    }else if(region_val=="4"){
    	$("#image_region_1+ .combo").hide();
    	$("#image_region_2+ .combo").hide();
    	$("#image_region_4").show();
      	
    } else if(region_val=="2"){
    	$("#image_region_1+ .combo").hide();
    	$("#image_region_4+ .combo").hide();
    	$("#image_region_2+ .combo").show(); 
  
    	
    }  
}


function checkPortsCount(obj){
	var boxArray = document.getElementsByName('ports'); 
	var total = 0;
 	for(var i=0;i<boxArray.length;i++){
	 	if(boxArray[i].checked){
	 	  total++;
	 	}
 	}
 	if(total > cdMaxPorts){ 
 		$(obj).removeAttr("checked");
 	 	top.$.messager.alert('警告','端口达到最大上限<br>如果需要开通更多端口，请联系管理员!','warning');  
 	}else{		
 		$("#portsCount").html(total);	
 	}

}

 

function getDefaultPort(){
	var region = document.getElementsByName("region"); 
    var region_val = " "; 
    for(var i=0;i<region.length;i++){
    	if(region[i].checked){
    		region_val = region[i].value;
    		break;
    	}
    }  
    //只有选择成都才做处理，其他地域跳过
    if(region_val=="2" ){ 
    	 isDefaultPortsCreated = true;
    	 var sysImageName = "";
    	 var chkObjs = document.getElementsByName("sysImageId"); 
	     for(var i=0;i<chkObjs.length;i++){
	        if(chkObjs[i].checked==true){  
	        	sysImageName = $(chkObjs[i]).next().html();
	        	break;
	        }
	     }
	     if(sysImageName.indexOf("windows")!=-1 || sysImageName.indexOf("Windows")!=-1){
	    	 $("tr[class='nochange']").remove();
	    	 var html = '<tr class="nochange">'+
             '  <td style="width:28px"><div class="datagrid-cell">'+
             '  <input name="ports" type="checkbox" disabled="disabled" checked="checked" onclick="checkPortsCount(this);" value="FTP&1&21,30000-30003" />'+
             '  </div></td>'+
             '  <td style="width:231px"><div class="datagrid-cell">FTP</div></td>'+
             '  <td style="width:231px"><div class="datagrid-cell">TCP</div></td>'+
             '  <td style="width:230px"><div class="datagrid-cell">21,30000-30003</div></td>'+
             ' </tr>';
             html = html +  '<tr class="nochange">'+
             '  <td style="width:28px"><div class="datagrid-cell">'+
             '  <input name="ports" type="checkbox" disabled="disabled" checked="checked" onclick="checkPortsCount(this);" value="RDP&1&3389" />'+
             '  </div></td>'+
             '  <td style="width:231px"><div class="datagrid-cell">RDP</div></td>'+
             '  <td style="width:231px"><div class="datagrid-cell">TCP</div></td>'+
             '  <td style="width:230px"><div class="datagrid-cell">3389</div></td>'+
             ' </tr>';
             
             $("tr[class='headtr']").after(html);
	    	 
	     }else if(sysImageName.indexOf("centos")!=-1 || sysImageName.indexOf("Centos")!=-1 ||sysImageName.indexOf("ubuntu")!=-1 || sysImageName.indexOf("Ubuntu")!=-1){
	    	 $("tr[class='nochange']").remove();
	    	 var html = '<tr class="nochange">'+
             '  <td style="width:28px"><div class="datagrid-cell">'+
             '  <input name="ports" type="checkbox" disabled="disabled" checked="checked" onclick="checkPortsCount(this);" value="FTP&1&21,30000-30003" />'+
             '  </div></td>'+
             '  <td style="width:231px"><div class="datagrid-cell">FTP</div></td>'+
             '  <td style="width:231px"><div class="datagrid-cell">TCP</div></td>'+
             '  <td style="width:230px"><div class="datagrid-cell">21,30000-30003</div></td>'+
             ' </tr>';
             html = html +  '<tr class="nochange">'+
             '  <td style="width:28px"><div class="datagrid-cell">'+
             '  <input name="ports" type="checkbox" disabled="disabled" checked="checked" onclick="checkPortsCount(this);" value="SSH&1&22" />'+
             '  </div></td>'+
             '  <td style="width:231px"><div class="datagrid-cell">SSH</div></td>'+
             '  <td style="width:231px"><div class="datagrid-cell">TCP</div></td>'+
             '  <td style="width:230px"><div class="datagrid-cell">22</div></td>'+
             ' </tr>'; 
             html = html +  '<tr class="nochange">'+
             '  <td style="width:28px"><div class="datagrid-cell">'+
             '  <input name="ports" type="checkbox" disabled="disabled" checked="checked" onclick="checkPortsCount(this);" value="主机面板&1&10000" />'+
             '  </div></td>'+
             '  <td style="width:231px"><div class="datagrid-cell">主机面板</div></td>'+
             '  <td style="width:231px"><div class="datagrid-cell">TCP</div></td>'+
             '  image_region_2<td style="width:230px"><div class="datagrid-cell">10000</div></td>'+
             ' </tr>';
             
             $("tr[class='headtr']").after(html);
	     }
    	
    }
	
}
function checkVpcInfo(){
	var str = $("#vpcname").val();
	if(str.length>=2&&str.length<=50){
		$("#for_vpcname").html("");
 	}else{
		$("#for_vpcname").html("VPC名字在2-50个字符内");
		$("#goCreate").attr("class","disabledbutton r");
		$("#goCreate").removeAttr("onclick");
		return false;		
	}
	str = $("#decription").val();
	if(str.length>100){
		$("#for_vpcname").html("VPC描 述在100个字符内");
		$("#goCreate").attr("class","disabledbutton r");
		$("#goCreate").removeAttr("onclick");
		return false;		
	}else{
		$("#for_vpcname").html("");
 	} 
	if(countHostAmount() >= 2){		
		$("#goCreate").attr("class","bluebutton r");
		$("#goCreate").attr("onclick","goCreate();"); 
	}
	
}

//On creation of a UUID object, set it's initial value
function UUID(){
    this.id = this.createUUID();
}
 
// When asked what this Object is, lie and return it's value
UUID.prototype.valueOf = function(){ return this.id; };
UUID.prototype.toString = function(){ return this.id; };
 
//
// INSTANCE SPECIFIC METHODS
//
UUID.prototype.createUUID = function(){
    //
    // Loose interpretation of the specification DCE 1.1: Remote Procedure Call
    // since JavaScript doesn't allow access to internal systems, the last 48 bits 
    // of the node section is made up using a series of random numbers (6 octets long).
    //  
    var dg = new Date(1582, 10, 15, 0, 0, 0, 0);
    var dc = new Date();
    var t = dc.getTime() - dg.getTime();
    var tl = UUID.getIntegerBits(t,0,31);
    var tm = UUID.getIntegerBits(t,32,47);
    var thv = UUID.getIntegerBits(t,48,59) + '1'; // version 1, security version is 2
    var csar = UUID.getIntegerBits(UUID.rand(4095),0,7);
    var csl = UUID.getIntegerBits(UUID.rand(4095),0,7);
    // since detection of anything about the machine/browser is far to buggy, 
    // include some more random numbers here
    // if NIC or an IP can be obtained reliably, that should be put in
    // here instead.
    var n = UUID.getIntegerBits(UUID.rand(8191),0,7) + 
            UUID.getIntegerBits(UUID.rand(8191),8,15) + 
            UUID.getIntegerBits(UUID.rand(8191),0,7) + 
            UUID.getIntegerBits(UUID.rand(8191),8,15) + 
            UUID.getIntegerBits(UUID.rand(8191),0,15); // this last number is two octets long
    return tl + tm  + thv  + csar + csl + n; 
};
 
//Pull out only certain bits from a very large integer, used to get the time
//code information for the first part of a UUID. Will return zero's if there 
//aren't enough bits to shift where it needs to.
UUID.getIntegerBits = function(val,start,end){
 var base16 = UUID.returnBase(val,16);
 var quadArray = new Array();
 var quadString = '';
 var i = 0;
 for(i=0;i<base16.length;i++){
     quadArray.push(base16.substring(i,i+1));    
 }
 for(i=Math.floor(start/4);i<=Math.floor(end/4);i++){
     if(!quadArray[i] || quadArray[i] == '') quadString += '0';
     else quadString += quadArray[i];
 }
 return quadString;
};
 
//Replaced from the original function to leverage the built in methods in
//JavaScript. Thanks to Robert Kieffer for pointing this one out
UUID.returnBase = function(number, base){
 return (number).toString(base).toUpperCase();
};
 
//pick a random number within a range of numbers
//int b rand(int a); where 0 <= b <= a
UUID.rand = function(max){
 return Math.floor(Math.random() * (max + 1));
};

function dynamicImage(region){ 
	ajax.remoteCall("bean://sysDiskImageService:selectDiskImageByRegion", 
			[ region ,userId],
			function(reply) {
				if( reply.status == "exception" ) 
				{ 
				} 
				else if(reply.result.status == "success") 
				{   
				   var defaultID = "";
				   var i = 0;
				   $("#sysImageId").combobox("clear");
				   var data,json;
				   data = [];
 		        	jQuery.each(reply.result.properties.imageList,function(idx,item){ //循环对象取值 
 		        		if(i == 0){
 		        			defaultID = item.id;
 		        		}
 		        		i ++;
  		        		data.push({ "text":item.name , "value": item.id });
  		            }); 
		        	 $("#sysImageId").combobox("loadData", data); 
		        	 $("#sysImageId").combobox("setValue", defaultID); 
					 
				}  
			}
		);  
}

function countHostAmount(){
	var m = 0;
 	var checkbox1 = document.getElementsByName("hostId"); 
    for(var i=0;i<checkbox1.length;i++){
		 if(checkbox1[i].checked == true){
		    m = m + 1;
		 }
    }
    var checkbox2 = document.getElementsByName("createhostinfo"); 
    for(var i=0;i<checkbox2.length;i++){
    	if(checkbox2[i].checked == true){
    		var val = checkbox2[i].value;
    		amount = val.split("/")[0];
    		m = m + parseInt(amount);
    	}
    } 
    
    if(m>=2){
    	var str = $("#vpcname").val();
     	var flag = true;
    	if(str.length>=2&&str.length<=50){
    		$("#for_vpcname").html("");
     	}else{
    		$("#for_vpcname").html("VPC名字在2-50个字符内");
    		$("#goCreate").attr("class","disabledbutton r");
    		$("#goCreate").removeAttr("onclick");
    		flag = false;
     	}
    	if(str.length>100){
    		$("#for_vpcname").html("VPC描 述在100个字符内");
    		$("#goCreate").attr("class","disabledbutton r");
    		$("#goCreate").removeAttr("onclick");
    		flag = false;
     	}else{
    		$("#for_vpcname").html("");
     	} 
    	if(flag){    		
    		$("#goCreate").attr("class","bluebutton r");
    		$("#goCreate").attr("onclick","goCreate();"); 
    	}
 	}else{
 		$("#goCreate").attr("class","disabledbutton r");
 		$("#goCreate").removeAttr("onclick");		
 	}
    return m;
}
 
function getPriceInfo(){ 
	var price = 0;  
	var priceForHour = 0;  
	var hostAmount = countHostAmount();
	var infp = "" ;
	if(hostAmount >= 2){
		info = "<i>单价:<strong>"+priceForHour+"元/小时</strong> 约合:<strong>"+price+"元/月</strong></i> &nbsp;(主机/"+countHostAmount()+"个&nbsp; IP/"+$("#ipAmount").val()+"个)";
	}else{
		info = "<i>单价:<strong>"+priceForHour+"元/小时</strong> 约合:<strong>"+price+"元/月</strong></i> &nbsp;(主机/"+countHostAmount()+"个&nbsp; IP/"+$("#ipAmount").val()+"个)<br>创建至少应选择2个主机";
		
	}
	$("#description_1").html(info);
   	var formData = $.formToBean(cloud_host_config_form);
 	ajax.remoteCall("bean://vpcService:countVpcPrice", 
		[ formData ],
		function(reply) { 
 			if (reply.status == "exception") {
 				if(reply.errorCode=="RMC_1"){
						top.$.messager.alert("警告","登陆超时，请重新登录","warning",function(){
							slideleft(1);
					});
				}else{ 
 				}
			} else if (reply.result.status == "fail") { 
			} else {   
				// 更新价格显示
				price = reply.result.properties.price;  
				priceForHour = reply.result.properties.priceForHour;  
				hostAmount = countHostAmount();
				infp = "" ;
				if(hostAmount >= 2){
					info = "<i>单价:<strong>"+priceForHour+"元/小时</strong> 约合:<strong>"+price+"元/月</strong></i> &nbsp;(主机/"+countHostAmount()+"个&nbsp; IP/"+$("#ipAmount").val()+"个)";
				}else{
					info = "<i>单价:<strong>"+priceForHour+"元/小时</strong> 约合:<strong>"+price+"元/月</strong></i> &nbsp;(主机/"+countHostAmount()+"个&nbsp; IP/"+$("#ipAmount").val()+"个)<br>创建至少应选择2个主机";
					
				}
				$("#description_1").html(info);
			}
		}
	);  
}