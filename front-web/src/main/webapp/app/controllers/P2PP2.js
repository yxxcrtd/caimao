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
    'app/views/financing/LoanFixP2PPanel',
    'app/views/financing/LoanRandomPanel',
    'app/views/financing/LoanRuleP2PPanelP2',
    'app/views/common/LoginWindow',
    'app/views/financing/LoanP2P2PPanel2',
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
], function(dom, domStyle, domClass, domConstruct, on,
            query, Ajax, Global, Helper, Win, Data, when, SlideContainer,
            LoanFixP2PPanel, LoanRandomPanel, LoanRuleP2PPanel, LoginWindow, LoanP2P2PPanel2,
            PzSideMenu,
            Button, Prompt, all, RSA, domAttr, Tooltip, TextBox, Product, WinUtil, User, cfg) {
    var protocolToggler = dom.byId('protocoltoggler'),
        depositFld,
        loanPanel,
        product,
        p2pConfig,
        productDetails,
        ratioObj,
        product_id = Product.getProductId(2),
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
        newRegirestDateTime,
        systemError = false;

    /**
     * 设置面板中填充的金额
     * @param deposit   本金
     * @param term  周期
     */
    function produceLoan(deposit, term) {
        var ratios = Product.getRatios(product),
            amounts = [],
            i = 0, len = ratios.length;
        for (; i <len; i++) {
            amounts.push(deposit * ratios[i]);
        }
        loanPanel.set('term', term);
        loanPanel.set('amounts', amounts);
    }

    /**
     * 使用默认的，页面初始化的时候用
     */
    function userDefault() {
        produceLoan(depositFld.getAmount(), 30);
        loanPanel.select(0);
        loanRulePanel.setData(loanPanel.getAmount(), depositFld.getAmount());
    }
    
    function check() {
        User.isTradePwd(null, function() {
        	loanP2Panel2.nextBtn.addDisabledMsg([1, '您还未设置安全密码，请先<a href="'+Global.baseUrl+'/account/tradepwd/set.htm">设置</a>']);
        });
    }
    
    function checkIsTrust(){
    	User.isTrust(null, function() {
            loanP2Panel2.nextBtn.addDisabledMsg([2, '请您先完成<a target="_blank" href="'+Global.baseUrl+'/user/'+(cfg.authentication ? 'uploadID.htm' : 'certification.htm')+'">实名认证</a>']);
        });
    }
    
    function initView() {
        // 融资独属菜单
        var pzSideMenu = new PzSideMenu({
            active: '1 2'
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
        // 这里获取P2P产品配置
        var promise3 = Ajax.get(Global.baseUrl + '/p2p/product/config', {
            product_id: product_id
        });

        all([promise1, promise2, promise3]).then(function(results){
            loanRulePanel.setValues({
                product: results[0].data,
                productDetails: results[1].data,
                p2pConfigList: results[2].data
            });
            product = results[0].data;
            productDetails = results[1].data;
            p2pConfig = results[2].data;
            product.productDetails = productDetails;
            product.p2pConfig = p2pConfig.productConfig;

            // 如果获取P2P配置错误，则不能进行其他的
            if (results[2].success == false) {
                // TODO 弹框提示错误
                systemError = true;
            }
            loanRulePanel.setMonthMaxRate(p2pConfig.month_rate_max);
            loanPanel.set('product', product);
            var minDepoist = product.prodAmountMin / product.prodLoanRatioMax;
            var maxDepoist = product.prodAmountMax / product.prodLoanRatioMin;
            // 这个自己加的吧
            if(minDepoist < 400000) minDepoist = 400000;

            depositFld.validates.push({
                pattern: function() {
                    return parseFloat(this.getAmount()) >= minDepoist;
                },
                message: '最小本金为' + Global.formatAmount(minDepoist)+'元'
            });
            depositFld.validates.push({
                pattern: function() {
                    return parseFloat(this.getAmount()) <= maxDepoist;
                },
                message: '最大本金为' + Global.formatAmount(maxDepoist)+'元'
            });
            depositFld.max=maxDepoist;
            depositFld.set('placeHolder', '最少'+Global.formatAmount(minDepoist,0,'w')+'，最多'+Global.formatAmount(maxDepoist,0,'w'));
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

        on(depositFld, 'keyup', function(e) {
            var deposit = this.getAmount();
            var day = loanRulePanel.daySelect.value;
            produceLoan(deposit, day);
            loanRulePanel.setData(loanPanel.getAmount(), deposit);
            if (deposit) {
                loanPanel.selectNode.style.display = 'block';
                loanPanel.blankNode.style.display = 'none';
            } else{
                loanPanel.selectNode.style.display = 'none';
                loanPanel.blankNode.style.display = 'block';
            }
        });

        // place loan panel 可融资金额那个框框
        loanPanel = new LoanFixP2PPanel({
            title: 'p2',
            showFee: true
        }, 'select-loan-panel');
        // 可融资金额的框框被点击了的效果
        on(loanPanel.selectNode, 'li:click', function() {
            var deposit = depositFld.getAmount();
            produceLoan(deposit, loanRulePanel.daySelect.value);
            loanRulePanel.setData(loanPanel.getAmount(), deposit, ratioObj);
        });
        // 获取服务器当前的时间
        when(Data.getTime()).then(function(time) {
            loanRulePanel.set('time', time);
        });
        // show protocol 借款协议点击的
        on(protocolToggler, 'click', function(e) {
            if (!win) {
                win = new Win({
                    width: 900,
                    height: 500,
                    title: '借款协议'
                });
                win.placeAt(document.body);
            }
            win.show();
            var id = 2;
            if (loanRulePanel.isAvailableP2P == true) {
                id = 3;
            }
            when(Data.getProtocol({id: id}, true), function(tmpl) {
                win.set('msg', tmpl);
            });
            e.stopPropagation();
        });

        // place right panel    左侧那边的规则框框
        loanRulePanel = new LoanRuleP2PPanel({
            unit: cfg.proMonthUnit,
            carry: cfg.proMonthCarry
        }, 'rightctn');
        // 当选择期限的发生了变换
        on(loanRulePanel.daySelect, 'change', function(value) {
            var deposit = depositFld.getAmount();
            produceLoan(deposit, value);
            loanRulePanel.setData(loanPanel.getAmount(), deposit);
        });
        // 当手续费滑动块变化了
        on(loanRulePanel.monthFee, 'change', function(value) {
            if (loanRulePanel.isAvailableP2P == false) return false;
            // 修改选中框框的手续费
            //console.log(value);
            value = (value / 100).toFixed(2);
            //console.log(value);
            // 变右侧框框的利息值
            loanRulePanel.setFeeRate(value);
            // 变左侧框框的利息值
            loanPanel.setFeeRate(value);
        });

        // 这个是显示第二步的那个操作
        function showPanel2() {
            if (systemError == true) {
                alert("系统出现异常，请稍后重试");
                return false;
            }
            // 顶部步骤选中的样式修改
            query('.navi-container li.ui-step-active').removeClass('ui-step-active');
            domClass.add(query('.navi-container li')[1], 'ui-step-active');
            // 隐藏第一个面板
            dom.byId('panel1').style.display = 'none';
            if (!loanP2Panel2) {
                loanP2Panel2 = new LoanP2P2PPanel2({
                    bill: loanRulePanel.bill
                });
                checkIsTrust();
                check();
                loanP2Panel2.placeAt('contentctn', 'first');
                // 上一步的按钮点击的操作
                on(loanP2Panel2.prevBtn, 'click', function() {
                    loanP2Panel2.hide();
                    query('.navi-container li.ui-step-active').removeClass('ui-step-active');
                    domClass.add(query('.navi-container li')[0], 'ui-step-active');
                    dom.byId('panel1').style.display = 'block';
                });
                // 这个是确认支付按钮点击的操作吧
                on(loanP2Panel2.nextBtn, 'click', function() {
                    if (loanP2Panel2.isValid()) {
                    	loanP2Panel2.nextBtn.loading(true);
                        if (loanRulePanel.isAvailableP2P == true) {
                            // 进行借贷融资的过程
                            Ajax.post(Global.baseUrl + '/p2p/loan/apply', {
                                trade_pwd: RSA.encryptedString(key, loanP2Panel2.pwdFld.get('value')),
                                produce_id: product_id,
                                produce_term: loanRulePanel.daySelect.value,
                                deposit_amount: Global.switchAmount(depositFld.getAmount(), cfg.proMonthUnit, cfg.proMonthCarry),
                                loan_amount: Global.switchAmount(loanPanel.getAmount(), cfg.proMonthUnit, cfg.proMonthCarry),
                                prev_store: loanP2Panel2.prev_store,
                                'abstract': loanRulePanel.getAbstract(),
                                lever : loanRulePanel.lever,
                                cmValue : loanRulePanel.cmValue,
                                custumRate : loanRulePanel.monthFeeRate
                            }).then(function(response) {
                                loanP2Panel2.nextBtn.loading(false);
                                if (response.success) {
                                    loanP2Panel2.hide();
                                    query('.navi-container li.ui-step-active').removeClass('ui-step-active');
                                    domClass.add(query('.navi-container li')[2], 'ui-step-active');
                                    var successPanel = new Prompt({
                                        type: 'success',
                                        msg: '恭喜您，借贷融资申请成功！',
                                        subMsg: '<span style="color: #666666">满标后将给你开通交易账户，请耐心等待。</span>'
                                        + '</br>' + '<span style="color: #666666">如有疑问请拨打客服电话<b class="am-ft-orange">'+Global.hotline+'</b></span>',
                                        linkText: '查看申请记录',
                                        link:'p2p/userTargetPage.htm'
                                    });
                                    successPanel.placeAt('contentctn', 'first');
                                } else {
                                    Tooltip.show(response.msg, loanP2Panel2.nextBtn.innerNode, 'warning');
                                }
                            });
                        } else {
                            // 普通借贷融资的过程
                            Ajax.post(Global.baseUrl + '/financing/loan/apply', {
                                trade_pwd: RSA.encryptedString(key, loanP2Panel2.pwdFld.get('value')),
                                produce_id: product_id,
                                produce_term: loanRulePanel.daySelect.value,
                                deposit_amount: Global.switchAmount(depositFld.getAmount(), cfg.proMonthUnit, cfg.proMonthCarry),
                                loan_amount: Global.switchAmount(loanPanel.getAmount(), cfg.proMonthUnit, cfg.proMonthCarry),
                                prev_store: loanP2Panel2.prev_store,
                                'abstract': loanRulePanel.getAbstract()
                            }).then(function(response) {
                                loanP2Panel2.nextBtn.loading(false);
                                if (response.success) {
                                    loanP2Panel2.hide();
                                    query('.navi-container li.ui-step-active').removeClass('ui-step-active');
                                    domClass.add(query('.navi-container li')[2], 'ui-step-active');
                                    var successPanel = new Prompt({
                                        type: 'success',
                                        msg: '恭喜您，融资申请已成功！',
                                        subMsg: '<span style="color: #666666">融资申请正在紧张处理，请耐心等待。</span>'
                                        + '</br>' + '<span style="color: #666666">如有疑问请拨打客服电话<b class="am-ft-orange">'+Global.hotline+'</b></span>',
                                        linkText: '查看申请记录',
                                        link:'financing/apply.htm'
                                    });
                                    successPanel.placeAt('contentctn', 'first');
                                } else {
                                    Tooltip.show(response.msg, loanP2Panel2.nextBtn.innerNode, 'warning');
                                }
                            });
                        }
                    }
                });
            }
            loanP2Panel2.show();
            WinUtil.scrollIntoView(loanP2Panel2.domNode);
            loanP2Panel2.set('bill', loanRulePanel.bill);
            loanP2Panel2.setValues({
                isAvailableP2P: loanRulePanel.isAvailableP2P,
                term : loanRulePanel.daySelect.value,
                depositAmount: depositFld.getAmount(),
                loanAmount: loanPanel.getAmount(),
                cmValue : loanRulePanel.cmValue,
                pzFeeRate : loanRulePanel.pzFeeRate,
                lever : loanRulePanel.lever,
                monthFeeRate : loanRulePanel.monthFeeRate
            });
            loanP2Panel2.setManageFeeTotal(loanRulePanel.manageFee, loanRulePanel.manageRate);
            when(Data.getDeposit(0), function(deposit) {
                if (deposit) {
                    loanP2Panel2.set('availableAmount', deposit);
                } else {
                    loanP2Panel2.set('availableAmount', 0);
                }
            });

        }

        // first page button 我要操盘那个按钮
        nextBtn = new Button({
            label: '我要操盘',
            disabled: !dom.byId('checkbox1').checked,
            enter: true,
            onClick: function() {
                if (depositFld.checkValidity() && loanPanel.isValid()) {
                    nextBtn.loading(true);
                    when(Data.getUser(), function(user) {
                        nextBtn.loading(false);
                        if (user) {
                            showPanel2();
                        } else {
                            if (!loginWin) {
                                loginWin = new LoginWindow({
                                    onLogin: function() {
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
        on(dom.byId('checkbox1'), 'click', function() {
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
        init: function() {
            Ajax.get(Global.baseUrl + '/sec/rsa', {
            }).then(function (response) {
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