/**
 * Controller for login, handler with all business event
 * */
define([
    'app/controllers/Helper',
    'dojo/dom',
    'dojo/on',
    'app/common/Ajax',
    'dojo/query',
    'dojo/dom-class',
    'dojo/dom-construct',
    'dojo/dom-style',
    'app/common/RSA',
    'app/common/Global',
    'dojo/cookie',
    'app/views/common/PzSideMenu',
    'app/common/Data',
    'dojo/when',
    'dojo/promise/all',
    'app/views/financing/RepaymentPanel',
    'app/ux/GenericTooltip',
    'app/views/financing/ContractInfoPanel',
    'app/common/Dict',
    'dojo/request/notify',
    'app/jquery/Pagination',
    'dojo/domReady!'
], function (Helper, dom, on,
             Ajax, query, domClass, domConstruct, domStyle, RSA,
             Global, cookie, SideMenu, Data, when, all,
             RepaymentPanel, Tooltip, ContractInfoPanel, Dict, notify,Pagination) {

    var config = {};
    queryParams = {
    		status: 0,
            start: 0,
            limit: 10
     };
    
    function setQuery(params, pageNumber, isFirst) {
	    destoryApplyLoanList();
        if (isFirst) {
            params.start = 0;
        }
        setApplyLoans(params, pageNumber, isFirst);
    }
    
    function setApplyLoans(params, pageNumber, isFirst) {
        when(Data.getApplyLoans(params, true), function (data) {
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
            setList(data);
        });
    }
    
    function destoryApplyLoanList(){
		var items = $('#applyctn div');//获取id为financingctn的下的所有div
        if (items.length > 1) {
            for (var i = 1; i < items.length; i++) {//将除了第一行的所有数据先删除，再赋值
               domConstruct.destroy(items[i]);
            }
        }
	}

    function setList(data){
    	var applys = data.items || [];
        for (var j = 0; j < applys.length; j++) {
            var CP = new ContractInfoPanel({
                noLink: true
            });
            CP.placeAt('applyctn');
            CP.set('apply', applys[j]);
        }
    }
    
    function initView() {

        var sideMenu = new SideMenu({
            active: '2 1'
        });
        sideMenu.placeAt('sidemenuctn');
        
        setQuery(queryParams,1,true);
        var tab = dom.byId('tab');
        //var panels = query('.panel', tab);
        var spans = query('.head span', tab);
        var s_id = 0;
        spans.forEach(function (item, i) {
            on(item, 'click', function () {
                tabView(item, i);
                Tooltip.hide();
            });

        });

        function tabView(item, i) {
            domClass.remove(spans[s_id], 'select');
            domClass.add(item, 'select');
            s_id = i;
            queryParams.status=i;
            setQuery(queryParams,1,true);
        }

    }

    return {
        init: function () {
            (function () {
                Ajax.get(Global.baseUrl + '/sec/rsa', {}).then(function (response) {
                    if (response.success) {
                        modulus = response.data.modulus;
                        exponent = response.data.exponent;
                    } else {
                        //TODO
                    }

                });
            })();
            initView();
            Helper.init(config);
        }
    }
});