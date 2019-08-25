FXC.ns("FXC.system");

FXC.system.user_config={
	myName:'FXC.system.user',
	formPanelComboboxs:{'dormitoryId':{
		valueField:'id',
		textField:'code',
		queryParams:{search_THAN_syrs:0},
		loadJs:'/xxgl/dormitory.js',
	}},
	afterAdd:function(){
		this.setFormVal('xb',1,'radio');
		this.setFormVal('status',5,'radio');
	},
	afterEdit:function(row){
		var me = this;
		if(row && row.dormitoryId){
			$.post(_webApp+'/xxgl/dormitory/subsidiaryList'+suffix, {
				search_EQ_id : row.dormitoryId
			}, function(r) {
				var result = JSON.parse(r);
				var combox = $(me.form.find('input[comboname="dormitoryId"]'));
				combox.combobox('setValue',result.rows[0].id);
				combox.combobox('setText',result.rows[0].code);
			});
		}
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
		field:'姓名',
		type:'text',
		name:'search_EQ_name'
	},{
		field:'院系',
		type:'combobox',
		name:'search_EQ_faculty',
		textField:'name',
		valueField:'id',
		queryParams:{search_THAN_code:'FACULTY'},
		lazy:true,//自定义懒加载
		url:_webApp + '/system/dictionary/list'+suffix,
		//value:1
		//data:[{id:1,text:'xxx'},{id:2,text:'sss'},{id:3,text:'sss'}]
		// data:['美术','音乐']
	},{
		field:'宿舍',
		type:'panelCombobox',
		name:'search_EQ_dormitoryId',
		textField:'code',
		valueField:'id',
		lazy:true,//自定义懒加载
		url:_webApp+'/xxgl/dormitory/subsidiaryList'+suffix,
		loadJs:'/xxgl/dormitory.js',
	}
	// ,{
	// 	field:'角色',
	// 	type:'panelCombobox',
	// 	name:'search_EQ_roleId',
	// 	valueField:'id',
	// 	textField:'name',
	// 	//url:'',//没设置使用role.js默认的listUrl
	// 	loadJs:'/system/role.js',
	// }
	,{
		field:'学号',
		type:'text',
		name:'search_Like_number'
	},{
		field:'身份证',
		type:'text',
		name:'search_EQ_sfz'
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
			field : 'name',
			title : '名字',
			width : 100,
		},{
			field : 'number',
			title : '学号',
			width : 100,
			sortable : true
		},{
			field : 'faculty',
			title : '院系',
			width : 100,
		},{
			field : 'xb',
			title : '性别',
			width : 100,
			formatter : function(value, row, index) {
				return value == 1 ? '男':'女';
			}
		},{
			field : 'dz',
			title : '地址',
			width : 100,
		},{
			field : 'zzmm',
			title : '政治面貌',
			width : 100,
		},{
			field : 'sfz',
			title : '身份证',
			width : 100,
		},{
			field : 'zy',
			title : '专业',
			width : 100,
		},{
			field : 'cellPhone',
			title : '电话',
			width : 100,
		},{
			field : 'remarks',
			title : '备注',
			width : 100,
		}]
	]
};

FXC.system.user_config.west_conf = {
	formUrl:'/form/system/user.html',
	widthRatio:0.6
};

FXC.system.user_config.east_conf = {
	widthRatio:0.4,
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
