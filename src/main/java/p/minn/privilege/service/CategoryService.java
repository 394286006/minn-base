package p.minn.privilege.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import p.minn.common.entity.Globalization;
import p.minn.common.entity.IdEntity;
import p.minn.common.repository.GlobalizationDao;
import p.minn.common.utils.MyGsonMap;
import p.minn.common.utils.Page;
import p.minn.privilege.entity.Category;
import p.minn.privilege.repository.CategoryDao;
import p.minn.privilege.utils.Utils;
import p.minn.vo.User;

/**
 * 
 * @author minn
 * @QQ:3942986006
 *
 */
@Service
public class CategoryService {

	@Autowired
	private CategoryDao categoryDao;
	 
	@Autowired
	private GlobalizationDao globalizationDao;
	
	
	public List<Map<String,Object>> getCategory(String lang) throws Exception{

		List<Map<String,Object>> list=categoryDao.getCategory(lang);
		List<Map<String,Object>> target=createTreeMenu(list,"-2","");
		return target;
	}
	
	private List<Map<String,Object>> createTreeMenu(List<Map<String,Object>> source,String parent,String pname){
		List<Map<String,Object>> children=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map:source){
			if(map.get("pid").toString().equals(parent)){
				map.put("pnode", pname);
				map.put("children", createTreeMenu(source,map.get("id").toString(),map.get("text").toString()));
				children.add(map);
			}
		}
		return children;
	}
	
	

	public Object query(String messageBody, String lang) {
		// TODO Auto-generated method stub
		Page page=(Page) Utils.gson2T(messageBody, Page.class);
		Map<String,String> condition=Utils.getCondition(page);
		StringBuilder sql=new StringBuilder();
		if(condition!=null){
			if(StringUtils.isNotEmpty(condition.get("nodeid"))){
				sql.append(" and pid='"+condition.get("nodeid")+"'");
			}
			if(StringUtils.isNotEmpty(condition.get("name"))){
				sql.append(" and exists(select * from v_globalization_dictionary where tablename='resource' and language='"+lang+"' and name like '"+condition.get("name")+"%' and (tableid=resource.pid or tableid=resource.id))");
			}
			if(StringUtils.isNotEmpty(condition.get("resource"))){
				sql.append(" and type="+condition.get("resource"));
			}
			if(StringUtils.isNotEmpty(condition.get("resourceurltype"))){
				sql.append(" and urltype="+condition.get("resourceurltype"));
			}
		}
		int total=categoryDao.getTotal(lang,condition);
		page.setPage(page.getPage()+1);
		page.setTotal(total);
		
		List<Map<String,Object>> list=categoryDao.query(lang,page,condition);
	    page.setResult(list);
	
		return page;
	}


	public void update(User user,String messageBody, String lang) {
		// TODO Auto-generated method stub
		MyGsonMap<Map,Category> msm=MyGsonMap.getInstance(messageBody,Map.class, Category.class); 
		Category category=msm.gson2T(Category.class);
		if(category.getId().intValue()<0){
			throw new RuntimeException("根节点不能被修改!");
		}
		Map map=msm.gson2Map();
		category.setUpdateid(user.getId());
		categoryDao.update(category);
		Globalization glz=new Globalization();
		glz.setId(Double.valueOf(map.get("gid").toString()).intValue());
		glz.setName(map.get("name").toString());
		glz.setUpdateid(user.getId());
		glz.setLanguage(map.get("language").toString());
		globalizationDao.update(glz);
		 glz=new Globalization();
		 glz.setUpdateid(user.getId());
		glz.setId(Double.valueOf(map.get("gcommentid").toString()).intValue());
		glz.setName(map.get("comment").toString());
		glz.setLanguage(map.get("language").toString());
		globalizationDao.update(glz);
	}


	public void save(User user,String messageBody, String lang) {
		// TODO Auto-generated method stub
		MyGsonMap<Map,Category> msm=MyGsonMap.getInstance(messageBody,Map.class, Category.class); 
		Category category=msm.gson2T(Category.class);
		Map map=msm.gson2Map();
		category.setCreateid(1);
		if(categoryDao.checkName(lang,map.get("name").toString())>0)
			throw new RuntimeException("name:"+map.get("name").toString()+", exists!");
		category.setCreateid(user.getId());
		categoryDao.save(category);
		Globalization glz=new Globalization();
		glz.setTableid(category.getId().toString());
		glz.setName(map.get("name").toString());
		glz.setCreateid(user.getId());
		glz.setLanguage(map.get("language").toString());
		glz.setTablename("category");
		glz.setTablecolumn("name");
		globalizationDao.save(glz);
		 glz=new Globalization();
		glz.setTableid(category.getId().toString());
		glz.setName(map.get("comment").toString());
		glz.setCreateid(user.getId());
		glz.setLanguage(map.get("language").toString());
		glz.setTablename("category");
		glz.setTablecolumn("comment");
		globalizationDao.save(glz);
	}


	public void delete(String messageBody) {
		// TODO Auto-generated method stub
		IdEntity idEntity=(IdEntity) Utils.gson2T(messageBody,IdEntity.class);
		globalizationDao.deleteByTableId(idEntity.getId().toString(),"category");
		categoryDao.delete(idEntity);
	}
}
