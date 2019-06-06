/*交易买入*/
var container = {},key;
// 秘钥接口地址 get
container.rsa = basePath + "/sec/rsa";
// 查询homs子账号
container.combineidUrl = basePath + "/homs/combineid?";
// 证券详细查询 get
container.trendDataUrl = basePath + "/quote/stock/{0}/trend_data";
// 证券行情实时查询 get
container.realTimeDataUrl = basePath + "/quote/stock/{0}/real_time_data";
// 获取可用余额 get
container.assetsinfo = basePath + "/homs/assetsinfo";
// 获取可买数量 post
container.sellquantity = basePath + "/stock/sellquantity";
// 获取正确代码 get
container.stockDataUrl =  basePath + "/quote/stock/stock_data";
// 账户交易查询
container.holdingUrl = basePath + "/stock/child/holding";

// 卖出接口 post
container.sellUrl = basePath+"/stock/sell";

function request(paras) {
	var url = location.href;
	var paramStr = url.substring(url.indexOf('?') + 1, url.length).split('&');
	var j;
	var paramObj = {};
	for (var i = 0; j = paramStr[i]; i++) {
		paramObj[j.substring(0, j.indexOf('=')).toLowerCase()] = j.substring(j
				.indexOf('=') + 1, j.length);
	}

	var returnValue = paramObj[paras.toLowerCase()];

	if (typeof (returnValue) == "undefined") {
		return "";
	} else {
		return returnValue;
	}
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

function queryAmount(){
	if($("#stockCode").val()==""){
		return;
	}
	if(container.homsData){
		container.needAmountAjax = $.commAjax({
			url:container.sellquantity,
			type:"post",
			data:{
				homs_combine_id:container.homsData.homsCombineId,
				homs_fund_account:container.homsData.homsFundAccount,
				stock_code:$("#stockCode").val(),
				entrust_price:$.parseMoney($("#priceSpinner").val()),
				exchange_type:""
			},
			success:function(data){
				if(data.success){
					container.priceBool = true;
					$("#cSellNumber").html(data.data);
					container.curNumber = data.data;
				}
			}
			
		});
	}
}

//数量增加减少
function tagsinput(){
	 var patrn = new RegExp(/^(([1-9]\d*)|0)(\.\d{1,2})?$/);
	
	$(".tagsinput-addMoney").click(function(){
		var textInput = $(this).parent().prev().find("input").eq(0);
		val = textInput.val();
		 if (!patrn.test(val)){
			 val = 0;
		 }
		
		
		if(val >= 10000){
			textInput.val(10000);
			return;
		}
		textInput.val(parseFloat(parseFloat(val)+0.01).toFixed(2));
		queryAmount();
	});

	$(".tagsinput-minusMoney").click(function(){

		var textInput = $(this).parent().next().find("input").eq(0);
		val = textInput.val();
		 if (!patrn.test(val)){
			 val = 10000.01;
		 }
		
		if(val <= 0.01){

			textInput.val(0.01);
			return;
		}
		textInput.val(parseFloat(parseFloat(val)-0.01).toFixed(2));
		queryAmount();
	});
	
	
	

	 var patrn2 = new RegExp(/^(([1-9]\d*)|0)$/);
	$(".tagsinput-addNumber").click(function(){

		var textInput = $(this).parent().prev().find("input").eq(0);
		textInput.focus();
		val = textInput.val();
		 if (!patrn2.test(val)||val<100){
			 val = 0;
		 }
		
		
		if(val >= 999900){
			textInput.val(1000000)
			return;
		}
		textInput.val(parseInt(val/100)*100+100);
	});

	$(".tagsinput-minusNumber").click(function(){
		var textInput = $(this).parent().next().find("input").eq(0);
		textInput.focus();
		val = textInput.val();
		 if (!patrn2.test(val)||val>1000000){
			 val = 1000100;
		 }
		if(val <= 200){
			textInput.val(100);
			return;
		}
		textInput.val(parseInt(val/100)*100-100);
	});
	
	$("#sellNumber").keydown(function(e){

        var key = window.event ? e.keyCode : e.which; 
        if(!isNumber(key) && key != 8){
        	return false;
        }
        return (!e.shiftKey && isNumber(key)) || isSpecialKey(key);
	});
	
	
	$("#stockCode").keydown(function(e){

        var key = window.event ? e.keyCode : e.which; 
        if(!isNumber(key) && key != 8){
        	return false;
        }
        return (!e.shiftKey && isNumber(key)) || isSpecialKey(key);
	});
	
}
$(tagsinput);


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
	container.homsData = container.homsDatas[index];
	getEnableWithdraw();
}
//获取子账户余额
function getEnableWithdraw(){
	$.commAjax({
		url:container.assetsinfo,
		data:{
			homs_fund_account:container.homsData.homsFundAccount,
			homs_combine_id:container.homsData.homsCombineId
		},
		success:function(data){
			if(data.success){
				$("#available").html(parseFloat(data.data.curAmount/100).toFixed(2));
				queryAmount();
			}else{
				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){}
				});
			}
		}
	});
	showHomesData();
}


// 键盘联想获取交易信息
function getStockCode(){
	if(container.stockAjax){
		container.stockAjax.abort();
	}
	container.stockAjax = $.commAjax({
		url:container.stockDataUrl+"/"+$("#stockCode").val(),
		success:function(data){
			if(data.success){
				var codeHtml = '<ul class="select-results am-selectList">',
				datas = data.data;
				if(datas.length == 0){
					container.stockShowBool = false;
					$("#codeList").hide();
					return;
				}
				for(var i in datas){
					codeHtml += '<li onclick="choseStockCode(\''+datas[i].stockCode.trim() +'\',\''+datas[i].stockName.trim() +'\')">'+
					'<span class="am-right-5">'+ datas[i].stockCode +'</span><span>'+datas[i].stockName+'</span>'+
					'</li>';
				}
				codeHtml += '</ul>';
				var codeList = $("#codeList");
				codeList.html("");
				codeList.html(codeHtml);
				codeList.show();
				container.stockShowBool = true;
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

//获取实时行情
function getTimeData(stockCode){
	if(container.realTimeAjax){
		container.realTimeAjax.abort();
	}
	container.realTimeAjax = $.commAjax({
		url:container.realTimeDataUrl.format(stockCode),
		success:function(data){
			if(data.success){
				var timeData = data.data, fmClassGreen="am-ft-green",fmClassRed="am-ft-red";
				if(timeData.chargeRate == 0){
					$("#newPrice").removeClass(fmClassGreen);
					$("#chargeRate").removeClass(fmClassGreen);
					$("#chargeValue").removeClass(fmClassGreen);
									
					$("#newPrice").removeClass(fmClassRed);
					$("#chargeRate").removeClass(fmClassRed);
					$("#chargeValue").removeClass(fmClassRed);
					
				}else if(timeData.chargeRate>0){
					$("#newPrice").removeClass(fmClassGreen);
					$("#chargeRate").removeClass(fmClassGreen);
					$("#chargeValue").removeClass(fmClassGreen);

					$("#newPrice").addClass(fmClassRed);
					$("#chargeRate").addClass(fmClassRed);
					$("#chargeValue").addClass(fmClassRed);
				}else{
					$("#newPrice").removeClass(fmClassRed);
					$("#chargeRate").removeClass(fmClassRed);
					$("#chargeValue").removeClass(fmClassRed);
					
					$("#newPrice").addClass(fmClassGreen);
					$("#chargeRate").addClass(fmClassGreen);
					$("#chargeValue").addClass(fmClassGreen);
				}
				
				if(timeData.buyPrice1==timeData.prevClosePrice){
					$("#buyPrice1").removeClass(fmClassGreen);
					$("#buyCount1").removeClass(fmClassGreen);
					$("#buyPrice1").addClass(fmClassRed);
					$("#buyCount1").addClass(fmClassRed);
				}else if(timeData.buyPrice1>timeData.prevClosePrice){
					$("#buyPrice1").removeClass(fmClassGreen);
					$("#buyCount1").removeClass(fmClassGreen);
					$("#buyPrice1").addClass(fmClassRed);
					$("#buyCount1").addClass(fmClassRed);
				}else{
					$("#buyPrice1").removeClass(fmClassRed);
					$("#buyCount1").removeClass(fmClassRed);
					$("#buyPrice1").addClass(fmClassGreen);
					$("#buyCount1").addClass(fmClassGreen);
				}
				
				if(timeData.buyPrice2==timeData.prevClosePrice){
					$("#buyPrice2").removeClass(fmClassGreen);
					$("#buyCount2").removeClass(fmClassGreen);
					$("#buyPrice2").addClass(fmClassRed);
					$("#buyCount2").addClass(fmClassRed);
				}else if(timeData.buyPrice2>timeData.prevClosePrice){
					$("#buyPrice2").removeClass(fmClassGreen);
					$("#buyCount2").removeClass(fmClassGreen);
					$("#buyPrice2").addClass(fmClassRed);
					$("#buyCount2").addClass(fmClassRed);
				}else{
					$("#buyPrice2").removeClass(fmClassRed);
					$("#buyCount2").removeClass(fmClassRed);
					$("#buyPrice2").addClass(fmClassGreen);
					$("#buyCount2").addClass(fmClassGreen);
				}
				
				if(timeData.buyPrice3==timeData.prevClosePrice){
					$("#buyPrice3").removeClass(fmClassGreen);
					$("#buyCount3").removeClass(fmClassGreen);
					$("#buyPrice3").addClass(fmClassRed);
					$("#buyCount3").addClass(fmClassRed);
				}else if(timeData.buyPrice3>timeData.prevClosePrice){
					$("#buyPrice3").removeClass(fmClassGreen);
					$("#buyCount3").removeClass(fmClassGreen);
					$("#buyPrice3").addClass(fmClassRed);
					$("#buyCount3").addClass(fmClassRed);
				}else{
					$("#buyPrice3").removeClass(fmClassRed);
					$("#buyCount3").removeClass(fmClassRed);
					$("#buyPrice3").addClass(fmClassGreen);
					$("#buyCount3").addClass(fmClassGreen);
				}
				
				if(timeData.buyPrice4==timeData.prevClosePrice){
					$("#buyPrice4").removeClass(fmClassGreen);
					$("#buyCount4").removeClass(fmClassGreen);
					$("#buyPrice4").addClass(fmClassRed);
					$("#buyCount4").addClass(fmClassRed);
				}else if(timeData.buyPrice4>timeData.prevClosePrice){
					$("#buyPrice4").removeClass(fmClassGreen);
					$("#buyCount4").removeClass(fmClassGreen);
					$("#buyPrice4").addClass(fmClassRed);
					$("#buyCount4").addClass(fmClassRed);
				}else{
					$("#buyPrice4").removeClass(fmClassRed);
					$("#buyCount4").removeClass(fmClassRed);
					$("#buyPrice4").addClass(fmClassGreen);
					$("#buyCount4").addClass(fmClassGreen);
				}
				
				if(timeData.buyPrice5==timeData.prevClosePrice){
					$("#buyPrice5").removeClass(fmClassGreen);
					$("#buyCount5").removeClass(fmClassGreen);
					$("#buyPrice5").addClass(fmClassRed);
					$("#buyCount5").addClass(fmClassRed);
				}else if(timeData.buyPrice5>timeData.prevClosePrice){
					$("#buyPrice5").removeClass(fmClassGreen);
					$("#buyCount5").removeClass(fmClassGreen);
					$("#buyPrice5").addClass(fmClassRed);
					$("#buyCount5").addClass(fmClassRed);
				}else{
					$("#buyPrice5").removeClass(fmClassRed);
					$("#buyCount5").removeClass(fmClassRed);
					$("#buyPrice5").addClass(fmClassGreen);
					$("#buyCount5").addClass(fmClassGreen);
				}
				
				
				if(timeData.sellPrice1==timeData.prevClosePrice){
					$("#sellPrice1").removeClass(fmClassGreen);
					$("#sellCount1").removeClass(fmClassGreen);
					$("#sellPrice1").addClass(fmClassRed);
					$("#sellCount1").addClass(fmClassRed);
				}else if(timeData.sellPrice1>timeData.prevClosePrice){
					$("#sellPrice1").removeClass(fmClassGreen);
					$("#sellCount1").removeClass(fmClassGreen);
					$("#sellPrice1").addClass(fmClassRed);
					$("#sellCount1").addClass(fmClassRed);
				}else{
					$("#sellPrice1").removeClass(fmClassRed);
					$("#sellCount1").removeClass(fmClassRed);
					$("#sellPrice1").addClass(fmClassGreen);
					$("#sellCount1").addClass(fmClassGreen);
				}
				
				if(timeData.sellPrice2==timeData.prevClosePrice){
					$("#sellPrice2").removeClass(fmClassGreen);
					$("#sellCount2").removeClass(fmClassGreen);
					$("#sellPrice2").addClass(fmClassRed);
					$("#sellCount2").addClass(fmClassRed);
				}else if(timeData.sellPrice2>timeData.prevClosePrice){
					$("#sellPrice2").removeClass(fmClassGreen);
					$("#sellCount2").removeClass(fmClassGreen);
					$("#sellPrice2").addClass(fmClassRed);
					$("#sellCount2").addClass(fmClassRed);
				}else{
					$("#sellPrice2").removeClass(fmClassRed);
					$("#sellCount2").removeClass(fmClassRed);
					$("#sellPrice2").addClass(fmClassGreen);
					$("#sellCount2").addClass(fmClassGreen);
				}
				
				if(timeData.sellPrice3==timeData.prevClosePrice){
					$("#sellPrice3").removeClass(fmClassGreen);
					$("#sellCount3").removeClass(fmClassGreen);
					$("#sellPrice3").addClass(fmClassRed);
					$("#sellCount3").addClass(fmClassRed);
				}else if(timeData.sellPrice3>timeData.prevClosePrice){
					$("#sellPrice3").removeClass(fmClassGreen);
					$("#sellCount3").removeClass(fmClassGreen);
					$("#sellPrice3").addClass(fmClassRed);
					$("#sellCount3").addClass(fmClassRed);
				}else{
					$("#sellPrice3").removeClass(fmClassRed);
					$("#sellCount3").removeClass(fmClassRed);
					$("#sellPrice3").addClass(fmClassGreen);
					$("#sellCount3").addClass(fmClassGreen);
				}
				
				if(timeData.sellPrice4==timeData.prevClosePrice){
					$("#sellPrice4").removeClass(fmClassGreen);
					$("#sellCount4").removeClass(fmClassGreen);
					$("#sellPrice4").addClass(fmClassRed);
					$("#sellCount4").addClass(fmClassRed);
				}else if(timeData.sellPrice4>timeData.prevClosePrice){
					$("#sellPrice4").removeClass(fmClassGreen);
					$("#sellCount4").removeClass(fmClassGreen);
					$("#sellPrice4").addClass(fmClassRed);
					$("#sellCount4").addClass(fmClassRed);
				}else{
					$("#sellPrice4").removeClass(fmClassRed);
					$("#sellCount4").removeClass(fmClassRed);
					$("#sellPrice4").addClass(fmClassGreen);
					$("#sellCount4").addClass(fmClassGreen);
				}
				
		
				if(timeData.sellPrice5==timeData.prevClosePrice){
					$("#sellPrice5").removeClass(fmClassGreen);
					$("#sellCount5").removeClass(fmClassGreen);
					$("#sellPrice5").addClass(fmClassRed);
					$("#sellCount5").addClass(fmClassRed);
				}else if(timeData.sellPrice5>timeData.prevClosePrice){
					$("#sellPrice5").removeClass(fmClassGreen);
					$("#sellCount5").removeClass(fmClassGreen);
					$("#sellPrice5").addClass(fmClassRed);
					$("#sellCount5").addClass(fmClassRed);
				}else{
					$("#sellPrice5").removeClass(fmClassRed);
					$("#sellCount5").removeClass(fmClassRed);
					$("#sellPrice5").addClass(fmClassGreen);
					$("#sellCount5").addClass(fmClassGreen);
				}
				
				if(timeData.upPrice==timeData.prevClosePrice){
					$("#upPrice").removeClass(fmClassGreen);
					$("#upPrice").addClass(fmClassRed);
				}else if(timeData.upPrice>timeData.prevClosePrice){
					$("#upPrice").removeClass(fmClassGreen);
					$("#upPrice").addClass(fmClassRed);
				}else{
					$("#upPrice").removeClass(fmClassRed);
					$("#upPrice").addClass(fmClassGreen);
				}
				
				if(timeData.downPrice==timeData.prevClosePrice){
					$("#downPrice").removeClass(fmClassGreen);
					$("#downPrice").addClass(fmClassRed);
				}else if(timeData.downPrice>timeData.prevClosePrice){
					$("#downPrice").removeClass(fmClassGreen);
					$("#downPrice").addClass(fmClassRed);
				}else{
					$("#downPrice").removeClass(fmClassRed);
					$("#downPrice").addClass(fmClassGreen);
				}
				
				

				$("#newPrice").html(timeData.newPrice);
				$("#chargeRate").html(parseFloat(timeData.chargeRate*100).toFixed(2)+"%");
				$("#chargeValue").html(timeData.chargeValue);
				$("#buyPrice1").html(timeData.buyPrice1);
				$("#buyCount1").html(timeData.buyCount1);
				$("#sellPrice1").html(timeData.sellPrice1);
				$("#sellCount1").html(timeData.sellCount1);
				
				$("#buyPrice2").html(timeData.buyPrice2);
				$("#buyCount2").html(timeData.buyCount2);
				$("#sellPrice2").html(timeData.sellPrice2);
				$("#sellCount2").html(timeData.sellCount2);
				
				$("#buyPrice3").html(timeData.buyPrice3);
				$("#buyCount3").html(timeData.buyCount3);
				$("#sellPrice3").html(timeData.sellPrice3);
				$("#sellCount3").html(timeData.sellCount3);
				
				$("#buyPrice4").html(timeData.buyPrice4);
				$("#buyCount4").html(timeData.buyCount4);
				$("#sellPrice4").html(timeData.sellPrice4);
				$("#sellCount4").html(timeData.sellCount4);
				
				$("#buyPrice5").html(timeData.buyPrice5);
				$("#buyCount5").html(timeData.buyCount5);
				$("#sellPrice5").html(timeData.sellPrice5);
				$("#sellCount5").html(timeData.sellCount5);
				
				$("#upPrice").html(timeData.upPrice);
				$("#downPrice").html(timeData.downPrice);
				
				
				container.exchangeType = timeData.exchangeType;
			
			}else{
				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){}
				});
			}
		}
		
	});
	container.realTimeout = setTimeout("getTimeData(\'"+stockCode+"\')",10000);
}

//选择证券
function choseStockCode(stockCode,stockName){
	container.stockShowBool = false;
	$("#codeList").hide();
	$("#stockCode").val(stockCode);
	//$("#showCodeDiv span").html(stockCode);
	$("#showCodeDiv").html(stockName);
	if(container.realTimeout){
		clearTimeout(container.realTimeout);
	}
	getTimeData(stockCode);
	
	if($("#stockCode").val() != container.oldStockCode||$("#cSellNumber").html()==""){
		container.oldStockCode = $("#stockCode").val();
		$.commAjax({
			url:container.trendDataUrl.format($("#stockCode").val()),
			success:function(data){
				if(data.success){
					var trendList = data.data.trendList;
					$("#priceSpinner").val(trendList[trendList.length-1].newPrice);
					queryAmount();
				}
			}
		});
	}
}

function showHomesData(){
	$("#choseHoms span").eq(0).html(container.homsData.operatorNo + ' '+container.homsData.prodName );
	var dialog = $.dialog();
	var commAjax1;
	
	commAjax1 = $.commAjax({
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
					lHtml += '<tr class="lHtml" onclick="choseStockCode(\''+datas[i].stockCode.trim() +'\',\''+datas[i].stockName.trim() +'\')" ><td class="tb-left">'+
						'<p class="labelIcon-single"><span class="am-ft-md am-ft-main">'+datas[i].stockName+'</span></p>'+
						'<p class="icon-USStock"><span class="am-ft-sm am-ft-gray">'+datas[i].stockCode+'</span></p>'+
						  '</td></tr>';
					
					rHtml += '<tr class="rHtml" onclick="choseStockCode(\''+datas[i].stockCode.trim() +'\',\''+datas[i].stockName.trim() +'\')">'+
				    '<td class="tb-center">'+datas[i].currentAmount+'</td>'+
				    '<td class="tb-center">'+datas[i].enableAmount+'</td>'+
				    '<td class="tb-center">'+(parseFloat(datas[i].costBalance/100).toFixed(2)-0)+'元</td>'+
				    '<td class="tb-right">'+(parseFloat(datas[i].marketValue/100).toFixed(2)-0)+'元</td>'+
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
	//键盘联想
	$("#stockCode").keyup(function(){
		if(container.timeout){
			clearTimeout(container.timeout);
		}		
		container.timeout = setTimeout(getStockCode, 500);
	});
	
	container.oldStockCode = "";
	$(document).click(function(e){

		var target = event.target?event.target:event.srcElement;
		var evEL = $(target);
		if(!evEL.hasClass("codeListLi") && evEL.attr("id")  != "stockCode" && container.stockShowBool){

			$("#codeList").hide();
			$("#codeList li").eq(0).click();
		}else{
			return;
		}

	});
	

	var commAjaxDialog = $.dialog();
	//初始化获取信息
	$.commAjax({
		url:container.combineidUrl,
		success:function(data){
			commAjaxDialog.close();
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
					$("#choseHoms_dummy").css({width:"100%",border:"none",height:"21px",fontSize:"12px"});
					choseHomsSelect.change(function(){
						choseHoms($(this).val());
					});
					
					
					for(var i in container.homsDatas){
						homsHtml += createHomsHtml(i);	
					}
					$("#panel2 ul").html(homsHtml);
					getEnableWithdraw();
					
				}else{
					$.dialog({
						content:"无交易子账号",
						title:"alert",
						ok:function(){}
					});
				}
			}else{
				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){
					}
				});
			}
		
			
			$(document).click(function(event){
				if(container.proceFocus != 1){
					return;
				}
				var target = event.target?event.target:event.srcElement;
				var evEL = $(target);
				while(true){
					if(evEL.attr("id")=="pricePanel"){
						break;
					}else if($("#pricePanel",evEL).length != 0){
						container.proceFocus = 0;
						var input = $("#priceSpinner");
						if(input.val()==""||input.val() == container.priceSpinner ){
							break;
						}
						container.priceSpinner = input.val();
						if(container.needAmountAjax){
							container.priceBool = false;
							container.needAmountAjax.abort();
						}
						queryAmount();
						break;
					}
					evEL = evEL.parent();
				}
			});
			
			$("#pricePanel").click(function(){
				container.proceFocus = 1;
			});
			
			$("#sellBtn").click(function(){
				if(!container.homsData){
					$.dialog({
						content:"无交易子账号",
						title:"alert",
						ok:function(){}
					});
					return;
				}
				
				var price = $("#priceSpinner").val();
				if(price <= 0.01 || price>10000){
					$.dialog({
						content:"委托价格超出范围",
						title:"alert",
						ok:function(){}
					})
					return;
				}
				
				var sellNumber = $("#sellNumber").val();
				if(sellNumber < 1 || sellNumber>1000000){

					$.dialog({
						content:"卖出数量超出范围",
						title:"alert",
						ok:function(){}
					})
					return;
				}
				
				var pwd = $("#tradePwd").val();
				if(pwd.length<6 || pwd.length>18){
					$.dialog({
						content:"密码格式不正确",
						title:"alert",
						ok:function(){}
					})
					return;
				}
				$.dialog({
					content:"确定要卖出吗?",
					title:"alert",
					ok:function(){
						setTimeout(sellAction,0)
					},
					cancel:function(){}
					
				});
				
				
			});
		}
	});
	
/*	$(".tradingIndex-rside").css({
		width:$(document).width()-160
	});
	$(".tradingIndex-rside").scroll(function(){
		$("#arrowShape").hide();
	});*/
	
	var stock = request("stock");
	if(stock != ""){
		container.stockAjax = $.commAjax({
			url:container.stockDataUrl+"/"+stock,
			success:function(data){
				if(data.success){
					var datas = data.data;
					if(datas.length == 1){
						choseStockCode(datas[0].stockCode,datas[0].stockName);
					}
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
	
	$("#reset").click(function(){
		$.dialog({
			title:"alert",
			content:"确定要重置吗？",
			ok:function(){
				if(container.realTimeout){
					clearTimeout(container.realTimeout);
				}
				$("#stockCode").val("");
				$("#showCodeDiv").empty();
				$("#priceSpinner").val("");
				$("#cSellNumber").empty();
				$("#sellNumber").val("");
				$("#buyPrice1").empty();
				$("#buyCount1").empty();
				$("#buyPrice2").empty();
				$("#buyCount2").empty();
				$("#buyPrice3").empty();
				$("#buyCount3").empty();
				$("#buyPrice4").empty();
				$("#buyCount4").empty();
				$("#buyPrice5").empty();
				$("#buyCount5").empty();
				$("#sellPrice1").empty();
				$("#sellCount1").empty();
				$("#sellPrice2").empty();
				$("#sellCount2").empty();
				$("#sellPrice3").empty();
				$("#sellCount3").empty();
				$("#sellPrice4").empty();
				$("#sellCount4").empty();
				$("#sellPrice5").empty();
				$("#sellCount5").empty();
				container.curNumber = null;
			},
			cancel:function(){}
		});
	});
	
	$("#scrollTable").scroll(function(){
		if($(this).scrollLeft() != 0){
			$("#arrowShape").remove();
			$(this).unbind("scroll");
		}
	});
	
	var amouts = $("#choseCount li");
	var amoutArr = [4,2,1];
	amouts.click(function(){
		amouts.removeClass("active");
		$(this).addClass("active");
		if(container.curNumber){
			$("#sellNumber").val(parseInt(container.curNumber/amoutArr[amouts.index($(this))]));
		}
	});
});

function sellAction(){
	var dialog = $.dialog();
	$.commAjax({
		url:container.sellUrl,
		data:{
			homs_combine_id:container.homsData.homsCombineId,
			homs_fund_account:container.homsData.homsFundAccount,
			stock_code:$("#stockCode").val(),
			entrust_quantity:$("#sellNumber").val(),
			entrust_price: $("#priceSpinner").val(),
			exchange_type:container.exchangeType,
			trade_pwd:RSAUtils.encryptedString(key, $("#tradePwd").val())
		},
		type:"post",
		success:function(data){
			dialog.close();
			if(data.success){
				$.dialog({
					content:"卖出成功",
					title:"ok",
					ok:function(){
						window.location.href = mobileUrl + "/trade/sell.htm";
					}
				})
			}else{
				$.dialog({
					content:data.msg,
					title:"alert",
					ok:function(){}
				})
			}
		}
	});
}