FXC.ns("FXC.xxgl");

FXC.xxgl.filemanagement_config = {
	idField:"id",
	permissionsKey:"xxgl:filemanagement",
	listUrl:_webApp + '/xxgl/filemanagement/list'+suffix,
	saveUrl:_webApp + '/xxgl/filemanagement/add'+suffix,
	delUrl:_webApp + '/xxgl/filemanagement/delete'+suffix,
	updateUrl:_webApp + '/xxgl/filemanagement/update'+suffix,
	formUrl:'/form/xxgl/filemanagement.html',
	singleSelect:true,//单选
	formPanelComboboxs:{'student.id':{
		valueField:'id',
		textField:'name',
		url:_webApp + '/xxgl/student/subsidiaryList'+suffix,
		loadJs:'/xxgl/student.js',
	}},
	afterEdit:function(row){
		var me = this;
		if(row.student){
			var combox = $(me.form.find('input[comboname="student.id"]'));
			console.log(row.student.name);
			combox.combobox('setValue',row.student.id);
			combox.combobox('setText',row.student.name);
		}
	},
	columns : [[ {
		field : 'id',
		title : 'ID',
		sortable : true,
		hidden : true
	},{
		field:'student',
		title:'学号',
		width:120,
		formatter : function(value, row, index) {
			return value ? value.number:'';
		}
	},{
		field:'student.name',
		title:'姓名',
		formatter : function(value, row, index) {
			return row.student ? row.student.name:'';
		}
	},{
		field:'student.sfz',
		title:'身份证',
		formatter : function(value, row, index) {
			return row.student ? row.student.sfz:'';
		}
	},{
		field:'student.cellPhone',
		title:'手机号',
		formatter : function(value, row, index) {
			return row.student ? row.student.cellPhone:'';
		}
	},{
		field:'createTime',
		title:'时间',
	}]]
};

FXC.xxgl.filemanagement = new getPage(FXC.xxgl.filemanagement_config);