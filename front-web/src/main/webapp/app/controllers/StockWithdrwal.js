define([
        "dojo/dom-class",
        'app/controllers/Helper',
        "dojo/_base/window",
        "dojo/dom",
        "dojo/on",
    	'app/common/Dict',
        "app/common/Ajax",
        "app/common/Global",
        'app/ux/GenericGrid',
        'app/stores/GridStore',
        "dojo/query",
        'dojo/dom-construct',
        'app/ux/GenericMsgBox',
        "dojox/widget/Standby",
        'app/ux/GenericComboBox',
        "dojo/store/Memory",
        'app/views/common/TradeSideMenu',
        'app/ux/GenericTextBox',
        'app/common/RSA',
        "dojo/domReady!"
    ],
    function (domClass, Helper, win, dom, on,Dict, Ajax, Global, Grid, 
    		Store, query, domConstruct, MsgBox, Standby, ComboBox, Memory, TradeSideMenu, TextBox, RSA) {

        var entrustGrid = null;
        var accountSelect = null;
        var logoutBtn = dom.byId('logout');
        var topbarUsername = dom.byId('topusername');
        var msgBox = null;
        var config = {};
        var navBarItems = query('.subnav-item', 'subheader');
        var key, pwdFld;

        function initView() {
//		domClass.add(navBarItems[3].childNodes[0], 'active');
//		//左侧菜单
//		query("ul.list-lside li:nth-child(4) > a").addClass("active");
            //左侧导航栏
            var sideMenu = new TradeSideMenu({
                active: '1 4'
            });
            sideMenu.placeAt('sidemenuctn');
            

            //初始化交易子账号下拉框
            Ajax.get(Global.baseUrl + '/homs/combineid', {
            }).then(function (response) {
                var selectData = [];
                var value = "";
                if (response.success) {
                    var data = response.data;
                    var id, item;
                    for (var i = 0; i < data.length; i++) {
                        if (!id) {
                            id = data[i].homsCombineId;
                        }
                        item = {};
                    	item.name = data[i].operatorNo + "  " + data[i].prodName;
    			    	item.id = data[i].homsCombineId;
    			    	item.prodName = data[i].prodName;
                        item.homsFundAccount = data[i].homsFundAccount;
                        selectData.push(item);
                    }
                    
                    accountSelect = new ComboBox({
                        id: "accountSelect",
                        style: 'width:200px;margin-left:10px;',
                        searchAttr: 'id',
            			labelAttr: 'name',
                        editable: false,
                        //item: item,
                        onChange: function (value) {
                            if (entrustGrid) {
                                entrustGrid.setQuery({
                                    'homs_combine_id': accountSelect.value,
                                    'homs_fund_account': accountSelect.store.get(accountSelect.value).homsFundAccount
                                });
                            }
                        }
                    }, "childAccountDiv");
                    
                    var accountStore = new Memory({
                        data: selectData
                    });
                    
                    accountSelect.set('store', accountStore);
                    accountSelect.set('item', accountStore.data[0]);
                    accountSelect.startup();
                    
                    queryEntrust(id);
                }

            });
        }

        function queryEntrust(v_fundaccount) {
        	function formatEntrustStatus(value, rowIndex, column){
        		return Dict.get('entrustStatus')[parseInt(value)];
        	}
        	function formatEntrustDirection(value, rowIndex, column){
        		return Dict.get('entrustDirection')[parseInt(value)];
        	}
            var gridLayout = [
                [
                    {'name': '委托编号', 'field': 'entrustNo', styles: 'text-align: center;', noresize: true, width: '8%'},
                    {'name': '委托时间', 'field': 'entrustTime', styles: 'text-align: center;', noresize: true, width: '8%', formatter: formatTime},
                    {'name': '证券代码', 'field': 'stockCode', styles: 'text-align: center;', noresize: true, width: '8%'},
                    {'name': '证券名称', 'field': 'stockName', styles: 'text-align: center;', noresize: true, width: '8%'},
                    {'name': '委托状态', 'field': 'entrustStatus', styles: 'text-align: center;', noresize: true, width: '8%',formatter:formatEntrustStatus},
                    {'name': '买卖方向', 'field': 'entrustDirection', styles: 'text-align: center;', noresize: true, width: '8%',formatter:formatEntrustDirection},
                    {'name': '委托价格', 'field': 'entrustPrice', styles: 'text-align: right;', noresize: true, width: '8%', formatter: formatPrice},
                    {'name': '委托数量', 'field': 'entrustAmount', styles: 'text-align: right;', noresize: true, width: '8%'},
                    {'name': '成交数量', 'field': 'businessAmount', styles: 'text-align: right;', noresize: true, width: '8%'},
                    {'name': '成交金额', 'field': 'businessBalance', styles: 'text-align: right;', noresize: true, width: '8%', formatter: formatPrice},
                    {'name': '操作', 'field': 'function', styles: 'text-align: center;', noresize: true, width: '8%', formatter: formatAction}
                ]
            ];
            var store = new Store({
                target: Global.baseUrl + '/stock/revocableentrust',
                allowNoTrailingSlash: true,
                pageSize: 100
            });

            var gridCfg = {
                store: store,
                structure: gridLayout
            };

            entrustGrid = new Grid(gridCfg);

            on(entrustGrid, 'CellClick', function (e) {
                if (e.cell.field === 'function') {
                    var entrust_no = e.grid.getItem(e.rowIndex)["entrustNo"];
                    var combine_id = e.grid.getItem(e.rowIndex)["homsCombineId"];
                    var content = "<div align=\"left\" ><b class=\"ui-text am-ft-md\" style=\"padding-left:20px;\">是否对委托号为" + entrust_no + "的委托进行撤单？</b></div>";
                    msgBox = new MsgBox({
                        title: '确认撤单',
                        confirmAndCancel: true,
                        //confirmText: comfirmContent,
                        msg: content,
                        closeAction: 'close',
                        contentClass: "con-box-transaction",
                        onConfirm: function () {
                        		var standby = new Standby({target: msgBox.domNode});
                        		document.body.appendChild(standby.domNode);
                        		standby.startup();
                        		standby.show();
                        		
                        		Ajax.post(Global.baseUrl + '/stock/withdrawal', {
                        			"homs_combine_id": combine_id,
                                    "homs_fund_account":accountSelect.store.get(accountSelect.value).homsFundAccount,
                                    "entrust_no": entrust_no
                                  /*  "trade_pwd": RSA.encryptedString(key, pwdFld.get('value'))*/
                        		}).then(function (response) {
                        			standby.hide();
                        			msgBox.close();
                        			if (response.success) {
                                        var html = '<div style="text-align: center;"><i class="ui-icon icon-middle-success"></i><span style="padding-left: 5px;font-weight: bold;font-size: 16px;vertical-align: middle;">委托成功！</span></div>';
                        				
                        				msgBox = new MsgBox({
                        					title: '确认撤单',
                        					confirmAndCancel: false,
                        					msg: html,
                        					closeAction: 'close',
                        					contentClass: "clearfix",
                        					alertClass: "box"
                        				});
                        				msgBox.placeAt(document.body);
                        				msgBox.show();
                        				//刷新表格
                        				entrustGrid.setQuery({
                        					'homs_combine_id': accountSelect.value,
                        					'homs_fund_account': accountSelect.store.get(accountSelect.value).homsFundAccount
                        				});
                        			} else {
                                        var html = '<div style="text-align: center;"><i class="ui-icon icon-middle-error"></i><span style="padding-left: 5px;font-weight: bold;font-size: 16px;vertical-align: middle;">委托失败！</span></div><div>'+response.msg+'</div>';
                        				
                        				msgBox = new MsgBox({
                        					title: '确认撤单',
                        					confirmAndCancel: false,
                        					msg: html,
                        					closeAction: 'close',
                        					contentClass: "clearfix",
                        					alertClass: "box"
                        				});
                        				msgBox.placeAt(document.body);
                        				msgBox.show();
                        			}
                        			
                        		});
                        	
                        },
                        onCancel: function () {
                            msgBox.close();
                        }
                    });
                 /*   pwdFld = new TextBox({
                        label: '安全密码',
                        validates: [{
                            pattern: /.+/,
                            message: '请输入安全密码'
                        }],
                        style: 'margin-left: 80px;margin-top: 10px;',
                        type: 'password'
                    });
                    pwdFld.placeAt(msgBox.contentNode, 3);*/
                    msgBox.placeAt(document.body);
                    msgBox.show();
                }
            });
            entrustGrid.setQuery({
                'homs_combine_id': accountSelect.value,
                'homs_fund_account': accountSelect.store.get(accountSelect.value).homsFundAccount
            });
            store.grid = entrustGrid;
            entrustGrid.placeAt('grid_f');
            entrustGrid.startup();

        }

        function addListeners() {

        }

        function formatTime(value, rowIndex, column) {
            value = value + "";
            for (var i = value.length; i < 6; i++) {
                value = "0" + value;
            }
            return value.substring(0, 2) + ":" + value.substring(2, 4) + ":" + value.substring(4, 6);
        }

        function formatPrice(value, rowIndex, column) {
            return (parseFloat(value)).toFixed(2);
        }

        function formatAction(value, rowIndex, column) {
            return '<a>撤单</a>';
        };

        return {
            init: function () {
            	Ajax.get(Global.baseUrl + '/sec/rsa', {
                }).then(function (response) {
                    if (response.success) {
                        key = RSA.getKeyPair(response.data.exponent, '', response.data.modulus);
                    } else {
                    }
                });
                initView();
                Helper.init(config);
                addListeners();
            }
        };

    });