/**
 * Created by luoqi11692 on 2015/2/23.
 */
define([
    'dojo/dom-geometry',
    'app/jquery/Raphael'
], function(domGeometry) {
    return {
        circleProgress: function(el, value, width, rad) {
            var value = parseInt(value).toFixed(0);
            var bb = domGeometry.position(el, true);
            var width = width || 100;
            var rad = width / 2;
            var colors = ['#f63a0f', '#f27011', '#f2b01e', '#f2d31b', '#86e01e', '#86e01e'];
            var index = 5 - Math.floor(value / 20);
            var paper = new Raphael(el, width, width);
            paper.customAttributes.arc = function(value, color, rad){
                var v = 3.6*value,
                    alpha = v == 360 ? 359.99 : v,
                    random = 90,
                    a = (random-alpha) * Math.PI/180,
                    b = random * Math.PI/180,
                    sx = width / 2 + rad * Math.cos(b),
                    sy = width / 2 - rad * Math.sin(b),
                    x = width / 2 + rad * Math.cos(a),
                    y = width / 2 - rad * Math.sin(a),
                    path = [['M', sx, sy], ['A', rad, rad, 0, +(alpha > 180), 1, x, y]];
                return { path: path, stroke: color }
            }

            paper.circle(width / 2,width / 2,width / 2 - 10 - 5).attr({ stroke: 'none', fill: '#193340' });
            //var title = paper.text(rad, rad + 5, value + '%').attr({
            //    font: '16px Arial',
            //    fill: '#fff'
            //}).toFront();
            paper.text(width / 2, width / 4 + 2, value + '%').attr({
                font: '16px Arial',
                fill: '#fff'
            });
            var z = paper.path().attr({arc: [value, colors[index], rad - 10], 'stroke-width': 10 });
            z.mouseover(function(){
                this.animate({ 'stroke-width': 15, opacity: .75 }, 1000, 'elastic');
                if(Raphael.type != 'VML') //solves IE problem
                    this.toFront();
            }).mouseout(function(){
                this.stop().animate({ 'stroke-width': 10, opacity: 1 }, 1000, 'elastic');
            });
        }
    };
});
