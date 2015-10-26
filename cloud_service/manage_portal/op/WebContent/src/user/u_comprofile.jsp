<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
    <base href="<%=request.getContextPath() %>/src/user/u_comprofile.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>公司简介 -- 致云 ZhiCloud</title>
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
	<div class="g-cp-bd">
		<div class="cp-sec1"></div>
		<div class="cp-sec2">
			<div class="wrap">
				<h3 class="cpt">公司简介</h3>
				<p>致云科技有限公司是一家专注于云计算领域的创新型高科技企业，拥有在云计算领域具备丰富经验的资深技术和运营团队<br/>总部及研发中心设在成都高新区，目前在成都、北京、上海、广州和深圳设立全资子公司。<br/>致云科技始终以技术创新为企业核心竞争力，已拥有几十项自主研发、自有知识产权的云计算核心专利技术；结合商业模式创新，以“市场主导、技术先行、自主创新、服务<br/>为本”为经营理念，打造一个共生、共赢、共进、共享的云计算产业链，并致力成为全球领先的云计算解决方案提供商和服务商</p>
			</div>
		</div>
		<div class="cp-sec3">
			<div class="wrap">
				<h3 class="cpt">核心竞争力</h3>
				<div class="core-comp-list">
					<div class="cc-iteam">
						<img class="f-db cp_zzyf" alt="自主研发" src="img/cp_zzyf.png"/>
						<h4>自主研发</h4>
					</div>
					<div class="cc-iteam cc-second">
						<img class="f-db cp_fmzl" alt="40项发明专利" src="img/cp_fmzl.png"/>
						<h4>40项发明专利</h4>
					</div>
					<div class="cc-iteam cc-third">
						<img class="f-db cp_dzhfw" alt="定制化服务" src="img/cp_dzhfw.png"/>
						<h4>定制化服务</h4>
					</div>
					<div class="cc-iteam">
						<img class="f-db cp_jjfa" alt="完整端到端得解决方案" src="img/cp_jjfa.png"/>
						<h4>完整端到端得解决方案</h4>
					</div>
				</div>
			</div>
		</div>
		<div class="cp-sec4">
			<div class="wrap">
				<h3 class="cpt">致云科技发展大事记</h3>
				<div class="develop-cont">
					<div class="cp-dev-cont cpd-r1">
						<div class="i"><img src="img/cp_dev_rowline.png" alt="创业团队在深圳成立" /></div>
						<div class="c">
							<label>2010&nbsp;&nbsp;12.26</label>
							<span>创业团队在深圳成立</span>
						</div>
					</div>
					<div class="cp-dev-dot"></div>
					<div class="cp-dev-line"></div>
					<div class="cp-dev-cont cpd-left cpd-l1">
						<div class="c">
							<label>2012&nbsp;&nbsp;09.29</label>
							<span>云平台发布</span>
						</div>
						<div class="i"><img src="img/cp_dev_rowline.png" alt="云平台发布" /></div>
					</div>
					<div class="cp-dev-dot"></div>
					<div class="cp-dev-line"></div>
					<div class="cp-dev-cont cpd-r2">
						<div class="i"><img src="img/cp_dev_rowline.png" alt="与成都高新区签订合作协议" /></div>
						<div class="c">
							<label>2013&nbsp;&nbsp;08.26</label>
							<span>与成都高新区签订合作协议</span>
						</div>
					</div>
					<div class="cp-dev-dot"></div>
					<div class="cp-dev-line"></div>
					<div class="cp-dev-cont cpd-left cpd-l2">
						<div class="c">
							<label>2013&nbsp;&nbsp;11.13</label>
							<span>总部及研发中心入驻成都高新区天府软件园</span>
						</div>
						<div class="i"><img src="img/cp_dev_rowline.png" alt="总部及研发中心入驻成都高新区天府软件园" /></div>
					</div>
					<div class="cp-dev-dot"></div>
					<div class="cp-dev-line"></div>
					<div class="cp-dev-cont cpd-r3">
						<div class="i"><img src="img/cp_dev_rowline.png" alt="荣获第六届中国行业信息化2014年度云计算行业最具成长力企业奖" /></div>
						<div class="c">
							<label>2014&nbsp;&nbsp;09.19</label>
							<span>荣获第六届中国行业信息化<br/>2014年度云计算行业最具成长力企业奖</span>
						</div>
					</div>
					<div class="cp-dev-dot"></div>
					<div class="cp-dev-line"></div>
					<!-- <div class="cp-dev-cont cpd-left cpd-l3">
						<div class="c">
							<label>2014&nbsp;&nbsp;09.29</label>
							<span>与国家北斗卫星导航数据中心签署战略合作协议</span>
						</div>
						<div class="i"><img src="img/cp_dev_rowline.png" alt="与国家北斗卫星导航数据中心签署战略合作协议" /></div>
					</div> -->
					<div class="cp-dev-cont cpd-left cpd-l3" style="left:255px;">
						<div class="c">
							<label>2014&nbsp;&nbsp;10.01</label>
							<span>广州、香港云计算中心正式上线商用</span>
						</div>
						<div class="i"><img src="img/cp_dev_rowline.png" alt="广州、香港云计算中心正式上线商用" /></div>
					</div>
					<div class="cp-dev-dot"></div>
					<div class="cp-dev-line"></div>
					<!-- <div class="cp-dev-cont cpd-r4">
						<div class="i"><img src="img/cp_dev_rowline.png" alt="广州、香港云计算中心正式上线商用" /></div>
						<div class="c">
							<label>2014&nbsp;&nbsp;10.01</label>
							<span>广州、香港云计算中心正式上线商用</span>
						</div>
					</div> -->
					<div class="cp-dev-cont cpd-r4" style="right:111px;">
					<div class="i"><img src="img/cp_dev_rowline.png" alt="与天府软件园签订战略合作协议共建国家软件公共服务云平台&amp;成都云计算中心上线" /></div>
						<div class="c">
							<label>2014&nbsp;&nbsp;12.31</label>
							<span>与天府软件园签订战略合作协议共建国家软件公共服务云平台<br/>&成都云计算中心上线</span>
						</div>
					</div>
					<div class="cp-dev-dot"></div>
					<div class="cp-dev-line"></div>
					<!-- <div class="cp-dev-cont cpd-left cpd-l4">
						<div class="c">
							<label>2014&nbsp;&nbsp;12.31</label>
							<span>与天府软件园签订战略合作协议共建国家软件公共服务云平台<br/>&成都云计算中心上线</span>
						</div>
						<div class="i"><img src="img/cp_dev_rowline.png" alt="与天府软件园签订战略合作协议共建国家软件公共服务云平台&amp;成都云计算中心上线" /></div>
					</div> -->
					<div class="cp-dev-cont cpd-left cpd-l4" style="left:171px;">
						<div class="c">
							<label>2015&nbsp;&nbsp;03.26</label>
							<span>携手天府软件园共建“众创”“双创”云服务计划</span>
						</div>
						<div class="i"><img src="img/cp_dev_rowline.png" alt="携手天府软件园共建“众创”“双创”云服务计划" /></div>
					</div>
					<div class="cp-dev-dot"></div>
					<!-- <div class="cp-dev-line"></div>
					<div class="cp-dev-cont cpd-r5">
						<div class="i"><img src="img/cp_dev_rowline.png" alt="携手天府软件园共建“众创”“双创”云服务计划" /></div>
						<div class="c">
							<label>2015&nbsp;&nbsp;03.26</label>
							<span>携手天府软件园共建“众创”“双创”云服务计划</span>
						</div>
					</div>
					<div class="cp-dev-dot"></div> -->
					<!-- <div class="cp-dev-line"></div>
					<div class="cp-dev-cont cpd-left cpd-l5">
						<div class="c">
							<label>2015&nbsp;&nbsp;03.31</label>
							<span>与广州电信签订合作协议</span>
						</div>
						<div class="i"><img src="img/cp_dev_rowline.png" alt="与广州电信签订合作协议" /></div>
					</div>
					<div class="cp-dev-dot"></div> -->
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
<script type="text/javascript">
	$(function(){
		navHighlight("uau","ucf");
	})
</script>
</body>
</html>