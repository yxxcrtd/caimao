define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/query',
    'app/ux/GenericButton',
    'dojo/on',
    'dojo/text!./templates/AvatarPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, Global, query, Button, on, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
	templateString: template,
	
	baseUrl: Global.baseUrl,
	upload: function() {},
	
	setValues: function(values) {
		for (var key in values) {
			this.set(key, values[key]);
		}
	},
	
	render: function() {
		var me = this;
        me.previewBtn = new Button({
            label: '选择图片',
            leftOffset: 0,
            width: 100
        });
		me.uploadBtn = new Button({
			label: '上传头像',
			disabled: true,
			enter:true,
			handler: me.upload
		});

        me.previewBtn.placeAt(me.previewBtnNode);
		
		me.uploadBtn.placeAt(me.uploadBtnNode);
	},
	
	postCreate: function() {
		var me = this;
		me.render();
		me.inherited(arguments);
	}});
});