package com.sjb.dao.system.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.fxc.entity.Order;
import com.fxc.entity.Order.OrderType;
import com.fxc.entity.QueryInfo;
import com.sjb.dao.system.MenuDaoBean;
import com.sjb.model.system.Menu;
import com.sjb.util.Treat;


@Repository
public class MenuDaoBeanImpl extends PublicDaoImpl<Menu, Long> implements MenuDaoBean{

	public MenuDaoBeanImpl(){
		super();
		super.sort = new ArrayList<Order>();
		super.sort.add(new Order("position",OrderType.ASC));
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public HashMap<String,Menu> getMenus() throws Exception{
		QueryInfo qi = new QueryInfo();
		HashMap<String,Menu> menus = new HashMap<String,Menu>();
		qi.setCancelConnection(true);
		List<Menu> li = super.gets(qi);
		if(!Treat.isEmpty(li)){
			for(Menu m : li){
				if(m.getParentId() == null)
					menus.put(m.getId().toString(), m);
				else
					menus.put(m.getPermission(), m);
			}
		}
		return menus;
	}
}
