
 var getLayoutGrid = function(conf){
//	 this.apply(conf);
//	for(var obj in conf){//调用config构造函数传入的config参数
//		this[obj] = conf[obj];
//	}
	//继承config
//	this.north;//上 
//	this.west;//左
//	this.east;//右

	this.id = FXC.getId();

	this.betweenHeight = 500;//中部高度

	this.minHeight = 250;//最小高度

	this.widthRatio;//宽度比例

	this.initForm = function(row){
		var me = this;
		if(!this.form){
			var div = $("#west_"+this.id);
			div.load(host+this.west_conf.formUrl,function(responseTxt,statusTxt,xhr){
			    if(statusTxt=="success"){
			    	//me.form = div.find('form').eq(0);
			    	//first-child失效原因是之前加载的页面有一个<meta charset="UTF-8">标签被加载进去 first-child只会查找第一个标签因为多了一个标签所以找不到
			    	me.form = div.find('form:first-child');
			    	me.form.form('load', row);
			    }else if(statusTxt=="error"){
			    	alert("Error: "+xhr.status+": "+xhr.statusText);
			    }
		    });
		}else{
			me.form.form('clear');
			me.form.form('load', row);
		}
	}

	this.initNorth = function(){
		var me = this;
		var grid = this.north;
		//grid.useEdit = false;
		grid.gridOnClickRow = function(index,row){
			me.initForm(row);
			me.east_select_conf.queryParams={};
			me.east_select_conf.queryParams[me.east_conf.relationField] = row[me.east_conf.vauleField];
			console.log(me.east_conf.relationField);
			console.log(row[me.east_conf.vauleField]);
			$('#east_'+me.id).datagrid(me.east_select_conf);
		}
		return this.north;
	}

	this.init = function(){
		this.form = null;
	};

	this.getWin = function(divTag){
		var me = this;
		var div = divTag.find('div:first-child');
		this.init();
		div.layout({height:CTT.height});
		if(this.west_conf){
			var width = this.west_conf.widthRatio ? this.west_conf.widthRatio:0.5;
			div.layout('add',{
				//title:this.west_conf.tile || '请选择一行数据编辑',
				//fit:true,
				collapsible:false,
				//border:false,
				width:FXC.easyui.getWidth(width),
				hright:this.betweenHeight,
				minHeight:this.minHeight,
				split:true,
			    region: 'west',
			    content:'<div id="west_'+this.id+'">请选择一行数据编辑</div>',
			});
			// FXC.EleResize.on($('#west_'+this.id)[0], function(){
			// 	var form = $('#west_'+this.id+' form');
			// 	form.form('resize',{
			// 		width:form.width(),
			// 		height:form.height(),
			// 	});
			// });
		}
		if(this.east_conf){
			var width=this.east_conf.widthRatio?this.east_conf.widthRatio:0.5;
			div.layout('add',{
				//title:' ',
				collapsible:false,
				width:FXC.easyui.getWidth(width),
				hright:this.betweenHeight,
				minHeight:this.minHeight,
				split:true,
			    region: 'east',
			    content:'<div id="east_'+this.id+'"></div>',
			});
			
			FXC.loadJS.load(this.east_conf.loadJs,function(){
				var url = me.east_conf.loadJs;
				url = url.substring(0,url.length-3);
				me.east_select_conf = eval('FXC'+url.replace(/\//g,"\.")+'.getSelectConf()');
				me.east_select_conf.toolbar = [{
					text : me.east_conf.buttonName || '请选择数据',
					iconCls : 'i_column_add',
					handler : function() {
						
					}
				}];
				if(me.east_conf.conf){//me.east_conf.conf需要更新的配置
					$.extend(me.east_select_conf,me.east_conf.conf);//conf的自定义属性不要冲突
				}
			});
		}
		if(this.north_conf){
			if(!this.north_conf.operate){
				this.north_conf.operate = {
					field : 'operate',
					title : '操作',
					width : 100,
					align : 'center',
					formatter : function(value, row, index) {
						var html = '';
						if(me.north.p_delete) {
							html += '<a class="operate_btn" href="javascript:void(0);" data-id="\''+row[me.idField]+'\'" name="sc">删除</a>';
						}
						return html;
					}
				};
			}
			this.north = new getPage(this.north_conf);
			var northConf = this.north_conf;
			var north = this.initNorth();
			div.layout('add',{
				//fit:true,
			    region: 'north',
			    border:false,
			    split:true,
			    height:northConf.height || 400,
			    minHeight:this.minHeight,
//			    height:220,
			    content:'<div id="north_'+this.id+'"></div>',
			});
			//初始化权限
			this.p_update = north.p_update;
			this.p_delete = north.p_delete;
			this.p_create = north.p_create;
			north.getWin($('#north_'+this.id));
		}
		//底部保存
		div.layout('add',{
		    region: 'south',
		    split:true,
		    minHeight:40,
		    border:false,
		    content:'<div id="south_'+this.id+'"><a name="save"><span>保存</span></a><a name="delete"><span>删除</span></a></div>'
		});
		//this.initbutton();
	};
 };