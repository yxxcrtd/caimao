define([
    'app/common/Global',
    'app/common/Array',
    'app/common/Data',
    'dojo/when'
], function(Global, arrayUtil, Data, when) {
	return {

        isTrust: function(successCallback, errorCallback) { // 实名认证
            when(Data.getUser(), function(user) {
                if (user && user.isTrust == '1') {
                    successCallback && successCallback();
                } else {
                    errorCallback && errorCallback();
                }
            });
        },

        getBindBankcard: function(successCallback) { // 获得一张绑定银行卡
            var bankcard;
            when(Data.getBankcards(), function(bankcards) {
                var i = 0, len = bankcards.length;
                for (; i < len; i++) {
                    if (bankcards[i].bankCardStatus == '1') {
                        bankcard = bankcards[i];
                        break;
                    }
                }
                successCallback(bankcard);
            });
        },

        isTradePwd: function(successCallback, errorCallback) {
            when(Data.getTradePwd(), function(data) {
                if (data && data.userTradePwdStrength) {
                    successCallback && successCallback();
                } else {
                    errorCallback && errorCallback();
                }
            });
        }

	}
});