/**
 * Created by xavier on 15/7/7.
 */

/**
 * 显示各种协议啥的
 * @type {{}}
 */
Protocol = {

    /* 邮币卡协议 */
    showYbkProtocol : function() {
        $("#_dialog").load("/ybk/protocol/ybk");
        this.bindClose();
    },

    /* QQ群更多 */
    showQQProtocol : function() {
        $("#_dialog").load("/ybk/protocol/qq");
        this.bindClose();
    },


    bindClose : function() {
        // 绑定关闭的事件
        $("#_dialog").on("click", "#dialog_close", function() {
            $("#_dialog").find("#dialog_point").remove();
        });
    }
};