/**
 * 页面初始化的东东
 * Created by xavier on 15/7/7.
 */

$(document).ready(function() {
    var _userInfo = null;
    var _accountInfo = null;
    var _msgOpen = 0;

    // 是否登录
    _userInfo = Data.getUser();
    if (_userInfo != null) {
        $('#_userName, #index_user_name').html(_userInfo.userRealName != '' ? _userInfo.userRealName : _userInfo.mobile);
        $("#_login").show();
    } else {
        $("#_unlogin").show();
    }

    // 公告信息
    $("#noticeIKnow").click(function () {
        $("#notice").hide();
    });

    $.get("/content/top_notice", function (response) {
        if (response.success) {
            $("#noticeBlock").html("<a href='/content/notice/" + response.data.id + "' target='_blank'>" + response.data.title + "</a>");
            $("#notice").show();
        } else {
            $("#notice").hide();
        }
    });

    //// 资产信息
    //_accountInfo = Data.getAccount();
    //if (_accountInfo != null) {
    //    $('#_accountPoint').show();
    //    $('#_availableAmount').html(CMUTILS.toYuanAndCommas(_accountInfo.avalaibleAmount - _accountInfo.freezeAmount));
    //    $('#_freezeAmount').html(CMUTILS.toYuanAndCommas(_accountInfo.freezeAmount));
    //}

    // 显示消息
    if (_userInfo != null) {
        // 显示消息数量
        var notReadNum = Data.getNotReadMsgNum();
        if (notReadNum > 0) {
            $('#_msgIcons').addClass('icon_info');
        } else {
            notReadNum = '';
        }
        if (notReadNum >= 100) notReadNum = "99+";
        $('#_notReadMsgNum').html(notReadNum);
    }

    $('#_openMsgSwitch').click(function() {
        if (_msgOpen == 0) {
            $.get("/content/msg_read_all", function(){
                $('#_msgIcons').removeClass('icon_info');
                $('#_notReadMsgNum').html('');
                $('#_msgPoint').load("/content/msg_list.html");
            });
            _msgOpen = 1;
        } else {
            $('#_msgPoint').html('');
            _msgOpen = 0;
        }
    });

    $(document).on("click", "#_msgPoint .pagination a", function(){
        var href = $(this).attr("href");
        $('#_msgPoint').load(href);
        return false;
    });



    // 左侧浮动条条
    $("#izl_rmenu").each(function(){
        //$(this).find(".btn-custom").mouseenter(function(){
        //    $(this).find(".custom").fadeIn("slow");
        //});
        //$(this).find(".btn-custom").mouseleave(function(){
        //    $(this).find(".custom").fadeOut("fast");
        //});
        var hoverTimer, outTimer;
        $(this).find(".btn-phone").mouseenter(function(){
            hoverTimer = setTimeout('$("#izl_rmenu").find(".phone").fadeIn("fast")', 200);
            //$("#izl_rmenu").find(".phone").fadeIn("fast");
        });
        $(this).find(".btn-phone").mouseleave(function(){
            if (typeof hoverTimer == 'number') clearTimeout(hoverTimer);
            $("#izl_rmenu").find(".phone").fadeOut("fast");
        });
        $(this).find(".btn-tel").mouseenter(function(){
            hoverTimer = setTimeout('$("#izl_rmenu").find(".tel").fadeIn("fast")', 200);
            //$("#izl_rmenu").find(".tel").fadeIn("fast");
        });
        $(this).find(".btn-tel").mouseleave(function(){
            if (typeof hoverTimer == 'number') clearTimeout(hoverTimer);
            $("#izl_rmenu").find(".tel").fadeOut("fast");
        });
        $(this).find(".btn-top").click(function(){
            $("html, body").animate({
                "scroll-top":0
            },"slow");
        });
    });
    var lastRmenuStatus=false;
    $(window).scroll(function(){//bug
        var _top=$(window).scrollTop();
        if(_top>200){
            $("#izl_rmenu").data("expanded",true);
        }else{
            $("#izl_rmenu").data("expanded",false);
        }
        if($("#izl_rmenu").data("expanded")!=lastRmenuStatus){
            lastRmenuStatus=$("#izl_rmenu").data("expanded");
            if(lastRmenuStatus){
                $("#izl_rmenu .btn-top").slideDown();
            }else{
                $("#izl_rmenu .btn-top").slideUp();
            }
        }
    });
});