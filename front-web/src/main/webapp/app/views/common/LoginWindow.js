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
    'dojo/text!./templates/LoginWindow.html'
], function(declare, _WidgetBase, _TemplatedMixin, domStyle, on, mouse, 
		DialogUnderlay, Global, query, focusUtil, dom, domConstruct, string,
        domClass, Position, Moveable, TextBox, Button, ViewMixin, Ajax, RSA,
        Tooltip, lang, cookie, MiniMsgBox, LoginTopbar, when, Data, template){
    return declare([_WidgetBase, _TemplatedMixin, ViewMixin], {
    	
    	templateString: template,

    	contentClass: '',
    	title: '欢迎登录',
    	closeAction: 'hide',
    	confirmText: '',
    	confirmAndCancel: false,
    		
    	_setContentClassAttr: function(value){
    		this._set('contentClass', value);
    		domClass.add(this.contentNode, value);		
    	},
    	
    	_setTitleAttr: function(value) {
    		this._set('title', value);
    		this.titleNode.innerHTML = value;
    	},
    	
    	show: function() {
    		var me = this;
    		me.enterBtns = query('.dijitButton.enterbutton').filter(function(i) {
    			return i != me.loginBtn.domNode;
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
    	
    	close: function() {
    		var me = this;
    		if (me.enterBtns) {
    			me.enterBtns.addClass('enterbutton');
    		}
    		domConstruct.destroy(me.domNode);
    		DialogUnderlay.hide();
    	},
    	
    	hide: function() {
    		var me = this;
    		if (me.enterBtns) {
    			me.enterBtns.addClass('enterbutton');
    		}
    		domStyle.set(me.domNode, {
    			display: 'none'
    		});
    		DialogUnderlay.hide();
    	},
    	
    	addListeners: function() {
    		var me = this;
    		on(me.closeNode, 'click', function() {
    			me[me.get('closeAction')]();
    		});
    	},

        render: function() {
            var me = this;
            on(me.closeNode, 'click', function() {
                me[me.get('closeAction')]();
            });
            me.usernameFld = new TextBox({
                placeHolder: '手机号',
                validates: [{
                    pattern: /.+/,
                    message: '请输入手机号'
                }]
            }, me.usernameNode);
            me.pwdFld = new TextBox({
                placeHolder: '密码',
                validates: [{
                    pattern: /.+/,
                    message: '请输入密码'
                }],
                type: 'password'
            }, me.pwdNode);
            me.loginBtn = new Button({
                label: '登录',
                enter: true,
                width: 200,
                onClick: function() {
                    if (me.isValid()) {
                        me.loginBtn.loading(true);
                        var key = RSA.getKeyPair(me.exponent, '', me.modulus),
                            username = lang.trim(me.usernameFld.get('value')),
                            pwd = RSA.encryptedString(key, me.pwdFld.get('value'));
                        Ajax.post(Global.baseUrl + '/user/login', {
                            'login_name': username,
                            'login_pwd': pwd
                        }).then(function (response) {
                            me.loginBtn.loading(false);
                            if (response.success) {
                                me.hide();
                                var msgBox = new MiniMsgBox({
                                    text: '登录成功'
                                });
                                msgBox.placeAt(document.body);
                                msgBox.show();
                                when(Data.getUser(), function(user) {
                                    var loginTopbar = new LoginTopbar({
                                        username: Global.sirOrLady(user)
                                    });
                                    domConstruct.empty(dom.byId('usertopbar'));
                                    domConstruct.place(loginTopbar.domNode, dom.byId('usertopbar'), 'replace');
                                });
                                cookie('username', username, {path: '/'});
                                cookie('loginname', username, {path: '/'});
                                if (me.onLogin) {
                                    me.onLogin();
                                }
                            } else {
                                Tooltip.show(response.msg, me.loginBtn.innerNode);
                            }
                        });
                    }
                }
            }, me.btnNode);
        },

        isValid: function() {
            return this.usernameFld.checkValidity() &&
                this.pwdFld.checkValidity();
        },
    	
    	postCreate: function(){
    		var me = this;
            (function () {
                Ajax.get(Global.baseUrl + '/sec/rsa', {
                }).then(function (response) {
                    if (response.success) {
                        me.modulus = response.data.modulus;
                        me.exponent = response.data.exponent;
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