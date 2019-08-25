package com.fxc.admin.jdbc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface CustomBinding {

	String FieldName();
	
	String FieldValue();
	
}
