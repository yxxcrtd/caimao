define([
    'app/common/Global',
    'app/common/Array'
], function(Global, arrayUtil) {
    //  1 按日融资， 2 按月融资， 3 实盘大赛， 4 最新特惠， 0 免费体验
    var products = {
        0: 1,
        1: 4,
        2: 3,
        10: 800461611335681,
        11: 800461779107841,
        12: 800461779107843
        //13: 800461779107843,
        //14: 800461812662274,
    };
	return {

        getProductId: function(type) {
            return products[type];
        },

        addable: function(product) {
            return product.prodAddMode == '1';
        },

        deferable: function(product) {
            return product.prodDeferedMode == '1';
        },

        getRatios: function(product) {
            var i = 0, len = product.prodLoanRatioMax - product.prodLoanRatioMin,
                ratios = [];
            for (; i <= len; i++) {
                ratios.push(product.prodLoanRatioMin + i);
            }
//            if (product.prodId == products[1]) {
//                var temp = [];
//                if (ratios.length >=3 ) {
//                    temp.push(ratios[0]);
//                    temp.push(ratios[Math.ceil(ratios.length / 2) - 1]);
//                    temp.push(ratios[ratios.length - 1]);
//                } else {
//                    temp = ratios;
//                }
//                ratios = temp;
//            }
            return ratios;
        },

        getDetail: function(product, productDetails, ratio, loanAmount, term) {
            var items = product.prodBillAccords.split(','),
                leftDetails = productDetails;
            if (typeof ratio != 'undefined' && items.indexOf('0') != -1) {
                leftDetails = arrayUtil.filter(leftDetails, function(i) {
                    return parseInt(ratio) >= i.loanRatioFrom && parseInt(ratio) < i.loanRatioTo;
                });
            }

            if (typeof loanAmount != 'undefined' && items.indexOf('1') != -1) {
                leftDetails = arrayUtil.filter(leftDetails, function(i) {
                    return parseFloat(loanAmount) >= i.loanAmountFrom && parseFloat(loanAmount) < i.loanAmountTo;
                });
            }

            if (typeof term != 'undefined' && items.indexOf('2') != -1) {
                leftDetails = arrayUtil.filter(leftDetails, function(i) {
                    return parseInt(term) >= i.loanTermFrom && parseInt(term) < i.loanTermTo;
                });
            }
            return leftDetails.length > 0 ? leftDetails[0] : null;
        },

        /**
         * 获取产品P2P的配置项
         * @param product   产品
         * @param p2pConfigList 产品总的配置
         * @param ratio 杠杆倍数
         */
        getP2PConfig: function(product, p2pConfigList, ratio) {
            // 如果没有配置产品像，返回null
            if (typeof p2pConfigList == 'undefined' || p2pConfigList.length == 0) return null;
            var p2pConfig = null;
            if (typeof ratio != 'undefined') {
                p2pConfig = arrayUtil.filter(p2pConfigList, function(i) {
                    return parseInt(ratio) == i.prodLever;
                });
            }
            return p2pConfig.length > 0 ? p2pConfig[0] : null;
        },

        getBill: function(product, productDetail) {
            var type = product.prodBillType,// 0:free, 1: interest, 2: fee
                unit = product.interestAccrualMode, // 0: day, 1: day, 2: month
                during = product.interestSettleDays;
            if (unit == '2') {
                during = parseInt(product.interestSettleDays / 30);
            }
            var amount = 0;
            if (type == 1) {
                amount = productDetail ? productDetail.interestRate : product.interestRate;
            } else if (type == 2) {
                amount = productDetail ? productDetail.fee : product.fee;
            }
            return [type, amount, unit, during];
        },

        getBillText: function(bill, loanAmount) {
            if (bill) {
                if (bill[0] == 0) {
                    return '免费';
                }
                if (bill[0] == 1) {
                    if (typeof bill[1] == 'undefined') {
                        return '无效值';
                    }
                    if (bill[2]==2) {
                        return '月利率：' + Global.formatNumber(bill[1] * 100, 2) + '%';
                    } else {
                        if (loanAmount) {
                            return Global.formatAmount(bill[1] * parseFloat(loanAmount)) + ' 元/天';
                        } else {
                            return '日利率：' + Global.formatNumber(bill[1] * 100, 3) + '%';
                        }
                    }
                }
                if (bill[0] == 2) {
                    if (typeof bill[1] == 'undefined') {
                        return '无效值';
                    }
                    return '管理费：' + Global.formatAmount(bill[1]) + ' 元';
                }
            } else {
                return '';
            }
        }
	}
});