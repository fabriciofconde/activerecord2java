package org.activerecord.hibernate.entity.event.listener;

import org.activerecord.hibernate.entity.event.DeleteLog;
import org.hibernate.HibernateException;
import org.hibernate.event.internal.DefaultDeleteEventListener;
import org.hibernate.event.spi.DeleteEvent;

/**
 * 
 * @author fabricio.conde
 *
 */
@SuppressWarnings("serial")
public class BeforeDeleteListener extends DefaultDeleteEventListener {

	@Override
	public void onDelete(DeleteEvent event) throws HibernateException {
		if (event.getObject() instanceof DeleteLog) {
        	DeleteLog deleteLog = (DeleteLog) event.getObject();
        	deleteLog.beforeDelete();
        }
    	
    	super.onDelete(event);
	}
    
}