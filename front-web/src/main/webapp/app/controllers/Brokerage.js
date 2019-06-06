define([
    'app/controllers/Helper',
    'dojo/_base/fx',
    'dijit/registry',
    'dojo/dom',
    'dojo/on',
    'app/common/Ajax',
    'dojo/query',
    'app/common/Global',
    'dojo/dom-style',
    'dojo/dom-class',
    'dojo/dom-construct',
    'dijit/focus',
    'dojo/cookie',
    'app/ux/GenericMiniMsgBox',
    'dojo/has',
    'dojo/_base/sniff',
    'dojo/_base/config',
    'app/common/Data',
    'dojo/when',
    'dojo/domReady!'
], function (Helper, fx, registry, dom, on, Ajax, query, Global, domStyle, domClass,
             domConstruct, focusUtil, cookie,
             MiniMsgBox, has, sniff, Config, Data, when) {

    function initView() {
        Ajax.get(Global.baseUrl + "/specialInterface/getCaimaoId").then(function(response){
            if(response.data){
                dom.byId('no_login_div').style.display = 'none';
                dom.byId('yes_login_div').style.display = 'block';
                dom.byId("popularTextUrl").value = location.protocol + '//' + location.host + Global.baseUrl + "?i=" + response.data;
                dom.byId("popularTextUrl2").value = location.protocol + '//' + location.host + Global.baseUrl + "?i=" + response.data;
                dom.byId("copy_url").setAttribute("data-clipboard-text", location.protocol + '//' + location.host + Global.baseUrl + "?i=" + response.data);
                dom.byId("copy_url01").setAttribute("data-clipboard-text", location.protocol + '//' + location.host + Global.baseUrl + "?i=" + response.data);
            }
        });

        Ajax.get(Global.baseUrl + "/activity/getBrokerageRank").then(function(response){
            if(response.data){
                var rank_html = "";
                for(var kk in response.data){
                    rank_html+='<li><span>'+(1+parseInt(kk))+'</span><span>'+response.data[kk].mobile+'</span><span>'+response.data[kk].pzCnt+'</span></li>';
                }
                dom.byId('rank1').innerHTML = rank_html;
            }
        });

        Ajax.get(Global.baseUrl + "/activity/getBrokerageRank2").then(function(response){
            if(response.data){
                var rank_html = "";
                for(var kk in response.data){
                    rank_html+='<li><span>'+(1+parseInt(kk))+'</span><span>'+response.data[kk].mobile+'</span><span>'+parseInt(response.data[kk].pzAmount / 100)+'</span></li>';
                }
                dom.byId('rank2').innerHTML = rank_html;
            }
        });
    }

    return {
        init: function () {
            initView();
            Helper.init();
        }
    }
});