// @koala-prepend "./../lib/jquery-1.11.1.min.js"
// @koala-prepend "./../common/utils.js"
// @koala-prepend "./../common/ybk_init.js"
// @koala-prepend "./../ux/protocol.js"

var exchageMarketUrl = "/api/ybk/query_collection_ranking",
    exchangeMarketPoint = '#marketPoint',
    page = 1,
    limit = 10,
    orderColumn = 'changeRate',
    orderDir = 'DESC';
$(document).ready(function() {
    var aArray = document.querySelector('#tabs').querySelectorAll('a');
    if(aArray.length > 0){
        aArray[0].click();
    }
});
function refreshMarket(shortName) {
    $.ajax({
        url: exchageMarketUrl,
        data: {
            exchange_short_name: shortName,
            limit: limit,
            page: page,
            order_column: orderColumn,
            orderDir: orderDir
        },
        type: 'GET',
        dataType: "json",
        success: function (res) {
            if (res.success == true) {
                $(exchangeMarketPoint).html(parseDataToTr(shortName,res.data.items));
            }
        }
    });
}

function parseDataToTr(shortName,data){
    var trStr = "";
    for(var i = 0,l = data.length; i < l; i++){
        trStr+="<tr onclick=\"window.location.href='/ybk/market_char.html?name="+shortName+"&code="+data[i].code+"'\">";
        if(i<3){
            trStr+="<td><em>"+(i+1)+"</em></td>";
        } else {
            trStr+="<td><i>"+(i+1)+"</i></td>";
        }
        trStr+="<td>"+data[i].code+"</td>";
        trStr+="<td>"+data[i].name+"</td>";
        trStr+="<td class='right'><b "+(data[i].changeRate > 0 ? 'class=red' : 'class=green')+">"+(data[i].currentPrice / 100).toFixed(2)+"</b> </td>";
        trStr+="<td class='right'><b "+(data[i].changeRate > 0 ? 'class=red' : 'class=green')+">"+(data[i].changeRate / 100).toFixed(2)+"%</b> </td>";
        trStr+="<td class='right'>"+(data[i].openPrice / 100).toFixed(2)+"</td>";
        trStr+="<td class='right'>"+(data[i].closePrice / 100).toFixed(2)+"</td>";
        trStr+="<td class='right'>"+(shortIndex(data[i].totalAmount/100,2,10000,['万','亿','兆']))+"</td>"
        trStr+="<td class='right'>"+(shortIndex(data[i].totalMoney / 100,2,10000,['万','亿','兆']))+"</td>";
        trStr+="<td class='right'>"+(data[i].highPrice / 100).toFixed(2)+"</td>";
        trStr+="<td class='right'>"+(data[i].lowPrice / 100).toFixed(2)+"</td>";
        trStr+="</tr>";
    }
    return trStr;
}

