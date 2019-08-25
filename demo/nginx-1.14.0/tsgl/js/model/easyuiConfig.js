/**
 * 向上寻找第一个对应的dom
 * @param dom
 * @param key （name，tagName,class...）标签属性
 * @param tagName （"xxx","DIV",xxclass...）对应值
 * @returns
 */
function upDiffusion(dom,key,tagName){
	var d=dom.parentNode;
	while(d[key].trim() != tagName.trim() || d["tagName"] == "BODY"){
		d=d.parentNode;
	}
	return d;
}

function JQupDiffusion(dom,key,tagName){
	return $(upDiffusion(dom,key,tagName));
}

//时间格式化
$.fn.datebox.defaults.formatter = function (date) {
    var y = date.getFullYear();
    var m = date.getMonth() + 1;
    var d = date.getDate();
    var h = date.getHours();
    return y + '-' + m + '-' + d;
};

// 文本编辑
$.extend($.fn.datagrid.defaults.editors, {
    text: {
		init: function(container, options){
			var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container);
			return input;
		},
		destroy: function(target){
			$(target).remove();
		},
		getValue: function(target){
			return $(target).val();
		},
		setValue: function(target, value){
			if(typeof value == "object")
				$(target).val(JSON.stringify(value));
			else
				$(target).val(value);
		},
		resize: function(target, width){
			$(target)._outerWidth(width);
		}
    },
    panelCombobox:{
		init:function(container, options){
			options.panelHeight="-35";
			var box = $('<input data-options="valueField:\''+(options.valueField?options.valueField:'')
				+'\',textField:\''+(options.textField?options.textField:'')+'\'"/>').appendTo(container);
			box.combobox(options);
			if(options.openPanelClickEvent){
				options.openPanelClickEvent(box,options);
			}else{
				//弹出面板选中事件
			}
			return box;
		},
		getValue: function (target) {//在鼠标点击其它行时（也就是编辑其他行时触发该方法）
			return $(target).combobox('getText');
		},
		setValue: function (target, value) {//在点击这一行的时候触发编辑事件把初始值赋值到控件中
			var options =FXC.easyui.optionsToJson(target.data('options'));
			if(typeof value == "object"){
				if(options.valueField){
					$(target).combobox('setValue',value[options.valueField]);
				}
				if(options.textField){
					$(target).combobox('setText',value[options.textField]);
				}
			}else{
				$(target).combobox('setText',value);
			}
		},
		resize: function (target, width) {
			var box = $(target);
			box.combobox('resize', width);
		},
		destroy: function (target) {
			$(target).combobox('destroy');
		}
	},
});


FXC.JC_formLoad = $.fn.form.methods.load;
$.extend($.fn.form.methods, {
	load: function(jq, data){
		if(data){
			Object.keys(data).forEach(function(key){
				var value = data[key];
				if(typeof value == "object"){
					var form, index;
					form = jq.serializeArray();
					for(index = 0; index < form.length; ++index){
						var names = form[index].name.split(".");
						if(names[0] == key && value[names[1]]){
							data[key+"."+names[1]]=value[names[1]];
						}
					}
				}
			});
			return FXC.JC_formLoad (jq, data);
		}
	},
});


$.extend($.fn.validatebox.methods, {  
  remove: function(jq, newposition){
    return jq.each(function(){
      $(this).removeClass("validatebox-text validatebox-invalid").unbind('focus.validatebox').unbind('blur.validatebox');
    });  
  },
  reduce: function(jq, newposition){  
    return jq.each(function(){  
       var opt = $(this).data().validatebox.options;
       $(this).addClass("validatebox-text").validatebox(opt);
    });  
  } 
});
$.extend($.fn.validatebox.defaults,{missingMessage:"<font size= 3 color= red>*</font>"});
$.extend($.fn.validatebox.defaults.rules, {
	minLength : {
    	validator: function (value) {
	      	var len = $.trim(value).length;
	      	return len >=6 && len <= 16;
		},
	    message: '请输入至少6-16个字符'
  	},
  idcard: {// 验证身份证
        validator: function (value) {
          return /^\d{15}(\d{2}[A-Za-z0-9])?$/i.test(value);
        },
        message: '身份证号码格式不正确'
      },
 length: { validator: function (value, param) {
        var len = $.trim(value).length;
        return len >= param[0] && len <= param[1];
      },
        message: "输入内容长度必须介于{0}和{1}之间."
      },
 phone: {// 验证电话号码
        validator: function (value) {
          return /^((\d{2,3})|(\d{3}\-))?(0\d{2,3}|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message: '格式不正确,请使用下面格式:020-88888888'
      },
mobile: {// 验证手机号码
        validator: function (value) {
          return /^(13|15|18)\d{9}$/i.test(value);
        },
        message: '手机号码格式不正确'
      },
intOrFloat: {// 验证整数或小数
        validator: function (value) {
          return /^\d+(\.\d+)?$/i.test(value);
        },
        message: '请输入数字，并确保格式正确'
      },
currency: {// 验证货币
        validator: function (value) {
          return /^\d+(\.\d+)?$/i.test(value);
        },
        message: '货币格式不正确'
      },
qq: {// 验证QQ,从10000开始
        validator: function (value) {
          return /^[1-9]\d{4,9}$/i.test(value);
        },
        message: 'QQ号码格式不正确'
      },
integer: {// 验证整数 可正负数
        validator: function (value) {
          //return /^[+]?[1-9]+\d*$/i.test(value);
 
          return /^([+]?[0-9])|([-]?[0-9])+\d*$/i.test(value);
        },
        message: '请输入整数'
      },
 age: {// 验证年龄
        validator: function (value) {
          return /^(?:[1-9][0-9]?|1[01][0-9]|120)$/i.test(value);
        },
        message: '年龄必须是0到120之间的整数'
      },
 
 chinese: {// 验证中文
        validator: function (value) {
          return /^[\Α-\￥]+$/i.test(value);
        },
        message: '请输入中文'
      },
english: {// 验证英语
        validator: function (value) {
          return /^[A-Za-z]+$/i.test(value);
        },
        message: '请输入英文'
      },
unnormal: {// 验证是否包含空格和非法字符
        validator: function (value) {
          return /.+/i.test(value);
        },
        message: '输入值不能为空和包含其他非法字符'
      },
 username: {// 验证用户名
        validator: function (value) {
          return /^[a-zA-Z][a-zA-Z0-9_]{5,15}$/i.test(value);
        },
        message: '用户名不合法（字母开头，允许6-16字节，允许字母数字下划线）'
      },
faxno: {// 验证传真
        validator: function (value) {
          //      return /^[+]{0,1}(\d){1,3}[ ]?([-]?((\d)|[ ]){1,12})+$/i.test(value);
          return /^((\d{2,3})|(\d{3}\-))?(0\d{2,3}|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i.test(value);
        },
        message: '传真号码不正确'
      },
 zip: {// 验证邮政编码
        validator: function (value) {
          return /^[1-9]\d{5}$/i.test(value);
        },
        message: '邮政编码格式不正确'
      },
ip: {// 验证IP地址
        validator: function (value) {
          return /d+.d+.d+.d+/i.test(value);
        },
        message: 'IP地址格式不正确'
      },
name: {// 验证姓名，可以是中文或英文
        validator: function (value) {
          return /^[\Α-\￥]+$/i.test(value) | /^\w+[\w\s]+\w+$/i.test(value);
        },
        message: '请输入姓名'
      },
date: {// 验证姓名，可以是中文或英文
        validator: function (value) {
          //格式yyyy-MM-dd或yyyy-M-d
          return /^(?:(?!0000)[0-9]{4}([-]?)(?:(?:0?[1-9]|1[0-2])\1(?:0?[1-9]|1[0-9]|2[0-8])|(?:0?[13-9]|1[0-2])\1(?:29|30)|(?:0?[13578]|1[02])\1(?:31))|(?:[0-9]{2}(?:0[48]|[2468][048]|[13579][26])|(?:0[48]|[2468][048]|[13579][26])00)([-]?)0?2\2(?:29))$/i.test(value);
        },
        message: '清输入合适的日期格式'
      },
msn: {
        validator: function (value) {
          return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(value);
        },
        message: '请输入有效的msn账号(例：abc@hotnail(msn/live).com)'
      },


});
