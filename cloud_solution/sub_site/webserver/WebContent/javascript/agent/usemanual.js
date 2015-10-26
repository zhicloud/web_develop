/* JavaScript Code */
$(function(){
    initLoadData();
    $('#subNav ul li a').on('click',function(){
        var href = $(this).attr("data-href");
        var title = $(this).text();
        if(href){
            $(this).addClass('current').parent().siblings().find('a').removeClass('current');
            getContentLoad(href);
            window.location.hash = href;
        }
    });
    
})
/**
 * 初始化
 * @author songgk
 * @parm 
 * @return
 */
function initLoadData(){
    var hash = window.location.hash; //用来获取或设置页面的标签值
    if(hash){
        hash = hash.substr(1,hash.length);
    }
    var href = 1;
    if(hash != null && hash != undefined && hash !=''){
        href = hash;
    }
    getContentLoad(href);
    $('#subNav ul li a').removeClass("current");
    $('#subNav ul li a[data-href="'+href+'"]').addClass('current');
}

/**
 * 页面加载
 * @author songgk
 * @parm 参数id
 * @return
 */
function getContentLoad(id){
    if(!id) return false;
    var url = a+'/public/html/usemanual/'+id+'.html';
    try{
        $("#getContents").load(url);
    }catch(e){
        //console.log(e);
    }
}