package p.minn.privilege.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import p.minn.common.annotation.MyParam;
import p.minn.common.exception.WebPrivilegeException;
import p.minn.privilege.service.GlobalizationService;
import p.minn.privilege.utils.Constant;
import p.minn.security.cas.springsecurity.auth.User;

/**
 * 
 * @author minn 
 * @QQ:3942986006
 * 
 */
@Controller
@RequestMapping("/gla")
@SessionAttributes(Constant.LOGINUSER)
public class GlobalizationController {

	@Autowired
	private GlobalizationService globalizationService;
	

	@RequestMapping(params = "method=update")
	@ResponseBody
	public Object update(@ModelAttribute(Constant.LOGINUSER) User user,@MyParam("language") String lang,@RequestParam("messageBody") String messageBody) {

		Object entity = null;
		try {
			globalizationService.update(user,messageBody,lang);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}

		return entity;
	}
	
	@RequestMapping(params = "method=del")
	@ResponseBody
	public Object delete(@MyParam("language") String lang,@RequestParam("messageBody") String messageBody){
		Object entity = null;
		try {
			 globalizationService.delete(messageBody);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}

		return entity;
	}
	
	@RequestMapping(params = "method=save")
	@ResponseBody
	public Object save(@ModelAttribute(Constant.LOGINUSER) User user,@MyParam("language") String lang,@RequestParam("messageBody") String messageBody) {
		Object entity = null;
		try {
			globalizationService.save(user,messageBody);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}

}
