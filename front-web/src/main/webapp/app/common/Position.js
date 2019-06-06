define([
	'dojo/window',
	'dojo/dom-geometry',
	'dojo/dom-style',
    'dojo/dom-class',
	'dojo/has',
	'dijit/place'
], function(winUtils, domGeometry, domStyle, domClass, has, place) {
	return {
		center: function(el, body) {
			
			var viewport = body ? domGeometry.position(body, true) : winUtils.getBox(document),
				bb = domGeometry.position(el, true),
				l = Math.floor((viewport.x || viewport.l || 0) + (viewport.w - bb.w) / 2),
				t = Math.floor((viewport.y || viewport.t || 0) + (viewport.h - bb.h) / 2);
            if (body) {
                t = Math.floor((viewport.h - bb.h) / 2);
            }
			domStyle.set(el, {
				left: l + 'px',
				top: t + 'px'
			});

            if (body) {
                domStyle.set(body, {
                    position: 'relative'
                });
                if (body.style.height) {
                    domStyle.set(el, {
                        position: 'absolute'
                    });
                }
            }
			return {l:l, t:t};
		},
		
		screenCenter: function(el) {
			var w = domGeometry.position(document.body).w,
				h = window.innerHeight || document.documentElement.clientHeight,
				bb = domGeometry.position(el, true),
				l = (w - bb.w) / 2,
				t = (h - bb.h) / 2 + (document.body.scrollTop + document.documentElement.scrollTop);
			domStyle.set(el, {
				left: l + 'px',
				top: t + 'px',
				postion: 'absolute'
			});
		},
		
		topLeft: function(el, body) {
			var viewport = body ? domGeometry.position(body, true) : winUtils.getBox(document),
				bb = domGeometry.position(el, true),
				l = Math.floor(viewport.x || viewport.l || 0),
				t = Math.floor(viewport.y || viewport.t || 0);
			domStyle.set(el, {
				left: l + 'px',
				top: t + 'px'
			});
			return {l:l, t:t};
		},

        adjustSize: function(el) {
            domClass.add(el, 'ui-text-adjust');
            if (has('ie')) {
                domStyle.set(el, {
                    zoom: 1
                });
            } else {
                domStyle.set(el, {
                    transform: 'scale(1)'
                });
            }

            if (el.style.display != 'none') {
                domStyle.set(el, {
                    display: 'inline-block'
                });
            }
            var count = 8,
                w = domGeometry.position(el, true).w,
                pbb = domGeometry.position(el.parentNode, true);
            for (var i = 0; i < count; i++) {
                if (w*(1 - i*0.1) > pbb.w) {
                    if (has('ie')) {
                        domStyle.set(el, {
                            zoom: 1 - (i + 1)*0.1
                        });
                    } else {
                        domStyle.set(el, {
                            transform: 'scale('+(1 - (i + 1)*0.1)+')'
                        });
                    }
                } else {
                    break;
                }
            }
        }
	};
});