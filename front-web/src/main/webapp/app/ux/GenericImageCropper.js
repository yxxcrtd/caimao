define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dojo/dom-style',
    'dojo/dom-attr',
    'dojo/on',
    'app/common/Global',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/string',
    'dojo/dom-geometry',
    'dojo/dom-class'
], function(declare, _WidgetBase, domStyle, domAttr, on, Global, dom, domConstruct, string, domGeometry, domClass){
    return declare([_WidgetBase], {
    	minWidth: 20,
        minHeight: 20,
        //gap between crop region border and container border
        gap: 50,
        //the initial crop region size
        cropSize: 0,
        //whether to keep crop region as a square
        keepSquare: false,
        postCreate: function() {
        	var me = this;
            this.inherited(arguments);
            this.updateUI();
            //this.connect(this.cropNode, 'onmousedown', '_onMouseDown');
            on(document, '.crop-node:mousedown', function(e) {
                me.dragging = e.target == me.cropNode ? 'move' : e.target.className;
                var pos = domGeometry.position(me.cropNode);
                var pos2 = domGeometry.position(me.domNode);
                //console.debug(pos,e);
                me.startedPos = {
                    x: e.pageX,
                    y: e.pageY,
                    h: pos.h - 2 //2 is border width
                    ,
                    w: pos.w - 2,
                    l: pos.x - pos2.x,
                    t: pos.y - pos2.y
                }
                var c = domStyle.set(e.target, 'cursor');
                domStyle.set(document.body, {
                    cursor: c
                });
                domStyle.set(me.cropNode, {
                    cursor: c
                });

            });

            on(document, 'mouseup', function(e) {
            	me.dragging = false;
                dojo.style(document.body, {
                    cursor: 'default'
                });
                dojo.style(me.cropNode, {
                    cursor: 'move'
                });
                me.onDone(me.getInfo());

            });
            //this.connect(document, 'onmouseup', '_onMouseUp');
            //this.connect(document, 'onmousemove', '_onMouseMove');
            on(document, 'mousemove', function(e) {
            	if (!me.dragging) return;

                if (me.dragging == 'move') me.doMove(e);
                else me.doResize(e);
                me.updateUI();

                me.onChange(me.getInfo());

            });
            document.ondragstart = function() {
                return false;
            }
            dojo.setSelectable(this.domNode, false);
            dojo.addClass(this.domNode, 'image-cropper');
            if (this.image) this.imageNode = dojo.create('img', {
                src: this.image
            }, this.domNode, 'first');
        }

        ,
        buildRendering: function() {
            this.inherited(arguments);
            this._archors = {};
            this._blockNodes = {};
            
            this.cropNode = dojo.create('div', {
                className: 'crop-node'
            }, this.domNode, 'last');

            var arr = ['lt', 't', 'rt', 'r', 'rb', 'b', 'lb', 'l'];
            for (var i = 0; i < 8; i++) {
                var img = dojo.create('img', {
                    className: arr[i]
                });
                img.src = this._blankGif;
                this.cropNode.appendChild(img);
                this._archors[arr[i]] = img;
            }

            arr = ['l', 't', 'r', 'b'];
            for (var i = 0; i < 4; i++) {
                this._blockNodes[arr[i]] = dojo.create('div', {
                    className: 'block block-' + arr[i]
                }, this.domNode, 'last');
            }
            
        },
        setImage: function(url) {
            if (this.imageNode) {
                domConstruct.destroy(this.imageNode);
                this.imageNode = null;
            }

            var img = new Image();
            var self = this;
            this.image = url;
            this.imageNode = img;
            domConstruct.place(img, this.domNode, 'first');
            this.imageNode.onload = function() {
                var i = new Image();
                i.src = self.imageNode.src;
                self.imageWidth = i.width;
                self.imageHeight = i.height;
                if (self.imageWidth > 490) {
                    domStyle.set(self.imageNode, {
                        width: '490px',
                        height: (490 * i.height / i.width) + 'px'
                    });
                } else {
                    domStyle.set(self.imageNode, {
                        width: self.imageWidth + 'px',
                        height: self.imageHeight + 'px'
                    });
                }
                self.setSize(self.imageNode.offsetWidth, self.imageNode.offsetHeight);
            }
            this.imageNode.src = url;
        },
        setSize: function(w, h) {
            console.debug('setting image cropper size: ', w, h);
            this.domNode.style.width = w + 'px';
            this.domNode.style.height = h + 'px';

            if (this.cropSize) {
                var m = Math.min(w, h, this.cropSize);

                dojo.style(this.cropNode, {
                    width: m - 2 + 'px',
                    height: m - 2 + 'px'
                });
            } else {
                dojo.style(this.cropNode, {
                    width: w - this.gap * 2 - 2 + 'px',
                    height: h - this.gap * 2 - 2 + 'px'
                });
            }
            var l = (w - this.cropNode.offsetWidth) / 2,
                t = (h - this.cropNode.offsetHeight) / 2;
            if (l < 0) l = 0;
            if (t < 0) t = 0;
            dojo.style(this.cropNode, {
                left: l + 'px',
                top: t + 'px'
            });
            this.posArchors();
            this.posBlocks();
            this.onChange(this.getInfo());
        }

        ,
        updateUI: function() {
            this.posArchors();
            this.posBlocks();

        }

        ,
        posArchors: function() {
            var a = this._archors,
                w = this.cropNode.offsetWidth,
                h = this.cropNode.offsetHeight;

            a.t.style.left = a.b.style.left = w / 2 - 4 + 'px';
            a.l.style.top = a.r.style.top = h / 2 - 4 + 'px';
        }

        ,
        posBlocks: function() {
            var p = this.startedPos,
                b = this._blockNodes;
            var l = parseInt(this.cropNode.style.left || dojo.style(this.cropNode, 'left'));
            var t = parseInt(this.cropNode.style.top || dojo.style(this.cropNode, 'top'));
            var w = this.cropNode.offsetWidth;
            var ww = this.domNode.offsetWidth;
            var h = this.cropNode.offsetHeight;
            var hh = this.domNode.offsetHeight;

            b = this._blockNodes;
            b.t.style.height = b.l.style.top = b.r.style.top = t + 'px';

            b.l.style.height = b.r.style.height = h + 'px';
            b.l.style.width = l + 'px';

            b.r.style.width = ww - w - l + 'px';
            b.b.style.height = hh - h - t + 'px';
        },
        getInfo: function() {
            return {
                w: this.cropNode.offsetWidth - 2,
                h: this.cropNode.offsetHeight - 2,
                l: parseInt(this.cropNode.style.left || dojo.style(this.cropNode, 'left')),
                t: parseInt(this.cropNode.style.top || dojo.style(this.cropNode, 'top')),
                cw: this.domNode.offsetWidth //container width
                ,
                ch: this.domNode.offsetHeight //container height
            };
        },
        
        getInfoActual: function() {
        	var rate = this.imageWidth / this.domNode.offsetWidth;
        	return {
                w: (this.cropNode.offsetWidth - 2) * rate,
                h: (this.cropNode.offsetHeight - 2) * rate,
                l: parseInt(this.cropNode.style.left || dojo.style(this.cropNode, 'left')) * rate,
                t: parseInt(this.cropNode.style.top || dojo.style(this.cropNode, 'top')) * rate
            };
        },

        doMove: function(e) {
            //console.debug('doing move',e);
            var s = this.cropNode.style,
                p0 = this.startedPos;
            var l = p0.l + e.pageX - p0.x;
            var t = p0.t + e.pageY - p0.y;
            if (l < 0) l = 0;
            if (t < 0) t = 0;
            var maxL = this.domNode.offsetWidth - this.cropNode.offsetWidth;
            var maxT = this.domNode.offsetHeight - this.cropNode.offsetHeight;
            if (l > maxL) l = maxL;
            if (t > maxT) t = maxT;
            s.left = l + 'px';
            s.top = t + 'px'

        }

        ,
        onChange: function() {
            //Event:
            //    When the cropping size is changed.
        },
        onDone: function() {
            //Event:
            //    When mouseup.
        },
        doResize: function(e) {
            var m = this.dragging,
                s = this.cropNode.style,
                p0 = this.startedPos;
            //delta x and delta y
            var dx = e.pageX - p0.x,
                dy = e.pageY - p0.y;

            if (this.keepSquare) {
                if (m == 'l') {
                    dy = dx;
                    if (p0.l + dx < 0) dx = dy = -p0.l;
                    if (p0.t + dy < 0) dx = dy = -p0.t;
                    m = 'lt';
                } else if (m == 'r') {
                    dy = dx;
                    m = 'rb';
                } else if (m == 'b') {
                    dx = dy;
                    m = 'rb';
                } else if (m == 'lt') {
                    dx = dy = Math.abs(dx) > Math.abs(dy) ? dx : dy;
                    if (p0.l + dx < 0) dx = dy = -p0.l;
                    if (p0.t + dy < 0) dx = dy = -p0.t;
                } else if (m == 'lb') {
                    dy = -dx;
                    if (p0.l + dx < 0) {
                        dx = -p0.l;
                        dy = p0.l;
                    }
                } else if (m == 'rt' || m == 't') {
                    dx = -dy;
                    m = 'rt';
                    if (p0.t + dy < 0) {
                        dy = -p0.t;
                        dx = -dy;
                    }
                }
            }
            if (/l/.test(m)) {
                dx = Math.min(dx, p0.w - this.minWidth);
                if (p0.l + dx >= 0) {
                    s.left = p0.l + dx + 'px';
                    s.width = p0.w - dx + 'px';
                } else {
                    s.left = 0;
                    s.width = p0.l + p0.w + 'px';
                }
            }
            if (/t/.test(m)) {
                dy = Math.min(dy, p0.h - this.minHeight);
                if (p0.t + dy >= 0) {
                    s.top = p0.t + dy + 'px';
                    s.height = p0.h - dy + 'px';
                } else {
                    s.top = 0;
                    s.height = p0.t + p0.h + 'px';
                }
            }
            if (/r/.test(m)) {
                dx = Math.max(dx, this.minWidth - p0.w);
                if (p0.l + p0.w + dx <= this.domNode.offsetWidth) {
                    s.width = p0.w + dx + 'px';
                } else {
                    s.width = this.domNode.offsetWidth - p0.l - 2 + 'px';
                }
            }
            if (/b/.test(m)) {
                dy = Math.max(dy, this.minHeight - p0.h);
                if (p0.t + p0.h + dy <= this.domNode.offsetHeight) {
                    s.height = p0.h + dy + 'px';
                } else {
                    s.height = this.domNode.offsetHeight - p0.t - 2 + 'px';
                }
            }

            if (this.keepSquare) {
                var min = Math.min(parseInt(s.width), parseInt(s.height));
                s.height = s.width = min + 'px';
            }
        }
    });
});