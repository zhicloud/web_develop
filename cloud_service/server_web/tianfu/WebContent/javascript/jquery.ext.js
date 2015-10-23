
JsUtil = {};

/**
 * 删除对象
 */
JsUtil.doDelete = function(obj)
{
	for( var p in obj )
	{
		delete obj[p];
	}
	delete obj;
};





//-----String的扩展--------
//实现String的trim()方法,用法：str = "hello ".trim();
String.prototype.trim = function(){
	return this.replace(/(^\s*)|(\s*$)/g,"");
};

//实现String的endsWith()方法,用法：str = "hello ".endsWith(".xls");
String.prototype.endsWith = function(s){
	if(s==null){
		return false;
	}
	var len = s.length;
	if(len>this.length){
		return false;
	}
	var sub = this.substring(this.length-len,this.length);
	return sub==s;
};
//实现String的startsWith()方法,用法：str = "hello ".startsWith(".xls");
String.prototype.startsWith = function(s){
	if(s==null){
		return false;
	}
	var len = s.length;
	if(len>this.length){
		return false;
	}
	var sub = this.substring(0,len);
	return sub==s;
};
// 替换所有的字符串
String.prototype.replaceAll = function (oldStr, newStr) {
	if( oldStr instanceof RegExp ){
		return this.replace(oldStr, newStr);
	} else {
		return this.replace(new RegExp(oldStr, "g"), newStr);
	}
};






//---Array的扩展--------
// 如果obj1中的元素obj2都有，而且相等，那么返回true，否则返回false
Array.prototype.equal = function(arr) {
	if( this.length==arr.length ){
		for( var i=0; i<this.length; i++ ){
			if( this[i]!=arr[i] ){
				return false;
			}
		}
		return true;
	}
	return false;
};
// 对数组里面的每一个数据都做为参数运行一次参数函数
// 例子：[].each(function(item, index){})
Array.prototype.each = function(fn) {
	for(var i=0; i<this.length; i++) {
		fn(this[i], i);
	}
};
// 删除数组中下标为idx的元素
Array.prototype.removeAt = function(idx) {
	this.splice(idx,1);
};
// 删除数组中一个值为obj的元素
Array.prototype.remove = function(obj) {
	for(var i=this.length; i>=0; i--) {
		if(this[i]==obj) {
			this.splice(i,1);
		}
	}
};
// 判断数组中是否存在着一个obj元素，start是开始循环的下标
Array.prototype.indexOf = function(obj, start) {
	if( start==null ) {
		start = 0;
	}
	for( var i=start; i<this.length; i++ ) {
		if(this[i]==obj) {
			return i;
		} 
	}
	return -1;
};
//
Array.prototype.contains = function(obj) {
	return this.indexOf(obj)>-1 ? true : false;
};
// 判断数组中是否存在着obj元素，存在则什么也不做，不存在的话就把obj添加进数组中
// 信赖于： indexOf 函数
Array.prototype.notRepeatAdd = function(obj) {
	var idx = this.indexOf(obj);
	if(idx==-1) {
		this.push(obj);
	}
};
// 将数组中的某个属性的值串起来组成新的数组返回
Array.prototype.joinProperty = function(prop) {
	var arr = [];
	for( var i=0; i<this.length; i++ ) {
		var item = this[i];
		arr.push(item[prop]);
	}
	return arr;
};
//
Array.prototype.toMap = function(prop) {
	var map = {};
	for( var i=0; i<this.length; i++ ) {
		var item = this[i];
		map[item[prop]] = item;
	}
	return map;
};

// 
CapacityUtil = new function(){
	
	var self = this;
	
	self.toByte = function(data, scale)
	{
		var big = new Big(data);
		big = big.toFixed(scale);
		return big+"B";
	};
	
	self.toKB = function(data, scale)
	{
		var big = new Big(data);
		big = big.div(1024);
		big = big.toFixed(scale);
		return big+"KB";
	};
	
	self.toMB = function(data, scale)
	{
		var big = new Big(data);
		big = big.div(1024);
		big = big.div(1024);
		big = big.toFixed(scale);
		return big+"MB";
	};
	
	self.toGB = function(data, scale)
	{
		var big = new Big(data);
		big = big.div(1024);
		big = big.div(1024);
		big = big.div(1024);
		big = big.toFixed(scale);
		return big+"GB";
	};
	
	self.toTB = function(data, scale)
	{
		var big = new Big(data);
		big = big.div(1024);
		big = big.div(1024);
		big = big.div(1024);
		big = big.div(1024);
		big = big.toFixed(scale);
		return big+"TB";
	};
	
	self.toCapacityLabel = function(data, scale)
	{
		if( scale==null )
		{
			scale = 2;
		}
		var data = new Big(data);
		var base = new Big(1024);
		if( data.cmp(base)<0 )
		{
			return self.toByte(data, scale);
		}
		else if( data.cmp(base.pow(2))<0 )
		{
			return self.toKB(data, scale);
		}
		else if( data.cmp(base.pow(3))<0 )
		{
			return self.toMB(data, scale);
		}
		else if( data.cmp(base.pow(4))<0 )
		{
			return self.toGB(data, scale);
		}
		else
		{
			return self.toTB(data, scale);
		}
	};
	
};


//
FlowUtil = new function(){
	
	var self = this;
	
	self.toBps = function(data, scale)
	{
		var big = new Big(data);
		big = big.toFixed(scale);
		return big+"B";
	};
	
	self.toKbps = function(data, scale)
	{
		var big = new Big(data);
		big = big.div(1000);
		big = big.toFixed(scale);
		return big+"KB";
	};
	
	self.toMbps = function(data, scale)
	{
		var big = new Big(data);
		big = big.div(1000);
		big = big.div(1000);
		big = big.toFixed(scale);
		return big+"MB";
	};
	
	self.toGbps = function(data, scale)
	{
		var big = new Big(data);
		big = big.div(1000);
		big = big.div(1000);
		big = big.div(1000);
		big = big.toFixed(scale);
		return big+"GB";
	};
	
	self.toTbps = function(data, scale)
	{
		var big = new Big(data);
		big = big.div(1000);
		big = big.div(1000);
		big = big.div(1000);
		big = big.div(1000);
		big = big.toFixed(scale);
		return big+"TB";
	};
	
	self.toFlowLabel = function(data, scale)
	{
		if( scale==null )
		{
			scale = 2;
		}
		var data = new Big(data);
		var base = new Big(1000);
		if( data.cmp(base)<0 )
		{
			return self.toBps(data, scale);
		}
		else if( data.cmp(base.pow(2))<0 )
		{
			return self.toKbps(data, scale);
		}
		else if( data.cmp(base.pow(3))<0 )
		{
			return self.toMbps(data, scale);
		}
		else if( data.cmp(base.pow(4))<0 )
		{
			return self.toGbps(data, scale);
		}
		else
		{
			return self.toTbps(data, scale);
		}
	};
	
};








//----jQuery的扩展-------
// 获取可见的窗口宽度
jQuery.fn.clientWidth = function(){
	return this[0].clientWidth;
};
// 获取可见的窗口高度
jQuery.fn.clientHeight = function(){
	return this[0].clientHeight;
};
// 获取可见的窗口宽度
jQuery.fn.scrollWidth = function(){
	return this[0].scrollWidth;
};
// 获取可见的窗口高度
jQuery.fn.scrollHeight = function(){
	return this[0].scrollHeight;
};
// 将jQuery对象数组的某个属性值组成一个新的数组
jQuery.fn.joinAttribute = function(key){
	var arr = [];
	for(var i=0; i<this.length; i++)
	{
		var item = this[i];
		arr.push(item.getAttribute(key));
	}
	return arr;
};
// 选择文本 
jQuery.fn.selectRange = function(start, end){
   return this.each(function(){
       if (this.setSelectionRange) {
           this.focus();
           this.setSelectionRange(start, end);
       } else if (this.createTextRange) {
           var range = this.createTextRange();
           range.collapse(true);
           range.moveEnd('character', end);
           range.moveStart('character', start);
           range.select();
       }
   });
};
// 给对象设置css文本，参数cover表示是否覆盖原有css样式
jQuery.fn.setCSSText = function(cssText, cover){
	if( cover==true ){
		this.each(function(){
			cssText = cssText.replaceAll(/[\r\n]+/g, ";");	//将回车转换为;
			cssText = cssText.replaceAll(/((\s*);*(\s*))*;((\s*);*(\s*))*/g, ";\n");	// 将一连串的;和空的字符转换为";\n"
			this.style.cssText = cssText;
		});
	} else {
		var cssSegmentArr = cssText.split(/(\s)*;(\s)*/);
		var obj = {};
		for( var i=0; i<cssSegmentArr.length; i++ ){
			var cssSegment = cssSegmentArr[i];
			var cssPair = cssSegment.split(":");
			if( cssPair.length<2 ){
				continue;
			}
			obj[cssPair[0].trim()] = cssPair[1].trim();
		}
		alert($.getAllProperties(obj));
		$(this).css(obj);
	}
};
//---------------------
// 获取一个frame的contentWindow到它的祖辈window的距离
jQuery.offsetFromWin = function(fromWin, toWin){
	fromWin = fromWin || window;
	toWin = toWin || top;
	var offset = {};
	offset.left = 0;
	offset.top = 0;
	var tempWin = fromWin;
	while( tempWin!=toWin && tempWin.frameElement!=null ){
		var ofs = $(tempWin.frameElement).offset();
		offset.left = ofs.left;
		offset.top = ofs.top;
		tempWin = tempWin.parent;
	}
	return offset;
};
//-----------------------
// 如果 str==null 返回 s, 否则返回str.trim()
jQuery.trim = function(str, s){
	if( str==null ){
		if( s==null ){
			return "";
		} else {
			return s;
		}
	} else {
		return str.trim();
	}
};
// 如果 str==null 或者 str=="" 返回 s, 否则返回str.trim()
jQuery.trim2 = function(str, s){
	if( str==null || str.trim()=="" ){
		if( s==null ){
			return "";
		} else {
			return s;
		}
	} else {
		return str.trim();
	}
};
// 
jQuery.showLimitedString = function(input, len){
	if(input==null){
		return "";
	} else if(input.length > len) {
		return input.substring(0, len-1)+"..";
	} else {
		return input;
	}
};
//
jQuery.split = function( str, sep ){
	if( str=="" || str==null){
		return [];
	} else {
		if( sep==null ){
			sep = ",";
		}
		return str.split(sep);
	}
};
//
jQuery.splitTrim = function( str, sep ){
	if( str=="" ){
		return [];
	} else {
		if( sep==null ){
			sep = ",";
		}
		var arr = str.split(sep);
		var result = [];
		for( var i=0; i<arr.length; i++ ){
			var s = arr[i].trim();
			if(s!=""){
				result.push(s);
			}
		}
		return result;
	}
};
// 是不是正数
jQuery.isInt = function(str)
{
	var er = /^\d+$/;
	return er.test(str);
};
// 是不是正数
jQuery.isFloat = function(str)
{
	var er = /^\d+(\.\d+)?$/;
	return er.test(str);
};
//
jQuery.numToString = function( num, len ){
	var s = ""+num;
	var slen = s.length;
	if( slen>len ){
		return s.substring(s.length-len);
	}
	if( slen<len ){
		var count = len-slen;
		var arr = [];
		for( var i=0; i<count; i++ ){
			arr.push("0");
		}
		return arr.join("")+s;
	}
	return s;
};
/**
 * 
 */
jQuery.cutDate = function(date, type)
{
	var y 	= date.getFullYear();
	var m 	= date.getMonth();
	var d 	= date.getDate();
	var h 	= date.getHours();
	var mi 	= date.getMinutes();
	var s 	= date.getSeconds();
	var ms 	= date.getMilliseconds();
	
	if( type=="year" ){
		return new Date(y);
	}
	if( type=="month" ){
		return new Date(y,m);
	}
	if( type=="date" || type=="day" ){
		return new Date(y,m,d);
	}
	if( type=="hour" ){
		return new Date(y,m,d,h);
	}
	if( type=="minute" ){
		return new Date(y,m,d,h,mi);
	}
	if( type=="second" ){
		return new Date(y,m,d,h,mi,s);
	}
	return new Date(y,m,d,h,mi,s,ms);
};
/**
 * 将一Date类型的对象，转换为一个 "yyyy-MM-dd HH:mm:ss.SSS" 这样的字符串
 */
jQuery.dateToString = function( date, format ){
	format = (format==null) ? "yyyy-MM-dd HH:mm:ss" : format;
	//
	var y 	= date.getFullYear();
	var m 	= date.getMonth()+1;
	var d 	= date.getDate();
	var h 	= date.getHours();
	var mi 	= date.getMinutes();
	var s 	= date.getSeconds();
	var ms 	= date.getMilliseconds();
	//
	format = format.replace("yyyy",	$.numToString(y,4));
	format = format.replace("MM",	$.numToString(m,2));
	format = format.replace("dd",	$.numToString(d,2));
	format = format.replace("HH",	$.numToString(h,2));
	format = format.replace("mm",	$.numToString(mi,2));
	format = format.replace("ss",	$.numToString(s,2));
	format = format.replace("SSS",	$.numToString(ms,3));
	return format;
};
/**
 * 将字符串根据格式,如"yyyy-MM-dd HH:mm:ss.SSS"转换为日期
 */
jQuery.strToDate = function(str, format){
	format = (format==null) ? "yyyy-MM-dd HH:mm:ss.SSS" : format;
	//
	var y_idx 	= format.indexOf("yyyy");
	var m_idx 	= format.indexOf("MM");
	var d_idx 	= format.indexOf("dd");
	var h_idx 	= format.indexOf("HH");
	var mi_idx 	= format.indexOf("mm");
	var s_idx 	= format.indexOf("ss");
	var ms_idx 	= format.indexOf("SSS");
	//
	var y=0, m=0, d=0, h=0, mi=0, s=0, ms=0;
	//
	if( y_idx==-1 ){	//format没有yyyy（年），那么必须有HH（小时）
		if( h_idx==-1 ){
			throw "转换失败str["+str+"],format["+format+"]";
		}
		var date = new Date();
		y = date.getFullYear();
		m = date.getMonth()+1;
		d = date.getDate(); 
		h = new Number(str.substring(h_idx,h_idx+2));
		if( mi_idx==-1 ){
			return new Date(y,m,d,h);
		}
		mi = new Number(str.substring(mi_idx,mi_idx+2));
		if( s_idx==-1 ){
			return new Date(y,m,d,h,mi);
		}
		s = new Number(str.substring(s_idx,s_idx+2));
		if( ms_idx==-1 ){
			return new Date(y,m,d,h,mi,s);
		}
		ms = new Number(str.substring(ms_idx,ms_idx+3));
		return new Date(y,m,d,h,mi,s,ms);
	} else {				//format有yyyy（年）
		y = new Number(str.substring(y_idx,y_idx+4));
		if( m_idx==-1 ){
			return new Date(y);
		}
		m = new Number(str.substring(m_idx,m_idx+2))-1;
		if( d_idx==-1 ){
			return new Date(y,m);
		}
		d = new Number(str.substring(d_idx,d_idx+2));
		if( h_idx==-1 ){
			return new Date(y,m,d);
		}
		h = new Number(str.substring(h_idx,h_idx+2));
		if( mi_idx==-1 ){
			return new Date(y,m,d,h);
		}
		mi = new Number(str.substring(mi_idx,mi_idx+2));
		if( s_idx==-1 ){
			return new Date(y,m,d,h,mi);
		}
		s = new Number(str.substring(s_idx,s_idx+2));
		if( ms_idx==-1 ){
			return new Date(y,m,d,h,mi,s);
		}
		ms = new Number(str.substring(ms_idx,ms_idx+3));
		return new Date(y,m,d,h,mi,s,ms);
	}
};
/**
 * 将日期字符串从一种格式转化为另一种格式，如：
 * 
 */
jQuery.formatDateString = function(dateString, format1, format2)
{
	var date = $.strToDate(dateString, format1);
	return $.dateToString(date, format2);
};
// 用于keypress事件, charCode跟ascii表对应
jQuery.charCodeToChar = function( charCode ){
	// 去除一些无用的charCode和功能键
	if( charCode<32 || charCode>127 ){	// 32是' ', 
		return null;
	}
	return String.fromCharCode(charCode);
};
//
jQuery.myUrlEncoder = function( str ){
	return encodeURIComponent(str).replaceAll("-", "%2D").replaceAll("%", "-");
};
//
jQuery.myUrlDecoder = function( str ){
	return decodeURIComponent(str.replaceAll("-", "%"));
};
//
jQuery.randomString = function(len){
	if( len==null ){
		len = 32;
	}
	var chars = [ 
			'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
			'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
	];
	var result = [];
	for( var i=0; i<len; i++ ){
		result.push( chars[ Math.floor( Math.random() * chars.length ) ] );
	}
	return result.join("");
};
/**
 * 将一个对象转换为一个json字符串
 */
jQuery.toJSONString = function(obj){
	if( obj==null ){
		return "null";
	}
	if( typeof(obj)=="boolean" ){
		return obj;
	}
	if( typeof(obj)=="string" ){
		return '"'+obj.replaceAll("\"","\\\"").replaceAll("\\\\","\\\\")+'"';	// 左边是正则，右边是转义字符
	}
	if( typeof(obj)=="number" ){
		return ""+obj;
	}
	if( obj instanceof Array ){
		var result = [];
		for( var i=0; i<obj.length; i++ ){
			result.push( jQuery.toJSONString(obj[i]) );
		}
		return "["+result.join(",")+"]";
	}
	if( typeof(obj)=="object" ){
		var result = [];
		for( var p in obj ){
			result.push('"'+p+'":'+jQuery.toJSONString(obj[p]));
		}
		return "{"+result.join(",")+"}";
	}
	return "\"^_^呵呵,json转换出错了^_^\"";
};
//
jQuery.arrayToMap = function(arr){
	var map = {};
	for( var i=0; i<arr.length; i++ ){
		map[arr[i]] = arr[i];
	}
	return map;
};
/**
 * 获取页面所在的应用名
 */
jQuery.getAppName = function(pWid){
	var win = (pWid!=null) ? pWid : window;
	var path = win.location.pathname;
	if( path.indexOf("/")==-1 ){
		return path;
	}else{
		if( path.startsWith("/") ){
			var idx = path.indexOf("/",1);
			return path.substring(1,idx);
		}else{
			var idx = path.indexOf("/");
			return path.substring(0,idx);
		}
	}
};
/**
 * 获取页面所在的上下文
 */
jQuery.getContextPath = function(pWid){
	return "/"+$.getAppName(pWid);
};
/**
 * 返回一个对象的所有属性串起来的字符串
 */
jQuery.getAllProperties = function(obj){
	var arr = [];
	for( var p in obj ){
		arr.push( p+" : "+obj[p] );
	}
	return arr.join("\n");
};
/**
 * 返回一个对象的所有属性键串起来的字符串
 */
jQuery.getAllKeys = function(obj,connStr){
	var arr = [];
	for( var p in obj ){
		arr.push( p );
	}
	return arr.join( (connStr==null) ? "," : connStr );
};
/**
 * 传进来一个form对象,将这个form对象的元素转成一个bean
 */
jQuery.formToBean = function(form, ignoreButton){
	if( ignoreButton==null || ignoreButton==true) {
		ignoreButton = true;
	} else {
		ignoreButton = false;
	}
	var object = {};
	var elements = form.elements;
	for (var i = 0; i < elements.length; i++ ) {
		var element = elements[i];
		switch (element.type) {
		case "radio" : 
			if (element.checked) {object[element.name]=element.value;}
			break;
		case "checkbox" : 
			if (!object[element.name]) {object[element.name] = new Array();};
			if (element.checked) {object[element.name].push(element.value);}
			break;
		case "select-one" : 
			var value = '', opt, index = element.selectedIndex;
			if (index >= 0) {
				opt = element.options[index];
				value = opt.value;
				if (!value && !('value' in opt)) value = opt.text;
			}
			object[element.name] = value;
			break;
		case "select-multiple" :
			if (!object[element.name]) {object[element.name] = new Array();};
			for (var j = 0; j < element.options.length; j++) {
				var opt = element.options[j];
				if (opt.selected) {
					var optValue = opt.value;
					if (!optValue && !('value' in opt)) optValue = opt.text;
					object[element.name].push(optValue);
				}
		    }
		    break;
		default : 
			if (ignoreButton) {
				if (element.type!="submit" && element.type!="button" && element.type!="reset") {
					object[element.name] = element.value;
				}
			} else {
				object[element.name] = element.value;
			}
			break;
		}
	}
	return object;
};
/**
 * 对map的key进行排序,然后返回一个list
 */
jQuery.sortMapKey = function(map){
	var arr = [];
	for( var key in map ){
		arr.push(key);
	}
	arr.sort();
	return arr;
};
/**
 * 打印页面
 */
jQuery.printPage = function(){
	window.print();
};
/**
 * 打印预览
 * <OBJECT classid="CLSID:8856F961-340A-11D0-A96B-00C04FD705A2" height="0" id="wb" name="webBrowser" width="0"></OBJECT>
 */
jQuery.printPreview = function(webBrowser){
	if($.browser.msie){
		webBrowser.execwb(7,1);
	} else {
		$.printPage();
	}
};
/**
 * 
 */
jQuery.setRadioGroupValue = function(groupName, value)
{
	$(":radio[name='"+groupName+"'][value='"+value+"']").prop("checked", true);
};




