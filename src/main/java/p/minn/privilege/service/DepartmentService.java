package p.minn.privilege.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;





















import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import p.minn.common.utils.MyGsonMap;
import p.minn.common.utils.Page;
import p.minn.common.utils.UtilCommon;
import p.minn.oauth.vo.User;
import p.minn.privilege.entity.Department;
import p.minn.privilege.entity.Dictionary;
import p.minn.privilege.entity.Globalization;
import p.minn.privilege.entity.IdEntity;
import p.minn.privilege.entity.Menu;
import p.minn.privilege.entity.Account;
import p.minn.privilege.repository.DepartmentDao;
import p.minn.privilege.repository.GlobalizationDao;
import p.minn.privilege.utils.Constant;
import p.minn.privilege.utils.Utils;
import p.minn.vo.MyUserDetails;

/**
 * 
 * @author minn
 * @QQ:3942986006
 * @comment 
 *
 */
@Service
public class DepartmentService {

	@Autowired
	private DepartmentDao departmentDao;
	 
	@Autowired
	private GlobalizationDao globalizationDao;
	
	
	/**
	 * 获取菜单
	 */
	public List<Map<String,Object>> getResource(User user,String lang) throws Exception{

		List<Map<String,Object>> list=departmentDao.getResource(lang);

		return Utils.createTreeMenu(list,"-2");
	}
	
	
	
	public Map<String,Object> checkCode(String code,String type) throws Exception{
		
		Map<String,Object>  rs=new HashMap<String, Object>();
		int c=departmentDao.checkCode(code);
		rs.put("count", c);
		
		return rs;
	}


	public Object query(String messageBody, String lang) {
		// TODO Auto-generated method stub
		Page page=(Page) Utils.gson2T(messageBody, Page.class);
		Map<String,String> condition=Utils.getCondition(page);
		int total=departmentDao.getTotal(lang,condition);
		page.setPage(page.getPage()+1);
		page.setTotal(total);
		
		List<Map<String,Object>> list=departmentDao.query(lang,page,condition);
	    page.setResult(list);
	
		return page;
	}


	public void update(User user,String messageBody, String lang) {
		// TODO Auto-generated method stub
		MyGsonMap<Map,Department> msm=MyGsonMap.getInstance(messageBody,Map.class, Department.class); 
		Department department=msm.gson2T(Department.class);
		Map map=msm.gson2Map();
		department.setUpdateid(user.getId());
		departmentDao.update(department);
		Globalization glz=new Globalization();
		glz.setUpdateid(user.getId());
		glz.setId(Double.valueOf(map.get("gid").toString()).intValue());
		glz.setName(map.get("name").toString());
		glz.setLanguage(map.get("language").toString());
		globalizationDao.update(glz);
	}


	public void save(User user,String messageBody, String lang) {
		// TODO Auto-generated method stub
		MyGsonMap<Map,Department> msm=MyGsonMap.getInstance(messageBody,Map.class, Department.class); 
		Department department=msm.gson2T(Department.class);
		department.setCreateid(user.getId());
		if(departmentDao.checkCode(department.getCode())>0)
			throw new RuntimeException("code:"+department.getCode()+", exists!");
		departmentDao.save(department);
		Map map=msm.gson2Map();
		Globalization glz=new Globalization();
		glz.setTableid(department.getId().toString());
		glz.setCreateid(user.getId());
		glz.setName(map.get("name").toString());
		glz.setLanguage(map.get("language").toString());
		glz.setTablecolumn("name");
		glz.setTablename("department");
		globalizationDao.save(glz);
	}


	public void delete(String messageBody) {
		// TODO Auto-generated method stub
		IdEntity idEntity=(IdEntity) Utils.gson2T(messageBody,IdEntity.class);
		globalizationDao.deleteByTableId(idEntity.getId().toString(),"resource");
		departmentDao.delete(idEntity);
	}
}
