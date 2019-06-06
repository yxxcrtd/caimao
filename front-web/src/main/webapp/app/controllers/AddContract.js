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
    'app/views/financing/LoanContractRulePanel',
    'app/views/financing/LoanP1Panel2',
    'app/ux/GenericButton',
    'app/ux/GenericPrompt',
    'dojo/promise/all',
    'app/common/RSA',
    'dojo/dom-attr',
    'app/ux/GenericTooltip',
    'app/ux/GenericTextBox',
    'app/common/Product',
    'dojo/_base/config',
    'dojo/domReady!'
], function(dom, domStyle, domClass, domConstruct, on,
            query, Ajax, Global, Helper, Win, Data, when, SlideContainer,
            LoanContractFixPanel, LoanContractRulePanel, LoanContractPanel2,
            Button, Prompt, all, RSA, domAttr, Tooltip, TextBox, Product, cfg) {
    var protocolToggler = dom.byId('protocoltoggler'),
        addAmoutFld,
        loanPanel,
        contract,
        product,
        productDetails,
        ratioObj,
        prodId,
        interestRate,
        win,
        slideContainer,
        slideContainer1,
        loanContractFixPanel,
        loanContractRulePanel,
        nextBtn,
        loanContractPanel2,
        loginWin,
        key,
        unit = 0,
        carry = 0,
        urlParams = Global.getUrlParam(),
        contractNo = urlParams['contract'],
        homsCombineId = urlParams['combine'],
        homsFundAccount = urlParams['fundaccount'];

    function userDefault(contract) {
        addAmoutFld.set('value', 10000);
        loanPanel.set('amounts', [parseFloat((addAmoutFld.getAmount() / contract.loanRatio).toFixed(0))]);
        loanPanel.select(0);
        loanContractRulePanel.setData(addAmoutFld.getAmount(), contract.loanRatio);
    }
    
    function setAmountRule(type) {
    	if (type == 1) { // 日融
    		unit = cfg.proDayUnit;
    		carry = cfg.proDayCarry;
    	} else if (type == 2) {
    		unit = cfg.proMonthUnit;
    		carry = cfg.proMonthCarry;
    	}
    }

    function initView() {
        // product data
    	dom.byId('contract-panel').innerHTML = contractNo;
//        var promise = Ajax.post(Global.baseUrl + '/financing/add/data', {
//        	contract_no:contractNo
//        });
//        when(promise).then(function(response){
//            product = response.data.product;
//            productDetails = response.data.productdetail;
//            prodId = response.data.contract.prodId;
//            loanContractRulePanel.setValues({
//                product: product
//            });
//        });

        when(Data.getContract(contractNo), function(data) {
            contract = data;
            prodId = contract.prodId;
            
            var minPromise,enableLoanMin;
            when(Data.getProduct({product_id:prodId},true),function(p){
            	if (p.addToProd) { 
            		prodId = p.addToProd;
            		minPromise = all([Data.getProduct({product_id: p.addToProd}, true), Data.getProductDetails({product_id: p.addToProd}, true)]).then(function(result) {
                    result[0].productDetails = result[1];
                    var  product = result[0],
                    	details = result[1];
                     enableLoanMin = product.prodAmountMin;
                     
                     setAmountRule(product.prodTypeId);
                     
                     addAmoutFld.validates.push({
                         pattern: function() {
                             return parseFloat(this.getAmount()) >= enableLoanMin;
                         },
                         message: '可追加金额最小为' + Global.formatAmount(enableLoanMin) + '元'
                     });
                     loanContractRulePanel.set('product', product);
                     
                     return enableLoanMin;
                });
            		 all([maxPromise, minPromise]).then(function(result) {
                         addAmoutFld.set('placeHolder', '最少'+Global.formatAmount(result[1],0,'w')+'，最多'+Global.formatAmount(result[0],0,'w'));
                         addAmoutFld.set('precision', unit == 1 ? 0 : 2);
                         loanPanel.set('unit', unit);
                         loanPanel.set('carry', carry);
                         userDefault(contract);
                     });
            }else {
            	minPromise = when(Data.getProductDetails({product_id: contract.prodId}, true), function(d) {
                    p.productDetails = d;
                    var  product = p;
                     enableLoanMin = product.prodAmountMin;
                     setAmountRule(product.prodTypeId);
                     addAmoutFld.validates.push({
                         pattern: function() {
                             return parseFloat(this.getAmount()) >= enableLoanMin;
                         },
                         message: '可追加金额最小为' + Global.formatAmount(enableLoanMin) + '元'
                     });
                     loanContractRulePanel.set('product', product);
                     
                     return enableLoanMin;
                });
            	 all([maxPromise, minPromise]).then(function(result) {
                     addAmoutFld.set('placeHolder', '最少'+Global.formatAmount(result[1],0,'w')+'，最多'+Global.formatAmount(result[0],0,'w'));
                     addAmoutFld.set('precision', unit == 1 ? 0 : 2);
                     loanPanel.set('unit', unit);
                     loanPanel.set('carry', carry);
                     userDefault(contract);
                 });
            }
                  
            });
            
            var maxPromise = Ajax.get(Global.baseUrl + '/financing/add/maxamount', {
                contract_no: contractNo
            }).then(function(response) {
                if (response.success) {
                    addAmoutFld.validates.push({
                        pattern: function() {
                            return parseFloat(this.getAmount()) <= response.data;
                        },
                        message: '可追加金额最大为' + Global.formatAmount(response.data) + '元'
                    });
                    addAmoutFld.max=response.data;
                }
                return response.data;
            });
/*            var minPromise = all([Data.getProduct({product_id: prodId}), Data.getProductDetails({product_id: prodId})]).then(function(result) {
                result[0].productDetails = result[1];
                var product = result[0],
                    details = result[1],
                    enableLoanMin = product.prodAmountMin;
                addAmoutFld.validates.push({
                    pattern: function() {
                        return parseFloat(this.getAmount()) >= enableLoanMin;
                    },
                    message: '可追加金额最小为' + Global.formatAmount(enableLoanMin) + '元'
                });
                loanContractRulePanel.set('product', product);
                userDefault(contract);
                return enableLoanMin;
            });

            all([maxPromise, minPromise]).then(function(result) {
                addAmoutFld.set('placeholder', '最少'+Global.formatAmount(result[1],0,'w')+'，最多'+Global.formatAmount(result[0],0,'w'));
            });*/

        });

        function produceLoan(deposit, c) {

            loanPanel.setValues({
            	amounts: [deposit*c],
            	amount:deposit*c
            });

            var day = loanContractRulePanel.daySelect.value;
            for(var i in productDetails){
            	var productDetail = productDetails[i];
            	if(day >= productDetail.loanTermFrom && day < productDetail.loanTermTo && 
            			deposit >= productDetail.loanAmountFrom && deposit < productDetail.loanAmountTo ){
            		interestRate = productDetail.interestRate;
            		loanContractRulePanel.interestNode.innerHTML = productDetail.interestRate;
            	
            		//productDetail.interestRate;
            		break;
            	}
            }
        }
        
        // place deposit field
        addAmoutFld = new TextBox({
            style: 'margin: 20px 0px;width: 320px;font-size: 30px;',
            validates: [{
                pattern: /.+/,
                message: '请输入金额'
            }],
            limitRegex: /[\d\.]/,
            isAmount: true,
            isNumber: true
        }, 'deposit-amount-fld');

        on(addAmoutFld, 'keyup', function(e) {
            loanPanel.set('amounts', [parseFloat((addAmoutFld.getAmount() / contract.loanRatio).toFixed(0))]);
            loanContractRulePanel.setData(addAmoutFld.getAmount(), contract.loanRatio);
        });

        // place loan panel
        loanPanel = new LoanContractFixPanel({
            title: 'add',
            itemStyle: 'width:325px;'
        }, 'select-loan-panel');

 

        // show protocol
//        on(protocolToggler, 'click', function(e) {
//            if (!win) {
//                win = new Win({
//                    width: 900,
//                    height: 500,
//                    title: '追加融资盘协议'
//                });
//                win.placeAt(document.body);
//            }
//            win.show();
//            when(Data.getProtocolP1(), function(tmpl) {
//                win.set('msg', tmpl);
//            });
//            e.stopPropagation();
//        });

        // place right panel
        loanContractRulePanel = new LoanContractRulePanel({}, 'rightctn');

        function showPanel2() {
            query('.navi-container li.ui-step-active').removeClass('ui-step-active');
            domClass.add(query('.navi-container li')[1], 'ui-step-active');
            dom.byId('panel1').style.display = 'none';
            if (!loanContractPanel2) {
                loanContractPanel2 = new LoanContractPanel2({
                    bill: loanContractRulePanel.bill,
                    unit: unit,
                    carry: carry
                });
                loanContractPanel2.nextBtn.set('label', '确认追加');
                loanContractPanel2.placeAt('contentctn', 'first');
                on(loanContractPanel2.prevBtn, 'click', function() {
                    loanContractPanel2.hide();
                    query('.navi-container li.ui-step-active').removeClass('ui-step-active');
                    domClass.add(query('.navi-container li')[0], 'ui-step-active');
                    dom.byId('panel1').style.display = 'block';
                });

                on(loanContractPanel2.nextBtn, 'click', function() {
                    if (loanContractPanel2.isValid()) {
                        loanContractPanel2.nextBtn.loading(true);
                        Ajax.post(Global.baseUrl + '/financing/add/apply', {
                            contract_no:contractNo,
                            trade_pwd: RSA.encryptedString(key, loanContractPanel2.pwdFld.get('value')),
                            produce_id:prodId,
                            produce_term: loanContractRulePanel.term,
                            deposit_amount:Global.switchAmount(loanPanel.getAmount(), unit, carry),
                            add_amount: Global.switchAmount(addAmoutFld.getAmount(), unit, carry),
                            prev_store: loanContractPanel2.prev_store
                        }).then(function(response) {
                            loanContractPanel2.nextBtn.loading(false);
                            if (response.success) {
                                loanContractPanel2.hide();
                                query('.navi-container li.ui-step-active').removeClass('ui-step-active');
                                domClass.add(query('.navi-container li')[2], 'ui-step-active');
                                var successPanel = new Prompt({
                                    type: 'success',
                                    msg: '恭喜您，追加申请已成功！',
                                    subMsg: '<span style="color: #666666">追加申请正在紧张处理，请耐心等待。如有疑问请拨打客服电话 ' +
                                        '<b class="am-ft-orange"><br/>'+Global.hotline+'</b></span>',
                                        linkText: '查看申请记录',
                                        link:'financing/apply.htm'
                                });
                                successPanel.placeAt('contentctn', 'first');
                            } else {
                                Tooltip.show(response.msg, loanContractPanel2.nextBtn.innerNode, 'warning');
                            }
                        });
                    }
                });
            }
            loanContractPanel2.show();
            loanContractPanel2.set('bill', loanContractRulePanel.bill);
            loanContractPanel2.setValues({
                depositAmount: loanPanel.getAmount(),
                loanAmount: addAmoutFld.getAmount()
            });
            all([Data.getAccount()]).then(function(result) {
                var avalaibleAmount = result[0].avalaibleAmount - result[0].freezeAmount;
                loanContractPanel2.set('availableAmount', avalaibleAmount || 0);
            });

        }

        // first page button
        nextBtn = new Button({
            label: '下一步',
            //disabled: !dom.byId('checkbox1').checked,
            enter: true,
            onClick: function() {
            	// && loanPanel.isValid()
                if (addAmoutFld.checkValidity()) {
                    showPanel2();
                }

            }
        }, 'loaninfobtn');
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