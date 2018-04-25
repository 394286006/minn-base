package p.minn.privilege.web;



import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import p.minn.common.annotation.MyParam;
import p.minn.common.exception.WebPrivilegeException;
import p.minn.privilege.service.MenuService;
import p.minn.privilege.utils.Constant;
import p.minn.vo.User;

/**
 * 
 * @author minn 
 * @QQ:3942986006
 * @comment 菜单管理
 * 
 */
@Controller
@RequestMapping("/menu")
@SessionAttributes(Constant.LOGINUSER)
public class MenuController {

	@Autowired
	private MenuService menuService;
	 
	@RequestMapping(params = "method=getPrivateMenu")
	@ResponseBody
	public Object getPrivateMenu(HttpServletRequest req,HttpServletResponse rep,@ModelAttribute(Constant.LOGINUSER) User user,@RequestParam(required=false, defaultValue="zh") String lang){
		Object entity = null;
		try {
			entity = menuService.getPrivateMenu(user, lang);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new WebPrivilegeException(e.getMessage());
		}

		return entity;
	}

	@RequestMapping(params = "method=getResource")
	@ResponseBody
	public Object getResource(@ModelAttribute(Constant.LOGINUSER) User user,@MyParam("language") String lang,@RequestParam(required=false, defaultValue="web") String atype) {
		Object entity = null;
		try {
			entity = menuService.getResource(user, lang);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}

		return entity;
	}

	
	@RequestMapping(params = "method=save")
	@ResponseBody
	public Object save(@ModelAttribute(Constant.LOGINUSER) User user,@RequestParam("messageBody") String messageBody,@MyParam("language") String lang) {
		Object entity = null;
		try {
			menuService.save(user,messageBody,lang);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}
	
	@RequestMapping(params = "method=del")
	@ResponseBody
	public Object delete(@RequestParam("messageBody") String messageBody) {
		Object entity = null;
		try {
			menuService.delete(messageBody);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}
	
	@RequestMapping(params = "method=update")
	@ResponseBody
	public Object update(@ModelAttribute(Constant.LOGINUSER) User user,@RequestParam("messageBody") String messageBody,@MyParam("language") String lang) {
		Object entity = null;
		try {
			menuService.update(user,messageBody,lang);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}

	
	@RequestMapping(params="method=query")
	public Object query(@RequestParam("messageBody") String messageBody,@MyParam("language") String lang){
		Object entity = null;
		try {
			entity=menuService.query(messageBody,lang);
		 } catch (Exception e) {
				entity = new WebPrivilegeException(e.getMessage());
		 }
		return entity;
	}

	@RequestMapping(params = "method=checkCode")
	@ResponseBody
	public Object checkCode(@RequestParam String code,@RequestParam String type) {
		Object entity = null;
		try {
			entity = menuService.checkCode(code,type);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;

	}
	
}
