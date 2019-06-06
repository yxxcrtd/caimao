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
    'dojo/dom-style',
    'dojo/dom-class',
    'app/ux/GenericTextBox',
    'app/ux/GenericDisplayBox',
    'app/common/Ajax',
    'dojo/_base/config',
    'app/views/account/UserDetailPanel',
    'app/views/account/UserDetailEditPanel',
    'dojo/text!./templates/UserInfoPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		domConstruct, query, Global, on, dom, string, domStyle, domClass, TextBox, DisplayBox,Ajax,
		cfg,UserDetailPanel,UserDetailEditPanel, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	baseUrl: Global.baseUrl,
    	username: '',
    	usernameReal: '',
    	mobile: '',
    	email: '',
    	userNickName: '',
    	placeOfOrigin: '',
    	gender: '',
    	introducer: '',
    	address: '',
    	editing: false,
    	// html
    	usernameRealTrueHTML: '<span>真实姓名：</span><b>${usernameReal}</b>',
    	usernameRealFalseHTML: '<span>真实姓名：</span><span class="am-ft-gray">未认证</span>' +
    		'<span class="ui-aseparator">|</span><a href="'+Global.baseUrl+'/user/'+(cfg.authentication ? 'uploadID.htm' : 'certification.htm')+'">实名认证</a>',
    	medifyBtnHTML: '<a href="'+Global.baseUrl+'/user/changemobile.htm">${action}</a>',
    	saveBtnHTML: '<a href="javascript:void(0)">保存</a>',
    	blankHTML: '<span class="am-ft-gray ui-text-extra">未填写</span>',
    	saveDetail: function() {},
    	
    	_setUsernameAttr: function(value) {
    		var me = this;
    		this._set('username', value);
    		if (value) {
    			this.usernameNode.innerHTML = value;
    		} else {
    			var el = domConstruct.toDom('<div class="username-prompt">您还未设置用户名，注：用户名只能设置一次，不能再次修改。'+
    					'<p><a id="editusernamebtn" style="color: #0088cc;">点击修改</a>'+
    					'<a id="saveusernamebtn" style="color: #0088cc;display:none;">点击保存</a></p></div>');
    			domConstruct.place(el, this.msgNode);
    			on(dom.byId('editusernamebtn'), 'click', function() {
    				if (!me.userNameFld) {
    					me.userNameFld = new TextBox({
    						validates: [{
    							pattern: /.+/,
    							message: '请输入用户名'
    						}],
    						style: 'display: inline-block;margin-bottom: 4px;'
    					});
    					me.userNameFld.placeAt(me.userNameCtnNode);
    					me.userNameFld.textbox.focus();
    					this.style.display = 'none';
    					dom.byId('saveusernamebtn').style.display = 'block';
    				}
    			});
    			on(dom.byId('saveusernamebtn'), 'click', function(e) {
    				if (me.userNameFld.checkValidity()) {
    					var username = me.userNameFld.get('value');
    					Ajax.post(Global.baseUrl + '/user/username/set', {
    						user_name: username
    					}).then(function(response) {
    						if (response.success) {
    							me.usernameNode.innerHTML = username;
    							el.style.display = "none";
    							me.userNameFld.domNode.style.display= 'none';
    						}
    					});
    				}
    			});
    		}
    	},
    	
    	/*_setAddressAttr: function(value) {
    		this._set('address', value);
    		this.userAddressNode.innerHTML = value;
    	},*/
    	
    	_setUsernameRealAttr: function(value) {
    		this._set('usernameReal', value);
    		this.usernameRealNode.innerHTML = value ? 
    				string.substitute(this.usernameRealTrueHTML, {usernameReal: value}) : this.usernameRealFalseHTML;
    	},
    	
    	_setMobileAttr: function(value) {
    		this._set('mobile', value);
    		this.mobileNode.innerHTML = Global.encodeInfo(value, 3, 3);
    		var btn = domConstruct.toDom(string.substitute(this.medifyBtnHTML, {action: '修改'}));
    		domConstruct.place(btn, this.mobileNode.parentNode);
    	},
    	
    	_setEmailAttr: function(value) {
    		this._set('email', value);
    		if (value) {
    			this.emailNode.innerHTML = value;
    			this.emailLinkNode.innerHTML = '修改';
    		} else {
    			domStyle.set(this.emailNode, {
    				color: '#959595',
    				fontWeight: 'normal'
    			});
    			this.emailNode.innerHTML = '未绑定';
    			this.emailLinkNode.innerHTML = '绑定';
    		}
    	},
    	
    	templateString: template,
    	
    	render: function() {
    		var me = this;
    		me.detailPanel = new UserDetailPanel();
    		me.detailEditPanel = new UserDetailEditPanel();
    		me.detailPanel.placeAt(me.detailCtnNode);
    		me.detailEditPanel.domNode.style.display = 'none';
    		me.detailEditPanel.placeAt(me.detailCtnNode);
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	afterSave: function(saved) {
    		var me = this;
    		if (saved) {
    			var values = me.detailEditPanel.getValues();
    			me.detailPanel.setValues(values);
    			me.switchPanel();
    		}
    	},
    	
    	switchPanel: function() {
    		var me = this;
    		var editing = me.get('editing');
    		this.detailBtnNode.innerHTML = !editing ? '保存' : '修改';
    		var inPanel = editing ? me.detailPanel.domNode : me.detailEditPanel.domNode,
    			outPanel = !editing ? me.detailPanel.domNode : me.detailEditPanel.domNode;
    		me.flip(inPanel, outPanel, function() {
				Global.focusText();
			});
    		me.set('editing', !me.get('editing'));
    	},
    	
    	flip: function(inPanel, outPanel, callback) {
    		domClass.remove(outPanel, 'flipin');
			domClass.add(outPanel, 'flip flipout');
    		setTimeout(function() {
    			outPanel.style.display = 'none';
    			inPanel.style.display = 'block';
    			domClass.remove(inPanel, 'flipout');
        		domClass.add(inPanel, 'flip flipin');
        		if (callback) {
        			callback();
        		}
    	    }, 225);
    	},
    	
    	addLinsteners: function() {
    		var me = this;
    		on(this.detailBtnNode, 'click', function() {
    			if (!me.get('editing')) {
    				me.switchPanel();
    				var values = me.detailPanel.getValues();
    				me.detailEditPanel.setValues(values);
    			} else {
    				me.saveDetail();
    			}
    		});
    	},
    	
    	postCreate: function(){
    		var me = this;
    		me.render();
    		me.addLinsteners();
    	    this.inherited(arguments);
    	}
    });
});