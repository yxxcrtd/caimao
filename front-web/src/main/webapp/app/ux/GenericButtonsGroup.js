define([ 'dojo/_base/declare',
         'app/ux/GenericButton',
         'dojox/layout/TableContainer'], 
function (declare, Button, TableContainer) {
	return declare(TableContainer, {
		style: 'width: 100%',
		cols: 4,
		showLabels: false,
        buttons: [],
        
        postMixInProperties: function(){
			this.inherited(arguments);
		},
		
		postCreate: function() {
			this.inherited(arguments);
			var i = 0,
				len = this.buttons.length;
			for (; i < len; i++) {
				this.addChild(new Button(this.buttons[i]));
			}
		}
	});
});