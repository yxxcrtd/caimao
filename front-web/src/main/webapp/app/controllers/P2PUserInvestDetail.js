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
    'dojo/domReady!'
], function (Helper, fx, registry, dom, on, Ajax, query, Global, domClass,
             domConstruct, mouse, focusUtil, cookie, Prompt, lang, Tooltip, DateUtil, Data, when, all, P2PSideMenu, Pagination) {

    var config = {};
    var urlParams = Global.getUrlParam(),
        investId = urlParams.investId,
        pagCreated = false,
        investDetail, targetDetail, sideMenu,
        investBox = document.getElementById("investBox"),
        html = investBox.getElementsByTagName("script")[0].innerHTML,
        cutdown = document.getElementById('cutdown'),
        overplus,
        timeLimit = [86400,3600,60,1],
        unit = ['天','小时','分','秒'],
        idx = 0,
        start = 0,
        limit = 10,
        rentmplCache = {};


    function initView() {
        sideMenu = new P2PSideMenu({
            active: '1 2'
        });
        sideMenu.placeAt('sidemenuctn');

        Ajax.get(Global.baseUrl + '/p2p/target/userInvestRecord', {
            investId : investId
        }).then(function (response) {
            if (response && response.data) {
                investDetail = response.data
                setInvestDetail(investDetail);
                Ajax.get(Global.baseUrl + '/p2p/target/detail', {
                    targetId : investDetail.targetId
                }).then(function (response) {
                    if (response && response.data) {
                        targetDetail = response.data;
                        setTargetDetail(targetDetail);
                    }
                });
            }
        });

        setInvestPageInterest();
    }

    function countdownSecond(){
        --overplus;
        idx = 0;
        if(overplus>0){
            cutdown.innerHTML = '项目还剩' + getTimeStr(overplus) + '结束';
            return setTimeout(countdownSecond, 1000);
        }
        cutdown.innerHTML = "项目已结束";
    }

    function formatUnixToDate(unixTIme){
        var tmpDate = new Date(parseInt(unixTIme));
        return tmpDate.getFullYear() + "-" + (tmpDate.getMonth() + 1) + "-" + tmpDate.getDate();
    }

    function setInvestDetail(investDetail){
        dom.byId("investValue").innerHTML = Global.formatAmount(investDetail.investValue, 0) + " 元";
        dom.byId("interestApply").innerHTML = Global.formatAmount(investDetail.payInterest, 0);
        if(investDetail.investStatus == 3){
            dom.byId("interestClose").innerHTML = Global.formatAmount(investDetail.yearValue /12 * investDetail.liftTime / 30 - investDetail.payInterest, 2);
        }
    }

    function setTargetDetail(targetDetail){
        dom.byId("targetMobile").innerHTML = targetDetail.mobile;
        dom.byId("targetUserName").innerHTML = targetDetail.loanUserName;
        dom.byId("targetUserNameF").innerHTML = targetDetail.loanUserName;
        dom.byId("targetLoanAmount").innerHTML = Global.formatAmount(targetDetail.targetAmount);
        dom.byId("targetLiftTime").innerHTML = formatUnixToDate(targetDetail.created) + " - " + formatUnixToDate(targetDetail.created + 86400000 * targetDetail.liftTime) + "（" + targetDetail.liftTime + "天）"
        dom.byId("targetLoanProcess").style.width = (targetDetail.targetRate * 100).toFixed(2) + "%";
        dom.byId("targetRate").innerHTML = (targetDetail.targetRate * 100).toFixed(2);
        dom.byId("targetcaimaoValue").innerHTML = Global.formatAmount(targetDetail.caimaoValue);
        dom.byId("targetLoanWarn").innerHTML = parseInt(targetDetail.targetAmount * 1.1) / 1000000 + "万元";

        query('.targetLoanAmountF',dom.byId('targetInfo')).forEach(function(item,i){
            item.innerHTML = parseInt(targetDetail.targetAmount) / 1000000 + "万元";
        });
        query('.targetMarketValue',dom.byId('targetInfo')).forEach(function(item,i){
            item.innerHTML = parseInt(targetDetail.targetAmount + targetDetail.payMargin) / 1000000 + "万元";
        });
        query('.targetYearRate',dom.byId('targetInfo')).forEach(function(item,i){
            item.innerHTML = parseInt(targetDetail.yearRate * 10000) / 100 + "%";
        });
        query('.targetInvestCnt',dom.byId('targetInfo')).forEach(function(item,i){
            item.innerHTML = targetDetail.investUserNum;
        });
        query('.targetActualValue',dom.byId('targetInfo')).forEach(function(item,i){
            item.innerHTML = Global.formatAmount(targetDetail.actualValue);
        });
        overplus = parseInt(targetDetail.targetLiftTime);
        countdownSecond();
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

    function setInvestPageInterest(){
        Ajax.get(Global.baseUrl + '/p2p/target/userPageInterest', {
            investId : investId,
            start : start,
            limit : limit
        }).then(function (response) {
            if (response && response.data && response.data.items) {
                investBox.innerHTML = rentmpl(html,{
                    data:response.data.items
                });

                if (!pagCreated) {
                    $('#light-pagination').pagination({
                        items: response.data.totalCount,
                        itemsOnPage: limit,
                        prevText: '上一页',
                        nextText: '下一页',
                        onPageClick: function (pageNumber, e) {
                            start = (pageNumber - 1) * limit;
                            setInvestPageInterest();
                        }
                    });
                    pagCreated = true;
                }
            }
        });
    }
    function getTimeStr(t,s){
        var str = s || '',
            ints = ~~(t/timeLimit[idx]),
            rem = t % timeLimit[idx];
        str += ints ? ints + unit[idx] : (s ? 0 + unit[idx] : '');
        ++idx;
        return idx >= timeLimit.length ? str : getTimeStr(rem,str)
    }

    return {
        init: function () {
            initView();
            Helper.init(config);
        }
    }
});