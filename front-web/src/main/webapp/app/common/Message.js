/**
 * generic message show, slide in the message box
 * */
define([
    'dojo/dom-construct',
    'dojo/fx',
	'dojo/_base/fx',
    'app/ux/GenericMessageBox'
], function(domConstruct, coreFx, baseFx, MessageBox) {
	var messageBox = new MessageBox({
		id: 'genericMessageBox'
	});
	messageBox.placeAt(document.body);
	return {
		msg: function(title, message) {
	    	var msgNode = domConstruct.toDom('<div class="genericMessageBoxMsg"><h3>' + title + '</h3><p>' + message + '</p></div>');
	    	domConstruct.place(msgNode, messageBox.domNode, 'last');
	    	coreFx.chain([
	    	    baseFx.fadeIn({
	    	    	node: msgNode,
	    	    	duration: 800
	    	    }),
	    	    baseFx.fadeOut({
	    	    	node: msgNode,
	    	    	delay: 300,
	    	    	duration: 800,
	    	    	onEnd: function(node) {
	    	    		domConstruct.destroy(node);
	    	    	}
	    	    })
	    	]).play();
	    }
	}
});