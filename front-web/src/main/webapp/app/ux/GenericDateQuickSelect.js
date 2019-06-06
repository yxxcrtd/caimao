define([ 
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/mouse',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/query',
    'dojo/dom-attr',
    'app/common/Date',
    'dojo/dom-construct',
    'dojo/text!./templates/GenericDateQuickSelect.html',
    'dojo/NodeList-dom'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, on, mouse, domClass, domStyle, query, domAttr, DateUtil, domConstruct, template) {
	return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
		templateString: template,
		
		activeIndex: 0,
		style: '',
		
		items: '',
		disableToday: false,
		
		_setStyleAttr: function(value) {
			this._set('style', value);
			domStyle.set(this.domNode, value);
		},
		
		addListeners: function() {
			var me = this;
			me.items.on('click', function(e) {
				me.select(me.items.indexOf(this));
			});
			if (me.get('onClick')) {
				me.items.on('click', function(e) {
					me.params.onClick.call(this, e);
				});
			}
		},
		
		select: function(index) {
			query('.active', this.domNode).removeClass('active');
			query('a', this.items[index]).addClass('active');
			this.set('activeIndex', index);
		},
		
		getData: function() {
			var dateStep = domAttr.get(this.items[this.activeIndex], 'data-date-step'),
				dateUnit = domAttr.get(this.items[this.activeIndex], 'data-date-unit'),
				today = new Date(),
				startDate = DateUtil.add(today, dateUnit, parseInt(dateStep));
			return {
				'start_date': DateUtil.format(startDate),
				'end_date': DateUtil.format(today)
			};
		},
		
		postCreate: function() {
    		var me = this;
    		if (me.get('disableToday')) {
    			domConstruct.destroy(query('.list-filter-item', me.domNode)[0]);
    		}
    		me.items = query('.list-filter-item', me.domNode);
    		me.select(me.get('activeIndex'));
    		me.addListeners();
    	    this.inherited(arguments);
    	}
	});
});