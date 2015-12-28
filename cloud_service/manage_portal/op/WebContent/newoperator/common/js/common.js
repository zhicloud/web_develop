
$(function(){
	//模拟checkbox
	$('.mn-checkbox').on('click',function(){
	    if($(this).siblings("input[type='checkbox']").prop("checked")){
	        $(this).removeClass('cur');
	        $(this).siblings("input[type='checkbox']").attr('checked',false);
	    }else{
	        $(this).addClass('cur');
	        $(this).siblings("input[type='checkbox']").attr('checked',true);
	    }
	});
	
})


















