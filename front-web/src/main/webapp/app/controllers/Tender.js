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
    'app/views/financing/TenderList',
    'app/views/financing/TenderRank',
    'app/common/Data',
    'dojo/when',
    'app/jquery/Pagination',
    'dojo/domReady!'
], function (dom, domStyle, domClass, domConstruct, on, query, Ajax, Global, Helper, TenderList, TenderRank, Data, when, Pagination) {
    var tenderList, tenderRank, isAblePay,
        orderFirstColumn = 'invs_rate',
        orderFirstDir = 'DESC',
        orderFirstDir1 = '',
        orderFirstDir2 = '',
        orderFirstDir3 = 'DESC',
        queryParams = {
            is_able_pay: '',
            order_first_column: 'invs_rate',
            order_first_dir: 'DESC',
            start: 0,
            limit: 10
        };

    function setQuery(params, pageNumber, isFirst) {
        destoryItemList();
        if (isFirst) {
            params.start = 0;
        }
        setTenderList(params, pageNumber, isFirst);
    }

    function setTenderList(params, pageNumber, isFirst) {
        when(Data.getTenders(params, true), function (data) {
            if (isFirst) {
                $('#light-pagination').pagination({
                    items: data.totalCount,
                    itemsOnPage: 10,
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
        tenderList = new TenderList();
        tenderList.placeAt('leftctn', 0);
        tenderRank = new TenderRank();
        tenderRank.placeAt('rightctn');

        domClass.add('invsAmountPre', 'active');

        when(Data.getTenderNewcomers({start: 0, limit: 10}), function (items) {
            tenderRank.set('items', items);
        });
        on(query('#id_status')[0], 'click', function (e) {
            if (query('#id_status')[0].checked) {
                queryParams.is_able_pay = '1';
            } else {
                queryParams.is_able_pay = '';
            }
            setQuery(queryParams, 1, true);
        });

        //按发布日期排序
        on(query('#invsDatePublish')[0], 'click', function (e) {
            domClass.remove('invsAmountPre', 'active');
            domClass.remove('invsDuring', 'active');
            domClass.add('invsDatePublish', 'active');
            queryParams.order_first_column = 'invs_date_publish';
            if (orderFirstDir1) {
                if (orderFirstDir1 == 'DESC') {
                    queryParams.order_first_dir = 'ASC';
                    orderFirstDir1 = 'ASC';
                    domClass.replace(tenderList.invsDatePublishNode, 'fa fa-arrow-up', 'fa fa-arrow-down');
                } else {
                    queryParams.order_first_dir = 'DESC';
                    orderFirstDir1 = 'DESC';
                    domClass.replace(tenderList.invsDatePublishNode, 'fa fa-arrow-down', 'fa fa-arrow-up');
                }
            } else {//第一次点击
                queryParams.order_first_dir = 'DESC';
                orderFirstDir1 = 'DESC';
            }
            $('#light-pagination').pagination('selectPage', 1);
        });

        //借款期限排序
        on(query('#invsDuring')[0], 'click', function (e) {
            domClass.remove('invsAmountPre', 'active');
            domClass.remove('invsDatePublish', 'active');
            domClass.add('invsDuring', 'active');
            queryParams.order_first_column = 'invs_during';
            if (orderFirstDir2) {
                if (orderFirstDir2 == 'DESC') {
                    queryParams.order_first_dir = 'ASC';
                    orderFirstDir2 = 'ASC';
                    domClass.replace(tenderList.invsDuringNode, 'fa fa-arrow-up', 'fa fa-arrow-down');
                } else {
                    queryParams.order_first_dir = 'DESC';
                    orderFirstDir2 = 'DESC';
                    domClass.replace(tenderList.invsDuringNode, 'fa fa-arrow-down', 'fa fa-arrow-up');
                }
            } else {//第一次点击
                queryParams.order_first_dir = 'DESC';
                orderFirstDir2 = 'DESC';
            }
            $('#light-pagination').pagination('selectPage', 1);
        });

        //年化收益排序
        on(query('#invsAmountPre')[0], 'click', function (e) {
            domClass.add('invsAmountPre', 'active');
            domClass.remove('invsDatePublish', 'active');
            domClass.remove('invsDuring', 'active');
            queryParams.order_first_column = 'invs_rate';
            if (orderFirstDir3) {
                if (orderFirstDir3 == 'DESC') {
                    queryParams.order_first_dir = 'ASC';
                    orderFirstDir3 = 'ASC';
                    domClass.replace(tenderList.invsAmountPreNode, 'fa fa-arrow-up', 'fa fa-arrow-down');
                } else {
                    queryParams.order_first_dir = 'DESC';
                    orderFirstDir3 = 'DESC';
                    domClass.replace(tenderList.invsAmountPreNode, 'fa fa-arrow-down', 'fa fa-arrow-up');
                }
            } else {//第一次点击
                queryParams.order_first_dir = 'DESC';
                orderFirstDir3 = 'DESC';
            }
            $('#light-pagination').pagination('selectPage', 1);
        });
    }

    return {
        init: function () {
            initView();
            setQuery(queryParams, 1, true);
            Helper.init();
        }
    }
});