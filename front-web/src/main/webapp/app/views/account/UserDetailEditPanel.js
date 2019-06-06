define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'dojo/dom-construct',
    'dojo/query',
    'app/common/Global',
    'dojo/on',
    'dojo/dom',
    'dojo/string',
    'dojo/dom-style',
    'app/ux/GenericTextBox',
    'app/ux/GenericDisplayBox',
    'app/ux/GenericRadioBox',
    'app/ux/GenericComboBox',
    'app/ux/GenericTextarea',
    'dojo/store/Memory',
    'app/stores/ComboStore',
    'dojo/text!./templates/UserDetailEditPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, domConstruct, query, 
		Global, on, dom, string, domStyle, TextBox, DisplayBox, RadioBox, ComboBox, 
		Textarea, Memory, ComboStore,template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	baseUrl: Global.baseUrl,
    	
    	templateString: template,
    	
    	user_nick_name: '',
    	come_from:'',
    	address:'',
    	occupation: '',
    	education: '',
    	inve_experience:'',
    	
    	
    	_setUser_nick_nameAttr: function(value) {
    		this.nickNameFld.set('value', value);
    	},
    	
    	_setCome_fromAttr: function(value) {
    		this.comefromFld.set('value', value);
    	},
    	
    	_setAddressAttr: function(value) {
    		this.addressFld.set('value', value);
    	},
    	
    	_setOccupationAttr: function(value) {
    		this.occupationFld.set('value', value);
    	},
    	
    	_setEducationAttr: function(value) {
    		this.educationFld.set('value', value);
    	},
    	
    	_setInve_experienceAttr: function(value) {
    		this.inveExperienceFld.set('value', value);
    	},
    	
    	getData: function() {
    		return {
    			nick_name: this.nickNameFld.get('value'),
    			come_from: this.comefromFld.get('value'),
    			address: this.addressFld.get('value'),
    			occupation: this.occupationFld.get('value'),
    			education: this.educationFld.get('value'),
    			inve_experience: this.inveExperienceFld.get('value')    			
    		};
    	},
    	
    	getValues: function() {
    		return {
    			user_nick_name: this.nickNameFld.get('value'),
    			come_from: this.comefromFld.get('value'),
    			address: this.addressFld.get('value'),    			
    			occupation: this.occupationFld.get('value'),
    			education: this.educationFld.get('value'),
    			inve_experience: this.inveExperienceFld.get('value')    			
    		};
    	},
    	
    	render: function() {
    		var me = this;
    		me.nickNameFld = new TextBox({
    			label: '昵称',
    			style: {
    				marginLeft: '100px'
    			},
    			labelStyle: {
    				left: '-130px'
    			}
    		});
    		me.comefromFld = new TextBox({
    			label: '籍贯',
    			style: {
    				marginLeft: '100px'
    			},
    			labelStyle: {
    				left: '-130px'
   			}
    		});
    		me.addressFld = new TextBox({
    			label: '收件地址',
    			style: {
    				marginLeft: '100px'
    			},
    			labelStyle: {
                    left: '-130px'
    			},
                id : "address_po"
    		});
            me.addressIntroductionFld = new DisplayBox({
                inputWidth: 260,
                value: "<div style='font-weight:normal;font-size:6px;width:200px'>(地址、收件人及联系方式填写尽量完整，方便礼品寄送)<div>",
                style: {
                    marginTop: '-20px'
                }
            });
    		me.occupationFld = new TextBox({
    			label: '职业',
                validates: [{
                    pattern: function() {
                        return this.get('value').length <= 10;
                    },
                    message: '字符个数不能超过10'
                }],
    			style: {
    				marginLeft: '100px'
    			},
    			labelStyle: {
    				left: '-130px'
    			}
    		});
    		me.educationFld = new ComboBox({
    			label: '教育程度',
    			searchAttr: 'dictItemCode',
    			labelAttr: 'dictItemName',
    			store: new ComboStore({
    				url: Global.baseUrl + '/dict',
    				requestMethod: 'get'
    			}),
    			query: {
					'code': 'EDUCATION'
				},
    			editable: false,
    			style: {
    				marginLeft: '100px'
    			}
    		});
    		//******************************//
    		me.inveExperienceFld = new ComboBox({
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
    			}
    		});
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
    		
    		me.nickNameFld.placeAt(me.formLeftNode);
    		me.comefromFld.placeAt(me.formRightNode);
            me.addressFld.placeAt(me.formLeftNode);
            me.occupationFld.placeAt(me.formRightNode);
            me.addressIntroductionFld.placeAt(me.formLeftNode);
            me.educationFld.placeAt(me.formLeftNode);
            me.inveExperienceFld.placeAt(me.formRightNode);

            //var addressIntroductionCtn = domConstruct.toDom('<div><label for="'+me.addressFld.get('id')+'" style="font-size: 14px; color: #666666; margin-left: 48px;">地址：</label></div>');
            //domConstruct.place(me.addressIntroductionFld.domNode, addressIntroductionCtn);
        },

        isValid: function() {
            return this.nickNameFld.checkValidity() &&
                    this.comefromFld.checkValidity() &&
                    this.addressFld.checkValidity() &&
                    this.occupationFld.checkValidity() &&
                    this.educationFld.checkValidity() &&
                    this.inveExperienceFld.checkValidity();
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