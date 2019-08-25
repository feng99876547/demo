package com.fxc.entity;

import com.alibaba.fastjson.JSONObject;

public class Result<T> {
	/**
	 * 返回对象包含T和List<T>
	 */
	private T rows;
	
	/**
	 * 返回的数据 一般用于json值或其他值
	 */
	private Object data;
	
	/**
	 * ids 包含id
	 */
	private Object ids;
	
	/**
	 * 总行数
	 */
	private Integer total;
	
	/**
	 * 自定义信息
	 */
	private String msg;
	
	/**
	 * true 业务不符合 结束修改保存不执行修改保存操作
	 */
	private Boolean success;

	
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success == null ? false : success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public Result(Boolean success,String msg) {
		super();
		this.success = success;
		this.msg = msg;
	}

	public Result(T rows,Boolean success) {
		super();
		this.setRows(rows);
		this.success = success;
	}
	
	public Result(T rows,Boolean success,String msg) {
		super();
		this.setRows(rows);
		this.success = success;
		this.msg = msg;
	}
	
	public Result(Boolean success) {
		super();
		this.success = success;
	}
	
	public Result() {
		super();
	}

	public Object getIds() {
		return ids;
	}

	public void setIds(Object ids) {
		this.ids = ids;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public T getRows() {
		return rows;
	}

	public void setRows(T rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "Result [rows=" + JSONObject.toJSONString(rows) + ", data=" + data + ", ids=" + ids + ", total=" + total + ", msg=" + msg + ", success=" + success
				+ "]";
	}
	
	@SuppressWarnings("rawtypes")
	public static Result create(boolean b,String msg){
		Result r = new Result();
		r.setSuccess(b);
		r.setMsg(msg);
		return r;
	}
	
	@SuppressWarnings("rawtypes")
	public static Result createData(boolean b,Object data){
		Result r = new Result();
		r.setSuccess(b);
		r.setData(data);
		return r;
	}
}
