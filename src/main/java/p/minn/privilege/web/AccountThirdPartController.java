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
import p.minn.privilege.service.AccountThirdPartService;
import p.minn.privilege.utils.Constant;
import p.minn.vo.User;

/**
 * 
 * @author minn
 * @QQ:3942986006
 * @comment 第三方帐号管理
 *
 */
@Controller
@RequestMapping("/acountTP")
@SessionAttributes(Constant.LOGINUSER)
public class AccountThirdPartController {

    @Autowired
	private AccountThirdPartService service;
 
	@RequestMapping(params = "method=save")
	@ResponseBody
	public Object save(@ModelAttribute(Constant.LOGINUSER) User user,@RequestParam("messageBody") String messageBody,
			@MyParam("language") String lang) {
		Object entity = null;
		try {
		  service.save(user,messageBody, lang);
		} catch (Exception e) {
		  e.printStackTrace();
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}

	

	@RequestMapping(params = "method=del")
	@ResponseBody
	public Object delete(@RequestParam("messageBody") String messageBody) {
		Object entity = null;
		try {
		  service.delete(messageBody);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}
	
	@RequestMapping(params = "method=unbind")
    @ResponseBody
    public Object unbind(@RequestParam("messageBody") String messageBody) {
        Object entity = null;
        try {
          service.delete(messageBody);
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
			service.update(user,messageBody, lang);
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
			entity = service.query(messageBody, lang);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}
	
	@RequestMapping(params = "method=getThirdParts")
    @ResponseBody
    public Object getThirdParts(@MyParam("language") String lang,
        @ModelAttribute(Constant.LOGINUSER) User user) {

        Object entity = null;
        try {
            entity = service.getThirdParts(lang, user);
        } catch (Exception e) {
            entity = new WebPrivilegeException(e.getMessage());
        }

        return entity;
    }
	@RequestMapping(params = "method=getLoginThirdParts")
    @ResponseBody
    public Object getLoginThirdParts(@RequestParam("lang") String lang) {

        Object entity = null;
        try {
            entity = service.getLoginThirdParts(lang);
        } catch (Exception e) {
            entity = new WebPrivilegeException(e.getMessage());
        }

        return entity;
    }

}
