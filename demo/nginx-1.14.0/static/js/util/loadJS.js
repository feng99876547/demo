
Fxc.ns('Fxc.util');
Fxc.ns('Fxc.admin');

/**
 * 加载所指定的JS文件
 * @param {String} url 要加载的JS文件
 * @param {function} resultFN 加载成功后执行的方法
 * @param {function} faultFN 加载失败后执行的方法
 * @param {boolean} running 是否运行代码（默认是运行）
 * @return {boolean} 返回是否已经加载过
 * Fxc.util.loadJS.load(url, resultFN, faultFN);
 *
 * @param {String} url 要加载的JS文件
 * @return {boolean} 返回是否已经加载过
 * Fxc.util.ajaxJS.isLoad(url);
 */
Fxc.util.loadJS = function(){
	var jss=null, ajax=null, isLoad=null, nav=null, isIE=null, run=null,
	nav = navigator.userAgent.toLowerCase(),
	isIE = !(/opera/.test(nav)) && (/msie/.test(nav)),
	run = true,
	jss = {},	//用于存储加载成功的JS
	ajax = function (url, resultFN, faultFN, async){
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
		xmlHttp.open("get",url,async===false?false:true);
		xmlHttp.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
		xmlHttp.onreadystatechange = function (){
			if(xmlHttp.readyState == 4){
				if(xmlHttp.status == 200){
					if(!isLoad(url)) jss[url]=url;	//if(!isLoad(url)) jss.push(url);
					try{
						(new Function(xmlHttp.responseText)).call(window);
						//(isIE && run) ? window.execScript(xmlHttp.responseText) : window.eval(xmlHttp.responseText);
					}catch(e){
						if(faultFN) faultFN(e);
					}
					if(resultFN) resultFN(xmlHttp.responseText,xmlHttp);
				}else{
					if(faultFN) faultFN(xmlHttp);
				}
			}
		};
		//xmlHttp.send(null);
		xmlHttp.send("_dc="+new Date().getTime());
	
	},
	isLoad = function(url){
		//return false;
		return jss[url]?true:false;
	};
	return {
		load: function(url, resultFN, faultFN, running, async){
			if(url == null || url == "" || typeof url!='string'){
				resultFN('ok'); return true;
			}
			var urls = url.split(";");
			Fxc.util.loadJS.loads(urls, resultFN, faultFN, running);
			//new ajax(url, resultFN, faultFN);
			return false;
		},
		loads: function(urls, resultFN, faultFN, running, async){
			run = running===false?false:true;
			var u = urls.constructor+'';
			if(u.match(/Array/)){
				function myload(i){
					if(isLoad(urls[i])){//是否加载过
						i++;
						if(i == urls.length){
							if(resultFN) resultFN();
						}else{
							myload(i);
						};
					}else{
						new ajax(urls[i], function(){
							i++;
							if(i == urls.length){
								if(resultFN) resultFN();
							}else{
								myload(i);
							};
						}, faultFN, async);
					}
				}
				if(urls.length>0) myload(0);
			}else{
				if(faultFN){
					faultFN('所加载的JS要数组类型！');
				}
			}
		},
		isLoad: function(url){
			return isLoad(url);
		},
		loadSup: function(ec, resultFN, faultFN, running, async){
			var e = Fxc.EC.get(ec);
			if(!e && resultFN) resultFN();
			else{
				var ls = function(sup){
					if(sup.sup){
						Fxc.util.loadJS.loadSup(sup.sup,function(){
							
						}, faultFN, running, async);
					}
					
					if(sup && !isLoad(sup.path)){
						new ajax(sup.pat, function(){
							ls(sup.sup);
							Fxc.EC.extend(sup);
						}, faultFN, async);
					}else{
						if(resultFN) resultFN();
					}
				};
				ls(e.sup);
			}
		}
	};
}();

