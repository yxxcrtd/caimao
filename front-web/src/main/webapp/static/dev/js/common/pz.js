/**
 * Created by Administrator on 2015/6/24.
 */
PZ = {
    /**
     * 获取融资的产品配置
     * @param productId
     * @returns {*}
     */
    getProductInfo : function (productId) {
        var productInfo = null;
        $.ajax({
            url : "/pz/product",
            type : "GET",
            data : {product_id : productId},
            dataType : "json",
            async : false,  // 要同步，不让返回值为空
            success : function(res) {
                if (res.success == true) {
                    productInfo = res.data;
                }
            }
        });
        return productInfo;
    },

    /**
     * 获取产品配置的详情
     * @param productId
     * @returns {*}
     */
    getProductDetail : function (productId) {
        var productDetail = null;
        $.ajax({
            url : "/pz/proddetail",
            type : "GET",
            data : {product_id : productId},
            dataType : "json",
            async : false,  // 要同步，不让返回值为空
            success : function(res) {
                if (res.success == true) {
                    productDetail = res.data;
                }
            }
        });
        return productDetail;
    },
    /**
     * 获取融资产品的P2P配置
     * @param productId
     * @returns {*}
     */
    getP2PConfig : function (productId) {
        var p2pConfig = null;
        $.ajax({
            url : "/p2p/product/config",
            type : "GET",
            data : {product_id : productId},
            dataType : "json",
            async : false,  // 要同步，不让返回值为空
            success : function(res) {
                if (res.success == true) {
                    p2pConfig = res.data;
                }
            }
        });
        return p2pConfig;
    },

    /**
     * 获取这被杠杆的费率
     * @param productDetail 产品详情
     * @param loanAmount    融资金额
     * @param lever 杠杆倍数
     */
    getProdFee : function (productDetail, loanAmount, lever) {
        for (var i = 0; i < productDetail.length; i++ ) {
            var detail = productDetail[i];
            // 在设定的杠杆中间
            if (detail.loanRatioFrom <= lever && lever < detail.loanRatioTo) {
                // 在设定的费率之间
                if (detail.loanAmountFrom <= loanAmount && loanAmount < detail.loanAmountTo) {
                    return detail.interestRate;
                }
            }
        }
        return false;
    },
    /**
     * 获取融资金额、杠杆倍数的产品详情
     * @param productDetail
     * @param loanAmount
     * @param lever
     * @returns {*}
     */
    queryProdDetail : function (productDetail, loanAmount, lever) {
        for (var i = 0; i < productDetail.length; i++ ) {
            var detail = productDetail[i];
            // 在设定的杠杆中间
            if (detail.loanRatioFrom <= lever && lever < detail.loanRatioTo) {
                // 在设定的费率之间
                if (detail.loanAmountFrom <= loanAmount && loanAmount < detail.loanAmountTo) {
                    return detail;
                }
            }
        }
        return false;
    },

    /**
     * 从P2P配置中获取指定杠杆的配置
     * @param p2pConfig
     * @param lever
     * @returns {*}
     */
    queryP2PConfig : function (p2pConfig, lever) {
        var pConfig = null;
        if (p2pConfig.productConfig.length > 0) {
            for (var i = 0, l = p2pConfig.productConfig.length; i < l; i++) {
                var tmp = p2pConfig.productConfig[i];
                if (tmp.prodLever == lever) {
                    return tmp;
                }
            }
        }
        return pConfig;
    },

    /**
     * 弹出对话框
     */
    // 关闭弹框
    closeDialog : function() {
        $("#_dialog").find("#dialog_point").remove();
        $('#_dialog').find('.mask').remove();
    },
    // 追加合约的对话框
    dialogContractAdd : function (contractNo) {
        $('#_dialog').load("/pz/dialog/add.html?contract_no="+contractNo);
    },
    // 追加保证金的对话框
    dialogContractIn : function (contractNo) {
        $('#_dialog').load("/pz/dialog/fundin.html?contract_no="+contractNo);
    },
    // 转出盈利的对话框
    dialogContractOut : function (contractNo) {
        $('#_dialog').load("/pz/dialog/fundout.html?contract_no="+contractNo);
    },
    // 还款的对话框
    dialogContractRepay : function (contractNo) {
        $('#_dialog').load("/pz/dialog/repay.html?contract_no="+contractNo);
    },
    // 还款的对话框
    dialogContractDefered : function (contractNo) {
        $('#_dialog').load("/pz/dialog/defered.html?contract_no="+contractNo);
    },
    // 显示交易密码的对话框
    dialogTradePwd : function() {
        $('#_dialog').load("/pz/dialog/tradepwd.html");
    }
};
