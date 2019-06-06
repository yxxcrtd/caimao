define([
    'app/controllers/Helper',
    'dojo/_base/fx',
    'dijit/registry',
    'dojo/dom',
    'dojo/on',
    'app/common/Ajax',
    'dojo/dom-style',
    'dojo/dom-attr',
    'dojo/query',
    'app/common/Global',
    'dojo/dom-class',
    'dojo/dom-construct',
    'dojo/mouse',
    'dijit/focus',
    'app/stores/GridStore',
    'app/ux/GenericGrid',
    'dojo/date/stamp',
    'dojo/cookie',
    'app/common/RSA',
    'dojo/_base/lang',
    'app/ux/GenericTooltip',
    'app/views/common/SideMenu',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'app/common/Data',
    'dojo/date',
    'app/common/Date',
    'dojo/when',
    'app/ux/GenericDateRange',
    'app/ux/GenericDateQuickSelect',
    'app/ux/GenericPrompt',
    'app/common/Dict',
    'app/common/User',
    'dojo/_base/config',
    'dojo/domReady!'
], function (Helper, fx, registry, dom, on, Ajax, domStyle, domAttr, query, Global, domClass, domConstruct, mouse, focusUtil, Store, Grid, stamp, cookie, RSA, lang, Tooltip, SideMenu, TextBox, Button, Data, date, DateUtil, when, DateRange, DateQuickSelect, Prompt, Dict, User,cfg) {

    var config = {}, fundGrid,
        withdrawAmountFld, tradePwdFld, nextBtn, dateRange, dateQuickSelect, gridLayout, status;
    var inquiryBtn = dom.byId('inquirybutton'),
        filterList = dom.byId('filterlist'),
        typeItems = query('li', filterList),
        bankcard;

    function showGrid() {
        var data = dateQuickSelect.getData();
        if (fundGrid) {
            fundGrid.setQuery({
                start_date: data.start_date,
                end_date: data.end_date,
                //默认的是待审核
                status: status || 'all'
            });
        }
    }

    function check() {
        User.isTradePwd(null, function () {
            nextBtn.set('disabled', true);
            nextBtn.set('disabledMsg', '您还未设置安全密码，请先<a href="' + Global.baseUrl + '/account/tradepwd/set.htm">设置</a>');
        });
    }

    function initView() {

        // check
        check();

        //左边导航栏
        var sideMenu = new SideMenu({
            active: '3 2'
        });
        sideMenu.placeAt('sidemenuctn');
        //获取账户信息
        when(Data.getAccount(), function (account) {
            dom.byId('accountBalance').innerHTML = Global.formatAmount(account.avalaibleAmount - account.freezeAmount, 2);
            withdrawAmountFld.validates.push({
                pattern: function () {
                    return this.getAmount() <= account.avalaibleAmount - account.freezeAmount + '';
                },
                message: '最大提现金额为' + Global.formatAmount(account.avalaibleAmount - account.freezeAmount, 2)
            });
            withdrawAmountFld.max = account.avalaibleAmount - account.freezeAmount;
            withdrawAmountFld.set('placeHolder', '最大提现' + Global.formatAmount(account.avalaibleAmount - account.freezeAmount));
        });
        when(Data.getBankcards(), function (bankcards) {
            var i = 0, len = bankcards.length;
            for (; i < len; i++) {
                if (bankcards[i].bankCardStatus == '1') {
                    bankcard = bankcards[i];
                    break;
                }
            }
            if (!bankcard) {
                nextBtn.set('disabled', true);
                nextBtn.set('disabledMsg', '您没有<a target="_blank" href="' + Global.baseUrl + '/account/bankcard/bind.htm?edit=0">绑定银行卡</a>，无法提现');
            } else {
                // 如果没有设置开户行等信息，让用户进行重新绑定
                if(bankcard.openBank == null || bankcard.openBank == "") {
                    nextBtn.set('disabled', true);
                    nextBtn.set('disabledMsg', '绑定银行卡未设置开户行等信息，请重新进行<a target="_blank" href="' + Global.baseUrl + '/account/bankcard/bind.htm?edit=0">绑定银行卡</a>');
                } else {
                    dom.byId('bankCardNo').innerHTML = bankcard.bankName + '  ' + Global.encodeInfo(bankcard.bankCardNo, 3, 3);
                }
            }

        });

        when(User.isTrust(function () {
        }, function () {
            nextBtn.set('disabled', true);
            nextBtn.set('disabledMsg', '请您先完成<a target="_blank" href="' + Global.baseUrl + '/user/'+(cfg.authentication ? 'uploadID.htm' : 'certification.htm')+'">实名认证</a>');
        }));

        var tab = dom.byId('tab');
        var panels = query('.panel', tab);
        var spans = query('.head span', tab);
        var s_id = 0;
        spans.forEach(function (item, i) {
            on(item, 'click', function () {
                tabView(item, i);
            });


        });
        function tabView(item, i) {
            domClass.remove(spans[s_id], 'select');
            domClass.add(item, 'select');
            domStyle.set(panels[s_id], 'display', 'none');
            domStyle.set(panels[i], 'display', '');
            s_id = i;
            showGrid();
            Tooltip.hide();
        }

        // 取现金额文本框
        withdrawAmountFld = new TextBox({
            validates: [
                {
                    pattern: /.+/,
                    message: '请输入提现金额'
                }
            ],
            unit: '元',
            limitRegex: /[\d\.]/,
            isAmount: true,
            isNumber: true,
            min: 100
        }, 'withdraw-amount-fld');

        //安全密码文本框
        tradePwdFld = new TextBox({
            validates: [
                {
                    pattern: /.+/,
                    message: '请输入安全密码'
                }
            ],
            type: 'password'
        }, 'trade-pwd-fld');
        //下一步
        nextBtn = new Button({
            label: '确认提现',
            enter: true,
            width: 200,
            onClick: function () {

            }
        }, 'nextBtn');

        on(nextBtn, 'click', function () {
            if (withdrawAmountFld.checkValidity() && tradePwdFld.checkValidity()) {
                nextBtn.loading(true);
                Ajax.post(Global.baseUrl+'/account/withdraw', RSA.encrypt(key, getData(), 'trade_pwd')
                ).then(function (response) {
                        nextBtn.loading(false);
                        if (response.success) {
                            dom.byId('panel1').style.display = 'none';
                            var successPanel = new Prompt({
                                type: 'success',
                                msg: '恭喜您，取现申请成功！',
                                subMsg: '<span style="color: #666666">审核通过后预计将于24小时内到账，请注意查收。</span>',
                                linkText: '查看取现记录',
                                linkClick: function() {
                                    dom.byId('panel1').style.display = 'block';
                                    on.emit(dom.byId('withdrawrecord'), 'click', {
                                        bubbles: true,
                                        cancelable: true
                                    });
                                    successPanel.hide(true);
                                }
                            });
                            successPanel.placeAt('panelctn');
                            successPanel.show();
                        } else {
                            Tooltip.show(response.msg, nextBtn.innerNode, 'warning');
                        }
                    });
            }

        });

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

    }

    //银行卡展示
//	Ajax.get(Global.baseUrl + "/bankcard").then(function(response){
//		if(response.success){
//			if('1' == response.data[0].bankCardStatus){
//				dom.byId('bankName').innerHTML = response.data[0].bankName;
//				dom.byId('bankCardNo').innerHTML = response.data[0].bankCardNo;
//			}
//			if('0' == response.data[0].bankCardStatus){
//				alert("银行卡待验");
//			}
//			if('3' == response.data[0].bankCardStatus){
//				alert('银行卡由主卡变副卡');
//			}
//		}
//		else{
//			alert('银行卡展示错误');
//		}
//	});

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
                //默认的是待审核
                status: status || 'all'
            });
        }
    });

    dateRange.placeAt('daterangectn');
    dateQuickSelect.placeAt('datequickselectctn');

    var formatAmount = function (value, rowIndex, column) {
        return '<b>' + Global.formatAmount(value, 2) + '</b>';
    };

    function formatOrderStatus(value, rowIndex, column) {
        return Dict.get('orderStatus')[parseInt(value)];
    };
    function formatCancel(value, rowIndex, column) {
        var calcelUrl = Global.baseUrl + '/account/withdraw/cancel?orderNo='+value;
        return store.getValue(store.grid.getItem(rowIndex), "orderStatus") == "02" ? '<a href="'+ calcelUrl +'">取消提现</a>' : "";
    };
    gridLayout = [
        [
            {'name': '订单号', 'field': 'orderNo', 'width': '20%', styles: 'text-align: center;', noresize: true},
            {'name': '申请日期', 'field': 'createDatetime', 'width': '15%', styles: 'text-align: center;', noresize: true},
            {'name': '取现金额（元）', 'field': 'orderAmount', 'width': '15%', styles: 'text-align: right;', noresize: true, formatter: formatAmount},
            {'name': '银行卡号', 'field': 'bankCardNo', 'width': '20%', styles: 'text-align: center;', noresize: true},
            {'name': '状态', 'field': 'orderStatus', 'width': '15%', styles: 'text-align: center;', noresize: true, formatter: formatOrderStatus},
            {'name': '操作', 'field': 'orderNo', 'width': '15%', styles: 'text-align: center;', noresize: true, formatter: formatCancel}
        ]
    ];

    store = new Store({
        target: Global.baseUrl + '/account/withdraw/page',
        allowNoTrailingSlash: true
    });

    var gridCfg = {
        store: store,
        structure: gridLayout,
        pageSize: 5,
        selectable: false
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

    Global.focusText();

    function initRSA() {
        Ajax.get(Global.baseUrl + '/sec/rsa', {
        }).then(function (response) {
            if (response.success) {
                key = RSA.getKeyPair(response.data.exponent, '', response.data.modulus);
            } else {
            }

        });
    }

    function getData() {
        return {
            withdraw_amount: withdrawAmountFld.getAmount(),
            trade_pwd: tradePwdFld.get('value')
        };
    }

    return {
        init: function () {
            initView();
            initRSA();
            Helper.init(config);
        }
    }
});