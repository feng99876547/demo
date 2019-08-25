FXC.loadJS.loadJs(host+'/js/util/easyUi.js');

/**
 *  查询grid接口
 * 作为公共的查询gird修改属性需要使用FXC.easyui.newSelectGrid方法重新构建一个 不然会冲突
 */
var selectGrid = function(conf){
	
	var me = this;

	this.id = FXC.getId();
	
	this.selectDataGrid = null;
	
	this.columns ;
	
	this.title = null;
	
	this.sortName = null;//排序字段
	
	this.sortOrder = null;//排序状态
	
	this.idField = 'id';
	
	this.queryParams ;//默认附加参数
	
	this.checkbox = true;//默认有checkbox

	this.singleSelect = true;//默认单选

	this.mark_search_form=CTT.mark_search_form;

	//初始化属性 需要放到后面
	FXC.easyui.initConfg(this,conf);
};

selectGrid.prototype.getDateTime = function(value){
	if(value){
		return new Date(value).format("yyyy-MM-dd hh:mm:ss");
	}
};

//更新当前id
selectGrid.prototype.initId = function(fn){
	this.id = FXC.getId();
};

/**
*清空查询重新加载
*/
selectGrid.prototype.reload = function(){
	this.searchForm.form("clear");
	this.runSearch();
}

selectGrid.prototype.getSearch_form_id=function(){
	return this.mark_search_form+this.id;
}

selectGrid.prototype.setSelectDataGrid = function(grid){
	this.selectDataGrid = grid;
}

/** 初始化搜索栏 事件 默认是会自动初始化的 */
selectGrid.prototype.initSearch = function() {
	var me = this;
	this.searchForm = $("#"+this.getSearch_form_id());
	// 对搜索按钮和单选框绑定点击搜索事件
	this.searchForm.find("input:radio").click(function() {
		me.runSearch();
	});
	this.searchForm.find("input").keydown(function(event) {
		if (event.keyCode == 13) {
			me.runSearch();
			return false;
		}
	});
	//初始化panelCombobox
	FXC.initSrarch.initPanelCombobox(this.searchForm.find('input[data-typefxc="panelCombobox"]'),me.createSearch);
	FXC.initSrarch.initCombobox(this.searchForm.find('input[data-typefxc="combobox"]'),me.createSearch);
};
//判断是否需要使用自定义查询方法
selectGrid.prototype.runSearch=function() {
	if(typeof this.search == 'function'){
		this.search();
	}else{
		this.searchload();
	}
}

selectGrid.prototype.searchload = function() {
	var searchData = $("#"+this.getSearch_form_id()).serializeJson();
	if(this.queryParams){
		$.extend(true,searchData,this.queryParams);
	}
	this.refresh(searchData);
};

selectGrid.prototype.pushToolbar = function(toolbar){
	var me = this;
	toolbar.push({
		//根据search_form_id生成对应的search form
		text:FXC.initSrarch.appendSearch(this),
	});
	toolbar.push({
		text: '搜索',
		iconCls: 'icon-query',
		handler: function(){
			me.runSearch();
		}
	});
	toolbar.push({
		text: '清空',
		iconCls: 'icon-query',
		handler: function(){
			 $("#"+me.getSearch_form_id()).form('clear');
		}
	});
}

/**
 * 刷新
 */
selectGrid.prototype.refresh =  function(data) {
	this.selectDataGrid.datagrid("reload",data?data:this.queryParams);
	//清除所有选中项
	this.selectDataGrid.datagrid("clearChecked");
};

/**
* 修正不符合框架类型的data
*/
selectGrid.prototype.verifyData = function(data){
	if(data && data.rows && data.rows.length > 0){
		return data;
	}else if(data.msg == '1001'){
		top.location.href=host+'/index.html';
	}else{
		return {rows:[],total:1};
	}
};


/**
*判断列是否需要加checkbox
*/
selectGrid.prototype.initChecked = function(columns,checkboxBoolean){
	if(columns[0][0].field == 'checkbox'){
		return columns;
	}else{
		var col = [];
		col[0] = [];
		var check = {field:'checkbox',checkbox: true,hidden:false};
		if(!checkboxBoolean) {
			check.hidden = true;
		}
		col[0].push(check);
		for(var i in columns[0]){
			col[0].push(columns[0][i]);
		}
	}
	return col;
};

//生成toolbar的html
selectGrid.prototype.getToolbar = function(){
	var toolbar=[];
	if(this.createSearch){
		this.pushToolbar(toolbar);
	}
	return toolbar
};

selectGrid.prototype.getSelectConf = function(){
	var me = this;
	var gridConf = {
		fit : true,
		url :this.url || this.selectUrl || this.listUrl,
		idField : this.idField || "id",
		//height : this.height || CTT.height,
		nowrap : true,
		striped:true,//条纹
		rownumbers : true,
		pagination : true,
		closed:false,
		singleSelect :  this.singleSelect,//默认多选
		checkOnSelect : true,
		selectOnCheck : true,
		pageSize : 20 ,
		pageList:[20, 30, 40],
		autoRowHeight : true,
		toolbar:this.toolbar || this.getToolbar(true),
		columns : this.columns,
		queryParams:this.queryParams || null,
		columns :this.initChecked(this.columns,this.checkbox),//this.checkbox默认不存在false 所以默认没有checkbox 和getWin的相反
		loadFilter:function (data) {//一次渲染这个方法会被触发两次 一次的data是field字段 一次是值
			return me.verifyData(data);
	    },
	};
	if(this.pageSize){//设置页大小
		FXC.easyui.setPageSize(gridConf,this.pageSize);
	}

	// this.initSearch();
	
	return  gridConf;
};




