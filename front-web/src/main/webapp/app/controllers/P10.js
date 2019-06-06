define([
    'dojo/dom',
    'dojo/dom-style',
    'dojo/dom-class',
    'dojo/dom-construct',
    'dojo/on',
    'dojo/query',
    'app/common/Ajax',
    'app/common/Global',
    'app/controllers/Helper',
    'app/ux/GenericWindow',
    'app/common/Data',
    'dojo/when',
    'app/ux/GenericSlideContainer',
    'app/views/financing/LoanFixPanel',
    'app/views/financing/LoanRandomPanel',
    'app/views/financing/LoanRulePanelP2',
    'app/views/common/LoginWindow',
    'app/views/financing/LoanP1Panel2',
    'app/views/common/PzSideMenu',
    'app/ux/GenericButton',
    'app/ux/GenericPrompt',
    'dojo/promise/all',
    'app/common/RSA',
    'dojo/dom-attr',
    'app/ux/GenericTooltip',
    'app/ux/GenericTextBox',
    'app/common/Product',
    'dojo/window',
    'app/common/User',
    'dojo/_base/config',
    'dojo/domReady!'
], function (dom, domStyle, domClass, domConstruct, on,
             query, Ajax, Global, Helper, Win, Data, when, SlideContainer,
             LoanFixPanel, LoanRandomPanel, LoanRulePanel, LoginWindow, LoanP2Panel2,
             PzSideMenu,
             Button, Prompt, all, RSA, domAttr, Tooltip, TextBox, Product, WinUtil, User, cfg) {
    var protocolToggler = dom.byId('protocoltoggler'),
        depositFld,
        loanPanel,
        product,
        productDetails,
        ratioObj,
        product_id = Product.getProductId(12),
        win,
        slideContainer,
        slideContainer1,
        loanFixPanel,
        loanRandomPanel,
        loanRulePanel,
        nextBtn,
        loanP2Panel2,
        loginWin,
        key,
        newRegirestDateTime;

    function produceLoan(deposit, term) {
        var ratios = Product.getRatios(product),
            amounts = [],
            i = 0, len = ratios.length;
        for (; i < len; i++) {
            amounts.push(deposit * ratios[i]);
        }
        loanPanel.set('term', term);
        loanPanel.set('amounts', amounts);
    }

    function userDefault() {
        produceLoan(depositFld.getAmount(), 30);
        loanPanel.select(0);
        loanRulePanel.setData(loanPanel.getAmount(), depositFld.getAmount());
    }

    function check() {
        User.isTradePwd(null, function () {
            loanP2Panel2.nextBtn.addDisabledMsg([1, '您还未设置安全密码，请先<a href="' + Global.baseUrl + '/account/tradepwd/set.htm">设置</a>']);
        });
    }

    function checkIsTrust() {
        User.isTrust(null, function () {
            loanP2Panel2.nextBtn.addDisabledMsg([2, '请您先完成<a target="_blank" href="' + Global.baseUrl + '/user/' + (cfg.authentication ? 'uploadID.htm' : 'certification.htm') + '">实名认证</a>']);
        });
    }

    function initView() {
        // 融资独属菜单
        var pzSideMenu = new PzSideMenu({
            active: '1 3'
        });
        // 获取新用户注册的时间
        newRegirestDateTime = Ajax.get(Global.baseUrl + '/other/data/o20150715', {});
        all([newRegirestDateTime]).then(function(results) {
            if (results[0].success == true) {
                newRegirestDateTime = results[0].data;
            } else {
                newRegirestDateTime = -1;
            }
        });
        pzSideMenu.placeAt('sidemenuctn');
        // product data
        var promise1 = Ajax.get(Global.baseUrl + '/product', {
            product_id: product_id
        });
        var promise2 = Ajax.get(Global.baseUrl + '/proddetail', {
            product_id: product_id
        });
        all([promise1, promise2]).then(function (results) {
            loanRulePanel.setValues({
                product: results[0].data,
                productDetails: results[1].data
            });
            product = results[0].data;
            productDetails = results[1].data;
            product.productDetails = productDetails;

            loanPanel.set('product', product);
            var minDepoist = product.prodAmountMin / product.prodLoanRatioMax;
            var maxDepoist = product.prodAmountMax / product.prodLoanRatioMin;

            depositFld.validates.push({
                pattern: function () {
                    return parseFloat(this.getAmount()) >= minDepoist;
                },
                message: '最小本金为' + Global.formatAmount(minDepoist) + '元'
            });
            depositFld.validates.push({
                pattern: function () {
                    return parseFloat(this.getAmount()) <= maxDepoist;
                },
                message: '最大本金为' + Global.formatAmount(maxDepoist) + '元'
            });
            depositFld.max = maxDepoist;
            depositFld.set('placeHolder', '最少' + Global.formatAmount(minDepoist, 0, 'w') + '，最多' + Global.formatAmount(maxDepoist, 0, 'w'));
            var temp = product.prodTerms.split(',');
            var storeData = [], i = 0, len = temp.length;
            for (; i < len; i++) {
                storeData.push({'id': temp[i], 'name': temp[i] / 30 + '个月'});
            }
            loanRulePanel.daySelect.store.setData(storeData);
            userDefault();
        });

        // place deposit field
        depositFld = new TextBox({
            style: 'width: 313px;font-size: 30px;margin-left:25px;',
            value: 100000,
            validates: [{
                pattern: /.+/,
                message: '请输入金额'
            }],
            limitRegex: /[\d\.]/,
            isAmount: true,
            isNumber: true,
            precision: cfg.proMonthUnit == '1' ? 0 : 2
        }, 'deposit-amount-fld');

        on(depositFld, 'keyup', function (e) {
            var deposit = this.getAmount();
            var day = loanRulePanel.daySelect.value;
            produceLoan(deposit, day);
            loanRulePanel.setData(loanPanel.getAmount(), deposit);
            if (deposit) {
                loanPanel.selectNode.style.display = 'block';
                loanPanel.blankNode.style.display = 'none';
            } else {
                loanPanel.selectNode.style.display = 'none';
                loanPanel.blankNode.style.display = 'block';
            }
        });

        // place loan panel
        loanPanel = new LoanFixPanel({
            title: 'p2',
            showFee: true
        }, 'select-loan-panel');

        on(loanPanel.selectNode, 'li:click', function () {
            var deposit = depositFld.getAmount();
            loanRulePanel.setData(loanPanel.getAmount(), deposit, ratioObj);
        });

        when(Data.getTime()).then(function (time) {
            loanRulePanel.set('time', time);
        });
        // show protocol
        on(protocolToggler, 'click', function (e) {
            if (!win) {
                win = new Win({
                    width: 900,
                    height: 500,
                    title: '借款协议'
                });
                win.placeAt(document.body);
            }
            win.show();
            when(Data.getProtocol({id: 2}), function (tmpl) {
                win.set('msg', tmpl);
            });
            e.stopPropagation();
        });

        // place right panel
        loanRulePanel = new LoanRulePanel({
            unit: cfg.proMonthUnit,
            carry: cfg.proMonthCarry
        }, 'rightctn');
        on(loanRulePanel.daySelect, 'change', function (value) {
            var deposit = depositFld.getAmount();
            produceLoan(deposit, value);
            loanRulePanel.setData(loanPanel.getAmount(), deposit);
        });

        function showPanel2() {
            query('.navi-container li.ui-step-active').removeClass('ui-step-active');
            domClass.add(query('.navi-container li')[1], 'ui-step-active');
            dom.byId('panel1').style.display = 'none';
            if (!loanP2Panel2) {
                loanP2Panel2 = new LoanP2Panel2({
                    bill: loanRulePanel.bill
                });
                checkIsTrust();
                check();
                loanP2Panel2.placeAt('contentctn', 'first');
                on(loanP2Panel2.prevBtn, 'click', function () {
                    loanP2Panel2.hide();
                    query('.navi-container li.ui-step-active').removeClass('ui-step-active');
                    domClass.add(query('.navi-container li')[0], 'ui-step-active');
                    dom.byId('panel1').style.display = 'block';
                });

                on(loanP2Panel2.nextBtn, 'click', function () {
                    if (loanP2Panel2.isValid()) {
                        loanP2Panel2.nextBtn.loading(true);
                        Ajax.post(Global.baseUrl + '/financing/loan/apply', {
                            trade_pwd: RSA.encryptedString(key, loanP2Panel2.pwdFld.get('value')),
                            produce_id: product_id,
                            produce_term: loanRulePanel.daySelect.value,
                            deposit_amount: Global.switchAmount(depositFld.getAmount(), cfg.proMonthUnit, cfg.proMonthCarry),
                            loan_amount: Global.switchAmount(loanPanel.getAmount(), cfg.proMonthUnit, cfg.proMonthCarry),
                            prev_store: loanP2Panel2.prev_store,
                            'abstract': loanRulePanel.getAbstract()
                        }).then(function (response) {
                            loanP2Panel2.nextBtn.loading(false);
                            if (response.success) {
                                loanP2Panel2.hide();
                                query('.navi-container li.ui-step-active').removeClass('ui-step-active');
                                domClass.add(query('.navi-container li')[2], 'ui-step-active');
                                var successPanel = new Prompt({
                                    type: 'success',
                                    msg: '恭喜您，融资申请已成功！',
                                    subMsg: '<span style="color: #666666">融资申请正在紧张处理，请耐心等待。</span>'
                                    + '</br>' + '<span style="color: #666666">如有疑问请拨打客服电话<b class="am-ft-orange">' + Global.hotline + '</b></span>',
                                    linkText: '查看申请记录',
                                    link: 'financing/apply.htm'
                                });
                                successPanel.placeAt('contentctn', 'first');
                            } else {
                                Tooltip.show(response.msg, loanP2Panel2.nextBtn.innerNode, 'warning');
                            }
                        });
                    }
                });
            }
            loanP2Panel2.show();
            WinUtil.scrollIntoView(loanP2Panel2.domNode);
            loanP2Panel2.set('bill', loanRulePanel.bill);
            loanP2Panel2.setValues({
                depositAmount: depositFld.getAmount(),
                loanAmount: loanPanel.getAmount()
            });
            when(Data.getDeposit(0), function (deposit) {
                if (deposit) {
                    loanP2Panel2.set('availableAmount', deposit);
                } else {
                    loanP2Panel2.set('availableAmount', 0);
                }
            });

        }

        // first page button
        nextBtn = new Button({
            label: '我要操盘',
            disabled: !dom.byId('checkbox1').checked,
            enter: true,
            onClick: function () {
                if (depositFld.checkValidity() && loanPanel.isValid()) {
                    nextBtn.loading(true);
                    when(Data.getUser(), function (user) {
                        nextBtn.loading(false);
                        if (user) {
                            showPanel2();
                        } else {
                            if (!loginWin) {
                                loginWin = new LoginWindow({
                                    onLogin: function () {
                                        showPanel2();
                                    }
                                });
                                loginWin.placeAt(document.body);
                            }
                            loginWin.show();
                        }
                    });
                }

            }
        }, 'loaninfobtn');

        // checkbox protocol
        on(dom.byId('checkbox1'), 'click', function () {
            var e = this;
            //nextBtn.set('disabled', !this.checked);
            when(Data.getUser(), function(user) {
                if (user) {
                    // 检查注册时间
                    if (newRegirestDateTime > 0) {
                        if (user.registerDatetime < newRegirestDateTime) {
                            nextBtn.set('disabled', !e.checked);
                        } else {
                            nextBtn.addDisabledMsg([1, '暂停融资']);
                        }
                    } else {
                        nextBtn.set('disabled', !e.checked);
                    }
                } else {
                    // 未登录
                    nextBtn.addDisabledMsg([1, '未登录，请先<a href="'+Global.baseUrl+'/user/login.htm">进行登录</a>']);
                }
            });
        });
    }

    return {
        init: function () {
            Ajax.get(Global.baseUrl + '/sec/rsa', {}).then(function (response) {
                if (response.success) {
                    key = RSA.getKeyPair(response.data.exponent, '', response.data.modulus);
                } else {
                }

            });
            initView();
            Helper.init();
        }
    }
});