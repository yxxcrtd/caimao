define([ 
    'dojo/_base/declare',
    'dijit/form/Button',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/dom-class',
    'dojo/dom-style',
    'app/ux/GenericTooltip',
    'dojo/has',
    'dojo/_base/sniff',
    'dojo/text!./templates/GenericButton.html'
], function (declare, Button, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, on, domClass, domStyle, Tooltip, has, sniff, template) {
	return declare([Button, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
		templateString: template,
		
		// config params
		cls: 'ui-button ui-button-extra',
		_setClsAttr: {node: 'domNode', type: 'class'},
		baseUrl: Global.baseUrl,
        style: '',
		textStyle: '',
		innerStyle: '',
		leftOffset: 0,
		color: '',
		hoverColor: '',
		width: '',
		height: 36,
		disabledMsg: '',
        disabledMsgs: [],
		enter: false,
        position: '',
		
		_setLeftOffsetAttr: function(value) {
			this._set('leftOffset', value);
			domStyle.set(this.domNode, {
				marginLeft: value + 'px'
			});
		},

        _setStyleAttr: function(value) {
            this._set('style', value);
            domStyle.set(this.domNode, value);
        },

        _setPositionAttr: function(value) {
            domStyle.set(this.domNode, {
                margin: '0 auto',
                display: 'block',
                'textAlign': value
            });
        },
		
		_setEnterAttr: function(value) {
			this._set('enter', value);
			domClass.add(this.domNode, 'enterbutton');
		},
		
		_setWidthAttr: function(value) {
			this._set('width', value);
			domStyle.set(this.innerNode, {
				width: (value - 10) + 'px' // the innerNode padding total is 10 px
			});
			domStyle.set(this.containerNode, {
				padding: '0px'
			});
		},
		
		_setHeightAttr: function(value) {
			this._set('height', value);
			domStyle.set(this.containerNode, {
				height: (value - 8) + 'px', // the padding total is 8 px
				lineHeight: (value - 8) + 'px' // center
			});
		},
		
		_setColorAttr: function(value) {
			this._set('color', value);
			this.innerNode.style.borderColor = value;
			this.innerNode.style.backgroundColor =value;
		},
		
		_setTextStyleAttr: function(value) {
			this._set('textStyle', value);
			domStyle.set(this.containerNode, value);
		},
		
		_setInnerStyleAttr: function(value) {
			this._set('innerStyle', value);
			domStyle.set(this.innerNode, value);
		},
		
		_setDisabledAttr: function(value) {
			this.inherited(arguments);
			domClass[value ? 'add' : 'remove'](this.containerNode, 'ui-button-disabled');
			if (!value) {
				this.set('disabledMsg', '');
                this.set('disabledMsgs', []);
			}
		},
		
		// bind event handlers
		addListeners: function() {
			var me = this;
			if(me.params && me.params.handler) {
				on(me, 'click', me.params.handler);
			}
			on(this, 'blur', function() {
				Tooltip.hide(this.innerNode);
			});
			
			// fix ie8 
            if (has("ie") <= 8) {
                on(me, 'click', function(e) {
                    if (me.domNode.parentNode.tagName.toLowerCase() === 'a') {
                        me.domNode.parentNode.click();
                    }
                    if (me.domNode.parentNode.parentNode.tagName.toLowerCase() === 'a') {
                        me.domNode.parentNode.parentNode.click();
                    }
                });
            }
			
			on(this, 'mouseenter', function() {
				var color = this.get('hoverColor');
				if (color && !this.get('disabled')) {
					//this.innerNode.style.borderColor = color;
					this.innerNode.style.backgroundColor = color;
				}
				// button disabled can show the reason
				var disabledMsg = this.get('disabledMsg') || (this.get('disabledMsgs')[0] ? this.get('disabledMsgs')[0][1]: '');
				if (this.get('disabled') && disabledMsg) {
					Tooltip.show(disabledMsg, this.innerNode);
				}
			});
			
			on(this, 'mouseleave', function() {
				var color;
				if (color = this.get('color')) {
					//this.innerNode.style.borderColor = color;
					this.innerNode.style.backgroundColor = color;
				}
				Tooltip.hide(this.innerNode);
			});
		},
		
		loading: function(disabled) {
			this.set('disabled', disabled);
			domStyle.set(this.loadingNode, 'display', disabled ? 'block' : 'none');
		},

        addDisabledMsg: function(obj) {
            this.disabledMsgs.push(obj);
            this.set('disabled', true);
        },

        removeDisabledMsg: function(id) {
            var i = 0, len = this.disabledMsgs.length;
            for (;i < len; i++) {
                if (this.disabledMsgs[i][0] == id) {
                    this.disabledMsgs.pop(this.disabledMsgs[i]);
                    break;
                }
            }
            if (this.disabledMsgs.length == 0) {
                this.set('disabled', false);
            }
        },
		
		postCreate: function() {
    		var me = this;
    		me.addListeners();
    		
    	    this.inherited(arguments);
    	}
	});
});