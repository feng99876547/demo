
FXC.ns("FXC.easyui");
/**
 * 设置form input attr 属性
 * 
 * name name
 * type 赋值的type
 * value value
 * form 查找的form对象
 * domType form 对象中的属性 如 input select等 默认input
 */
FXC.easyui.setFormAttr =function(name,type,value,form,domType){
	if(domType){
		form.find(domType+'[name="'+name+'"]').attr(type,value);
	}
	else{
		form.find('input[name="'+name+'"]').attr(type,value);
	}
};

FXC.easyui.event={};
FXC.easyui.event.commonsClick = function(e,fns){
	e = e || window.event;
	var target = e.target || e.srcElement;
	if(target.tagName.toLowerCase() === "a"){
		if(!fns){//默认的
			if(target.name == "bj"){
   				this.edit();
   			}
   			if(target.name == "sc"){
   				this.del();
   			}
		}else{
			for(var key in fns){
				if(target.name == key){
					fns[key](target,this);
   	   			}
			}
		}
	}
};

/**
 * 设置form input val 属性
 * 
 * name name
 * value value
 * form 查找的form对象
 * domType form 对象中的属性 如 input select等 默认input
 */
FXC.easyui.setFormVal = function(name,value,form,domType){
	if(domType){
		switch(domType){
			case 'radio':
			  form.find('[name="'+name+'"][value="'+value+'"]').prop("checked", "checked");
			  break;
			default:
			  form.find('domType[name="'+name+'"]').val(value);
		}
	}else{
		form.find('*[name="'+name+'"]').val(value);
	}
}

/**
* 将data-options 字符串转json
*/
FXC.easyui.optionsToJson = function(str){
	var arr = str.replace(/\'/g,"").split(",");
	var obj = {};
	for (var i in arr) {
		var val = arr[i].split(":");
		obj[val[0]] = val[1];
	}
	return obj;
}

FXC.easyui.initSelectCustom = function (dom,datas,namekey,valuekey,showOne){
	if(!showOne){//默认会有一行空白的
		dom.append('<option value="">全 部</option>');
	}
	if(datas!=null){
		$.each(datas, function(index, node) {
			dom.append(
					'<option value="' + node[valuekey] + '">' + node[namekey]
							+ '</option>');
		});
	}
};
/**
 * 二维数组格式
 * data [['val','text'],['val','text']]
 */
FXC.easyui.initSelectArray  = function (dom,datas){
	dom.append('<option value="">请选择</option>');
	if(datas!=null){
		$.each(datas, function(index, node) {
			dom.append('<option value="' + node[0] + '">' + node[1]+'</option>');	
		});
	}
};

/**
 * 一维数组
 * data [val,val]
 */
FXC.easyui.initSelectSingleArray  = function (dom,datas){
	dom.append('<option value="">请选择</option>');
	if(datas!=null){
		$.each(datas, function(index, node) {
			dom.append('<option value="' + node + '">' + node+'</option>');	
		});
	}
};

/**
*获取div的宽度
* 需要获取的dom
* ratio 比例 小数点 如 0.2
*/
FXC.easyui.getWidth = function(ratio){
	var dom = $("#main_right");
	return dom.outerWidth()*ratio;
};


/**
*设置easyui的pagesize
*/
FXC.easyui.setPageSize = function(conf,pageSize){
	conf.pageSize = pageSize;
	conf.pageList = [pageSize];
}

/**
*创建Dialog = function(id){} 因为不创建重复id 所以使用完弹窗关闭时记得销毁div
*/
FXC.easyui.creatreDialogDiv = function(id){
	var id = 'creatreDialog_'+id;
	var div = document.getElementById(id);
	if(!div){
		div = document.createElement("div");
		div.id = id;
		div.innerHTML='<div></div>';
		document.body.appendChild(div);
		return $(div);
	}
	return $(div);
}


/**
 *数组对象去重 
 *排除参数1中 有和参数2重复的
 * 
 */
FXC.easyui.duplicateRemoval = function(rowsextra,rows,key){
	if(rows){
		for(var r in rows){
			for(ro in rowsextra){
				if(rows[r][key] == rowsextra[ro][key] ){
					rowsextra.splice(ro,1);
					break;
				}
			}
			
		}
	}
};

/**
 *设置多选  一般默认单选
 *boolean true 单选 false多选
 */
FXC.easyui.setSingleSelect = function(rowsextra,boolean){
	if(rows){
		for(var r in rows){
			for(ro in rowsextra){
				if(rows[r][key] == rowsextra[ro][key] ){
					rowsextra.splice(ro,1);
					break;
				}
			}
			
		}
	}
};

/**
*将构造函数中的值初始化到对象中
*/
FXC.easyui.initConfg = function(target,object){
	if(object){
		target.conf = {};
	    $.extend(true,target.conf,object);
	    for(var obj in target.conf){
	        target[obj] = target.conf[obj];
	    }
	}
};

/**
*new 一个新的 selectgrid
*/
FXC.easyui.newSelectGrid = function(selectConf){
	return new selectGrid(selectConf.conf);
};


/**
*初始化upload控件
*/
FXC.easyui.uploadImage = function(form){
	var target = form.find('div[data-type="uploadImage"]');
	if(target.size()>0){
		FXC.loadJS.loadJs(host+'/js/jquery/plupload.full.min.js');
		for(var i=0;i<target.size();i++){
			var tag = target.eq(i);
			var input = JQupDiffusion(tag.get(0),"tagName","TD").find("input");
			FXC.util.position(tag,input,4.5);
			var id = CTT.upoload_mark+FXC.getId();
			tag.attr('id',id);

			var upload = new FXC.util.uploader();
			upload.uploadfiles = tag;
			upload.url=_webApp+tag.attr('data-url');
			upload.browse_button = id;
			upload.input = input;

			var div = document.createElement("DIV");
			document.getElementsByTagName("body")[0].appendChild(div);

			upload.container = div;
			upload.subfileName = $('*[name="'+tag.attr('data-imagesName')+'"]',form);
			upload.init();
		}
	}
}

FXC.Ueditor = function(){
	var state = false;//初始化状态

	var init = function(){
		window.UEDITOR_HOME_URL = host+'/ueditor/';
		FXC.loadJS.loadJs(host+'/ueditor/ueditor.config.js');
		FXC.loadJS.loadJs(host+'/ueditor/ueditor.all.js');
		FXC.loadJS.loadJs(host+'/ueditor//lang/zh-cn/zh-cn.js');

		UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
		UE.Editor.prototype.getActionUrl = function(action) {
		    if (action == 'config') {//需要修改配置去后台自己把json字符串打印赋值过来 
		    	var config = eval('({"videoMaxSize":102400000,"videoActionName":"uploadvideo","fileActionName":"uploadfile","fileManagerListPath":"/ueditor/jsp/upload/file/","imageCompressBorder":1600,"imageManagerAllowFiles":[".png",".jpg",".jpeg",".gif",".bmp"],"imageManagerListPath":"/ueditor/jsp/upload/image/","fileMaxSize":51200000,"fileManagerAllowFiles":[".png",".jpg",".jpeg",".gif",".bmp",".flv",".swf",".mkv",".avi",".rm",".rmvb",".mpeg",".mpg",".ogg",".ogv",".mov",".wmv",".mp4",".webm",".mp3",".wav",".mid",".rar",".zip",".tar",".gz",".7z",".bz2",".cab",".iso",".doc",".docx",".xls",".xlsx",".ppt",".pptx",".pdf",".txt",".md",".xml"],"fileManagerActionName":"listfile","snapscreenInsertAlign":"none","scrawlActionName":"uploadscrawl","videoFieldName":"upfile","imageCompressEnable":true,"videoUrlPrefix":"","fileManagerUrlPrefix":"","catcherAllowFiles":[".png",".jpg",".jpeg",".gif",".bmp"],"imageManagerActionName":"listimage","snapscreenPathFormat":"","scrawlPathFormat":"","scrawlMaxSize":2048000,"imageInsertAlign":"none","catcherPathFormat":"/ueditor/jsp/upload/image/{yyyy}{mm}{dd}/{time}{rand:6}","catcherMaxSize":2048000,"snapscreenUrlPrefix":"","imagePathFormat":"","imageManagerUrlPrefix":"'+imagePath+'","scrawlUrlPrefix":"","scrawlFieldName":"upfile","imageMaxSize":2048000,"imageAllowFiles":[".png",".jpg",".jpeg",".gif",".bmp"],"snapscreenActionName":"uploadimage","catcherActionName":"catchimage","fileFieldName":"upfile","fileUrlPrefix":"","imageManagerInsertAlign":"none","catcherLocalDomain":["127.0.0.1","localhost","img.baidu.com"],"filePathFormat":"image","videoPathFormat":"/ueditor/jsp/upload/video/{yyyy}{mm}{dd}/{time}{rand:6}","fileManagerListSize":20,"imageActionName":"uploadimage","imageFieldName":"upfile","imageUrlPrefix":"'+imagePath+'","scrawlInsertAlign":"none","fileAllowFiles":[".png",".jpg",".jpeg",".gif",".bmp",".flv",".swf",".mkv",".avi",".rm",".rmvb",".mpeg",".mpg",".ogg",".ogv",".mov",".wmv",".mp4",".webm",".mp3",".wav",".mid",".rar",".zip",".tar",".gz",".7z",".bz2",".cab",".iso",".doc",".docx",".xls",".xlsx",".ppt",".pptx",".pdf",".txt",".md",".xml"],"catcherUrlPrefix":"","imageManagerListSize":20,"catcherFieldName":"source","videoAllowFiles":[".flv",".swf",".mkv",".avi",".rm",".rmvb",".mpeg",".mpg",".ogg",".ogv",".mov",".wmv",".mp4",".webm",".mp3",".wav",".mid"]})');
		        if(!this.options) this.options={};
		        UE.utils.extend(this.options, config);
		        this.fireEvent('serverConfigLoaded');
	            this._serverConfigLoaded = true;
		    }else if(action == 'uploadimage'){//上传图片 
		    	// return _webApp+"/tsgl/themeEdit/subImage.do";
		    	return _webApp+"/xxgl/announcement/subImage.do";
		    }else if(action == 'listimage'){//上传图片
		    	// return _webApp+"/tsgl/themeEdit/getImage.do";
		    	return _webApp+"/xxgl/announcement/getImage.do";
		    }else{
		    	return this._bkGetActionUrl.call(this, action);
		    }
		}
		state = true;
	}
	

	return{
		getUeditor:function(id){
			if (!state) {init();};
			var ue = UE.getEditor(id,{
		    	//toolbars:UEtoolbars,
		    	sfyxsdxtp:true,//是否开启选图片
		    	autoHeightEnabled: false,// 是否自动长高,默认true
		        autoFloatEnabled: true,//是否保持toolbar的位置不动,默认true
		        enableAutoSave :false,//关闭自动保存
		        allHtmlEnabled :false,//提交到后台的数据是否包含整个html字符串 
		    	elementPathEnabled:true, //是否启用元素路径，默认是显示
				wordCount:true, //是否开启字数统计
				maximumWords:2000,//允许的最大字符数
				elementPathEnabled:true, //是否启用元素路径，默认是显示
				initialFrameHeight:380,//高
				initialFrameWidth:750,//宽
				fullscreen :false,//关闭全屏
				zIndex : 9999,
   	 		});
   	 		ue.__allListeners['oneUpload'] = function(w){
				console.log('不麻烦的话就自己写个上传,要不然就用多图上传');
			}
   	 		return ue;
		}
	}
}();

FXC.easyui.initUeditor = function(conf){
	var target = conf.form.find('textarea[data-type="ueditor"]');
	if(target.size()>0){
		for(var i=0;i<target.size();i++){
			var tag = target.eq(i);
			if(!tag.attr('id')){//id是初始化的标志不要在tag对象中设置id
				var id = 'ueditor_'+FXC.getId();
				tag.attr('id',id);
				var name = tag.attr('name');
				if(!conf.form_ueditor){
					conf.form_ueditor={};
				}	
				var ue = FXC.Ueditor.getUeditor(id);
				conf.form_ueditor[name]=ue;
			}
		}
	}
}

FXC.easyui.initPanelCombobox = function(conf,form){
	var targets = conf.form.find('input[class="easyui-combobox"]');
	if(targets.size()>0){
		for(var i=0;i<targets.size();i++){
			var target = $(targets[i]),name = target.attr('name');
			target.combobox({
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
					var item = conf.formPanelComboboxs[name],combox=$(this);
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
		}
	}
}

FXC.easyui.initDateTimeBbox=function(form){
	var target = form.find('input[data-type="datetimebox"]');
	if(target.size()>0){
		for(var i=0;i<target.size();i++){
			var tag = target.eq(i);
			tag.datetimebox({
			    required: false,
			    showSeconds: true,
			    formatter:function(date){
			    	var year = date.getFullYear();
		            var month = date.getMonth() + 1;
		            var day = date.getDate();
		            var hour = date.getHours();
		            var minute = date.getMinutes();
		            var seconds = date.getSeconds();
		            month = month < 10 ? '0' + month : month;
		            day = day < 10 ? '0' + day : day;
		            hour = hour < 10 ? '0' + hour : hour;
		            minute = minute < 10 ? '0' + minute : minute;
		            seconds = seconds < 10 ? '0' + seconds : seconds;
		            return year + "-" + month + "-" + day + " " + hour+":"+minute+":"+seconds;
			    }
			});
		}
	}
}