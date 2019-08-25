FXC.ns("FXC.xxgl");

FXC.xxgl.schoolcard_config = {
	idField:"id",
	permissionsKey:"xxgl:schoolcard",
	listUrl:_webApp + '/xxgl/schoolcard/list'+suffix,
	saveUrl:_webApp + '/xxgl/schoolcard/add'+suffix,
	delUrl:_webApp + '/xxgl/schoolcard/delete'+suffix,
	updateUrl:_webApp + '/xxgl/schoolcard/update'+suffix,
	formUrl:'/form/xxgl/schoolcard.html',
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
	createSearch:[{
		field:'学生',
		type:'panelCombobox',
		name:'search_EQ_student',
		textField:'name',
		valueField:'id',
		lazy:true,//自定义懒加载
		url:_webApp+'/xxgl/student/subsidiaryList'+suffix,
		loadJs:'/xxgl/student.js',
	}],
	columns : [[ {
		field : 'id',
		title : 'ID',
		sortable : true,
		hidden : true
	},{
		field:'student',
		title:'姓名',
		width:130,
		formatter : function(value, row, index) {
			return value ? value.name:'';
		}
	},{
		field:'je',
		title:'金额',
		width:130,
	},{
		field:'card',
		title:'卡号',
		width:130,
	},{
		field:'pzh',
		title:'开通凭证',
		width:130,
	},{
		field:'createTime',
		title:'时间',
	}]]
};

FXC.xxgl.schoolcard = new getPage(FXC.xxgl.schoolcard_config);
