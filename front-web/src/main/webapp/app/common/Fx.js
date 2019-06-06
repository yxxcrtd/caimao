define([
	'dojo/_base/fx',
	'dojo/fx',
    'dojo/fx/easing',
	'dojo/dom',
	'dojo/dom-construct',
	'app/common/Position',
	'dojo/_base/lang',
	'dojo/dom-class',
	'dojo/dom-attr',
	'dojo/dom-style',
	'dojo/dom-geometry',
	'app/common/Global',
	'dojo/sniff',
	'dojo/_base/window',
	'dojo/window'
], function(fx, coreFx, easing, dom, domConstruct, Position, lang, domClass,
		domAttr, style, domGeometry, Global, has, baseWindow, win) {
	
	return {
		slideInCenter: function(el, body, offset) {
			var pos = Position.center(el, body);
			return fx.animateProperty({
			    node: el,
			    properties: {
			    	opacity: {start:0, end: 1}, 
			        top: {start:pos.t - (offset || 100), end: pos.t}
			    }
		    });
		},
		slideIn: function(el) {
			var height = domGeometry.getMarginBox(el).h;
			return fx.animateProperty({
			    node: el,
			    properties: {
			    	opacity: {start:0, end: 1}, 
			        top: {start: 0 - height, end: 0}
			    },
			    beforeBegin: function() {
			    	el.style.top = (0 - height) + 'px';
                    el.style.display = 'block';
			    }
		    });
		},
		slideOut: function(el) {
			var height = domGeometry.getMarginBox(el).h;
			return fx.animateProperty({
			    node: el,
			    properties: {
			    	opacity: {start:1, end: 0}, 
			        top: {start: 0, end: 0 - height}
			    },
                onEnd: function() {
                    el.style.display = 'none';
                }
		    });
		},
		slideOutCenter: function(el, body, offset) {
			var pos = Position.center(el, body);
			return fx.animateProperty({
			    node: el,
			    properties: {
			    	opacity: {start:1, end: 0}, 
			        top: {start:pos.t, end: pos.t + (offset || 100)}
			    },
			    onEnd: function() {
			    	el.style.display = 'none';
			    }
		    });
		},
		
		wait: function(el, time) {
			return fx.animateProperty({
				node: el,
 			    duration: time || 1000
 		    })
		},
		
		slideInOutCenter: function(el, body, offset) {
			return coreFx.chain([this.slideInCenter(el, body, offset), 
			                     this.wait(el, 1000),
			                     this.slideOutCenter(el, body, offset)]);
		},
		
		collapse: function(el) {
			return fx.animateProperty({
			    node: el,
			    properties: {
			        height: {end: 0}
			    },
			    onEnd: function() {
			    	el.style.display = 'none';
			    	el.style.height = '';
                    el.style.overflow = 'inherit';
			    },
                beforeBegin: function() {
                    el.style.overflow = 'hidden';
                }
		    });
		},
		
		expand: function(el) {
			return fx.animateProperty({
			    node: el,
			    properties: {
			        height: {start: 0, end: function() {
			        	return domGeometry.getMarginBox(el).h;
			        }}
			    },
			    beforeBegin: function() {
			    	el.style.display = 'block';
                    el.style.overflow = 'hidden';
			    },
                onEnd: function() {
                    el.style.overflow = 'inherit';
                }
		    });
		},
		
		scrollToY: function(scrollTargetY, speed, easing) {
			// scrollTargetY: the target scrollY property of the window
		    // speed: time in pixels per second
		    // easing: easing equation to use

		    var scrollY = window.scrollY,
		        scrollTargetY = scrollTargetY || 0,
		        speed = speed || 2000,
		        easing = easing || 'easeOutSine',
		        currentTime = 0;

		    // min time .1, max time .8 seconds
		    var time = Math.max(.1, Math.min(Math.abs(scrollY - scrollTargetY) / speed, .8));

		    // easing equations from https://github.com/danro/easing-js/blob/master/easing.js
		    var PI_D2 = Math.PI / 2,
		        easingEquations = {
		            easeOutSine: function (pos) {
		                return Math.sin(pos * (Math.PI / 2));
		            },
		            easeInOutSine: function (pos) {
		                return (-0.5 * (Math.cos(Math.PI * pos) - 1));
		            },
		            easeInOutQuint: function (pos) {
		                if ((pos /= 0.5) < 1) {
		                    return 0.5 * Math.pow(pos, 5);
		                }
		                return 0.5 * (Math.pow((pos - 2), 5) + 2);
		            }
		        };

		    // add animation loop
		    function tick() {
		        currentTime += 1 / 60;

		        var p = currentTime / time;
		        var t = easingEquations[easing](p);

		        if (p < 1) {
		            requestAnimFrame(tick);

		            window.scrollTo(0, scrollY + ((scrollTargetY - scrollY) * t));
		        } else {
		            window.scrollTo(0, scrollTargetY);
		        }
		    }

		    // call it once to get started
		    tick();
		},
		
		show: function(el, styles, cls) {
			el.isShow = true;
			var properties = {
					opacity: {start: 0, end: 1}
		    };
			if (styles) {
				properties = lang.mixin(properties, styles);
			}
			fx.animateProperty({
			    node: el,
			    properties: properties,
			    beforeBegin: function() {
			    	el.style.visibility = 'visible';
			    }
		    }).play();
			if (cls) {
				domClass.add(el, cls);
			}
		},
		
		hide: function(el, styles, cls) {
			el.isShow = false;
			var properties = {
					opacity: {start: 1, end: 0}
		    };
			if (styles) {
				properties = lang.mixin(properties, styles);
			}
			fx.animateProperty({
			    node: el,
			    properties: properties,
			    onEnd: function() {
			    	if (!el.isShow) {
			    		el.style.visibility = 'hidden';
			    	}
			    }
		    }).play();
			if (cls) {
				domClass.remove(el, cls);
			}
		},
		
		fillAllWords: function(items, data) {
			items = items.reverse();
			var me = this;
			var complete = function() {
				if (items.length == 0) {
					return false;
				}
				var item = items.pop(),
					key = domAttr.get(item, 'data-field'),
					val = data[key],
					bb = domGeometry.position(item, true);
				if (val) {
					var el = domConstruct.create('i', {className: 'filltext'}, item),
						type = domAttr.get(item, 'data-type');
					if (type == 'amount') {
						val = Global.formatAmount(val);
					} else if (type == 'name') {
						val = Global.encodeInfo(val, 1, 0);
					} else if (type == 'idcard') {
						val = Global.encodeInfo(val, 4, 4);
					}
					me.scrollIntoView(item);
					me.fillWords(el, val, complete);
				} else {
					complete();
				}
			};
			complete();
		},
		
		fillWords: function(el, text, complete) {
			text = text + '';
			var len = text.length,
				count = 0;
			(function loop() {
				el.innerHTML = text.slice(0, count);
				if (count <= len - 1) {
					setTimeout(function() {
						loop.call();
					}, 100);
					count++;
				} else {
					complete();
				}
			})();
		},
		
		get: function(/*Document*/ doc){
			// summary:
			//		Get window object associated with document doc.
			// doc:
			//		The document to get the associated window for.

			// In some IE versions (at least 6.0), document.parentWindow does not return a
			// reference to the real window object (maybe a copy), so we must fix it as well
			// We use IE specific execScript to attach the real window reference to
			// document._parentWindow for later use
			if(has("ie") && window !== document.parentWindow){
				/*
				In IE 6, only the variable "window" can be used to connect events (others
				may be only copies).
				*/
				doc.parentWindow.execScript("document._parentWindow = window;", "Javascript");
				//to prevent memory leak, unset it after use
				//another possibility is to add an onUnload handler which seems overkill to me (liucougar)
				var win = doc._parentWindow;
				doc._parentWindow = null;
				return win;	//	Window
			}

			return doc.parentWindow || doc.defaultView;	//	Window
		},
		
		scrollIntoView: function(/*DomNode*/ node, /*Object?*/ pos){
			// summary:
			//		Scroll the passed node into view using minimal movement, if it is not already.

			// Don't rely on node.scrollIntoView working just because the function is there since
			// it forces the node to the page's bottom or top (and left or right in IE) without consideration for the minimal movement.
			// WebKit's node.scrollIntoViewIfNeeded doesn't work either for inner scrollbars in right-to-left mode
			// and when there's a fixed position scrollable element

			try{ // catch unexpected/unrecreatable errors (#7808) since we can recover using a semi-acceptable native method
				node = dom.byId(node);
				var	doc = node.ownerDocument || baseWindow.doc,	// TODO: why baseWindow.doc?  Isn't node.ownerDocument always defined?
					body = baseWindow.body(doc),
					html = doc.documentElement || body.parentNode,
					isIE = has("ie"),
					isWK = has("webkit");
				// if an untested browser, then use the native method
				if(node == body || node == html){ return; }
				if(!(has("mozilla") || isIE || isWK || has("opera") || has("trident")) && ("scrollIntoView" in node)){
					node.scrollIntoView(false); // short-circuit to native if possible
					return;
				}
				var	backCompat = doc.compatMode == 'BackCompat',
					rootWidth = Math.min(body.clientWidth || html.clientWidth, html.clientWidth || body.clientWidth),
					rootHeight = Math.min(body.clientHeight || html.clientHeight, html.clientHeight || body.clientHeight),
					scrollRoot = (isWK || backCompat) ? body : html,
					nodePos = pos || domGeometry.position(node),
					el = node.parentNode,
					isFixed = function(el){
						return (isIE <= 6 || (isIE == 7 && backCompat))
							? false
							: (has("position-fixed-support") && (style.get(el, 'position').toLowerCase() == "fixed"));
					},
					self = this,
					scrollElementBy = function(el, x, y){
						if(el.tagName == "BODY" || el.tagName == "HTML"){
							if (x == '0') {
								var topbar = dom.byId('topbar');
								if (topbar && isFixed(topbar)) {
									y = y - topbar.offsetHeight;
								}
								var mainMenu = dom.byId('subheader');
								if (mainMenu && isFixed(mainMenu)) {
									y = y - mainMenu.offsetHeight;
								}
								self.scrollToY((window.scrollY || el.scrollTop) + y);
							} else {
								self.get(el.ownerDocument).scrollBy(x, y);
							}
						}else{
							x && (el.scrollLeft += x);
							y && (el.scrollTop += y);
						}
					};
				if(isFixed(node)){ return; } // nothing to do
				while(el){
					if(el == body){ el = scrollRoot; }
					var	elPos = domGeometry.position(el),
						fixedPos = isFixed(el),
						rtl = style.getComputedStyle(el).direction.toLowerCase() == "rtl";

					if(el == scrollRoot){
						elPos.w = rootWidth; elPos.h = rootHeight;
						if(scrollRoot == html && isIE && rtl){ elPos.x += scrollRoot.offsetWidth-elPos.w; } // IE workaround where scrollbar causes negative x
						if(elPos.x < 0 || !isIE || isIE >= 9){ elPos.x = 0; } // older IE can have values > 0
						if(elPos.y < 0 || !isIE || isIE >= 9){ elPos.y = 0; }
					}else{
						var pb = domGeometry.getPadBorderExtents(el);
						elPos.w -= pb.w; elPos.h -= pb.h; elPos.x += pb.l; elPos.y += pb.t;
						var clientSize = el.clientWidth,
							scrollBarSize = elPos.w - clientSize;
						if(clientSize > 0 && scrollBarSize > 0){
							if(rtl && has("rtl-adjust-position-for-verticalScrollBar")){
								elPos.x += scrollBarSize;
							}
							elPos.w = clientSize;
						}
						clientSize = el.clientHeight;
						scrollBarSize = elPos.h - clientSize;
						if(clientSize > 0 && scrollBarSize > 0){
							elPos.h = clientSize;
						}
					}
					if(fixedPos){ // bounded by viewport, not parents
						if(elPos.y < 0){
							elPos.h += elPos.y; elPos.y = 0;
						}
						if(elPos.x < 0){
							elPos.w += elPos.x; elPos.x = 0;
						}
						if(elPos.y + elPos.h > rootHeight){
							elPos.h = rootHeight - elPos.y;
						}
						if(elPos.x + elPos.w > rootWidth){
							elPos.w = rootWidth - elPos.x;
						}
					}
					// calculate overflow in all 4 directions
					var	l = nodePos.x - elPos.x, // beyond left: < 0
//						t = nodePos.y - Math.max(elPos.y, 0), // beyond top: < 0
						t = nodePos.y - elPos.y, // beyond top: < 0
						r = l + nodePos.w - elPos.w, // beyond right: > 0
						bot = t + nodePos.h - elPos.h; // beyond bottom: > 0
					var s, old;
					if(r * l > 0 && (!!el.scrollLeft || el == scrollRoot || el.scrollWidth > el.offsetHeight)){
						s = Math[l < 0? "max" : "min"](l, r);
						if(rtl && ((isIE == 8 && !backCompat) || isIE >= 9)){ s = -s; }
						old = el.scrollLeft;
						scrollElementBy(el, s, 0);
						s = el.scrollLeft - old;
						nodePos.x -= s;
					}
					if(bot * t > 0 && (!!el.scrollTop || el == scrollRoot || el.scrollHeight > el.offsetHeight)){
						s = Math.ceil(Math[t < 0? "max" : "min"](t, bot));
						old = el.scrollTop;
						scrollElementBy(el, 0, s);
						s = el.scrollTop - old;
						nodePos.y -= s;
					}
					el = (el != scrollRoot) && !fixedPos && el.parentNode;
				}
			}catch(error){
				console.error('scrollIntoView: ' + error);
				node.scrollIntoView(false);
			}
		},

        // show the amount dynamically
        animNumber: function(el, start, end, count, duration) {
            var start = parseFloat(start) || 0,
                end = parseFloat(end) || 0,
                duration = duration || 1000,
                count = count || 10,
                interval = (end - start) / count,
                cur = start,
                index = 0,
                me = this,
                parses = (end + '').split('.').length == 2 ? (end + '').split('.')[1].length : 0;
            (function loop() {
                setTimeout(function() {
                    if (index < count) {
                        el.innerHTML = Global.formatAmount(cur, parses);
                        index ++;
                        cur = start + (end - start) * easing.quintOut(index / count);
                        loop();
                    } else {
                        el.innerHTML = Global.formatAmount(end);
                    }
                }, duration / count);
            })();
        },

        shakeX: function(el) {
            if (el.parentNode) {
                style.set(el.parentNode, {
                    perspective: '2500px'
                });
            }
            domClass.add(el, 'shakeX');
        }
	};
});