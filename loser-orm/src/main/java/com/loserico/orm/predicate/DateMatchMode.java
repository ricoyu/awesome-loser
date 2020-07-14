package com.loserico.orm.predicate;

public enum DateMatchMode {
	// Two dates are exact the same
	EXACT,
	// Date1 is earlier than date2
	EARLIER_THAN,

	EARLIER_THAN_OR_SAME,

	LATER_THAN,

	LATER_THAN_OR_SAME,
	// Include later than or same day as date1, earlier than date2
	BETWEEN;
}