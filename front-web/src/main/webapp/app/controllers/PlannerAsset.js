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
	'app/stores/ClientGridStore',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie, Grid, ClientGridStore) {
	
	var config = {};
	var navBarItems = query('.subnav-item', 'subheader'),
		topbarUsername = dom.byId('topusername'),
		sideBarItems = query('.list-lside-item', 'sidebar'),
		logoutBtn = dom.byId('logout'),
		grid,
		assetTotalValueEl = dom.byId('asset_total_value'),
		curAmountEl = dom.byId('cur_amount'),
		stockAssetEl = dom.byId('stock_asset'),
		availableAmountEl = dom.byId('available_amount');
	
	function initView() {
		
		domClass.add(navBarItems[4].childNodes[0], 'active');
		domClass.add(sideBarItems[0].childNodes[0], 'active');
		var store;
		Ajax.post(Global.baseUrl + '/account/homsfatheraccount/detail', {
		}).then(function(response) {
			if (response.success) {
				assetTotalValueEl.innerHTML = Global.formatAmount(response.data.fatherAccount.asset_total_value);
				curAmountEl.innerHTML = Global.formatAmount(response.data.fatherAccount.cur_amount);
				stockAssetEl.innerHTML = Global.formatAmount(response.data.fatherAccount.stock_asset);
				availableAmountEl.innerHTML = Global.formatAmount(response.data.fatherAccount.available_amount);
				store = new ClientGridStore({
					data: {
		                items: response.data.fathercombostock
		            }
				});
				grid.setStore(store);
			} else {
				// fix the height
				grid.viewsNode.style.height = '1px';
				var info = response.exceptions[0]['info'];
				grid._isLoading = false;
				grid._isLoaded = true;
				grid.showMessage('<span><i class="fa" style="color: #f0700c;">&#xf05a;</i> '+info+'</span>');
			}
		});
		
		var formatAction = function(value, rowIndex, column) {
			return '<a>买入<i class="fa am-left-5" style="color: #f0700c">&#xf107;</i></a>';
		};
		
		var formatAmount = function(value, rowIndex, column) {
			return '<b>'+Global.formatAmount(value)+'</b>';
		};
		
		var gridLayout = [[
		    {'name': '证券代码', 'field': 'stock_code', styles: 'text-align: center;', noresize: true, width: '15%'},
		    {'name': '证券名称', 'field': 'stock_name', styles: 'text-align: center;', noresize: true, width: '15%'},
		    {'name': '当前数量', 'field': 'current_amount', styles: 'text-align: right;', noresize: true, width: '15%'},
		    {'name': '可用数量', 'field': 'enable_amount', styles: 'text-align: right;', noresize: true, width: '15%'},
		    {'name': '当前成本（元）', 'field': 'cost_balance', styles: 'text-align: right;', noresize: true, width: '15%',formatter:formatAmount},
		    {'name': '最新市值（元）', 'field': 'market_value', styles: 'text-align: right;', noresize: true, width: '15%',formatter:formatAmount}
		]];
		
		var gridCfg = {
			//store: store,
			structure: gridLayout,
			gridTitle: '我的持仓',
			pageSize: 5,
			actionButtons: [{
				text: '买入',
				handler: function(row) {
					window.open(Global.baseUrl + '/stock/buy.htm?code=' + row.stock_code);
				}
			}, {
				text: '卖出',
				handler: function(row) {
					window.open(Global.baseUrl + '/stock/sell.htm?code=' + row.stock_code);
				}
			}]
		};
		var grid = new Grid(gridCfg, 'grid');
		//grid.placeAt('grid');
		grid.startup();
	}
	
	function addListeners() {
	}
	
	return {
		init: function() {
			initView();
			Helper.init(config);
		}
	}
});