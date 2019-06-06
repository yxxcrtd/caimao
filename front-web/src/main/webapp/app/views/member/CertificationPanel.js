define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/ux/GenericTextBox',
    'app/ux/GenericDisplayBox',
    'app/ux/GenericButton',
    'app/ux/GenericTooltip',
    'app/common/Date',
    'dojo/text!./templates/CertificationPanel.html'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, TextBox, DisplayBox, Button, Tooltip, DateUtil, template) {
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {

        templateString: template,

        onConfirm: function () {
        },

        render: function () {
            var me = this,
                realName = me.nameFld = new TextBox({
                    label: '真实姓名',
                    leftOffset: 330,
                    validates: [
                        {
                            pattern: function() {
                                return this.get('value').length <= 20;
                            },
                            message: '真实姓名长度不能超过20'
                        }, {
                            pattern: /.+/,
                            message: '请输入真实姓名'
                        }]
                }),
                IDNo = me.IDNoFld = new TextBox({
                    label: '身份证号码',
                    leftOffset: 330,
                    validates: [{
                        pattern: /.+/,
                        message: '请输入身份证号码'
                    }, {
                        pattern: /^(([0-9]{17}[0-9X]{1})|([0-9]{15}))$/,
                        message: '身份证号码要求15位或18位数字，18位末位可以为X'
                    }, {
                        pattern: function () {
                            var _id = me.IDNoFld.get('value');
                            var powers = new Array("7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2");
                            var parityBit = new Array("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2");
                            if (_id.length == 15) {
                                //TODO
                                return true;
                            } else if (_id.length == 18) {
                                _id = _id + '';
                                var _num = _id.substr(0, 17);
                                var _parityBit = _id.substr(17);
                                var _power = 0;
                                for (var i = 0; i < 17; i++) {
                                    _power += parseInt(_num.charAt(i)) * parseInt(powers[i]);
                                }
                                var mod = parseInt(_power) % 11;
                                if (parityBit[mod] == _parityBit) {
                                    return true;
                                }
                                return false;
                            }
                        },
                        message: '身份证号码有误,请检查'
                    }, {
                        pattern: function () {
                            var value = this.get('value'),
                                dateStr = value.slice(6, 10) + '-' + value.slice(10, 12) + '-' + value.slice(12, 14);
                            return DateUtil.parse(dateStr) ? true : false;
                        },
                        message: '身份证号码有误,请检查'
                    }, {
                        pattern: function () {
                            var value = this.get('value'),
                                dateStr = value.slice(6, 10) + '-' + value.slice(10, 12) + '-' + value.slice(12, 14);
                            var year18 = DateUtil.format(DateUtil.add(DateUtil.parse(dateStr), 'year', 18));
                            return year18 <= DateUtil.format(new Date()) ? true : false;
                        },
                        message: '未满18周岁不能实名认证'
                    }]
                }),
                confirmBtn = me.confirmBtn = new Button({
                    label: '下一步',
                    enter: true,
                    position: 'center',
                    handler: me.onConfirm
                });

            realName.placeAt(me.formNode);
            IDNo.placeAt(me.formNode);
            confirmBtn.placeAt(me.formNode);
        },

        isValid: function () {
            return this.nameFld.checkValidity() &&
                this.IDNoFld.checkValidity();
        },

        getData: function () {
            return {
                'real_name': this.nameFld.get('value'),
                'idcard': this.IDNoFld.get('value')
            };
        },

        showError: function (err) {
            Tooltip.show(err.info, this.confirmBtn.domNode);
        },

        postCreate: function () {
            var me = this;
            me.render();
            me.inherited(arguments);
        }

    });
});