package org.activerecord.hibernate.entity.event.listener;

import org.activerecord.hibernate.entity.event.LoadLog;
import org.hibernate.HibernateException;
import org.hibernate.event.RefreshEvent;
import org.hibernate.event.def.DefaultRefreshEventListener;

/**
 * 
 * @author fabricio.conde
 *
 */
@SuppressWarnings("serial")
public class RefreshListener extends DefaultRefreshEventListener {

	@Override
	public void onRefresh(RefreshEvent event) throws HibernateException {
		
		if (event.getObject() instanceof LoadLog) {
			LoadLog loadLog = (LoadLog) event.getObject();
			loadLog.afterLoad();
        }
		
		super.onRefresh(event);
	}

    
}