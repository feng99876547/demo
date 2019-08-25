var theme = {
	/*主题切换数据*/
	themeData : [ {
		"name" : "UI lightness",
		"href" : "../css/plugin/ui-lightness/jquery.ui.theme.css"
	}, {
		"name" : "UI darkness",
		"href" : "../css/plugin/ui-darkness/jquery.ui.theme.css"
	}, {
		"name" : "Smoothness",
		"href" : "../css/plugin/smoothness/jquery.ui.theme.css"
	}, {
		"name" : "Start",
		"href" : "../css/plugin/start/jquery.ui.theme.css"
	}, {
		"name" : "Redmond",
		"href" : "../css/plugin/redmond/jquery.ui.theme.css"
	}, {
		"name" : "Sunny",
		"href" : "../css/plugin/sunny/jquery.ui.theme.css"
	}, {
		"name" : "Overcast",
		"href" : "../css/plugin/overcast/jquery.ui.theme.css"
	}, {
		"name" : "Le Frog",
		"href" : "../css/plugin/le-frog/jquery.ui.theme.css"
	}, {
		"name" : "Flick",
		"href" : "../css/plugin/flick/jquery.ui.theme.css"
	}, {
		"name" : "Pepper Grinder",
		"href" : "../css/plugin/pepper-grinder/jquery.ui.theme.css"
	}, {
		"name" : "Eggplant",
		"href" : "../css/plugin/eggplant/jquery.ui.theme.css"
	}, {
		"name" : "Dark Hive",
		"href" : "../css/plugin/dark-hive/jquery.ui.theme.css"
	}, {
		"name" : "Cupertino",
		"href" : "../css/plugin/cupertino/jquery.ui.theme.css"
	}, {
		"name" : "South St",
		"href" : "../css/plugin/south-street/jquery.ui.theme.css"
	}, {
		"name" : "Blitzer",
		"href" : "../css/plugin/blitzer/jquery.ui.theme.css"
	}, {
		"name" : "Humanity",
		"href" : "../css/plugin/humanity/jquery.ui.theme.css"
	}, {
		"name" : "Hot Sneaks",
		"href" : "../css/plugin/hot-sneaks/jquery.ui.theme.css"
	}, {
		"name" : "Excite Bike",
		"href" : "../css/plugin/excite-bike/jquery.ui.theme.css"
	}, {
		"name" : "Vader",
		"href" : "../css/plugin/vader/jquery.ui.theme.css"
	}, {
		"name" : "Dot Luv",
		"href" : "../css/plugin/dot-luv/jquery.ui.theme.css"
	}, {
		"name" : "Mint Choc",
		"href" : "../css/plugin/mint-choc/jquery.ui.theme.css"
	}, {
		"name" : "Black Tie",
		"href" : "../css/plugin/black-tie/jquery.ui.theme.css"
	}, {
		"name" : "Trontastic",
		"href" : "../css/plugin/trontastic/jquery.ui.theme.css"
	}, {
		"name" : "Swanky Purse",
		"href" : "../css/plugin/swanky-purse/jquery.ui.theme.css"
	} ],
	/*切换主题*/
	switchThemes : function(themeName) {
		var locStr = "";
		
		for ( var i = 0; i < this.themeData.length; i++) {
			if (themeName == this.themeData[i].name) {
				locStr = this.themeData[i].href;
			}
		}
		this.updateCSS(locStr);
	},
	/*根据cookie切换主题*/
	autoThemesSwitch : function(locStr) {		
		if ($.cookie("jquery-ui-theme")) {
			var themeName = $.cookie("jquery-ui-theme");
			var locStr = "";
			for ( var i = 0; i < this.themeData.length; i++) {
		
				if (themeName == this.themeData[i].name) {
					locStr = this.themeData[i].href;					
				}
			}
			this.updateCSS(locStr);
		};
	},
	/*更改css引用*/
	updateCSS : function(locStr) {
		var cssLink = $('<link href="' + locStr + '" type="text/css" rel="Stylesheet" class="ui-theme" />');
		$("head").append(cssLink);
		if ($("link.ui-theme").size() > 3) {
			$("link.ui-theme:first").remove();
		}
		;
	}
};
