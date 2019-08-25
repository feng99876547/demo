//垃圾文档
//每个节点可以包括下列属性：
//id：节点的 id，它对于加载远程数据很重要。
//text：要显示的节点文本。
//state：节点状态，'open' 或 'closed'，默认是 'open'。当设置为 'closed' 时，该节点有子节点，并且将从远程站点加载它们。
//checked：指示节点是否被选中。
//attributes：给一个节点添加的自定义属性。
//children：定义了一些子节点的节点数组。
FXC.ns("FXC.communal"); 
FXC.communal.shopStype = function(){
	
	var ids,checked,getAll;
	//创建dialogs
	var createDialogs = function(){
		var dialogs = document.createElement("div");
		dialogs.id = "dialogs";
		dialogs.className = "none";
		return dialogs;
	};
	
	var createshopStype = function(){
		var shopStype = document.createElement("div");
		shopStype.id = "shopStype_dialog";
		shopStype.innerHTML = '<div class="search_bar1" id="shopStype_search">'+
					'<form>'+
						'<input type="hidden" name="id"></input>'+
						'<div class="search-row">'+
							'<span class="search-item"> 名称：<input name="LIKE_name" type="text" />'+
							'</span> <input class="search_btn" type="button" value="搜索" />'+
						'</div>'+
					'</form>'+
				'</div>'+
				'<div>'+
					'<div id="shopStype_gird"></div>'+
				'</div>';
		return shopStype;
	};
	
	var dialogsDom = createDialogs();
	
	var shopStype = createshopStype();
	
//	var delDialogs = function(){
//		$('#dialogs').remove();
//	};
	
	
	var hasPermissions = function(rows){
		ids = new Array();
		for(var i in rows){
			ids.push(rows[i].id);
		}
	};
	
	var initElement = function(){
		if(!document.getElementById("dialogs"))
			document.getElementsByTagName("body")[0].appendChild(dialogsDom);
		if(!document.getElementById("shopStype_dialog"))
			document.getElementById("dialogs").appendChild(shopStype);
	};
	
	var TreegridBeforeExpand = function(row){
		if(!row.children){
			$.post(_webApp+"/login/shop/stypelist",{faShopType:row.id}, 
			function(da) {
				var data = eval('(' + da + ')');
				if(data && data.rows && data.rows.length>0){
					for(var i=0;i<data.rows.length;i++){
						data.rows[i]["state"]="closed";
					}
					$('#shopStype_gird').treegrid('append',{  
		                parent: row.id,  // 这里指定父级标识就可以了  
		                data: data.rows
					});  
				}
				
			});
		}
	};
		
	var getChildDate = function(da){
		var data = []; 
		var getChild = function(id,f){
			var leaf=null;
			for(var i=0; i<da.length;i++){
				if( !da[i].matching  && da[i].faShopType && da[i].faShopType.id == id){
					if(f==null) return true;
					var shopStype = da[i];
					da[i].matching = 1;
					leaf = getChild(da[i].id,null);//null 判断是否有有子集
					if(leaf  == true){//递归添加子集
						if(typeof(shopStype.children == "undefined"))
							shopStype.children=[];
						getChild(da[i].id,shopStype.children);
					}
					f.push(shopStype);
				}
			}
			return leaf;
		};
		for(var i=0; i<da.length;i++){
			if(!da[i].faShopType){
				da[i].matching = 1;//这个值存在就已经被匹配过了
				da[i]["state"] = "closed";//默认关闭
				da[i].children = [];
				data.push(da[i]);
				getChild(da[i].id, data[data.length-1].children );
			}
		}
		return data;
	};
	
	
	var initTreegrid = function(){
		var conf = {
			url : _webApp + "/login/shop/stypelist",
			idField : "id",
			treeField : 'name',
//				parent:"faShopType",
			columns : [[
				{
					field : 'id',
					title : 'ID',
					sortable : true,
					hidden : true
				},{
					field : 'name',
					title : '名称',
					width : 150,
					sortable : true
				},{
					field : 'remark',
					title : '备注',
					width : 220,
					sortable : true
				}
			]],
		};
		if(getAll){
			conf.url = _webApp + "/login/shop/stypeAll";
			conf["loadFilter"] = function(data){
				if(data && data.rows && data.rows.length>0){
					data.rows = getChildDate(data.rows);
				}
				return data;
			};
		}else{
			//动态加载  无法分辨出子节点 遍历父节点去匹配是否有子节点很浪费性能
			conf["loadFilter"] = function(data){
				if(data && data.rows && data.rows.length>0){
					for(var i=0;i<data.rows.length;i++){
						data.rows[i]["state"]="closed";//state=closed将设置为父节点
//						data.rows[i]["children"]=[{}];
					}
				}
				return data;
			};
			conf["onBeforeExpand"] = function(row){
				TreegridBeforeExpand(row);
			}
		}
		$('#shopStype_gird').treegrid(conf);
	};
	
	var treegridDialog = function(fn,checked){
		var $dialog = $('#shopStype_dialog');
		initTreegrid();
		$dialog.dialog({
			title : "商店种类",
			width : 680,
			height : 420,
			modal : true,
			resizable : true,
			buttons : [{
				text : '确定',
				handler : function() {
					var rows = $('#shopStype_gird').datagrid('getSelections');
					if(checked){//单选
						rows = rows[rows.length-1];
					}
					fn(rows);
					$dialog.dialog("close");
				}
			}, {
				text : '取消',
				handler : function() {
					$dialog.dialog("close");
//					delDialogs();
				}
			}]
		});
	}
	
	var initGrid = function(checked,rows){
		 hasPermissions(rows);
		 $('#shopStype_gird').datagrid({
				url : _webApp + "/login/shop/stypelist",
				sortName : "sequence",
				sortOrder : "asc",
				idField : "id",
//				queryParams:{
//					"ISNULL-faShopType":" ",
//				},
				// fit:true,
				height : 347,
				nowrap : true,
				singleSelect:checked||true,
				striped:true,
				pagination : true,
				rownumbers : true,
				pageSize : 200,
				pageList : [200],
				singleSelect:checked,
				autoRowHeight : false,
				columns : [[{
					checkbox : true
				}, {
					field : 'id',
					title : 'ID',
					sortable : true,
					hidden : true,
					formatter : function(value, row, index) {
						if($.inArray(row.id,ids)>=0){
							$('#shopStype_gird').datagrid('checkRow', index);
						}					
					}
				}, {
					field : 'name',
					title : '名称',
					width : 120,
					sortable : true
				}, {
					field : 'remark',
					title : '备注',
					width : 200,
					sortable : true
				}]],
				onLoadSuccess:function(){
					
				}
		});
	};
	
	var gridDialog = function(fn,checked,rows){
		var $dialog = $('#shopStype_dialog');
		initGrid(checked,rows);
		// 清楚所有选中项，防止出现删除后的项目依然存在于选中项中
		$('#shopStype_gird').datagrid("clearChecked");
		$dialog.dialog({
			title : "商店种类",
			width : 680,
			height : 420,
			modal : true,
			resizable : true,
			buttons : [{
				text : '确定',
				handler : function() {
					var rows = $('#shopStype_gird').datagrid('getChecked');
					fn(rows);
					$dialog.dialog("close");
				}
			}, {
				text : '取消',
				handler : function() {
					$dialog.dialog("close");
//					delDialogs();
				}
			}]
		});
	};
	return {
		/**
		 * fn 选中fn
		 * checked true 单选 false多选
		 * rows 已经选中的数据
	 	*/
		getPanel:function(fn,checked,rows){
			initElement();
			return gridDialog(fn,checked,rows);
		},
		//checked true 单选 false多选
		getTreegrid:function(fn,checked,all){
			getAll = all || true;//默认一次获取所有
			initElement();
			return treegridDialog(fn,checked);
		}
	};
}();

	