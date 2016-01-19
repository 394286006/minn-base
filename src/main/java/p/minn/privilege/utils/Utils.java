package p.minn.privilege.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import p.minn.common.utils.UtilCommon;

/**
 * 
 * @author minn
 * @QQ:3942986006
 * @comment 工具类
 *
 */

public class Utils extends UtilCommon{

	private static final String KEY_ID="id";
	
	/**
	 * 适用flexgrid的数据格式
	 * @param source 数据源
	 * @param keys 显示顺序
	 * @return
	 */
	public static List<Map<String,Object>> list2Grid(List<Map<String,Object>> source,List<String> keys){
		Map<String,Object> row=null;
		List<Map<String,Object>> rows=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> data:source){
			row=new HashMap<String,Object>();
			row.put(KEY_ID, data.get(KEY_ID));
			List<Object> tarr=new ArrayList<Object>();
				 for(String k: keys){
					 tarr.add(data.get(k));
				 }
			row.put("cell", tarr);
				 
		    rows.add(row);
		}
		
		return rows;
	}
	
	/**
	 * 适用flexgrid的数据格式
	 * @param source 数据源
	 * @param keys 显示顺序
	 * @return
	 */
	public static List<Map<String,Object>> list2Grid(List<Map<String,Object>> source,String[] keys){
		Map<String,Object> row=null;
		List<Map<String,Object>> rows=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> data:source){
			row=new HashMap<String, Object>();
			row.put(KEY_ID, data.get(KEY_ID));
			List<Object> tarr=new ArrayList<Object>();
				 for(String k: keys){
					 
					 tarr.add(data.get(k));
				 }
			row.put("cell", tarr);
				 
		    rows.add(row);
		}
		
		return rows;
	}
	
	public static List<Map<String,Object>> createTreeMenu(List<Map<String,Object>> source,String parent){
		List<Map<String,Object>> children=new ArrayList<Map<String,Object>>();
		for(Map<String,Object> map:source){
			if(map.get("pid").toString().equals(parent)){
				if(map.get("selected").toString().equals("1")){
					map.put("selected", true);
				}else{
					map.put("selected", false);
				}
				map.put("children", createTreeMenu(source,map.get("id").toString()));
				children.add(map);
			}
		}
		return children;
	}
	
	public static String getReadUploadPath(HttpServletRequest req){
		return req.getSession().getServletContext().getRealPath("/fronimg/")+"/";
	}
	public static String getReadDataPath(HttpServletRequest req){
		return req.getSession().getServletContext().getRealPath("/data/")+"/";
	}
}
