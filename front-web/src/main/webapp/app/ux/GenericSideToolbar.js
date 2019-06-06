define([ 
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/views/ViewMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/dom-construct',
    'app/common/Position',
    'app/common/Fx',
    'dojo/text!./templates/GenericSideToolbar.html'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin,
		Global, on, domClass, domStyle, domConstruct, Position, Fx, template) {
	return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
		templateString: template,
		
		items: [],
		
		_setItemsAttr: function(value) {
			var i = 0, len = value.length;
			for(; i < len; i++) {
				this.addItem(value[i]);
			}
		},
		
		setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	addItem: function(item) {
    		if (item.xtype == 'totop') {
    			var itemEl = domConstruct.toDom('<div class="sidetoolbar-item" style="visibility:hidden;">' +
                    '<i class="fa totopicon s-icon">&#xf077;</i><span class="totoptext s-text">返回顶部</span></div>');
    			domConstruct.place(itemEl, this.domNode);
    			on(itemEl, 'click', function() {
    				Fx.scrollToY(0, null, 'easeInOutSine');
    			});
    			var scrollTop = 0;
    			on(window, 'scroll', function(e) {
    				var top = document.body.scrollTop + document.documentElement.scrollTop,
    					pos = 100;
    				if (scrollTop < pos && top >= pos) {
    					Fx.show(itemEl);
    				} else if (scrollTop >= pos && top < pos) {
    					Fx.hide(itemEl);
    				}
    				scrollTop = top;
    			});
    		}
            else if (item.xtype == 'home' && location.pathname !== item.url) {
                var itemEl = domConstruct.toDom('<div class="sidetoolbar-item"><i class="fa">&#xf015;</i></div>');
                domConstruct.place(itemEl, this.domNode);
                on(itemEl, 'click', function(e) {
                    location.href = item.url;
                });
            }
            else if (item.xtype == 'qq') {
                var itemEl = domConstruct.toDom('<a href="http://wpa.b.qq.com/cgi/wpa.php?ln=1&uin='+this.QQ+'" target="_blank">' +
                    '<div class="sidetoolbar-item">' +
                    '<i class="fa s-icon">&#xf1d6;</i><span class="totoptext s-text">联系客服</span></div></a>');
                domConstruct.place(itemEl, this.domNode);
            }
            else if (item.xtype == 'wechat') {
                var itemEl = domConstruct.toDom('<div class="sidetoolbar-item">' +
                    '<img src="'+Global.baseUrl+'/app/resources/image/ewm.png" />' +
                    '<i class="fa">&#xf1d7;</i></div>');
                domConstruct.place(itemEl, this.domNode);
                var imgEl = query('img', itemEl)[0];
                if (has("ie") <= 8) {
                    domStyle.set(imgEl, {
                        visibility: 'hidden',
                        zoom: 3,
                        left: '-120px'
                    });
                    on(itemEl, 'mouseenter', function(e) {
                        domStyle.set(imgEl, {
                            visibility: 'visible'
                        });
                    });
                    on(itemEl, 'mouseleave', function(e) {
                        domStyle.set(imgEl, {
                            visibility: 'hidden'
                        });
                    });
                }
            }
    	},
		
		postCreate: function() {
    		var me = this;
    	    this.inherited(arguments);
    	}
	});
});