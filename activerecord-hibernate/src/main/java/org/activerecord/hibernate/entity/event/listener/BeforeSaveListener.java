package org.activerecord.hibernate.entity.event.listener;

import org.activerecord.hibernate.entity.event.InsertLog;
import org.hibernate.event.SaveOrUpdateEvent;
import org.hibernate.event.def.DefaultSaveEventListener;

/**
 * 
 * @author fabricio.conde
 *
 */
@SuppressWarnings("serial")
public class BeforeSaveListener extends DefaultSaveEventListener {
    
    @Override
    public void onSaveOrUpdate(SaveOrUpdateEvent event) {
    	if (event.getObject() instanceof InsertLog) {
        	InsertLog insertLog = (InsertLog) event.getObject();
        	insertLog.beforeInsert();
        }
    	
    	super.onSaveOrUpdate(event);
    }
    
}