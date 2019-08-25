package com.fxc.business;

import java.lang.reflect.Field;
import java.util.ArrayList;

import com.fxc.entity.QueryInfo;

public interface BusinesProcessor {

	public String validationUnique(Object obj, ArrayList<Field> arrayList) throws  Exception;

	public String setNotNull(Object obj, ArrayList<Field> arrayList) throws  Exception;

	public String setUpdateNotNull(Object obj, ArrayList<Field> arrayList) throws  Exception;
	
	public void setisCreateUser(Object obj, ArrayList<Field> arrayList) throws  Exception;
	
	public void setisUpdateUser(Object obj, ArrayList<Field> arrayList) throws  Exception;

	public void setDefaultValue(Object obj, ArrayList<Field> arrayList) throws  Exception;

//	public void setisUserName(Object obj, ArrayList<Field> arrayList) throws  Exception;

	public void setNotSelect(QueryInfo qi, ArrayList<Field> arrayList) throws  Exception;

	public String validationUniqueUpDate(Object obj, ArrayList<Field> arrayList) throws  Exception;

	public void setCustomEnumValue(Object obj, ArrayList<Field> arrayList) throws  Exception;

	public boolean setBinding(Object obj, ArrayList<Field> value) throws Exception;

	public void setAssociationField(Object obj, ArrayList<Field> value) throws Exception;
	
	public void setFieldassociation(Object obj, ArrayList<Field> value) throws Exception;
	
	public void setFixedIntValue(Object obj, ArrayList<Field> value) throws Exception;

}
