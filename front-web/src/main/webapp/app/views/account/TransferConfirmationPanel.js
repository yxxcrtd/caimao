define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/views/ViewMixin',
    'dojo/dom-construct',
    'dojo/query',
    'app/common/Global',
    'dojo/on',
    'app/common/Ajax',
    'dojo/dom',
    'dojo/string',
    'dojo/dom-style',
    'app/ux/GenericTextBox',
    'app/ux/GenericButton',
    'app/ux/GenericDisplayBox',
    'app/ux/GenericRadioBox',
    'app/ux/GenericComboBox',
    'app/ux/GenericTextarea',
    'dojo/store/Memory',
    'app/stores/ComboStore',
    'app/ux/GenericPrompt',
    'app/ux/GenericTooltip', 
    'dojo/_base/config',
    'dojo/text!./templates/TransferConfirmationPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin, domConstruct, query,
            Global, on,Ajax, dom, string, domStyle, TextBox,Button,DisplayBox, RadioBox, ComboBox,
            Textarea, Memory, ComboStore,Prompt, Tooltip, cfg, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {

        baseUrl: Global.baseUrl,

        templateString: template,

        user_nick_name: '',
        address:'',
        education: '',

        _setUser_nick_nameAttr: function(value) {
            this.nickNameFld.set('value', value);
        },

        _setAddressAttr: function(value) {
            this.addressFld.set('value', value);
        },

        _setEducationAttr: function(value) {
            this.educationFld.set('value', value);
        },

        getData: function() {
            return {
                nick_name: this.nickNameFld.get('value'),
                address: this.addressFld.get('value'),
                education: this.educationFld.get('value')
            };
        },

        getValues: function() {
            return {
                user_nick_name: this.nickNameFld.get('value'),
                address: this.addressFld.get('value'),
                education: this.educationFld.get('value')
            };
        },

        render: function() {
            var me = this;
            /*me.inveExperienceFld = new ComboBox({
             label: '投资经验',
             store: new Memory({
             data:[{name: '1年', id: '1年'},
             {name: '2-5年', id: '2-5年'},
             {name: '5-10年', id: '5-10年'}]
             }),
             searchAttr: 'id',
             labelAttr: 'name',
             editable: false,
             style: {
             marginLeft: '100px'
             },
             labelStyle: {
             left: '-30px'
             }
             });*/
            var bankCardsName = cfg.bankCards.split(','), bankData = [];
            if(bankCardsName){
	            for(var i = 0;i < bankCardsName.length - 1;i++){
	            	bankData[i] = {'name': bankCardsName[i], 'id': bankCardsName[i]};
	            }
            }
            me.educationFld = new ComboBox({
                label: '入账银行',
                searchAttr: 'id',
                labelAttr: 'name',
                item: bankData[0],
                store: new Memory({
                    data:bankData
                }),
                editable: false,
                style: {
                    marginLeft: '200px',
                    marginBottom:'20px'
                }
            });
            me.nickNameFld = new TextBox({
                label: '转账金额',
                style: {
                    marginLeft: '200px',
                    marginBottom: '20px'
                },
                labelStyle: {
                    left: '-130px'
                },
                label: '充值金额',
                validates: [{
                    pattern: /.+/,
                    message: '请输入充值金额'
                }],
                unit: '元',
                limitRegex: /[\d\.]/,
                isAmount: true,
                isNumber: true
            });

            me.addressFld = new Textarea({
            });
            //******************************//
            /*me.inveExperienceFld = new ComboBox({
                label: '投资经验',
                store: new Memory({
                    data:[{name: '1年', id: '1年'},
                        {name: '2-5年', id: '2-5年'},
                        {name: '5-10年', id: '5-10年'}]
                }),
                searchAttr: 'id',
                labelAttr: 'name',
                editable: false,
                style: {
                    marginLeft: '100px'
                },
                labelStyle: {
                    left: '-30px'
                }
            });*/
            //*****************************//

//    		me.genderFld = new RadioBox({
//    			label: '性别',
//    			items: [{
//    		        value: '1',
//    		        name: 'gender',
//    		        label: '男'
//    			}, {
//    		        value: '2',
//    		        name: 'gender',
//    		        label: '女'
//    			}]
//    		});


//    		var store = new Memory({
//    	        data: [
//    	            {name: '高中', id: '高中'},
//    	            {name: '大学', id: '大学'},
//    	            {name: '硕士', id: '硕士'}
//    	        ]
//    	    });

//    		me.introFld = new TextBox({
//    			label: '介绍人',
//    			style: {
//    				marginLeft: '100px'
//    			},
//    			labelStyle: {
//    				left: '-30px'
//    			}
//    		});
//    		me.addressFld = new Textarea({
//    			style: {
//    				marginLeft: '10px'
//    			}
//    		});
//    		var addressCtn = domConstruct.toDom('<div><label for="'+me.addressFld.get('id')+'" style="font-size: 14px; color: #666666; margin-left: 48px;">地址：</label></div>');
//    		domConstruct.place(me.addressFld.domNode, addressCtn);

            me.educationFld.placeAt(me.formLeftNode, 0);
            me.nickNameFld.placeAt(me.formLeftNode, 1);
            me.addressFld.placeAt(me.remarkNode);

            me.nextBtn = new Button({
                label: '确定',
                enter: true,
                width: 140,
                height: 36,
                style: {
                    'paddingTop' : '30px',
                    'marginLeft' : '223px'
                },
                onClick: function() {
                    if (me.nickNameFld.checkValidity()) {
                        var att = '';
                        att = '入账银行：' + me.educationFld.value + ' 备注：' + me.addressFld.get('value');
                        me.nextBtn.loading(true);
                        Ajax.post(Global.baseUrl  + "/account/charge",{
                            pay_company_no:'-2',
                            order_abstract:att,
                            charge_amount:me.nickNameFld.getAmount(),//充值金额
                            terminal_type:0,
                            pay_type:-2
                        }).then(function(response){
                            me.nextBtn.loading(false);
                            if(response.success){
                                me.hide();
                                var prompt = new Prompt({
                    type: 'success',
                    msg: '恭喜您，银行转账确认单提交成功！',
                    subMsg: '<span style="color: #666666">申请确认正在紧张处理，请耐心等待。如有疑问请拨打客服电话<br/> ' +
                        '<b class="am-ft-orange">'+Global.hotline+'</b></span>',
                    linkText: '返回会员中心',
                    link:'home/index.htm'
                });
                prompt.placeAt('transferConfirm');
                            }else{
                                Tooltip.show(response.msg, me.nextBtn.innerNode, 'warning');
                            }
                            
                        });
                    }
                }
            });
            me.nextBtn.placeAt(me.formLeftNode,7);
        },

        setValues: function(values) {
            for (var key in values) {
                this.set(key, values[key]);
            }
        },

        postCreate: function(){
            var me = this;
            me.render();
            this.inherited(arguments);
        }
    });
});