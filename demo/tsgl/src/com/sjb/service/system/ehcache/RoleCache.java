package com.sjb.service.system.ehcache;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.sjb.model.system.Permission;
import com.sjb.service.ObjectEHCache;


/**
 * 角色缓存
 * @author fxc
 */
@Component
public class RoleCache extends ObjectEHCache<HashMap<String,Permission>>{

	public RoleCache() {
		super("roles");
	}
}
