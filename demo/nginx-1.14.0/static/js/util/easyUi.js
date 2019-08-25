
FXC.ns("FXC.easyui");

/**
 * 设置form input attr 属性
 * 
 * name name
 * type 赋值的type
 * value value
 * form 查找的form对象
 * domType form 对象中的属性 如 input select等 默认input
 */
FXC.easyui.setFormAttr =function(name,type,value,form,domType){
	if(domType){
		form.find(domType+'[name="'+name+'"]').attr(type,value);
	}
	else{
		form.find('input[name="'+name+'"]').attr(type,value);
	}
		
}

/**
 * 设置form input val 属性
 * 
 * name name
 * value value
 * form 查找的form对象
 * domType form 对象中的属性 如 input select等 默认input
 */
FXC.easyui.setFormVal = function(name,value,form,domType){
	if(domType)
		form.find(domType+'[name="'+name+'"]').val(value);
	else
		form.find('input[name="'+name+'"]').val(value);
}

/**
* 将data-options 字符串转json
*/
FXC.easyui.optionsToJson = function(str){
	var arr = str.replace(/\'/g,"").split(",");
	var obj = {};
	for (var i in arr) {
		var val = arr[i].split(":");
		obj[val[0]] = val[1];
	}
	return obj;
}

FXC.easyui.initSelectCustom = function (dom,datas,namekey,valuekey,showOne){
	if(!showOne){//默认会有一行空白的
		dom.append('<option value="">全 部</option>');
	}
	if(datas!=null){
		$.each(datas, function(index, node) {
			dom.append(
					'<option value="' + node[valuekey] + '">' + node[namekey]
							+ '</option>');
		});
	}
};
/**
 * 二维数组格式
 * data [['val','text'],['val','text']]
 */
FXC.easyui.initSelectArray  = function (dom,datas){
	dom.append('<option value="">请选择</option>');
	if(datas!=null){
		$.each(datas, function(index, node) {
			dom.append('<option value="' + node[0] + '">' + node[1]+'</option>');	
		});
	}
};

/**
 * 一维数组
 * data [val,val]
 */
FXC.easyui.initSelectSingleArray  = function (dom,datas){
	dom.append('<option value="">请选择</option>');
	if(datas!=null){
		$.each(datas, function(index, node) {
			dom.append('<option value="' + node + '">' + node+'</option>');	
		});
	}
};

/**
*获取div的宽度
* 需要获取的dom
* ratio 比例 小数点 如 0.2
*/
FXC.easyui.getWidth = function(ratio){
	var dom = $("#main_right");
	return dom.outerWidth()*ratio;
};
