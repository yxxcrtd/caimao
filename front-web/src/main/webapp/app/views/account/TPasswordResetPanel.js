define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'app/common/Global',
    'dojo/query',
    'dojo/dom-class',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/TPasswordResetPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		TextBox, Button, Global, query, domClass, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	baseUrl: Global.baseUrl,
    	
    	onConfirm: function() {},
    	prev: function() {},
    	
    	render: function() {
    		var me = this,
    			password = me.passwordFld = new TextBox({
    				label: '设置安全密码',
    				style: 'margin-left: 360px',
    				labelStyle: {
    					left: '-130px'
    				},
    				promptMessage: '6-16位字符，可包含英文字符、数字、符号',
    				validates: [{
    					pattern: /.+/,
    					message: '请输入安全密码'
    				}, {
    					pattern: /^.{6,16}$/,
    					message: '密码长度在6-16个字符'
    				}, {
    					pattern: /^[A-Za-z0-9\s!"#$%&'()*+,-.\/:;<=>?@\[\\\]^_`{|}~]+$/,
    					message: '密码格式错误'
    				}],
    				type: 'password',
                    showStrength: true,
    				onKeyUp: function(e) {
    					me.switchPasswordLevel(Global.checkStrong(this.get('value')));
    				},
    				
    				extraHTML: '<p class="ui-prompt-grade" style="position: absolute; top: 30px">' +
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
    					message: '请再次输入安全密码'
    				}, {
    					pattern: function() {
    						return me.confirmPasswordFld.get('Value') == me.passwordFld.get('Value');
    					},
    					message: '两次密码输入不一致'
    				}],
    				type: 'password'
    			}),
    			prevBtn = me.prevBtn = new Button({
    				label: '上一步',
    				handler: me.prev,
    				style: {
    					'marginLeft': '360px'
    				},
    				color: '#E2E2E2',
        			hoverColor: '#EDEDED',
        			textStyle: {
        				color: '#666666'
        			},
        			innerStyle: {
        				borderColor: '#C9C9C9'
        			}
    			}),
    			confirmBtn = me.confirmBtn = new Button({
    				label: '提交',
    				enter:true,
    				handler: me.onConfirm,
    				style: {
    					'marginLeft': '0px'
    				}
    			});
    		
    		password.placeAt(me.formNode);
    		confirmPassword.placeAt(me.formNode);
    		prevBtn.placeAt(me.formNode);
    		confirmBtn.placeAt(me.formNode);
    	},
    	
    	switchPasswordLevel: function(level) {
    		var gradeEl = query('.ui-prompt-grade', this.passwordFld.domNode)[0],
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
    			transaction_pwd: this.passwordFld.getValue()
    		}
    	},
    	
    	reset: function() {
    		this.passwordFld.reset();
			this.confirmPasswordFld.reset();
    	},
    	
    	isValid: function() {
    		return this.passwordFld.checkValidity() && this.confirmPasswordFld.checkValidity();
    	},
    	
    	showError: function(err) {
    		Tooltip.show(err.info, this.confirmBtn.domNode);
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});