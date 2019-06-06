/**
 * this file deals with almost function controllers used
 */
define([
    'app/controllers/Helper',
    'app/ux/Swiper',
    'dojo/query',
    'dojo/on',
    'dojo/dom',
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
    'app/common/Ajax',
    'app/common/Product',
    'app/ux/GenericComboBox',
    "dojo/store/Memory",
    'dojo/_base/fx'
], function (Helper, swiper, query, on, dom, domStyle, domClass,
             when, Data, Global, cookie, focusUtil, Tooltip, RSA, lang,Ajax,Product,ComboBox, Memory, fx) {
    var config = {},
        loginBtnEl = dom.byId('login_button'),
        usernameEl = dom.byId('username'),
        passwordEl = dom.byId('password'),
        formEl = dom.byId('loginform'),
        errorCount = 0,
        key;

    function flip(inPanel, outPanel, callback) {
        domClass.remove(outPanel, 'flipin');
        domClass.add(outPanel, 'flip flipout');
        setTimeout(function() {
            outPanel.style.display = 'none';
            inPanel.style.display = 'block';
            domClass.remove(inPanel, 'flipout');
            domClass.add(inPanel, 'flip flipin');
            if (callback) {
                callback();
            }
        }, 225);
    }

    function initView() {

        var themeCombo = new ComboBox({
            store: new Memory({
                data: [{
                    id: 'cobalt', name: 'Cobalt'
                }, {
                    id: 'vermeil', name: 'Vermeil'
                }]
            }),
            searchAttr: 'id',
            labelAttr: 'name',
            item: {id: 'vermeil', name: 'Vermeil'},
            editable: false,
            style: {
                width: '70px',
                position: 'fixed',
                right: '20px',
                top: '40px',
                zIndex: '100000'
            },
            onChange: function(v) {
                domClass.remove(document.body, 'cobalt');
                domClass.remove(document.body, 'vermeil');
                domClass.add(document.body, v);
            }
        });
        themeCombo.placeAt(document.body);

        query('li', dom.byId('headmenu')).at(0).addClass('active');
        when(Data.getUser(), function(user) {
            if (!user) {
                dom.byId('loginform').style.display = 'block';
            }
        });
        when(Data.getProduct({'product_id': Product.getProductId(1)}, true), function(data) {
            dom.byId('dayrange-4').innerHTML = data.prodTerms + '-N';
            dom.byId('maxamount-4').innerHTML = Global.formatAmount(data.prodAmountMax, 0, 'w');
            dom.byId('maxratio-4').innerHTML = data.prodLoanRatioMax;
            dom.byId('maxprofit-4').innerHTML = data.prodLoanRatioMax;
        });
        when(Data.getProduct({'product_id': Product.getProductId(2)}, true), function(data) {
            var days = data.prodTerms.split(',');
            dom.byId('dayrange-3').innerHTML = parseInt(days[0]) / 30 + '-' + parseInt(days[days.length - 1]) / 30;
            dom.byId('maxamount-3').innerHTML = Global.formatAmount(data.prodAmountMax, 0, 'w');
            dom.byId('maxratio-3').innerHTML = data.prodLoanRatioMax;
            dom.byId('maxprofit-3').innerHTML = data.prodLoanRatioMax;
        });
        var isValid = function () {
            var username = lang.trim(dom.byId('username').value),
                password = dom.byId('password').value;
            if (!username) {
                errorCount++;
                domClass.add(usernameEl.parentNode, 'ui-form-item-error');
                setTimeout(function() {
                    Tooltip.show('请输入登录号', usernameEl.parentNode, 'warning');
                }, 300);

                return false;
            }
            else if (!password) {
                errorCount++;
                domClass.add(passwordEl.parentNode, 'ui-form-item-error');
                setTimeout(function() {
                    Tooltip.show('请输入密码', passwordEl.parentNode, 'warning');
                }, 300);

                return false;
            }
            return true;
        };

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
                        var loginForm = dom.byId('loginform');
                        loginForm.submit(); // this will pop the prompt for ff
                        cookie('username', dom.byId('username').value, {path: '/'});
                        cookie('loginname', dom.byId('username').value, {path: '/'});
                        var returnUrl = parseUrl().url; // reload to refer url
                        if (returnUrl) {
                            location.href = returnUrl;
                        } else {
                            location.href = Global.baseUrl + '/home/index.htm';
                        }

                    } else {
                        setTimeout(function() {
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
                time: 3000
            });
            Helper.init(config);
        }
    }
});