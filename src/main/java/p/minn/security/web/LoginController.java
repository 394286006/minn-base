package p.minn.security.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import p.minn.common.exception.WebPrivilegeException;
import p.minn.privilege.utils.Constant;
import p.minn.security.cas.springsecurity.auth.User;
import p.minn.security.service.IAccountService;


/**
 * 
 * @author minn
 * @QQ:3942986006
 *
 */
@Controller
public class LoginController {
	
	@Autowired
	private IAccountService accountService;


	@RequestMapping(value="login")
	public Object Login(HttpServletRequest req){
		Object entity = null;
		try {
			String userName = null;
			User user=null;
			Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if (principal instanceof User) {
				user=(User)principal;
				userName = user.getUsername();
			} else {
				userName = principal.toString();
			}
			if(!userName.equals(Constant.ANONYMOUSUSER)){
				req.getSession().setAttribute(Constant.LOGINUSER,user);
				entity=user;
			}else{
				throw new WebPrivilegeException("login fail");
			}
		 } catch (Exception e) {
				entity = new WebPrivilegeException(e.getMessage());
			}
			return entity;
	}

	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public Object logout (HttpServletRequest request, HttpServletResponse response) {
		Object entity = null;
		try{
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null){    
				new SecurityContextLogoutHandler().logout(request, response, auth);
			}
			Map<String,String> rs=new HashMap<String,String>();
			rs.put("info", "logout success");
			entity=rs;
		}catch(Exception e){
			entity = new WebPrivilegeException(e.getMessage());
		}
		
		return entity ;
	}
	
	@RequestMapping(value="/logoutHandler", method = RequestMethod.GET)
	public Object logoutHandler (HttpServletRequest request, HttpServletResponse response) {
		Object entity = null;
		try{
			Map<String,String> rs=new HashMap<String,String>();
			rs.put("info", "logout success");
			entity=rs;
		}catch(Exception e){
			entity = new WebPrivilegeException(e.getMessage());
		}
		
		return entity ;
	}

}
