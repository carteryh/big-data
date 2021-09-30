package cn.itcast.util;

import java.util.List;

import javax.annotation.Resource;


import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;


import org.springframework.stereotype.Component;

@Component("ehcacheUtil")
public class EhcacheUtil {
	
	private EhcacheUtil ehcacheUtil;
	
	//单例
	private static Cache mycache;
	public static synchronized Cache getInstance() {
		if(mycache == null){
//			System.out.println("单例创建");
			CacheManager cacheManager = CacheManager.create();
			// 获取ehcache配置文件中的一个cache
			mycache = cacheManager.getCache("myCache");
		}
		return mycache;
	}
	
	//cache put
	public boolean put(String key,String val){
		try{
			if(null == mycache){
				mycache = getInstance();
			}
			// 添加数据到缓存中
			Element element = new Element(key, val);
			mycache.put(element);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	public boolean putList(String key,List val){
		try{
			if(null == mycache){
				mycache = getInstance();
			}
			// 添加数据到缓存中
			Element element = new Element(key, val);
			mycache.put(element);
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	//cache get
	public String get(String key){
		if(null == mycache){
			mycache = getInstance();
		}
		Element result = mycache.get(key);
		if(null==result){
			return null;
		}else{
			return (String) result.getValue();
		}
	}
	
	public List getList(String key){
		if(null == mycache){
			mycache = getInstance();
		}
		Element result = mycache.get(key);
		if(null==result){
			return null;
		}else{
			return (List) result.getValue();
		}
	}
	
	public static void main(String[] args){
//		new EhcacheUtil().test();
		String name = "张三";
		String person = "我叫张三";
		new EhcacheUtil().put(name, person);
		String result = new EhcacheUtil().get(name);
		System.out.println(result);
	}
	
	public void test(){
//		CacheManager cacheManager = CacheManager.create("ehcache.xml");
		CacheManager cacheManager = CacheManager.create();
		// 获取ehcache配置文件中的一个cache
//		net.sf.ehcache.Cache sample = cacheManager.getCache("sample");
		net.sf.ehcache.Cache sample = cacheManager.getCache("myCache");
		// 获取页面缓存
		//BlockingCache cache = new BlockingCache(cacheManager.getEhcache("SimplePageCachingFilter"));
		// 添加数据到缓存中
		Element element = new Element("key", "val");
		sample.put(element);
		Element result = sample.get("key");
		
		System.out.println(result.toString());
		System.out.println(result.getValue());
		

	}
	
	public static Cache getMycache() {
		return mycache;
	}

	public static void setMycache(Cache mycache) {
		EhcacheUtil.mycache = mycache;
	}
	
	public EhcacheUtil getEhcacheUtil() {
		return ehcacheUtil;
	}
	@Resource
	public void setEhcacheUtil(EhcacheUtil ehcacheUtil) {
		this.ehcacheUtil = ehcacheUtil;
	}
}

