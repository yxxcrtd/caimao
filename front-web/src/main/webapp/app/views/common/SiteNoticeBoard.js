/**
 * Created by xiexl on 2015/3/25.
 * Desc 顶部公告栏组件
 */
define([
    '../../../dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/views/ViewMixin',
    'dojo/query',
    'dojo/on',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/dom-construct',
    'app/common/Position',
    'app/common/Fx',
    'dojo/_base/config',
    'dojo/cookie',
    'dojo/json',
    'dojo/string',
    'dojo/text!./templates/SiteNoticeBoard.html',
    'app/jquery/$'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin,
             query, on, domClass, domStyle, domConstruct, Position, Fx,cfg,cookie,json,strUtil, template) {
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, ViewMixin], {
        templateString: template,

        show:function(){
            var me = this;
            //if must be close(can't hide or show), we will return it.
            if(true == me._IsMustClose()){
                return;
            }

            //set value again
            me._setNoticeInfo();

            domStyle.set(me.siteNoticeTipNode,"display","none");
            // me.siteNoticeTipNode.style.display="none";
            $(me.siteNoticeNode).slideDown(1000);

            //set cookie value
            //cookie("sn_isHide","false",{expires: 365 });
            cookie("sn_isHide", null, {expires: -1});
        },

        hide:function(){
            var me = this;
            //if must be close(can't hide or show), we will return it.
            if(true == me._IsMustClose()){
                return;
            }

            $(me.siteNoticeNode).slideUp(1000, function () {
                domStyle.set(me.siteNoticeTipNode, "display", "block");
                // me.siteNoticeTipNode.style.display="block";
            });

            //set cookie value
            cookie("sn_isHide","true",{expires: 365 });
        },

        //internal function, direct to hide ,don't use animation
        _directToHide:function(){
            var me = this;
           /* $(me.siteNoticeNode).slideUp(1, function () {
                domStyle.set(me.siteNoticeTipNode, "display", "block");
            });*/
            domStyle.set(me.siteNoticeNode,"display","none");
            domStyle.set(me.siteNoticeTipNode, "display", "block");
        },

        //internal function, direct to close notice function,can't be show again
        _close:function(){
            var me = this;
            /* $(me.siteNoticeNode).slideUp(1, function () {
             domStyle.set(me.siteNoticeTipNode, "display", "block");
             });*/
            domStyle.set(me.siteNoticeNode,"display","none");
            domStyle.set(me.siteNoticeTipNode, "display", "none");
        },

        //internal function, 0:hide,can be show again,1:show,2:close,can't be show again
        _getStatus:function(){
            //if the sitenotice title is null or empty,we will close the notice board
            if(cfg.siteNotice == undefined
                /*|| cfg.siteNotice.content == undefined
                || cfg.siteNotice.title == undefined
                || strUtil.trim(cfg.siteNotice.content).length==0*/
                || strUtil.trim(cfg.siteNotice.title).length==0){
                return 2;
            }
            else{
                //get local cookie value
                var bIsHide =cookie("sn_isHide");
                if(bIsHide!=undefined && bIsHide!=null && "true" == bIsHide){
                    //if user requested to hide the notice,we hide it.
                    return 0;
                }
                else{
                    return 1;
                }
            }
        },

        //internal function, current status is hidden
        _IsHidden:function(){
            var me = this;
            return (0==me._getStatus())
        },

       /* _IsShow:function(){
            var me = this;
            return (1==me._getStatus())
        },*/

        //internal function, current status is must to be close.
        _IsMustClose:function(){
            var me = this;
            return (2==me._getStatus())
        },

        //set title and content value
        _setNoticeInfo:function(){
            var me = this;
            //set title and content value
            me.siteNoticeTitleNode.innerHTML=cfg.siteNotice.title;
            me.siteNoticeContentNode.innerHTML=cfg.siteNotice.content;
        },

        // bind event handlers
        addListeners: function() {
            var me = this;
            on(me.siteNoticeTipNode,'click',function(){
                //Fx.scrollToY(0, null, 'easeInOutSine');
                me.show();
            });

            on(me.siteNoticeCloseNode,'click',function() {
                me.hide();
            });
        },

        postCreate: function() {
            var me = this;
            me.addListeners();

            //init stage, if the status is hidden from the cookie, we hide it
            if(me._IsHidden()){
                //if user requested to hide the notice,we will hide it.
                me._directToHide();
            }
            else if(me._IsMustClose()){
                //delete local cookie
                cookie("sn_isHide", null, {expires: -1});
                me._close();
            }
            else{//just to show
                //set title and content value
                me._setNoticeInfo();
            }

            this.inherited(arguments);
        }
    });
});
