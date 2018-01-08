package p.minn.privilege.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import p.minn.common.utils.LogArrayList;
import p.minn.common.utils.MyGsonMap;
import p.minn.common.utils.Page;
import p.minn.privilege.entity.Globalization;
import p.minn.privilege.entity.IdEntity;
import p.minn.privilege.entity.Menu;
import p.minn.privilege.entity.Role;
import p.minn.privilege.entity.RoleMenu;
import p.minn.privilege.repository.GlobalizationDao;
import p.minn.privilege.repository.RoleDao;
import p.minn.privilege.utils.Constant;
import p.minn.privilege.utils.Utils;
import p.minn.security.cas.springsecurity.auth.User;

/**
 * 
 * @author minn
 * @QQ:3942986006
 * @comment 角色业务
 *
 */
@Service
public class RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private GlobalizationDao globalizationDao;
	/**
	 * 查找角色资源
	 * @param m
	 * @return
	 */
	public List<Map<String,Object>> getRoleResource(String lang,String messageBody) throws Exception{
		
		List<Map<String,Object>> list=null;

			Page page=(Page) Utils.gson2T(messageBody, Page.class);
			Map<String,String> condition=Utils.getCondition(page);
			list=roleDao.getRoleResource(lang,condition.get("roleid").toString());
			List<Map<String,Object>> target=Utils.createTreeMenu(list,"-2");
			list=target;
		

		return list;
	}

	public Object query(String messageBody, String lang) {
		// TODO Auto-generated method stub
		Page page=(Page) Utils.gson2T(messageBody, Page.class);
		Map<String,String> condition=Utils.getCondition(page);
		StringBuilder sql=new StringBuilder();
		if(condition!=null){
			if(StringUtils.isNotEmpty(condition.get("active"))){
				sql.append(" and active="+condition.get("active"));
			}
			if(StringUtils.isNotEmpty(condition.get("name"))){
				sql.append(" and exists(select * from globalization where tablename='role' and language='"+lang+"' and name like '"+condition.get("name")+"%' and tableid=role.id)");
			}
		}
		int total=roleDao.getTotal(lang,condition);
		page.setPage(page.getPage()+1);
		page.setTotal(total);
		
		List<Map<String,Object>> list=null;
		if(total>=0){
			list=roleDao.query(lang,page,condition);
		}else{
			list=new ArrayList<Map<String, Object>>();
		}
			
	    page.setResult(list);
	    return page;
	}

	public void update(User user,String messageBody, String lang) {
		// TODO Auto-generated method stub
		MyGsonMap<Map,Role> msm=MyGsonMap.getInstance(messageBody,Map.class, Role.class); 
		Role role=msm.gson2T(Role.class);
		Map map=msm.gson2Map();
		role.setUpdateid(user.getId());
		roleDao.update(role);
		Globalization glz=new Globalization();
		glz.setUpdateid(user.getId());
		glz.setId(Double.valueOf(map.get("gid").toString()).intValue());
		glz.setName(map.get("name").toString());
		glz.setLanguage(map.get("language").toString());
		globalizationDao.update(glz);
	}

	public void save(User user,String messageBody, String lang) {
		// TODO Auto-generated method stub
		MyGsonMap<Map,Role> msm=MyGsonMap.getInstance(messageBody,Map.class, Role.class); 
		Role role=msm.gson2T(Role.class);
		role.setCreateid(user.getId());
		if(roleDao.checkCode(role.getCode())>0)
			throw new RuntimeException("code:"+role.getCode()+", exists!");
		roleDao.save(role);
		Map map=msm.gson2Map();
		Globalization glz=new Globalization();
		glz.setTableid(role.getId().toString());
		glz.setCreateid(user.getId());
		glz.setName(map.get("name").toString());
		glz.setLanguage(map.get("language").toString());
		glz.setTablename("role");
		globalizationDao.save(glz);
	}

	public void delete(String messageBody) {
		// TODO Auto-generated method stub
		IdEntity idEntity=(IdEntity) Utils.gson2T(messageBody,IdEntity.class);
		globalizationDao.deleteByTableId(idEntity.getId(),"role");
		roleDao.delete(idEntity);
	}

	public void saveRoleResource(String messageBody) {
		// TODO Auto-generated method stub
	
		 Map map=(Map) Utils.gson2T(messageBody,Map.class);
		 String roleid=map.get("roleid").toString();
		 String rids=map.get("resourceids").toString();
	     if(StringUtils.isNotEmpty(rids)){
	   	     List<RoleMenu> rms=new LogArrayList<RoleMenu>();
	   	     String[] resourceids=rids.split(",");
	    	 for(String rid:resourceids){
				   rms.add(new RoleMenu(roleid,rid));
			   }
	    	 roleDao.delRoleResource(roleid);
			 roleDao.saveRoleResource(rms);
	    }
		  
		  
	}
	
}
