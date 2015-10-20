/**
 * 检查是否session超时
 */
function checkImageName(){ 
	jQuery.ajax({
        url: path+'/main/checklogin',
        type: 'post', 
        dataType: 'json',
        timeout: 10000,
        async: true,
        error: function()
        {
            alert('Error!');
        },
        success: function(result)
        {
        	if(result.status == "fail"){
        		jAlert('登录或以超时，请重新登录','提示', function(r) {
	    			top.document.location.href = path;
	    			});
        		return false;
        	}else{
        		return true;
        	} 
        }
     });
}