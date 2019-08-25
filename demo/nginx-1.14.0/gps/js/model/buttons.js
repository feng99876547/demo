FXC.loadJS.loadJs(host+'/js/model/select.js');

/**
*用于初始化事件
*/
var Easyui_InitEvent = function(){};

Easyui_InitEvent.prototype.initDom = function(form,conf){
	FXC.easyui.uploadImage(form);
	FXC.easyui.initUeditor(conf?conf:this);
	FXC.easyui.initDateTimeBbox(form);
	FXC.easyui.initPanelCombobox(conf?conf:this);
}

Easyui_InitEvent.prototype.initFormDom = function(div){
	this.dialog = div;
	this.form = div.find('form:first-child');
	this.initDom(this.form);
};

Easyui_InitEvent.prototype.initFormLayoutDom = function(div){
	this.form = div.find('form:first-child');
	this.initDom(this.form);
};

/**
 * 设置form input attr 属性
 * name name
 * type 赋值的type
 * value value
 * form 查找的form对象
 * domType form 对象中的属性 如 input select等
 */
Easyui_InitEvent.prototype.setFormAttr = function(name,type,value,domType){
	FXC.easyui.setFormAttr(name,type,value,this.getForm(),domType);
};

Easyui_InitEvent.prototype.setFormVal = function(name,value,domType){
	FXC.easyui.setFormVal(name,value,this.getForm(),domType);
};

Easyui_InitEvent.prototype.getFormVal = function(name){
	return this.form.find('*[name="'+name+'"]').val();
};

Easyui_InitEvent.prototype.getForm = function(){
	if(this.form){
		return this.form;
		
	}else{
		this.form = this.dialog.find("form");
		return this.form;
	}
};

var Easyui_Toolbar = function(){};


inheritPrototype(Easyui_Toolbar,selectGrid);

//可实现多继承
// Easyui_Toolbar.inherits(new Easyui_InitEvent(),new selectGrid());
Easyui_Toolbar.inherits(new Easyui_InitEvent());

Easyui_Toolbar.prototype.getToolbar=function(selectbool){
	var me = this;
	if(this.toolbar == null){
		var me = this;
		var toolbar=[];
		if(me.setToolbar){
			Object.keys(me.setToolbar).forEach(function(i){
				if(typeof me.setToolbar[i] == "string"){
					toolbar.push(eval("me."+me.setToolbar[i]+"()"));
				}else{
					var but = {};
					$.extend(true,but,me.setToolbar[i]);
					but.handler = function(){
						but,me.setToolbar[i].handler(me);
					}
					if(but._permissionKey){
						but.disabled = me.verifyAuthority(but._permissionKey);
					}else{
						alert("自定义("+but.text+")按钮没有设置权限控制");
					}
					toolbar.push(but);
				}
			});
		}else{
			toolbar =[me.getAdd(),me.getRead(),me.getDel()];
		}
		if(me.createSearch){
			if(selectbool) toolbar = [];
			me.pushToolbar(toolbar);
		}
		this.toolbar = toolbar;
	}
	return this.toolbar;
};


Easyui_Toolbar.prototype.getAdd = function(){
	var me = this;
	return {
		id : 'add_btn',
		text : '新增',
		iconCls : 'icon-add',
		disabled : me.verifyAuthority('p_add'),
		handler : function() {
			me.add();
		}
	}
};

Easyui_Toolbar.prototype.getDel = function(){
	var me = this;
	return {
		id : 'delete_btn',
		text : '删除',
		iconCls : 'icon-remove',
		disabled : me.verifyAuthority('p_delete'),
		handler : function() {
			if(me.singleSelect)
				me.del();
			else
				me.dels();
		}
	}
};

Easyui_Toolbar.prototype.getRead = function(){
	var me = this;
	return {
		id : 'add_btn',
		text : '查看',
		iconCls : 'icon-search',
		disabled : me.verifyAuthority('p_read'),
		handler : function() {
			me.read();
		}
	}
}

Easyui_Toolbar.prototype.read = function(data) {
	var me = this;
	this.resetForm(this.dialog);
	var row = me.dataGrid.datagrid('getSelected');
	if(row){
		if(typeof this.beforeEdit == "function"){
			this.beforeEdit(row);
		}
		this.dialog.find("form").form('load', row);
		this.getOpenEdit("编辑"+(this.title?this.title:''),null);
		if(typeof this.afterEdit == "function"){
			this.afterEdit(row);
		}
	}else{
		me.messagerShow('请选中一行数据进行查看');
	}
};

Easyui_Toolbar.prototype.addOpenEdit = function(data){
	var buttons = this.getAddButtons(this.saveUrl,null,data);
	this.getOpenEdit("新增"+(this.title?this.title:''),buttons);
};
	
Easyui_Toolbar.prototype.editOpenEdit = function(data){
	var buttons = this.getUpdateButtons(this.updateUrl,null,data);
	this.getOpenEdit("编辑"+(this.title?this.title:''),buttons);
};

Easyui_Toolbar.prototype.add = function(data) {
	this.resetForm(this.dialog);
	if(typeof this.beforeAdd == "function"){
		this.beforeAdd();
	}
	this.addOpenEdit(data);
};
	
/**
 * 查看 包含 修改 有修改自带查看 有查看不一定有修改
 */
Easyui_Toolbar.prototype.edit = function(data) {
	var me = this;
	this.resetForm(this.dialog);
	var row = me.dataGrid.datagrid('getSelected');
	if(typeof this.beforeEdit == "function"){
		this.beforeEdit(row);
	}
	this.dialog.find("form").form('load', row);
	this.editOpenEdit();
	if(typeof this.afterEdit == "function"){
		this.afterEdit(row);
	}
};

/**
 * 删除
 */
Easyui_Toolbar.prototype.del = function() {
	var me = this;
	var row = me.dataGrid.datagrid('getSelected');
	if (row) {
		$.messager.confirm('确认信息？', '确认删除' + row[me.idField], function(r) {
			if (r) {
				me.ajaxToDelte(row[me.idField]);
				return true;
			}else{
				return true;
			}
		});
	}else{
		$.messager.alert('提示', '请勾选需要删除的数据!');
	}

};
	
/**
 * 批量删除
 */
Easyui_Toolbar.prototype.dels = function() {
	var me = this,rows = me.dataGrid.datagrid('getChecked');
	if (rows.length) {
		var html = "";
		var ids = "";
		for (var i in rows) {
			html += '[<span class="cRed">' + rows[i][me.idField] + '</span>]';
			ids += ","+rows[i].id;

		}
		ids = ids.substring(1);
		$.messager.confirm('确认信息？', '确认删除' + html, function(r) {
			if (r) {
				me.ajaxToDelte(ids);
			}
		});
	} else {
		$.messager.alert('提示', '请勾选需要删除的数据!');
	}
};

Easyui_Toolbar.prototype.feedback = function(r,dialog,fns) {
	var me = this;
	var result = JSON.parse(r);
	if(result == null){
		result={};
		result["success"] = false;
		result["msg"] = "错误";
	}
	if(result.success){
		me.messagerShow(result.msg);

		if(typeof fns === 'function'){
			//返回true将结束方法
			if(fns(result.rows))return;
		}
		if(dialog){
			dialog.dialog("close");
		}
		me.refresh();
	}else if(result.msg=='1001'){
		//console.log(result.msg);
		top.location.href=host+'/index.html';
	}else{
		if(result.msg)
			$.messager.alert("结果", result.msg);
	}
};
	

Easyui_Toolbar.prototype.validate = function(arr, form, options){
	var me = this;
	if(me.setValidate){
		for(var obj in me.setValidate){
			if(typeof me.setValidate[obj] == "string"){
				var dom = $('*[name="'+me.setValidate[obj]+'"]',form);
				dom.validatebox({
					required:true,
				});
			}else{
				Object.keys(me.setValidate[obj]).forEach(function(key){
					var dom = $('*[name="'+key+'"]',form);
				    dom.validatebox(me.setValidate[obj][key]);
				});
			}
		}
	}
	//如果需要先展示出体现效果可以在初始化时先触发submit提交事件
	return form.form('validate');//validate方法主要是验证那些numberbox，validatebox这些控件的validtype是否满足
}

Easyui_Toolbar.prototype.ajaxToSave = function(dialog,url,fns,data) {
	var me = this;
	var options = {
		url : url,
		success : function(result) {
			me.feedback(result, dialog,fns);
		},
		error:function(r){
			console.log(r);
			var result = JSON.parse(r.responseText);
			alert(result.msg);
		}
	};
	if(data){//添加的提交参数
		options['data'] = data;
	}
	if(me.setValidate){
		options.beforeSubmit = function(arr, form, options) {
			return me.validate(arr, form, options);
		};
	}
	if(typeof me.beforeSubmit == "function"){
		me.beforeSubmit(me.form);
	}
    $("form", dialog).ajaxForm(options).submit();
};
	
//清空表单数据和效验
Easyui_Toolbar.prototype.resetForm = function($form) {
    //$form.validatebox('remove');
    this.clearValidate(this.setValidate,$form);
    $form.form('clear');
}

/**
*清空验证
*/
Easyui_Toolbar.prototype.clearValidate = function(validates,form){
	var me = this;
	for(var obj in validates){
		if(typeof me.setValidate[obj] == "string"){
			var dom = $('*[name="'+me.setValidate[obj]+'"]',form);
			dom.validatebox({
				required:false,
			});
		}else{
			Object.keys(me.setValidate[obj]).forEach(function(key){
				var dom = $('*[name="'+key+'"]',form);
			    dom.validatebox({required:false});
			});
		}
	}
};

	
/**
 *提示
 */
Easyui_Toolbar.prototype.messagerShow = function(message) {
    $.messager.show({
        title : '提示',
        msg : message,
        timeout : 2000,
        style : {
            right : '',
            top : document.body.scrollTop + document.documentElement.scrollTop,
            bottom : ''
        }
    });
};
	

/**
 * 获取修改按钮
 */
Easyui_Toolbar.prototype.getUpdateButtons = function(url,fns,data) {
	if(this.verifyAuthority('p_update')){
		return null;
	}
	return this.getAddButtons(url,fns,data);
};

/**
*新增功能肯定有按钮 而修改分查看和修改
*/
Easyui_Toolbar.prototype.getAddButtons = function(url,fns,data) {
	var buttons = new Array();
	var me = this;
	if(me.dialogButtons){
		Object.keys(me.dialogButtons).forEach(function(i){
			if(me.dialogButtons[i] == "ok"){
				buttons.push({
					text : '确定',
					handler : function() {
						me.ajaxToSave(me.dialog,url,fns,data);
					}
				});
			}else if(me.dialogButtons[i] == "no"){
				buttons.push({
					text : '取消',
					handler : function() {
						me.dialog.dialog("close");
					}
				});
			}else{
				var but = new Object();
				$.extend(true,but,me.dialogButtons[i]);
				but.handler = function(){
					me.dialogButtons[i].handler(me);
				}
				if(but._permissionKey){
					but.disabled = me.verifyAuthority(but._permissionKey);
				}else{
					alert("自定义("+but.text+")按钮没有设置权限控制");
				}
				buttons.push(but);
			}
		});
	}else{
		buttons =[{
			text : '确定',
			handler : function() {
				me.ajaxToSave(me.dialog,url,fns,data);
			}
		},{
			text : '取消',
			handler : function() {
				me.dialog.dialog("close");
				
			}
		}];
	}
	return buttons;
};

/**
 * 编辑弹出框
 */
Easyui_Toolbar.prototype.getOpenEdit = function(title,buttons) {
	var me = this;
	var $thisDialog = this.dialog;
	$thisDialog.dialog({
		title : title,
		width : me.width || 500,
		height:me.height || 500,
		modal : true,
		resizable : true,
		closed:false,
		buttons : buttons,
		onOpen:function(){
			if(me.onOpen)
				me.onOpen();
		},
		onClose:function(){
			me.resetForm($thisDialog);//清空验证信息
			if(typeof me.dialogClose == "function"){
				me.dialogClose();
			}
		}
	});
};
	
Easyui_Toolbar.prototype.ajaxToDelte = function(id) {
	var me = this,url = me.delUrl;
	$.post(url, {
		id : id
	}, function(result) {
		me.feedback(result);
	});
};

Easyui_Toolbar.prototype.ajaxPost = function(url,param) {
	var me = this;
	$.post(url,param, function(result) {
		me.feedback(result,me.dialog);
	});
};
