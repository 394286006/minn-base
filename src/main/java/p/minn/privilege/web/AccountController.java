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
import p.minn.privilege.utils.Constant;
import p.minn.security.service.AccountService;
import p.minn.vo.User;

/**
 * 
 * @author minn
 * @QQ:3942986006
 * @comment 用户管理
 *
 */
@Controller
@RequestMapping("/account")
@SessionAttributes(Constant.LOGINUSER)
public class AccountController {

	@Autowired
	private AccountService accountService;

	@RequestMapping(params = "method=save")
	@ResponseBody
	public Object save(@ModelAttribute(Constant.LOGINUSER) User user,@RequestParam("messageBody") String messageBody,
			@MyParam("language") String lang) {
		Object entity = null;
		try {
			accountService.save(user,messageBody, lang);
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
			accountService.delete(messageBody);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}

	
	@RequestMapping(params = "method=update")
	@ResponseBody
	public Object update(@ModelAttribute(Constant.LOGINUSER) User user,@RequestParam("messageBody") String messageBody,
			@MyParam("language") String lang) {
		Object entity = null;
		try {
			accountService.update(user,messageBody, lang);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}

	

	@RequestMapping(params = "method=query")
	public Object query(@RequestParam("messageBody") String messageBody,
			@MyParam("language") String lang) {
		Object entity = null;
		try {
			entity = accountService.query(messageBody, lang);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}

	

	@RequestMapping(params = "method=saveRoleRes")
	public Object saveAccountRole(
			@RequestParam("messageBody") String messageBody) {
		Object entity = null;
		try {
			accountService.saveAccountRole(messageBody);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}

	@RequestMapping(params = "method=getAccountRole")
	public Object getAccountRole(
			@RequestParam("messageBody") String messageBody,
			@MyParam("language") String lang) {
		Object entity = null;
		try {
			entity = accountService.getAccountRole(lang, messageBody);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}

}
