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
    'app/views/financing/LoanRulePanel',
    'app/views/common/LoginWindow',
    'app/views/financing/LoanP1Panel2',
    'app/views/common/PzSideMenu',
    'app/ux/GenericButton',
    'app/ux/GenericPrompt',
    'dojo/promise/all',
    'app/common/RSA',
    'dojo/dom-attr',
    'dojo/window',
    'app/common/User',
    'app/ux/GenericTooltip',
    'app/common/Product',
    'dojo/_base/config',
    'dojo/domReady!'
], function(dom, domStyle, domClass, domConstruct, on,
            query, Ajax, Global, Helper, Win, Data, when, SlideContainer,
            LoanFixPanel, LoanRandomPanel, LoanRulePanel, LoginWindow, LoanP1Panel2,
            PzSideMenu,
            Button, Prompt, all, RSA, domAttr, WinUtil, User,Tooltip, Product, cfg) {
    var protocolToggler = dom.byId('protocoltoggler'),
        win,
        product,
        product_id = Product.getProductId(1),
        selectAmount = cfg.proDayLoanSelect.split(','),
        slideContainer,
        slideContainer1,
        loanFixPanel,
        loanRandomPanel,
        loanRulePanel,
        nextBtn,
        loanP1Panel2,
        loginWin,
        key,
        newRegirestDateTime;

    function userDefault() {
        loanRandomPanel.loanAmountFld.set('value', 100000);
        loanFixPanel.select(0);
        loanRulePanel.set('loanAmount', loanFixPanel.amount);
        loanRulePanel.select(0);
    }

    function check() {
        User.isTradePwd(null, function() {
            loanP1Panel2.nextBtn.addDisabledMsg([1, '您还未设置安全密码，请先<a href="'+Global.baseUrl+'/account/tradepwd/set.htm">设置</a>']);
        });
    }

    function checkIsTrust(){
        User.isTrust(null, function() {
            loanP1Panel2.nextBtn.addDisabledMsg([2, '请您先完成<a target="_blank" href="'+Global.baseUrl+'/user/'+(cfg.authentication ? 'uploadID.htm' : 'certification.htm')+'">实名认证</a>']);
        });
    }
    
    function initView() {
        // 融资独属菜单
        var pzSideMenu = new PzSideMenu({
            active: '1 1'
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
        all([promise1, promise2]).then(function(results){
            results[0].data.prodId = product_id;
            product = results[0].data;
            loanRulePanel.setValues({
                product: product,
                productDetails: results[1].data
            });
            loanRulePanel.set('term', product.prodTerms.split(',')[0]);
            loanRandomPanel.set('product', product);
            loanFixPanel.set('product', product);
            loanFixPanel.set('amounts', selectAmount);
            userDefault();
        });
        
        when(Data.getTime()).then(function(time) {
            loanRulePanel.set('time', time);
        });
        
        // show protocol
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
            when(Data.getProtocol({id: 1}), function(tmpl) {
                win.set('msg', tmpl);
            });
            e.stopPropagation();
        });

        // place left panel
        slideContainer = new SlideContainer({

        }, 'leftctn');
        loanFixPanel = new LoanFixPanel();
        loanRandomPanel = new LoanRandomPanel({
            unit: cfg.proDayUnit
        });

        slideContainer.add(loanFixPanel, loanRandomPanel);

        on(loanFixPanel.nextNode, 'click', function() {
            slideContainer.next();
            loanRulePanel.set('loanAmount', slideContainer.getActiveItem().getAmount());
        });
        on(loanRandomPanel.nextNode, 'click', function() {
            slideContainer.next();
            loanRulePanel.set('loanAmount', slideContainer.getActiveItem().getAmount());
        });

        // change by type in
        on(loanRandomPanel.loanAmountFld, 'keyup', function(e) {
            loanRulePanel.set('loanAmount', loanRandomPanel.loanAmountFld.getAmount());
        });

        // place right panel
        loanRulePanel = new LoanRulePanel({
            unit: cfg.proDayUnit,
            carry: cfg.proDayCarry
        }, 'rightctn');
        
        function showPanel2() {
            query('.navi-container li.ui-step-active').removeClass('ui-step-active');
            domClass.add(query('.navi-container li')[1], 'ui-step-active');
            dom.byId('panel1').style.display = 'none';
            if (!loanP1Panel2) {
                loanP1Panel2 = new LoanP1Panel2({
                    bill: loanRulePanel.bill,
                    unit: cfg.proDayUnit,
                    carry: cfg.proDayCarry
                });
                checkIsTrust();
                check();
                loanP1Panel2.placeAt('contentctn', 'first');
                on(loanP1Panel2.prevBtn, 'click', function() {
                    loanP1Panel2.hide();
                    query('.navi-container li.ui-step-active').removeClass('ui-step-active');
                    domClass.add(query('.navi-container li')[0], 'ui-step-active');
                    dom.byId('panel1').style.display = 'block';
                });

                on(loanP1Panel2.nextBtn, 'click', function() {
                    if (loanP1Panel2.isValid()) {
                    	loanP1Panel2.nextBtn.loading(true);
                        Ajax.post(Global.baseUrl + '/financing/loan/apply', {
                            trade_pwd: RSA.encryptedString(key, loanP1Panel2.pwdFld.get('value')),
                            produce_id: product_id,
                            produce_term: loanRulePanel.term,
                            deposit_amount: Global.switchAmount(loanRulePanel.depositAmount, cfg.proDayUnit, cfg.proDayCarry),
                            loan_amount: Global.switchAmount(loanRulePanel.loanAmount, cfg.proDayUnit, cfg.proDayCarry),
                            'abstract': loanRulePanel.getAbstract()
                        }).then(function(response) {
                        	loanP1Panel2.nextBtn.loading(false);
                            if (response.success) {
                                loanP1Panel2.hide();
                                query('.navi-container li.ui-step-active').removeClass('ui-step-active');
                                domClass.add(query('.navi-container li')[2], 'ui-step-active');
                                var successPanel = new Prompt({
                                    type: 'success',
                                    msg: '恭喜您，操盘申请已成功！',
                                    subMsg: '<span style="color: #666666">操盘申请正在紧张处理，请耐心等待。</span>'
                                            + '</br>' + '<span style="color: #666666">如有疑问请拨打客服电话<b class="am-ft-orange">'+Global.hotline+'</b></span>',
                                    linkText: '查看申请记录',
                                    link:'financing/apply.htm'
                                });
                                successPanel.placeAt('contentctn', 'first');
                            } else {
                                Tooltip.show(response.msg, loanP1Panel2.nextBtn.innerNode, 'warning');
                            }
                        });
                    }
                });
            }
            loanP1Panel2.show();
            WinUtil.scrollIntoView(loanP1Panel2.domNode);
            loanP1Panel2.set('bill', loanRulePanel.bill);
            loanP1Panel2.setValues(loanRulePanel.getValues());
            when(Data.getDeposit(0), function(deposit) {
                if (deposit) {
                    loanP1Panel2.set('availableAmount', deposit);
                } else {
                    loanP1Panel2.set('availableAmount', 0);
                }
            });

        }

        // first page button
        nextBtn = new Button({
            label: '我要操盘',
            disabled: !dom.byId('checkbox1').checked,
            enter: true,
            onClick: function() {
                if (slideContainer.isValid() && loanRulePanel.isValid()) {
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

        on(loanFixPanel.selectNode, 'li:click', function() {
            var loanAmount = parseFloat(domAttr.get(this, 'data-amount'));
            loanRulePanel.set('loanAmount', loanAmount);
        });

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