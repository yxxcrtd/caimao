define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'app/ux/GenericDisplayBox',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'app/ux/GenericPhoneCodeBox',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'app/common/Date',
    'dojo/dom-class',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/ForgetLoginPwdConfirmPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, 
		query, DisplayBox, TextBox, GenericButton,GenericPhoneCodeBox, dom, domConstruct, on, DateUtil, domClass, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	
    	mobile:'',
    	
    	checkCode:'',
    	
    	next: function() {},

        reset: function() {},
    	
    	_setMobileAttr: function(value) {
    		this._set('mobile', value);
            this.mobileFld.displayNode.innerHTML = Global.encodeInfo(value, 3, 3);
    	},
		
    	_setCheckCodeAttr: function(value) {
    		this._set('checkCode', value);
    	},
    	
		setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	render: function() {
    		var me = this;

            me.mobileFld = new DisplayBox({
                'label': '手机号',
                leftOffset: 360
            });
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

    		me.newPwdFld = new TextBox({
     			label: '输入新密码',
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
				}],
				type: 'password',
                showStrength: true,
				onKeyUp: function(e) {
					me.switchPasswordLevel(Global.checkStrong(this.get('value')));
				},
				
				extraHTML: '<p class="ui-prompt-grade" style="position: absolute; bottom: -18px;left: -1px;">' +
					'<span class="in"></span><span class="in"></span><span class="in"></span></p>'
			}),
			me.confirmPasswordFld = new TextBox({
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

            me.mobileFld.placeAt(me.formNode);
            me.codeFld.placeAt(me.formNode);

     		me.newPwdFld.placeAt(me.formNode);
    		me.confirmPasswordFld.placeAt(me.formNode);

            me.prevBtn = new GenericButton({
                'label': '上一步',
                style: {
                    marginLeft: '300px'
                },
                color: '#E2E2E2',
                hoverColor: '#EDEDED',
                textStyle: {
                    color: '#666666'
                },
                innerStyle: {
                    borderColor: '#C9C9C9'
                },
                handler: me.prev
            });

    		me.confirmBtn = new GenericButton({
    			'label': '确定',
    			enter: true,
    			handler: me.next
    		});

            me.prevBtn.placeAt(me.formNode);
    		me.confirmBtn.placeAt(me.formNode);

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
    	showError: function(err) {
    		Tooltip.show(err, this.confirmBtn.domNode);
    	},
    	isValid: function() {
    		return this.newPwdFld.checkValidity()&&
    		this.confirmPasswordFld.checkValidity() && this.codeFld.checkValidity();
    	},
    	getData: function() {
    		return {
    			mobile:this.mobile,
                //code: this.codeFld.get('value'),
    			//check_code:this.checkCode,
                check_code:this.codeFld.get('value'),
    			user_pwd: this.newPwdFld.get('value')
    		};
    	},
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});