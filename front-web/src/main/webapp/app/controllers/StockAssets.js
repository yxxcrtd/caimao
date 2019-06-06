define([
	'app/controllers/Helper',
	"dojo/_base/window",
  "dojo/dom",
  "dojo/on",
  "app/common/Ajax",
  "app/common/Global",
  "dojo/dom-class",
  "dijit/Dialog",
  "dijit/form/Button",
  "dojo/store/Memory",
  'app/ux/GenericMsgBox',
  'app/ux/GenericGrid',
  'app/stores/GridStore',
  "dojo/query",
  "dojo/io-query",
  'app/ux/GenericComboBox',
  'app/views/common/TradeSideMenu',
  "dojo/domReady!"
], function(Helper,win , dom , on ,Ajax,Global, domClass ,Dialog, Button,Memory, MsgBox,Grid, Store,query,ioQuery,ComboBox,TradeSideMenu){
	
	var accountSelect = null;	
	var entrustGrid = null;
	var config = {};
	var navBarItems = query('.subnav-item', 'subheader');
	var combine, fundaccount;
	
	function initView(){
		//左侧菜单
//		query("ul.list-lside li:nth-child(1) > a").addClass("active");
//		domClass.add(navBarItems[3].childNodes[0], 'active');
		//左侧导航栏
		 sideMenu = new TradeSideMenu({
	            active: '1 1'
	        });
       sideMenu.placeAt('sidemenuctn');
		Ajax.get(Global.baseUrl + '/homs/combineid',{			
		}).then(function(response){
			var selectData = [];
			var value = "";
			if(response.success){
			    var data = response.data;
			    for(var i=0 ; i<data.length ; i++){
			    	var item = {};
			    	item.name = data[i].operatorNo + "  " + data[i].prodName;
			    	item.id = data[i].homsCombineId;
			    	item.prodName = data[i].prodName;
			    	//item.cur_amount = data[i].cur_amount;
			    	//item.assetTotalValue = data[i].asset_total_value;
			    	//item.stockAsset = data[i].stock_asset;
			    	//item.available_amount = data[i].available_amount;
			    	item.homsFundAccount = data[i].homsFundAccount;
			    	selectData.push(item);

			    	if(i==0){
			    		value = data[i].operatorNo + "  " + data[i].prodName;
			    		queryAssets(item);
			    		queryShares(item);
			    	}
			    }
			}
			
			var accountStore = new Memory({
				data:selectData
		    });
			
		    accountSelect = new ComboBox({
		        id: "accountSelect",
		        store: accountStore,
		        searchAttr: 'id',
    			labelAttr: 'name',
		        value: value ,
		        style: 'width:200px;margin-left:10px;',
		        editable:false,
		        onChange:function(value){
		        	queryAssets(accountSelect.item);
					entrustGrid.setQuery({
			   			'homs_combine_id': accountSelect.value,
			   			'homs_fund_account':accountSelect.item.homsFundAccount
			   		});
		        }
		    }, "childAccountDiv");
		    accountSelect.startup();
		});
	}
	
	function queryShares(item){
		
		gridLayout = [[
		     		    {'name': '证券代码', 'field': 'stockCode', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '证券名称', 'field': 'stockName', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '当前数量', 'field': 'currentAmount', styles: 'text-align: right;', noresize: true, width: '10%'},
		     		    {'name': '可用数量', 'field': 'enableAmount', styles: 'text-align: right;', noresize: true, width: '10%'},
		     		    {'name': '当前成本', 'field': 'costBalance', styles: 'text-align: right;', noresize: true, width: '10%',formatter:formatPrice},
		     		    {'name': '最新市值', 'field': 'marketValue', styles: 'text-align: right;', noresize: true, width: '10%',formatter:formatPrice},
		     		    {'name': '操作', action: true, styles: 'text-align: center;', noresize: true, width: '10%',formatter:formatAction}
		     		]];
		var store = new Store({
   	        target: Global.baseUrl + '/stock/child/holding',
   	        allowNoTrailingSlash: true
   		});
   		
   		gridCfg = {
   			store: store,
   			structure: gridLayout,
   			actionButtons: [{
				text: '买入',
				handler: function(row) {
					var uri = Global.baseUrl + "/trade/buy.htm";
					var query = {
					    code: row['stockCode'],
					    combine: combine,
					    fundaccount: fundaccount
					 };
					  
					  // Assemble the new uri with its query string attached.
					  var queryStr = ioQuery.objectToQuery(query);
					  uri = uri + "?" + queryStr;				  
					  window.location.href = uri;
				}
			}, {
				text: '卖出',
				handler: function(row) {
					var uri = Global.baseUrl + "/trade/sell.htm";
					var query = {
					    code: row['stockCode'],
					    combine: combine,
					    fundaccount: fundaccount
					};
					  
					// Assemble the new uri with its query string attached.
					var queryStr = ioQuery.objectToQuery(query);
					uri = uri + "?" + queryStr;
					window.location.href = uri;
				}
			}]
   		};
   		
   		entrustGrid = new Grid(gridCfg);

   		entrustGrid.setQuery({
   			'homs_combine_id': item.id,
   			'homs_fund_account':item.homsFundAccount
   		});
   		store.grid = entrustGrid;
   		entrustGrid.placeAt('grid_f');
   		entrustGrid.startup();   		
						
	}
	
	function addListeners(){

	}
	
	function queryAssets(item){
		combine = item.id;
		fundaccount = item.homsFundAccount;
		Ajax.get(Global.baseUrl + '/homs/assetsinfo',{
			"homs_fund_account":item.homsFundAccount,"homs_combine_id":item.id
		}).then(function(response){
			if(response.success){
				var dataItem = response.data;
				//资金余额
				dom.byId("cur_amount").innerHTML = (parseFloat(dataItem.currentCash)/100).toFixed(2);
				//账户总值
				dom.byId("assetTotalValue").innerHTML = (parseFloat(dataItem.totalAsset)/100).toFixed(2);
				//证券市值
				dom.byId("stockAsset").innerHTML = (parseFloat(dataItem.totalMarketValue)/100).toFixed(2);
				//可用余额
				dom.byId("available_amount").innerHTML = (parseFloat(dataItem.curAmount)/100).toFixed(2); 
			}
		});	
	} 
	
	function formatPrice(value, rowIndex, column){
		return (parseFloat(value)/100).toFixed(2);
	}
	
	function formatAction(value, rowIndex, column) {
		return '<a>买入<i class="fa am-left-5" style="color: #f0700c">&#xf107;</i></a>';
	};
	
	return {
		init: function() {
			initView();	
			Helper.init(config);
			addListeners();
		}
	};

});