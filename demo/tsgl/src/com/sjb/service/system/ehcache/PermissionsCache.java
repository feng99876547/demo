package com.sjb.service.system.ehcache;


import org.springframework.stereotype.Component;

import com.sjb.model.system.Permission;
import com.sjb.service.ObjectEHCache;


/**
 * 权限本地EhCache
 * @author fxc
 *
 */
@Component
public class PermissionsCache extends ObjectEHCache<Permission>{

	public PermissionsCache() {
		super("permissions");
	}

}
