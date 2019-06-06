define([ 
    'dojo/_base/declare',
    'dijit/form/Select',
    'dijit/_TemplatedMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/mouse',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/text!./templates/GenericSelection.html'
], function (declare, Select, _TemplatedMixin, Global, on, mouse, domClass, domStyle, template) {
	return declare([Select, _TemplatedMixin], {
		templateString: template,
		
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