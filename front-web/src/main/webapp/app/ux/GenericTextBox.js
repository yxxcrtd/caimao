define([ 
    'dojo/_base/declare',
    'dojo/window',
    'dijit/form/ValidationTextBox',
    'app/common/Global',
    'dojo/on',
    'dojo/dom-construct',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/dom-attr',
    'app/ux/GenericTooltip',
    'app/ux/GenericKeyboard',
    'dojo/number',
    'dojo/keys',
    'dojo/query',
    'dijit/registry',
    'dojo/has',
    'dojo/_base/sniff',
    'dojo/dom-geometry',
    'app/common/Position',
    'dojo/text!./templates/GenericTextBox.html'
], function (declare, win, TextBox, Global, on, domConstruct, domClass, domStyle, domAttr, 
		Tooltip, Keyboard, number, keys, query, registry, has, sniff, domGeometry, Position, template) {
	var unitNode;
	return declare([TextBox], {
		templateString: template,
		// config params
		label: '',
		labelStyle: '',
		cls: 'ui-input ui-input-extra',
		inputWidth: 194,
		separator: '：',
		//required: true,
		validates: [], 
		limitRegex: '',
		precision: 2,
		isNumber: false,
		leftOffset: 0,
		unit: '',
		unitStyle: '',
		isAmount: false,
        showStrength: false,
		
		// invoke when call set attribute
		_setLabelAttr: function(value) {
			this._set('label', value);
			this.labelNode.innerHTML = value + this.separator;
			domStyle.set(this.labelNode, {
				left: '-130px'
			});
		},

        _updatePlaceHolder: function(){
            if(this._phspan){
                this._phspan.style.display = (this.placeHolder && !this.textbox.value) ? "" : "none";
                Position.adjustSize(this._phspan);
            }
        },

        switchPasswordLevel: function(level) {
            var levelEl = query('p', this.streEl)[0];
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
        },

        _setShowStrengthAttr: function(value) {
            var me = this;
            if (value) {
                me.streEl = domConstruct.toDom('<div class="ui-prompt-extra"><p style="height: 100%; width: 0%;" class="genericProgressBarBad"></p></div>');
                domConstruct.place(me.streEl, me.domNode);
            }
            on(me, 'keyup', function(e) {
                me.switchPasswordLevel(Global.checkStrong(me.get('value')));
            });
        },

		_setTypeAttr: function(value) {
			var me = this;
			this._set('type', value);
			this.inherited(arguments);
			var isPwd = (value === 'password');
			if (isPwd) {
				if (has('ie') > 8 || !has('ie')) { // ie8 the type is readonly
					var eyeEl = domConstruct.toDom('<i class="fa fa1-eye input-pwd-extra" style="right: 20px">&#xf06e;</i>');
					domConstruct.place(eyeEl, this.domNode);
					on(eyeEl, 'click', function() {
						var watching = !domClass.contains(this, 'fa1-eye');
						if (!watching) {
							domClass.replace(this, 'fa1-eye-slash', 'fa1-eye');
							eyeEl.innerHTML = '&#xf070;';
							domAttr.set(me.textbox, {
								type: 'text'
							});
							me.textbox.focus();
						} else {
							domClass.replace(this, 'fa1-eye', 'fa1-eye-slash');
							eyeEl.innerHTML = '&#xf06e;';
							domAttr.set(me.textbox, {
								type: 'password'
							});
							me.textbox.focus();
						}
					});
				}
				var keyboardEl = domConstruct.toDom('<i class="fa input-pwd-extra">&#xf11c;</i>');
				domConstruct.place(keyboardEl, this.domNode);
				Keyboard.attach(me.textbox, keyboardEl);

				// reset input width
				var tw = parseFloat(me.domNode.style.width),
					dw = (eyeEl ? 20 : 0) + (keyboardEl ? 20 : 0),
					offset = 4;
				domAttr.set(me.textbox, {
					style: 'width:' + (tw - dw - offset) + 'px !important'
				});
			}
		},

		_setUnitAttr: function(value) {
			this._set('unit', value);
			if (value) {
				unitNode = domConstruct.create('span', {className: 'ui-label-extra',
					style: 'left: 105px; margin-top: -18px', // fix the position, but not recommend
					innerHTML: value}, this.domNode);
			}
		},

		_setUnitStyleAttr: function(value) {
			this._set('unitStyle', value);
			domStyle.set(unitNode, value);
		},

		_setLeftOffsetAttr: function(value) {
			this._set('leftOffset', value);
			domStyle.set(this.domNode, {
				marginLeft: value + 'px'
			});
//			domStyle.set(this.labelNode, {
//				left: (360 - 130) + 'px'
//			});
		},

		_setInputWidthAttr: function(value) {
			this._set('inputWidth', value);
			this.domNode.style.width = value + 'px';
		},

		_setLabelStyleAttr: function(value) {
			this._set('labelStyle', value);
			domStyle.set(this.labelNode, value);
		},

		_setClsAttr: {node: 'domNode', type: 'class'},

		// bind event handlers
		addListeners: function() {
			var me = this;

			// limit input
			on(me, 'keypress', function(event) {
			    var charOrCode = event.charCode || event.keyCode;
				switch(charOrCode){
			      case keys.LEFT_ARROW:
			      case keys.UP_ARROW:
			      case keys.DOWN_ARROW:
			      case keys.RIGHT_ARROW:
			        break;
			      case keys.ENTER:
			    	  this.textbox.blur();
			    	  var btns = query('.dijitButton.enterbutton');
			    	  var tempBtns = [];
			    	  btns.forEach(function(btnEl) {
							var btn = registry.byNode(btnEl);
							if (!btn.get('disabled') && btnEl.offsetHeight !==0 && (btn.handler || btn.onClick)) {
								tempBtns.push(btn);
							}
						});
			    	  if (tempBtns.length > 0) {
			    		  (tempBtns[0].handler || tempBtns[0].onClick).call(tempBtns[0]);
			    	  }
			    	  event.preventDefault();
				      break;
			      case keys.BACKSPACE:
			        break;
			      case keys.TAB:
			        break;
			      case keys.ESCAPE:
			        break;
			      default:
			          var cc = String.fromCharCode(charOrCode);
                      var getCursorPos = function(elm) {
                          if(elm.createTextRange) { // IE
                              var range = document.selection.createRange();
                              range.setEndPoint('StartToStart', elm.createTextRange());
                              return range.text.length;
                          } else if(typeof elm.selectionStart == 'number') { // Firefox
                              return elm.selectionStart;
                          }
                      };
                      if (me.limitRegex && !me.limitRegex.test(cc)) {
					      event.preventDefault();
					  }
					  if (me.get('isAmount') && me.precision) { // number precision
					      var value = me.textbox.value + '';
						  var match = /\.+?(.*)/.exec(value);
						  if (match && (match[1].length >= me.precision || cc == '.') &&
								(value.indexOf('.') !== -1 && getCursorPos(me.textbox) > value.indexOf('.'))) { // no IE
						      event.preventDefault();
						  }
					  } else if (me.get('isAmount') && me.precision == 0) {
                          if (cc == '.') {
                              event.preventDefault();
                          }
                      }
					  
					  if (me.get('isAmount')) {
	                      	var value = me.textbox.value + '';
	                          if (parseFloat(value + cc) > 99999999999) {
	                              event.preventDefault();
	                          }
	                      }

                      if (me.get('isAmount')) {
                      	var value = me.textbox.value + '';
                          if (parseFloat(value + cc) > 99999999999) {
                              event.preventDefault();
                          }
                      }

					  if (me.get('max')) { // number max
                          var value = me.textbox.value + '';
                          if (parseFloat(value + cc) > parseFloat(me.get('max')) / 100) {
                              event.preventDefault();
                          }
                      }
			      }
			});
			on(me.textbox, 'paste', function(e) {
                var clipboardData = e.clipboardData || window.clipboardData;
				var data = clipboardData.getData('text');
				e.preventDefault();
				var value = data;
				if (me.isAmount) {
					value = parseFloat(data);
					if (me.precision) {
						var match = /\.+?(.*)/.exec(value+'');
						if (match && (match[1].length >= me.precision)) {
							value = value.toFixed(me.precision);
						}
					}
				}
				me.set('value', value);
			});

			// hover the domnode, if has a error, show it
			on(me.domNode, 'mouseenter', function(e) {
				var error = me.getErrorMessage();
				if (me.state === 'Error' && error) {
					me.displayMessage(error);
				}
			});
		},

		// call by confirm
		checkValidity: function() {
			this._hasBeenBlurred = true;
			return this.validate();
		},

		// override
		validator: function(/*anything*/ value, /*__Constraints*/ constraints){
			// summary:
			//		Overridable function used to validate the text input against the regular expression.
			// tags:
			//		protected
			var len = this.validates.length,
				i = 0,
				validates = this.get('validates');

			for(; i < len; i++) {
				var pattern = validates[i]['pattern'],
					message = validates[i]['message'];
				if (typeof pattern == 'function') {
					var v = pattern.call(this);
					if (!v) {
						this.set('message', message);
						return false;
					}
				}
				else if(!pattern.test(value)) {
					this.set('message', message);
					return false;
				}
			}

			return (new RegExp("^(?:" + this._computeRegexp(constraints) + ")"+(this.required?"":"?")+"$")).test(value) &&
				(!this.required || !this._isEmpty(value)) &&
				(this._isEmpty(value) || this.parse(value, constraints) !== undefined); // Boolean
		},

		// override
		validate: function(/*Boolean*/ isFocused){
			// summary:
			//		Called by oninit, onblur, and onkeypress.
			// description:
			//		Show missing or invalid messages if appropriate, and highlight textbox field.
			// tags:
			//		protected
			var message = "";
			var isValid = this.disabled || this.isValid(isFocused);
			if(isValid){ this._maskValidSubsetError = true; }
			var isEmpty = this._isEmpty(this.textbox.value);
			var isValidSubset = !isValid && isFocused && this._isValidSubset();
			this._set("state", isValid ? "" : (((((!this._hasBeenBlurred || isFocused) && isEmpty) || isValidSubset) && (this._maskValidSubsetError || (isValidSubset && !this._hasBeenBlurred && isFocused))) ? "Incomplete" : "Error"));
			this.focusNode.setAttribute("aria-invalid", this.state == "Error" ? "true" : "false");

			if(this.state == "Error"){
				this._maskValidSubsetError = isFocused && isValidSubset; // we want the error to show up after a blur and refocus
				message = this.getErrorMessage(isFocused);
			}else if(this.state == "Incomplete"){
				message = this.getPromptMessage(isFocused); // show the prompt whenever the value is not yet complete
				this._maskValidSubsetError = !this._hasBeenBlurred || isFocused; // no Incomplete warnings while focused
			}else if(isEmpty){
				message = this.getPromptMessage(isFocused); // show the prompt whenever there's no error and no text
			}
			this.set("message", message);

			return isValid;
		},

		// override
		isValid: function(isFocused){
			// summary:
			//		Tests if value is valid.
			//		Can override with your own routine in a subclass.
			// tags:
			//		protected
			return isFocused ? true : this.validator(this.get('value'), this.get('constraints'));
		},

		// override
		getErrorMessage: function(/*Boolean*/ /*===== isFocused =====*/){
			// summary:
			//		Return an error message to show if appropriate
			// tags:
			//		protected

			if(this.message) {
				return this.message;
			}

			var invalid = this.invalidMessage == "$_unset_$" ? this.messages.invalidMessage :
				!this.invalidMessage ? this.promptMessage : this.invalidMessage;
			var missing = this.missingMessage == "$_unset_$" ? this.messages.missingMessage :
				!this.missingMessage ? invalid : this.missingMessage;
			return (this.required && this._isEmpty(this.textbox.value)) ? missing : invalid; // String
		},

		// override
		// change: if the field blur, the message should display
		displayMessage: function(/*String*/ message, type){
			// summary:
			//		Overridable method to display validation errors/hints.
			//		By default uses a tooltip.
			// tags:
			//		extension
            type = type || '';
			var me = this;
            var TooltipTypeMap = {'': 'info', 'Warning': 'warning', 'Error': 'warning'};
			if(message){
                var bb = domGeometry.position(me.domNode);
                if (bb.w && bb.h) {
                    win.scrollIntoView(me.domNode);
                    setTimeout(function() {
                        Tooltip.show(message, me.domNode, TooltipTypeMap[me.state]);
                    }, 100);
                    //Tooltip.show(message, me.domNode, TooltipTypeMap[me.state]);
                    if (me.state === 'Error') {
                        domClass.add(me.domNode, 'dijitTextBoxError');
                    }
                }
			}else{
				Tooltip.hide(me.domNode);
				domClass.remove(me.domNode, 'dijitTextBoxError');
			}
		},

		// change to chinese amount
		numToCny: function (num) {
			if (parseFloat(num) > 999999999999099) {
				ret = '金额过大无法表示';
				return ret;
			}
		    var capUnit = ['万','亿','万','圆',''];
		    var capDigit = { 2:['角','分',''], 4:['仟','佰','拾','']};
		    var capNum=['零','壹','贰','叁','肆','伍','陆','柒','捌','玖'];
		    if (((num.toString()).indexOf('.') > 16)||(isNaN(num)))
		        return '';
		    num = (Math.round(num*100)/100).toString();
		    num =((Math.pow(10,19-num.length)).toString()).substring(1)+num;
		    var i,ret,j,nodeNum,k,subret,len,subChr,CurChr=[];
		    for (i=0,ret='';i<5;i++,j=i*4+Math.floor(i/4)){
		        nodeNum=num.substring(j,j+4);
		        for(k=0,subret='',len=nodeNum.length;((k<len) && (parseInt(nodeNum.substring(k), 10)!=0));k++){
		            CurChr[k%2] = capNum[nodeNum.charAt(k)]+((nodeNum.charAt(k)==0)?'':capDigit[len][k]);
		            if (!((CurChr[0]==CurChr[1]) && (CurChr[0]==capNum[0])))
		                if(!((CurChr[k%2] == capNum[0]) && (subret=='') && (ret=='')))
		                    subret += CurChr[k%2];
		        }
		        subChr = subret + ((subret=='')?'':capUnit[i]);
		        if(!((subChr == capNum[0]) && (ret=='')))
		            ret += subChr;
		    }
		    ret=(ret=='')? capNum[0]+capUnit[3]: ret;
//		    if (/undefined/.test(ret)) {
//		    	ret = '金额过大无法表示';
//		    }
		    return ret;
		},

		// get the amount unit cent
		getAmount: function() {
			return Global.parseAmount(this.get('value'));
		},

		get: function(param) {
			var res = this.inherited(arguments);
			if (res && param === 'value' && this.get('isAmount')) {
				var temp = number.parse(res, {
				    places: 2
				});
				if (temp) {
					res = temp;
				}
			}
			return res;
		},

		reset: function() {
			this.focused = true; // reset will validate the field, focused false will pop the message
			this.inherited(arguments);
			this.focused = false;
		},

		postCreate: function() {
    		var me = this;
    		if (typeof me.get('maxLength') == 'number') {
    			domAttr.set(me.textbox, 'maxLength', me.get('maxLength'));
    		}
    		if (me.params.extraHTML) {
    			var dom = domConstruct.toDom(me.params.extraHTML);
    			domConstruct.place(dom, me.domNode);
    		}
    		if (me.get('isAmount')) {
    			me.validates.push({
					pattern: /^\d+\.?\d*$/,
					message: '请输入正确的金额格式'
				});
    			me.validates.push({
					pattern: function() {
						return isNaN(parseFloat(this.get('value'))) ? true : (parseFloat(this.get('value')) > 0);
					},
					message: '金额数值必须大于0'
				});
				if(me.get('min')){
					me.validates.push({
						pattern: function() {
							return isNaN(parseFloat(this.get('value'))) ? true : (parseFloat(this.get('value')) >= 100);
						},
						message: '金额数值必须大于 ' + me.get('min')
					});
				}
    			me.validates.push({
					pattern: /^.*$/,
					message: ''
				}); // default one
    			me.set('precision', me.precision);
    			var amountEl = domConstruct.toDom('<div class="tooltip-amount" style="display: none"></div>');
    			domConstruct.place(amountEl, me.domNode);
    			on(me, 'focus', function(e) {
    				var value = parseFloat(me.get('value'));
    				me.set('value', value);
    				if (value) {
    					amountEl.innerHTML = me.numToCny(Global.fillZero(parseFloat(value)).replace('.', '0'));
    					amountEl.style.display = 'block';
    				} else {
    					amountEl.style.display = 'none';
    				}
    			});
    			on(me, 'blur', function(e) {
    				var num = number.format(parseFloat(me.textbox.value), {
    				    places: 2
    				});
    				me.set('value', num);
    				amountEl.style.display = 'none';
    			});
    			on(me, 'keyup', function(e) {
    				var value = parseFloat(me.get('value'));
    				if (value) {
    					amountEl.innerHTML = me.numToCny(Global.fillZero(parseFloat(value)).replace('.', '0'));
    					amountEl.style.display = 'block';
    				} else {
    					amountEl.style.display = 'none';
    				}
    			});
    		}
    		me.addListeners();
    	    this.inherited(arguments);
    	}
	});
});