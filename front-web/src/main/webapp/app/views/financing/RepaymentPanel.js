define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dojo/dom-style',
    'dojo/on',
    'dojo/mouse',
    'dijit/DialogUnderlay',
    'app/common/Global',
    'dojo/query',
    'dijit/focus',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/string',
    "dojo/dom-class",
    'app/common/Position',
    'dojo/dnd/Moveable',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'app/views/ViewMixin',
    'app/common/Ajax',
    'app/common/RSA',
    'app/ux/GenericTooltip',
    'dojo/_base/lang',
    'dojo/cookie',
    'app/ux/GenericMiniMsgBox',
    'app/views/common/LoginTopbar',
    'dojo/when',
    'app/common/Data',
    'dojo/text!./templates/RepaymentPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, domStyle, on, mouse, 
		DialogUnderlay, Global, query, focusUtil, dom, domConstruct, string,
        domClass, Position, Moveable, TextBox, Button, ViewMixin, Ajax, RSA,
        Tooltip, lang, cookie, MiniMsgBox, LoginTopbar, when, Data, template){
    return declare([_WidgetBase, _TemplatedMixin, ViewMixin], {
    	
    	templateString: template,
    	
    	width: '',
        height: '',

    	contentClass: '',
    	title: '确认还款',
    	//合约ID
    	contractId:'',
    	//借款金额
    	loanAmount: Global.EMPTY,
    	//可用余额
    	availableBalance: Global.EMPTY,
    	//罚息
//    	punitive: Global.EMPTY,
    	//合计
    	totalAmount: Global.EMPTY,

    	closeAction: 'hide',
    	confirmText: '',
    	confirmAndCancel: false,
    	
    	_setWidthAttr: function(value) {
            domStyle.set(this.domNode, {
                width: value + 'px'
            });
        },

        _setHeightAttr: function(value) {
            domStyle.set(this.contentNode, {
                height: value + 'px',
                'overflowY': 'scroll'
            });
        },
    		
    	_setContentClassAttr: function(value){
    		this._set('contentClass', value);
    		domClass.add(this.contentNode, value);		
    	},
    	
    	_setTitleAttr: function(value) {
    		this._set('title', value);
    		this.titleNode.innerHTML = value;
    	},
    	
    	//合约ID
    	_setContractIdAttr: function(value){
    		this._set('contractId', value);
    		this.contractIdNode.innerHTML = value;
    	},
    	
    	//借款金额
    	_setLoanAmountAttr: function(value){
    		this._set('loanAmount', value);
    		this.loanAmountNode.innerHTML = Global.formatAmount(value);
    	},
    	
    	//可用余额
    	_setAvailableBalanceAttr: function(value){
    		this._set('availableBalance', value);
    		this.availableBalanceNode.innerHTML = Global.formatAmount(value);
    	},
    	//罚息
//    	_setPunitiveAttr: function(value){
//    		this._set('punitive', value);
//    		this.punitiveNode.innerHTML = Global.formatAmount(value);
//    	},
    	//合计
    	_setTotalAmountAttr: function(value){
    		this._set('totalAmount', value);
    		this.totalAmountNode.innerHTML = Global.formatAmount(value);
    	},
    	show: function() {
    		var me = this;
    		me.enterBtns = query('.dijitButton.enterbutton').filter(function(i) {
    			return i != me.confirmBtn.domNode;
    		});
    		me.enterBtns.removeClass('enterbutton');
    		domStyle.set(me.domNode, {
    			display: 'block'
    		});
    		Position.screenCenter(me.domNode);
    		DialogUnderlay.show({}, 9999);
    		if (document.activeElement) {
    			document.activeElement.blur();
    		}
    		me.domNode.focus();
    	},
    	
    	close: function() {
    		var me = this;
    		if (me.enterBtns) {
    			me.enterBtns.addClass('enterbutton');
    		}
    		domConstruct.destroy(me.domNode);
    		DialogUnderlay.hide();
            Tooltip.hide();
    	},
    	
    	hide: function() {
    		var me = this;
    		if (me.enterBtns) {
    			me.enterBtns.addClass('enterbutton');
    		}
    		domStyle.set(me.domNode, {
    			display: 'none'
    		});
    		DialogUnderlay.hide();
            Tooltip.hide();
    	},
    	
    	addListeners: function() {
    		var me = this;
    		on(me.closeNode, 'click', function() {
    			me[me.get('closeAction')]();
    		});
    	},

        render: function() {
            var me = this;
            on(me.closeNode, 'click', function() {
                me[me.get('closeAction')]();
            });
    		me.tradePwdFld = new TextBox({
                label: '安全密码',
    			validates: [{
					pattern: /.+/,
					message: '请输入安全密码 '
				}],
				style: {
					marginLeft: '140px'
				},
				type: 'password'
    		}, me.tradePwdNode);
            me.confirmBtn = new Button({
                label: '确认还款',
                enter: true,
                width: 200,
                position: 'center'
            }, me.btnNode);
            on(me.confirmBtn, 'click', function() {
                if (me.isValid()) {
                    this.loading(true);
                    var pwd = RSA.encryptedString(me.key, me.tradePwdFld.get('value'));
                    Ajax.post(Global.baseUrl + '/financing/repay', {
                        'contract_no': me.contractId,
                        'repay_amount': me.get('loanAmount'),
                        'trade_pwd': pwd
                    }).then(function (response) {
                    	me.confirmBtn.loading(false);
                        if (response.success) {
                            me.formNode.style.display = 'none';
                            me.promptNode.style.display = 'block';
                            me.linkNode.href = Global.baseUrl + "/home/index.htm";
                        } else {
                            Tooltip.show(response.msg, me.confirmBtn.innerNode, 'warning');
                        }
                    });
                }
            });
        },

        isValid: function() {
            return this.tradePwdFld.checkValidity();
        },
    	
    	postCreate: function(){
    		var me = this;
            (function () {
                Ajax.get(Global.baseUrl + '/sec/rsa', {
                }).then(function (response) {
                    if (response.success) {
                        me.modulus = response.data.modulus;
                        me.exponent = response.data.exponent;
                        me.key = RSA.getKeyPair(me.exponent, '', me.modulus);

                    } else {
                        //TODO
                    }

                });
            })();
    		
    		domStyle.set(me.domNode, {
    			position: 'absolute',
    			'zIndex': '10000',
    			'padding': '4px'
    		});
    		
    		new Moveable(me.domNode, {
    			handle: me.titleCtnNode
    		});

            me.render();
    		
    		me.inherited(arguments);
    	}
    });
});