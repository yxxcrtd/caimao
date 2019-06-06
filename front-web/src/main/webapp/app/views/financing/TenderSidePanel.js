define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'app/ux/GenericButton',
    'app/ux/GenericTextBox',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'app/common/Date',
    'app/views/ViewMixin',
    'app/ux/GenericWindow',
    'app/common/Data',
    'dojo/when',
    'dojo/_base/config',
    'app/common/User',
    'dojo/text!./templates/TenderSidePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,
            Global, query, Button, TextBox, dom, domConstruct, on, DateUtil, ViewMixin, Win, Data, when, cfg,User, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        availableAmount: '',

        tender: '',

        _setTenderAttr: function(data) {
            var min = data.invsAmountMin,
                max = data.invsAmountPre - data.invsAmountActual;
            this.amountFld.validates.push({
                pattern: function() {
                    return this.getAmount() >= Math.min(min, max) + '';
                },
                message: '最小投标金额为' + Global.formatAmount(Math.min(min, max))
            });
            this.amountFld.validates.push({
                pattern: function() {
                    return this.getAmount() <= max + '';
                },
                message: '最大投标金额为' + Global.formatAmount(max)
            });
            this.amountFld.max=max;
            this.amountFld.set('placeHolder', Global.formatAmount(Math.min(min, max)) + '-' + Global.formatAmount(max));
        },

        _setAvailableAmountAttr: function(value) {
            this.availableAmountNode.innerHTML = Global.formatAmount(value);
        },
        
    	render: function() {
    		var me = this;
            me.amountFld = new TextBox({
                validates: [{
                    pattern: /.+/,
                    message: '请输入投标金额'
                }],
                unit: '元',
                limitRegex: /[\d\.]/,
                isAmount: true,
                isNumber: true
            }, me.amountNode);
            me.pwdFld = new TextBox({
                placeHolder: '安全密码',
                validates: [{
                    pattern: /.+/,
                    message: '请输入安全密码'
                }],
                type: 'password'
            }, me.pwdNode);
            me.confirmBtn = new Button({
                'label': '立即投标',
                position: 'center',
                disabledMsg: '勾选协议即可投标',
                disabled: !me.checkNode.checked,
                width: 220
            }, me.confirmBtnNode);
            on(me.checkNode, 'click', function() {
                me.confirmBtn.set('disabled', !this.checked);
                if(this.checked){
                	when(Data.getUser(), function(user) {
                		if(user){
                			 User.isTrust(null, function() {
                				 me.confirmBtn.set('disabled', true);
                				 me.confirmBtn.set('disabledMsg', '请您先完成<a target="_blank" href="'+Global.baseUrl+'/user/'+(cfg.authentication ? 'uploadID.htm' : 'certification.htm')+'">实名认证</a>');
                	         });
                			 User.isTradePwd(null, function() {
                              	me.confirmBtn.set('disabled', true);
                              	me.confirmBtn.set('disabledMsg', '您还未设置安全密码，请先<a href="'+Global.baseUrl+'/account/tradepwd/set.htm">设置</a>');
                              });
                		}else{
                			
                		}
                	});
                	
                }
            });
            on(me.protocolNode, 'click', function(e) {
                if (!me.win) {
                    me.win = new Win({
                        width: 900,
                        height: 500,
                        title: 'p2p协议'
                    });
                    me.win.placeAt(document.body);
                }
                me.win.show();
                when(Data.getProtocol({id: 3}), function(tmpl) {
                    me.win.set('msg', tmpl);
                });
            });
    	},

        reset: function() {
            this.amountFld.reset();
            this.pwdFld.reset();
        },
        
        isValid: function() {
            return this.amountFld.checkValidity() && this.pwdFld.checkValidity();
        },
    	
    	postCreate: function() {
    		var me = this;
            me.render();
    		me.inherited(arguments);
    	}
    	
    });
});