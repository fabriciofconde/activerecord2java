package org.activerecord.hibernate.config;

import org.hibernate.Session;

/**
 * 
 * @author fabricio.conde
 *
 */
public interface HibernateConfig {

	/**
	 * 
	 * @return
	 */
	Session getSession();
	
	/**
	 * 
	 */
	void config();
	
	
	/**
	 * 
	 * @return
	 */
	boolean useQueryHintNolockAlways();
	
}
