define([ 
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/on',
    'dijit/registry',
    'dojo/mouse',
    'dojo/dom-class',
    'dojo/dom-style',
    'app/ux/GenericDateBox',
    'app/common/Date',
    'dojo/text!./templates/GenericDateRange.html'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, on, registry, mouse, domClass, domStyle, DateBox, DateUtil, template) {
	return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
		templateString: template,
		
		startDate: new Date(),
		endDate: new Date(),
		leftOffset: 0,
		
		_setLeftOffsetAttr: function(value) {
			this._set('leftOffset', value);
			domStyle.set(this.domNode, {
				marginLeft: value + 'px'
			});
		},
		
		_setStartDateAttr: function(value) { // value should be date obj
			this._set('startDate', value);
			this.startDateFld.set('value', value);
			this.endDateFld.constraints.min = value;
		},
		
		_setEndDateAttr: function(value) {
			this._set('endDate', value);
			this.endDateFld.set('value', value);
			this.startDateFld.constraints.max = value;
		},
		
		render: function() {
			var me = this;
		},
		
		getData: function() {
			return {
				'start_date': DateUtil.format(this.startDateFld.get('value')),
				'end_date': DateUtil.format(this.endDateFld.get('value'))
			};
		},
		
		setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
		
		addListeners: function() {
			var me = this;
			on(me.startDateFld, 'change', function(value) {
				me.endDateFld.constraints.min = value;
			});
			on(me.endDateFld, 'change', function(value) {
				me.startDateFld.constraints.max = value;
			});
		},
		
		postCreate: function() {
    		var me = this;
    		me.render();
    		me.addListeners();
    	    this.inherited(arguments);
    	}
	});
});