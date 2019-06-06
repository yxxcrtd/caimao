define([ 
        "dojo/dom-class",
        'app/controllers/Helper',
         "dojo/_base/window",
        "dojo/dom",
        "dojo/on",
    	'app/common/Dict',
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
        'app/views/common/TradeSideMenu',
        'dojo/when',
        "dojo/domReady!"
        ],
function(domClass, Helper, win , dom , on ,Dict,Ajax,Global, 
		DateUtil, Grid, Store,query,ComboBox,Memory,DateRange,DateQuickSelect,TradeSideMenu, when){
	
	var entrustGrid = null;
	var historyEntrustGrid = null;
	var accountSelect = null;
	var historyDateRange = null;
	var historyDateQuickSelect = null;
	var config = {};
	var navBarItems = query('.subnav-item', 'subheader');
	
	function initView(){
		//左侧导航栏
		 sideMenu = new TradeSideMenu({
	            active: '1 5'
	        });
       sideMenu.placeAt('sidemenuctn');
		
		//初始化交易子账号下拉框
		Ajax.get(Global.baseUrl + '/homs/combineid',{			
		}).then(function(response){
			var selectData = [];
			var value = "", firstItem = {};
			if(response.success){
			    var data = response.data;
			    for(var i=0 ; i<data.length ; i++){			    	
			    	var item = {};
			    	item.name = data[i].operatorNo + "  " + data[i].prodName;
			    	item.id = data[i].homsCombineId;
			    	item.prodName = data[i].prodName;
			    	item.homsFundAccount = data[i].homsFundAccount;
			    	selectData.push(item);
			    	
			    	if(i==0){
                        firstItem = item;
			    		value = data[i].name;
			    		queryEntrust(firstItem);
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
		        item: firstItem,
		        style: 'width:200px;margin-left:10px;',
		        editable:false,
		        onChange:function(value){
		        	entrustGrid.setQuery({
			   			'homs_combine_id': accountSelect.value,
			   			'homs_fund_account':accountSelect.item.homsFundAccount
			   		});
		        }
		    }, "childAccountDiv");
		    accountSelect.startup();
		});	
		
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
		   			'start_date':data.start_date,
		   			'end_date':data.end_date,
		   			'order_field':"entrust_timestart" //无效
		   		});
			}
		});
		
		historyDateRange.placeAt('historydaterangectn');
		historyDateQuickSelect.placeAt('historydatequickselectctn');
		
		//初始化历史表
		queryHistoryEntrust();
			
	}
	
	function queryEntrust(item){
		
		var gridLayout = [[
		     		    {'name': '委托编号', 'field': 'entrustNo', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '委托时间', 'field': 'entrustTime', styles: 'text-align: center;', noresize: true, width: '10%',formatter:formatTime}, 
		     		    {'name': '证券代码', 'field': 'stockCode', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '证券名称', 'field': 'stockName', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '委托状态', 'field': 'entrustStatus', styles: 'text-align: center;', noresize: true, width: '10%',formatter:formatEntrustStatus},
		     		    {'name': '买卖方向', 'field': 'entrustDirection', styles: 'text-align: center;', noresize: true, width: '10%',formatter:formatEntrustDirection}, 
		     		    {'name': '委托价格', 'field': 'entrustPrice', styles: 'text-align: right;', noresize: true, width: '10%',formatter:formatPriceNotDivide},
		     		    {'name': '委托数量', 'field': 'entrustAmount', styles: 'text-align: right;', noresize: true, width: '10%'},
		     		    {'name': '成交数量', 'field': 'businessAmount', styles: 'text-align: right;', noresize: true, width: '10%'},
		     		    {'name': '成交金额', 'field': 'businessBalance', styles: 'text-align: right;', noresize: true, width: '10%',formatter:formatPrice}
		     		]];
		var store = new Store({
   	        target: Global.baseUrl + '/stock/child/curentrust',
   	        allowNoTrailingSlash: true
   		});
   		
   		var gridCfg = {
   			store: store,
   			structure: gridLayout
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
	
	function queryHistoryEntrust(){
		
		var gridLayout = [[
		     		    {'name': '委托编号', 'field': 'entrustNo', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '委托时间', 'field': 'entrustDate', styles: 'text-align: center;', noresize: true, width: '12%',formatter:formatDate}, 
		     		    {'name': '证券代码', 'field': 'stockCode', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '证券名称', 'field': 'stockName', styles: 'text-align: center;', noresize: true, width: '10%'},
		     		    {'name': '委托状态', 'field': 'entrustStatus', styles: 'text-align: center;', noresize: true, width: '10%',formatter:formatEntrustStatus},
		     		    {'name': '买卖方向', 'field': 'entrustDirection', styles: 'text-align: center;', noresize: true, width: '10%',formatter:formatEntrustDirection},
		     		    {'name': '委托价格', 'field': 'entrustPrice', styles: 'text-align: right;', noresize: true, width: '10%',formatter:formatPriceNotDivide},
		     		    {'name': '委托数量', 'field': 'entrustAmount', styles: 'text-align: right;', noresize: true, width: '10%'},
		     		    {'name': '成交数量', 'field': 'businessAmount', styles: 'text-align: right;', noresize: true, width: '10%'},
		     		    {'name': '成交金额', 'field': 'businessBalance', styles: 'text-align: right;', noresize: true, width: '10%',formatter:formatPrice}
		     		]];
		var store = new Store({
   	        target: Global.baseUrl + '/stock/child/hisentrust',
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
   			'start_date':data.start_date,
   			'end_date':data.end_date,
   			'order_field':"entrust_timestart" //无效
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
	   			'start_date':data.start_date,
	   			'end_date':data.end_date,
	   			'order_field':"entrust_timestart" //无效
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
	
	function formatDate(value, rowIndex, column){
		if(value.length == 8){
			var date = value.substring(0,4) + "-" + value.substring(4,6) + "-" + value.substring(6,8); 
			return date;
		}else{
			return value;
		}
	}
	
	function formatPrice(value, rowIndex, column){
		return (parseFloat(value)/100).toFixed(2);
	}
	
	function formatPriceNotDivide(value, rowIndex, column){
		return (parseFloat(value)).toFixed(2);
	}
	function formatEntrustStatus(value, rowIndex, column){
		return Dict.get('entrustStatus')[value];
	}
	function formatEntrustDirection(value, rowIndex, column){
		return Dict.get('entrustDirection')[parseInt(value)];
	}
	
	return {
		init: function() {
			initView();		
			Helper.init(config);
			addListeners();
		}
	};
	
});