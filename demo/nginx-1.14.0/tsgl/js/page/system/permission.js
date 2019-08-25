FXC.ns("FXC.system");

FXC.system.permission_config = {
	title:"权限",
	idField:"id",
	permissionsKey : "system:permission",
	listUrl:_webApp + '/system/permission/list'+suffix,
	saveUrl:_webApp + '/system/permission/add'+suffix,
	delUrl:_webApp + '/system/permission/delete'+suffix,
	updateUrl:_webApp + '/system/permission/update'+suffix,
	selectUrl:_webApp + '/system/permission/list'+suffix,
	formUrl:'/form/system/permission.html',
	singleSelect:true,//单选
	width:400,//dialog的宽高
	height:300,
	setValidate:['name','permission','type',{'type':{required:true,validType:'intOrFloat'}}],
	//singleSelect:false,//多选
	//checkbox:false,//取消checkbox
	createSearch:[{
		field:'状态',
		type:"radio",
		name:"search_EQ_status",
		value:[{
			checked : true,
			text:'全部',
			value:""
		},{
			text:'可用',
			value:"1"
		},{
			text:'不可用',
			value:"0"
		}]
	},{
		field:'名称',
		type:"text",
		name:"search_LIKE_name"
	}],
	columns : [[ {
		field : 'id',
		title : 'ID',
		sortable : true,
		hidden : true
	},{
		field : 'name',
		title : '名称',
		sortable : true
	},{
		field : 'permission',
		title : '权限字符',
		sortable : true
	},{
		field : 'sequence',
		title : '顺序',
		sortable : true
	},{
		field : 'description',
		title : '描述',
		sortable : true
	}, {
		field : 'type',
		title : '权限类型',
		sortable : true,
		formatter : function(value, row, index) {
			var html = value;
			switch (value) {
				case 0:
					html = "系统";
					break;
				case 1:
					html = "普通";
					break;
				case 2:
					html = "附属";
					break;
				case 3:
					html = "私有";
					break;
			}
			return html;
		}
	}, {
		field : 'status',
		title : '状态',
		sortable : true,
		formatter : function(value, row, index) {
			var html = value;
			switch (value) {
				case 0:
					html = "不可用";
					break;
				case 1:
					html = "可用";
					break;
			}
			return html;
		}
	}]]

};

FXC.system.permission = new getPage(FXC.system.permission_config);

FXC.system.permission_select = new selectGrid(FXC.system.permission_config);