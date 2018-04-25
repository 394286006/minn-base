package p.minn.privilege.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




















import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import p.minn.common.entity.Globalization;
import p.minn.common.entity.IdEntity;
import p.minn.common.repository.GlobalizationDao;
import p.minn.common.utils.ConstantCommon;
import p.minn.common.utils.MyGsonMap;
import p.minn.common.utils.Page;
import p.minn.privilege.entity.Dictionary;
import p.minn.privilege.entity.Menu;
import p.minn.privilege.repository.MenuDao;
import p.minn.privilege.utils.Constant;
import p.minn.privilege.utils.Utils;
import p.minn.vo.User;
import p.minn.vo.MyUserDetails;

/**
 * 
 * @author minn
 * @QQ:3942986006
 * @comment 菜单业务管理
 *
 */
@Service
public class MenuService {

	@Autowired
	private MenuDao menuDao;
	 
	@Autowired
	private GlobalizationDao globalizationDao;
	
	/**
	 * 获取菜单
	 * @throws Exception 
	 */
	public List<Map<String,Object>> getPrivateMenu(User user,String lang) throws Exception{
		
		List<Map<String,Object>> list=menuDao.getPrivateMenu(lang,user);
		List<Map<String,Object>> target=createTreeMenu(list,"-1");
		list=target;
		return list;
	}
	
	
	/**
	 * 获取菜单
	 */
	public List<Map<String,Object>> getResource(User user,String lang) throws Exception{

		List<Map<String,Object>> list=menuDao.getResource(lang);
			List<Map<String,Object>> target=createTreeMenu(list,"-2");
			list=target;
		return list;
	}
	
	private List<Map<String,Object>> createTreeMenu(List<Map<String,Object>> source,String parent){
		List<Map<String,Object>> children=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map:source){
			if(!map.containsKey("icon")) {
				map.put("icon", ConstantCommon.DEFAULT_ICON);
			}
			if(map.get("pid").toString().equals(parent)){
				if(map.get("type_v")!=null&&map.get("type_v").toString().equals("-1"))
				map.put("children", createTreeMenu(source,map.get("id").toString()));
				children.add(map);
			}
		}
		return children;
	}
	
	public Map<String,Object> checkCode(String code,String type) throws Exception{
		
		Map<String,Object>  rs=new HashMap<String, Object>();
		int c=menuDao.checkCode(code);
		rs.put("count", c);
		
		return rs;
	}


	@Cacheable("menuResult")
	public Object query(String messageBody, String lang) {
		// TODO Auto-generated method stub
		Page page=(Page) Utils.gson2T(messageBody, Page.class);
		Map<String,String> condition=Utils.getCondition(page);
		int total=menuDao.getTotal(lang,condition);
		page.setPage(page.getPage()+1);
		page.setTotal(total);
		
		List<Map<String,Object>> list=menuDao.query(lang,page,condition);
	    page.setResult(list);
	
		return page;
	}


	@CacheEvict(value="menuResult",allEntries=true)
	public void update(User user,String messageBody, String lang) {
		// TODO Auto-generated method stub
		MyGsonMap<Map,Menu> msm=MyGsonMap.getInstance(messageBody,Map.class, Menu.class); 
		Menu menu=msm.gson2T(Menu.class);
		Map map=msm.gson2Map();
		menu.setUpdateid(user.getId());
		menuDao.update(menu);
		Globalization glz=new Globalization();
		glz.setUpdateid(user.getId());
		glz.setId(Double.valueOf(map.get("gid").toString()).intValue());
		glz.setName(map.get("name").toString());
		glz.setLanguage(map.get("language").toString());
		globalizationDao.update(glz);
	}

	@CacheEvict(value="menuResult",allEntries=true)
	public void save(User user,String messageBody, String lang) {
		// TODO Auto-generated method stub
		MyGsonMap<Map,Menu> msm=MyGsonMap.getInstance(messageBody,Map.class, Menu.class); 
		Menu menu=msm.gson2T(Menu.class);
		menu.setCreateid(user.getId());
		if(menuDao.checkCode(menu.getCode())>0)
			throw new RuntimeException("code:"+menu.getCode()+", exists!");
		menuDao.save(menu);
		Map map=msm.gson2Map();
		Globalization glz=new Globalization();
		glz.setTableid(menu.getId().toString());
		glz.setCreateid(user.getId());
		glz.setName(map.get("name").toString());
		glz.setLanguage(map.get("language").toString());
		glz.setTablecolumn("name");
		glz.setTablename("resource");
		globalizationDao.save(glz);
	}


	@CacheEvict(value="menuResult",allEntries=true)
	public void delete(String messageBody) {
		// TODO Auto-generated method stub
		IdEntity idEntity=(IdEntity) Utils.gson2T(messageBody,IdEntity.class);
		globalizationDao.deleteByTableId(idEntity.getId().toString(),"resource");
		menuDao.delete(idEntity);
	}
}
