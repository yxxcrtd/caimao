define([
    'app/controllers/Helper',
    'dojo/_base/fx',
    'dijit/registry',
    'dojo/dom',
    'dojo/on',
    'app/common/Ajax',
    'dojo/query',
    'app/common/Global',
    'dojo/dom-class',
    'dojo/dom-construct',
    'dojo/dom-attr',
    'dojo/mouse',
    'dijit/focus',
    'dojo/cookie',
    'app/ux/GenericPrompt',
    'dojo/_base/lang',
    'app/ux/GenericTooltip',
    'app/common/Date',
    'app/common/Data',
    'dojo/when',
    'dojo/promise/all',
    'app/views/common/P2PSideMenu',
    'app/jquery/Pagination',
    'app/ux/GenericDateRange',
    'app/ux/GenericDateQuickSelect',
    'dojo/domReady!'
], function (Helper, fx, registry, dom, on, Ajax, query, Global, domClass,
             domConstruct, domAttr, mouse, focusUtil, cookie, Prompt, lang, Tooltip, DateUtil, Data, when, all, P2PSideMenu, Pagination, DateRange, DateQuickSelect) {

    var config = {};
    var investStatus = "",
        pagCreated = false,
        sideMenu,
        investBox = document.getElementById("investBox"),
        html = investBox.getElementsByTagName("script")[0].innerHTML,
        cutdown = document.getElementById('cutdown'),
        start = 0,
        limit = 10,
        rentmplCache = {},
        dateRange, dateQuickSelect,
        dateStart, dateEnd,
        filterList = dom.byId('filterlist'),
        inquiryBtn = dom.byId('inquirybutton');


    function initView() {
        sideMenu = new P2PSideMenu({
            active: '1 2'
        });
        sideMenu.placeAt('sidemenuctn');

        when(Data.getAccount()).then(function(data) {
            if(data){
                dom.byId('userAvalaibleAmount').innerHTML = Global.formatAmount(data.avalaibleAmount - data.freezeAmount, 2)
            }
        });

        Ajax.get(Global.baseUrl + '/p2p/target/userSummaryInfo', {}).then(function (response) {
            if (response && response.data) {
                setSummaryDetail(response.data);
            }
        });

        dateRange = new DateRange({
            startDate: new Date(),
            endDate: new Date()
        });
        dateQuickSelect = new DateQuickSelect({
            onClick: function () {
                var data = dateQuickSelect.getData();
                dateRange.setValues({
                    startDate: DateUtil.parse(data.start_date),
                    endDate: DateUtil.parse(data.end_date)
                });
                dateStart = data.start_date;
                dateEnd = data.end_date;
                pagCreated = false;
                setUserLoanInvestPage();
            }
        });


        dateRange.placeAt('daterangectn');
        dateQuickSelect.placeAt('datequickselectctn');

        initDate(dateQuickSelect);

        initSetUserLoanInvestPage(dateRange);

        on(inquiryBtn, 'click', function () {
            var data = dateRange.getData();
            dateStart = data.start_date;
            dateEnd = data.end_date;
            pagCreated = false;
            setUserLoanInvestPage();
        });

        on(filterList, 'li:click', function (e) {
            investStatus = domAttr.get(this, 'data-type');
            var activeEl = query('.active', filterList)[0];
            domClass.remove(activeEl, 'active');
            activeEl = query('a', this)[0];
            domClass.add(activeEl, 'active');
            var data = dateRange.getData();
            dateStart = data.start_date;
            dateEnd = data.end_date;
            pagCreated = false;
            setUserLoanInvestPage();
        });
    }

    function initDate(dateQuickSelectX){
        dateQuickSelectX.select(1);
        var data = dateQuickSelectX.getData();
        dateRange.setValues({
            startDate: DateUtil.parse(data.start_date),
            endDate: DateUtil.parse(data.end_date)
        });
        dateStart = data.start_date;
        dateEnd = data.end_date;
    }

    function initSetUserLoanInvestPage(dateRangeX){
        var data = dateRangeX.getData();
        dateStart = data.start_date;
        dateEnd = data.end_date;
        setUserLoanInvestPage();
    }

    function setSummaryDetail(Summarydetail){
        dom.byId("userTotalInvestment").innerHTML = Global.numberFormatS(parseInt(Summarydetail.userTotalInvestment) / 100);
        dom.byId("userTotalIncome").innerHTML = Global.numberFormatS(parseInt(Summarydetail.userTotalIncome) / 100);
        dom.byId("userTotalMarginReceived").innerHTML = Global.numberFormatS(parseInt(Summarydetail.userTotalMarginReceived) / 100);
        dom.byId("userTotalMarginClosed").innerHTML = Global.numberFormatS(parseInt(Summarydetail.userTotalMarginClosed) / 100);
        dom.byId("userTotalInterestReceived").innerHTML = Global.numberFormatS(parseInt(Summarydetail.userTotalInterestReceived) / 100);
        dom.byId("userTotalInterestClosed").innerHTML = Global.numberFormatS(parseInt(Summarydetail.userTotalInterestClosed) / 100);
    }

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

    function setUserLoanInvestPage(){
        Ajax.get(Global.baseUrl + '/p2p/target/userInvestPage', {
            investStatus : investStatus,
            start : start,
            limit : limit,
            startDate : dateStart,
            endDate : dateEnd
        }).then(function (response) {
            if (response && response.data && response.data.items) {
                investBox.innerHTML = rentmpl(html,{
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
                            setUserLoanInvestPage();
                        }
                    });
                    pagCreated = true;
                }
            }
        });
    }

    return {
        init: function () {
            initView();
            Helper.init(config);
        }
    }
});