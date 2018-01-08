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
import p.minn.privilege.service.CategoryService;
import p.minn.privilege.utils.Constant;
import p.minn.security.cas.springsecurity.auth.User;

/**
 * 
 * @author minn 
 * @QQ:3942986006
 * 
 */
@Controller
@RequestMapping("/category")
@SessionAttributes(Constant.LOGINUSER)
public class CategoryController {

	@Autowired
	private CategoryService categoryService;
	
	@RequestMapping(params = "method=getCategory")
	@ResponseBody
	public Object getCategory(@MyParam("language") String lang) {
		Object entity = null;
		try {
			entity = categoryService.getCategory(lang);
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
			categoryService.save(user,messageBody,lang);
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
			categoryService.delete(messageBody);
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
			categoryService.update(user,messageBody,lang);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}


	@RequestMapping(params="method=query")
	public Object query(@RequestParam("messageBody") String messageBody,@MyParam("language") String lang){
		Object entity = null;
		try {
			entity=categoryService.query(messageBody,lang);
		 } catch (Exception e) {
				entity = new WebPrivilegeException(e.getMessage());
		 }
		return entity;
	}

}
