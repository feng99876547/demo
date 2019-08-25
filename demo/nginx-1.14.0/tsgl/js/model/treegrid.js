
/**
*用的少就先不优化了
*/
var getTreeGridPage = function(conf){
	
	var me = this;
	
	getPage.apply(this,arguments);
	//设置用于父作为查找子节点的条件字段 默认key是parentId
	this.setParam = function(param,row){
		param['search_EQ_parentId'] = row.id;
	};

	/**刷新*/
	this.refresh =  function(data) {
		me.dataGrid.treegrid('reload',data?data:this.queryParams);
	};

	/**  新增 */
	this.add = function() {
		this.resetForm(this.form);
		if(typeof this.beforeAdd == "function"){
			this.beforeAdd();
		}
		var buttons = this.getAddButtons(this.saveUrl,null,null);
		this.getOpenEdit("新增"+this.title,buttons);
	};
	
	this.addChild = function() {
		this.resetForm(this.form);
		var row = me.dataGrid.datagrid('getSelected');
		var buttons = this.getAddButtons(this.saveUrl,function(rerow){
				me.dataGrid.treegrid('reload',row.id);
				me.dialog.dialog("close");
				return true;
			},{parentId:row.id});
		this.getOpenEdit("新增"+this.title,buttons);
	};
	

	/** 修改 */
	this.edit = function() {
		var row = me.dataGrid.datagrid('getSelected');
		if(typeof this.beforeEdit == "function"){
			this.beforeEdit(row);
		}
		this.dialog.find("form").form('load', row);
		var buttons = this.getUpdateButtons(this.updateUrl,function(rerow){
			me.dataGrid.treegrid('update',{
				id: rerow.id,
				row: rerow
			});
			me.dialog.dialog("close");
			return true;
		},null);
		this.getOpenEdit("编辑"+this.title,buttons);
	};
	
	this.operate = {
		field : 'operate',
		title : '操作',
		width : 130,
		align : 'center',
		formatter : function(value, row, index) {
			var html = '';
			if(!me.verifyAuthority('p_add')) {
				html += '<a class="operate_btn" href="javascript:void(0);" name="xz">新增</a>';
			}
			if(!me.verifyAuthority('p_update')) { 
				html += '<a class="operate_btn" href="javascript:void(0);" name="bj">编辑</a>';
			}
			if(!me.verifyAuthority('p_delete')) {
				html += '<a class="operate_btn" href="javascript:void(0);" name="sc">删除</a>';
			}
			return html;
		}
	};
	
	this.setGrid = function(){
		me.initOperate();//初始化Operate
		me.gridConf = {
				fit : true,//设置fit属性后height就无效了
				url:me.listUrl,
			    idField:me.idField,
			    treeField:me.treeField,
			    nowrap : true,
				// striped:true,//条纹
				// rownumbers : true,
			    singleSelect : true,
				checkOnSelect : false,
				selectOnCheck : false,
				queryParams:me.queryParams,
				//height:me.height|| CTT.height,
			    columns:me.columns,
			    toolbar:me.getToolbar(),
			    onBeforeLoad:function(row,param){
			    	if(row){//存在row是点击父菜单事件
			    		me.setParam(param,row);
			    	}else{//默认查询 如果查询条件有该字段使用查询条件的值
			    		if(!param[me.rootParamKey]){
			    			param[me.rootParamKey]=me.rootParamValue;
			    		}
			    	}
			    },
			    loadFilter:function(data,parentId){
			    	if(data && data.rows){
			    		for(var i in data.rows){
			    			//设置该节点为父节点
			    			data.rows[i]['state'] = 'closed';
			    		}
			    		return data.rows;
			    	}
			    },
				onClickRow:function(index,row){
					if(typeof me.onClickRow == "function"){
						me.onClickRow(row,index,me);
					}
				}

		};
		
		me.dataGrid =  me.dom.treegrid(me.gridConf);
		
		var fns = {
			xz:function(target,me){
				me.addChild();
			},
			bj:function(target,me){
				me.edit();
			},
			sc:function(target,me){
				me.del();
			}
		};

		me.bindClick(me.dataGrid.treegrid("options"),fns);
		
		return me.dataGrid;
	};
	

	/**
	 * divTag jq div对象
	 */
	this.getWin = function(divTag){
		this.dom = divTag;
		if(me.formUrl){//加载form
			var div = this.dom.next();
			//使用属性动态创建form样式不好调 静态页面的请求流量很小 有需要在优化
			div.load(me.formUrl,function(responseTxt,statusTxt,xhr){
			    if(statusTxt=="success"){
			    	me.initFormDom(div);
			    	return me.setGrid();
			    }else if(statusTxt=="error"){
			    	alert("Error: "+xhr.status+": "+xhr.statusText);
			    }
		    });
		}
	};
}

inheritPrototype(getTreeGridPage,getPage);
