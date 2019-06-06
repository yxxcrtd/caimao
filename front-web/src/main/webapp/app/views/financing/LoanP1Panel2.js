define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/views/ViewMixin',
    'app/common/Global',
    'dojo/query',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/dom-class',
    'dojo/dom-attr',
    'dojo/on',
    'app/ux/GenericButton',
    'app/ux/GenericTextBox',
    'app/ux/GenericComboBox',
    'dojo/store/Memory',
    'dojo/promise/all',
    'app/common/Data',
    'dojo/text!./templates/LoanP1Panel2.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, Global, query,
		dom, domConstruct, domClass, domAttr, on, Button, TextBox, ComboBox, Memory, all, Data, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        depositAmount: '',
        loanAmount: '',
        availableAmount: '',
        billAmount: 0,
        prev_store: 1,
        unit: 0,
        carry: 0,

        getUnit: function() {
            return this.unit == 1 ? 0 : undefined;
        },

        _setLoanAmountAttr: function(value) {
            var me = this;
            this.loanAmount = value;
            if (this.bill[0] == 0) {
                this.billAmount = 0;
            } else if (this.bill[0] == 1) {
                this.billAmount = value * this.bill[1];
            } else if (this.bill[0] == 2) {
                this.billAmount = this.bill[1];
            }
            all([Data.getUser(), Data.getGrades()]).then(function(results) {
                var user = results[0],
                    grades = results[1];
                var discount = 1;
                if (results[1]) {
                	var discountObj = Global.findObj(results[1], 'userGrade', user.userGrade);
                    if (discountObj) {
                        discount = discountObj.discountRate;
                    }
                }
                
                var totalBillEl = dom.byId('totalbill');
                if (totalBillEl) {
//                if (this.daySelect) {
//                    totalBillEl.innerHTML = Global.formatAmount(this.billAmount * this.daySelect.value);
//                } else {
//                    totalBillEl.innerHTML = Global.formatAmount(this.billAmount);
//                }
                    totalBillEl.innerHTML = Global.formatAmount(me.billAmount * me.bill[3] * discount);
                    if (discount == 1) {
                        dom.byId('totalbilldesc').style.display = 'none';
                    } else {
                        dom.byId('totalbilldesc').style.display = 'inline-block';
                        domAttr.set(dom.byId('totalbilldesc'), {
                            'data-help': '您享有' + (discount * 100).toFixed(0) + '%的费用折扣'
                        });
                    }
                }
            });

        },

        _setDepositAmountAttr: function(value) {
            this.depositAmount = value;
            this.depositAmountNode.innerHTML = Global.formatAmount(value, this.getUnit(), '', this.carry);
        },

        _setAvailableAmountAttr: function(value) {
        	var me = this;
            this._set('availableAmount', value);
            
            all([Data.getUser(), Data.getGrades()]).then(function(results) {
                var user = results[0],
                    grades = results[1];
                var discount = 1;
                if (results[1]) {
                	var discountObj = Global.findObj(results[1], 'userGrade', user.userGrade);
                    if (discountObj) {
                        discount = discountObj.discountRate;
                    }
                }
                
                me.availableAmountNode.innerHTML = Global.formatAmount(value);
                var needAmount = me.depositAmount + me.billAmount * me.bill[3] * discount* me.prev_store - value;
                me.needAmountNode.innerHTML = Global.formatAmount(needAmount);
                var needRecharge = needAmount > 0;
                me.needRechargeCtn.style.display = needRecharge ? 'block': 'none';
                if (needRecharge) {
                	me.nextBtn.addDisabledMsg([3, '余额不足请充值']);
                } else {
                	me.nextBtn.removeDisabledMsg(3);
                }
            });
        },

        _setBillAttr: function(value) {
            this._set('bill', value);
        },

        render: function() {
            var me = this;
            me.prevBtn = new Button({
                label: '上一步',
                height: 36,
                color: '#E2E2E2',
                hoverColor: '#EDEDED',
                textStyle: {
                    color: '#666666'
                },
                innerStyle: {
                    borderColor: '#C9C9C9'
                },
                handler: me.prev
            }, me.prevBtnNode);
            me.nextBtn = new Button({
                label: '确认支付',
                height: 36,
                style: {
                    marginLeft: '0px'
                },
                enter: true,
                handler: me.next
            }, me.nextBtnNode);

            me.pwdFld = new TextBox({
                label: '安全密码',
                validates: [{
                    pattern: /.+/,
                    message: '请输入安全密码'
                }],
                style: 'margin: 20px auto 0px;',
                type: 'password'
            }, me.pwdNode);

            if (me.params.bill) {
                me.bill = me.params.bill;
                if (me.bill[0] == 0) {
                    var html = '<span class="am-right-5">免费</span>';
                    domConstruct.place(domConstruct.toDom(html), me.contentNode);
                } else if (me.bill[2] == 0 || me.bill[2] == 1) {
//                    var html = '<span class="am-right-5">预存</span>';
//                    domConstruct.place(domConstruct.toDom(html), me.contentNode);
//                    var store = new Memory({
//                        data: [
//                            {name: '1天', id: 1},
//                            {name: '2天', id: 2},
//                            {name: '3天', id: 3},
//                            {name: '4天', id: 4},
//                            {name: '5天', id: 5}
//                        ]
//                    });
//                    me.daySelect = new ComboBox({
//                        store: store,
//                        searchAttr: 'id',
//                        labelAttr: 'name',
//                        editable: false,
//                        item: {name: '5天', id: 5},
//                        style: 'width: 50px; display: inline-block;margin-left: 0px;',
//                        onChange: function(value) {
//                            dom.byId(totalbill).innerHTML = Global.formatAmount(value * me.billAmount);
//                            me.set('prev_store', value);
//                            me.set('availableAmount', me.availableAmount);
//                        }
//                    });
//                    me.set('prev_store', 5);
//                    me.daySelect.placeAt(me.contentNode);
                    var html = '<span class="am-left-10">首付费用 <span class="am-numberOrange" id="totalbill"></span>元</span><i class="fa help" id="totalbilldesc" data-help="">&#xf059;</i>';
                    domConstruct.place(domConstruct.toDom(html), me.contentNode);
                } else if(me.bill[2] == '2') {
                    var html = '<span class="am-right-5">首付费用 <span class="am-numberOrange" id="totalbill"></span>元</span><i class="fa help" id="totalbilldesc" data-help="">&#xf059;</i>';
                    domConstruct.place(domConstruct.toDom(html), me.contentNode);
                }

            }

        },

        isValid: function() {
            return this.pwdFld.checkValidity();
        },
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});