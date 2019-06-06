define([ 
    'dojo/_base/declare',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'app/common/Global',
    'dojo/on',
    'dojo/mouse',
    'dojo/dom-construct',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/dom-attr',
    'app/ux/GenericTooltip',
    'dijit/focus'
], function (declare, TextBox, Button, Global, on, mouse, domConstruct, domClass, domStyle, domAttr, Tooltip, focusUtil) {
	return declare([TextBox], {
		
		validates: [{
			pattern: /.+/,
			message: '请输入短信验证码'
		}],
		
		limitRegex: /[\d\w]/,
		
		maxLength: 6,
		
		render: function() {
			var me = this;
			me.sendBtn = new Button({
				label: '发送验证码',
				style: {
					position: 'absolute',
					marginLeft: 0,
					left: '115px',
					marginTop: '-25px'
				},
				textStyle: {
    				color: '#666',
    				fontWeight: 'normal',
					fontSize: '12px',
					padding: '0 15px'
    			},
    			innerStyle: {
    				backgroundColor: '#F8F8F8',
    				border: '1px solid #C4C4C4',
    				padding: '1px'
    			}
			});
			me.sendBtn.placeAt(me.domNode);
            on(me.sendBtn, 'click', function() {
                me.countdown(60);
            });
        },
		
		countdown: function(time) {
			var me = this,
				target = me.sendBtn.containerNode,
				label = '重发验证码';
			me.set('isFirst', false);
			me.sendBtn.set('disabled', true);
			focusUtil.focus(me.textbox);
			Global.loop(time, function(t) {
				target.innerHTML = '（' + t +'）' + label;
			}, function() {	
				target.innerHTML = label;
				me.sendBtn.set('disabled', false);
			});
		},
		
		showError: function(message) {
			Tooltip.show(message, this.sendBtn.domNode);
		},
		
		postCreate: function() {
    		var me = this;
    		me.render();
    	    this.inherited(arguments);
    	}
	});
});