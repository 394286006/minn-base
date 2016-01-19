package p.minn.privilege.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import p.minn.common.utils.Page;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author minn
 * @QQ:3942986006
 * @comment 公共查询模块解析器
 */
@SuppressWarnings("unchecked")
public class QueryConfigUtil {

	public static final String UNKNOWN="unknown";
	
	private static final String ID="id";
	
	private static final String AUTOSQL="autosql";
	
	private static final String AUTOTOTALSQL="autototalsql";
	
	private static final String TABLE="table";
	
	private static final String RCOLUMN="rcolumn";
	
	private static final String GSOURCE="gsource";
	
	private static final String COLUMN="column";
	
	private static final String WIDTH="width";
	
	private static final String HIDDEN="hide";
	
	private static final String TYPE="type";
	
	private static final String CTYPE="ctype";
	
	private static final String DEFAULT_DS="privilege";
	
	private static final String DEFAULT_NS="p.minn.admin.privilege.repository.CommonDao";
	
	private static final String DS="ds";
	
	private static final String NS="ns";
	
	private static final String SLASH="/";
	
	private static final String TEXT="text";
	
	private static final String DEFAULT_PREFIX="ds/configquery/";
	
	private static final String DEFAULT_SUBFIX=".json";
	
	private static final String DEFAULT_FILE=DEFAULT_PREFIX+"privilege"+DEFAULT_SUBFIX;
	
	private static final String DEFAULT_CONFIG_FILE=DEFAULT_PREFIX+"config.json";
	
	private static final StringBuffer FNAME=new StringBuffer();
	
	public static String getTotalSqlByName(String name,Map<String,String> param,String filename) throws Exception{
		
		  StringBuffer sql=new StringBuffer();
		  JsonNode rn=getQueryConfigDs(name,filename);
		    
		  boolean autototalsql=true;
		  try{
			  autototalsql=  rn.get(AUTOTOTALSQL).asBoolean();
		  }catch(RuntimeException re){
			  autototalsql=true;
		  }
		    String table=null;
		    if(autototalsql){
		    	table=rn.get(TABLE).asText();
		    }
		    
		    sql.append("select count(*) as c");
		
		    sql.append(" from ");
		    sql.append(table);
		    sql.append(" where 1=1 ");
		    StringBuffer wheresql=getWhereSql(rn,param, filename);
		    sql.append(wheresql);
		  return sql.toString();
	}
	
	public static void completeWhereParam(String name,Map<String,String> param,String filename) throws Exception{
		 JsonNode rn=getQueryConfigDs(name,filename);
		 JsonNode querykey=rn.get("querykey");
		    Iterator<JsonNode> pk=querykey.elements();
		    while(pk.hasNext()){
		    	JsonNode k=pk.next();
		    	if(!param.containsKey(k.get(ID).asText())){
		    		param.put(k.get(ID).asText(), UNKNOWN);
		    	}
		    	
		    }
	}
	
	public static StringBuffer getWhereSql(JsonNode rn,Map<String,String> param,String filename){
		  StringBuffer wheresql=new StringBuffer();
		    JsonNode wherekey=rn.get("wherekey");
		    Iterator<String> pk=param.keySet().iterator();
		    while(pk.hasNext()){
		    	String k=pk.next();
		    	Iterator<JsonNode> it=wherekey.get(k).elements();
		    	if(param.get(k).equals(UNKNOWN)){
		    		continue;
		    	}
		    	wheresql.append(" and ");
		    	wheresql.append("(");
		    	
		    	int idx1=0;
		    	while(it.hasNext()){
		    		JsonNode tmp=it.next();
		    		if(idx1>0){
		    			wheresql.append(" or ");
		    		}
		    		if(tmp.get(TYPE)==null||"string".equalsIgnoreCase(tmp.get(TYPE).asText())){
		    			wheresql.append(tmp.get("val").asText());
		    			wheresql.append( " like '");
		    			wheresql.append(param.get(k));
		    			wheresql.append("%'");
		    		}
		    		if(tmp.get(TYPE)!=null&&"number".equalsIgnoreCase(tmp.get(TYPE).asText())){
		    			wheresql.append(tmp.get("val").asText());
		    			wheresql.append("=");
		    			wheresql.append(param.get(k));
		    		}
		    		idx1++;
		    		
		    	}
		    	wheresql.append(")");
		    }
		    return wheresql;
	}
	
	
	
	public static String getSqlByName(String name,String lang,Map<String,String> param,Page page,String filename) throws Exception{
		  
		StringBuffer sql=new StringBuffer();
	    JsonNode rn=getQueryConfigDs(name,filename);
	    
	    boolean autosql=true;
	    try{
	    	autosql=rn.get(AUTOSQL).asBoolean();
	    }catch(RuntimeException re){
	    	autosql=true;
	    }
	    String table=null;
	    if(autosql){
	    	table=rn.get(TABLE).asText();
	    }
	    
	    Iterator<JsonNode> ns=rn.get("gridkey").elements();
	  
	    sql.append("select ");
	    int idx=0;
	    while(ns.hasNext()){
	    	
	    	JsonNode n=ns.next();
	    	if(idx>0){
	    		sql.append(Constant.SPLIT);
	    	}
	    	if(n.get(Constant.MKEY)!=null){
	    		sql.append("CAST((select max(name)");
	    		sql.append(	"from v_dictionary  where mkey='");
	    		sql.append(n.get("mkey").textValue());
	    	    sql.append("' and val=");
	    		sql.append(n.get(RCOLUMN).textValue());
	    		sql.append(") as char) as ");
	    		sql.append(n.get(COLUMN).textValue());
	    	}else if(n.get(GSOURCE)!=null){
	    		sql.append("CAST((select max(name)");
	    		sql.append(	" from globalization  where tablename='");
	    		sql.append(n.get("tablename").textValue());
	    	    sql.append("' and tableid=");
	    		sql.append(n.get("tablename").textValue());
	    		sql.append(".id and language='"+lang+"') as char) as ");
	    		sql.append(n.get(COLUMN).textValue());
	    	}else{
	    		if(n.get(RCOLUMN)!=null){
	    			sql.append("CAST(");
		    		sql.append(n.get(RCOLUMN).textValue());
		    		sql.append(" as char)");
		    		sql.append(" as ");
		    		sql.append(n.get(COLUMN).textValue());
	    		}else{
	    			sql.append(n.get(COLUMN).textValue());
	    		}
	    	}
	    		
	    	idx++;
	    }
	
	    sql.append(" from ");
	    sql.append(table);
	    sql.append(" where 1=1 ");
	    
	    StringBuffer wheresql=getWhereSql(rn,param, filename);
	  
	    sql.append(wheresql);
	    
	    sql.append(" limit ");
	    sql.append(page.getStartR());
	    sql.append(Constant.SPLIT);
	    sql.append(page.getEndR());
	    
	    
		return sql.toString();
	}
	
	
	
	
	public static Map<String,Object> getViewConfigByName(String name,String filename) throws Exception{
		   
		   Map<String,Object> root=getQueryConfigDs(filename);
		   root=(Map<String,Object>) root.get(name);
		   if(!root.containsKey(AUTOTOTALSQL)){
			   root.put(AUTOTOTALSQL, true);
		   }
		   if(!root.containsKey(AUTOSQL)){
			   root.put(AUTOSQL, true);
		   }
		   root.remove(TABLE);
		   root.remove("wherekey");
		   root.remove(DS);
		   root.remove(NS);
		   List<Map<String,Object>> list= (List<Map<String,Object>>) root.get("querykey");
		   StringBuffer mkey=new StringBuffer();
		   Map<String,Object> keys=new HashMap<String,Object>();
		   for(Map<String,Object> m:list){
			   m.put(m.get(Constant.RESOURCEKEY).toString(), Utils.getResourceName(m.get(Constant.RESOURCEKEY)));
			   if(m.get(CTYPE)!=null&&"select".equals(m.get(CTYPE).toString())){
				   keys.put(m.get(Constant.RESOURCEKEY).toString(), m);
			   }else{
				   m.put(CTYPE, TEXT);
			   }
			  
		   }
		   
		   list= (List<Map<String,Object>>) root.get("gridkey");
		   int i=0;
		   for(Map<String,Object> m:list){
			   setStyle(m);
			   
			   m.put(m.get(Constant.RESOURCEKEY).toString(), Utils.getResourceName(m.get(Constant.RESOURCEKEY)));
			   if(m.get(Constant.MKEY)!=null){
				   if(i>0){
					   mkey.append(Constant.SPLIT);
				   }
				   mkey.append("'");
				   mkey.append(m.get(Constant.MKEY));
				   mkey.append("'");
				   if(keys.containsKey(m.get(Constant.RESOURCEKEY).toString())){
					   Map<String,Object> tmp= (Map<String, Object>) keys.get(m.get(Constant.RESOURCEKEY).toString());
					   tmp.put(Constant.MKEY, m.get(Constant.MKEY));
				   }
				  
				   i++;
				   
			   }
		   }
           
		   root.put(Constant.MKEY, mkey.toString());
           return root;
	}
	
	private static void setStyle(Map<String,Object> m){
		 if(!m.containsKey(WIDTH)){
			   m.put(WIDTH, "120");
		   }
		   if(!m.containsKey(HIDDEN)){
			   m.put(HIDDEN, false);
		   }
	}
	
	
	private static String getCurrentFile(){
		return QueryConfigUtil.class.getResource(SLASH).getFile();
	}
	
	public static File getDsFile(String filename){
	
		FNAME.delete(0, FNAME.length());
		FNAME.append(getCurrentFile());
		if(filename==null||filename.equals("")){
			FNAME.append(DEFAULT_FILE);
		}else{
			FNAME.append(DEFAULT_PREFIX);
			FNAME.append(filename);
			FNAME.append(DEFAULT_SUBFIX);
		}
		File file=new File(FNAME.toString());
		return file;
	}
	
	
	public static Map<String,Object>  getConfigQuery() throws Exception{
		
		 ObjectMapper mapper=new ObjectMapper();
		 File file=new File(getCurrentFile()+DEFAULT_CONFIG_FILE);
		 Map<String,Object> root=mapper.readValue(file, Map.class);
		 Iterator<String> it= root.keySet().iterator();
	     
		    while(it.hasNext()){
		    	String k=it.next();
		    	List<Map<String,Object>> vs=(List<Map<String, Object>>) root.get(k);
		    	for(Map<String,Object> v:vs){
		    		if(!v.containsKey("enable")){
		    			v.put("enable", true);
		    		}
			    	v.put(TEXT, Utils.getResourceName(v.get(Constant.RESOURCEKEY)));
		    	}
		    }
		    
		 
		  return root;
	}
	
	
	/**
	 * @deprecated
	 * @return
	 * @throws Exception
	 */
	public static List<Map<String,Object>> getQueryConfig(String filename) throws Exception{
		
	  	  List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
	  	  
		   Map<String,Object> root=getQueryConfigDs(filename);
	         
		   Iterator<String> it= root.keySet().iterator();
		     
		   Map<String,Object> menu=null;
		    while(it.hasNext()){
		    	menu=new HashMap<String, Object>();
		    	String k=it.next();
		    	Map<String,Object> v=(Map<String, Object>) root.get(k);
		    	menu.put(ID, k);
		    	menu.put(TEXT, Utils.getResourceName(v.get(Constant.RESOURCEKEY)));
		    	list.add(menu);
		    }
		    
		    return list;
	}
	
	public static Map<String,Object> getQueryConfigDs(String filename) throws Exception{
		   ObjectMapper mapper=new ObjectMapper();
		   Map<String,Object> root=mapper.readValue(getDsFile(filename), Map.class);
		   return root;
	}
	
	public static JsonNode getQueryConfigDs(String name,String filename) throws Exception{
		ObjectMapper mapper=new ObjectMapper();
	    JsonNode root=mapper.readTree(getDsFile(filename));
	    return root.path(name);
	}
	
	public static String getDs(String name,String filename) throws Exception{
		JsonNode jn=getQueryConfigDs(name,filename);
		if(jn.get(DS)==null)
		{
			return DEFAULT_DS+"Session";
		}else{
			return jn.get(DS).asText()+"Session";
		}
		
	}
	
	public static String getNs(String name,String filename) throws Exception{
		JsonNode jn=getQueryConfigDs(name,filename);
		if(jn.get(NS)==null)
		{
			return DEFAULT_NS+"."+name;
		}else{
			return jn.get(NS).asText()+"."+name;
		}
		
	}
}
