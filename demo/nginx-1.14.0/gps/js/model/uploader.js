FXC.ns("FXC.util");

//下面到时候要用再优化下
FXC.util.uploader = function(){
	
	var me = this;

	this.ifInit = false;

	this.uploadfiles;

	this.browse_button;

	this.subfileName;//用于生成图片名字的唯一字段

	this.input;

	this.imageDiv;//显示图片的img 如果多张就改数组

	this.image;//imageDiv中的img

	this.mouseoutState = false;

	this.showImg = function(){
		if(this.mouseoutState){
			if(!me.imageDiv && me.input.val()){
				var div = document.createElement("DIV");
				var img = document.createElement("IMG");
				div.appendChild(img);
				div.style.position="absolute";
				img.style.width="230px";
				img.style.height="230px";
				img.src = imagePath+me.input.val();

				me.uploadfiles.parent().append(div);
				me.imageDiv = div;
				me.image = img;
			}
			if(me.input.val()){
				me.image.src = imagePath+me.input.val()+"?time="+(new Date()).getTime();
				me.imageDiv.style.display="block";
			}
		}
	};
	
	//为input绑定展示功能
	this.bandMouseout = function(){
		this.input.mouseover(function(){
			me.mouseoutState = true;
			setTimeout(function(){
				me.showImg();
			}, 800);
		});
		this.input.mouseout(function(){
			me.mouseoutState = false;
			if(me.imageDiv) me.imageDiv.style.display="none";
		});
	}

	this.clearFiles = function(uploader, files){
		uploader.splice(1, files.length);//清除之前file
	};

	this.FilesAdded = function(uploader, files){
		if(files.length>1){
			for(var i = 0;i<files.length;i++){
				up.removeFile(files[i]);
			}
			alert("一次请选择一个");
			return;
		}
		// var value = me.subfileName.val();
		// if(value.trim().length>0){
			// uploader.setOption('multipart_params', {subfileName:value});
			uploader.setOption('multipart_params', {subfileName:(new Date().getTime())});
			uploader.start();
		// }else{
		// 	me.clearFiles(uploader,files);
		// 	var name = me.subfileName
		// 	alert("请先保存后在上传照片");
		// }
	};

	this.PostInit = function(uploader){

	},

	 //图片上传成功触发，ps:data是返回值（第三个参数是返回值）
	this.FileUploaded = function(uploader,files,data){
		console.log(data.response);
		me.input.val(data.response);
	};
	 //会在文件上传过程中不断触发，可以用此事件来显示上传进度监听函数参数：(uploader,file)
	 this.UploadProgress = function(up, file){
	 	if(document.getElementById(file.id)){
			document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";
		}	
	 };
	 this.Error = function(up, err){
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
	 };
	 this.init = function(){
	 	this.up = new  plupload.Uploader({
		    runtimes : 'html5,html4',
	        browse_button : me.browse_button, //触发文件选择对话框的按钮，为那个元素id
	        url : me.url, //服务器端的上传页面地址
			// chunk_size:'100kb',//上传文件时每片分割的大小
	        max_file_size: '2mb',//限制为2MB
	        prevent_duplicates : false,// 允许重复选择
	        container: me.container, // ... or DOM Element itself
	        filters: [{
	       		title: "上传文件",
	       		extensions: "txt,jpg,png",
	        }],//图片限制
	        // multipart_params:me.multipart_params,//附带参数
			init: {
				PostInit:me.PostInit,
			    //图片选择完毕触发
				FilesAdded:me.FilesAdded,
				// QueueChanged:function(uploader){
				// },
				// FileFiltered:function(uploader,file){//该事件会在每一个文件被添加到上传队列前触发监听函数参数：(uploader,file)
				// },
				// ChunkUploaded:function(uploader,file,responseObject){
				// },
				FileUploaded:me.FileUploaded,
				UploadProgress:me.UploadProgress,
				// FilesRemoved:function(uploader,files){
				// 	console.log("移除");
				// 	console.log(files);
				// },
				Error:me.Error
			}
		});
	 	this.up.init();
	 	this.bandMouseout();
	 }
}

