define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'app/ux/GenericButton',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'app/common/Date',
    'app/views/ViewMixin',
    'dojo/text!./templates/TenderPanel3.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,
            Global, query, Button, dom, domConstruct, on, DateUtil, ViewMixin, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        tender: '',

        _setTenderAttr: function(data) {
            this.userNameNode.innerHTML = Global.encodeInfo(data.userRealName, 1, 0);
            this.userPhoneNode.innerHTML = Global.encodeInfo(data.userPhone, 3, 3);
            this.idcardNode.innerHTML = Global.encodeInfo(data.idcard, 3, 3);
            this.registerDatetimeNode.innerHTML = data.registerDatetime;
            this.loanNumberNode.innerHTML = data.loanNumber;
            this.repayNumberNode.innerHTML = data.repayNumber;
        },

    	render: function() {
    		var me = this;
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.inherited(arguments);
    	}
    	
    });
});