// @koala-prepend "./../lib/jquery-1.11.1.min.js"
// @koala-prepend "./../common/utils.js"
// @koala-prepend "./../common/ybk_init.js"

$(document).ready(function() {
    var exchageCompositeIndexUrl = "/api/ybk/query_composite_index",
        exchangeListPoint = '#exchangeListPoint',
        exchangeListTmpl = $('#exchangeListTmpl').html();

    function refreshExchangeComposite() {
        $.ajax({
            url : exchageCompositeIndexUrl,
            type : 'GET',
            dataType : "json",
            success : function (res) {
                if (res.success == true) {
                    $(exchangeListPoint).html(CMUTILS.rentmpl(exchangeListTmpl, {data : res.data}));
                }
            }
        });
    }

    refreshExchangeComposite();
    setInterval(refreshExchangeComposite(), 10000);

});




