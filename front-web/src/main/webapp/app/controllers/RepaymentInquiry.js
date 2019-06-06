define([
    'app/controllers/Helper',
	'dojo/_base/fx', 
	'dijit/registry',
	'dojo/dom',
	'dojo/on',
	'app/common/Ajax',
	'dojo/query',
	'app/common/Global',
	'dojo/dom-class',
	'dojo/cookie',
	'app/ux/GenericGrid',
	'app/stores/GridStore',
	'app/ux/GenericDateBox',
	'app/common/Date',
	'app/ux/GenericDateRange',
	'app/ux/GenericDateQuickSelect',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, cookie, Grid, Store, DateTextBox, DateUtil, DateRange, DateQuickSelect) {
	
	var config = {};
	var navBarItems = query('.subnav-item', 'subheader'),
		topbarUsername = dom.byId('topusername'),
		sideBarItems = query('.list-lside-item', 'sidebar'),
		logoutBtn = dom.byId('logout'),
		grid, gridCfg = {}, gridLayout, dateRange,
		inquiryBtn = dom.byId('inquirybutton'),
		dateQuickSelect;
	
	function initView() {
		// request data by ajax, render data
		domClass.add(navBarItems[2].childNodes[0], 'active');
		domClass.add(sideBarItems[2].childNodes[0], 'active');
		
		
		var formatAction = function(value, rowIndex, column) {
			return '<a>'+value+'</a>';
		};
		var typeMap = {'1': '普通', '2': '打新'};
		var formatType = function(value, rowIndex, column) {
			return typeMap[value];
		};
		var formatRatio = function(value, rowIndex, column) {
			return ((1 / (1 + value)) * 100).toFixed(2) + '%';
		};
		var formatAmount = function(value, rowIndex, column) {
			return '<b>'+Global.formatAmount(value / 100)+'</b>';
		};
		var modeMap = {'1': '按月结息'};
		var formatMode = function(value, rowIndex, column) {
			return modeMap[value];
		};
		var formatRate = function(value, rowIndex, column) {
			return value * 100 + '%';
		};
		var statusMap = {'0': '还款现申请', '1': '登记成功', '2': '还款中', '3': '还款成功', '4': '还款失败'};
		var formatStatus = function(value, rowIndex, column) {
			return statusMap[value];
		};
		
		gridLayout = [[
  		    {'name': '还款时间', 'field': 'create_datetime_label', styles: 'text-align: center;', noresize: true, width: '14%'},
  		    {'name': '还款金额（元）', 'field': 'repay_principal_amount', styles: 'text-align: right;', noresize: true, width: '20%', formatter: formatAmount},
  		    {'name': '还款利息（元）', 'field': 'repay_interest_amount', styles: 'text-align: right;', noresize: true, width: '20%', formatter: formatAmount},
  		    {'name': '还款状态', 'field': 'order_status', styles: 'text-align: center;', noresize: true, width: '15%', formatter: formatStatus},
  		    {'name': '合约号', 'field': 'contract_no', styles: 'text-align: center;', noresize: true, width: '20%', formatter: formatAction	}
  		]];
		
		var store = new Store({
	        target: Global.baseUrl + '/financing/repay/query',
	        allowNoTrailingSlash: true
		});
		
		gridCfg = {
			store: store,
			structure: gridLayout,
			pageSize: 5
		};
		
		grid = new Grid(gridCfg);
		on(grid, 'CellClick', function(e) {
			if (e.cell.field === 'contract_no') {
				//location.href = Global.baseUrl + '/financing/contract.htm?contract='+e.cell.grid.getItem(e.rowIndex)[e.cell.field];
				window.open(Global.baseUrl + '/financing/contract.htm?contract='+e.cell.grid.getItem(e.rowIndex)[e.cell.field]);
			}
		});
		grid.setQuery({
			'start_date': DateUtil.format(new Date()),
			'end_date': DateUtil.format(new Date())
		});
		store.grid = grid;
		grid.placeAt('grid_f');
		grid.startup();
		
		Global.focusText();
		
		dateRange = new DateRange({
			style: {
				marginLeft: '90px'
			},
			startDate: new Date(),
			endDate: new Date()
		});
		dateRange.placeAt('daterangectn');
		
		// add date quick select
		dateQuickSelect = new DateQuickSelect({
			onClick: function() {
				var data = dateQuickSelect.getData();
				dateRange.setValues({
					startDate: DateUtil.parse(data.start_date),
					endDate: DateUtil.parse(data.end_date)
				});
				grid.setQuery(data);
			}
		});
		dateQuickSelect.placeAt('datequickselectctn');
	}
	
	function addListeners() {
		
		on(inquiryBtn, 'click', function() {
			grid.setQuery(dateRange.getData());
		});
	}
	
	return {
		init: function() {
			initView();
			Helper.init(config);
			addListeners();
		}
	}
});