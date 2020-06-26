package org.activerecord.hibernate.entity.event.listener;

import org.activerecord.hibernate.entity.event.UpdateLog;
import org.hibernate.HibernateException;
import org.hibernate.event.MergeEvent;
import org.hibernate.event.def.DefaultMergeEventListener;

/**
 * 
 * @author fabricio.conde
 *
 */
@SuppressWarnings("serial")
public class BeforeMergeListener extends DefaultMergeEventListener {

	@Override
	public void onMerge(MergeEvent event) throws HibernateException {
		if (event.getOriginal() instanceof UpdateLog) {
        	UpdateLog updateLog = (UpdateLog) event.getOriginal();
        	updateLog.beforeUpdate();
        }
		
		super.onMerge(event);
	}
    
}