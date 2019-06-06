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
    'dojo/string',
    'app/ux/GenericProgressBar',
    'dojo/dom-style',
    'dojo/dom-class',
    'dojo/text!./templates/SafetyInformation.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		domConstruct, query, Global, on, dom, string, ProgressBar, domStyle, domClass, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	baseUrl: Global.baseUrl,
    	templateString: template,
    	userPwdStrength: '',
    	tradePwdFlag: '',
    	userTradePwdStrength: '',
    	mobile: '',
    	email: '',
    	tradePwdQuestion: '',
    	
    	_setMobileAttr: function(value) {
    		this._set('mobile', value);
    		if (value) {
    			domClass.add(this.mobileIconNode, 'icon-middle-success');
    			this.mobileNode.innerHTML = Global.encodeInfo(value, 3, 3);
    			this.mobileLinkNode.innerHTML = '修改';
    			this.mobileLinkNode.href = this.baseUrl + '/user/changemobile.htm';
    		} else {
    			// it seems not run forever
    			domClass.add(this.mobileIconNode, 'icon-middle-error');
    		}
    	},
    	
    	_setEmailAttr: function(value) {
    		this._set('email', value);
    		if (value) {
    			domClass.add(this.emailIconNode, 'icon-middle-success');
    			this.emailNode.innerHTML = value;
    			this.emailLinkNode.innerHTML = '修改';
    			this.emailLinkNode.href = this.baseUrl + '/user/changemail.htm';
    		} else {
    			domClass.add(this.emailIconNode, 'icon-middle-error');
    			this.emailNode.innerHTML = '未绑定';
    			domClass.add(this.emailNode, 'am-ft-orange');
    			this.emailLinkNode.innerHTML = '绑定';
    			this.emailLinkNode.href = this.baseUrl + '/user/changemail.htm';
    		}
    	},
    	
    	_setUserPwdStrengthAttr: function(value) {
    		this._set('userPwdStrength', value);
    		this.pwdProgressBar.set('value', parseInt(value) / 3);
    	},
    	
    	_setUserTradePwdStrengthAttr: function(value) {
    		this._set('userTradePwdStrength', value);
    		if (this.tradePwdStrongNode) {
    			this.tradePwdProgressBar = new ProgressBar({
        			label: '当前安全度：',
        			value: parseInt(value) / 3
        		});
    			this.tradePwdProgressBar.placeAt(this.tradePwdStrongNode);
    		}
    		if (value) {
    			domClass.add(this.tradePwdIconNode, 'icon-middle-success');
    			this.tradePwdLinkNode.innerHTML = '修改';
    			domStyle.set(this.tradePwdLinkNode, {
    				'display': 'block',
    				'line-height': '24px'
    			});
    			this.tradePwdLinkForgetNode.innerHTML = '忘记了,找回密码';
    			this.tradePwdLinkNode.href = this.baseUrl + '/user/changetradepwd.htm';
    			this.tradePwdLinkForgetNode.href = this.baseUrl + '/account/tradepwd/findtradepwd.htm';
    		} else {
    			domClass.add(this.tradePwdIconNode, 'icon-middle-error');
    			domConstruct.place(domConstruct.toDom('<p><span class="am-ft-orange am-ft-md">未设置</span></p>'), this.tradePwdStrongNode, 'after');
    			domConstruct.destroy(this.tradePwdStrongNode);
    			this.tradePwdLinkNode.innerHTML = '设置';
    			this.tradePwdLinkNode.href = this.baseUrl + '/account/tradepwd/set.htm';
    		}
    	},
    	
    	_setTradePwdQuestionAttr: function(value) {
    		this._set('tradePwdQuestion', value);
    		if (value) {
    			domClass.add(this.pwdQuestionIconNode, 'icon-middle-success');
    			this.tradePwdQuestionNode.innerHTML = '已设置';
    			this.tradePwdQuestionLinkForgetNode.innerHTML = '忘记了,找回密保';
    			this.tradePwdQuestionLinkForgetNode.href = this.baseUrl + '/account/tradepwd/findquestion.htm';
    		} else {
    			domClass.add(this.pwdQuestionIconNode, 'icon-middle-error');
    			this.tradePwdQuestionNode.innerHTML = '未设置';
    			this.tradePwdQuestionLinkForgetNode.innerHTML = '设置';
    			this.tradePwdQuestionLinkForgetNode.href = this.baseUrl + '/account/tradepwd/question.htm';
    		}
    	},
    	
    	render: function() {
    		var me = this;
    		me.pwdProgressBar = new ProgressBar({
    			label: '当前安全度：'
    		});
    		me.pwdProgressBar.placeAt(me.pwdStrongNode);
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	postCreate: function(){
    		var me = this;
    		me.render();
    	    this.inherited(arguments);
    	}
    });
});