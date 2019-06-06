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
    'app/common/Dict',
    'app/common/Date',
    'app/ux/GenericGrid',
    'app/stores/GridStore',
    'app/common/Fx',
    'dojo/text!./templates/ContractExtendPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global,
            query, Button, dom, domConstruct, domClass, on,Dict, DateUtil, Grid, Store, Fx, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {

        baseUrl: Global.baseUrl,
        templateString: template,
        title: '展期信息',

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
               {'name': '申请时间', 'field': 'applyDatetime', 'width': '10%', styles: 'text-align: center;', noresize: true},
	           {'name': '开始日期', 'field': 'contractBeginDate', 'width': '10%', styles: 'text-align: center;', noresize: true},
	           {'name': '结束日期', 'field': 'contractEndDate', 'width': '10%', styles: 'text-align: center;', noresize: true},
	           {'name': '展期天数（天）', 'field': 'prodTerm', 'width': '10%', styles: 'text-align: center;', noresize: true},
	           {'name': '月利率（%）', 'field': 'interestRate', 'width': '10%', styles: 'text-align: right;', noresize: true,formatter: formatInterestRate},
	           {'name': '状态', 'field': 'verifyStatus', 'width': '10%', styles: 'text-align: center;', noresize: true,formatter: formatVerifyStatus}
            ]];

            var store = new Store({
                target: Global.baseUrl + '/financing/defered/page',
                allowNoTrailingSlash: true
            });
            var gridCfg = {
                store: store,
                structure: gridLayout,
                pageSize: 5
            };
            var grid = new Grid(gridCfg);
            grid.setQuery({
                'rel_contract_no': contractNo,
                'start_date': '2000-01-01',
                'end_date': '3016-01-01'
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
    //审核状态转义
    function formatVerifyStatus(value, rowIndex, column){
    	return Dict.get('verifyStatus')[parseInt(value)];
	}
    //月利率转换成百分比
    function formatInterestRate(value){
    	return parseFloat(value * 100).toFixed(3);
    }
});