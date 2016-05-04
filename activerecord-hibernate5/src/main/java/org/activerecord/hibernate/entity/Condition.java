package org.activerecord.hibernate.entity;

import org.activerecord.hibernate.entity.enums.Operator;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;


/**
 * 
 * @author fabricio.conde
 *
 */
public class Condition {

	private String name;
	private Object value;
	private Operator operator;
	
	/**
	 * Default constructor
	 */
	public Condition() {
	}
	
	/**
	 * @param name
	 * @param operator
	 * @param value
	 */
	public Condition(String name, Operator operator, Object value) {
		this.name = name;
		this.value = value;
		this.operator = operator;
	}
	
	/**
	 * @param name
	 * @param value
	 */
	public Condition(String name, Object value) {
		this(name, Operator.eq, value);
	}
	
	/**
	 * @return the operator
	 */
	public Operator getOperator() {
		return operator;
	}

	/**
	 * @param operator the operator to set
	 */
	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public String constructQuery() {
		return operator.constructCondition(name);
	}
	
	public <T extends Model> Criterion constructCriteria(Criteria criteria) {
		Criterion criterion = operator.constructCondition(name, value);
		criteria.add(criterion);
		return criterion;
	}
	
	public void setParameters(Query query) {
		operator.setParameters(query, name, value);
	}
	
	/*private <T, S> Path<?> getPath(From<T, S> root, String name) {
		int index = name.indexOf(".");
		if (index > 0 ) {
			String attribute = name.substring(0, index);
			From<S, ?> join = getJoin(attribute, root.getJoins());
			if (join == null) {
				join = root.join(attribute);
			}
			return getPath(join, name.substring(index + 1));
		} else {
			return root.get(name);
		}
	}*/
	
	/*private <T> Join<T, ?> getJoin(String name, Set<Join<T, ?>> joins) {
		for (Join<T, ?> join : joins) {
			if (join.getAttribute().getName().equals(name)) {
				return join;
			}
		}
		return null;
	}*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((operator == null) ? 0 : operator.hashCode());
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
		Condition other = (Condition) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (operator != other.operator)
			return false;
		return true;
	}
	
}
