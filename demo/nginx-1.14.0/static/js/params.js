var root = location.pathname.split("/")[1];
//var $path='http://'+location.host+'/'+root;
var $path='http://'+location.host+'/newxin';
var host = 'http://'+location.host;
var _webApp = $path;

var suffix = ".do"
if(!window.FXC) FXC = {};
if(!window.CONSTANT) CTT = {};


var original_id = 0;

FXC.getId = function(){
	return original_id++;
}
/**
 * 用于一次性定义多层全局对象 没有创建 就创建
 */
FXC.ns = function(){
	var ns=function(n, o,name){
		if(n.length == 0) return;
		if(!o[n[0]]) o[n[0]] = {};
		ns(n.slice(1, n.length), o[n[0]],name);
	};
	
	return function(str){
		if(typeof str != 'string' || str.length == 0) return;
		str = str.split('.');
		ns(str,window,str);
	};
}();

/**
 * 用于全局常量定义 防止重复命名 如果存在赋值失败
 */
CTT.ns = function(){
	var ns=function(n, o,val,name){
		if(n.length == 0) return;
		if(!o[n[0]]){
			o[n[0]] = val;
		}else if(n.indexOf(".") == -1){
			alert(name+"变量已经存在");
		}
		ns(n.slice(1, n.length), o[n[0]],name);
	};
	
	return function(str,val){
		if(typeof str != 'string' || str.length == 0) return;
		str = str.split('.');
		ns(str,CTT,val,str);
	};
}();

//定义常量tabdiv
CTT.ns("tabDiv","TAB_DIV_");
CTT.ns("tabId","#tab");
CTT.ns("height",document.documentElement.clientHeight-117);