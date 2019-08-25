FXC.ns("FXC.system");

FXC.system.dictionary_config = {
	title:"字典",
	idField:"id",
	treeField:'name',
	permissionsKey : "system:dictionary",
	listUrl:_webApp + '/system/dictionary/list'+suffix,
	saveUrl:_webApp + '/system/dictionary/add'+suffix,
	delUrl:_webApp + '/system/dictionary/delete'+suffix,
	updateUrl:_webApp + '/system/dictionary/update'+suffix,
	formUrl:'/form/system/dictionary.html',
	width:300,
	height:250,
	pageSize:200,
	rootParamKey:'search_EQ_level',
	rootParamValue:0,
	createSearch:[{
		field:'名称',
		type:"text",
		name:"search_LIKE_name"
	},{
		field:'编码',
		type:"text",
		name:"search_EQ_code"
	},{
		field:'层级',
		type:"text",
		name:"search_EQ_level"
	},{
		field:'创建人',
		type:'panelCombobox',
		name:'search_EQ_createUser',
		valueField:'id',
		textField:'name',
		//url:_webApp + '/system/user/subsidiaryList'+suffix,
		loadJs:'/system/user.js',
	},{
		field:'删除状态',
		type:'combobox',
		name:'search_EQ_state',
		data:[['','全部'],[1,'未删除'],[5,'删除']]
	}],
	columns : [[{
		field : 'id',
		title : 'ID',
		sortable : true,
		hidden : true
	},{
		field : 'name',
		title : '名称',
		width : 180,
		sortable : true
	},{
		field : 'code',
		title : '编码',
		width : 120,
		sortable : true
	},{
		field : 'remarks',
		title : '备注',
		width : 200,
	},{
		field : 'sequence',
		title : '排序',
	},{
		field : 'showPeanl',
		title : '弹窗',
	},{
		field : 'level',
		title : '层级',
	},{
		field : 'createUser',
		title : '创建人',
		formatter : function(value, row, index) {
			return value?value.id:value;
		}
	},{
		field : 'createTime',
		title : '创建时间',
		sortable : true,
	},{
		field : 'updateUser',
		title : '修改人',
		formatter : function(value, row, index) {
			return value?value.id:value;
		}
	},{
		field : 'updateDate',
		title : '修改时间',
		sortable : true,
	}]]

};

FXC.system.dictionary = new getTreeGridPage(FXC.system.dictionary_config);