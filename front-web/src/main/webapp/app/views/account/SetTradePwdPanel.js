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
    'dojo/text!./templates/SetTradePwdPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, domConstruct, 
		query, Global, on, dom, string, GenericButton, TextBox,GenericPhoneCodeBox,  DisplayBox,domClass, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
   
    	next: function() {},

        reset: function() {},
    	
    	render: function() {
    		var me = this;
    		
     		me.newPwdFld = new TextBox({
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
					message: '请再次输入安全密码'
				}, {
					pattern: function() {
						return me.confirmPasswordFld.get('Value') == me.newPwdFld.get('Value');
					},
					message: '两次密码输入不一致'
				}],
				type: 'password'
			}),
			me.newPwdFld.placeAt(me.formNode);
			me.confirmPasswordFld.placeAt(me.formNode);

            me.prevBtn = new GenericButton({
                'label':'上一步',
                style:{
                    marginLeft: '285px'
                },
                handler: me.prev
            });

    		me.commit = new GenericButton({
        		'label':'提交',
    			style:{
    				marginLeft: '70px'
    			},
    			enter: true,
    			handler: me.next
    		});

            me.prevBtn.placeAt(me.formNode);
    		me.commit.placeAt(me.formNode);
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
    			trade_pwd: this.newPwdFld.get('value')
    		};
    	},
    	showError: function(message) {
    		Tooltip.show(message, this.commit.domNode);
    	},
    	isValid: function() {
			return this.newPwdFld.checkValidity()&&
			this.confirmPasswordFld.checkValidity();
	    },	
    	postCreate: function(){
    		var me = this;
    		me.render();
    	    this.inherited(arguments);
    	}
    });
});