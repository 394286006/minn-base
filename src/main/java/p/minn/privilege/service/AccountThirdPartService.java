package p.minn.privilege.service;

import java.util.List;
import java.util.Map;

















import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import p.minn.common.utils.MyGsonMap;
import p.minn.common.utils.Page;
import p.minn.privilege.entity.AccountThirdPart;
import p.minn.privilege.entity.IdEntity;
import p.minn.privilege.repository.AccountThirdPartDao;
import p.minn.privilege.utils.Utils;
import p.minn.security.cas.springsecurity.auth.User;

/**
 * 
 * @author minn
 * @QQ:3942986006
 * @comment 
 */
@Service
public class AccountThirdPartService {

	@Autowired
	private AccountThirdPartDao dao;
	
	public Page query(String messageBody, String lang) {
		// TODO Auto-generated method stub
		Page page=(Page) Utils.gson2T(messageBody, Page.class);
		Map<String,String> condition=Utils.getCondition(page);
		List<Map<String,Object>> list=dao.query(lang,page,condition);
	    page.setResult(list);
	    return page;
	}

	public void update(User user,String messageBody, String lang) {
		// TODO Auto-generated method stub
		AccountThirdPart account =(AccountThirdPart) Utils.gson2T(messageBody,AccountThirdPart.class);
		account.setUpdateid(user.getId());
		dao.update(account);
	}

	public void save(User user,String messageBody, String lang) {
		// TODO Auto-generated method stub
		MyGsonMap<Map,AccountThirdPart> msm=MyGsonMap.getInstance(messageBody,Map.class, AccountThirdPart.class); 
		AccountThirdPart account=msm.gson2T(AccountThirdPart.class);
		account.setCreateid(user.getId());
		dao.save(account);
	}

	public void delete(String messageBody) {
		// TODO Auto-generated method stub
		IdEntity idEntity=(IdEntity) Utils.gson2T(messageBody,IdEntity.class);
		dao.delete(idEntity);
	}

  public List<Map<String,Object>> getThirdParts(String lang, User user) {
    // TODO Auto-generated method stub
    return dao.getThirdParts(lang, user.getId());
  }

  public List<Map<String,Object>> getLoginThirdParts(String lang) {
    // TODO Auto-generated method stub
    return dao.getLoginThirdParts(lang);
  }




}
