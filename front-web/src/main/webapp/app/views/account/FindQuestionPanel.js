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
    'dojo/text!./templates/FindQuestionPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, domConstruct, 
		query, Global, on, dom, string, GenericButton, TextBox,GenericPhoneCodeBox,  DisplayBox, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	mobile: '',
    
    	next: function() {},
    	
    	_setMobileAttr: function(value) {
    		this._set('mobile', value);
    		this.oldMobileFld.displayNode.innerHTML = Global.encodeInfo(value, 3, 3);
    	},
    	
    	render: function() {
    		var me = this;
    		me.oldMobileFld = new DisplayBox({
    			'label': '绑定的手机号'
    		});
    		me.oldMobileFld.placeAt(me.formNode);
    		
    		me.codeFld = new GenericPhoneCodeBox({
    			'label':'验证码',
    			leftOffset: 360,
    			inputWidth: 100,
	    		validates: [{
					//not empty  
					pattern: /.+/,
					message: '请输入手机验证码'
				}]
    		});
    		me.codeFld.placeAt(me.formNode);
    		
    		me.tradePwdFld = new TextBox({
    			'label': '安全密码',
    			leftOffset:360,
    			type:'password',
    			validates:[{
    				pattern:/.+/,
    				message:'请输入安全密码'
    			}]
    		});
    		me.tradePwdFld.placeAt(me.formNode);
    		
    		me.commit = new GenericButton({
    			'label':'下一步',
				style:{
					marginLeft: '360px'
				},
				enter: true,
				handler: me.next
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
    			mobile: this.mobile,
    			code:this.codeFld.get('value'),
    			tradePwd:this.tradePwdFld.get('value')
    		};
    	},
    	showError: function(message) {
    		Tooltip.show(message, this.commit.domNode);
    	},
    	isValid: function() {
    		  return  this.codeFld.checkValidity() &&
			          this.tradePwdFld.checkValidity();
    	},
    	postCreate: function(){
    		var me = this;
    		me.render();
    	    this.inherited(arguments);
    	}
    });
});