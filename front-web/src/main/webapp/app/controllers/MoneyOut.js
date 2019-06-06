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
    'app/views/common/TradeSideMenu',
    'app/views/trade/OutInfoPanel',
    'app/views/trade/OutConfirmPanel',
    'dojo/when',
    'app/common/Data',
    'app/ux/GenericTooltip',
    'app/ux/GenericPrompt',
    'dojo/domReady!'
], function (dom, domStyle, domClass, domConstruct, on, query, Ajax, Global, Helper, TradeSideMenu, OutInfoPanel,
             OutConfirmPanel, when, Data, Tooltip, Prompt) {

    var infoPanel,
        confirmPanel,
        successPanel;

    function initView() {
        var sideMenu = new TradeSideMenu({
            active: '1 8'
        });
        sideMenu.placeAt('sidemenuctn');

        infoPanel = OutInfoPanel();
        infoPanel.placeAt('contentctn');
        
        when(Data.getTradeAccounts()).then(function(items) {
            if(items.length==0 || !items){
         	   infoPanel.nextBtn.set('disabled',true);
         	   infoPanel.nextBtn.set('disabledMsg', '您没有操作员编号，不能转出盈利！');
            }
         });
        on(infoPanel.nextBtn, 'click', function () {
            if (infoPanel.isValid()) {
                infoPanel.hide();
                if (!confirmPanel) {
                    confirmPanel = new OutConfirmPanel();
                    confirmPanel.placeAt('contentctn');
                    on(confirmPanel.prevBtn, 'click', function () {
                        confirmPanel.hide();
                        infoPanel.show();
                    });
                    on(confirmPanel.confirmBtn, 'click', function () {
                        confirmPanel.confirmBtn.loading(true);
                        Ajax.post(Global.baseUrl + '/homs/out', {
                            homs_fund_account: infoPanel.tradeAccountFld.activeItem.homsFundAccount,
                            homs_combine_id: infoPanel.tradeAccountFld.activeItem.homsCombineId,
                            trans_amount: infoPanel.amountFld.getAmount()
                        }).then(function (response) {
                            confirmPanel.confirmBtn.loading(false);
                            if (response.success) {
                                confirmPanel.hide();
                                var successPanel = new Prompt({
                                    type: 'success',
                                    msg: '恭喜您，转出成功！',
                                    linkText: '我的资产',
                                    link: 'trade/assets.htm'
                                });
                                successPanel.placeAt('contentctn');
                            } else {
                                Tooltip.show(response.msg, confirmPanel.confirmBtn.innerNode, 'warning');
                            }
                        });
                    });
                }
                confirmPanel.show();
                confirmPanel.setValues(infoPanel.getValues());
            }
        });

        // 可用余额
        when(Data.getAccount()).then(function (acc) {
            infoPanel.set('account', acc);
        });
    }

    return {
        init: function () {
            initView();
            Helper.init();
        }
    }
});