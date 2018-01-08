package p.minn.privilege.web;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.LocaleResolver;

import p.minn.common.annotation.MyParam;
import p.minn.common.exception.WebPrivilegeException;
import p.minn.privilege.service.DepartmentService;
import p.minn.privilege.utils.Constant;
import p.minn.security.cas.springsecurity.auth.User;

/**
 * 
 * @author minn 
 * @QQ:3942986006
 * @comment 
 * 
 */
@Controller
@RequestMapping("/department")
@SessionAttributes(Constant.LOGINUSER)
public class DepartmentController {

	@Autowired
	private DepartmentService departmentService;
	
	@Autowired
    private LocaleResolver localeResolver;


	@RequestMapping(params = "method=getResource")
	@ResponseBody
	public Object getResource(@ModelAttribute(Constant.LOGINUSER) User user,@MyParam("language") String lang,@RequestParam(required=false, defaultValue="web") String atype) {
		Object entity = null;
		try {
			entity = departmentService.getResource(user, lang);
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
			departmentService.save(user,messageBody,lang);
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
			departmentService.delete(messageBody);
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
			departmentService.update(user,messageBody,lang);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}

	
	@RequestMapping(params="method=query")
	public Object query(@RequestParam("messageBody") String messageBody,@MyParam("language") String lang){
		Object entity = null;
		try {
			entity=departmentService.query(messageBody,lang);
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
			entity = departmentService.checkCode(code,type);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;

	}
	
}
