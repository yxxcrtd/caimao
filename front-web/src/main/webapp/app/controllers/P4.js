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
    'app/common/Date',
    'dojo/when',
    'app/views/common/LoginWindow',
    'app/ux/GenericButton',
    'app/ux/GenericPrompt',
    'dojo/promise/all',
    'app/common/RSA',
    'dojo/dom-attr',
    'app/ux/GenericTooltip',
    'app/ux/GenericTextBox',
    'app/common/User',
    'app/views/financing/LoanEnrollPanel',
    'dojo/mouse',
    'dojo/_base/config',
    'dojo/domReady!'
], function(dom, domStyle, domClass, domConstruct, on,
            query, Ajax, Global, Helper, Win, Data, DateUtil, when,
            LoginWindow, Button, Prompt, all, RSA, domAttr, Tooltip, TextBox, User, LoanEnrollPanel,mouse,cfg) {
    var ratioObj,
        win,
        loginWin,
        enrollPanel,
        enrollPanel2,
        product,
        prompt,
        key,
        tabs,
        userData0,
        userData1,
        contents;

    function check() {
        User.isTradePwd(null, function() {
            enrollPanel.nextBtn.addDisabledMsg([1, '您还未设置安全密码，请先<a href="'+Global.baseUrl+'/account/tradepwd/set.htm">设置</a>']);
            enrollPanel2.nextBtn.addDisabledMsg([1, '您还未设置安全密码，请先<a href="'+Global.baseUrl+'/account/tradepwd/set.htm">设置</a>']);

        });

    }

    function checkIsTrust(){
        User.isTrust(null, function() {
            enrollPanel.nextBtn.addDisabledMsg([2, '请您先完成<a target="_blank" href="'+Global.baseUrl+'/user/'+(cfg.authentication ? 'uploadID.htm' : 'certification.htm')+'">实名认证</a>']);
            enrollPanel2.nextBtn.addDisabledMsg([2, '请您先完成<a target="_blank" href="'+Global.baseUrl+'/user/'+(cfg.authentication ? 'uploadID.htm' : 'certification.htm')+'">实名认证</a>']);
        });
    }

    function setTabData(tab, data) {
        query('span', tab)[0].innerHTML = data.contestName + '（'
            +data.contestBeginDate.slice(5,7)+'月'+data.contestBeginDate.slice(8)+'日-'
            +data.contestEndDate.slice(5,7)+'月'+data.contestEndDate.slice(8)+'日）报名人数<label class="am-ft-xxxl">'+data.contestUserNum+'</label>人';
    }

    function setProgressData(content, data) {
        query('.startDate', content)[0].innerHTML = DateUtil.format(DateUtil.parse(data.contestBeginDate, 'yyyy-MM-dd'), 'MM月dd日');
        query('.endDate', content)[0].innerHTML = DateUtil.format(DateUtil.parse(data.contestEndDate, 'yyyy-MM-dd'), 'MM月dd日');
        query('.publishDate', content)[0].innerHTML = DateUtil.format(DateUtil.parse(data.rankingDate, 'yyyy-MM-dd'), 'MM月dd日');
    }

    function setRewardData(content, data) {
        var i = 0, len = data.length, item;
        for (; i < len; i++) {
            item = data[i];
            query('.bonus-number-'+item['rank'], content)[0].innerHTML = Global.formatAmount(item.rewardAmount, 0);
        }
    }

    function setRankData(content, data) {
        var html = '', i = 0, items = data.items || [], len = items.length, item;
        for (; i < len; i++) {
            item = items[i];
            html += '<tr><td class="td-center">'+(i+1)+'</td><td class="td-left">'+Global.encodeInfo(item.userRealName, 1, 0)+'</td><td class="td-right">'+Global.formatAmount(item.rankingTotalAsset)+'元</td></tr>';
        }
        domConstruct.place(domConstruct.toDom(html), query('.rank-ctn', content)[0]);
    }

    function initView() {

        when(Data.getUser(), function(user) {
            if (user) {
                check();
                checkIsTrust();
            }
        });

    	var me=this;

        Ajax.get(Global.baseUrl + '/contest/info', {
            state: 0 // current vol
        }).then(function(response) {
            if (response.success) {
                var info = response.data[0];
                // product data
                all([Data.getProduct({'product_id': info.prodId}, true), Data.getProductDetails({'product_id': info.prodId}, true)]).then(function(result) {
                    if (result) {
                        product = result[0];
                        result[0].productDetails = result[1];
                        enrollPanel.set('product', result[0]);
                    }
                });
                setTabData(tabs[0], info);
                setProgressData(contents[0], info);
                var startDate = info.contestBeginDate,
                    endDate = info.contestEndDate,
                    disabled = true,
                    curDate = DateUtil.format(new Date());
                if (curDate >= startDate && curDate <= endDate) {
                    disabled = false;
                }
                enrollPanel.set('prodId', info.prodId);
                enrollPanel.set('disabled', disabled);
                enrollPanel.set('info', info);
                Ajax.get(Global.baseUrl + '/contest/reward', {
                    contest_id: info.contestId
                }).then(function(response) {
                    if (response.success) {
                        setRewardData(contents[0], response.data);
                    }
                });
                Ajax.get(Global.baseUrl + '/contest/result', {
                    product_id: info.prodId
                }).then(function(response) {
                    if (response.success) {
                        setRankData(contents[0], response.data);
                        me.userData0=response.data.items;
                    }
                });
                on(query('.rank-ctn', contents[0])[0], on.selector('tr', mouse.enter), function(e) {
            	    var items = query('tr',query('.rank-ctn', contents[0])[0]);
                    index = items.indexOf(this);
                    if(index>0){
        	            item = me.userData0[index-1];
        	            that = this;
        	            when(Data.getStockCurentrust({
        	         	   homs_fund_account: item.homsFundAccount,
        	                homs_combine_id: item.homsCombineId,
        	                userId:item.userId
        	            })).then(function(value) {
        	            	   if (value) {
        	                     if(value.length>0){
        	                    	 var data=getMax(value);
        	                    	 var status=data.entrustDirection=='1'?'买入':'卖出';
        	                    	 var html = '<div>'+Global.encodeInfo(item.userRealName, 1, 0)+'在'+formatTime(data.entrustTime)+'委托'+status+data.stockName+'('+data.stockCode+')'+data.entrustAmount+'股</div>' ;
        	                    	 Tooltip.show(html, that);
        	                     }
        	                 }
        	            });
                    }
              });
                on(query('.rank-ctn', contents[0])[0], on.selector('tr', mouse.leave), function(e) {
                    Tooltip.hide();
                });
            }
        });

        Ajax.get(Global.baseUrl + '/contest/info', {
            state: 1 // next vol
        }).then(function(response) {
            if (response.success) {
                var info = response.data[0];
                // product data
                all([Data.getProduct({'product_id': info.prodId}, true), Data.getProductDetails({'product_id': info.prodId}, true)]).then(function(result) {
                    if (result) {
                        product = result[0];
                        result[0].productDetails = result[1];
                        enrollPanel2.set('product', result[0]);
                    }
                });
                setTabData(tabs[1], info);
                setProgressData(contents[1], info);
                var startDate = info.contestBeginDate,
                    endDate = info.contestEndDate,
                    disabled = true,
                    curDate = DateUtil.format(new Date());
                if (curDate >= startDate && curDate <= endDate) {
                    disabled = false;
                }
                enrollPanel2.set('prodId', info.prodId);
                enrollPanel2.set('disabled', disabled);
                enrollPanel2.set('info', info);
                Ajax.get(Global.baseUrl + '/contest/reward', {
                    contest_id: info.contestId
                }).then(function(response) {
                    if (response.success) {
                        setRewardData(contents[1], response.data);
                    }
                });
                Ajax.get(Global.baseUrl + '/contest/result', {
                    product_id: info.prodId
                }).then(function(response) {
                    if (response.success) {
                        setRankData(contents[1], response.data);
                        me.userData1=response.data.items;
                    }
                });
                on(query('.rank-ctn', contents[1])[0], on.selector('tr', mouse.enter), function(e) {
            	    var items = query('tr',query('.rank-ctn', contents[1])[0]);
                    index = items.indexOf(this);
                    if(index>0){
        	            item = me.userData1[index-1];
        	            that = this;
        	            when(Data.getStockCurentrust({
        	         	   homs_fund_account: item.homsFundAccount,
        	                homs_combine_id: item.homsCombineId,
        	                userId:item.userId
        	            })).then(function(value) {
        	            	   if (value) {
        	                     if(value.length>0){
        	                    	 var data=getMax(value);
        	                    	 var status=data.entrustDirection=='1'?'买入':'卖出';
        	                    	 var html = '<div>'+Global.encodeInfo(item.userRealName, 1, 0)+'在'+formatTime(data.entrustTime)+'委托'+status+data.stockName+'('+data.stockCode+')'+data.entrustAmount+'股</div>' ;
        	                    	 Tooltip.show(html, that);
        	                     }
        	                 }
        	            });
                    }
              });
                on(query('.rank-ctn', contents[1])[0], on.selector('tr', mouse.leave), function(e) {
                    Tooltip.hide();
                });
            }
        });

        tabs = query('.list-titleCompetition-item');
        contents = query('#panel1,#panel2');

        on(dom.byId('tabctn'), '.list-titleCompetition-item:click', function(e) {
            var index = tabs.indexOf(this);
            tabs.removeClass('bg-red');
            query(this).addClass('bg-red');
            contents.style({
                display: 'none'
            });
            contents[index].style.display = 'block';
        });

        // place enroll panel
        enrollPanel = new LoanEnrollPanel({}, 'enroll-panel1');
        enrollPanel2 = new LoanEnrollPanel({}, 'enroll-panel2');

        on(enrollPanel.nextBtn, 'click', function() {
            if (enrollPanel.isValid()) {
                enrollPanel.nextBtn.loading(true);
                when(Data.getUser(), function(user) {
                    enrollPanel.nextBtn.loading(false);
                    if (user) {
                        checkRecharge(enrollPanel);
                    } else {
                        if (!loginWin) {
                            loginWin = new LoginWindow({
                                onLogin: function() {
                                    checkRecharge(enrollPanel);
                                }
                            });
                            loginWin.placeAt(document.body);
                        }
                        loginWin.show();
                    }
                });
            }
        });
        on(enrollPanel2.nextBtn, 'click', function() {
            if (enrollPanel2.isValid()) {
                enrollPanel2.nextBtn.loading(true);
                when(Data.getUser(), function(user) {
                    enrollPanel2.nextBtn.loading(false);
                    if (user) {
                        checkRecharge(enrollPanel2);
                    } else {
                        if (!loginWin) {
                            loginWin = new LoginWindow({
                                onLogin: function() {
                                    checkRecharge(enrollPanel2);
                                }
                            });
                            loginWin.placeAt(document.body);
                        }
                        loginWin.show();
                    }
                });
            }
        });

        function checkRecharge(panel) {
            check();
            checkIsTrust();
            when(Data.getDeposit(0), function(deposit) {
                var needAmount = panel.totalAmount;
                if (deposit < needAmount) {
                    if (!win) {
                        win = new Win({
                            width:400,
                            height:170,
                            title:'请充值',
                            style:'padding: 0px'
                        });
                        var warningPanel = new Prompt({
                        	type: 'warning',
                        	msg: '余额不足，请<a target="_blank" href="'+Global.baseUrl+'/account/recharge.htm">充值</a>',
                        	style:'overflow-x: hidden;margin-left:-35px'
                        });
                        warningPanel.placeAt(win.contentNode);
                        win.placeAt(document.body);
                    }
                    win.show();
                } else {
                    Ajax.post(Global.baseUrl + '/financing/loan/apply', {
                        trade_pwd: RSA.encryptedString(key, panel.pwdFld.get('value')),
                        produce_id: panel.prodId,
                        produce_term: product.prodTerms,
                        deposit_amount: panel.depositAmount.toFixed(0),
                        loan_amount: panel.loanAmount.toFixed(0),
                        prev_day: 0
                    }).then(function(response) {
                        if (response.success) {
                            showPanel2(panel);
                        } else {
                            Tooltip.show(response.msg, panel.nextBtn.domNode, 'warning');
                        }
                    });
                }
            });
        }

        function showPanel2(panel) {
            panel.hide();
            if (!prompt) {
                var prompt = new Prompt({
                    type: 'success',
                    msg: '恭喜您，实盘大赛报名申请已成功！',
                    subMsg: '<span style="color: #666666">报名申请正在紧张处理，请耐心等待。如有疑问请拨打客服电话<br/> ' +
                        '<b class="am-ft-orange">'+Global.hotline+'</b></span>',
                    linkText: '查看申请记录',
                    link:'financing/apply.htm'
                });
                prompt.placeAt(panel.domNode.parentNode);
                var helpHTML = '<div class="con-instructions">'+
                    '<b class="am-ft-md">实盘大赛说明：</b>'+
                    '<ul><li>我们提醒您：股市有风险，投资需谨慎！市场风险莫测，务请谨慎行事！</li>'+
                        '<li>比赛保证金：比赛结束时如无亏损全额返保证金及盈利（如有），如亏损扣除亏损剩余返还。</li>'+
                        '<li>比赛周期： 比赛周期为报名后下一个交易日起1个月。</li>'+
                        '<li>报名费：为参赛费用，一经报名成功不予退还。</li>'+
                        '<li>退出比赛：1.  当总资产低于***元时自动平仓并退出比赛，剩余保证金退还给您。2.  中途主动要求退出比赛，平仓后可退出，剩余保证金退还给您，报名费不予退还，比赛成绩不予计入名次。</li>'+
                    '</ul>'+
                '</div>';
                var helpDom = domConstruct.toDom(helpHTML);
                domConstruct.place(helpDom, panel.domNode.parentNode);
            }
        }
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