define([
	'dojo/_base/declare',
	'dijit/_WidgetBase', 
	'dijit/_TemplatedMixin',
	'dojo/text!./templates/GenericInput.html'
], function (declare, _WidgetBase, _TemplatedMixin, template) {
return declare([_WidgetBase, _TemplatedMixin], {
    	templateString: template,
    	baseClass: 'genericInput',

	postCreate: function() {
		this.inherited(arguments);
	}
    	
    });
});