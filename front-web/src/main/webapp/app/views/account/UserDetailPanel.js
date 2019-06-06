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
    'dojo/text!./templates/UserDetailPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, domConstruct, query, Global, on, dom, string, domStyle, TextBox, DisplayBox, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	baseUrl: Global.baseUrl,
    	
    	templateString: template,
    	
    	
    	user_nick_name: '',
    	come_from:'',
    	address:'',
    	occupation: '',
    	education: '',
    	inve_experience:'',
    	
    	_setDataAttr: function(value) {
    		this.nickNameFld.set('value', value.userNickName);
    		this.comefromFld.set('value', value.userComefrom);
    		this.addressFld.set('value', value.address);
    		this.occupationFld.set('value', value.userOccupation);
    		this.educationFld.set('value', value.userEducation);
    		this.inveExperienceFld.set('value', value.invrEmpiric);
    	},
    	
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
    	
    	
    	render: function() {
    		var me = this;
    		me.nickNameFld = new DisplayBox({
    			label: '昵称',
                inputWidth: 260
    		});
    		me.comefromFld = new DisplayBox({
    			label: '籍贯',
                inputWidth: 260
    		});
    		me.addressFld = new DisplayBox({
    			label: '收件地址',
                inputWidth: 260
    		});
    		me.occupationFld = new DisplayBox({
    			label: '职业',
                inputWidth: 260
    		});
    		me.educationFld = new DisplayBox({
    			label: '教育程度',
                inputWidth: 260
    		});
    		me.inveExperienceFld = new DisplayBox({
    			label: '投资经验',
                inputWidth: 260
    		});    		
    		
    		me.nickNameFld.placeAt(me.formLeftNode);
    		me.comefromFld.placeAt(me.formRightNode);
    		me.addressFld.placeAt(me.formLeftNode);
    		me.occupationFld.placeAt(me.formRightNode);
    		me.educationFld.placeAt(me.formLeftNode);
    		me.inveExperienceFld.placeAt(me.formRightNode);
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