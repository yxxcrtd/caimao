define([
    'dojo/_base/declare',
    'dijit/_WidgetBase',
    'dijit/_TemplatedMixin',
    'dojo/dom-construct',
    'dojo/dom-style',
    'dojo/dom-class',
    'dojo/_base/sniff',
    'dojo/text!./templates/GenericProgressBar.html',
    'dojo/i18n!./nls/GenericProgressBar'
], function(declare, _WidgetBase, _TemplatedMixin, domConstruct, domStyle, domClass, has, template, nls){
    return declare([_WidgetBase, _TemplatedMixin], {

        value: 0,

        templateString: template,
        baseClass: 'genericProgressBar',
        label: '',
        labelStyle: '',
        type: '',

        _setTypeAttr: function(type) {
            this._set('type', type);
        },

        decorate: function(value) {
            var target = this.barNode,
                textEl = this.textNode,
                innerStr = 'innerHTML';
            if (has('ie')) {
                innerStr = 'innerText'; // ie trick, strange
            }
            value = value || 0;
            domClass.remove(target, 'genericProgressBarBad');
            domClass.remove(target, 'genericProgressBarNormal');
            domClass.remove(target, 'genericProgressBarGood');
            domClass.remove(target, 'genericProgressBarGreat');
            switch(parseInt(value * 4)) {
                case 0:
                    if (this.type == '2') {
                        domClass.add(target, 'genericProgressBarGreat');
                    } else {
                        domClass.add(target, 'genericProgressBarBad');
                    }
                    textEl[innerStr] = nls.bad;
                    break;
                case 1:
                    domClass.add(target, 'genericProgressBarNormal');
                    textEl[innerStr] = nls.normal;
                    break;
                case 2:
                    domClass.add(target, 'genericProgressBarGood');
                    textEl[innerStr] = nls.good;
                    break;
                case 3:
                case 4:
                    if (this.type == '2') {
                        domClass.add(target, 'genericProgressBarGray');
                    } else {
                        domClass.add(target, 'genericProgressBarGreat');
                    }
                    textEl[innerStr] = nls.great;
                    break;
            }
            setTimeout(function() {
                domStyle.set(target, 'width', value * 100 + '%');
            }, 100);

        },

        _setTextAttr: function(value) {
            if (value) {
                this.textNode.innerHTML = value;
            }
        },

        _setValueAttr: function(value) {
            this._set('value', value);
            this.decorate(value);
        },

        _setLabelAttr: function() {
            this.labelNode = domConstruct.create('label', {innerHTML: this.params.label}, this.domNode, 'first');
            this.labelNode.style.cssFloat = 'left';
            this.labelNode.style.styleFloat = 'left';
        },

        _setLabelStyleAttr: function(value) {
            if (!this.labelNode && value) {
                this.labelNode = domConstruct.create('label', {innerHTML: this.params.label}, this.domNode, 'first');
            }
            domStyle.set(this.labelNode, value);
        },

        postCreate: function(){
            this.inherited(arguments);
        }
    });
});