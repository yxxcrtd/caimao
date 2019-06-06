
	define([
	    'dojo/dom',
	    'dojo/dom-style',
	    'dojo/dom-class',
	    'dojo/dom-construct',
	    'dojo/on',
		'dojo/query',
		'app/common/Ajax',
		'app/common/Global',
		'app/controllers/Helper',
		'dojo/domReady!'
	], function(dom, domStyle, domClass, domConstruct, on, query, Ajax, Global, Helper) {

        var  urlParams = Global.getUrlParam(),
            id = urlParams.id;

		function initView() {
			// request data by ajax, render data
            // 获取公告信息
            Ajax.get(Global.baseUrl + '/content/notice_info?id='+id).then(function (response) {
                if (response.success) {
                    dom.byId("noticeTitlePoint").innerHTML = response.data.title;
                    dom.byId("noticeContentPoint").innerHTML = response.data.content;
                    dom.byId("noticeSourcePoint").innerHTML = response.data.source;
                    dom.byId("noticeCreatedPoint").innerHTML = new Date(parseInt(response.data.created)).toLocaleDateString();
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