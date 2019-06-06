define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/views/ViewMixin',
    'app/common/Global',
    'dojo/query',
    'app/ux/GenericDisplayBox',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'app/ux/GenericComboBox',
    'dojo/store/Memory',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'app/ux/GenericTooltip',
    'app/common/Date',
    'app/views/financing/LoanProtocolPanel',
    'app/ux/GenericWindow',
    'app/common/Data',
    'dojo/when',
    'dojo/text!./templates/LoanExtendPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, Global, query,
		DisplayBox, TextBox, Button, ComboBox, Memory, dom, domConstruct, on, Tooltip,
        DateUtil, LoanProtocolPanel, Win, Data, when, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
        amount: '',//账户金额
        monthRate:'',
        loanAmount:'',
        interest:'',
        contractNo:'',
    	prev: function() {},
    	next: function() {},
		
    	_setAmountAttr: function(value) {
    		this._set('amount', value);
    	},
    	
    	_setContractNoAttr: function(value) {
    		this._set('contractNo', value);
    	},
    	
    	_setInterestAttr: function(value) {
    		this._set('interest', Global.formatAmount(value));
    	}, 
    	
    	_setMonthRateAttr: function(value) {
    		this._set('monthRate', value);
    		//this.monthRateNode.innerHTML = value+'%';
    	}, 
    	_setLoanAmountAttr: function(value) {
    		this._set('loanAmount', Global.formatAmount(value));
    	}, 
    	
		setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	render: function() {
    		var me = this;
    		var store = new Memory({
            });
    	     me.daySelect = new ComboBox({
                 store: store,
                 searchAttr: 'id',
                 labelAttr: 'name',
                 editable: false,
                 item: {name: '1个月', id: 30},
                 style: 'width: 70px; display: inline-block;margin-left: 0px;'
             }, me.daySelectNode);
    		me.nextBtn = new Button({
    			label: '下一步',
                style: 'margin-top: 15px;',
                //disabled: !me.checkboxNode.checked,
    			enter:true
    		}, me.nextBtnNode);
    	},
    	
    	getData: function() {
    		return{
    		  contract_no: this.contractNo,
    		  produce_id:  "3",
    	      produce_term:this.daySelect.value,
    	      amount:this.amount,
    	      interest:this.interest
    		};
    	},
    	
    	showError: function(message) {
    		Tooltip.show(message, this.confirmBtn.domNode);
    	},
    	
    	getValues: function() {
  		  return {
  	    	  
    	  };
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});