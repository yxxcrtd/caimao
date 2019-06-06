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
    'dojo/dom-class',
    'dojo/dom-construct',
    'app/ux/GenericPhoneCodeBox',
    'app/ux/GenericButton',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/PhoneCertificationBox.html'
], function(declare, _WidgetBase, _TemplatedMixin, domStyle, on, mouse, DialogUnderlay, Global, query, focusUtil, domClass, domConstruct, PhoneCodeBox, Button, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin], {
    	phone: '',
        width: '',
        height: '',
    	
    	onConfirm: function() {},
    	fetchCode: function() {},
    	
    	templateString: template,

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
    	
    	_setPhoneAttr: function(value) {
    		this._set('phone', value);
    		this.phoneNode.innerHTML = value;
    	},
    	
    	getCode: function() {
    		return this.phoneCodeFld.get('value');
    	},
    	
    	showUnderlay: function() {
    		DialogUnderlay.show({}, 9999);
    	},
    	
    	showError: function(message) {
			Tooltip.show(message, this.confirmBtn.domNode);
		},
    	
    	countdown: function(time) {
    		var me = this,
    			target = query('i', me.countNode)[0];
    		Global.loop(time, function(t) {
    			target.innerHTML = '（' + t +'）';
    		}, function() {
    			target.innerHTML = '';
    			me.setDisabled(me.countNode, false);
    		}, me);
    	},
    	
    	setDisabled: function(el, disabled) {
    		el.disabled = disabled;
    	},
    	
    	close: function() {
    		var me = this;
    		me.hide();
			if(me.params.afterClose) {
				me.params.afterClose();
			}
    	},
    	
    	show: function() {
    		var me = this;
    		me.phoneCodeFld.reset();
    		domStyle.set(me.domNode, {
    			display: 'block'
    		});
    		me.showUnderlay();
    	},
    	
    	hide: function() {
    		var me = this;
    		Tooltip.hide();
    		domStyle.set(me.domNode, {
    			display: 'none'
    		});
    		DialogUnderlay.hide();
    	},
    	
    	removeError: function(el) {
    		domClass.remove(el, 'ui-input-error');
    		var errorEl = query('.ui-form-explain', el.parentNode);
    		if (errorEl && errorEl.length > 0) {
    			domConstruct.destroy(errorEl[0]);
    		}
    	},
    	
    	addError: function(el, msg) {
    		if (msg) {
    			domClass.add(el, 'ui-input-error');
    			var errorEl = domConstruct.toDom('<div class="ui-form-explain" style="left: 380px;"><i class="ui-icon icon-small-error"></i><span class="font-color-red">'+msg+'</span></div>');
    			domConstruct.place(errorEl, el.parentNode);
    		} else {
    			var errorEl = domConstruct.toDom('<div class="ui-form-explain" style="left: 380px;><i class="ui-icon icon-small-success"></i></div>');
    			domConstruct.place(errorEl, el.parentNode);
    		}
    	},
    	
    	render: function() {
    		var me = this;
    		me.phoneCodeFld = new PhoneCodeBox({
    			label: '短信验证码',
    			style: {
    				marginLeft: '160px'
    			},
    			inputWidth: 100
    		});
    		me.confirmBtn = new Button({
    			label: '确定',
    			style: {
    				marginLeft: '160px'
    			},
    			enter: true,
    			handler: me.onConfirm
    		});
    		me.phoneCodeFld.placeAt(me.formNode);
    		me.confirmBtn.placeAt(me.formNode);
    	},
    	
    	isValid: function() {
    		return this.phoneCodeFld.checkValidity();
    	},
    	
    	postCreate: function(){
    		var me = this;
    		
    		me.render();
    		
    		domStyle.set(me.domNode, {
    			position: 'absolute',
    			top: '50%',
    			left: '50%',
    			'marginLeft': '-260px',
    			'marginTop': '-145px',
    			'backgroundColor': '#fff',
    			'zIndex': '10000'
    		});
    		
    		// close button
    		on(me.closeNode, 'click', function() {
    			me.close();
    		});
    		
    		on(me.closeNode, mouse.enter, function() {
    			domStyle.set(this, {
    				transform: 'rotate(360deg)',
    				transition: 'all 0.5s'
    			});
    		});
    		on(me.closeNode, mouse.leave, function() {
    			domStyle.set(this, {
    				transform: 'rotate(0deg)'
    			});
    		});
    		
    		on(me.editPhoneNode, 'click', function() {
    			me.close();
    			if (me.params.changePhone) {
    				me.params.changePhone();
    			}
    		});

            on(me.phoneCodeFld.sendBtn, 'click', function() {
                me.fetchCode();
            });
    		
    		me.inherited(arguments);
    	}
    });
});