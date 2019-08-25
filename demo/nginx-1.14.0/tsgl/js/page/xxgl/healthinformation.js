FXC.ns("FXC.xxgl");

FXC.xxgl.healthinformation_config = {
	idField:"id",
	permissionsKey:"xxgl:healthinformation",
	listUrl:_webApp + '/xxgl/healthinformation/list'+suffix,
	saveUrl:_webApp + '/xxgl/healthinformation/add'+suffix,
	delUrl:_webApp + '/xxgl/healthinformation/delete'+suffix,
	updateUrl:_webApp + '/xxgl/healthinformation/update'+suffix,
	formUrl:'/form/xxgl/healthinformation.html',
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
	},{
		field:'tjbg',
		title:'体检报告',
		formatter : function(value, row, index) {
			if(value){
				var name = value.substring(value.lastIndexOf("/")+1);
				return '<a class="operate_btn" href = "'+imagePath+value+'" download = "'+name+'">下载</a>';
			}else{
				return '<a class="operate_btn" href="javascript:void(0);">未上传</a>'
			}
			
		}
	}]]
};

FXC.xxgl.healthinformation = new getPage(FXC.xxgl.healthinformation_config);