FXC.ns("FXC.tsgl");

FXC.tsgl.onlineArticles_config = {
	title:"主题",
	idField:"id",
	permissionsKey:"tsgl:onlineArticles",
	listUrl:_webApp + '/tsgl/onlineArticles/list'+suffix,
	saveUrl:_webApp + '/tsgl/onlineArticles/add'+suffix,
	delUrl:_webApp + '/tsgl/onlineArticles/delete'+suffix,
	updateUrl:_webApp + '/tsgl/onlineArticles/update'+suffix,
	getImageUrl:_webApp+'/tsgl/onlineArticles/xiazai'+suffix,
	height:730,
	width:800,
	p_add:false,
	p_update:false,
	formUrl:'/form/tsgl/onlineArticles.html',
	singleSelect:true,//单选
	setValidate:['theme','createTime'],
	initFormEvent:function(form){
		var test = form.find('input[name="test"]');
		test.bind('click',function(){
			if(this.value.trim()!="")
				window.open(this.value,'','height=500,width=611,scrollbars=yes,status =yes')
		});
	},
	beforeSubmit:function(form){
		var content = this.form.find('*[name="content"]').val();
		this.form.find('*[name="content"]').val(content.replace(/\"/g,"'"));  
	},
	afterEdit:function(row){
		var ue = this.form_ueditor['content'];
		ue.execCommand( "cleardoc" );
		ue.execCommand('insertHtml', row.content);
		this.setFormVal("test",host+"/admin/editMoel.html?id="+row.id);
	},
	onAfterRender:function(target){
		//提前初始化 不然点击事件时在初始化ueditor设置了延时 需要这边也延时执行相关事件
		this.dialog.dialog({closed:true,});
	},
	ajaxToDelte:function(id) {
		var me = this,url = me.delUrl,row = me.dataGrid.datagrid('getSelected');
		$.post(url, {
			id : id,
			themeEditId:row.themeEditId,
		}, function(result) {
			me.feedback(result);
		});
	},
	createSearch:[{
		field:'主题',
		type:"text",
		name:"search_LIKE_theme"
	}],
	columns : [[ {
		field : 'id',
		title : 'ID',
		sortable : true,
		hidden : true
	},{
		field:'title',
		title:'标题',
		width:300,
	},{
		field:'createTime',
		title:'创建时间',
		sortable:true
	},{
		field:'updateTime',
		title:'修改时间',
		sortable:true
	}]]

};

FXC.tsgl.onlineArticles = new getPage(FXC.tsgl.onlineArticles_config);
