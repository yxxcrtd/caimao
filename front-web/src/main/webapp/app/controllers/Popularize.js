define([
	'app/controllers/Helper',
	'dojo/_base/fx',
	'dijit/registry',
	'dojo/dom',
	'dojo/on',
	'app/common/Ajax',
	'dojo/query',
	'app/common/Global',
	'dojo/dom-style',
	'dojo/dom-class',
	'dojo/dom-attr',
	'dojo/dom-construct',
	'dojo/_base/lang',
	'dojo/mouse',
	'dijit/focus',
	'dojo/cookie',
	'app/ux/GenericDateRange',
	'app/ux/GenericDateQuickSelect',
	'app/ux/GenericPaginationBox',
	'app/views/message/MessagePanel',
	'app/common/Date',
	'app/common/Data',
	'app/stores/GridStore',
	'app/ux/GenericGrid',
	'dojo/date/stamp',
	'dojo/when',
	'app/ux/GenericButton',
	'app/ux/GenericMsgBox',
	'app/views/common/SideMenu',
	'app/views/financing/ScoreWindow',
	'app/common/Dict',
	'app/jquery/ZeroClipboard',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domStyle, domClass, domAttr,
			domConstruct,lang, mouse, focusUtil, cookie, DateRange, DateQuickSelect,
			PaginationBox, MessagePanel, DateUtil, Data,Store,Grid,stamp,when,
			Button,MsgBox,SideMenu,ScoreWindow, Dict) {

	var config = {},scoreWindow, userData,dateRange,dateQuickSelect,gridLayout,status;
	var inquiryBtn = dom.byId('inquirybutton'),
		filterList = dom.byId('filterlist'),
		image = dom.byId('image'),
		typeItems = query('li',filterList),
		picClip;
	var refUserId = '',fundGrid2, fundGrid3;

	function initView() {
		//左边导航栏
		var sideMenu = new SideMenu({
			active: 6
		});
		sideMenu.placeAt('sidemenuctn');

		var tab = dom.byId('tab');
		var panels =  query('.panel',tab);
		var spans = query('.head span',tab);
		var s_id = 0;
		spans.forEach(function(item,i){
			on(item,'click',function(){
				tabView(item,i);
			});


		});
		function tabView(item,i){
			domClass.remove(spans[s_id],'select');
			domClass.add(item,'select');
			domStyle.set(panels[s_id],'display','none');
			domStyle.set(panels[i],'display','');
			s_id = i;
			showGrid();
			showGrid2();
			showGrid3();

			/*if (i == 0) {
			 ZeroClipboard.copy('copyButton', dom.byId('popularTextUrl').value);
			 picClip = ZeroClipboard.copy('copy2Button', dom.byId('popularPicUrl').value);
			 }*/
		}
		//复制
		copyBtn = new Button({
			label: '复制',
			width: 100,
			style: {
				marginLeft: '30px'
			},
			onClick: function() {
				//alert("已复制到剪切板");
			}
		},'copyButton');

		copy2Btn = new Button({
			label: '复制链接',
			width: 100,
			style: {
				marginLeft: '30px'
			},
			onClick: function() {
				//alert("已复制到剪切板");
			}
		},'copy2Button');

		//兑现返现
		copyBtn = new Button({
			label: '兑现返现',
			enter: true,
			width: 100,
			style: {
				marginTop: '15px',
				marginRight: '0',
				marginBottom: '0',
				marginLeft: '0'
			},
			onClick: function() {
				if(0 == userData.userScore){
					alert("积分是0，无法兑换。");
					return;
				}
				scoreWindow = new ScoreWindow({
					width: 490
				});
				on(scoreWindow.returnNode, 'click', function(e) {
					on.emit(query('.tab span')[2], 'click', {
						bubbles: true,
						cancelable: true
					});
					scoreWindow.close();
				});
				scoreWindow.placeAt(document.body);
				scoreWindow.show();
				scoreWindow.set('score',userData.userScore);
			}
		},'makeGood');


		//获取用户信息
		when(Data.getUser(),function(user){
			if(!user){
				alert('获取用户信息失败。');
			}
			userData = user;
			var userId = user.userId;
			refUserId = user.userId;
			var popularTextUrl = location.protocol + '//' + location.host + Global.baseUrl + "/user/register.htm" + "?u=" + userId;
			//推荐URL
			//dom.byId("popularTextUrl").value = popularTextUrl;
			//用户积分
			dom.byId("userScore").innerHTML = user.userScore;
			//推荐人个数
			dom.byId("refUser").innerHTML = user.count || 0;
			//************2015-03-13 13:41 update  zhangguangling  Start******************//
			Ajax.get(Global.baseUrl + "/financing/getLoanUserCount",{
					'userId':userId
				}
			).then(function(response){
					if(response.success){
						//接贷人个数
						dom.byId("jiedai").innerHTML = response.data.count || 0;
					}else{
						alert(response.msg);
					}

				});
			//************2015-03-13 13:41 update zhangguangling End******************//
		});

		Ajax.get(Global.baseUrl + "/user/score/page",{
			status:'1',
			start:0,
			limit:1000
		}).then(function(response){
			if(response.data){
				dom.byId("duixian").innerHTML = Global.formatAmount(Global.sum(response.data.items || [],"scoreToMoney"));
			}else{
				alert(response.msg);
			}

		});

		Ajax.get(Global.baseUrl + "/activity/getInviteInfo").then(function(response){
			if(response.data && response.data != '用户不存在'){
				dom.byId("regCnt").innerHTML = response.data.regCnt;
				dom.byId("pzCnt").innerHTML = response.data.pzCnt;
				dom.byId("pzAmount").innerHTML = response.data.pzAmount / 100;
				dom.byId("interestAll").innerHTML = response.data.interestAmount / 100;
				dom.byId("returnLevel").innerHTML = "V" + response.data.commissionLevel;
				dom.byId("returnLevel2").innerHTML = "V" + response.data.commissionLevel;
				dom.byId("returnRate").innerHTML = parseInt(response.data.commissionRate * 10000) / 100 + "%";
				dom.byId("returnTotal").innerHTML = response.data.commissionTotal;
				dom.byId("returnInvite").innerHTML = response.data.inviteTotal;
				dom.byId("returnAll").innerHTML = response.data.commissionTotal + response.data.inviteTotal;
			}
		});

		Ajax.get(Global.baseUrl + "/specialInterface/getCaimaoId").then(function(response){
			if(response.data){
				dom.byId("popularTextUrl").value = location.protocol + '//' + location.host + Global.baseUrl + "?i=" + response.data;
				dom.byId("popularTextUrl2").value = location.protocol + '//' + location.host + Global.baseUrl + "?i=" + response.data;
				dom.byId("copy_url").setAttribute("data-clipboard-text", location.protocol + '//' + location.host + Global.baseUrl + "?i=" + response.data);
			}
		});

		function showGrid2() {
			if (fundGrid2) {
				fundGrid2.setQuery({
					'refUserId': refUserId
				});
			}
		}

		function showGrid3() {
			if (fundGrid3) {
				fundGrid3.setQuery({
					'userId': refUserId
				});
			}
		}

		//***************兑现记录  start***********************//
		function showGrid() {
			var data = dateQuickSelect.getData();
			if (fundGrid) {
				fundGrid.setQuery({
					start_date: data.start_date,
					end_date: data.end_date,
					//默认的是待审核
					status: status || '0'
				});
			}
		}
		on(inquiryBtn, 'click', function() {
			var data = dateRange.getData();
			fundGrid.setQuery({
				start_date: data.start_date,
				end_date: data.end_date,
				status: status || '0'
			});
		});

		on(filterList, 'li:click', function(e) {
			status = domAttr.get(this, 'data-type');
			var activeEl = query('.active', filterList)[0];
			domClass.remove(activeEl, 'active');
			activeEl = query('a', this)[0];
			domClass.add(activeEl, 'active');
			var data = dateRange.getData();
			fundGrid.setQuery({
				start_date: data.start_date,
				end_date: data.end_date,
				status: status || '1'
			});
		});

		//显示图片和链接
		function show(node){
			var nextNode = node.nextSibling || node.nextElementSibling;
			//image.src = lang.trim(nextNode.innerHTML.split("*")[0] + '_' + nextNode.innerHTML.split("*")[1]) + '.jpg';
			when(Data.getUser(), function(user) {
				var popularTextUrl = location.origin + Global.baseUrl + "/user/register.htm" + "?u=" + user.userId;
				dom.byId("popularPicUrl").value = "<a href='"+popularTextUrl+"'><img src='" + image.src + "' alt=''/></a>";
				if (picClip) {
					picClip.setText(dom.byId("popularPicUrl").value);
				}
			});
		};

		//默认图片和链接
		//show(dom.byId('input_first'));

		//点击radio切换图片和链接
		//on(document,'input[type=radio]:click',function(e) {
		//    show(this);
		//});

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
					//默认的是待审核
					status: status || '0'
				});
			}
		});

		dateRange.placeAt('daterangectn');
		dateQuickSelect.placeAt('datequickselectctn');

		var formatAmount = function(value, rowIndex, column) {
			return '<b>'+Global.formatAmount(value,2)+'</b>';
		};
		var formatStatus = function(value, rowIndex, column) {
			return Dict.get('verifyStatus')[parseInt(value)];
		};

		gridLayout = [[
			{'name': '申请日期', 'field': 'createDatetime', 'width': '25%', styles: 'text-align: center;', noresize: true},
			{'name': '返现日期', 'field': 'verifyDatetime', 'width': '25%', styles: 'text-align: center;', noresize: true},
			{'name': '兑换积分', 'field': 'transAmount', 'width': '15%', styles: 'text-align: center;', noresize: true},
			{'name': '返现金额（元）', 'field': 'scoreToMoney', 'width': '15%', styles: 'text-align: right;', noresize: true,formatter: formatAmount},
			{'name': '状态', 'field': 'verifyStatus', 'width': '20%', styles: 'text-align: center;', noresize: true, formatter: formatStatus}
		]];

		var store = new Store({
			target: Global.baseUrl + '/user/score/page',
			allowNoTrailingSlash: true
		});
		var gridCfg = {
			store: store,
			structure: gridLayout,
			pageSize: 5
		};
		fundGrid = new Grid(gridCfg);
		fundGrid.setQuery({
			'status': '0',
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


		//******************推广用户  start***************************//
		when(Data.getUser(),function(user){
			if(!user){
				alert('获取用户信息失败。');
			}
			userData = user;
			refUserId = user.userId;
			var gridLayout = [[
				{'name': '姓名', 'field': 'userRealName', 'width': '25%', styles: 'text-align: center;', noresize: true},
				{'name': '手机号', 'field': 'mobile', 'width': '25%', styles: 'text-align: center;', noresize: true}
			]];

			var store = new Store({
				target: Global.baseUrl + '/user/queryRefUser',
				allowNoTrailingSlash: true
			});
			var gridCfg = {
				store: store,
				structure: gridLayout,
				pageSize: 5
			};
			fundGrid2 = new Grid(gridCfg);
			fundGrid2.setQuery({
				'refUserId': refUserId
			});
			store.grid = fundGrid;
			fundGrid2.placeAt('promotedUser');
			fundGrid2.startup();
			//*****************推广用户  start****************************//
		});

		function formatDatex(value, rowIndex, column) {
			return new Date(parseInt(value) * 1000).toLocaleString();
		}

		//******************返佣列表  start***************************//
		when(Data.getUser(),function(user){
			if(!user){
				alert('获取用户信息失败。');
			}
			userData = user;
			refUserId = user.userId;
			var gridLayout = [[
				{'name': '积分数量', 'field': 'returnAmount', 'width': '25%', styles: 'text-align: center;', noresize: true},
				{'name': '时间 ', 'field': 'created', 'width': '25%', styles: 'text-align: center;', noresize: true, formatter: formatDatex}
			]];

			var store = new Store({
				target: Global.baseUrl + '/user/queryReturnHistory',
				allowNoTrailingSlash: true
			});
			var gridCfg = {
				store: store,
				structure: gridLayout,
				pageSize: 5
			};
			fundGrid3 = new Grid(gridCfg);
			fundGrid3.setQuery({
				'userId': refUserId
			});
			store.grid = fundGrid;
			fundGrid3.placeAt('returnInterest');
			fundGrid3.startup();
		});
	}
	//初始化
	return {
		init: function() {
			initView();
			Helper.init(config);
		}
	};
});