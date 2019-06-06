/**
 * This file is the application's main JavaScript file. It is listed as a dependency in run.js and will automatically
 * load when run.js loads.
 *
 * Because this file has the special filename 'main.js', and because we've registered the 'app' package in run.js,
 * whatever object this module returns can be loaded by other files simply by requiring 'app' (instead of 'app/main').
 *
 * In all cases, whatever function is passed to define() is only invoked once, and the returned value is cached.
 *
 * More information about everything described about the loader throughout this file can be found at
 * <http://dojotoolkit.org/reference-guide/loader/amd.html>.
 */
define(['dojo/_base/config', 'require', 'app/common/Fix', 'dojo/domReady!'], function (config, require, Fix) {
	var controller = config.controller || 'Login';
	// set the controller accoding the config param: controller
	function dispatcher(controller) {
		var reqPath = 'app/controllers/' + controller;
		require([reqPath], function(con) {
			con.init();
		});
	}
	dispatcher(controller);
});