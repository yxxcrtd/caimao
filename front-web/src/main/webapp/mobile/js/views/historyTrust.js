/*单日交易查询*/
var container = {};
//历史查询交易记录
container.hisentrustUrl = basePath +  "/stock/child/hisentrust?_"+new Date().getTime();
//&homs_combine_id=7242&homs_fund_account=12310001
//start_date=2015-01-19&end_date=2015-01-26&order_field=business_timestart&start=0&limit=5

var pageSize = 10,startDate = '',endDate = '',start = 0,hisAjax,totalCount;


var entrustStatuss ={  1:	'未报',
		2:  '待报',
		3:  '正报',
		4:	'已报',		
		5:	'废单',		
		6:	'部成',		
		7:	'已成',		
		8:	'部撤',		
		9:	'已撤',		
		'a':	'待撤',		
		'A':	'未撤',		
		'B':	'待撤',		
		'C':	'正撤',		
		'D':	'撤认',		
		'E':	'撤废',		
		'F':	'已撤'},
entrustDirections = {
1:	'买入',		
2:	'卖出'		
}


function showHistrusts(){
	if(start == 0 && hisAjax){
		hisAjax.abort();
	}else if(start !=0 && hisAjax.readyState != 4){
		return;
	}
	var dialog = $.dialog();
	hisAjax = $.commAjax({
		url:container.hisentrustUrl,
		data:{
			start_date:startDate,
			end_date:endDate,
			order_field:"business_timestart",
			start:start++*pageSize,
			limit:pageSize
		},
		success:function(data){
			dialog.close();
			if(data.success){
				console.info(data);
				totalCount = data.data.totalCount;
				createTrustHtml(data.data.items);
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

function createTrustHtml(trusts){
	container.trusts = trusts;
	var lHtml = '',rHtml='';
	for(var i in trusts){
		lHtml += '<tr class="lDeal" >'+
				'<td class="tb-left">'+
				'<p class="labelIcon-single"><span class="am-ft-md am-ft-main">'+trusts[i].stockName+'</span></p>'+
				'<p class="icon-USStock"><span class="am-ft-sm am-ft-gray">'+trusts[i].stockCode+'</span></p>'+
				'</td></tr>';
		//  ui-input-radio-active

		rHtml += '<tr class="rDeal" >'+
					'<td class="tb-center">'+trusts[i].entrustNo+'</td>'+
				    '<td class="tb-center">'+
				    trusts[i].entrustDate.substr(0,4)+"-"+
				    trusts[i].entrustDate.substr(4,2)+"-"+
				    trusts[i].entrustDate.substr(6,2)+'</td>'+
				    '<td class="tb-center"><span class="am-ft-md">'+entrustStatuss[trusts[i].entrustStatus]+'</span></td>'+
				    '<td class="tb-center"><span class="am-ft-md">'+entrustDirections[trusts[i].entrustDirection]+'</span></td>'+
				    '<td class="tb-center">'+trusts[i].entrustPrice+'元</td>'+
				    '<td class="tb-right">'+trusts[i].entrustAmount+'</td>'+
				    '<td class="tb-center">'+trusts[i].businessAmount+'</td>'+
				    '<td class="tb-right">'+(trusts[i].businessBalance/100)+'元</td>'+
				  '</tr>';
		
		
	}
	$("#lTrustsTitle").append(lHtml);
	$("#rTrustsTitle").append(rHtml);
	
}


$(function(){

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
	
	
	$("#scrollTable").scroll(function(){
		if($(this).scrollLeft() != 0){
			$("#arrowShape").remove();
			$(this).unbind("scroll");
		}
	});
	$(document).scroll(function(){	
	   if(($(window).scrollTop() + $(window).height() > $(document).height()-40) && ((start*pageSize-1)<totalCount)){
		   showHistrusts();
        }

			
	});
	
	//初始化查询
	showHistrusts();
	
	$("#startDate").change(function(){
		startDate = $(this).val();
		start = 0;
		$(".lDeal").remove();
		$(".rDeal").remove();
		showHistrusts();
	});
	
	$("#endDate").change(function(){
		endDate = $(this).val();
		start = 0;
		$(".lDeal").remove();
		$(".rDeal").remove();
		showHistrusts();
	});
})