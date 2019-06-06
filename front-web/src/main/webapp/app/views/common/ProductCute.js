define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'app/views/ViewMixin',
    'dojo/on',
    'dojo/query',
    'app/common/Fx',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/text!./templates/ProductCute.html'
], function(declare, _WidgetBase, _TemplatedMixin, ViewMixin, on, query, Fx, domClass, domStyle, template){
    return declare([_WidgetBase, _TemplatedMixin, ViewMixin], {
        templateString: template,

        cls: '',
        info: '',
        url: '',
        style: '',

        _setClsAttr: function(value) {
            domClass.add(this.imgNode, value);
        },

        _setStyleAttr: function(value) {
            domStyle.set(this.domNode, value);
        },

        _setUrlAttr: function(value) {
            this.linkNode.href = value;
        },

        _setInfoAttr: function(value) {
            this.infoNode.innerHTML = value;
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