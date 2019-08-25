//依赖于jquery.contextMenu.js
$(document).ready(function(){
		$.contextMenu({
	        selector: '.nav li', 
	        callback: function(key, options) {
	           if(key=="closeThis"){
	        	   var id =$(this).find("a").attr("href").substring(1);
		           if(id!='index'){
		        	   delTab(id);
		           }
	           }else if(key=="closeOther"){
	        	   var nextNum=$(this).nextAll().length;
	        	   var preNum=$(this).prevAll().length;
	        	   //关闭后面的tab页
	        	   for(var i=nextNum+preNum;i>preNum;i--){
	        		   delTabForIndex(i);
		           }
	        	   //关闭前面的tab页
	        	   for(var i=preNum-1;i>0;i--){
	        		   delTabForIndex(i);
		           }	        	   
	           }else if(key=="closeAll"){
	        	   var num =$(this).parent().children().length;		   
	        	   for(var i=num-1;i>0;i--){
	        		   delTabForIndex(i);
		           }
	           }           
	        },
	        items: {
	            "closeThis": {name: "关闭当前页", icon: "closeThis"},
	            "closeOther": {name: "关闭其他页", icon: "closeOther"},
	            "closeAll": {name: "关闭全部页", icon: "closeAll"}
	        }
	    });
	});