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
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'app/common/RSA',
    'app/common/User',
    'dojo/_base/config',
    'app/ux/GenericWindow',
    'dojo/domReady!'
], function (Helper, fx, registry, dom, on, Ajax, query, Global, domClass,
             domConstruct, mouse, focusUtil, cookie, Prompt, lang, Tooltip, DateUtil, Data, when, all, P2PSideMenu, Pagination, TextBox, Button, RSA, User, cfg, Win) {

    var config = {};
    var urlParams = Global.getUrlParam(),
        protocolToggler = dom.byId('protocoltoggler'),
        win,
        targetId = urlParams.targetId,
        pagCreated = false,
        detail, sideMenu,
        investBox = document.getElementById("investBox"),
        html = investBox.getElementsByTagName("script")[0].innerHTML,
        cutdown = document.getElementById('cutdown'),
        overplus,
        investMinValue,
        timeLimit = [86400,3600,60,1],
        unit = ['天','小时','分','秒'],
        idx = 0,
        start = 0,
        limit = 10,
        rentmplCache = {},
        nextBtn,yearRate = 0;


    // show protocol 借款协议点击的
    on(protocolToggler, 'click', function(e) {
        if (!win) {
            win = new Win({
                width: 900,
                height: 500,
                title: '借款协议'
            });
            win.placeAt(document.body);
        }
        win.show();
        when(Data.getProtocol({id: 3}, true), function(tmpl) {
            win.set('msg', tmpl);
        });
        e.stopPropagation();
    });
    // checkbox protocol
    on(dom.byId('checkbox1'), 'click', function() {
        nextBtn.set('disabled', !this.checked);
    });

    function initView() {
        sideMenu = new P2PSideMenu({
            active: '1 1'
        });
        sideMenu.placeAt('sidemenuctn');

        var investAmountFld,pwdFld;

        investAmountFld = new TextBox({
            label:'投资金额',
            labelStyle:{
                color:'#999',
                left: '-130px'
            },
            style:'margin-left: 80px;width:100px;',
            validates: [
                {
                    pattern: /.+/,
                    message: '请输入投资金额'
                }
            ],
            unit: '元',
            unitStyle:{
                left:'5px',
                marginTop: '-18px',
                zIndex:'-1'
            },
            limitRegex: /[\d\.]/,
            isAmount: true,
            isNumber: true
        }, dom.byId("investAmount"));

        on(investAmountFld, 'keyup', function() {
            dom.byId("yujishouyi").innerHTML = Global.formatAmount(this.textbox.value * yearRate / 12, 2);
        });

        pwdFld = new TextBox({
            label: '安全密码',
            labelStyle:{
                color:'#999',
                left: '-130px'
            },
            validates: [{
                pattern: /.+/,
                message: '请输入安全密码'
            }],
            style: 'margin: 20px auto 0px;margin-left:80px;color:#999 !important;',
            type: 'password'
        }, dom.byId("secPwd"));

        nextBtn = new Button({
            label: '投资',
            enter: true,
            disabled: !dom.byId('checkbox1').checked,
            width: 200,
            onClick: function () {
                if (investMinValue != 0) {
                    // 检查是否值最小值的整数倍
                    if (investAmountFld.get('value') % investMinValue != 0) {
                        investAmountFld.displayMessage('投资金额必须是'+investMinValue+"的整数倍", "Error");
                        return false;
                    }
                }
                if (investAmountFld.checkValidity() && pwdFld.checkValidity()) {
                    nextBtn.loading(true);
                    Ajax.post(Global.baseUrl+'/p2p//invest/apply', {
                        trade_pwd: RSA.encryptedString(key, pwdFld.get('value')),
                        invest_value: investAmountFld.get('value'),
                        target_id: targetId
                    }).then(function (response) {
                        nextBtn.loading(false);
                        if (response.success) {
                            dom.byId('targetInfo').style.display="none";
                            var successPanel = new Prompt({
                                type: 'success',
                                msg: '恭喜您，投资成功！',
                                linkText: '查看投资记录',
                                link:'p2p/userInvestPage.htm'
                            });
                            successPanel.placeAt('no_content', 'first');
                        }else{
                            Tooltip.show(response.msg, nextBtn.innerNode, 'warning');
                        }
                        });
                }
            }
        }, 'touzi');

        when(Data.getUser(), function(user) {
            if (user) {
                when(User.isTrust(function () {
                    User.isTradePwd(null, function () {
                        nextBtn.set('disabled', true);
                        nextBtn.set('disabledMsg', '您还未设置安全密码，请先<a href="' + Global.baseUrl + '/account/tradepwd/set.htm">设置</a>');
                    });
                }, function () {
                    nextBtn.set('disabled', true);
                    nextBtn.set('disabledMsg', '请您先完成<a target="_blank" href="' + Global.baseUrl + '/user/' + (cfg.authentication ? 'uploadID.htm' : 'certification.htm') + '">实名认证</a>');
                }));
            } else {
                // 未登录
                nextBtn.set('disabled', true);
                nextBtn.set('disabledMsg', '请您进行<a target="_blank" href="' + Global.baseUrl + '/user/login.htm">登录</a>');
            }
        });

        // 获取投资相关的配置
        Ajax.get(Global.baseUrl + '/p2p/product/config', {
            product_id : 0
        }).then(function(response) {
            if (response && response.data) {
                investMinValue = response.data.invest_min_value;
            }
        });

        // 获取产品详情
        Ajax.get(Global.baseUrl + '/p2p/target/detail', {
            targetId : targetId
        }).then(function (response) {
            if (response && response.data) {
                detail = response.data;
                setTargetDetail(detail);
            }
        });



        setLoanInvestPage();
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

    function setTargetDetail(targetDetail){
        dom.byId("targetMobile").innerHTML = targetDetail.mobile;
        dom.byId("targetUserName").innerHTML = targetDetail.loanUserName;
        dom.byId("targetUserNameF").innerHTML = targetDetail.loanUserName;
        dom.byId("targetLoanAmount").innerHTML = Global.formatAmount(targetDetail.targetAmount);
        dom.byId("targetLiftTime").innerHTML = formatUnixToDate(targetDetail.created) + " - " + formatUnixToDate(targetDetail.created + 86400000 * targetDetail.liftTime) + "（" + targetDetail.liftTime + "天）"
        dom.byId("targetLoanProcess").style.width = (targetDetail.targetRate * 10000) / 100 + "%";
        dom.byId("targetRate").innerHTML = (targetDetail.targetRate*100).toFixed(2);
        dom.byId("targetcaimaoValue").innerHTML = Global.formatAmount(targetDetail.caimaoValue);
        dom.byId("targetLoanWarn").innerHTML = parseInt(targetDetail.targetAmount * 1.1) / 1000000 + "万元";

        query('.targetLoanAmountF',dom.byId('targetInfo')).forEach(function(item,i){
            item.innerHTML = parseInt(targetDetail.targetAmount) / 1000000 + "万元";
        });
        query('.targetMarketValue',dom.byId('targetInfo')).forEach(function(item,i){
            item.innerHTML = parseInt(targetDetail.targetAmount + targetDetail.payMargin) / 1000000 + "万元";
        });
        query('.targetYearRate',dom.byId('targetInfo')).forEach(function(item,i){
            yearRate = (targetDetail.yearRate * 100).toFixed(2);
            item.innerHTML = yearRate + "%";
        });
        query('.targetInvestCnt',dom.byId('targetInfo')).forEach(function(item,i){
            item.innerHTML = targetDetail.investUserNum;
        });
        query('.targetActualValue',dom.byId('targetInfo')).forEach(function(item,i){
            item.innerHTML = Global.formatAmount(targetDetail.actualValue);
        });
        overplus = parseInt(targetDetail.targetLiftTime);
        countdownSecond();
        // 最高投资数量
        dom.byId("maxInvestAmountNode").innerHTML = Global.formatAmount(targetDetail.targetOver);

        // 已经满标了，置灰按钮
        if (targetDetail.targetRate == 1) {
            nextBtn.addDisabledMsg([3, '已满标']);
        } else {
            if (targetDetail.targetLiftTime <= 0) {
                nextBtn.addDisabledMsg([3, '项目已结束']);
            }
        }
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

    function setLoanInvestPage(){
        Ajax.get(Global.baseUrl + '/p2p/target/loanInvest', {
            targetId : targetId,
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
                            setLoanInvestPage();
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

    function initRSA() {
        Ajax.get(Global.baseUrl + '/sec/rsa', {
        }).then(function (response) {
            if (response.success) {
                key = RSA.getKeyPair(response.data.exponent, '', response.data.modulus);
            } else {
            }

        });
    }

    return {
        init: function () {
            initView();
            initRSA();
            Helper.init(config);
        }
    }
});