// @koala-prepend "./../lib/jquery-1.11.1.min.js"
// @koala-prepend "./../lib/jquery.uploadify.min.js"
// @koala-prepend "./../lib/formValidate.js"
// @koala-prepend "./../common/utils.js"
// @koala-prepend "./../common/ybk_init.js"
// @koala-prepend "./../v2/actionTip.js"
// @koala-prepend "./../ux/protocol.js"

$(document).ready(function() {


    var divOne = $('#openAccountDivOne'),
        divTwo = $('#openAccountDivTwo'),
        divThree = $('#openAccountDivThree'),
        divFour = $('#openAccountDivFour'),
        divSuccess = $('#openAccountDivSuccess'),
        formOne = $('#openAccountFormOne'),
        formTwo = $('#openAccountFormTwo'),
        formExchangeThree = $('#openAccountExchangeFormThree'),
        formBankThree = $('#openAccountBankFormThree'),
        formFour = $('#openAccountFormFour'),
        btnNextOne = $('#nextOpentAccountBtnOne'),
        btnPreTwo = $('#preOpenAccountBtnTwo'),
        btnNextTwo = $('#nextOpenAccountBtnTwo'),
        btnPreThree = $('#preOpenAccountBtnThree'),
        btnNextThree = $('#nextOpenAccountBtnThree'),
        btnPreFour = $('#preOpenAccountBtnFour'),
        btnNextFour = $('#nextOpenAccountBtnFour'),

        aSelectBank = $('.selectBankA'),
        aSelectExchange = $('.selectExchangeA'),

        // 上传图片的东东
        uploadIdCardAPic = $('#uploadIdCardAPic'),
        uploadIdCardBPic = $('#uploadIdCardBPic'),
        uploadBankPic = $('#uploadBankPic'),
        spanUploadIdCardAPic = $('#uploadIdCardAPicSpan'),
        spanUploadIdCardBPic = $('#uploadIdCardBPicSpan'),
        spanUploadBankPic = $('#uploadBankPicSpan'),
        spanUploadIdCardATip = $('#uploadIdCardATipSpan'),
        spanUploadIdCardBTip = $('#uploadIdCardBTipSpan'),
        spanUploadBankTip = $('#uploadBankTipSpan');

    var inputPhone = $('#phone'),
        inputPwd = '#password',
        inputAuthCode = '#authCode',
        inputName = $('#name'),
        inputIdCard = $('#idcard'),
        inputBankNoExchange = $('#bankNoExchange'),
        inputBankNoBank = $('#bankNoBank'),
        inputXieyi = $('#xieyi');

    var naviUl = $('#naviUl'),
        passwordDiv = '#passwordDiv',
        authCodeDiv = '#authCodeDiv',
        passwordHtml = $(passwordDiv).html(),
        authCodeHtml = $(authCodeDiv).html(),
        getAuthCode = '#getAuthCode',
        exchangeIdSelect = $('#exchangeIdSelect'),
        supportBankSelectList = $('#supportBankSelectList'),
        supportBankExchangeList = $('#supportBankExchangeList'),
        mobileExists = false,   // 手机号是否存在
        authExists = false, // 是否已经实名认证
        threeShowTab = 'exchange',// 第三步的是什么，默认是交易所
        periodSeconds = 0,
        allExchange = {},
        allSupportBank = {},
        newPwd = '',
        supportBankStr = '';

    // 提交表单依赖的变量
    var varPhone = '',
        varPwd = '',
        varAuthCode = '',
        varName = '',
        varIdCard = '',
        varBankType = '',
        varBankNo = '',
        varExchange = [],
        varIdAPic = '',
        varIdBPic = '',
        varBankPic = '',
        varToken = '';

    // 导航到第一步
    selectNavigation(1);

    // 表单验证初始化
    formValidate.init($(formOne));
    formValidate.init($(formTwo));
    formValidate.init($(formExchangeThree));
    formValidate.init($(formBankThree));
    formValidate.init($(formFour));

    // 先清空两个文本框
    $(passwordDiv).html('');
    $(authCodeDiv).html('');

    // 协议展示
    $('#showXieyiA').click(function() {
        Protocol.showYbkProtocol();
    });

    /**
     * 先获取些基本信息
     */
    // 获取所有交易所
    $.ajax({
        url : ybkUrl + '/api/ybk/query_exchange_list',
        data : {status : 2},
        type : 'GET',
        dataType : 'json',
        success : function(res) {
            if (res.success == true) {
                $.each(res.data, function(i, e) {
                    if (e.number != '') {
                        allExchange[e.id] = e;
                        supportBankStr += e.supportBank + ',';
                        $(exchangeIdSelect).append("<option value='"+ e.id +"'>"+ e.name +"</option>");
                    }
                });
                // 获取所有银行信息
                $.ajax({
                    url : ybkUrl + '/api/ybk/query_bank_list',
                    type : 'GET',
                    dataType : 'json',
                    success : function (res) {
                        if (res.success == true) {
                            var arrSupportBank = supportBankStr.split(",");
                            $.each(res.data, function(i, e) {
                                if (arrSupportBank.indexOf(e.bankNo) != -1) {
                                    allSupportBank[e.bankNo] = e;
                                    $(supportBankSelectList).append("<option value='"+ e.bankNo +"'>"+ e.bankName +"</option>");
                                }
                            });
                        } else {
                            // TODO 提示错误信息
                            alert(res.msg);
                        }
                    }
                });
            } else {
                // TODO 提示错误信息
                alert(res.msg);
            }
        }
    });


    /**
     * 根据页面上的变量，初始化一些东东
     */
    if (_token != '') {
        varToken = _token;
        // 有手机号，写入手机号，选中第几步，隐藏第一步，显示第二步
        varPhone = _mobile;
        $(inputPhone).val(varPhone).attr("readonly", true);
        selectNavigation(2);
        $(divOne).hide();
        $(divTwo).show();
        mobileExists = true;
        if (_name != '') {
            authExists = true;
            // 已经实名认证了，写入实名认证信息，显示第三步
            $(inputName).val(_name).attr("readonly", true);
            $(inputIdCard).val(_idcard).attr("readonly", true);
            selectNavigation(3);
            $(divTwo).hide();
            $(divThree).show();
            changeSelectBankOrExchange(threeShowTab);
        }
    }

    /**
     * 第一步操作的东东
     */
    // 手机号修改的事件，检查手机号是否注册
    $(inputPhone).change(function() {
        if (formValidate.validateInput(inputPhone) == false) {
            return false;
        }
        var mobile = $(this).val();
        var res = checkMobileExists(mobile);
        console.info(res);
        if (res == true) {
            // 手机号存在
            mobileExists = true;
            $(passwordDiv).show();$(authCodeDiv).hide();
            $(passwordDiv).html(passwordHtml);$(authCodeDiv).html('');
            $(inputAuthCode).val('');
        } else {
            // 手机号不存在
            mobileExists = false;
            $(passwordDiv).hide();$(authCodeDiv).show();
            $(passwordDiv).html('');$(authCodeDiv).html(authCodeHtml);
            $(inputPwd).val('');
        }
    });
    // 检查手机号是否已经存在了
    function checkMobileExists(phone) {
        var result = false;
        $.ajax({
            url : ybkUrl + "/api/user/mobile_exists",
            data : {mobile : phone},
            type : 'GET',
            dataType : "json",
            async : false,
            success : function(res) {
                if (res.success == true) {
                    result = res.data;
                } else {
                    formValidate.showTips(inputPhone, res.msg);
                }
            }
        });
        return result;
    }

    // 获取注册验证码
    $(divOne).on('click', getAuthCode, function() {
        if (formValidate.validateInput(inputPhone) == false) {
            return false;
        }
        if (periodSeconds > 0) return false;
        $.get(ybkUrl + '/api/user/registercode', {'mobile': $(inputPhone).val().trim()}, function (response) {
            if (response.success) {
                periodSeconds = 60;
                var disableInterval = window.setInterval(function () {
                    if (periodSeconds > 0) {
                        $(getAuthCode).html("(" + (--periodSeconds) + ")后重新发送");
                    } else {
                        window.clearInterval(disableInterval);
                        $(getAuthCode).html("获取验证码");
                    }
                }, 1000);
            } else {
                formValidate.showTips(inputAuthCode, response.msg);
            }
        });
    });

    /**
     * 第一步的下一步操作
     */
    $(btnNextOne).click(function() {
        if (formValidate.validateForm(formOne) == false) {
            return false;
        }
        varPhone = $(inputPhone).val();
        varPwd = $(inputPwd).val();
        varAuthCode = $(inputAuthCode).val();
        if (mobileExists == true && (varToken == '' || varToken == false)) {
            // 执行登陆操作
            varToken = login(varPhone, varPwd);
            // 获取用户信息
            if (varToken == false) {
                formValidate.showFormTips(btnNextOne, "用户名或密码错误");
                return false;
            }
            $.ajax({
                url : ybkUrl + '/api/user/get_user_info',
                data : {token : varToken},
                type : 'GET',
                dataType : "json",
                async : false,
                success : function(res) {
                    if (res.success == true) {
                        if (res.data.isTrust == 1) {
                            authExists = true;
                            varName = res.data.userName;
                            varIdCard = res.data.idcard;
                            $(inputName).val(varName).attr("readonly", true);
                            $(inputIdCard).val(varIdCard).attr("readonly", true);
                        } else {
                            authExists = false;
                        }
                    } else {
                        formValidate.showFormTips(btnNextOne, "获取用户信息错误");
                    }
                }
            });
            // 手机号不允许修改
            $(inputPhone).attr("readonly", true);
            $(passwordDiv).html('');
            $(authCodeDiv).html('');
        } else if (mobileExists == false) {
            // 待下一步执行注册操作
            varToken = '';
            // 手机号不允许修改
            $(inputPhone).attr("readonly", true);
            $(passwordDiv).html('');
        }
        $(divOne).hide();
        $(divTwo).show();
        selectNavigation(2);
    });

    // 第二步中的返回上一步操作
    $(btnPreTwo).click(function() {
        //$(passwordDiv).hide();
        //$(authCodeDiv).hide();
        //$(inputPwd).val('');
        //$(inputAuthCode).val('');
        $(divTwo).hide();
        $(divOne).show();
        selectNavigation(1);
    });


    /**
     * 第二步中的操作
     */
    // 第二步中的下一步(根据交易所来)操作
    $(btnNextTwo).click(function() {
        if (formValidate.validateForm(formTwo) == false) {
            return false;
        }
        var result = setpTwoOper();
        if (result == false) {
            return false;
        }
        $(divTwo).hide();
        $(divThree).show();
        changeSelectBankOrExchange(threeShowTab);
        selectNavigation(3);
    });


    // 第二步公共的用户操作
    function setpTwoOper() {
        varName = $(inputName).val();
        varIdCard = $(inputIdCard).val();
        var result = true;
        if (mobileExists == false) {
            //// 手机号不存，进行注册
            //if (checkMobileExists(varPhone) == false) {
            //    var newPwd = varIdCard.substr(-6);
            //    var registerRes = register(varPhone, newPwd, varAuthCode);
            //    if (registerRes == false) {
            //        formValidate.showTips(inputIdCard, "注册用户错误");
            //        result = false;
            //    }
            //}
            //if (varToken == '' || varToken == false) {
            //    varToken = login(varPhone, newPwd);
            //    if (varToken == false) {
            //        formValidate.showTips(inputIdCard, "自动登录失败");
            //        result = false;
            //    }
            //}
            // 注册、登陆、实名 三合一
            var newPwd = varIdCard.substr(-6);
            varToken = registerLoginAuth(varPhone, newPwd, varAuthCode, varName, varIdCard);
            if (varToken == false) {
                formValidate.showTips(inputIdCard, "注册登录失败，请刷新重试");
                return false;
            } else {
                //$('#registerOK').css("display", "block");
                document.getElementById("registerOK").style.display = "block";
            }
            authExists = true;
            mobileExists = true;
        }
        if (authExists == false) {
            // 用户未进行实名认证
            $.ajax({
                url : ybkUrl + '/api/user/user_auth',
                data : {token : varToken, real_name : varName, idcard_no : varIdCard},
                type : 'POST',
                dataType : 'json',
                async : false,
                success : function(res) {
                    if (res.success == true) {
                        authExists = true;
                    } else {
                        formValidate.showTips(inputIdCard, res.msg);
                        result = false;
                    }
                }
            });
        }
        return result;
    }

    /**
     * 第三步操作
     */
    $(btnNextThree).click(function() {
        if ($(inputXieyi).prop("checked") == false) {
            formValidate.showFormTips(btnNextThree, "请阅读并同意委托开户协议");
            return false;
        }
        if (threeShowTab == 'exchange') {
            if (formValidate.validateForm(formExchangeThree) == false) {
                return false;
            }
            varBankNo = $(inputBankNoExchange).val();
        } else {
            if (formValidate.validateForm(formBankThree) == false) {
                return false;
            }
            varBankNo = $(inputBankNoBank).val();
        }
        if (varBankType == '') {
            formValidate.showFormTips(btnNextThree, "请选择银行");
            return false;
        }
        if (varExchange.length <= 0) {
            formValidate.showFormTips(btnNextThree, "请选择要开户的交易所");
            return false;
        }
        $(divThree).hide();
        bindUploadPic();
        $(divFour).show();
        selectNavigation(4);
    });
    // 第三步返回的东东
    $(btnPreThree).click(function() {
        $(divThree).hide();
        $(divTwo).show();
    });
    // 选择以什么方式进行开户的东东
    $(aSelectBank).click(function() {
        changeSelectBankOrExchange('bank');
    });
    $(aSelectExchange).click(function() {
        changeSelectBankOrExchange('exchange');
    });
    // 选择是交易所或者还是银行来选择开户
    function changeSelectBankOrExchange(id) {
        $(aSelectBank).removeClass("cur");
        $(aSelectExchange).removeClass("cur");
        if (id == 'exchange') {
            $(aSelectExchange).addClass("cur");
            $(formBankThree).hide();
            $(formExchangeThree).show();
            $(inputBankNoExchange).val('');
            $(exchangeIdSelect).trigger("change");
            threeShowTab = id;
        } else {
            $(aSelectBank).addClass("cur");
            $(formBankThree).show();
            $(formExchangeThree).hide();
            $(inputBankNoBank).val('');
            $(supportBankSelectList).trigger("change");
            threeShowTab = id;
        }
    }


    // 选择完交易所，下边的支持银行跟着变
    $(exchangeIdSelect).change(function() {
        if ($(this).val() == '') return false;
        varBankType = '';
        var exchangeId = $(this).val();
        var exchangeInfo = allExchange[exchangeId];
        var suppBanks = exchangeInfo.supportBank.split(',');
        $(supportBankExchangeList).html('');
        for (var i = 0; i < suppBanks.length; i++) {
            var bankInfo = allSupportBank[suppBanks[i]];
            $(supportBankExchangeList).append("<li class='selectBankLi' data-bankNo='"+bankInfo.bankNo+"'><img src='/static/ybk/img/bank/"+bankInfo.bankNo +".jpg'  alt='"+ bankInfo.bankName +"'/></li>");
        }
        $('.supportExchangeList').html('');
        varExchange = [];
        varExchange.push(parseInt(exchangeId));
    });
    // 支持的银行点击效果
    $(formExchangeThree).on("click", '.selectBankLi', function() {
        $(".selectBankLi").removeClass("cur");
        $(this).addClass("cur");
        varBankType = $(this).attr('data-bankNo');
        var supportExchange = getSupportBankExchangeList(varBankType);
        $('.supportExchangeList').html('');
        $.each(supportExchange, function(i, e){
            $('.supportExchangeList').append("<label><input type='checkbox' checked='checked' value='"+ e.id +"'><span></span>"+ e.name +"</label>");
            if (varExchange.indexOf(e.id) == -1) varExchange.push(e.id);
        });
    });

    // 选择的银行变化
    $(supportBankSelectList).change(function() {
        if ($(this).val() == '') return false;
        varExchange = [];
        varBankType = $(this).val();
        var supportExchange = getSupportBankExchangeList(varBankType);
        $('.supportExchangeList').html('');
        $.each(supportExchange, function(i, e){
            $('.supportExchangeList').append("<label><input type='checkbox' checked='checked' value='"+ e.id +"'><span></span>"+ e.name +"</label>");
            if (varExchange.indexOf(e.id) == -1) varExchange.push(e.id);
        });
    });

    // 下边支持该银行的交易所变化
    $('.supportExchangeList,.supportExchangeBankList').on("click", 'input', function() {
        var exchangeId = parseInt($(this).val());
        if ($(this).prop("checked") == true) {
            varExchange.push(exchangeId);
        } else {
            var tmpExchangeId = [];
            for (var i = 0,j = varExchange.length; i < j; i++) {
                var id = varExchange.pop();
                if (id != exchangeId) tmpExchangeId.push(id);
            }
            varExchange = tmpExchangeId;
        }
    });

    /**
     * 第四步操作
     */
    $(btnNextFour).click(function() {
        // 参数验证
        if (varToken == "" || varToken == false) {
            formValidate.showFormTips(btnNextFour, "请刷新页面重新开始");
            return false;
        }
        if (varExchange.length == 0) {
            formValidate.showFormTips(btnNextFour, "请返回上一步选择开户交易所");
            return false;
        }
        if (varIdAPic == "") {
            formValidate.showFormTips(btnNextFour, "请上传身份证正面照片");
            return false;
        }
        if (varIdBPic == "") {
            formValidate.showFormTips(btnNextFour, "请上传身份证反面照片");
            return false;
        }
        if (varBankPic == "") {
            formValidate.showFormTips(btnNextFour, "请上传银行卡正面照片");
            return false;
        }
        var exchangeIds = '';
        for (var i = 0; i < varExchange.length; i++) {
            exchangeIds += varExchange[i] + ',';
        }
        $.ajax({
            url : ybkUrl + '/api/ybk/register',
            data : {token : varToken, bank_code : varBankType, bank_num : varBankNo, bank_picture : varBankPic, card_positive : varIdAPic, card_opposite : varIdBPic, exchange_id : exchangeIds},
            type : 'POST',
            dataType : 'json',
            success : function(res) {
                if (res.success == true) {
                    $('.layout_wrap').css("display", 'block');
                } else {
                    formValidate.showFormTips(btnNextFour, "申请开户请求失败");
                }
            }
        });
    });
    // 第三步返回
    $(btnPreFour).click(function() {
        $(divFour).hide();
        $(divThree).show();
    });

    /**
     * 上传图片的
     */
    function bindUploadPic() {
        // 身份证正面照片
        $(uploadIdCardAPic).uploadify({
            buttonText : "点击上传",
            buttonCursor : 'hand',
            formData : {token : varToken},
            fileObjName : 'file',
            fileSizeLimit : '5120KB',
            fileTypeExts : '*.gif; *.jpg; *.png;*.jpeg',
            method : 'post',
            multi : true,
            queueSizeLimit : 999,
            swf : '/static/ybk/prod/js/uploadify.swf',
            uploader : ybkUrl + '/api/ybk/uploadIMG',
            onUploadSuccess : function(file, data, response) {
                var res = $.parseJSON(data);
                if (res.success == true) {
                    varIdAPic = res.data;
                    $(spanUploadIdCardAPic).html("<img src='"+varIdAPic+"' />").show();
                    $(spanUploadIdCardATip).hide();
                } else {
                    alert("图片上传失败");
                    $(spanUploadIdCardAPic).html("").hide();
                    $(spanUploadIdCardATip).show();
                }
            },
            onUploadError : function(file, errorCode, errorMsg, errorString) {
                alert("上传失败请确认图片格式是否正确");
            }
        });
        // 身份证反面照片
        $(uploadIdCardBPic).uploadify({
            buttonText : "点击上传",
            buttonCursor : 'hand',
            formData : {token : varToken},
            fileObjName : 'file',
            fileSizeLimit : '5120KB',
            fileTypeExts : '*.gif; *.jpg; *.png;*.jpeg',
            method : 'post',
            multi : true,
            queueSizeLimit : 999,
            swf : '/static/ybk/prod/js/uploadify.swf',
            uploader : ybkUrl + '/api/ybk/uploadIMG',
            onUploadSuccess : function(file, data, response) {
                var res = $.parseJSON(data);
                if (res.success == true) {
                    varIdBPic = res.data;
                    $(spanUploadIdCardBPic).html("<img src='"+varIdBPic+"' />").show();
                    $(spanUploadIdCardBTip).hide();
                } else {
                    alert("图片上传失败");
                    $(spanUploadIdCardBPic).html("").hide();
                    $(spanUploadIdCardBTip).show();
                }
            },
            onUploadError : function(file, errorCode, errorMsg, errorString) {
                alert("上传失败请确认图片格式是否正确");
            }
        });
        // 身份证反面照片
        $(uploadBankPic).uploadify({
            buttonText : "点击上传",
            buttonCursor : 'hand',
            formData : {token : varToken},
            fileObjName : 'file',
            fileSizeLimit : '5120KB',
            fileTypeExts : '*.gif; *.jpg; *.png;*.jpeg',
            method : 'post',
            multi : true,
            queueSizeLimit : 999,
            swf : '/static/ybk/prod/js/uploadify.swf',
            uploader : ybkUrl + '/api/ybk/uploadIMG',
            onUploadSuccess : function(file, data, response) {
                var res = $.parseJSON(data);
                if (res.success == true) {
                    varBankPic = res.data;
                    $(spanUploadBankPic).html("<img src='"+varBankPic+"' />").show();
                    $(spanUploadBankTip).hide();
                } else {
                    alert("图片上传失败");
                    $(spanUploadBankPic).html("").hide();
                    $(spanUploadBankTip).show();
                }
            },
            onUploadError : function(file, errorCode, errorMsg, errorString) {
                alert("上传失败请确认图片格式是否正确");
            }
        });
    }


    /**
     * 根据银行卡类型获取支持的交易所列表
     * @param bankType
     * @returns {Array}
     */
    function getSupportBankExchangeList(bankType) {
        var supportExchangeList = [];
        $.ajax({
            url : ybkUrl + '/api/ybk/query_exchange_by_bankno',
            data : {bank_no : bankType},
            type : 'GET',
            dataType : 'json',
            async : false,
            success : function(res) {
                if (res.success == true) {
                    $.each(res.data, function(i, e) {
                    if (e.number != '') supportExchangeList.push(e);
                    });
                }
            }
        });
        return supportExchangeList;
    }


    /**
     * 登录操作
     * @param phone
     * @param pwd
     * @returns {boolean}
     */
    function login(phone, pwd) {
        var token = false;
        $.ajax({
            url : ybkUrl + '/api/user/login',
            data : {mobile : phone, login_pwd : pwd},
            type : 'GET',
            dataType : 'json',
            async : false,
            success : function(res) {
                if (res.success == true) {
                    token = res.data;
                } else {
                    // 错误提示
                    token = false;
                }
            }
        });
        return token;
    }

    /**
     * 注册操作
     * @param phone
     * @param pwd
     * @param authCode
     * @returns {boolean}
     */
    function register(phone, pwd, authCode) {
        var result = false;
        var postData = {mobile : phone, login_pwd : pwd, sms_code : authCode};
        if (_refCaimaoId != '') {
            postData['ref_caimao_id'] = _refCaimaoId;
        }
        $.ajax({
            url : ybkUrl + '/api/user/register',
            data : postData,
            type : 'POST',
            dataType : 'json',
            async : false,
            success : function(res) {
                result = res.success == true;
            }
        });
        return result;
    }

    /**
     * 登陆注册实名认证三合一接口
     * @param phone
     * @param pwd
     * @param authCode
     * @param name
     * @param cardId
     * @returns {boolean}
     */
    function registerLoginAuth(phone, pwd, authCode, name, cardId) {
        var token = false;
        var postData = {mobile : phone, login_pwd : pwd, sms_code : authCode, real_name : name, idcard_no : cardId};
        if (_refCaimaoId != '') {
            postData['ref_caimao_id'] = _refCaimaoId;
        }
        $.ajax({
            url : ybkUrl + '/api/user/registerLoginAuth',
            data : postData,
            type : 'POST',
            dataType : 'json',
            async : false,
            success : function(res) {
                if (res.success == true) {
                    token = res.data;
                } else {
                    // 错误提示
                    token = false;
                }
            }
        });
        return token;
    }

    /**
     * 选择当前是在第几步
     * @param step
     */
    function selectNavigation(step) {
        $(naviUl).find("li").removeClass("cur");
        $(naviUl).find("li:eq("+(step - 1)+")").addClass("cur");
    }

});




