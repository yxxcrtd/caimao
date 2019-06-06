define([ 
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'app/common/Global',
    'dojo/on',
    'dojo/mouse',
    'dojo/dom-class',
    'dojo/dom-style',
    'dojo/store/Memory',
    'app/ux/GenericTooltip',
    'app/ux/GenericSelect',
    'dojo/text!./templates/GenericPaginationBox.html'
], function (declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, 
		Global, on, mouse, domClass, domStyle, Memory, Tooltip, Select, template) {
	return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
		templateString: template,
		
		toggleButton: function(name, disabled) {
			if (name === 'prev') {
				domClass[disabled ? 'add' : 'remove'](this.prevBtnNode, 'genericButtonDisabled');
			}
			else if (name === 'next') {
				domClass[disabled ? 'add' : 'remove'](this.prevBtnNode, 'genericButtonDisabled');
			}
		},
		
		render: function() {
			var me = this;
			me.pageSelect = new Select({
				options: [{
					label: '5',
					value: 5
				}, {
					label: '10',
					value: 10
				}, {
					label: '20',
					value: 20
				}], 
				style: 'width: 50px; height: 25px;',
				textStyle: {
					width: '25px'
				}
			});
			me.pageSelect.placeAt(me.selectNode);
			me.toggleButton('prev', true);
		},
		
		postCreate: function() {
    		var me = this;
    		me.render();
    	    this.inherited(arguments);
    	}
	});
});