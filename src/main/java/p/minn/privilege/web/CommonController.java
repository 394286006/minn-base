package p.minn.privilege.web;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import p.minn.common.annotation.MyParam;
import p.minn.common.exception.WebPrivilegeException;
import p.minn.privilege.service.CommonService;
import p.minn.privilege.service.DictionaryService;
import p.minn.privilege.utils.Constant;

/**
 * 
 * @author minn 
 * @QQ:3942986006
 * @comment 角色管理
 * 
 */
@Controller
@RequestMapping("/common")
@SessionAttributes(Constant.LOGINUSER)
public class CommonController {

	@Autowired
	private DictionaryService dictionaryService;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * @comment 提供页面跳转转换
	 * @param pidx
	 *            对应的jsp页面名称
	 * @return            
	 */
	@RequestMapping(params = "method=index")
	public String index(HttpServletRequest req, @RequestParam String pidx,@RequestParam Map<String,String> param) {
		param.remove("method");
		param.remove("pidx");
		Iterator<String> it=param.keySet().iterator();
		while(it.hasNext()){
			String k=it.next();
			req.setAttribute(k, param.get(k));
		}
		return pidx;
	}

	
	/**
	 * @deprecated
	 * @param lang
	 * @return
	 */
	@RequestMapping(params = "method=getQCDic")
	@ResponseBody
	public Object getQueryConfigDic(@MyParam("language") String lang,@RequestParam(required=false, defaultValue="web") String dic) {

		Object entity = null;
		try {
			entity = commonService.getQueryConfigDic(lang,dic);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}

		return entity;
	}
	
	@RequestMapping(params = "method=configquery")
	@ResponseBody
	public Object configquery(@MyParam("language") String lang,@RequestParam("messageBody") String messageBody) {

		Object entity = null;
		try {
			entity=commonService.configQuery(lang,messageBody);
		} catch (Exception e) {
			e.printStackTrace();
			entity = new WebPrivilegeException(e.getMessage());
		}

		return entity;
	}
	
	
	
	
	@RequestMapping(params = "method=getView")
	@ResponseBody
	public Object getView(@MyParam("language") String lang,@RequestParam("messageBody") String messageBody) {

		Object entity = null;
		try {
			entity=commonService.getViewConfigByName(lang,messageBody);
		} catch (Exception e) {
			entity = new WebPrivilegeException(e.getMessage());
		}

		return entity;
	}
}
