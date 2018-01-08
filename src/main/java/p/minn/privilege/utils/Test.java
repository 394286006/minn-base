package p.minn.privilege.utils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import p.minn.privilege.entity.Account;

public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Account user=new Account();
		user.setActive(1);
		user.setId(1);
		user.setLoginType(1);
		user.setName("minn民");
		user.setPwd("123");
		user.setType(1);
	 
		//System.out.println(Utils.getBeanValue(user));
		//getResourceKey(null);
		
			//
			BCryptPasswordEncoder encode=new BCryptPasswordEncoder();
			System.out.println(encode.encode("89bed836cd5a7f00b8d6aed6dd80a248bf035cce4a7a9f68f2b0d39e"));
	}
	
	public static void getResourceKey(Method method) throws Exception{
		
		 Map<String,String> param=new HashMap<String,String>();
		    param.put("name", "Base");
		    param.put("active", "1");
			//QueryConfigUtil.getViewConfigByName("getConfigMenu");
			//QueryConfigUtil.getSqlByName("getConfigMenu","en",param,null);
			//QueryConfigUtil.getTotalSqlByName("getConfigMenu",param);
			//QueryConfigUtil.getQueryConfig();
	}
	
	
	
	
	

}
