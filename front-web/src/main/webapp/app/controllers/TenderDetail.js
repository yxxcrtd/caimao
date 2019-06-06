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
    'app/common/Data',
    'dojo/when',
    'app/views/financing/TenderPanel1',
    'app/views/financing/TenderPanel2',
    'app/views/financing/TenderPanel3',
    'app/views/financing/TenderPanel4',
    'app/views/financing/TenderSidePanel',
    'app/views/common/LoginWindow',
    'app/ux/GenericTooltip',
    'app/common/RSA',
    'app/common/User',
    'app/ux/GenericMiniMsgBox',
    'app/jquery/Pagination',
    'dojo/_base/config',
    'dojo/domReady!'
], function(dom, domStyle, domClass, domConstruct, on, query, Ajax, Global, Helper, Data, when,
            TenderPanel1, TenderPanel2, TenderPanel3, TenderPanel4, TenderSidePanel, LoginWindow, Tooltip, RSA, User, MiniMsgBox, Pagination,cfg) {

    var panel1, panel2, panel3, panel4, sidePanel,
        invsId = Global.getUrlParam('invs'),
        loginWin, key, msgBox,
        queryParams = {
    	        invs_id: Global.getUrlParam('invs'),
                start: 0,
                limit: 5
         };
    
    function check() {
        User.isTradePwd(null, function() {
        	sidePanel.confirmBtn.set('disabled', true);
        	sidePanel.confirmBtn.set('disabledMsg', '您还未设置安全密码，请先<a href="'+Global.baseUrl+'/account/tradepwd/set.htm">设置</a>');
        });
    }
    
    function checkIsTrust(){
    	User.isTrust(null, function() {
    		sidePanel.confirmBtn.set('disabled', true);
    		sidePanel.confirmBtn.set('disabledMsg', '请您先完成<a target="_blank" href="'+Global.baseUrl+'/user/'+(cfg.authentication ? 'uploadID.htm' : 'certification.htm')+'">实名认证</a>');
        });
    }
    
    function setQuery(params, pageNumber, isFirst) {
	    destoryTenderPayUsers();
        if (isFirst) {
            params.start = 0;
        }
        setTenderPayUsers(params, pageNumber, isFirst);
    }

    function setTenderPayUsers(params, pageNumber, isFirst) {
        when(Data.getTenderPayUsers(params, true), function (data) {
          if (isFirst) {
            $('#light-pagination').pagination({
                items: data.totalCount,
                itemsOnPage: 5,
                prevText: '上一页',
                nextText: '下一页',
                onPageClick: function (pageNumber, e) {
                    queryParams.start = (pageNumber - 1) * queryParams.limit;
                    setQuery(queryParams, pageNumber);
                }
            });
         }
         panel4.set('users', data);
    });
   }
    
   function destoryTenderPayUsers(){
		var items = $('#payUser tr');//获取id为payUser的下的所有tr
        if (items.length > 1) {
            for (var i = 1; i < items.length; i++) {//将除了第一行的所有数据先删除，再赋值
               domConstruct.destroy(items[i]);
            }
        }
	}
   
    function initView() {
        panel1 = new TenderPanel1();
        panel1.placeAt('leftctn');
        panel2 = new TenderPanel2();
        panel2.placeAt('leftctn');
        panel3 = new TenderPanel3();
        panel3.placeAt('leftctn');
        panel4 = new TenderPanel4();
        panel4.placeAt('leftctn');
        sidePanel = new TenderSidePanel();
        sidePanel.placeAt('rightctn');

        when(Data.getTenderDetail({id: invsId}, true), function(data) {
            panel1.set('tender', data);
            panel2.set('tender', data);
            panel3.set('tender', data);
            sidePanel.set('tender', data);
        });
        setTenderPayUsers(queryParams, 1, true);
        
        when(Data.getTenderPayUsers({invs_id: invsId,
            start: 0,
            limit: 1000}, true), function(data) {
            panel4.set('totalInfos', data);
        });

        when(Data.getUser(), function(user) {
            if (user) {
                when(Data.getAccount(), function(acc) {
                    sidePanel.set('availableAmount', acc.avalaibleAmount - acc.freezeAmount);
                });
            }
        });

        on(sidePanel.confirmBtn, 'click', function() {
            if (sidePanel.isValid()) {
                sidePanel.confirmBtn.loading(true);
                when(Data.getUser(), function(user) {
                    if (user) {
                    	checkIsTrust();
                    	check();
                        Ajax.post(Global.baseUrl + '/p2p/subject/pay', {
                            invs_id: invsId,
                            pay_amount: sidePanel.amountFld.getAmount(),
                            trade_pwd: RSA.encryptedString(key, sidePanel.pwdFld.get('value'))
                        }).then(function(response) {
                            sidePanel.confirmBtn.loading(false);
                            if (!msgBox) {
                                msgBox = new MiniMsgBox();
                                msgBox.placeAt(document.body);
                            }
                            if (response.success) {
                                msgBox.setValues({
                                    type: 'success',
                                    text: '投标成功'
                                });
                                msgBox.show();
                                setTimeout(function() {
                                    location.reload();
                                }, 2000);
                            } else {
                                Tooltip.show(response.msg, sidePanel.confirmBtn.innerNode, 'warning');
                            }
                            sidePanel.reset();
                        });
                    } else {
                        sidePanel.confirmBtn.loading(false);
                        if (!loginWin) {
                            loginWin = new LoginWindow({
                                onLogin:function() {
                                    when(Data.getAccount(), function(acc) {
                                        sidePanel.set('availableAmount', acc.avalaibleAmount - acc.freezeAmount);
                                    });
                                    checkIsTrust();
                                    check();
                                }
                            });
                            loginWin.placeAt(document.body);
                        }
                        loginWin.show();
                    }
                });
            }
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