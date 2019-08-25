package com.fxc.entity;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fxc.admin.jdbc.util.Treat;
import com.fxc.exception.BusinesException;
import com.fxc.utils.GetPara;


public class QueryInfo {

	private Integer begin;
	
	private Integer end;//页面大小
	
	/**
	 * 对象为className.fieldName
	 * 非对象 fieldName
	 */
	private ArrayList<String> cancel;//多用于 noselect注释取消对应字段的查找
	
	private String meSelect;//自身只查某个属性 只查某些字段已经打开了关联 使用该字段大部分情况下要配合cancelConnection字段 取消关联
	
	private String sort;//排序的列名
	
	private String order;//升降序
	
	private ArrayList<Order> or ;
	
//	private HttpServletRequest request;
	private HashMap<String,Object> params;
//	
//	private HttpServletResponse response;
	
	private HashMap<String,Object> search;//查询条件
	
	private String keyName;//做缓存需要的查询条件字段
	
	//注意带不同的条件使用不同的keyValue 用(field,说明)  第一个是sql查询条件的字段 防止因为不同条件查询出一样的值
	private String keyValue;//缓存key
	/**
	 * 返回结果集
	 */
//	private Result result;
	

	private boolean cancelConnection;//默认null null和true 取消所有关联   false使用关联查询   包含对象和list
	
	/**
	 * 在业务层控制 到时候要换map
	 * （对对象来说这个属性暂时可以理解为添加对象关联查询）
	 * 对象为className.fieldName
	 * 非对象 fieldName
	 */
	private HashMap<String,String> cancelNotSelect;//取消NotSelect 用逗号分割多个字段 在业务层控制
	
	
	private HashMap<String,String> connection;//重新指定内联外联 
	
	public Integer getBegin() {
		if(begin == null)
			this.begin = 0;
		return begin;
	}
	public void setBegin(Integer begin) {
		this.begin = begin;
	}
	public Integer getEnd() {
		if(end == null)
			this.end = 999;
		return end;
	}
	public void setEnd(Integer end) {
		this.end = end;
	}

	public QueryInfo(Integer begin, Integer end,String meSelect) {
		super();
		this.begin = begin;
		this.end = end;
		this.meSelect = meSelect;
	}
	
	public QueryInfo(Integer begin, Integer end) {
		super();
		this.begin = begin;
		this.end = end;
	}
	
	public QueryInfo(HashMap<String, Object> where) {
		super();
		this.search = where;
	}
	
	public QueryInfo() {
		super();
	}
	
	public QueryInfo(HttpServletRequest request) {
		super();
//		this.request = request;
	}
	
	public ArrayList<String> getCancel() {
		return cancel;
	}
	
	public void addCancel(String Str) {
		if(this.cancel==null)
			this.cancel = new ArrayList<String>();
		this.cancel.add(Str);
	}
	
	
//	public HttpServletRequest getRequest() {
//		return request;
//	}
//	
//	public void setRequest(HttpServletRequest request) {
//		this.request = request;
//	}
//	
//	public HttpServletResponse getResponse() {
//		return response;
//	}
//	
//	public void setResponse(HttpServletResponse response) {
//		this.response = response;
//	}
	
	public String getSort() {
		//这里要不要验证下去掉 select update insert 等关键字出现返回xxxx
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	public String getMeSelect() {
		return meSelect;
	}
	public void setMeSelect(String meSelect) {
		this.meSelect = meSelect;
	}
	
	public HashMap<String,String> getConnection() {
		return connection;
	}
	public void setConnection(HashMap<String,String> connection) {
		this.connection = connection;
	}
	
	public boolean isCancelConnection() {
		return cancelConnection;
	}
	/**
	 * true 取消所有关联查询
	 * @param cancelConnection
	 */
	public void setCancelConnection(boolean cancelConnection) {
		this.cancelConnection = cancelConnection;
	}
	
	//这边要想办法 把初始化和获取分开 每次多了一个判断
	public HashMap<String, Object> getSearch() {
		if(this.search == null)
			this.search = new HashMap<String, Object>();
		return search;
	}
	public void setSearch(HashMap<String, Object> search) {
		this.search = search;
	}
	
	public String getOrder() {
		return order;
	}
	
	public void setOrder(String order) {
		this.order = order;
	}
	public ArrayList<Order> getOr() {
		return or;
	}
	public void setOr(ArrayList<Order> or) {
		this.or = or;
	}
	
//	public Result<?> getResult() {
//		return result;
//	}
//	public void setResult(Result<?> result) {
//		this.result = result;
//	}
	
	public HashMap<String, Object> getParams() {
		return params;
	}
	public void setParams(HashMap<String, Object> params) {
		this.params = params;
	}
	
	public Object getParams(String key) {
		if(this.params==null)
			return null;
		else
			return params.get(key);
	}
	
	/**
	 * 如果取到的参数为空将抛出异常
	 * @param key
	 * @return
	 */
	public Object getNotNullParams(String key)throws BusinesException{
		Object obj = getParams(key);
		if(Treat.isEmpty(obj)){
			throw new BusinesException(key+"为必须参数不能为空");
		}
		return obj;
	}
	
	public void init(HttpServletRequest request, HttpServletResponse response) throws Exception{
		this.setParams(GetPara.getParams(request));
		//分页参数配置
		String page = request.getParameter("page");
    	String pageSize = request.getParameter("rows");
    	Integer begin = null;
    	if(page == null){
    		page = "1";
    	}
    	if(pageSize==null){
    		pageSize = "20";	
    	}
    	begin = Integer.parseInt(pageSize)*(Integer.parseInt(page)-1);
		this.setBegin(begin);
		this.setEnd(Integer.parseInt(pageSize));
	}
	
	public HashMap<String,String> getCancelNotSelect() {
		return cancelNotSelect;
	}
	
	public void setCancelNotSelect(HashMap<String,String> cancelNotSelect) {
		this.cancelNotSelect = cancelNotSelect;
	}
	
	public void addCancelNotSelect(String key){
		if(cancelNotSelect == null)
			cancelNotSelect = new HashMap<String,String>();
		cancelNotSelect.put(key, key);
	}
	public String getKeyName() {
		return this.keyName == null ? "id": keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getKeyValue() {
		return keyValue;
	}
	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}
	
	
}
