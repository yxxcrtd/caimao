define([
    'app/controllers/Helper',
    'dojo/_base/fx',
    'dijit/registry',
    'dojo/dom',
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
    'app/views/common/P2PSideMenu',
    'app/jquery/Pagination',
    'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domStyle, domClass,
            domConstruct, mouse, focusUtil, cookie, P2PSideMenu, Pagination) {

    var config = {};
    var sideMenu, rentmplCache = {}, pagCreated = false;

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
        sideMenu = new P2PSideMenu({
            active: '1 1'
        });
        sideMenu.placeAt('sidemenuctn');

        var start = 0,
            limit = 10,
            order = 'year_rate',
            asc = 0,
            targetStatus = 0,
            targetBox = document.getElementById("targetBox"),
            targetList = document.getElementById("targetList"),
            html = targetBox.getElementsByTagName("script")[0].innerHTML,
            current;

        var tab = dom.byId('tab');
        var spans = query('.head span',tab);
        var s_id = 0;
        spans.forEach(function(item,i){
            on(item,'click',function(){
                tabView(item,i);
            });
        });

        function tabView(item,i){
            domClass.remove(spans[s_id],'select');
            domClass.add(item,'select');
            s_id = i;
            start = 0;
            if(s_id == 0){
                targetStatus = 0;
            }else{
                targetStatus = 1
            }
            pagCreated = false;
            setLoanCount();
            setLoanPage();
        }

        setLoanCount();
        setLoanPage();

        function setLoanCount(){
            Ajax.get(Global.baseUrl + '/p2p/target/queryLoanCount', {
                targetStatus:0
            }).then(function (response) {
                if (response && response.data) {
                    dom.byId('totalTargetA').innerHTML = response.data;
                }
            });

            Ajax.get(Global.baseUrl + '/p2p/target/queryLoanCount', {
                targetStatus:1
            }).then(function (response) {
                if (response && response.data) {
                    dom.byId('totalTargetB').innerHTML = response.data;
                }
            });
        }

        function setLoanPage(){
            Ajax.get(Global.baseUrl + '/p2p/target/page', {
                order : order,
                asc : asc,
                start : start,
                limit : limit,
                targetStatus:targetStatus
            }).then(function (response) {
                if (response && response.data) {
                    var records = [];
                    if(response.data.items){
                        for(var i = 0; i < response.data.items.length; i++) {
                            var tmp = response.data.items[i];
                            // 标的总金额    万
                            tmp.targetAmount = parseFloat((tmp.targetAmount / 1000000).toFixed(4));
                            // 年利率
                            tmp.yearRate = (tmp.yearRate * 100).toFixed(2);
                            // 标的进度
                            tmp.targetRate = (tmp.targetRate * 100).toFixed(2);
                            //tmp.targetOver = (tmp.targetOver / 1000000) > 1 ? parseFloat(tmp.targetOver / 1000000)+" 万" : Global.formatAmount(tmp.targetOver)+" 元";
                            tmp.targetOver = Global.formatAmount(tmp.targetOver)+" 元";
                            records[i] = tmp;
                        }
                        targetBox.innerHTML = rentmpl(html,{
                            data : records
                        });
                    }

                    if (pagCreated == false) {
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

                }
            });
        }

        on(dom.byId('targetList'), 'click', function(e){
            var tar = e.target,
                act = tar.getAttribute('act'),
                data = tar.getAttribute('data-val');
            if(act && data){
                if(current == tar){
                    asc = asc == 1?0:1;
                    order = data;
                    asc && domClass.contains(current,'desc') && domClass.remove(current, 'desc');
                    asc && !domClass.contains(current,'asc') && domClass.add(current,'asc');
                    !asc && domClass.contains(current,'asc') && domClass.remove(current, 'asc');
                    !asc && !domClass.contains(current,'desc') && domClass.add(current,'desc');
                }else{
                    current && domClass.contains(current,'desc') && domClass.remove(current, 'desc');
                    current && domClass.contains(current,'asc') && domClass.remove(current, 'asc');
                    current = tar;
                    asc = 1;
                    order = data;
                    domClass.contains(current,'desc') && domClass.remove(current,'desc');
                    !domClass.contains(current,'asc') && domClass.add(current,'asc');
                }
                setLoanPage();
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