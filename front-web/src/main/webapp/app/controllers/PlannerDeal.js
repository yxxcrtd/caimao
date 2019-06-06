define([
        'app/controllers/Helper',
        "dijit/form/Select", 
        "dojo/_base/window",
        "dojo/dom",
        "dojo/on",
        "app/common/Ajax",
        "app/common/Global",
        'app/common/Date',
        'app/ux/GenericGrid',
        'app/stores/GridStore',
        "dojo/query",
        'app/ux/GenericComboBox',
        "dojo/store/Memory",
        'app/ux/GenericDateRange',
        'app/ux/GenericDateQuickSelect',
        "dojo/domReady!"
        ],
function(Helper,Select, win , dom , on ,Ajax,Global, DateUtil, Grid, Store,query,ComboBox,Memory,DateRange,DateQuickSelect){
	
	var entrustGrid = null;
	var accountSelect = null; 
	var historyDateRange = null;
	var historyDateQuickSelect = null;
	var config = {};
	
	function initView(){
		//左侧菜单
		query("ul.list-lside li:nth-child(3) > a").addClass("active");
		query(".subnav-item:nth-child(5) > a").addClass("active");
		
		var myDate=new Date(); 
		myDate.setDate(myDate.getDate()-7);
		historyDateRange = new DateRange({
			startDate: myDate,
			endDate: new Date()
		});
		historyDateQuickSelect = new DateQuickSelect({
			disableToday: true,
			onClick: function() {
				var data = historyDateQuickSelect.getData();
				historyDateRange.setValues({
					startDate: DateUtil.parse(data.start_date),
					endDate: DateUtil.parse(data.end_date)
				});
				historyEntrustGrid.setQuery({
		   			'business_timestart':data.start_date,
		   			'business_timeend':data.end_date,
		   			'order_field':"business_timestart" //无效
		   		});
			}
		});
		
		historyDateRange.placeAt('historydaterangectn');
		historyDateQuickSelect.placeAt('historydatequickselectctn');
		
		queryDeal();
		queryHistoryDeal();
	}
	
	function queryDeal(){
		
		gridLayout = [[
		     		    {'name': '成交编号', 'field': 'business_no', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '成交时间', 'field': 'business_time', styles: 'text-align: center;', noresize: true, width: '10%',formatter:formatTime}, 		     		     
		     		    {'name': '证券代码', 'field': 'stock_code', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '证券名称', 'field': 'stock_name', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '买卖方向', 'field': 'entrust_direction_name', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '成交价格', 'field': 'business_price', styles: 'text-align: right;', noresize: true, width: '10%',formatter:formatPriceNotDivide},
		     		    {'name': '成交数量', 'field': 'business_amount', styles: 'text-align: right;', noresize: true, width: '10%'},
		     		    {'name': '成交金额', 'field': 'business_balance', styles: 'text-align: right;', noresize: true, width: '10%',formatter: formatPrice}
		     		]];
		var store = new Store({
   	        target: Global.baseUrl + '/stock/father/curdeal',
   	        allowNoTrailingSlash: true
   		});
   		
   		gridCfg = {
   			store: store,
   			structure: gridLayout
   		};
   		
   		entrustGrid = new Grid(gridCfg);

   		store.grid = entrustGrid;
   		entrustGrid.placeAt('grid_f');
   		entrustGrid.startup();   		
						
	}
	
	function queryHistoryDeal(){
		
		var gridLayout = [[
		     		    {'name': '成交编号', 'field': 'business_no', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '成交时间', 'field': 'entrust_date', styles: 'text-align: center;', noresize: true, width: '10%',formatter:formatDate},
		     		    {'name': '证券代码', 'field': 'stock_code', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '证券名称', 'field': 'stock_name', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '委托状态', 'field': 'entrust_status_name', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '买卖方向', 'field': 'entrust_direction_name', styles: 'text-align: center;', noresize: true, width: '10%'},		     		   
		     		    /*{'name': '成交价格', 'field': 'business_price', styles: 'text-align: right;', noresize: true, width: '10%',formatter:formatPrice},*/
		     		    {'name': '成交数量', 'field': 'business_amount', styles: 'text-align: right;', noresize: true, width: '10%'},
		     		    {'name': '成交金额', 'field': 'business_balance', styles: 'text-align: right;', noresize: true, width: '15%',formatter:formatPrice}
		     		]];
		var store = new Store({
   	        target: Global.baseUrl + '/stock/father/historydeal',
   	        allowNoTrailingSlash: true
   		});
   		
   		var gridCfg = {
   			store: store,
   			structure: gridLayout,
   			pageSize: 5
   		};
   		
   		historyEntrustGrid = new Grid(gridCfg);

   		var data = historyDateRange.getData();
   		historyEntrustGrid.setQuery({
   			'business_timestart':data.start_date,
   			'business_timeend':data.end_date,
   			'order_field':"business_timestart" //无效
   		});
   		store.grid = historyEntrustGrid;
   		historyEntrustGrid.placeAt('history_grid_f'); 						
	}
	
	function addListeners(){
		on(dom.byId("entrustLi"),"click",function(){
			// 操作标签
			var tag = document.getElementById("table-tags").getElementsByTagName("li");
			var taglength = tag.length;
			for(i=0; i<taglength; i++){
				tag[i].className = "";
			}
			this.parentNode.className = "selectTag";
			// 操作内容
			for(i=0; j=document.getElementById("tagContent"+i); i++){
				j.style.display = "none";
			}
			document.getElementById("tagContent0").style.display = "block";
			entrustGrid.startup();
		});
		
		on(dom.byId("historyEntrustLi"),"click",function(){
			// 操作标签
			var tag = document.getElementById("table-tags").getElementsByTagName("li");
			var taglength = tag.length;
			for(i=0; i<taglength; i++){
				tag[i].className = "";
			}
			this.parentNode.className = "selectTag";
			// 操作内容
			for(i=0; j=document.getElementById("tagContent"+i); i++){
				j.style.display = "none";
			}
			document.getElementById("tagContent1").style.display = "block";
			
			historyEntrustGrid.startup();
		});
		
		on(dom.byId("historyinquirybutton"),"click",function(){
			var data = historyDateRange.getData();
			historyEntrustGrid.setQuery({
	   			'business_timestart':data.start_date,
	   			'business_timeend':data.end_date,
	   			'order_field':"business_timestart" //无效
	   		});			
		});
	
	}
	
	function formatTime(value, rowIndex, column){
		value = value + "";
		for(var i=value.length; i<6 ;i++){
			value = "0" + value;
		}	
		return value.substring(0, 2) + ":" + value.substring(2, 4) + ":" + value.substring(4, 6);
	}
	
	function formatPrice(value, rowIndex, column){
		return (parseFloat(value)/100).toFixed(2);
	}
	
	function formatPriceNotDivide(value, rowIndex, column){
		return (parseFloat(value)).toFixed(2);
	}
	
	function formatDate(value, rowIndex, column){
		if(value.length == 8){
			var date = value.substring(0,4) + "-" + value.substring(4,6) + "-" + value.substring(6,8); 
			return date;
		}else{
			return value;
		}
	}
	
	return {
		init: function() {
			initView();	
			Helper.init(config);
			addListeners();
		}
	};
	
});