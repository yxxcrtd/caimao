define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'dojo/dom-construct',
    'dojo/query',
    'app/common/Global',
    'dojo/on',
    'dojo/dom',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/_base/config',
    'dojo/text!./templates/UserStatusPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, domConstruct, 
		query, Global, on, dom, domClass, domStyle, Config, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	baseUrl: Global.baseUrl,
    	templateString: template,
    	
    	isTrust: '',
    	isTradePwd: '',
    	isBankCard: '',
    	
    	_setIsTrustAttr: function(value) {
    		this._set('isTrust', value);
    		domClass[value ? 'add' : 'remove'](this.isTrustNode, 'ui-step-tips-active');
    		value ? this.isTrustTextNode.innerHTML = '实名认证' :
    			domConstruct.create('a', {innerHTML: '实名认证', href: Global.baseUrl +'/user/'+(Config.authentication ? 'uploadID.htm' : 'certification.htm')+'', target: '_blank'}, this.isTrustTextNode);
    	},
    	
    	_setIsTradePwdAttr: function(value) {
    		this._set('isTradePwd', value);
    		domClass[value ? 'add' : 'remove'](this.isTradePwdNode, 'ui-step-tips-active');
    		value ? domConstruct.create('a', {innerHTML: '修改安全密码', href: Global.baseUrl +'/user/changetradepwd.htm', target: '_blank'}, this.isTradePwdTextNode) :
    			domConstruct.create('a', {innerHTML: '设置安全密码', href: Global.baseUrl +'/account/tradepwd/set.htm', target: '_blank'}, this.isTradePwdTextNode);
    	},
    	
    	_setIsBankCardAttr: function(value) {
    		var me = this;
    		this._set('isBankCard', value);
    		domClass[value ? 'add' : 'remove'](this.isBankCardNode, 'ui-step-tips-active');
    		value ? domConstruct.create('a', {innerHTML: '更换银行卡', href: Global.baseUrl +'/account/bankcard/bind.htm?edit=1', target: '_blank'}, this.isBankCardTextNode) :
    			domConstruct.create('a', {innerHTML: '绑定银行卡', href: Global.baseUrl +'/account/bankcard/bind.htm', target: '_blank'}, this.isBankCardTextNode);
    	},
    	
    	_setTradePwdStrengthAttr: function(value) {
    		var strengthMap = {'': '差', '1': '低', '2': '中', '3': '高'};
    		this.showTradePwdNode.innerHTML = strengthMap[value];
    		domClass.add(this.showTradePwdNode, 'safety-color-' + value);
    	},
    	
    	_setBankNoAttr: function(value) {
    		if (value) {
    			domStyle.set(this.showBankCardNode, {
        			'backgroundImage': 'url('+Config.baseUrl+'app/resources/image/bosheng/borkers-logo/vertical-bank-'+value+'.png)',
        			'backgroundSize': '85%',
        			'backgroundPosition': '50% 15%',
        			'backgroundRepeat': 'no-repeat'
        		});
    		}
    	},
    	
    	_setUsernameRealAttr: function(value) {
    		this.showTrustNode.innerHTML = value;
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	addLinsters: function() {
    		var me = this;
    		on(me.statusCtnNode, '.iconfont:mouseover', function() {
    			if (domClass.contains(this.parentNode.parentNode, 'ui-step-tips-active')) {
    				query('.iconfontinner', this).addClass('iconfontinnerhover');
    			}
    		});
    		on(me.statusCtnNode, '.iconfont:mouseout', function() {
    			query('.iconfontinner', this).removeClass('iconfontinnerhover');
    		});
    	},
    	
    	postCreate: function(){
    		var me = this;
    		me.addLinsters();
    	    this.inherited(arguments);
    	}
    });
});