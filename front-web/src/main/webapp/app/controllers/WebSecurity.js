define([
    'app/controllers/Helper',
    'dojo/query',
    'dojo/on',
    'dojo/dom',
    'dojo/dom-style',
    'dojo/dom-class',
    'app/common/Fx'
], function (Helper, query, on, dom, domStyle, domClass, Fx) {
    var config = {};

    function initView() {
        //on(document, 'click', function(e) {
        //    if(this.expanded) {
        //        query('.fa', this).removeClass('fa-rotate-180');
        //        Fx.collapse(query('.security-item-show', this.parentNode)[0]).play();
        //    } else {
        //        query('.fa', this).addClass('fa-rotate-180');
        //        Fx.expand(query('.security-item-show', this.parentNode)[0]).play();
        //    }
        //    this.expanded = !this.expanded;
        //});
    }

    return {
        init: function () {
            initView();
            Helper.init(config);
        }
    }
});