/**
 * Created by xavier on 15/7/9.
 */


formValidate = {
    // 表单的对象
    formObj : null,

    /**
     * 写好的各个验证类型的, 标签中的属性名：validateType, 多个使用空格隔开，参数使用 : 分割
     * 例如： <input id="##" validateType="empty:这个值不能为空 phone" value=""/>
     */
    validateTypes : {
        // 不能为空  参数1 提示消息
        empty : function (value, args) {
            var res = {"success" : true};
            var message = typeof args[1] == "undefined" ? "内容不能为空" : args[1];
            if (value == "") {
                res.success = false;
                res.message = message;
            }
            return res;
        },
        // 手机号格式 无参数
        phone : function(value, args) {
            var res = {"success" : true};
            if (value != '') {
                if ( ! /^1[0-9]{10}$/.test(value)) {
                    res.success = false;
                    res.message = "请输入正确的手机号";
                }
            }
            return res;
        },
        // 邮箱格式 无参数
        email : function (value, args) {
            var res = {"success" : true};
            if (value != '') {
                if ( ! /\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*/.test(value)) {
                    res.success = false;
                    res.message = "请输入正确的邮箱";
                }
            }
            return res;
        },
        // 密码格式 无参数
        password : function(value, args) {
            var res = {"success" : true};
            if (value != '') {
                if (! /^[\w\W]{6,16}$/.test(value)) {
                    res.success = false;
                    res.message = "请输入正确的密码格式";
                }
            }
            return res;
        },
        // 金额验证 无参数
        money : function(value, args) {
            var res = {"success" : true};
            value = CMUTILS.removeCommas(value);
            if (value != '') {
                if (! /^([1-9]{1}[0-9]*|0{1})\.?[0-9]{0,2}$/.test(value)) {
                    res.success = false;
                    res.message = "请输入正确的金额";
                }
            }
            return res;
        },
        // 昵称验证 无参数
        nickname : function(value, args) {
            var res = {"success" : true};
            if (value != '') {
                if (! /^([\u4E00-\u9FA5]|\w)*$/.test(value)) {
                    res.success = false;
                    res.message = "请输入正确的昵称";
                }
            }
            return res;
        },

        // 长度限制 参数1 最小长度 参数2 最大长度
        len : function (value, args) {
            var res = {"success" : true};
            if (value.length < args[1]) {
                res.success = false;
                res.message = "长度不能小于"+args[1];
            }
            if (value.length > args[2]) {
                res.success = false;
                res.message = "长度不能超过"+args[2];
            }
            return res;
        },
        // 数值不能小于 参数1 最小值
        min : function (value, args) {
            var res = {"success" : true};
            value = CMUTILS.removeCommas(value);
            if (eval(value) < eval(args[1])) {
                res.success = false;
                res.message = "不能小于"+args[1];
            }
            return res;
        },
        // 数值不能大于 参数1 最大值
        max : function (value, args) {
            var res = {"success" : true};
            value = CMUTILS.removeCommas(value);
            if (eval(value) > eval(args[1])) {
                res.success = false;
                res.message = "不能大于"+args[1];
            }
            return res;
        },
        // 必须是整形数据 无参数
        interge : function (value, args) {
            var res = {"success" : true};
            if (!/^[0-9]+$/.test(value)) {
                res.success = false;
                res.message = "必须是整形数据";
            }
            return res;
        },
        // 必须是数值类型  无参数
        number : function (value, args) {
            var res = {"success" : true};
            if ( ! isNumber(value)) {
                res.success = false;
                res.message = "请填写正确的数值";
            }
            return res;
        },
        // 图片验证码格式是否正确 无参数
        captcha : function (value, args) {
            var res = {"success" : true};
            if ( ! /^[0-9A-Za-z]{4}$/.test(value)) {
                res.success = false;
                res.message = "请输入正确的验证码";
            }
            return res;
        },
        // 手机号码的验证 无参数
        authcode : function (value, args) {
            var res = {"success" : true};
            if ( ! /^[0-9]{6}$/.test(value)) {
                res.success = false;
                res.message = "请输入正确的验证码";
            }
            return res;
        },
        // 是否与其他字段的值相同  参数1 字段ID  参数2 错误信息
        same : function (value, args) {
            var res = {"success" : true};
            if (value != $(args[1]).val()) {
                res.success = false;
                res.message = args[2];
            }
            return res;
        }
    },


    /**
     * 初始化绑定表单
     * @param fromObj 表单的对象
     * @param event 验证的事件
     * @returns {formValidate}
     */
    init : function(fromObj, event) {
        this.formObj = fromObj;
        var eObj = this;
        if (typeof event == "undefined") event = "change";
        $(fromObj).on(event, "input,select", function() {
            eObj.validateInput(this);
        });
        return this;
    },
    /**
     * 验证表单方法
     * @param obj   一个页面有多个表单，需要传递验证的表单的对象
     * @returns {boolean}
     */
    validateForm : function(obj) {
        var success = true, eObj = this;
        obj = typeof obj == "undefined" ? this.formObj : obj;
        // 循环其中的input框，进行校验
        $(obj).find("input,select").each(function(n, obj) {
            success = eObj.validateInput(obj) == false ? false : success;
        });
        return success;
    },
    /**
     * 验证控件的方法
     * @param input 控件的ID 或者 控件对象
     * @returns {boolean}
     */
    validateInput : function(input) {
        if (typeof input == "string") input = $('#'+input);
        var value = $(input).val(), res = {"success" : true};
        var validateTypes = $(input).attr("validateType") != undefined ? $(input).attr("validateType").split(" ") : false;
        if (validateTypes !== false) {
            for (var i = 0, j = validateTypes.length; i < j; i++) {
                var type = validateTypes[i];
                var args = type.split(":");
                //console.info("验证的类型： " + args[0] + " 验证的值：" + value);
                if (typeof this.validateTypes[args[0]] == "undefined") {
                    console.error("未定义 " + args[0] + " 的验证方法");
                    continue;
                }
                res = this.validateTypes[args[0]](value, args);
                if (res.success == false) break;
            }
        }
        if (res.success == false) {
            // 显示错误信息
            this.showTips(input, res.message);
        }
        return res.success;
    },
    /**
     * 显示提示信息
     * @param obj   控件的对象
     * @param message   错误的提示信息
     */
    showTips : function(obj, message) {
        buildDom('#'+$(obj).attr('id'));
        document.querySelector('#'+$(obj).attr('id')).bottom().error(message, 3000);
    },

    /**
     * 显示某个ID的提示，在上方
     * @param id
     * @param message
     */
    showFormTips : function (id, message) {
        id = typeof id == 'string' ? id : $(id).attr('id');
        buildDom('#'+id);
        document.querySelector('#'+id).top().error(message, 3000);
    }
};