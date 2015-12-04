<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
     <base href="<%=request.getContextPath() %>/src/user/u_arcser.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>备案服务 -- 致云 ZhiCloud</title>
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
	<div class="g-as-bd">
		<div class="wrap">
			<div class="as-cont">
				<img class="as-img f-db" src="img/as_img.png" alt="备案封面" />
				<h3 class="c-474 font24 f-fwn">ICP备案</h3>
				<div class="as-info">
					<dl>
						<dt>步骤一：准备资料</dt>
						<dd>资料包下载：<a href="https://www.zhicloud.com/download/ICP-ZHICLOUD.zip">[ICP备案资料-ZHICLOUD.zip]</a></dd>
						<dd>1. 网站备案信息核验单（盖章，请确保打印成 一页 ，不写日期）</dd>
						<dd>2. 网站信息登记表（盖章，不能有空项）</dd>
						<dd>3. 营业执照副本原件（或个人备案主体提供身份证）扫描件、复印件（盖章）</dd>
						<dd>4. 网站负责人身份证原件和复印件（盖章）</dd>
						<dd class="c-red">注：若网站负责人不是法人，需要提供网站备案及管理的授权书。</dd>
						<dd>5. 域名证书复印件（盖章）</dd>
						<dd class="c-red">注：域名证书的所有方必须是备案主体；否则，需将域名转让，或出具域名转让协议。</dd>
						<dd>6. 主体负责人身份证原件扫描件、复印件（盖章）</dd>
						<dd>7. 若贵公司是第一次备案（公司没有备案号），需要填写主体负责人信息表（盖章）</dd>
						<dd class="c-red">备注：填写过程中请详细参考下载资料包中的《备案所需资料和注意事项》。</dd>
					</dl>
					<dl>
						<dt>步骤二：备案资料审查</dt>
						<dd>请将所有资料通过E-Mail发给备案专员，备案专员会为您进行初步审核，并与您确认: </dd>
						<dd>专员：刘婉露</dd>
						<dd>电话：4000-212-999</dd>
						<dd>邮箱：beian@zhicloud.com</dd>
						<dd>QQ号：2041426810（备案专用QQ，不受理其他咨询问题）</dd>
					</dl>
					<dl>
						<dt>步骤三：提供备案资料</dt>
						<dd>备案资料审查通过后，按备案专员指引，根据您所在地情况，进行负责人拍照及将备案资料邮寄</dd>
						<dd>致云科技广州公司、成都公司、上海公司（不可到付）：</dd>
					</dl>
					<ul>
						<li>
							<h4>广州公司：</h4>
							<p>广州市天河区黄埔大道西100号富力盈泰广场A塔1011</p>
							<p>电话：020-38260999</p>
						</li>
						<li>
							<h4>成都公司：</h4>
							<p>成都市高新区天府软件园C区12栋15层</p>
							<p>电话：028-69189999</p>
						</li>
						<li>
							<h4>上海公司：</h4>
							<p>上海市普陀区真北路915号1505</p>
							<p>电话：021-62868099</p>
						</li>
						<li>
							<h4>备注：</h4>
							<p>管局审核备案时间：根据《非经营性互联网信息服务备案管理办法》（信息产业部令33号令)第12条规定，省<br/>通信管理局在收到备案人提交的备案材料后，材料齐全的，应在二十个工作日内予以备案；材料不齐全的，不<br/>予备案，在二十个工作日内通知备案人并说明理由。</p>
						</li>
					</ul>
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
		navHighlight("uts","uas");
	})
</script>
</body>
</html>
