/**
 * this file deals with almost function controllers used
 */
define([
    'app/controllers/Helper',
    'app/ux/Swiper',
    'dojo/query',
    'dojo/on',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/dom-style',
    'dojo/dom-class',
    'dojo/when',
    'app/common/Data',
    'app/common/Global',
    'dojo/cookie',
    'dijit/focus',
    'app/ux/GenericTooltip',
    'app/common/RSA',
    'dojo/_base/lang',
    'dojo/_base/config',
    'app/common/Ajax',
    'app/common/Product',
    'app/ux/GenericProgressBar',
    'dojo/number',
    'app/views/common/SiteNoticeBoard',
    'dojo/_base/fx'
], function (Helper, swiper, query, on, dom, domConstruct, domStyle, domClass, when, Data, Global, cookie, focusUtil, Tooltip, RSA,
             lang, cfg, Ajax, Product, ProgressBar, number,NoticeBoard,fx) {
    var config = {},
        loginBtnEl = dom.byId('login_button'),
        usernameEl = dom.byId('username'),
        passwordEl = dom.byId('password'),
        formEl = dom.byId('loginform'),
        errorCount = 0,
        key;

    function initView() {
        var username = cookie('loginname');
        if (username) {
            usernameEl.value = username;
        }
        number.round(10.71, 0, 2.5);
        query('.frame-all-index').on('click', function() { // ie8 not support class delegate
            query('.ui-button', this).addClass('bounce');
        });
        
        /*//获取累计融资
        Ajax.get(Global.baseUrl + '/financing/getLoanTotalAmount', {
        }).then(function(response) {
            if (response.success && response.data) {
            	dom.byId('totalAmountNode').innerHTML=Global.formatAmount(response.data.loanAmount,0,'w');
            }
        });
        //获取会员人数
        Ajax.get(Global.baseUrl + '/user/getRegisterUserCount',{}).then(function(response) {
            if (response.success && response.data) {
            	dom.byId('totalRegisterNode').innerHTML=Global.formatAmount(response.data.count*100,0,'w');
            }
        });*/
        function removeBounce(el) {
            domClass.remove(el, 'bounce');
        }
        query('.frame-all-index .ui-button').on('webkitAnimationEnd', function() {
            removeBounce(this);
        });
        query('.frame-all-index .ui-button').on('mozAnimationEnd', function() {
            removeBounce(this);
        });
        query('.frame-all-index .ui-button').on('animationend', function() {
            removeBounce(this);
        });
        when(Data.getUser(), function (user) {
            if (!user) {
                dom.byId('loginform_div').style.display = 'block';
            }
        });

        /*if (cfg.isProFree === "true") {
            when(Data.getProduct({'product_id': Product.getProductId(0)}, true), function (data) {
                dom.byId('pt0-desc').innerHTML = Global.delHtmlTag(data.prodNote);
                dom.byId('pt0-loanamount').innerHTML = Global.formatAmount(data.prodAmountMax, 0, 'w');
                dom.byId('pt0-cashamount').innerHTML = Global.formatAmount(data.prodAmountMax / data.prodLoanRatioMax, 0, 'w');
                Product.getBillText();
            });
        }
        if (cfg.isProDay === "true") {
            when(Data.getProduct({'product_id': Product.getProductId(1)}, true), function (data) {
                var days = data.prodTerms.split(',');
                dom.byId('dayrange-1').innerHTML = parseInt(days[0]);
                dom.byId('maxamount-1').innerHTML = Global.formatAmount(data.prodAmountMax, 0, 'w');
                dom.byId('maxratio-1').innerHTML = data.prodLoanRatioMax;
                dom.byId('maxprofit-1').innerHTML = data.prodLoanRatioMax;
            });
        }

        if (cfg.isProMonth === "true") {
            when(Data.getProduct({'product_id': Product.getProductId(2)}, true), function (data) {
                var days = data.prodTerms.split(',');
                dom.byId('dayrange-2').innerHTML = (parseInt(days[0]) / 30).toFixed(0) + '-' + (parseInt(days[days.length - 1]) / 30).toFixed(0);
                dom.byId('maxamount-2').innerHTML = Global.formatAmount(data.prodAmountMax, 0, 'w');
                dom.byId('maxratio-2').innerHTML = data.prodLoanRatioMax;
                dom.byId('maxprofit-2').innerHTML = data.prodLoanRatioMax;
            });
        }

        if (cfg.isProP2P === "true") {
            when(Data.getTenders({
                order_first_column: 'invs_rate',
                order_first_dir: 'DESC',
                is_able_pay: 1,
                start: 0,
                limit: 1
            }, true), function (res) {
                var items = res.items || [],
                    item;
                if (items.length > 0) {
                    item = items[0];
                }
                if (item) {
                    dom.byId('pro-p2p-ctn').style.display = 'block';
                    var rate = item.invsAmountActual / item.invsAmountPre;
                    dom.byId('p2p-rate').innerHTML = Global.formatNumber(item.invsRate * 100);
                    dom.byId('p2p-day').innerHTML = Global.formatNumber(item.invsDuring / 30, 0);
                    dom.byId('p2p-amount').innerHTML = Global.formatAmount(item.invsAmountPre);
                    dom.byId('p2p-amountleft').innerHTML = Global.formatAmount(item.invsAmountPre - item.invsAmountActual);
                    dom.byId('p2p-btn').href = Global.baseUrl + '/tenderdetail.htm?invs=' + item.invsId;
                    var progressBar = new ProgressBar({
                        type: 2,
                        value: rate
                    }, 'p2p-progressbar');
                    progressBar.set('text', Global.formatNumber(rate * 100) + '%');
                } else {
                    domConstruct.destroy(dom.byId('pro-p2p-ctn'));
                }
            });
        }*/

        // 显示投资列表
        // 显示总数的东东
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
        var targetBox = document.getElementById("targetBox"),
        html = targetBox.getElementsByTagName("script")[0].innerHTML;
        // TODO 点击切换的效果
        query('.top1 li').forEach(function(item,i){
            on(item,'click',function(){
                domClass.remove(query('.top1 li')[0], 'active');
                domClass.remove(query('.top1 li')[1], 'active');
                domClass.add(item,'active');
                if(i == 0){
                    // 显示正在进行的投标
                    setLoanPage('year_rate', 0, 0, 5)
                } else {
                    setLoanPage('target_full_datetime', 0, 1, 5)
                }
            });
        });
        setLoanPage('year_rate', 0, 0, 5);
        // TODO 设置那个啥，哈哈
        function setLoanPage(order, asc, targetStatus, limit){
            Ajax.get(Global.baseUrl + '/p2p/target/page', {
                order : order,
                asc : asc,
                start : 0,
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
                        targetBox.innerHTML = rentmpl(html, {
                            data : records
                        });
                    }
                }
            });
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

        // 显示融资排行与实时动态
        Ajax.get(Global.baseUrl + '/other/data/pz_ranking',{}).then(function(response) {
            if (response.success && response.data) {
                var classArr = {1 : "icon_one", 2 : "icon_two", 3 : "icon_three", 4 : "icon_shu", 5 : "icon_shu"};
                var pzRankingHtml = "";
                for (var i = 0; i < response.data.length; i++) {
                    pzRankingHtml += "<tr><td><i class=\"icon "+classArr[i+1]+"\" style=\"text-align:left;\">"+(i+1)+"</i></td>" +
                    "<td>" + response.data[i].mobile + "</td>" +
                    "<td><span class=\"font_orange\">"+Global.formatAmount(response.data[i].cashAmount, 0, 1)+"元</span> </td>" +
                    "<td>" + response.data[i].loanRatio + "倍</td>" +
                    "<td>"+Global.formatAmount(response.data[i].loanAmount, 0, 1)+"元</td>";
                }
                dom.byId('pzRanking').innerHTML = pzRankingHtml;
            }
        });
        // 显示融资实时动态
        Ajax.get(Global.baseUrl + '/other/data/pz_realtime',{}).then(function(response) {
            if (response.success && response.data) {
                var pzRealtimeHtml = "";
                for (var i = 0; i < response.data.length; i++) {
                    pzRealtimeHtml += "<tr><td>" + response.data[i].applyDatetime + "</td><td>"+response.data[i].mobile+"</td><td><span class=\"font_orange\">"+Global.formatAmount(response.data[i].loanAmount, 0, 1)+"</span>元</td></tr>";
                }
                dom.byId('pzRealetime').innerHTML = pzRealtimeHtml;
            }
        });


        var isValid = function () {
            var username = lang.trim(dom.byId('username').value),
                password = dom.byId('password').value;
            if (!username) {
                errorCount++;
                domClass.add(usernameEl.parentNode, 'ui-form-item-error');
                setTimeout(function () {
                    Tooltip.show('请输入登录号', usernameEl.parentNode, 'warning');
                }, 300);

                return false;
            }
            else if (!password) {
                errorCount++;
                domClass.add(passwordEl.parentNode, 'ui-form-item-error');
                setTimeout(function () {
                    Tooltip.show('请输入密码', passwordEl.parentNode, 'warning');
                }, 300);

                return false;
            }
            return true;
        };

        var parseUrl = function() {
            var paramIndex = location.href.indexOf('?'),
                paramsObj = {};
            if (paramIndex > 0) {
                var params = location.href.slice(paramIndex + 1).split('&');
                for (var i = 0, len = params.length; i < len; i++) {
                    var dict = params[i].split('=');
                    paramsObj[dict[0]] = dict[1];
                }
            }
            return paramsObj;
        }

        var onLogin = function () {
            if (errorCount == 3) {
                location.href = Global.baseUrl + '/user/login.htm?c=' + errorCount;
            }
            if (isValid()) {
                Ajax.post(Global.baseUrl + '/user/login', {
                    'login_name': lang.trim(usernameEl.value),
                    'login_pwd': RSA.encryptedString(key, passwordEl.value)
                }).then(function (response) {
                    if (response.success) {
                        // firefox save password prompt
                        // ajax want to show the password prompt, for chrome, set the password field in the form element
                        // for ff, submit the form
                        //var loginForm = dom.byId('loginform');
                        //loginForm.submit(); // this will pop the prompt for ff
                        cookie('username', dom.byId('username').value, {path: '/'});
                        cookie('loginname', dom.byId('username').value, {path: '/'});
                        var returnUrl = parseUrl().url; // reload to refer url
                        if (returnUrl) {
                            location.href = returnUrl;
                        } else {
                            location.href = Global.baseUrl + '/home/index.htm';
                        }

                    } else {
                        setTimeout(function () {
                            Tooltip.show(response.msg, loginBtnEl, 'warning');
                        }, 300);
                        passwordEl.value = '';
                        focusUtil.focus(passwordEl);
                        errorCount++;
                    }
                }, function () {
                });
            }
        };
        on(loginBtnEl, 'click', onLogin);
    }

    return {
        init: function () {
            Ajax.get(Global.baseUrl + '/sec/rsa', {
            }).then(function (response) {
                if (response.success) {
                    key = RSA.getKeyPair(response.data.exponent, '', response.data.modulus);
                } else {
                }

            });
            initView();
            var swiperConfig = new swiper({
                node: ".fullSlide",
                time: 8000
            });
            Helper.init(config);

            // place site notice board
            var snBoard = new NoticeBoard();
            snBoard.placeAt(document.body,0);
        }
    }
});