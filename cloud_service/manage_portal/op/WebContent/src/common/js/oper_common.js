/**
 * JavaScript oper_common.js
 * SONGGK
 */
$(function(){
	$('.nav-list .nav-item>a').on('click',function(){
		if($(this).attr('data-dropdown') == 1){
			if($(this).hasClass('current')){
				$(this).removeClass('current');
				$(this).find('i.icon-arrow').addClass('icon-arrow-right').removeClass('icon-arrow-bottom');
				$(this).parent().find('ul').slideUp(200);
			}else{
				$('.submenu').slideUp(200).parent().find('> a').removeClass('current');
				$(this).addClass('current');
				$(this).find('i.icon-arrow').addClass('icon-arrow-bottom').removeClass('icon-arrow-right');
				$(this).parent().find('ul').slideDown(200);
			}
		}
	})
})

/*$(function(){
	$('a.menuParentItem').on('click',function(){
		var dataState = $(this).attr('data-state');
		if(dataState == 1){
			if($(this).hasClass('current')){
				$(this).removeClass('current');
				$(this).find('i.icon-arrow').addClass('icon-arrow-right').removeClass('icon-arrow-bottom');
				$(this).parent().find('ul').slideUp(200);
			}else{
				$('.submenu').slideUp(200).parent().find('a.menuParentItem').removeClass('current');
				$(this).addClass('current');
				$(this).find('i.icon-arrow').addClass('icon-arrow-bottom').removeClass('icon-arrow-right');
				$(this).parent().find('ul').slideDown(200);
			}
		}
	})
	
	$('.nav-item>a').each(function(){
        $this = $(this);
		if($this[0].href == String(window.location)){
			$(this).addClass('current').parent().siblings().find('a.menuParentItem').removeClass('current');
		}
	});
	
	$("ul.submenu li>a").each(function(){  
        $this = $(this);
        if($this[0].href == String(window.location)){  
            $this.parents('.submenu').slideDown(200);
            $this.addClass("current");
            $this.parents('.nav-item').find('a.menuParentItem').addClass('current');
            $this.parents('.nav-item').siblings().find('a.menuParentItem').removeClass('current');
        }  
    });
})*/



