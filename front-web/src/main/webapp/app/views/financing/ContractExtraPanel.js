define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'app/common/Date',
    'dojo/string',
    'app/common/Fx',
    'dojo/dom-class',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/ContractExtraPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, query, dom, domConstruct, on, DateUtil, string, Fx, domClass, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	baseUrl: Global.baseUrl,
    	
    	templateString: template,
    	
    	order_amount: '',
    	apply_datetime: '',
    	order_status: '',
		order_status_label: '',
		new_contract_no: '',
		
		_setOrder_amountAttr: function(value) {
    		this._set('order_amount', value);
    		this.addAmountNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	_setApply_datetimeAttr: function(value) {
    		this._set('apply_datetime', value);
    		this.beginDateNode.innerHTML = value.slice(0,10);
    	},
    	
    	_setOrder_status_labelAttr: function(value) {
    		this._set('order_status_label', value);
    		this.statusNode.innerHTML = value;
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    		
    		this.afterSet();
    	},
    	
    	afterSet: function() {
    		var status = this.get('order_status');
    		if (status == '1') { // valid
    			var statusIconEl = domConstruct.toDom('<i class="icon-contract icon-contractXin"></i>');
    			var newContractNo = this.get('new_contract_no');
    			domConstruct.place(statusIconEl, this.statusNode);
    			on(statusIconEl, 'mouseenter', function() {
    				Tooltip.show('已生成新合约：' + newContractNo, statusIconEl);
    			});
    			on(statusIconEl, 'mouseleave', function() {
    				Tooltip.hide();
    			});
    		}
    	},
    	
    	addListeners: function() {
    		var me = this;
    		on(me.slideNode, 'click', function() {
    			var hidden = (me.contentNode.style.display === 'none');
    			var newIcon = hidden ? '&#xf068;' : '&#xf067;';
    			Fx[hidden ? 'expand': 'collapse'](me.contentNode).play();
    			me.slideIconNode.innerHTML = newIcon;
    		});
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.addListeners();
    		me.inherited(arguments);
    	}
    	
    });
});