<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<div class="mc-slider">
	<div class="mc-tabsinfo">
		<a id="myoverview" class="m-tab" onclick="onclickSwitch(this);">
			<img class="mcnav_1" src="<%=request.getContextPath() %>/src/user/img/mcnav_1_i.png" alt="概览"/>
			<label>概览</label>
		</a>
		<a id="mycloudser" class="m-tab" onclick="onclickSwitch(this);">
			<img class="mcnav_2" src="<%=request.getContextPath() %>/src/user/img/mcnav_2_i.png" alt="我的云主机"/>
			<label>我的云主机</label>
		</a>
		<a id="myclouddrive" class="m-tab" onclick="onclickSwitch(this);">
			<img class="mcnav_3" src="<%=request.getContextPath() %>/src/user/img/mcnav_3_i.png" alt="我的云硬盘"/>
			<label>我的云硬盘</label>
		</a>
		<a id="myexclucloud" class="m-tab" onclick="onclickSwitch(this);">
			<img class="mcnav_4" src="<%=request.getContextPath() %>/src/user/img/mcnav_4_i.png" alt="我的专属云"/>
			<label>我的专属云</label>
		</a>
		<a id="myaccount" class="m-tab" onclick="onclickSwitch(this);">
			<img class="mcnav_5" src="<%=request.getContextPath() %>/src/user/img/mcnav_5_i.png" alt="我的账户"/>
			<label>我的账户</label>
		</a>
		<a id="myoperalog" class="m-tab" onclick="onclickSwitch(this);">
			<img class="mcnav_6" src="<%=request.getContextPath() %>/src/user/img/mcnav_6_i.png" alt="操作日志"/>
			<label>操作日志</label>
		</a>
		<a id="myfeedback" class="m-tab" onclick="onclickSwitch(this);">
			<img class="mcnav_7" src="<%=request.getContextPath() %>/src/user/img/mcnav_7_i.png" alt="意见反馈"/>
			<label>意见反馈</label>
		</a>
		<a id="myuploadfile" class="m-tab" onclick="onclickSwitch(this);">
			<img class="mcnav_8" src="<%=request.getContextPath() %>/src/user/img/mcnav_8_i.png" alt="上传文件"/>
			<label>上传文件</label>
		</a>
		<%-- <a id="myfilingsys" class="m-tab" onclick="onclickSwitch(this);">
			<img class="mcnav_9" src="<%=request.getContextPath() %>/src/user/img/mcnav_9_i.png" alt="ICP备案系统"/>
			<label>ICP备案系统</label>
		</a> --%>
	</div>
</div>