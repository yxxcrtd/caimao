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
    'dojo/dom-class',
    'dojo/on',
    'app/common/Date',
    'app/ux/GenericGrid',
	'app/stores/GridStore',
	'app/common/Fx',
	'dojo/text!./templates/ContractRepaymentPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, 
		query, Button, dom, domConstruct, domClass, on, DateUtil, Grid, Store, Fx, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	baseUrl: Global.baseUrl,
    	templateString: template,
        title: '还款记录',
    	
    	contractNo: '',

        _setTitleAttr: function(value) {
            this.titleNode.innerHTML = value;
        },
    	
    	render: function() {
    		var me = this;
    		var formatAmount = function(value, rowIndex, column) {
    			return '<b>'+Global.formatAmount(value)+'</b>';
    		};
    		var contractNo = me.get('contractNo');
    		var gridLayout = [[
                {'name': '支付时间', 'field': '', 'width': '20%', styles: 'text-align: center;', noresize: true},
                {'name': '支付金额（元）', 'field': '', 'width': '20%', styles: 'text-align: right;', noresize: true},
                {'name': '状态', 'field': '', 'width': '20%', styles: 'text-align: center;', noresize: true}
      		]];
   		   		
      		var store = new Store({
      	        target: Global.baseUrl + '/financing/repay/page',
      	        allowNoTrailingSlash: true
      		});
      		var gridCfg = {
      			store: store,
      			structure: gridLayout,
      			pageSize: 5
      		};
      		var grid = new Grid(gridCfg);
      		grid.setQuery({
    			'contract_no': contractNo,
                'start_date': '2000-01-01',
                'end_date': '2016-01-01'
    		});
      		store.grid = grid;
    		me.grid = grid;
    		grid.placeAt(me.gridNode);
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.render();
    		me.inherited(arguments);
    	}
    	
    });
});