define([
    'app/controllers/Helper',
    "dojo/_base/window",
    "dojo/dom",
    "dojo/on",
    "app/common/Ajax",
    "app/common/Global",
    "dojo/dom-class",
    "app/ux/GenericNumberSpinner",
    "dijit/Dialog",
    "app/ux/GenericTextBox",
    "app/ux/GenericButton",
    "dojo/store/Memory",
    "dijit/form/FilteringSelect",
    'app/ux/GenericMsgBox',
    "dojox/widget/Standby",
    "dojo/query",
    "dojo/io-query",
    'app/ux/GenericComboBox',
    'app/stores/GenericQueryReadStore',
    'app/views/common/TradeSideMenu',
    'app/common/RSA',
    'dojo/keys',
    "dojo/domReady!"
], function (Helper, win, dom, on, Ajax, Global,
             domClass, NumberSpinner, Dialog, TextBox, Button,
             Memory, FilteringSelect, MsgBox, Standby,
             query, ioQuery, ComboBox, QueryReadStore, TradeSideMenu, RSA,keys) {

    var config = {};
    //所有显示的实时数据
    var allItems = ["stockName", "stockCode", "upPrice", "downPrice", "newPrice", "chargeRate", "chargeValue", "buyPrice1",
        "buyPrice2", "buyPrice3", "buyPrice4", "buyPrice5", "sellPrice1", "sellPrice2", "sellPrice3", "sellPrice4",
        "sellPrice5", "buyCount1", "buyCount2", "buyCount3", "buyCount4", "buyCount5", "sellCount1", "sellCount2",
        "sellCount3", "sellCount4", "sellCount5"];
    //所有需判断红涨绿跌
    var items = ["newPrice", "buyPrice1", "buyPrice2", "buyPrice3", "buyPrice4", "buyPrice5",
        "sellPrice1", "sellPrice2", "sellPrice3", "sellPrice4", "sellPrice5"
        , "upPrice", "downPrice"];

    var regex = /^[0]+\.?[0]*$/,  //正则判断是否是0
        intervalFlag = null,
        codeBox = null,
        accountSelect = null,
        priceSpinner = null,
        amountSpinner = null,
        tradePwdBox = null,
        msgBox = null,
        paramObject = {},
        key,
        code,
        max = '0.0',
        min ='0.0',
        buyBtn = null;
    var navBarItems = query('.subnav-item', 'subheader');
    var sideMenu;

    function initView() {
        sideMenu = new TradeSideMenu({
            active: '1 2'
        });
        sideMenu.placeAt('sidemenuctn');
        var url = window.location.href;
        var urlParam = url.substring(url.indexOf("?") + 1, url.length);
        paramObject = ioQuery.queryToObject(urlParam);

        //初始化交易子账号下拉框
        Ajax.get(Global.baseUrl + '/homs/combineid', {}).then(function (response) {
            var selectData = [];
            var firstItem;
            if (response.success) {
                var data = response.data;
                for (var i = 0; i < data.length; i++) {
                    var item = {};
			    	item.name = data[i].operatorNo + "  " + data[i].prodName;
			    	item.id = data[i].homsCombineId;
			    	item.prodName = data[i].prodName;
                    //item.cur_amount = data[i].cur_amount;
                    item.homsFundAccount = data[i].homsFundAccount;
                    selectData.push(item);

                    if (i == 0) {
                        firstItem = item;
                    }
                }
                firstItem = Global.findObj(selectData, 'homsFundAccount', paramObject.fundaccount, 'id', paramObject.combine) || firstItem;
                queryAssets(firstItem);
            }

            var accountStore = new Memory({
                data: selectData
            });
            accountSelect = new ComboBox({
                id: "accountSelect",
                store: accountStore,
                searchAttr: 'id',
    			labelAttr: 'name',
    			item: firstItem,
                style: 'width:200px;margin-left:10px;margin-bottom:0px',
                editable: false,
                onChange: function (value) {
                    queryAssets(value);
                }
            }, "childAccountDiv");
            //accountSelect.startup();

            //初始化完交易子账号，若发现有股票代码，则初始化交易信息
            if (paramObject.code) {
                code = paramObject.code.replace(/\W.*/, "");
                Ajax.get(Global.baseUrl + "/quote/stock/stock_data/" + code, {}).then(function (response) {
                    if (response.success) {
                        var data = response.data;

                        if (data.length == 0) {
                            codeBox.set("value", code);
                        } else {
                            codeBox.set("value", data[0].stockCode + " " + data[0].stockName);
                        }
                    } else {
                        codeBox.set("value", paramObject.code);
                    }
                    getCodeInfo();
                });
            }
        });

        //初始化键盘精灵
        var store = new QueryReadStore({
            url: Global.baseUrl + "/quote/stock/stock_data",
            requestMethod: "get"
        });
        codeBox = new ComboBox({
            id: "codeBox",
            name: "state",
            store: store,
            style: 'width:200px;margin-left:10px;margin-bottom:0px;',
            maxlength: 6,
            validates: [{
                pattern: /.+/,
                message: '请输入证券代码'
            }, {
                pattern: /^\d{6}.*$/,
                message: '证券代码格式有误，请重新输入'
            }],
            autoComplete: false,
            hasDownArrow: false,
            onKeyUp: function (e) {
                var regex = /[A-Za-z0-9]+\s\S+/;
                var value = codeBox.get("value");
                if (!regex.test(value)) {
                    codeBox.set("value", value.replace(/\W.*/, ""));
                }

                var charOrCode = e.charCode || e.keyCode;
                if (charOrCode == 13) {
                    amountSpinner.focus();
                    e.preventDefault();
                }
            },
            onBlur: function () {
                var regex = /[A-Za-z0-9]+\s\S+/;
                var codeRegex = /\d{6}/;
                code = codeBox.getFilterValue();
                if (this.dropDown && code != "") {
                    var matches = this.dropDown.containerNode.firstChild.nextSibling.innerText;
                    if (regex.test(matches) && !codeBox.item) {
                        if (codeRegex.test(code) && matches.indexOf(code) < 0) {
                            Ajax.get(Global.baseUrl + "/quote/stock/stock_data/" + code, {}).then(function (response) {
                                if (response.success) {
                                    var data = response.data;
                                    if (data.length != 0) {
                                        codeBox.set("value", data[0].stockCode + " " + data[0].stockName);
                                    }
                                }
                            });
                        } else {
                            codeBox.set("value", matches);
                        }
                    }
                    getCodeInfo();
                }

                codeBox.checkValidity(); // valid after set value
            }
        }, "stock_code");
        //codeBox.startup();

        //初始化价格微调器
        priceSpinner = new NumberSpinner({
            smallDelta: 0.01,
            constraints: {min: 0.01, max: 10000},
            id: "priceSpinner",
            style: "width:210px;margin-bottom:0px;",
            onChange: function () {
                getBuyable();
            },
            validates: [{
                pattern: /.+/,
                message: '请输入委托价格'
            }]
        }, "priceSpinnerDiv");
        //priceSpinner.startup();

        //初始化数量微调器
        amountSpinner = new NumberSpinner({
            smallDelta: 100,
            constraints: {min: 100, max: 1000000},
            id: "amountSpinner",
            style: "width:210px;margin-bottom: 0px;",
            validates: [{
                pattern: /.+/,
                message: '请输入委托数量'
            }, {
                pattern: function () {
                    var value = parseFloat(this.get('value'));
                    var buyAmount=dom.byId("buyalbeAmount").innerHTML;
                    if(parseFloat(value)>parseFloat(buyAmount)){
                    	return false;
                    }
                    return true;
                },
                message: '超出可买数量'
            },{
                pattern: function () {
                    var value = parseFloat(this.get('value'));
                    return value%100==0 ? true : false;
                },
                message: '必须是100的整数倍'
            },{
                pattern: function () {
                    var value = parseFloat(this.get('value'));
                    return parseFloat(value)==0 ? false : true;
                },
                message: '委托数量为0，不可买入！'
            }],
            onKeyUp: function (event) {
                switch (event.keyCode) {
                    case 13:
                        tradePwdBox.focus();
                        break;
                }
            },
            onKeyPress: function(event) {
            	var charOrCode = event.charCode || event.keyCode;
				switch(charOrCode){
			      case keys.LEFT_ARROW:
			      case keys.UP_ARROW:
			      case keys.DOWN_ARROW:
			      case keys.RIGHT_ARROW:
			        break;
			      case keys.ENTER:
			    	  this.textbox.blur();
			    	  var btns = query('.dijitButton.enterbutton');
			    	  var tempBtns = [];
			    	  btns.forEach(function(btnEl) {
							var btn = registry.byNode(btnEl);
							if (!btn.get('disabled') && btnEl.offsetHeight !==0 && (btn.handler || btn.onClick)) {
								tempBtns.push(btn);
							}
						});
			    	  if (tempBtns.length > 0) {
			    		  (tempBtns[0].handler || tempBtns[0].onClick).call(tempBtns[0]);
			    	  }
			    	  event.preventDefault();
				      break;
			      case keys.BACKSPACE:
			        break;
			      case keys.TAB:
			        break;
			      case keys.ESCAPE:
			        break;
			      default:
			          var cc = String.fromCharCode(charOrCode);
			          var reg=/[\d]/;
			          if(!reg.test(cc)){
			        	  event.preventDefault();
			          }
                      if (cc == '.') {
                          event.preventDefault();
                      }
			      }
            } 
        }, "amountSpinnerDiv");
        //amountSpinner.startup();

        //初始化密码输入框
        tradePwdBox = new TextBox({
            name: "tradePwdBox",
            style: "width:200px; margin-bottom: 0px;",
            type: "password",
            leftOffset: 10,
            validates: [{
                pattern: /.+/,
                message: '请输入安全密码'
            }],
            onKeyUp: function (e) {
                var charOrCode = e.charCode || e.keyCode;
                if (charOrCode == 13) {
                    e.preventDefault();
                }
            }
        }, "tradePwd");
        //tradePwdBox.startup();

        if (!paramObject.code) {
            //初始化分时图
            drawEmptyChart('timeShareChart');
        }

        // add buy button
        buyBtn = new Button({
            label: '买入',
            innerStyle: {
                width: '198px'
            },
            enter: true,
            leftOffset: 130
        });
        buyBtn.placeAt('buybuttonctn');
    }

    function isValid() {
        return codeBox.checkValidity() &&
            priceSpinner.checkValidity() &&
            amountSpinner.checkValidity() &&
            tradePwdBox.checkValidity();

    }

    function addListeners() {

        on(dom.byId("allAmount"), "click", function () {
            clearChecked();
            domClass.add("allAmount", "active");
            var maxAmount = "0";
            maxAmount = Math.floor(parseFloat(dom.byId("buyalbeAmount").innerHTML) / 100) * 100;
            maxAmount = isNaN(maxAmount) ? 0 : maxAmount;
            maxAmount = maxAmount > 1000000 ? 1000000 : maxAmount;
            maxAmount=Math.floor(maxAmount/100)*100;//取100的整数倍
            amountSpinner.set("value", maxAmount);
        });

        on(dom.byId("halfAmount"), "click", function () {
            clearChecked();
            domClass.add("halfAmount", "active");
            var maxAmount = "0";
            maxAmount = Math.floor(parseFloat(dom.byId("buyalbeAmount").innerHTML) / 200) * 100;
            maxAmount = isNaN(maxAmount) ? 0 : maxAmount;
            maxAmount = maxAmount > 1000000 ? 1000000 : maxAmount;
            maxAmount=Math.floor(maxAmount/100)*100;//取100的整数倍
            amountSpinner.set("value", maxAmount);
        });

        on(dom.byId("oneThirdAmount"), "click", function () {
            clearChecked();
            domClass.add("oneThirdAmount", "active");
            var maxAmount = "0";
            maxAmount = Math.floor(parseFloat(dom.byId("buyalbeAmount").innerHTML) / 300) * 100;
            maxAmount = isNaN(maxAmount) ? 0 : maxAmount;
            maxAmount = maxAmount > 1000000 ? 1000000 : maxAmount;
            maxAmount=Math.floor(maxAmount/100)*100;//取100的整数倍
            amountSpinner.set("value", maxAmount);
        });

        on(dom.byId("quarterAmount"), "click", function () {
            clearChecked();
            domClass.add("quarterAmount", "active");
            var maxAmount = "0";
            maxAmount = Math.floor(parseFloat(dom.byId("buyalbeAmount").innerHTML) / 400) * 100;
            maxAmount = isNaN(maxAmount) ? 0 : maxAmount;
            maxAmount = maxAmount > 1000000 ? 1000000 : maxAmount;
            maxAmount=Math.floor(maxAmount/100)*100;//取100的整数倍
            amountSpinner.set("value", maxAmount);
        });


        on(buyBtn, 'click', function () {
            if (!isValid()) {
                return;
            }

            var homs_combine_id = accountSelect.value;
            var homs_fund_account = accountSelect.get('store').get(homs_combine_id).homsFundAccount;
            var stock_code = codeBox.getFilterValue();
            var stock_name = dom.byId("stock_name").innerHTML;
            var entrust_price = priceSpinner.get("value");
            var entrust_amount = amountSpinner.get("value");
            var exchange_type = dom.byId("exchange_type").value;
            var user_trade_pwd = tradePwdBox.get("value");
            var data = {
                'homs_combine_id': homs_combine_id,
                'homs_fund_account': homs_fund_account,
                'stock_code': stock_code,
                'entrust_quantity': entrust_amount,
                'entrust_price': entrust_price,
                'exchange_type': exchange_type,
                'trade_pwd': RSA.encryptedString(key, user_trade_pwd)
            };

            var content = "<div class=\"ui-form-item\"><label class=\"ui-label\">交易子账号：</label><b class=\"ui-text am-ft-md\">" + homs_combine_id + "</b></div></div>"
                + "<div class=\"ui-form-item\"><label class=\"ui-label\">证券代码：</label><b class=\"ui-text am-ft-md\">" + stock_code + "</b></div></div>"
                + "<div class=\"ui-form-item\"><label class=\"ui-label\">证券名称：</label><b class=\"ui-text am-ft-md\">" + stock_name + "</b></div></div>"
                + "<div class=\"ui-form-item\"><label class=\"ui-label\">买卖方向：</label><b class=\"ui-text am-ft-md\">委托买入</b></div></div>"
                + "<div class=\"ui-form-item\"><label class=\"ui-label\">委托价格：</label><b class=\"ui-text am-ft-md\">" + entrust_price + " 元"+"</b></div></div>"
                + "<div class=\"ui-form-item\"><label class=\"ui-label\">委托数量：</label><b class=\"ui-text am-ft-md\">" + entrust_amount + " 股"+"</b></div></div>";

            var comfirmContent = "<div class=\"con-success clearfix\"><div class=\"cm-left successIcon-lside\"><i class=\"ui-icon icon-middle-doubt\"></i></div><div class=\"cm-left successText-rside\"><p class=\"layout02\"><b>是否提交以上委托？</b></p></div></div>";

            var callback = function () {
                var standby = new Standby({target: msgBox.domNode});
                document.body.appendChild(standby.domNode);
                standby.startup();
                standby.show();

                Ajax.post(Global.baseUrl + '/stock/buy', data).then(function (response) {
                    //隐藏
                    standby.hide();
                    msgBox.close();
                    if (response.success) {
                        var html = '<div style="text-align: center;"><i class="ui-icon icon-middle-success"></i><span style="padding-left: 5px;font-weight: bold;font-size: 16px;vertical-align: middle;">委托成功！</span></div>';
                        msgBox = new MsgBox({
                            title: '确认买入',
                            confirmAndCancel: false,
                            msg: html,
                            closeAction: 'close',
                            contentClass: "clearfix",
                            alertClass: "box"
                        });
                        msgBox.placeAt(document.body);
                        msgBox.show();

                        queryAssets(accountSelect.value);
                        //清空所有
                        clearAll();
                    } else {
                        var html = '<div style="text-align: center;"><i class="ui-icon icon-middle-error"></i><span style="padding-left: 5px;font-weight: bold;font-size: 16px;vertical-align: middle;">委托失败！</span></div><div style="font-size: 14px;text-align: center;">'+response.msg+'</div>';

                        msgBox = new MsgBox({
                            title: '确认买入',
                            confirmAndCancel: false,
                            msg: html,
                            closeAction: 'close',
                            contentClass: "clearfix",
                            alertClass: "box"
                        });
                        msgBox.placeAt(document.body);
                        msgBox.show();
                    }
                });
            };

            msgBox = new MsgBox({
                title: '确认买入',
                confirmAndCancel: true,
                confirmText: comfirmContent,
                msg: content,
                width:500,
                closeAction: 'close',
                contentClass: "con-box-transaction",
                onConfirm: callback,
                onCancel: function () {
                    msgBox.close();
                }
            });
            msgBox.placeAt(document.body);
            msgBox.show();
        });

        for (var i = 0; i < items.length; i++) {
            on(dom.byId(items[i]), 'click', function () {
                if (dom.byId(this).innerHTML != "--") {
                    //alert(dom.byId(this).innerHTML);
                    priceSpinner.set("value", dom.byId(this).innerHTML);
                    getBuyable();
                }
            });
        }
        ;
    }


    //输入代码6位则查询其相关信息，否则则清空数据
    function getCodeInfo() {
        code = codeBox.getFilterValue();
        if (intervalFlag != null) {
            clearInterval(intervalFlag);
            intervalFlag = null;
        }
        clearTradeInfo();
        clearRealTimeDate();

        if (code.length == 6) {
            getRealTimeDataFirst(code);
            //设置定时任务 5秒
            intervalFlag = setInterval(getRealTimeData, 5000);
            drawTimeShareChart('timeShareChart', code, Global.baseUrl + '/quote/stock/' + code + '/trend_data');
        } else {
            drawEmptyChart('timeShareChart');
        }
    }

    //获取行情数据，设置交易信息
    function getRealTimeDataFirst(code) {
    	if (code) {
    		Ajax.get(Global.baseUrl + '/quote/stock/' + code + '/real_time_data', {}).then(function (response) {
                if (response.success) {
                    setRealTimeData(response.data);
                    setTradeInfo(response.data);
                } else {
                }
            });
    	}
        
    }

    //获取行情数据
    function getRealTimeData() {
    	if (code) {
    		Ajax.get(Global.baseUrl + '/quote/stock/' + code + '/real_time_data', {}).then(function (response) {
                if (response.success) {
                    setRealTimeData(response.data);
                } else {
                }
            });
    	}
        
    }

    //设置行情数据
    function setRealTimeData(data) {
    	max = priceSpinner.maxValue=data.maxPrice;
    	min = priceSpinner.minValue=data.minPrice;
        var prePrice = data.prevClosePrice;
        //设置数据
        for (var i = 0; i < allItems.length; i++) {
            dom.byId(allItems[i]).innerHTML = data[allItems[i]];
        }
        dom.byId("chargeRate").innerHTML = (parseFloat(data["chargeRate"]) * 100).toFixed(2) + "%";
        //部分空数据处理
        if (regex.test(data["newPrice"])) {
            dom.byId("newPrice").innerHTML = "--";
            dom.byId("chargeRate").innerHTML = "--";
            dom.byId("chargeValue").innerHTML = "--";
        }
        for (var i = 1; i <= 5; i++) {
            var buyPrice = "buyPrice" + i;
            var buyCount = "buyCount" + i;
            var sellPrice = "sellPrice" + i;
            var sellCount = "sellCount" + i;
            if (regex.test(data[buyPrice])) {
                dom.byId(buyPrice).innerHTML = "--";
                dom.byId(buyCount).innerHTML = "--";
            }
            if (regex.test(data[sellPrice])) {
                dom.byId(sellPrice).innerHTML = "--";
                dom.byId(sellCount).innerHTML = "--";
            }
            ;
        }
        //红涨绿跌
        if (data["newPrice"] > prePrice) {
            domClass.add("chargeRate", "am-ft-red");
            domClass.add("chargeValue", "am-ft-red");
        } else if (data["newPrice"] < prePrice) {
            domClass.add("chargeRate", "am-ft-green");
            domClass.add("chargeValue", "am-ft-green");
        }
        for (var i = 0; i < items.length; i++) {
            var price = data[items[i]];
            if (regex.test(price)) {

            } else if (price > prePrice) {
                domClass.add(items[i], "am-ft-red");
            } else if (price < prePrice) {
                domClass.add(items[i], "am-ft-green");
            }
        }
    }

    //设置交易信息
    function setTradeInfo(data) {
        if (dom.byId("stock_name").innerHTML != "" || !isNaN(priceSpinner.get("value"))) {
            return;
        }
        dom.byId("stock_name").innerHTML = data.stockName;
        dom.byId("exchange_type").value = data.exchangeType;
        if (!regex.test(data.newPrice)) {
            priceSpinner.set("value", data.newPrice);
        } else if (!regex.test(data.sellPrice1)) {
            priceSpinner.set("value", data.sellPrice1);
        } else {
            priceSpinner.set("value", data.prevClosePrice);
        }
    }

    //清空行情数据
    function clearRealTimeDate() {
        //清空数据
        for (var i = 0; i < allItems.length; i++) {
            dom.byId(allItems[i]).innerHTML = "--";
        }
        //清空样式
        for (var i = 0; i < items.length; i++) {
            domClass.remove(items[i], "am-ft-red am-ft-green");
        }
        domClass.remove("chargeRate", "am-ft-red am-ft-green");
        domClass.remove("chargeValue", "am-ft-red am-ft-green");
    }

    //清空交易信息
    function clearTradeInfo() {
        dom.byId("stock_name").innerHTML = "";
        priceSpinner.reset();
        amountSpinner.reset();
        dom.byId("buyalbeAmount").innerHTML = "";
        tradePwdBox.reset();
    }

    //清空选中
    function clearChecked() {
        domClass.remove("allAmount", "active");
        domClass.remove("halfAmount", "active");
        domClass.remove("oneThirdAmount", "active");
        domClass.remove("quarterAmount", "active");
    }

    //清空所有
    function clearAll() {
        codeBox.reset();
        if (intervalFlag != null) {
            clearInterval(intervalFlag);
            intervalFlag = null;
        }
        clearTradeInfo();
        clearChecked();
        clearRealTimeDate();

        //初始化分时图
        drawEmptyChart('timeShareChart');
    }

    function queryAssets(item) {
        if (typeof item == 'string') {
            item = accountSelect.get('store').get(item);
        }
		if (item)
		{
			Ajax.get(Global.baseUrl + '/homs/assetsinfo', {
						"homs_fund_account": item.homsFundAccount, "homs_combine_id": item.id
					}).then(function (response) {
						if (response.success) {
							var dataItem = response.data;
							dom.byId("balanceDecimal").innerHTML = (parseFloat(dataItem.curAmount) / 100).toFixed(2);
						}
					});
		}  
    }

    //查询可买
    function getBuyable() {
        var homs_combine_id = accountSelect.value;
        var homs_fund_account = accountSelect.get('store').get(homs_combine_id).homsFundAccount;
        var stock_code = codeBox.getFilterValue();
        var entrust_price = priceSpinner.get("value");
        var exchange_type = dom.byId("exchange_type").value;
        if (isNaN(entrust_price)) {
            return;
        }
        var data = {
            'homs_combine_id': homs_combine_id,
            'homs_fund_account': homs_fund_account,
            'stock_code': stock_code,
            'entrust_price': entrust_price,
            'exchange_type': exchange_type
        };
        Ajax.post(Global.baseUrl + '/stock/buyquantity', data).then(function (response) {
            if (response.success) {
                dom.byId("buyalbeAmount").innerHTML = Math.floor(parseFloat(response.data.curNumber) / 100) * 100;
            } else {
                dom.byId("buyalbeAmount").innerHTML = "0";
                codeBox.displayMessage(response.msg, 'error');
            }
        });
    }

    return {
        init: function () {
            Ajax.get(Global.baseUrl + '/sec/rsa', {}).then(function (response) {
                if (response.success) {
                    key = RSA.getKeyPair(response.data.exponent, '', response.data.modulus);
                } else {
                }
            });
            initView();
            Helper.init(config);
            addListeners();
        }
    };

});