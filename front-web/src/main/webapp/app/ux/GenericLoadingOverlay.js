define([ 
    'dojo/_base/declare',
    'dojox/widget/Standby',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/dom-class',
    'dojo/dom-style'
], function (declare, Standby, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, on, domClass, domStyle) {
	return declare([Standby, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
		
		color: '#666666',
		
		templateString:
			"<div>" +
				"<div style=\"display: none; opacity: 0; z-index: 9999; " +
					"position: absolute; cursor:wait;\" dojoAttachPoint=\"_underlayNode\"></div>" +
				"<i class=\"fa fa-spin fa-3x\" " +
					"style=\"opacity: 0; display: none; z-index: -10000; color: #fff; text-shadow: 0px 0px 2px #000;" +
					"position: absolute; top: 0px; left: 0px; cursor:wait;\" "+
					"dojoAttachPoint=\"_imageNode\">&#xf110;</i>" +
				"<div style=\"opacity: 0; display: none; z-index: -10000; position: absolute; " +
					"top: 0px;\" dojoAttachPoint=\"_textNode\"></div>" +
			"</div>",
		
		postCreate: function() {
    		var me = this;
    	    this.inherited(arguments);
    	}
	});
});