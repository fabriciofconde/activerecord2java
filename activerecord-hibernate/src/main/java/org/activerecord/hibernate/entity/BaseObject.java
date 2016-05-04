package org.activerecord.hibernate.entity;

import org.activerecord.hibernate.config.HibernateConfig;
import org.hibernate.Query;
import org.hibernate.Session;


/**
 * @author fabriciofconde
 *
 */
public class BaseObject {
	
	private static HibernateConfig hibernateConfig;
	
	/**
	 * 
	 * @param query
	 * @param filter
	 * @return
	 */
	protected static Query createQuery(Query query, Filter filter) {
		filter.setParameters(query);
		filter.setPage(query);
		filter.config(query);
		return query;
	}
	
	/*@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static <T extends Model, S extends Model> TypedQuery<S> createQuery(Class<T> entityType, String attribute, Class<S> attributeType, Filter filter) {
		CriteriaBuilder builder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<S> cQuery = builder.createQuery(attributeType);
		Root<T> root = cQuery.from(entityType);
		if (attribute != null) {
			Join join = root.join(attribute);
			cQuery.select(join);
		}
		filter.constructQuery(builder, cQuery, root);
		return createQuery(cQuery, filter);
	}*/
	
	/**
	 * 
	 * @param clazz
	 * @param filter
	 * @return
	 */
	protected static <T extends Model> Query createQuery(Class<T> clazz, Filter filter) {
		Query query = getSession().createQuery(filter.constructQuery(clazz));
		return createQuery(query, filter);
	}
	
	/**
	 * 
	 * @param clazz
	 * @param builder
	 * @param paramValues
	 * @return
	 */
	protected static <T extends Model> Query createQuery(Class<T> clazz, Filter.Builder builder, Object... paramValues) {
		return createQuery(clazz, createFilter(builder, paramValues));
	}
	
	/**
	 * 
	 * @param builder
	 * @param paramValues
	 * @return
	 */
	protected static <T extends Model> Filter createFilter(Filter.Builder builder, Object... paramValues) {
		Filter filter = builder.build();
		if (paramValues != null) {
			for (int i = 0; i < paramValues.length; i += 2) {
				filter.addCondition(paramValues[i].toString(), paramValues[i + 1]);
			}
		}
		return filter;
	}
	
	/**
	 * 
	 * @param _hibernate
	 */
	public static void setHibernate(HibernateConfig _hibernateConfig) {
		hibernateConfig = _hibernateConfig;
	}
	
	/**
	 * 
	 * 
	 * @return
	 */
	protected static Session getSession() {
		if (hibernateConfig == null)
			throw new IllegalStateException("Object static hibernateConfig cannot be null. It is necessary to implement the interface org.activerecord.hibernate.config.HibernateConfig and call the method config() interface/implementation in the application start");
		return hibernateConfig.getSession();
	}
	
}
