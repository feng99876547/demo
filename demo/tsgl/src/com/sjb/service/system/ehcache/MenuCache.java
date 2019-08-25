package com.sjb.service.system.ehcache;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sjb.model.system.Menu;
import com.sjb.service.ObjectEHCache;


/**
 * 角色缓存
 * @author fxc
 */
@Component
public class MenuCache extends ObjectEHCache<List<Menu>>{

	public MenuCache() {
		super("menu");
	}

}
