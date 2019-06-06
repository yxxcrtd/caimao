define([ 
    'dojo/_base/declare',
    'dojox/gauges/AnalogArcIndicator',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/mouse',
    'dojo/dom-class',
    'dojo/dom-style'
], function (declare, AnalogArcIndicator, _WidgetBase, 
		Global, on, mouse, domClass, domStyle) {
	return declare([AnalogArcIndicator, _WidgetBase], {
		
		// not common, if not suit, please change
		_createArc: function(val) {
			if(this.shape){
				var startAngle = this._gauge._mod360(this._gauge.endAngle);
				var a = this._gauge._getRadians(this._gauge._getAngle(val));
				var sa = this._gauge._getRadians(startAngle);

//				if (this._gauge.orientation == 'cclockwise'){
//					var tmp = a;
//					a = sa;
//					sa = tmp;
//				}

				var arange;
				var big = 0;
				if (sa<=a)
					arange = a-sa;
				else
					arange = 2*Math.PI+a-sa;
				if(arange>Math.PI){big=1;}
				
				var cosa = Math.cos(a);
				var sina = Math.sin(a);
				var cossa = Math.cos(sa);
				var sinsa = Math.sin(sa);
				var off = this.offset + this.width;
				var p = ['M'];
				p.push(this._gauge.cx+this.offset*sinsa);
				p.push(this._gauge.cy-this.offset*cossa);
				p.push('A', this.offset, this.offset, 0, big, 1);
				p.push(this._gauge.cx+this.offset*sina);
				p.push(this._gauge.cy-this.offset*cosa);
				p.push('L');
				p.push(this._gauge.cx+off*sina);
				p.push(this._gauge.cy-off*cosa);
				p.push('A', off, off, 0, big, 0);
				p.push(this._gauge.cx+off*sinsa);
				p.push(this._gauge.cy-off*cossa);
				p.push('z');
				this.shape.setShape(p.join(' '));
				this.currentValue = val;
			}
		},
		
		postCreate: function() {
    		var me = this;
    	    this.inherited(arguments);
    	}
	});
});