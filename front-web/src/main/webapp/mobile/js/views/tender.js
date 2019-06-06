$(function() {
	var container = {};
	container.tenderUrl = basePath
			+ "/p2p/subject/page?status=&rate=&during=&date_publish=&start=0&limit=10000";
	container.payuserUrl=basePath+"/payuser";
	function dialog(msg) {
		$.dialog({
			content : msg,
			title : "alert",
			ok : function() {
			}
		});
	}
	$.commAjax({
		url : container.tenderUrl,
		type : "get",
		data : {
			order_first_column:'invs_status',
			order_first_dir:'DESC'
			
		},
		success : function(data2) {
			if (data2.success) {
				container.tenderList = data2.data.items;
				divHtmlContent();
			} else {
				dialog(data2.msg);

			}

		}

	});
	function divHtmlContent() {
		var htmlContent = "";
		var len = container.tenderList.length;
		
		if(container.tenderList==null||len==0){
			htmlContent +="<p class='tx-warning con-spacing10'>";
			htmlContent +="<span class='am-ft-tabText'>抱歉，您来晚了一步，当前没有可投的项目！</span>";
			htmlContent +="</p>";
			

		}else{
			
		for ( var i = 0; i < len; i++) {
			var item = container.tenderList[i];
			var invsAmountActual = item.invsAmountActual;
			var invsAmountPre = item.invsAmountPre;
			var lackAmount = (invsAmountPre - invsAmountActual)/100;
			var percentAmount = parseFloat(
					(invsAmountActual / invsAmountPre) * 100).toFixed(2);
			
			htmlContent += "<div class='bg-white con-index'>";
			htmlContent += "<aside class='lineBottom-gray am-pb-10 am-ft-center'>";
			htmlContent += "<p>" + item.invsName + "</p></aside>";
			htmlContent += "<aside class='am-ft-center pt-1'>";
			htmlContent += "	<big class='am-ft-red'>" + parseFloat(item.invsRate * 100).toFixed(2)
					+ "<span class='am-ft-xxxl'>%</span></big>";
			htmlContent += "	<p>";
			htmlContent += "	<span class='am-ft-tabText'>年化收益</span>"
			htmlContent += "</p>";
			htmlContent += "</aside>";
			htmlContent += "<aside class='am-ft-center pt-1'>";
			htmlContent += "<p class='am-ft-tabText'>";
			htmlContent += "已完成<span class='am-ft-red'>" + percentAmount
					+ "%</span><span class='bar'>｜</span>还需资金";
			htmlContent += "<span class='am-ft-red'>" + lackAmount + "</span>元";
			htmlContent += "</p>";
			htmlContent += "</aside>";
			htmlContent += "<aside class='am-ft-center am-pt-20'>";
			htmlContent += "<div class='btn-page btn-page-center con-spacing15'>";
			if(item.isAblePay==-3){
				htmlContent += "	<a class='am-button btn-disabled am-button-red'> <span>未到发布时间</span>";
				htmlContent += "	</a>";
				
			}else if(item.isAblePay==-2){
				htmlContent += "	<a class='am-button btn-disabled am-button-red'> <span>购买期限已过</span>";
				htmlContent += "	</a>";
				
			}else if(item.isAblePay==-1){
				htmlContent += "	<a class='am-button btn-disabled am-button-red'> <span>已满标</span>";
				htmlContent += "	</a>";
				
			}else if(item.isAblePay==1){
				htmlContent += "	<a class='am-button am-button-red' href ='"+mobileUrl+"/tenderDetail.htm?invs="+item.invsId+"'> <span>立即投标</span>";
				htmlContent += "	</a>";
				
			}else{
				htmlContent += "	<a class='am-button btn-disabled am-button-red'> <span>暂不允许购买</span>";
				htmlContent += "	</a>";
			}
				
			
			htmlContent += "</div>";
			htmlContent += "<p>";
			htmlContent += "	<span class='am-ft-tabText'>已有<span>"+item.invsNumPays+"</span>人投资</span>";
			htmlContent += "</p>";
			htmlContent += "</aside>";
			htmlContent += "</div>";
		}
		
	}
		
		$('#p2pList').html(htmlContent);

	}

});