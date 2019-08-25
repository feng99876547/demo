package com.fxc.admin.jdbc.service.entity;

import java.util.List;

import com.fxc.admin.jdbc.service.listener.serviceSection.AfertAddListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.AfertGetListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.AfterUpdateListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeAddListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeDelListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeGetListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeGetsListener;
import com.fxc.admin.jdbc.service.listener.serviceSection.BeforeUpdateListener;

public interface ServiceSection<T> {
	/**
	 * @return the beforeAdd
	 */
	public BeforeAddListener<T> getBeforeAdd();

	/**
	 * @param beforeAdd the beforeAdd to set
	 */
	public void setBeforeAdd(BeforeAddListener<T> beforeAdd);

	/**
	 * @return the afertAdd
	 */
	public AfertAddListener<T> getAfertAdd() ;

	/**
	 * @param afertAdd the afertAdd to set
	 */
	public void setAfertAdd(AfertAddListener<T> afertAdd);

	/**
	 * @return the beforeUpdate
	 */
	public BeforeUpdateListener<T> getBeforeUpdate();
	

	public void setAfterUpdate(AfterUpdateListener<T> afterUpdate);
	/**
	 * @return the AfterUpdate
	 */
	public AfterUpdateListener<T> getAfterUpdate();

	/**
	 * @param beforeUpdate the beforeUpdate to set
	 */
	public void setBeforeUpdate(BeforeUpdateListener<T> beforeUpdate) ;

	/**
	 * @return the beforeGets
	 */
	public BeforeGetsListener<List<T>> getBeforeGets();

	/**
	 * @param beforeGets the beforeGets to set
	 */
	public void setBeforeGets(BeforeGetsListener<List<T>> beforeGets);

	/**
	 * @return the beforeGet
	 */
	public BeforeGetListener<T> getBeforeGet();

	/**
	 * @param beforeGet the beforeGet to set
	 */
	public void setBeforeGet(BeforeGetListener<T> beforeGet);
	/**
	 * @return the afertGet
	 */
	public AfertGetListener<T> getAfertGet();

	/**
	 * @param afertGet the afertGet to set
	 */
	public void setAfertGet(AfertGetListener<T> afertGet);
	
	public BeforeDelListener<List<T>> getBeforeDel();

	public void setBeforeDel(BeforeDelListener<List<T>> beforeDel);
}
