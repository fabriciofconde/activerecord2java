package org.activerecord.hibernate.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Id;
import javax.persistence.Transient;

import org.activerecord.hibernate.entity.Filter.Builder;
import org.activerecord.hibernate.entity.enums.Operator;
import org.activerecord.hibernate.entity.event.DeleteLog;
import org.activerecord.hibernate.entity.event.InsertLog;
import org.activerecord.hibernate.entity.event.LoadLog;
import org.activerecord.hibernate.entity.event.UpdateLog;
import org.activerecord.hibernate.exception.ActiveRecordHibernateException;
import org.activerecord.hibernate.util.BeanUtil;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;



/**
 * <p>Base class for all entities. Embeds entity manager in it and provides a bunch of DAL abstractions to make data access a lot simpler.</p>
 * <p>This allows activerecord pattern style of usage,</p>
 * 
 * <pre> 
 * Person.get(Person.class, 1L);
 * Person.findBy(Person.class, "firstName", "Fabricio", "lastName", "Conde");
 * Person.findAllBy(Person.class, "firstName", "Fabricio");
 * Person.findAll(Person.class);
 * Person.count(Person.class);
 * Person.exists(Person.class);
 * personInstance.save();
 * personInstance.delete();
 * personInstance.update();
 * personInstance.flush();
 * personInstance.refresh();
 * </pre>
 * <p>and more.</p>
 * 
 * @author fabricio.conde
 *
 */
@SuppressWarnings("serial")
public abstract class Model extends BaseObject implements Serializable, InsertLog, UpdateLog, DeleteLog, LoadLog {
	
	
	/**
	 * 
	 */
	public Model() {
		super();
	}
	
	
	/**
	 * The model identifier. Override and annotate with {@link Id}
	 * 
	 * @return an Serializable with id
	 */
	public abstract Serializable getId();

	
	/**
	 * set the attributes of the model with attributes the argument
	 * 
	 * @param attributes
	 */
	@Transient
	public void attributes(Map<String, Object> attributes) {
		try {
			BeanUtil.load(this, attributes);
		} catch (Exception e) {
			throw new ActiveRecordHibernateException("Failed while updating the attributes", e);
		}
	}
	
	
	/**
	 * set the attributes of the model with attributes the argument
	 * 
	 * @param attributes
	 */
	@Transient
	public Map<String, Object> attributes() {
		HashMap<String, Object> attributes = new HashMap<String, Object>();
		
		try {
			BeanUtil.read(this, attributes);
		} catch (Exception e) {
			throw new ActiveRecordHibernateException("Failed while get the attributes", e);
		}
		
		return attributes;
	}
	
	
	/**
	 * Save this entity to the persistence context
	 * 
	 * @return True if haven't problem 
	 */
	public boolean save() {
		return execute(new Executor<Boolean>() {
			@Override
			public Boolean execute(Session session) {
				session.save(Model.this);
				return true;
			}
		});
	}
	
	/**
	 * Save this entity to the persistence context
	 * 
	 * @return True if haven't problem 
	 */
	public boolean saveOrUpdate() {
		return execute(new Executor<Boolean>() {
			@Override
			public Boolean execute(Session session) {
				session.saveOrUpdate(Model.this);
				return true;
			}
		});
	}
	
	/**
	 * Delete this entity from the persistence context
	 * 
	 * @return True if haven't problem 
	 */
	public boolean delete() {
		return execute(new Executor<Boolean>() {
			@Override
			public Boolean execute(Session session) {
				session.delete(Model.this);
				return true;
			}
		});
	}
	
	
	/**
	 * Merge this entity with the one from the persistence context
	 * 
	 * @return True if haven't problem 
	 */
	public boolean update() {
		return execute(new Executor<Boolean>() {
			@Override
			public Boolean execute(Session session) {
				session.update(Model.this);
				return true;
			}
		});
	}
	
	
	/**
	 * 
	 * @return
	 */
	public boolean merge() {
		return execute(new Executor<Boolean>() {
			@Override
			public Boolean execute(Session session) {
				session.merge(Model.this);
				return true;
			}
		});
	}
	
	
	/**
	 * 
	 * @return
	 */
	public boolean persist() {
		return execute(new Executor<Boolean>() {
			@Override
			public Boolean execute(Session session) {
				session.persist(Model.this);
				return true;
			}
		});
	}
	
	
	/**
	 * 
	 */
	public void flush() {
		execute(new Executor<Void>() {
			@Override
			public Void execute(Session session) {
				session.flush();
				return null;
			}
		});
	}
	
	
	/**
	 * Reload this entity from the persistence context
	 */
	public void refresh() {
		execute(new Executor<Void>() {
			@Override
			public Void execute(Session session) {
				session.refresh(Model.this);
				return null;
			}
		});
	}
	
	
	/**
	 * 
	 */
	public void afterLoad() {}
	
	
	/**
	 * 
	 */
	public void beforeInsert() {}
	
	
	/**
	 * 
	 */
	public void beforeUpdate() {}
	
	
	/**
	 * 
	 */
	public void beforeDelete() {}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
		return result;
	}

	
	/**
	 * 
	 * @param executor
	 * @return T
	 */
	protected static <T> T execute(Executor<T> executor) {
		return executor.execute(getSession());
	}
	
	
	/**
	 * 
	 * @author fabricio.conde
	 *
	 * @param <T>
	 */
	private static interface Executor<T> {
		T execute(Session session);
	}
	
	
	/**
	 * Returns the entity identified by the id
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Model> T get(final Class<T> clazz, final Serializable id) {
		T domain = (T) getSession().get(clazz, id);
		return domain;
	}
	
	
	/**
	 * Returns the entity identified by the id
	 * 
	 * @param clazz
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Model> T load(final Class<T> clazz, final Serializable id) {
		T domain = (T) getSession().load(clazz, id);
		return domain;
	}
	
	
	/**
	 * Checks if an entity exists with the given id
	 * 
	 * @param clazz
	 * @param id
	 * @return True if exists
	 */
	public static <T extends Model> boolean exists(final Class<T> clazz, final Serializable id) {
		ClassMetadata classMetadata = getSession().getSessionFactory().getClassMetadata(clazz);
		Criteria criteria = getSession().createCriteria(clazz);
		criteria
			.add(Restrictions.eq(classMetadata.getIdentifierPropertyName(), id))
			.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).longValue() > 0;
	}
	
	
	/**
	 * Returns the first row matching the given key value pairs. 
	 * The key value pairs are supplied as arguments like (key1, value1, key2, value2)
	 * 
	 * @param clazz
	 * @param paramValues
	 * @return T
	 */
	public static <T extends Model> T findBy(final Class<T> clazz, final Object... paramValues) {
		List<T> list = findAllBy(clazz, paramValues);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	
	/**
	 * Returns the first row of entities matching the given filter. 
	 * 
	 * @param clazz
	 * @param filter
	 * @return T
	 */
	public static <T extends Model> T findBy(final Class<T> clazz, final Filter filter) {
		List<T> list = findAllBy(clazz, filter);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	
	/**
	 * Returns the first row of entities matching the given args. 
	 * 
	 * @param clazz
	 * @param attribute
	 * @param operator
	 * @param value
	 * @return T
	 */
	public static <T extends Model> T findByAttributeAndOperatorAndValue(final Class<T> clazz, final String attribute, final Operator operator, final Object... value) {
		Filter filter = Filter.Builder.select().build();
		if (!operator.equals(Operator.between) && !operator.equals(Operator.in))
			filter.addCondition(attribute, operator, value[0]);
		else
			filter.addCondition(attribute, operator, value);
		return findBy(clazz, filter);
	}
	
	
	/**
	 * Returns a list of entities matching the given key value pairs. 
	 * The key value pairs are supplied as arguments like (key1, value1, key2, value2)
	 * 
	 * @param clazz
	 * @param paramValues
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Model> List<T> findAllBy(final Class<T> clazz, final Object... paramValues) {
		return createQuery(clazz, Filter.Builder.select(), paramValues).list();
	}
	
	
	/**
	 * Returns a list of entities matching the given filter
	 * 
	 * @param clazz
	 * @param filter
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Model> List<T> findAllBy(final Class<T> clazz, final Filter filter) {
		return createQuery(clazz, filter).list();
	}
	
	
	/**
	 * 
	 * @param clazz
	 * @param attribute
	 * @param operator
	 * @param value
	 * @return
	 */
	public static <T extends Model> List<T> findAllByAttributeAndOperatorAndValue(final Class<T> clazz, final String attribute, final Operator operator, final Object... value) {
		Filter filter = Filter.Builder.select().build();
		if (!operator.equals(Operator.between) && !operator.equals(Operator.in))
			filter.addCondition(attribute, operator, value[0]);
		else
			filter.addCondition(attribute, operator, value);
		return findAllBy(clazz, filter);
	}
	
	
	/**
	 * Returns all the rows in the table
	 * 
	 * @param clazz
	 * @return an List<T>
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Model> List<T> findAll(final Class<T> clazz) {
		return (List<T>) createQuery(clazz, Builder.select().build()).list();
	}
	
	
	/**
	 * Returns the total count of rows in the table
	 * 
	 * @param clazz
	 * @return an long with amount
	 */
	public static <T extends Model> long count(final Class<T> clazz) {
		Criteria criteria = getSession().createCriteria(clazz);
		criteria.setProjection(Projections.rowCount());
		return ((Number) criteria.uniqueResult()).longValue();
	}
	
	
	/**
	 * Returns the count of rows matching the given key value pairs. 
	 * The key value pairs are supplied as arguments like (key1, value1, key2, value2)
	 * 
	 * @param clazz
	 * @param paramValues
	 * @return an long with amount 
	 */
	public static <T extends Model> long countBy(final Class<T> clazz, final Object... paramValues) {
		Filter filter = createFilter(Filter.Builder.count(), paramValues);
		return countBy(clazz, filter);
	}
	
	
	/**
	 * Returns the count of rows matching the given filter
	 * 
	 * @param clazz
	 * @param filter
	 * @return an long with amount 
	 */
	public static <T extends Model> long countBy(final Class<T> clazz, final Filter filter) {
		return ((Number) createQuery(clazz, filter).uniqueResult()).longValue();
	}
	
	
	/**
	 * Return the count of rows matching the given example
	 * @param clazz
	 * @param example
	 * @return
	 */
	public static <T extends Model> Number countByExample(final Class<T> clazz, final T example) {
		ClassMetadata classMetadata = getSession().getSessionFactory().getClassMetadata(clazz);
		final Criteria criteria = getSession().createCriteria(clazz);
		criteria.setProjection(Projections.countDistinct(classMetadata.getIdentifierPropertyName()));
		criteria.add(
        	Example.create(example)
        	.enableLike(MatchMode.ANYWHERE)
        	.ignoreCase()
        	.setPropertySelector(new PropertySelectorImpl()));
        return (Number) criteria.uniqueResult();
    }
	
	
	/**
	 * Delete the entity identified by the id without load entity
	 * 
	 * @param clazz
	 * @param id
	 * @return True if deleted
	 */
	public static <T extends Model> boolean delete(final Class<T> clazz, final Serializable id) {
		return execute(new Executor<Boolean>() {
			@Override
			public Boolean execute(Session session) {
				ClassMetadata classMetadata = session.getSessionFactory().getClassMetadata(clazz);
				Filter filter = Filter.Builder.delete().build();
				filter.addCondition(classMetadata.getIdentifierPropertyName(), id);
				Query query = Model.createQuery(clazz, filter);
				int i = query.executeUpdate();
				return i > 0;
			}
		});
	}
	
	
	/**
	 * Deletes the rows matching the given key value pairs.
	 * The key value pairs are supplied as arguments like (key1, value1, key2, value2)
	 * 
	 * @param clazz
	 * @param paramValues
	 * @return an int with amount deleted
	 */
	public static <T extends Model> int deleteAllBy(final Class<T> clazz, final Object... paramValues) {
		Filter filter = createFilter(Filter.Builder.delete(), paramValues);
		return deleteAllBy(clazz, filter);
	}
	
	
	/**
	 * Deletes the rows matching the given filter
	 * 
	 * @param clazz
	 * @param filter
	 * @return an int with amount deleted
	 */
	public static <T extends Model> int deleteAllBy(final Class<T> clazz, final Filter filter) {
		return execute(new Executor<Integer>() {
			@Override
			public Integer execute(Session session) {
				Query query = Model.createQuery(clazz, filter);
				int i = query.executeUpdate();
				return i;
			}
		});
	}
	
	
	/**
	 * 
	 * @param clazz
	 * @param example
	 * @return List<T>
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Model> List<T> findAllByExample(final Class<T> clazz, final T example) {
        final Session s = getSession();
        final Criteria c = s.createCriteria(clazz);
        c.add(
        	Example.create(example)
        	.enableLike(MatchMode.ANYWHERE)
        	.ignoreCase()
        	.setPropertySelector(new PropertySelectorImpl()));
        return (List<T>) c.list();
    }

	
	/**
	 * 
	 * @param clazz
	 * @param example
	 * @param pageSize
	 * @param start
	 * @param sort
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Model> List<T> findAllByExample(final Class<T> clazz, final T example, String sort, boolean ascendingOrder) {
        final Session s = getSession();
        final Criteria c = s.createCriteria(clazz);
        c.add(
        	Example.create(example)
        	.enableLike(MatchMode.ANYWHERE)
        	.ignoreCase()
        	.setPropertySelector(new PropertySelectorImpl()));
        c.addOrder(ascendingOrder ? Order.asc(sort) : Order.desc(sort));
        
        return (List<T>) c.list();
    }
	
	
	/**
	 * 
	 * @param clazz
	 * @param example
	 * @param pageSize
	 * @param start
	 * @param sort
	 * @return
	 */
	public static <T extends Model> List<T> findAllByExample(final Class<T> clazz, final T example, Integer pageSize, Integer start, String sort) {
        boolean asc = true;
        if (sort != null && !sort.trim().equals("") && sort.trim().startsWith("-")) {
        	asc = false;
        	sort = sort.substring(1, sort.length());
		}
		return findAllByExample(clazz, example, pageSize, start, sort, asc);
    }
	
	
	/**
	 * 
	 * @param clazz
	 * @param example
	 * @param pageSize
	 * @param start
	 * @param sort
	 * @param order
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Model> List<T> findAllByExample(final Class<T> clazz, final T example, Integer pageSize, Integer start, String sort, boolean asc) {
        final Session s = getSession();
        final Criteria c = s.createCriteria(clazz);
        c.add(
        	Example.create(example)
        	.enableLike(MatchMode.ANYWHERE)
        	.ignoreCase()
        	.setPropertySelector(new PropertySelectorImpl()));
        
		if (sort != null && !sort.equals("")) {
			c.addOrder(asc ? Order.asc(sort) : Order.desc(sort));
		}
		
		if (pageSize != null) {
			c.setMaxResults(pageSize);
		}
		
		if (start != null) {
			c.setFirstResult(start);
		}
        
        return (List<T>) c.list();
    }

	
	/**
	 * 
	 * @param clazz
	 * @param example
	 * @return T
	 */
	public static <T extends Model> T findOneByExample(final Class<T> clazz, final T example) {
	    final List<T> list = findAllByExample(clazz, example);
	    if (list != null && !list.isEmpty())
			return list.get(0);
		return null;
	}
	
}