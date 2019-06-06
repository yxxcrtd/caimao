define([ 
    'dojo/_base/declare',
    'dijit/form/DateTextBox',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/mouse',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/text!./templates/GenericDateBox.html'
], function (declare, DateTextBox, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, on, mouse, domClass, domStyle, template) {
	return declare([DateTextBox, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
		templateString: template,
		
		// config params
		cls: 'ui-input-date-extra',
		_setClsAttr: {node: 'domNode', type: 'class'},
		constraints: {datePattern: 'yyyy-MM-dd'},
		
		// bind event handlers
		addListeners: function() {
			var me = this;		
		},
		
		postCreate: function() {
    		var me = this;
    		me.addListeners();
    	    this.inherited(arguments);
    	}
	});
});