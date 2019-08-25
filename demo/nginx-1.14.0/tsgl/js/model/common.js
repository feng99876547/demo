
FXC.loadJS.loadJs(host+'/js/model/buttons.js');

var getPage = function(conf){
	//借用构造函数
	selectGrid.apply(this,arguments);

	var me = this;

	this.dataGrid = null;
	
	this.sortName = null;//排序字段
	
	this.sortOrder = null;//排序状态
	
	this.idField = 'id';
	
	this.useEdit = true; //是否使用编辑 删除 事件
	
	this.queryParams ;//默认附加参数
	
	this.editIndex = undefined;//编辑标记
	
	//this.p_* 操作权限

	this.checkbox = true;//默认有checkbox

	this.singleSelect = true;//默认单选

	this.dialogClose;//dialogd的关闭事件

	this.onAfterRender;//用于自定义 在grid渲染之后需要执行的事件

//	this.search = function(){} //搜索时触发的事件
	
//  this.form; //主面板的form	
	
//	this.searchForm ; //主面板查询form 
	
//	this.select_searchForm;//查询面板使用的form 
	
//his.search_form_id;//查询面板使用的searchid
	
	this.dialog = null;//form表达div 属于form的父容器his.vali
	
	this.form = null;//form 
	
	this.dom = null;//grid对应的div

	this.dialogButtons = null;//重新定义dialog按钮
	
	this.bindClickStates = false;//用于给Grid的table绑定事件 且只绑定一次 因为load的适合不是重新渲染 
	
	this.isAdmin = false;//代表是否是超级管理员
	/**
	 * 权限初始化
	 */
	if(conf.permissionsKey){
		if(permissionyz()){
			this.isAdmin = true;
		}else{
			var permissions =menuPermission[conf.permissionsKey];
			for(var i in permissions){//初始化权限
				if(permissions[i].permission.indexOf(conf.permissionsKey)==0){
					var index = permissions[i].permission.lastIndexOf(":"),
					name = permissions[i].permission.substring(index+1);
					me['p_'+name] = true;//配置中定义的权限在后面初始化会覆盖这边的
				}
			}
		}
	}

	this.operate = { 
		field : 'operate',
		title : '操作',
		width : 100,
		align : 'center',
		formatter : function(value, row, index) {
			var html = '';
			if(!me.verifyAuthority('p_update')) { 
				html += '<a class="operate_btn" href="javascript:void(0);"  name="bj">编辑</a>';
			}
			if(!me.verifyAuthority('p_delete')) {
				html += '<a class="operate_btn" href="javascript:void(0);"  name="sc">删除</a>';
			}
			return html;
		}
	};

 	$.extend(true,this,conf);//初始化属性
	
};

inheritPrototype(getPage,Easyui_Toolbar);

/** 获取验证权限  拥有权限返回false 有权限返回true*/
getPage.prototype.verifyAuthority = function(key){
	return !(typeof this[key] !== "undefined" ? this[key] : this.isAdmin);
};


/**刷新*/
getPage.prototype.refresh =  function(data) {
	this.dataGrid.datagrid("reload",data?data:this.queryParams);
	//清除所有选中项
	this.dataGrid.datagrid("clearChecked");
};

//是否需要使用Operate功能列
getPage.prototype.initOperate = function(){
	if(this.useEdit){
		var len = this.columns[0].length -1;
		if(this.bindClickStates == false && 
				this.columns && this.columns[0][len] && this.columns[0][len].field.trim() != 'operate'){
			this.columns[0].push(this.operate);
			this.bindClickStates = true;
		}
	}
};

//绑定到input的事件转到图片上或是按钮
getPage.prototype.bindImg = function(dom,fns){
	dom.get(0).parentNode.onclick=function(e){
		e = e || window.event;
		var target = e.target || e.srcElement;
   		if(target.tagName.toLowerCase() == "img"){
   			fns();
   			return;
   		};
	}
};


getPage.prototype.bindClick = function(bindOptions,fns){
	//不要忘记加url属性 没配置是不会触发这个方法的
	var me = this;
	bindOptions.view.onAfterRender = function(target){
		//因为onAfterRender方法在每次视图被呈现后包含刷新都会调用
		var datagridView = JQupDiffusion(me.dom.get(0),"tagName","DIV");
		var tab = datagridView.find("a[class='operate_btn']:first");
		if(tab.length>0){
			var dom = upDiffusion(tab.get(0),"tagName","TABLE");
			dom = upDiffusion(dom,"tagName","DIV");
			//在tab外层div添加事件 如果选择给tabel对象添加监听因为冒泡是由内而外就会先触发内层的编辑事件在触发选中事件 因为选中事件也是绑定在tabel外层
			FXC.util.removeAnonymousEvent(dom,'click');
			FXC.util.addAnonymousListener(dom,'click',function(e){
				FXC.easyui.event.commonsClick.apply(me,[e,fns]);
			});
		}
		if(typeof me.onAfterRender == "function"){
			me.onAfterRender(target);
		}
		// this.dom.css({display:"block"});
		// me.dom.css({visible:"visible"});
		me.dom.css({display:"block"});
	};
	//初始化查询控件事件
	me.initSearch();
};

getPage.prototype.endEditing = function(){
	var me = this;
	if (me.editIndex == undefined){return true}
	if (me.dataGrid.datagrid('validateRow', me.editIndex)){//验证行是否存在
		me.dataGrid.datagrid('endEdit', me.editIndex);
		me.editIndex = undefined;
		return true;
	} else {
		return false;
	}
}

getPage.prototype.gridOnClickRow = function(index,row){//找不到监听管理控制器啊
	var me = this;
	me.dataGrid.datagrid('acceptChanges');//点击先提交所有被更改的数据
	//是否可以被编辑
	if (me.editIndex != index){
		if (me.endEditing()){
			me.editIndex = index;
		} else {
			me.dataGrid.datagrid('selectRow', me.editIndex);
		}
	}
	me.dataGrid.datagrid('selectRow', index).datagrid('beginEdit', index);
	if(typeof me.onClickRow == "function"){
		me.onClickRow(row,index,me);
	}
}

getPage.prototype.setGrid = function(){
	var me = this;
	me.gridConf = {
			fit : true,
			url : me.listUrl,
//				sortName : "position",
//				sortOrder : "asc",
			idField : me.idField || "id",
//				treeField : 'name',
			//height:me.height || CTT.height,
			nowrap : true,
			striped:true,//条纹
			rownumbers : true,
			pagination : true,
			pageSize : 20 ,
			pageList:[20, 30, 40],
			autoRowHeight : true,
			//closed:false,
			singleSelect : me.singleSelect, //默认单选
			checkOnSelect : true,
			selectOnCheck : true,
			columns :me.initChecked(me.columns,me.checkbox),
			toolbar:me.getToolbar(false),
			queryParams:me.queryParams || null,
			loadFilter:function (data) {//一次渲染这个方法会被触发两次 一次的data是field字段 一次是值
				return me.verifyData(data);
		    },
		    onLoadError:function(data){
		    	return me.verifyData(data);
		    },
		    /*
		    function(){
		    	var me = this;
		    	//之前这样可以是因为gridOnClickRow方法在this对象中 me对象指向的是上面定义的me
		    	//改prototype后方法中的this对象指向的eayuui解析过程中的对象 所以那不到需要的属性
				this.gridOnClickRow = function(index,row){
					me.dataGrid.datagrid('acceptChanges');
				}
		    }
		    */
		    // onClickRow:me.gridOnClickRow 
			onClickRow:function(index,row){
				me.gridOnClickRow(index,row);
			},
	};
	if(me.pageSize){
		FXC.easyui.setPageSize(me.gridConf,me.pageSize);
	}
	if(me.sortName && me.sortOrder){//排序
		me.gridConf.sortName = me.sortName;
		me.gridConf.sortOrder = me.sortOrder;
	}
	this.dataGrid =  me.dom.datagrid(me.gridConf);
	this.bindClick(me.dom.datagrid("options"));
	return me.dataGrid;
};

getPage.prototype.initGrid = function(){
	var me = this;
	if(me.formUrl){//加载form
		var div = this.dom.next();
		//使用属性动态创建form样式不好调 静态页面的请求流量很小 有需要在优化
		div.load(host+me.formUrl,function(responseTxt,statusTxt,xhr){
		    if(statusTxt=="success"){
		    	me.initFormDom(div);
		    	if(typeof me.initFormEvent == "function"){
		    		me.initFormEvent(me.form);
		    	};
		    	return me.setGrid();
		    }else if(statusTxt=="error"){
		    	alert("Error: "+xhr.status+": "+xhr.statusText);
		    }
	    });
	}else{
		me.operate = null;
		return me.setGrid();
	}
};

getPage.prototype.getWin = function(divTag){
	var me = this;
	this.initOperate();//初始化Operate
	this.dom =divTag;
	return this.initGrid();
};



var getTabPage = function(conf){
	getPage.apply(this,[conf]);
}

inheritPrototype(getTabPage,getPage);

getTabPage.prototype.onOpen = function(){
	this.formTabs.tabs('select', 0);
};

getTabPage.prototype.initGrid = function(){
	if(this.formTabs) return this.setGrid();
	var me = this;
	// this.formTabs = this.dom.find("div");
	this.formTabs = this.dom.next();
	var formTabs = this.formTabs;
	formTabs.tabs({
		width : me.width || 500,
		height:me.height || 500,
		onSelect:function(title,index){
			// var tab = formTabs.tabs('getTab',index);
			// var ops = tab.panel('options');
			//如果没有查看权限这边 this.formTabs.tabs('select', 0); 或是找找有没有事件触发前的方法 返回false的那种
			var tab = me.tabForm[index];
			if(!tab.nature){
				if(!tab.obj){//初始化对象
					var div = $("#"+tab.id);
					switch(tab.type){
						case 'form':
							div.load(host+tab.formUrl,function(responseTxt,statusTxt,xhr){
							    if(statusTxt=="success"){
							    }else if(statusTxt=="error"){
							    	alert("Error: "+xhr.status+": "+xhr.statusText);
							    }
						    });
							break;
						case 'grid':
							if(!tab.selectGrid)
								tab.selectGrid = new selectGrid(tab.conf).getSelectConf();
							tab.selectGrid.onBeforeLoad = function(param){
								if(!param[tab.relationField])
									return false;
							};
							tab.obj = div.datagrid(tab.selectGrid);
							if(me.getFormVal(tab.vauleField)!=""){
								var data ={};
								data[tab.relationField]=me.getFormVal(tab.vauleField);
								tab.obj.datagrid('reload',data);
							}
							break;
					}
				}else{
					switch(tab.type){
						case 'grid':
							if(me.getFormVal(tab.vauleField)!=""){
								var data ={};
								data[tab.relationField]=me.getFormVal(tab.vauleField);
								tab.obj.datagrid('reload',data);
							}else{
								tab.obj.datagrid('loadData', {total: 0, rows: [] });
							}
							break;
					}
				}
			}
	    },
	    onAdd:function(title,index){
	    	var tab = me.tabForm[index];
	    	if(tab.nature){
	    		var div = $("#"+tab.id);
	    		div.load(host+tab.formUrl,function(responseTxt,statusTxt,xhr){
				    if(statusTxt=="success"){
						me.initFormDom(formTabs);
				    	if(typeof me.initFormEvent == "function"){
				    		me.initFormEvent(me.form);
				    	};
				    	me.dialog.dialog({closed:true});//内tabs 没有渲染关闭外层标签页后在打开 内tabs的标签页的a标签点击事件没了
				    	return me.setGrid();
				    }else if(statusTxt=="error"){
				    	alert("Error: "+xhr.status+": "+xhr.statusText);
				    }
			    });
	    	}
	    }
	});
	Object.keys(me.tabForm).forEach(function(i){
		var tab = me.tabForm[i];
		if(!tab.id){
			tab.id = 'tabForm'+FXC.getId();
			switch(tab.type){
				case 'form':
				    formTabs.tabs('add',{
					    title:tab.title,
					    content:'<div id = "'+tab.id+'" ></div>',
					    closable:false,
					});
				  	break;
				case 'grid':
				    formTabs.tabs('add',{
					    title:tab.title,
					    content:'<div id = "'+tab.id+'" ></div>',
					    closable:false,
					});
				  	break;
			}
		}
	});
}

var getTabProcessPage = function(conf){
	getPage.apply(this,[conf]);
}

inheritPrototype(getTabProcessPage,getPage);

getTabProcessPage.prototype.getIndex = function(status){
	for(var i in this.status){
		if(status == this.status[i]){
			return i;
		}
	}
	return -1;
}

getTabProcessPage.prototype.closeTabs = function(){
	var me = this;
	Object.keys(me.tabForm).forEach(function(i){
		me.formTabs.tabs('disableTab', i*1);
	});
}

getTabProcessPage.prototype.dialogClose = function(){
	this.refresh();
}

getTabProcessPage.prototype.initGrid = function(){
	if(this.formTabs) return this.setGrid();
	var me = this;
	this.formTabs = this.dom.next();
	var formTabs = this.formTabs;
	formTabs.tabs({
		width : me.width || 500,
		height:me.height || 500,
		onAdd:function(title,index){
	    	var tab = me.tabForm[index];
	    	var div = $("#"+tab.id);
	    	switch(tab.type){
				case 'form':
					if(!tab.nature){
						tab.initPanel = function(row){
							if(this.form){
								this.form.form('load', row);
								if(typeof this.initGrid == "function") this.initGrid(tab,me);
							}else{
								var _tab = this;
								div.load(host+this.formUrl,function(responseTxt,statusTxt,xhr){
								    if(statusTxt=="success"){
								    	_tab.form = div.find('form:first-child');
								    	me.initDom(_tab.form,_tab);
								    	if(typeof _tab.initGrid == "function") _tab.initGrid(tab,me);
								    }else if(statusTxt=="error"){
								    	alert("Error: "+xhr.status+": "+xhr.statusText);
								    }
							    });
							}
						}
					}
					break;
				case 'grid':
					if(!tab.conf){
						FXC.loadJS.load(tab.loadJs,function(){
							var conf = new getPage(eval(tab.config));
							conf.p_add = true;
							conf.p_read = true;
							conf.p_update = false;
							conf.p_delete = false;
							conf.useEdit = false;
							delete conf.createSearch;
							if(typeof tab.initGrid == "function")
								tab.initGrid(conf,tab,me);
							tab.initPanel = function(param){
								if(!this.conf){
									this.conf = conf;
									this.conf.queryParams = param;
									conf.getWin($("#"+this.id));
								}else{
									this.conf.dataGrid.datagrid("reload",param);
								}
							}
						});
					}
					break;
			}
	    }
	});
	var tab = me.tabForm[0];//先初始化主面板
	if(!tab.id) tab.id = 'tabForm'+FXC.getId();
	formTabs.tabs('add',{
	    title:tab.title,
	    content:'<div id = "'+tab.id+'" ></div>',
	    closable:false,
	});
 	$("#"+tab.id).load(host+tab.formUrl,function(responseTxt,statusTxt,xhr){
	    if(statusTxt=="success"){
    		me.initFormDom(formTabs);
    		tab.form = me.form;
			if(typeof me.initFormEvent == "function"){
	    		me.initFormEvent(me.form);
	    	};
	    	me.dialog.dialog({closed:true});
	    	Object.keys(me.tabForm).forEach(function(i){
	    		if(i==0) return true;
				var tab = me.tabForm[i];
				if(!tab.id)
					tab.id = 'tabForm'+FXC.getId();
				switch(tab.type){
					case 'form':
					    formTabs.tabs('add',{
						    title:tab.title,
						    content:'<div id = "'+tab.id+'" ></div>',
						    closable:false,
						});
					  	break;
					case 'grid':
					    formTabs.tabs('add',{
						    title:tab.title,
						    content:'<div id = "'+tab.id+'" ></div><div></div>',
						    closable:false,
						});
					  	break;
				}
			});
			
	    }else if(statusTxt=="error"){
	    	alert("Error: "+xhr.status+": "+xhr.statusText);
	    }
    });
	return me.setGrid();
};