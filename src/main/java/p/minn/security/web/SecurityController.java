package p.minn.security.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.LocaleResolver;

import p.minn.common.exception.WebPrivilegeException;
import p.minn.privilege.utils.Constant;
import p.minn.privilege.utils.Utils;
import p.minn.security.cas.springsecurity.auth.User;
import p.minn.security.service.IAccountService;
import p.minn.security.service.SecurityService;

/**
 * 
 * @author minn
 * @QQ:3942986006
 *
 */
@Controller
public class SecurityController {
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private IAccountService accountService;
	
	@Autowired
    private LocaleResolver localeResolver; 
	
	@RequestMapping(value="swfseckey")
	public Object getKey(HttpServletRequest req,HttpServletResponse rep,@RequestParam(required=false, defaultValue="zh") String lang,@RequestParam String skey){
		Object entity = null;
		try {
			Locale local=new Locale(lang);
			localeResolver.setLocale(req, rep, local);
			Map<String,String> data=new HashMap<String,String>();
			data.put("key", Utils.getSecurityKey());
			entity=data;
		 } catch (Exception e) {
				entity = new WebPrivilegeException(e.getMessage());
			}
			return entity;
	}
	
	@RequestMapping(value="swfsecRamdom")
	public Object getSecurityRamdom(){
		Object entity = null;
		try {
			Map<String,String> data=new HashMap<String,String>();
			data.put("random",Utils.getSecurityRandom());
			entity=data;
		 } catch (Exception e) {
				entity = new WebPrivilegeException(e.getMessage());
			}
			return entity;
	}
	
	@RequestMapping(value="/swfqrcodekey")
    public Object genQrcodeKey(HttpServletRequest request,HttpServletResponse rep,@RequestParam(required=false, defaultValue="zh") String lang){
        Object entity = null;
        try {
           Object user= request.getSession().getAttribute(Constant.LOGINUSER);
            Map<String,String> data=new HashMap<String,String>();
            data.put("key", Utils.getSecurityKey());
            if(user!=null){
              accountService.updateKey(((User)user).getUsername(), data.get("key")+"_"+lang);
            }
            entity=data;
         } catch (Exception e) {
           e.printStackTrace();
                entity = new WebPrivilegeException(e.getMessage());
            }
            return entity;
    }
	
	@RequestMapping(value="/swfcheckqrcode")
    public Object checkQrcode(HttpServletRequest req,HttpServletResponse rep,@RequestParam String randomKey){
        Object entity = null;
        try {
          Map<String,Object> data=new HashMap<String,Object>();
          data.put("access",  accountService.checkQrCodeByRandomKey(randomKey));
            entity=data;
         } catch (Exception e) {
           e.printStackTrace();
                entity = new WebPrivilegeException(e.getMessage());
            }
            return entity;
    }
	
}
