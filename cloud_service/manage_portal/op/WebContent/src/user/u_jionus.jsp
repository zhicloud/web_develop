<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_jionus.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>加入我们 -- 致云 ZhiCloud</title>
	<link rel="shortcut icon" href="../common/img/favicon.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../common/css/global.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="css/usersite.css" media="all"/>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/dep/jquery.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
</head>
<body>
<div class="g-doc">
	<!-- header -->
	<%@ include file="../common/tpl/u_header.jsp"%>
	<!-- /header -->
	<div class="f-cb"></div>
	<div class="g-ju-bd">
		<div class="ju-sec1">
			<div class="wrap">
				<h3>招聘流程</h3>
				<div class="ju-list">
					<div class="ju-iteam ju-first"><img alt="流程一" src="img/ju_first.png"/><p>将简历投递到我们招聘邮箱<br/>hr@zhicloud.com</p></div>
					<div class="ju-iteam ju-second"><img alt="流程二" src="img/ju_second.png"/><p>经过筛选，获得面试邀请。</p></div>
					<div class="ju-iteam ju-third"><img alt="流程三" src="img/ju_third.png"/><p>如有下一步，<br/>衷心高兴向你发出Offer</p></div>
				</div>
			</div>
		</div>
		<div class="ju-sec2">
			<div class="wrap">
				<ul>
					<li><span class="icon-wxyj f-db"></span><label class="f-db">五险一金</label></li>
					<li><span class="icon-zypx f-db"></span><label class="f-db">专业培训</label></li>
					<li><span class="icon-dxxj f-db"></span><label class="f-db">带薪休假</label></li>
					<li><span class="icon-ygss f-db"></span><label class="f-db">员工宿舍</label></li>
					<li><span class="icon-cdkf f-db"></span><label class="f-db">茶点咖啡</label></li>
				</ul>
			</div>
		</div>
		<div class="ju-sec3" id="ju_detail">
			<div class="ju-nav" id="job-nav">
				<div class="wrap">
					<div class="m-tabs">
				        <ul>
				            <li class="current"><a href="javascript:;">技术类</a></li>
				            <li><a href="javascript:;">产品类</a></li>
				            <li><a href="javascript:;">运营类</a></li>
				            <li><a href="javascript:;">运维类</a></li>
				            <li><a href="javascript:;">综合类</a></li>
				        </ul>
				    </div>
				</div>
			</div>
			<div class="f-cb"></div>
			<div class="ju-recruit">
				<div class="wrap">
					<div class="m-container">
						<div class="m-content current">
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>资深C++研发工程师</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 云主机管理平台、分布式块存储、SDN等分布式计算技术研发；</dd>
										<dd>2. 云计算相关技术预研和产品路线规划；</dd>
										<dd>3. 技术问题支持及排查；</dd> 
									</dl>
									<dl>
										<dt>开发环境：</dt>
										<dd>linux/gcc/C++</dd> 
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 对多线程工作机制和原理有清晰认识，精通现代开发语言中的线程模型，能够熟练使用&lt;thread&gt;/&lt;mutex&gt;
/&lt;condition_variable&gt;等标准库根据设计要求进行高性能服务开发，能够准确定位系统性能瓶颈并优化解决。；</dd>
										<dd>2. 熟悉网络通讯原理，对网络交换、路由、NAT等常见网络概念有清醒认识，能够熟练使用epoll等基础框架根据设计要求开发高性能网络通讯组件，能够进行网络通讯故障分析、排查，能够自行设计私有通讯协议或者根据RFC文档实现协议栈；</dd>
										<dd>3. 熟悉常用数据结构和设计模式，能够根据需要进行数据结构和流程设计，能够对现有问题进行定位和优化；</dd>
										<dd>4. 有电信、网络通讯、视频安防、网络存储或其他大型软件系统设计及核心模块开发经验，要求8年以上linux/c++开发经验；
思路清晰，逻辑严密，能够快速分析定位并解决各种复杂技术问题；</dd>
										<dd>5. 思路清晰，逻辑严密，能够快速分析定位并解决各种复杂技术问题；</dd>
										<dd>6. 能够有效管理时间，主动积极沟通，合理制定工作计划，确保任务保质保量完成，有团队管理或者创业经验优先；</dd>
										<dd>7. 对技术创新和改进充满热情，勇于挑战未知领域并不断自我折腾，拥有geek精神的优先；</dd>
										<dd>8. 熟悉云计算技术，期望用自主研发的本土技术改变云计算甚至传统IT领域格局的优先；</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都、广州</dd>
									</dl>
								</div>
							</div>
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>Web研发工程师</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. web前端产品、web服务接口研发；</dd>
										<dd>2. 公司网站开发、运营与维护；</dd>
										<dd>3. 产品文档编写与内部培训；</dd>
										<dd>4. 现场问题支持，必要时进行客户交流</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 计算机、通讯等相关专业，全日制本科以上学历；</dd>
										<dd>2. 精通HTML、CSS、JavaScript、jQuery等前台相关技术，熟悉W3C网页标准；</dd>
										<dd>3. 熟悉java编程语言以及spring、mybatis等java开源框架，对于互联网领域常规框架、接口与协议有深刻理解；</dd>
										<dd>4. 精通mysql关系型数据库，了解mysql的调优技巧，能熟练编写sql；</dd>
										<dd>5. 2年以上大型商业网站相关研发、运维经验；</dd>
										<dd>6. 有一定架构能力和算法能力，有良好编码规范；</dd>
										<dd>7. 良好的学习能力和沟通能力，追求完美，有工作激情，能在较大强度下工作；</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都</dd>
									</dl>
								</div>
							</div>
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>移动客户端研发工程师</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. ios/android客户端研发；</dd>
										<dd>2. 产品文档编写与内部培训；</dd>
										<dd>3. 现场问题支持，必要时进行客户交流；</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 计算机、通讯等相关专业，全日制本科以上学历；</dd>
										<dd>2. 2年以上ios/android商业软件研发、运维经验，深刻理解移动客户端软件及服务端开发特点 </dd>
										<dd>3. 精通各平台常用开发框架及功能库，能够根据公司需求迅速进行移动客户端开发；</dd>
										<dd>4. 对网络编程、多线程有一定了解，能够与后台服务器配合完成应用层通讯交互；</dd>
										<dd>5. 有一定架构能力和算法能力，有良好编码规范；</dd>
										<dd>6. 良好的学习能力和沟通能力，追求完美，有工作激情，能在较大强度下工作；</dd>
										<dd>7. 拥有云计算、云存储相关研发经验优先；</dd>
										<dd>8. 应聘者需提供自己担当主要开发工作的客户端产品界面截图供参考；</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都</dd>
									</dl>
								</div>
							</div>
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>测试工程师</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 负责公司项目/产品的测试工作，包含需求分析、测试方案编写、环境搭建、用例编写、测试执行等；</dd>
										<dd>2. 对Bug进行跟踪处理，确保产品高质量交付；</dd>
										<dd>3. 编写自动化脚本，涉及Web以及底层平台，熟悉python；</dd>
										<dd>4. 编写测试报告、产品文档，做好知识传递和产品交付；</dd>
										<dd>5. 技术支持，能快速分析解决产品问题。</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 计算机、通讯等相关专业，本科以上学历；</dd>
										<dd>2. 熟练掌握软件测试理论、测试过程设计及测试用例设计方法，熟悉常用测试工具；</dd>
										<dd>3. 至少熟悉一种自动化语言(python最佳),能熟练使用自动化工具进行自动化测试；</dd>
										<dd>4. 熟悉linux操作系统，熟练掌握常用linux命令；</dd>
										<dd>5. 具备发现和解决问题的能力，具备质量缺陷分析、判断能力；</dd>
										<dd>6. 严谨细致，具有出色的独立工作能力及团队合作精神，有良好的沟通能力；</dd>
										<dd>7.拥有大型网络通讯平台测试经验优先。</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都</dd>
									</dl>
								</div>
							</div>
						</div>
						<div class="m-content">
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>UI设计师</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 负责公司客户端产品的UI设计（后台前段界面/网站界面/图标设计等）；</dd>
										<dd>2. 参与前期产品规划及视觉研究；</dd>
										<dd>3. 分析业务需求、并加以分解归纳出产品人机交互界面体验设计需求；</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 一年以上UI界面制作经验；有Web界面制作经验佳；</dd>
										<dd>2. 对色彩感觉敏锐，色感好，熟悉平面设计的色彩运用;熟练掌握Photoshop、Illustrator等图形设计软件；</dd>
										<dd>3. 有良好的沟通能力，团队合作精神，能较好地与产品、研发人员沟通；</dd>
										<dd>4. 乐于研究设计新的流行趋势并付诸实践；</dd>
										<dd>5. 有丰富的设计理论支撑，感性思考，理性设计；</dd>
										<dd>6. 求职需先提供作品集；</dd>
										<dd>7. 有手绘/插画美术功底为佳；</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都</dd>
									</dl>
								</div>
							</div>
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>前端开发工程师</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. Web前端表现层开发；</dd>
										<dd>2. JavaScript程序模块开发，通用类库、框架编写；</dd>
										<dd>3. 配合UI设计和后台开发人员实现产品界面和功能；</dd>
										<dd>4. 通过各种前端技术手段，提高用户体验并满足性能要求；</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 扎实的计算机基础,有至少1年的Web前端开发经验；</dd>
										<dd>2. 熟悉HTML、CSS等前端技术，熟悉页面架构和布局；</dd>
										<dd>3. 熟悉JavaScript，熟悉jQuery等框架；</dd>
										<dd>4. 有Mobile端HTML5项目经验者优先；</dd>
										<dd>5. 熟悉W3C标准，对表现与数据分离、Web语义化等有深刻理解；</dd>
										<dd>6. 对后端语言(C/C++/PHP/Java等)有一定了解者优先；</dd>
										<dd>7. 有良好的技术视野和规划能力；</dd>
										<dd>8. 良好的沟通与表达能力、思路清晰，较强的动手能力与逻辑分析能力；</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都、广州、上海</dd>
									</dl>
								</div>
							</div>
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>产品经理</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 负责公司云产品的发展和规划设计；</dd>
										<dd>2. 负责产品的架构设计；</dd>
										<dd>3. 负责产品的优化和改善工作；</dd>
										<dd>4. 根据公司总体战略，规划公司产品的技术构架，整合资源，进行项目的整体策划；</dd>
										<dd>5. 负责与研发中心领导共同对团队的工作标准、制度、项目管理规范、信息安全性等制度建设及实施；</dd>
										<dd>6. 制定总体及阶段性的业务发展策略；</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 本科及以上学历，计算机相关专业；</dd>
										<dd>2. 有多年云技术市场与开发经验，对云存储、云调度等平台管理有较强认识；</dd>
										<dd>3. 具有较强的系统架构设计能力；</dd>
										<dd>4. 精通TCP/IP协议，熟悉IDC网络架构，熟悉运营商网络结构；</dd>
										<dd>5. 具有良好的沟通能力，良好的团队协作精神；</dd>
										<dd>6. 具备熟练的技术调研能力并能完成相应技术报告，具有良好的书面和口头表达能力，较高的文档撰写水平；</dd>
										<dd>7. 热爱研发工作，思维缜密，逻辑性强，工作细心；</dd>
										
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都</dd>
									</dl>
								</div>
							</div>
						</div>
						<div class="m-content">
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>运营总监</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 负责公司总体运营计划规划与落实，完成公司年度运营目标；</dd>
										<dd>2. 整合公司及各类行业资源，监督并协助该计划的完成；</dd>
										<dd>3. 负责公司云计算中心的策划、营销、市场推广；</dd>
										<dd>4. 对公司云产品进行有效的运营，行使对运营工作的指导、监督和管理权力；</dd>
										<dd>5. 按照公司业务经营发展需要，协同其他管理部门，进行有针对性的流程优化工作；分析客户的需求与市场环境，优化运营战略；</dd>
										<dd>6. 对中心战略、项目进度、周期、人员配置、商务协调及营收进行管理，完成运营的业绩指标。</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 5年以上IT、科技行业管理者从业经验；</dd>
										<dd>2. 有多年的数据中心（IDC）营销、策划、推广工作经验者优先；</dd>
										<dd>3. 熟悉国内外云服务运营；</dd>
										<dd>4. 熟悉通讯服务市场，有较为丰富IT行业运营思想，有相应的资源；</dd>
										<dd>5. 具备较强的团队管理能力、项目管理能力、队伍培训能力；</dd>
										<dd>6. 具有较强的领导和组织协调能力、判断力与决策能力，计划与执行能力，具备突出的团队合作精神；</dd>
										<dd>7. 具备较好的心理素质和工作承压能力，认真、踏实，责任心强；</dd>
										<dd>8. 具备对工作流程的塑造和改造的能力。</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都</dd>
									</dl>
								</div>
							</div>
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>品牌策划总监</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 负责企业品牌微博与微信等媒体平台的推广、企业文化传播建设等工作；</dd>
										<dd>2. 负责公司官网的日常维护；</dd>
										<dd>3. 负责公司新闻稿件的撰写；</dd>
										<dd>4. 负责线上活动的策划推广；</dd>
										<dd>5. 负责媒体关系的拓展与维护；</dd>
										<dd>6. 负责危机公关处理。</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 三年以上IT行业新媒体运营工作经验，熟悉互联网和新媒体的运营体系；</dd>
										<dd>2. 市场营销、公共关系、新闻等相关专业；</dd>
										<dd>3. 熟悉互联网语言，有新闻类独立撰稿的能力；</dd>
										<dd>4. 具备良好的口头与书面表达能力，执行力与抗压性好；</dd>
										<dd>5. 具备较强的服务意识；</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都</dd>
									</dl>
								</div>
							</div>
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>产品/品牌策划经理</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 负责企业品牌微博与微信等媒体平台的推广、企业文化传播建设等工作；</dd>
										<dd>2. 负责公司官网的日常维护；</dd>
										<dd>3. 负责公司新闻稿件的撰写；</dd>
										<dd>4. 负责线上活动的策划推广；</dd>
										<dd>5. 负责媒体关系的拓展与维护；</dd>
										<dd>6. 负责危机公关处理。</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 一年以上IT行业新媒体运营工作经验，熟悉互联网和新媒体的运营体系；</dd>
										<dd>2. 市场营销、公共关系、新闻等相关专业；</dd>
										<dd>3. 熟悉互联网语言，有新闻类独立撰稿的能力；</dd>
										<dd>4. 具备良好的口头与书面表达能力，执行力与抗压性好；</dd>
										<dd>5. 具备较强的服务意识；</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都</dd>
									</dl>
								</div>
							</div>
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>媒体公关</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 负责公司产品与服务的内容合作、公关传播等，建立所负责项目的媒体资源库，能协助进行危机管理，能够与相关媒体进行有效沟通；</dd>
										<dd>2. 市场宣传稿件策划及撰写，组织和执行，建立稳固发展的市场及媒体关系；</dd>
										<dd>3. 负责媒体投放计划制定，投放中的监督控制及后期投放合理性分析；</dd>
										<dd>4. 负责公司媒体预算制定；</dd>
										<dd>5. 组织有关公司自身产品、服务及相关合作伙伴、云计算行业相关的媒体推广活动工作，包括节假日活动等；</dd>
										<dd>6. 制定公司云计算产品与服务的媒体广告投放策略，对于广告投放效果负责；</dd>
										<dd>7. 负责公司产品的网络广告投放策划，购买，执行和数据跟踪分析；</dd>
										<dd>8. 组织媒体传播的策划，撰稿及发布工作；</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 广告,市场营销,新闻传播本科学历；</dd>
										<dd>2. 三年以上公关、媒体、广告的工作经验，拥有丰富的科技媒体资源（如infoq、Techweb、36氪等）；</dd>
										<dd>3. 具备敏锐的市场嗅觉,思路清晰,具有分析和制定策略的能力,能根据市场需求进行公关媒体开发,提出媒体整合策略，能够联合相关重要媒体共同推出具有影响力的活动；</dd>
										<dd>4. 有较强沟通能力和谈判技巧及项目管理能力；</dd>
										<dd>5. 良好的商业合作意识，协调沟通能力以及判断能力，能独立完成媒体合作计划；</dd>
										<dd>6. 具有策划和撰稿能力，擅长媒介数据分析及市场信息研究，并能根据传播策略撰写媒介分析报告和媒介策划；</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都、广州</dd>
									</dl>
								</div>
							</div>
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>网络推广</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 根据公司实际业务需求来策划、制定、实施线上宣传推广活动方案；</dd>
										<dd>2. 公司网站优化、关键字优化、内外部链接优化、前端代码优化、图片优化等，提升网站自然流量；</dd>
										<dd>3. 负责网络营销活动及网络广告投放的效果监控，并做相应的数据分析形成阶段性数据报告，并提出后续优化方案；</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 大学专科以上学历，该领域2年以上工作经验；</dd>
										<dd>2. 熟悉网络推广、搜索引擎、内容优化等各种网络营销方法；</dd>
										<dd>3. 有过成功的网络广告投放经验；</dd>
										<dd>4. 具备良好的沟通能力及文案写作能力。</dd>
										<dd>5. 能结合公司产品和服务，挖掘创新，增加网站曝光量；</dd>
										<dd>6. 了解邮件、社区论坛、社交网络等常用的网络推广方法；</dd>
										<dd>7. 了解SEO理论知识，熟悉Baidu、google等搜索引擎的优化；</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都</dd>
									</dl>
								</div>
							</div>
						</div>
						<div class="m-content">
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>网络工程师</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 负责云数据中心网络结构的方案设计和实施；</dd>
										<dd>2. 负责云数据中心网络基础设施的配置及维护；</dd>
										<dd>3. 负责网络虚拟化技术的实现；</dd>
										<dd>4. 制定网络基础架构的运作流程和政策，编制相关的技术文档；</dd>
										<dd>5. 与其它IT团队和其它部门合作，共同进行新技术研究和测试；</dd>
										<dd>6. 配合销售，售前对相关项目给出网络及安全部署架构，根据客户相关情况进行项目网络架构规划；</dd>
										<dd>7. 配合项目经理实施与客户进行沟通对网络产品进行评估与建议。</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 本科及以上学历，计算机相关专业；</dd>
										<dd>2. 5年以上工作经验，有数据中心日常运营、维护经验优先考虑；</dd>
										<dd>3. 熟悉各种网络及系统的安全防护措施；</dd>
										<dd>4. 熟悉网络路由器、交换机的配置及维护；</dd>
										<dd>5. 熟悉常用的系统和网络测试工具；</dd>
										<dd>6. 了解主流网络产品，熟悉Cisco、H3C、Juniper等主流厂商的路由器、交换机、防火墙等设备；</dd>
										<dd>7. 熟悉VMware、Xen、Hpyer-V、KVM等业界主流的虚拟化平台技术，有实际的项目经验者优先考虑；</dd>
										<dd>8. 具有Cisco、华为、H3C等厂商发的资质认证证书者优先考虑。</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都</dd>
									</dl>
								</div>
							</div>
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>高级售前工程师</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 售前技术支持，配合与支持销售活动，协作销售人员与客户交流，向客户讲解公司产品和技术方案，分析与理解客户需求，并根据客户需求制定相应的技术方案；</dd>
										<dd>2. 投标技术支持，协作销售人员参与竞标，并提供技术支持，负责向相关人员介绍产品技术参数以及投标技术文件的应答，为项目投标做好充分的技术材料准备；</dd>
										<dd>3. 为客户提供解决方案；</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 本科学历，理工科等相关专业背景；</dd>
										<dd>2. 计算机、信息、通信或相关专业；</dd>
										<dd>3. 5年以上相关领域工作经验；至少3年云计算技术管理经验；</dd>
										<dd>4. 熟悉售前工作流程，能深入挖掘用户需求，具备良好的沟通表达能力，以及方案建议书写作技能；</dd>
										<dd>5. 熟悉云计算技术架构及安装部署技术流程；</dd>
										<dd>6. 硬件知识：熟悉x86服务器、存储、交换机等厂家和设备；</dd>
										<dd>7. 软件知识：熟悉各种操作系统（windows、Linux等），且能够安装和部署在Linux及此平台上的中间件、数据库和其它应用；</dd>
										<dd>8. 网络知识：熟悉网络的各种形态，熟悉TCP/IP等各种协议；</dd>
										<dd>9. 虚拟化知识：熟悉主流虚拟化产品，如：vmware、citrix等虚拟化技术；</dd>
										<dd>10. 了解云计算相关技术，如：分布式计算、分布式存储等技术；</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都、广州、上海</dd>
									</dl>
								</div>
							</div>
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>高级实施工程师</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 售前技术支持，配合与支持销售活动，协作销售人员与客户交流，向客户讲解公司产品和技术方案，分析与理解客户需求，并根据客户需求制定相应的技术方案；</dd>
										<dd>2. 投标技术支持，协作销售人员参与竞标，并提供技术支持，负责向相关人员介绍产品技术参数以及投标技术文件的应答，为项目投标做好充分的技术材料准备；</dd>
										<dd>3. 为客户提供解决方案；</dd>
										<dd>4. 客户现场POC环境搭建，协作客户验证平台的稳定性、可靠性、以及可扩展性等；</dd>
										<dd>5. 负责已签项目的工程实施，包括软硬件的安装、调试等事宜；</dd>
										<dd>6. 为客户提供售后技术支持服务，包括服务器、存储、网络设备、虚拟化以及云平台产品的支持；</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 本科学历，理工科等相关专业背景；</dd>
										<dd>2. 计算机、信息、通信或相关专业；</dd>
										<dd>3. 5年以上相关领域工作经验；至少3年云计算技术管理经验；</dd>
										<dd>4. 熟悉云计算技术架构及安装部署技术流程；</dd>
										<dd>5. 硬件知识：熟悉x86服务器、存储、交换机等厂家和设备；</dd>
										<dd>6. 软件知识：熟悉各种操作系统（windows、Linux等），且能够安装和部署在Linux及此平台上的中间件、数据库和其它应用；</dd>
										<dd>7. 网络知识：熟悉网络的各种形态，熟悉TCP/IP等各种协议；</dd>
										<dd>8. 虚拟化知识：熟悉主流虚拟化产品，如：vmware、citrix等虚拟化技术；</dd>
										<dd>9. 了解云计算相关技术，如：分布式计算、分布式存储等技术；</dd>
										<dd>10. 具有强烈的责任感和很好的团队合作精神，具体较好的自学能力，能够承受工作压力；</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都、广州、上海</dd>
									</dl>
								</div>
							</div>
						</div>
						<div class="m-content">
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>前台</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 全面负责公司办公室日常行政各方面事项，担任公司前台；</dd>
										<dd>2. 负责公司内部文档整理；</dd>
										<dd>3. 协助做好日常人事行政事务处理；</dd>
										<dd>4. 完成上级领导交办的其他事情；</dd>
										<dd>5. 安排约会、会议室及差旅预定；</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 具有大专以上学历，形象良好，气质佳，反映灵活；</dd>
										<dd>2. 熟练运用OFFICE办公软件，思维严谨，办事高效；</dd>
										<dd>3. 条理清晰，擅言谈；</dd>
										<dd>4. 有责任心、良好的沟通能力；</dd>
										<dd>5. 性格开朗，吃苦耐劳、具有团队合作精神；</dd>
										<dd>6. 有良好文字功底优先考虑；</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都</dd>
									</dl>
								</div>
							</div>
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>总经理助理</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 负责总经理的日程安排；</dd>
										<dd>2. 负责来访嘉宾接待、商务随行等；</dd>
										<dd>3. 在总经理领导下负责办公室的全面工作，努力作好总经理的参谋助手，起到承上启下的作用，认真做到全方位服务；</dd>
										<dd>4. 在总经理领导下负责企业具体管理工作的布置、实施、检查、督促、落实执行情况；</dd>
										<dd>5. 协助总经理作好经营服务各项管理并督促、检查落实贯彻执行情况；</dd>
										<dd>6. 协助总经理调查研究、了解公司经营管理情况并提出处理意见或建议，供总经理决策；</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 形象气质佳，有3年以上IT行业助理岗位工作经验；</dd>
										<dd>2. 本科以上学历，文秘、行政等相关专业；</dd>
										<dd>3. 工作细致认真、谨慎细心、有条理性、责任心强；</dd>
										<dd>4. 具有良好的人际沟通、协调能力、团队意识强，保密意识强；</dd>
										<dd>5. 人品善良，性格开朗、直率；责任心、事业心强，能承受工作压力，团队协作能力；</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都</dd>
									</dl>
								</div>
							</div>
							<div class="m-iteam">
								<div class="t">
									<h3><i class="icon-tit-tips mr30"></i>融资经理</h3>
									<span class="m-time">发布时间：<i class="start-time">2015/4/2</i>&nbsp;&nbsp;截止时间：<i class="end-time">2015/12/11</i></span>
								</div>
								<div class="c">
									<dl>
										<dt>岗位描述：</dt>
										<dd>1. 负责公司与金融机构,投资机构,风险投资机构的联络、接洽;</dd>
										<dd>2. 根据公司需要，协助制定资金计划，并且针对计划，多渠道安排资金到位；</dd>
										<dd>3. 负责为公司融资项目寻找符合条件的资金方；</dd>
										<dd>4. 负责公司融资渠道的开发、维护；</dd>
										<dd>5. 为公司项目融资方案提出合理化建议；</dd>
										<dd>6. 完成上级交办的任务。</dd>
									</dl>
									<dl>
										<dt>任职要求：</dt>
										<dd>1. 本科及以上学历，投资、金融或其他经济类相关专业；</dd>
										<dd>2. 具有3年以上银行、基金、第三方理财机构等相关工作经验者优先；</dd>
										<dd>3. 具有IT行业融资经验的优先；</dd>
										<dd>4. 具有丰富的融资渠道和业内广泛的人际关系；</dd>
										<dd>5. 具有良好的融资分析能力和判断能力；</dd>
										<dd>6. 金融知识扎实，熟练掌握融资流程和专项业务知识；</dd>
										<dd>7. 有较强的谈判技能，具备良好的沟通能力；</dd>
										<dd>8. 善于处理复杂的人际关系，能够承受较强的工作压力。</dd>
									</dl>
									<dl>
										<dt>工作地点：</dt>
										<dd>成都</dd>
									</dl>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="f-cb"></div>
	<!-- footer -->
	<%@ include file="../common/tpl/u_footer.jsp"%>
	<!-- /footer -->
</div>
<!-- login -->
<%@ include file="../common/tpl/u_login.jsp"%>
<!-- /login -->
<!-- JavaScript -->
<script type="text/javascript" src="js/u_jionus.js"></script>
<script type="text/javascript">
	$(function(){
		navHighlight("uau","uju");
	})
</script>
<!-- /JavaScript -->
</body>
</html>