package p.minn.privilege.service;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;

import p.minn.common.entity.Globalization;
import p.minn.common.entity.IdEntity;
import p.minn.common.repository.GlobalizationDao;
import p.minn.common.utils.MyGsonMap;
import p.minn.common.utils.Page;
import p.minn.privilege.entity.Dictionary;
import p.minn.privilege.repository.DictionaryDao;
import p.minn.privilege.utils.Constant;
import p.minn.privilege.utils.Utils;
import p.minn.vo.User;

/**
 * @author minn
 * @QQ:3942986006
 *
 */
@Service
public class DictionaryService {

	
	@Autowired
	private DictionaryDao dictionaryDao;
	
	@Autowired
	private GlobalizationDao globalizationDao;
	
	/**
	 * 获取数据字典
	 * @param lang
	 * @param type 'RESOURCETYPE','RESOURCEURLTYPE'
	 * @return
	 */
	public Map<String,List<Map<String,Object>>> getDic(String lang,String type) throws Exception{

	    List<Map<String,Object>> list=dictionaryDao.getDic(lang,type);
	    
	    Map<String,List<Map<String,Object>>> keys=new HashMap<String, List<Map<String,Object>>>();
	    List<Map<String,Object>> v=null;
	    
	    for(Map<String,Object> m:list){
	    	String k=m.get(Constant.MKEY).toString();
	    	if(keys.containsKey(k)){
	    		v=keys.get(k);
	    	    v.add(m);
	    	}else{
	    		v=new ArrayList<Map<String,Object>>();
	    		v.add(m);
	    		keys.put(k, v);
	    	}
	    }
	    
		return keys;
	}
	
	
	/**
	 * 获取字典类型
	 * @param lang
	 * @param type 'RESOURCETYPE'
	 * @return
	 */
	public List<Map<String,Object>> getDicType(String lang) throws Exception{

	    List<Map<String,Object>> list=dictionaryDao.getDicType(lang);
	    
		return list;
	}
	
	
	/**
	 * 分页查询
	 * @param lang
	 * @return
	 * @throws Exception 
	 */
	public Page query(String messageBody,String lang) throws Exception{
		
		Page page=(Page) Utils.gson2T(messageBody, Page.class);
		Map<String,String> condition=Utils.getCondition(page);
		System.out.println("pkey:"+condition.get("pkey"));
		int total=dictionaryDao.getTotal(lang,condition);
		page.setPage(page.getPage()+1);
		page.setTotal(total);
		
		List<Map<String,Object>> list=dictionaryDao.query(lang,page,condition);
	    page.setResult(list);
	
		return page;
	} 
	
	/**
	 * @param lang
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String,Object>> getDicLanguage(String messageBody,String lang) throws Exception{
		
		Map map=Utils.gson2Map(messageBody);
		List<Map<String,Object>> list=globalizationDao.query(lang,null,map);
		return list;
	}  

	public void update(User user,String messageBody,String lang){
		MyGsonMap<Map,Dictionary> msm=MyGsonMap.getInstance(messageBody,Map.class, Dictionary.class); 
		Dictionary dictionary=msm.gson2T(Dictionary.class);
		Map map=msm.gson2Map();
		dictionary.setUpdateid(user.getId());
		dictionaryDao.update(dictionary);
		Globalization glz=new Globalization();
		glz.setUpdateid(user.getId());
		glz.setId(Double.valueOf(map.get("gid").toString()).intValue());
		glz.setName(map.get("name").toString());
		globalizationDao.update(glz);
	}
	
	@Transactional
	public void delete(String messageBody){
		IdEntity idEntity=(IdEntity) Utils.gson2T(messageBody,IdEntity.class);
		globalizationDao.deleteByTableId(idEntity.getId().toString(),"dictionary");
		dictionaryDao.delete(idEntity);
		
	}

	public void save(User user,String messageBody) {
		// TODO Auto-generated method stub
		MyGsonMap<Map,Dictionary> msm=MyGsonMap.getInstance(messageBody,Map.class, Dictionary.class); 
		Dictionary dictionary=msm.gson2T(Dictionary.class);
		dictionary.setCreateid(1);
		if(dictionaryDao.checkType(dictionary)>0)
			throw new RuntimeException("mkey:"+dictionary.getMkey()+",val:"+dictionary.getVal()+", exists!");
		dictionary.setCreateid(user.getId());
		dictionaryDao.save(dictionary);
		Map map=msm.gson2Map();
		Globalization glz=new Globalization();
		glz.setCreateid(user.getId());
		glz.setTableid(dictionary.getId().toString());
		glz.setName(map.get("name").toString());
		glz.setLanguage(map.get("language").toString());
		glz.setTablecolumn("name");
		glz.setTablename("dictionary");
		globalizationDao.save(glz);
	}
}
