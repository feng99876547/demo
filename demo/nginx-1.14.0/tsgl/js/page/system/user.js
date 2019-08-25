FXC.ns("FXC.system");

FXC.system.user_config={
	myName:'FXC.system.user',
	afterAdd:function(){
		this.setFormVal('status',5,'radio');
	},
};

FXC.system.user_config.north_conf = {
	idField:"id",
	permissionsKey : "system:user",
	listUrl:_webApp + '/system/user/list'+suffix,
	saveUrl: _webApp + '/system/user/add'+suffix,
	delUrl:_webApp + '/system/user/delete'+suffix,
	updateUrl:_webApp + '/system/user/update'+suffix,
	selectUrl:_webApp + '/system/user/subsidiaryList'+suffix,//作为附属权限使用的url
	singleSelect:true,//单选
	setValidate:['name','number','loginName','faculty','cellPhone'],
	height:200,
	createSearch:[{
		field:'账号',
		type:'text',
		name:'search_EQ_loginName'
	},{
		field:'姓名',
		type:'text',
		name:'search_EQ_name'
	},{
		field:'手机号',
		type:'text',
		name:'search_EQ_cellPhone'
	}],
	columns : [[{
			field : 'id',
			title : 'ID',
			hidden : true
		},{
			field : 'loginName',
			title : '账号',
			width : 100,
			sortable : true
		},{
			field : 'createTime',
			title : '创建时间',
			width : 150,
		},{
			field : 'name',
			title : '名字',
			width : 120,
		},{
			field : 'cellPhone',
			title : '电话',
			width : 120,
		},{
			field : 'remarks',
			title : '备注',
			width : 120,
		}]
	]
};

FXC.system.user_config.west_conf = {
	formUrl:'/form/system/user.html',
	widthRatio:0.4
};

FXC.system.user_config.east_conf = {
	widthRatio:0.6,
	buttonName:'添加角色',
	relationField:'id',
	vauleField:'id',
	loadJs:'/system/role.js',
	dialogUrl : _webApp + '/system/role/subsidiaryList'+suffix,
	conf:{
		pageSize:200,
		url:_webApp+'/system/user/getMyRoles'+suffix,
		//checkbox:true,//设置checkbox 
		columns : [[{ 
			field : 'id',
			title : 'ID',
			hidden : true
		},{
			field : 'name',
			title : '名称',
			width:140,
		},{
			field : 'role',
			title : '标识',
			width:160,
		},{
			field : 'description',
			title : '描述',
			width:160,
		}]]
	}
	
}

FXC.system.user = new getLayoutGrid(FXC.system.user_config);

FXC.system.user_select = new selectGrid(FXC.system.user_config.north_conf);
