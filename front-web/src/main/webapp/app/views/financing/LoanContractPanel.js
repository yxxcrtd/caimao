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
    'dojo/text!./templates/LoanContractPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, Global, query,
		dom, domConstruct, domClass, domAttr, on, Button, TextBox, ComboBox, Memory, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        depositAmount: '',
        loanAmount: '',
        availableAmount: '',
        interestAmount: '',
        prev_day: 5,
   

        _setDepositAmountAttr: function(value) {
            this.depositAmount = value;
            this.depositAmountNode.innerHTML = Global.formatAmount(value);
        },

        _setAvailableAmountAttr: function(value) {
            this.availableAmountNode.innerHTML = Global.formatAmount(value);
            var needAmount = this.depositAmount - value;
            this.needAmountNode.innerHTML = Global.formatAmount(needAmount);
            var needRecharge = needAmount > 0;
            this.needRechargeCtn.style.display = needRecharge ? 'block': 'none';
        },

        _setInterestAmountAttr: function(value) {
            this.interestAmount = value;
            this.rateNode.innerHTML = Global.formatAmount(parseFloat(this.daySelect.get('value')) * value);
        },
        _setTestAttr:function(value){
        	alert(value);
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
                label: '确认融资',
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
                style: 'margin: 20px 0px 0px 400px;',
                type: 'password'
            }, me.pwdNode);
            var store = new Memory({
                data: [
                    {name: '1天', id: 1},
                    {name: '2天', id: 2},
                    {name: '3天', id: 3},
                    {name: '4天', id: 4},
                    {name: '5天', id: 5}
                ]
            });
            me.daySelect = new ComboBox({
                store: store,
                searchAttr: 'id',
                labelAttr: 'name',
                value: '5天',
                style: 'width: 50px; display: inline-block;margin-left: 0px;',
                onChange: function(value) {
                    me.rateNode.innerHTML = Global.formatAmount(value * me.interestAmount);
                    me.set('prev_day', value);
                }
            }, me.daySelectNode);
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