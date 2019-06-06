define([
        'app/controllers/Helper',
        'dojo/query',
        'dojo/on',
        'dojo/dom',
    	'dojo/dom-style',
    	'dojo/dom-class',
    	'dojo/_base/fx'
        ],function(Helper,query,on,dom,domStyle,domClass,fx){
	var config = {};
	function initView(){
	}
	
	return {
		init:function(){
			initView();
			Helper.init(config);
		}
	}
});