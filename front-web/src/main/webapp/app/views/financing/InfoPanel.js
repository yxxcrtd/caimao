define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'app/views/ViewMixin',
    'dojo/dom',
    'dojo/dom-construct',
    'dojo/on',
    'dojo/query',
    'app/common/Fx',
    'dojo/dom-class',
    'dojo/dom-style',
    'app/common/Global',
    'dojo/text!./templates/InfoPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, ViewMixin, dom,
            domConstruct, on, query, Fx, domClass, domStyle, Global, template){
    return declare([_WidgetBase, _TemplatedMixin, ViewMixin], {
        templateString: template,

        data: '',
        title: '',

        _setTitleAttr: function(value) {
            this._set('title', value);
            this.titleNode.innerHTML = value;
        },

        _setDataAttr: function(data) {
            this.totalAssetNode.innerHTML = Global.formatAmount(data.totalAsset,2);
            this.totalMarketValueNode.innerHTML = Global.formatAmount(data.totalMarketValue,2);
            this.curAmountNode.innerHTML = Global.formatAmount(data.curAmount,2);
            this.enableRatioNode.innerHTML = Global.formatAmount(data.enableRatio * data.loanAmount,2);
            this.exposureRatioNode.innerHTML = Global.formatAmount(data.exposureRatio * data.loanAmount,2);
        },

        _setProductTypeAttr: function(type) {
            if (type == '4' || type == '0') {
                domConstruct.destroy(this.enableRatioCtn);
                domConstruct.destroy(this.exposureRatioCtn);
            }
        },

        render: function() {
            var me = this;
        },

        buildRendering: function() {
            var me = this;
            me.inherited(arguments);
            me.render();
        }
    });
});