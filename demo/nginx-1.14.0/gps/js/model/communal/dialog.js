/**
 * 用于公共查询面板
 */
FXC.ns("FXC.communal"); 
FXC.communal.dialog = function(){
	
	
	
	var verification_id = function(id){
		if($("#"+id).size()>0){
			return true;
		}else{
			return false;
		}
	};
	
	//创建dialogs
	var createDialogs = function(id){
		var dialogs = document.createElement("div");
		dialogs.id = id;
		dialogs.innerHTML = '<div id="'+id+'_grid"></div>'
		return dialogs;
	};

	
	//初始化dialogDom
	 

	
	//页面中不存在创建添加
	var initElement = function(id){
		var dialogDom = createDialogs(id);
		document.getElementsByTagName("body")[0].appendChild(dialogDom);
	};

	var getId = function(id){
		return "diaLog_grid_"+id;
	}
	
	var getDialog = function(dialogDom,grid,title,fn,width,height){
		return dialogDom.dialog({
			title : title,
			width : width || 680,
			height : height || 420,
			modal : true,
			resizable : true,
			// onClose:function(){
			// 	$(this).dialog('destroy');
			// },
			buttons : [{
				text : '确定',
				handler : function() {
					//单选多选得调一下 还有页面大小也要可以重置
					var rows = grid.datagrid('getChecked');
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
			var id = getId(confg.id),dialog,select_grid;

			if(!verification_id(id)){
				initElement(id);
				dialog =  $("#"+id);
				select_grid = dialog.find("div:first-child");
				confg.setSelectDataGrid(select_grid.datagrid(confg.getSelectConf()));
				confg.initSearch();
				getDialog(dialog,select_grid,title,fn,width,height);
			}else{
				dialog =  $("#"+id);
				confg.reload();
				dialog.dialog("open");
			}
		}
	};
}();

	