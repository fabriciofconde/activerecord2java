package org.activerecord.hibernate.util;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.activerecord.hibernate.exception.ActiveRecordHibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.TypeToken;

/**
 * 
 *
 */
public class PropertyUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);
	
	/**
	 * Check if the given type represents a "simple" property:
	 * a primitive, a String or other CharSequence, a Number, a Date,
	 * a URI, a URL, a Locale, a Class, or a corresponding array.
	 * <p>Used to determine properties to check for a "simple" dependency-check.
	 * @param clazz the type to check
	 * @return whether the given type represents a "simple" property
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchMethodException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 * @see org.springframework.beans.factory.support.RootBeanDefinition#DEPENDENCY_CHECK_SIMPLE
	 * @see org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#checkDependencies
	 */
	public static boolean isSimpleProperty(Class<?> clazz) 
	throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, 
	IllegalAccessException, InvocationTargetException {
		return isSimpleValueType(clazz) || (clazz.isArray() && isSimpleValueType(clazz.getComponentType()));
	}

	/**
	 * Check if the given type represents a "simple" value type:
	 * a primitive, a String or other CharSequence, a Number, a Date,
	 * a URI, a URL, a Locale or a Class.
	 * 
	 * @param clazz the type to check
	 * @return whether the given type represents a "simple" value type
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchMethodException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	public static boolean isSimpleValueType(Class<?> clazz) 
	throws SecurityException, IllegalArgumentException, ClassNotFoundException, NoSuchMethodException, 
	IllegalAccessException, InvocationTargetException {
		return isPrimitiveOrWrapper(clazz) || clazz.isEnum() ||
				CharSequence.class.isAssignableFrom(clazz) ||
				Number.class.isAssignableFrom(clazz) ||
				Date.class.isAssignableFrom(clazz) ||
				clazz.equals(URI.class) || clazz.equals(URL.class) ||
				clazz.equals(Locale.class) || clazz.equals(Class.class) || 
				clazz.equals(Serializable.class) || clazz.equals(Timestamp.class);
	}
	
	/**
	 * Checks if the given type is a collection. If includeMaps is set, maps will be treated as collections
	 * 
	 * @param type
	 * @param includeMaps
	 * @return
	 */
	public static boolean isCollectionProperty(Type type, boolean includeMaps) {
		if (type instanceof Class) {
			return isCollectionProperty((Class<?>)type, includeMaps);
		}
		if (type instanceof ParameterizedType) {
			return isCollectionProperty((Class<?>)((ParameterizedType) type).getRawType(), includeMaps);
		}
		return false;
	}
	
	/**
	 * Checks if the given clazz is a collection. If includeMaps is set, maps will be treated as collections
	 * 
	 * @param clazz
	 * @param includeMaps
	 * @return
	 */
	public static boolean isCollectionProperty(Class<?> clazz, boolean includeMaps) {
		return Collection.class.isAssignableFrom(clazz) || (includeMaps && isMapProperty(clazz));
	}
	
	/**
	 * Checks if the given type is a map
	 * 
	 * @param type
	 * @return
	 */
	public static boolean isMapProperty(Type type) {
		if (type instanceof Class) {
			return isMapProperty((Class<?>)type);
		}
		if (type instanceof ParameterizedType) {
			return isMapProperty((Class<?>)((ParameterizedType) type).getRawType());
		}
		return false;
	}
	
	/**
	 * Checks if the given class is a map
	 * 
	 * @param clazz
	 * @return
	 */
	public static boolean isMapProperty(Class<?> clazz) {
		return Map.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Returns the element type of the collection
	 * 
	 * @param type
	 * @return
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 */
	public static Class<?> getCollectionElementType(Type type) throws SecurityException, NoSuchMethodException {
		if (! (type instanceof ParameterizedType)) {
			return Object.class;
		}
		ParameterizedType ptype = (ParameterizedType) type;
		Class<?> rawType = (Class<?>) ptype.getRawType();
		
			if (Collection.class.isAssignableFrom(rawType)) {
				return TypeToken.of(type).resolveType(Collection.class.getMethod("add", Object.class).getGenericParameterTypes()[0]).getRawType();
			}
			if (Map.class.isAssignableFrom(rawType)) {
				return TypeToken.of(type).resolveType(Map.class.getMethod("put", Object.class, Object.class).getGenericParameterTypes()[1]).getRawType();
			}
		return Object.class;
	}
	
	/**
	 * Returns the property value for the given property name from the bean
	 * 
	 * @param bean
	 * @param name
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws InvocationTargetException 
	 */
	public static Object getProperty(Object bean, String name) 
	throws ClassNotFoundException, SecurityException, NoSuchMethodException, 
	IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		try {
			Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass("org.apache.commons.beanutils.PropertyUtils");
			Method method = clazz.getMethod("getProperty", Object.class, String.class);
			return method.invoke(null, bean, name);
		} catch (InvocationTargetException e) {
			if (e.getCause() instanceof NoSuchMethodException) {
				logger.debug("Getter doesn't exist for the property {} in the class {}", name, bean.getClass());
				return null;
			}
			throw e;
		} 
	}
	
	/**
	 * Sets the value to the property on the bean
	 * 
	 * @param bean
	 * @param name
	 * @param value
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static void setProperty(Object bean, String name, Object value) 
	throws ClassNotFoundException, SecurityException, NoSuchMethodException, 
	IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass("org.apache.commons.beanutils.BeanUtils");
		Method method = clazz.getMethod("setProperty", Object.class, String.class, Object.class);
		if (value == null) {
			method.invoke(null, bean, name, method.getReturnType().cast(value));
		} else {
			method.invoke(null, bean, name, value);
		}
	}
	
	public static Method getReadMethod(Object bean, String name) 
	throws ClassNotFoundException, SecurityException, NoSuchMethodException, 
	IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass("org.apache.commons.beanutils.PropertyUtils");
		Method method = clazz.getMethod("getPropertyDescriptor", Object.class, String.class);
		Object descriptor = method.invoke(null, bean, name);
		if (descriptor == null) {
			throw new ActiveRecordHibernateException("Property descriptor not found for the field - " + name);
		}
		method = clazz.getMethod("getReadMethod", PropertyDescriptor.class);
		return (Method) method.invoke(null, descriptor);
	}
	
	/**
	 * Checks if the given class is primitive type or wrapper
	 * 
	 * @param clazz
	 * @return
	 * @throws ClassNotFoundException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static Boolean isPrimitiveOrWrapper(Class<?> clazz) 
	throws ClassNotFoundException, SecurityException, NoSuchMethodException, 
	IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Class<?> utils = Thread.currentThread().getContextClassLoader().loadClass("org.apache.commons.lang3.ClassUtils");
		Method method = utils.getMethod("isPrimitiveOrWrapper", Class.class);
		return (Boolean) method.invoke(null, clazz);
	}
}
