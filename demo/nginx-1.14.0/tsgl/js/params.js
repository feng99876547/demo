var root = location.pathname.split("/")[1];
//var $path='http://'+location.host+'/'+root;
var $path='http://'+location.host+'/tsgl';
//var $path='http://120.79.239.126/tsgl';
var host = 'http://'+location.host;
var _webApp = $path;
var imagePath = host+"/upload";

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

function object (o){//这个o相当于父对象实例
    function F(){}//这个F相当子对象
    F.prototype=o;//继承
    return new F();//实例化传出
}

/*该继承只继承prototype属性和方法不包含this属性*/
function inheritPrototype(subType,superType){
    var prototype=object(superType.prototype);
    prototype.constructor=subType;
    subType.prototype=prototype;
}


/**
*inherits 实现 prototype继承arguments的属性
*该继承包含了prototype属性和父对象的this属性
*/
Function.prototype.inherits=function(){
    var arr=arguments; //将接收到的arguments对象传给数组arr
    if(!this.prototype)this.prototype={};
    for(var i=0;i<arr.length;i++){
      for(var k in arr[i]){
        var obj=arr[i];
        this.prototype[k]=obj[k];
      }
    }
};

//定义常量tabdiv
CTT.ns("tabDiv","TAB_DIV_");
CTT.ns("tabId","#tab");
CTT.ns("height",document.documentElement.clientHeight-117);
CTT.ns("mark_search_form","mark_search_form_");
CTT.ns("upoload_mark","upoload_mark_");