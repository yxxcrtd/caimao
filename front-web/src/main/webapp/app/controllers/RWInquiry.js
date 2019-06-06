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
	'dojo/dom-construct',
	'dojo/mouse',
	'dijit/focus',
	'dojo/cookie',
	'app/ux/GenericGrid',
	'app/stores/GridStore',
	'app/ux/GenericDateBox',
	'dojo/date/stamp',
	'app/common/Date',
	'app/ux/GenericDateRange',
	'app/ux/GenericDateQuickSelect',
	'app/ux/GenericButton',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, domAttr,
		domConstruct, mouse, focusUtil, cookie, Grid, Store, DateTextBox, stamp, 
		DateUtil, DateRange, DateQuickSelect, Button) {
	
	var config = {};
	var navBarItems = query('.subnav-item', 'subheader'),
		topbarUsername = dom.byId('topusername'),
		sideBarItems = query('.list-lside-item', 'sidebar'),
		logoutBtn = dom.byId('logout'),
		RGrid, RGridCfg = {}, RGridLayout, RStartDateEl, REndDateEl,
		RInquiryBtn = dom.byId('rechargeinquirybutton'),
		RDateFilterList = dom.byId('rechargedatefilterlist'),
		RDateItems = query('li', RDateFilterList),
		WGrid, WGridCfg = {}, WGridLayout,
		WInquiryBtn = dom.byId('withdrawinquirybutton'),
		WDateFilterList = dom.byId('withdrawdatefilterlist'),
		WDateItems = query('li', WDateFilterList),
		tab = dom.byId('table-tags'),
		rechargeDateRange, rechargeDateQuickSelect,
		withdrawDateRange, withdrawDateQuickSelect,
		rechargeFilter = dom.byId('rechargefilterlist'),
		withdrawFilter = dom.byId('withdrawfilterlist');
	
	function getFilter(el) {
		return domAttr.get(query('.active', el)[0].parentNode, 'data-type');
	}
	
	function initView() {
		
		// render button
		var rechargeBtn = new Button({
			label: '充值',
			leftOffset: 0,
			width: 60
		}, dom.byId('rechargebtn'));
		var withdrawBtn = new Button({
			label: '取现',
			leftOffset: 0,
			color: '#E2E2E2',
			hoverColor: '#EDEDED',
			textStyle: {
				color: '#666666'
			},
			innerStyle: {
				borderColor: '#C9C9C9'
			},
			width: 60
		}, dom.byId('withdrawbtn'));
		// my Account data
		Ajax.post(Global.baseUrl + '/account/query', {
		}).then(function(response) {
			if (response.success) {
				 dom.byId('avalaible_amount_label').innerHTML = Global.formatAmount(response.data.avalaible_amount);
				 dom.byId('freeze').innerHTML = Global.formatAmount(response.data.freeze_amount);
			} else {
			}
		});
		// request data by ajax, render data
		domClass.add(navBarItems[1].childNodes[0], 'active');
		domClass.add(sideBarItems[0].childNodes[0], 'active');
		
		rechargeDateRange = new DateRange({
			startDate: new Date(),
			endDate: new Date()
		});
		rechargeDateQuickSelect = new DateQuickSelect({
			onClick: function() {
				var data = rechargeDateQuickSelect.getData();
				rechargeDateRange.setValues({
					startDate: DateUtil.parse(data.start_date),
					endDate: DateUtil.parse(data.end_date)
				});
				doQuery(RGrid, data, '01', getFilter(rechargeFilter));
			}
		});
		
		rechargeDateRange.placeAt('rechargedaterangectn');
		rechargeDateQuickSelect.placeAt('rechargedatequickselectctn');
		
		withdrawDateRange = new DateRange({
			startDate: new Date(),
			endDate: new Date()
		});
		withdrawDateQuickSelect = new DateQuickSelect({
			onClick: function() {
				var data = withdrawDateQuickSelect.getData();
				withdrawDateRange.setValues({
					startDate: DateUtil.parse(data.start_date),
					endDate: DateUtil.parse(data.end_date)
				});
				doQuery(WGrid, data, '02', getFilter(withdrawFilter));
			}
		});
		
		withdrawDateRange.placeAt('withdrawdaterangectn');
		withdrawDateQuickSelect.placeAt('withdrawdatequickselectctn');
		// render grid
		
		var dateFormat = function(value, rowIndex, column) {
			return value.slice(0, 10);
		};
		var formatAmount = function(value, rowIndex, column) {
			return '<b>'+Global.formatAmount(value)+'</b>';
		};
		var statusMap = {'02': '待充值', '03':'充值成功'};
		var formatStatus = function(value, rowIndex, column) {
			return statusMap[value];
		};
		var bankMap = {'ICBC': '中国工商银行', 'ABC': '中国农业银行', 'BHB':'河北银行', 'BOC':'中国银行', 'CCB':'中国建设银行',
				'CEB': '中国光大银行', 'CIB': '兴业银行', 'CITIC': '中信银行', 'CMB': '招商银行', 'CMBC': '中国民生银行',
				'COMM': '交通银行', 'GDB': '广东发展银行', 'HXB': '华夏银行', 'PSBC': '邮政储蓄银行', 'SDB': '深圳发展银行',
				'SHB': '上海银行', 'SPDB': '上海浦东发展银行', 'BEA': '东亚银行', 'WZCB': '温州银行', 'BJBANK': '北京银行',
				'SHRCB': '上海农商银行'
			};
		var formatBank = function(value, rowIndex, column) {
			return bankMap[value];
		};
		
		RGridLayout = [[
   		    {'name': '发生时间', 'field': 'create_datetime', 'width': '20%', styles: 'text-align: center;', noresize: true},
   		    {'name': '发生金额（元）', 'field': 'order_amount', 'width': '20%', styles: 'text-align: right;', noresize: true, formatter: formatAmount},
   		    {'name': '订单编号', 'field': 'order_no', 'width': '30%', styles: 'text-align: center;', noresize: true},
   		    {'name': '状态', 'field': 'order_status', 'width': '10%', styles: 'text-align: center;', noresize: true, formatter: formatStatus},
   		    {'name': '充值方式', 'field': 'bank_no', 'width': '20%', styles: 'text-align: center;', noresize: true, formatter: formatBank}
   		]];
		
		var WStatusMap = {'02':'待审核', '03':'审核通过'};
		var WFormatStatus = function(value, rowIndex, column) {
			return WStatusMap[value];
		};
		
		WGridLayout = [[
   		    {'name': '发生时间', 'field': 'create_datetime', 'width': '20%', styles: 'text-align: center;', noresize: true},
   		    {'name': '发生金额（元）', 'field': 'order_amount', 'width': '20%', styles: 'text-align: right;', noresize: true, formatter: formatAmount},
   		    {'name': '订单编号', 'field': 'order_no', 'width': '30%', styles: 'text-align: center;', noresize: true},
   		    {'name': '状态', 'field': 'order_status', 'width': '10%', styles: 'text-align: center;', noresize: true, formatter: WFormatStatus}
   		]];
		
		store = new Store({
	        target: Global.baseUrl + '/account/chargewithdraw/page',
	        allowNoTrailingSlash: true
		});
		RGridCfg = {
			store: store,
			structure: RGridLayout,
			pageSize: 5
		};
		RGrid = new Grid(RGridCfg);
		doQuery(RGrid, {start_date: DateUtil.format(new Date()), end_date: DateUtil.format(new Date())}, '01', getFilter(rechargeFilter));
		store.grid = RGrid;
		RGrid.placeAt('rechargegrid');
		// withdraw
		store = new Store({
	        target: Global.baseUrl + '/account/chargewithdraw/page',
	        allowNoTrailingSlash: true
		});
		WGridCfg = {
			store: store,
			structure: WGridLayout,
			pageSize: 5
		};
		WGrid = new Grid(WGridCfg);
		doQuery(WGrid, {start_date: DateUtil.format(new Date()), end_date: DateUtil.format(new Date())}, '02', getFilter(withdrawFilter));
		store.grid = WGrid;
		WGrid.placeAt('withdrawgrid');
		
		// goto withdraw tab
		if(location.hash && /withdraw/.test(location.hash.slice(1))) {
			selectTag('tagContent1', document.getElementById("table-tags").getElementsByTagName("li")[1].children[0]);
			WGrid.startup();
		} else {
			RGrid.startup();
		}
	}
	
	function doQuery(grid, data, type, status) {
		grid.setQuery({
			trans_datetime_begin: data.start_date,
			trans_datetime_end: data.end_date,
			biz_type: type,
			order_status: status || '03'
		});
	}
	
	function addListeners() {
		
		on(RInquiryBtn, 'click', function() {
			doQuery(RGrid, rechargeDateRange.getData(), '01', getFilter(rechargeFilter));
		});
		
		on(WInquiryBtn, 'click', function() {
			doQuery(WGrid, withdrawDateRange.getData(), '02', getFilter(withdrawFilter));
		});
		
		on(tab, 'li:click', function() {
			WGrid.startup();
			RGrid.startup();
		});
		
		on(rechargeFilter, 'li:click', function() {
			query('.active', rechargeFilter).removeClass('active');
			query('a', this).addClass('active');
			doQuery(RGrid, rechargeDateRange.getData(), '01', getFilter(rechargeFilter));
		});
		
		on(withdrawFilter, 'li:click', function() {
			query('.active', withdrawFilter).removeClass('active');
			query('a', this).addClass('active');
			doQuery(WGrid, withdrawDateRange.getData(), '02', getFilter(withdrawFilter));
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