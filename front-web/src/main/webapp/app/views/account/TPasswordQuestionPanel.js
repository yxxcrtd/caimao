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
    'dojo/text!./templates/TPasswordQuestionPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		TextBox, Button, ComboBox, Global, query, domClass, Tooltip, Store, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,

        reset: function() {},
    	
    	onConfirm: function() {},
    	
    	getData: function() {
    		return {
    			'question1': this.question1Fld.value,
    			'answer1': this.answer1Fld.get('value'),
    			'question2': this.question2Fld.value,
    			'answer2': this.answer2Fld.get('value'),
    			'question3': this.question3Fld.value,
    			'answer3': this.answer3Fld.get('value')
    		};
    	},
    	
    	isValid: function() {
    		return this.question1Fld.checkValidity() &&
    		this.answer1Fld.checkValidity() &&
    		this.question2Fld.checkValidity() &&
    		this.answer2Fld.checkValidity() &&
    		this.question3Fld.checkValidity() &&
    		this.answer3Fld.checkValidity();
    	},
    	
    	showError: function(err) {
    		Tooltip.show(err, this.confirmBtn.domNode,'warning');
    	},
    	
    	render: function() {
    		var me = this;
    		me.question1Fld = new ComboBox({
    			label: '问题1',
    			leftOffset: 360,
    			editable: false,
    			validates: [{
					pattern: /.+/,
					message: '请选择问题'
				}],
    			searchAttr: 'questionId',
    			labelAttr: 'questionTitle',
    			store: new Store({
    				url: Global.baseUrl + '/pwdquestion',
    				requestMethod: 'get'
    			})
    		});
    		me.answer1Fld = new TextBox({
    			label: '答案',
    			validates: [{
					pattern: /.+/,
					message: '请输入答案'
				}, {
					pattern: /^.{0,32}$/,
					message: '答案长度超过限制'
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
    			searchAttr: 'questionId',
    			labelAttr: 'questionTitle',
    			store: new Store({
    				url: Global.baseUrl + '/pwdquestion',
    				requestMethod: 'get'
    			})
    		});
    		me.answer2Fld = new TextBox({
    			label: '答案',
    			validates: [{
					pattern: /.+/,
					message: '请输入答案'
				}, {
					pattern: /^.{0,32}$/,
					message: '答案长度超过限制'
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
    			searchAttr: 'questionId',
    			labelAttr: 'questionTitle',
    			store: new Store({
    				url: Global.baseUrl + '/pwdquestion',
    				requestMethod: 'get'
    			})
    		});
    		me.answer3Fld = new TextBox({
    			label: '答案',
    			validates: [{
					pattern: /.+/,
					message: '请输入答案'
				}, {
					pattern: /^.{0,32}$/,
					message: '答案长度超过限制'
				}],
    			leftOffset: 360
    		});

            me.prevBtn = new Button({
                'label': '上一步',
                style: {
                    marginLeft: '310px'
                },
                handler: this.prev
            });    

            me.confirmBtn = new Button({
                label: '提交',
                style: {
                    marginLeft: '44px'
                },
                enter: true,
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

            me.prevBtn.placeAt(me.formNode);
    		me.confirmBtn.placeAt(me.formNode);
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});