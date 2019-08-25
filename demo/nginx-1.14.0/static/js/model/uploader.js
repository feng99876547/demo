FXC.ns("FXC");

FXC.uploader = function(){
	return {
		tag : null,//控件面板
		up:new plupload.Uploader({
			me:this,
			runtimes : 'html5,html4',
	        browse_button : 'pickfiles', //触发文件选择对话框的按钮，为那个元素id
	        url : 'images/upload.shtml', //服务器端的上传页面地址
//			        chunk_size:'100kb',
	        max_file_size: '2mb',//限制为2MB
	        prevent_duplicates : false,// 允许重复选择
	        container: document.getElementById('upload'), // ... or DOM Element itself
	        filters: [{
	        	title: "上传文件",
	        	extensions: "txt,jpg,png",
	        }],//图片限制
//	        multipart_params:{idaa:"idaa"},//附带参数
			init: {
				PostInit: function(uploader) {
					document.getElementById('filelist').innerHTML = '';

					document.getElementById('uploadfiles').onclick = function() {
						uploader.start();
						return false;
					};
					document.getElementById('clear').onclick = function() {
						uploader.getOption('parent').clearFiles();
						document.getElementById('filelist').innerHTML = '';
						return false;
					};
					
				},
			    //图片选择完毕触发
				FilesAdded: function(up, files) {
					if(files.length>1){
						for(var i = 0;i<files.length;i++){
							up.removeFile(files[i]);
						}
						alert("一次请选择一个");
						return;
					}
					plupload.each(files, function(file) {
						document.getElementById('filelist').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ') <b></b></div>';
//								document.getElementById('filelist').innerHTML = '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ') <b></b></div>';
					});
				},
				QueueChanged:function(uploader){
//							if (uploader.files.length > 1) {
//						        uploader.splice(1, 1);
//						    }
				},
				FileFiltered:function(uploader,file){//该事件会在每一个文件被添加到上传队列前触发监听函数参数：(uploader,file)
					/*
					 * file的status 对应下值
					 * 值为2，代表队列中的文件正在上传时plupload实例的state属性值
					 * QUEUED	值为1，代表某个文件已经被添加进队列等待上传时该文件对象的status属性值
					 * UPLOADING	值为2，代表某个文件正在上传时该文件对象的status属性值
					 * FAILED	值为4，代表某个文件上传失败后该文件对象的status属性值
					 * DONE	值为5，代表某个文件上传成功后该文件对象的status属性值
					 */
					
				},
//						ChunkUploaded:function(uploader,file,responseObject){
//						},
				
				//图片上传成功触发，ps:data是返回值（第三个参数是返回值）
				FileUploaded:function(uploader,files,data){
					var result = JSON.parse(data.response);
					if(result.success){
						uploader.getOption('parent').tag.dialog("close");
						uploader.getOption('successfn')(result.rows);
					}
					
				},
				//会在文件上传过程中不断触发，可以用此事件来显示上传进度监听函数参数：(uploader,file)
				UploadProgress: function(up, file) {
					if(document.getElementById(file.id)){
						document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
					}
						
				},
//						FilesRemoved:function(uploader,files){
//							console.log("移除");
//							console.log(files);
//						},
				Error: function(up, err) {
//							if(uploader && uploader.files){
//								for(var i = 0;i<uploader.files.length;i++){
//									uploader.removeFile(uploader.files[i]);
//								}
//							}
					$.messager.show({
				        title : '提示',
				        msg : err.message,
				        timeout : 2000,
				        style : {
				            right : '',
				            top : document.body.scrollTop + document.documentElement.scrollTop,
				            bottom : ''
				        }
				    });
//					document.getElementById('console').appendChild(document.createTextNode("\nError #" + err.code + ": " + err.message));
				}
			}
		 }),
		 ifInit:false,
		 clearFiles:function(){
			 var upload = this.up;
			 if(upload && upload.files){
					for(var i = 0;i<upload.files.length;i++){
						upload.removeFile(upload.files[i]);
					}
				}
				
		 },
		 init:function(tag,url,data,fn){
			 this.tag = tag;
			 if(data){//设置参数
				 this.up.setOption('multipart_params', data);
			 } 
			 if(this.ifInit == false){
				 this.up.init();
				 this.up.setOption('parent', this);
				 this.up.setOption('url', url);
				 this.up.setOption('successfn', fn);
				 this.ifInit = true;
				 return;
			 }
			 //清空Files
			 this.clearFiles();
			//清空显示内容
			document.getElementById('filelist').innerHTML = '';
			
		 }
	};
}

//下面到时候要用再优化下
//FXC.uploader = function(){
//	
//	this.tag = null;//控件面板
//	
//	this.ifInit = false;
//	
//	this.clearFiles = function(){
//		 var upload = this.up;
//		 if(upload && upload.files){
//				for(var i = 0;i<upload.files.length;i++){
//					upload.removeFile(upload.files[i]);
//				}
//			}
//			
//	 };
//	
//	 this.init = function(tag,url,data,fn){
//		 this.tag = tag;
//		 if(data){//设置参数
//			 this.up.setOption('multipart_params', data);
//		 } 
//		 if(this.ifInit == false){
//			 this.up.init();
//			 this.up.setOption('parent', this);
//			 this.up.setOption('url', url);
//			 this.up.setOption('successfn', fn);
//			 this.ifInit = true;
//			 return;
//		 }
//		 //清空Files
//		 this.clearFiles();
//		//清空显示内容
//		document.getElementById('filelist').innerHTML = '';
//		
//	 };
//	 
//	this.up = new  plupload.Uploader({
//		me:this,
//		runtimes : 'html5,html4',
//        browse_button : 'pickfiles', //触发文件选择对话框的按钮，为那个元素id
//        url : 'images/upload.shtml', //服务器端的上传页面地址
////		        chunk_size:'100kb',
//        max_file_size: '2mb',//限制为2MB
//        prevent_duplicates : false,// 允许重复选择
//        container: document.getElementById('upload'), // ... or DOM Element itself
//        filters: [{
//        	title: "上传文件",
//        	extensions: "txt,jpg,png",
//        }],//图片限制
////        multipart_params:{idaa:"idaa"},//附带参数
//		init: {
//			PostInit: function(uploader) {
//				document.getElementById('filelist').innerHTML = '';
//
//				document.getElementById('uploadfiles').onclick = function() {
//					uploader.start();
//					return false;
//				};
//				document.getElementById('clear').onclick = function() {
//					uploader.getOption('parent').clearFiles();
//					document.getElementById('filelist').innerHTML = '';
//					return false;
//				};
//				
//			},
//		    //图片选择完毕触发
//			FilesAdded: function(up, files) {
//				if(files.length>1){
//					for(var i = 0;i<files.length;i++){
//						up.removeFile(files[i]);
//					}
//					alert("一次请选择一个");
//					return;
//				}
//				plupload.each(files, function(file) {
//					document.getElementById('filelist').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ') <b></b></div>';
////							document.getElementById('filelist').innerHTML = '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ') <b></b></div>';
//				});
//			},
//			QueueChanged:function(uploader){
////						if (uploader.files.length > 1) {
////					        uploader.splice(1, 1);
////					    }
//			},
//			FileFiltered:function(uploader,file){//该事件会在每一个文件被添加到上传队列前触发监听函数参数：(uploader,file)
//				/*
//				 * file的status 对应下值
//				 * 值为2，代表队列中的文件正在上传时plupload实例的state属性值
//				 * QUEUED	值为1，代表某个文件已经被添加进队列等待上传时该文件对象的status属性值
//				 * UPLOADING	值为2，代表某个文件正在上传时该文件对象的status属性值
//				 * FAILED	值为4，代表某个文件上传失败后该文件对象的status属性值
//				 * DONE	值为5，代表某个文件上传成功后该文件对象的status属性值
//				 */
//				
//			},
////					ChunkUploaded:function(uploader,file,responseObject){
////					},
//			
//			//图片上传成功触发，ps:data是返回值（第三个参数是返回值）
//			FileUploaded:function(uploader,files,data){
//				console.log(data.response);
//				var result = JSON.parse(data.response);
//				if(result.success){
//					uploader.getOption('parent').tag.dialog("close");
//					uploader.getOption('successfn')(result.rows);
//				}
//				
//			},
//			//会在文件上传过程中不断触发，可以用此事件来显示上传进度监听函数参数：(uploader,file)
//			UploadProgress: function(up, file) {
//				if(document.getElementById(file.id)){
//					document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
//				}
//					
//			},
////					FilesRemoved:function(uploader,files){
////						console.log("移除");
////						console.log(files);
////					},
//			Error: function(up, err) {
////						if(uploader && uploader.files){
////							for(var i = 0;i<uploader.files.length;i++){
////								uploader.removeFile(uploader.files[i]);
////							}
////						}
//				$.messager.show({
//			        title : '提示',
//			        msg : err.message,
//			        timeout : 2000,
//			        style : {
//			            right : '',
//			            top : document.body.scrollTop + document.documentElement.scrollTop,
//			            bottom : ''
//			        }
//			    });
////				document.getElementById('console').appendChild(document.createTextNode("\nError #" + err.code + ": " + err.message));
//			}
//		}
//	 });
//	
//}

