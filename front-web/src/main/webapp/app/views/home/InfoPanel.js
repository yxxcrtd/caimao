define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'app/views/ViewMixin',
    'app/common/Global',
    'app/ux/GenericProgressBar',
    'dojo/dom-class',
    'dojo/dom-attr',
    'app/ux/GenericTooltip',
    'dojo/on',
    'app/ux/GenericButton',
    'app/common/Dict',
    'app/common/Data',
    'dojo/when',
    'app/common/Fx',
    'dojo/_base/config',
    'dojo/text!./templates/InfoPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, ViewMixin, Global,
            ProgressBar, domClass, domAttr, Tooltip, on, Button, Dict, Data, when, Fx, Config, template){
    return declare([_WidgetBase, _TemplatedMixin, ViewMixin], {
        templateString: template,
        mobile: '',
        userRealName: '',
        mobileNode0: '为您的账户安全，请尽快绑定手机。',
        mobileNode1: '',
        userRealNameNode0: '为保障您的权利，请尽快完成实名认证。<a target="_blank" href="'+Global.baseUrl + '/user/'+(Config.authentication ? 'uploadID.htm' : 'certification.htm')+'">立即认证</a>',
        userRealNameNode1: '',
        tradePwdNode0: '为您的账户安全，请尽快设置安全密码。<a target="_blank" href="'+Global.baseUrl+'/account/tradepwd/set.htm">立即设置</a>',
        tradePwdNode1: '',
        bankcardNode0: '为您的快捷操作，请尽快绑定银行卡。<a target="_blank" href="'+Global.baseUrl+'/account/bankcard/bind.htm?edit=0">立即绑定</a>',
        bankcardNode1: '',

        
        _setMobileAttr: function(value) {
            if (value) {
                domClass.add(this.mobileNode, 'd-user-status-true');
                this.set('mobileNode1', '您已绑定手机 '+Global.encodeInfo(value, 3, 3)+'。<a target="_blank" href="'+Global.baseUrl+'/user/changemobile.htm">更换手机</a>');
            }
        },

        _setTradePwdAttr: function(value) {
            if (value) {
                domClass.add(this.tradePwdNode, 'd-user-status-true');
                this.set('tradePwdNode1', '您已设置安全密码，强度为'+Dict.get('pwdStrength')[parseInt(value)]+'。' +
                    '<a target="_blank" href="'+Global.baseUrl+'/user/changetradepwd.htm">修改安全密码</a>');
            }
            this.securityBar.set('value', Global.getSecurity(this.securityBar.get('value'), {
                'tradePwd': parseFloat(value || 0) / 3
            }));
        },

        _setBankcardAttr: function(value) {
            if (value.length > 0) {
                domClass.add(this.bankcardNode, 'd-user-status-true');
                this.set('bankcardNode1', '您已绑定银行卡。' +
                    '<a target="_blank" href="'+Global.baseUrl+'/account/bankcard/bind.htm?edit=1">更换银行卡</a>');
            }
            this.securityBar.set('value', Global.getSecurity(this.securityBar.get('value'), {
                'card': !!value.length + 0
            }));
        },
        
        //借款总额
        _setTotalLoanAmountAttr:function(value){
            Fx.animNumber(this.loanAmountNode, 0, value, 20);
        },
        
        //可用余额
        _setAvalaibleAmountAttr:function(value){
        	this.avalaibleAmount = value;
        },
        //保证金总额  
        _setTotalCashAmountAttr:function(value){
            Fx.animNumber(this.totalCashAmountNode, 0, value, 20);
        },
        
        //冻结金额
        _setFreezeAmountAttr:function(value){
            Fx.animNumber(this.freezeAmountNode, 0, value, 20);
        },

        _setTotalSettledInterestAttr: function(value) {
            Fx.animNumber(this.totalSettledInterestNode, 0, value, 20);
        },
        afterSet: function(values) {
            var me = this;
            if (values && values.userId) {
                me.greetNode.innerHTML = Global.greet() + '，' + Global.sirOrLady(values);
                me.gradeNode.innerHTML = values.userGrade;
                me.securityBar.set('value', Global.getSecurity(me.securityBar.get('value'), {
                    'loginPwd': parseFloat(values.userPwdStrength) / 3,
                    'realName': parseFloat(values.isTrust),
                    'phone': !!values.mobile + 0,
                    'email': !!values.email + 0
                }));
                if (me.isTrust == 1) {
                    domClass.add(this.userRealNameNode, 'd-user-status-true');
                    this.set('userRealNameNode1', '您已完成实名认证 '+Global.encodeInfo(values.userRealName, 1, 0));
                }
            } else if (values && values.pzAccountId) {
                Fx.animNumber(this.avalaibleAmountNode, 0, values.avalaibleAmount - values.freezeAmount, 20);
            }
        },

        render: function() {
            var me = this;
            me.securityBar = new ProgressBar({
                label: '账户安全：',
                style: 'height: 25px;'
            }, me.sucurityNode);
            on(me.userStatusNode, '.fa:mouseover', function(e) {
                var setted = domClass.contains(this, 'd-user-status-true');
                Tooltip.show(me.get(domAttr.get(this, 'data-dojo-attach-point') + (setted ? 1 : 0)), this, setted ? 'info': 'warning', ['above-centered']);
            });
            on(me.userStatusNode, '.fa:mouseout', function(e) {
                Tooltip.hide();
            });
            me.rechangeBtn = new Button({
                label: '充值',
                width: 130,
                height: 36
            }, me.rechangeBtnNode);
            me.withdrawBtn = new Button({
                label: '取现',
                width: 130,
                height:36,
                color: '#E2E2E2',
                hoverColor: '#EDEDED',
                textStyle: {
                    color: '#666666'
                },
                innerStyle: {
                    borderColor: '#C9C9C9'
                }
            }, me.withdrawBtnNode);

            when(Data.getUserExtra()).then(function(data) {
                if (data.userPhoto) {
                    me.avatarNode.src = Config.baseUrl+ 'avatars/' + data.userPhoto + '?' + (new Date()).valueOf();
                }
            });
        },

        postCreate: function() {
            var me = this;
            me.render();
            me.inherited(arguments);
        }
    });
});