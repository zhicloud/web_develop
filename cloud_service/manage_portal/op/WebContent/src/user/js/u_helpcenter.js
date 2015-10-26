/* JavaScript Code */
$(function(){
    initLoadData();
    $('#subNav a.hc-link').on('click',function(){
        var datahref = $(this).attr("data-href");
        if(datahref){
            $(this).addClass('current').parent().siblings().find('a').removeClass('current');
            $(this).addClass('current').parents('.m-tab').siblings().find('a').removeClass('current');
            $(this).addClass('current').parents('.m-tab').siblings().find('h3 a').removeClass('current');
            getContentLoad(datahref);
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
    $('#subNav .m-tab a.hc-link').removeClass("current");
    $('#subNav .m-tab a.hc-link[data-href="'+href+'"]').addClass('current');
}

/**
 * 页面加载
 * @author songgk
 * @parm 参数id
 * @return
 */
function getContentLoad(id){
    if(!id) return false;
    var url = propath+'/src/common/tpl/helpcenter/'+id+'.html';
    try{
        $("#getContents").load(url);
    }catch(e){
        //console.log(e);
    }
}