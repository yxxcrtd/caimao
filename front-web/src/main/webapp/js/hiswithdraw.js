$(function () {
		//初始化列表模板
 		initTemplate();
 		//相应菜单加载样式
 		$("#jsTopMenu > li:eq(3) > a").addClass("hov");
		$(".left_bar > ul > li:eq(2) > a").addClass("hov");
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
		case '02':
			return '待处理';
		case '03':
			return '成功';
		case '04':
			return '失败';
		case '05':
			return '已取消';
		default :
			return '--';
	}
}
var color = function(n){
	switch (n){
		case '02':
			return 'color: #eb4636;';
		case '03':
			return 'color: #6DBE09;';
		default :
			return '';
	}
}
var template = function() {
/*<div class="chong3">
	<ul>
	    <li style="padding-left: 0px;">#orderNo#</li>
	    <li><span style="color: #686868;">#createDatetime#</span></li>
	    <li style="padding-left: 110px; color: #eb4636;" >#orderAmount#</li>
	    <li style="padding-left: 145px;">#bankCardNo#</li>
	    <li style="padding-left: 90px;#color#">#orderStatus#</li>
	    <li style="padding-left: 140px;">#operate#</li>
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
	return lineTemplate.replace("#orderNo#",item.orderNo).replace("#createDatetime#",item.createDatetime).replace("#orderAmount#",toYuanAndCommas(item.orderAmount)).replace("#bankCardNo#",item.bankCardNo).replace("#orderStatus#",status(item.orderStatus)).replace("#operate#",item.orderStatus=='02'?'<a style="color:#44ACEB;cursor:pointer;">取消提现</a>':'--').replace("#color#",color(item.orderStatus));
}

var showHis = function(options){
	var defaults = {start_date:$('#txtBeginDate').val(),end_date:$('#txtEndDate').val(),status:$(".chong1-xia button[style]").attr("no"),start:0,limit:limit};
	var data = $.extend({}, defaults, options); 
	$.get("/account/withdraw/page",data,function(response){
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

