define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'dojo/dom-construct',
    'dojo/_base/config',
    'dojo/text!./templates/BankcardBindedPanel.html'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, query, domConstruct, Config, template) {
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {

        templateString: template,

        baseUrl: Global.baseUrl,
        // attach point param
        bankcardImg: '',
        bankcardName: '',
        cardholder: '',
        bankcardNo: '',
        bankcardBindStatus: '',
        tradePwd: '',
        statusMap: {'0': '正在绑定', '1': '已验证', '2': '验证未通过', '9': '锁定'},
        bankcard: '',

        _setBankcardAttr: function (bankcard) {
            query('img', this.bankcardImgNode)[0].src = Config.baseUrl + 'app/resources/image/bosheng/borkers-logo/small-bank-' + bankcard.bankNo + '.png';
            this.bankcardNameNode.innerHTML = bankcard.bankName;
            this.cardholderNode.innerHTML = bankcard.bankCardName;
            this.bankcardNoNode.innerHTML = bankcard.bankCardNo;
            if (bankcard.province != null && bankcard.city != null && bankcard.openBank != null) {
                this.openBankNode.innerHTML = bankcard.province + " " + bankcard.city + " " + bankcard.openBank;
            }
        },

        _setBankcardImgAttr: function (value) {
            this._set('bankcardImg', value);
            query('img', this.bankcardImgNode)[0].src = Config.baseUrl + 'app/resources/image/bosheng/borkers-logo/small-bank-' + value + '.png';
        },

        _setBankcardNameAttr: function (value) {
            this._set('bankcardName', value);
            this.bankcardNameNode.innerHTML = value;
        },

        _setCardholderAttr: function (value) {
            this._set('cardholder', value);
            this.cardholderNode.innerHTML = value;
        },

        _setBankcardNoAttr: function (value) {
            this._set('bankcardNo', value);
            this.bankcardNoNode.innerHTML = value;
        },

        _setBankcardBindStatusAttr: function (value) {
            this._set('bankcardBindStatus', value);
            this.bankcardBindStatusNode.innerHTML = this.statusMap[value];
            if (value == '0') {
                domConstruct.empty(this.bankupdateNode);
            }
        },

        _setTradePwdAttr: function (value) {
            this._set('tradePwd', value);
            this.tradePwdNode.innerHTML = ''; //TODO(parseInt(value) ? '修改': '设置') + '安全密码';
        },

        setValues: function (values) {
            for (var key in values) {
                this.set(key, values[key]);
            }
        },

        postCreate: function () {
            var me = this;
            me.inherited(arguments);
        }

    });
});