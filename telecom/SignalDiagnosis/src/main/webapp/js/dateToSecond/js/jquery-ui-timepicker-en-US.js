/* 汉化 Datepicker 和 Timepicker。Written by 田志良 */
(function ($) {
    // 汉化 Datepicker
    $.datepicker.regional['zh-CN'] =
    {
        clearText: 'Clear', clearStatus: 'Remove the selected date',
        closeText: 'Close', closeStatus: "Don't change the current selection",
        prevText: '&lt;Last month', prevStatus: 'Show last month',
        nextText: 'Next month&gt;', nextStatus: 'Show next mont',
        currentText: 'Today', currentStatus: 'Show this month',
        monthNames: ['January', 'February', 'March', 'April', 'May', 'June',
        'July', 'August', 'September', 'October', 'November', 'December'],
        monthNamesShort: ['One', 'Two', 'Three', 'Four', 'Five', 'Six',
        'Seven', 'Eight', 'Nine', 'Ten', 'Eleven', 'Twelve'],
        monthStatus: 'Select month', yearStatus: 'Select year',
        weekHeader: 'Week', weekStatus: 'Week in year',
        dayNames: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
        dayNamesShort: ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'],
        dayNamesMin: ['日', '一', '二', '三', '四', '五', '六'],
        dayStatus: 'Set the DD to start in a week', dateStatus: 'Select m Month d Day, DD',
        dateFormat: 'yy-mm-dd', firstDay: 1,
        initStatus: 'Please select date', isRTL: false
    };
    $.datepicker.setDefaults($.datepicker.regional['zh-CN']);

    //汉化 Timepicker
	$.timepicker.regional['zh-CN'] = {
		timeOnlyTitle: 'Please select time',
		timeText: 'Time',
		hourText: 'hour',
		minuteText: 'Minute',
		secondText: 'Second',
		millisecText: 'Microsecond',
		timezoneText: 'Timezone',
		currentText: 'Now the time',
		closeText: 'Close',
		timeFormat: 'hh:mm',
		amNames: ['AM', 'A'],
		pmNames: ['PM', 'P'],
		ampm: false
	};
	$.timepicker.setDefaults($.timepicker.regional['zh-CN']);
})(jQuery);
