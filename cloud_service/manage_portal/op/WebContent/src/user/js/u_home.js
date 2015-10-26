/**
 * Created by songgk on 2015/05/18.
 */

;(function($){
	$('.g-banner').unslider({
		speed: 500,
		delay: 6000,
		arrows: true,
		fluid: true,
		dots: true
	});
    
	$(".iproduct").hover(function(event){
		$(this).find(".iproducthover").css("top","0");
	},function(event){
		$(this).find(".iproducthover").css("top","-152px");
	});
	
	$("p.arrows,.g-banner a").hover(function(){
		$(".next").css("opacity","1");
		$(".prev").css("opacity","1");
	},function(){
		$(".next").css("opacity","0.5");
		$(".prev").css("opacity","0.5");
	});
})(jQuery);





