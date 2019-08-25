package com.sjb.util;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.TypeUtils;
import com.fxc.admin.jdbc.annotation.JsonEnum;

public class MyJson {

	public MyJson() {
		// TODO Auto-generated constructor stub
	}
	
	public static String toJSONString(Object object, SerializerFeature... features) {
		SerializeWriter out = new SerializeWriter();
		try{
	        String s;
	        JSONSerializer serializer = new JSONSerializer(out);
	        SerializerFeature arr$[] = features;
	        int len$ = arr$.length;
	        for (int i$ = 0; i$ < len$; i$++) {
	            SerializerFeature feature = arr$[i$];
	            serializer.config(feature, true);
	        }
	        //添加过滤filters
	        serializer.getValueFilters().add(new ValueFilter() {
	            public Object process(Object obj, String s, Object value) {
	                if (null != value) {
	                    if (value.getClass().isEnum() && value instanceof JsonEnum) {
	                        return ((JsonEnum) value).getValue();
	                    }
	                    return value;
	                } else {
	                    return "";
	                }
	            }
	        });
	        serializer.write(object);
	        s = out.toString();
	        return s;
		}finally{
			out.close();
		}
    }
	
	public static  Object toJSON(Object javaObject){
        return toJSON(javaObject, ParserConfig.getGlobalInstance());
    }
	
	@SuppressWarnings("rawtypes")
	public static  Object toJSON(Object javaObject, ParserConfig mapping){
        if(javaObject == null)
            return null;
        if(javaObject instanceof JSON)
            return (JSON)javaObject;
        if(javaObject instanceof Map)
        {
            Map map = (Map)javaObject;
            JSONObject json = new JSONObject(map.size());
            String jsonKey;
            Object jsonValue;
            for(Iterator i$ = map.entrySet().iterator(); i$.hasNext(); json.put(jsonKey, jsonValue))
            {
                java.util.Map.Entry entry = (java.util.Map.Entry)i$.next();
                Object key = entry.getKey();
                jsonKey = TypeUtils.castToString(key);
                jsonValue = toJSON(entry.getValue());
            }

            return json;
        }
        if(javaObject instanceof Collection)
        {
            Collection collection = (Collection)javaObject;
            JSONArray array = new JSONArray(collection.size());
            Object jsonValue;
            for(Iterator i$ = collection.iterator(); i$.hasNext(); array.add(jsonValue))
            {
                Object item = i$.next();
                jsonValue = toJSON(item);
            }

            return array;
        }
        Class clazz = javaObject.getClass();
        if(clazz.isEnum()){
        	if(javaObject instanceof JsonEnum){
        		return ((JsonEnum)javaObject).getValue();
        	}else{
        		return ((Enum)javaObject).name();
        	}
        }
        if(clazz.isArray())
        {
            int len = Array.getLength(javaObject);
            JSONArray array = new JSONArray(len);
            for(int i = 0; i < len; i++)
            {
                Object item = Array.get(javaObject, i);
                Object jsonValue = toJSON(item);
                array.add(jsonValue);
            }

            return array;
        }
        if(mapping.isPrimitive(clazz))
            return javaObject;
        try
        {
            List getters = TypeUtils.computeGetters(clazz, null);
            JSONObject json = new JSONObject(getters.size());
            FieldInfo field;
            Object jsonValue;
            for(Iterator i$ = getters.iterator(); i$.hasNext(); json.put(field.getName(), jsonValue))
            {
                field = (FieldInfo)i$.next();
                Object value = field.get(javaObject);
                jsonValue =toJSON(value);
            }

            return json;
        }
        catch(Exception e)
        {
            throw new JSONException("toJSON error", e);
        }
    }

	
}
