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
		
		
//		startDateEl = new DateTextBox({
//			value: new Date(),
//			style: 'width: 120px'
//		});
//		startDateEl.placeAt('startdate');
//		endDateEl = new DateTextBox({
//			value: new Date(),
//			style: 'width: 120px'
//		});
//		endDateEl.placeAt('enddate');
		
		var formatAction = function(value, rowIndex, column) {
			return '<a target="_blank" href="'+Global.baseUrl + '/financing/contract.htm?hiscontract='+value+'">'+value+'</a>';
		};
		var typeMap = {'1': '普通', '2': '打新'};
		var formatType = function(value, rowIndex, column) {
			return typeMap[value];
		};
		var formatRatio = function(value, rowIndex, column) {
			return ((1 / (1 + value)) * 100).toFixed(2) + '%';
		};
		var formatAmount = function(value, rowIndex, column) {
			return '<b>'+Global.formatAmount(value)+'</b>';
		};
		var modeMap = {'1': '按月结息'};
		var formatMode = function(value, rowIndex, column) {
			return modeMap[value];
		};
		var formatRate = function(value, rowIndex, column) {
			return value * 100 + '%';
		};
		var statusMap = {'4': '全部还款（终止）', '5': '追加（终止）', '6':'展期（终止）'};
		var formatStatus = function(value, rowIndex, column) {
			return statusMap[value];
		};
		
		gridLayout = [[
  		    {'name': '开始日期', 'field': 'contract_begin_date', styles: 'text-align: center;', noresize: true, width: '14%'},
  		    {'name': '结束日期', 'field': 'contract_end_date', styles: 'text-align: center;', noresize: true, width: '14%'},
  		    {'name': '借款金额（元）', 'field': 'loan_amount', styles: 'text-align: right;', noresize: true, width: '15%', formatter: formatAmount},
  		    {'name': '归还本金（元）', 'field': 'repay_amount', styles: 'text-align: right;', noresize: true, width: '15%', formatter: formatAmount},
  		    {'name': '利息（元）', 'field': 'total_interest', styles: 'text-align: right;', noresize: true, width: '12%', formatter: formatAmount},
  		    {'name': '状态', 'field': 'contract_status', styles: 'text-align: center;', noresize: true, width: '15%', formatter: formatStatus},
  		    {'name': '合约号', 'field': 'contract_no', styles: 'text-align: center;', noresize: true, width: '20%', formatter: formatAction}
  		]];
		
		var store = new Store({
	        target: Global.baseUrl + '/financing/hiscontract/list',
	        allowNoTrailingSlash: true
		});
		
		gridCfg = {
			store: store,
			structure: gridLayout,
			pageSize: 5
		};
		
		grid = new Grid(gridCfg);
//		on(grid, 'CellClick', function(e) {
//			if (e.cell.field === 'contract_no') {
//				cookie('contractNo', e.cell.grid.getItem(e.rowIndex)[e.cell.field], {path: '/'});
//				window.open(Global.baseUrl + '/financing/contract.htm');
//				cookie('contractNo', null, {expires: -1, path: '/'});
//			}
//		});
		grid.setQuery({
			'start_date': DateUtil.format(DateUtil.add(new Date(), 'day', -7)),
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
			startDate: DateUtil.add(new Date(), 'day', -7),
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
			},
			disableToday: true
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