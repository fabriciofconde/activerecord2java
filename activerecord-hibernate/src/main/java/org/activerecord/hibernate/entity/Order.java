package org.activerecord.hibernate.entity;

import org.hibernate.Criteria;




/**
 * 
 * @author fabricio.conde
 *
 */
public class Order {

	private String name;
	private boolean asc;
	
	/**
	 * 
	 * @param name
	 * @param asc
	 */
	public Order(String name, boolean asc) {
		if (name == null)
			throw new IllegalArgumentException("Argument name is mandatory");
		this.name = name;
		this.asc = asc;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the asc
	 */
	public boolean isAsc() {
		return asc;
	}
	
	/**
	 * 
	 * @return
	 */
	public String constructQuery() {
		return String.format("%s %s", name, asc ? "asc " : "desc ");
	}
	
	/**
	 * 
	 * @param criteria
	 * @return
	 */
	public <T extends Model> Criteria constructCriteria(Criteria criteria) {
		criteria.addOrder(asc ? org.hibernate.criterion.Order.asc(name) : org.hibernate.criterion.Order.desc(name));
		return criteria;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (asc ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Order other = (Order) obj;
		if (asc != other.asc)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
