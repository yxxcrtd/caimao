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
	'dojo/mouse',
	'dijit/focus',
	'dojo/cookie',
	'app/ux/GenericDateRange',
	'app/ux/GenericDateQuickSelect',
	'app/ux/GenericPaginationBox',
	'app/views/message/MessagePanel',
	'app/common/Date',
	'app/ux/GenericMsgBox',
	'app/views/common/SideMenu',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domStyle, domClass, domAttr,
		domConstruct, mouse, focusUtil, cookie, DateRange, DateQuickSelect, 
		PaginationBox, MessagePanel, DateUtil, MsgBox,SideMenu) {
	
	var config = {};
	var topbarUsername = dom.byId('topusername'),
		navBarItems = query('.subnav-item', 'subheader'),
		logoutBtn = dom.byId('logout'),
		inquiryBtn = dom.byId('inquirybtn'),
		content, dateRange, dateQuickSelect, paginationBox, messagePanel,
		data, start, limit, msgBox;
	
	function queryList(startDate, endDate, p_start, p_limit) {
		messagePanel.mask();
		var dateData = dateRange.getData();
		Ajax.post(Global.baseUrl + '/msg/list', {
			push_type: 'all',
			start_date: dateData.start_date || DateUtil.format(new Date()),
			end_date: dateData.end_date || DateUtil.format(new Date()),
			start: p_start || 0,
			limit: p_limit || 5
		}).then(function(response) {
			if (response.success) {
				messagePanel.set('items', response.data.items);
				messagePanel.unmask();
				start = p_start || 0;
				limit = p_limit || 5;
				data = response.data;
				
				response.data.firstPage = !start;

				if(response.data.pages == 0){
					response.data.lastPage = true;	
				}else{
					response.data.lastPage = response.data.start / response.data.limit + 1 == response.data.pages;
				}

                
				domClass[response.data.firstPage ? 'add' : 'remove'](paginationBox.prevBtnNode, 'genericButtonDisabled');
				domClass[response.data.lastPage ? 'add' : 'remove'](paginationBox.nextBtnNode, 'genericButtonDisabled');
			} else {
			}
		});
	}
	
	function initView() {
		  var sideMenu = new SideMenu({
	            active: 4
	        });
        sideMenu.placeAt('sidemenuctn');
		
		msgBox = new MsgBox({});
		msgBox.placeAt(document.body);
		msgBox.hide();
		
		dateRange = new DateRange({
			leftOffset: 90
		});
		dateQuickSelect = new DateQuickSelect({
			style: {
				marginTop: '2px',
				'float': 'right'
			},
			onClick: function() {
				var data = dateQuickSelect.getData();
				dateRange.setValues({
					startDate: DateUtil.parse(data.start_date),
					endDate: DateUtil.parse(data.end_date)
				});
				queryList(data.start_date, data.end_date, start, limit);
			}
		});
		paginationBox = new PaginationBox();
		messagePanel = new MessagePanel();
		messagePanel.placeAt('rightctn');
		queryList();
		
		dateRange.placeAt('daterange');
		dateQuickSelect.placeAt('filterbox');
		paginationBox.placeAt('rightctn');
	}
	
	function addListeners() {
		on(inquiryBtn, 'click', function() {
			queryList(DateUtil.format(dateRange.get('startDate')), DateUtil.format(dateRange.get('endDate')), start, limit);
		});
		
		on(paginationBox.pageSelect, 'change', function(count) {
			queryList(DateUtil.format(dateRange.get('startDate')), DateUtil.format(dateRange.get('endDate')), 0, count);
		});
		
		on(paginationBox.prevBtnNode, 'click', function() {
			if (!domClass.contains(paginationBox.prevBtnNode, 'genericButtonDisabled')) {
				queryList(DateUtil.format(dateRange.get('startDate')), DateUtil.format(dateRange.get('endDate')), start - limit, limit);
			}
		});
		
		on(paginationBox.nextBtnNode, 'click', function() {
			if (!domClass.contains(paginationBox.nextBtnNode, 'genericButtonDisabled')) {
				queryList(DateUtil.format(dateRange.get('startDate')), DateUtil.format(dateRange.get('endDate')), start + limit, limit);
			}
		});
		
		on(messagePanel, 'li:click', function() {
			var me = this;
			var msgId = parseInt(domAttr.get(me, 'data-id'));
			var type = domAttr.get(me, 'data-type');
			msgBox.set('msg', '');
			msgBox.set('title', '消息内容');
			Ajax.post(Global.baseUrl + '/msg/detail', {
				msg_no: msgId
			}).then(function(response) {
				if (response.success) {
					msgBox.set('msg', response.data.msgContent);
					messagePanel.setReaded(me);
					msgBox.show();
				} else {
					
				}
			});
		});
	}
	
	return {
		init: function() {
			initView();
			addListeners();
			Helper.init(config);
		}
	};
});