/*个人配置展示*/
var container = {};
// 合约列表
container.p2pSubject = basePath + "/p2p/subject/page";

function createP2PHtml(subject){
	return '<div class="bg-white con-index">'
	+'<aside class="lineBottom-gray am-pb-15 am-ft-center">'
	+'<p>'
	+ subject.invsName
	+'</p>'
	+'</aside>'
	+'<aside class="am-ft-center pt-1">'
	+'<big class="am-ft-red" >'
	+ (subject.invsRate*100).toFixed(2)
	+'<span class="am-ft-xxxl">%</span></big>'
	+'<p><span class="am-ft-tabText">年化收益</span></p>'
	+'</aside>'
	+'<aside class="am-ft-center pt-1">'
	+'<p class="am-ft-tabText">已完成<span class="am-ft-red">'
	+ parseFloat(subject.invsAmountActual/subject.invsAmountPre*100).toFixed(2)
	+'%</span><span class="bar">｜</span>还需资金<span class="am-ft-red">'
	+ parseInt((subject.invsAmountPre-subject.invsAmountActual)/100)
	+'</span>元</p>'
	+'</aside>'
	+'<aside class="am-ft-center am-pt-20">'
	+'<div class="btn-page btn-page-center con-spacing15">'
	        +'<a href="'
	        +mobileUrl
	        +'/tenderDetail.htm?invs='+subject.invsId+'" class="am-button am-button-red">'
	       + '<span>立即投标</span>'
	       +'</a>'        
	+'</div>'
	+'<p><span class="am-ft-tabText">已有 '
	+ subject.invsNumPays
	+' 人投资</span></p>'
	+'</aside>'
	+'</div>'
}


$(function(){
	var swiper = new Swiper('.swiper-container', {
		pagination : '.pagination',
		loop : true,
		grabCursor : true,
		paginationClickable : true
	});
    $('#visit_pc').click(function() {
        document.cookie = 'visit_device=pc; path=/';
        window.location.href = "/";
    });
	$(".doc").show();
	var resize = function(e) {
		var query = $('.swiper-container');
		var clientW = query.width();
		$(".doc").css({
			"width" : clientW
		});
		$(".swiper-container").css({
			"height" : clientW / 360 * 149
		})

		$("#bellowDevice").css({
			"padding-top" : clientW / 360 * 149+42
		});
		
		$(".device").css({
			"height" : clientW / 360 * 149
		})
	}
	resize();
	$(window).bind('resize', resize);
	resize();
	setInterval(function() {
		swiper.swipeNext();
	}, 5000);
	
	
	$.commAjax({
		url : container.p2pSubject,
		data : {
			is_able_pay:"1",
			start:0,
			limit:1
		},
		success:function(data){
			if(data.success){
				if(data.data.items != null&&data.data.items.length != 0){
					$("#content").html(createP2PHtml(data.data.items[0]));
				}
			}else{
				$.dialog({
			        content : data.msg,
					title:"alert",
					ok:function(){}
				});
			}
		}
			
	});
});