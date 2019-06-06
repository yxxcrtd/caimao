//菜单js
function left_show(id){
    $("#nav").find("li").removeClass("cur").siblings("#top_nav_" + id).addClass("cur");
    $(".menu_list").addClass("hidden").siblings("#left_nav_" + id).removeClass("hidden");
}

function show_alert(msg,title) {
    if(title){
        $('#myModalLabel').html(title);
    }
    $('#show_alert_msg').html(msg);
    $('#myModal').modal('show');
}