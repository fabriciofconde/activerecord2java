package org.activerecord.hibernate.util;

import org.activerecord.hibernate.exception.ActiveRecordHibernateException;
import org.apache.commons.beanutils.ConvertUtils;

/**
 * 
 *
 */
public class ConvertUtil {

	/**
	 * @param value
	 * @param targetType
	 */
	public static Object convert(Object value, Class<?> targetType) {
		try {
			return ConvertUtils.convert(value, targetType);
		} catch (Exception e) {
			throw new ActiveRecordHibernateException("Failed while converting the type", e);
		}
	}
}
