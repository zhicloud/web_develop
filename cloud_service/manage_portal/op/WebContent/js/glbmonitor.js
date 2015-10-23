//选项卡
;(function($){
    $.fn.zcloud_tab = function(options){
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
            });
        });
    };
})(jQuery);

//导航高亮
function navHighlight(param){
	if(param == "xtjk"){
		$('.m-tabs li:eq(0) a').addClass('current').parent().siblings().find('a').removeClass('current');
		$('.m-tabs li:eq(0) a').find("img").attr("src","img/"+$('.m-tabs li:eq(0) a').find("img").attr("class")+"_h.png");
	}else if(param == "zygl"){
		$('.m-tabs li:eq(1) a').addClass('current').parent().siblings().find('a').removeClass('current');
		$('.m-tabs li:eq(1) a').find("img").attr("src","img/"+$('.m-tabs li:eq(1) a').find("img").attr("class")+"_h.png");
	}else if(param == "gjsz"){
		$('.m-tabs li:eq(2) a').addClass('current').parent().siblings().find('a').removeClass('current');
		$('.m-tabs li:eq(2) a').find("img").attr("src","img/"+$('.m-tabs li:eq(2) a').find("img").attr("class")+"_h.png");
	}else if(param == "czrz"){
		$('.m-tabs li:eq(3) a').addClass('current').parent().siblings().find('a').removeClass('current');
		$('.m-tabs li:eq(3) a').find("img").attr("src","img/"+$('.m-tabs li:eq(3) a').find("img").attr("class")+"_h.png");
	}
}

//导航跳转
function onSwitch(target){
	if( target.id=="xtjk" ){
		window.location.href = "xtjk.html";
	}else if( target.id=="zygl" ){
		window.location.href = "zygl.html";
	}else if( target.id=="gjsz" ){
		window.location.href = "gjsz.html";
	}else if( target.id=="czrz" ){
		window.location.href = "czrz.html";
	}
}