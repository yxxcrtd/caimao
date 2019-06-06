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
    'dojo/text!./templates/TenderPanel4.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin,
            Global, query, Button, dom, domConstruct, on, DateUtil, ViewMixin, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
    	
    	templateString: template,

        users: [],
        
        totalInfos: [],

        _setUsersAttr: function(data) {
            users = data.items || [];
            var i = 0, len = users.length,
                html = '';
            for (; i < len; i++) {
                var user = users[i];
                html += '<tr><td>'+Global.encodeInfo(user.mobile, 3, 3)+'</td><td><em>'+Global.formatAmount(user.invrAmountPay)+'</em>&nbsp;元</td><td>'+user.invrDatePay+'</td></tr>';
            }
            domConstruct.place(domConstruct.toDom(html), this.rowCtnNode);
           // this.totalAmountNode.innerHTML = Global.formatAmount(Global.sum(users, 'invrAmountPay'));
           // this.countNode.innerHTML = len;
        },
        
        _setTotalInfosAttr: function(data) {
        	totalInfos = data.items || [];
        	var len = totalInfos.length;
            this.totalAmountNode.innerHTML = Global.formatAmount(Global.sum(totalInfos, 'invrAmountPay'));
            this.countNode.innerHTML = len;
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