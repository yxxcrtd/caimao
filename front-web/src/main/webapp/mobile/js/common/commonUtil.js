$.extend($,{
	commAjax:function(json){
		if(json.data == null){
			json.data = {};
		}
		json.data["_"]= new Date().getTime();
		json.dataType = "json";
		return $.ajax(json);
	},
	// 写cookies
	setCookie : function(name, value) {
		var Days = 30;
		var exp = new Date();
		exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
		document.cookie = name + "=" + escape(value) + ";expires="
				+ exp.toGMTString();
	},
	// 读取cookies
	getCookie : function(name) {
		var arr, reg = new RegExp("(^| )" + name + "=([^;]*)(;|$)");

		if (arr = document.cookie.match(reg))

			return unescape(arr[2]);
		else
			return null;
	},
	// 删除cookies
	delCookie : function(name) {
		var exp = new Date();
		exp.setTime(exp.getTime() - 1);
		var cval = getCookie(name);
		if (cval != null)
			document.cookie = name + "=" + cval + ";expires="
					+ exp.toGMTString();
	},
	// 身份证号码验证
	certificateValidate : function(idcard) {
		var Errors = new Array("0", "身份证号码位数不对!", "身份证号码出生日期超出范围或含有非法字符!",
				"身份证号码校验错误!", "身份证地区非法!");
		var area = {
			11 : "北京",
			12 : "天津",
			13 : "河北",
			14 : "山西",
			15 : "内蒙古",
			21 : "辽宁",
			22 : "吉林",
			23 : "黑龙江",
			31 : "上海",
			32 : "江苏",
			33 : "浙江",
			34 : "安徽",
			35 : "福建",
			36 : "江西",
			37 : "山东",
			41 : "河南",
			42 : "湖北",
			43 : "湖南",
			44 : "广东",
			45 : "广西",
			46 : "海南",
			50 : "重庆",
			51 : "四川",
			52 : "贵州",
			53 : "云南",
			54 : "西藏",
			61 : "陕西",
			62 : "甘肃",
			63 : "青海",
			64 : "宁夏",
			65 : "新疆",
			71 : "台湾",
			81 : "香港",
			82 : "澳门",
			91 : "国外"
		}
		var Y, JYM;
		var S, M;
		var idcard_array = new Array();
		idcard_array = idcard.split("");
		if (area[parseInt(idcard.substr(0, 2))] == null)
			return Errors[4];
		switch (idcard.length) {
		case 15:
			if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0
					|| ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard
							.substr(6, 2)) + 1900) % 4 == 0)) {
				ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/; // 测试出生日期的合法性
			} else {
				ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/; // 测试出生日期的合法性
			}
			if (ereg.test(idcard))
				return Errors[0];
			else
				return Errors[2];
			break;
		case 18:
			if (parseInt(idcard.substr(6, 4)) % 4 == 0
					|| (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard
							.substr(6, 4)) % 4 == 0)) {
				ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/; // 闰年出生日期的合法性正则表达式
			} else {
				ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/; // 平年出生日期的合法性正则表达式
			}
			if (ereg.test(idcard)) {
				S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10]))
						* 7
						+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11]))
						* 9
						+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12]))
				
						* 10
						+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13]))
						* 5		+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14]))
						* 8
						+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15]))
						* 4
						+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16]))
						* 2 + parseInt(idcard_array[7]) * 1
						+ parseInt(idcard_array[8]) * 6
						+ parseInt(idcard_array[9]) * 3;
				Y = S % 11;
				M = "F";
				JYM = "10X98765432";
				M = JYM.substr(Y, 1);
				if (M == idcard_array[17])
					return Errors[0];
				else
					return Errors[3];
			} else
				return Errors[2];
			break;
		default:
			return Errors[1];
			break;
		}
	},
	//bankno为银行卡号 banknoInfo为显示提示信息的DIV或其他控件
	luhmCheck:function(bankno){
		var lastNum=bankno.substr(bankno.length-1,1);//取出最后一位（与luhm进行比较）
		 
	    var first15Num=bankno.substr(0,bankno.length-1);//前15或18位
	    var newArr=new Array();
	    for(var i=first15Num.length-1;i>-1;i--){    //前15或18位倒序存进数组
	    newArr.push(first15Num.substr(i,1));
	    }
	    var arrJiShu=new Array();  //奇数位*2的积 <9
	    var arrJiShu2=new Array(); //奇数位*2的积 >9
	     
	    var arrOuShu=new Array();  //偶数位数组
	    for(var j=0;j<newArr.length;j++){
	    if((j+1)%2==1){//奇数位
	        if(parseInt(newArr[j])*2<9)
	        arrJiShu.push(parseInt(newArr[j])*2);
	        else
	        arrJiShu2.push(parseInt(newArr[j])*2);
	    }
	    else //偶数位
	    arrOuShu.push(newArr[j]);
	    }
	     
	    var jishu_child1=new Array();//奇数位*2 >9 的分割之后的数组个位数
	    var jishu_child2=new Array();//奇数位*2 >9 的分割之后的数组十位数
	    for(var h=0;h<arrJiShu2.length;h++){
	    jishu_child1.push(parseInt(arrJiShu2[h])%10);
	    jishu_child2.push(parseInt(arrJiShu2[h])/10);
	    }       
	     
	    var sumJiShu=0; //奇数位*2 < 9 的数组之和
	    var sumOuShu=0; //偶数位数组之和
	    var sumJiShuChild1=0; //奇数位*2 >9 的分割之后的数组个位数之和
	    var sumJiShuChild2=0; //奇数位*2 >9 的分割之后的数组十位数之和
	    var sumTotal=0;
	    for(var m=0;m<arrJiShu.length;m++){
	    sumJiShu=sumJiShu+parseInt(arrJiShu[m]);
	    }
	     
	    for(var n=0;n<arrOuShu.length;n++){
	    sumOuShu=sumOuShu+parseInt(arrOuShu[n]);
	    }
	     
	    for(var p=0;p<jishu_child1.length;p++){
	    sumJiShuChild1=sumJiShuChild1+parseInt(jishu_child1[p]);
	    sumJiShuChild2=sumJiShuChild2+parseInt(jishu_child2[p]);
	    }     
	    //计算总和
	    sumTotal=parseInt(sumJiShu)+parseInt(sumOuShu)+parseInt(sumJiShuChild1)+parseInt(sumJiShuChild2);
	     
	    //计算Luhm值
	    var k= parseInt(sumTotal)%10==0?10:parseInt(sumTotal)%10;       
	    var luhm= 10-k;
	     
	    if(lastNum==luhm){
	    //$("#banknoInfo").html("Luhm验证通过");
	    return true;
	    }
	    else{
	    //$("#banknoInfo").html("银行卡号必须符合Luhm校验");
	    return false;
	    }    	
	},
	parseMoney :function(money){
		return money.split(",").join("");
	}
});

function limit_money_input() { 
	var moneyInput = $("input.money");
	moneyInput.bind("contextmenu", function(){  
        return false;  
    });  
  
	moneyInput.css('ime-mode', 'disabled');  
	 var value = null;
	moneyInput.bind("keydown", function(e) { 
		value = $(this).val();
        var key = window.event ? e.keyCode : e.which;
        if(isNumber(key)&&/\.\d{2}/.test(moneyInput.val())&&(moneyInput.val().length-moneyInput.focus()[0].selectionStart)<=2){
        	return false
        }
        if (isFullStop(key)) {  
            return $(this).val().indexOf('.') < 0 && $(this).val().length != 0;  
        } 
        return (isSpecialKey(key)) || (isNumber(key) && !e.shiftKey);
    }); 
    
    var reg = /^(([1-9]\d{0,9})|0)(\.\d{0,2})?$/;

    moneyInput.bind("keypress", function(e) {
        if(reg.test($(this).val())){
        	value = $(this).val();
        }else{
        	$(this).val(value);
        }
    }); 
    
	moneyInput.bind("blur",function(){
		if(moneyInput.val() == "" || moneyInput.val() == "."){
			moneyInput.val("");
			return;
		}
		var n = parseFloat(moneyInput.val()).toLocaleString();
		moneyInput.val(n)
    });
	moneyInput.bind("focus",function(){
		var n = moneyInput.val().split(",").join("");
		moneyInput.val(n);
    });
}  
  
function isNumber(key) {  
    return key >= 48 && key <= 57  
}
  
function isSpecialKey(key) {  
    //8:backspace; 46:delete; 37-40:arrows; 36:home; 35:end; 9:tab; 13:enter  
    return key == 8 || key == 46 || (key >= 37 && key <= 40) || key == 35 || key == 36 || key == 9 || key == 13  
}
  
function isFullStop(key) {  
    return key == 190 || key == 110;  
}



$(limit_money_input);



String.prototype.format=function()  
{  
  if(arguments.length==0) return this;  
  for(var s=this, i=0; i<arguments.length; i++)  
    s=s.replace(new RegExp("\\{"+i+"\\}","g"), arguments[i]);  
  return s;  
};


