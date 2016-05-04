package org.activerecord.hibernate.entity.enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author fabricio.conde
 *
 */
public enum Operator {

	eq("=") {
		@Override
		protected Criterion createCriterion(String propertyName, Object... parameters) {
			return Restrictions.eq(propertyName, parameters[0]);
		} 
	},
	
	ne("!=") {
		@Override
		protected Criterion createCriterion(String propertyName, Object... parameters) {
			return Restrictions.ne(propertyName, parameters[0]);
		}
	},
	
	le("<=") {
		@Override
		protected Criterion createCriterion(String propertyName, Object... parameters) {
			return Restrictions.le(propertyName, parameters[0]);
		}
	},
	
	lt("<") {
		@Override
		protected Criterion createCriterion(String propertyName, Object... parameters) {
			return Restrictions.lt(propertyName, parameters[0]);
		}
	},
	
	ge(">=") {
		@Override
		protected Criterion createCriterion(String propertyName, Object... parameters) {
			return Restrictions.ge(propertyName, parameters[0]);
		}
	}, 
	
	gt(">") {
		@Override
		protected Criterion createCriterion(String propertyName, Object... parameters) {
			return Restrictions.gt(propertyName, parameters[0]);
		}
	},
	
	like("like") {
		@Override
		protected Criterion createCriterion(String propertyName, Object... parameters) {
			return Restrictions.like(propertyName, parameters[0]);
		}
	},
	
	between("between") {
		@Override
		public String constructCondition(String propertyName) {
			return String.format("%s between :from%s and :to%s", propertyName, propertyName, propertyName);
		}
		
		@Override
		public Criterion constructCondition(String propertyName, Object... parameters) {
			return createCriterion(propertyName, ":from" + propertyName, ":to" + propertyName);
		}
		
		@Override
		public void setParameters(Query query, String name, Object value) {
			Object[] values = null;
			if (value instanceof Object[]) {
				values = (Object[]) value;
			}
			if (values == null || values.length != 2) {
				throw new IllegalArgumentException("Value - " + value + " should be an array of size 2");
			}
			super.setParameters(query, "from" + name, values[0]);
			super.setParameters(query, "to" + name, values[1]);
		}
		
		@Override
		protected Criterion createCriterion(String property, Object... parameters) {
			return Restrictions.between(property, parameters[0], parameters[1]);
		}
	}, 
	
	in("in") {
		@Override
		protected Criterion createCriterion(String property, Object... parameters) {
			return Restrictions.in(property, parameters);
		}

		@Override
		public void setParameters(Query query, String name, Object value) {
			if (value instanceof Object[]) {
				value = Arrays.asList((Object[]) value);
			}
			ArrayList<Object> list = new ArrayList<Object>();
			for (Object val : (List<?>) value) {
				//list.add(ConvertUtil.convert(val, param.getParameterType()));
				list.add(val);
			}
			query.setParameterList(name, list);
		}
	},
	
	isNotNull("is not null") {
		@Override
		protected Criterion createCriterion(String propertyName, Object... parameters) {
			return Restrictions.isNotNull(propertyName);
		}

		@Override
		public void setParameters(Query query, String name, Object value) {
			
		}

		@Override
		public String constructCondition(String propertyName) {
			return String.format(" %s %s", propertyName, getOperator());
		}
	};
	
	
	private String operator;
	
	private Operator(String operator) {
		this.operator = operator;
	}
	
	protected String getOperator() {
		return operator;
	}

	public String constructCondition(String propertyName) {
		return String.format(" %s %s :%s", propertyName, operator, propertyName);
	}
	
	public Criterion constructCondition(String propertyName, Object... parameters) {
		return createCriterion(propertyName, parameters);
	}
	
	protected abstract Criterion createCriterion(String propertyName, Object... parameters);
	
	public void setParameters(Query query, String name, Object value) {
		//value = ConvertUtil.convert(value, param.getParameterType());
		query.setParameter(name, value);
	}
	
}
