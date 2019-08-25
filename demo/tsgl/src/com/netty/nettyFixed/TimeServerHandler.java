package com.netty.nettyFixed;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.internal.org.bouncycastle.jcajce.provider.asymmetric.rsa.RSAUtil;
import com.fxc.entity.QueryInfo;
import com.fxc.utils.ContextUtils;
import com.fxc.utils.GetPara;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;


public class TimeServerHandler extends ChannelHandlerAdapter {

	private int counter;
	
//	    
//	private static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKwiA0jqXSvwePjcHE0c1bKxvXB4Xx6J/vRTVzMnagTD82d6MWKElfqrBLFxO4JMwvpSchIVdOaLGsYVpOEVZSxv0Re21bsn+/5ZVJWIXGjX8CmcxO8ZLl3wZaKGSyPhX1nnQ2RCbfCm+sgQL1NDxnb0ivEv48/uh456yesZK/hLAgMBAAECgYBjFllJug/UYWNh7mMkSLsDWKmyerhWvh1TzD3gJoozIBGXuZGAIs6rM5NKCeK1yTZcHuWnX06h/+VzrbXyE/zeu134rYyfjcZtzi1xmRBeojjGes9YIUYjPiHrJ4eorGNQETeMPN9JEXjzQIB+iPfa5W+ovtWBdD59acv2M74/wQJBAOFQ8Xs1cG/lwfs2GDU7RFN5gauOgV94y0q324XgONIxQ2hoj1EceqkkYfruMyUc+X/CCkqHqkCR4N4RlJndXVECQQDDkvkHhCMS69rPsNwzLql10uWkKwyd6bYRK866QsWu1fOBJqji9nbQdEIN5nzYHVL377ce6ketjYvKOs0WYeTbAkAeKHtqBmkaUSJ5wW/UEC7BDY5xrA1c/goX4iwb6zsvxzBClVw5expf8WK7944Y9ZvfodVGzInZQq1ai/fb8GlBAkA+XoyUXQtiaVzqhxhyKhtYu90IMcJgSbwKVdIjjE/Gpex174JEfxz89VZrYGnH8fIXp5bagRRCwLUn1QC6dpWpAkAyjPKPJAX17LpmRDqZcW1hw+eb4Ls9Mkkn9au0B3LCADJW8jNG9kqLbAezaeQWAsxyDE5935zB+ChultA+/WJa";

	//白名单集合
	private String ips = "127.0.0.1";//可以是redis的监听事件 单配置修改后触发监听事件修改ips
	
	private String[] iplist = null;
	
	private int len = 0;
	
	private void init(){
		iplist = ips.split(",");
		len = iplist.length;
	}
	
	public TimeServerHandler(){
		super();
		init();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String body = (String) msg;
		JSONObject json = null;
		try {
			json = JSONObject.parseObject(body);
		} catch (Exception e) {
			System.out.println("json格式有错");
		}
		
		//data验签
		
		Object obj = inoveMethod(json);
		
		String bodys = obj.toString();//转jsonString
		
		ctx.write(bodys);
	}
	
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
		String ip = ctx.channel().remoteAddress().toString();
		System.out.println("clientIp:"+ip);
		String[] client = ip.split("\\:");
		for(int i = 0;i<len;i++){
			if(client[0].equals(iplist[i])){
				super.handlerAdded(ctx);
				return;
			}
		}
		System.out.println("非法用户链接netty服务端,客户ip:"+ip);
		ctx.close();
	} 
	
	public static Object inoveMethod(JSONObject json) throws Exception{
		
		JSONObject data = json.getJSONObject("data");
		
		String className = json.getString("action");
		
		String meth = json.getString("method");
		
		Object controller = ContextUtils.appContext.getBean(className);
		
		Object obj = null;
		
		try {
			//统一定义使用QueryInfo参数的方法 就不去遍历判断方法参数类型是否匹配了（主要针对重构函数 不过一般不存在这种情况）
			Method method = obj.getClass().getMethod(meth,new Class[]{QueryInfo.class});
			QueryInfo queryInfo = new QueryInfo();
//			queryInfo.setParams(GetPara.getSearch(data));
			//初始化参数
			String page = json.getString("page");
	    	String pageSize = json.getString("rows");
//	    	queryInfo.initPage(page,pageSize);
	    	initQueryInfo(queryInfo,data);
	    	obj = method.invoke(controller, new Object[]{queryInfo});
	    	return obj;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}
	
	public static void initQueryInfo(QueryInfo queryInfo,JSONObject data){
		Set<String> keys = data.keySet();
		//初始化QueryInfo
		for (String key : keys) {
			switch (key) {
				case "begin":
					queryInfo.setBegin(Integer.parseInt(data.getString("begin")));
					break;
				case "end":
					queryInfo.setEnd(Integer.parseInt(data.getString("end")));
					break;
				case "meSelect":
					queryInfo.setMeSelect(data.getString("meSelect"));
					break;
				case "sort":
					queryInfo.setSort(data.getString("sort"));
					break;
				case "order":
					queryInfo.setOrder(data.getString("order"));
					break;
				case "keyName":
					queryInfo.setKeyName(data.getString("keyName"));
					break;
				case "keyValue":
					queryInfo.setKeyValue(data.getString("keyValue"));
					break;
				default:
					break;
			}
		}
	}
	
	//对异常进行自定义处理
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)throws Exception{
		cause.printStackTrace();
		ctx.write(cause.getMessage());
		super.exceptionCaught(ctx, cause);
	}
	
	public static void main(String[] args) {
		
	}
}

