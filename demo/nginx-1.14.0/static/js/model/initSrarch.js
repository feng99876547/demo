FXC.ns("FXC");

FXC.initSrarch = function(){
	var zwf = ":";
	
	this.defaultSpanClass = ' search-span ';
	
	//默认的input样式
	this.defaultInputClass = ' ';
	
	//默认的select样式
	this.defaultSelectClass = ' ';
	
	//默认的radio样式
	this.defaultRadioClass = ' ';
	/**
	 * 验证id是否存在
	 */
	var verification_id = function(id){
		if($(id).size()>0){
			alert("发现bug,创建search控件使用的"+id+"已经在页面中存在");
		}
	};
	
	/**
	 * 继承
	 * 将b的属性复制给a 相同不覆盖
	 */
	var extend = function(a,b){
		if(a==null)
			a = {};
		if(b && typeof b == "object"){
			for(var key in b){
				if(a[key] == undefined)//注释掉相同会覆盖
					a[key] = b[key];
			}
		}
	};
	
	/**
	 *  生成search html 
	 */
	var getSearch = function(conf){
		
		var me = this;
		
		var createInput = function(item){
			var html = '<span class = "'+me.defaultSpanClass+'" >'+item.field+zwf;
			html+='<input type = "text" name = "'+item.name+'"'
			+' class = "'+defaultInputClass+'" '
			+(item.value ? ' value = "'+item.value+'" ':'')
			+'>';
			html+='</span>';
			return html;
		};
		
		var createRadio = function(item,conf){
			var html = '<span class = "'+me.defaultSpanClass+'" >'+item.field+zwf;
			for(var i in item.value){
				var name = item.value[i].name || item.name;
				html+='<input type = "radio" name = "'+name+'"'
				+' class = "'+defaultRadioClass+'" '
				+' value = "'+item.value[i].value+'" '
				+(item.value[i].checked ?'checked = "checked" ':'')
				+'>'+item.value[i].text;
			}
			html+='</span>';
			return html;
		};
		
		
		if(conf.createSearch && conf.createSearch.length>0){
			var html = '<form id = "'+conf.search_form_id+'">';
			for(var i=0;i<conf.createSearch.length;i++){
				var item = conf.createSearch[i];
				if(item.type == "text"){
					html += createInput(item);
				}else if(item.type == "radio"){
					html +=  createRadio(item,conf);
				}
			}
			html += '</form>';
		}
		return html;
	};
	

	
	return {
		
		/**
		 * 获取html
		 */
		appendSearch : function(conf){
			if(!verification_id(conf.search_form_id)){
				//获取search html
				return getSearch(conf);
			}
		},
	
		/**
		 * 把所有配置传进来重写查询方法这边可以使用重写的 
		 *  插入search组件
		 * 
		 *  dom 将search组件插入到dom元素之前
		 *  grid 需要更新的表格
		 *  items 需要创建的item
		 *  search_id search组件使用的id
		 *  queryParams 需要附加的查询参数
		 */
//		appendPanelSearch : function(dom,grid,items,search_id,queryParams){
		appendPanelSearch : function(dom,grid,conf){
			if(!verification_id(conf.search_form_id)){
				//初始化search
				var obj = getSearch(conf,conf.search_form_id);
				if(obj['sear']){
					select_searchForm = $(obj['form']);
					var parent = dom.parentNode;
					parent.insertBefore(obj['sear'],dom);
//					initPanelSearch(obj['form'],grid,conf.search_form_id,conf.queryParams);
					initPanelSearch(select_searchForm,$(grid),conf);
				}
			}else{
				$(grid).datagrid("clearChecked");
			}
		}
	}
}();

