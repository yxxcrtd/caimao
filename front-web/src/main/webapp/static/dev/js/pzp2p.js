/**
 * 按月配资P2P版本
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
    var productId = 3,  // 产品ID
        productInfo = null,
        productDetail = null,
        accountInfo = null,
        userInfo = null,
        p2pConfig = null,
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

        curP2PConfig = null,

        //记录全局需要记录的值
        curShoufuFee = 0,
        curFeeRate = 0,
        curCmAmount = 0,
        curTerm = 0,
        curMarginAmount = 0,
        curLoanAmount = 0,
        curLever = 0,
        isP2P = false,
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
    // 获取P2P配置
    p2pConfig = PZ.getP2PConfig(productId);
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
    //console.info("P2P产品配置");
    //console.info(p2pConfig);
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
            var feeRateStr = 0;
            if (activityEnd == '0') {
                feeRateStr = feeRate ? CMUTILS.accMul(feeRate + 0.001, 100).toFixed(2) + "%" : "无效值";
            } else {
                feeRateStr = feeRate ? CMUTILS.accMul(feeRate, 100).toFixed(2) + "%" : "无效值";
            }
            selectPzAmountArr[i] = {
                lever : i,
                feeRate : feeRate,
                feeRateStr : feeRateStr,
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

            curCmAmount = 0;

            // 获取P2P下杠杆倍数的配置
            curP2PConfig = PZ.queryP2PConfig(p2pConfig, lever);
            //console.info(curP2PConfig);
            if (curP2PConfig != null) {
                if (curP2PConfig.isAvailable == true) {
                    // 计算财猫提供的、P2P总借贷的、费率
                    $('._p2p').show();
                    isP2P = true;
                    $('#yuefeilv').css({"float" : "right"});

                    // 设置滑块的值
                    Slider.setMin(detail.interestRate * 10000);
                    Slider.setMax(p2pConfig.month_rate_max * 10000);
                    Slider.setVal(detail.interestRate * 10000);

                } else {
                    $('._p2p').hide();
                    $('#yuefeilv').css({"float" : "left"});
                    isP2P = false;
                }
            } else {
                $('._p2p').hide();
                $('#yuefeilv').css({"float" : "left"});
                isP2P = false;
            }

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
            $('._p2p').hide();
            $('#yuefeilv').css({"float" : "left"});
            isP2P = false;
            selectError = true;
        }
        // 下发申请按钮的联动
        $(tongyiChecked).trigger("change");
        return detail;
    }

    // 计算那些费用的方法啥的
    function calcFeiyong() {
        // 首付费用 借款金额 * 费率
        curShoufuFee = CMUTILS.accMul(curFeeRate, curLoanAmount);
        curTerm = $(shiyongqixian).val();
        var zonggong = curMarginAmount;
        if (isP2P) {
            $('#right_menu').addClass("p2p");
            if (curFeeRate >= curP2PConfig.caimaoRate) {
                curCmAmount = CMUTILS.accMul(curLoanAmount, curP2PConfig.chuziRate);
            }
            //var manageFeeAmount = curP2PConfig.manageFee + CMUTILS.accMul(curLoanAmount, curP2PConfig.manageRate);
            var manageFeeAmount = curP2PConfig.manageFee;
            zonggong = CMUTILS.accAdd(zonggong, manageFeeAmount);
            //if (manageFeeAmount > 0) {
            //    $('#guanlifei').html(CMUTILS.toYuanAndCommas(manageFeeAmount)+"元");
            //    $('._manage_fee').show();
            //} else {
            //    $('._manage_fee').hide();
            //}
            $('#guanlifei').html(CMUTILS.toYuanAndCommas(manageFeeAmount)+"元");
            $('._manage_fee').show();
            if (curP2PConfig.manageRate > 0) {
                $('#p2p_margin_rate').html("包含管理利率:" + (curP2PConfig.manageRate * 100) + "%");
            } else {
                $('#p2p_margin_rate').html("");
            }
            // 首付费用
            curShoufuFee = CMUTILS.accMul(curShoufuFee, curTerm/30);
            zonggong = CMUTILS.accAdd(zonggong, curShoufuFee);
            $('#caimaotigong').html(CMUTILS.toYuanAndCommas(curCmAmount)+"元");
            $('#caimaotigong2').html(CMUTILS.toYuanAndCommas(curCmAmount)+"元");
            $('#p2pjiedai').html(CMUTILS.toYuanAndCommas(curLoanAmount)+"元");
            $('#shengyujine').html(CMUTILS.toYuanAndCommas(CMUTILS.accSub(curLoanAmount, curCmAmount))+"元");
            $('#gangganbeishu').html(curLever + "倍");
        } else {
            $('#right_menu').removeClass("p2p");
            $('#p2p_margin_rate').html("");
            zonggong = CMUTILS.accAdd(zonggong, curShoufuFee);
        }

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
        if (isP2P) {
            // 如果是P2P的借贷，重新计算首付费用
            calcFeiyong();
        }
    });

    // 滑动块儿的初始化
    Slider.init();
    $('#customFeeRate').change(function() {
        $('#yuefeilv').html((Slider.val / 100).toFixed(2) + "%");
        curFeeRate = Slider.val / 10000;
        calcFeiyong();
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
        if (isP2P) {
            Protocol.showP2PProtocol();
        } else {
            Protocol.showLoanProtocol();
        }
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
        if (isP2P == true) {
            // 发送申请的请求
            $.ajax({
                url : "/p2p/loan/apply",
                type : "POST",
                data : {
                    trade_pwd: RSAUtils.encryptedString(tradePwd),
                    produce_id: productId,
                    produce_term: curTerm,
                    deposit_amount: curMarginAmount,
                    loan_amount: curLoanAmount,
                    'abstract': "按月融资",
                    lever : defaultSelect,
                    cmValue : curCmAmount,
                    custumRate : curFeeRate
                },
                dataType : "json",
                success : function(res) {
                    if(res.success) {
                        // 成功消息
                        Alert.show("融资申请成功");
                    } else {
                        formValidate.showFormTips('dialogQuerenzhifu', res.msg);
                    }
                }
            });
        } else {
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
        }

    });

    /**
     * 关闭框框按钮
     */
    $('#_dialog').on("click", "#dialog_close", function() {
        PZ.closeDialog();
    });

});

