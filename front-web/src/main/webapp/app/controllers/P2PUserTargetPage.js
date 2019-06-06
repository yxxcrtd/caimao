define([
    'app/controllers/Helper',
    'dojo/_base/fx',
    'dijit/registry',
    'dojo/dom',
    'dojo/dom-attr',
    'dojo/on',
    'app/common/Ajax',
    'dojo/query',
    'app/common/Global',
    'dojo/dom-style',
    'dojo/dom-class',
    'dojo/dom-construct',
    'dojo/mouse',
    'dijit/focus',
    'dojo/cookie',
    'app/views/common/PzSideMenu',
    'app/jquery/Pagination',
    'app/ux/GenericWindow',
    'dojo/domReady!'
], function(Helper,fx, registry, dom, domAttr, on, Ajax, query, Global, domStyle, domClass,
            domConstruct, mouse, focusUtil, cookie, PzSideMenu, Pagination, Win) {

    var config = {};
    var sideMenu, rentmplCache = {}, pagCreated = false, filterList = dom.byId('filterlist');
    var start = 0,
        limit = 10,
        targetStatus = 0,
        targetBox = document.getElementById("targetBoxX"),
        html = targetBox.getElementsByTagName("script")[0].innerHTML,
        win;

    function rentmpl(str, data) {
        var fn = !/\W/.test(str) ?
            rentmplCache[str] = rentmplCache[str] ||
            rentmpl(document.getElementById(str).innerHTML) :
            new Function("obj",
                "var p=[];" +
                "with(obj){p.push('" +
                str
                    .replace(/\\/g, "\\\\")
                    .replace(/[\r\t\n]/g, " ")
                    .split("<%").join("\t")
                    .replace(/((^|%>)[^\t]*)'/g, "$1\r")
                    .replace(/\t=(.*?)%>/g, "',$1,'")
                    .split("\t").join("');")
                    .split("%>").join("p.push('")
                    .split("\r").join("\\'") + "');}return p.join('');");
        return data ? fn(data) : fn;
    }

    function initView() {
        sideMenu = new PzSideMenu({
            active: '2 4'
        });
        sideMenu.placeAt('sidemenuctn');

        on(filterList, 'li:click', function (e) {
            targetStatus = domAttr.get(this, 'data-type');
            var activeEl = query('.active', filterList)[0];
            domClass.remove(activeEl, 'active');
            activeEl = query('a', this)[0];
            domClass.add(activeEl, 'active');
            pagCreated = false;
            setLoanPage();
        });

        setLoanPage();
    }

    function setLoanPage(){
        Ajax.get(Global.baseUrl + '/p2p/target/userTargetPage', {
            start : start,
            limit : limit,
            targetStatus:targetStatus
        }).then(function (response) {
            if (response && response.data && response.data.items) {
                targetBox.innerHTML = rentmpl(html,{
                    data:response.data.items
                });
                if (response.data.items.length != 0) {
                    $('#light-pagination').show();
                } else {
                    $('#light-pagination').hide();
                }

                if (!pagCreated) {
                    $('#light-pagination').pagination({
                        items: response.data.totalCount,
                        itemsOnPage: limit,
                        prevText: '上一页',
                        nextText: '下一页',
                        onPageClick: function (pageNumber, e) {
                            start = (pageNumber - 1) * limit;
                            setLoanPage();
                        }
                    });
                    pagCreated = true;
                }
                addListenCancel();
            }
        });
    }

    function addListenCancel(){
        on(query(".cancelTargetBtn"), 'click', function(e){
            var targetId = domAttr.get(this, 'targetId');
            if(confirm('确认要取消此借款申请吗？')){
                Ajax.get(Global.baseUrl + '/p2p/target/cancelTarget', {
                    targetId:targetId
                }).then(function (response) {
                    if (!win) {
                        win = new Win({
                            width: 500,
                            height: 100,
                            title: '提示信息',
                            closeAction:'reload'
                        });
                        win.placeAt(document.body);
                    }
                    if(response && response.success) {
                        win.set('msg', '<p style="text-align: center;font-size: 16px;color:green">取消成功</p>');
                    }else{
                        win.set('msg', '<p style="text-align: center;font-size: 16px;color:red">'+ response.msg +'</p>');
                    }
                    win.show();
                });
            }
        });
    }

    return {
        init: function() {
            initView();
            Helper.init(config);
        }
    }
});