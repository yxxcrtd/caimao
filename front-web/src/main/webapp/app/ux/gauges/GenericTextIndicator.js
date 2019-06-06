define([ 
    'dojo/_base/declare',
    'dojox/gauges/TextIndicator',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/mouse',
    'dojo/dom-class',
    'dojo/dom-style'
], function (declare, TextIndicator, _WidgetBase, 
		Global, on, mouse, domClass, domStyle) {
	return declare([TextIndicator, _WidgetBase], {
		
		draw: function(group, /*Boolean?*/ dontAnimate){
			// summary:
			//		Override of dojox.gauges._Indicator.draw
			var v = this.value;
			var label = this.label;
			
			
			var x = this.x ? this.x : 0;
			var y = this.y ? this.y : 0;
			var align = this.align ? this.align : "middle";
			if(!this.shape){
				this.shape = group.createText({
					x: x,
					y: y,
					text: parseFloat(v) + '%',
					align: align
				});
			}else{
				this.shape.setShape({
					x: x,
					y: y,
					text: parseFloat(v) + '%',
					align: align
				});
			}
			this.shape.setFill(this.color);
			if (this.font) this.shape.setFont(this.font);
			
		},
		
		postCreate: function() {
    		var me = this;
    	    this.inherited(arguments);
    	}
	});
});