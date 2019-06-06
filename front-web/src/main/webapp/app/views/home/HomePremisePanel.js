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
    'dojo/_base/config',
    'dojo/dom-class',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/HomePremisePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, query, dom, domConstruct, on, cfg,domClass, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	bankCardFlag: '',
    	isTrust: '',
    	tradepwdFlag: '',
    	realNameTpl: '<p class="am-mt-5"><span class="am-ft-sm am-ft-gray">真实有效的身份证即可实名认证成功</span></p>',
    	bankcardTpl: '<p class="am-mt-5"><span class="am-ft-sm am-ft-gray">绑定一张有效的银行卡，方便资金操作</span></p>',
    	tradePwdTpl: '<p class="am-mt-5"><span class="am-ft-sm am-ft-gray">安全密码是平台上进行核心操作时，唯一身份认证。</span></p>',
    	
    	_setBankCardFlagAttr: function(value) {
    		this._set('bankCardFlag', value);
    		domClass[(parseInt(value) ? 'add' : 'remove')](this.bankcardNode, 'ui-step-tips-active');
    		if (parseInt(value)) {
    			this.bankcardLinkNode.innerHTML = '绑定银行卡';
    		} else {
    			domConstruct.place(domConstruct.toDom(this.bankcardTpl), this.bankcardLinkNode, 'after');
    			this.bankcardLinkNode.innerHTML = '<a href="'+Global.baseUrl+'/account/bankcard/bind.htm">绑定银行卡</a>';
    		}
    	},
    	
    	_setIsTrustAttr: function(value) {
    		this._set('isTrust', value);
    		domClass[(parseInt(value) ? 'add' : 'remove')](this.realNameNode, 'ui-step-tips-active');
    		if (parseInt(value)) {
    			this.realNameLinkNode.innerHTML = '实名认证';
    		} else {
    			domConstruct.place(domConstruct.toDom(this.realNameTpl), this.realNameLinkNode, 'after');
    			this.realNameLinkNode.innerHTML = '<a href="'+Global.baseUrl+'/user/'+(cfg.authentication ? 'uploadID.htm' : 'certification.htm')+'">实名认证</a>';
    		}
    	},
    	
    	_setTradepwdFlagAttr: function(value) {
    		this._set('tradePwdFlag', value); // watch event
    		domClass[(parseInt(value) ? 'add' : 'remove')](this.tradePwdNode, 'ui-step-tips-active');
    		if (parseInt(value)) {
    			this.tradePwdLinkNode.innerHTML = '安全密码';
    		} else {
    			domConstruct.place(domConstruct.toDom(this.tradePwdTpl), this.tradePwdLinkNode, 'after');
    			this.tradePwdLinkNode.innerHTML = '<a href="'+Global.baseUrl+'/account/tradepwd/set.htm">安全密码</a>';
    		}
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	baseUrl: Global.baseUrl,
    	
    	postCreate: function() {
    		var me = this;
    		me.inherited(arguments);
    	}
    	
    });
});