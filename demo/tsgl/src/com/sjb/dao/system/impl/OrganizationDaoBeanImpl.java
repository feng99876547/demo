package com.sjb.dao.system.impl;

import org.springframework.stereotype.Repository;

import com.fxc.admin.jdbc.dao.PublicDaoImpl;
import com.sjb.dao.system.OrganizationDaoBean;
import com.sjb.model.system.Organization;

@Repository
public class OrganizationDaoBeanImpl extends PublicDaoImpl<Organization,Long> implements OrganizationDaoBean{

}
