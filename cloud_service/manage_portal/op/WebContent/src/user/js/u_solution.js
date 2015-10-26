
$(function(){
	$('a[href^="#"]').click(function() {
        var target = $($(this).attr('href'));
        var pos = target.offset();
        var scrollTop = pos.top;
        if(pos) {
            $('html, body').animate({
                scrollTop: scrollTop,
                scrollLeft: pos.left
            }, 1000);
        }
        return false;
    });
	
	$("#mag_plat").on("click",function(){
		$("#m1 ul li").removeClass("active");
		$("#s1").css("left",0);
	});
	
	$("#cld_stora").on("click",function(){
		$("#m2 ul li").removeClass("active");
		$("#s2").css("left",0);
	});
	
	$("#cld_dest").on("click",function(){
		$("#m3 ul li").removeClass("active");
		$("#s3").css("left",0);
	});
	$("#cld_audit").on("click",function(){
		$("#m4 ul li").removeClass("active");
		$("#s4").css("left",0);
	});
})

function gotoSolution(x,y){
	$("#m"+x+" ul li").removeClass("active");
	$("#m"+x+" ul li:eq("+(y-2)+")").addClass("active");
	$("#s"+x).css("left",(y-1)*(-100)+"%");
}