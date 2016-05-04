package org.activerecord.hibernate.entity.event.listener;

import org.activerecord.hibernate.entity.event.UpdateLog;
import org.hibernate.event.internal.DefaultUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;

/**
 * 
 * @author fabricio.conde
 *
 */
@SuppressWarnings("serial")
public class BeforeUpdateListener extends DefaultUpdateEventListener {
    
    @Override
    public void onSaveOrUpdate(SaveOrUpdateEvent event) {
    	if (event.getObject() instanceof UpdateLog) {
        	UpdateLog updateLog = (UpdateLog) event.getObject();
        	updateLog.beforeUpdate();
        }
    	
    	super.onSaveOrUpdate(event);
    }
    
}