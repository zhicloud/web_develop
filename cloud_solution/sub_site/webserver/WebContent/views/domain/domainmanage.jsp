<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@include file="/views/common/common.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>域名管理</title>
</head>
<body>
	<di<div class="g-mn">
		<div class="dm-list">
			<div class="dm-iteam">
				<h3>组播地址管理</h3>
				<div class="dm-cont">
					<ul>
						<li><img src="<%=request.getContextPath()%>/images/mult_address.png" alt="组播地址管理"/></li>
						<li>
							<input type="text" name="multAddress" id="mult_address" value="${broadcast }" disabled/>
							<a class="btn_ma_edit mda-sty" href="javascript:;">编辑</a>
							<a class="btn_ma_save mda-sty mda-save-sty" href="javascript:;" style="display: none;" onclick="saveGroupIP()">保存</a>
						</li>
					</ul>
				</div>
			</div>
			<div class="dm-iteam" id="goods_albums_box">
				<h3>域名修改</h3>
				<div class="dm-cont">
					<ul>
						<li><img src="<%=request.getContextPath()%>/images/mody_domain.png" alt="域名修改"/></li>
						<li>
							<input type="text" name="domainAddress" id="domain_address" value="${domain }" disabled/>
							<a class="btn_da_edit mda-sty" href="javascript:;">编辑</a>
							<a class="btn_da_save mda-sty mda-save-sty" href="javascript:;" style="display: none;" onclick="saveDomain()">保存</a>
						</li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<div class="g-ft"></div>
<script type="text/javascript">
//保存组播地址
function saveGroupIP(){
	var broadcast = $("#mult_address").val();
	//var reg = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	var reg = /^(2[2-3][4-9])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
	if(!reg.test(broadcast)){
		jQuery.messager.alert('提示:','请输入正确的组播地址格式(D类IP地址)','info'); 
		$("#mult_address").val("${broadcast }");
		return;
	}
	jQuery.messager.confirm('提示:','修改组播地址后,所有服务将会重启,确定修改吗?',function(event){ 
			if(event){
			    $.messager.progress({ 
			        title: '请稍后', 
			        msg: '由于修改后需要重启服务,大概需要10秒的时间', 
			        text: '保存中...' 
			    }); 
				$.ajax({
			        type:"POST",
			        url:"<%=request.getContextPath() %>/domain/setbroadcast",
			        data:{broadcast:broadcast},
			        datatype: "json",
			        success:function(data){
			        	var re = eval(data);
			        	$.messager.progress('close');
			       		if(re.status == "success"){
			       			jQuery.messager.alert('提示:','修改成功','info',function(){
			       				window.location.href = "<%=request.getContextPath() %>/domain/manage";
			       			}); 
			       			
			       		}else{
			       			jQuery.messager.alert('提示:',re.message,'error',function(){
			       				$("#mult_address").val("${broadcast }");
			       			}); 
			       		}       
			        },
			        complete: function(XMLHttpRequest, textStatus){
			        },
			        error: function(){
			        }         
			     });
			}
	}); 
}
//保存域名信息
function saveDomain(){
	var domain = $("#domain_address").val();
	if(/.*[\u4e00-\u9fa5]+.*$/.test(domain)) 
	{ 
		jQuery.messager.alert('提示:','域名不能含有中文','info'); 
		return; 
	} 
	if(domain.length!=""&&domain.length>100){
		jQuery.messager.alert('提示:','域名长度超出定义的100个字符,请重新输入','info'); 
		return;
	}
	
	jQuery.messager.confirm('提示:','修改域名后,所有服务将会重启,确定修改吗?',function(event){ 
		if(event){ 
		    $.messager.progress({ 
		        title: '请稍后', 
		        msg: '由于修改后需要重启服务,大概需要10秒的时间', 
		        text: '保存中...' 
		    }); 
			$.ajax({
		        type:"POST",
		        url:"<%=request.getContextPath() %>/domain/setdomain",
		        data:{domain:domain},
		        datatype: "json",
		        success:function(data){
		        	var re = eval(data);
		        	$.messager.progress('close');
		       		if(re.status == "success"){
		       			jQuery.messager.alert('提示:','修改成功','info',function(){
		       				window.location.href = "<%=request.getContextPath() %>/domain/manage";
		       			}); 
		       		}else{
		       			jQuery.messager.alert('提示:',re.message,'error',function(){
		       				$("#domain_address").val("${domain}");
		       			}); 
		       			
		       		}       
		        },
		        complete: function(XMLHttpRequest, textStatus){
		        },
		        error: function(){
		        }          
		        
		     });
		}
	}); 
}
$(function(){
	$(".btn_ma_edit").on("click",function(){
		$(this).css("display","none");
		$(".btn_ma_save").css("display","inline-block");
		$("#mult_address").attr("disabled",false);
	})
	$(".btn_da_edit").on("click",function(){
		$(this).css("display","none");
		$(".btn_da_save").css("display","inline-block");
		$("#domain_address").attr("disabled",false);
	})
	
	$(".btn_ma_save").on('click',function(){
		$(this).css("display","none");
		$(".btn_ma_edit").css("display","inline-block");
		$("#mult_address").attr("disabled",true);
	})
	$(".btn_da_save").on('click',function(){
		$(this).css("display","none");
		$(".btn_da_edit").css("display","inline-block");
		$("#domain_address").attr("disabled",true);
	})
})
</script>

</body>
</html>