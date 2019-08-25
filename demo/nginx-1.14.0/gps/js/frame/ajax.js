FXC.ns("FXC");
FXC.loadJS = function(){
	var js = new Array();
	var isload = function(name){
		for(var i in js){
			if(js[i]==name){
				return true;
			}
		}
		return false;
	};
	
	var loadjs = function(url,resultFN,faultFN,async){
		var xmlHttp =null;
		var strInnerHTML = null;
		try{
			if(window.ActiveXObject){
				xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
			}else if(window.XMLHttpRequest){
				xmlHttp = new XMLHttpRequest();
			}
		}catch(e){
			alert("晕");
		}
		//xmlHttp.open("get",url,true);
		xmlHttp.open("get",url,async?async:false);
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		xmlHttp.onreadystatechange = function (){
			if(xmlHttp.readyState == 4){
				if(xmlHttp.status == 200){
					try{
						//(new Function(xmlHttp.responseText)).call(window);
						window.eval(xmlHttp.responseText);
						js.push(url);
						if(resultFN){resultFN()};
					}catch(e){
						console.log("----------error----------");
						console.log(e);
						if(faultFN) faultFN(e);
					}
				}else{
					if(faultFN) faultFN(xmlHttp);
				}
			}
		};
		//xmlHttp.send(null);
		xmlHttp.send("_dc="+new Date().getTime());
	};
	
	/**
	 * 点击左侧菜单时默认的操作
	 */
	var defresultFN = function(url,div){
		eval('FXC'+url.replace(/\//g,"\.")+'.getWin(div)');
	};
	
	return {
		/**默认同步加载	*/
		load:function(url,resultFN,faultFN,async){
			url = host+"/js/page"+url;
			if(!isload(url)){
				loadjs(url,resultFN,faultFN,async?async:false);
			}else{
				if(typeof resultFN == "function")
					resultFN();
			}
		},
		loadTab:function(url,div,async){
			var suburl = host+"/js/page"+url+".js";
			if(!isload(suburl)){
				loadjs(suburl,null,null,async?async:false);
			}
			defresultFN(url,div);
		},
		/**
		 * @param urls
		 * @returns
		 */
		loads:function(urls,faultFN,async){
			for(var i in urls){
				var url = urls[i];
				FXC.loadJS.load(url,null,faultFN,async);//同时加载多个js异步加载会出点问题 下一步考虑下怎么优化
			}
		},
		loadJs:function(url){
			if(!isload(url)){
				loadjs(url);
			}
		},
	}
}();

$(document).ready(function() {
	FXC.AJAX = function(url,data,fn){
		$.ajax({
		    type : 'post',
		    async:false,
		    url: url,
		    dataType : 'json',
		    data: data,
		    success : function(result){
		    	if(result && result.msg=="1001"){
					top.location.href=host+'/index.html';
				}else{
					fn(result)
				}
		        
		    },
		    error:function(data){
		    	var result = data.responseJSON;
		    	if(result && result.msg=="1001"){
					top.location.href=host+'/index.html';
				}else{
					alert('请求出现异常!');
				}
		    }
		});
	};

	FXC.AJAXText = function(url,data,fn){
		$.ajax({
		    type : 'post',
		    async:false,
		    url: url,
		    dataType : 'text',
		    data: data,
		    success : function(result){
		    	if(result && result.msg=="1001"){
					top.location.href=host+'/index.html';
				}else{
					fn(result)
				}
		        
		    },
		    error:function(data){
		    	var result = data.responseJSON;
		    	if(result && result.msg=="1001"){
					top.location.href=host+'/index.html';
				}else{
					alert('请求出现异常!');
				}
		    }
		});
	};
});
