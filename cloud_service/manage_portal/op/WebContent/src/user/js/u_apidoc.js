/* JavaScript Code */
$(function(){
    initLoadData();
    $('#subNav ul li a').on('click',function(){
        var href = $(this).attr("data-href");
        if(href){
            $(this).addClass('current').parent().siblings().find('a').removeClass('current');
            getContentLoad(href);
        }
    });
    
  //折叠菜单栏
    $('.m-tab h3').on('click',function(){
        var p_par = $(this).parent();
        var i_sp = $(this).find('i.ad_icons1');
        if(i_sp.hasClass('tab-open')){
            p_par.find('ul').slideUp();
            i_sp.removeClass('tab-open').addClass('tab-close');
        }else{
            p_par.find('ul').slideDown();
            i_sp.removeClass('tab-close').addClass('tab-open');
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
    var href = 11;
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
    var url = propath+'/src/common/tpl/apidoc/'+id+'.html';
    try{
        $("#getContents").load(url);
    }catch(e){
        //console.log(e);
    }
}