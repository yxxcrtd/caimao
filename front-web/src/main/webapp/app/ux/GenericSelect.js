define([ 'dojo/_base/declare',
         'dijit/form/Select',
         'dojo/dom-style'], 
function (declare, Select, domStyle) {
	return declare(Select, {
		style: 'width: 100%',
		textStyle: '',
		
		_setTextStyleAttr: function(value) {
			domStyle.set(this.textDirNode, value);
		}
	});
});