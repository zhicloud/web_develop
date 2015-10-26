<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html lang="zh-cmn-Hans">
<head>
   <base href="<%=request.getContextPath() %>/src/user/u_overview.jsp" />
	<meta charset="utf-8"/>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta name="viewport" content="width=device-width">
	<meta name="keywords" content="致云，致云科技，ZhiCloud，云计算服务商，云计算提供商，自主研发，云平台，云计算平台，云主机，云服务器，云存储，云硬盘，云解决方案" />
	<meta name="description" content="致云科技ZhiCloud是一家专注于云计算领域的创新型高科技企业，是目前国内极少数完全依托自有知识产权，拥有云计算核心专利，提供公有云、私有云、混合云等全产品线的云计算解决方案提供商和服务商。" />
	<title>我的云端-ICP备案系统 -- 致云 ZhiCloud</title>
	<link rel="shortcut icon" href="../common/img/favicon.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
	<link rel="stylesheet" type="text/css" href="../common/css/global.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="css/mycloud.css" media="all"/>
    
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
	<div class="mc-fs-bd mc-ucenter">
		<div class="wrap">
			<%@ include file="../common/tpl/u_mcslider.jsp"%>
			<!--内容区 start-->
			<div id="content" style="min-height: 596px;">
				<div class="icp-content" data-spm=a3c20>
					<div class="icp-content-wrapper">
						<div class="icp-content-block-title">
							<label>开始备案：</label>		
							<a target="_blank" href="javascript:;" class="float-right link-default"><i class="icp-icon-small-question"></i>&nbsp;备案帮助</a>
							<span class="float-right ">&nbsp;&nbsp;&nbsp;&nbsp;</span>
							<a target="_blank" href="javascript:;" class="float-right link-default"><i class="icp-icon-small-question"></i>&nbsp;核验点地址</a>	 
						</div>
						<form  id="J_form" method="post">
							<input name='_tb_token_' type='hidden' value='zYalXxyPao'>
							<input type="hidden" name="action" value="order_action"/>
							<input type="hidden" id="J_hidCommonFormErrorInfo" data-tip-title="" data-main-button-text="确定"/>
							<input type="hidden" name="event_submit_do_selfBaValidate" value="ok">
							<input type="hidden" id="J_hidcsfttoken" value="zYalXxyPao" />	
							<!--新手引导页控制，不需要则不加，需要时加此hidden，1是首页，2是验证备案-->
							<input type="hidden" class="J_hidGuidanceInfo" value="2" > 
							<div class="icp-content-block">
								<div class="icp-tipmessage-yellow"><span>请填写以下信息开始备案，系统将根据您填写的域名和证件，自动验证您的备案类型</span></div>
								<div class="icp-form icp-form-type-block">
									<div class="icp-form-item">
										<div class="icp-form-item-left-title"><em class="i-must">*</em>主办单位所属区域:</div>
										<div class="icp-form-item-center-content">
											<select class="icp-form-item-select select-district J_must" name="provinceId" id="J_province" data-val="provice" data-info="provice"  >
												<option value="-1">--请选择--</option>
												<option value="110000" >北京市</option>
												<option value="120000" >天津市</option>
												<option value="130000" >河北省</option>
												<option value="140000" >山西省</option>
												<option value="150000" >内蒙古自治区</option>
												<option value="210000" >辽宁省</option>
												<option value="220000" >吉林省</option>
												<option value="230000" >黑龙江省</option>
												<option value="310000" >上海市</option>
												<option value="320000" >江苏省</option>
												<option value="330000" >浙江省</option>
												<option value="340000" >安徽省</option>
												<option value="350000" >福建省</option>
												<option value="360000" >江西省</option>
												<option value="370000" >山东省</option>
												<option value="410000" >河南省</option>
												<option value="420000" >湖北省</option>
												<option value="430000" >湖南省</option>
												<option value="440000" >广东省</option>
												<option value="450000" >广西壮族自治区</option>
												<option value="460000" >海南省</option>
												<option value="500000" >重庆市</option>
												<option value="510000" >四川省</option>
												<option value="520000" >贵州省</option>
												<option value="530000" >云南省</option>
												<option value="540000" >西藏自治区</option>
												<option value="610000" >陕西省</option>
												<option value="620000" >甘肃省</option>
												<option value="630000" >青海省</option>
												<option value="640000" >宁夏回族自治区</option>
												<option value="650000" >新疆维吾尔自治区</option>
											</select>
											<select class="icp-form-item-select select-district margin-left-10 J_must" name="cityId" id="J_city" data-val="city" data-info="city"><option value="-1">--请选择--</option></select>							
											<select class="icp-form-item-select select-district margin-left-10 J_must" name="countyId" id="J_district" data-val="district" data-info="district"><option value="-1">--请选择--</option></select>
										</div>
										<div class="icp-form-item-right-tip display-none">
											<a class="icp-icon"></a>
											<div class="icp-form-item-right-message"><em class="i-triangle-left"></em></div>
										</div>
									</div>
									<div class="icp-form-item">
										<div class="icp-form-item-left-title"><em class="i-must">*</em>主办单位性质 :</div>
										<div class="icp-form-item-center-content">
											<select class="icp-form-item-select J_must" name="organizersNature" id="J_dwxz" data-val="dwxz" data-info="dwxz"  disabled>
											<option value="-1">请选择单位性质</option>
											<option value="1" >军队</option>
											<option value="2" >政府机关</option>
											<option value="3" >事业单位</option>
											<option value="4" >企业</option>
											<option value="5" >个人</option>
											<option value="6" >社会团体</option>
											</select>
											<div id="div1" class="icp-form-item-center-tip display-none"></div>
										</div>
										<div class="icp-form-item-right-tip display-none">
											<a class="icp-icon"></a>
											<div class="icp-form-item-right-message"><em class="i-triangle-left"></em></div>
										</div>
									</div>
									<div class="icp-form-item">
										<div class="icp-form-item-left-title"><em class="i-must">*</em>主办单位证件类型 :</div>
										<div class="icp-form-item-center-content">
											<select class="icp-form-item-select J_must  " name="identityType" id="J_dwzjlx" data-val="dwzjlx" data-info="dwzjlx"  disabled><option value="-1">请选择证件类型</option></select>
											<div id="div2" class="icp-form-item-center-tip display-none"></div>
										</div>
										<div class="icp-form-item-right-tip display-none">
											<a class="icp-icon"></a>
											<div class="icp-form-item-right-message"><em class="i-triangle-left"></em></div>
										</div>
									</div>
									<div class="icp-form-item">
										<div class="icp-form-item-left-title"><em class="i-must">*</em>主办单位证件号码 :</div>
										<div class="icp-form-item-center-content">
											<input type="text" class="icp-form-item-text J_must J_dwCard" disabled="disabled" value="" id="J_dwZjhm" data-info="dwzjhm" data-val="dwzjhm" name="identityNum">
											<div id="J_zjlxPic_zjhm" class="J_zjlxPic display-none"><a class="font12 J_zjlxPicA" href="javascript:;"><span class="J_zjlxPicTxt"></span>证件号码图示</a></div>
											<div id="div3" class="icp-form-item-center-tip display-none"></div>
										</div>
										<div class="icp-form-item-right-tip display-none">
											<a class="icp-icon"></a>
											<div class="icp-form-item-right-message"><em class="i-triangle-left"></em></div>
										</div>
									</div>
									<div class="icp-form-item">
										<div class="icp-form-item-left-title"><em class="i-must">*</em>域名:</div>
										<div class="icp-form-item-center-content">
											<span>www.</span><input type="text" disabled="disabled" class="icp-form-item-text width-303 J_must J_domain J_webDomain" id="J_proValDomain" data-info="domain" name="domainName" value="" data-val="domain">
											<div id="div21" class="icp-form-item-center-tip display-none"><em class="i-triangle-top"></em></div>
										</div>
										<div class="icp-form-item-right-tip display-none">
											<a class="icp-icon"></a>
											<div class="icp-form-item-right-message"><em class="i-triangle-left"></em></div>
										</div>
									</div>
									<div class="icp-form-item">
										<div class="icp-form-item-left-title"><em class="i-must">*</em>验证码:</div>
										<div class="icp-form-item-center-content">
											<input type="text" class="icp-form-item-text authcode-text J_must J_accountAuthCode" data-info="accountAuthCode" data-val="accountAuthCode" name="selfCaptcha"/> 
											<img alt="" id="J_authcodeImg" class="authcode-img J_authcodeImg" src="http://pin.aliyun.com/get_img?identity=gein.cn&sessionid=91666981-8DT18OQ7NLYEP0TDMHVQ3-5MCDS2CI-1XM4" />
											<a href="javascript:;" class="margin-left-10 link-default J_authcodeImg">看不清，换一张</a>
										</div>
										<div class="icp-form-item-right-tip correct-no-shows display-none">
											<a class="icp-icon"></a>
											<div class="icp-form-item-right-message"><em class="i-triangle-left"></em></div>
										</div>
									</div>
								</div>
							</div>
							<div class="icp-form-type-block-bottom-operation ">
								<input type="hidden" id="captchaSessionId" value="91666981-8DT18OQ7NLYEP0TDMHVQ3-5MCDS2CI-1XM4"/>
								<button type="button" class="icp-button-main icp-button-size-large J_btnSubmit">验证备案类型</button>
								<input type="hidden" name="event_submit_do_selfBaValidate" value="ok"/>	
							</div>
					</form>
				</div>
			</div>
    		</div>
    		<!--内容区 end-->
			
		</div>
	</div>
	<div class="f-cb"></div>
	<!-- footer -->
	<%@ include file="../common/tpl/u_footer.jsp"%>
	<!-- /footer -->
</div>
<!-- JavaScript -->
<script type="text/javascript" src="<%=request.getContextPath() %>/dep/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/goup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/common.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
$(function(){
	navHighlight("umc","umfs");
})
</script>
<!-- /JavaScript -->
</body>
</html>