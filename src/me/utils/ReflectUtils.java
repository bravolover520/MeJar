package me.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Java 反射的主要功能时用来做框架,其他一般用在API不开放的私有方法。
 * @author Jesus{931178805@qq.com}
 * </br>2014年8月14日
 */
public class ReflectUtils {
	
	/**
	 * 尝试得到实体类
	 * @param clsToLoad
	 * @param superCls
	 * @return
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> T tryLoadClass(String clsToLoad, Class<T> superCls) throws 
				ClassNotFoundException, InstantiationException, IllegalAccessException {
		Class<?> aCls = Class.forName(clsToLoad);
		Class<? extends T> wrapperCls = aCls.asSubclass(superCls);
		return wrapperCls.newInstance();
	}
	
	/***
	 * 得到当前类public所有的变量
	 * @param classPath
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static List<String> getPublicFieldsByReflect(String classPath) throws ClassNotFoundException {
		List<String> strings = new ArrayList<String>();
		Class<?> cls = Class.forName(classPath);	// 加载类  
		Field[] fields = cls.getFields();			// 获得当前类下public的变量
		for (int i = 0; i < fields.length; i++) {
			strings.add(fields[i].getName());
		}
		return strings;
	}
	
	/***
	 * 得到当前类public/private/protected所有的变量
	 * @param classPath
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static List<String> getAllFieldsByReflect(String classPath) throws ClassNotFoundException {
		List<String> strings = new ArrayList<String>();
		Class<?> cls = Class.forName(classPath);	// 加载类  
		Field[] fields = cls.getDeclaredFields();	// 获得当前类下public/private/protected的变量
		for (int i = 0; i < fields.length; i++) {
			strings.add(fields[i].getName());
		}
		return strings;
	}
	
	/***
	 * 得到当前类public/private/protected,static所有的方法
	 * @param classPath
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static List<String> getAllMethodsByReflect(String classPath) throws ClassNotFoundException {
		List<String> strings = new ArrayList<String>();
		Class<?> cls = Class.forName(classPath);// 加载类  
		Method[] methods = cls.getDeclaredMethods();// 获得类的方法集合 
		for (int i = 0; i < methods.length; i++) {
			strings.add(methods[i].getName());
		}
		return strings;
	}
	
	/***
	 * 得到当前类public(包括static)所有的方法 和 父类 的public方法
	 * </br>
	 * <font color=red><font/>
	 * 注意：假如调用和被调用的类在同一个package下，则得到当前类public/private/protected,static所有的方法 和 父类 的public方法
	 * @param classPath
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static List<String> getPublicMethodsByReflect(String classPath) throws ClassNotFoundException {
		List<String> strings = new ArrayList<String>();
		Class<?> cls = Class.forName(classPath);// 加载类  
		Method[] methods = cls.getMethods();
		for (int i = 0; i < methods.length; i++) {
			strings.add(methods[i].getName());
		}
		return strings;
	}

}
