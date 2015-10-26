/**
 * Created by songgk on 2015/05/18.
 */

;(function($){
	//绑定选项卡
    $('#ju_detail').zc_tab({
        tab : ['.m-tabs','li'],
        content : ['.m-container','.m-content'],
        current : 'current',
        classname : "current"
    });
    
    var scrollH;
    var juOffsetTop = $('.ju-sec3').offset().top;
    $(document).scroll(function(){
        scrollH = $(document).scrollTop();
        if(scrollH >= juOffsetTop){
            $("#job-nav").addClass("fixed");
        }else{
            $("#job-nav").removeClass("fixed");
        }
    });
})(jQuery);

