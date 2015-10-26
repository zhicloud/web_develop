/**
 * Created by songgk on 2015/5/15.
 */
//选项卡
;(function($){
    $.fn.zc_tab = function(options){
        var defaults = {
            tab : ['.tabs','li'],
            content : ['.containers','.content'],
            current : 'selected',
            classname : "show",
            event     : 'click'
        };
        var o = $.extend({},defaults,options);
        var event = o.event ? o.event : 'hover';
        return this.each(function(){
            var self = $(this),
                tabs = self.find(o.tab[0]),
                li   = tabs.find(o.tab[1]),
                wrap = self.find(o.content[0]),
                content = wrap.find(o.content[1]);
            li.die().bind(event,function(){
                var index = $(this).index();
                $(this).addClass(''+o.current+'').siblings().removeClass(''+o.current+'');
                content.eq(index).css({display:"block","visibility":"visible"}).addClass(''+ o.classname+'').siblings().removeClass(''+ o.classname+'').css({display:"none","visibility":"hidden"});
                	// 滚动              
                var top = $(this).parents('#ju_detail').position().top;
                $('html,body').animate({scrollTop:top},0);
            });
        });
    };
})(jQuery);
//字数统计
;(function($){
    $.fn.zc_words_deal = function(options){
        var defaults = {
            defaults : 0,
            maxwords : 260,
            doms     : '#word-count',
            maxdoms  : '#maxdoms'
        };
        var o = $.extend({},defaults,options);
        function getLength(str){
            str = $.trim(str);
            return str.replace(/[^\x00-\xff]/g,"aa").length; //中文 = 2字节
        }
        return this.each(function(){
            var self = $(this),
                dom = (typeof o.doms === "string") ? $(''+ o.doms+'') : o.doms,
                maxDoms = (typeof o.maxdoms === "string") ? $(''+ o.maxdoms+'') : o.maxdoms;
            dom.text(parseInt(o.defaults));
            maxDoms.text(parseInt(o.maxwords));
            self.bind('focus keyup input paste',function(e){
                var textval = $.trim(self.val());
                var len = textval.length;
                if(len > parseInt(o.maxwords)){
                    self.val(textval.substring(0,parseInt(o.maxwords)));
                }
                var len2 = len >= parseInt(o.maxwords) ? parseInt(o.maxwords) : len;
                dom.text(len2);
            });
        });
    };
})(jQuery);
//模拟HTML5 number属性
;(function($){
    $.fn.nmw_number = function(options){
        var defaults = {
            filter   : true, //如果开启，过滤其他字符
            isInt    : false, //只能是整数
            readonly : false //文本框只读
        };
        var o = $.extend({},defaults,options);
        return this.each(function(i){
            var self = $(this),
                vals = self.attr("value"),
                names = self.attr("name") || i,
                min = self.attr("min") || 1,
                max = self.attr("max") || 99,
                step = self.attr("step") || 1;
            vals = !isNaN(vals) ? parseFloat(vals) : 0;
            min = !isNaN(min) ? parseFloat(min) : 1;
            max = !isNaN(max) ? parseFloat(max) : 99;
            step = !isNaN(step) ? parseFloat(step) : 1;
            if(o.isInt){
                vals = parseInt(vals);
                min  = parseInt(min);
                max  = parseInt(max);
                step = parseInt(step);
            }
            vals = vals ? vals : 1;
            var readonly = o.readonly ? "readonly" : "";
            var filter   = o.filter   ? " number"  : ''; //依托于common.js
             var maxlen = parseInt(max).toString().length;
            var newHtml = '<span class="p-number f-usn" name="number-input-wrap-'+i+'">' +
                '<a class="sub"><i class="sp_icons ssub"></i></a>' +
                '<input type="text" maxlength="'+maxlen+'" value="'+vals+'" name="numberplug-'+names+'" class="tn mf05 mr05 '+filter+'" '+readonly+' data-ui="numberplug">' +
                '<a class="add"><i class="sp_icons sadd"></i></a>' +
                '</span>';
            self.before(newHtml);
            var nums = vals;
            var numberWrap = $('[name="number-input-wrap-'+i+'"]'),
                subBtn = $('.sub',numberWrap),
                addBtn = $('.add',numberWrap);
            var input =  $('input',numberWrap);
            subBtn.live('click',function(e){
                nums = !isNaN(input.val()) ? parseFloat(input.val()) : 1;
                if(o.isInt) nums = parseInt(nums);
                nums = nums ? nums : 1;
                if(nums > min){
                    nums = parseFloat(nums - step);
                }
                input.val(nums);self.val(nums);
            });
            addBtn.live('click',function(e){
                nums = !isNaN(input.val()) ? parseFloat(input.val()) : 1;
                nums = nums ? nums : 1;
                if(o.isInt) nums = parseInt(nums);
                if(nums < max){
                    nums = parseFloat(nums + step);
                }
                input.val(nums);self.val(nums);
            });
            input.change(function(){
                var _me = $(this);
                var _v = $.trim(_me.val());
                var num = _v;
                if(!/^(\+|-)?\d+$/.test(_v) || 0 >= _v){
                    num = min;
                }else if(min > _v){
                    num = min;
                }else if(max < _v){
                    num = max;
                }
                _me.val(num);
            });
        });
    };
})(jQuery);
/**
 * 模拟HTML5 number属性
 * @param a
 * @param b
 * 2015/02/02
 */
;(function($){
    var index = 0;
    function InputNumber(el,options){
        (typeof el === "string") && (el = $(el));
        this.el = el;
        index++;
        this.index = index;
        var config = this.config;
        this.config = $.extend({},config,options);
        this.params = {
            value  : ($.trim(el.val()) && !isNaN($.trim(el.val()))) ? $.trim(el.val()) : 1,
            min    : ($.trim(el.attr("min")) && !isNaN($.trim(el.attr("min")))) ? $.trim(el.attr("min")) : 1,
            max    : ($.trim(el.attr("max")) && !isNaN($.trim(el.attr("max")))) ? $.trim(el.attr("max")) : 99,
            step   : ($.trim(el.attr("step")) && !isNaN($.trim(el.attr("step")))) ? $.trim(el.attr("step")) : 1
        };
        this.count = ((this.params.value >= this.params.min) && (this.params.value <= this.params.max))
                   ? this.params.value : this.params.min;
        this.render();
        this.callback();
        return this;
    }
    InputNumber.prototype.config = {
        filter : true,  //过滤非数字
        readonly : false, //文本框只读
        callback : function(){} //回调函数
    };
    InputNumber.prototype.render = function(){
        var me = this, el = me.el, o = me.config, param = me.params;
        var maxLen = el.attr("maxlength") ? parseInt(el.attr("maxlength")) : param.max.toString().length;
        var name   = el.attr("name") ? ("numberplug-"+el.attr("name")) : "numberplug-"+me.index;
        var readonly = o.readonly ? "readonly" : "";
        var newEl = '<span class="p-number f-usn" id="ui-'+name+'">' +
            '<a class="sub"><i class="sp_icons ssub"></i></a>' +
            '<input type="text" maxlength="'+maxLen+'" value="'+param.value+'" name="'+name+'" id="'+(name+index)+'" class="tn mf05 mr05" '+readonly+' data-ui="numberplug">' +
            '<a class="add"><i class="sp_icons sadd"></i></a>' +
            '</span>';
        el.before(newEl);
        this.id = name;
        return this.id;
    };
    InputNumber.prototype.callback = function(){
        var me = this, o = me.config, param = me.params, id = me.id;
        var $el = $('#ui-'+id),
            add = $('.add',$el),
            sub = $('.sub',$el),
            fields = $('input[name="'+id+'"]',$el);
        add.click(function(){
            var _v = parseInt($.trim(fields.val()));
            var _c = _v + parseInt(param.step);
            if(_c <= param.max){
                me.count = parseInt(_c);
            }
            fields.val(me.count);
            (typeof o.callback === "function") && (o.callback.call(me));
        });
        sub.click(function(){
            var _v = parseInt($.trim(fields.val()));
            var _c = _v - parseInt(param.step);
            if(_c >= param.min){
                me.count = parseInt(_c);
            }
            fields.val(me.count);
            (typeof o.callback === "function") && (o.callback.call(me));
        });
        fields.change(function(e){
            var _v = $.trim(fields.val());
            me.count = _v;
            if(!/^(\+|-)?\d+$/.test(_v) || 0 >= _v){
                me.count = 1;
            }else if(param.min > _v){
                fields.val(param.min);
                me.count = param.min;
            }else if(param.max < _v){
                fields.val(param.max);
                me.count = param.max;
            }
            fields.val(me.count);
            (typeof o.callback === "function") && (o.callback.call(me));
        });
    };
    window.inputNumber = function(el,settings){
        return new InputNumber(el,settings);
    };
    $.fn.inputNumber = function(settings){
        return new InputNumber($(this),settings);
    };
})(jQuery);
//过滤输入特殊字符
function stripscript(s) {
    s = $.trim(s);
    var pattern = new RegExp("[`~!#$^&*()=|{}':;'\\[\\]<>/?#]")
    var rs = "";
    for (var i = 0; i < s.length; i++) {
        rs = rs + s.substr(i, 1).replace(pattern, '');
    }
    return rs;
}
//过滤HTML标签
function removeHTMLTag(str) {
    str = str.replace(/<\/?[^>]*>/g,'');
    str = str.replace(/[ | ]*\n/g,'\n');
    return str;
}
//只能输入数字
function allowNum(s){
    s = $.trim(s);
    s = s.replace(/[^-\d]/g,'');
   /* var str = s.replace(/[^\d]/g,'');*/
   /* s = parseInt((s=s.replace(/\D/g,''))==''?'0':s,10);*/
    return s;
}
//获取文本长度
//中文 = 2字节
function getLength(str){
    return $.trim(str).replace(/[^\x00-\xff]/g,"aa").length;
};
//获取URL参数
function getUrlParam(url, param){
	var re = new RegExp("(\\\?|&)" + param + "=([^&]+)(&|$)", "i");
	var m = url.match(re);
	if (m)
		return m[2];
	else
		return '';
}