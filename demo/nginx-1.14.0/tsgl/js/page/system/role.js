FXC.ns("FXC.system");

FXC.system.role_config = {myName:'FXC.system.role'};

FXC.system.role_config.north_conf = {
	idField:"id",
	permissionsKey : "system:role",
	listUrl:_webApp + '/system/role/list'+suffix,
	saveUrl: _webApp + '/system/role/add'+suffix,
	delUrl:_webApp + '/system/role/delete'+suffix,
	updateUrl:_webApp + '/system/role/update'+suffix,
	singleSelect:true,//单选
	height:200,
	setValidate:['name'],
	createSearch:[{
		field:'名称',
		type:"text",
		name:"search_LIKE_name"
	},{
		field:'标识',
		type:"text",
		name:"search_LIKE_role"
	}],
	columns : [[{
			field : 'id',
			title : 'ID',
			hidden : true
		}, {
			field : 'name',
			title : '名称',
			width : 100,
			sortable : true
		}, {
			field : 'role',
			title : '标识',
			width : 100,
			sortable : true
		}, {
			field : 'description',
			title : '描述',
			width : 200,
			sortable : true
		}]
	]
};

FXC.system.role_config.west_conf = {
	formUrl:'/form/system/role.html',
	widthRatio:0.4
};

FXC.system.role_config.east_conf = {
	loadJs:'/system/permission.js',
	widthRatio:0.6,
	buttonName:'附加权限',
	relationField:'roleId',
	vauleField:'id',
	dialogUrl : _webApp + '/system/role/getChoosablePermission'+suffix,
	conf:{
		pageSize:200,
		url:_webApp+'/system/role/getRolePermission'+suffix,
		//checkbox:true,//设置checkbox 
		columns : [[{ 
			field : 'id',
			title : 'ID',
			sortable : true,
			hidden : true
		}, {
			field : 'name',
			title : '名称',
			width:140,
		}, {
			field : 'permission',
			title : '权限字符',
			width:160,
		}, {
			field : 'description',
			title : '描述',
			width:160,
		}]]
	}
};

FXC.system.role = new getLayoutGrid(FXC.system.role_config);

FXC.system.role_select = new selectGrid(FXC.system.role_config.north_conf);



