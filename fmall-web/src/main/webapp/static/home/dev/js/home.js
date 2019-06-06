/**
 * Created by Administrator on 2015/11/10.
 */

$(function(){
    var ali_box=$(".page_list");
    var aslide_img=$('.banner_box b');
    var iNow=0;
    var list_num = aslide_img.length;
    var banner_list = "";
    for(var l = 0; l<list_num; l++){
        if(l == 0){
            ali_box.append('<li class="cur" style="width: 28px;"></li>');
        }else{
            ali_box.append('<li></li>');
        }
    }
    var ali=$('.banner li');
    ali.each(function(index){
        $(this).click(function(){
            var o_index = ali_box.find('.cur').index();
            slide(index,o_index);
        })
    });

    function slide(index,o_index){
        if(!arguments[1]) o_index = "";
        var _index;
        iNow=index;
        ali.eq(index).addClass('cur').siblings().removeClass();
        ali.eq(index).stop().animate({width:28},400).siblings().stop().animate({width:10},400).removeClass().stop().animate({width:10},400);
        aslide_img.eq(index).stop().animate({opacity:1},600).css({"z-index":5}).siblings().stop().animate({opacity:0},600).css({"z-index":3});
    }
    function autoRun(){
        iNow++;
        if(iNow==ali.length){
            iNow=0;
        }
        slide(iNow);
    }
    var timer=setInterval(autoRun,4000);
    ali.hover(function(){
        clearInterval(timer);
    },function(){
        timer=setInterval(autoRun,4000);
    });

    aslide_img.hover(function(){
        clearInterval(timer);
    },function(){
        timer=setInterval(autoRun,4000);
    });
});