/*个人配置展示*/
var container = {};

container.jourPage = basePath + "/account/jour/page";

var commAjax,nextTrue=true;
var bizTypes = {
		"01":"充值",		
		"02":"取现",		
		"03":"借款",		
		"04":"追加借款",		
		"05":"还款",	
		"06":"结息",		
		"07":"内部转入",		
		"08":"内部转出",		
		"09":"交易转入",		
		"10":"交易转出",		
		"11":"冻结",		
		"12":"解冻",		
		"13":"P2P标-划入",		
		"14":"P2P标-划出",		
		"15":"积分换钱",		
		"00":"其它"
};



var pageNum,pageSize=10;

function getJourPageAction(){
	commAjax = $.commAjax({
		url:container.jourPage,
		data:{
			biz_type:"",
			start_date:$("#startDate").val(),
			end_date:$("#endDate").val(),
			start:pageNum++*pageSize,
			limit:pageSize
		},
		success:function(data){
			
			if(data.success){
				var innerHtml = "";
				var items = data.data.items;
				for (var i in items){
					var item = items[i];
					var dates = item.transDatetime.split(" ");
					innerHtml +='<aside>'+
					'<div class="contractNum bg-gray con-spacing10">'+
					'<b>'+bizTypes[item.accountBizType]+'</b><span class="bar am-ft-xl am-ft-gray">|</span><b class="am-ft-red">¥ '+
					item.transAmount/100+'</b>'+
					'</div>'+
					'<dl class="dl-moneyMain bg-white">'+
					'<dt>'+
					'<p><span class="am-ft-sm am-ft-tabText">'+dates[0]+'</span></p>'+
					'<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">'+dates[1]+'</span></p>'+
					'</dt>'+
					'<dt>'+
					'<p><b class="am-right-5">'+parseFloat(item.transAmount/100).toFixed(2)+'</b><span class="am-ft-sm">元</span></p>'+
					'<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">发生金额</span></p>'+
					'</dt>'+
					'<dt>'+
					'<p><b class="am-right-5">'+parseFloat(item.postAmount/100).toFixed(2)+'</b><span class="am-ft-sm">元</span></p>'+
					'<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">变动后余额</span></p>'+
					'</dt>'+
					'</dl>'+
					'</aside>';
				}

				$("#loading").hide();
				$("#loading").before(innerHtml);
			}else{
				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){}
				});
			}
		}
	});
}

function jourPage(){
	pageNum = 0;
	if(commAjax){
		commAjax.abort();
	}
	$("#content aside").remove();
	getJourPageAction();
}

function nextJourPage(){
	if(commAjax.readyState != 4 && nextTrue){
		return;
	}
	$("#loading").show();
	getJourPageAction();
}

$(function(){
	$(window).scroll(function(){

        if(($(window).scrollTop() + $(window).height() > $(document).height()-40) ){
        	nextJourPage();
        }

    });
	
	
	jourPage();
	$(".date").scroller('destroy').scroller({
			preset:"date",
			theme: 'android-ics light', //皮肤样式
			display: 'bottom', //显示方式 
            mode: 'scroller', //日期选择模式
            dateFormat: 'yy-mm-dd', // 日期格式
            lang:'zh',
            dateOrder: 'yymmdd', //面板中日期排列格式
            endYear:2030 //结束年份
		});
	$("#startDate").change(function(){
		var sDate = new Date($(this).val()),
		 eDate = new Date($("#endDate").val());
		if($("#endDate").val() != "" && sDate > eDate){
			$.dialog({
				content:"起始时间不能大于结束时间！",
				title:"alert",
				ok:function(){}
			});
			nextTrue = false;
			return;
		}
		nextTrue = true;
		jourPage();
	});
	$("#endDate").change(function(){
		var sDate = new Date($("#startDate").val()),
		 eDate = new Date($(this).val());
		if($("#startDate").val() != "" && sDate > eDate){
			$.dialog({
				content:"结束时间不能小于起始时间！",
				title:"alert",
				ok:function(){}
			});
			nextTrue = false;
			return;
		}
		nextTrue = true;
		jourPage();
	});
	
	
})

/*
<aside>
<div class="contractNum bg-gray con-spacing10">
<b>借款</b><span class="bar am-ft-xl am-ft-gray">|</span><b class="am-ft-red">¥ 10000</b>
</div>

<!--金额-->
<dl class="dl-moneyMain bg-white">
<dt>
<p><span class="am-ft-sm am-ft-tabText">2014-06-02</span></p>
<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">09:23:12</span></p>
</dt>
<dt>
<p><b class="am-right-5">1,000</b><span class="am-ft-sm">元</span></p>
<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">发生金额</span></p>
</dt>
<dt>
<p><b>借款</b></p>
<p class="am-pt-5"><span class="am-ft-sm am-ft-tabText">状态</span></p>
</dt>
</dl>
<!--金额-->
</aside>*/