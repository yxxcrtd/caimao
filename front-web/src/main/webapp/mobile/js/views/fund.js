$(function() {
	var container = {};

	// 查询操作中合约列表
	container.onworkingContractUrl = basePath + "/financing/contract/page";
	// 查询操作结束合约列表
	container.historyContractUrl = basePath + "/financing/hiscontract/page";
	// 查询申请中合约列表
	container.applyContractUrl = basePath + "/financing/loanapply/page";

	var tabActionMap = {};
	tabActionMap['操盘中'] = container.onworkingContractUrl;
	tabActionMap['已结束'] = container.historyContractUrl;
	tabActionMap['审核中'] = container.applyContractUrl;
	var productMap = {};
	productMap[1] = "月融资";
	productMap[3] = "日融资";
	productMap[2] = "实盘大赛";
	productMap[4] = "免费体验";
	// 进入 首页先调用操作中的合约
	commAjaxAction(container.onworkingContractUrl);
	// 生成合约列表页面
	function createContractList(items) {
		var len = items.length;
		var contentHtml = "";
		for ( var i = 0; i < len; i++) {
			var item = items[i];
			// 总操盘资金
			var totalAmount = parseFloat(item.cashAmount + item.loanAmount)
					.toFix(2);
			contentHtml += "<a href='infomation-3.html' class='am-ft-main'>";
			contentHtml += "<div class='contractNum bg-gray con-spacing10 clearfix'>";
			if (item.prodId == '1') {
				contentHtml += "<p class='cm-left contractNum-month'>";
				contentHtml += "<mark class='label'>月融资</mark>";
			}
			if (item.prodId == '2') {
				contentHtml += "<p class='cm-left contractNum-competition'>";
				contentHtml += "<mark class='label'>实盘大赛</mark>";
			}
			if (item.prodId == '3') {
				contentHtml += "<p class='cm-left contractNum-day'>";
				contentHtml += "<mark class='label'>日融资</mark>";
			}
			if (item.prodId == '4') {
				contentHtml += "<p class='cm-left contractNum-free'>";
				contentHtml += "<mark class='label'>免费体验</mark>";
			}
			contentHtml += "<b class='am-left-10'>" + item.contractNo
					+ "</b></p>";
			contentHtml += "<p class='cm-right'>";
			// 开始日期结束日期
			contentHtml += "<span class='am-ft-sm am-ft-gray'>"
					+ item.contractBeginDate + "<em class='bar'>|</em>"
					+ item.contractEndDate + "</span>";
			contentHtml += "</p></div><dl class='dl-moneyMain bg-white'>";
			// 总操盘资金
			contentHtml += "<dt><p><b>" + totalAmount
					+ "</b><span class='am-ft-sm'>元</span>";
			contentHtml += "</p><p class='am-pt-5'><span class='am-ft-sm am-ft-tabText'>总操盘资产</span>";
			contentHtml += "</p></dt><dt><p>";
			// 亏损警告线
			contentHtml += "<b>35,000</b><span class='am-ft-sm'>元</span>";
			contentHtml += "</p><p class='am-pt-5'><span class='am-ft-sm am-ft-tabText'>亏损警告线</span>";
			contentHtml += "</p></dt><dt><p>";
			// 亏损平仓线
			contentHtml += "<b>33,000</b><span class='am-ft-sm'>元</span>";
			contentHtml += "</p><p class='am-pt-5'><span class='am-ft-sm am-ft-tabText'>亏损平仓线</span>";
			contentHtml += "</p></dt></dl></a>";

		}
		$('#panelContent').html(contentHtml);
	}
	// 请求后台数据
	function commAjaxAction(url) {
		var data0;
		if(url==container.applyContractUrl){
			data0={
					start_date : '',
					end_date : '',
					start : 0,
					limit : 2000,
					status:'0'
				};
		}else{
			data0={
					start_date : '',
					end_date : '',
					start : 0,
					limit : 2000
				};
		}
		console.info("url"+url);
		$.commAjax({
			url : url,
			type : "get",
			data : data0,
			success : function(data2) {

				if (data2.success) {
					// 请求成功更新页面信息
					container.contractItems = data2.data.items;
					// 查询是否有合约的存在
					if (container.contractItems == null
							|| container.contractItems.length < 1) {
						$('#panel1').css({
							"display" : "block"
						});
						$('#panel2').css({
							"display" : "none"
						});

					} else {
						$('#panel1').css({
							"display" : "none"
						});
						$('#panel2').css({
							"display" : "block"
						});
						createContractList(container.contractItems);
					}

				} else {
					// 请 求不成功，跳转错误页面
					alert("ehe"+data2.msg);
				}
			}

		});
	}

	$("#tabList li").click(function() {
		console.info(333);
		var tabText = $("a", this).text();
		var tabUrl = tabActionMap[tabText];
		$("#tabList li").removeClass("active");
		$(this).addClass("active");
		// 请求事件
		commAjaxAction(tabUrl);

	});
});