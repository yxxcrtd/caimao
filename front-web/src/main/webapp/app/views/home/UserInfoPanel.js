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
    'dojo/dom-class',
    'dojo/dom-style',
    'app/ux/GenericProgressBar',
    'app/views/home/UserStatusPanel',
    'dojo/text!./templates/UserInfoPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, domConstruct, 
		query, Global, on, dom, domClass, domStyle, ProgressBar, UserStatusPanel, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	baseUrl: Global.baseUrl,
    	username: '',
    	bankNo: '',
    	email: '',
    	lastLoginIP: '',
    	lastLoginTime: '',
    	totalAsset: 0.00,
    	isBankCardFn: function() {},
    	isTrust: false,
    	usernameReal: '',
    	isTradePwd: false,
    	isBankCard: false,
    	avatar: '',
    	score: 0,
    	role: '',
    	tradePwdStrength: '',
    	userScore: '',
    	
    	// when call setter, manually modify the dom display
    	_setEmailAttr: function(value) {
    		this._set('email', value); //call watch
    		//this.emailNode.innerHTML = value ? value : '<a href="'+Global.baseUrl+'/user/changemail.htm">绑定</a>';
    	},
    	
    	_setScoreAttr: function(value) {
    		if (this.pwdProgressBar) {
    			this.pwdProgressBar.set('value', value / 100);
    		}
    	},
    	
    	_setAvatarAttr: function(value) {
    		this._set('avatar', value); //call watch
    		if (value) {
    			this.avatarNode.src = value;
    		}
    	},
    	
    	_setUsernameAttr: function(value) {
    		this._set('username', value);
    		this.usernameNode.innerHTML = Global.greet(new Date()) + '，' + value;
    	},
    	
    	_setLastLoginIPAttr: function(value) {
    		this._set('lastLoginIP', value);
    		this.lastLoginIPNode.innerHTML = value;
    	},
    	
    	_setLastLoginTimeAttr: function(value) {
    		this._set('lastLoginTime', value);
    		this.lastLoginTimeNode.innerHTML = value;
    	},
    	
    	_setTotalAssetAttr: function(value) {
    		this._set('totalAsset', value);
    		var parts = (value + '').split('.');
    		this.totalAssetIntNode.innerHTML = parts[0];
    		this.totalAssetDecNode.innerHTML = '.' + parts[1]; //TODO format the dot
    	},
    	
    	_setUserScoreAttr: function(value) {
    		this.userScoreNode.innerHTML = value;
    	},
    	
    	templateString: template,
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    		this.afterSet(values);
    	},
    	
    	afterSet: function(values) {
    		var me = this,
    			role = me.get('role');
    		if (role == '9') {
    			me.userStatusPanel.destroy();
    			me.userStatusPanel = null;
    		} // planner
    		if (me.userStatusPanel) {
    			me.userStatusPanel.domNode.style.display = 'block';
    			me.userStatusPanel.setValues(values);
    		}
    	},
    	
    	render: function() {
    		var me = this;
    		me.avatarOverlay = domConstruct.create('a', 
    				{innerHTML: '更换头像', className: 'image-overlay-extra',
    					href: Global.baseUrl+'/user/avatar.htm'}, me.avatarNode, 'after');
    		me.pwdProgressBar = new ProgressBar({
    			label: '账户安全：',
    			labelStyle: {
    				'fontSize': '12px'
    			}
    		});
    		me.avatarOverlay.style.display = 'none';
    		me.pwdProgressBar.placeAt(me.scoreNode);
    		me.userStatusPanel = new UserStatusPanel({}, me.userStatusCtnNode);
    		me.userStatusPanel.domNode.style.display = 'none';
    	},
    	
    	addLinsters: function() {
    		var me = this;
    		on(me.avatarCtnNode, 'mouseenter', function() {
    			me.avatarOverlay.style.display = 'block';
    		});
    		on(me.avatarCtnNode, 'mouseleave', function() {
    			me.avatarOverlay.style.display = 'none';
    		});
    	},
    	
    	postCreate: function(){
    		var me = this;
    		me.render();
    		me.addLinsters();
    	    var domNode = this.domNode;
    	    this.inherited(arguments);
    	}
    });
});