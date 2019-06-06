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
    'app/ux/GenericButton',
    'app/ux/GenericTextBox',
    'app/ux/GenericPhoneCodeBox',
    'app/ux/GenericDisplayBox',
    'dojo/dom-class',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/ChangeLoginPwdPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, domConstruct, 
		query, Global, on, dom, string, GenericButton, TextBox,GenericPhoneCodeBox,  DisplayBox,domClass, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	username: '',
    	usernameReal: '',
    	userLoginName:'',
    	
    	next: function() {},
    	
    	_setUserLoginNameAttr: function(value) {
    		this._set('userLoginName', value);
    		this.userLoginNameFld.displayNode.innerHTML = value;//Global.encodeInfo(value, 3, 3);
    	},
    	
    	render: function() {
    		var me = this;
    		me.userLoginNameFld = new DisplayBox({
    			'label': '用户名'
    		});
    		me.userLoginNameFld.placeAt(me.formNode);  		
    		
    		me.oldPwdFld = new TextBox({
    			'label': '旧密码',
    			'type':'password',
    			style: {
    				marginLeft: '360px'
    			},
    			labelStyle: {
					left: '-130px'
				},
				validates: [{
					//not empty  
					pattern: /.+/,
					message: '请输入密码'
				}]
    		});
    
    		
     		me.newPwdFld = new TextBox({
     			label: '新密码',
				style: 'margin-left: 360px',
				labelStyle: {
					left: '-130px'
				},
				promptMessage: '6-16位字符，可包含英文字符、数字、符号',
				validates: [{
					pattern: /.+/,
					message: '请输入登录密码'
				}, {
					pattern: /^.{6,16}$/,
					message: '密码长度在6-16个字符'
				}, {
					pattern: /^[A-Za-z0-9\s!"#$%&'()*+,-.\/:;<=>?@\[\\\]^_`{|}~]+$/,
					message: '密码格式错误'
				}, {
					pattern: function() {
						return me.oldPwdFld.get('Value') != me.newPwdFld.get('Value');
					},
					message: '新密码和旧密码输入一样'
				}],
				type: 'password',
                showStrength: true,
				onKeyUp: function(e) {
					me.switchPasswordLevel(Global.checkStrong(this.get('value')));
				},
				
				extraHTML: '<p class="ui-prompt-grade" style="position: absolute; bottom: -18px;left: -1px;">' +
					'<span class="in"></span><span class="in"></span><span class="in"></span></p>'
			}),
			confirmPassword = me.confirmPasswordFld = new TextBox({
				label: '再次输入',
				style: 'margin-left: 360px',
				labelStyle: {
					left: '-130px'
				},
				validates: [{
					pattern: /.+/,
					message: '请再次输入登录密码'
				}, {
					pattern: function() {
						return me.confirmPasswordFld.get('Value') == me.newPwdFld.get('Value');
					},
					message: '两次密码输入不一致'
				}],
				type: 'password'
			}),
			me.oldPwdFld.placeAt(me.formNode);
     		me.newPwdFld.placeAt(me.formNode);
    		me.confirmPasswordFld.placeAt(me.formNode);
    		me.commit = new GenericButton({
    			'label':'提交',
    			enter: true,
			style:{
				marginLeft: '360px'
			}
    		});
    		me.commit.placeAt(me.formNode);
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	switchPasswordLevel: function(level) {
    		var gradeEl = query('.ui-prompt-grade', this.newPwdFld.domNode)[0],
    			levels = query('.in', gradeEl);
    		switch(level) {
    		case 0:
    			domClass.remove(levels[0], 'strength01');
    			domClass.remove(levels[1], 'strength02');
    			domClass.remove(levels[2], 'strength03');
    			break;
    		case 1:
    			domClass.add(levels[0], 'strength01');
    			domClass.remove(levels[1], 'strength02');
    			domClass.remove(levels[2], 'strength03');
    			break;
    		case 2:
    			domClass.add(levels[0], 'strength01');
    			domClass.add(levels[1], 'strength02');
    			domClass.remove(levels[2], 'strength03');
    			break;
    		case 3:
    			domClass.add(levels[0], 'strength01');
    			domClass.add(levels[1], 'strength02');
    			domClass.add(levels[2], 'strength03');
    			break;
    		}
    	},
    	
    	getData: function() {
    		return {
    			oldPwd: this.oldPwdFld.get('value'),
    			newPwd: this.newPwdFld.get('value')
    		};
    	},
    	showError: function(message) {
    		Tooltip.show(message, this.commit.domNode);
    	},
    	isValid: function() {
    		return this.oldPwdFld.checkValidity() &&
    			this.newPwdFld.checkValidity() && 
    			this.confirmPasswordFld.checkValidity();
    	},
    	postCreate: function(){
    		var me = this;
    		me.render();
    	    this.inherited(arguments);
    	}
    });
});