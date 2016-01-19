package p.minn.privilege.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import p.minn.common.annotation.MyParam;
import p.minn.common.exception.WebPrivilegeException;
import p.minn.privilege.service.LogService;
import p.minn.privilege.utils.Constant;

/**
 * 
 * @author minn
 * @QQ:3942986006
 * @comment 操作日志
 * 
 */

@Controller
@RequestMapping("/log")
@SessionAttributes(Constant.LOGINUSER)
public class LogController {

	@Autowired
	private LogService logService;
	
	
	@RequestMapping(params = "method=query")
	@ResponseBody
	public Object query(@RequestParam("messageBody") String messageBody,@MyParam("language") String lang) {
	 
		Object entity = null;
		try {
			entity = logService.query(messageBody,lang);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;

	}
	
	@RequestMapping(params = "method=getLogDetail")
	@ResponseBody
	public Object getLogDetail(@RequestParam("messageBody") String messageBody,@MyParam("language") String lang) {
		Object entity = null;
		try {
			entity = logService.getDetail(lang, messageBody);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;

	}
	
	@RequestMapping(params = "method=getSignature")
	@ResponseBody
	public Object getSignature(@MyParam("language") String lang) {

		Object entity = null;
		try {
			entity = logService.getSignature(lang);
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
			logService.delete(messageBody);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}
		return entity;
	}
}
