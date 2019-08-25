
FXC.ns("FXC.util");


FXC.util.addEventListener=function(element,type,handler){
    if(element.addEventListener){
        element.addEventListener(type,handler,false);
    }
    else if(element.attachEvent){
        element.attachEvent('on'+type,handler);
    }
    else{
        element["on"+type]=handler /*直接赋给事件*/
    }

};
    
FXC.util.removeEventListener = function(element,type,handler) {   /*Chrome*/
    if (element.removeEventListener)
        element.removeEventListener(type, handler, false);
    else if (element.deattachEvent) {               /*IE*/
        element.deattachEvent('on' + type, handler);
    }
    else {
        element["on" + type] = null;
        /*直接赋给事件*/
    }
};
/**
封装该方法是为了添加匿名函数后可以删除 所以只适用于匿名函数添加监听 非匿名函数请使用addEventListener和removeEventListener
addAnonymousListener 和 removeAnonymousEvent 是一套对应使用的
obj 指向的dom对象
ev  事件名称(代替函数名用)
fn  匿名函数
boolean false 代表匿名函数 true代表传入的是非匿名函数
*/
FXC.util.addAnonymousListener = function(obj,ev,fn){
	var func = function(){        //将函数专为匿名函数并改变this指向 低版本ie下的必然操作  标准下如此操作没有副作用
		fn.apply(obj,arguments);
	};
	obj.funByEv = obj.funByEv || {} ,
	obj.funByEv[ev] = obj.funByEv[ev] || [];
	obj.funByEv[ev].push(func);
	FXC.util.addEventListener(obj,ev,func);
};

FXC.util.removeAnonymousEvent = function(obj,ev){
	if(obj.funByEv && obj.funByEv[ev]){
		if(obj.funByEv[ev].length>0){
			//这边使用的是先进先出的规则 最先添加的同名监听会先被删除
			FXC.util.removeEventListener(obj,ev,obj.funByEv[ev][0]);
			//console.log("删除("+ev+")匿名函数监听成功");
			(obj.funByEv[ev]).splice(0,1);
		}
	}
	// else{
	// 	console.log("删除("+ev+")匿名函数监听失败 或不存在");
	// }
};

FXC.util.getDateTime = function (value){
	if(value){
		return new Date(value).format("yyyy-MM-dd hh:mm:ss");
	}
}

/**
*判断是一维数组还是二维数组还是对象
* 2 二维数组 1 一维数组
*/
FXC.util.isArryOrObj = function(val){
	if(val instanceof Array){
		if(val[0] instanceof Array){
			return 2;
		}else{
			return 1;
		}
	}else{
		return 0;
	}
}

/**
*上传图标的定位
* target需要定位的目标
*position 参与比较定位的目标
*/
FXC.util.position = function(target,position,trimmer){
	var width = position.height()+trimmer;
	target.width(width);
	target.height(width);
	var top = (0-width)+"px";
	var left = (position.width()-width+trimmer)+"px";
	target.css({"background-image":"url(/css/images/uploadlog.png)","background-size":"cover"
		,"margin-top":top,"margin-left":left});

};

