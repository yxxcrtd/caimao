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
    'dojo/text!./templates/FindTradePwdStep1Panel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, domConstruct, 
		query, Global, on, dom, string, GenericButton, TextBox,GenericPhoneCodeBox,  DisplayBox, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	mobile: '',
    	tradeQquestion:'',
    	questionId:'',
    
    	next: function() {},
    	
    	_setMobileAttr: function(value) {
    		this._set('mobile', value);
    		this.oldMobileFld.displayNode.innerHTML = Global.encodeInfo(value, 3, 3);
    	},
    	
    	_setTradeQquestionAttr: function(value) {
    		this._set('questionId', value[0].questionId);
    		this.questionFld.displayNode.innerHTML = value[0].questionTitle;
    	},
    	
    	render: function() {
    		var me = this;
    		me.oldMobileFld = new DisplayBox({
    			'label': '原手机号'
    		});
    		me.oldMobileFld.placeAt(me.formNode);
    		
    		me.codeFld = new GenericPhoneCodeBox({
    			'label':'短信验证码',
    			style: {
    				marginLeft: '360px'
    			},
    			labelStyle: {
    				left: '-130px'
    			},
    			validates: [{
    				//not empty  
    				pattern: /.+/,
    				message: '请输入验证码'
    			}],
    			buttonLeft: 480,
    			inputWidth: 100
    		});
    		me.codeFld.placeAt(me.formNode);
    		
    		me.questionFld = new DisplayBox({
    			'label': '安全保护问题'
    		});
    		me.questionFld.placeAt(me.formNode);
    		
    		me.answerFld = new TextBox({
    			'label':'答案',
    			style: {
    				marginLeft: '360px'
    			},
    			labelStyle: {
					left: '-130px'
				},
	    		validates: [{
	    			//not empty  
					pattern: /.+/,
					message: '请输入答案'
				}]
    		});
    		
    		me.answerFld.placeAt(me.formNode);
    		
    		me.commit = new GenericButton({
    			'label':'下一步',
				style:{
					marginLeft: '360px'
				},
				enter: true,
				handler: me.next
    		});
    		me.commit.placeAt(me.formNode);
    	/*	var html = domConstruct.toDom('<a href="'+Global.baseUrl+'/account/tradepwd/findtradepwd.htm" class="am-left-10">重新选择验证方式</a>');
    		domConstruct.place(html, me.formNode);*/
    	},
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	getData: function() {
    		return {
    			mobile: this.mobile,
    			code: this.codeFld.get('value'),
    			question_id:this.questionId,
    			answer:this.answerFld.get('value')
    		};
    	},
    	showError: function(message) {
    		Tooltip.show(message, this.commit.domNode);
    	},
    	isValid: function() {
     		return this.codeFld.checkValidity() &&
     			   this.answerFld.checkValidity();
    	},
    	postCreate: function(){
    		var me = this;
    		me.render();
    	    this.inherited(arguments);
    	}
    });
});