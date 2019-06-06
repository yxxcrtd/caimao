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
    'dojo/dom-construct',
    'dojo/mouse',
    'dijit/focus',
    'dojo/cookie',
    'app/ux/GenericPrompt',
    'app/views/financing/LoanExtendPanel',
    'app/views/financing/LoanExtendSuccessPanel',
    'app/views/financing/ContractSimplePanel',
    'app/views/financing/LoanExtendConfirmPanel',
    'app/views/financing/LoanP1Panel2',
    'app/common/RSA',
    'dojo/_base/lang',
    'app/ux/GenericTooltip',
    'app/common/Date',
    'app/common/Data',
    'dojo/when',
    'dojo/promise/all',
    'app/views/financing/ContractInfoPanel',
    'app/common/Product',
    'dojo/domReady!'
], function (Helper, fx, registry, dom, on, Ajax, query, Global, domClass,
             domConstruct, mouse, focusUtil, cookie, Prompt, LoanExtendPanel,
             LoanExtendSuccessPanel, ContractSimplePanel, LoanExtendConfirmPanel, Content2,
             RSA, lang, Tooltip, DateUtil, Data, when, all, ContractInfoPanel, Product) {

    var config = {};
    var content1, content2, content3,
        key, contractPanel,
        urlParams = Global.getUrlParam(),
        contractNo = urlParams.contract,
        homsCombineId = urlParams.combine,
        homsFundAccount = urlParams.fundaccount,
        contract,
        product,
        views = [],
        product_id,
        ratio,
        loanAmount,
        term,
        detail,
        bill,
        navBarItems = query('li', 'navbar');

    function setBill(c, p, t) {
        bill = Product.getBill(p, Product.getDetail(p, p.productDetails, c.loanRatio, c.loanAmount, t));
        content1.billNode.innerHTML = Product.getBillText(bill, c.loanAmount);
    }

    function initView() {

        when(Data.getContract(contractNo), function (data) {
            contract = data;
            contractPanel.set('contract', contract);
            var promise;
            when(Data.getProduct({product_id: contract.prodId}, true), function (p) {
                if (p.deferedToProd) {
                    promise = all([Data.getProduct({product_id: p.deferedToProd}, true), Data.getProductDetails({product_id: p.deferedToProd}, true)]).then(function (result) {
                        result[0].productDetails = result[1];
                        product = result[0];
                        return result[0];
                    });
                } else {
                    promise = when(Data.getProductDetails({product_id: contract.prodId}, true), function (d) {
                        p.productDetails = d;
                        product = p;
                        return p;
                    });
                }
                when(promise, function (p) {
                    product = p;
                    var temp = p.prodTerms.split(',');
                    var storeData = [], i = 0, len = temp.length;
                    for (; i < len; i++) {
                        storeData.push({'id': temp[i], 'name': temp[i] / 30 + '个月'});
                    }
                    content1.daySelect.store.setData(storeData);
                    setBill(contract, p, content1.daySelect.value);
                });
            });
        });

        domClass.add(navBarItems[0], 'ui-step-active');

        contractPanel = new ContractInfoPanel({
            noLink: true,
            style: 'border-top: 1px solid rgb(224, 224, 224)'
        });
        contractPanel.placeAt('contentctn');
        contractPanel.set('expanded', true);

        // first panel
        content1 = new LoanExtendPanel();
        content1.placeAt('contentctn');

        on(content1.daySelect, 'change', function (value) {
            setBill(contract, product, value);
        });

        on(content1.nextBtn, 'click', function (e) {
            query('.navi-container li.ui-step-active').removeClass('ui-step-active');
            domClass.add(query('.navi-container li')[1], 'ui-step-active');
            dom.byId('panel1').style.display = 'none';
            if (!content2) {
                content2 = new Content2({
                    bill: bill
                });
                content2.nextBtn.set('label', '确认展期');
                content2.placeAt('contentctn');
                on(content2.prevBtn, 'click', function () {
                    content2.hide();
                    query('.navi-container li.ui-step-active').removeClass('ui-step-active');
                    domClass.add(query('.navi-container li')[0], 'ui-step-active');
                    dom.byId('panel1').style.display = 'block';
                });

                on(content2.nextBtn, 'click', function () {
                    if (content2.isValid()) {
                        content2.nextBtn.loading(true);
                        Ajax.post(Global.baseUrl + '/financing/defered/apply', {
                            contract_no: contractNo,
                            trade_pwd: RSA.encryptedString(key, content2.pwdFld.get('value')),
                            produce_id: product.deferedToProd,
                            produce_term: content1.daySelect.value
                        }).then(function (response) {
                            content2.nextBtn.loading(false);
                            if (response.success) {
                                content2.hide();
                                query('.navi-container li.ui-step-active').removeClass('ui-step-active');
                                domClass.add(query('.navi-container li')[2], 'ui-step-active');
                                var successPanel = new Prompt({
                                    type: 'success',
                                    msg: '恭喜您，展期申请已成功！',
                                    subMsg: '<span style="color: #666666">展期申请正在紧张处理，请耐心等待。如有疑问请拨打客服电话 ' +
                                    '<b class="am-ft-orange">' + Global.hotline + '</b></span>',
                                    linkText: '查看申请记录',
                                    link:'financing/apply.htm'
                                });
                                successPanel.placeAt('contentctn');
                            } else {
                                Tooltip.show(response.msg, content2.nextBtn.domNode, 'warning');
                            }
                        });
                    }
                });
            }
            content2.show();
            content2.set('bill', bill);
            content2.setValues({
                depositAmount: 0,
                loanAmount: contract.loanAmount
            });
            all([Data.getDeposit(2, contractNo)]).then(function (result) {
                var deposit = result[0];
                content2.set('availableAmount', deposit || 0);
            });
        });
    }

    function initRSA() {
        Ajax.get(Global.baseUrl + '/sec/rsa', {}).then(function (response) {
            if (response.success) {
                key = RSA.getKeyPair(response.data.exponent, '', response.data.modulus);
            } else {
                //TODO
            }

        });
    }

    return {
        init: function () {
            initView();
            Helper.init(config);
            initRSA();
        }
    }
});