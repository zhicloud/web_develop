<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.UserOrderVO"%>
<%@page import="com.zhicloud.op.vo.ShoppingCartVO"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.vo.DiskPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.BandwidthPackageOptionVO"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.math.BigInteger"%>
<%@page import="com.zhicloud.op.vo.SysDiskImageVO"%>
<%@page import="com.zhicloud.op.vo.MemoryPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.CpuPackageOptionVO"%>
<%@ page import="java.util.*"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	List<SysDiskImageVO> sysDiskImageOptions  = (List<SysDiskImageVO>)request.getAttribute("sysDiskImageOptions");
	
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType); 
	
	
	Integer chance = (Integer)request.getAttribute("trailchance");
	if( chance==null )
	{
		chance = 1;
	}
	
// 	BigDecimal i = new BigDecimal("1073741824");
// 	BigDecimal k = new BigDecimal("1000000");
// 	BigInteger j = new BigInteger("1073741824"); 
%>
<!DOCTYPE html>
<!-- trail.jsp -->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta charset="UTF-8" />
<meta http-equiv="Content-Type" content="textml; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
<title>试用 - 云端在线</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/sequence-wizard.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery.sequence-min.js"></script> 
<script type="text/javascript">
 
$(document).ready(function(){
	var show_per_page = 8; 
	var number_of_items = $('#isos').children().size();
	var number_of_pages = Math.ceil(number_of_items/show_per_page);
	$('#current_page').val(0);
	$('#show_per_page').val(show_per_page);
	$('#number_of_pages').val(number_of_pages);
	if(number_of_pages>1){
	var navigation_html = '<a class="arrow" onclick="javascript:previous()"><img src="<%=request.getContextPath()%>/images/button_prev_disabled.gif" width="16" height="16" alt="上一页" /></a>';
	var current_link = 0;
	while(number_of_pages > current_link){
		navigation_html += '<a class="num" onclick="javascript:go_to_page(' + current_link +')" longdesc="' + current_link +'">'+ (current_link + 1) +'</a>';
		current_link++;
	}
	navigation_html += '<a class="arrow" onclick="javascript:next()"><img src="<%=request.getContextPath()%>/images/button_next.gif" width="16" height="16" alt="下一页" /></a>';
	$('.wizardbtn .page').html(navigation_html);
	$('.wizardbtn .page .num:first').addClass('active');
	$('#isos').children().css('display', 'none');
	$('#isos').children().slice(0, show_per_page).css('display', 'block');	
	}
	
	
	
	var b1;
	var b2;
	var s1;
	var s2;
	$("#cartpopup td").mouseover(function(){
		$(this).parent().find("td").css("background-color","#fff");
		$(this).parent().find(".remove").find("img").attr("src","<%=request.getContextPath()%>/images/button_remove_red_16_16.png");
	});
	$("#cartpopup td").mouseout(function(){
		$(this).parent().find("td").css("background-color","none");
		$(this).parent().find(".remove").find("img").attr("src","<%=request.getContextPath()%>/images/button_remove_gray_16_16.png");
	});
	$("#navcart").mouseenter(function(){
		b1=setTimeout("$('#cartpopup').fadeIn(300)",300);
		s1 && clearTimeout(s1);
		s2 && clearTimeout(s2);
	});
	$("#navcart").mouseleave(function(){
		s1=setTimeout("$('#cartpopup').fadeOut(300)",300);
		b1 && clearTimeout(b1);
		b2 && clearTimeout(b2);
	});
	$("#cartpopup").mouseenter(function(){
		b2=setTimeout("$('#cartpopup').fadeIn(300)",300);
		s1 && clearTimeout(s1);
		s2 && clearTimeout(s2);
	});
	$("#cartpopup").mouseleave(function(){
		s2=setTimeout("$('#cartpopup').fadeOut(300)",300);
		b1 && clearTimeout(b1);
		b2 && clearTimeout(b2);
	});
	var step;
	var options = {
        nextButton: false,
        prevButton: false,
        pagination: false,
        animateStartingFrameIn: true,
        autoPlay: false,
        autoPlayDelay: 3000,
        preloader: true,
        preloadTheseFrames: [1],
        preloadTheseImages: [
        ],
		startingFrameID:1
    };
	var optionsback = {
        nextButton: false,
        prevButton: false,
        pagination: false,
        animateStartingFrameIn: false,
        autoPlay: false,
        autoPlayDelay: 3000,
        preloader: true,
        preloadTheseFrames: [1],
        preloadTheseImages: [],
        startingFrameID:4
    };
	if($.cookie('type')!=''&&$.cookie('type')=='trail'){
    var wSequence = $("#sequence").sequence(optionsback).data("sequence");
		
	}else{
		
    var wSequence = $("#sequence").sequence(options).data("sequence");
	}
	$(".nstep").click(function(){
		wSequence.next();
		nextstep();
	});
	$(".pstep").click(function(){
		wSequence.prev();
		prevstep();
	});
	$(".fstep").click(function(){
		wSequence.next();
		firststep();
	});
	$(".radio").change(function(){
		radiochange();
	});
	$(".checkbox").change(function(){
		checkboxchange();
	});
	firststep();
	radiochange();
	checkboxchange();
	
});
$(document).ready(function(){
	$('body').mousemove(function(e) {  
		var xx=e.pageX;
		var yy=e.pageY; 
		var movement=-(xx/$(window).width()-0.5);
		$(".scloud1").css("margin-left",(movement*200)+180+"px");
	$(".scloud2").css("margin-left",(movement*120)-700+"px");
	$(".scloud3").css("margin-left",(movement*20)-200+"px");
});
	setInterval("parallax()",20);
	var b3;
	var b4;
	var s3;
	var s4;
	$("#pbtn").mouseenter(function(){
		b3=setTimeout("$('#productmenu').fadeIn(300)",300);
		s3 && clearTimeout(s3);
		s4 && clearTimeout(s4);
	});
	$("#pbtn").mouseleave(function(){
		s3=setTimeout("$('#productmenu').fadeOut(300)",300);
		b3 && clearTimeout(b3);
		b4 && clearTimeout(b4);
	});
	$("#productmenu").mouseenter(function(){
		b4=setTimeout("$('#productmenu').fadeIn(300)",300);
		s3 && clearTimeout(s3);
		s4 && clearTimeout(s4);
	});
	$("#productmenu").mouseleave(function(){
		s4=setTimeout("$('#productmenu').fadeOut(300)",300);
		b3 && clearTimeout(b3);
		b4 && clearTimeout(b4);
	});
});
function parallax(){
	$(".banner").html(movement);
	var movement=$(document).scrollTop()/($(document).height()-$(window).height())-0.5;
	$(".scloud1").css("margin-top",(movement*200)+"px");
	$(".scloud2").css("margin-top",(movement*120)+"px");
	$(".scloud3").css("margin-top",(movement*20)+"px");
}
function previous(){
	new_page = parseInt($('#current_page').val()) - 1;
	if($('.wizardbtn .page .active').prev('.num').length==true){
		go_to_page(new_page);
	}
}
function next(){
	new_page = parseInt($('#current_page').val()) + 1;
	//if there is an item after the current active link run the function
	if($('.wizardbtn .page .active').next('.num').length==true){
		go_to_page(new_page);
	}
}
function go_to_page(page_num){
	var show_per_page = parseInt($('#show_per_page').val());
	start_from = page_num * show_per_page;
	end_on = start_from + show_per_page;
	$('#isos').children().css('display', 'none').slice(start_from, end_on).css('display', 'block');
	$('.num[longdesc=' + page_num +']').addClass('active').siblings('.active').removeClass('active');
	$('#current_page').val(page_num);
	if(page_num<=0){
		$('.wizardbtn .page .arrow:first-child img').attr("src","<%=request.getContextPath()%>/images/button_prev_disabled.gif");
		$('.wizardbtn .page .arrow:last-child img').attr("src","<%=request.getContextPath()%>/images/button_next.gif");
	}else if (parseInt($('#number_of_pages').val())-1 <= page_num){
		$('.wizardbtn .page .arrow:first-child img').attr("src","<%=request.getContextPath()%>/images/button_prev.gif");
		$('.wizardbtn .page .arrow:last-child img').attr("src","<%=request.getContextPath()%>/images/button_next_disabled.gif")
	}else{
		$('.wizardbtn .page .arrow:first-child img').attr("src","<%=request.getContextPath()%>/images/button_prev.gif");
		$('.wizardbtn .page .arrow:last-child img').attr("src","<%=request.getContextPath()%>/images/button_next.gif")
	}
}
function firststep(){
	step=0;
	$(".trialsteps>.step:lt("+step+")").css("color","#999");
	$(".trialsteps>.step:lt("+step+")").css("background","url(<%=request.getContextPath()%>/images/wizard_circle_green.png) no-repeat center bottom");
	$(".trialsteps>.step:eq("+step+")").css("color","#2980b9");
	$(".trialsteps>.step:eq("+step+")").css("background","url(<%=request.getContextPath()%>/images/wizard_circle_green.png) no-repeat center bottom");
	$(".trialsteps>.step:gt("+step+")").css("color","#999");
	$(".trialsteps>.step:gt("+step+")").css("background","url(<%=request.getContextPath()%>/images/wizard_circle_gray.png) no-repeat center bottom");
	$(".trialsteps>#greenline").css("-webkit-transition-duration","1s");
	$(".trialsteps>#greenline").css("-moz-transition-duration","1s");
	$(".trialsteps>#greenline").css("-ms-transition-duration","1s");
	$(".trialsteps>#greenline").css("-o-transition-duration","1s");
	$(".trialsteps>#greenline").css("transition-duration","1s");
	$(".trialsteps>#greenline").css("width",step*120);
}
function laststep(){
	step=3;
	$(".trialsteps>.step:lt("+step+")").css("color","#999");
	$(".trialsteps>.step:lt("+step+")").css("background","url(<%=request.getContextPath()%>/images/wizard_circle_green.png) no-repeat center bottom");
	$(".trialsteps>.step:eq("+step+")").css("color","#2980b9");
	$(".trialsteps>.step:eq("+step+")").css("background","url(<%=request.getContextPath()%>/images/wizard_circle_green.png) no-repeat center bottom");
	$(".trialsteps>.step:gt("+step+")").css("color","#999");
	$(".trialsteps>.step:gt("+step+")").css("background","url(<%=request.getContextPath()%>/images/wizard_circle_gray.png) no-repeat center bottom");
	$(".trialsteps>#greenline").css("-webkit-transition-duration","0s");
	$(".trialsteps>#greenline").css("-moz-transition-duration","0s");
	$(".trialsteps>#greenline").css("-ms-transition-duration","0s");
	$(".trialsteps>#greenline").css("-o-transition-duration","0s");
	$(".trialsteps>#greenline").css("transition-duration","0s");
	$(".trialsteps>#greenline").css("width",step*120);
}
function nextstep(){
	step++;
	$(".trialsteps>.step:lt("+step+")").css("color","#999");
	$(".trialsteps>.step:lt("+step+")").css("background","url(<%=request.getContextPath()%>/images/wizard_circle_green.png) no-repeat center bottom");
	$(".trialsteps>.step:eq("+step+")").css("color","#2980b9");
	$(".trialsteps>.step:eq("+step+")").css("background","url(<%=request.getContextPath()%>/images/wizard_circle_green.png) no-repeat center bottom");
	$(".trialsteps>.step:gt("+step+")").css("color","#999");
	$(".trialsteps>.step:gt("+step+")").css("background","url(<%=request.getContextPath()%>/images/wizard_circle_gray.png) no-repeat center bottom");
	$(".trialsteps>#greenline").css("width",step*120);
	getComfirmInfo();
}
function prevstep(){
	step--;
	$(".trialsteps>.step:lt("+step+")").css("color","#999");
	$(".trialsteps>.step:lt("+step+")").css("background","url(<%=request.getContextPath()%>/images/wizard_circle_green.png) no-repeat center bottom");
	$(".trialsteps>.step:eq("+step+")").css("color","#2980b9");
	$(".trialsteps>.step:eq("+step+")").css("background","url(<%=request.getContextPath()%>/images/wizard_circle_green.png) no-repeat center bottom");
	$(".trialsteps>.step:gt("+step+")").css("color","#999");
	$(".trialsteps>.step:gt("+step+")").css("background","url(<%=request.getContextPath()%>/images/wizard_circle_gray.png) no-repeat center bottom");
	$(".trialsteps>#greenline").css("width",step*120);
}
function radiochange(){
			$(".radio").next("label").removeClass("checked");
			$(".radio:checked").next("label").addClass("checked");
}
function checkboxchange(){
			$(".checkbox").next("label").removeClass("checked");
			$(".checkbox:checked").next("label").addClass("checked");
}
function getComfirmInfo(){
	$("#comfirm").empty();
	var iteminfo="";
	var sysImageName="";
	var ports = "";
	var chkObjs = document.getElementsByName("item");
    var item = "";
    for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
        	item = chkObjs[i].value;
            break;
        }
    }
    if(item=="1"){
    	iteminfo = "2核CPU/2G内存/100G HHD/2Mbps 带宽";
    }else if(item=="2"){
    	iteminfo = "4核CPU/4G内存/100G HHD/2Mbps 带宽";
    	
    }else if(item=="3"){
    	iteminfo = "4核CPU/8G内存/100G HHD/2Mbps 带宽";
    	
    }
    
    var chkObjs = document.getElementsByName("sysImageId"); 
    for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked==true){  
        	sysImageName = chkObjs[i].id;
        	break;
        }
    }
    chkObjs = document.getElementsByName("ports");   
       for(var i=0;i<chkObjs.length;i++){
           if(chkObjs[i].checked==true){ 
        		   
        	   ports = ports+chkObjs[i].id+", "; 
           } 
    	
    } 
    ports = ports.substr(0, ports.length-2);
	var info ="<td valign='middle'>"+$("#name").val()+"</td>"+
              "<td valign='middle'>"+iteminfo+"</td>" +
              " <td valign='middle'>试用</td> <td valign='middle'><b>免费</b></td>";
    var info ="<tr> <td width='56' style='color:#999'>主机名</td> <td colspan='3'>"+$("#name").val()+" </td> </tr>"+
              "<tr> <td style='color:#999'>配置</td> <td colspan='3'>"+iteminfo+"</td> </tr>"+
              "<tr> <td width='56' style='color:#999'>镜像</td> <td colspan='3'>"+sysImageName+"</td> </tr>"+
              "<tr> <td style='color:#999'>端口</td> <td colspan='3'>"+ports+"</td></tr>"+
              "<tr> <td width='56' style='color:#999;border:none 0;'>付费方式</td> <td style='border:none 0;'>试用</td> <td width='56' style='color:#999;border:none 0;'>价格</td><td style='border:none 0;'><b>免费</b></td> </tr>";
              
	$("#comfirm").append(info);
    
	
} 
</script>
<!--[if IE 6]>
    <script type="text/javascript" src="js/DD_belatedPNG.js"></script>
    <script language="javascript" type="text/javascript">
    DD_belatedPNG.fix("*");
    </script>
<![endif]-->
</head>

<body>
<div class="page">
  <div class="pagebox">
    <div class="header">
      <div class="headerbox">
        <div class="headerlogo l">
				<a href="<%=request.getContextPath() %>/"><img src="<%=request.getContextPath()%>/images/logo_green_light_145_29.gif" width="145" height="29" alt="云端在线" /></a>
			</div>
			<%
				if(loginInfo!=null){
			%>
			<div class="headerregister r">
				<a href="javascript:void(0);" id="logout_link">注销</a>
			</div>
			<div class="headerlogin r">
				 <%=loginInfo.getAccount()%> 
			</div>
			<%
				} else{
			%>
			<div class="headerregister r">
				<a href="<%=request.getContextPath()%>/user/register.do">注册</a>
			</div>
			<div class="headerlogin r">
				<a href="<%=request.getContextPath()%>/user.do?url=<%=request.getContextPath()%>/user/buy.do">登录</a>
			</div>
			<%
				}
			%>
		</div>
    </div>
    <div class="nav">
      <div class="navbox">
        <div class="navbutton l"><a href="<%=request.getContextPath()%>/">首页</a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l"  ><a href="javascript:void(0);" id="pbtn">产品<img src="<%=request.getContextPath()%>/images/button_navlist.gif" width="12" height="12" /></a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l" id="navactive"><a href="<%=request.getContextPath()%>/user/buy.do">定制云主机</a></div>
        <div class="navsplitter l">&nbsp;</div>
        <div class="navbutton l"><a href="<%=request.getContextPath()%>/public/downloadPage.do">相关下载</a></div>
        <div class="navbutton r"  style="position:relative; z-index:99999"><a href="<%=request.getContextPath()%>/user/shoppingcart.do" id="navcart" >0</a></div>
        <div class="navsplitter r">&nbsp;</div>
        <div class="navcontrol r"><a href="<%=request.getContextPath()%>/user.do"><span>我的云端</span></a></div>
        <div id="cartpopup">
          <div class="cartpopuptitle">购物车</div>
          <div id="cartpopup1" style="display:none;">
            <div class="cartpopupbox">
              <table cellspacing="0">
                <tr class="oddrow">
                  <td class="image"><img src="<%=request.getContextPath()%>/images/icon_product_1.png" width="20" height="20" alt="云主机" /></td>
                  <td class="name">服务器名1</td>
                  <td class="price">￥28</td>
                  <td class="remove"><a href="javascript:void(0);"><img src="<%=request.getContextPath()%>/images/button_remove_gray_16_16.png" width="16" height="16" alt="删除" /></a></td>
                </tr>
                <tr>
                  <td class="image"><img src="<%=request.getContextPath()%>/images/icon_product_1.png" width="20" height="20" alt="云主机" /></td>
                  <td class="name">服务器名1服务器名1服</td>
                  <td class="price">￥3000</td>
                  <td class="remove"><a href="javascript:void(0);"><img src="<%=request.getContextPath()%>/images/button_remove_gray_16_16.png" width="16" height="16" alt="删除" /></a></td>
                </tr>
                <tr class="oddrow">
                  <td class="image"><img src="<%=request.getContextPath()%>/images/icon_product_1.png" width="20" height="20" alt="云主机" /></td>
                  <td class="name">服务器名1</td>
                  <td class="price">￥28</td>
                  <td class="remove"><a href="javascript:void(0);"><img src="<%=request.getContextPath()%>/images/button_remove_gray_16_16.png" width="16" height="16" alt="删除" /></a></td>
                </tr>
              </table>
            </div>
            <div class="viewcarttotal"><b>99</b>台云主机</div>
            <div class="viewcartprice">合计：<b>3000</b>元</div>
            <div class="clear"></div>
            <a href="javascript:void(0);" class="viewcartbutton">去购物车结算</a> </div>
          <div id="cartpopup0"><img src="<%=request.getContextPath()%>/images/image_emptycart.gif" width="98" height="134" alt="购物车还没有商品" /></div>
        </div>
        <div id="productmenu"  >
	        <a href="<%=request.getContextPath()%>/user/cloudsever.do">云主机</a>
	       <a href="<%=request.getContextPath()%>/user/cloudstorage.do">云存储</a> 
	       <a href="<%=request.getContextPath()%>/user/database.do">云数据库</a>
	       <a href="<%=request.getContextPath()%>/user/banancing.do">负载均衡</a>
	       <a href="<%=request.getContextPath()%>/user/sdn.do">SDN</a>
	       <a href="<%=request.getContextPath()%>/user/recovery.do">云容灾</a>
	      </div>
      </div>
    </div>
    <div class="main">
      <div class="fullcontainer">
        <div class="fulltitlebar">
          <div class="tab tabactive">试　　用</div>
          <!-- <div class="tab">包月使用</div>
          <div class="tab">按量付费</div> -->
        </div> 
        <%
			if( loginInfo!=null && chance==0) {
		%>
        <div class="fullcontent" id="trial0" >
		<div class="fullcenter"><br/><br/><br/><img src="<%=request.getContextPath()%>/images/image_sorry.gif" width="60" height="110" alt="抱歉" /><br /><br /><br />
		  您已参加过试用，每个账户仅有一次试用机会。<br /><br /><br /><br /></div>
		        </div> 
		 <%
			} else {
		 %>
        <div  class="content fulltable"  id="trial1">
        
          <div class="trialsteps"> <img id="grayline" src="<%=request.getContextPath()%>/images/wizard_line_gray.gif" width="360" height="16" /> <img id="greenline" src="<%=request.getContextPath()%>/images/wizard_line_green.gif" width="1" height="16" />
            <div class="step">套餐选择</div>
            <div class="step">镜像选择</div>
            <div class="step">网络配置</div>
            <div class="step">配置确认</div>
          </div>
          <form action="" id="trail_form" method="get">
          <%
				if(loginInfo==null){
			%>
          <input name="name" id="name" type="hidden" value="host1"/>
          <%
				} else{
		  %>
          <input name="name" id="name" type="hidden" value="<%=loginInfo.getAccount() %>_1"/>
          <%
				}
		  %>
          
            <div id="sequence">
              <ul class="sequence-canvas">
                <li  >
                  <div class="wizarddesc">请选择您需要试用的套餐</div>
                  <div class="wizardcont">
                    <input  name="item" id="item1" type="radio" value="1" class="radio" checked="checked"/>
                    <label class="labeliso" for="item1">
                    <div class="labelcol" style="width:390px">2核CPU/2G内存/100G HHD/2Mbps 带宽</div>
                    <div class="labelcol"><b>免费</b></div>
                    </label>
                    <input name="item" id="item2" type="radio" value="2" class="radio" />
                    <label class="labeliso" for="item2">
                    <div class="labelcol" style="width:390px">4核CPU/4G内存/100G HHD/2Mbps 带宽</div>
                    <div class="labelcol"><b>免费</b></div>
                    </label>
                    <input name="item" id="item3" type="radio" value="3" class="radio" />
                    <label class="labeliso" for="item3">
                    <div class="labelcol" style="width:390px">4核CPU/8G内存/100G HHD/2Mbps 带宽</div>
                    <div class="labelcol"><b>免费</b></div>
                    </label>
                  </div>
                  <!--<div class="wizardcont">
                    <table class="paytable" cellspacing="0">
                      <tr class="firstrow">
                        <td>&nbsp;</td>
                        <td width="42">CPU</td>
                        <td width="42">内存</td>
                        <td width="84">磁盘</td>
                        <td width="168">带宽</td>
                        <td width="56">付费方式</td>
                        <td width="84">价格</td>
                      </tr>
                      <tr class="evenrow">
                        <td><input name="" type="radio" value="" checked="checked" /></td>
                        <td>2核</td>
                        <td>2G</td>
                        <td>100G HHD</td>
                        <td>2Mbps</td>
                        <td>试用</td>
                        <td><b>免费</b></td>
                      </tr>
                      <tr>
                        <td><input name="" type="radio" value="" /></td>
                        <td>4核</td>
                        <td>4G</td>
                        <td>500G HHD</td>
                        <td>2Mbps</td>
                        <td>试用</td>
                        <td><b>免费</b></td>
                      </tr>
                      <tr class="evenrow">
                        <td><input name="" type="radio" value="" /></td>
                        <td>4核</td>
                        <td>8G</td>
                        <td>500G HHD</td>
                        <td>2Mbps</td>
                        <td>试用</td>
                        <td><b>免费</b></td>
                      </tr>
                    </table>
                  </div>-->
                  <div class="wizardbtn">
                    <input type="button" class="wizardbutton nstep" value="下一步"/>
                  </div>
                </li>
                <li>
                  <div class="wizarddesc">请选择镜像</div> 
                  <div class="wizardcont" id="isos">
                  <%
                        Integer ischecked = 0;
                        String checked="";
						for( SysDiskImageVO sysDiskImageOption : sysDiskImageOptions )
						{
							
							if(sysDiskImageOption.getRealImageId()==null||sysDiskImageOption.getRealImageId().length()==0){
								continue;
							}
							if(ischecked==0){
					%>
                    <input id="<%=sysDiskImageOption.getName()%>" name="sysImageId" type="radio" value="<%=sysDiskImageOption.getId()%>" class="radio" checked="checked"    />
					<%
							}else{
					%>
			 		
                    <input id="<%=sysDiskImageOption.getName()%>" name="sysImageId" type="radio" value="<%=sysDiskImageOption.getId()%>" class="radio"    />
					<% 
							}
							ischecked++;
							
					%> 
                    <label class="labeliso" for="<%=sysDiskImageOption.getName()%>">
                    <div class="labeltitle" ><%=sysDiskImageOption.getName()%></div>
                    <div class="labeldesc"><%=sysDiskImageOption.getDescription()%></div>
                    </label>
					<%
						}
					%>
                     
                  </div>
                  
                  <div class="wizardbtn">
                   <div class="page l"></div>
					<input type='hidden' id='current_page' />
					<input type='hidden' id='show_per_page' />
                    <input type='hidden' id='number_of_pages' />
                    <input type="button" class="wizardbutton pstep" value="上一步" style="background:url(<%=request.getContextPath()%>/images/button_bg_orange_light_120_40.gif) no-repeat"/>
                    &nbsp;&nbsp;
                    <input type="button" class="wizardbutton nstep" value="下一步"/>
                  </div>
                </li>
                <li>
                  <div class="wizarddesc">请选择需要允许的网络协议</div>
                  <div class="wizardcont">
                  <div class="wizardports">
                    <input id="HTTP" name="ports" type="checkbox" value="TCP:80,8080" class="checkbox"  checked="checked"/>
                    <label class="labelport" for="HTTP">HTTP<br/><b>TCP:80,8080</b></label>
                    
                    <input id="SSH/SCP" name="ports" type="checkbox" value="TCP:22" class="checkbox" />
                    <label class="labelport" for="SSH/SCP">SSH/SCP<br/><b>TCP:22</b></label>
                    
                    <input id="RDP" name="ports" type="checkbox" value="TCP:3389" class="checkbox" />
                    <label class="labelport" for="RDP">RDP<br/><b>TCP:3389</b></label>
                    
                    <input id="MySQL" name="ports" type="checkbox" value="TCP:1433,3306" class="checkbox" />
                    <label class="labelport" for="MySQL">MySQL<br/><b>TCP:1433,3306</b></label>
                    
                    <input id="SMTP/POP3" name="ports" type="checkbox" value="TCP:25,110" class="checkbox" />
                    <label class="labelport" for="SMTP/POP3">SMTP/POP3<br/><b>TCP:25,110</b></label>
                    
                    <input id="IMAP" name="ports" type="checkbox" value="TCP:143" class="checkbox" />
                    <label class="labelport" for="IMAP">IMAP<br/><b>TCP:143</b></label>
                    
                    <input id="HTTPS" name="ports" type="checkbox" value="TCP:443,8443" class="checkbox" />
                    <label class="labelport" for="HTTPS">HTTPS<br/><b>TCP:443,8443</b></label>
                    
                    <input id="Telnet" name="ports" type="checkbox" value="TCP:23" class="checkbox" />
                    <label class="labelport" for="Telnet">Telnet<br/><b>TCP:23</b></label>
                    
                    <input id="FTP" name="ports" type="checkbox" value="TCP:20,21" class="checkbox"  />
                    <label class="labelport" for="FTP">FTP<br/><b>TCP:20,21</b></label>
                    
                    <input id="TFTP" name="ports" type="checkbox" value="UDP:69" class="checkbox" />
                    <label class="labelport" for="TFTP">TFTP<br/><b>UDP:69</b></label>
                    
                    <input id="NNTP" name="ports" type="checkbox" value="TCP:119" class="checkbox" />
                    <label class="labelport" for="NNTP">NNTP<br/><b>TCP:119</b></label>
                    
                    <input id="IPSec" name="ports" type="checkbox" value="UDP:500" class="checkbox" />
                    <label class="labelport" for="IPSec">IPSec<br/><b>UDP:500</b></label>
                    
                    <input id="L2TP" name="ports" type="checkbox" value="UDP:1701" class="checkbox" />
                    <label class="labelport" for="L2TP">L2TP<br/><b>UDP:1701</b></label>
                    
                    <input id="PPTP" name="ports" type="checkbox" value="TCP:1723" class="checkbox" />
                    <label class="labelport" for="PPTP">PPTP<br/><b>TCP:1723</b></label>
                    
                    <input id="ORACLE" name="ports" type="checkbox" value="TCP:1521,1158,2100" class="checkbox" />
                    <label class="labelport" for="ORACLE">ORACLE<br/><b>TCP:1521,1158,2100</b></label>
                     
                    <!-- 
                    <input id="FTP_Passive" name="ports" type="checkbox" value="TCP:33000-33003" class="checkbox" />
                    <label class="labelport" for="FTP_Passive">FTP_Passive<br/><b>TCP:33000-33003</b></label>
                    -->
                    
                    <input id="SNMP" name="ports" type="checkbox" value="UDP:161" class="checkbox" />
                    <label class="labelport" for="SNMP">SNMP<br/><b>UDP:161</b></label>
                    </div>
                  </div>
                  <div class="wizardbtn">
                    <input type="button" class="wizardbutton pstep" value="上一步" style="background:url(<%=request.getContextPath()%>/images/button_bg_orange_light_120_40.gif) no-repeat"/>
                    &nbsp;&nbsp;
                    <input type="button" class="wizardbutton nstep" value="下一步"/>
                  </div>
                </li>
                <li>
                  <div class="wizarddesc">您将要试用的云主机配置如下</div>
                  <div class="wizardcont" style="height:390px">
                  <div class="wizardtable">
                    <table class="paytable" cellspacing="0" style="width:600px" id="comfirm">
                      <tr>
                        <td width="56" style="color:#999">主机名</td>
                        <td colspan="3">username_host_1</td>
                      </tr>
                      <tr>
                        <td style="color:#999">配置</td>
                        <td colspan="3">4核CPU/4G内存/500G HHD/2Mbps 带宽</td>
                      </tr>
                      <tr>
                        <td width="56" style="color:#999">镜像</td>
                        <td colspan="3">CentOS 64bit 纯净版</td>
                      </tr>
                      <tr>
                        <td style="color:#999">端口</td>
                        <td colspan="3">FTP, HTTP, IMAP, SMTP, RDS, Telnet, Telnet, Telnet, Telnet, Telnet, Telnet, Telnet, Telnet, Telnet, Telnet, Telnet, Telnet</td>
                      </tr>
                      <tr>
                        <td width="56" style="color:#999;border:none 0;">付费方式</td>
                        <td style="border:none 0;">试用</td>
                        <td width="56" style="color:#999;border:none 0;">价格</td>
                        <td style="border:none 0;"><b>免费</b></td>
                      </tr>
                    </table>
                  </div>
                  <div style="text-align:right;color:#999; padding:15px 170px 0 170px;width:560px;:">每个账户仅有<b style="color:#f39c12;">1</b>次试用机会</div>
                  </div>
                  <div class="wizardbtn">
                    <input type="button" class="wizardbutton pstep" value="上一步" style="background:url(<%=request.getContextPath()%>/images/button_bg_orange_light_120_40.gif) no-repeat"/>
                    &nbsp;&nbsp;
                    <%
						if(loginInfo!=null){
							if(chance!=0){
								
							
					%> 
                    <input type="button" id="confirmbutton" class="wizardbutton" onclick="getTrail();" value="确认"/>
					<%
					     
					   		}else{
				   %>
                    <input type="button" class="wizardbutton buttondisabled" disabled="disabled"    value="您已有试用"/>
						   
					<%  	}
							
						} else{
					%>
                    <input type="button" class="wizardbutton"  id="toLogin" value="登陆后确认"/> 
					<%
						}
					%>
                  </div>
                </li>
              </ul>
            </div>
            <div class="clear">&nbsp;</div>
          </form>
        </div>
        <%
            }
        %>
      </div>
    </div>
  </div>
  <div class="pagefooter">
    <div class="footer">
      <div class="footerbox">
        <div class="footercopyright">Copyright &copy; 2014 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1</div>
      </div>
    </div>
  </div>
</div>
</body> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/big.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.cookie.js"></script>
	<script type="text/javascript">
	window.name = "selfWin";
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	
	var _terminal_user_add_dlg_scope_ = new function(){
		
		var self = this;
		 
		
		// 保存
		self.save = function() {
			var sysImageId = $("#sysImageId").val();
			if(sysImageId==""){
				top.$.messager.alert('警告','请选择镜像','warning');
			}else{
				
				var formData = $.formToBean(cart_form);
				ajax.remoteCall("bean://paymentService:addCart", 
					[ formData ],
					function(reply) {
						if (reply.status == "exception") {
							top.$.messager.alert('警告',reply.exceptionMessage,'warning');
						} else if (reply.result.status == "success") {
							// 注册成功
							$("<div class=\"datagrid-mask\"></div>").css({
								display:"block",
								width:"100%",
								height:"100%"
							}).appendTo("body"); 
							$("<div class=\"datagrid-mask-msg\"></div>").html("正在添加...").appendTo("body").css({
								display:"block",
								left:($(document.body).outerWidth(true) - 190) / 2,
								top:($(window).height() - 45) / 2
							});
							// 跳转页面
							window.setTimeout(function(){
								window.location.href="<%=request.getContextPath()%>/user/buy.do"; 
							}, 500);
						} else {
							top.$.messager.alert('警告',reply.result.message,'warning');
						}
					}
				);
			}
		};
		
	
		// 关闭
		self.close = function() {
			$("#terminal_user_add_dlg").dialog("close");
		};
	
		//--------------------------
		
		// 初始化
		$(document).ready(function(){
			if('<%=chance%>'=='0'){
				$.cookie('type',null,{path:"/"});
			} 
			if($.cookie('type')!=''){
				if($.cookie('type')=='trail'){ 						
				    laststep(); 
				    var chkObjs = document.getElementsByName("item"); 
	                for(var i=0;i<chkObjs.length;i++){
	                    if(chkObjs[i].value==($.cookie('item'))){ 
	                    	chkObjs[i].checked="checked";
	                    	break;
	                    }
	                }
				    chkObjs = document.getElementsByName("sysImageId"); 
	                for(var i=0;i<chkObjs.length;i++){
	                    if(chkObjs[i].value==($.cookie('sysImageId'))){ 
	                    	chkObjs[i].checked="checked"; 
	                    	break;
	                    }
	                }
				    chkObjs = document.getElementsByName("ports"); 
				    chkObjs[0].checked=false;
				    var ports = $.cookie('ports'); 
				    ports = ports.split(",");
				    for(var j=0;j<ports.length;j++){
		                for(var i=0;i<chkObjs.length;i++){
		                    if(chkObjs[i].value== decodeURIComponent(ports[j])){ 
		                    	chkObjs[i].checked="checked"; 
		                    }
		                }
				    	
				    }
				    radiochange();  
					checkboxchange();
				    getComfirmInfo();
					
				}
			}
			
               
			// 保存
			$("#put_into_cart").click(function() {
				self.save();
			}); 
			$("#toLogin").click(function() { 
			    var chkObjs = document.getElementsByName("item");
			    var item = "";
                for(var i=0;i<chkObjs.length;i++){
                    if(chkObjs[i].checked){
                    	item = chkObjs[i].value;
                        break;
                    }
                }
			    chkObjs = document.getElementsByName("sysImageId");
			    var sysImageId = "";
                for(var i=0;i<chkObjs.length;i++){
                    if(chkObjs[i].checked){
                    	sysImageId = chkObjs[i].value;
                        break;
                    }
                }
                var str=document.getElementsByName("ports");
                var objarray=str.length;
                var ports="";
                for (i=0;i<objarray;i++)
                {
                  if(str[i].checked == true)
                  {
                	  ports+=encodeURIComponent(str[i].value)+",";
                  }
                }   
				$.cookie('item',item,{expires:1,path:'/'});
				$.cookie('sysImageId',sysImageId,{expires:1,path:'/'});
				$.cookie('ports',ports,{expires:1,path:'/'});
				$.cookie('type','trail',{expires:1,path:'/'});
				var url = encodeURIComponent($("#url").val()+"/user/buy.do");  
				window.location.href="<%=request.getContextPath()%>/user.do?url="+url;
			}); 
			// 注销
			$("#logout_link").click(function(){
				logout();
			});  
		});
	};

	// 试用
	function getTrail() { 		
		var formData = $.formToBean(trail_form);
		ajax.remoteCall("bean://paymentService:getTrail", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") { 
					// 跳转页面 
					window.setTimeout(function(){
				        window.location.href="<%=request.getContextPath()%>/user/successtrail.do";
					}, 500);
				} else {
					top.$.messager.alert('警告',reply.result.message,'warning'); 
				}
			}
		); 
	}
   	function deleteDetail(id,price){ 
	   $("#configId").val(id);
	   $("#detailPrice").val(price);
		var formData = $.formToBean(delete_form);
	   ajax.remoteCall("bean://paymentService:deleteDetailAndConfig", 
			[ formData],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") { 
					window.location.href="<%=request.getContextPath()%>/user/buy.do";  						 
				} else {
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	} 
	// 注销
	function logout(){
		ajax.remoteCall("bean://sysUserService:logout",
			[],
			function(reply)
			{
				if( reply.status=="exception" )
				{
					top.$.messager.alert('提示','会话超时，请重新登录','info',function(){
						window.location.href = "<%=request.getContextPath()%>/user.do";
					});
					//top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} 
				else if( reply.result.status=="success" )
				{
					// 登录成功
					$("<div class=\"datagrid-mask\"></div>").css({
						display:"block",
						width:"100%",
						height:"100%"
					}).appendTo("body"); 
					$("<div class=\"datagrid-mask-msg\"></div>").html("成功退出，正在跳转页面。。。").appendTo("body").css({
						display:"block",
						left:($(document.body).outerWidth(true) - 190) / 2,
						top:($(window).height() - 45) / 2
					});
					// 跳转页面
					window.setTimeout(function(){
						top.location.href = "<%=request.getContextPath()%>/user.do";
					}, 500);
				}
				else
				{
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	}
	function addPort(){
		var port = $("#port").val();
		var flag = true;
		$('*[name="ports"]').each(function(){
			 if($(this).val()==port){
					top.$.messager.alert('警告','端口已存在','warning'); 
					flag = false;
				 
			 }
		});
		if(flag==true){
			
			$("#showports").append("<a href='javascript:void(0);' onclick='deletePort($(this));'>"+port+ "</a>");
			var val = $("#ports").val();
		    val = val + port+",";
		    $("#ports").val(val);
			$("#port").val(""); 
		}
	} 
	
	function deletePort(self){
		var port = self.text();
		self.remove();
		var ports = $("#ports").val(); 
		ports = ports.replace(port+",","");
		$("#ports").val(ports);
	}
	</script>
</html>
