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
	'app/views/financing/LoanToBorrowPanel',
	'app/views/financing/LoanBorrowedPanel',
	'app/common/Date',
	'dojo/domReady!'
], function(Helper,fx, registry, dom, on, Ajax, query, Global, domClass, 
		domConstruct, mouse, focusUtil, cookie, LoanToBorrowPanel, LoanBorrowedPanel, DateUtil) {
	
	var config = {};
	var navBarItems = query('.subnav-item', 'subheader'),
		sideBarItems = query('.list-lside-item', 'sidebar'),
		topbarUsername = dom.byId('topusername'),
		logoutBtn = dom.byId('logout'),
		content;
	
	function initView() {
		// request data by ajax, render data
		domClass.add(navBarItems[2].childNodes[0], 'active');
		domClass.add(sideBarItems[0].childNodes[0], 'active');
		
		
		Ajax.post(Global.baseUrl + '/financing/contract/userid/detail', {
		}).then(function(response) {
			if (response.success) { //TODO
				var contract = response.data.contract;
				// the loan apply only exist one
				var loanApply = response.data.addLoanApply || response.data.deferedLoanApply || response.data.normalLoanApply;
				var data = contract ? contract : loanApply;
				if (data) {
					var total = contract? contract.loan_amount : loanApply.order_amount;
					content = new LoanBorrowedPanel({
						isContract: !loanApply,
						contractNo: data.contract_no,
						last_contract_no: data.last_contract_no,
						contractType: data.prod_id,
				    	status: data.contract_status || data.order_status,
				    	loanAmountLeft: total - (data.repay_amount || 0),
				    	loanAmount: total,
				    	repayAmount: (data.repay_amount || 0),
				    	unsettledInterest: (data.total_interest || 0 - data.settled_interest || 0),
				    	totalInterest: (data.total_interest || 0),
				    	accruedInterest: (data.accrued_interest || 0),
				    	settledInterest: (data.settled_interest || 0),
				    	interestRate: data.interest_day_rate,
				    	ratio: data.loan_ratio,
				    	cashAmount: data.cash_amount,
				    	endDate: data.contract_end_date,
				    	interestRate: data.interest_day_rate,
				    	dayLeft: DateUtil.difference(DateUtil.today(), DateUtil.parse(data.contract_end_date))
					});
				} else {
					content = new LoanToBorrowPanel();
					Ajax.get(Global.baseUrl + '/user/cms/mcat', {
					}).then(function(response) {
						if (response.success) {
							content.setValues({
								member: response.data.members.total_member_count,
								turnover: response.data.turnover.total_turnover
							});
						}
					});
				}
			}
			content.placeAt('contentctn');
		});
	}
	
	function addListeners() {

	}
	
	return {
		init: function() {
			initView();
			Helper.init(config);
			addListeners();
		}
	}
});