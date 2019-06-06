
define([
	    'dojo/dom',
	    'dojo/dom-style',
	    'dojo/dom-class',
	    'dojo/dom-construct',
	    'dojo/on',
	    'app/common/Date',
		'dojo/query',
		'app/common/Ajax',
		'app/common/Global',
		'app/controllers/Helper',
		'dojo/domReady!'
	], function(dom, domStyle, domClass, domConstruct, on,DateUtil, query, Ajax, Global, Helper) {
		
		function initView() {
			var id = Global.getUrlParam('id');
	    	//获取资讯内容
	    	Ajax.get(Global.baseUrl + '/info/contentPage',{
	          	'id':id
	      	}).then(function(response){
	      		if(response.success){
	      			dom.byId('infoTitle').innerHTML = response.data.items[0].infoTitle;
	      			dom.byId('createDatetime').innerHTML = DateUtil.format(new Date(parseInt(response.data.items[0].createDatetime)));
	      			dom.byId('infoContent').innerHTML = response.data.items[0].infoContent;
	      		}
	      		else{
	      			//TODO
	      		}
	      	});
		}
		
		return {
			init: function() {
				initView();
				Helper.init();
			}
		}
	});