/**
 * global configuration
 * */
define([
    'dojo/dom',
    'dijit/focus',
    'dojo/number',
    'dojox/math/round',
    'dojo/_base/config',
	'dojo/_base/lang'
], function(dom, focusUtils,number, round, Config, lang) {
	
	function Evaluate(character) {
	    if (character >= 48 && character <= 57)
	        return 1;
	    if ((character >= 65 && character <= 90) || (character >= 97 && character <= 122))
	        return 2;
	    if ((character >= 32 && character <= 47) || (character >= 58 && character <= 64) || (character >= 94 && character <= 96) || (character >= 123 && character <= 126))
	        return 4;
	    else
	        return 8;
	}
	
	function bitTotal(num) {
	    modes = 0;
	    for (var i = 0; i < 4; i++) {
	        if (num & 1) modes++;
	        num >>>= 1;
	    }
	    return modes;
	}
	
	// requestAnimationFrame compatible
	window.requestAnimFrame = (function(){
	    return  window.requestAnimationFrame       ||
	            window.webkitRequestAnimationFrame ||
	            window.mozRequestAnimationFrame    ||
	        function( callback ){
	            window.setTimeout(callback, 1000 / 60);
	        };
	})();
	
	// ie8 
	if (!Array.prototype.indexOf) {
		Array.prototype.indexOf = function(elt /* , from */) {
			var len = this.length >>> 0;
			var from = Number(arguments[1]) || 0;
			from = (from < 0) ? Math.ceil(from) : Math.floor(from);
			if (from < 0)
				from += len;
			for (; from < len; from++) {
				if (from in this && this[from] === elt)
					return from;
			}
			return -1;
		};
	}
	
	return {
		// this param should be changed to suit your circumstance
		baseUrl: '',

        QQ: Config.QQ,
        hotline: Config.hotline,
		
		// email validation regex expression from baidu
		emailReg: '/^([a-zA-Z0-9_\.\-\+])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/',
		
		// accumulate password strength
		checkStrong:function(str) {
		    var sPW = str;
		    if (sPW.length < 1)
		        return 0;
		    Modes = 0;
		    for (var i = 0; i < sPW.length; i++) {
		        Modes |= Evaluate(sPW.charCodeAt(i));
		    }
		    var num = bitTotal(Modes);
		    return num;
		},
	
		// simple encrypt information with ***
		encodeInfo: function(info, headCount, tailCount) {
			headCount = headCount || 0;
			tailCount = tailCount || 0;
			info = lang.trim(info);
			var header = info.slice(0, headCount),
				len = info.length,
				tailer = info.slice(len - tailCount),
				mask = '**************************************************', // allow this length
				maskLen = len - headCount - tailCount;
			return maskLen > 0 ? (header + mask.substring(0, maskLen) + tailer) : info;
		},
		
		// invoke function as time as require
		loop: function(count, fn, onComplete, scope, time) {
			time = time || 1;
			var timeoutId;
			if (!count) {
				(function() {
					fn(count);
					setTimeout(arguments.callee, time * 1000);
				})();
			}
			else if (count > 0) {
				(function() {
					fn(count);
					if (count > 0) {
						timeoutId = setTimeout(arguments.callee, time * 1000);
						if (scope) {
							scope.timeoutId = timeoutId;
						}
					} else {
						if (onComplete) {
							onComplete();
						}
					}
					count--;
				})();
			}
		},
		
		// say hello according time
		greet: function(date) {
            var date = date || new Date(),
			    hour = date.getHours(),
				words = '';
			switch (true) {
			case hour >= 0 && hour < 5:
				words = '凌晨好';
				break;
			case hour >= 5 && hour < 8:
				words = '早上好';
				break;
			case hour >= 8 && hour < 11:
				words = '上午好';
				break;
			case hour >= 11 && hour < 13:
				words = '中午好';
				break;
			case hour >= 13 && hour < 17:
				words = '下午好';
				break;
			case hour >= 17 && hour < 19:
				words = '傍晚好';
				break;
			case hour >= 19 && hour <= 23:
				words = '晚上好';
				break;
			}
			return words;
		},
		
		//convert amount
		decodeAmount: function(value) {
			return parseInt(value) / 100.0;
		},
		
		encodeAmount: function(value) {
			return parseInt(parseInt(value) * 100);
		},
		
		// fix precision
		roundAmount: function(value, places) {
			if (typeof value === 'string') {
				value = parseFloat(value);
			}
			if (typeof places === 'number') {
				return round(value, places);
			}
			return round(value, 2);
		},
		
		// add bank card mask
		maskBankcard: function(no) {
			var len = no.length,
				left = no.slice(0, 4),
				right = no.slice(len-4, len);
			return left + ' xxxx xxxx ' + right;
		},
		
		//focus the first input when rendered
		focusText: function(el) {
			setTimeout(function() {
				var firstEl = el || 
					dom.byId('tofocus') || 
					dom.byId('dijit_form_ValidationTextBox_0') || 
					dom.byId('widget_dijit_form_ValidationTextBox_0') || 
					dom.byId('uniqName_1_0') ||
                    dom.byId('uniqName_2_0');
				if (firstEl) {
					focusUtils.focus(firstEl);
				}
			}, 200);
		},
		
		/**
		 * format cash amount, change unit cent to yuan, remain two numbers after dot
		 * @param value number
		 * @param places number -- numbers of digital after dot
		 * @return
		 */
        formatAmount: function(value, places, unit, carry) {
            if (this.isEmpty(value) || isNaN(value)) {
                return '';
            }
            if (carry == 1) { // 舍去
                value = ('00' + value).split('.')[0];
                return number.format(parseInt(value.slice(0, value.length - 2)), {
                    places: (typeof places !== 'number') ? 2 : places
                });
            }
            t_value = value / 100;
            value = this.fillZero(this.roundAmount(t_value, places), places);

            if (this.fillZero(this.roundAmount(t_value, 2), 2).split('.')[1] != '00') {
                return number.format(value, {
                    places: (typeof places !== 'number') ? 2 : places
                });
            }
            else if (unit == 'w') {
                var num = value.split('.')[0];
                if (num.length >= 5 && num.slice(num.length - 4) == '0000') {
                    value = this.roundAmount(parseFloat(value) / 10000, 6);
                    value = number.format(value, {
                        places: 0
                    });
                    return value + '万';
                }
            }

            value = number.format(value, {
                places: places || 0
            });

            return value;
        },
		
		formatDate: function(date) {
			return date ? date.slice(0, 4) + '-' + date.slice(4, 6) + '-' + date.slice(6, 8) : '无';
		},

        getInterest: function(obj) {
            var type = obj.prodBillType,// 0:free, 1: interest, 2: fee
                unit = obj.interestAccrualMode; // 0: day, 1: day, 2: month
            var res = '';
            if (type == 0) {
                return '免费';
            }
            if (type == 1) {
                return (unit==2 ? '月': '日') + '利率：' + (obj.interestRate || obj.interestDayRate);
            }
            if (type == 2) {
                return '管理费：' + this.formatAmount(obj.fee) + ' 元';
            }
        },
		
		/**
		 * format number
		 * @param value number
		 * @param places number -- numbers of digital after dot
		 * @return
		 */
		formatNumber: function(value, places) {
			return this.fillZero(this.roundAmount(value, places), places);
		},
		
		// change to wan unit
		formatAmountUnit: function(value) {
			var num = value.split('.')[0];
			if (num.length >= 5) {
				value = this.roundAmount(parseFloat(value) / 10000, 6);
				return value + '万';
			} else {
				return value;
			}
		},
		
		/**
		 * parse cash amount, change unit yuan to cent
		 * @param value string
		 * @return
		 */
		parseAmount: function(value) {
			return this.roundAmount(parseFloat(value) * 100, 0);
		},


        switchAmount: function(value, unit, carry) {
            // unit: 1为元，0为分， carry：1为舍去，0为四舍五入
            value = ('00' + value).split('.')[0];
            if (unit == 1) { // 元
                if (carry == 1) {
                    return parseInt(value.slice(0, value.length - 2) + '00');
                } else {
                    var prefixStr = value.slice(0, value.length - 2),
                        tenPos = value.slice(value.length - 2, value.length - 1);
                    if (tenPos < (5 + '')) {
                        return parseInt(prefixStr + '00');
                    } else {
                        return parseInt(parseInt(prefixStr) + 1 + '00');
                    }
                }
            }
            return parseInt(value);
        },
		
		fillZero: function(value, decNum) {
			if (typeof value === 'number') {
				value = value + '';
			}
			if (typeof decNum === 'undefined') {
				decNum = 2;
			}
			var pre = '0000000000000000000000000';
			var num = value.split('.');
			return decNum === 0 ? num[0] : num[0] + '.' + (num[1] || '') + pre.slice(0, decNum - (num[1] || '').length);
		},
		
		// default value, init value
		isEmpty: function(value) {
			if (value === this.EMPTY) {
				return true;
			}
			return false;
		},
		
		// default value
		EMPTY: '',
		
		/**
		 * make the loading dot, like . then .. then ...
		 * @param el object,
		 * @param interval number
		 * @return
		 */
		waitDot: function(el, interval) {
			setInterval(function() {
				if (el.innerHTML.length >= 3) {
					el.innerHTML = "";
				} else {
			    	el.innerHTML += ".";
			    }
			        
			}, interval || 100);
		},
		
		/**
		 * check object if empty
		 * @param obj object
		 * @return boolean
		 * */
		isOwnEmptyObj: function(obj) {
			 for(var name in obj) { 
		        if(obj.hasOwnProperty(name)) 
		        { 
		            return false; 
		        } 
		    } 
		    return true; 
		},
		
		/**
		 * make a sweet call for login user
		 * @param obj object
		 * @return string
		 * */
		sirOrLady: function(params) {
			var res = '';
			if (params.userNickName) {
				res = params.userNickName;
			} else if (params.idcard && params.userRealName&&params.isTrust==1) {
				var userRealName=params.userRealName.replace(/\s+/g,"");
				var first_name = userRealName.length == 4 ? userRealName.slice(0,2) :
					userRealName.slice(0,1);
				if (params.idcard.length == 15) {
					res = first_name + (parseInt(params.idcard.slice(14, 15)) % 2 ? '先生' : '女士');
				} else {
					res = first_name + (parseInt(params.idcard.slice(16, 17)) % 2 ? '先生' : '女士');
				}
			} else {
				res = params.userName || params.mobile || params.email;
			}
			return res;
		},

        /**
         *
         * @param obj object
         * @return number 0 - 1
         * */
        getSecurity: function(currentRate, keys) {
            keys = keys || [];
            currentRate = currentRate || 0;
            var items = {'loginPwd': 10, 'realName': 10, 'card': 5, 'tradePwd': 10, 'phone': 10, 'email': 5, 'question': 5};
            var totalScore = 0;
            for (var i in items) {
                totalScore += items[i];
            }
            var currentScore = currentRate * totalScore;
            for (var i in keys) {
                if (i in items) {
                    currentScore += items[i] * keys[i];
                }
            }
            return currentScore / totalScore;
        },

        getUrlParam: function(param) {
            var paramIndex = location.href.indexOf('?');
            if (paramIndex > 0) {
                var params = location.href.slice(paramIndex + 1).split('&'),
                    paramsObj = {};
                for (var i = 0, len = params.length; i < len; i++) {
                    var dict = params[i].split('=');
                    paramsObj[dict[0]] = dict[1];
                }
                return param ? paramsObj[param] : paramsObj;
            }
        },

        sum: function(array, field) {
            var i = 0, len = array.length,
                res = 0;
            for (i; i < len; i++) {
                res += array[i][field];
            }
            return res;
        },

        findObj: function(array, key, value, key2, value2) {
            var i = 0, len = array.length,
                res;
            for (i; i < len; i++) {
                if (array[i][key] == value && !key2) {
                    return array[i];
                } else if (key2 && array[i][key] == value && array[i][key2] == value2) {
                	return array[i];
                }
            }
        },

        getContractStatusDict: function() {
            return {'0': '操作中', '1': '终止'};
        },

        // remove all html tag
        delHtmlTag: function(str) {
            return str.replace(/<[^>]+>/g,'');
        },


		numberFormatS: function(number){
			if(number == null || number == '' || isNaN(number)) return 0;
			var o = number.toString().replace(/\b(0+)/g,'');
			return o.replace(/^(\d+)((\.\d+)?)$/,function(s,s1,s2){return s1.replace(/\d{1,3}(?=(\d{3})+$)/g,'$&,') + s2;});
		}
 	};
});