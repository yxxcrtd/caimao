define([
], function() {
    var dict = {
        contractStatus: {
            0: '操盘中',
            1: '终止'
        },
        productID: {
            1: '免费体验',
            2: '实盘大赛',
            3: '按月融资',
            4: '按日融资',
            5: '最新特惠'
        },

        pwdStrength: {
            1: '弱',
            2: '中',
            3: '强'
        },

        invsInterestType: {
            1: '息随本清'
        },
        
        verifyStatus: {
        	0: '待审',
        	1: '审核通过',
        	2: '审核不通过',
        	3: '审核不通过'
        },
        entrustStatus:{
        	'1':	'未报',
        	'2':    '待报',
        	'3':    '正报',
        	'4':	'已报',		
        	'5':	'废单',		
        	'6':	'部成',		
        	'7':	'已成',		
        	'8':	'部撤',		
        	'9':	'已撤',		
        	'a':	'待撤',		
        	'A':	'未撤',		
        	'B':	'待撤',		
        	'C':	'正撤',		
        	'D':	'撤认',		
        	'E':	'撤废',		
        	'F':	'已撤'		
        },
        entrustDirection:{
        	1:	'股票买入',		
        	2:	'股票卖出'		
        },
        
        //订单状态
        orderStatus:{
        	02:'待处理',
        	03:'成功',
        	04:'失败',
        	05:'已取消',
        	06:'待确认'
        },
        loanApplyStatus: {
            0: '申请中',
            1: '申请通过',
            2: '申请失败'
        }
    };
	return {
        get: function(code) {
            return dict[code];
        }
	}
});