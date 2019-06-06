define([ 
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/mouse',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/dom-construct',
    'dojo/query',
    'dojo/fx/Toggler',
    'dojo/_base/fx', 
	'dojo/fx',
	'app/ux/GenericTooltip',
	'dojo/window',
	'app/common/Fx',
    'dojo/_base/config',
    'dojo/text!./templates/BankcardSelectionField.html'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, on, mouse, 
		domClass, domStyle, domConstruct, query, Toggler, fx, coreFx, Tooltip, win, Fx, Config, template) {
	return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
		templateString: template,
		
		// config params
		label: '',
		inputCls: 'ui-input-extra',
		inputWidth: 194,
		separator: '：',
		banks: '',
		bankIndex: null,
		
		// invoke when call set attribute
		_setBanksAttr: function(value) {
			this._set('banks', value);
			var len = value.length,
				i = 0;
			var fastHtml = '<i class="icon-contract icon-contractKuai" style="position: absolute;top: 2px;right: 2px; color: firebrick;"></i>';
			for (; i <len; i++) {
				var html = '<li class="list-chooseBank-item" style="position: relative;">' +
					'<input type="radio" name="bank_code" style="display:none">' +
					'<a class="ui-logo-small">' + ((value[i]['bank_is_quick_pay'] == '1') ? fastHtml : '') +
					'<img src="' + Config.baseUrl + 'app/resources/image/bosheng/borkers-logo/bank-'+value[i]['bank_no']+'.jpg" width="110" height="25"></a></li>';
				var bankItemEl = domConstruct.toDom(html);
				domConstruct.place(bankItemEl, this.bankListNode);
			}
		},
		
		collapseBankList: function() {
			this.bankListToggler.hide();
			domStyle.set(this.bankListToggleNode, {
				transform: 'rotate(180deg)',
				transition: 'all 0.2s'
			});
			this.bankListToggleNode.collapsed = true;
		},
		
		expandBankList: function() {
			this.bankListToggler.show();
			domStyle.set(this.bankListToggleNode, {
				transform: 'rotate(0deg)',
				transition: 'all 0.2s'
			});
			this.bankListToggleNode.collapsed = false;
		},
		
		// bind event handlers
		addListeners: function() {
			var me = this;
			on(me.bankListNode, '.list-chooseBank-item:click', function(e) {
				var selectEl = query('.active', me.bankListNode)[0],
					newSelectEl = query('.ui-logo-small', this)[0];
				if (selectEl) {
					domClass.remove(selectEl, 'active');
				}
				domClass.add(newSelectEl, 'active');
				var target = query('span:first-child', me.bankcardNode)[0];
				target.innerHTML = ''; 
				domConstruct.empty(target);
				domClass.add(target, 'ui-logo-small');
				domConstruct.create('img', {src: query('img', this)[0].src, width: '110', height: '25'}, target, 'first');
				me.set('bankIndex', query('.list-chooseBank-item', me.bankListNode).indexOf(this));
				domClass.remove(me.bankcardNode, 'dijitTextBoxError');
				Tooltip.hide(me.bankcardNode);
			});
			
			// expand or collapse bankcard list
			on(me.bankListToggleNode, 'click', function(event) {
				!this.collapsed ? me.collapseBankList() : me.expandBankList();
			});
			
			on(me.bankcardNode, 'mouseenter', function() {
				if (domClass.contains(this, 'dijitTextBoxError')) {
					Tooltip.show('请选择绑定银行', this);
				}
			});
		},
		
		checkValidity: function() {
			var me = this;
			if (me.bankIndex === null) {
				Fx.scrollIntoView(me.bankcardNode);
				domClass.add(me.bankcardNode, 'dijitTextBoxError');
				setTimeout(function() {
					Tooltip.show('请选择绑定银行', me.bankcardNode);
				}, 100);
				return false;
			}
			return true;
		},
		
		postCreate: function() {
    		var me = this;
    		me.bankListToggler = new Toggler({
    		    node: me.bankListBoxNode,
    		    showFunc: coreFx.wipeIn,
    		    hideFunc: coreFx.wipeOut
    		}),
    		me.addListeners();
    	    this.inherited(arguments);
    	}
	});
});