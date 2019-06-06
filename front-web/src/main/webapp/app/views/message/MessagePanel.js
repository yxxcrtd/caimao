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
    'app/ux/GenericLoadingOverlay',
    'dojo/_base/lang',
    'dojo/dom-class',
    'dojo/text!./templates/MessagePanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		domConstruct, query, Global, on, dom, string, LoadingOverlay, lang, domClass, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,	
    	baseUrl: Global.baseUrl,
    	items: '',
    	start: 0,
    	limit: 0,
    	
    	_setItemsAttr: function(value) {
    		value = value || [];
    		var me = this,
    			html = '',
    			i = 0,
    			len = value.length,
    			itemTpl = '<li class="list-notice-item" ${style} data-id=${pushMsgId} data-type=${pushModel}>' +
    				'<i class="icon-type fa ${icon}"></i>' +
    				'<span class="info" style="width:500px;margin-left: -71px;">${pushMsgTitle}</span>' +
    				'<span class="date" style="width: 145px;">${createDatetime}</span></li>';
    		domConstruct.empty(me.listNode);
    		for (; i < len; i++) {
    			var icon = parseInt(value[i]['isRead']) ? 'fa-folder-open' : 'fa-folder';
    			var style = parseInt(value[i]['isRead']) ? '' : 'style="font-weight: bold"';
    			html += string.substitute(itemTpl, lang.mixin(value[i], {icon: icon, style: style}));
    		}
    		var contentEl = domConstruct.toDom(html);
    		domConstruct.place(contentEl, me.listNode);
    	},
    	 
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	mask: function() {
    		this.loadingOverlay.show();
    	},
    	
    	unmask: function() {
    		this.loadingOverlay.hide();
    	},
    	
    	setReaded: function(el) {
    		el.style.fontWeight = 'normal';
    		var iconEl = query('i', el)[0];
    		domClass.remove(iconEl, 'fa-folder');
    		domClass.add(iconEl, 'fa-folder-open');
    	},
    	
    	render: function() {
    		var me = this;
    		me.loadingOverlay = new LoadingOverlay({target: me.domNode});
    		document.body.appendChild(me.loadingOverlay.domNode);
    		me.loadingOverlay.startup();
    	},
    	
    	postCreate: function() {
    		var me = this;
    	    me.render();
    	    this.inherited(arguments);
    	}
    });
});