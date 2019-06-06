$(function () {
		//初始化列表模板
 		initTemplate();
 		//相应菜单加载样式
 		$("#jsTopMenu > li:eq(3) > a").addClass("hov");
		$(".left_bar > ul > li:eq(3) > a").addClass("hov");
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
		//类型选择
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
var type = function(n){
	switch (n){
		case '01':
			return '充值';
		case '02':
			return '取现';
		case '03':
			return '借款';
		case '05':
			return '还款';
		case '06':
			return '利息';
		case '09':
			return '交易转入';
		case '10':
			return '交易转出';
		default :
			return '其他';
	}
}
var template = function() {
/*<div class="chong3">
	<ul>
	    <li style="padding-left: 0px;">#id#</li>
	    <li>#userRealName#</li>
	    <li style="padding-left: 140px;"><span style="color: #686868;">#transDatetime#</span></li>
	    <li style="padding-left: 50px;">#accountBizType#</li>
	    <li style="padding-left: 135px; color: #color#">#transAmount#</li>
	    <li style="padding-left: 130px;">#postAmount#</li>
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
	return lineTemplate.replace("#id#",item.id).replace("#userRealName#",item.userRealName).replace("#transDatetime#",item.transDatetime).replace("#accountBizType#",type(item.accountBizType)).replace("#color#",item.transAmount>0?"#eb4636;":"#5789CE;").replace("#transAmount#",(item.transAmount>0?"+":"")+toYuanAndCommas(item.transAmount)).replace("#postAmount#",toYuanAndCommas(postAmount));
}

var showHis = function(options){
	var defaults = {start_date:$('#txtBeginDate').val(),end_date:$('#txtEndDate').val(),biz_type:$(".chong1-xia button[style]").attr("no"),start:0,limit:limit};
	var data = $.extend({}, defaults, options); 
	$.get("/account/jour/page",data,function(response){
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

