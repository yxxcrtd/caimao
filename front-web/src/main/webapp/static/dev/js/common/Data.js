/**
 * AJAX获取数据方法
 * Created by xavier on 15/6/25.
 */


Data = {
    _key : null,
    _userInfo : null,
    _accountInfo : null,
    _userExtInfo : null,
    _notReadMsgNum : null,
    _newRegisterDate : null,

    /**
     * 获取登录的KEY
     * @returns {*}
     */
    getKey : function() {
        if (this._key) return this._key;
        var e = this;
        $.ajax({
            url : "/sec/rsa",
            type : "GET",
            dataType : "json",
            async : false,  // 要同步，不让返回值为空
            success : function(res) {
                if (res.success == true) {
                    var modulus = res.data.modulus;
                    var exponent = res.data.exponent;
                    e._key = RSAUtils.getKeyPair(exponent, '', modulus);
                }
            }
        });
        return this._key;
    },

    /**
     * 获取用户的账户资产信息
     * @returns {*}
     */
    getAccount : function() {
        if (this._accountInfo) return this._accountInfo;
        var e = this;
        $.ajax({
            url : "/account",
            type : "GET",
            dataType : "json",
            async : false,  // 要同步，不让返回值为空
            success : function(res) {
                if (res.success == true) {
                    e._accountInfo = res.data;
                }
            }
        });
        return this._accountInfo;
    },

    /**
     * 获取用户信息
     * @returns {*}
     */
    getUser : function () {
        if (this._userInfo) return this._userInfo;
        var e = this;
        $.ajax({
            url : "/user",
            type : "GET",
            dataType : "json",
            async : false,  // 要同步，不让返回值为空
            success : function(res) {
                if (res.success == true) {
                    e._userInfo = res.data;
                }
            }
        });
        return this._userInfo;
    },

    /**
     * 获取用户的扩展信息
     * @returns {*}
     */
    getUserExt : function () {
        if (this._userExtInfo) return this._userExtInfo;
        var e = this;
        $.ajax({
            url : "/userextra",
            type : "GET",
            dataType : "json",
            async : false,  // 要同步，不让返回值为空
            success : function(res) {
                if (res.success == true) {
                    e._userExtInfo = res.data;
                }
            }
        });
        return this._userExtInfo;
    },

    /**
     * 获取未读消息数量
     * @returns {null}
     */
    getNotReadMsgNum : function () {
        if (this._notReadMsgNum) return this._notReadMsgNum;
        var e = this;
        $.ajax({
            url : "/content/notread_msg_num",
            type : "GET",
            dataType : "json",
            async : false,  // 要同步，不让返回值为空
            success : function(res) {
                if (res.success == true) {
                    e._notReadMsgNum = res.data;
                }
            }
        });
        return this._notReadMsgNum;
    },
    /**
     * 获取新用户注册的时间
     * @returns {null}
     */
    getNewRegisterDate : function () {
        if (this._newRegisterDate) return this._newRegisterDate;
        var e = this;
        $.ajax({
            url : "/other/data/o20150715",
            type : "GET",
            dataType : "json",
            async : false,  // 要同步，不让返回值为空
            success : function(res) {
                if (res.success == true) {
                    e._newRegisterDate = res.data;
                }
            }
        });
        return this._newRegisterDate;
    }
};