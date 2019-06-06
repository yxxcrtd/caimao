
var _cnf = {
    bkMusicIsOpen: 1,
    firstTapPlay: true
};

$(function(){
    var _imgarr = [];
    $('.swiper-slide').each(function(){
        if($(this).data('bg') && $(this).data('bg') != ''){
            if (!$(this).hasClass('swiper-slide-duplicate')) _imgarr.push($(this).data('bg'));
        } else {
            var _imgobj = $(this).find('img').first();
            if(_imgobj && _imgobj.length > 0) {
                var _simg = _imgobj.attr('src');
                if (_simg != undefined ) {
                    //$(this).css({ 'background-image' : 'url(' + _simg + ')', 'background-size': '100% 100%' });
                    if (!$(this).hasClass('swiper-slide-duplicate')) _imgarr.push(_simg);
                } else {
                    if (!$(this).hasClass('swiper-slide-duplicate')) _imgarr.push('/misc/images/walbum/img.jpg');
                }
            }else{
                if (!$(this).hasClass('swiper-slide-duplicate')) _imgarr.push('/misc/images/walbum/img.jpg');
            }
        }
    });
    var _dirstr = '<ul class="f-cb">';
    for(var _i in _imgarr){
        _dirstr += '<li><img class="" alt="" src="'+_imgarr[_i]+'" data-index="'+_i+'" /></li>';
    }
    _dirstr += '</ul>';
    if($('#top-leftmenu').length > 0) {
        $('#top-leftmenu').html(_dirstr);
        $('#top-leftmenu ul').delegate('li img', 'click', function(){
            var _d = $(this).data('index');
            if(!isNaN(_d)) {
                _d = parseInt(_d);
                if(_d >= 0) _swiper.swipeTo(_d);
            }
        });
    }

    if(_cnf.bkMusicIsOpen) {
        if($('.ui-music audio').length > 0) {
            var _player = $('.ui-music audio').get(0), isPlaying = false, isPause = false;

            if( _player != undefined) {
                $('.ui-music').bind({
                    'click': function(e){
                        e.preventDefault();
                        if(!_player.paused) {
                            isPause = true;
                            isPlaying = false;
                            $('.ui-music').removeClass('playing');
                            $(_player).attr('data-play', 0);
                            _player.pause();
                        } else {
                            isPause = false;
                            isPlaying = true;
                            $('.ui-music').addClass('playing');
                            $(_player).attr('data-play', 1);
                            _player.play();
                        }
                    }
                });
                _player.addEventListener('loadeddata', function(a){
                    $('.ui-music').addClass('playing');
                    if(_player.paused){ _player.play(); }
                    if(_player.paused){ $('.ui-music').removeClass('playing'); }
                });

                if(_cnf.firstTapPlay) {
                    if(!isPlaying && !isPause) {
                        $('body').bind('mousedown touchstart', function(){
                            if(_player.readyState >= 2) {
                                _player.play();
                                if(!$('.ui-music').hasClass('playing')) { $('.ui-music').addClass('playing'); }
                                $('body').unbind('mousedown touchstart');
                            }
                        });
                    }
                }
            }
        }
    }

    //$("#baoming").html(inname);

    var _urlbase = location.origin + location.pathname;
    _tid = getQueryString('tid');
    _keyid = _tid;
    if(getQueryString('keyid') != null) _keyid = getQueryString('keyid');
    if($('#keyid').length > 0) _keyid = $('#keyid').data('keyid');
    if(getQueryString('refid') != null) {
        _refid = getQueryString('refid');
        $.get('weiform.php?act=getinfo', { 'keyid': _keyid, 'refid': _refid }, function(json) {
            if(json.error == 0 && typeof(json.info) != "undefined") {
                if($('.ajaxloadNum').length > 0) $('.ajaxloadNum').html(json.refnum);
                for(var k in json.info) {
                    if($('.ajaxload-' + k).length > 0) {
                        $('.ajaxload-' + k).html(json.info[k]);
                    }
                }
            }
        });
        if($('form').length > 0) $('form').attr('action', '/walbum/save?refid=1&return=' + encodeURIComponent(_urlbase + '?tid=' +_keyid + '&keyid=' + _keyid));
    }else if($('[class^="ajaxload"]').length > 0) {
        _tid = getQueryString('tid');
        if($('form').length > 0) $('form').attr('action', '/walbum/save?refid=1&return=' + encodeURIComponent(_urlbase + '?tid=' + _keyid + '&keyid=' + _keyid));
    }

    if($('.effect').length > 0) {
        var _p = {
            'luowu' : '/misc/js/jquery/jquery.let_it_snow.js',
            'zhiwen' : '/misc/js/jquery/jquery.fingertouch.js',
            'tumo' : '/misc/js/jquery/jquery.escratch.js',
            'shuqian' : '/misc/js/jquery/jquery..js'
        };
        $('.effect').each(function(idx, ele){
            var _n = $(ele).data('name');
            var _c = $(ele).data('config');
            switch(_n) {
                case "luowu":
                    if("let_it_snow" in $(ele)) {
                        $(ele).let_it_snow(_c);
                    } else {
                        $.getScript(_p[_n], function() {
                            $(ele).let_it_snow(_c);
                        });
                    }
                    break;
                case "zhiwen":
                    if("fingertouch" in $(ele)) {
                        $(ele).fingertouch(_c);
                    } else {
                        $.getScript(_p[_n], function() {
                            $(ele).fingertouch(_c);
                        });
                    }
                    break;
                case "tumo":
                    if("escratch" in $(ele)) {
                        $(ele).escratch(_c);
                    } else {
                        $.getScript(_p[_n], function() {
                            $(ele).escratch(_c);
                        });
                    }
                    break;
                case "shuqian":
                    break;
                case "":
                default:
                    break;
            }
        });
    }

    if($('.vedio_link').length > 0) {
        $('.vedio_link').each(function(idx, ele){
            $(this).bind('click', function(e){
                e.preventDefault();
                var _u = $(this).data('src');
                var _p = $(this).parent().attr('id');
                u_video(_u, _p);
            });
        });
    }

    var u_video = function(url, pid) {
        if(url) {
            var _e = $('<div id="video_wrp">');
            var _h = $(window).height() - 46;
            _e.css({'width':'100%', 'height':'100%', 'z-index':'1000000', 'top':0, 'left':0, 'position':'absolute', 'background':'rgba(0,0,0,0.5)'});
            _e.addClass('video_mask');
            _e.html('<iframe width="100%" height="'+_h+'" style="margin-top:40px;" frameborder="0" allowfullscreen="" src="'+url+'"></iframe>');
            $('<a id="close_video" class="v_c_mask"> </a>').appendTo(_e);
            $('body').append(_e);
            $('#close_video').bind('click', function(){ u_closemask(pid); });
            if($('#' + pid).length > 0) { $('#' + pid).hide(); }
            if(_player && !_player.paused) {
                $('.ui-music').removeClass('playing');
                _player.pause();
            }
        }
    }

    var u_closemask = function(pid) {
        if($('#video_wrp').length > 0) { $('#video_wrp').remove(); }
        if($('#' + pid).length > 0) { $('#' + pid).show(); }
    }

});

$(document).ready(function(){
    $(".frm-location").focus(function(){
        $(".frm-location").html("正在获取您的位置信息");
        getnavlocation();
    });
    var _a = {};
    $('.swiper-slide-active .ptc').children().each(function(idx, ele){
        _a[idx] = $(ele).attr('class');
        $(ele).addClass('animated1');
        $(ele).removeClass();
    });
    setTimeout(function() {
        //$('#loading').hide();
        $('.swiper-slide-active .ptc').children().each(function(idx, ele){
            $(ele).addClass(_a[idx]);
        });
    },2000);


    $(".jmselect").focus(function(event) {
        // alert("123");
        /* Act on the event */
        $(".jmselect").jmselect();
    });
});

function getQueryString(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}




function getnavlocation()
{
    if (navigator.geolocation)
    {
        $("#location").html("正在获取您的geo位置信息");
        navigator.geolocation.getCurrentPosition(showPosition,showError);
    }
    else{
        //$("#location").html("Geolocation is not supported by this browser.");
        $("#location").html("无法获取到您的位置，您可以手动修正位置信息");
    }

}
function showPosition(position)
{
    //$("#location").html("position.coords.latitude");
    $("#location").html("Latitude: " + position.coords.latitude + "<br />Longitude: " + position.coords.longitude );
    $.post("getlocation.php",{lx:position.coords.latitude,ly:position.coords.longitude},function(msg){
        if(msg.status=="OK"){
            $("#location").html("获取到您的位置是"+msg.result.formatted_address+"，您可以手动修正位置信息");
            $("input[name=location]").val(msg.result.formatted_address);
        }else{
            alert("无法获取到您的位置，您可以手动修正位置信息");
        }

    },"json");
}
function showError(error)
{
    switch(error.code)
    {
        case error.PERMISSION_DENIED:
            $("#location").html("获取位置失败，请手动增加地址。");
            break;
        case error.POSITION_UNAVAILABLE:
            $("#location").html("获取位置失败，请手动增加地址。");
            break;
        case error.TIMEOUT:
            $("#location").html("获取位置失败，请手动增加地址。");
            break;
        case error.UNKNOWN_ERROR:
            $("#location").html("获取位置失败，请手动增加地址。");
            break;
    }
}

// is_weixn_62();
function is_weixn_62(){
    var ua = navigator.userAgent.toLowerCase();
    if(ua.match(/MicroMessenger/i)=="micromessenger") {
        if(ua.indexOf('6.0.2.58')>0) {
            return true;
        }
    }
    return false;
}
        