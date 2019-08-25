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
		
		var createRadio = function(item){
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
		
		var createPanelCombobox = function(item,i){
			var html = '<span class = "'+me.defaultSelectClass+'" >'+item.field+zwf;
			//html+='<input class="easyui-combobox panelCombobox" data-options="valueField:\'id\',textField:\'name\',data:\'[{id:1,name:管理员}]\',onBeforeLoad:function(param){return true};">';
			html+='<input  class="easyui-combobox" data-typefxc="panelCombobox" data-indexfxc="'+i+'"'+ 
			' name="'+item.name+'" '+
			'style="width:150px;">';
			html+='</span>';
			return html;
		};

		var createCombobox = function(item){
			var html = '<span class = "'+me.defaultSelectClass+'" >'+item.field+zwf;
			//html+='<input class="easyui-combobox panelCombobox" data-options="valueField:\'id\',textField:\'name\',data:\'[{id:1,name:管理员}]\',onBeforeLoad:function(param){return true};">';
			html+='<input  class="easyui-combobox" data-typefxc="combobox" data-indexfxc="'+i+'"'+ 
			' name="'+item.name+'" '+
			'style="width:150px;">'
			html+='</span>';
			return html;
		}
		
		if(conf.createSearch && conf.createSearch.length>0){
			var html = '<form id = "'+conf.getSearch_form_id()+'">';
			for(var i=0;i<conf.createSearch.length;i++){
				var item = conf.createSearch[i];
				if(item.type == "text"){
					html += createInput(item);
				}else if(item.type == "radio"){
					html +=  createRadio(item);
				}else if(item.type == "panelCombobox"){
					html +=  createPanelCombobox(item,i);
				}else if(item.type == "combobox"){
					html +=  createCombobox(item);
				}
			}
			html += '</form>';
		}
		return html;
	};
	
	var obj =function(){};
	
	return {
		
		/**
		 * 获取html
		 */
		appendSearch : function(conf){
			if(!verification_id(conf.getSearch_form_id())){
				//获取search html
				return getSearch(conf);
			}
		},
		/**
		* 初始化自定义initPanelCombobox组件
		*/
		initPanelCombobox:function function_name(inputs,items) {
			inputs.combobox({
				panelHeight:-2,
				height:20,
				//readonly:true,//设置只读onShowPanel事件不触发
				//url:_webApp + '/system/role/pubList'+suffix,
				bodyCls:"cssaa",//值是样式
				cls:"cssaa",
				// style : { 
			 //      'border' : '0px', 
			 //    }, 
				onBeforeLoad:function(){
					$(this).combobox('panel').css("border","0px");//取消panel的border
				},
				onShowPanel:function(){
					var item = items[$(this).attr("data-indexfxc")],combox=$(this);
					
					FXC.loadJS.load(item.loadJs,function(){
						var url = item.loadJs;
						url = url.substring(0,url.length-3);
						if(!combox.objGridConf){
							combox.objGridConf = FXC.easyui.newSelectGrid( eval('FXC'+url.replace(/\//g,"\.")+'_select'));
						}
						if(item.queryParams){combox.objGridConf.queryParams = item.queryParams;}
						if(item.url){combox.objGridConf.url = item.url;}
						FXC.communal.dialog.getDialog(item.title,combox.objGridConf,function(row){
							combox.combobox('setValue',row[0][item.valueField]);
							combox.combobox('setText',row[0][item.textField]);
						});
					});
				}
			});
		},
		initCombobox:function function_name(inputs,items) {
			for(var i=0;i<inputs.length;i++){
				var input = $(inputs[i]);
				var item = {};
				$.extend(true,item,items[input.attr("data-indexfxc")]);
				var _onBeforeLoad = item.onBeforeLoad;
				var _onShowPanel =  item.onShowPanel;
				if(!item.height) item.height = 20;
				if(!item.panelHeight) item.panelHeight = 'auto';
				if(item.data){
					var len = item.data.length;
					switch(FXC.util.isArryOrObj(item.data)){
						case 1://一维数组
							var da = new Array();
						  	for(var i=0;i<len;i++){
						  		//value text 默认值
						  		da.push({value:item.data[i],text:item.data[i]});
						  	}
						  	item.data = da;
						  	break;
						case 2://二维数组
							var da = new Array();
							for(var i=0;i<len;i++){
						  		//value text 默认值
						  		da.push({value:item.data[i][0],text:item.data[i][1]});
						  	}
						  	item.data = da;
					  		break;
					}
				}
				item.onBeforeLoad = function(param){
					if(typeof _onBeforeLoad=="function"){
						return _onBeforeLoad(param);
					}else{//默认懒加载 在点击时加载
						if(item.lazy) return false;
						else return true;
					}
				};
				item.onShowPanel = function(rec){
					if(item.lazy == true && item.url){
						var me = input.combobox("options"),oda=input.combobox("getData");
						if(oda == null || oda.length==0){
							FXC.AJAX(item.url,item.queryParams,function(data){
								if(data.rows.length==0){
									input.combobox("disable");
									alert("因为没有加载到数据,暂时先禁用组件,可更具业务具体需求在做处理");
								}
								input.combobox("loadData", data.rows);
                            	//input.combobox('select',data[0].text);//默认选中第一项
							});
						}
					}
					if(typeof _onShowPanel=="function"){
						_onShowPanel(rec);
					}
				};
				input.combobox(item);
			}
		}
	}
}();

