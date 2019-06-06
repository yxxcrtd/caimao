define([
	'dojo/date',
	'dojo/date/stamp',
	'dojo/date/locale'
], function(date, stamp, locale) {
	return {
		format: function(date, pattern) {
//			return stamp.toISOString(date, {
//				selector: 'date'
//			})
            return locale.format(date, {datePattern: pattern || 'yyyy-MM-dd', selector: "date"});
		},
		
		parse: function(dateStr, pattern) {
			return locale.parse(dateStr, {datePattern: pattern || 'yyyy-MM-dd', selector: 'date'});
		},

        formatStr: function(dateStr, pattern, toPattern) {
            return this.format(this.parse(dateStr, pattern), toPattern);
        },
	
		difference: function(date1, date2, unit) {
			return date.difference(date1, date2, unit || 'day');
		},
		
		add: date.add,
		
		today: function() {
			return new Date(this.format(new Date()));
		}
	};
});