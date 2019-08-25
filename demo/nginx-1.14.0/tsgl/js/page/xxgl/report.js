FXC.ns("FXC.xxgl");

FXC.xxgl.report_config = {
	idField:"id",
	permissionsKey:"xxgl:report",
	listUrl:_webApp + '/xxgl/report/list'+suffix,
	saveUrl:_webApp + '/xxgl/report/add'+suffix,
	delUrl:_webApp + '/xxgl/report/delete'+suffix,
	updateUrl:_webApp + '/xxgl/report/update'+suffix,
	singleSelect:true,//单选
	width:998,
	//0新建1缴费2档案转入3健康状态4宿舍管理5校园卡6流程完毕
	status:['newState','payment','fileTransfer','healthStatus','dormRoom','schoolCard','end'],
	formPanelComboboxs:{'student.id':{
		valueField:'id',
		textField:'name',
		loadJs:'/xxgl/student.js',
	}},
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
	afterEdit:function(row){
		var me = this;
		if(row.student){
			var combox = $(me.form.find('input[comboname="student.id"]'));
			combox.combobox('setValue',row.student.id);
			combox.combobox('setText',row.student.name);
		}
	},
	tabForm:[
		{
			type:'form',
			nature:true,
			title:'学员报到',
			formUrl:'/form/xxgl/report.html',
		},{
			type:'grid',
			title:'缴费',
			relationField:'search_EQ_student',
			vauleField:'id',
			loadJs:'/xxgl/contributions.js',
			config:'FXC.xxgl.contributions_config',
			initGrid:function(config,tab,me){
				var natureTab = me.tabForm[0];
				config.beforeAdd = function(){
					var _this = this;
					var combox = $(_this.form.find('input[comboname="student.id"]'));
					var natureTabCombox = $(natureTab.form.find('input[comboname="student.id"]'));
					combox.combobox('setValue',natureTabCombox.combobox('getValue'));
					combox.combobox('setText',natureTabCombox.combobox('getText'));
					combox.combobox('readonly',true); 
				}
			}
		},{
			type:'grid',
			title:'档案转入',
			relationField:'search_EQ_student',
			vauleField:'id',
			loadJs:'/xxgl/filemanagement.js',
			config:'FXC.xxgl.filemanagement_config',
			initGrid:function(config,tab,me){
				var natureTab = me.tabForm[0];
				config.beforeAdd = function(){
					var _this = this;
					var combox = $(_this.form.find('input[comboname="student.id"]'));
					var natureTabCombox = $(natureTab.form.find('input[comboname="student.id"]'));
					combox.combobox('setValue',natureTabCombox.combobox('getValue'));
					combox.combobox('setText',natureTabCombox.combobox('getText'));
					combox.combobox('readonly',true);
				}
			}
		},{
			type:'grid',
			title:'健康报告',
			relationField:'search_EQ_student',
			vauleField:'id',
			loadJs:'/xxgl/healthinformation.js',
			config:'FXC.xxgl.healthinformation_config',
			initGrid:function(config,tab,me){
				var natureTab = me.tabForm[0];
				delete config.columns[0][6];
				config.beforeAdd = function(){
					var _this = this;
					var combox = $(_this.form.find('input[comboname="student.id"]'));
					var natureTabCombox = $(natureTab.form.find('input[comboname="student.id"]'));
					combox.combobox('setValue',natureTabCombox.combobox('getValue'));
					combox.combobox('setText',natureTabCombox.combobox('getText'));
					combox.combobox('readonly',true);
				}
			}
		},{
			type:'form',
			title:'宿舍分配',
			formUrl:'/form/xxgl/ssfp.html',
			formPanelComboboxs:{'dormitoryId':{
				valueField:'id',
				textField:'code',
				queryParams:{search_THAN_syrs:0},
				loadJs:'/xxgl/dormitory.js',
			}},
			initGrid:function(tab,me){
				var natureTab = me.tabForm[0];
				var combox = $(this.form.find('input[comboname="student.id"]'));
				var suse =  $(this.form.find('input[comboname="dormitoryId"]'));
				var natureTabCombox = $(natureTab.form.find('input[comboname="student.id"]'));
				var id = natureTabCombox.combobox('getValue');
				if(id){
					FXC.AJAX(_webApp+'/xxgl/student/subsidiaryList'+suffix,{search_EQ_id:id},function(result){
						if(result && result.success){
							combox.combobox('setValue',id);
							combox.combobox('setText',result.rows[0].name);
							// suse.combobox('setValue',result.rows[0].dormitoryId);
						}else{
							console.log("错误");
						}
					});
				}else{
					console.log(natureTabCombox.combobox('getText'));
					combox.combobox('setValue',id);
					combox.combobox('setText',natureTabCombox.combobox('getText'));
				}
				combox.combobox('readonly',true);
				tab.form.get(0).onsubmit = function(){
					var dormitory = tab.form.find('input[comboname="dormitoryId"]').val();
					if(dormitory==""){
						alert("宿舍不能为空!");
						return false;
					}
					FXC.AJAX(_webApp+'/xxgl/report/updatess'+suffix,{id:me.getFormVal("student.id"),dormitoryId:dormitory},function(result){
						if(result && result.success){
							me.messagerShow(result.msg);
						}else{
							me.messagerShow("错误");
						}
					});
					return false;
				};
			}
		},{
			type:'grid',
			title:'校园卡管理',
			relationField:'search_EQ_student',
			vauleField:'id',
			loadJs:'/xxgl/schoolcard.js',
			config:'FXC.xxgl.schoolcard_config',
			initGrid:function(config,tab,me){
				var natureTab = me.tabForm[0];
				config.beforeAdd = function(){
					var _this = this;
					var combox = $(_this.form.find('input[comboname="student.id"]'));
					var natureTabCombox = $(natureTab.form.find('input[comboname="student.id"]'));
					combox.combobox('setValue',natureTabCombox.combobox('getValue'));
					combox.combobox('setText',natureTabCombox.combobox('getText'));
					combox.combobox('readonly',true);
				}
			}
		}
	],
	onOpen:function(){
		var status = this.getFormVal('status');
		this.closeTabs();
		if(!status || this.getIndex(status) == 0){
			this.formTabs.tabs('enableTab',0);
			this.formTabs.tabs('select',0);
		}else{
			var index = this.getIndex(status);
			if(index>-1){
				if(index >= this.status.length-1){
					index = this.status.length-2;//最后一步流程是end 下标比长度小一位
				}
				this.formTabs.tabs('enableTab',index*1);
				this.formTabs.tabs('select',index*1);
				var student = this.getFormVal("student.id");
				this.tabForm[index].initPanel({search_EQ_student:student});
			}
		}
	},
	getAddButtons:function(url,fns,data){
		var me = this;
		buttons =[{
			text : '保存',
			id:"button_save"+me.id,
			disabled:!(me.getFormVal("status")==''),
			handler : function(a) {
				var student = me.getFormVal("student.id");
				FXC.AJAX(me.saveUrl,{'student.id':student,status:'newState'},function(result){
					if(result && result.success){
						me.setFormVal('status','newState');
						me.setFormVal('id',result.rows.id);
						me.setFormVal('student.name',result.rows.name);
						$("#"+"button_save"+me.id).linkbutton('disable');
						$("#"+"button_next"+me.id).linkbutton('enable');
					}
					me.messagerShow(result.msg);
				});
			}
		},{
			id:"button_next"+me.id,
			text : '下一步',
			disabled:me.getFormVal("status")=='',
			handler : function() {
				var status = me.getFormVal('status');
				if(status){
					var id =  me.getFormVal("id");
					var index = me.getIndex(status)*1;
					if(index>-1){
						if(index>=me.status.length-1){
							me.dialog.dialog("close");
							me.refresh();
							return;
						}
						index++;
					}
					var status = me.status[index];
					FXC.AJAX(me.updateUrl,{id:id,status:status},function(result){//更新当前流程为下一流程
						if(result && result.success){
							me.setFormVal('status',me.status[index]);
							if(index < me.status.length-1){
								var student = me.getFormVal("student.id");
								me.formTabs.tabs('disableTab', index-1);
								me.formTabs.tabs('enableTab',index);
								me.formTabs.tabs('select',index);
								me.tabForm[index].initPanel({search_EQ_student:student});
							}else{
								me.dialog.dialog("close");
								me.refresh();
							}
						}else{
							alert("错误");
						}
					});
				}else{
					alert("下一步status为空请联系管理员");
				}
			}
		}];
		return buttons;
	},
	columns : [[ {
		field : 'id',
		title : 'ID',
		sortable : true,
		hidden : true
	},{
		field:'student',
		title:'学号',
		formatter : function(value, row, index) {
			return value ? value.number:'';
		}
	},{
		field:'student.name',
		title:'姓名',
		width:100,
		formatter : function(value, row, index) {
			return row.student ? row.student.name:'';
		}
	},{
		field:'student.xb',
		title:'性别',
		formatter : function(value, row, index) {
			return row.student ? row.student.xb:'';
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
		field:'xq',
		title:'学期',
		formatter : function(value, row, index) {
			return '都是一期';
		}
	},{
		field:'createTime',
		title:'创建时间',
	},{
		field:'status',
		title:'报到流程',
	}]]

};

FXC.xxgl.report = new getTabProcessPage(FXC.xxgl.report_config);