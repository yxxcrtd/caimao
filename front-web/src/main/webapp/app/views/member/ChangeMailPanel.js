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
    'app/ux/GenericTooltip',
    'dojo/text!./templates/ChangeMailPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, domConstruct, 
		query, Global, on, dom, string, GenericButton, TextBox,GenericPhoneCodeBox,  DisplayBox, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	username: '',
    	usernameReal: '',
    	
    	oldMail:'',
    	
    	next: function() {},
    	
    	_setOldMailAttr: function(value) {
    		this._set('oldMail', value);
    		this.mailFld.displayNode.innerHTML = value;
    		if(value == ''){
    			this.mailFld.domNode.style.display = 'none';
    		} else {
    			dom.byId('headname').innerHTML = '修改绑定邮箱';
    		}
    	},
    	
    	render: function() {
    		var me = this;
    		me.mailFld = new DisplayBox({
    			'label': '原绑定邮箱'
    		});
    		me.mailFld.placeAt(me.formNode);
    		
    		me.newMailFld = new TextBox({
				label: '新绑定邮箱',
    			style: {
    				marginLeft: '360px'
    			},
    			labelStyle: {
					left: '-130px'
				},
				validates: [{
					//not empty  
					pattern: /.+/,
					message: '请输入绑定邮箱'
				}, {
					//regular  mail
					pattern: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
					message: '请填写您正确的邮箱'
				}, {
					pattern: function() {
						return this.get('Value') != me.get('oldMail');
					},
					message: '新邮箱跟原邮箱一致，请重新输入'
				}]
    		});
    		me.newMailFld.placeAt(me.formNode);
    		
    		me.tradePwdFld = new TextBox({
				label:'安全密码',
				type:'password',
    			style: {
    				marginLeft: '360px'
    			},
    			labelStyle: {
    				left: '-130px'
    			},
    			validates: [{
					//not empty  
					pattern: /.+/,
					message: '请输入安全密码'
				}]
    		});
    		me.tradePwdFld.placeAt(me.formNode);
    		
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
    	getData: function() {
    		return {
    			email: this.newMailFld.get('value'),
    			tradePwd: this.tradePwdFld.get('value')
    		};
    	},
    	showError: function(message) {
    		Tooltip.show(message, this.commit.domNode,'warning');
    	},
    	isValid: function() {
    		return this.newMailFld.checkValidity() &&
    			this.tradePwdFld.checkValidity();
    	},
    	postCreate: function(){
    		var me = this;
    		me.render();
    	    this.inherited(arguments);
    	}
    });
});