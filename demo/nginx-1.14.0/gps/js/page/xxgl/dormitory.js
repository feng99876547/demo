FXC.ns("FXC.xxgl");

FXC.xxgl.dormitory_config = {
	idField:"id",
	permissionsKey:"xxgl:dormitory",
	listUrl:_webApp + '/xxgl/dormitory/list'+suffix,
	saveUrl:_webApp + '/xxgl/dormitory/add'+suffix,
	delUrl:_webApp + '/xxgl/dormitory/delete'+suffix,
	updateUrl:_webApp + '/xxgl/dormitory/update'+suffix,
	formUrl:'/form/xxgl/dormitory.html',
	singleSelect:true,//单选
	setValidate:['lou','ceng','code','type','zdrs'],
	getUpdateButtons : function(url,fns,data) {
		var me = this;
		if(this.verifyAuthority('p_update')){
			return null;
		}
		buttons =[{
			text : '总人数加1',
			handler : function() {
				var id = me.getFormVal("id");
				if(id){
					FXC.AJAX(_webApp+'/xxgl/dormitory/updateRSZ.do',{id:id},function(result){
						if(result && result.success){
							me.setFormVal('zdrs',me.getFormVal("zdrs")*1+1);
							me.setFormVal('syrs',me.getFormVal("syrs")*1+1);
						}
						me.refresh();
						me.messagerShow(result.msg);
					});
				}
				
			}
		},{
			text : '总人数减1',
			handler : function() {
				var id = me.getFormVal("id");
				if(id){
					FXC.AJAX(_webApp+'/xxgl/dormitory/updateRSJ.do',{id:id},function(result){
						if(result && result.success){
							me.setFormVal('zdrs',me.getFormVal("zdrs")*1-1);
							me.setFormVal('syrs',me.getFormVal("syrs")*1-1);
						}
						me.refresh();
						me.messagerShow(result.msg);
					});
				}
			}
		},{
			text : '确定',
			handler : function() {
				me.ajaxToSave(me.dialog,url,fns,data);
			}
		},{
			text : '取消',
			handler : function() {
				me.dialog.dialog("close");
				
			}
		}];
		return buttons;
	},
	beforeAdd:function(){
		this.setFormVal('type',1,'radio');
		this.setFormVal('yzrs',0);
		this.setFormVal('syrs',0);
		this.setFormAttr("zdrs","disabled",false);
		this.setFormAttr("yzrs","disabled",false);
		this.setFormAttr("syrs","disabled",false);
	},
	beforeEdit:function(){
		this.setFormAttr("zdrs","disabled",true);
		this.setFormAttr("yzrs","disabled",true);
		this.setFormAttr("syrs","disabled",true);
	},
	createSearch:[{
		field:'宿舍楼',
		type:'text',
		name:'search_EQ_lou',
	},{
		field:'宿舍层数',
		type:'text',
		name:'search_EQ_ceng',
	},{
		field:'宿舍编号',
		type:'text',
		name:'search_EQ_code',
	},{
		field:'宿舍类型',
		type:'combobox',
		name:'search_EQ_type',
		data:[['','全部'],[1,'男'],[2,'女']],
	}],
	tabForm:[
		{
			type:'form',
			nature:true,
			title:'宿舍编辑',
			formUrl:'/form/xxgl/dormitory.html',
		},{
			type:'grid',
			title:'成员查看',
			relationField:'search_EQ_dormitoryId',
			vauleField:'id',
			conf:{
				pageSize:20,
				listUrl:_webApp + '/xxgl/student/subsidiaryList'+suffix,
				//checkbox:true,//设置checkbox 
				columns : [[{
					field : 'name',
					title : '名字',
					width:140,
				},{
					field : 'number',
					title : '学号',
					width : 100,
				},{
					field : 'xb',
					title : '性别',
					width : 100,
					formatter : function(value, row, index) {
						return value == 1 ? '男' : '女';
					}
				}]]
			}
			
		}
	],
	columns : [[ {
		field : 'id',
		title : 'ID',
		sortable : true,
		hidden : true
	},{
		field:'lou',
		title:'楼号',
	},{
		field:'ceng',
		title:'楼层',
	},{
		field:'code',
		title:'宿舍编号',
	},{
		field:'type',
		title:'男/女',
		formatter : function(value, row, index) {
			return value == 1 ? '男':'女';
		}
	},{
		field:'zdrs',
		title:'满员',
	},{
		field:'yzrs',
		title:'已住',
	},{
		field:'syrs',
		title:'剩余',
	}]]

};

FXC.xxgl.dormitory = new getTabPage(FXC.xxgl.dormitory_config);

FXC.xxgl.dormitory_select = new selectGrid(FXC.xxgl.dormitory_config);
