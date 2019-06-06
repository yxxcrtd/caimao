define([
    'dojo/dom',
    'dojo/dom-style',
    'dojo/dom-class',
    'dojo/dom-construct',
    'dojo/on',
    'dojo/query',
    'app/common/Ajax',
    'app/common/Global',
    'app/controllers/Helper',
    'dojo/window',
    'dojo/domReady!'
], function(dom, domStyle, domClass, domConstruct, on, query, Ajax, Global, Helper, win) {

    var anchor = Global.getUrlParam('a');

    function initView() {

        query('.help-pannel-02 tr:nth-child(odd)').addClass('help-tr-01');
        query('.help-pannel-02 tr:nth-child(even)').addClass('help-tr-02');

        query('.help-head li').on('click', function() {
            var tabs =  query('.help-head li'),
                contents = query('.help-content .content');
            tabs.removeClass('select');
            var index = tabs.indexOf(this);
            tabs.at(index).addClass('select');
            contents.style('display','none');
            contents.at(index).style('display', 'block');
        });
        function pos(a) {
            var indexs = a.split(',');
            on.emit(query('.help-head li').at(indexs[0])[0], 'click', {
                bubbles: true,
                cancelable: true
            });
            var target = dom.byId('pos-'+indexs[0]+'-'+indexs[1]);
            win.scrollIntoView(target);
            domClass.add(target, 'select');
        }
        if (anchor) {
            pos(anchor);
        }
    }

    return {
        init: function() {
            initView();
            Helper.init();
        }
    }
});