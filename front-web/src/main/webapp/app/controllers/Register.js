/**
 * Controller for login, handler with all business event
 * */
define([
	'app/controllers/Helper',
	'dojo/_base/fx', 
	'dijit/registry',
	'dojo/dom', 
	'app/views/member/PhoneCertificationBox',
	'dojo/on',
	'dojo/mouse',
	'dojo/keys',
	'dojo/request',
	'dojo/json',
	'app/common/Ajax',
	'dojo/query',
	'dojo/dom-class',
	'dojo/dom-construct',
	'dojo/dom-style',
	'app/common/Global',
	'dojo/fx/Toggler',
	'dojo/fx',
	'dijit/focus',
	'app/common/RSA',
	'app/ux/GenericButton',
    'dojo/cookie',
    'app/ux/GenericWindow',
    'dojo/when',
    'app/common/Data',
	'dojo/domReady!'
], function(Helper, fx, registry, dom, PhoneCertificationBox,
		on, mouse, keys, request, json, Ajax, query, domClass, domConstruct,
        domStyle, Global, Toggler, coreFx, focusUtil, RSA, Button, cookie, Win, when, Data) {
	
	var config = {'login': false};
	var errors = {},
		mobileRegex = /^1[34578]\d{9}$/,
		mobileErrorMsg = '请填写您正确的手机号',
		passwordConfirmErrorMsg = '两次密码输入不一致',
		formItemInputs = query('.ui-form .ui-input'),
		captchaImgEl = dom.byId('captchaimg'),
		phoneNumberEl = dom.byId('phonenumber'),
        refPhoneEl = dom.byId('ref_phone'),
		protocolLink = dom.byId('showtext2'),
		protocol = dom.byId('contentid2'),
		protocolToggler = dom.byId('protocoltoggler'),
		passwordEl = dom.byId('password'),
		passwordConfirmEl = dom.byId('passwordconfirm'),
		regBtn,
		captchaEl = dom.byId('captcha'),
		key, cerBox, win;
	
	function initData() {
	} 
	
	function initView() {
		regBtn = new Button({
			id: 'registerbutton',
			'label': '立即注册',
			enter: true,
            disabledMsg: '勾选融资协议即可注册',
            color: '#d72a26',
            hoverColor: '#ca2e35',
            width: 250,
            height: 40,
			style: {
				marginLeft: '100px'
			}
		});
		regBtn.placeAt('registerform');
		regBtn.set('disabled', !protocolToggler.checked);
	}
	
	function initRSA() {
		Ajax.get(Global.baseUrl + '/sec/rsa', {
		}).then(function(response) {
			if (response.success) {
				key = RSA.getKeyPair(response.data.exponent, '', response.data.modulus);
			} else {
				//TODO
			}
			
		});
	}
	
	function validatePhone(value) {
		// firstly check the phone format 
		value = value || phoneNumberEl.value;
		var result;
		if (!mobileRegex.test(value)) {
			errors.phonenumber = mobileErrorMsg;
			showError({'phonenumber': mobileErrorMsg});
			return false;
		}
		// secondly check if has registered
		Ajax.post(Global.baseUrl + '/user/mobile/check', {
			mobile: value
		}, true).then(function(response) {
			if (response.success) {
				showError({'phonenumber': false});
				result = true;
			} else {
				errors.phonenumber = response.msg;
				showError({'phonenumber': errors.phonenumber});
				result = false;
			}
		});
		return result;
	}

    function validateRefPhone(value) {
        // firstly check the phone format
        value = value || refPhoneEl.value;
        if (value == '') return true;
        var result;
        if (!mobileRegex.test(value)) {
            errors.ref_phone = mobileErrorMsg;
            showError({'ref_phone': mobileErrorMsg});
            return false;
        }
        // secondly check if has registered
        Ajax.post(Global.baseUrl + '/user/mobile/check', {
            mobile: value
        }, true).then(function(response) {
            if (response.success) {
                errors.ref_phone = "邀请人手机号未找到";
                showError({'ref_phone': errors.ref_phone});
                result = false;
            } else {
                showError({'ref_phone': false});
                result = true;
            }
        });
        return result;
    }
	
	function validatePassword(value) {
		value = value || passwordEl.value;
		var lengthReg = /^.{6,16}$/, // 6-16 length
			patternReg = /^[A-Za-z0-9\s!"#$%&'()*+,-.\/:;<=>?@\[\\\]^_`{|}~]+$/;
		if (!lengthReg.test(value)) {
			showError({'password': '密码长度在6-16个字符'});
			return;
		}
		
		if (!patternReg.test(value)) {
			showError({'password': '密码格式错误'});
			return;
		}
		showError({'password': false});
		return true;
	}
	
	function validatePasswordConfirm() {
		if (passwordEl.value !== passwordConfirmEl.value) {
			errors.passwordconfirm = passwordConfirmErrorMsg;
			showError({'passwordconfirm': passwordConfirmErrorMsg});
			return false;
		} else {
			delete errors.passwordconfirm;
			showError({'passwordconfirm': false});
			return true;
		}
	}
	
	function validateCaptcha(value) {
		value = value || captchaEl.value;
		var result = true;
		if (!value) {
			showError({'captcha': '请输入验证码'});
			result = false;
		}
		Ajax.post(Global.baseUrl + '/captcha/check', {
			'captcha': value	
		}, true).then(function(response) {
			if (response.success) {
				showError({'captcha': false});
				result = true;
			} else {
				showError({'captcha': '验证码不正确'});
				result = false;
			}
		});
		return result;
	}
	
	function removeError(id) {
		var el = dom.byId(id);
		domClass.remove(el, 'ui-input-error');
		errorEl = query('.ui-form-explain', el.parentNode);
		if (errorEl && errorEl.length > 0) {
			domConstruct.destroy(errorEl[0]);
		}
	}
	
	function addError(id, msg) {
		if (msg) {
			domClass.add(dom.byId(id), 'ui-input-error');
			errorEl = domConstruct.toDom('<div class="ui-form-explain"><i class="ui-icon icon-small-error"></i><span class="font-color-red">'+msg+'</span></div>');
			domConstruct.place(errorEl, dom.byId(id).parentNode);
		} else {
			errorEl = domConstruct.toDom('<div class="ui-form-explain"><i class="ui-icon icon-small-success"></i></div>');
			domConstruct.place(errorEl, dom.byId(id).parentNode);
		}
	}
	
	function showError(errs) {
		if (errs && typeof errs === 'object') {
			for (el in errs) {
				removeError(el);
				addError(el, errs[el]);
			}
		}
	}
	
	function switchPasswordLevel(level) {
		var levelEl = query('.ui-prompt-grade p')[0];
		switch(level) {
		case 0:
			domClass.remove(levelEl, 'genericProgressBarBad');
			domClass.remove(levelEl, 'genericProgressBarGood');
			domClass.remove(levelEl, 'genericProgressBarGreat');
            domStyle.set(levelEl, {
                width: '0%'
            });
			break;
		case 1:
			domClass.add(levelEl, 'genericProgressBarBad');
			domClass.remove(levelEl, 'genericProgressBarGood');
			domClass.remove(levelEl, 'genericProgressBarGreat');
            domStyle.set(levelEl, {
                width: '33%'
            });
			break;
		case 2:
			domClass.add(levelEl, 'genericProgressBarGood');
			domClass.remove(levelEl, 'genericProgressBarBad');
			domClass.remove(levelEl, 'genericProgressBarGreat');
            domStyle.set(levelEl, {
                width: '66%'
            });
			break;
		case 3:
			domClass.add(levelEl, 'genericProgressBarGreat');
			domClass.remove(levelEl, 'genericProgressBarBad');
			domClass.remove(levelEl, 'genericProgressBarGood');
            domStyle.set(levelEl, {
                width: '100%'
            });
			break;
		}
	}
	
	function isValid() {
		var valid = true;
		valid = validatePhone() && valid;
		valid = validatePassword() && valid;
		valid = validatePasswordConfirm() && valid;
        valid = validateRefPhone() && valid;
		valid = validateCaptcha() && valid;
		return valid;
	}
	
	function getValues() {
		var values = {};
		values.user_loginname = phoneNumberEl.value;
		values.user_loginpwd = RSA.encryptedString(key, passwordEl.value);
		values.captcha = captchaEl.value;
		values.company_id = 1;
		values.business_type = 1;
		return values;
	}
	
	function refreshCaptcha() {
		captchaImgEl.src = Global.baseUrl + '/captcha?_=' + new Date().valueOf();
	}
	
	function popCerWin() {
		if (cerBox) {
			cerBox.set('phone', Global.encodeInfo(phoneNumberEl.value.toString(), 3, 4));
			cerBox.show();
			return;
		}
		cerBox = new PhoneCertificationBox({
			phone: Global.encodeInfo(phoneNumberEl.value.toString(), 3, 4),
            width: 450,
			onConfirm: function() {
				if (cerBox.isValid()) {
                    var loginName = phoneNumberEl.value,
                        loginPwd = RSA.encryptedString(key, passwordEl.value);
					Ajax.post(Global.baseUrl + '/user/regist', {
						sms_code: cerBox.getCode(),
						login_name: phoneNumberEl.value,
						login_pwd: RSA.encryptedString(key, passwordEl.value),
                        ref_phone: refPhoneEl.value,
						u: Global.getUrlParam('u') || ''
					}).then(function(response) {
						if (response.success) {
                            Ajax.post(Global.baseUrl + '/user/login', {
                                'login_name': loginName,
                                'login_pwd': loginPwd
                            }).then(function(response) {
                                if(response.success){
                                    location.href = Global.baseUrl + '/user/register1.htm';
                                }else{
                                }
                            });
						} else {
							cerBox.showError(response.msg);
						}
					});
				}
			},
			
			fetchCode: function() {
				Ajax.post(Global.baseUrl + '/sms/registercode', {
					mobile: phoneNumberEl.value
				}).then(function(response) {
					if (!response.success) {
						cerBox.phoneCodeFld.showError(response.msg);
					} else {
						cerBox.phoneCodeFld.countdown(60);
					}
				});
			},
			
			afterClose: function() {
				// refresh captcha
				refreshCaptcha();
				captchaEl.value = '';
				removeError('captcha');
			},
			
			changePhone: function() {
				focusUtil.focus(phoneNumberEl);
				phoneNumberEl.value = '';
			}
		});
		cerBox.placeAt(document.body);
		cerBox.show();
	}
	
	function onRegister() {
		
		if (!regBtn.disabled && isValid()) {
			popCerWin();
		}
	}
	
	function addListeners() {
		formItemInputs.on(mouse.enter, function(event) {
			domClass.add(this, 'ui-input-enter');
		});
		formItemInputs.on(mouse.leave, function(event) {
			domClass.remove(this, 'ui-input-enter');
		});
		formItemInputs.on('focus', function(event) {
			removeError(this);
			domClass.add(this, 'ui-input-focus');
		});
		formItemInputs.on('blur', function(event) {
			domClass.remove(this, 'ui-input-focus');
		});
		
		on(captchaImgEl, 'click', function(event) {
			refreshCaptcha();
		});
		on(protocolLink, 'click', function(event) {
			if(!win){
				win = new Win({
					width:900,
					height:500,
					title:"注册协议"
				});
				win.placeAt(document.body);
			}
			win.show();
			when(Data.getProtocol({id:0}),function(tmpl){
				win.set("msg",tmpl);
			});
			event.stopPropagation();
/*			if (!this.isClicked) {
				protocol.style.display = 'block';
				this.innerHTML = '《关闭网上融资用户服务协议》';
				fx.animateProperty({
					node: protocol,
					properties: {
						height: {end: 200, start:0}
					}
				}).play();
				this.isClicked = true;
			} else {
				this.innerHTML = '《网上融资用户服务协议》';
				fx.animateProperty({
					node: protocol,
					properties: {
						height: {end: 0, start:200}
					},
					onEnd: function() {
						protocol.style.display = 'none';
					}
				}).play();
				this.isClicked = false;
			}*/
		});
		on(phoneNumberEl, 'blur', function() {
			validatePhone(this.value);
		});
		on(passwordEl, 'keyup', function() {
			var value = this.value,
				level = Global.checkStrong(value);
			switchPasswordLevel(level);
		});
		on(passwordEl, 'blur', function() {
			if (validatePassword(this.value)) {
				focusUtil.focus(passwordConfirmEl);
			}
		});
        on(refPhoneEl, 'blur', function() {
            validateRefPhone(this.value);
        });
		on(captcha, 'blur', function() {
			validateCaptcha(this.value);
		});
		on(passwordConfirmEl, 'blur', function() {
			validatePasswordConfirm();
		});
		on(regBtn, 'click', onRegister);
		on(protocolToggler, 'click', function() {
			regBtn.set('disabled', !this.checked);
		});
	}
	return {
		init: function() {
			initData();
			initView();
			addListeners();
			initRSA();
			Helper.init(config);
		}
	}
});