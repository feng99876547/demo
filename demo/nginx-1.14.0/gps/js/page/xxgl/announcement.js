FXC.ns("FXC.xxgl");

FXC.xxgl.announcement_config = {
	title:"公告",
	idField:"id",
	permissionsKey:"xxgl:announcement",
	listUrl:_webApp + '/xxgl/announcement/list'+suffix,
	saveUrl:_webApp + '/xxgl/announcement/add'+suffix,
	delUrl:_webApp + '/xxgl/announcement/delete'+suffix,
	updateUrl:_webApp + '/xxgl/announcement/update'+suffix,
	height:715,
	width:800,
	formUrl:'/form/xxgl/announcement.html',
	singleSelect:true,//单选
	setValidate:['title','content','createTime'],
	beforeSubmit:function(form){
		var content = this.form.find('*[name="content"]').val();
		this.form.find('*[name="content"]').val(content.replace(/\"/g,"'"));  
	},
	afterEdit:function(row){
		var ue = this.form_ueditor['content'];
		ue.execCommand( "cleardoc" );
		ue.execCommand('insertHtml', row.content);
		if(row.top){
			this.setFormVal("istop",1,'radio');
		}else{
			this.setFormVal("istop",2,'radio');
		}
	},
	beforeAdd:function(row){
		console.log(new Date());
		var ue = this.form_ueditor['content'];
		ue.execCommand( "cleardoc" );
		// this.setFormVal("createTime",new Date());
		this.setFormVal("istop",2,'radio');
	},
	onAfterRender:function(target){
		//提前初始化 不然点击事件时在初始化ueditor设置了延时 需要这边也延时执行相关事件
		this.dialog.dialog({closed:true});
	},
	createSearch:[{
		field:'标题',
		type:"text",
		name:"search_LIKE_title"
	}],
	columns : [[ {
		field : 'id',
		title : 'ID',
		sortable : true,
		hidden : true
	},{
		field:'title',
		title:'标题',
		width:300
	},{
		field:'createTime',
		title:'发布时间',
		sortable:true
	},{
		field:'effective',
		title:'有效期',
		sortable:true
	},{
		field : 'top',
		title : '置顶',
		sortable : true,
		formatter : function(value, row, index) {
			return value ? "是":"否";
		}
	}]]

};

FXC.xxgl.announcement = new getPage(FXC.xxgl.announcement_config);
