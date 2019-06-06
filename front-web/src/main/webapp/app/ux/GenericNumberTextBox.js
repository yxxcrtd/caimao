define([ 'dojo/_base/declare',
         'dojo/dom-style',
         'dijit/form/NumberTextBox' ], 
function (declare, domStyle, NumberTextBox) {
	return declare(NumberTextBox, {
		label: 'Unknown: ',
		invalidMessage: 'Please enter a numeric value.',
		style: 'width: 100%',
		
		postCreate: function() {
			this.inherited(arguments);
			if(this.maxlength){
				domStyle.set(this.domNode, 'maxlength', this.maxlength);
			}
		}
	});
});