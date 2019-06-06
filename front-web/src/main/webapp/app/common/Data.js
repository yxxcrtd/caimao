/**
 * generic post request, the data format is json
 * */
define([
    'dojo/request',
    'app/common/Ajax',
    'app/common/Global',
    'dojo/_base/config'
], function (request, Ajax, Global, cfg) {
    var user, userExtra, account, tradePwd, banks, bankcards, product, productDetails, protocol,
        protocolP1, contract, homsContracts, repayData, homsAccounts, homsAsset, hac,
        defers, tenders, tenderDetail, tenderPayUsers, statisticInfo, time, myTenders, hisContracts, curContracts,
        applyLoans, cache = {}, grades;
    return {

        getApplyLoans: function (params, reload) {
            if (reload) {
                applyLoans = null;
            }
            return applyLoans || Ajax.get(Global.baseUrl + '/financing/loanapply/page', params).then(function (response) {
                    if (response.success) {
                        applyLoans = response.data;
                    }
                    return applyLoans;
                });
        },

        getCurContracts: function (params, reload) {
            if (reload) {
                curContracts = null;
            }
            return curContracts || Ajax.get(Global.baseUrl + '/financing/contract/page', params).then(function (response) {
                    if (response.success) {
                        curContracts = response.data;
                    }
                    return curContracts;
                });
        },

        getHisContracts: function (params, reload) {
            if (reload) {
                hisContracts = null;
            }
            return hisContracts || Ajax.get(Global.baseUrl + '/financing/hiscontract/page', params).then(function (response) {
                    if (response.success) {
                        hisContracts = response.data;
                    }
                    return hisContracts;
                });
        },

        getTime: function (reload) {
            if (reload) {
                time = null;
            }
            return Ajax.get(Global.baseUrl + '/time').then(function (response) {
                if (response.success) {
                    time = response.data;
                }
                return time;
            });
        },

        getUser: function () {
            return user || Ajax.get(Global.baseUrl + '/user').then(function (response) {
                    if (response.success) {
                        user = response.data;
                    }
                    return user;
                });
        },

        getUserExtra: function () {
            return userExtra || Ajax.get(Global.baseUrl + '/userextra').then(function (response) {
                    if (response.success) {
                        userExtra = response.data;
                    }
                    return userExtra;
                });
        },

        getAccount: function () {
            return account || Ajax.get(Global.baseUrl + '/account').then(function (response) {
                    if (response.success) {
                        account = response.data;
                    }
                    return account;
                });
        },

        getHAC: function () {
            return hac || Ajax.get(Global.baseUrl + '/home/richhbi').then(function (response) {
                    if (response.success) {
                        hac = response.data || [];
                    }
                    return hac || [];
                });
        },

        getHomsAccounts: function () {
            return homsAccounts || Ajax.get(Global.baseUrl + '/home/richhbi').then(function (response) {
                    if (response.success) {
                        homsAccounts = response.data;
                    }
                    return homsAccounts;
                });
        },

        getHomsAsset: function (homs_fund_account, homs_combine_id) {
            return homsAsset || Ajax.get(Global.baseUrl + '/homs/assetsinfo', {
                    homs_fund_account: homs_fund_account,
                    homs_combine_id: homs_combine_id
                }).then(function (response) {
                    if (response.success) {
                        homsAsset = response.data;
                    }
                    return homsAsset;
                });
        },

        getTradePwd: function () {
            return tradePwd || Ajax.get(Global.baseUrl + '/tradepwd').then(function (response) {
                    if (response.success) {
                        tradePwd = response.data;
                    }
                    return tradePwd;
                });
        },

        getBanks: function () {
            return banks || Ajax.get(Global.baseUrl + '/bank', {
                    'pay_company_no': cfg.paymentPlatform == 'a' ? 1 : (cfg.paymentPlatform == 'b' ? 2 : 3)//1:通联 2：易宝
                }).then(function (response) {
                    if (response.success) {
                        banks = response.data;
                    }
                    return banks;
                });
        },

        getBankcards: function () {
            return bankcards || Ajax.get(Global.baseUrl + '/bankcard').then(function (response) {
                    if (response.success) {
                        bankcards = response.data;
                    }
                    return bankcards;
                });
        },

        getProtocol: function (params, reload) {
            if (reload) {
                protocol = null;
            }
            return protocol || request(Global.baseUrl + "/protocolp" + params.id + ".htm").then(function (response) {
                    protocol = response;
                    return protocol;
                });
        },

        getProtocolP1: function () {
            return protocolP1 || request(Global.baseUrl + '/protocolp1.htm').then(function (response) {
                    protocolP1 = response;
                    return protocolP1;
                });
        },

        getProduct: function (params, reload) {
            if (reload) {
                product = null;
            }
            return product || Ajax.get(Global.baseUrl + '/product', params).then(function (response) {
                    if (response.success) {
                        product = response.data;
                    }
                    return product;
                });
        },

        getProductDetails: function (params, reload) {
            if (reload) {
                productDetails = null;
            }
            return productDetails || Ajax.get(Global.baseUrl + '/proddetail', params).then(function (response) {
                    if (response.success) {
                        productDetails = response.data;
                    }
                    return productDetails;
                });
        },

        getDeposit: function (type, contract) { // 0: loan, 1: add, 2: defer
            return Ajax.get(Global.baseUrl + '/deposit', {
                'contract_no': contract || '',
                'loan_apply_action': type
            }).then(function (response) {
                if (response.success) {
                    return response.data;
                }
                return 0;
            });
        },

        //借款详情
        getContract: function (contractNo) {
            return contract || Ajax.get(Global.baseUrl + '/financing/contract/detail', {
                    'contract_no': contractNo
                }).then(function (response) {
                    if (response.success) {
                        contract = response.data;
                    }
                    return contract;
                });
        },

        getContractsByHoms: function (params, reload) {
            if (reload) {
                homsContracts = null;
            }
            return homsContracts || Ajax.get(Global.baseUrl + '/financing/contract/list', params).then(function (response) {
                    if (response.success) {
                        homsContracts = response.data;
                    }
                    return homsContracts;
                });
        },

        //还款数据准备
        getRepayData: function (contractNo, reload) {
            if (reload) {
                repayData = null;
            }
            return repayData || Ajax.post(Global.baseUrl + "/financing/repay/data", {
                    'contract_no': contractNo
                }).then(function (response) {
                    if (response.success) {
                        repayData = response.data;
                    }
                    return repayData;
                });
        },

        getDefers: function (params, reload) {
            if (reload) {
                defers = null;
            }
            return defers || Ajax.get(Global.baseUrl + '/financing/defered/page', params).then(function (response) {
                    if (response.success) {
                        defers = response.data;
                    }
                    return defers;
                });
        },

        getTenders: function (params, reload) {
            if (reload) {
                tenders = null;
            }
            return tenders || Ajax.get(Global.baseUrl + '/p2p/subject/page', params).then(function (response) {
                    if (response.success) {
                        tenders = response.data;
                    }
                    return tenders;
                });
        },

        getMyTenders: function (params, reload) {
            if (reload) {
                myTenders = null;
            }
            return myTenders || Ajax.get(Global.baseUrl + '/p2p/subject/personalInvs', params).then(function (response) {
                    if (response.success) {
                        myTenders = response.data;
                    }
                    return myTenders;
                });
        },

        getTenderDetail: function (params, reload) {
            if (reload) {
                tenderDetail = null;
            }
            return tenderDetail || Ajax.get(Global.baseUrl + '/p2p/subject/detail', params).then(function (response) {
                    if (response.success) {
                        tenderDetail = response.data;
                    }
                    return tenderDetail;
                });
        },

        getTenderPayUsers: function (params, reload) {
            if (reload) {
                tenderPayUsers = null;
            }
            return tenderPayUsers || Ajax.get(Global.baseUrl + '/p2p/payuser', params).then(function (response) {
                    if (response.success) {
                        tenderPayUsers = response.data;
                    }
                    return tenderPayUsers;
                });
        },

        getStatisticInfo: function () {
            return statisticInfo || Ajax.get(Global.baseUrl + '/financing/statistic').then(function (response) {
                    if (response.success) {
                        statisticInfo = response.data;
                    }
                    return statisticInfo;
                });
        },
        getTenderNewcomers: function (params, reload) {
            if (reload) {
                cache['tenderNewcomers'] = null;
            }
            return cache['tenderNewcomers'] || Ajax.get(Global.baseUrl + '/p2p/subject/top', params).then(function (response) {
                    if (response.success) {
                        cache['tenderNewcomers'] = response.data;
                    }
                    return cache['tenderNewcomers'];
                });
        },

        // get homs child account list
        getTradeAccounts: function (reload) {
            if (reload) {
                cache['tradeAccounts'] = null;
            }
            return cache['tradeAccounts'] || Ajax.get(Global.baseUrl + '/homs/combineid').then(function (response) {
                    if (response.success) {
                        cache['tradeAccounts'] = response.data;
                    }
                    return cache['tradeAccounts'];
                });
        },

        // get homs child account asset
        getTradeAccountAsset: function (params, reload) {
            if (reload) {
                cache['tradeAccountAsset' + params.homs_fund_account + params.homs_combine_id] = null;
            }
            return cache['tradeAccountAsset' + params.homs_fund_account + params.homs_combine_id] || Ajax.get(Global.baseUrl + '/homs/assetsinfo', params).then(function (response) {
                    if (response.success) {
                        cache['tradeAccountAsset' + params.homs_fund_account + params.homs_combine_id] = response.data;
                    }
                    return cache['tradeAccountAsset' + params.homs_fund_account + params.homs_combine_id];
                });
        },
        getStockCurentrust: function (params, reload) {
            if (reload) {
                cache['stockCurentrust' + params.homs_fund_account + params.homs_combine_id + params.userId] = null;
            }
            return cache['stockCurentrust' + params.homs_fund_account + params.homs_combine_id + params.userId] || Ajax.get(Global.baseUrl + '/stock/curentrust', params).then(function (response) {
                    if (response.success) {
                        cache['stockCurentrust' + params.homs_fund_account + params.homs_combine_id + params.userId] = response.data;
                    }
                    return cache['stockCurentrust' + params.homs_fund_account + params.homs_combine_id + params.userId];
                });
        },

        getGrades: function (reload) {
            if (reload) {
                cache['grades'] = null;
            }
            return cache['grades'] || Ajax.get(Global.baseUrl + '/grades').then(function (response) {
                    if (response.success) {
                        cache['grades'] = response.data;
                    }
                    return cache['grades'];
                });
        }
    }
});