// @koala-prepend "./lib/jquery-1.11.1.min.js"
// @koala-prepend "./common/utils.js"
// @koala-prepend "./common/Data.js"
// @koala-prepend "./lib/slides.js"
// @koala-prepend "./lib/RSA.js"
// @koala-prepend "./lib/formValidate.js"
// @koala-prepend "./v2/actionTip.js"

var errorCount = 0, userNameIntval, passwordIntval, errorIntval;

function showLoanPanel(o, id){
    $(o).addClass("color").siblings("li").removeClass("color");
    if(id == 1){
        $("#loanPanel0").hide();
        $("#loanPanel1").show();
    }else{
        $("#loanPanel1").hide();
        $("#loanPanel0").show();
    }
}

function flush_zhishu(){
    $.ajax({
        url: '/quote/stock/market_match',
        type: 'POST',
        contentType:'application/json; charset=UTF-8',
        data: '{"v":"caimao.json.001","k":"a0aea509d9059bd18735f4b8d499cfcd","o":[{"act":109,"id1":000001,"tp1":1}]}',
        dataType: 'json',
        processData: false,
        success: function (response) {
            if(response.success && response.data != ""){
                var data = $.parseJSON(response.data);
                var zhishu_sh = $("#zhishu_sh");
                var is_class = data.o[0].o.h.cur >= data.o[0].o.h.pre?"red":"cyan";
                var li = zhishu_sh.find("li");
                li.eq(1).addClass(is_class).html(parseFloat(data.o[0].o.h.cur).toFixed(2) + "<i></i>");
                li.eq(2).addClass(is_class).html(CMUTILS.formatPer((data.o[0].o.h.cur - data.o[0].o.h.pre) / data.o[0].o.h.pre) + "%");
            }
        }
    });
    $.ajax({
        url: '/quote/stock/market_match',
        type: 'POST',
        contentType:'application/json; charset=UTF-8',
        data: '{"v":"caimao.json.001","k":"a0aea509d9059bd18735f4b8d499cfcd","o":[{"act":109,"id1":399001,"tp1":2}]}',
        dataType: 'json',
        processData: false,
        success: function (response) {
            if(response.success && response.data != ""){
                var data = $.parseJSON(response.data);
                var zhishu_sz = $("#zhishu_sz");
                var is_class = data.o[0].o.h.cur >= data.o[0].o.h.pre?"red":"cyan";
                var li = zhishu_sz.find("li");
                li.eq(1).addClass(is_class).html(parseFloat(data.o[0].o.h.cur).toFixed(2) + "<i></i>");
                li.eq(2).addClass(is_class).html(CMUTILS.formatPer((data.o[0].o.h.cur - data.o[0].o.h.pre) / data.o[0].o.h.pre) + "%");
            }
        }
    });
    $.ajax({
        url: '/quote/stock/market_match',
        type: 'POST',
        contentType:'application/json; charset=UTF-8',
        data: '{"v":"caimao.json.001","k":"a0aea509d9059bd18735f4b8d499cfcd","o":[{"act":109,"id1":399006,"tp1":2}]}',
        dataType: 'json',
        processData: false,
        success: function (response) {
            if(response.success && response.data != ""){
                var data = $.parseJSON(response.data);
                var zhishu_szc = $("#zhishu_szc");
                var is_class = data.o[0].o.h.cur >= data.o[0].o.h.pre?"red":"cyan";
                var li = zhishu_szc.find("li");
                li.eq(1).addClass(is_class).html(parseFloat(data.o[0].o.h.cur).toFixed(2) + "<i></i>");
                li.eq(2).addClass(is_class).html(CMUTILS.formatPer((data.o[0].o.h.cur - data.o[0].o.h.pre) / data.o[0].o.h.pre) + "%");
            }
        }
    });
}
flush_zhishu();
setInterval("flush_zhishu()", 5000);


$(document).ready(function() {
    formValidate.init($('#loginform'));
    //登录
    $(document).on("click", "#login_button", function(){
        if (errorCount == 3) {
            location.href = '/user/login.html?c=' + errorCount;
        }
        if (formValidate.validateForm($('#loginform')) == false) {
            return false;
        }
        $.post("/user/login", {
            'login_name': $.trim($("#username").val()),
            'login_pwd': RSAUtils.encryptedString($("#password").val())
        }, function(response){
            if (response.success) {
                var returnUrl = CMUTILS.parseUrl().url;
                CMUTILS.setCookie('_loginName', $.trim($("#username").val()), 7);
                if (returnUrl) {
                    location.href = returnUrl;
                } else {
                    location.href = '/account/index.htm';
                }
            } else {
                formValidate.showFormTips('login_button', response.msg);
                $('#password').val("").focus();
                errorCount++;
            }
        });
    });
    $('#username').val(CMUTILS.getCookie('_loginName'));
});

