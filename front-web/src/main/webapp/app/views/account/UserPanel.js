define([
    'dojo/_base/declare',
    'dijit/_WidgetBase', 
    'dijit/_TemplatedMixin',
    'dijit/_WidgetsInTemplateMixin',
    'dojo/text!./templates/UserPanel.html'
], function(declare, _WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin, template){
    return declare([_WidgetBase, _TemplatedMixin, _WidgetsInTemplateMixin], {
    	
    	templateString: template,
    	baseClass: 'user',
    	
    	data: function(values) {
    		for (v in values) {
    			if (this[v] && this[v].setValue) {
    				this[v].setValue(values[v]);
    			}
    		}
    	},
    	
    	fieldSetter: function() {
    		this.email.setValue = function(value) {
    			this.innerHTML = value;
    		};
    		this.security.setValue = function(value) {
    			this.set('value', value);
    		}
    		this.lastLoginIP.setValue = function(value) {
    			this.innerHTML = value;
    		}
    		this.lastLoginTime.setValue = function(value) {
    			this.innerHTML = value;
    		}
    	},
    	
    	postCreate: function() {
    		this.inherited(arguments);
    		this.fieldSetter();
    	}
    	
    });
});