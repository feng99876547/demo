package com.sjb.dao.system.impl;



import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.fxc.entity.Order;
import com.fxc.entity.Order.OrderType;
import com.sjb.dao.system.PermissionDaoBean;
import com.sjb.model.system.Permission;


@Repository
public class PermissionDaoBeanImpl extends PublicDaoImpl<Permission, Long> implements PermissionDaoBean{
	public PermissionDaoBeanImpl(){
		super.sort = new ArrayList<Order>();
		sort.add(new Order("Permission","sequence", OrderType.ASC));//默认排序
	}
	
}
