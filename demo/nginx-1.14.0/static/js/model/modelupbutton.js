
var getPageUpButton = function(conf){
	//继承getPage
	getPage.apply(this,arguments);
	
	var me = this;
	
	this.useEdit = false;//取消编辑调用
	
	this.height = 380;
	
	this.autoHeight = function(type) {
		if(!type)
			type = 1;
		switch (type) {
			case 1:
				return me.height;
				break;
			case 2:                                                                                                               //50是按钮的高
				return document.documentElement.clientHeight - $("#search").outerHeight(true) - me.height - $("#buttons").outerHeight(true)-40;
				break;
		}
	};
	
	/**
	 * 编辑弹出框
	 */
	this.openEdit = function(row) {
		me.resetForm($('form',me.editPanle));
		//先清空
		me.extraGird.thirdParty.datagrid('loadData', { total: 0, rows: [] });
		
		if(row){//修改
			$('form', me.editPanle).form('load', row);
			me.extraGird.thirdParty.datagrid('load', {
				id : row[me.idField]
			});
		}
		me.switchEditPanle(2);
		me.editPanle.panel({title : '新增'+ me.title ? me.title :"详细"});
	};
	
	//上下框的点击事件 
	this.onClickRow = function(row,index){
		//需要扩展内部在监听
		me.openEdit(row);
	};
	
	//必要时重写
	this.add = function() {
		this.openEdit();
	},
	
	
	/**
	 * 重写编辑
	 */
	this.editPanle = $('#edit_panle').panel({
//		height : me.autoHeight(2),
		tools : [{
			iconCls : 'icon-add',
			handler : function() {
				alert('new');
			}
		}, {
			iconCls : 'icon-save',
			handler : function() {
				alert('save');
			}
		}]
	});
	
	/**
	 * 设置panel
	 */
	this.setPanel = function(){
		//设置没有选中时候的title 
		me.editPanle.panel({title:me.title ? me.title+"详细信息":"详细信息",height : me.autoHeight(2)});
	};
	
	/**
	 * 自适应窗口大小
	 */
	this.adaptive = function() {
		$(window).resize(function() {
			me.dataGrid.datagrid('resize', {
				height : me.autoHeight(1),
			});
			me.editPanle.panel('resize', {
				height : me.autoHeight(2),
			});
		});
	};
	
	/**
	 * 初始化按钮绑定
	 */
	this.initBtns = function() {
		$("#save-btn").click(function() {
			me.ajaxToSave();
		});
		$("#refresh-btn").click(function() {
			var row = me.dataGrid.datagrid('getSelected');
			if(row){
				me.onClickRow(row,null,me);
			}
			
		});
	};
	
	
	/**
	 * 切换编辑面板
	 */
	this.switchEditPanle = function(type){
		switch (type) {
			case 1:
				$("#edit_panle .norecord").show();
				$("#edit_panle .record").hide();
				me.editPanle.panel({
					title : me.title+"详细信息"
				});
				break;
			case 2:
				$("#edit_panle .record").show();
				$("#edit_panle .norecord").hide();
				break;
		}
	};
	
	

//	this.ajaxToSave = function() {
//		var rows = me.extraGird.datagrid("getRows");
//		var ids = new Array();
//		if (rows) {
//			for (var i in rows) {
//				ids.push(rows[i].id);
//			}
//		}
//		var options = {
//			url : this.saveUrl,
//			beforeSubmit : me.setValidate ? me.validate : null, // 表单元素有效性验证
//			data : {
//				ids : ids
//			},
//			success : function(result) {
//				feedback(result);
//			}
//		};
//		$('form', me.editPanle).ajaxSubmit(options);
//	};
	/**
	 * ajax储存
	 */
	this.ajaxToSave = function() {
		var rows = me.extraGird.thirdParty.datagrid("getRows");
		var ids = new Array();
		if (rows) {
			for (var i in rows) {
				if(rows[i])//QQ浏览器的rows返回[null]长度为1值为空
					ids.push(rows[i].id);
			}
		}
		var id = $('input[name="id"]',me.editPanle).val();
		if(!me.updateUrl)
			alert("是否不需要设置updateUrl 如果名字不一样请改成updateUrl");
		if(!me.saveUrl)
			alert("是否不需要设置saveUrl 如果名字不一样请改成saveUrl");
		var options = {
			url : id ? me.updateUrl : me.saveUrl,//注意路径的key要匹配
			beforeSubmit : me.validate, // 表单元素有效性验证
			data : {
				ids : ids,
			},
			success : function(result) {
				me.feedback(result);
				if(!id){//如果是新增就清空选中项
					me.dataGrid.datagrid("clearChecked");
				}
				me.switchEditPanle(1);
			}
		};
		$('form', me.editPanle).ajaxSubmit(options);
	}
};


