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
import p.minn.privilege.service.DictionaryService;
import p.minn.privilege.utils.Constant;
import p.minn.security.cas.springsecurity.auth.User;

/**
 * 
 * @author minn 
 * @QQ:3942986006
 * @comment 角色管理
 * 
 */
@Controller
@RequestMapping("/dic")
@SessionAttributes(Constant.LOGINUSER)
public class DictionaryController {

	@Autowired
	private DictionaryService dictionaryService;
	

	@RequestMapping(params = "method=getDic")
	@ResponseBody
	public Object getDic(@MyParam("language") String lang,
			 @RequestParam String type) {

		Object entity = null;
		try {
			entity = dictionaryService.getDic(lang, type);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}

		return entity;
	}
	
	@RequestMapping(params = "method=getDicType")
	@ResponseBody
	public Object getDicType(@MyParam("language") String lang){
		Object entity = null;
		try {
			entity = dictionaryService.getDicType(lang);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}

		return entity;
	}
	
	@RequestMapping(params = "method=getDicLang")
	@ResponseBody
	public Object getDicLanguage(@RequestParam("messageBody") String messageBody,@MyParam("language") String lang){
		Object entity = null;
		try {
			entity = dictionaryService.getDicLanguage(messageBody,lang);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new WebPrivilegeException(e.getMessage());
		}

		return entity;
	}
	
	@RequestMapping(params="method=query")
	public Object query(@RequestParam("messageBody") String messageBody,@MyParam("language") String lang){
		Object entity = null;
		try {
			entity=dictionaryService.query(messageBody,lang);
		 } catch (Exception e) {
				entity = new WebPrivilegeException(e.getMessage());
			}
			return entity;
	}
	
	@RequestMapping(params = "method=update")
	@ResponseBody
	public Object update(@ModelAttribute(Constant.LOGINUSER) User user,@MyParam("language") String lang,@RequestParam("messageBody") String messageBody) {

		Object entity = null;
		try {
			dictionaryService.update(user,messageBody,lang);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new WebPrivilegeException(e.getMessage());
		}

		return entity;
	}
	
	@RequestMapping(params = "method=del")
	@ResponseBody
	public Object delete(@MyParam("language") String lang,@RequestParam("messageBody") String messageBody){
		Object entity = null;
		try {
			dictionaryService.delete(messageBody);
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
			dictionaryService.save(user,messageBody);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}
}
