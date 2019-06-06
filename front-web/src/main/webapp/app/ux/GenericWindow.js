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
    'app/common/Fx',
    'dojo/text!./templates/GenericWindow.html'
], function(declare, _WidgetBase, _TemplatedMixin, domStyle, on, mouse, 
		DialogUnderlay, Global, query, focusUtil, dom, domConstruct, string,domClass, Position, Moveable, Fx, template){
    return declare([_WidgetBase, _TemplatedMixin], {
    	
    	templateString: template,
    	
    	alertClass:'',
    	contentClass: '',
    	msg: '',
        width: '',
    	title: '',
    	closeAction: 'hide',
    	confirmText: '',
    	confirmAndCancel: false,
    	onConfirm: function() {},
    	onCancel:function() {},
    	confirmTextTpl: '<div class="con-success clearfix"> ' +
		'<div class="cm-left successIcon-lside"><i class="ui-icon icon-middle-doubt"></i></div>'+
		'<div class="cm-left successText-rside">'+
		'<p class="layout02"><b>${confirmText}</b></p>'+
		'</div></div>',
    	confirmAndCancelTpl: '<p class="am-mt-5 am-ft-center">'+
    		'<a data-dojo-attach-point="confirmBtnNode" href="javascript:void(0)" class="dijitButton enterbutton ui-button ui-button-morange"><b class="ui-button-text am-ft-white am-ft-md">确定</b></a>'+
    		'<a data-dojo-attach-point="cancelBtnNode" href="javascript:void(0)" class="ui-button ui-button-mgray"><b class="ui-button-text am-ft-white am-ft-md">取消</b></a>'+
    		'</p>',
    	
    	_setAlertClassAttr:function(value){
    		this._set('alertClass', value);
    		domClass.add(this.alertNode, value);	
    	},

        _setWidthAttr: function(value) {
            domStyle.set(this.domNode, {
                width: value + 'px'
            });
        },

        _setHeightAttr: function(value) {
            domStyle.set(this.contentNode, {
                height: value + 'px',
                'overflowY': 'auto'
            });
        },
    		
    	_setContentClassAttr: function(value){
    		this._set('contentClass', value);
    		domClass.add(this.contentNode, value);		
    	},
    	
    	_setMsgAttr: function(value) {
    		this._set('msg', value);
    		this.msgNode.innerHTML = value;
    	},
    	
    	_setTitleAttr: function(value) {
    		this._set('title', value);
            domStyle.set(this.titleCtnNode, {
                display: 'block'
            });
    		this.titleNode.innerHTML = value;
    	},
    	
    	show: function() {
    		var me = this;
    		me.enterBtns = query('.dijitButton.enterbutton').filter(function(i) {
    			return i != me.confirmBtnNode;
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
            //Fx.shakeX(me.domNode);
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

		reload: function(){
			location.reload();
		},
    	
    	addListeners: function() {
    		var me = this;
    		on(me.closeNode, 'click', function() {
    			me[me.get('closeAction')]();
    		});
    		
    		if (me.confirmBtnNode) {
    			on(me.confirmBtnNode, 'click', function() {
        			me.onConfirm.call();
        		});
    		}
    		
    		if (me.cancelBtnNode) {
    			on(me.cancelBtnNode, 'click', function() {
        			me.onCancel.call();
        		});
    		}
    	},
    	
    	buildRendering: function(){
    		var templateString = this.templateString,
    			confirmTextHTML = '',
    			confirmAndCancelHTML = '';
    		
    		if (this.get('confirmText')) {
    			//confirmTextHTML = string.substitute(this.confirmTextTpl, {'confirmText': this.confirmText});
    			confirmTextHTML = this.confirmText;
    		}
    		
    		if (this.get('confirmAndCancel')) {
    			confirmAndCancelHTML = this.confirmAndCancelTpl;
    		}
    		templateString = string.substitute(templateString, {'confirmTextHTML': confirmTextHTML, 'confirmAndCancelHTML': confirmAndCancelHTML});
    		this.templateString = templateString;
    		
			this.inherited(arguments);
		},
    	
    	postCreate: function(){
    		var me = this;
    		
    		domStyle.set(me.domNode, {
    			position: 'absolute',
    			'zIndex': '10000',
    			'padding': '4px'
    		});
    		
    		new Moveable(me.domNode, {
    			handle: me.titleCtnNode
    		});
    		
    		me.addListeners();
    		
    		me.inherited(arguments);
    	}
    });
});