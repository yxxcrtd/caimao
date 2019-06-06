// @koala-prepend "./lib/jquery-1.11.1.min.js"
// @koala-prepend "./common/utils.js"
// @koala-prepend "./common/Data.js"
// @koala-prepend "./common/Dict.js"
// @koala-prepend "./ux/protocol.js"
// @koala-prepend "./lib/RSA.js"

//定义页面内变量
var GlobalHomsCombineId = "", GlobalHomsFundAccount = "", labelSelect = "hold", blurControl = false, intervalFlag = null, nowSelectTrade = null, nowStockCode = null, nowStockName = null, nowStockMarketType = null;
var intervalHold = null, intervalEntrust = null, intervalDeal = null, optionalBlurControl = false, intervalStockList = null, stockChartChange = false, isQuantity = false;
var list_market_type = "70", list_rank = "10", list_start = 0, list_lens = 100;
var user_stock_list = [];
var stockListScrollHeight = 0, stockListScrollTop = 0, stockListDivHeight = 0;
var chartObj;

//切换homs账户
function changeCombineId(o, homsCombineId, homsFundAccount){
    //更新变量
    GlobalHomsCombineId = homsCombineId;
    GlobalHomsFundAccount = homsFundAccount;

    //变更选中样式
    $(o).css("color","#5588CA").siblings("li").css("color","#99999B");

    //余额
    showHomsAssetsInfo();

    //持仓
    if(labelSelect == "hold"){
        showStockHold();
    }
    //委托
    else if(labelSelect == "cur_entrust"){
        showStockCurEntrust();
    }
    //成交
    else if(labelSelect == "cur_deal"){
        showStockCurDeal();
    }
}

//切换面板
function changeLabel(o, labelStr){
    //更新变量
    labelSelect = labelStr;

    //变更选中样式
    $(o).addClass("hov").siblings("a").removeClass("hov");

    //显示面板
    $(".stock_label").hide();
    $("#stock_" + labelStr).show();

    //持仓
    if(labelSelect == "hold"){
        //清除定时任务
        if (intervalHold != null) {
            clearInterval(intervalHold);
        }
        showStockHold();
        intervalHold = setInterval("showStockHold(0)", 5000);
    }
    //委托
    else if(labelSelect == "cur_entrust"){
        if (intervalEntrust != null) {
            clearInterval(intervalEntrust);
        }
        showStockCurEntrust();
        intervalEntrust = setInterval("showStockCurEntrust(0)", 5000);
    }
    //成交
    else if(labelSelect == "cur_deal"){
        if (intervalDeal != null) {
            clearInterval(intervalDeal);
        }
        showStockCurDeal();
        intervalDeal = setInterval("showStockCurDeal(0)", 5000);
    }
}

//刷新homs账户详情
function showHomsAssetsInfo(){
    if(GlobalHomsCombineId == "" || GlobalHomsFundAccount == "") return false;
    $.get("/homs/assetsinfo", {homs_fund_account:GlobalHomsFundAccount, homs_combine_id:GlobalHomsCombineId}, function(response){
        if(response.success && response.data){
            $("#available_amount").html(parseFloat(parseFloat(response.data.curAmount)/100).toFixed(2));
        }
    });
}

//刷新持仓
var stockHoldBox,stockHoldBoxHTML;
function showStockHold(){
    stockHoldBox = $("#stock_hold tbody");
    if(stockHoldBoxHTML == null){
        stockHoldBoxHTML = stockHoldBox.find("script").eq(0).html();
    }
    if(GlobalHomsCombineId == "" || GlobalHomsFundAccount == "" || stockHoldBoxHTML == null) return false;
    $.get("/stock/child/holding", {homs_fund_account:GlobalHomsFundAccount, homs_combine_id:GlobalHomsCombineId}, function(response){
        if(response.success){
            stockHoldBox.html(CMUTILS.rentmpl(stockHoldBoxHTML,{data:response.data}));
        }
    });
}

//刷新委托
var stockCurEntrustHoldBox,stockCurEntrustHoldBoxHTML;
function showStockCurEntrust(){
    stockCurEntrustHoldBox = $("#stock_cur_entrust tbody");
    if(stockCurEntrustHoldBoxHTML == null){
        stockCurEntrustHoldBoxHTML = stockCurEntrustHoldBox.find("script").eq(0).html();
    }
    if(GlobalHomsCombineId == "" || GlobalHomsFundAccount == "" || stockCurEntrustHoldBoxHTML == null) return false;
    $.get("/stock/child/curentrust", {homs_fund_account:GlobalHomsFundAccount, homs_combine_id:GlobalHomsCombineId}, function(response){
        if(response.success) {
            stockCurEntrustHoldBox.html(CMUTILS.rentmpl(stockCurEntrustHoldBoxHTML,{data:response.data}));
        }
    });
}

//刷新成交
var stockCurDealBox, stockCurDealBoxHTML;
function showStockCurDeal(){
    stockCurDealBox = $("#stock_cur_deal tbody");
    if(stockCurDealBoxHTML == null){
        stockCurDealBoxHTML = stockCurDealBox.find("script").eq(0).html();
    }
    if(GlobalHomsCombineId == "" || GlobalHomsFundAccount == "" || stockCurDealBoxHTML == null) return false;
    $.get("/stock/child/curdeal", {homs_fund_account:GlobalHomsFundAccount, homs_combine_id:GlobalHomsCombineId}, function(response){
        if(response.success) {
            stockCurDealBox.html(CMUTILS.rentmpl(stockCurDealBoxHTML,{data:response.data}));
        }
    });
}

function showCancel(no, status){
    var stockStatus = parseInt(status);
    if(stockStatus > 0 && stockStatus < 7 && stockStatus != 5){
        return '<b><span style="color: #48A9EE;cursor: pointer" onclick="stockCancel(this, '+no+')">撤单</span></b>'
    }
}

//委托取消
function stockCancel(obj, entrustNo){
    if(GlobalHomsCombineId == "" || GlobalHomsFundAccount == "") return false;
    $.post("/stock/withdrawal", {homs_fund_account:GlobalHomsFundAccount, homs_combine_id:GlobalHomsCombineId, entrust_no:entrustNo}, function(response){
        if(response.success) {
            $(obj).parent().parent().parent().remove();
        }
    });
}

//文本框输入
$(document).on("keyup", ".inputX", function(){
    if($(this).attr("type") == "text" && !$(this).hasClass("stockInput")){
        var value = CMUTILS.clearNoNum($(this).val());
        $(this).val(value);
    }
    if($(this).val().length > 0){
        $(this).parent().find(".text_placeholder").hide();
    }else{
        $(this).parent().find(".text_placeholder").show();
        var text_note = $(this).parent().find(".text_note");
        if(typeof(text_note.attr("data-text"))!="undefined"){
            text_note.text(text_note.attr("data-text"));
        }
    }
});

//监听股票输入框
$(document).on("keyup", ".stockInput", function(){
    var stockList = $(this).parent().parent().parent().find(".stockList");
    var value = $(this).val().replace(/[^\d\w]/g,'');
    $(this).val(value);

    if($(this).val().length > 0){
        stockList.show();
    }else{
        stockList.empty();
        stockList.hide();
    }
    if(value != ""){
        $.get("/quote/stock/stock_data/" + value, function(response){
            if(response.success && response.data.length >0){
                var html = "";
                for(var i in response.data){
                    var stockCode = response.data[i].stockCode;
                    var stockName = response.data[i].stockName;
                    var stockCodeStr = stockCode.replace(new RegExp(value,"g"),"<b style='color:red;'>"+value+"</b>");

                    html += "<li data-stockCode=" + stockCode + " data-stockName=\"" + stockName + "\"><span>" + stockCodeStr + "　" + stockName + "</span></li>";
                }
                stockList.html(html);
                stockList.show();
            }
        });
    }
});

//监听自选股股票输入框
$(document).on("keyup", "#optional_stock_search", function(){
    var stockList = $(this).parent().find("#optional_stock_search_list");
    var value = $(this).val().replace(/[^\d\w]/g,'');
    $(this).val(value);

    if($(this).val().length > 0){
        stockList.show();
    }else{
        stockList.empty();
        stockList.hide();
    }
    if(value != ""){
        $.get("/quote/stock/stock_data/" + value, function(response){
            if(response.success && response.data.length >0){
                var html = "";
                for(var i in response.data){
                    var stockCode = response.data[i].stockCode;
                    var stockName = response.data[i].stockName;
                    var stockCodeStr = stockCode.replace(new RegExp(value,"g"),"<b style='color:red;'>"+value+"</b>");

                    html += "<li><span class='showStockCode' data-stockName='"+stockName+"' data-stockCode='"+stockCode+"' style='display: inline-block;width: 200px;'>" + stockCodeStr + "　　" + stockName + "</span><span class='stock_i_op' data-stockCode='" + stockCode + "'>+</span></li>";
                }
                stockList.html(html);
                stockList.show();
            }
        });
    }
});

//监听自选股股票选择
$(document).on("click", "#optional_stock_search_list li .stock_i_op", function(){
    var stockCode = $(this).attr("data-stockCode");
    $("#optional_stock_search").val("");
    $("#optional_stock_search_list").hide();
    optionalBlurControl = true;
    $.post("/stock/optionalStockAdd", {stockCode:stockCode}, function(response){
        if(response.success && response.data == 1){
            getOptionalStock();
        }
    });
});

//监听股票选择
$(document).on("click", "#optional_stock_search_list li .showStockCode", function(){
    var stockCode = $(this).attr("data-stockCode");
    var stockName = $(this).attr("data-stockName");
    $("#optional_stock_search").val("");
    $("#optional_stock_search_list").hide();
    optionalBlurControl = true;
    nowStockCode = stockCode;
    nowStockName = stockName;
    //初始化股票信息
    getStockCodeInfo();
});

//自选股搜索
$(document).on("blur", "#optional_stock_search", function(){
    setTimeout(function(){
        if(optionalBlurControl == false){
            $("#optional_stock_search").val("");
            $("#optional_stock_search_list").hide();
        }
        optionalBlurControl = false;
    },200);
});

//监听股票输入框
$(document).on("blur", ".stockInput", function(){
    var curLi = $(this).parent().parent().parent().find(".stockList li").eq(0);
    setTimeout(function(){
        if(typeof(curLi) != "undefined" && blurControl == false){
            curLi.click();
        }
        blurControl = false;
    },200);
});

//监听股票选择
$(document).on("click", ".stockList li", function(){
    var stockCode = $(this).attr("data-stockCode");
    var stockName = $(this).attr("data-stockName");
    var tradeBox = $(this).parent().parent();
    tradeBox.find(".stockInput").val(stockCode);
    tradeBox.find(".text_placeholder").eq(0).hide();
    tradeBox.find(".text_note").eq(0).text(stockName);
    $(".stockList").hide();
    blurControl = true;
    nowSelectTrade = $(this).parent().attr("data-list");
    nowStockCode = stockCode;
    nowStockName = stockName;
    isQuantity = true;
    //初始化股票信息
    getStockCodeInfo();
});

//是否显示添加自选股按钮
function stockDetailShowOptional(){
    if(nowStockCode != null && user_stock_list.indexOf(nowStockCode) < 0){
        $("#jia").text("加入自选");
    }else{
        $("#jia").text("已添加");
    }
}

//获取股票信息
function getStockCodeInfo(){
    if(nowStockCode != null && typeof(nowStockCode) != "undefined" && nowStockCode != ""){
        //清除定时任务
        if (intervalFlag != null) {
            clearInterval(intervalFlag);
            intervalFlag = null;
        }
        //清除输入框
        clearStockInput();
        //清除右侧所有股票数据
        clearStockRight();
        //渲染数据
        if(nowStockCode.length == 6){
            stockChartChange = true;
            //显示返回股票详情按钮
            $(".backStockDetail").show();
            //显示添加自选股
            stockDetailShowOptional();
            //获取行情数据
            getRealTimeData(1);
            //设置定时任务 5秒刷新
            intervalFlag = setInterval("getRealTimeData(0)", 5000);
        }
    }
}

//获取实时成交
function getMarketData(data){
    $.ajax({
        url: '/quote/stock/market_match',
        type: 'POST',
        contentType:'application/json; charset=UTF-8',
        data: '{"v":"caimao.json.001","k":"a0aea509d9059bd18735f4b8d499cfcd","o":[{"act":107,"id1":"' + data['stockCode'] + '","tp1":"' + data['exchangeType'] + '","px":"0","pl":"10"}]}',
        dataType: 'json',
        processData: false,
        success: function (response) {
            if(response.success && response.data != ""){
                setMarketData($.parseJSON(response.data));
            }
        }
    });
}

//获取实时成交涨跌图标
function getWind(value){
    if(value == 0){
        return "stock_mark_up";
    }else if(value == 1){
        return "stock_mark_down";
    }else{
        return "";
    }
}

//获取实时成交涨跌颜色
function getColor(value){
    if(value == 0){
        return "color:#eb4636";
    }else if(value == 1){
        return "color:#6CBF0B";
    }else{
        return "";
    }
}

//设置实时成交信息
function setMarketData(data){
    var html = "";
    for(var i in data.o[0].o){
        var o_length = data.o[0].o.length;
        var tmp_data = data.o[0].o[o_length - 1 - i];
        if(i == 0){
            $("#stock_ticker_now").html(tmp_data.vol);
        }
        html += '<li><label>' + CMUTILS.formatTime(tmp_data.date) + '</label><b>' + tmp_data.pri + '</b><span style="' + getColor(tmp_data.wind) + '">' + tmp_data.vol + '</span><i class="' + getWind(tmp_data.wind) + '"></i></li>';
    }
    $("#latest_trans").html(html);
}

//获取实时股票数据
function getRealTimeData(isFirst){
    if(nowStockCode){
        $.get('/quote/stock/' + nowStockCode + '/real_time_data').then(function (response) {
            if (response.success) {
                setRealTimeData(response.data);
                if(isFirst){
                    setTradeInfo(response.data);
                }
                //刷新实时成交
                getMarketData(response.data);
            }
        });
    }
}

//设置实时股票数据
function setRealTimeData(data){
    nowStockMarketType = data['exchangeType'];
    if(stockChartChange){
        //初始化K线图
        init_kline();
        stockHqPlay();
        stockChartChange = false;
    }
    if(isQuantity){
        //可买卖数量
        if(nowSelectTrade == 'buy'){
            getBuyable();
        }else{
            getSellable();
        }
        isQuantity = false;
    }
    //股票名称
    $("#r_stock_name").html(data['stockCode'] + "  " + data['stockName']);
    //行情信息
    var stockSell5 = $("#stockSell5");
    var stockBuy5 = $("#stockBuy5");
    for(var i = 0;i < 5;i++){
        //设置卖5
        var sellPrice = data['sellPrice' + (5 - i)];
        if(sellPrice > data['openPrice']){
            stockSell5.find("b").eq(i).removeClass("tradeSellColor").addClass("tradeBuyColor");
        }else if(sellPrice < data['openPrice']){
            stockSell5.find("b").eq(i).removeClass("tradeBuyColor").addClass("tradeSellColor");
        }
        stockSell5.find("b").eq(i).html(sellPrice);
        stockSell5.find("span").eq(i).html(data['sellCount' + (5 - i)]);
        //设置买5
        var buyPrice = data['buyPrice' + (i + 1)];
        if(buyPrice > data['openPrice']){
            stockBuy5.find("b").eq(i).removeClass("tradeSellColor").addClass("tradeBuyColor");
        }else if(buyPrice < data['openPrice']){
            stockBuy5.find("b").eq(i).removeClass("tradeBuyColor").addClass("tradeSellColor");
        }
        stockBuy5.find("b").eq(i).html(buyPrice);
        stockBuy5.find("span").eq(i).html(data['buyCount' + (i + 1)]);
    }

    //stock name
    $("#stock_detail_name").html(data['stockName']);
    $("#stock_detail_code").html(data['stockCode']);
    $("#stock_detail_price b").text(data['newPrice']);
    if(data['chargeValue'] >= 0){
        $("#stock_detail_price").css("background","#EB4535");
        $("#stock_detail_price .symbol_jian").removeClass("symbol_jian_down");
        $("#stock_detail_price .symbol_jian").addClass("symbol_jian_up");
    }else{
        $("#stock_detail_price").css("background","#29B204");
        $("#stock_detail_price .symbol_jian").removeClass("symbol_jian_up");
        $("#stock_detail_price .symbol_jian").addClass("symbol_jian_down");
    }

    //ticker
    $("#stock_ticker_newest").html(data['newPrice']);
    $("#stock_ticker_highest").html(data['maxPrice']);
    $("#stock_ticker_change").html(data['chargeValue']);
    $("#stock_ticker_increase").html(CMUTILS.formatPer(data['chargeRate']) + "%");
    $("#stock_ticker_up").html(data['upPrice']);
    $("#stock_ticker_out").html(CMUTILS.formatNumToUnit(data['outside']));
    $("#stock_ticker_open").html(data['openPrice']);
    $("#stock_ticker_lowest").html(data['minPrice']);
    $("#stock_ticker_amount").html(CMUTILS.formatNumToUnit(data['totalAmount']));
    $("#stock_ticker_amplitude").html((CMUTILS.formatPer((data['maxPrice'] - data['minPrice']) / data['prevClosePrice'])) + "%");
    $("#stock_ticker_total").html(CMUTILS.formatNumToUnit(data['total']));
    $("#stock_ticker_down").html(data['downPrice']);
    $("#stock_ticker_in").html(CMUTILS.formatNumToUnit(data['inside']));

    //更新颜色
    if(data['chargeValue'] > 0){
        $("#stock_ticker_change, #stock_ticker_increase").removeClass("colorDown").addClass("colorUp");
    }else{
        $("#stock_ticker_change, #stock_ticker_increase").removeClass("colorUp").addClass("colorDown");
    }
}

//设置交易框
function setTradeInfo(data){
    $("#exchange_type_" + nowSelectTrade).val(data['exchangeType']);
    if(nowSelectTrade == 'sell'){
        var stockSellPrice = $("#stockSellPrice");
        stockSellPrice.val(data['buyPrice1']);
        stockSellPrice.parent().find(".text_placeholder").hide();
    }else{
        var stockBuyPrice = $("#stockBuyPrice");
        stockBuyPrice.val(data['sellPrice1']);
        stockBuyPrice.parent().find(".text_placeholder").hide();
    }
}

//清除表单
function clearStockInput(){
    var stockNoInput = $("#stockSellCode");
    if(nowSelectTrade == "sell") stockNoInput =  $("#stockBuyCode");
    stockNoInput.val("");
    stockNoInput.parent().find(".text_placeholder").show();
    stockNoInput.parent().parent().parent().find(".stockList").empty();
    var text_note = stockNoInput.parent().find(".text_note");
    if(typeof(text_note.attr("data-text"))!="undefined"){
        text_note.text(text_note.attr("data-text"));
    }
    $("#stockBuyPrice, #stockBuyAmount, #stockSellPrice, #stockSellAmount, #stockBuyPwd, #stockSellPwd").empty();
}

//清除股票
function clearStockRight(){
    //买卖盘
    $("#stockBuy5, #stockSell5").find("b, span").empty();
    //信息
    $("#stock_ticker").find("span").empty();
    //实时成交
    $("#latest_trans").empty();
}

//查询可买数量
function getBuyable() {
    return;
    var entrustPrice = $("#stockBuyPrice").val();
    var exchangeType = $("#exchange_type_buy").val();
    var buyAbleAmount = $("#buyAbleAmount");
    if (isNaN(entrustPrice)) {
        return;
    }
    var data = {
        'homs_combine_id': GlobalHomsCombineId,
        'homs_fund_account': GlobalHomsFundAccount,
        'stock_code': nowStockCode,
        'entrust_price': entrustPrice,
        'exchange_type': exchangeType
    };
    $.post("/stock/buyquantity", data, function(response){
        if (response.success) {
            buyAbleAmount.html("可买 " + Math.floor(parseFloat(response.data.curNumber) / 100) * 100);
            buyAbleAmount.attr("data-text", Math.floor(parseFloat(response.data.curNumber) / 100) * 100);
        } else {
            buyAbleAmount.attr("data-text", 0);
            buyAbleAmount.html("可买 0");
        }
    });
}

//查询可卖数量
function getSellable(){
    return;
    var exchangeType = $("#exchange_type_sell").val();
    var sellAbleAmount = $("#sellAbleAmount");
    var data = {
        'homs_combine_id': GlobalHomsCombineId,
        'homs_fund_account': GlobalHomsFundAccount,
        'stock_code': nowStockCode,
        'exchange_type': exchangeType
    };
    $.post("/stock/sellquantity", data, function(response){
        if (response.success) {
            sellAbleAmount.html("可卖 " + response.data);
            sellAbleAmount.attr("data-text", response.data);
        } else {
            sellAbleAmount.attr("data-text", 0);
            sellAbleAmount.html("可卖 0");
        }
    });
}

//快速选择数量
function buyAmountFast(o, num){
    $(o).parent().parent().find("a").removeClass("active");
    $(o).addClass("active");
    var buyAbleAmount = $("#buyAbleAmount").attr("data-text");
    if(typeof(buyAbleAmount) != "undefined" && buyAbleAmount !='' && buyAbleAmount != 0){
        var amount = parseInt(buyAbleAmount / num);
        amount = parseInt(parseInt(amount/100) * 100);
        $("#stockBuyAmount").val(amount);
        $("#stockBuyAmount").parent().find(".text_placeholder").hide();
    }
}

//快速选择数量
function sellAmountFast(o, num){
    $(o).parent().parent().find("a").removeClass("active");
    $(o).addClass("active");
    var sellAbleAmount = $("#sellAbleAmount").attr("data-text");
    if(typeof(sellAbleAmount) != "undefined" && sellAbleAmount !='' && sellAbleAmount != 0){
        $("#stockSellAmount").val(parseInt(sellAbleAmount / num));
        $("#stockSellAmount").parent().find(".text_placeholder").hide();
    }
}

//买
function stockBuy(){
    var validates;
    var exchangeType = $("#exchange_type_buy").val();
    //股票代码
    var stockCode = $("#stockBuyCode").val();
    validates = [{
        pattern: /.+/,
        message: '请输入证券代码'
    }, {
        pattern: /^\d{6}.*$/,
        message: '证券代码格式有误，请重新输入'
    }];
    if(!testInput(stockCode, validates)) return;

    //委托价格
    var stockPrice = $("#stockBuyPrice").val();
    validates = [{
        pattern: /.+/,
        message: '请输入委托价格'
    }];
    if(!testInput(stockPrice, validates)) return;

    //委托数量
    var stockBuyAmount = $("#stockBuyAmount").val();
    validates = [{
        pattern: /.+/,
        message: '请输入委托数量'
    },
        /*{
        pattern: function () {
            var buyAmount = $("#buyAbleAmount").attr("data-text");
            return parseFloat(stockBuyAmount) <= parseFloat(buyAmount);
        },
        message: '超出可买数量'
    },*/
        {
        pattern: function () {
            return (stockBuyAmount % 100) == 0;
        },
        message: '必须是100的整数倍'
    },{
        pattern: function () {
            return parseFloat(stockBuyAmount) != 0;
        },
        message: '委托数量为0，不可买入！'
    }];
    if(!testInput(stockBuyAmount, validates)) return;

    //资金密码
    var stockBuyPwd = $("#stockBuyPwd").val();
    validates = [{
        pattern: /.+/,
        message: '请输入安全密码'
    }];
    if(!testInput(stockBuyPwd, validates)) return;

    var postData = {
        'homs_combine_id': GlobalHomsCombineId,
        'homs_fund_account': GlobalHomsFundAccount,
        'stock_code': stockCode,
        'entrust_quantity': stockBuyAmount,
        'entrust_price': stockPrice,
        'exchange_type': exchangeType,
        'trade_pwd': RSAUtils.encryptedString(stockBuyPwd)
    };

    $("#stockBuyAmount,#stockBuyPwd").val("");
    $.post("/stock/buy", postData, function(response){
        if(response.success){
            $("#stockBuyPrice, #stockBuyAmount, #stockBuyPwd").empty();
            $("#show_alert_stock").html('<span style="color:#6CBF0B">委托成功</span>').show().delay(3000).fadeOut();
            showStockCurEntrust();
        }else{
            $("#show_alert_stock").html(response.msg).show().delay(3000).fadeOut();
        }
    });
}

//卖
function stockSell(){
    var validates;
    var exchangeType = $("#exchange_type_sell").val();
    //股票代码
    var stockCode = $("#stockSellCode").val();
    validates = [{
        pattern: /.+/,
        message: '请输入证券代码'
    }, {
        pattern: /^\d{6}.*$/,
        message: '证券代码格式有误，请重新输入'
    }];
    if(!testInput(stockCode, validates)) return;

    //委托价格
    var stockPrice = $("#stockSellPrice").val();
    validates = [{
        pattern: /.+/,
        message: '请输入委托价格'
    }];
    if(!testInput(stockPrice, validates)) return;

    //委托数量
    var stockSellAmount = $("#stockSellAmount").val();
    validates = [{
        pattern: /.+/,
        message: '请输入委托数量'
    },
        /*{
        pattern: function () {
            var sellAmount = $("#sellAbleAmount").attr("data-text");
            return parseFloat(stockSellAmount) <= parseFloat(sellAmount);
        },
        message: '超出可卖数量'
    },*/
        {
        pattern: function () {
            return parseFloat(stockSellAmount) != 0;
        },
        message: '委托数量为0，不可买入！'
    }];
    if(!testInput(stockSellAmount, validates)) return;

    //资金密码
    var stockBuyPwd = $("#stockSellPwd").val();
    validates = [{
        pattern: /.+/,
        message: '请输入安全密码'
    }];
    if(!testInput(stockBuyPwd, validates)) return;

    var postData = {
        'homs_combine_id': GlobalHomsCombineId,
        'homs_fund_account': GlobalHomsFundAccount,
        'stock_code': stockCode,
        'entrust_quantity': stockSellAmount,
        'entrust_price': stockPrice,
        'exchange_type': exchangeType,
        'trade_pwd': RSAUtils.encryptedString(stockBuyPwd)
    };
    $("#stockSellAmount,#stockSellPwd").val("");
    $.post("/stock/sell", postData, function(response){
        if(response.success){
            $("#stockBuyPrice, #stockBuyAmount, #stockBuyPwd").empty();
            $("#show_alert_stock").html('<span style="color:#6CBF0B">委托成功</span>').show().delay(3000).fadeOut();
            showStockCurEntrust();
        }else{
            $("#show_alert_stock").html(response.msg).show().delay(3000).fadeOut();
        }
    });
}

//表单验证
function testInput(value, validates){
    var msg = "";
    for(var i in validates){
        var regular = validates[i].pattern;
        if(typeof(regular) == "function"){
            if(!regular()){
                msg = validates[i].message;break;
            }
        }else{
            if(!regular.test(value)){
                msg = validates[i].message;break;
            }
        }
    }
    if(msg != ""){
        $("#show_alert_stock").html(msg).show().delay(3000).fadeOut();
        return false;
    }else{
        return true;
    }
}

//获取自选股
function getOptionalStock(){
    $.when($.ajax("/stock//queryOptionalStock"), $.ajax("/stock/queryOptionalStockHot")).done(function(response1, response2){
        if(response1[0].success && response2[0].success){
            user_stock_list = [];
            var data1 = response1[0].data;
            var data2 = response2[0].data;
            var hot_stock_list = [];
            for(var i in data1){
                user_stock_list.push(data1[i].stockCode);
            }

            if(user_stock_list.length < 20){
                for(var i in data2){
                    if(user_stock_list.indexOf(data2[i].stockCode) < 0){
                        hot_stock_list.push(data2[i].stockCode);
                    }
                }
                hot_stock_list = hot_stock_list.slice(0, 20 - user_stock_list.length);
                $(".hot_optional_stock_x").show();
            }else{
                $(".hot_optional_stock_x").hide();
            }

            var stock_list = user_stock_list.concat(hot_stock_list);
            if(stock_list.length <= 0) return;

            $.get("/quote/stock/" + stock_list.join(",") + "/batch_data", function(response){
                if(response.success){
                    var user_stock_html = "";
                    var hot_stock_html = "";
                    for(var i in response.data){
                        var stock_info = response.data[i];
                        if(user_stock_list.indexOf(stock_info.stockCode) >= 0){
                            user_stock_html += '' +
                            '<tr data-stock-code="'+stock_info.stockCode+'">' +
                            '<td><i class="'+(stock_info.chargeValue >=0 ?'dian_1':'dian_2')+'"></i>'+stock_info.stockName+'</td>' +
                            '<td class="'+(stock_info.chargeValue >=0 ?'colorUp':'colorDown')+'">'+stock_info.newPrice+'</td>' +
                            '<td class="'+(stock_info.chargeValue >=0 ?'colorUp':'colorDown')+'">'+CMUTILS.formatPer(stock_info.chargeRate)+'%</td>' +
                            '<td><span class="stock_i_op" data-stock-code="'+stock_info.stockCode+'">-</span></td>' +
                            '</tr>';
                        }
                        $("#user_optional_stock").html(user_stock_html);

                        if(hot_stock_list.indexOf(stock_info.stockCode) >= 0){
                            hot_stock_html += '' +
                            '<tr data-stock-code="'+stock_info.stockCode+'">' +
                            '<td><i class="dian_1"></i>'+stock_info.stockName+'</td>' +
                            '<td class="'+(stock_info.chargeValue >=0 ?'colorUp':'colorDown')+'">'+stock_info.newPrice+'</td>' +
                            '<td class="'+(stock_info.chargeValue >=0 ?'colorUp':'colorDown')+'">'+CMUTILS.formatPer(stock_info.chargeRate)+'%</td>' +
                            '<td><span class="stock_i_op" data-stock-code="'+stock_info.stockCode+'">+</span></td>' +
                            '</tr>';
                        }
                        $("#hot_optional_stock").html(hot_stock_html);
                    }
                }
            });
        }
    });
}

//添加自选股
$(document).on("click", "#hot_optional_stock .stock_i_op", function(){
    var stockCode = $(this).attr("data-stock-code");
    $.post("/stock/optionalStockAdd", {stockCode:stockCode}, function(response){
        if(response.success && response.data == 1){
            getOptionalStock();
        }else{
            alert(response.msg);
        }
    });
});

//删除自选股
$(document).on("click", "#user_optional_stock .stock_i_op", function(){
    var stockCode = $(this).attr("data-stock-code");
    $.post("/stock/optionalStockDel", {stockCode:stockCode}, function(response){
        if(response.success && response.data == 1){
            getOptionalStock();
        }else{
            alert(response.msg);
        }
    });
});

//股票列表添加自选股
$(document).on("click", "#stock_list_box .stock_i_op", function(){
    var stock_i_op = $(this);
    var stockCode = stock_i_op.attr("data-stock-code");
    $.post("/stock/optionalStockAdd", {stockCode:stockCode}, function(response){
        if(response.success && response.data == 1){
            getOptionalStock();
            stock_i_op.remove();
        }else{
            alert(response.msg);
        }
    });
});

//交易详情添加自选股
function stockDetailAddOptional(o){
    if($(o).text() == "已添加") return;
    $.post("/stock/optionalStockAdd", {stockCode:nowStockCode}, function(response){
        if(response.success && response.data == 1){
            getOptionalStock();
            $("#jia").text("已添加");
        }else{
            alert(response.msg);
        }
    });
}

//切换股票市场列表
function changeMarketType(data){
    $("#stockDetailMain").hide();
    $("#stockListMain").show();

    list_market_type = data;
    if (intervalStockList != null) {
        clearInterval(intervalStockList);
    }
    getStockList(0);
    intervalStockList = setInterval("getStockListFlush()", 5000);
}

//获取股票市场列表
function getStockList(appendType){
    $.ajax({
        url: '/quote/stock/market_match',
        type: 'POST',
        contentType:'application/json; charset=UTF-8',
        data: '{"v":"caimao.json.001","k":"a0aea509d9059bd18735f4b8d499cfcd","o":[{"act":155,"px":'+ list_start +',"pl":'+list_lens+', "p1":'+list_market_type+', "p2":'+list_rank+'}]}',
        dataType: 'json',
        processData: false,
        success: function (response) {
            if(response.success && response.data != ""){
                var stock_list_box = $("#stock_list_box");
                if(appendType == 1){
                    stock_list_box.append(parseStockList($.parseJSON(response.data)));
                }else{
                    stock_list_box.html(parseStockList($.parseJSON(response.data)));
                }
                stockListDivHeight = stock_list_box.height();
            }
        }
    });
}

//刷新股票列表
function getStockListFlush(){
    var lens = list_start + list_lens;
    $.ajax({
        url: '/quote/stock/market_match',
        type: 'POST',
        contentType:'application/json; charset=UTF-8',
        data: '{"v":"caimao.json.001","k":"a0aea509d9059bd18735f4b8d499cfcd","o":[{"act":155,"px":0,"pl":'+lens+', "p1":'+list_market_type+', "p2":'+list_rank+'}]}',
        dataType: 'json',
        processData: false,
        success: function (response) {
            if(response.success && response.data != ""){
                var stock_list_box = $("#stock_list_box");
                stock_list_box.html(parseStockList($.parseJSON(response.data)));
                stockListDivHeight = stock_list_box.height();
            }
        }
    });
}

//处理股票列表
function parseStockList(data){
    var html = "";
    for(var i in data.o[0].o){
        var stockData = data.o[0].o[i];
        var zd = parseFloat(stockData.h.cur - stockData.h.pre).toFixed(2);
        var zdClass = zd >=0?"colorUp":"colorDown";

        html+='<ul data-stock-code="'+stockData.h.code+'">' +
        '<li style="width: 76px;">　'+stockData.h.code+'</li>' +
        '<li style="width: 85px;">'+stockData.h.name+'</li>' +
        '<li class="'+zdClass+'">'+stockData.h.cur+'</li>' +
        '<li class="'+zdClass+'">'+zd+'</li>' +
        '<li class="'+zdClass+'">'+CMUTILS.formatPerNum((stockData.h.cur - stockData.h.pre) / stockData.h.pre, 2)+'%</li>' +
        '<li>'+stockData.m.open+'</li>' +
        '<li>'+stockData.m.high+'</li>' +
        '<li>'+(stockData.m.low > 10000?0:stockData.m.low)+'</li>' +
        '<li>'+stockData.h.pre+'</li>' +
        '<li>'+(stockData.m.low > 10000?0:CMUTILS.formatPer((stockData.m.high - stockData.m.low) / stockData.h.pre))+'%</li>' +
        '<li>'+parseFloat(stockData.e.pe).toFixed(2)+'</li>' +
        '<li>'+CMUTILS.formatNumToUnit(stockData.m.vol)+'</li>' +
        '<li>'+parseFloat(stockData.m.swap).toFixed(2)+'%</li>' +
        '<li style="width: 80px;">'+CMUTILS.formatNumToUnit(stockData.m.sum)+'</li>' +
        '<li style="width: 40px;">'+(user_stock_list.indexOf(stockData.h.code) >= 0?'':'<span class="stock_i_op" data-stock-code="'+stockData.h.code+'">+</span>')+'</li>' +
        '</ul>';
    }
    return html;
}

//股票列表排序
function stock_list_sort(o, sortCode){
    $(o).parent().find("span").removeClass("symbol_up symbol_down");
    if(sortCode == 10){
        list_rank = list_rank == 11?10:11;
        $(o).find("span").addClass(list_rank == 10?"symbol_down":"symbol_up");
    }else{
        list_rank = sortCode;
        $(o).find("span").addClass("symbol_down");
    }
}

//右侧浮动
$(document).on("click", "#control_stock_box", function(){
    var control_stock_box = $("#control_stock_box");
    var stockHqBox = $("#stockHqBox");
    if(stockHqBox.is(":hidden")){
        control_stock_box.text(">>收起行情图");
    }else{
        control_stock_box.text("展开行情图>>");
    }
    if(nowStockCode == null){
        if($("#stock_list_box").html() == ""){
            changeMarketType(70);
        }
    }
    stockHqBox.slideToggle();
});

//自选股选中
$(document).on("click", "#user_optional_stock tr td, #hot_optional_stock tr td", function(){
    var td_index = $(this).index();
    if(td_index < 3){
        nowStockCode = $(this).parent().attr("data-stock-code");
        nowStockName = $(this).parent().find("td").eq(0).text();
        getStockCodeInfo();
        $("#stockListMain").hide();
        $("#stockDetailMain").show();
    }
});

//股票列表点击显示详情
$(document).on("click", "#stock_list_box ul li", function(){
    var td_index = $(this).index();
    if(td_index < 14){
        nowStockCode = $(this).parent().attr("data-stock-code");
        nowStockName = $(this).parent().find("li").eq(1).text();
        getStockCodeInfo();
        $("#stockListMain").hide();
        $("#stockDetailMain").show();
    }
});

$(document).on("click", "#unbaleBuy", function(){
    Protocol.showUnableStock();
});

//获取指数
function getZhiShu(){
    $.ajax({
        url: '/quote/stock/market_match',
        type: 'POST',
        contentType:'application/json; charset=UTF-8',
        data: '{"v":"caimao.json.001","k":"a0aea509d9059bd18735f4b8d499cfcd","o":[{"act":109,"id1":000001,"tp1":1}]}',
        dataType: 'json',
        processData: false,
        success: function (response) {
            if(response.success && response.data != ""){
                var data = $.parseJSON(response.data);
                var zhishu_sh = $("#zhishu_sh");
                var is_class = data.o[0].o.h.cur >= data.o[0].o.h.pre?"colorUp":"colorDown";
                zhishu_sh.find("b").html(data.o[0].o.h.name);
                zhishu_sh.find("span").addClass(is_class).html(data.o[0].o.h.cur);
                zhishu_sh.find("label").addClass(is_class).html(CMUTILS.formatPer((data.o[0].o.h.cur - data.o[0].o.h.pre) / data.o[0].o.h.pre) + "%");
            }
        }
    });
    $.ajax({
        url: '/quote/stock/market_match',
        type: 'POST',
        contentType:'application/json; charset=UTF-8',
        data: '{"v":"caimao.json.001","k":"a0aea509d9059bd18735f4b8d499cfcd","o":[{"act":109,"id1":399001,"tp1":2}]}',
        dataType: 'json',
        processData: false,
        success: function (response) {
            if(response.success && response.data != ""){
                var data = $.parseJSON(response.data);
                var zhishu_sz = $("#zhishu_sz");
                var is_class = data.o[0].o.h.cur >= data.o[0].o.h.pre?"colorUp":"colorDown";
                zhishu_sz.find("b").html(data.o[0].o.h.name);
                zhishu_sz.find("span").addClass(is_class).html(data.o[0].o.h.cur);
                zhishu_sz.find("label").addClass(is_class).html(CMUTILS.formatPer((data.o[0].o.h.cur - data.o[0].o.h.pre) / data.o[0].o.h.pre) + "%");
            }
        }
    });
    $.ajax({
        url: '/quote/stock/market_match',
        type: 'POST',
        contentType:'application/json; charset=UTF-8',
        data: '{"v":"caimao.json.001","k":"a0aea509d9059bd18735f4b8d499cfcd","o":[{"act":109,"id1":399006,"tp1":2}]}',
        dataType: 'json',
        processData: false,
        success: function (response) {
            if(response.success && response.data != ""){
                var data = $.parseJSON(response.data);
                var zhishu_szc = $("#zhishu_szc");
                var is_class = data.o[0].o.h.cur >= data.o[0].o.h.pre?"colorUp":"colorDown";
                zhishu_szc.find("b").html(data.o[0].o.h.name);
                zhishu_szc.find("span").addClass(is_class).html(data.o[0].o.h.cur);
                zhishu_szc.find("label").addClass(is_class).html(CMUTILS.formatPer((data.o[0].o.h.cur - data.o[0].o.h.pre) / data.o[0].o.h.pre) + "%");
            }
        }
    });
}


//股票详情点击买
function stockDetailBuy() {
    if(nowStockCode == null || nowStockName == null) return;
    var tradeBox = $("#tradeBoxBuy");
    tradeBox.find(".stockInput").val(nowStockCode);
    tradeBox.find(".text_placeholder").eq(0).hide();
    tradeBox.find(".text_note").eq(0).text(nowStockName);
    $(".stockList").hide();
    nowSelectTrade = "buy";
    isQuantity = true;
    //初始化股票信息
    getStockCodeInfo();
}

//股票详情点击卖
function stockDetailSell(){
    if(nowStockCode == null || nowStockName == null) return;
    var tradeBox = $("#tradeBoxSell");
    tradeBox.find(".stockInput").val(nowStockCode);
    tradeBox.find(".text_placeholder").eq(0).hide();
    tradeBox.find(".text_note").eq(0).text(nowStockName);
    $(".stockList").hide();
    nowSelectTrade = "sell";
    isQuantity = true;
    //初始化股票信息
    getStockCodeInfo();
}

function showStockDetail(){
    $("#stockListMain").hide();
    $("#stockDetailMain").show();
}

//获取指数
getZhiShu();
setInterval("getZhiShu()", 5000);
//获取自选股
getOptionalStock();
setInterval("getOptionalStock()", 5000);
function init_kline(){
    if(chartObj != null){
        chartObj.toStock(nowStockCode, nowStockMarketType);
    }
}

function stockHqPlay(){
    if(chartObj != null){
        if(!$("#stockDetailMain").is(":hidden") && !$("#stockHqBox").is(":animated") && nowStockCode != null && nowStockMarketType != null){
            chartObj.play()
        }else{
            chartObj.pause();
        }
    }else{
        if(!$("#stockDetailMain").is(":hidden") && !$("#stockHqBox").is(":animated") && nowStockCode != null && nowStockMarketType != null){
            CFG.user.stockID = nowStockCode;
            CFG.user.ex = nowStockMarketType;
            CFG.user.double = 1;
            CFG.user.timeShare = 1;
            CFG.user.popstop = 1;
            CFG.user.vMinRuleHeight = 70;
            CFG.user.schemes = "mobile";
            CFG.user.pause = 1;
            chartObj = chart('kline');
            singleLayout(chartObj);
            chartObj.play()
        }
    }
}
setInterval("stockHqPlay()", 1000);

function calYK(marketValue, costBalance){
    if(costBalance == 0){
        return 0;
    }else{
        return CMUTILS.formatPer((marketValue - costBalance) / costBalance);
    }
}

//初始化的
$(document).ready(function(){
    $("#combine_account_box").find("li").eq(1).click();
    //股票列表滚动
    $("#stock_list_box").scroll(function(){
        stockListScrollHeight = $(this)[0].scrollHeight;
        stockListScrollTop = $(this)[0].scrollTop;
        if(stockListScrollTop + stockListDivHeight >= stockListScrollHeight){
            list_start += list_lens;
            getStockList(1);
        }
    });
});

//获取历史委托、历史成交