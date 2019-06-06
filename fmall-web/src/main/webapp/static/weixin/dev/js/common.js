/**
 * 后台公共请求的东西
 * Created by Administrator on 2016/1/13.
 */
// @koala-prepend "./jquery-1.11.1.min.js"
/**
 * 加心
 * @param e 加心对象
 * @param shareId   分享ID
 * @param checkOpenId   查看这个人的openId
 */
function optAddLike(e, checkOpenId, shareId) {
    var isLike = $(e).attr('data-isLike');
    if (isLike == '1') return;
    // 先改变加心的样式并加一
    $(e).removeClass('fav').addClass('faved').html();
    $(e).html(parseInt($(e).html())+1);
    var data = {openId : checkOpenId, shareId : shareId};
    $.ajax({
        url : "/weixin/guji/ajax_like.html",
        type : "POST",
        data : data,
        dataType : "json",
        success : function(m) {
            $(e).attr('data-isLike', '1');
        }
    });
    return true;
}

/**
 * 点击关注/已关注的操作
 */
function optFollow() {
    var e = $('#follow');
    var isFollow = $(e).attr('data-isFollow');
    if (isFollow == 'true') {
        $('#mask').show();
    } else {
        optConfFollow();
        var isFollowGuji = $(e).attr('data-isFollowGuji');
        if (isFollowGuji == "0") {
            $('#followOfficial').show();
        }
    }
}
/**
 * 隐藏关注服务号的弹框
 */
function closeFollowAlert() {
    $('#followOfficial').hide();
}

/**
 * 确认要（取消/关注）关注操作
 */
function optConfFollow() {
    var e = $('#follow');
    var isFollow = $(e).attr('data-isFollow');
    var wxId = $(e).attr('data-wxId');
    var checkOpenId = $(e).attr('data-checkOpenId');
    var followOpt = isFollow == 'true' ? 'del' : 'add';
    var data = {openId : checkOpenId, focusWxId : wxId, opt : followOpt};
    $.ajax({
        url : "/weixin/guji/ajax_follow.html",
        type : "POST",
        data : data,
        dataType : "json",
        success : function(m) {
            if (m.success == true) {
                if (isFollow == 'true') {
                    $(e).removeClass('followed').html('关注').attr('data-isFollow', 'false');
                } else {
                    $(e).addClass('followed').html('已关注').attr('data-isFollow', 'true');
                }
            } else {
            }
        }
    });
    optCancelFollow();
}

/**
 * 取消关注选项列表显示
 */
function optCancelFollow() {
    $('#mask').hide();
}


/**
 * 加载大厅后续的推荐列表
 * @param data
 * @param callback
 */
function loadHallList(data, callback) {
    $.ajax({
        url : "/weixin/guji/ajax_hall_list.html",
        type : "POST",
        data : data,
        dataType : "json",
        success : callback
    });
}

/**
 * 加载关注订阅后续的推荐列表
 * @param data
 * @param callback
 */
function loadSubscribeList(data, callback) {
    $.ajax({
        url : "/weixin/guji/ajax_subscribe_list.html",
        type : "POST",
        data : data,
        dataType : "json",
        success : callback
    });
}

/**
 * 模板替换的方法
 * @param str
 * @param data
 * @returns {*}
 */
function rentmpl (str, data) {
    var fn = !/\W/.test(str) ?
        rentmplCache[str] = rentmplCache[str] ||
            rentmpl(document.getElementById(str).innerHTML) :
        new Function("obj",
            "var p=[];" +
            "with(obj){p.push('" +
            str
                .replace(/\\/g, "\\\\")
                .replace(/[\r\t\n]/g, " ")
                .split("<%").join("\t")
                .replace(/((^|%>)[^\t]*)'/g, "$1\r")
                .replace(/\t=(.*?)%>/g, "',$1,'")
                .split("\t").join("');")
                .split("%>").join("p.push('")
                .split("\r").join("\\'") + "');}return p.join('');");
    return data ? fn(data) : fn;
}

