FXC.loadJS.loadJs(host+'/js/model/common.js');
var getLayoutGrid = function(conf){
 	 
	var me = this;
//	this.north;//上 
//	this.west;//左
	this.east;//右

	this.id = FXC.getId();

	this.betweenHeight = 500;//中部高度

	this.minHeight = 250;//最小高度

	this.widthRatio;//宽度比例

	this.gridName;//用于指向复杂面板组合中的主grid

	$.extend(true,this,conf);

	//更新当前north id
	this.initId = function(fn){
		this.north.initId(fn);
	};

	this.setSelectDataGrid = function(grid){
		this.north.selectDataGrid(grid);
	};

/////////////////////////////////////////initgrid///////////////////////////////////////////////////////////////////////

	/**
	 * 新增
	 */
	this.north_conf.add = function() {
		me.clearEdit();
		me.edit();
		if(typeof me.afterAdd == "function"){
			me.afterAdd();
		}
	};


	//删除
	this.north_conf.ajaxToDelte = function(id) {
		var rows = me.east.datagrid("getRows");
		var ids = new Array();
		var north = me.north;
		if (rows) {
			for (var i in rows) {
				if(rows[i])
					ids.push(rows[i]['id']);
			}
		}
		var url = north.delUrl;
		$.post(url, {
			id : id,
			ids :ids
		}, function(result) {
			north.feedback(result,me.dialog);
			me.clearEdit();
		});
	};


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	//east删除事件
	this.eastDel = function(){
		var gird = this.east;
		var row = gird.datagrid('getSelected');
		if (row) {
			var index = gird.datagrid('getRowIndex', row);
			gird.datagrid('deleteRow', index);
		}
	};

	this.clearEdit=function(){
		if(this.east.attr("class")){
			this.north.dataGrid.datagrid('clearSelections');//清空选中
			var rows = this.east.datagrid('getRows');//新增时清空数据
			var len = rows.length;
			for(var i=0;i<len;i++){
				this.east.datagrid('deleteRow',0);
			}
		}
		this.initForm();
	}

	//初始化编辑面板
	this.edit = function(row){
		this.initForm(row);
		if(row){
			this.east_select_conf.queryParams={};
			this.east_select_conf.queryParams[this.east_conf.relationField] = row[this.east_conf.vauleField];
		}else{
			this.east_select_conf.queryParams={};
		}
		this.east = this.east.datagrid(this.east_select_conf.getSelectConf());
	}

	this.initNorth = function(){
		var gridconf = this.north;
		gridconf.gridOnClickRow = function(index,row){
			//两次之间点击相同的id不刷新点击事件
			if(me.form && me.form.find('[name="'+gridconf.idField+'"]').val() == row[gridconf.idField]){
				return;
			}
			me.edit(row);
			if(typeof me.afterEdit == "function"){
				me.afterEdit(row);
			}
		}

		return this.north;
	}

	this.init = function(){
		this.form = null;
	};

	this.getWin = function(divTag){
		var div = divTag;
		// var div = divTag.find('div:first-child');
		this.init();
		div.layout({height:CTT.height});

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		if(this.west_conf){//左边
			var width = this.west_conf.widthRatio ? this.west_conf.widthRatio:0.5;
			div.layout('add',{
				//title:this.west_conf.tile || '请选择一行数据编辑',
				//fit:true,
				collapsible:false,
				resizable:true,
				//border:false,
				width:FXC.easyui.getWidth(width),
				height:this.betweenHeight,
				minHeight:this.minHeight,
				split:true,
			    region: 'west',
			    content:'<div id="west_'+this.id+'">请选择一行数据编辑</div>',
			});
		};

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		if(this.east_conf){//右边
			var width=this.east_conf.widthRatio?this.east_conf.widthRatio:0.5;
			div.layout('add',{
				//title:' ',
				collapsible:false,
				width:FXC.easyui.getWidth(width),
				hright:this.betweenHeight,
				minHeight:this.minHeight,
				split:true,
				resizable:true,
			    region: 'east',
			    content:'<div id="east_'+this.id+'"></div>',
			});
			//初始化east
			this.east = $('#east_'+this.id);
			//加载js 初始化配置 应该在点击时触发初始化下部优化
			FXC.loadJS.load(this.east_conf.loadJs,function(){
				var url = me.east_conf.loadJs;
				url = url.substring(0,url.length-3);
				var selectGridConfig = eval('FXC'+url.replace(/\//g,"\.")+'_select');
				me.east_select_conf = selectGridConfig;
				me.east_dialog_conf = selectGridConfig;
				if(me.east_conf.conf){//如果需要重新定义配置new一个新对象
					me.east_select_conf = FXC.easyui.newSelectGrid(selectGridConfig);
					$.extend(me.east_select_conf,me.east_conf.conf);//conf的自定义属性不要冲突
					//if(me.east_conf.conf.height) me.east_select_conf.height = me.east_conf.conf.height;
					me.east_dialog_conf = FXC.easyui.newSelectGrid(selectGridConfig);//初始化dialog需要的grid

					me.east_dialog_conf.url = me.east_conf.dialogUrl;
					me.east_dialog_conf.singleSelect = false;//默认多选
					me.east_dialog_conf.queryParams = me.east_conf.queryParams;
				}
				if(!me.east_conf.conf.singleSelect)	me.east_conf.conf.singleSelect = true;//默认单选
				//east添加删除功能
				me.east_select_conf.columns[0].push({
					field : 'operate',
					title : '',
					width : 100,
					align : 'center',
					formatter : function(value, row, index) {	
						var html='';
						if(!me.north.verifyAuthority('p_update')){
							//html+='<a class="operate_btn" href="javascript:void(0)");">删除</a>';
							
							html+='<a class="operate_btn" href="javascript:eval('+me.myName+'.eastDel());");">删除</a>';
						}			
						return html;
					}
				});
				me.east_select_conf.onBeforeLoad = function(data){
					if(data[me.east_conf.relationField]){//带有relationField参数表示不是新增
						return true;
					}else{//新增时参数为空不需要请求加载
						return false;
					}
				};
				me.east_select_conf.toolbar = [{
					text : me.east_conf.buttonName || '请选择数据',
					iconCls : 'i_column_add',
					handler : function(){
						FXC.communal.dialog.getDialog( '请选择',me.east_dialog_conf,function(rows){
							if(rows){
								var rowsextra = me.east.datagrid("getRows");
								FXC.easyui.duplicateRemoval(rows,rowsextra,"id");
								for(var i in rows){
									me.east.datagrid("appendRow",rows[i]);
								}
							}
						});
					}
				}];
			});
		}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		if(this.north_conf){//上面

			if(!this.north_conf.operate){
				this.north_conf.operate = {
					field : 'operate',
					title : '操作',
					width : 100,
					align : 'center',
					formatter : function(value, row, index) {
						var html = '';
						if(!me.north.verifyAuthority('p_delete')) {
							html += '<a class="operate_btn" href="javascript:void(0);" data-id="\''+row[me.idField]+'\'" name="sc">删除</a>';
						}
						return html;
					}
				};
			}
			this.north = new getPage(this.north_conf);
//			var northConf = this.north_conf;
			var north = this.initNorth();
			div.layout('add',{
				//fit:true,
			    region: 'north',
			    border:false,
			    split:true,
			    height:this.north.height || 400,
			    minHeight:this.minHeight,
//			    height:220,
			    content:'<div id="north_'+this.id+'"></div>',
			});
			var formdiv = $("#west_"+this.id);
			formdiv.load(host+this.west_conf.formUrl,function(responseTxt,statusTxt,xhr){
			    if(statusTxt=="success"){
			    	//first-child失效原因是之前加载的页面有一个<meta charset="UTF-8">标签被加载进去 first-child只会查找第一个标签因为多了一个标签所以找不到
			    	//me.form = formdiv.find('form:first-child');
			    	//FXC.easyui.uploadImage(me.form);
			    	me.initFormLayoutDom(formdiv);
			    	me.form.hide();
			    	north.setToolbar=['getAdd','getDel'];
					north.getWin($('#north_'+me.id));
			    }else if(statusTxt=="error"){
			    	alert("Error: "+xhr.status+": "+xhr.statusText);
			    }
		    });
			
		}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////		//底部保存
		
		//底部按钮
		div.layout('add',{
		    region: 'south',
		    split:true,
		    minHeight:30,
		    maxHeight:30,
		    border:false,
		    content:'<div style="margin: 0 auto;text-align:center;"><button class="south_btn" id="south_save_'+this.id+'">保存</button>'
		    // <a class="south_btn" id="south_del_'+this.id+'"><span>刷新</span></a></div>'
		});

		$("#south_save_"+this.id).click(function(){
			var row = me.north.dataGrid.datagrid('getSelected');
			var rows = me.east.datagrid("getRows");
			var ids = new Array();
			var north = me.north;
			if (rows) {
				for (var i in rows) {
					if(rows[i])
						ids.push(rows[i]['id']);
				}
			}
			var id = $('input[name="'+north.idField+'"]',me.form).val();
			
			var options = {
				url : id ? north.updateUrl : north.saveUrl,//注意路径的key要匹配
				beforeSubmit : function(arr, form, options) {
					return north.validate(arr, form, options);
				}, // 表单元素有效性验证
				data : {
					ids : ids,
				},
				success : function(result) {
					north.feedback(result);
					if(!id){//如果是新增就清空选中项
						north.dataGrid.datagrid("clearChecked");
					}
					me.clearEdit();
				}
			};
			 me.form.ajaxSubmit(options);
		});
		//this.initbutton();
		divTag.css({display:"block"});
	};
 };

 inheritPrototype(getLayoutGrid,Easyui_InitEvent);

 getLayoutGrid.prototype.initForm = function(row){
 	this.form.show();
	this.north.resetForm(this.form);
	if(row) this.form.form('load', row);
}