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
import p.minn.privilege.service.RoleService;
import p.minn.privilege.utils.Constant;
import p.minn.vo.User;

/**
 * 
 * @author minn
 * @QQ:3942986006
 * @comment 角色管理
 *
 */
@Controller
@RequestMapping("/role")
@SessionAttributes(Constant.LOGINUSER)
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@RequestMapping(params = "method=save")
	@ResponseBody
	public Object save(@ModelAttribute(Constant.LOGINUSER) User user,@RequestParam("messageBody") String messageBody,@MyParam("language") String lang) {
		Object entity = null;
		try {
			roleService.save(user,messageBody,lang);
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
			roleService.delete(messageBody);
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
			roleService.update(user,messageBody,lang);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}
	
	
	@RequestMapping(params="method=query")
	public Object query(@RequestParam("messageBody") String messageBody,@MyParam("language") String lang){
		Object entity = null;
		try {
			 entity=roleService.query(messageBody,lang);
		 } catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		 }
		return entity;
	}
	
	
	
	@RequestMapping(params="method=saveRoleRes")
	public Object saveRoleResource(@RequestParam("messageBody") String messageBody){
		Object entity = null;
		try {
			roleService.saveRoleResource(messageBody);
		 } catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}
	
	@RequestMapping(params="method=getRoleRes")
	public Object getRoleResource(@RequestParam("messageBody") String messageBody,@MyParam("language") String lang){
		Object entity = null;
		try {
			 entity=roleService.getRoleResource(lang,messageBody);
		 } catch (Exception e) {
				entity = new WebPrivilegeException(e.getMessage());
			}
			return entity;
	}
	
}
