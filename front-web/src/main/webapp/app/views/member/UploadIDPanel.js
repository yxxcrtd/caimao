define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
	'dojo/dom-style',
    'app/common/Date',
    'app/ux/GenericButton',
    'app/ux/GenericTextBox',
    'app/ux/GenericTooltip',
    'dojo/text!./templates/UploadIDPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, query, domStyle,DateUtil,Button, TextBox, Tooltip, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
	templateString: template,
	
	baseUrl: Global.baseUrl,
	upload: function() {},
	
	setValues: function(values) {
		for (var key in values) {
			this.set(key, values[key]);
		}
	},
	
	isValid: function() {
		var me = this;
        var files = query('input[type=file]', this.domNode);
		var res = this.realNameFld.checkValidity() && this.IDNoFld.checkValidity();
        if (!files[0].value) {
        	setTimeout(function() {
        		Tooltip.show('请选择正面图片', me.uploadBtn.innerNode, 'warning');
        	}, 200);
            
            return false;
        } else if (!files[1].value) {
        	setTimeout(function() {
        		Tooltip.show('请选择反面图片', me.uploadBtn.innerNode, 'warning');
        	}, 200);
            
            return false;
        }
        return res;
	},
	
	render: function() {
		var me = this;
		me.uploadBtn = new Button({
			label: '提交',
			leftOffset: 95,
			enter:true,
			handler: me.upload
		});
		me.realNameFld = new TextBox({
			label: '真实姓名',
            leftOffset: 78,
            style: {
            	'display': 'inline-block'
            },
			validates: [{
                pattern: function() {
                    return this.get('value').length <= 20;
                },
                message: '真实姓名长度不能超过20'
            }, {
				pattern: /.+/,
				message: '请输入真实姓名'
			}]
		});
		me.IDNoFld = new TextBox({
			label: '身份证号码',
            leftOffset: 78,
            style: {
            	'display': 'inline-block'
            },
			validates: [{
				pattern: /.+/,
				message: '请输入身份证号码'
			}, {
				pattern: /^(([0-9]{17}[0-9X]{1})|([0-9]{15}))$/,
				message: '身份证号码要求15位或18位数字，18位末位可以为X'
			}, {
				pattern: function() {
					var _id = me.IDNoFld.get('value');
					var powers = new Array("7","9","10","5","8","4","2","1","6","3","7","9","10","5","8","4","2");   
				    var parityBit = new Array("1","0","X","9","8","7","6","5","4","3","2");
			        if(_id.length == 15) {   
			            //TODO 
			        	return true;
			        } else if (_id.length == 18) {
			        	_id = _id + '';
			        	var _num=_id.substr(0,17);   
				        var _parityBit=_id.substr(17);   
				        var _power=0;
				        for (var i = 0; i< 17; i++) {
				        	_power += parseInt(_num.charAt(i))*parseInt(powers[i]);
				        }
				        var mod=parseInt(_power)%11;
				        if(parityBit[mod]==_parityBit){   
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
				pattern: function() {
					var value = this.get('value'),
						dateStr = value.slice(6, 10) + '-' + value.slice(10, 12) + '-' + value.slice(12, 14);
					var year18 = DateUtil.format(DateUtil.add(DateUtil.parse(dateStr), 'year', 18));
					return year18 <= DateUtil.format(new Date()) ? true : false;
			     },
			     message: '未满18周岁不能实名认证'
			}]
		});
		
		me.IDNoFld.placeAt(me.IDNoNode);
		me.uploadBtn.placeAt(me.uploadBtnNode);
		me.realNameFld.placeAt(me.realNameNode);
	},
	
	postCreate: function() {
		var me = this;
		me.render();
		me.inherited(arguments);
	}});
});