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

public class ServiceSectionImpl<T> implements ServiceSection<T>{
	
	private BeforeAddListener<T> beforeAdd;
	
	private AfertAddListener<T> afertAdd;
	
	private BeforeUpdateListener<T> beforeUpdate;
	
	private AfterUpdateListener<T> afterUpdate;
	
	private BeforeGetsListener<List<T>> beforeGets;
	
	private BeforeGetListener<T> beforeGet;
	
	private AfertGetListener<T> afertGet;
	
	private BeforeDelListener<List<T>> beforeDel;

	/**
	 * @return the beforeAdd
	 */
	public BeforeAddListener<T> getBeforeAdd() {
		return beforeAdd;
	}

	/**
	 * @param beforeAdd the beforeAdd to set
	 */
	public void setBeforeAdd(BeforeAddListener<T> beforeAdd) {
		this.beforeAdd = beforeAdd;
	}

	/**
	 * @return the afertAdd
	 */
	public AfertAddListener<T> getAfertAdd() {
		return afertAdd;
	}

	/**
	 * @param afertAdd the afertAdd to set
	 */
	public void setAfertAdd(AfertAddListener<T> afertAdd) {
		this.afertAdd = afertAdd;
	}

	/**
	 * @return the beforeUpdate
	 */
	public BeforeUpdateListener<T> getBeforeUpdate() {
		return beforeUpdate;
	}
	
	/**
	 * @param beforeUpdate the beforeUpdate to set
	 */
	public void setBeforeUpdate(BeforeUpdateListener<T> beforeUpdate) {
		this.beforeUpdate = beforeUpdate;
	}
	
	/**
	 * @param afterUpdate the afterUpdate to set
	 */
	public void setAfterUpdate(AfterUpdateListener<T> afterUpdate) {
		this.afterUpdate = afterUpdate;
	}

	/**
	 * @return the afterUpdate
	 */
	public AfterUpdateListener<T> getAfterUpdate() {
		return afterUpdate;
	}

	/**
	 * @return the beforeGets
	 */
	public BeforeGetsListener<List<T>> getBeforeGets() {
		return beforeGets;
	}

	/**
	 * @param beforeGets the beforeGets to set
	 */
	public void setBeforeGets(BeforeGetsListener<List<T>> beforeGets) {
		this.beforeGets = beforeGets;
	}

	/**
	 * @return the beforeGet
	 */
	public BeforeGetListener<T> getBeforeGet() {
		return beforeGet;
	}

	/**
	 * @param beforeGet the beforeGet to set
	 */
	public void setBeforeGet(BeforeGetListener<T> beforeGet) {
		this.beforeGet = beforeGet;
	}

	/**
	 * @return the afertGet
	 */
	public AfertGetListener<T> getAfertGet() {
		return afertGet;
	}

	/**
	 * @param afertGet the afertGet to set
	 */
	public void setAfertGet(AfertGetListener<T> afertGet) {
		this.afertGet = afertGet;
	}

	public ServiceSectionImpl() {
		super();
	}

	public BeforeDelListener<List<T>> getBeforeDel() {
		return beforeDel;
	}

	public void setBeforeDel(BeforeDelListener<List<T>> beforeDel) {
		this.beforeDel = beforeDel;
	}


}
