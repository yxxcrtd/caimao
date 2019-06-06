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
	'dojo/dom-construct',
	'dojo/mouse',
	'dijit/focus',
	'dojo/cookie',
	'app/ux/GenericGrid',
	'app/stores/GridStore',
	'app/ux/GenericDateBox',
	'dojo/date/stamp',
	'dojo/date',
	'app/common/Date',
	'dojo/dom-attr',
	'app/ux/GenericDateRange',
	'app/ux/GenericDateQuickSelect',
	'app/views/common/SideMenu',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie, Grid, Store, DateTextBox, stamp, date, 
		DateUtil, domAttr, DateRange, DateQuickSelect,SideMenu) {
	
	var config = {};
	var navBarItems = query('.subnav-item', 'subheader'),
		topbarUsername = dom.byId('topusername'),
		sideBarItems = query('.list-lside-item', 'sidebar'),
		logoutBtn = dom.byId('logout'),
		fundGrid, gridCfg = {}, gridLayout, startDateEl, endDateEl,
		inquiryBtn = dom.byId('inquirybutton'),
		filterList = dom.byId('filterlist'),
		typeItems = query('li',filterList),
		bizType,
		dateRange, dateQuickSelect,sideMenu;
	
	function initView() {
		   sideMenu = new SideMenu({
	            active: '3 3'
	        });
       sideMenu.placeAt('sidemenuctn');
		dateRange = new DateRange({
			startDate: new Date(),
			endDate: new Date()
		});
		
		dateQuickSelect = new DateQuickSelect({
			onClick: function() {
				var data = dateQuickSelect.getData();
				dateRange.setValues({
					startDate: DateUtil.parse(data.start_date),
					endDate: DateUtil.parse(data.end_date)
				});
				fundGrid.setQuery({
					start_date: data.start_date,
					end_date: data.end_date,
					biz_type: bizType || 'all'
				});
			}
		});
		
		dateRange.placeAt('daterangectn');
		dateQuickSelect.placeAt('datequickselectctn');
		
		// render grid
		
		var formatAction = function(value, rowIndex, column) {
			return '<a>详情</a>';
		};
		
		var formatAmount = function(value, rowIndex, column) {
			return '<b>'+Global.formatAmount(value,2)+'</b>';
		};
		
		gridLayout = [[
		                {'name': '单号', 'field': 'id', 'width': '23%', styles: 'text-align: center;', noresize: true},
		                {'name': '用户姓名', 'field': 'userRealName', 'width': '18%', styles: 'text-align: center;', noresize: true},
		      		    {'name': '发生时间', 'field': 'transDatetime', 'width': '22%', styles: 'text-align: center;', noresize: true},
		      		    {'name': '发生金额（元）', 'field': 'transAmount', 'width': '20%', styles: 'text-align: right;', noresize: true, formatter: formatAmount},
		      		    {'name': '变动后余额（元）', 'field': 'postAmount', 'width': '20%', styles: 'text-align: right;', noresize: true, formatter: formatAmount}
   		]];
		
		store = new Store({
	        target: Global.baseUrl + '/account/jour/page',
	        allowNoTrailingSlash: true
		});
		gridCfg = {
			store: store,
			structure: gridLayout,
			pageSize: 5
		};
		fundGrid = new Grid(gridCfg);
		fundGrid.setQuery({
			'biz_type': 'all',
			'start_date': stamp.toISOString(new Date(), {
				selector: 'date'
			}),
			'end_date': stamp.toISOString(new Date(), {
				selector: 'date'
			})
		});
		store.grid = fundGrid;
		fundGrid.placeAt('fundgrid');
		fundGrid.startup();
		
		Global.focusText();
	}
	
	function setDate(type) {
		var startDate, endDate, today = new Date();
		if (type == 0) {
			startDate = date.add(today, 'day', 0);
		}
		else if (type == 1) {
			startDate = date.add(today, 'week', -1);
		} 
		else if (type == 2) {
			startDate = date.add(today, 'month', -3);
		}
		else if (type == 3) {
			startDate = date.add(today, 'month', -6);
		}
		startDateEl.set('value', startDate);
		endDateEl.set('value', today);
	}
	
	function addListeners() {
		
		on(inquiryBtn, 'click', function() {
			var data = dateRange.getData();
			fundGrid.setQuery({
				start_date: data.start_date,
				end_date: data.end_date,
				biz_type: bizType || 'all'
			});
		});
		
		on(filterList, 'li:click', function(e) {
			bizType = domAttr.get(this, 'data-type');
			var activeEl = query('.active', filterList)[0];
			domClass.remove(activeEl, 'active');
			activeEl = query('a', this)[0];
			domClass.add(activeEl, 'active');
			var data = dateRange.getData();
			fundGrid.setQuery({
				start_date: data.start_date,
				end_date: data.end_date,
				biz_type: bizType || 'all'
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