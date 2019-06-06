define([
	'dojo/_base/declare',
	'dijit/_WidgetBase', 
	'dijit/_TemplatedMixin',
	'dojo/on',
	'dojo/text!./templates/GenericGridButton.html'
], function (declare, _WidgetBase, _TemplatedMixin, on, template) {
return declare([_WidgetBase, _TemplatedMixin], {
		text: '',
    	
    	templateString: template,
    	baseClass: 'genericButton',
    	
    	_setTextAttr: function(value) {
    		this.focusNode.innerHTML = value;
    	},

	postCreate: function() {
		this.inherited(arguments);
		if (this.params.onClick) {
			on(this.domNode, 'click', this.params.onClick);
		}
	}
    	
    });
});