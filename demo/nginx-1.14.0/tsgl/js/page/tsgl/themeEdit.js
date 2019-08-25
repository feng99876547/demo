FXC.ns("FXC.tsgl");

FXC.tsgl.themeEdit_config = {
	title:"主题",
	idField:"id",
	permissionsKey:"tsgl:themeEdit",
	listUrl:_webApp + '/tsgl/themeEdit/list'+suffix,
	saveUrl:_webApp + '/tsgl/themeEdit/add'+suffix,
	delUrl:_webApp + '/tsgl/themeEdit/delete'+suffix,
	updateUrl:_webApp + '/tsgl/themeEdit/update'+suffix,
	getImageUrl:_webApp+'/tsgl/themeEdit/xiazai'+suffix,
	publishUrl:_webApp+'/tsgl/themeEdit/publish'+suffix,
	height:715,
	width:800,
	formUrl:'/form/tsgl/themeEdit.html',
	singleSelect:true,//单选
	setValidate:['theme','createTime'],
	// initFormEvent:function(form){
	// 	var test = form.find('input[name="test"]');
	// 	test.bind('click',function(){
	// 		if(this.value.trim()!="")
	// 			window.open(this.value,'','height=500,width=611,scrollbars=yes,status =yes')
	// 	});
	// },
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
	beforeAdd:function(row){
		var ue = this.form_ueditor['content'];
		ue.execCommand( "cleardoc" );
		this.setFormVal("createTime",new Date());
	},
	// dialogClose:function(){
	// 	var ue = this.form_ueditor['content'];
	// 	ue.execCommand( "cleardoc" );
	// },
	onAfterRender:function(target){
		//提前初始化 不然点击事件时在初始化ueditor设置了延时 需要这边也延时执行相关事件
		this.dialog.dialog({closed:true});
	},
	dialogButtons:[{
		text : '发布文章',
		_permissionKey:'p_publish',
		handler : function(me) {
			me.ajaxToSave(me.dialog,me.publishUrl);
		}
	},{
		text : '生成图片',
		_permissionKey:'p_xiazai',
		handler : function(me) {
			debugger;
			var id = me.form.find('*[name="id"]').val();
			if(id!="" && id.length>0){
				var a = document.createElement('a')
			    var event = new MouseEvent('click')
			    a.download = name || '1234';
			    a.href = me.getImageUrl+'?id='+id;
			    a.dispatchEvent(event);
			}
		}
	}
	,'ok','no'],
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
		sortable : true
	},{
		field:'createTime',
		title:'创建时间',
		sortable:true
	},{
		field : 'releaseStatus',
		title : '是否发布',
		sortable : true,
		formatter : function(value, row, index) {
			var html = value;
			switch (value) {
				case 1:
					html = "未发布";
					break;
				case 2:
					html = "发布";
					break;
			}
			return html;
		}
	}]]

};

FXC.tsgl.themeEdit = new getPage(FXC.tsgl.themeEdit_config);
