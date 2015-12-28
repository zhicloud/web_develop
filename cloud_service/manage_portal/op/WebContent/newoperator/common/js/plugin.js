/**
 * Created by sgaoke on 2015/8/17.
 */
//选项卡
;(function($){
    $.fn.zc_tab = function(options){
        var defaults = {
            tab : ['.tabs','li'],
            content : ['.container','.content'],
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
    			$('.pop-right-slide').parent().height($('.pop-right-slide').outerHeight());
            });
        });
    };
})(jQuery);

