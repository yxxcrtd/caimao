define([
    'app/controllers/Helper',
    'dojo/_base/fx',
    'dijit/registry',
    'dojo/dom',
    'dojo/on',
    'app/common/Ajax',
    'dojo/query',
    'app/common/Global',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/cookie',
    'dojo/dom-attr',
    'app/ux/GenericTooltip',
    'app/views/common/SideMenu',
    'app/views/account/RechargeFastInfoPanel',
    'app/views/account/RechargeFastConfirmPanel',
    'app/views/account/RechargeInfoPanel',
    'app/views/account/RechargeConfirmPanel',
    'app/views/account/TransferConfirmationPanel',
    'dojo/when',
    'app/common/Data',
    'app/common/Date',
    'app/ux/GenericGrid',
    'dojo/date/stamp',
    "app/ux/GenericTextBox",
    'app/ux/GenericButton',
    'app/ux/GenericDateQuickSelect',
    'app/ux/GenericDateRange',
    'app/stores/GridStore',
    'app/common/User',
    'app/common/Dict',
    'app/ux/GenericWindow',
    'dojo/_base/config',
    'dojo/domReady!'
], function (Helper, fx, registry, dom, on, Ajax, query, Global, domClass, domStyle,
             cookie, domAttr, Tooltip, SideMenu, RechargeFastInfoPanel, RechargeFastConfirmPanel,
             RechargeInfoPanel, RechargeConfirmPanel, TransferConfirmationPanel, when, Data, DateUtil, Grid, stamp, TextBox, Button, DateQuickSelect,
             DateRange, Store, User, Dict, Win, cfg) {

    var config = {};
    var rechangeFastInfoPanel, rechangeFastConfirmPanel, rechangeInfoPanel, rechargeConfirmPanel, submitForm;
    var inquiryBtn = dom.byId('inquirybutton'),
        filterList = dom.byId('filterlist'),
        typeItems = query('li', filterList),
        status,
        win;

    function getFee(bank, amount) {
        var fee = 0.00;
        if (bank && amount) {
            fee = bank.rates * amount;
        }
        return fee;
    }

    function initView() {

        when(User.isTrust(function () {
        }, function () {
            // rechangeFastInfoPanel.nextBtn.set('disabled', true);
            rechangeInfoPanel.nextBtn.set('disabled', true);
            // rechangeFastInfoPanel.nextBtn.set('disabledMsg', '请您先完成<a target="_blank" href="'+Global.baseUrl+'/user/certification.htm">实名认证</a>');
            rechangeInfoPanel.nextBtn.set('disabledMsg', '请您先完成<a target="_blank" href="' + Global.baseUrl + '/user/' + (cfg.authentication ? 'uploadID.htm' : 'certification.htm') + '">实名认证</a>');
            nextAlipayButton.set('disabled', true);
            nextAlipayButton.set('disabledMsg', '请您先完成<a target="_blank" href="' + Global.baseUrl + '/user/' + (cfg.authentication ? 'uploadID.htm' : 'certification.htm') + '">实名认证</a>');
            transferConfirmationPanel.nextBtn.set('disabled', true);
            transferConfirmationPanel.nextBtn.set('disabledMsg', '请您先完成<a target="_blank" href="' + Global.baseUrl + '/user/' + (cfg.authentication ? 'uploadID.htm' : 'certification.htm') + '">实名认证</a>');
        }));

        var sideMenu = new SideMenu({
            active: '3 1'
        });
        sideMenu.placeAt('sidemenuctn');

        var tab = dom.byId('tab');
        var panels = query('.panel', tab);
        var spans = query('.head span', tab);
        var s_id = 0;
        spans.forEach(function (item, i) {
            on(item, 'click', function () {
                tabView(item, i);
                Tooltip.hide();
            });

        });

        function tabView(item, i) {
            domClass.remove(spans[s_id], 'select');
            domClass.add(item, 'select');
            domStyle.set(panels[s_id], 'display', 'none');
            domStyle.set(panels[i], 'display', '');
            s_id = i;
            showGrid();
        }

//        rechangeFastInfoPanel = new RechargeFastInfoPanel();
//        rechangeFastInfoPanel.placeAt('panel1');
//        on(rechangeFastInfoPanel.nextBtn, 'click', function () {
//            rechangeFastInfoPanel.hide();
//            if (!rechangeFastConfirmPanel) {
//                rechangeFastConfirmPanel = new RechargeFastConfirmPanel();
//                rechangeFastConfirmPanel.placeAt('panel1');
//                on(rechangeFastConfirmPanel.preBtn, "click", function () {
//                    rechangeFastConfirmPanel.hide();
//                    rechangeFastInfoPanel.show();
//                });
//            } else {
//                rechangeFastConfirmPanel.show();
//            }
//        })
//        rechangeFastInfoPanel.bankListFld.onSelect = function (bank) {
//            rechangeFastInfoPanel.set('fee', getFee(bank, rechangeFastInfoPanel.amountFld.getAmount()));
//        };
//        on(rechangeFastInfoPanel.amountFld, 'keyup', function () {
//            rechangeFastInfoPanel.set('fee', getFee(rechangeFastInfoPanel.bankListFld.getBank(), this.getAmount()));
//        });

        //网银充值
        rechangeInfoPanel = new RechargeInfoPanel();
        rechangeInfoPanel.placeAt('panel2');

        when(Data.getBanks(), function (banks) {
            rechangeInfoPanel.bankListFld.set('banks', banks);
            //rechangeFastInfoPanel.bankListFld.set('banks', banks);
        });
        when(Data.getAccount(), function (account) {
            rechangeInfoPanel.set('avalaibleAmount', account.avalaibleAmount - account.freezeAmount);
            //rechangeFastInfoPanel.set('avalaibleAmount', account.avalaibleAmount);
        });

        on(rechangeInfoPanel.nextBtn, 'click', function () {
            if (rechangeInfoPanel.isValid()) {
                rechangeInfoPanel.hide();
                if (!rechargeConfirmPanel) {
                    rechargeConfirmPanel = new RechargeConfirmPanel();
                    rechargeConfirmPanel.placeAt('panel2');
                    //上一步
                    on(rechargeConfirmPanel.preBtn, 'click', function () {
                        rechargeConfirmPanel.hide();
                        rechangeInfoPanel.show();
                    });
                    //确认充值
                    on(rechargeConfirmPanel.confirmBtn, 'click', function () {
                        rechargeConfirmPanel.confirmBtn.loading(true);
                        accountChargeUrl = "/account/charge";
                        if (cfg.paymentPlatform == "c") {   // 汇付宝单独的
                            accountChargeUrl = "/account/charge/heepay";
                        }
                        Ajax.post(Global.baseUrl + accountChargeUrl, {
                            //充值金额
                            'charge_amount': rechangeInfoPanel.amountFld.getAmount(),
                            //三方支付公司编号
                            'pay_company_no': cfg.paymentPlatform == 'a' ? 1 : (cfg.paymentPlatform == 'b' ? 2 : 3),//1:通联  2：易宝  3: 汇付宝
                            //银行行别
                            'bank_no': rechangeInfoPanel.bankListFld.getBank().bankNo,
                            'pay_type': cfg.paymentPlatform == 'a' ? '1' : (cfg.paymentPlatform == 'b' ? '12' : '20'), // 1：通联网银   12：易宝网银 20: 汇付宝银行卡支付
                            'terminal_type': 0
                        }, true).then(function (response) {
                            rechargeConfirmPanel.confirmBtn.loading(false);
                            if (response.success) {
                                if (cfg.paymentPlatform == 'a') {//通联
                                    submitForm = dom.byId('recharge-form');
                                    //url
                                    domAttr.set(submitForm, {
                                        'action': response.data.submitUrl
                                    });
                                    query('input[name=orderCurrency]', submitForm)[0].value = response.data.orderCurrency;
                                    query('input[name=orderAmount]', submitForm)[0].value = response.data.orderAmount;
                                    query('input[name=orderNo]', submitForm)[0].value = response.data.orderNo;
                                    query('input[name=signMsg]', submitForm)[0].value = response.data.signMsg;
                                    query('input[name=inputCharset]', submitForm)[0].value = response.data.inputCharset;
                                    query('input[name=pickupUrl]', submitForm)[0].value = response.data.pickupUrl;
                                    query('input[name=receiveUrl]', submitForm)[0].value = response.data.receiveUrl;
                                    query('input[name=version]', submitForm)[0].value = response.data.version;
                                    query('input[name=language]', submitForm)[0].value = response.data.language;
                                    query('input[name=signType]', submitForm)[0].value = response.data.signType;
                                    query('input[name=merchantId]', submitForm)[0].value = response.data.merchantId;
                                    query('input[name=payType]', submitForm)[0].value = response.data.payType;
                                    query('input[name=orderDatetime]', submitForm)[0].value = response.data.orderDatetime;
                                    query('input[name=issuerId]', submitForm)[0].value = response.data.issuerId;
                                    query('input[name=pan]', submitForm)[0].value = response.data.pan;
                                    query('input[name=ext1]', submitForm)[0].value = response.data.ext1;
                                    query('input[name=ext2]', submitForm)[0].value = response.data.ext2;
                                    submitForm.submit();
                                } else if (cfg.paymentPlatform == 'b') {//易宝
                                    submitForm = dom.byId('recharge-form_yibao');
                                    //url
                                    domAttr.set(submitForm, {
                                        'action': response.data.submitUrl
                                    });
                                    query('input[name=p0_Cmd]', submitForm)[0].value = response.data.ext1;
                                    query('input[name=p1_MerId]', submitForm)[0].value = response.data.merchantId;
                                    query('input[name=p2_Order]', submitForm)[0].value = response.data.orderNo;
                                    query('input[name=p3_Amt]', submitForm)[0].value = response.data.orderAmount;
                                    query('input[name=p4_Cur]', submitForm)[0].value = response.data.orderCurrency;
                                    query('input[name=p8_Url]', submitForm)[0].value = response.data.receiveUrl;
                                    query('input[name=pa_MP]', submitForm)[0].value = response.data.ext2;
                                    query('input[name=pd_FrpId]', submitForm)[0].value = response.data.pan;
                                    query('input[name=hmac]', submitForm)[0].value = response.data.signMsg;
                                    submitForm.submit();
                                } else {
                                    submitForm = dom.byId('recharge-form_heepay');
                                    //url
                                    domAttr.set(submitForm, {
                                        'action': response.data.submitUrl
                                    });
                                    query('input[name=version]', submitForm)[0].value = response.data.version;
                                    query('input[name=is_phone]', submitForm)[0].value = response.data.isPhone;
                                    query('input[name=pay_type]', submitForm)[0].value = response.data.payType;
                                    query('input[name=pay_code]', submitForm)[0].value = response.data.payCode;
                                    query('input[name=agent_id]', submitForm)[0].value = response.data.agentId;
                                    query('input[name=agent_bill_id]', submitForm)[0].value = response.data.agentBillId;
                                    query('input[name=pay_amt]', submitForm)[0].value = response.data.payAmt;
                                    query('input[name=notify_url]', submitForm)[0].value = response.data.notifyUrl;
                                    query('input[name=return_url]', submitForm)[0].value = response.data.returnUrl;
                                    query('input[name=user_ip]', submitForm)[0].value = response.data.userIp;
                                    query('input[name=agent_bill_time]', submitForm)[0].value = response.data.agentBillTime;
                                    query('input[name=goods_name]', submitForm)[0].value = response.data.goodsName;
                                    query('input[name=goods_num]', submitForm)[0].value = response.data.goodsNum;
                                    query('input[name=remark]', submitForm)[0].value = response.data.remark;
                                    if (response.data.isTest == "1") {
                                        query('input[name=is_test]', submitForm)[0].value = response.data.isTest;
                                    }
                                    query('input[name=goods_note]', submitForm)[0].value = response.data.goodsNote;
                                    query('input[name=sign]', submitForm)[0].value = response.data.sign;
                                    submitForm.submit();
                                }
                            } else {
                                Tooltip.show(response.msg, rechargeConfirmPanel.confirmBtn.domNode, 'warning');
                            }
                        });

                    });
                }
                rechargeConfirmPanel.show();
                rechargeConfirmPanel.setValues(rechangeInfoPanel.getValues());
            }
        });

        function againRecharge(no, type) {
            Ajax.post(Global.baseUrl + '/account/againCharge/heepay', {
                'no': no,
                'type': type,
                'terminal_type': 0
            }, true).then(function (response) {
                //rechargeConfirmPanel.confirmBtn.loading(false);
                if (response.success) {
                    if (type == 'a') {//通联
                        submitForm = dom.byId('againCharge-form');
                        //url
                        domAttr.set(submitForm, {
                            'action': response.data.submitUrl
                        });
                        query('input[name=orderCurrency]', submitForm)[0].value = response.data.orderCurrency;
                        query('input[name=orderAmount]', submitForm)[0].value = response.data.orderAmount;
                        query('input[name=orderNo]', submitForm)[0].value = response.data.orderNo;
                        query('input[name=signMsg]', submitForm)[0].value = response.data.signMsg;
                        query('input[name=inputCharset]', submitForm)[0].value = response.data.inputCharset;
                        query('input[name=pickupUrl]', submitForm)[0].value = response.data.pickupUrl;
                        query('input[name=receiveUrl]', submitForm)[0].value = response.data.receiveUrl;
                        query('input[name=version]', submitForm)[0].value = response.data.version;
                        query('input[name=language]', submitForm)[0].value = response.data.language;
                        query('input[name=signType]', submitForm)[0].value = response.data.signType;
                        query('input[name=merchantId]', submitForm)[0].value = response.data.merchantId;
                        query('input[name=payType]', submitForm)[0].value = response.data.payType;
                        query('input[name=orderDatetime]', submitForm)[0].value = response.data.orderDatetime;
                        query('input[name=issuerId]', submitForm)[0].value = response.data.issuerId;
                        query('input[name=pan]', submitForm)[0].value = response.data.pan;
                        query('input[name=ext1]', submitForm)[0].value = response.data.ext1;
                        query('input[name=ext2]', submitForm)[0].value = response.data.ext2;
                        submitForm.submit();
                    } else if (type == "b") {//易宝
                        submitForm = dom.byId('againCharge-form_yibao');
                        //url
                        domAttr.set(submitForm, {
                            'action': response.data.submitUrl
                        });
                        query('input[name=p0_Cmd]', submitForm)[0].value = response.data.ext1;
                        query('input[name=p1_MerId]', submitForm)[0].value = response.data.merchantId;
                        query('input[name=p2_Order]', submitForm)[0].value = response.data.orderNo;
                        query('input[name=p3_Amt]', submitForm)[0].value = response.data.orderAmount;
                        query('input[name=p4_Cur]', submitForm)[0].value = response.data.orderCurrency;
                        query('input[name=p8_Url]', submitForm)[0].value = response.data.receiveUrl;
                        query('input[name=pa_MP]', submitForm)[0].value = response.data.ext2;
                        query('input[name=pd_FrpId]', submitForm)[0].value = response.data.pan;
                        query('input[name=hmac]', submitForm)[0].value = response.data.signMsg;
                        submitForm.submit();
                    } else {
                        submitForm = dom.byId('againCharge-form_heepay');
                        //url
                        domAttr.set(submitForm, {
                            'action': response.data.submitUrl
                        });
                        query('input[name=version]', submitForm)[0].value = response.data.version;
                        query('input[name=is_phone]', submitForm)[0].value = response.data.isPhone;
                        query('input[name=pay_type]', submitForm)[0].value = response.data.payType;
                        query('input[name=pay_code]', submitForm)[0].value = response.data.payCode;
                        query('input[name=agent_id]', submitForm)[0].value = response.data.agentId;
                        query('input[name=agent_bill_id]', submitForm)[0].value = response.data.agentBillId;
                        query('input[name=pay_amt]', submitForm)[0].value = response.data.payAmt;
                        query('input[name=notify_url]', submitForm)[0].value = response.data.notifyUrl;
                        query('input[name=return_url]', submitForm)[0].value = response.data.returnUrl;
                        query('input[name=user_ip]', submitForm)[0].value = response.data.userIp;
                        query('input[name=agent_bill_time]', submitForm)[0].value = response.data.agentBillTime;
                        query('input[name=goods_name]', submitForm)[0].value = response.data.goodsName;
                        query('input[name=goods_num]', submitForm)[0].value = response.data.goodsNum;
                        query('input[name=remark]', submitForm)[0].value = response.data.remark;
                        if (response.data.isTest == "1") {
                            query('input[name=is_test]', submitForm)[0].value = response.data.isTest;
                        }
                        query('input[name=goods_note]', submitForm)[0].value = response.data.goodsNote;
                        query('input[name=sign]', submitForm)[0].value = response.data.sign;
                        submitForm.submit();
                    }
                } else {
                    Tooltip.show(response.msg, rechargeConfirmPanel.confirmBtn.domNode, 'warning');
                }
            });
        }

        //*******************支付宝转账  start*******************//
        var alipayFld = new TextBox({
            style: {
                'display': 'inline-block'
            },
            validates: [{
                pattern: /.+/,
                message: '请输入支付宝账号'
            }]
        }, 'alipayFldId');
        var alipayAmount = new TextBox({
            style: {
                'display': 'inline-block'
            },
            validates: [{
                pattern: /.+/,
                message: '请输入充值金额'
            }],
            limitRegex: /[\d\.]/,
            isAmount: true,
            isNumber: true
        }, 'alipayAmountId');

        var nextAlipayButton = new Button({
            label: '确定',
            enter: true,
            position: 'center',
            width: 150,
            onClick: function () {
                if (alipayFld.checkValidity() && alipayAmount.checkValidity()) {
                    nextAlipayButton.loading(true);
                    Ajax.post(Global.baseUrl + "/account/charge", {
                        pay_company_no: '-1',
                        order_abstract: alipayFld.value,//支付宝账号
                        charge_amount: alipayAmount.getAmount(),//充值金额
                        terminal_type: 0,
                        pay_type: -1
                    }).then(function (response) {
                        nextAlipayButton.loading(false);
                        if (response.success) {
                            dom.byId('alipay1').style.display = "none";
                            dom.byId('alipay2').style.display = "block";
                            dom.byId('u_alipayName').innerHTML = alipayFld.value;
                            dom.byId('u_money').innerHTML = alipayAmount.value;
                        } else {
                            alert(response.msg);
                        }

                    });
                }
            }
        }, 'nextAlipayButton');

        if (dom.byId('zfbprevbtn')) {
            on(dom.byId('zfbprevbtn'), 'click', function (e) {
                dom.byId('alipay2').style.display = 'none';
                dom.byId('alipay1').style.display = 'block';
            });
        }

        //*******************支付宝转账  end*******************//

        //充值记录
        //***************充值记录***********************//
        function showGrid() {
            var data = dateQuickSelect.getData();
            if (fundGrid) {
                fundGrid.setQuery({
                    start_date: data.start_date,
                    end_date: data.end_date,
                    //默认的是充值成功
                    status: status || 'all'
                });
            }
        }

        on(inquiryBtn, 'click', function () {
            var data = dateRange.getData();
            fundGrid.setQuery({
                start_date: data.start_date,
                end_date: data.end_date,
                status: status || 'all'
            });
        });

        on(filterList, 'li:click', function (e) {
            status = domAttr.get(this, 'data-type');
            var activeEl = query('.active', filterList)[0];
            domClass.remove(activeEl, 'active');
            activeEl = query('a', this)[0];
            domClass.add(activeEl, 'active');
            var data = dateRange.getData();
            fundGrid.setQuery({
                start_date: data.start_date,
                end_date: data.end_date,
                status: status || 'all'
            });
        });

        dateRange = new DateRange({
            startDate: new Date(),
            endDate: new Date()
        });
        dateQuickSelect = new DateQuickSelect({
            onClick: function () {
                var data = dateQuickSelect.getData();
                dateRange.setValues({
                    startDate: DateUtil.parse(data.start_date),
                    endDate: DateUtil.parse(data.end_date)
                });
                fundGrid.setQuery({
                    start_date: data.start_date,
                    end_date: data.end_date,
                    //默认的充值成功
                    status: status || 'all'
                });
            }
        });

        dateRange.placeAt('daterangectn');
        dateQuickSelect.placeAt('datequickselectctn');

        var formatAmount = function (value, rowIndex, column) {
            return '<b>' + Global.formatAmount(value, 2) + '</b>';
        };
        var formatStatus = function (value, rowIndex, column) {
            return Dict.get('orderStatus')[parseInt(value)];
        };
        var formatAction = function (value, rowIndex, column) {
            var item = this.grid.store.items[rowIndex];

            if (item.orderStatus == '02') {
                return '<a>再充值</a>';
            } else {
                return '';
            }
        };

        gridLayout = [[
            {'name': '单号', 'field': 'orderNo', 'width': '20%', styles: 'text-align: center;', noresize: true},
            {
                'name': '订单金额（元）',
                'field': 'orderAmount',
                'width': '15%',
                styles: 'text-align: right;',
                noresize: true,
                formatter: formatAmount
            },
            {
                'name': '创建时间',
                'field': 'paySubmitDatetime',
                'width': '15%',
                styles: 'text-align: center;',
                noresize: true
            },
            {
                'name': '订单状态',
                'field': 'orderStatus',
                'width': '15%',
                styles: 'text-align: center;',
                noresize: true,
                formatter: formatStatus
            },
            {
                'name': '操作',
                'field': 'operate',
                action: true,
                'width': '15%',
                styles: 'text-align: center;',
                noresize: true,
                formatter: formatAction
            },
            {'name': '摘要', 'field': 'orderAbstract', 'width': '20%', styles: 'text-align: center;', noresize: true}
        ]];

        var store = new Store({
            target: Global.baseUrl + '/account/charge/page',
            allowNoTrailingSlash: true
        });
        var gridCfg = {
            store: store,
            structure: gridLayout,
            pageSize: 5
        };
        fundGrid = new Grid(gridCfg);
        fundGrid.setQuery({
            'status': 'all',
            'start_date': stamp.toISOString(new Date(), {
                selector: 'date'
            }),
            'end_date': stamp.toISOString(new Date(), {
                selector: 'date'
            })
        });
        store.grid = fundGrid;
        fundGrid.placeAt('fundgrid');
        fundGrid.startup();

        on(fundGrid, 'cellClick', function (e) {
            if (e.cell.field === 'operate') {
                var item = e.grid.store.items[e.rowIndex];

                if (item.orderStatus == '02') {
                    againRecharge(item.orderNo, cfg.paymentPlatform);
                }
            }
        });

        Global.focusText();
        on(rechangeInfoPanel.amountFld, 'keyup', function () {
            rechangeInfoPanel.set('fee', getFee(rechangeInfoPanel.bankListFld.getBank(), this.getAmount()));
        });

        rechangeInfoPanel.bankListFld.onSelect = function (bank) {
            rechangeInfoPanel.set('fee', getFee(bank, rechangeInfoPanel.amountFld.getAmount()));
        };

        //银行转账确认单start
        transferConfirmationPanel = new TransferConfirmationPanel();

        transferConfirmationPanel.placeAt('transferConfirm');
        //银行转账确认单end
        // 如果是充值回调, 返回充值列表
        var pageGoto = query("#pageGoto")[0].value;
        if(pageGoto == "rechargeList"){
            tabView(spans[3], 3);
        }
    }

    return {
        init: function () {
            initView();
            Helper.init(config);
        }
    }
});