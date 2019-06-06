define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/views/ViewMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/dom-construct',
    'app/common/Position',
    'app/common/Fx',
    'dojo/text!./templates/GenericLoader.html'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, Global, on, domClass, domStyle, domConstruct, Position, Fx, template) {
	return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {

		templateString: template,
        type: 'loader1',
        style: 'position: absolute',

        show: function(el) {
            Position.center(this.domNode, el || document.body);
        },

        hide: function() {
            Fx.hide(this.domNode);
        },

        render: function() {
            var me = this;
        },
		
		postCreate: function() {
    		var me = this;
            me.render();
    	    this.inherited(arguments);
    	}
	});
});