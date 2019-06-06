/**
 * Created by xavier on 15/7/7.
 */

/**
 * 显示各种协议啥的
 * @type {{}}
 */
Protocol = {

    /* 借款协议 */
    showLoanProtocol : function() {
        $("#_dialog").load("/protocol/loan");
        this.bindClose();
    },

    /* P2P协议 */
    showP2PProtocol : function() {
        $("#_dialog").load("/protocol/p2p");
        this.bindClose();
    },

    /* 注册协议 */
    showRegiesterProtocol : function() {
        $("#_dialog").load("/protocol/regiester");
        this.bindClose();
    },

    /* 借款协议 */
    showUnableStock : function() {
        $("#_dialog").load("/protocol/unableStock");
        this.bindClose();
    },

    bindClose : function() {
        // 绑定关闭的事件
        $("#_dialog").on("click", "#dialog_close", function() {
            $("#_dialog").find("#dialog_point").remove();
        });
    }
};