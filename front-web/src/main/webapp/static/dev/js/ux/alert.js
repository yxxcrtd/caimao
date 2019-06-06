/**
 * Created by xavier on 15/7/3.
 */

Alert = {
    // 控件
    dialogId : $("#_dialog"),
    html : '<div class="layout" id="dialog_point"><h3 class="title">操作成功 <a href="javascript:void(0);" id="__dialog_close"><i class="icons icon_close"></i></a></h3><p class="success-msg"><i></i> <span>${__dialog_msg}</span></p><div class="group m_t_30" style="text-align: center;"><button class="btn btn_bule" id="__dialogOK">${__dialogOK}</button></div></div>',
    btnId : '#__dialogOK',
    closeId : '#__dialog_close',

    show : function(msg, btnStr, funEvent) {
        this.html = this.html.replace("${__dialog_msg}", msg);
        if (typeof btnStr == "undefined")  btnStr = "确定";
        this.html = this.html.replace("${__dialogOK}", btnStr);
        // 显示提示框
        $('#_dialog').html(this.html);

        if (typeof funEvent == "undefined") funEvent = this.close;
        // 绑定事件
        $('#_dialog').on("click", "#__dialogOK", funEvent);
        // 关闭按钮绑定事件
        $('#_dialog').on("click", "#__dialog_close", this.close);
        return this;
    },
    close : function() {
        $("#_dialog").find("#dialog_point").remove();
        $('#_dialog').find('.mask').remove();
    }
};