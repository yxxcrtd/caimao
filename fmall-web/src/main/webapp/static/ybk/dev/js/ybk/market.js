// @koala-prepend "./../lib/jquery-1.11.1.min.js"
// @koala-prepend "./../common/utils.js"
// @koala-prepend "./../common/ybk_init.js"


$(document).ready(function() {
    var exchageMarketUrl = "/api/ybk/query_collection_ranking",
        exchangeMarketPoint = '#marketPoint',
        exchangeMarketTmpl = $('#marketTmpl').html(),
        pagePoint = $('#page'),
        page = 1,
        limit = 50,
        orderColumn = 'code',
        orderDir = 'ASC';

    // 排序字段的点击效果
    $('.orderSort').click(function(){
        // 取消所有的样式
        $(this).parent().parent().find('i').removeClass('desc').removeClass('asc');
        orderColumn = $(this).attr("data-cloumn");
        if (orderDir == 'ASC') {
            orderDir = 'DESC';
            $(this).parent().find('i').addClass('desc');
        } else {
            orderDir = 'ASC';
            $(this).parent().find('i').addClass('asc');
        }
        page = 1;
        refreshMarket();
    });

    function refreshMarket() {
        $.ajax({
            url : exchageMarketUrl,
            data : {exchange_short_name : shortName, limit : limit, page : page, order_column : orderColumn, order_dir : orderDir},
            type : 'GET',
            dataType : "json",
            success : function (res) {
                if (res.success == true) {
                    $(exchangeMarketPoint).html(CMUTILS.rentmpl(exchangeMarketTmpl, {data : res.data.items}));
                    generagePage(page, limit, res.data.totalCount);
                }
            }
        });
    }

    // 生成分页的html
    function generagePage(curPage, limit, totalNum) {
        curPage = parseInt(curPage);
        var pageHtml = "",
            totalPage = Math.ceil(totalNum / limit);
        pageHtml += '<li class="btn pageNum" data-page="1"><a href="javascript:void(0);">首页</a></li>';
        pageHtml += '<li class="btn pageNum" data-page="' + ((curPage - 1) < 0 ? 0 : (curPage - 1)) + '"><a href="javascript:void(0);">上一页</a></li>';
        for (var i = 5; i >= 1 ; i--) {
            if (curPage - i > 0) {
                pageHtml += '<li class="number pageNum" data-page="' + (curPage - i) + '">' + (curPage - i) + '</li>';
            }
        }
        pageHtml += '<li class="number pageNum cur" data-page="' + (curPage) + '">' + (curPage) + '</li>';
        for (var i = 1; i <= 5; i++) {
            if (curPage + i <= totalPage) {
                pageHtml += '<li class="number pageNum" data-page="' + (curPage + i) + '">' + (curPage + i) + '</li>';
            }
        }
        pageHtml += '<li class="btn pageNum" data-page="' + ((1 + curPage) > totalPage ? totalPage : (1 + curPage) )+ '"><a href="javascript:void(0);">下一页</a></li>';
        pageHtml += '<li class="btn pageNum" data-page="'+totalPage+'"><a href="javascript:void(0);">末页</a></li>';
        pagePoint.html(pageHtml);
    }

    $(pagePoint).on('click', 'li', function(e) {
        page = $(this).attr('data-page');
        refreshMarket();
    });

    refreshMarket();
    setInterval(refreshMarket(), 10000);

});




