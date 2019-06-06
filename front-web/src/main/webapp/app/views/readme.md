view template:

	define([
	    'dojo/_base/declare',
	    'dijit/_WidgetBase', 
	    'dijit/_TemplatedMixin',
	    'app/views/ViewMixin',
	    'dojo/text!./templates/changethis.html'
	], function(declare, _WidgetBase, _TemplatedMixin, ViewMixin, template){
	    return declare([_WidgetBase, _TemplatedMixin, ViewMixin], {
	    	templateString: template,
	    	postCreate: function() {
                var me = this;
                me.inherited(arguments);
            }
	    });
	});