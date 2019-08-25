FXC.ns("FXC.system");
FXC.system.role = new getLayoutGrid();
FXC.system.role.north_conf = {
	idField:"id",
	permissionsKey : "system:role",
	listUrl:_webApp + '/system/role/list'+suffix,
	saveUrl: _webApp + '/system/role/add'+suffix,
	delUrl:_webApp + '/system/role/delete'+suffix,
	updateUrl:_webApp + '/system/role/update'+suffix,
	singleSelect:true,//单选
	height:400,
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
			field : 'checkbox',
			checkbox : true
		},{
			field : 'id',
			title : 'ID',
			sortable : true,
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



FXC.system.role.west_conf ={
	formUrl:'/form/system/role.html',
	widthRatio:0.4
};

FXC.system.role.east_conf ={
	widthRatio:0.6,
	buttonName:'附加权限',
	relationField:'roleId',
	vauleField:'id',
	loadJs:'/system/permission.js',
	conf:{
		url:_webApp+'/system/role/getRolePermission'+suffix,
	}
};