package p.minn.privilege.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;








import org.springframework.web.bind.annotation.RequestParam;

import p.minn.common.utils.MyGsonMap;
import p.minn.common.utils.Page;
import p.minn.privilege.repository.CommonDao;
import p.minn.privilege.utils.Constant;
import p.minn.privilege.utils.QueryConfigUtil;
import p.minn.privilege.utils.Utils;

/**
 * @author minn
 * @QQ:3942986006
 *
 */
@Service
public class CommonService {

	@Autowired
	private ApplicationContext appContext;
	
	@Autowired
	private DictionaryService dictionaryService;
	
	@Autowired
	private CommonDao commonDao;
	
	/**
	 * @deprecated
	 * @param lang
	 * @return
	 * @throws Exception
	 */
	public Map<String,List<Map<String,Object>>> getQueryConfigDic(String lang,@RequestParam(required=false, defaultValue="web") String dic) throws Exception{

		Map<String, List<Map<String, Object>>>  dics=null;// dictionaryService.getDic(lang, "'SYSTEM'",dic);
		List<Map<String, Object>> sys=dics.get("SYSTEM");
	
		Map<String,Object> menu=QueryConfigUtil.getConfigQuery();
	    
	    for(Map<String, Object> m:sys){
	    	if(menu.get(m.get("id"))!=null){
	    		m.put("data", menu.get(m.get("id")));
	    		m.put("enable", true);
	    	}else{
	    		m.put("enable", false);
	    	}
	    }
	    
		return dics;
	}

	
	public Map<String,Object> getViewConfigByName(String lang,String atype,String name,String filename) throws Exception{
		Map<String,Object> data=QueryConfigUtil.getViewConfigByName(name, filename);
		//data.put("querydic",  dictionaryService.getDic(lang,data.get(Constant.MKEY).toString(),atype)) ;
		data.remove(Constant.MKEY);
		return data;
	}


	public  Map<String,Object> configQuery(String lang ,Page page,Map<String,String> param,String filename) throws Exception {
		Map<String,Object>  rs=null;
		try{
			SqlSession session=null;
			String name=param.get("name");
				Map<String,String> sqlparam=new HashMap<String,String>();
				String[] qkeys=param.get("qkey").split(Constant.SPLIT);
				String[] qvals=param.get("qval").split(Constant.SPLIT);
				for(int i=0;i<qkeys.length;i++){
						if(!qkeys[i].equals("")){
							sqlparam.put(qkeys[i], qvals[i]);
						}
				}
				QueryConfigUtil.completeWhereParam(name,sqlparam, filename);
				int total=0;
				if(param.get("autototalsql").equals("true")){
					total=commonDao.getTotal(QueryConfigUtil.getTotalSqlByName(name, sqlparam, filename));
				}else{
					session=getSessionByName(QueryConfigUtil.getDs(name,filename));
					
					total=(Integer) session.selectOne(QueryConfigUtil.getNs(name,filename)+"Total",sqlparam);
					
				}
				page.setTotal(total);
				List<Map<String,Object>> list=null;
				if(param.get("autosql").equals("true")){
					
					list= commonDao.query(QueryConfigUtil.getSqlByName(name, lang, sqlparam, page, filename));
				}else{
				   if(session==null){
					   session=getSessionByName(QueryConfigUtil.getDs(name,filename));
				   }
				   
				   sqlparam.put("startR", page.getStartR().toString());
				   sqlparam.put("endR", page.getEndR().toString());
				   sqlparam.put("lang", lang);
				   list=session.selectList(QueryConfigUtil.getNs(name,filename),sqlparam);
				}
				  if(session!=null){
					  session.close();
				  }
				 List<Map<String,Object>> rows=Utils.list2Grid(list,param.get("column").split(Constant.SPLIT));
		
				 rs=Utils.getResultMap(total,page,rows);
				 
				 
		}catch(Exception e){
			e.printStackTrace();
		}
		return rs;
	}
	
	private SqlSession getSessionByName(String name){
		SqlSessionTemplate sfb=appContext.getBean(name,SqlSessionTemplate.class);
		return sfb.getSqlSessionFactory().openSession();
	}


	public Object getViewConfigByName(String lang, String messageBody) throws Exception {
		// TODO Auto-generated method stub
		 Map map=(Map) Utils.gson2Map(messageBody);
		 Map<String,Object> data=QueryConfigUtil.getViewConfigByName(map.get("name").toString(),map.get("filename").toString());
		 data.put("querydic",  dictionaryService.getDic(lang,data.get(Constant.MKEY).toString())) ;
		 data.remove(Constant.MKEY);
		return data;
	}


	public Object configQuery(String lang, String messageBody) throws Exception {
		// TODO Auto-generated method stub
		MyGsonMap<Map,Page> msm=MyGsonMap.getInstance(messageBody,Map.class, Page.class); 
		Page page=msm.gson2T(Page.class);
		 Map param=(Map) msm.gson2Map();
		SqlSession session=null;
		String methodName=param.get("methodName").toString();
		String filename=param.get("fileName").toString();
		Map<String,String> sqlparam=Utils.getCondition(page);
		QueryConfigUtil.completeWhereParam(methodName,sqlparam, filename);
		int total=0;
		if(param.get("autototalsql").toString().equals("true")){
			total=commonDao.getTotal(QueryConfigUtil.getTotalSqlByName(methodName, sqlparam, filename));
		}else{
			session=getSessionByName(QueryConfigUtil.getDs(methodName,filename));
			total=(Integer) session.selectOne(QueryConfigUtil.getNs(methodName,filename)+"Total",sqlparam);
			
		}
		page.setPage(page.getPage()+1);
		page.setTotal(total);
		List<Map<String,Object>> list=null;
		if(param.get("autosql").toString().equals("true")){
			list= commonDao.query(QueryConfigUtil.getSqlByName(methodName, lang, sqlparam, page, filename));
		}else{
		   if(session==null){
			   session=getSessionByName(QueryConfigUtil.getDs(methodName,filename));
		   }
		   sqlparam.put("startR", page.getStartR().toString());
		   sqlparam.put("endR", page.getEndR().toString());
		   sqlparam.put("lang", lang);
		   list=session.selectList(QueryConfigUtil.getNs(methodName,filename),sqlparam);
		}
	    if(session!=null){
		  session.close();
	    }
		page.setResult(list);
		return page;
	}
}
