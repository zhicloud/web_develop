<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@include file="/views/common/common.jsp" %>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="" />
	<meta name="description" content="" />
	<title>服务器管理-网络管理</title>
</head>

<body>
<div class="main">
	<div class="g-hd">
	    <div class="hd-logo"><img src="<%=request.getContextPath() %>/images/sermag_logo.png" alt="logo" /></div>
	    <div class="hd-cont">
    		<label>sgaoke@zhicloud.com</label>
    		<span>&nbsp;|&nbsp;</span>
    		<a class="modify-pwd" id="modify_pwd" href="javascript:;">修改密码</a>
    		<span>&nbsp;|&nbsp;</span>
    		<a class="login-out" href="javascript:;">退出</a>
	    </div>
	</div>
	<div class="g-sd">
	    <ul>
    		<li><a href="s_runstate.html"><i class="icon-runstate"></i>运行状态<span class="icon-arrow"></span></a></li>
    		<li><a href="s_netmanage.html"><i class="icon-netmanage"></i>网络管理<span class="icon-arrow"></span></a></li>
    		<li><a href="s_dommanage.html"><i class="icon-dommanage"></i>域名管理<span class="icon-arrow"></span></a></li>
    		<li><a href="s_sysmanage.html"><i class="icon-sysmanage"></i>系统管理<span class="icon-arrow"></span></a></li>
    		<li><a href="s_loglist.html"><i class="icon-loglist"></i>日志清单<span class="icon-arrow"></span></a></li>
	    </ul>
	</div>
	<div class="g-mn">
		<div class="im-wrap">
			<div class="im-title" style="margin-bottom: 5px;">
				<h3>IP地址管理</h3>
				<a class="serrestart" href="javascript:;">重启服务</a>
			</div>
			<div class="im-cont">
				<div class="datagrid-wrap">
					<table class="datagrid-view">
						<thead>
							<tr>
								<th style="width: 20%;">网络设备名</th>
								<th style="width: 20%;">IP</th>
								<th style="width: 20%;">子网掩码</th>
								<th style="width: 20%;">网关</th>
								<th style="width: 20%;">&nbsp;</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${niList }" var="ni">
								<tr>
								<td class="dis"><input type="hidden" name="tdIpt" class="td-sty-ipt" data-val="${ni.name }" value="${ni.name }"/>${ni.name }</td>
								<td>${ni.ip }</td>
								<td>${ni.netmask }</td>
								<td>${ni.gateway }</td>
								<td><a class="opr-dhcp opr-sty" href="javascript:;">DHCP</a>&nbsp;|&nbsp;<a class="opr-manual opr-sty" href="javascript:;">手动</a></td>
							</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="datagrid-pager" style="display: block;">
						<div class="pagination">
							<a class="allarrow-left" href="javascript:;"></a>
							<a class="arrow-left" href="javascript:;"></a>
							<label>|</label>
							<input type="text" name="pagenum" id="pagenum" class="pagenum" value="1"/>
							<label>|</label>
							<a class="allarrow-right" href="javascript:;"></a>
							<a class="arrow-right" href="javascript:;"></a>
						</div>
					</div>
					<div class="datagrid-btn" style="display: none;">
						<div class="btn-info">
							<a class="btn-sty btn-save" id="dg_save" href="javascript:;">保存</a>
							<a class="btn-sty btn-cancel" id="dg_cancel" href="javascript:;">取消</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="na-wrap">
			<div class="na-title">
				<h3>网卡聚合</h3>
				<div class="na-info">
					<div class="na-create-box">
						<p class="na-create-tip na-tip"><span>网卡还没有聚合</span></p>
						<a class="btn-pm-create" href="javascript:;">创建</a>
					</div>
					<div class="na-confirm-box">
						<p class="na-confirm-tip na-tip">
							<input type="text" name="pnName" placeholder="请输入聚合名称" class="pm-name ipt-pm-sty" style="margin-right: 5px;"/>
							<input type="text" name="pnMember" placeholder="请勾选聚合成员" class="pm-member ipt-pm-sty" />
						</p>
						<a class="btn-pm-confirm" href="javascript:;">确定</a>
					</div>
					<div class="na-reset-box">
						<p class="na-reset-tip na-tip">
							<label>聚合名称：</label><span>服务器123</span>
							<label style="margin-left: 50px;">聚合成员：</label><span>NIC,NIC2,NIC3</span>
						</p>
						<a class="btn-pm-reset" href="javascript:;">重置</a>
					</div>
				</div>
			</div>
			<div class="na-cont">
				<div class="datagrid-wrap">
					<table class="datagrid-view">
						<thead>
							<tr>
								<th style="width: 40%;"><input type="checkbox" name="cbPmMember" id="cb_pm_member_all" class="cb-pm" />序号</th>
								<th style="width: 40%;">宽带</th>
								<th style="width: 20%;">状态</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${niList }" var="ni">
								<tr>
									<td><input type="checkbox" name="cbPmMember" id="cb_pm_member_one" class="cb-pm" />${ni.name }</td>
									<td>${ni.speed }</td>
									<td><i class="icon-none-state"></i>未聚合</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<div class="datagrid-pager">
						<div class="pagination">
							<a class="allarrow-left" href="javascript:;"></a>
							<a class="arrow-left" href="javascript:;"></a>
							<label>|</label>
							<input type="text" name="pagenum" id="pagenum" class="pagenum" value="1"/>
							<label>|</label>
							<a class="allarrow-right" href="javascript:;"></a>
							<a class="arrow-right" href="javascript:;"></a>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="g-ft"></div>
</div>
<script type="text/javascript">
var data = [];
var path = "<%=request.getContextPath()%>";
var name="";
$(function(){
    $('.btn-pm-create').on('click',function(){
    	$(".na-create-box").css("display","none");
    	$(".cb-pm").css("display","inline-block");
    	$(".na-confirm-box").css("display","block");
    	$(".na-reset-box").css("display","none");
    })
    
    $(".btn-pm-confirm").on("click",function(){
    	$(".na-create-box").css("display","none");
    	$(".cb-pm").css("display","none");
    	$(".na-confirm-box").css("display","none");
    	$(".na-reset-box").css("display","block");
    })
    
    $(".btn-pm-reset").on("click",function(){
    	$(".na-create-box").css("display","block");
    	$(".cb-pm").css("display","none");
    	$(".na-confirm-box").css("display","none");
    	$(".na-reset-box").css("display","none");
    })
    
    $(".opr-manual").on('click',function(){
    	$(this).addClass("active");
    	var preallIteams = $(this).parent().prevAll();
    	$(preallIteams).each(function(index){
    		if(index<(preallIteams.length-1)){
    			if(!($(this).hasClass("dis"))){
	    			$(this).addClass("dis");
	    			$(this).html('<input type="text" name="tdIpt" class="td-sty-ipt" data-val="'+$(this).text()+'" value="'+$(this).text()+'"/>');
	    		}
    		}
		});
    	$('.datagrid-pager').css("display","none");
    	$('.datagrid-btn').css("display","block");
    })
    
    $("#dg_cancel").on('click',function(){
    	$(".opr-manual").removeClass("active");
    	$(".opr-dhcp").removeClass("active");
    	$("td.dis").each(function(index){
    		var iptval = $(this).children().attr("data-val");
    		$(this).html(iptval);
    		$(this).removeClass("dis");
    	})
    	$('.datagrid-btn').css("display","none");
    	$('.datagrid-pager').css("display","block");
    	
    })
    
    $("#dg_save").on('click',function(){
    	$(".opr-manual").removeClass("active");
    	$(".opr-dhcp").removeClass("active");
    	$("td.dis").each(function(index){
    		var iptval = $(this).children().val();
    		data.push(iptval);
    		$(this).html(iptval);
    		$(this).removeClass("dis");
    	})
    	$('.datagrid-btn').css("display","none");
    	$('.datagrid-pager').css("display","block");
    	jQuery.ajax({
			url: path+'/net/modifymanual',
			type: 'post', 
			data:{data:data},
			dataType: 'json',
			timeout: 10000,
			async: false,
			success:function(data){
				if(data.status == "success"){
					window.location.reload();
				}else if(data.status == "fail"){
					return;
				}
			}
		});
    })
    var m = 0;
    $("#cb_pm_member_all").on("click",function(){
    	if($(this).prop("checked")){
    		m = $(".datagrid-view input[type='checkbox']").length-1;
    		$(".datagrid-view input[type='checkbox']").attr("checked",true);
    	}else{
    		m = 0;
    		$(".datagrid-view input[type='checkbox']").attr("checked",false);
    	}
    })
    $(".datagrid-view input[type='checkbox']").each(function(index){
    	if(index > 0){
    		$(this).on('click',function(){
    			if($(this).prop("checked")){
    				m++;
    				if(m>0){
    					$("#cb_pm_member_all").attr("checked",true);
    				}
    			}else{
    				m--;
    				if(m<1){
    					$("#cb_pm_member_all").attr("checked",false);
    				}
    			}
    		})
    	}
    })
    
    $(".serrestart").on('click',function(){
    	var modifyHtml = '<div class="serrestart-shadow"></div>';
            modifyHtml += '<div class="grid-layer-serrestart">' +
                '<div class="ss-box-info">'+
                	'<h3>确认重启</h3>'+
	                '<div class="ss-tit-cont">'+
		                '<span class="ss-pur-img"></span>'+
		                '<span class="ss-pur-txt">您确定要重启吗？</span>'+
	                '</div>'+
	                '<div class="ss-btn-cont">'+
		                '<a id="ss_btn_confirm" onclick="restart();" class="ss-btn-confirm btn816">确定</a>'+
		                '<a id="ss_btn_cancel" class="ss-btn-cancel btn816">取消</a>'+
	                '</div>'+
                '</div>'+
                '</div>';
            $('body').append(modifyHtml);
    })
    $("body").delegate("#ss_btn_cancel","click",function(){
		$('.serrestart-shadow,.grid-layer-serrestart').hide().remove();
	})
    
    //点击DHCP
	$(".opr-dhcp").on('click',function(){
		$(this).addClass("active");
		name = $("td.dis").children().attr("data-val");
		var dhcpState = false;
		if(dhcpState){
			var modifyHtml = '<div class="dhcp-shadow"></div>';
            modifyHtml += '<div class="grid-layer-dhcp">' +
                '<div class="dhcp-box">'+
                	'<h3>DHCP修改</h3>'+
                	'<p>已使用DHCP获取了IP，子网掩码，网关，如需继续修改请点击手动修改</p>'+
                	'<div class="btn-dhcp-cont"><a href="javascript:;" class="btn-dhcp-confirm">我知道了</a></div>'+
                '</div>'+
                '</div>';
            $('body').append(modifyHtml);
		}else{
			var modifyHtml = '<div class="serrestart-shadow"></div>';
            modifyHtml += '<div id="conf_dhcp" class="grid-layer-serrestart">' +
                '<div class="ss-box-info">'+
                	'<h3>确认使用</h3>'+
	                '<div class="ss-tit-cont">'+
		                '<span class="ss-pur-img"></span>'+
		                '<span class="ss-pur-txt">您确定要使用DHCP吗？</span>'+
	                '</div>'+
	                '<div class="ss-btn-cont">'+
		                '<a id="ss_btn_confirm" onclick="useDHCP();" class="ss-btn-confirm btn816">确定</a>'+
		                '<a id="ss_btn_cancel" class="btn-dhcp-confirm btn816">取消</a>'+
	                '</div>'+
                '</div>'+
                '</div>';
            $('body').append(modifyHtml);
// 			$('.datagrid-pager').css("display","none");
//     		$('.datagrid-btn').css("display","block");
		}
	})
	$("body").delegate(".btn-dhcp-confirm","click",function(){
		$(".opr-dhcp").removeClass("active");
		$('.dhcp-shadow,.grid-layer-dhcp').hide().remove();
	})
})

function restart(){
	jQuery.get(path + "/net/restart",function(data){
		if(data.status == "success"){   
			$('#ss_btn_cancel').click();
			var modifyHtml = '<div class="dhcp-shadow"></div>';
            modifyHtml += '<div class="grid-layer-dhcp">' +
                '<div class="dhcp-box">'+
                	'<h3>提示</h3>'+
                	'<p>网络接口重启成功</p>'+
                	'<div class="btn-dhcp-cont"><a href="javascript:;" class="btn-dhcp-confirm">确定</a></div>'+
                '</div>'+
                '</div>';
            $('body').append(modifyHtml);
		}else{  
			$('#ss_btn_cancel').click();
			var modifyHtml = '<div class="dhcp-shadow"></div>';
            modifyHtml += '<div class="grid-layer-dhcp">' +
                '<div class="dhcp-box">'+
                	'<h3>提示</h3>'+
                	'<p>网络接口重启失败，请联系管理员</p>'+
                	'<div class="btn-dhcp-cont"><a href="javascript:;" class="btn-dhcp-confirm">确定</a></div>'+
                '</div>'+
                '</div>';
            $('body').append(modifyHtml);
		}
	});
}

function useDHCP(){
	$('#ss_btn_cancel').click();
	jQuery.post(path + "/net/modifydhcp",{name:name},function(data){
		if(data.status == "success"){   
			var modifyHtml = '<div class="dhcp-shadow"></div>';
            modifyHtml += '<div class="grid-layer-dhcp">' +
                '<div class="dhcp-box">'+
                	'<h3>提示</h3>'+
                	'<p>DHCP使用成功</p>'+
                	'<div class="btn-dhcp-cont"><a href="javascript:;" class="btn-dhcp-confirm">确定</a></div>'+
                '</div>'+
                '</div>';
            $('body').append(modifyHtml);
		}else{  
			var modifyHtml = '<div class="dhcp-shadow"></div>';
            modifyHtml += '<div class="grid-layer-dhcp">' +
                '<div class="dhcp-box">'+
                	'<h3>提示</h3>'+
                	'<p>DHCP失败，请联系管理员</p>'+
                	'<div class="btn-dhcp-cont"><a href="javascript:;" class="btn-dhcp-confirm">确定</a></div>'+
                '</div>'+
                '</div>';
            $('body').append(modifyHtml);
		}
	});
}
</script>

</body>
</html>
