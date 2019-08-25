FXC.ns("FXC");

FXC.msgbox = function(){
	var func = "";
	var create_mask = function(){//创建遮罩层的函数
	    var mask=document.createElement("div");
	    mask.id="mask";
	    mask.style.position="absolute";
	    mask.style.filter="progid:DXImageTransform.Microsoft.Alpha(style=4,opacity=25)";//IE的不透明设置
	    mask.style.opacity=0.4;//Mozilla的不透明设置
	    mask.style.background="black";
	    mask.style.top="0px";
	    mask.style.left="0px";
	    mask.style.width=get_width();
	    mask.style.height=get_height();
	    mask.style.zIndex=1000;
	    document.body.appendChild(mask);
	};
	var create_msgbox =  function(w,h,t){//创建弹出对话框的函数
	    var box=document.createElement("div")    ;
	    box.id="msgbox";
	    box.style.position="absolute";
	    box.style.width=w;
	    box.style.height=h;
	    box.style.overflow="visible";
	    box.innerHTML=t;
	    box.style.zIndex=1001;
	    document.body.appendChild(box);
	    var butt = box.getElementsByTagName("input");
	    //监听点击事件
	    for(var i=0;i<butt.length;i++){
	    	 console.log(butt[i].value);
	    	 if(butt[i].value == "确定"){
	    		 FXC.addEvent(butt[i],"click",remove);
	    	 }
	    }
	    re_pos();
	};
	var re_mask = function(){
	    /*
	    更改遮罩层的大小,确保在滚动以及窗口大小改变时还可以覆盖所有的内容
	    */
	    var mask=document.getElementById("mask")    ;
	    if(null==mask)return;
	    mask.style.width=get_width()+"px";
	    mask.style.height=get_height()+"px";
	};
	var re_pos = function(){
	    /*
	    更改弹出对话框层的位置,确保在滚动以及窗口大小改变时一直保持在网页的最中间
	    */
	    var box=document.getElementById("msgbox");
	    if(null!=box){
	        var w=box.style.width;
	        var h=box.style.height;
	        box.style.left=get_left(w)+"px";
	        box.style.top=get_top(h)+"px";
	    }
	};
	var remove = function(){
	    /*
	    清除遮罩层以及弹出的对话框
	    */
	    var mask=document.getElementById("mask");
	    var msgbox=document.getElementById("msgbox");
	    if(null==mask&&null==msgbox)return;
	    document.body.removeChild(mask);
	    document.body.removeChild(msgbox);
	};
	var get_width = function(){
	    return (document.body.clientWidth+document.body.scrollLeft);
	};
	var get_height = function(){
	    return (document.body.clientHeight+document.body.scrollTop);
	};
	var get_left = function(w){
	    var bw=document.body.clientWidth;
	    var bh=document.body.clientHeight;
	    w=parseFloat(w);
	    return (bw/2-w/2+document.body.scrollLeft);
	};
	var get_top = function(h){
	    var bw=document.body.clientWidth;
	    var bh=document.body.clientHeight;
	    h=parseFloat(h);
	    return (bh/2-h/2+document.body.scrollTop);
	};
	var re_show = function(){
	    /*
	    重新显示遮罩层以及弹出窗口元素
	    */
	    re_pos();
	    re_mask();    
	};
	var load_func = function(){
	    /*
	    加载函数,覆盖window的onresize和onscroll函数
	    */
	    window.onresize=re_show;
	    window.onscroll=re_show;    
	};
	return {
		/*        
		title :弹出对话框的标题,标题内容最好在25个字符内,否则会导致显示图片的异常                                                            
		text  :弹出对话框的内容,可以使用HTML代码,例如<font color='red'>删除么?</font>,如果直接带入函数,注意转义
		func  :弹出对话框点击确认后执行的函数,需要写全函数的引用,例如add(),如果直接带入函数,注意转义。
		cancel:弹出对话框是否显示取消按钮,为空的话不显示,为1时显示
		focus :弹出对话框焦点的位置,0焦点在确认按钮上,1在取消按钮上,为空时默认在确认按钮上
		icon  :弹出对话框的图标
		*/ 
		getBox:function(title,content,fun,cancel,focus){
			 func = fun ? fun:func;
			 create_mask();
			 var temp="<div style=\"width:300px;border: 2px solid #37B6D1;background-color: #fff; font-weight: bold;font-size: 12px;\" >"
		            +"<div style=\"line-height:25px; padding:0px 5px;    background-color: #37B6D1;\">"+title+"</div>"
		            +"<table  cellspacing=\"0\" border=\"0\"><tr><td style=\" padding:0px 0px 0px 20px; \"></td>"
		            +"<td ><div style=\"background-color: #fff; font-weight: bold;font-size: 12px;padding:20px 0px ; text-align:left;\">"+content
		            +"</div></td></tr></table>"
		            +"<div style=\"text-align:center; padding:0px 0px 20px;background-color: #fff;\"><input type='button'  style=\"border:1px solid #CCC; background-color:#CCC; width:50px; height:25px;\" value='确定'id=\"msgconfirmb\" >";
		    if(null!=cancel){temp+="   <input type='button' style=\"border:1px solid #CCC; background-color:#CCC; width:50px; height:25px;\" value='取消'  id=\"msgcancelb\"   >";}
		    temp+="</div></div>";
		    create_msgbox(400,200,temp);
		    if(focus==0||focus=="0"||null==focus){document.getElementById("msgconfirmb").focus();}
		    else if(focus==1||focus=="1"){document.getElementById("msgcancelb").focus();}  
		}
	}
}();



