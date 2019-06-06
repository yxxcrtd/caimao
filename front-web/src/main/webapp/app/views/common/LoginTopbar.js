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
    'app/common/Ajax',
    'app/common/Global',
    'app/common/Data',
    'dojo/when',
    'dojo/_base/config',
    'dojo/text!./templates/LoginTopbar.html'
], function (declare, _WidgetBase, _TemplatedMixin, ViewMixin, on, query, Fx,
             domClass, domStyle, Ajax, Global, Data, when,Config, template) {
    return declare([_WidgetBase, _TemplatedMixin, ViewMixin], {
        templateString: template,

        username: '',

        _setUsernameAttr: function (value) {
            this.usernameNode.innerHTML = value;
        },

        render: function () {
            var me = this;
            on(me.logoutNode, 'click', function () {
                Ajax.post(Global.baseUrl + '/user/logout', {
                }).then(function (response) {
                    if (response.success) {
                        location.href = Global.baseUrl + '/user/login.htm';
                    } else {
                        location.href = Global.baseUrl + '/user/login.htm';
                    }
                });
            });
            when(Data.getUserExtra()).then(function(data) {
                if (data.userPhoto) {
                    me.avatarNode.src = Config.baseUrl+ 'avatars/' + data.userPhoto + '?' + (new Date()).valueOf();
                }
            });
        },

        buildRendering: function () {
            var me = this;
            me.inherited(arguments);
            me.render();
        }
    });
});