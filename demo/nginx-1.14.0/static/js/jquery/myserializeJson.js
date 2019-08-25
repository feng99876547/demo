(function($) {
	$.fn.serializeJson = function() {
		var serializeObj = {};
		var array = this.serializeArray();
		var str = this.serialize();
		$(array).each(
				function() {
					if (serializeObj[this.name]) {
						if ($.isArray(serializeObj[this.name])
								&& this.value != "") {
							serializeObj[this.name].push(this.value);
						} else {
							if (this.value != "") {
								serializeObj[this.name] = [
										serializeObj[this.name], this.value ];
							}
						}
					} else {
						if (this.value != "") {
							serializeObj[this.name] = this.value;
						}
					}
				});
		return serializeObj;
	};
})(jQuery);