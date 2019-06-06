/*我的字长查询*/
var container = {};

// 查询homs子账号
container.combineidUrl = basePath + "/homs/combineid";
// 查询账户信息
container.assetsinfoUrl = basePath + "/homs/assetsinfo";
// 账户交易查询
container.holdingUrl = basePath + "/stock/child/holding";


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
	$(".lHtml").remove();
	$(".rHtml").remove();
	container.homsData = container.homsDatas[index];
	$("#panel2").hide();
	$("#panel1").show();
	$("#headerPanel").show();
	showHomesData()
}

function showHomesData(){
	$("#choseHoms span").eq(0).html(container.homsData.operatorNo + ' '+container.homsData.prodName );
	var dialog = $.dialog();
	console.info(dialog);
	var commAjax1,commAjax2;
	commAjax1 = $.commAjax({
		url:container.assetsinfoUrl,
		data:{
			homs_fund_account:container.homsData.homsFundAccount,
			homs_combine_id:container.homsData.homsCombineId	
		},
		success:function(data){
			if(commAjax2 && commAjax2.readyState == 4){
				dialog.close();
			}
			if(data.success){
				$("#totalNetAssets").html(parseFloat(data.data.totalNetAssets/100).toLocaleString());
				$("#curAmount").html(parseFloat(data.data.curAmount/100).toLocaleString());
				$("#currentCash").html(parseFloat(data.data.currentCash/100).toLocaleString());
				$("#totalMarketValue").html(parseFloat(data.data.totalMarketValue/100).toLocaleString());
				
			}else{

				dialog.close();
				$.dialog({
					content:data.msg,
					title:"alert",
					lock:false,
					ok:function(){}
				});
			}
		}
	});
	
	commAjax2 = $.commAjax({
		url:container.holdingUrl,
		data:{
			homs_combine_id:container.homsData.homsCombineId,
			homs_fund_account:container.homsData.homsFundAccount
		},
		success:function(data){
			if(commAjax1 && commAjax1.readyState == 4){
				dialog.close();
			}
			if(data.success){
				var lHtml="",rHtml="",datas = data.data;
		        
				for(var i in datas){
					lHtml += '<tr class="lHtml"><td class="tb-left">'+
						'<p class="labelIcon-single"><span class="am-ft-md am-ft-main">'+datas[i].stockName+'</span></p>'+
						'<p class="icon-USStock"><span class="am-ft-sm am-ft-gray">'+datas[i].stockCode+'</span></p>'+
						  '</td></tr>';
					
					rHtml += '<tr class="rHtml">'+
				    '<td class="tb-center">'+datas[i].currentAmount+'</td>'+
				    '<td class="tb-center">'+datas[i].enableAmount+'</td>'+
				    '<td class="tb-center">'+datas[i].costBalance/100+'元</td>'+
				    '<td class="tb-right">'+datas[i].marketValue/100+'元</td>'+
					'</tr>';
				}
				$("#lAsset").append(lHtml);
				$("#rAsset").append(rHtml);
				
			}else{
				dialog.close();
				
				$.dialog({
					content:data.msg,
					title:"alert",
					lock:false,
					ok:function(){}
				});
			}
			
			
		}
	});
	
}

	
$(function(){
	
	$("#scrollTable").scroll(function(){
		if($(this).scrollLeft() != 0){
			$("#arrowShape").remove();
			$(this).unbind("scroll");
		}
			
	});
	
	$.commAjax({
		url:container.combineidUrl,
		success:function(data){
			if(data.success){
				container.homsDatas = data.data;
				if(container.homsDatas.length > 0){
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
					showHomesData();
				}else{
					$.dialog({
						content:"无交易子账号",
						title:"alert",
						ok:function(){
						}
					});
				}
				$("#choseHoms span")
			}else{
				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){}
				});
			}
		}
	});
});