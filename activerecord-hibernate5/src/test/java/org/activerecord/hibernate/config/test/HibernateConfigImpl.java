package org.activerecord.hibernate.config.test;

import org.activerecord.hibernate.config.HibernateConfig;
import org.activerecord.hibernate.entity.BaseObject;
import org.hibernate.Session;

/**
 * 
 * @author fabricio.conde
 *
 */
public class HibernateConfigImpl implements HibernateConfig {

	@Override
	public Session getSession() {
		return HibernateUtil.getCurrentSession();
	}

	@Override
	public void config() {
		BaseObject.setHibernate(this);
	}
	
	public void destroy() {
		HibernateUtil.closeSession();
	}

}
