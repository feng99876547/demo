/**
 * 用于公共查询面板
 */
FXC.ns("FXC.communal"); 
FXC.communal.dialog = function(){
	
	var select_Grid;
	
	var verification_id = function(id){
		if($("#"+id).size()>0){
			return true;
		}else{
			return false;
		}
	};
	
	//创建dialogs
	var createDialogs = function(){
		var dialogs = document.createElement("div");
		dialogs.id = "select_dialog";
		dialogs.innerHTML = '<div id="select_search"></div><div id="select_grid" ></div>'
		return dialogs;
	};

	
	//初始化dialogDom
	var dialogDom = createDialogs();

	
	//页面中不存在创建添加
	var initElement = function(){
		if(!document.getElementById("dialogs")){
			document.getElementsByTagName("body")[0].appendChild(dialogDom);
		}
	};
	
	var getDialog = function(title,fn,width,height){
		return $(dialogDom).dialog({
			title : title,
			width : width || 680,
			height : height || 420,
			modal : true,
			resizable : true,
			buttons : [{
				text : '确定',
				handler : function() {
					//单选多选得调一下 还有页面大小也要可以重置
					var rows = $('#select_grid').datagrid('getChecked');
					if(rows.length == 0){
						alert("请选中一行数据");
						return;
					}
					fn(rows);
					$(dialogDom).dialog("close");
				}
			}, {
				text : '取消',
				handler : function() {
					$(dialogDom).dialog("close");
				}
			}]
		});
	};
	return {
		/**
		 * title 标题
		 * confg 更具命名空间区分的配置
		 * fn 选中后需要处理的函数
		 * checked 单选还是多选 false 单选 true 多选
		 * width 重新赋值宽
		 * height 重新赋值高
		 */
		getDialog:function(title,confg,fn,checked,width,height){
			initElement();
			getDialog(title,fn,width,height);
			var select_search = document.getElementById("select_search");
			var select_grid = document.getElementById("select_grid");
			if(!verification_id(confg.select_search_id)){
				$("#select_grid").datagrid(confg.getSelectConf());
				if(confg.createSearch){//如果存在查询条件 初始化
//					FXC.initSrarch.appendPanelSearch(select_search,select_grid,confg.createSearch,"select_search"+confg.id);
					FXC.initSrarch.appendPanelSearch(select_search,select_grid,confg);
				}
			}else{
				$("#"+confg.select_search_id+" form").form('clear');//清除条件选中项
				$("#select_grid").datagrid("load",{});//更新数据
				$("#select_grid").datagrid("clearSelections");
			}
		}
	};
}();

	