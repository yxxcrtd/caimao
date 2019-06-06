/*单日成交查询*/
var container = {};2
// 查询homs子账号
container.combineidUrl = basePath + "/homs/combineid?_"+new Date().getTime();
// 查询当日成交记录
container.curdealUrl = basePath +  "/stock/child/curdeal?_"+new Date().getTime();
//&homs_combine_id=7242&homs_fund_account=12310001


var endealstatuss ={  1:	'未报',
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


//生成选择列表
function createHomsHtml(index){
	if(container.homsDatas[index].prodName == "实盘大赛"){
		container.homsDatas[index].prodName = proContest;
	}
	return '<li class="datalist-item clearfix">'+
		'<a onclick="choseHoms('+index+')" class="am-grid datalist-line">'+
		       '<div class="am-grid-item am-grid-item-75  am-ft-left">'+
		         '<span class="am-ft-md am-ft-main">'+container.homsDatas[index].operatorNo+
		         ' '+container.homsDatas[index].prodName+
		         '</span></div>' +
		        '<div class="am-grid-item am-grid-item-25 am-ft-right am-right-15">'+
		        '<span class="type-ico am-icon-arrow-right"></span>'+
		        '</div>'+
		'</a>';
}

//选择子账号
function choseHoms(index){
	container.trustIndex = -1;
	container.homsData = container.homsDatas[index];
	$(".lDeal").remove();
	$(".rDeal").remove();
	showCurdeals();
}


function showCurdeals(){
	$.commAjax({
		url:container.curdealUrl,
		data:{
			homs_combine_id:container.homsData.homsCombineId,
			homs_fund_account:container.homsData.homsFundAccount
		},
		success:function(data){
			if(data.success){
				createTrustHtml(data.data);
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

function createTrustHtml(deals){
	container.deals = deals;
	var lHtml = '',rHtml='';
	for(var i in deals){
		lHtml += '<tr class="lDeal">'+
				'<td class="tb-left">'+
				'<p class="labelIcon-single"><span class="am-ft-md am-ft-main">'+deals[i].stockName+'</span></p>'+
				'<p class="icon-USStock"><span class="am-ft-sm am-ft-gray">'+deals[i].stockCode+'</span></p>'+
				'</td></tr>';
		//  ui-input-radio-active

		while(deals[i].businessTime.length<6){
			deals[i].businessTime = "0"+deals[i].businessTime;
		}
		
		rHtml += '<tr class="rDeal" >'+
					'<td class="tb-center">'+deals[i].entrustNo+'</td>'+
				    '<td class="tb-center">'+
				    deals[i].businessTime.substr(0,2)+":"+
				    deals[i].businessTime.substr(2,2)+":"+
				    deals[i].businessTime.substr(4,2)+'</td>'+
				    '<td class="tb-center"><span class="am-ft-md">'+entrustDirections[deals[i].entrustDirection]+'</span></td>'+
				    '<td class="tb-center">'+deals[i].businessAmount+'</td>'+
				    '<td class="tb-right">'+(deals[i].businessBalance/100)+'元</td>'+
				    '<td class="tb-right">'+(deals[i].totalFare/100)+'元</td>'+	    
				  '</tr>';
		
		
	}
	$("#lDealsTitle").after(lHtml);
	$("#rDealsTitle").after(rHtml);
	
}


$(function(){

	$("#scrollTable").scroll(function(){

		if($(this).scrollLeft() != 0){
			$("#arrowShape").remove();
			$(this).unbind("scroll");
		}
			
	});
	
	//初始化获取信息
	$.commAjax({
		url:container.combineidUrl,
		success:function(data){
			if(data.success){
				container.homsDatas = data.data,homsHtml = "";
				if(container.homsDatas.length != 0){
					container.homsData = container.homsDatas[0];
					var homsHtml = "";
					for(var i in container.homsDatas){
						homsHtml += "<option value='"+i+"'>"+container.homsDatas[i].operatorNo+" "+container.homsDatas[i].prodName+"</option>";
					}
					var choseHomsSelect= $("#choseHoms");
					choseHomsSelect.html(homsHtml);
					choseHomsSelect.scroller('destroy').scroller({
						preset:"select",
			    		theme: 'android-ics light', //皮肤样式
			            display: 'bottom', //显示方式 
			            mode: 'scroller', //日期选择模式
			            lang:'zh'
					});
					$("#choseHoms_dummy").css({width:"100%",border:"none"});
					choseHomsSelect.change(function(){
						choseHoms($(this).val());
					});
					
					showCurdeals();
				}else{
					$.dialog({
						content:"无交易子账号",
						title:"alert",
						ok:function(){}
					});
					return;
				}
				for(var i in container.homsDatas){
					homsHtml += createHomsHtml(i);	
				}
				$("#panel2 ul").html(homsHtml);
			}else{
				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){
					}
				});
			}
		}
	});
			
})