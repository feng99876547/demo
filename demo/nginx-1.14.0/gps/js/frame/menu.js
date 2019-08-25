

/**
 * 初始化tab 监听add事件
 */
function initTab(){
	$(CTT.tabId).tabs({
		fit:true,
		onAdd:function(title,index){
			var tab = $(CTT.tabId).tabs('getTab',index);
			var div = $('#child_'+tab.attr('id'));
			FXC.loadJS.loadTab(div.attr('data-url'),div);
		},
//		onClose:function(title,index){
////			mainTab.tabs('getTab',label).panel('destroy');
//		}
	});
}

//添加tab的接口
function addTab(id, url, label, icon) {
	var mainTab = $(CTT.tabId);
	var added = false;
	// 如果ID已存在，则选中tab
	var selectTab = mainTab.tabs('getTab',label);
	if(selectTab){//label titlename 不能是数字否则是按照索引找
		mainTab.tabs('select',label);
		return;
	}
	mainTab.tabs('add',{
		id:id,
	    title:label,
//	    href:_webApp+'/admin/form.html',
	    content:'<div id = "child_'+id+'" data-url = "'+url+'" style="visiblity:hidden;"></div><div style="visiblity:hidden;"></div>',
	    closable:true,
	    destroy:false,//关闭tabel时销毁div
	    //tools后面的刷新按钮
//	    tools:[{
//			iconCls:'icon-mini-refresh',
//			handler:function(){
//				alert('refresh');
//			}
//	    }]
	});
}
/**
 * 点击子菜单
 * @param obj
 */
function clickMenu(obj) {
	var tabid = CTT.tabDiv+$(obj).attr('data-tabid');
	var url = $(obj).attr('data-url');
	var name = $(obj).attr('data-name');
	var icon = $(obj).attr('data-icon');
	addTab(tabid, url, name, icon);
}
/**
 * 点击分组菜单
 * @param obj
 */
//function clickGroupMenu(obj){
//	var menuId = $(obj).attr('data-id');
	//创建cookie 有效期为365天 权限验证已经和前端没关系了
//    $.cookie('menuId',menuId, { expires:365});
//}
/**
 * 初始化系统菜单
 * 
 * @param menus
 */
function initMenu() {
	if (menusJson) {
		$.each(menusJson, function(i, menu) {
			if(menu.childMenu){
				var strMenuItemHTML = "";
				$.each(menu.childMenu, function (j, childMenu) {//font-weight:bold
					var len = childMenu.permission.lastIndexOf(":");
					menuPermission[childMenu.permission.substring(0,len)] = childMenu.listPermissions;
					strMenuItemHTML += '<div style="height:22px;padding-top:10px;font-size:10pt;"><a onclick="clickMenu(this)" '
						+' data-tabid="' + childMenu.id +'" data-url="' + childMenu.url +'" data-name="' + childMenu.name +'" class="menuLink">' + childMenu.name + '</a></div>';
				});
				$("#menu").accordion('add', {
					title: menu.name,
					content: strMenuItemHTML,
					selected: false
				});
			}
		});
	}
}


