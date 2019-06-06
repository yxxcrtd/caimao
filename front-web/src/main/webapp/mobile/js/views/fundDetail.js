$(function() {
	var container = {};
	var basePath = $("#basePath").val();
	//合约详情
	container.contractDetail = basePath + "/financing/contract/detail";
	// 还款记录查询
	container.repayList = basePath + "/repay/page";
	
	$.commAjax({
		url : container.contractDetail,
		type : "get",
		data : {
			contract_no:container.contract_no
		},
		success : function(data2) {

			if (data2.success) {
				// 请求成功更新页面信息
				container.contractDetail = data2.data;
				
				
			} else {
				// 请 求不成功，跳转错误页面
				alert(data2.msg);
			}
		}

	});
	
	$.commAjax({
		url : container.repayList,
		type : "get",
		data : {
			contract_no:container.contract_no
			
		},
		success : function(data2) {

			if (data2.success) {
				// 请求成功更新页面信息
				container.contractDetail = data2.data;
				var len = data2.data.length;
				var listContent="";
				for(var i=0;i<len;i++){
					listContent +="<tr>";
					listContent +="<td class='tb-left'>2014-09-09  12：45</td>";
					listContent +="<td class='tb-center'>1000.00元</td>";
					listContent +="<td class='tb-right'>已支付</td>";					
					listContent +="</tr>";
				}
				$('#replyList').html(listContent);
				
				
			} else {
				// 请 求不成功，跳转错误页面
				alert(data2.msg);
			}
		}

	});
});