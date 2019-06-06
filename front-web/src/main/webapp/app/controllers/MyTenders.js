define([
    'dojo/dom',
    'dojo/dom-style',
    'dojo/dom-class',
    'dojo/dom-construct',
    'dojo/dom-attr',
    'dojo/on',
    'dojo/query',
    'app/common/Ajax',
    'app/common/Global',
    'app/controllers/Helper',
    'app/views/common/SideMenu',
    'app/views/financing/MyTenderList',
    'app/common/Data',
    'dojo/when',
    'app/jquery/Pagination',
    'dojo/domReady!'
], function(dom, domStyle, domClass, domConstruct, domAttr, on, query, Ajax, Global, Helper, SideMenu, TenderList, Data, when, Pagination) {

    var tenderList, sideMenu, pageSize = 5, bizType,
        filterList = dom.byId('filterlist'),
        queryParams = {
            status:'',
            order_first_column: 'invs_rate',
            order_first_dir: 'DESC',
            start: 0,
            limit: pageSize
        };

    function setQuery(params, pageNumber, isFirst) { 
        destoryItemList();
        if (isFirst) {
            params.start = 0;
        }
        setTenderList(params, pageNumber, isFirst);
    }

    function setTenderList(params, pageNumber, isFirst) { 
        when(Data.getMyTenders(params, true), function (data) {
            if (isFirst) {
                $('#light-pagination').pagination({
                    items: data.totalCount,
                    itemsOnPage: pageSize,
                    prevText: '上一页',
                    nextText: '下一页',
                    onPageClick: function (pageNumber, e) {
                        queryParams.start = (pageNumber - 1) * queryParams.limit;
                        setQuery(queryParams, pageNumber);
                    }
                });
            }
            tenderList.set('items', data.items);
        });
    }

    function destoryItemList() {
        var items = query('tr', tenderList.itemCtnNode);//获取所有的tr
        if (items.length > 1) {
            for (var i = 1; i < items.length; i++) {//将除了第一行的所有数据先删除，再赋值
                domConstruct.destroy(items[i]);
            }
        }
    }

    function initView() {
        sideMenu = new SideMenu({
            active: '6 1'
        });
        sideMenu.placeAt('sidemenuctn');
        tenderList = new TenderList({
            hideTitle: true
        });
        tenderList.placeAt('tenderlistctn', 0);
    }

    function addListeners() {
        on(filterList, 'li:click', function(e) {
            bizType = domAttr.get(this, 'data-type');
            var activeEl = query('.active', filterList)[0];
            domClass.remove(activeEl, 'active');
            activeEl = query('a', this)[0];
            domClass.add(activeEl, 'active');

            queryParams.status = bizType;

            setQuery(queryParams, 1, true);
        });
    }

    return {
        init: function() {
            initView();
            setQuery(queryParams, 1, true);
            Helper.init();
            addListeners();
        }
    }
});