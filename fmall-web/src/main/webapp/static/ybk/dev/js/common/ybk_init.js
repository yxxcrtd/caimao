/**
 * Created by Administrator on 2015/9/22.
 */

$(document).ready(function() {
    $.ajax({
        url : '/ybk/get_user_info.html',
        type : 'GET',
        dataType : 'json',
        success : function(res) {
            if (res.success == true) {
                $('.noLogin').hide();
                $('.login').show();
                if (res.data.name != '' && res.data.name != null) {
                    $('#_userName').html(res.data.mobile);
                } else {
                    $('#_userName').html(res.data.mobile);
                }
            } else {
                $('.noLogin').show();
                $('.login').hide();
            }
        }
    });
});
