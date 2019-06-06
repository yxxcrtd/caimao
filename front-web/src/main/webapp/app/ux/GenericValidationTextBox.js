define(['dojo/_base/declare',
        "dijit/form/ValidationTextBox",
        'app/ux/GenericTooltip'
        ],
function (declare,ValidationTextBox,Tooltip){	
	return declare(ValidationTextBox,{
		
		displayMessage: function(/*String*/ message){
			// summary:
			//		Overridable method to display validation errors/hints.
			//		By default uses a tooltip.
			// tags:
			//		extension
			if(message){
				Tooltip.show(message, this.domNode, this.tooltipPosition, !this.isLeftToRight());
			}else{
				Tooltip.hide(this.domNode);
			}
		}
	});	
});