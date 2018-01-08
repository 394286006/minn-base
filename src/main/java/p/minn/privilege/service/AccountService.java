package p.minn.privilege.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;















import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import p.minn.common.utils.LogArrayList;
import p.minn.common.utils.MyGsonMap;
import p.minn.common.utils.Page;
import p.minn.privilege.entity.AccountThirdPart;
import p.minn.privilege.entity.Department;
import p.minn.privilege.entity.IdEntity;
import p.minn.privilege.entity.Account;
import p.minn.privilege.entity.AccountRole;
import p.minn.privilege.repository.AccountDao;
import p.minn.privilege.repository.AccountThirdPartDao;
import p.minn.privilege.repository.DepartmentDao;
import p.minn.privilege.repository.RoleDao;
import p.minn.privilege.utils.Utils;
import p.minn.security.cas.springsecurity.auth.MyPasswordEncoder;
import p.minn.security.cas.springsecurity.auth.User;
import p.minn.security.service.IAccountService;

/**
 * 
 * @author minn
 * @QQ:3942986006
 * @comment 业务层
 */
@Service
public class AccountService implements IAccountService{

	@Autowired
	private AccountDao accountDao;
	
	@Autowired
    private DepartmentDao departmentDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
    private AccountThirdPartDao accountThirdPartDao;
	
	//@Autowired
	 private MyPasswordEncoder passwordEncoder;
	 
	/**
	 * 查找角色资源
	 * @param m
	 * @return
	 */
	public List<Map<String,Object>> getAccountRole( String lang, String messageBody) throws Exception{
		List<Map<String,Object>> list=null;
		Page page=(Page) Utils.gson2T(messageBody, Page.class);
		Map<String,String> condition=Utils.getCondition(page);
		list=accountDao.getAccountRole (lang,condition);
		List<Map<String,Object>> target=Utils.createTreeMenu(list,"-2");
		list=target;

		return list;
	}
	
	/**
	 * 登录验证
	 * @param name
	 * @param pwd
	 * @return
	 */
	public Account login(String name,String pwd){
		return accountDao.checkAccount(name, pwd);
		
	}
	
	public Account findAccountByLoginName(String loginName) {
		return accountDao.findByLoginName(loginName);
	}

	public String getCurrentAccountName() {
		Account account = (Account) SecurityUtils.getSubject().getPrincipal();
		return account.getName();
	}

	

	public List<String> getRoleListByAccountId(Integer accountid) {
		// TODO Auto-generated method stub
		return accountDao.getAccountRoleList(accountid);
	}

	public List<String> getRoleRealmListByAccountId(Integer accountid) {
		// TODO Auto-generated method stub
		return accountDao.getAccountRoleRealmList(accountid);
	}

	public Account findAccountByLoginName(String loginName, String password) {
		// TODO Auto-generated method stub
		return accountDao.checkAccount(loginName, password);
	}

	public Page query(String messageBody, String lang) {
		// TODO Auto-generated method stub
		Page page=(Page) Utils.gson2T(messageBody, Page.class);
		Map<String,String> condition=Utils.getCondition(page);
		int total=accountDao.getTotal(lang,condition);
		page.setPage(page.getPage()+1);
		page.setTotal(total);
		
		List<Map<String,Object>> list=null;
		if(total>=0){
			list=accountDao.query(lang,page,condition);
		}else{
			list=new ArrayList<Map<String, Object>>();
		}
			
	    page.setResult(list);
	    return page;
	}

	public void update(User user,String messageBody, String lang) {
		// TODO Auto-generated method stub
		Account account =(Account) Utils.gson2T(messageBody, Account.class);
		Account lastaccount=accountDao.getAccountById(account);
		if(lastaccount==null){
			throw new RuntimeException("user not found:"+account.getName());
		}
		account.setUpdateid(user.getId());
		account.setPwd(Utils.getPwd(account.getPwd()));
		accountDao.update(account);
	}

	public void save(User user,String messageBody, String lang) {
		// TODO Auto-generated method stub
		MyGsonMap<Map,Account> msm=MyGsonMap.getInstance(messageBody,Map.class, Account.class); 
		Account account=msm.gson2T(Account.class);
		account.setCreateid(1);
		passwordEncoder=new MyPasswordEncoder();
		account.setPwd(passwordEncoder.encode(account.getPwd()));
		account.setCreateid(user.getId());
		if(accountDao.findByLoginName(account.getName())!=null)
			throw new RuntimeException("code:"+account.getName()+", exists!");
		accountDao.save(account);
	}

	public void delete(String messageBody) {
		// TODO Auto-generated method stub
		IdEntity idEntity=(IdEntity) Utils.gson2T(messageBody,IdEntity.class);
		accountDao.delete(idEntity);
	}

	public void saveAccountRole(String messageBody) {
		// TODO Auto-generated method stub
		 Map map=(Map) Utils.gson2T(messageBody,Map.class);
		 String accountid=map.get("accountid").toString();
		 String roleids=map.get("roleids").toString();
	     if(StringUtils.isNotEmpty(roleids)){
	   	     List<AccountRole> uss=new LogArrayList<AccountRole>();
	   	     String[] resourceids=roleids.split(",");
	    	 for(String rid:resourceids){
	    		 uss.add(new AccountRole(accountid,rid));
			   }
			 accountDao.delAccountRole(accountid);
			 accountDao.saveAccountRole(uss);
	    }
	}

  public List<Department> getDepartmentByAcountId(Integer accountid) {
    // TODO Auto-generated method stub
    return departmentDao.getDepartmentByAcountId(accountid);
  }

  @Override
  public Account findAccountByRandomKey(String randomKey) {
    // TODO Auto-generated method stub
    return accountDao.findByRandomKey(randomKey);
  }

  @Override
  public void updateKey(String name, String randomKey) {
    // TODO Auto-generated method stub
    accountDao.updateKey(name, randomKey);
    
  }

  @Override
  public boolean checkQrCodeByRandomKey(String randomKey) {
    // TODO Auto-generated method stub
    return accountDao.checkQrCodeByRandomKey(randomKey)==1;
  }

  @Override
  public Account findAccountByThirdPart(String name,String secretkey) {
    // TODO Auto-generated method stub
    AccountThirdPart atp=accountThirdPartDao.findByName(name);
    if(atp==null){
      return null;
    }else if(atp.getSecretkey().equals(secretkey)){
      return accountDao.findAccountById(atp.getAccountId());
    }else{
      return null;
    }
   
  }

}
