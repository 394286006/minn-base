package p.minn.privilege.service;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;











import p.minn.common.utils.Page;
import p.minn.oauth.vo.User;
import p.minn.privilege.entity.Globalization;
import p.minn.privilege.entity.IdEntity;
import p.minn.privilege.repository.GlobalizationDao;
import p.minn.privilege.utils.Utils;

/**
 * @author minn
 * @QQ:3942986006
 *
 */
@Service
public class GlobalizationService {

	
	@Autowired
	private GlobalizationDao globalizationDao;
	
	
	
	/**
	 * 分页查询
	 * @param lang
	 * @return
	 * @throws Exception 
	 */
	public Page query(String messageBody,String lang) throws Exception{
		
		Page page=(Page) Utils.gson2T(messageBody, Page.class);
		Map<String,String> condition=Utils.getCondition(page);
		List<Map<String,Object>> list=globalizationDao.query(lang,null,condition);
	    page.setResult(list);
	
		return page;
	} 
	
	public void update(User user,String messageBody,String lang){
		Globalization globalization=(Globalization) Utils.gson2T(messageBody,Globalization.class);
		globalization.setUpdateid(user.getId());
		globalizationDao.update(globalization);
	}
	
	public void delete(String messageBody){
		IdEntity idEntity=(IdEntity) Utils.gson2T(messageBody,IdEntity.class);
		globalizationDao.delete(idEntity);
	}

	public void save(User user,String messageBody) {
		// TODO Auto-generated method stub
		Globalization globalization=(Globalization) Utils.gson2T(messageBody,Globalization.class);
		globalization.setCreateid(user.getId());
		globalizationDao.save(globalization);
	}

}
