/**
 * Created by xavier on 15/6/29.
 */

/**
 * 滑动块的东东
 * 千分位的
 * @type {{el: null, ruler: null, bar: null, input: null, max: null, span: null, rate_min: null, rate_max: null, rate: null, starX: number, isMove: boolean, pos: null, init: Function, move: Function, setMin: Function}}
 */
Slider = {
    el : null,
    ruler : null,
    bar : null,
    inputText : null,
    max : null,
    span : null,
    rate_min : null,
    rate_max : null,
    rate : null,
    starX : 0,
    isMove : false,
    pos : null,
    val : 0,

    init : function() {
        // 滑块变动的内容
        this.el = $("#drapBar");
        this.ruler = this.el.find('i:first');
        this.bar = this.el.find('b:first');
        this.inputText = this.el.find('input:first');
        this.max = this.ruler.width() - this.bar.width();
        this.span = this.el.find('span:first');
        this.rate_min = this.el.data('min');
        this.rate_max = this.el.data('max');
        this.rate = this.rate_max - this.rate_min;
        this.starX = 0;
        this.isMove = false;
        this.pos = null;
        //console.info("滑块宽度" + this.max);

        var e = this;

        this.bar.mousedown(function(event) {
            e.starX = event.pageX;
            e.isMove = true;
            e.pos = e.bar.position();
        });

        $(document).bind('mouseup', function() {
            if (e.isMove) {
                e.isMove = false;
            }
        }).bind('mousemove', function(event) {
            if (e.isMove) {
                var x = event.pageX - e.starX;
                e.move(x);
                return false;
            }
        }).bind("keydown", function(event) {
            if (event.keyCode == 39) {
                e.val += 1;
                e.move(0);
            } else if (event.keyCode == 37) {
                e.val += -1;
                e.move(0);
            }
        });
    },

    move : function (x) {
        //console.info("滑动距离:" + x);
        // 这个就要根据实际宽度计算移动的比例了
        x = (x / this.max) * this.rate;
        //console.info("比例距离:" + x);
        this.val += x;
        //console.info("当前VAL " + this.val);

        if (this.val <= this.rate_min) this.val = this.rate_min;
        if (this.val >= this.rate_max) this.val = this.rate_max;
        var cur_rate = (this.val / 100).toFixed(2);
        //console.info("滑块比例 " + cur_rate);

        this.span.html(cur_rate + "%");
        this.inputText.val(cur_rate);
        $(this.inputText).trigger("change");

        var show_rate = ((1 - (this.rate_max - this.val) / this.rate) * 100).toFixed(2);
        //console.info("位置比例：" + show_rate);

        this.bar.css({
            'left': show_rate + "%"
        });
    },
    // 设置滑块的最小值
    setMin : function (min) {
        this.rate_min = min;
        this.rate = this.rate_max - this.rate_min;
    },
    // 设置滑块的最大值
    setMax : function (max) {
        this.rate_max = max;
        this.rate = this.rate_max - this.rate_min;
    },
    // 设置滑块的当前值
    setVal : function(val) {
        //console.info("手动设置值 " + val);
        this.val = val;
        this.move(0);
    }
};
