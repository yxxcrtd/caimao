define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'dojo/dom-style',
    'dojo/on',
    'dojo/mouse',
    'dijit/DialogUnderlay',
    'app/common/Global',
    'dojo/query',
    'dijit/focus',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/string',
    "dojo/dom-class",
    'app/common/Position',
    'dojo/dnd/Moveable',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'app/views/ViewMixin',
    'app/common/Ajax',
    'app/common/RSA',
    'app/ux/GenericTooltip',
    'dojo/_base/lang',
    'dojo/cookie',
    'app/ux/GenericMiniMsgBox',
    'app/views/common/LoginTopbar',
    'dojo/when',
    'app/common/Data',
    'dojo/text!./templates/ScoreWindow.html'
], function (declare, _WidgetBase, _TemplatedMixin, domStyle, on, mouse, DialogUnderlay, Global, query, focusUtil, dom, domConstruct, string, domClass, Position, Moveable, TextBox, Button, ViewMixin, Ajax, RSA, Tooltip, lang, cookie, MiniMsgBox, LoginTopbar, when, Data, template) {
    return declare([_WidgetBase, _TemplatedMixin, ViewMixin], {

        templateString: template,

        width: '',
        height: '',

        contentClass: '',
        title: '兑现返现',
        //积分
        score: '',
        closeAction: 'hide',
        confirmText: '',
        confirmAndCancel: false,
        scoreRule: '',

        _setWidthAttr: function (value) {
            domStyle.set(this.domNode, {
                width: value + 'px'
            });
        },

        _setHeightAttr: function (value) {
            domStyle.set(this.contentNode, {
                height: value + 'px',
                'overflowY': 'scroll'
            });
        },

        _setContentClassAttr: function (value) {
            this._set('contentClass', value);
            domClass.add(this.contentNode, value);
        },

        _setTitleAttr: function (value) {
            this._set('title', value);
            this.titleNode.innerHTML = value;
        },

        //积分
        _setScoreAttr: function (value) {
            this._set('score', value);
            this.scoreNode.innerHTML = value;
        },


        show: function () {
            var me = this;
            (function () {
                Ajax.get(Global.baseUrl + '/score/rule', {
                }).then(function (response) {
                    if (response.success) {
                    	scoreRule = response.data.items[0].paramValue;
                        me.scoreRuleNode.innerHTML = scoreRule + "单位积分转1单位本位币";
                    } else {
                        //TODO
                    }

                });
            })();
            me.enterBtns = query('.dijitButton.enterbutton').filter(function (i) {
                return i != me.confirmBtn.domNode;
            });
            me.enterBtns.removeClass('enterbutton');
            domStyle.set(me.domNode, {
                display: 'block'
            });
            Position.screenCenter(me.domNode);
            DialogUnderlay.show({}, 9999);
            if (document.activeElement) {
                document.activeElement.blur();
            }
            me.domNode.focus();
        },

        close: function () {
            var me = this;
            if (me.enterBtns) {
                me.enterBtns.addClass('enterbutton');
            }
            domConstruct.destroy(me.domNode);
            DialogUnderlay.hide();
        },

        hide: function () {
            var me = this;
            if (me.enterBtns) {
                me.enterBtns.addClass('enterbutton');
            }
            domStyle.set(me.domNode, {
                display: 'none'
            });
            DialogUnderlay.hide();
        },

        addListeners: function () {
            var me = this;
            on(me.closeNode, 'click', function () {
                me[me.get('closeAction')]();
            });
        },

        render: function () {
            var me = this;
            on(me.closeNode, 'click', function () {
                me[me.get('closeAction')]();
            });
            me.scoreFld = new TextBox({
                label: ' 兑换金额',
                unit: '元',
                validates: [
                    {
                        pattern: /^[0-9]*$/,
                        message: '兑换金额必须是整数'
                    },
                    {
                        pattern: /.+/,
                        message: '请输入兑换金额 '
                    },
                    {
                        pattern: function () {
                            return parseInt(this.get('value')) * parseInt(scoreRule) <= me.score;
                        },
                        message: '积分余额不足 '
                    }
                ],
                style: {
                    marginLeft: '140px'
                }
            }, me.makeGoodNode);
            me.tradePwdFld = new TextBox({
                label: ' 安全密码',
                validates: [
                    {
                        pattern: /.+/,
                        message: '请输入安全密码 '
                    }
                ],
                style: {
                    marginLeft: '140px'
                },
                type: 'password'
            }, me.tradePwdNode);
            me.confirmBtn = new Button({
                label: '确认兑换',
                enter: true,
                width: 150,
                position: 'center'
            }, me.btnNode);
            on(me.confirmBtn, 'click', function () {
                if (me.isValid()) {
                    this.loading(true);
                    var pwd = RSA.encryptedString(me.key, me.tradePwdFld.get('value'));
                    Ajax.post(Global.baseUrl + "/user/score/apply", {
                        'score': me.scoreFld.get('value'),
                        'tradePwd': pwd
                    }).then(function (response) {
                    	me.confirmBtn.loading(false);
                        if (response.data) {
                            me.formNode.style.display = 'none';
                            me.successfulNode.style.display = 'block';
                        }
                        else {
                            Tooltip.show(response.msg, me.confirmBtn.innerNode, 'warning');
                        }
                    });
                }
            });
        },

        isValid: function () {
            return this.scoreFld.checkValidity() && this.tradePwdFld.checkValidity;
        },
        postCreate: function () {
            var me = this;
            (function () {
                Ajax.get(Global.baseUrl + '/sec/rsa', {
                }).then(function (response) {
                    if (response.success) {
                        me.modulus = response.data.modulus;
                        me.exponent = response.data.exponent;
                        me.key = RSA.getKeyPair(me.exponent, '', me.modulus);

                    } else {
                        //TODO
                    }

                });
            })();

            domStyle.set(me.domNode, {
                position: 'absolute',
                'zIndex': '10000',
                'padding': '4px'
            });

            new Moveable(me.domNode, {
                handle: me.titleCtnNode
            });

            me.render();

            me.inherited(arguments);
        }
    });
});