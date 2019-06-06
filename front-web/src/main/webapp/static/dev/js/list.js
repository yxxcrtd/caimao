/**
 * Created by Administrator on 2015/6/5.
 */
// @koala-prepend "./lib/jquery-1.11.1.min.js"

//点击展开关闭
function change_content(id){
    if(id > 0){
        if( $('#content_' + id).css("display") == "none"){
            $('#content_' + id).show();
            $('#btn_' + id).show();
        }else{
            $('#content_' + id).hide();
            $('#btn_' + id).hide();
        }
    }
}
