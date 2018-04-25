package p.minn.privilege.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import p.minn.common.entity.IdEntity;
import p.minn.common.utils.Page;
import p.minn.privilege.repository.LogDao;
import p.minn.privilege.utils.Constant;
import p.minn.privilege.utils.Utils;

/**
 * 
 * @author minn
 * @QQ:3942986006
 * @comment 操作日志
 *
 */
@Service
public class LogService {

	@Autowired
	private LogDao logDao;

	/**
	 * 查询操作详细日志
	 * @param lang
	 * @return
	 * @throws Exception 
	 */
	public Map<String,Object> getDetail(String lang,String column,String id) throws Exception{

		List<Map<String,Object>> list=logDao.getLogChangeDetail(lang,id);
		for(int i=0;i<list.size();i++){
			Map<String,Object> obj=list.get(i);
			obj.put("id", i);
			obj.put("k_name",Utils.getResourceName(obj.get("k")));
		}
		
		List<Map<String,Object>> rows=Utils.list2Grid(list,column.split(Constant.SPLIT));

		Map<String,Object> rs=Utils.getResultMap(rows);
	
		return rs;
	}

	public Map<String,List<Map<String,Object>>> getSignature(String lang) {
		
		List<Map<String,Object>> list=logDao.getSignature(lang);
	    
	    Map<String,List<Map<String,Object>>> keys=new HashMap<String, List<Map<String,Object>>>();
	    
	    List<Map<String,Object>> v=null;
	    
	    for(Map<String,Object> m:list){
	    	String k=m.get("mkey").toString();
	    	Object txt=m.get("text");
	    	if(txt==null){
	    		continue;
	    	}
	    	if(txt.toString().equals("")){
    			m.put("text", Utils.getResourceName(m.get("id")));
    		}
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

	public Page query(String messageBody, String lang) {
		// TODO Auto-generated method stub
		Page page=(Page) Utils.gson2T(messageBody, Page.class);
		Map<String,String> condition=Utils.getCondition(page);
		int total=logDao.getTotal(lang,condition);
		page.setPage(page.getPage()+1);
		page.setTotal(total);
		
		List<Map<String,Object>> list=logDao.query(lang,page,condition);
		for(Map<String,Object> obj:list){
			   obj.put("tablekey_name",Utils.getResourceName(obj.get("tablekey")));
			   obj.put("signature_name",Utils.getResourceName(obj.get("signature")));
		}
		

		page.setResult(list);
		return page;
	}

	public List<Map<String,Object>> getDetail(String lang, String messageBody) {
		// TODO Auto-generated method stub
		 Map map=(Map) Utils.gson2T(messageBody,Map.class);
		 List<Map<String,Object>> list=logDao.getLogChangeDetail(lang,map.get("id").toString());
			for(int i=0;i<list.size();i++){
				Map<String,Object> obj=list.get(i);
				obj.put("id", i);
				obj.put("k_name",Utils.getResourceName(obj.get("k")));
			}
		return list;
	}

	public void delete(String messageBody) {
		// TODO Auto-generated method stub
		IdEntity idEntity=(IdEntity) Utils.gson2T(messageBody,IdEntity.class);
		logDao.delete(idEntity);
		logDao.deleteDetail(idEntity);
	}
}
