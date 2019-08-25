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



/**
 *  简化模版
 */
var config = function(conf){
	
	var me = this;
	
	this.dataGrid = null;
	
	this.columns = null;
	
	this.title = null;
	
	this.sortName = null;//排序字段
	
	this.sortOrder = null;//排序状态
	
	this.delmark = "id";//用于删除中显示的key
	
	this.idField = 'id';
	
	this.useEdit = true; //是否使用编辑 删除 事件
	
	this.queryParams ;//默认附加参数
	
	this.editIndex = undefined;//编辑标记
	
	this.p_update;//操作权限
	
	this.p_delete;//操作权限
	
	this.p_create;//操作权限
	
//	this.search = function(){} //搜索时触发的事件
	
//  this.form; //主面板的form	
	
//	this.searchForm ; //主面板查询form 
	
//	this.select_searchForm;//查询面板使用的form 
	
	this.id = FXC.getId();
	
	this.search_form_id = "search_form_"+this.id;//查询面板使用的searchid
	
	this.dialog = null;//form表达div
	
	this.form = null;//form 
	
	this.dom = null;//grid对应的div
	
	this.bindClickStates = false;//用于给Grid的table绑定事件 且只绑定一次 因为load的适合不是重新渲染
	
	this.bindaddEventListener = false;
	
	this.getDateTime = function(value){
		if(value){
			return new Date(value).format("yyyy-MM-dd hh:mm:ss");
		}
	};
	
	
	/**
	 * 设置form input attr 属性
	 * 
	 * name name
	 * type 赋值的type
	 * value value
	 * form 查找的form对象
	 * domType form 对象中的属性 如 input select等
	 */
	this.setFormAttr = function(name,type,value,domType){
		FXC.easyui.setFormAttr(name,type,value,this.getForm(),domType)
	};
	
	this.setFormVal = function(name,value,domType){
		FXC.easyui.setFormVal(name,value,this.getForm(),domType)
	};
	
	this.getForm = function(){
		if(this.form){
			return this.form;
			
		}else{
			this.form = this.dialog.find("form");
			return this.form;
		}
	};
	
	/**
	 * 权限初始化
	 */
	if(conf.permissionsKey){
		if(permissionyz()){
			this.p_update = true;this.p_create = true;this.p_delete = true;
		}else{
			var permissions =menuPermission[conf.permissionsKey];
			for(var i in permissions){//初始化权限
				if(permissions[i].permission==(conf.permissionsKey+":add")){
					this.p_create = true;
				}else if(permissions[i].permission==(conf.permissionsKey+":delete")){
					this.p_delete = true;
				}else if(permissions[i].permission==(conf.permissionsKey+":update")){
					this.p_update = true;
				}
			}
		}
		
	}

	this.setValidateOperate = function(row){
		var html = '';
			if(row[me.idField] && me.p_update) { 
				html += '<a class="operate_btn" href="javascript:void(0);"  name="bj">编辑</a>';
			}
			if(row[me.idField] && me.p_delete) {
				html += '<a class="operate_btn" href="javascript:void(0);"  name="sc">删除</a>';
			}
			return html;
	}

	this.operate = { //默认编辑按钮
		field : 'operate',
		title : '操作',
		width : 100,
		align : 'center',
		formatter : function(value, row, index) {
			return me.setValidateOperate(row);
		}
	};
	
	/** 初始化搜索栏 事件 默认是会自动初始化的 */
	this.initSearch = function() {
		var me = this;
		// 对搜索按钮和单选框绑定点击搜索事件
		$("#"+this.search_form_id+" input:radio").click(function() {
			me.searchload();
		});
		$("#"+this.search_form_id+" input").keydown(function(event) {
			if (event.keyCode == 13) {
				me.searchload();
				return false;
			}
			//easyui 控件生成时默认给input添加了监听事件 一按回车就会跳转 所以这边返回false
		});
	};
	
	this.searchload = function() {
		var searchData = $("#"+this.search_form_id).serializeJson();
		if(this.queryParams){
			extend(searchData,this.queryParams);
		}
		this.dataGrid.datagrid('reload', searchData);
		//清楚所有选中项，防止出现删除后的项目依然存在于选中项中
		this.dataGrid.datagrid("clearChecked");
	};
	
	this.getToolbar = function(){
		var me = this;
		var toolbar=[];
		if(me.toolbar){//写死了 toolbar 第一个按钮是先增 第二个是删除
			var tabIndex = 0;
			if(me.toolbar[0]){
				toolbar[tabIndex] = me.toolbar[0];
				toolbar[tabIndex++]['disabled'] = !me.p_create;
			}
			if(me.toolbar[1]){
				toolbar[tabIndex] = me.toolbar[1];
				toolbar[tabIndex++]['disabled'] = !me.p_delete;
			}
		}else{
			toolbar =[{
				id : 'add_btn',
				text : '新增',
				iconCls : 'icon-add',
				disabled : !me.p_create,
				handler : function() {
					me.add();
				}
			},{
				id : 'delete_btn',
				text : '删除',
				iconCls : 'icon-remove',
				disabled : !me.p_delete,
				handler : function() {
					if(me.singleSelect)
						me.del();
					else
						me.dels();
				}
			}];
		}
		if(this.createSearch){
			toolbar.push({
				//根据search_form_id生成对应的search form
				text:FXC.initSrarch.appendSearch(this),
			});
			toolbar.push({
				text: '搜索',
				iconCls: 'icon-query',
				handler: function(){
					if(typeof me.search == 'function'){
						me.search();
					}else{
						me.searchload();
					}
				}
			});
		}
		return toolbar
	};
	
	/**
	 * 属性赋值 简单理解继承
	 * grid面板生成前需要初始化的内容
	 */
	this.initAtt=function(){
		for(var obj in conf){//调用config构造函数传入的config参数
			this[obj] = conf[obj];
		}
	};
	
	this.initOperate = function(){
		if(me.useEdit){
			var len = me.columns[0].length -1;
			if(me.bindClickStates == false && 
					me.columns && me.columns[0][len] && me.columns[0][len].field.trim() != 'operate'){
				me.columns[0].push(me.operate);
			}
		}
	};

	/**
	 * 刷新
	 */
	this.refresh =  function() {
		me.dataGrid.datagrid("reload");
		//清除所有选中项
		me.dataGrid.datagrid("clearChecked");
	};
	
	this.feedback = function(r,dialog,fns) {
		//console.log(r);
		var result = JSON.parse(r);
		if(result == null){
			result={};
			result["success"] = false;
			result["msg"] = "错误";
		}
		if(result.success){
			if(typeof fns === 'function'){
				fns(result.rows);
			}
			me.messagerShow(result.msg);
			if(dialog){
				dialog.dialog("close");
			}
			me.refresh();
		}else if(result.msg=='1001'){
			//console.log(result.msg);
			top.location.href=host+'/admin/index.html';
		}else{
			if(result.msg)
				$.messager.alert("结果", result.msg);
		}
	};
	
	this.ajaxToSave = function(dialog,url,fns,data) {
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
			options.beforeSubmit = me.validate;
		}
        $("form", dialog).ajaxSubmit(options);
	};
	
	this.resetForm = function($form) {
	    //jquery.validate插件验证错误信息清除
	    $("input.error", $form).removeClass('error');
	    $("label.error", $form).remove();
	    $form.form('clear');
	}
	
	/**
	 *表单元素有效性验证，使用jquery.validate插件
	 */
	this.validate = function(arr, $form, options) {
		//jquery.validate插件无及时验证方法，只能通过提交触发
		var rules;
		//设置验证
		if(me.setValidate){
			rules = {};
			for(var obj in me.setValidate){
				rules[me.setValidate[obj]] = {};
				rules[me.setValidate[obj]]["required"] = true;
			}
		}
		
		$form.validate({
			debug : true,
			rules : rules
		});
		$form.submit();
		return $form.valid();
	};
	
	/**
	 *提示
	 */
	this.messagerShow = function(message) {
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
	 * 编辑弹出框
	 */
	this.openEdit = function(title,url,fns,data) {
		//名字要统一啊
		var $thisDialog = me.dialog;
		$thisDialog.dialog({
			title : title,
			width : 400,
			modal : true,
			resizable : true,
			buttons : [{
				text : '确定',
				handler : function() {
					me.ajaxToSave($thisDialog,url,fns,data);
				}
			}, {
				text : '取消',
				handler : function() {
					$thisDialog.dialog("close");
					
				}
			}],
			onClose:function(){
				me.resetForm($thisDialog);//清空验证信息
//				$(this).dialog('destroy');//销毁  
			}
		});
	};
	
	
	/**
	 * 删除
	 */
	this.del = function() {
		var row = me.dataGrid.datagrid('getSelected');
		if (row) {
			$.messager.confirm('确认信息？', '确认删除' + row[me.delmark], function(r) {
				if (r) {
					me.ajaxToDelte(row[me.idField]);
					return true;
				}else{
					return true;
				}
			});
		}

	};
	
	/**
	 * 批量删除
	 */
	this.dels = function() {
		var rows = me.dataGrid.datagrid('getChecked');
		if (rows.length) {
			var html = "";
			var ids = "";
			for (var i in rows) {
				html += '[<span class="cRed">' + rows[i][me.delmark] + '</span>]';
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
	
	/**
	 * 扩展新增之前
	 */
	this.onBeforeAdd = function(){
		if(typeof this.beforeAdd == "function"){
			this.beforeAdd();
		}
	};
	/**
	 * 拿到row后扩展编辑之前
	 */
	this.onBeforeEdit = function(row){
		if(typeof this.beforeEdit == "function"){
			this.beforeEdit(row);
		}
	};
	
	/**
	 * 新增
	 */
	this.add = function() {
		this.dialog.form('clear');
		this.onBeforeAdd();
//		$("#permission_dialog form input[name='type'][value=0]").prop("checked", true);
		this.addOpenEdit();
	};
	
	this.addOpenEdit = function(data){
		me.openEdit("新增"+this.title,this.saveUrl,null,data);
	};
	
	this.editOpenEdit = function(data){
		this.openEdit("编辑"+this.title,this.updateUrl,null,data);
	};
	/**
	 * 修改
	 */
	this.edit = function() {
		this.dialog.form('clear');
		var row = me.dataGrid.datagrid('getSelected');
		this.onBeforeEdit(row);
		this.dialog.find("form").form('load', row);
		this.editOpenEdit();
	};
	
	
	this.ajaxToDelte = function(id) {
		var url = me.delUrl;
		$.post(url, {
			id : id
		}, function(result) {
			me.feedback(result,me.dialog);
		});
	};
	
	this.ajaxPost = function(url,param) {
		$.post(url,param, function(result) {
			me.feedback(result,me.dialog);
		});
	};
	
	//绑定到input的事件转到图片上或是按钮
	this.bindImg = function(dom,fns){
		dom.get(0).parentNode.onclick=function(e){
			e = e || window.event;
			var target = e.target || e.srcElement;
	   		if(target.tagName.toLowerCase() == "img"){
	   			fns();
	   			return;
	   		};
		}
	};
	
	/**
	 * 注意
	 */
	this.bindMouseup = function(e,fns){
		e = e || window.event;
		var target = e.target || e.srcElement;
		if(target.tagName.toLowerCase() === "a"){
			if(fns==null){//默认的
				if(target.name == "bj"){
	   				me.edit();
	   			}
	   			if(target.name == "sc"){
	   				me.del();
	   			}
			}else{
				for(var key in fns){
					if(target.name == key){
						fns[key](target,me);
	   	   			}
				}
			}
		}
	};
	
	this.afterInit = function(){
		this.bindaddEventListener = true;
		if(typeof me.initSearch == "function")
			me.initSearch();
	};
	
	
	this.bindClick = function(bindOptions,fns){
		//不要忘记加url属性 没配置是不会触发这个方法的
		var me = this;
		bindOptions.view.onAfterRender = function(target){
			//在每次渲染面板成功时设置bindaddEventListener=true 绑定事件成功后设置false 防止多次绑定
			//因为onAfterRender方法在每次视图被呈现后包含刷新都会调用
			if(me.bindaddEventListener == true){
				var datagridView = JQupDiffusion(me.dom.get(0),"tagName","DIV");
				var tab = datagridView.find("a[class='operate_btn']:first");
				if(tab.length>0){
					var dom = upDiffusion(tab.get(0),"tagName","TABLE");
					dom = upDiffusion(dom,"tagName","DIV");
					//在tab外层div添加事件 如果选择给tabel对象添加监听因为冒泡是由内而外就会先触发内层的编辑事件在触发选中事件 因为选中事件也是绑定在tabel外层
					if (window.addEventListener) {
						dom.addEventListener("click",function(e){me.bindMouseup(e,fns)}, false);  
					} else {
						dom.attachEvent("onclick",function(e){me.bindMouseup(e,fns)});
					}  
					me.bindClickStates = true;
					me.bindaddEventListener = false;
				}
			}
			if(typeof me.onAfterRender == "function"){
				me.onAfterRender(target);
			}
		};
	};
	
	this.endEditing = function(){
		if (me.editIndex == undefined){return true}
		if (me.dataGrid.datagrid('validateRow', me.editIndex)){//验证行是否存在
			me.dataGrid.datagrid('endEdit', me.editIndex);
			me.editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	
	this.initChecked = function(columns){
		var col = [];
		col[0] = [];
		if(!me.singleSelect){
			col[0].push({checkbox : true})
		}
		for(var i in columns[0]){
			col[0].push(columns[0][i]);
		}
		return col;
	};
};

var getPage = function(conf){
	//继承config
	config.apply(this,arguments);
	var me = this;

	this.gridOnClickRow = function(index,row){//找不到监听管理控制器啊 
		me.dataGrid.datagrid('acceptChanges');//点击先提交所有被更改的数据
		//是否可以被编辑
		if (me.editIndex != index){
			if (me.endEditing()){
				me.editIndex = index;
			} else {
				me.dataGrid.datagrid('selectRow', me.editIndex);
			}
		}
		me.dataGrid.datagrid('selectRow', index).datagrid('beginEdit', index);
		if(typeof me.onClickRow == "function"){
			me.onClickRow(row,index,me);
		}
	}

	this.setGrid = function(){
		me.gridConf = {
				fit : true,
				url : me.listUrl,
//				sortName : "position",
//				sortOrder : "asc",
				idField : me.idField || "id",
//				treeField : 'name',
				//height:me.height || CTT.height,
				nowrap : true,
				striped:true,//条纹
				rownumbers : true,
				pagination : true,
				pageSize : 20 ,
				pageList:[20, 30, 40],
				autoRowHeight : false,
				singleSelect : me.singleSelect || true,
				checkOnSelect : false,
				selectOnCheck : false,
				columns :me.initChecked(me.columns),
				toolbar:me.getToolbar(),
				queryParams:me.queryParams || null,
				loadFilter:function (data) {//一次渲染这个方法会被触发两次 一次的data是field字段 一次是值
					//console.log(data); 
			        if(data && data.msg == '1001'){
			        	top.location.href=host+'/admin/index.html';
			        }else if(Object.keys(data).length === 0){
			        	return {rows:{},total:0};
			        }else{
			        	return data;
			        }
			    },
				onClickRow:me.gridOnClickRow

		};
		if(me.pageSize){
			me.gridConf.pageSize = me.pageSize;
			me.gridConf.pageList = [me.pageSize];
		}
		if(me.sortName && me.sortOrder){//排序
			me.gridConf.sortName = me.sortName;
			me.gridConf.sortOrder = me.sortOrder;
		}
		
		me.dataGrid =  me.dom.datagrid(me.gridConf);
		
		me.bindClick(me.dom.datagrid("options"));
		
		me.afterInit();//初始化方法
		
		return me.dataGrid;
	};
	
	//initAtt初始化属性在该方法中 所以需要使用重写属性要先执行该方法 
	this.getWin = function(divTag){
		me.initAtt();//初始化属性
		me.initOperate();//初始化Operate
		me.dom =divTag;
		if(me.formUrl){//加载form
			var div = this.dom.find("div");
			//使用属性动态创建form样式不好调 静态页面的请求流量很小 有需要在优化
			div.load(host+me.formUrl,function(responseTxt,statusTxt,xhr){
			    if(statusTxt=="success"){
			    	me.dialog = div;
			    	me.form = div.find('form');
			    	return me.setGrid();
			    }else if(statusTxt=="error"){
			    	alert("Error: "+xhr.status+": "+xhr.statusText);
			    }
		    });
		}else{
			me.operate = null;
			return me.setGrid();
		}
	};
	
	/** 
	 * 用于当中第三方查询 需要后台配置对应的权限
	 */
	this.getSelectConf = function(){
		me.initAtt();//初始化属性
		var gridConf = {
			fit : true,
			url : me.listUrl,
//				sortName : "position",
//				sortOrder : "asc",
			idField : me.idField || "id",
//				treeField : 'name',
			//height : me.height || CTT.height,
			nowrap : true,
			striped:true,//条纹
			rownumbers : true,
			pagination : true,
			pageSize : me.pageSize || 20,
			autoRowHeight : false,
			singleSelect :  true,
			checkOnSelect : true,
			selectOnCheck : false,
			columns : me.columns,
		};
		return  gridConf;
	};
}

//$("#permission_dialog form input[name='status'][value=1]").prop("checked", true);

var getTreeGridPage = function(conf){
	
	config.apply(this,arguments);
	
	var me = this;
	
	/**  新增 */
	this.add = function() {
		this.dialog.form('clear');
		this.onBeforeAdd();
		me.openEdit("新增"+this.title,me.saveUrl,function(rerow){
			me.dom.treegrid('reload');
		});
	},
	
	this.addChild = function() {
		this.dialog.form('clear');
		var row = me.dataGrid.datagrid('getSelected');
		this.openEdit("新增"+this.title,me.saveUrl,function(rerow){
//			me.dom.treegrid('append',{
//				parent:row.id,
//				row: rerow
//			});
			me.dom.treegrid('reload',row.id);
		},{parentId:row.id});
	},
	
	
	/** 修改 */
	this.edit = function() {
		var row = me.dataGrid.datagrid('getSelected');
		this.onBeforeEdit(row);
		this.dialog.find("form").form('load', row);//触发顺序不对啊 先触发a标签事件 在执行选中 所以得不到选中行
		this.openEdit("编辑"+me.title,me.updateUrl,function(rerow){
			me.dom.treegrid('update',{
				id: rerow.id,
				row: rerow
			});
		});
	};
	
	this.bindMouseup = function(e,fns){
		e = e || window.event;
		var target = e.target || e.srcElement;
		if(target.tagName.toLowerCase() === "a"){
			if(fns==null){//默认的
				if(target.name == "bj"){
	   				me.edit();
	   			}
	   			if(target.name == "sc"){
	   				me.del();
	   			}
	   			if(target.name == "xz"){
	   				me.addChild();
	   			}
			}else{
				for(var key in fns){
					if(target.name == key){
						fns[key](target,me);
	   	   			}
				}
			}
		}
	};
	
	
	this.operate = {
		field : 'operate',
		title : '操作',
		width : 100,
		align : 'center',
		formatter : function(value, row, index) {
			var html = '';
			if(row[me.idField] && me.p_create) {
				html += '<a class="operate_btn" href="javascript:void(0);" name="xz">新增</a>';
			}
			if(row[me.idField] && me.p_update) { 
				html += '<a class="operate_btn" href="javascript:void(0);" name="bj">编辑</a>';
			}
			if(row[me.idField] && me.p_delete) {
				html += '<a class="operate_btn" href="javascript:void(0);" name="sc">删除</a>';
			}
			return html;
		}
	};
	
	this.setGrid = function(){
		me.gridConf = {
				fit : true,//设置fit属性后height就无效了
				url:me.listUrl,
			    idField:me.idField,
			    treeField:me.treeField,
			    nowrap : true,
				// striped:true,//条纹
				rownumbers : true,
			    singleSelect : true,
				checkOnSelect : false,
				selectOnCheck : false,
				//height:me.height|| CTT.height,
			    columns:me.columns,
			    toolbar:me.getToolbar(),
			    onBeforeLoad:function(row,param){
			    	if(row)
			    		$(this).treegrid('options').url=me.listUrl;
			    },
			    loadFilter:function(data,parentId){
			    	if(data && data.rows){
			    		for(var i in data.rows){
			    			//设置该节点为父节点
			    			data.rows[i]['state'] = 'closed';
			    		}
			    		return data.rows;
			    	}
			    },
				onClickRow:function(index,row){
					if(typeof me.onClickRow == "function"){
						me.onClickRow(row,index,me);
					}
				}

		};
		
		me.dataGrid =  me.dom.treegrid(me.gridConf);
		
		me.bindClick(me.dataGrid.treegrid("options"));
		
		me.afterInit();//初始化方法
		
		return me.dataGrid;
	}
	
	/**
	 * divTag jq div对象
	 */
	this.getWin = function(divTag){
		me.initAtt();//初始化属性
		this.dom = divTag;
		if(me.formUrl){//加载form
			var div = this.dom.find("div");
			//使用属性动态创建form样式不好调 静态页面的请求流量很小 有需要在优化
			div.load(me.formUrl,function(responseTxt,statusTxt,xhr){
			    if(statusTxt=="success"){
			    	me.dialog = div;
			    	me.form = div.find('form');
			    	return me.setGrid();
			    }else if(statusTxt=="error"){
			    	alert("Error: "+xhr.status+": "+xhr.statusText);
			    }
		    });
		}
	};
}


