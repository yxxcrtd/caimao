define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'app/common/Date',
    'dojo/string',
    'dojo/text!./templates/CheckPanel.html',
    'dojo/text!./templates/CheckPanelItem.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, query, dom, domConstruct, on, DateUtil, string, template, itemTemplate){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	
    	baseUrl: Global.baseUrl,
    	
    	list: '',
    	
    	_setListAttr: function(value) {
    		this._set('list', value);
    		var i  = 0, len = value.length, html = '';
    		var typeMap = {'0': '借款', '1': '追加', '2': '展期'};
    		for (; i < len; i++) {
    			var type = value[i]['loan_apply_action'];
    			var values = {
    				type: typeMap[type],
    				contract_link: value[i]['contract_no'] ? Global.baseUrl + '/financing/contract.htm?contract='+value[i]['contract_no'] : '',
    				contract_no: value[i]['contract_no'],
    				order_status: '审核中', //value[i]['verify_status'],
    				apply_datetime: value[i]['apply_datetime']
    			};
    			if (value[i].prod_id == '1') {
    				values.cls = 'icon-contractPu';
    			} else if (value[i].prod_id == '2') {
    				values.cls = 'icon-contractDuan';
    			} else {
    				values.cls = '';
    			}
    			if (type == '0') {
    				values.result = '<span>借款<b class="am-ft-orange"> '+Global.formatAmount(value[i]['order_amount'])+'</b> 元</span>';
    			} else if (type === '2') {
    				values.result = '<span>展期至<b class="am-ft-orange"> '+value[i]['contract_end_date']+'</b></span>';
    			} else if (type === '1') {
    				values.result = '<span>追加<b class="am-ft-orange"> '+Global.formatAmount(value[i]['order_amount'])+'</b> 元</span>';
    			} else {
    				values.result = '';
    			}
    			html += string.substitute(itemTemplate, values);
    		}
    		if (!html) {
    			html = '<div style="text-align: center;"><div style="color: #f0700c;font-size: 30px;margin-top:70px;">' +
    				'<i class="fa">&#xf118;</i><span style="color: #666;"> 您没有申请单</span></div>' +
    				'<div>您可以进行以下操作：<p>1.	查看<a href="'+Global.baseUrl+'/financing/apply/inquiry.htm">历史申请</a></p>' +
    				'<p>2.	查看<a href="'+Global.baseUrl+'/financing/loan.htm">当前借款</a>发起申请</p></div></div>';
    		}
    		domConstruct.place(domConstruct.toDom(html), this.domNode);
    	},
    	
    	setValues: function(values) {
    		for (var key in values) {
    			this.set(key, values[key]);
    		}
    	},
    	
    	postCreate: function() {
    		var me = this;
    		me.inherited(arguments);
    	}
    	
    });
});