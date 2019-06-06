define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'app/ux/GenericComboBox',
    'app/common/Global',
    'dojo/query',
    'dojo/dom-class',
    'app/ux/GenericTooltip',
    'app/stores/ComboStore',
    'dojo/text!./templates/TPasswordQuestionUpdatePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		TextBox, Button, ComboBox, Global, query, domClass, Tooltip, Store, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	onConfirm: function() {},
    	
    	getData: function() {
    		return {
    			'question1_id': this.question1Fld.value,
    			'question1_answer': this.answer1Fld.get('value'),
    			'question2_id': this.question2Fld.value,
    			'question2_answer': this.answer2Fld.get('value'),
    			'question3_id': this.question3Fld.value,
    			'question3_answer': this.answer3Fld.get('value'),
    			"trade_pwd":this.tradePwdFld.get('value')
    		};
    	},
    	
    	isValid: function() {
    		return this.tradePwdFld.checkValidity()&&
    		this.question1Fld.checkValidity() &&
    		this.answer1Fld.checkValidity() &&
    		this.question2Fld.checkValidity() &&
    		this.answer2Fld.checkValidity() &&
    		this.question3Fld.checkValidity() &&
    		this.answer3Fld.checkValidity();
    	},
    	
    	showError: function(err) {
    		Tooltip.show(err, this.confirmBtn.domNode);
    	},
    	
    	render: function() {
    		var me = this;
    		me.tradePwdFld = new TextBox({
    			label:"安全密码",
    			type:"password",
    			leftOffset: 360,
    			validates: [{
					pattern: /.+/,
					message: '请输入安全密码'
				}]
    		});
    		me.question1Fld = new ComboBox({
    			label: '问题1',
    			leftOffset: 360,
    			editable: false,
    			validates: [{
					pattern: /.+/,
					message: '请选择问题'
				}],
    			searchAttr: 'question_id',
    			labelAttr: 'question_title',
    			store: new Store({
    				url: Global.baseUrl + '/user/pwdquestion/list',
    				requestMethod: 'post'
    			})
    		});
    		me.answer1Fld = new TextBox({
    			label: '答案',
    			validates: [{
					pattern: /.+/,
					message: '请输入答案'
				}],
    			leftOffset: 360
    		});
    		me.question2Fld = new ComboBox({
    			label: '问题2',
    			leftOffset: 360,
    			editable: false,
    			validates: [{
					pattern: /.+/,
					message: '请选择问题'
				}],
    			searchAttr: 'question_id',
    			labelAttr: 'question_title',
    			store: new Store({
    				url: Global.baseUrl + '/user/pwdquestion/list',
    				requestMethod: 'post'
    			})
    		});
    		me.answer2Fld = new TextBox({
    			label: '答案',
    			validates: [{
					pattern: /.+/,
					message: '请输入答案'
				}],
    			leftOffset: 360
    		});
    		me.question3Fld = new ComboBox({
    			label: '问题3',
    			leftOffset: 360,
    			editable: false,
    			validates: [{
					pattern: /.+/,
					message: '请选择问题'
				}],
    			searchAttr: 'question_id',
    			labelAttr: 'question_title',
    			store: new Store({
    				url: Global.baseUrl + '/user/pwdquestion/list',
    				requestMethod: 'post'
    			})
    		});
    		me.answer3Fld = new TextBox({
    			label: '答案',
    			validates: [{
					pattern: /.+/,
					message: '请输入答案'
				}],
    			leftOffset: 360
    		});
    		me.confirmBtn = new Button({
    			label: '提交',
    			enter: true,
    			leftOffset: 360,
    			handler: this.onConfirm
    		});
    		
    		me.question1Fld.set('related', [me.question2Fld, me.question3Fld]);
    		me.question2Fld.set('related', [me.question1Fld, me.question3Fld]);
    		me.question3Fld.set('related', [me.question1Fld, me.question2Fld]);
    		
    		me.question1Fld.placeAt(me.formNode);
    		me.answer1Fld.placeAt(me.formNode);
    		me.question2Fld.placeAt(me.formNode);
    		me.answer2Fld.placeAt(me.formNode);
    		me.question3Fld.placeAt(me.formNode);
    		me.answer3Fld.placeAt(me.formNode);
    		me.tradePwdFld.placeAt(me.formNode);
    		me.confirmBtn.placeAt(me.formNode);
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});