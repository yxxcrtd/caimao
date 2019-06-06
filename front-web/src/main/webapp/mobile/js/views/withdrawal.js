// 撤单页面
var container = {},key;
// 秘钥接口地址 get
container.rsa = basePath + "/sec/rsa?_="+new Date().getTime();
// 查询homs子账号
container.combineidUrl = basePath + "/homs/combineid?_"+new Date().getTime();
// 撤单查询
container.revocableentrustUrl = basePath + "/stock/revocableentrust?_"+new Date().getTime();
// 撤单请求
container.withdrawalUrl = basePath + "/stock/withdrawal";



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

//获取秘钥
$.commAjax({
	url : container.rsa,
	success : function(data) {
		if (!data.success) {
			$.dialog({
				content:data.msg,
				title:"alert",
				ok:function(){}
			});
			return;
		}
		var modulus = data.data.modulus;
		var exponent = data.data.exponent;
		key = RSAUtils.getKeyPair(exponent, "", modulus);

	}
});

//创建子账号列表
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
	$("#choseHoms span").eq(0).html(container.homsDatas[index].operatorNo+" "+container.homsDatas[index].prodName);
	$("#panel2").hide();
	$(".trust").remove();
	$("#panel1").show();
	$("#tab").show();
	showRevocableentrusts();
}

function showRevocableentrusts(){
	$.commAjax({
		url:container.revocableentrustUrl,
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

function createTrustHtml(trusts){
	container.trusts = trusts;
	var lHtml = '',rHtml='';
	for(var i in trusts){
		lHtml += '<tr id="lTrust'+i+'"  class="trust">'+
				'<td class="tb-left">'+
				  '<a onclick="choseTrust('+i+')" class="clearfix">'+
				'<div class="cm-left am-pt-5"><span  class="ui-input-radio trustSpan" id="trustSpan'+i+'">'+
				 '</span></div>'+
				'<div class="cm-left am-left-5">'+
				'<p class="labelIcon-single"><span class="am-ft-md am-ft-main">'+trusts[i].stockName+'</span></p>'+
				'<p class="icon-USStock"><span class="am-ft-sm am-ft-gray">'+trusts[i].stockCode+'</span></p>'+
				'</div></a></td></tr>';
		//  ui-input-radio-active
		rHtml += '<tr id="rTrust'+i+'"  class="trust">'+
					'<td class="tb-center">'+trusts[i].entrustNo+'</td>'+
				    '<td class="tb-center">'+
				    trusts[i].entrustTime.substr(0,2)+":"+
				    trusts[i].entrustTime.substr(2,2)+":"+
				    trusts[i].entrustTime.substr(4,2)+'</td>'+
				    '<td class="tb-center">'+entrustStatuss[trusts[i].entrustStatus]+'</td>'+
				    '<td class="tb-center"><span class="am-ft-md">'+entrustDirections[trusts[i].entrustDirection]+'</span></td>'+
				    '<td class="tb-right"><span class="am-ft-md">'+trusts[i].entrustPrice+'元</span></td>'+
				    '<td class="tb-center">'+trusts[i].entrustAmount+'</td>'+
				    '<td class="tb-center">'+trusts[i].businessAmount+'</td>'+
				    '<td class="tb-right">'+trusts[i].businessBalance+'元</td>'+
				  '</tr>';
		
		
	}
	$("#lTrustsTitle").after(lHtml);
	$("#rTrustsTitle").after(rHtml);
	
}

function choseTrust(index){
	container.trustIndex = index;
	$(".trustSpan").removeClass("ui-input-radio-active");
	$("#trustSpan"+index).addClass("ui-input-radio-active");
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
					showRevocableentrusts();
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
	container.trustIndex = -1;
	$("#drawalBtn").click(function(){
		if(container.trustIndex == -1){
			$.dialog({
				content:"请选择单号",
				title:"alert",
				ok:function(){}
			});
			return;
		}
		
		var dialog = $.dialog({
			content:"确定要撤单吗?",
			title:"alert",
			ok:function(){
				setTimeout(withdrawalAction,0)	
			},
			cancel:function(){}
		});
	});
});

function withdrawalAction(){
	var dialog = $.dialog();
	$.commAjax({
		url:container.withdrawalUrl,
		type:"post",
		data:{
			homs_combine_id:container.homsData.homsCombineId ,
			homs_fund_account:container.homsData.homsFundAccount ,
			entrust_no:container.trusts[container.trustIndex].entrustNo
		},
		success:function(data){
			dialog.close();
			if(data.success){
				$("#lTrust"+container.trustIndex).remove();
				$("#rTrust"+container.trustIndex).remove();
				container.trustIndex = -1;
				$.dialog({
					content:"撤单成功",
					title:"ok",
					ok:function(){}
				});
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