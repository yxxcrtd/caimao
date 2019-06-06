define([
	'dojo/_base/declare',
	'dijit/_WidgetBase', 
	'dijit/_TemplatedMixin',
	'dojo/dom-construct',
	'dojo/fx',
	'dojo/_base/fx',
	'dojo/text!./templates/GenericMessageBox.html'
], function (declare, _WidgetBase, _TemplatedMixin, domConstruct, coreFx, baseFx, template) {
return declare([_WidgetBase, _TemplatedMixin], {
    	templateString: template,
    	baseClass: 'genericMessageBox',
    	
    msg: function(title, message) {
    	var msgNode = domConstruct.toDom('<div class="genericMessageBoxMsg"><h3>' + title + '</h3><p>' + message + '</p></div>');
    	domConstruct.place(msgNode, this.domNode, 'last');
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
    },

	postCreate: function() {
		this.inherited(arguments);
	}
    	
    });
});