/**
 * this file deals with almost function controllers used
 */
define([
    'app/controllers/Helper',
    'dojo/query',
    'dojo/on',
    'dojo/dom',
    'dojo/dom-style',
    'dojo/dom-class',
    'app/common/Ajax',
    'app/common/Global',
    'dojo/dom-construct',
    'app/common/Date',
    'app/jquery/Pagination',
    'dojo/_base/fx'
], function (Helper, query, on, dom, domStyle, domClass, Ajax, Global, domConstruct, DateUtil, Pagination, fx) {
    var config = {},
        pageCount1 = 10,
        pageCount2 = 10,
        pagCreated1 = false,
        pagCreated2 = false,
        params1 = {
            'start': 0,
            'limit': pageCount1,
            'infoChannel': 1
        },
        params2 = {
            'start': 0,
            'limit': pageCount2,
            'infoChannel': 2
        };

    function setQuery1(params, pageNumber, isFirst) {
        destroyList(dom.byId('moreInfo1'));
        if (isFirst) {
            params.start = 0;
        }
        setList1(params, pageNumber, isFirst);
    }

    function setQuery2(params, pageNumber, isFirst) {
        destroyList(dom.byId('moreInfo2'));
        if (isFirst) {
            params.start = 0;
        }
        setList2(params, pageNumber, isFirst);
    }

    function destroyList(parentEl) {
        domConstruct.empty(parentEl);
    }

    function setList1(params, pageNumber, isFirst) {
        Ajax.get(Global.baseUrl + '/info/titlePage', params1
        ).then(function (response) {
                if (response.success) {
                    if (response && response.data && response.data.items) {
                        for (var i = 0; i < response.data.items.length; i++) {
                            domConstruct.place(
                                domConstruct.toDom('<li class="new-list-item"><a href="' + Global.baseUrl + '/information.htm?id=' + response.data.items[i].id + '' + '" target="_blank" class="class=new-title">' + response.data.items[i].infoTitle + '</a><span class="new-time">' + DateUtil.format(new Date(parseInt(response.data.items[i].createDatetime))) + '</span></li>'), 'moreInfo1');
                        }
                        if (!pagCreated1) {
                            $('#light-pagination1').pagination({
                                items: response.data.totalCount,
                                itemsOnPage: pageCount1,
                                prevText: '上一页',
                                nextText: '下一页',
                                onPageClick: function (pageNumber, e) {
                                    params1.start = (pageNumber - 1) * params1.limit;
                                    setQuery1(params1, pageNumber);
                                }
                            });
                            pagCreated1 = true;
                        }
                    }

                    else {
                        domConstruct.place(domConstruct.toDom('<li class="list-newsIndex-item">无更多公告</li>'), 'info');
                    }
                }
                else {
                    //TODO
                }
            });
    }

    function setList2(params, pageNumber, isFirst) {
        Ajax.get(Global.baseUrl + '/info/titlePage', params2
        ).then(function (response) {
                if (response.success) {
                    if (response && response.data && response.data.items) {
                        for (var i = 0; i < response.data.items.length; i++) {
                            domConstruct.place(
                                domConstruct.toDom('<li class="new-list-item"><a href="' + Global.baseUrl + '/information.htm?id=' + response.data.items[i].id + '' + '" target="_blank" class="class=new-title">' + response.data.items[i].infoTitle + '</a><span class="new-time">' + DateUtil.format(new Date(parseInt(response.data.items[i].createDatetime))) + '</span></li>'), 'moreInfo2');
                        }
                        if (!pagCreated2) {
                            $('#light-pagination2').pagination({
                                items: response.data.totalCount,
                                itemsOnPage: pageCount2,
                                prevText: '上一页',
                                nextText: '下一页',
                                onPageClick: function (pageNumber, e) {
                                    params2.start = (pageNumber - 1) * params2.limit;
                                    setQuery2(params2, pageNumber);
                                }
                            });
                            pagCreated2 = true;
                        }
                    }

                    else {
                        domConstruct.place(domConstruct.toDom('<li class="list-newsIndex-item">无更多资讯</li>'), 'info');
                    }
                }
                else {
                    //TODO
                }
            });
    }

    function initView() {
        $(document).ready(function () {
            $(".s-tab li").click(function () {
                if (!$(this).hasClass("active")) {
                    $(".s-tab li.active").removeClass("active");
                    $(this).addClass("active");


                    $(".tab-contain.active").removeClass("active");
                    $(".tab-contain").eq($(this).index()).addClass("active");
                }
            });
        });
        setList1(params1,1,true);
        setList2(params2,1,true);
    }

    return {
        init: function () {
            initView();
            Helper.init(config);
        }
    }
});