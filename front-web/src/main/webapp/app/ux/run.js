require({
	baseUrl: '/front/',
	packages: [
		{name: 'dojo', location: 'dojo/dojo'},
		{name: 'dijit', location: 'dojo/dijit'},
		{name: 'dojox', location: 'dojo/dojox'},
		{name: 'app', location: 'app'}
	]
}, [ 
	'dijit/layout/BorderContainer', 
	'dijit/layout/ContentPane',
	'dijit/Fieldset',
	'dijit/registry',
	'dijit/form/Button',
	'dijit/layout/TabContainer',
	'dijit/form/HorizontalSlider',
	'dijit/form/HorizontalRuleLabels',
	'app/ux/GenericGrid',
	'app/ux/GenericProgressBar',
	'app/common/Message',
	'app/stores/GridStore',
	'dijit/form/RadioButton',
	'dijit/form/NumberTextBox',
	'dijit/form/CheckBox',
	'dojo/json',
	'dojo/dom-construct',
	'dojo/domReady!'
], function(BorderContainer, ContentPane, Fieldset, registry, Button, TabContainer, Slider, RuleLabels, Grid, ProgressBar, Message, Store, RadioButton, NumberTextBox, CheckBox, json, domConstruct) {
	
	var createGrid = function(parent, config) {
		var childWidgets = registry.findWidgets(parent.domNode),
			oldGrid,
			config = config || {},
			grid;
		if (childWidgets.length > 0) {
			oldGrid = childWidgets[1];
			oldGrid.destroy();
		}
		var formatAction = function(value, rowIndex, column) {
			return '<a href="###">买入</a><a href="###">卖出</a>';
		};
		var layout = [[
		    {'name': 'ext_flag', 'field': 'ext_flag', 'width': '10%', styles: 'text-align: center;', noresize: true, editable: true},
		    {'name': 'kind_code', 'field': 'kind_code', 'width': '10%', styles: 'text-align: center;', noresize: true, editable: true},
		    {'name': 'lifecycle', 'field': 'lifecycle', 'width': '10%', styles: 'text-align: center;', noresize: true, editable: true},
		    {'name': 'param_code', 'field': 'param_code', 'width': '10%', styles: 'text-align: center;', noresize: true},
		    {'name': 'param_desc', 'field': 'param_desc', 'width': '10%', styles: 'text-align: center;', noresize: true, editable: true},
		    {'name': 'param_name', 'field': 'param_name', 'width': '10%', styles: 'text-align: center;', noresize: true, editable: true},
		    {'name': 'param_regex', 'field': 'param_regex', 'width': '10%', styles: 'text-align: center;', noresize: true, editable: true},
		    {'name': 'param_value', 'field': 'param_value', 'width': '10%', styles: 'text-align: center;', noresize: true, editable: true},
		    {'name': 'platform', 'field': 'platform', 'width': '10%', styles: 'text-align: center;', noresize: true, editable: true},
		    {'name': 'rel_org', 'field': 'rel_org', 'width': '10%', styles: 'text-align: center;', noresize: true, editable: true}
		]],
			store = new Store({
	        target: '',
	        allowNoTrailingSlash: true,
	        idAttribute: 'param_code'
	        
	    }),
	    	gridCfg = {
			store: store,
			structure: layout
			//gridTitle: 'sysparam',
			//createButton: true,
			//deleteButton: true
		};
		if (!('page' in config)) {
			gridCfg.pageSize = 5;
		}
		if (config.page && config.pageSize) {
			gridCfg.pageSize = config.pageSize;
		}
		grid = new Grid(gridCfg);
		store.grid = grid;
		
		//grid.setStore(store);
		parent.addChild(grid);
		return grid;
	};
	
	var createGridConfig = function() {
		var configPanel = new ContentPane(),
			pageToggle = new CheckBox({
				checked: true,
				disabled: true,
				onChange: function(value) {
					//pageText.set('disabled', !value);
					configPanel.states.page = value;
					configPanel.states.pageSize = pageText.get('value');
					createGrid(registry.byId('Grid'), configPanel.states);
				}
			}),
			pageText = new NumberTextBox({
				value: 5,
				onKeyUp: function(event) {
					if (!isNaN(parseInt(this.displayedValue))) {
						configPanel.states.pageSize = parseInt(this.displayedValue);
						configPanel.states.page = true;
						createGrid(registry.byId('Grid'), configPanel.states);
					}
				}
			});
		
		configPanel.addChild(pageToggle);
		configPanel.addChild(pageText);
		domConstruct.create('label', {innerHTML: 'Page: '}, pageToggle.domNode, 'before');
		domConstruct.create('div', {innerHTML: 'currently grid not support: no paging, multi-delete, action message.', className: 'description'}, configPanel.domNode, 'first');
		
		configPanel.states = {};
		
		return configPanel;
	};
	
	var createRankBar = function(parent) {
		var rankBar = new ProgressBar({
			value: 0.75
		});
		parent.addChild(rankBar);
		return rankBar;
	};
	
	var createRankBarConfig = function(target) {
		var configPanel = new ContentPane();
		var slider = new Slider({
			name: 'slider',
	        value: 0.75,
	        minimum: 0,
	        maximum: 1,
	        intermediateChanges: true,
	        style: 'width:300px;',
	        onChange: function(value) {
	        	target.set('value', value);
	        }
		});
		var label = new RuleLabels({
			style: 'width:300px;height: 20px;',
			labels: ['0%', '100%']
		});
		configPanel.addChild(slider);
		configPanel.addChild(label);
		return configPanel;
	};
	
	var addButton = function(id, pos, onclick) {
		var button = new Button({
			label: id,
			onClick: onclick || function() {
				var panel = registry.byId(id);
				if (!panel) {
					var panel = new ContentPane({
						id: id,
						title: id
					});
					var fieldset = new Fieldset({
	    				title: 'Config',
	    				style: 'margin-bottom: 20px;'
	    			});
					target = eval('create' + id)(panel);
					fieldset.addChild(eval('create' + id + 'Config')(target));
	    			panel.addChild(fieldset, 0);
		    		//panel.addChild(target);
		    		tc.addChild(panel);
				}
				tc.selectChild(panel);
			}
		});
		pos.addChild(button);
	};
	
	var bc = new BorderContainer({
        style: "height: 100%; width: 100%;"
    });

    var cp1 = new ContentPane({
        region: "left",
        style: "width: 100px; overflow-x: hidden;"
    });
    bc.addChild(cp1);
    
    addButton('Grid', cp1);
    addButton('RankBar', cp1);
    addButton('MessageBox', cp1, function() {
    	Message.msg('test', 'test');
    });
    
    var tc = new TabContainer({
    	region: "center"
    });
    
    bc.addChild(tc);

    bc.placeAt(document.body);
    bc.startup();
});