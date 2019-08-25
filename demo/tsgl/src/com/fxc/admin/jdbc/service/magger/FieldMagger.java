package com.fxc.admin.jdbc.service.magger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Component;

import com.fxc.business.BusinesImpl;

@Component
public class FieldMagger {

	//到时候看下内存占用情况 如果内存占用过多 写个接口 将field改String 调用时由String获取Field 使用完在释放Field 更高端定时清理一段时间内未被使用的Field
	//或是达到一定的数量后开始清理使用次数最少的field  或是使用合理的设计方式 将模块系统拆分 减少每个系统需要反射的字段
	private  HashMap<String,HashMap<String,ArrayList<Field>>> businessFields = new HashMap<String,HashMap<String,ArrayList<Field>>>();
	
	public FieldMagger() {
	}
	
	public  HashMap<String,ArrayList<Field>> getFields(Class<?> clas){
		HashMap<String,ArrayList<Field>> fields = businessFields.get(clas.getName());
		if(fields == null){
			fields = BusinesImpl.setBusiness(clas);
			businessFields.put(clas.getName(), fields);
		}
		return fields;
	}

}
