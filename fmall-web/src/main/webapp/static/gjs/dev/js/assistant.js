$(function(){
	var btn1=$('.followBtn1'),btn2=$('.followBtn2'),mark=$('.followMark');
	$(".followBtn").click(function(){
		mark.css("display",'block');
	});
	$(".no").click(function(){
		mark.css("display",'none');
	});
	$(".yes").click(function(){
		mark.css("display",'none');
		if (btn1.css('display')=='none') {
			btn2.css("display",'none');
			btn1.css("display",'block');
		}else{
			btn1.css("display",'none');
			btn2.css("display",'block');
		};
	});
});