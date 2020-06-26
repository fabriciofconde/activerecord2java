package org.activerecord.hibernate.entity.event.listener;

import org.activerecord.hibernate.entity.event.InsertLog;
import org.hibernate.HibernateException;
import org.hibernate.event.PersistEvent;
import org.hibernate.event.def.DefaultPersistEventListener;

/**
 * 
 * @author fabricio.conde
 *
 */
@SuppressWarnings("serial")
public class BeforePersistListener extends DefaultPersistEventListener {

	@Override
	public void onPersist(PersistEvent event) throws HibernateException {
		if (event.getObject() instanceof InsertLog) {
        	InsertLog insertLog = (InsertLog) event.getObject();
        	insertLog.beforeInsert();
        }
		
		super.onPersist(event);
	}
    
}