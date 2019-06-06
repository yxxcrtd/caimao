define([
], function() {
	var o_parseInt = parseInt;
    parseInt = function(string, radix) { // fix parseInt function in IE8 if the radix undefined
        return o_parseInt(string, radix || 10);
    }
});