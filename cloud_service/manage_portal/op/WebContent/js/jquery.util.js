/*********************************
这个js里面的一些工具类是信赖于jquery.ext.js
*********************************/
if( jQuery.chz==null ){
	jQuery.chz = {};
}

/**
 * 事件列表延迟触发器
 * 例子:
 * 	function func(arr){
		var str = arr[0];
		alert(str);
	}
 * var eli = new EventListInvoker(1000);
 * eli.push( invoker, func, ["hello world 1"] );
 * eli.push( invoker, func, ["hello world 2"] );
 * 说明:
 * invoker: 具有func这个函数的对象(如window)
 * func: 是 定义的函数名
 * ["hello world 1"]: 是 传给func函数的参数
 */
function EventListInvoker(delay){
	
	var self = this;
	
	this.__delay = null;
	
	this.__eventList = [];
	
	this.__focusFuncElement = null;
	
	this.__initialize = function(){
		if( (typeof delay)!="number" ){
			alert("参数错误");
			return ;
		}
		self.__delay = delay;
	}
	
	/**
	 * invoker: func的持有者
	 * func: 要调用的函数
	 * arr： 传给func函数的参数
	 */
	this.push = function( invoker, func, arr){
		self.__eventList.push([invoker, func, arr]);
		self.__start();
	}
	
	/**
	 * 
	 */
	this.__start = function(){
		
		//判断当前是否有其它的函数等待执行
		if( self.__focusFuncElement!=null ){
			//有返回
			return ;
		}
		
		//取得要执行的函数
		if(self.__eventList.length==0){
			return ;
		}
		self.__focusFuncElement = self.__eventList.shift();
		
		//事件函数列表为空，返回
		if(self.__focusFuncElement==null){
			return ;
		}
		
		//取得要调用的函数和参数
		var invoker = self.__focusFuncElement[0];
		var func = self.__focusFuncElement[1];
		var argus = self.__focusFuncElement[2];
		
		//按延迟调用这个函数
		window.setTimeout(function(){
			func.apply( invoker, argus );
			self.__focusFuncElement = null;
			self.__start();
		},this.__delay);
		
	}
	
	//初始化
	self.__initialize();
}
// end EventListInvoker

//-----------------------

/**
 * ajax的调用工具,使用方式:
	var ajax = new RemoteCallUtil("/afManage/RemoteCall.let");
	ajax.remoteCall("com.suntek.web.afManage.resource.dwr.Video:test",
		[],
		function(reply){
			alert(getAllProperties(reply.getResult()));
		}
	);
 */
function RemoteCallUtil(urlBase){
	
	var self = this;
	
	this.async = true;	// 默认是异常
	this._urlBase = urlBase;
	
	this._ajax ;
	this._status = "waiting";		// waiting, calling
	this._callArray = [];
	this._currentCallObject ;
	
	this.createHttpRequest = function(){
		var http_request;
		if(window.XMLHttpRequest) {
			http_request = new XMLHttpRequest();
			if (http_request.overrideMimeType) {
				http_request.overrideMimeType('text/xml');
			}
		} else if (window.ActiveXObject) {	
			try {
				http_request = new ActiveXObject("Msxml2.XMLHTTP");
			} catch(e) {
				try {
					http_request = new ActiveXObject("Microsoft.XMLHTTP");
				} catch (e) {}
			}
		}
		if (!http_request) {
			window.alert("不能创建XMLHTTPRequest对象实例。");
		}
		return http_request;
	}
	
	this.remoteCall = function( url, parameters, callback ){
		var callObj = {};
		callObj["url"] = url;				// 调用的url
		callObj["parameters"] = parameters;	// 参数
		callObj["callback"] = callback;		// 回调函数
		callObj["async"] = self.async;		// 同步,异常
		self._callArray.push(callObj);
		self._remoteCall();
	};
	
	this._remoteCall = function(){
		if( this._status=="calling" ){
			return ;
		}
		if( this._callArray.length==0 ){
			this._status=="waiting";
			return ;
		}
		// 设置状态为开始工作
		this._status = "calling";
		this._currentCallObject = this._callArray.shift();
		var url        = this._currentCallObject["url"];
		var parameters = this._currentCallObject["parameters"];
		var async      = this._currentCallObject["async"];
		
		var sendObj = {};
		if( url.startsWith("class://") )
		{
			url = url.substring("class://".length);
			var arr = url.split(":");
			sendObj.className        = $.trim(arr[0]);
			sendObj.methodName       = $.trim(arr[1]);
			sendObj.methodParameters = parameters;
			sendObj.async = async;
		}
		else if( url.startsWith("bean://") )
		{
			url = url.substring("bean://".length);
			var arr = url.split(":");
			sendObj.beanName        = $.trim(arr[0]);
			sendObj.methodName       = $.trim(arr[1]);
			sendObj.methodParameters = parameters;
			sendObj.async = async;
		}
		else
		{
			throw new Error("wrong url ["+url+"]");
		}
		
		
		this._ajax = this.createHttpRequest();
		this._ajax.onreadystatechange = this._ajaxCallback;
		this._ajax.open("post", this._urlBase, async);
		this._ajax.setRequestHeader("Content-Type","text/plain");
		var content = $.toJSONString(sendObj);

		this._ajax.send(content);
	};
	
	this._ajaxCallback = function(){
		if( self._ajax.readyState==4 ){
			var httpStatus = self._ajax.status;
			if( httpStatus==200 ){
				// 调用回调函数
				var reply = new RemoteCallResult(self._ajax.responseText, self._currentCallObject);
				self._status = "waiting";
				self._currentCallObject["callback"](reply);
				// 调用下一个远程方法
				self._remoteCall();
			} else if( httpStatus==404 || httpStatus==503 ) {
				if( window.confirm("服务器返回ajax请求状态为["+httpStatus+"],可能服务器服务正在重启,是否重新登录?") ){
					top.location.href = "/sso_logout.jsp";
				}
			} else if( httpStatus==12029 ) {
				alert("服务器返回ajax请求状态为["+httpStatus+"],当前网络可能出现问题,请检测网络情况是否正常!")
			} else {
				alert("ajax请求的状态为["+httpStatus+"]");
			}
		}
	};
	
}	// RemoteCallUtil

/**
 * RemoteCallUtil.remoteCall 的call返回的对象
 */
function RemoteCallResult(responseText, invokeInfo){
	
	var self = this;
	
	this.invokeInfo = invokeInfo;	// url, parameters, callback, async
	
	this.responseText = responseText;
	this.responseObject ;
	
	this.status ;			// success, Exception
	this.result ;
	this.exceptionClass ;
	this.exceptionMessage ;
	this.errorCode ;
	
	var initialize = function(){
		if( self.responseText=="" ){
			self.status = "exception";
			self.exceptionClass = "javascript exception";
			self.exceptionMessage = "session超时,或没有登录!";
			alert(self.exceptionMessage);
			return ;
		} else {
			try{
				eval("self.responseObject = "+self.responseText);
			}catch(e){
				alert("无法处理json:["+self.responseText+"]");
			}
			self.status = self.responseObject["status"];
			self.result = self.responseObject["result"];
			self.exceptionClass = self.responseObject["exceptionClass"];
			self.exceptionMessage = self.responseObject["exceptionMessage"];
			self.errorCode = self.responseObject["errorCode"];
		}
		if( self.status=="exception" && self.exceptionMessage=="Read timed out" ){
			self.showException();
		}
	};
	
	this.getResult = function(){
		return this.result;
	};
	
	this.showException = function(){
		var arr = [];
		arr.push("exceptionClass:"+self.exceptionClass);
		arr.push("exceptionMessage:"+self.exceptionMessage);
		arr.push("errorCode:"+self.errorCode);
		arr.push("url:"+self.invokeInfo["url"]);
		arr.push("parameters:"+self.invokeInfo["parameters"]);
		arr.push("async:"+self.invokeInfo["async"]);
		alert( arr.join("\n") );
	};
	
	initialize();
	
}	// RemoteCallResult




