/**
 * 配资合约
 * Created by xavier on 15/6/30.
 */


// @koala-prepend "./lib/jquery-1.11.1.min.js"
// @koala-prepend "./common/utils.js"
// @koala-prepend "./common/pz.js"
// @koala-prepend "./common/Data.js"
// @koala-prepend "./lib/RSA.js"
// @koala-prepend "./ux/alert.js"
// @koala-prepend "./lib/formValidate.js"
// @koala-prepend "./v2/actionTip.js"

$(document).ready(function() {

    /**
     * 追加合约
     */
    // 显示追加合约的框框
    $('.zhuijiaheyue').click(function() {
        var contractNo = $(this).attr("data-contract-no");
        PZ.dialogContractAdd(contractNo);
    });

    /**
     * 追加保证金
     */
    // 显示追加保证金的框框
    $('.zhuijiabaozhengjin').click(function() {
        var contractNo = $(this).attr("data-contract-no");
        PZ.dialogContractIn(contractNo);
    });


    /**
     * 转出盈利
     */
    // 显示转出盈利的框框
    $('.zhuanchuyingli').click(function() {
        var contractNo = $(this).attr("data-contract-no");
        PZ.dialogContractOut(contractNo);
    });

    /**
     * 还款
     */
    // 显示还款的框框
    $('.huankuan').click(function() {
        var contractNo = $(this).attr("data-contract-no");
        PZ.dialogContractRepay(contractNo);
    });

    /**
     * 展期
     */
    // 显示展期的框框
    $('.zhanqi').click(function() {
        var contractNo = $(this).attr("data-contract-no");
        PZ.dialogContractDefered(contractNo);
    });


    /**
     * 关闭框框按钮
     */
    $('#_dialog').on("click", "#dialog_close", function() {
        PZ.closeDialog();
    });
});