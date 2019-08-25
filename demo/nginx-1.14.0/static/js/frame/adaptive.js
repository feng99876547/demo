//自适应浏览器大小 调整id="div_Right"的高度和宽度以及表格最后一列内容为空的表格宽度
function adaptiveSize(){
	var w_browse = document.documentElement.clientWidth;//获取浏览器宽度	
	var h_browse = document.documentElement.clientHeight;//获取浏览器高度
	//计算元素高度和宽度
	var h_header = $("#header").outerHeight(true);
	var h_footer = $("#footer").outerHeight(true);
	var w_main_left = $("#main_left").outerWidth(true);
	var w_main_right=(w_browse-w_main_left-8);
	var h_main_right = (h_browse-h_header-h_footer-2);	
	var h_main_right_content = (h_main_right-36);		
	//主要元素高宽设置					
	$("#main_right").css("width",w_main_right);
	$("#main_right").css("height",h_main_right);
	$("#main_right_content").css("height",h_main_right_content);
}
$(document).ready( function() {
	adaptiveSize();
	window.onresize = adaptiveSize;
});
