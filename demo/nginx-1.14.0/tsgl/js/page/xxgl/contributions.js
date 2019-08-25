FXC.ns("FXC.xxgl");

FXC.xxgl.contributions_config = {
	idField:"id",
	permissionsKey:"xxgl:contributions",
	listUrl:_webApp + '/xxgl/contributions/list'+suffix,
	saveUrl:_webApp + '/xxgl/contributions/add'+suffix,
	delUrl:_webApp + '/xxgl/contributions/delete'+suffix,
	updateUrl:_webApp + '/xxgl/contributions/update'+suffix,
	formUrl:'/form/xxgl/contributions.html',
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
		title:'费用',
		width:130,
	},{
		field:'zfddh',
		title:'支付订单号',
		width:130,
	},{
		field:'date',
		title:'时间',
	}]]
};

FXC.xxgl.contributions = new getPage(FXC.xxgl.contributions_config);

FXC.xxgl.contributions_select = new selectGrid(FXC.xxgl.contributions_config);
