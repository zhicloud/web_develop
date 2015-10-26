/**
 * Created by songgk on 2015/05/18.
 */

;(function($){
	$(window).scroll(function(){
	    var bodyScrollTop = $(window).scrollTop();
	    var offsetTwoTop = $('.floor_two').offset().top - 300;
	    var offsetThreeTop = $('.floor_three').offset().top - 300;
	    var offsetFourTop = $('.floor_four').offset().top - 300;
	    var offsetFiveTop = $('.floor_five').offset().top - 300;
	    if(bodyScrollTop >= offsetTwoTop){
	        $('.floor_two').addClass("animatp2");
	    }else{
	    		//$('.floor_two').removeClass("animatp2");
	    }
	    if(bodyScrollTop >= offsetThreeTop){
	        $('.floor_three').addClass("animatp3");
	    }else{
	    		//$('.floor_three').removeClass("animatp3");
	    }
	    
	    if(bodyScrollTop >= offsetFourTop){
	        $('.floor_four').addClass("animatp4");
	    }else{
	    		//$('.floor_four').removeClass("animatp4");
	    }
	    if(bodyScrollTop >= offsetFiveTop){
	        $('.floor_five').addClass("animatp5");
	    }else{
	    		//$('.floor_five').removeClass("animatp5");
	    }
	}).trigger('scroll');
})(jQuery);