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
	'dojo/dom-attr',
	'dojo/cookie',
	'app/ux/GenericGrid',
	'app/stores/GridStore',
	'app/ux/GenericDateBox',
	'app/common/Date',
	'app/ux/GenericDateRange',
	'app/ux/GenericDateQuickSelect',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, domAttr,
		cookie, Grid, Store, DateTextBox, DateUtil, DateRange, DateQuickSelect) {
	
	var config = {};
	var navBarItems = query('.subnav-item', 'subheader'),
		topbarUsername = dom.byId('topusername'),
		sideBarItems = query('.list-lside-item', 'sidebar'),
		logoutBtn = dom.byId('logout'),
		grid, gridCfg = {}, gridLayout, dateRange,
		inquiryBtn = dom.byId('inquirybutton'),
		dateQuickSelect,
		filter = dom.byId('filterlist');
	
	function getFilter() {
		return domAttr.get(query('.active', filter)[0].parentNode, 'data-type');
	}
	
	function initView() {
		// request data by ajax, render data
		domClass.add(navBarItems[2].childNodes[0], 'active');
		domClass.add(sideBarItems[3].childNodes[0], 'active');
		
		var formatAction = function(value, rowIndex, column) {
			return '<a target="_blank" href="'+Global.baseUrl + '/financing/contract.htm?hiscontract='+value+'">'+value+'</a>';
		};
		var formatRatio = function(value, rowIndex, column) {
			return ((1 / (1 + value)) * 100).toFixed(2) + '%';
		};
		var formatAmount = function(value, rowIndex, column) {
			return '<b>'+Global.formatAmount(value)+'</b>';
		};
		
		gridLayout = [[
  		    {'name': '申请时间', 'field': 'apply_datetime', styles: 'text-align: center;', noresize: true, width: '15%'},
  		    {'name': '申请业务', 'field': 'loan_apply_action_name', styles: 'text-align: center;', noresize: true, width: '12%'},
  		    {'name': '审核时间', 'field': 'verify_datetime', styles: 'text-align: center;', noresize: true, width: '15%'},
  		    {'name': '订单金额（元）', 'field': 'order_amount', styles: 'text-align: right;', noresize: true, width: '18%', formatter: formatAmount},
  		    {'name': '状态', 'field': 'order_status_name', styles: 'text-align: center;', noresize: true, width: '10%'},
  		    {'name': '申请单号', 'field': 'order_no', styles: 'text-align: center;', noresize: true, width: '20%'}
  		]];
		
		var store = new Store({
	        target: Global.baseUrl + '/financing/loanapply/list',
	        allowNoTrailingSlash: true
		});
		
		gridCfg = {
			store: store,
			structure: gridLayout,
			pageSize: 5
		};
		
		grid = new Grid(gridCfg);

		grid.setQuery({
			'apply_datetime_start': DateUtil.format(DateUtil.add(new Date(), 'day', -7)),
			'apply_datetime_end': DateUtil.format(new Date()),
			'verify_status': getFilter()
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
				grid.setQuery({
					'apply_datetime_start': data.start_date,
					'apply_datetime_end': data.end_date,
					'verify_status': getFilter()
				});
			},
			disableToday: true
		});
		dateQuickSelect.placeAt('datequickselectctn');
	}
	
	function addListeners() {
		
		on(inquiryBtn, 'click', function() {
			var data = dateRange.getData();
			grid.setQuery({
				'apply_datetime_start': data.start_date,
				'apply_datetime_end': data.end_date,
				'verify_status': getFilter()
			});
		});
		
		on(filter, 'li:click', function() {
			query('.active', filter).removeClass('active');
			query('a', this).addClass('active');
			var data = dateRange.getData();
			grid.setQuery({
				'apply_datetime_start': data.start_date,
				'apply_datetime_end': data.end_date,
				'verify_status': getFilter()
			});
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