package org.activerecord.hibernate.entity;

import java.util.Arrays;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.Type;

/**
 * 
 * @author fabricio.conde
 *
 */
public class ConditionHQL {

	private String hqlRestriction;
	private Object[] values;
	private Type[] types;
	
	
	/**
	 * Default constructor
	 */
	public ConditionHQL() {
	}
	
	/**
	 * 
	 * @param hqlRestriction
	 * @param value
	 * @param type
	 */
	public ConditionHQL(String hqlRestriction, Object value, Type type) {
		this.hqlRestriction = hqlRestriction;
		this.values = new Object[] { value };
		this.types = new Type[] { type };
	}
	
	/**
	 * @param sqlRestriction
	 * @param values
	 * @param types
	 */
	public ConditionHQL(String hqlRestriction, Object[] values, Type[] types) {
		this.hqlRestriction = hqlRestriction;
		this.values = values;
		this.types = types;
	}
	
	
	public String constructQuery() {
		return hqlRestriction;
	}
	
	public int setParameters(Query query, int position) {
		for (Object value : values) {
			query.setParameter(position++, value);
		}
		return position;
	}
	
	public <T extends Model> Criterion constructCriteria(Criteria criteria) {
		Criterion criterion = Restrictions.sqlRestriction(hqlRestriction, values, types);
		criteria.add(criterion);
		return criterion;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hqlRestriction == null) ? 0 : hqlRestriction.hashCode());
		result = prime * result + Arrays.deepHashCode(values);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ConditionHQL other = (ConditionHQL) obj;
		if (hqlRestriction == null) {
			if (other.hqlRestriction != null)
				return false;
		} else if (!hqlRestriction.equals(other.hqlRestriction))
			return false;
		if (!Arrays.deepEquals(values, other.values))
			return false;
		return true;
	}

}
