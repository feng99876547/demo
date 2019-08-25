var mask = $("<div id='mask'></div>");
var maskDiv = $("<div/>");
maskDiv.css({
        "position": "absolute",
        "margin": "0",
        "background-color": "#ffffff",
        "height": document.documentElement.clientHeight,
        "filter": "alpha(opacity=30)",
        "opacity": "1",
        "overflow": "hidden",
        "width": document.documentElement.clientWidth,
        "z-index": "998"
    });
maskDiv.appendTo(mask);
var label = "系统加载中";
var maskMsgDiv = $('<div><img src="'+_webApp+'/images/frame/loading.gif"/></div>');
maskMsgDiv.append('<div>' + label + '</div>');
maskMsgDiv.css({
	"position": "absolute",
	"z-index": "999",
	"height":"200",
	"width":"100"
});
maskMsgDiv.css("top",Math.round(maskDiv.height()/2-maskMsgDiv.height()/2));
maskMsgDiv.css("left",Math.round(maskDiv.width()/2-maskMsgDiv.width()/2));
maskMsgDiv.appendTo(mask);
mask.appendTo($("body"));
window.onresize=function(){	
	maskDiv.height(document.documentElement.clientHeight);
	maskDiv.width(document.documentElement.clientWidth);
	maskMsgDiv.css("top",Math.round(maskDiv.height()/2-maskMsgDiv.height()/2));
	maskMsgDiv.css("left",Math.round(maskDiv.width()/2-maskMsgDiv.width()/2));
	adaptiveSize();
};
maskMsgDiv.click(function(){loaded();});
function systemLoaded(){
	setTimeout('$("#mask").remove()',500);//延时取消遮罩层
}