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
    'dojo/text!./templates/LogoutTopbar.html'
], function(declare, _WidgetBase, _TemplatedMixin, ViewMixin, on, query, Fx, domClass, domStyle, template){
    return declare([_WidgetBase, _TemplatedMixin, ViewMixin], {
        templateString: template,

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