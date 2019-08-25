package com.fxc.admin.jdbc.annotation;

import java.util.HashMap;
import java.util.Map;

import com.sjb.util.Treat;

/**
 * 统一管理所有枚举
 * @author fxc
 *
 */
public class Enums {
	
	public static Map<String, Enum<?>[]> enumMap = new HashMap<String,Enum<?>[]>();
	
	/**使用静态变量 保存该对象下的枚举实例*/
	static{
		Class<?>[] fields = Enums.class.getDeclaredClasses();
		for(Class<?> c :fields){
			enumMap.put(c.getSimpleName(), (Enum<?>[]) c.getEnumConstants());
		}
	}
	
	
	/**
	 *  周期单位  
	 *  使用这种下标的要注意 这边拿到的枚举值是int类型 如果在sgin加签操作时要在加签之前转成字符串 否则会出现加签不一致的问题
	 * 年 月 天
	 * @author fxc
	 */
	public static enum UnitOfCycle{
		D,//日
		M,//月
		Y;//年
	}
	
	/**
	 * 还款周期
	 * 1,按月;3,一次;2,按季;5,按年;6,指定周期
	 * @author fxc
	 */
	public static enum Payfrequencytype implements JsonEnum{
		month("1"),
		season("2"),
		order("3"),
		year("5"),
		assign("6");//可以理解为自定义周期
		
		private String value;
		
		Payfrequencytype(String value){
			this.value = value;
		}
		
		public String getValue(){
			return this.value;
		}
	}
	
	/**
	 * 还款方式
	 * @author fxc
	 */
	public  static  enum RepaymentMethod implements JsonEnum {
		RPT_02("RPT-02"),//等额本金
		RPT_01("RPT-01"),//等额本息
		RPT_03("RPT-03"),//一次性还本付息
		RPT_04("RPT-04"),//按期付息一次还本
		RPT_13("RPT-13");//等额平息

		private String value;
		
		RepaymentMethod(String value){
			this.value = value;
		}
		
		public String getValue(){
			return this.value;
		}
	}
	
	/**
	 * 更具name获取
	 * @param clas
	 * @param name
	 * @return
	 */
	public static <T> Enum<?> getEnum(Class<?> clas,String name) throws Exception{
		Enum<?>[] ob = enumMap.get(Treat.getEnumName(clas));
		Enum<?> em = null;
		if(ob == null){
			ob = (Enum<?>[]) clas.getEnumConstants();
			enumMap.put(clas.getName(), ob);
		}
		for(Enum<?> o : ob){
			if(name.equals(o.name())){
				em =(Enum<?>) o;
				break;
			}
		}
		return em;
	}
	
	/**
	 * 更具下标获取
	 * @param clas
	 * @param name
	 * @return
	 */
	public static <T> Enum<?> getEnum(Class<?> clas,int i)throws Exception{
		Enum<?>[] ob = enumMap.get(Treat.getEnumName(clas));
		if(ob == null){
			ob = (Enum<?>[]) clas.getEnumConstants();
			enumMap.put(clas.getName(), ob);
		}
		try {
			Enum<?> e = ob[i];
			return e;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
