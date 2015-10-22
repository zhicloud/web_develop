<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>  
<%@page import="java.math.BigDecimal"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String userName = "";
	if(loginInfo!=null){
		userName = loginInfo.getAccount();
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>


<script src="javascript/page.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4"); 
var userName = '<%=userName%>';
$(document).ready(function(){
	init(6);
	if(userName!= ''){ 
		inituser(userName,0);
	}else{
		inituser();
	} 
	$(".jobtitlebar a").click(function(event){
		$(".jobtitlebar .active").removeClass("active");
		$(this).addClass("active");
		$(".jobs").css("display","none");
		$("#"+$(this).attr("name")).css("display","block");
	});
});
</script>
<!--[if IE 6]>
<script src="javascript/DD_belatedPNG.js"></script>
<script type="text/javascript">
	DD_belatedPNG.fix("*");
</script>
<![endif]-->
</head>

<body>
<div class="page">
  <div class="pagewhite">
    <div class="header pageheader">
        <div class="top">
			<a class="logo l" href="<%=request.getContextPath()%>/"> 
			<img src="<%=request.getContextPath()%>/image/logo_tf.png" width="184" height="34" alt="天府软件园创业场" /></a>
			<div id="beforelogin" class="user r">
				<a id="loginlink" href="javascript:void(0);" class="graylink">登录</a><span>|</span>
				<a id="reglink" href="javascript:void(0);">注册</a>
			</div>
			<div id="afterlogin" class="user r" style="display: none;">
				<img class="reddot" src="<%=request.getContextPath()%>/image/reddot.png" width="6" height="6" alt=" " />
				<a id="logoutlink" href="javascript:void(0);">注销</a><span>|</span>
				<a href="<%=request.getContextPath()%>/user.do" class="bluelink">我的云端</a>
			</div>
			<div class="nav r">
				<a href="<%=request.getContextPath()%>/" style="background: transparent;"><img id="nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/nav_1_i.png" width="20" height="20" alt="首页" style="padding: 8px 0" /> </a>
				<a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a>
				<a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
				<a href="<%=request.getContextPath()%>/solution.do">解决方案</a>
				<a href="<%=request.getContextPath()%>/help.do">帮助中心</a>
				<a href="<%=request.getContextPath()%>/aboutus.do">关于我们</a>
				<a href="#" style="display: none"></a>
				<a href="<%=request.getContextPath()%>/user.do?flag=login" style="display: none"></a>
				<a href="#" style="display:none;"></a>
				<a href="#" style="display: none">我的云端</a>
			</div>
		</div>
		<div class="subnav">
			<div class="box">1</div>
			<div class="box">2</div>
			<div class="box">3</div>
			<div class="box">4</div>
			<div class="box">5</div>
			<div class="box">6</div>
			<div class="box">7</div>
			<div class="box">8</div>
			<div class="box">9</div>
			<div class="box">
				<a href="#"><img id="nav_10_1" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_1_i.png" width="24" height="24" alt="概览" /><br />概览</a><a
					href="#"><img id="nav_10_2" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_2_i.png" width="24" height="24" alt="我的云主机" /><br />我的云主机</a><a
					href="#"><img id="nav_10_3" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_3_i.png" width="24" height="24" alt="我的云硬盘" /><br />我的云硬盘</a><a
					href="#"><img id="nav_10_4" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_4_i.png" width="24" height="24" alt="我的账户" /><br />我的账户</a><a
					href="#"><img id="nav_10_5" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_5_i.png" width="24" height="24" alt="操作日志" /><br />操作日志</a><a
					href="#"><img id="nav_10_6" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_6_i.png" width="24" height="24" alt="意见反馈" /><br />意见反馈</a><a
					href="#"><img id="nav_10_7" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_7_i.png" width="24" height="24" alt="文件夹" /><br />文件夹</a>
			</div>
		</div>
	</div>
    <div class="pagemain">
      <div class="box page7" style="height:600px;">
        <div class="titlebar" style="padding-top:24px">
          <div class="l">关于我们</div>
          <div class="r"><a href="aboutus.do">公司简介</a>　|　<a href="job.do" class="active">加入我们</a></div>
          <div class="clear"></div>
        </div>
        <div style="position: relative;width:960px; height:288px; margin:122px auto 0 auto; background: url(image/job_banner.png) no-repeat left top"><a href="mailto:hr@zhicloud.com" style="display:block; width:150px; height:30px; position:absolute; top:200px;left:0;">&nbsp;</a></div>
      </div>
      <div class="box" style="padding:60px 0;"><img src="image/job_desc.png" width="766" height="128" /></div>
      <div class="jobtitlebar"> <a name="job2" href="javascript:void(0);" class="active">技术类</a> <a name="job4" href="javascript:void(0);">产品类</a> <a name="job5" href="javascript:void(0);">运营类</a> <a  name="job3" href="javascript:void(0);">运维类</a> <a name="job1" href="javascript:void(0);">综合类</a></div>
      <div class="box" style="width:765px; padding:10px 0 0 0; text-align:right;"><a href="mailto:hr@zhicloud.com"><img src="image/job_mail.png" width="261" height="20" /></a></div>
      <div class="jobs"   id="job1">
        <div class="job">
          <div class="jobname"><b>前台</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都<br />
            <strong>工作内容</strong>：<br />
            全面负责公司办公室日常行政各方面事项，担任公司前台；<br />
            负责公司内部文档整理；<br />
            协助做好日常人事行政事务处理；<br />
            完成上级领导交办的其他事情；<br />
            安排约会、会议室及差旅预定；<br />
            <strong>职位要求</strong>：<br />
            具有大专以上学历，形象良好，气质佳，反映灵活；<br />
            熟练运用OFFICE办公软件，思维严谨，办事高效；<br />
            条理清晰，擅言谈；<br />
            有责任心、良好的沟通能力；<br />
            性格开朗，吃苦耐劳、具有团队合作精神；<br />
            有良好文字功底优先考虑；<br />
            <br />
          </p>
          <p>公司上班时间为周一至周五 9：00-17:30 双休 国家法定节假日都休<br />
            晋升空间大、机会多，愿意从基础做起，有服务意识、良好心态、积极主动的优先。<br />
          </p>
        </div>
        <div class="job">
        <div class="jobname"><b>总经理助理</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都<br />
            <strong>工作内容</strong>：<br />
            负责总经理的日程安排；<br />
            负责来访嘉宾接待、商务随行等；<br />
            在总经理领导下负责办公室的全面工作，努力作好总经理的参谋助手，起到承上启下的作用，认真做到全方位服务；<br />
            在总经理领导下负责企业具体管理工作的布置、实施、检查、督促、落实执行情况；<br />
            协助总经理作好经营服务各项管理并督促、检查落实贯彻执行情况；<br />
            协助总经理调查研究、了解公司经营管理情况并提出处理意见或建议，供总经理决策；<br />
            <strong>职位要求</strong>：<br />
            形象气质佳，有3年以上IT行业助理岗位工作经验；<br />
            本科以上学历，文秘、行政等相关专业；<br />
            工作细致认真、谨慎细心、有条理性、责任心强；<br />
            具有良好的人际沟通、协调能力、团队意识强，保密意识强；<br />
            人品善良，性格开朗、直率；责任心、事业心强，能承受工作压力，团队协作能力；<br />
            <br />
          </p>
        </div>
        <div class="job"  style="border-bottom:none;">
          <div class="jobname"><b>融资经理</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都<br />
            <strong>工作内容</strong>：<br />
            负责公司与金融机构,投资机构,风险投资机构的联络、接洽;<br />
            根据公司需要，协助制定资金计划，并且针对计划，多渠道安排资金到位；<br />
            负责为公司融资项目寻找符合条件的资金方；<br />
            负责公司融资渠道的开发、维护；<br />
            为公司项目融资方案提出合理化建议；<br />
            完成上级交办的任务。<br />
            <strong>职位要求</strong>：<br />
            本科及以上学历，投资、金融或其他经济类相关专业；<br />
            具有3年以上银行、基金、第三方理财机构等相关工作经验者优先；<br />
            具有IT行业融资经验的优先；<br />
            具有丰富的融资渠道和业内广泛的人际关系；<br />
            具有良好的融资分析能力和判断能力；<br />
            金融知识扎实，熟练掌握融资流程和专项业务知识；<br />
            有较强的谈判技能，具备良好的沟通能力；<br />
            善于处理复杂的人际关系，能够承受较强的工作压力。<br />
            <br />
          </p>
        </div>
      </div>
      <div class="jobs" id="job2" style="display:block"><div class="job">
      	<div class="jobname"><b>C++研发工程师</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都<br />
            <strong>工作内容</strong>：<br />
            产品研发、测试；<br />
            产品文档编写；<br />
            内部培训；<br />
            现场问题支持，必要时进行客户交流；<br />
            <strong>职位要求</strong>：<br />
            计算机、通讯等相关专业，本科以上学历；<br />
            熟练掌握C++开发，熟悉多线程与socket网络通讯开发；<br />
            熟悉linux操作系统使用，熟练掌握系统安装、C++环境配置编译等技能；<br />
            需有两年以上linux操作系统下C++网络/多线程应用开发 经验；<br />
            拥有云计算或者大型网络通讯平台开发经验优先；<br />
            有大型互联网、金融系统、大型流媒体、视频监控系统研发经验优先；<br />
            会使用python进行快速原型开发优先；<br />
            具有独立的工作能力、团队协作能力及良好的沟通能力；<br />
            <br />
          </p>
        </div>
        <div class="job">
        <div class="jobname"><b>Web研发工程师</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都<br />
            <strong>工作内容</strong>：<br />
            web前端产品、web服务接口研发；<br />
            公司网站开发、运营与维护；<br />
            产品文档编写与内部培训；<br />
            现场问题支持，必要时进行客户交流；<br />
            <strong>职位要求</strong>：<br />
            计算机、通讯等相关专业，全日制本科以上学历；<br />
            精通HTML、CSS、JavaScript、jQuery等前台相关技术，熟悉W3C网页标准；<br />
            熟悉java编程语言以及spring、mybatis等java开源框架，对于互联网领域常规框架、接口与协议有深刻理解；<br />
            精通mysql关系型数据库，了解mysql的调优技巧，能熟练编写sql；<br />
            2年以上大型商业网站相关研发、运维经验；<br />
            有一定架构能力和算法能力，有良好编码规范；<br />
            良好的学习能力和沟通能力，追求完美，有工作激情，能在较大强度下工作；<br />
            <br />
          </p>
        </div>
        <div class="job">
          <div class="jobname"><b>移动客户端研发工程师</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都<br />
            <strong>工作内容</strong>：<br />
            ios/android客户端研发；<br />
            产品文档编写与内部培训；<br />
            现场问题支持，必要时进行客户交流；<br />
            <strong>职位要求</strong>：<br />
            计算机、通讯等相关专业，全日制本科以上学历；<br />
            2年以上ios/android商业软件研发、运维经验，深刻理解移动客户端软件及服务端开发特点；<br />
            精通各平台常用开发框架及功能库，能够根据公司需求迅速进行移动客户端开发；<br />
            对网络编程、多线程有一定了解，能够与后台服务器配合完成应用层通讯交互；<br />
            有一定架构能力和算法能力，有良好编码规范；<br />
            良好的学习能力和沟通能力，追求完美，有工作激情，能在较大强度下工作；<br />
            拥有云计算、云存储相关研发经验优先；<br />
            应聘者需提供自己担当主要开发工作的客户端产品界面截图供参考；<br />
            <br />
          </p>
        </div>
        <div class="job"  style="border-bottom:none;">
          <div class="jobname"><b>测试工程师</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都<br />
            <strong>工作内容</strong>：<br />
            负责公司项目/产品的测试工作，包含需求分析、测试方案编写、环境搭建、用例编写、测试执行等；<br />
            对Bug进行跟踪处理，确保产品高质量交付；<br />
            编写自动化脚本，涉及Web以及底层平台，熟悉python；<br />
            编写测试报告、产品文档，做好知识传递和产品交付；<br />
            技术支持，能快速分析解决产品问题。<br />
            <strong>职位要求</strong>：<br />
            计算机、通讯等相关专业，本科以上学历；<br />
            熟练掌握软件测试理论、测试过程设计及测试用例设计方法，熟悉常用测试工具；<br />
            至少熟悉一种自动化语言(python最佳),能熟练使用自动化工具进行自动化测试；<br />
            熟悉linux操作系统，熟练掌握常用linux命令；<br />
            具备发现和解决问题的能力，具备质量缺陷分析、判断能力；<br />
            严谨细致，具有出色的独立工作能力及团队合作精神，有良好的沟通能力；<br />
            拥有大型网络通讯平台测试经验优先。<br />
            <br />
          </p>
        </div>
      </div>
      <div class="jobs" id="job3">
      	<div class="job">
          <div class="jobname"><b>网络工程师</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都<br />
            <strong>工作内容</strong>：<br />
            负责云数据中心网络结构的方案设计和实施；<br />
            负责云数据中心网络基础设施的配置及维护；<br />
            负责网络虚拟化技术的实现；<br />
            制定网络基础架构的运作流程和政策，编制相关的技术文档；<br />
            与其它IT团队和其它部门合作，共同进行新技术研究和测试；<br />
            配合销售，售前对相关项目给出网络及安全部署架构，根据客户相关情况进行项目网络架构规划；<br />
            配合项目经理实施与客户进行沟通对网络产品进行评估与建议。<br />
            <strong>职位要求</strong>：<br />
            本科及以上学历，计算机相关专业；<br />
            5年以上工作经验，有数据中心日常运营、维护经验优先考虑；<br />
            熟悉各种网络及系统的安全防护措施；<br />
            熟悉网络路由器、交换机的配置及维护；<br />
            熟悉常用的系统和网络测试工具；<br />
            了解主流网络产品，熟悉Cisco、H3C、Juniper等主流厂商的路由器、交换机、防火墙等设备；<br />
            熟悉VMware、Xen、Hpyer-V、KVM等业界主流的虚拟化平台技术，有实际的项目经验者优先考虑；<br />
            具有Cisco、华为、H3C等厂商发的资质认证证书者优先考虑。<br />
            <br />
          </p>
          <p>公司上班时间为周一至周五 9：00-17:30 双休 国家法定节假日都休<br />
            晋升空间大、机会多，愿意从基础做起，有服务意识、良好心态、积极主动的优先。<br />
          </p>
        </div>
        <div class="job">
        <div class="jobname"><b>高级售前工程师</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都、广州、上海<br />
            <strong>工作内容</strong>：<br />
            售前技术支持，配合与支持销售活动，协作销售人员与客户交流，向客户讲解公司产品和技术方案，分析与理解客户需求，并根据客户需求制定相应的技术方案；<br />
            投标技术支持，协作销售人员参与竞标，并提供技术支持，负责向相关人员介绍产品技术参数以及投标技术文件的应答，为项目投标做好充分的技术材料准备；<br />
            为客户提供解决方案；<br />
            <strong>职位要求</strong>：<br />
            本科学历，理工科等相关专业背景；<br />
            计算机、信息、通信或相关专业；<br />
            5年以上相关领域工作经验；至少3年云计算技术管理经验；<br />
            熟悉售前工作流程，能深入挖掘用户需求，具备良好的沟通表达能力，以及方案建议书写作技能；<br />
            熟悉云计算技术架构及安装部署技术流程；<br />
            硬件知识：熟悉x86服务器、存储、交换机等厂家和设备；<br />
            软件知识：熟悉各种操作系统（windows、Linux等），且能够安装和部署在Linux及此平台上的中间件、数据库和其它应用；<br />
            网络知识：熟悉网络的各种形态，熟悉TCP/IP等各种协议；<br />
            虚拟化知识：熟悉主流虚拟化产品，如：vmware、citrix等虚拟化技术；<br />
            了解云计算相关技术，如：分布式计算、分布式存储等技术；<br />
            <br />
          </p>
        </div>
        <div class="job"  style="border-bottom:none;">
          <div class="jobname"><b>高级实施工程师</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都、广州、上海<br />
            <strong>工作内容</strong>：<br />
            售前技术支持，配合与支持销售活动，协作销售人员与客户交流，向客户讲解公司产品和技术方案，分析与理解客户需求，并根据客户需求制定相应的技术方案；<br />
            投标技术支持，协作销售人员参与竞标，并提供技术支持，负责向相关人员介绍产品技术参数以及投标技术文件的应答，为项目投标做好充分的技术材料准备；<br />
            为客户提供解决方案；<br />
            客户现场POC环境搭建，协作客户验证平台的稳定性、可靠性、以及可扩展性等；<br />
            负责已签项目的工程实施，包括软硬件的安装、调试等事宜；<br />
            为客户提供售后技术支持服务，包括服务器、存储、网络设备、虚拟化以及云平台产品的支持；<br />
            <strong>职位要求</strong>：<br />
            本科学历，理工科等相关专业背景；<br />
            计算机、信息、通信或相关专业；<br />
            5年以上相关领域工作经验；至少3年云计算技术管理经验；<br />
            熟悉云计算技术架构及安装部署技术流程；<br />
            硬件知识：熟悉x86服务器、存储、交换机等厂家和设备；<br />
            软件知识：熟悉各种操作系统（windows、Linux等），且能够安装和部署在Linux及此平台上的中间件、数据库和其它应用；<br />
            网络知识：熟悉网络的各种形态，熟悉TCP/IP等各种协议；<br />
            虚拟化知识：熟悉主流虚拟化产品，如：vmware、citrix等虚拟化技术；<br />
            了解云计算相关技术，如：分布式计算、分布式存储等技术；<br />
            具有强烈的责任感和很好的团队合作精神，具体较好的自学能力，能够承受工作压力；<br />
            <br />
          </p>
        </div>
      </div>
      <div class="jobs" id="job4">
      	<div class="job">
          <div class="jobname"><b>UI设计师</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都<br />
            <strong>工作内容</strong>：<br />
            负责公司客户端产品的UI设计（后台前段界面/网站界面/图标设计等）；<br />
            参与前期产品规划及视觉研究；<br />
            分析业务需求、并加以分解归纳出产品人机交互界面体验设计需求；<br />
            <strong>职位要求</strong>：<br />
            一年以上UI界面制作经验；有Web界面制作经验佳；<br />
            对色彩感觉敏锐，色感好，熟悉平面设计的色彩运用;熟练掌握Photoshop、Illustrator等图形设计软件；<br />
            有良好的沟通能力，团队合作精神，能较好地与产品、研发人员沟通；<br />
            乐于研究设计新的流行趋势并付诸实践；<br />
            有丰富的设计理论支撑，感性思考，理性设计；<br />
            求职需先提供作品集；<br />
            有手绘/插画美术功底为佳；<br />
            <br />
          </p>
        </div>
        <div class="job">
        <div class="jobname"><b>前端开发工程师</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都、广州、上海<br />
            <strong>工作内容</strong>：<br />
            Web前端表现层开发；<br />
            JavaScript程序模块开发，通用类库、框架编写；<br />
            配合UI设计和后台开发人员实现产品界面和功能；<br />
            通过各种前端技术手段，提高用户体验并满足性能要求；<br />
            <strong>职位要求</strong>：<br />
            扎实的计算机基础,有至少1年的Web前端开发经验；<br />
            熟悉HTML、CSS等前端技术，熟悉页面架构和布局；<br />
            熟悉JavaScript，熟悉jQuery等框架；<br />
            有Mobile端HTML5项目经验者优先；<br />
            熟悉W3C标准，对表现与数据分离、Web语义化等有深刻理解；<br />
            对后端语言(C/C++/PHP/Java等)有一定了解者优先；<br />
            有良好的技术视野和规划能力；<br />
            良好的沟通与表达能力、思路清晰，较强的动手能力与逻辑分析能力；<br />
            <br />
          </p>
        </div>
        <div class="job"  style="border-bottom:none;">
          <div class="jobname"><b>产品经理</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都<br />
            <strong>工作内容</strong>：<br />
            负责公司云产品的发展和规划设计；<br />
            负责产品的架构设计；<br />
            负责产品的优化和改善工作；<br />
            根据公司总体战略，规划公司产品的技术构架，整合资源，进行项目的整体策划；<br />
            负责与研发中心领导共同对团队的工作标准、制度、项目管理规范、信息安全性等制度建设及实施；<br />
            制定总体及阶段性的业务发展策略；<br />
            <strong>职位要求</strong>：<br />
            本科及以上学历，计算机相关专业；<br />
            有多年云技术市场与开发经验，对云存储、云调度等平台管理有较强认识；<br />
            具有较强的系统架构设计能力；<br />
            精通TCP/IP协议，熟悉IDC网络架构，熟悉运营商网络结构；<br />
            具有良好的沟通能力，良好的团队协作精神；<br />
            具备熟练的技术调研能力并能完成相应技术报告，具有良好的书面和口头表达能力，较高的文档撰写水平；<br />
            热爱研发工作，思维缜密，逻辑性强，工作细心；<br />
            <br />
          </p>
        </div>
      </div>
      <div class="jobs" id="job5">
      	<div class="job">
          <div class="jobname"><b>运营总监</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都<br />
            <strong>工作内容</strong>：<br />
            负责公司总体运营计划规划与落实，完成公司年度运营目标；<br />
            整合公司及各类行业资源，监督并协助该计划的完成；<br />
            负责公司云计算中心的策划、营销、市场推广；<br />
            对公司云产品进行有效的运营，行使对运营工作的指导、监督和管理权力；<br />
            按照公司业务经营发展需要，协同其他管理部门，进行有针对性的流程优化工作；分析客户的需求与市场环境，优化运营战略；<br />
            对中心战略、项目进度、周期、人员配置、商务协调及营收进行管理，完成运营的业绩指标。<br />
            <strong>职位要求</strong>：<br />
            5年以上IT、科技行业管理者从业经验；<br />
            有多年的数据中心（IDC）营销、策划、推广工作经验者优先；<br />
            熟悉国内外云服务运营；<br />
            熟悉通讯服务市场，有较为丰富IT行业运营思想，有相应的资源；<br />
            具备较强的团队管理能力、项目管理能力、队伍培训能力；<br />
            具有较强的领导和组织协调能力、判断力与决策能力，计划与执行能力，具备突出的团队合作精神；<br />
            具备较好的心理素质和工作承压能力，认真、踏实，责任心强；<br />
            具备对工作流程的塑造和改造的能力。<br />
            <br />
          </p>
        </div>
      	<div class="job">
          <div class="jobname"><b>品牌策划总监</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都<br />
            <strong>工作内容</strong>：<br />
            负责企业品牌微博与微信等媒体平台的推广、企业文化传播建设等工作；<br />
            负责公司官网的日常维护；<br />
            负责公司新闻稿件的撰写；<br />
            负责线上活动的策划推广；<br />
            负责媒体关系的拓展与维护；<br />
            负责危机公关处理。<br />
            <strong>职位要求</strong>：<br />
            三年以上IT行业新媒体运营工作经验，熟悉互联网和新媒体的运营体系；<br />
            市场营销、公共关系、新闻等相关专业；<br />
            熟悉互联网语言，有新闻类独立撰稿的能力；<br />
            具备良好的口头与书面表达能力，执行力与抗压性好；<br />
            具备较强的服务意识；<br />
            <br />
          </p>
        </div>
        <div class="job">
        <div class="jobname"><b>产品/品牌策划经理</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都<br />
            <strong>工作内容</strong>：<br />
            负责企业品牌微博与微信等媒体平台的推广、企业文化传播建设等工作；<br />
            负责公司官网的日常维护；<br />
            负责公司新闻稿件的撰写；<br />
            负责线上活动的策划推广；<br />
            负责媒体关系的拓展与维护；<br />
            负责危机公关处理。<br />
            <strong>职位要求</strong>：<br />
            一年以上IT行业新媒体运营工作经验，熟悉互联网和新媒体的运营体系；<br />
            市场营销、公共关系、新闻等相关专业；<br />
            熟悉互联网语言，有新闻类独立撰稿的能力；<br />
            具备良好的口头与书面表达能力，执行力与抗压性好；<br />
            具备较强的服务意识；<br />
            <br />
          </p>
        </div>
        <div class="job">
          <div class="jobname"><b>媒体公关</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都、广州<br />
            <strong>工作内容</strong>：<br />
            负责公司产品与服务的内容合作、公关传播等，建立所负责项目的媒体资源库，能协助进行危机管理，能够与相关媒体进行有效沟通；<br />
            市场宣传稿件策划及撰写，组织和执行，建立稳固发展的市场及媒体关系；<br />
            负责媒体投放计划制定，投放中的监督控制及后期投放合理性分析；<br />
            负责公司媒体预算制定；<br />
            组织有关公司自身产品、服务及相关合作伙伴、云计算行业相关的媒体推广活动工作，包括节假日活动等；<br />
            制定公司云计算产品与服务的媒体广告投放策略，对于广告投放效果负责；<br />
            负责公司产品的网络广告投放策划，购买，执行和数据跟踪分析；<br />
            组织媒体传播的策划，撰稿及发布工作；<br />
            <strong>职位要求</strong>：<br />
            广告,市场营销,新闻传播本科学历；<br />
            三年以上公关、媒体、广告的工作经验，拥有丰富的科技媒体资源（如infoq、Techweb、36氪等）；<br />
            具备敏锐的市场嗅觉,思路清晰,具有分析和制定策略的能力,能根据市场需求进行公关媒体开发,提出媒体整合策略，能够联合相关重要媒体共同推出具有影响力的活动；<br />
            有较强沟通能力和谈判技巧及项目管理能力；<br />
            良好的商业合作意识，协调沟通能力以及判断能力，能独立完成媒体合作计划；<br />
            具有策划和撰稿能力，擅长媒介数据分析及市场信息研究，并能根据传播策略撰写媒介分析报告和媒介策划；<br />
            <br />
          </p>
        </div>
        <div class="job"  style="border-bottom:none;">
        <div class="jobname"><b>网络推广</b></div>
          <div class="clear"></div>
          <br />
          <p><strong>工作地点</strong>：成都、广州<br />
            <strong>工作内容</strong>：<br />
            根据公司实际业务需求来策划、制定、实施线上宣传推广活动方案；<br />
            公司网站优化、关键字优化、内外部链接优化、前端代码优化、图片优化等，提升网站自然流量；<br />
            负责网络营销活动及网络广告投放的效果监控，并做相应的数据分析形成阶段性数据报告，并提出后续优化方案；<br />
            <strong>职位要求</strong>：<br />
            大学专科以上学历，该领域2年以上工作经验；<br />
            熟悉网络推广、搜索引擎、内容优化等各种网络营销方法；<br />
            有过成功的网络广告投放经验；<br />
            具备良好的沟通能力及文案写作能力。<br />
            能结合公司产品和服务，挖掘创新，增加网站曝光量；<br />
            了解邮件、社区论坛、社交网络等常用的网络推广方法；<br />
            了解SEO理论知识，熟悉Baidu、google等搜索引擎的优化；<br />
            <br />
          </p>
        </div>
      </div>
    </div>
    <div class="footer">
		<div class="box">
			<div class="sitemap">
				产品<br />
				<a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a><br />
				<a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
			</div>
			<div class="sitemap">
				解决方案<br />
				<a href="<%=request.getContextPath()%>/solution.do">云管理平台</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云存储</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云桌面</a>
			</div>
			<div class="sitemap">
				帮助中心<br />
				<a href="<%=request.getContextPath()%>/help.do">常见问题</a><br />
				<a href="<%=request.getContextPath()%>/help.do">账户相关指南</a><br />
				<a href="<%=request.getContextPath()%>/help.do">云主机指南</a>
			</div>
			<div class="sitemap">
				关于我们<br />
				<a href="<%=request.getContextPath()%>/aboutus.do">关于致云</a><br />
				<a href="<%=request.getContextPath()%>/job.do">加入我们</a><br />
				<a href="<%=request.getContextPath()%>/aboutus.do">联系我们</a>
			</div>
			<div class="sitemap" style="width: 100px;">
				关注我们<br />
				<a href="javascript:void(0);">微信公众号</a><br />
				<img src="<%=request.getContextPath()%>/image/weixin.gif" width="70" height="70" />
			</div>
			<div class="sitemap">
				&nbsp;<br />
				<a href="http://weibo.com/zhicloud" target="_blank">新浪微博</a><br />
				<img src="<%=request.getContextPath()%>/image/weibo.gif" width="70" height="70" />
			</div>
			<div class="hotline">
				<img src="<%=request.getContextPath()%>/image/tel.png" width="30" height="30"
					style="vertical-align: middle" /> 客服热线<br />
				<span style="font-size: 22px; color: #595959;">4000-212-999</span><br />
				<span>客服服务时间：7X24小时</span>
			</div>
			<div class="clear"></div>
			<div class="copyright">
				Copyright &copy; 2014 <a href="http://www.zhicloud.com"
					target="_blank">致云科技有限公司</a>, All rights reserved.
				蜀ICP备14004217号-1
			</div>
		</div> 
	</div>
  </div>
</div>
<div class="pageright">
  <iframe id="loginiframe" src="<%=request.getContextPath() %>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  <iframe id="regiframe" src="<%=request.getContextPath() %>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
</div>
</div>
</body>
</html>