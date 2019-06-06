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
    'app/views/financing/LoanP3Panel2',
    'app/ux/GenericButton',
    'app/ux/GenericPrompt',
    'dojo/promise/all',
    'app/common/RSA',
    'dojo/dom-attr',
    'app/ux/GenericTooltip',
    'dijit/layout/ContentPane',
    'dojo/window',
    'app/common/User',
    'dojo/_base/config',
    'dojo/domReady!'
], function(dom, domStyle, domClass, domConstruct, on,
            query, Ajax, Global, Helper, Win, Data, when, SlideContainer,
            LoanFixPanel, LoanRandomPanel, LoanRulePanel, LoginWindow, LoanP3Panel2, Button, Prompt, all, RSA, domAttr, Tooltip, ContentPane, win, User,cfg) {
  

    var configView = {};
    configView.product_id = 1;

    configView.loanRandomPanel = new LoanRandomPanel({});
    
/*    function format_number(n){
    	var b = parseInt(n);
    	var len = b.length;
    	if(b<=3){return b;}
    	var r = b%3;
    	return r>0?b.slice(0,r)+","+b.slice(r,len).match(/\d{3}/g).join(","):b.slice(r,len).match(/\d{3}/g).join(",")
    }
    */
    function check() {
        User.isTradePwd(null, function() {
            configView.loanP3Panel2.nextBtn.addDisabledMsg([1, '您还未设置安全密码，请先<a href="'+Global.baseUrl+'/account/tradepwd/set.htm">设置</a>']);
        });
    }
    
    function checkIsTrust(){
    	User.isTrust(null, function() {
            configView.loanP3Panel2.nextBtn.addDisabledMsg([2, '请您先完成<a target="_blank" href="'+Global.baseUrl+'/user/'+(cfg.authentication ? 'uploadID.htm' : 'certification.htm')+'">实名认证</a>']);
        });
    }
    
    function initView() {
    	Ajax.get(Global.baseUrl+"/product",{
    		product_id:configView.product_id
    	}).then(function(request){
    		if(request.success){
    			configView.data = request.data;
    			configView.m1 = configView.data.prodAmountMax/100;
    			configView.m2 = configView.data.prodAmountMax/configView.data.prodLoanRatioMax/100;
    			configView.m3 = configView.m1+configView.m2;
    			dom.byId("pz_m1").innerHTML = Global.formatNumber(parseInt(configView.m1), 0);
    			dom.byId("pz_m2").innerHTML = Global.formatNumber(parseInt(configView.m2), 0);
    			dom.byId("pz_m3").innerHTML = Global.formatNumber(parseInt(configView.m3), 0);
                dom.byId("pz_m4").innerHTML = configView.data.prodTerms;
            }
    	});
    	
    	// first page button

    	configView.nextBtn = new Button({
            label: '我要体验',
            disabled: false,
            enter: true,
            onClick: function() {
            	when(Data.getUser(), function(user) {
            		configView.nextBtn.loading(false);
                    if (user) {
                        showPanel2();
                    } else {
                        if (!configView.loginWin) {
                        	configView.loginWin = new LoginWindow({
                                onLogin: function() {
                                    showPanel2();
                                }
                            });
                        	configView.loginWin.placeAt(document.body);
                        }
                        configView.loginWin.show();
                    }
                });
            }
        }, 'loaninfobtn');
       
    	//显示第二个页面
    	function showPanel2() {
            query('.navi-container li.ui-step-active').removeClass('ui-step-active');
            domClass.add(query('.navi-container li')[1], 'ui-step-active');
            dom.byId('panel1').style.display = 'none';
            if (!configView.loanP3Panel2) {
            	configView.loanP3Panel2 = new LoanP3Panel2({});
            	configView.loanP3Panel2.placeAt('contentctn', 'first');
            	checkIsTrust();
                check();
                on(configView.loanP3Panel2.prevBtn, 'click', function() {
                	configView.loanP3Panel2.hide();
                    query('.navi-container li.ui-step-active').removeClass('ui-step-active');
                    domClass.add(query('.navi-container li')[0], 'ui-step-active');
                    dom.byId('panel1').style.display = 'block';
                });

                //支付成功页面
                on(configView.loanP3Panel2.nextBtn, 'click', function() {
                    if (configView.loanP3Panel2.isValid()) {
                    	configView.loanP3Panel2.nextBtn.loading(true);
                        Ajax.post(Global.baseUrl + '/financing/loan/apply', {
                            trade_pwd: RSA.encryptedString(configView.key, configView.loanP3Panel2.pwdFld.get('value')),
                            produce_id: configView.product_id,
                            produce_term: configView.data.prodTerms,
                            deposit_amount: (configView.m2*100).toFixed(0),
                            loan_amount: (configView.m1*100).toFixed(0),
                            prev_store: configView.loanP3Panel2.prev_store
                        }).then(function(response) {
                        	configView.loanP3Panel2.nextBtn.loading(false);
                            if (response.success) {
                            	configView.loanP3Panel2.hide();
                                query('.navi-container li.ui-step-active').removeClass('ui-step-active');
                                domClass.add(query('.navi-container li')[2], 'ui-step-active');
                                configView.successPanel = new Prompt({
                                    type: 'success',
                                    msg: '恭喜您，免费体验申请已成功！',
                                    subMsg: '<span style="color: #666666">免费体验申请正在紧张处理，请耐心等待。如有疑问请拨打客服电话 ' +
                                        '<b class=".am-ft-orange"><br/>'+Global.hotline+'</b></span>',
                                    linkText: '查看申请记录',
                                    link:'financing/apply.htm'
                                });
                                configView.successPanel.placeAt('contentctn', 'first');
                            } else {
                                Tooltip.show(response.msg, configView.loanP3Panel2.nextBtn.innerNode, 'warning');
                            }
                        });
                    }
                });
            }
            configView.loanP3Panel2.show();
            win.scrollIntoView(configView.loanP3Panel2.domNode);
            configView.values = {
            		loanAmount: configView.m1*100,
            		depositAmount: configView.m2*100,
            		produceId: configView.product_id, 
            		interestAmount: 0
           }
            configView.loanP3Panel2.setValues(configView.values);
            when(Data.getDeposit(0), function(deposit) {
                if (deposit) {
                	configView.loanP3Panel2.set('availableAmount', deposit);
                } else {
                	configView.loanP3Panel2.set('availableAmount', 0);
                }
            });

        }
    }

    return {
        init: function() {
            Ajax.get(Global.baseUrl + '/sec/rsa', {
            }).then(function (response) {
                if (response.success) {
                    configView.key = RSA.getKeyPair(response.data.exponent, '', response.data.modulus);
                } else {
                }

            });
            
            initView();
            
            Helper.init();
        }
    }
});