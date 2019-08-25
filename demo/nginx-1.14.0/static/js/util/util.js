
FXC.ns("FXC");

FXC.constants = {};
FXC.constants.symbol = {
		eq:"为",
		gt:"大于",
		lt:"小于"
};

/**
 * 兼容IE和FF与chrome监听事件
 * obj dom元素
 * ev 事件
 * fn 绑定的函数
 */
FXC.addEvent = function (obj,ev,fn){
	if(obj.attachEvent){
		obj.attachEvent('on'+ev,fn);//针对IE浏览器
	}else{
		obj.addEventListener(ev,fn,false);//针对FF与chrome
	}
};

/**
 * 兼容IE和FF与chrome监听事件 可以在上层绑定之前像绑定的dom中传入参数
 * 多了参数解锁各种姿势哦
 * obj dom元素
 * ev 事件
 * fnName 函数名 引用无效
 * arg 函数参数 
 */
FXC.addEventFnName = function (obj,ev,fnName){
	var FalseHijackedFn;
	var len = arguments.length;
	if(fnName && len>2){
		var params =  []; 
		var pstr = "";
		var xb = 0;
		for (var i = 3; i < len; i++) {
			params[xb++] =arguments[i]
	    }
		for(var i = 0;i<params.length;i++){
			pstr+= "params["+i+"],";
		};
		pstr = pstr.substring(0,pstr.length-1);
		console.log(pstr);
		FalseHijackedFn = function(){
			eval( fnName+"("+pstr+")");
//			fnName(eval(pstr));
		};
	}
	if(obj.attachEvent){
		obj.attachEvent('on'+ev,FalseHijackedFn);//针对IE浏览器
	}else{
		obj.addEventListener(ev,FalseHijackedFn,false);//针对FF与chrome
	}
};


/**
 * 兼容IE和FF与chrome监听事件 可以在上层绑定之前像绑定的dom中传入参数
 * obj dom元素
 * ev 事件 
 * fn 函数 函数的参数已经写死这边最多可以传入5个参数
 * arg 函数参数 
 */
FXC.addEventParam = function (obj,ev,fn){
	var FalseHijackedFn;
	var params =  []; 
	var xb = 0;
	var len = arguments.length;
	for (var i = 3; i < len; i++) {
		params[xb++] =arguments[i]
    }
	len = params.length;
	/*
	 * 写死 一般参数没有那么多 如果很多可以在方法外在封装一层无参方法
	 *function t = function(){
	 *	fnName(...);
	 *} 
	 */
	FalseHijackedFn = function(){
		if(len == 1){
			fn(params[0]);
		}else if(len == 2){
			fn(params[0],params[1]);
		}else if(len == 3){
			fn(params[0],params[1],params[2]);
		}else if(len == 4){
			fn(params[0],params[1],params[2],params[3]);
		}else if(len == 5){
			fn(params[0],params[1],params[2],params[3],params[4]);
		}
	};
	if(obj.attachEvent){
		obj.attachEvent('on'+ev,FalseHijackedFn);//针对IE浏览器
	}else{
		obj.addEventListener(ev,FalseHijackedFn,false);//针对FF与chrome
	}
};

/**
 * 键盘事件只能输入数字
 */
FXC.onlyNumber = function(e){
		 var k = event.keyCode;
		 //tab 没有去找ask
		 if ((k <= 57 && k >= 48) || (k <= 105 && k >= 96) || (k == 8)){
			 return true;
		}else {  
		     return false;  
		}  
}

/**
 * 键盘松开事件 屏蔽汉字
 * param dom需要屏蔽的dom
 */
FXC.shieldingChinese = function(dom){
	if(dom){
		FXC.addEvent(dom,"keyup",function(e){
			var val = dom.value;
			//去掉中文
			if(val.length>0){
				dom.value =  FXC.validation.chinese(val);
			}
		});
	}
}

FXC.prints = {};
/**
 * 打印信息
 * prompt 提示方式
 * msg 提示内容
 */
FXC.prints.msg = function(prompt,msg){
	if(prompt.trim()=="alert"){
		alert(msg);
	}
};

/**
 * 长度打印信息
 * prompt 提示方式
 * content 提示内容
 * compare 比较符号
 * length 长度
 */
FXC.prints.length = function(prompt,content,compare,length){
	if(prompt.trim()=="alert"){
		alert(content+"的长度不能"+compare+(length==0  ? "空":length));
	}
};

FXC.validation = {};
/**
 * 替换掉字符串中的汉字包括符号
 * param str 字符串
 */
FXC.validation.chinese = function(str){
	var val =str;
	var reg = /[\u4E00-\u9FA5\uf900-\ufa2d]/ig;
	val = val.replace(reg,'');
	return val;
};

/**
 * 验证手机号是否正确
 * param phone 字符串
 * 正确返回true 错误返回false
 */
FXC.validation.phone = function(phone,prompt,msg){
	if((/^1[34578]\d{9}$/.test(phone))){
        return true; 
    }else{
    	if(prompt){
    		msg = msg ? msg : "手机格式不正确!";
    		FXC.prints.msg(prompt,msg);
    	}
    	return false; 
    }
};

/**
 * 长度验证
 * param max 最大长度
 * param min 最小长度
 * param str 字符串
 * param content 提示内容
 * param prompt 提示方式
 * 
 * 正确返回true 错误返回false
 */
FXC.validation.length = function(max,min,str,content,prompt){
		var length = str.length;
		if(length==0){
			FXC.prints.length(prompt,content,FXC.constants.symbol.eq,length);
			return false; 
		}else if(length<min){
			FXC.prints.length(prompt,content,FXC.constants.symbol.lt,min);
			return false; 
		}else if(length>max){
			FXC.prints.length(prompt,content,FXC.constants.symbol.gt,max);
			return false; 
		}
    	return true; 
};

/**
 * 长度验证 是否为空
 * param str 字符串
 * param content 提示内容
 * param prompt 提示方式
 * 
 * 正确返回true 错误返回false
 */
FXC.validation.lengthIsNull = function(str,content,prompt){
	var length = str.length;
	if(length==0){
		FXC.prints.length(prompt,content,FXC.constants.symbol.eq,length);
		return false; 
	}
	return true; 
};

FXC.getDateTime = function (value){
	if(value){
		return new Date(value).format("yyyy-MM-dd hh:mm:ss");
	}
}


///**
// * 数组json字符串转json
// */
//FXC.arrayTOjsonObj(){
//	
//}
