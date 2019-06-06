// @koala-prepend "./lib/jquery-1.11.1.min.js"
// @koala-prepend "./common/utils.js"
// @koala-prepend "./common/Data.js"
// @koala-prepend "./common/Dict.js"
// @koala-prepend "./lib/RSA.js"
// @koala-prepend "./lib/ZeroClipboard.min.js"

function testInput(value, validates){
    var msg = "";
    for(var i in validates){
        var regular = validates[i].pattern;
        if(typeof(regular) == "function"){
            if(!regular()){
                msg = validates[i].message;break;
            }
        }else{
            if(!regular.test(value)){
                msg = validates[i].message;break;
            }
        }
    }
    if(msg != ""){
        $("#show_alert_stock").html(msg).show().delay(3000).fadeOut();
        return false;
    }else{
        return true;
    }
}

function exchange_score(){
    var exchange_score = $("#tuihuan_score").val();

    var pwd = $("#pwd").val();
    var validates = [{
        pattern: /.+/,
        message: '请输入要兑换的数量'
    }, {
        pattern: function () {
            var user_score = $("#user_score").text();
            return parseInt(exchange_score) <= parseInt(user_score);
        },
        message: '超出可兑换数量'
    }];
    if(!testInput(exchange_score, validates)) return;

    validates = [{
        pattern: /.+/,
        message: '请输入安全密码'
    }];
    if(!testInput(pwd, validates)) return;

    var postData = {
        'score': exchange_score,
        'tradePwd': RSAUtils.encryptedString(pwd)
    };

    $.post("/user/score/apply", postData, function(response){
        if(response.success){
            $("#stockBuyPrice, #stockBuyAmount, #stockBuyPwd").empty();
            $("#show_alert_stock").html('<span style="color:#6CBF0B">兑换成功</span>').show().delay(3000).fadeOut();
            setTimeout(function(){
                window.location.href="/popularize/exchangeRecord.htm";
            },2000);
        }else{
            $("#show_alert_stock").html(response.msg).show().delay(3000).fadeOut();
        }
    });
}

$(document).ready(function(){
    var CopyUrl = new ZeroClipboard(document.getElementById('copy_url'));
    CopyUrl.on( "ready", function() {
        CopyUrl.on( "aftercopy", function() {
            document.getElementById('copy_tip').style.display = 'block';
        });
    });
});