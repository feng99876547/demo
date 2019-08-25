package com.sjb.util.jwt;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
 
import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.internal.com.fasterxml.jackson.databind.ObjectMapper;
import com.sjb.model.system.Role;
import com.sjb.model.system.User;
 
public class JWT {
 
	private static final String SECRET = "YYFaxb321as-w*dfaJEm9^&sdfnk*.sadf/adsd";
	
	private static final String EXP = "exp";
	
	private static final String PAYLOAD = "payload";
	 
//	private static final long maxAge = 30l * 60l * 1000l;
	
	private static final long maxAge = 9000l;
	/**
	 * get jwt String of object
	 * @param object
	 *            the POJO object
	 * @param maxAge
	 *            the milliseconds of life time
	 * @return the jwt token
	 */
	public static <T> String sign(T object) {
		try {
			final JWTSigner signer = new JWTSigner(SECRET);
			final Map<String, Object> claims = new HashMap<String, Object>();
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = mapper.writeValueAsString(object);
			claims.put(PAYLOAD, jsonString);
			//currentTimeMillis单位毫秒
			claims.put(EXP, System.currentTimeMillis() + maxAge);
			return signer.sign(claims);
		} catch(Exception e) {
			return null;
		}
	}
	
	
	/**
	 * get the object of jwt if not expired
	 * @param jwt
	 * @return POJO object
	 */
	public static<T> T unsign(String jwt, Class<T> classT) {
		final JWTVerifier verifier = new JWTVerifier(SECRET);
	    try {
			final Map<String,Object> claims= verifier.verify(jwt);
			if (claims.containsKey(EXP) && claims.containsKey(PAYLOAD)) {
				long exp = (Long)claims.get(EXP);
				long currentTimeMillis = System.currentTimeMillis();
				if (exp > currentTimeMillis) {
					String json = (String)claims.get(PAYLOAD);
					ObjectMapper objectMapper = new ObjectMapper();
					return objectMapper.readValue(json, classT);
				}
			}
			return null;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * 更新过期时间  
	 * 由客户端定时像后台跟新过期时间  
	 */
	
	public static void main(String[] args) {
		User us = new User();
//		us.setUsername("2133435");
//		us.setAgainstUrl("sdfsdfsdf");
//		us.setPositiveUrl("dsfsdfxcvcxvxcvxcvxcv gfbhdfgdfwer234vxcvxcv gfxcvxcv gf234324234");
		ArrayList<Role> li = new ArrayList<Role>();
		li.add(new Role(3l));
		us.setRoleList(li);
		String str = JWT.sign(us);
		
		for(int i=0;i<1000;i++){
			System.out.println("oooo:"+i);
			try {
				Thread.sleep(i*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			getUser(str);
		}
	}
	
	public static void getUser(String str){
		User uu = JWT.unsign(str, User.class);
//		System.out.println(uu.getUsername());
	}
}
