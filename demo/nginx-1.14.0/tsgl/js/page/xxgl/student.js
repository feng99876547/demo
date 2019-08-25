FXC.ns("FXC.xxgl");

FXC.xxgl.student_config = {
	idField:"id",
	permissionsKey : "xxgl:student",
	listUrl:_webApp + '/xxgl/student/list'+suffix,
	saveUrl: _webApp + '/xxgl/student/add'+suffix,
	delUrl:_webApp + '/xxgl/student/delete'+suffix,
	updateUrl:_webApp + '/xxgl/student/update'+suffix,
	selectUrl:_webApp + '/xxgl/student/subsidiaryList'+suffix,//作为附属权限使用的url
	formUrl:'/form/xxgl/student.html',
	singleSelect:true,//单选
	setValidate:['name','number','faculty','sfz','cellPhone'],
	height:500,
	beforeAdd:function(){
		this.setFormVal('xb',1,'radio');
		this.setFormVal('status',5,'radio');
	},
	createSearch:[{
		field:'姓名',
		type:'text',
		name:'search_EQ_name'
	},{
		field:'学号',
		type:'text',
		name:'search_LIKE_number'
	},{
		field:'身份证',
		type:'text',
		name:'search_EQ_sfz'
	},{
		field:'宿舍',
		type:'panelCombobox',
		name:'search_EQ_dormitoryId',
		textField:'code',
		valueField:'id',
		lazy:true,//自定义懒加载
		url:_webApp+'/xxgl/dormitory/subsidiaryList'+suffix,
		loadJs:'/xxgl/dormitory.js',
	}],
	columns : [[{
			field : 'id',
			title : 'ID',
			hidden : true
		},{
			field : 'number',
			title : '学号',
			width : 120,
			sortable : true
		},{
			field : 'name',
			title : '名字',
			width : 120,
		},{
			field : 'xb',
			title : '性别',
			width : 100,
			formatter : function(value, row, index) {
				return value == 1 ? '男':'女';
			}
		},{
			field : 'sfz',
			title : '身份证',
			width : 100,
		},{
			field : 'faculty',
			title : '学院',
			width : 100,
		},{
			field : 'zy',
			title : '专业',
			width : 100,
		},{
			field : 'className',
			title : '班级',
			width : 100,
		},{
			field : 'cellPhone',
			title : '电话',
			width : 100,
		}]
	]
};



FXC.xxgl.student = new getPage(FXC.xxgl.student_config);

FXC.xxgl.student_select = new selectGrid(FXC.xxgl.student_config);
