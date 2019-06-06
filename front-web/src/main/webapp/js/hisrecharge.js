$(function () {
		//初始化列表模板
 		initTemplate();
 		//相应菜单加载样式
 		$("#jsTopMenu > li:eq(3) > a").addClass("hov");
		$(".left_bar > ul > li:eq(1) > a").addClass("hov");
		//日期控件
    	$('#txtBeginDate').datetimepicker({
			yearOffset:0,
			lang:'ch',
			timepicker:false,
			format:'Y-m-d',
			formatDate:'Y-m-d',
			minDate:'2015-01-01',
			maxDate:"+1970/01/02",
			value:getDate(new Date()),
			onChangeDateTime:function() {
				$(".chong1-shang_r button").attr("style","");
				$('#txtEndDate').datetimepicker({minDate:$('#txtBeginDate').val()});
			}
		});

		$('#txtEndDate').datetimepicker({
			yearOffset:0,
			lang:'ch',
			timepicker:false,
			format:'Y-m-d',
			formatDate:'Y-m-d',
			minDate:'2015-01-01',
			maxDate:"+1970/01/02",
			value:getDate(new Date()),
			onChangeDateTime:function(){
				$(".chong1-shang_r button").attr("style","");
				$('#txtBeginDate').datetimepicker({maxDate:$('#txtEndDate').val()});
			}
		});
		//加载数据
		showHis();
		//上一页
		$("#prev").click(function(){
			if(current>1){
				showHis({start:(current-2)*limit});
			}
		});
		//下一页
		$("#next").click(function(){
			if(current<pages){
				showHis({start:current*limit});
			}
		});
		//阶段选择
		$(".chong1-shang_r button").click(function(){
			$(".chong1-shang_r button").removeAttr("style");
			$(this).css({"background-color":"#5687CA",color:"#ffffff"});
			var i = $(this).index(".chong1-shang_r button");
			$('#txtEndDate').val(getDate(new Date()));
			switch(i){ 
				case 0: 
 					$('#txtBeginDate').val(getDate(new Date()));
					break; 
				case 1: 
 					$('#txtBeginDate').val(getDate(addDays(new Date(),-7)));
					break;
				case 2:
					$('#txtBeginDate').val(getDate(addMonths(new Date(),3)));
					break;
				case 3:
					$('#txtBeginDate').val(getDate(addMonths(new Date(),6)));
					break;
			}
			showHis();
		});
		//状态选择
		$(".chong1-xia button").click(function(){
			$(".chong1-xia button").removeAttr("style");
			$(this).css({"background-color":"#5687CA",color:"#ffffff"});
			showHis();
		});
		
		$("#search").click(function(){
			showHis();
		});
});
var lineTemplate;
var currentPage=1,limit=10,pages=1;
var status={
02:'待处理',
03:'成功',
04:'失败',
05:'已取消',
06:'待确认'
}
var color = function(n){
	if(n=="02"){
		return 'color: #eb4636;';
	}else if(n=="03"){
		return 'color: #6EBF0A;';
	}
	return "";
}
var channel={
"-1":'支付宝',
"-2":'银行转账',
"3":'网银充值'
}

var template = function() {
/*<div class="chong3">
	<ul>
	    <li style="padding-left: 0px;">#orderNo#</li>
	    <li>#orderAmount#</li>
	    <li style="padding-left: 120px;"><span style="color: #686868;">#createDatetime#</span></li>
	    <li style="padding-left: 115px;#color#">#orderStatus#</li>
	    <li style="padding-left: 140px;">#channelId#</li>
	    <li style="padding-left: 135px;">#operator#</li>
	</ul>
</div>*/
}

var initTemplate = function(){
	lineTemplate = new String(template);
	lineTemplate = line.substring(line.indexOf("/*") + 3,line.lastIndexOf("*/"));
}

var generate = function(item){
	if(!lineTemplate){
		initTemplate();
	}
	return lineTemplate.replace("#orderNo#",item.orderNo).replace("#orderAmount#",toYuanAndCommas(item.orderAmount)).replace("#createDatetime#",getDate(item.createDatetime)).replace("#orderStatus#",status[item.orderStatus]).replace("#color#",color(item.orderStatus)).replace("#channelId#",channel[item.channelId]).replace("#operator#",item.channelId=="-1"?'<a href="/account/recharge/zhifubao.htm" style="color:#44ACEB;cursor:pointer;">再充值</a>':"");
}

var showHis = function(options){
	var defaults = {start_date:$('#txtBeginDate').val(),end_date:$('#txtEndDate').val(),status:$(".chong1-xia button[style]").attr("no"),start:0,limit:limit};
	var data = $.extend({}, defaults, options); 
	$.get("/account/charge/page",data,function(response){
		if(response.success){
			current = parseInt(response.data.start)/parseInt(response.data.start)+1;
			pages=parseInt(response.data.pages);
			var list="";
			for(var item in response.data.items){
				list+=generate(item);
			}
			$(".chong3").remove();
			$(".chong2").after(list);
		}
	})
}

