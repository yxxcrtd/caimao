/**
 * P2P投资页面
 * Created by xavier on 15/7/2.
 */
// @koala-prepend "./lib/jquery-1.11.1.min.js"
// @koala-prepend "./lib/jquery.simplePagination.js"
// @koala-prepend "./common/utils.js"
// @koala-prepend "./common/Data.js"
// @koala-prepend "./common/pz.js"
// @koala-prepend "./lib/RSA.js"
// @koala-prepend "./ux/protocol.js"
// @koala-prepend "./lib/formValidate.js"
// @koala-prepend "./v2/actionTip.js"

$(document).ready(function() {

    var formObj = $('#targetDetail'),
        inputTouziAmount = $('#touzijine'),
        inputTradePwd = $('#tradePwd'),
        checkboxJiekuanxieyi = $('#checkJiekuanxieyi'),
        aJiekuanxieyi = $('#aJiekuanxieyi'),
        btnLijitouzi = $('#lijitouzi'),
        errorMsg = $('#errorMsg'),
        aDaojishi = $('#daojishi');
        errorLoginStr = "请先进行<a href='/user/login.html?return=/p2p/index.html'>登录</a>",
        errorAuthStr = "请先进行<a href='/account/safe.htm'>实名认证</a>",
        enableIvest = false,
        accountInfo = null,
        userInfo = null,
        p2pConfig = null,
        timeLimit = [86400,3600,60,1],
        unit = ['天','小时','分','秒'],
        idx = 0;

    // 绑定输入框的提示信息
    buildDom('#touzijine');

    // 表单的验证
    formValidate.init(formObj);

    //    INIT(0, "认购中"),
    //    REPAYMENT(1, "还款中"),
    //    END(2, "已还款"),
    //    FAIL(3, "流标"),
    //    CANCEL(4, "撤销"),
    //    FAIL_ERROR(5, "流标失败"),
    //    FULL(6,"满标"),
    //    COMMIT_LOAN_APPLY(7,"已经发出融资申请");
    switch(parseInt(targetStatus)) {
        case 1:
        case 2:
            changeDisableBtn(false, "已结束", 1);
            break;
        case 3:
            changeDisableBtn(false, "已流标", 1);
            break;
        case 4:
            changeDisableBtn(false, "已撤销", 1);
            break;
        case 6:
        case 7:
            changeDisableBtn(false, "已满标", 1);
            break;
        default:
            changeDisableBtn(true, "", 1);
            break;
    }
    // 判断是否登录状态
    userInfo = Data.getUser();
    accountInfo = Data.getAccount();
    if (userInfo == null) {
        // 未登录
        changeDisableBtn(false, errorLoginStr, 1);
    } else if (accountInfo == null) {
        // 未实名认证
        changeDisableBtn(false, errorAuthStr, 1);
    }


    // 修改是否可以投资的东东
    function changeDisableBtn(isOk, errorStr, errorType) {
        enableIvest = isOk;
        if (typeof errorType == "undefined") {
            formValidate.showFormTips(btnLijitouzi, errorStr);
        } else {
            $(errorMsg).html(errorStr);
        }
        if (isOk) {
            $(btnLijitouzi).removeClass("btn_gray");
        } else {
            $(btnLijitouzi).addClass("btn_gray");
        }
    }

    p2pConfig = PZ.getP2PConfig(3);

    // 投资金额变动的事件
    $(inputTouziAmount).keyup(function() {
        var amount = $(this).val();
        var income = CMUTILS.accDiv(CMUTILS.accMul(amount, yearRate), 12);
        document.querySelector('#touzijine').setStyle('height:20px;line-height:20px;color:#000;font-size:12px;overflow:hidden;z-index:2;').bottom().notice('<i style="float:left;">最高:<b style="font-weight:700">'+maxTZAmount+'</b>元</i><i style="float:right">预计月收益：<b style="color:#eb4636">'+CMUTILS.toYuanAndCommas(income*100)+'</b>元</i>')
        //$(spanYuqishouyi).html(CMUTILS.toYuanAndCommas(income*100));
    });
    $(inputTouziAmount).trigger('keyup');


    // 同意框是更改的时间
    $(checkboxJiekuanxieyi).change(function(e) {
        if (enableIvest == true) {
            if ($(this).prop("checked") == true) {
                changeDisableBtn(true, "");
            } else {
                $(btnLijitouzi).addClass("disabled");
            }
        }
    });
    $(checkboxJiekuanxieyi).trigger("change");

    // 借款协议的东东
    $(aJiekuanxieyi).click(function() {
        Protocol.showP2PProtocol();
    });

    // 倒计时的
    function countdownSecond(){
        --overplus;
        idx = 0;
        if(overplus>0){
            $(aDaojishi).html('项目还剩' + getTimeStr(overplus) + '结束');
            return setTimeout(countdownSecond, 1000);
        }
        $(aDaojishi).html("项目已结束");
    }
    function getTimeStr(t,s){
        var str = s || '',
            ints = ~~(t/timeLimit[idx]),
            rem = t % timeLimit[idx];
        str += ints ? ints + unit[idx] : (s ? 0 + unit[idx] : '');
        ++idx;
        return idx >= timeLimit.length ? str : getTimeStr(rem,str)
    }
    countdownSecond();

    // 投资的按钮
    $(btnLijitouzi).click(function() {
        if (enableIvest == false) return false;
        if (formValidate.validateForm(formObj) == false) {
            return false;
        }
        if ($(checkboxJiekuanxieyi).prop("checked") == false) {
            formValidate.showFormTips(btnLijitouzi, "请勾选同意协议");
            return false;
        }
        var maxValue = CMUTILS.removeCommas(maxTZAmount);
        var investAmount = $(inputTouziAmount).val();
        var tradePwd = $(inputTradePwd).val();

        if (investAmount == '') {
            changeDisableBtn(enableIvest, "请输入投资金额");
            return false;
        }
        if (eval(investAmount) > eval(maxValue)) {
            changeDisableBtn(enableIvest, "投资金额大于最大投资额");
            return false;
        }
        // 必须是100的整数倍
        if (investAmount % p2pConfig.invest_min_value != 0) {
            changeDisableBtn(enableIvest, "投资金额必须是100的整数倍");
            return false;
        }

        if (tradePwd == '') {
            changeDisableBtn(enableIvest, "请输入安全密码");
            return false;
        }

        // 发送申请的请求
        $.ajax({
            url : "/p2p/invest/apply",
            type : "POST",
            data : {
                trade_pwd: RSAUtils.encryptedString(tradePwd),
                target_id: targetId,
                invest_value: investAmount
            },
            dataType : "json",
            success : function(res) {
                if (res.success) {
                    changeDisableBtn(enableIvest, "投资成功");
                    window.location.reload();
                } else {
                    changeDisableBtn(enableIvest, res.msg);
                }
            }
        });
    });

});

