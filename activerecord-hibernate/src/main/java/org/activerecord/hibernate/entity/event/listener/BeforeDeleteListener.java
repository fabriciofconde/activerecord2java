package org.activerecord.hibernate.entity.event.listener;

import org.activerecord.hibernate.entity.event.DeleteLog;
import org.hibernate.event.DeleteEvent;
import org.hibernate.event.def.DefaultDeleteEventListener;

/**
 * 
 * @author fabricio.conde
 *
 */
@SuppressWarnings("serial")
public class BeforeDeleteListener extends DefaultDeleteEventListener {
    
    @Override
    public void onDelete(DeleteEvent event) {
    	if (event.getObject() instanceof DeleteLog) {
        	DeleteLog deleteLog = (DeleteLog) event.getObject();
        	deleteLog.beforeDelete();
        }
    	
    	super.onDelete(event);
    }
    
}