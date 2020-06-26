package org.activerecord.hibernate.entity;

import java.util.ArrayList;
import java.util.List;

import org.activerecord.hibernate.entity.enums.Operator;
import org.hibernate.Criteria;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.type.Type;

/**
 * 
 * @author fabricio.conde
 *
 */
public class Filter {
	
	private String sql;
	private Integer pageNo = 0;
	private Integer perPage = 0;
	private boolean cacheable = false;
	
	private ArrayList<Condition> conditions = new ArrayList<Condition>();
	private ArrayList<ConditionHQL> conditionsHQL = new ArrayList<ConditionHQL>();
	private ArrayList<Order> orders = new ArrayList<Order>();
	
	
	/**
	 * 
	 * @param sql
	 * @param pageNo
	 * @param perPage
	 * @param cacheable
	 */
	private Filter(String sql, int pageNo, int perPage, boolean cacheable) {
		super();
		this.sql = sql;
		this.pageNo = pageNo > 0 ? pageNo : 1;
		this.perPage = (perPage < 1) ? 0 : perPage;
		this.cacheable = cacheable;
	}
	

	/**
	 * 
	 * @param pageNo
	 */
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	/**
	 * 
	 * @param perPage
	 */
	public void setPerPage(Integer perPage) {
		this.perPage = perPage;
	}
	
	/**
	 * @param cacheable the cacheable to set
	 */
	public void setCacheable(boolean cacheable) {
		this.cacheable = cacheable;
	}

	/**
	 * @param conditions the conditions to set
	 * @param orders the orders to set
	 */
	public void add(List<Condition> conditions, List<Order> orders) {
		if (conditions != null && !conditions.isEmpty()) {
			this.conditions.addAll(conditions);
		}
		if (orders != null && !orders.isEmpty()) {
			this.orders.addAll(orders);
		}
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void addCondition(String name, Object value) {
		addCondition(name, Operator.eq, value);
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void addConditionIsNull(String name) {
		addCondition(name, Operator.isNull, null);
	}
	
	/**
	 * 
	 * @param name
	 * @param value
	 */
	public void addConditionIsNotNull(String name) {
		addCondition(name, Operator.isNotNull, null);
	}
	
	/**
	 * 
	 * @param name
	 * @param operator
	 * @param value
	 */
	public void addCondition(String name, Operator operator, Object value) {
		conditions.add(new Condition(name, operator, value));
	}
	
	/**
	 * 
	 * @param sqlRestriction
	 * @param value
	 * @param type
	 */
	public void addConditionHQL(String sqlRestriction, Object value, Type type) {
		conditionsHQL.add(new ConditionHQL(sqlRestriction, value, type));
	}
	
	/**
	 * 
	 * @param sqlRestriction
	 * @param value
	 * @param type
	 */
	public void addConditionHQL(String sqlRestriction, Object[] value, Type[] type) {
		conditionsHQL.add(new ConditionHQL(sqlRestriction, value, type));
	}
	
	/**
	 * @return the shouldPage
	 */
	private boolean isShouldPage() {
		return perPage > 0;
	}

	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public <T extends Model> String constructQuery(Class<T> clazz) {
		StringBuilder writer = new StringBuilder(String.format(sql, clazz.getName()));
		
		if (!conditions.isEmpty() || !conditionsHQL.isEmpty()) {
			writer.append(" where ");
		}
		
		if (conditionsHQL != null && !conditionsHQL.isEmpty()) {
			for (int i = 0; i < conditionsHQL.size(); i++) {
				writer.append(conditionsHQL.get(i).constructQuery());
				if (i+1 < conditionsHQL.size()) {
					writer.append(" and ");
				}
			}
		}
		
		if (!conditions.isEmpty()) {
			if (!conditionsHQL.isEmpty()) {
				writer.append(" and ");
			}
			
			for (int i = 0; i < conditions.size(); i++) {
				writer.append(conditions.get(i).constructQuery());
				if (i+1 < conditions.size()) {
					writer.append(" and ");
				}
			}
		}
		
		if (orders != null && !orders.isEmpty()) {
			writer.append(" order by  ");
			int size = orders.size();
			for (int i = 0; i < size - 1; i++) {
				writer.append(orders.get(i).constructQuery()).append(", ");
			}
			writer.append(orders.get(size - 1).constructQuery());
		}
		
		return writer.toString();
	}
	
	/**
	 * 
	 * @param query
	 */
	public void setParameters(Query query) {
		if (conditionsHQL != null && !conditionsHQL.isEmpty()) {
			int position = 0;
			for (ConditionHQL conditionNative : conditionsHQL) {
				position = conditionNative.setParameters(query, position);
			}
		}
		
		if (conditions != null && !conditions.isEmpty()) {
			for (Condition condition : conditions) {
				condition.setParameters(query);
			}
		}
	}
	
	/**
	 * 
	 * @param query
	 */
	public void setPage(Query query) {
		if (isShouldPage()) {
			query.setFirstResult(getStart());
			query.setMaxResults(perPage);
		}
	}
	
	/**
	 * 
	 * @param criteria
	 */
	public void setPage(Criteria criteria) {
		if (isShouldPage()) {
			criteria.setFirstResult(getStart());
			criteria.setMaxResults(perPage);
		}
	}
	
	public void addOrder(String field, boolean asc) {
		orders.add(new Order(field, asc));
	}
	
	/**
	 * 
	 * @param query
	 */
	public void config(Query query) {
		query.setCacheable(cacheable);
	}
	
	/**
	 * 
	 * @param query
	 */
	public void lockOptions(Query query, boolean useQueryHintNolockAlways) {
		if (useQueryHintNolockAlways) {
			query.setLockOptions(LockOptions.READ);
		}
	}

	/**
	 * 
	 * @param query
	 */
	public void config(Criteria criteria) {
		criteria.setCacheable(cacheable);
	}
	
	/**
	 * 
	 * @param criteria
	 */
	public <T extends Model> void constructConditionInCriteria(Criteria criteria) {
		if (!conditionsHQL.isEmpty()) {
			for (ConditionHQL conditionNative : conditionsHQL) {
				conditionNative.constructCriteria(criteria);
			}
		}
		if (!conditions.isEmpty()) {
			for (Condition condition : conditions) {
				condition.constructCriteria(criteria);
			}
		}
		if (orders != null && !orders.isEmpty()) {
			for (Order order : orders) {
				order.constructCriteria(criteria);
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer getStart() {
		return (pageNo - 1) * perPage;
	}
	
	/**
	 * 
	 * @return
	 */
	public Integer getLimit() {
		return perPage;
	}

	/**
	 * 
	 * @param paginate
	 * @return
	 */
	public Filter clone(boolean paginate) {
		Filter clone = new Filter(sql, pageNo, perPage, cacheable);
		clone.add(conditions, orders);
		return clone;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (cacheable ? 1231 : 1237);
		result = prime * result + ((conditions == null) ? 0 : conditions.hashCode());
		result = prime * result + ((sql == null) ? 0 : sql.hashCode());
		result = prime * result + ((pageNo == null) ? 0 : pageNo.hashCode());
		result = prime * result + ((perPage == null) ? 0 : perPage.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Filter other = (Filter) obj;
		if (cacheable != other.cacheable)
			return false;
		if (conditions == null) {
			if (other.conditions != null)
				return false;
		} else if (!conditions.equals(other.conditions))
			return false;
		if (sql == null) {
			if (other.sql != null)
				return false;
		} else if (!sql.equals(other.sql))
			return false;
		if (pageNo == null) {
			if (other.pageNo != null)
				return false;
		} else if (!pageNo.equals(other.pageNo))
			return false;
		if (perPage == null) {
			if (other.perPage != null)
				return false;
		} else if (!perPage.equals(other.perPage))
			return false;
		return true;
	}
	
	
	public static class Builder {
		
		private boolean sqlSelect = false;
		private boolean sqlDelete = false;
		private boolean sqlCount = false;
		
		/**
		 * 
		 */
		private Builder() {
		}
		
		/**
		 * 
		 * @return builder
		 */
		public static Builder select() {
			Builder builder = new Builder();
			builder.sqlSelect = true;
			return builder;
		}
		
		/**
		 * 
		 * @return builder
		 */
		public static Builder delete() {
			Builder builder = new Builder();
			builder.sqlDelete = true;
			return builder;
		}
		
		/**
		 * 
		 * @return builder
		 */
		public static Builder count() {
			Builder builder = new Builder();
			builder.sqlCount = true;
			return builder;
		}

		/**
		 * 
		 * @return
		 */
		public Filter build() {
			String sql = null;
			int pageNo = 0;
			int perPage = 0;
			if (sqlSelect)
				sql = "from %s";
			if (sqlDelete)
				sql = "delete from %s";
			if (sqlCount) {
				sql = "select count(*) from %s";
				perPage = 1;
			}
			Filter filter = new Filter(sql, pageNo, perPage, false);
			return filter;
		}
		
	}

}
