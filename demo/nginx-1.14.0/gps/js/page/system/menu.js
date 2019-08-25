FXC.ns("FXC.system");

FXC.system.menu = new getPage({
	idField:"id",
	title:'菜单',
	permissionsKey : "system:permission",
	listUrl:_webApp + '/system/menu/list'+suffix,
	saveUrl: _webApp + '/system/menu/add'+suffix,
	delUrl:_webApp + '/system/menu/delete'+suffix,
	updateUrl:_webApp + '/system/menu/update'+suffix,
	formUrl:'/form/system/menu.html',
	singleSelect:true,//单选
	pageSize:100,
	width:400,//dialog的宽高
	height:300,
	chiledValidate:['name','url','permission','position','status'],
	parentValidate:['name','position','status'],
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
		field:'菜单名称',
		type:"text",
		name:"search_LIKE_name"
	},{
		field:'权限字符',
		type:"text",
		name:"search_LIKE_permission",
	}],
	toolbar : [ {
		text : '新增父菜单',
		iconCls : 'i_add',
		handler : function() {
			if(!FXC.system.menu.verifyAuthority('p_add'))
				FXC.system.menu.add();
			else{
				alert("没有权限");
			}
		}
	} ],
	//默认编辑方法扩展（编辑父菜单不需要提交permission和url 因为） 
	beforeEdit:function(row){//因为easyui的验证事件已经绑定了
		this.clearValidate(this.setValidate,this.form);
		this.setValidate = this.parentValidate;
		this.setFormAttr("permission","disabled",true);
		this.setFormAttr("url","disabled",true);
		if(typeof row.permission == "undefined"){
			row.permission = "";
		}
	},
	//默认编辑方法新增扩展  新增父菜单
	beforeAdd:function(){
		this.clearValidate(this.setValidate,this.form);
		this.setValidate = this.parentValidate;
		this.setFormAttr("permission","disabled",true);
		this.setFormAttr("url","disabled",true);
		this.setFormVal('menuType','PARENT');
	},
	//自定义的新增子菜单编辑函数
	addSubmenu:function(){
		this.clearValidate(this.setValidate,this.form);
		this.setValidate = this.chiledValidate;
		this.dialog.form('clear');
		this.setFormAttr("permission","disabled",false);
		this.setFormAttr("url","disabled",false);
		
		var row = this.dataGrid.datagrid('getSelected');
		this.setFormVal('parentId',row.id,'select');//parentId设置了残废值不会被提交
		this.setFormVal('menuType','CHILD');
		this.addOpenEdit({parentId:row.id});//提交时添加parentId
	},
	//自定义编辑 编辑子菜单
	editSubmenu : function () {
		this.clearValidate(this.setValidate,this.form);
		this.setValidate = this.chiledValidate;
		this.setFormAttr("permission","disabled",false);
		this.setFormAttr("url","disabled",false);
		
		var row = this.dataGrid.datagrid('getSelected');
		this.dialog.form('load', row);
		this.editOpenEdit({parentId:row.parentId});
	},
	onAfterRender:function(target){
		//初始化父菜单
		var data = $(target).datagrid('getData');
		if (data) {
			var parent = this.dialog.find('form').find('select[name="parentId"]');
			parent.html('<option > </option>');
			$.each(data.rows, function(index, node) {
				if(node.menuType == "PARENT"){
					parent.append(
							'<option value="' + node.id + '">' + node.name
									+ '</option>');
				}
			});
		}
	},
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
	columns : [[{
		field : 'id',
		title : 'ID',
		sortable : true,
		hidden : true
	}, {
		field : 'name',
		title : '菜单名称',
		width : 150,
		sortable : true,
	},{
		field : 'icon',
		title : '图标',
		width : 50,
		align : 'center',
		halign : 'center',
		formatter : function(value, row, index) {
			return '<i class="fa ' + value
					+ '"></i>';
		}
	},{
		field : 'url',
		title : 'URL',
		width : 200,
		sortable : true,
	},{
		field : 'permission',
		title : '对应权限',
		width : 220,
		sortable : true,
	}, {
		field : 'position',
		title : '位置',
		sortable : true,
	},{
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
	},{
		field : 'operate',
		title : '操作',
		width : 160,
		align : 'left',
		halign : 'center',
		formatter : function(value, row, index) {
			var me = FXC.system.menu;
			var html = "";
			if (row.menuType == "PARENT") {
				if (!me.verifyAuthority('p_update')) {
					html += '<a class="operate_btn" href="javascript:void(0);" name = "bj">编辑</a>';
				}
				if (!me.verifyAuthority('p_delete')) {
					html += '<a class="operate_btn" href="javascript:void(0);" name = "sc">删除</a>';
				}
				if (!me.verifyAuthority('p_add')) {
					html += '<a class="operate_btn" href="javascript:FXC.system.menu.addSubmenu(\''
							+ row.id
							+ '\');">新增子菜单</a>';
				}
			} else if (row.menuType == "CHILD") {
				if (!me.verifyAuthority('p_update')) {
					html += '<a class="operate_btn" href="javascript:FXC.system.menu.editSubmenu();">编辑</a>';
				}
				if (!me.verifyAuthority('p_delete')) {
					html += '<a class="operate_btn" href="javascript:void(0);" name = "sc">删除</a>';
				}

			}
			return html;
		}
	} ]],
});