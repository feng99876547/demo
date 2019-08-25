package com.fxc.admin.jdbc.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 换dubbo这边需要拆分 验证放到controller层 业务操作放到service层 验证通过在开始远程调用
 * 将按业务 和验证分为两块
 * @author fxc
 */
@Target({ ElementType.FIELD, ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Custom {
	
	/**
	 * 显示给前端的通俗名字
	 * @return
	 */
	String name() default "兄弟没设置name";
	
	/**
	 * 由业务配置类似拦截器控制
	 * 自动注入时 注入的默认值 只能是string类型字段
	 * 注意 请不要在对象中使用该注释 没进行处理  该值是在自动加载后修改的
	 * 属于业务
	 */
	String  DefaultStringValue() default "";
	
	
	/**
	 * 由业务配置类似拦截器控制
	 * 自动注入时 注入的默认值 只能是Integer类型字段
	 * 使用基本类型要注意初始化时的值就是0 所以使用的状态中不能用0 提交0会被认为无值会被附上默认值
	 * 基本类型中默认值只支持数字 不包含bolean char byte short等 
	 * 而且修改时还要注意 该值不能为空 默认值的问题如果没有提交该值默认值是0还要维护修改 所以最好放弃级别类型
	 * 注意 请不要在对象中使用该注释 没进行处理 该值是在自动加载后修改的
	 * 属于业务
	 */
	 int DefaultIntValue() default -1;
	 
	 /**
	 * 由业务配置类似拦截器控制
	 * 由后台定义的固定值 (需要写死的)
	 * 和CustomEnum的区别在于一般这种值就是一个或是固定的非选择型参数
	 * 注意 请不要在对象中使用该注释 没进行处理 该值是在自动加载后修改的
	 * 属于业务
	 */
	 String FixedIntValue() default "";
	
	
	/**
	 * 由业务配置类似拦截器控制
	 * 创建时自动引入session中对应的的字段
	 * 一般用于创建人字段
	 * 属于业务
	 */
	String createInjectUser() default "";
	
	
	/**
	 * 由业务配置类似拦截器控制
	 * 自动注入时 从session中获取用户注入
	 * 一般用于修改人字段
	 * 属于业务
	 */
	String isUpdateUser() default "";
	
	/**
	 * 由业务配置类似拦截器控制
	 * 唯一约束字段 业务层会先用该字段查是否存在
	 * 属于业务（因为涉及到jdbc操作）
	 */
	boolean isUnique() default false;
	
	/**
	 * 在加载赋值实体时有该注释的字段不会获取前端提交的值 由后台业务手动赋值
	 * 属于业务
	 */
	boolean noUpload() default false;
	
//	/**
//	 * 指定一个角色id  角色id和session中的匹配 该字段就重新从前端接收 配合noUpload()一起使用
//	 * 意思就是这边指定的角色可以自动修改该字段 不然要代码实现
//	 */
//	String roleUpload() default "";
	
	/**
	 * 底层createInfo方法中控制
	 * 该注释为ture 数据库中没有的字段 sql全部过滤
	 * 属于业务
	 */
	String Transient() default "";
	
	
	/**
	 * 标识id字段
	 */
	String IdKey() default "";
	/**
	 * 由业务配置类似拦截器控制
	 * 查询的动态生成sql过程中 添加到 QueryInfo 的cancel字段中 要区分对象还是字段 
	 * 如果需要要使用addSelect
	 * 如果是对象 需要 类名.field 表示取消的是对象
	 * 属于业务
	 */
	String NotSelect() default "";
	
	
	/**
	 *该注释字段值不能为空 为空报错返回页面
	 *属于验证
	 */
	boolean NotNull() default false;
	
	/**
	 * 如果指向的字段不为空那么我也不能为空
	 * 指向的字段为空我也要为空
	 * 属于验证
	 */
	String associationField() default "";
	
	/**
	 * 当我的值为xxx(数组下标0)时候 , 那么数组长度1之后指向的对应字段不能为空
	 * SPINSTERHOOD	未婚
	 * MARRIED	已婚
	 * DIVORCED	离异
	 * OTHER	其他
	 * 当marryState的值为MARRIED时，那么cpPagerName cpPagerId cpCustTel不能为空
	 * Custom(Fieldassociation={"MARRIED","cpPagerName","cpPagerId","cpCustTel"})
	 * 属于验证
	 */
	String[] Fieldassociation() default {};
	
	/**
	 * 在我不为空时还要对指定字段赋值 绑定关系 (包含添加 修改操作)
	 */
	 CustomBinding  Binding() default @CustomBinding(FieldName="",FieldValue="");
	/**
	 * 是否验证修改人(配置在创建人字段那 减少麻烦查找创建人字段)
	 * 修改操作时如果不是管理员那么创建人必须是自己才可以修改
	 * 属于业务
	 */
//	boolean UpdateValidation() default false;
	
}
