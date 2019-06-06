/**
 * 按月配资版本
 * Created by Administrator on 2015/6/24.
 */

// @koala-prepend "./lib/jquery-1.11.1.min.js"
// @koala-prepend "./common/utils.js"
// @koala-prepend "./common/pz.js"
// @koala-prepend "./common/Data.js"
// @koala-prepend "./lib/RSA.js"
// @koala-prepend "./ux/alert.js"
// @koala-prepend "./ux/slider.js"
// @koala-prepend "./ux/protocol.js"
// @koala-prepend "./lib/formValidate.js"
// @koala-prepend "./v2/actionTip.js"
// @koala-prepend "./lib/jquery.poshytip.js"

$(document).ready(function() {
    var productId = 800461779107843,  // 产品ID
        productInfo = null,
        productDetail = null,
        accountInfo = null,
        userInfo = null,
        defaultSelect = 1,
        chaqianAmount = 1,
        selectPzAmountTmpl = $('#selectPzAmountTmpl').html(),
        selectPzAmountPoint = $('#selectPzAmountPoint'),
        inputMarginAmount = $('#marginAmount'),
        shiyongqixian = $('#shiyongqixian'),
        tongyiChecked = $('#tongyi'),
        shenqingBtn = $('#shenqingpeizi'),
        querenBtn = $('#querenpeizi'),
        jiekuanxieyi = $('#jiekuanxieyi'),
        errorMsg = $('#errorMsg'),
        loginStr = "请先进行<a href='/user/login.html'>登录</a>",
        depositStr = "可用不足，请先进行<a href=''>充值</a>",

        //记录全局需要记录的值
        curFeeRate = 0,
        curTerm = 0,
        curMarginAmount = 0,
        curLoanAmount = 0,
        curLever = 0,
        selectError = false,
        userSetError = false,
        newRegisterDate = null;

    // 提示信息的展示
    $('#kuisunTip').poshytip({
        className : 'tip-yellow',
        showOn: 'hover',
        followCursor: true,
        alignTo: 'target',
        alignX: 'inner-left',
        alignY: 'bottom',
    });
    $('#pingcangTip').poshytip({
        className : 'tip-yellow',
        showOn: 'hover',
        followCursor: true,
        alignTo: 'target',
        alignX: 'inner-left',
        alignY: 'bottom',
        offsetX: 0,
        offsetY: 5
    });

    // 获取产品信息
    productInfo = PZ.getProductInfo(productId);
    // 获取产品详情
    productDetail = PZ.getProductDetail(productId);
    // 获取资产信息
    accountInfo = Data.getAccount();
    // 获取用户信息
    userInfo = Data.getUser();
    // 新用户注册的时间
    newRegisterDate = Data.getNewRegisterDate();

    $(inputMarginAmount).attr("placeholder", "请输入投资本金 最少" + CMUTILS.toWanAndCommas(productInfo.prodAmountMin) + "万 最多" + CMUTILS.toWanAndCommas(productInfo.prodAmountMax) + "万");

    buildDom('shenqingpeizi');
    buildDom('marginAmount');
    var btnError1 = document.querySelector('#shenqingpeizi').top();
    var inputError1 = document.querySelector('#marginAmount').bottom();

    if (userInfo == null) {
        // 让他进行登录
        errorMsg.html(loginStr);
        userSetError = true;
    } else if (userInfo.isSetTradePwd == 0) {
        errorMsg.html("请先设置<a href='/account/safe.htm'>安全密码</a>");
        userSetError = true;
    } else if (accountInfo == null) {
        errorMsg.html("请先进行<a href='/account/safe.htm'>实名认证</a>");
        userSetError = true;
    } else {
        $('#showAvailable').html("账户余额 " + CMUTILS.toYuanAndCommas(accountInfo.avalaibleAmount) + " 元");
    }
    if (userInfo != null) {
        if (newRegisterDate > 0) {
            if (userInfo.registerDatetime > newRegisterDate) {
                errorMsg.html("暂停融资");
                userSetError = true;
            }
        }
    }

    //console.info("融资产品配置");
    //console.info(productInfo);
    //console.info("融资产品详情");
    //console.info(productDetail);
    //console.info("资金信息");
    //console.info(accountInfo);

    // 写入选择期限的地方
    function appendSelectMonths(prodTerms) {
        var monthsArr = prodTerms.split(",");
        for (var i = 0, l = monthsArr.length; i < l; i++) {
            $(shiyongqixian).append("<option value='"+monthsArr[i]+"'>"+((monthsArr[i]/30)+"个月")+"</option>");
        }
    }
    appendSelectMonths(productInfo.prodTerms);

    // 写融资的选择框
    function wirtePzAmountPoint(marginAmount) {
        var selectPzAmountArr = [];
        for (var i = 1; i <= productInfo.prodLoanRatioMax; i++) {
            // 写入杠杆倍数、利率、金额
            var loanAmount = parseInt(CMUTILS.accMul(marginAmount, i) / 1000000) * 1000000;
            var feeRate = PZ.getProdFee(productDetail, loanAmount, i);
            selectPzAmountArr[i] = {
                lever : i,
                feeRate : feeRate,
                feeRateStr : feeRate ? CMUTILS.accMul(feeRate, 100).toFixed(2) + "%" : "无效值",
                loanAmount : loanAmount,
                loanAmountStr : CMUTILS.toWanAndCommas(loanAmount)
            };
        }
        //console.info(selectPzAmountArr);
        selectPzAmountPoint.html(CMUTILS.rentmpl(selectPzAmountTmpl, {data :selectPzAmountArr, select : defaultSelect}));
        writePzXuzhiPoint(marginAmount, defaultSelect);
    }
    // 写入融资须知里的哪些值
    function writePzXuzhiPoint(marginAmount, lever) {
        var detail = PZ.queryProdDetail(productDetail, CMUTILS.accMul(marginAmount, lever), lever);
        //console.info("写那些融资须知");
        //console.info("保证金 " + marginAmount+ " 杠杆 " + lever);
        //console.info(detail);
        if (detail) {
            var loanAmount = CMUTILS.accMul(marginAmount, lever);
            var totalUsedAmount = CMUTILS.accAdd(marginAmount, loanAmount);
            curFeeRate = detail.interestRate;

            $('#caopanxuzhi').html(detail.remark + "&nbsp;&nbsp;");
            $('#zongcaopanjin').html(CMUTILS.toYuanAndCommas(totalUsedAmount)+"元");
            $('#kuisunjingjiexian').html(CMUTILS.toYuanAndCommas(CMUTILS.accMul(loanAmount, detail.enableRatio))+"元");
            $('#kuisunpingcangxian').html(CMUTILS.toYuanAndCommas(CMUTILS.accMul(loanAmount, detail.exposureRatio))+"元");
            $('#yuefeilv').html(CMUTILS.accMul(curFeeRate, 100).toFixed(2)+"%");


            curMarginAmount = marginAmount;
            curLoanAmount = loanAmount;
            curLever = lever;

            calcFeiyong();
            selectError = false;
        } else {
            $('#zongcaopanjin').html("0元");
            $('#kuisunjingjiexian').html("0元");
            $('#kuisunpingcangxian').html("0元");
            $('#peizirifeiyong').html("0元/每天");
            $('#baozhengjinTip').html("0元");
            $('#shoufufeiyongTip').html("0元");
            $('#gongTip').html("0元");
            $('#yuefeilv').css({"float" : "left"});
            selectError = true;
        }
        // 下方申请按钮的联动
        $(tongyiChecked).trigger("change");
        return detail;
    }

    // 计算那些费用的方法啥的
    function calcFeiyong() {
        // 首付费用 借款金额 * 费率
        curShoufuFee = CMUTILS.accMul(curFeeRate, curLoanAmount);
        curTerm = $(shiyongqixian).val();
        var zonggong = CMUTILS.accAdd(curMarginAmount, curShoufuFee);

        $('#baozhengjinTip').html(CMUTILS.toYuanAndCommas(curMarginAmount) + "元");
        $('#shoufufeiyongTip').html(CMUTILS.toYuanAndCommas(curShoufuFee) + "元");
        $('#gongTip').html(CMUTILS.toYuanAndCommas(zonggong) + "元");

        // 当前余额与还差多少钱的显示
        if(accountInfo != null) {
            var avalaibleAmount = CMUTILS.accSub(accountInfo.avalaibleAmount, accountInfo.freezeAmount);
            $('#keyongbaozhengjinTip').html(CMUTILS.toYuanAndCommas(avalaibleAmount));
            chaqianAmount = CMUTILS.accSub(zonggong, avalaibleAmount);
            $('#haichaTip').html(CMUTILS.toYuanAndCommas(chaqianAmount));
            if (chaqianAmount > 0) {
                $('#haichaPoint').show();
            } else {
                $('#haichaPoint').hide();
            }
        }
    }

    // 页面初始化进行选择
    wirtePzAmountPoint(CMUTILS.accMul(inputMarginAmount.val(), 100));

    /**
     * 一系列事件处理
     */
    // 输入框选定改变的那个事件
    $(inputMarginAmount).keyup(function() {
        // 判断是否是整数
        if (!/^[0-9]+$]/.test(this.value)) {
            this.value = parseInt(this.value);
            if (isNaN(this.value)) this.value = '';
        }
        if (this.value == '') {
            inputError1.error("请输入投资金额", 3000);
        }
        wirtePzAmountPoint(CMUTILS.accMul(this.value, 100));
    });

    // 选择框的点击效果
    $(selectPzAmountPoint).on("click", "li", function(e) {
        var tmpSelect = $(this).attr("data-lever");
        if (writePzXuzhiPoint(CMUTILS.accMul($(inputMarginAmount).val(), 100), tmpSelect)) {
            // 去除所有的选中样式
            $(selectPzAmountPoint).find("li").each(function(li, num) {
                $(this).removeClass("hov");
            });
            $(this).addClass("hov");
            defaultSelect = tmpSelect;
            //console.info("选中的杠杆 " + defaultSelect);
        }
    });

    // 使用期限变动的事件
    $(shiyongqixian).change(function() {
        curTerm = $(this).val();
    });

    // 同意框是更改的事件
    $(tongyiChecked).change(function(e) {
        if ($(this).prop("checked") == true) {
            // 登录了，并且现在不差钱
            if (userInfo && chaqianAmount <= 0 && userSetError == false && selectError == false) {
                $(shenqingBtn).addClass("btn_blue");
                $(shenqingBtn).removeClass("btn_gray");
            }
        } else {
            $(shenqingBtn).addClass("btn_gray");
            $(shenqingBtn).removeClass("btn_blue");
        }
    });
    $(tongyiChecked).trigger("change");

    // 协议的弹框
    $(jiekuanxieyi).click(function() {
        Protocol.showLoanProtocol();
    });

    // 申请按钮点击的效果
    $(shenqingBtn).click(function() {
        if ($(tongyiChecked).prop("checked") == false) return false;
        if (selectError == true) return false;
        if (userSetError == true) return false;
        if (accountInfo == null) return false;
        if (chaqianAmount > 0) return false;
        // TODO 进行输入检查
        if (curMarginAmount == 0) {
            inputError1.error("请输入保证金", 3000);
            return false;
        }
        if (curLoanAmount == 0) {
            btnError1.error("请选择借款金额", 3000);
            return false;
        }
        // 弹框输入安全密码
        PZ.dialogTradePwd();
    });

    // 确认申请按钮
    $('#_dialog').on("click", "#dialogQuerenzhifu", function() {
        var tradePwd = $('#dialogTradePwd').val();
        if (tradePwd == '') {
            formValidate.showTips($('#dialogQuerenzhifu'), '请输入资金密码');
            return false;
        }
        // 发送申请的请求
        $.ajax({
            url : "/pz/loan/apply",
            type : "POST",
            data : {
                trade_pwd: RSAUtils.encryptedString(tradePwd),
                produce_id: productId,
                produce_term: curTerm,
                deposit_amount: curMarginAmount,
                loan_amount: curLoanAmount,
                'abstract': "按月融资"
            },
            dataType : "json",
            success : function(res) {
                if(res.success) {
                    // 成功消息
                    Alert.show("融资申请成功", "确定", function() {window.location.href = "/pz/apply.html"});
                } else {
                    formValidate.showFormTips('dialogQuerenzhifu', res.msg);
                }
            }
        });
    });

    /**
     * 关闭框框按钮
     */
    $('#_dialog').on("click", "#dialog_close", function() {
        PZ.closeDialog();
    });

});

