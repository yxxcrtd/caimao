//
$(function() {
	var container = {};
	container.status = 1;


	// 接口地址 get
	container.invListUrl = basePath+"/p2p/payed/page";
	var tabActionMap = {};
	tabActionMap['进行中'] =1;
	tabActionMap['投标持仓'] =2;
	tabActionMap['已结束'] =3;
	
	 queryList(container.status);
	$(".listTab-3 li").click(function() {
	
		
		var tabText = $("a", this).text();
		var status = tabActionMap[tabText];
		
		
		$(".listTab-3 li").removeClass("active");
		$(this).addClass("active");
		// 请求事件
		queryList(status);
		
	});
	
	function queryList(status) {
		$.commAjax({
			url : container.invListUrl,
			type : "get",
			data : {
				status: status,
				start :0,
				limit:1000
			},
			success : function(data2) {
				if (data2.success) {
					container.invList = data2.data.items;				
					createHtml();
	
				} else {
					dialog(data2.msg);
				}
	
			}
		});
	}
function createHtml() {
	var htmlContent = "";
	if(!container.invList){
		
		
		
		htmlContent +='<div class="con-spacing15 con-prompt">';
		htmlContent +='<p><span>您暂时还没有任何投标项目</span></p>';
		htmlContent +='<div class="btn-page btn-page-center con-spacing15">';
		htmlContent +='<a href="'+mobileUrl+'/tender.htm" class="am-button am-button-red">';
		htmlContent +='<span class="am-ft-white">去赚钱</span>';
		htmlContent += '</a></div></div>';
	}else{
	var len = container.invList.length;
	
		for (var i= 0 ; i < len ; i ++ ){
			var item = container.invList[i];
			
			htmlContent +="<div class='contractNum bg-gray con-spacing10 clearfix'>";
			htmlContent +="		<p class='cm-left'>";
			htmlContent +="			<span class='am-ft-md' > <a href='"+mobileUrl+"/tenderDetail.htm?invs="+item.invsId+"' class='am-ft-md'>"+item.invsName+"</a></span>";
			htmlContent +="		</p>";
			htmlContent +="		<p class='cm-right'>";
			htmlContent +="		</p>";
			htmlContent +="	</div>";
			htmlContent +="	<aside class='con-frame con-frame-notop'>";
			htmlContent +="		<ul class='datalist'>";
			htmlContent +="			<li class='datalist-item clearfix'>";
			htmlContent +="				<div class='am-grid datalist-line'>";
			htmlContent +="					<div class='am-grid-item am-grid-item-50  am-ft-left'>";
			htmlContent +="						<h4 class='am-ft-md cm-left am-ft-tabText'>投资金额</h4>";
			htmlContent +="					</div>";
			htmlContent +="					<div class='am-grid-item am-grid-item-50 am-ft-right am-right-15'>";
			htmlContent +="						<big class='am-ft-bignNum am-ft-orange am-right-5' >"+item.invrAmountPay/100+"</big>元";
			htmlContent +="					</div>";
			htmlContent +="				</div>";
			htmlContent +="			</li>";
			htmlContent +="			<li class='datalist-item clearfix'>";
			htmlContent +="				<div class='am-grid datalist-line'>";
			htmlContent +="					<div class='am-grid-item am-grid-item-50  am-ft-left'>";
			htmlContent +="						<h4 class='am-ft-md cm-left am-ft-tabText'>年化收益</h4>";
			htmlContent +="					</div>";
			htmlContent +="					<div class='am-grid-item am-grid-item-50 am-ft-right am-right-15'>";
			htmlContent +="						<big class='am-ft-bignNum am-ft-orange' >"+parseFloat(item.invsRate*100).toFixed(2)+"%</big>";
			htmlContent +="					</div>";
			htmlContent +="				</div>";
			htmlContent +="			</li>";
			htmlContent +="			<li class='datalist-item clearfix'>";
			htmlContent +="				<div class='am-grid datalist-line'>";
			htmlContent +="				<div class='am-grid-item am-grid-item-50  am-ft-left'>";
			htmlContent +="						<h4 class='am-ft-md cm-left am-ft-tabText'>借款期限</h4>";
			htmlContent +="					</div>";
			htmlContent +="					<div class='am-grid-item am-grid-item-50 am-ft-right am-right-15'>";
			htmlContent +="						<span class='am-ft-main'>"+item.invsDuring+"天</span>";
			htmlContent +="					</div>";
			htmlContent +="				</div>";
			htmlContent +="			</li>";
			htmlContent +="			<li class='datalist-item clearfix'>";
			htmlContent +="				<div class='am-grid datalist-line'>";
			htmlContent +="					<div class='am-grid-item am-grid-item-50  am-ft-left'>";
			htmlContent +="						<h4 class='am-ft-md cm-left am-ft-tabText'>申购时间</h4>";
			htmlContent +="					</div>";
			htmlContent +="					<div class='am-grid-item am-grid-item-50 am-ft-right am-right-15'>";
			htmlContent +="	<span class='am-ft-main' id='invrDatePay'>"+item.invrDatePay+"</span>";
			htmlContent +="</div>";
			htmlContent +="</div>";
			htmlContent +="	</li>";
			htmlContent +="	</ul>";
			htmlContent +="	</aside>";
		}
	}
	$('#investList').html(htmlContent);
}


});