package com.trs.gallery.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.StringValueResolver;


/**
 * Utils - Spring
 * 
 * @author yeliangqin
 * @version 3.0
 */
@Component("springUtils")
public final class SpringUtils implements ApplicationContextAware, EmbeddedValueResolverAware, DisposableBean {
	/** applicationContext */
	private static ApplicationContext applicationContext;

	private static StringValueResolver stringValueResolver;

	private static final Map<Class<?>, List<Field>> classPrivateFieldsCache = new ConcurrentReferenceHashMap<Class<?>, List<Field>>(256);

	/**
	 * 不可实例化
	 */
	private SpringUtils() {
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringUtils.applicationContext = applicationContext;
	}

	public void setEmbeddedValueResolver(StringValueResolver stringValueResolver) {
		SpringUtils.stringValueResolver = stringValueResolver;
	}

	public void destroy() throws Exception {
		applicationContext = null;
		stringValueResolver = null;
	}

	/**
	 * 获取applicationContext
	 * 
	 * @return applicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * 获取实例
	 * 
	 * @param name
	 *            Bean名称
	 * @return 实例
	 */
	public static Object getBean(String name) {
		Assert.hasText(name);
		return applicationContext.getBean(name);
	}

	/**
	 * 获取系统属性配置的值
	 * 
	 * @param name
	 *            属性名
	 * @param defultValue
	 *            默认值
	 * @return 属性值
	 */
	public static String getPropertiesValue(String name, String defultValue) {
		String temp = "${" + name;
		if (defultValue != null) {
			temp += ":" + defultValue;
		}
		temp += "}";
		return stringValueResolver.resolveStringValue(temp);
	}

	/**
	 * 获取实例
	 * 
	 * @param name
	 *            Bean名称
	 * @param type
	 *            Bean类型
	 * @return 实例
	 */
	public static <T> T getBean(String name, Class<T> type) {
		Assert.hasText(name);
		Assert.notNull(type);
		return applicationContext.getBean(name, type);
	}

	/**
	 * 获取实例
	 * 
	 * @param type
	 *            Bean类型
	 * @return 实例
	 */
	public static <T> T getBean(Class<T> type) {
		Assert.notNull(type);
		return applicationContext.getBean(type);
	}

	/**
	 * 复制属性
	 * 
	 * @param source
	 *            源对象
	 * @param target
	 *            目标对象
	 * @param ignore
	 *            判断是过滤指定属性还是拷贝指定属性
	 * @param properties
	 *            属性
	 * @throws BeansException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void copyProperties(Object source, Object target, boolean ignore, String... properties) throws BeansException {
		Assert.notNull(source, "Source must not be null");
		Assert.notNull(target, "Target must not be null");
		if (!ignore && (properties == null || properties.length == 0)) {
			return;
		}
		List<String> list = (properties != null ? Arrays.asList(properties) : null);
		PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(target.getClass());
		for (PropertyDescriptor targetPd : targetPds) {
			Method writeMethod = targetPd.getWriteMethod();
			if (writeMethod != null && ((ignore && (list == null || !list.contains(targetPd.getName()))) || (!ignore && list.contains(targetPd.getName())))) {
				PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
				if (sourcePd != null && sourcePd.getReadMethod() != null) {
					try {
						Method readMethod = sourcePd.getReadMethod();
						if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
							readMethod.setAccessible(true);
						}
						Object sourceValue = readMethod.invoke(source);
						// 处理集合情况，不然orphanRemoval = true,关联删除时会报错
						if (sourceValue != null && sourceValue instanceof Collection) {
							Method targetReadMethod = targetPd.getReadMethod();
							if (!Modifier.isPublic(targetReadMethod.getDeclaringClass().getModifiers())) {
								targetReadMethod.setAccessible(true);
							}
							Object targetValue = targetReadMethod.invoke(target);
							if (targetValue != null && targetValue instanceof Collection && sourceValue instanceof Collection) {
								if (!targetValue.equals(sourceValue)) {
									Collection targetCollection = (Collection) targetValue;
									Collection sourceCollection = (Collection) sourceValue;
									boolean equals = targetCollection.size() == sourceCollection.size();
									if (equals) {
										for (Object object : targetCollection) {
											if (!sourceCollection.contains(object)) {
												equals = false;
												break;
											}
										}
									}
									if (!equals) {
										targetCollection.clear();
										targetCollection.addAll((Collection) sourceValue);
									}
								}
							} else {
								if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
									writeMethod.setAccessible(true);
								}
								writeMethod.invoke(target, sourceValue);
							}
						} else {
							if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
								writeMethod.setAccessible(true);
							}
							writeMethod.invoke(target, sourceValue);
						}
					} catch (Throwable ex) {
						throw new FatalBeanException("Could not copy properties from source to target", ex);
					}
				}
			}
		}
	}

	/**
	 * 获取类实例的属性值
	 * 
	 * @param clazz
	 *            类名
	 * @param includeParentClass
	 *            是否包括父类的属性值
	 * @return 类名.属性名=属性类型
	 */
	public static List<Field> getClassPrivateFields(Class<?> clazz) {
		List<Field> fields = classPrivateFieldsCache.get(clazz);
		if (fields == null) {
			fields = new ArrayList<Field>();
			if (clazz == null || clazz.equals(Object.class)) {
				return fields;
			}
			Class<?> superclass = clazz.getSuperclass();
			fields.addAll(getClassPrivateFields(superclass));
			for (Field field : clazz.getDeclaredFields()) {
				if (field.getModifiers() == Modifier.PRIVATE) {
					fields.add(field);
				}
			}
		}
		return fields;
	}
}