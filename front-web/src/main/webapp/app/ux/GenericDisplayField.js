define(['dojo/_base/declare',
         'dijit/form/_FormWidget',
         'dojo/text!./templates/GenericDisplayField.html'], 
function (declare, _FormWidget, template) {
	return declare(_FormWidget, {
		templateString: template,
		style: 'width: 100%; padding: 1px 0;',

		postCreate: function() {
			this.inherited(arguments);
			this.focusNode.disabled = true;
		}
	});
});