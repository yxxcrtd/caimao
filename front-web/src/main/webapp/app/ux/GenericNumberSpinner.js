define([ 'dojo/_base/declare',
         'dojo/dom-style',
         'dijit/form/NumberSpinner',
         'app/ux/GenericTextBox',
         'app/ux/GenericTooltip',
         'dojo/dom-class',
         'dojo/text!./templates/Spinner.html'], 
function (declare, domStyle, NumberSpinner, TextBox, Tooltip, domClass, template) {	
	return declare([NumberSpinner, TextBox], {
		templateString: template,
		cssStateNodes: {
			'upArrowNode': 'dijitUpArrowButton',
			'downArrowNode': 'dijitDownArrowButton',
			'inputFieldNode': 'dijitInputField'
		},
		inputStyle: '',
		inputCls: 'ui-numberspinner-extra',
		
		_setInputStyleAttr: function(value) {
			domStyle.set(this.inputFieldNode, value);
		},
		
		_setInputClsAttr: function(value) {
			domClass.add(this.inputFieldNode, value);
		}
		
//		displayMessage: function(/*String*/ message){
//			// summary:
//			//		Overridable method to display validation errors/hints.
//			//		By default uses a tooltip.
//			// tags:
//			//		extension
//			if(message){
//				Tooltip.show(message, this.domNode, this.tooltipPosition, !this.isLeftToRight());
//			}else{
//				Tooltip.hide(this.domNode);
//			}
//		}

	});
});