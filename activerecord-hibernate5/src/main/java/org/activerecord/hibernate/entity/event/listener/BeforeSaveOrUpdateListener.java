package org.activerecord.hibernate.entity.event.listener;

import org.activerecord.hibernate.entity.Model;
import org.activerecord.hibernate.entity.event.InsertLog;
import org.activerecord.hibernate.entity.event.UpdateLog;
import org.hibernate.event.internal.DefaultSaveOrUpdateEventListener;
import org.hibernate.event.spi.SaveOrUpdateEvent;

/**
 * 
 * @author fabricio.conde
 *
 */
@SuppressWarnings("serial")
public class BeforeSaveOrUpdateListener extends DefaultSaveOrUpdateEventListener {
    
    @Override
    public void onSaveOrUpdate(SaveOrUpdateEvent event) {
        InsertLog insertLog = null;
        UpdateLog updateLog = null;
        
        if (event.getObject() instanceof Model) {
        	Model model = (Model) event.getObject();
        	
        	if (model.getId() != null)
        		updateLog = (UpdateLog) event.getObject();
        	else
        		insertLog = (InsertLog) event.getObject();
        	
        	if (insertLog != null)
        		insertLog.beforeInsert();
        	if (updateLog != null)
        		updateLog.beforeUpdate();
        }
        
        super.onSaveOrUpdate(event);
    }
    
}