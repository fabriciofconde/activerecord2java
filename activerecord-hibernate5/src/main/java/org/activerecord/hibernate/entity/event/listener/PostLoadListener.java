package org.activerecord.hibernate.entity.event.listener;

import org.activerecord.hibernate.entity.event.LoadLog;
import org.hibernate.event.internal.DefaultPostLoadEventListener;
import org.hibernate.event.spi.PostLoadEvent;

/**
 * 
 * @author fabricio.conde
 *
 */
@SuppressWarnings("serial")
public class PostLoadListener extends DefaultPostLoadEventListener {

	@Override
	public void onPostLoad(PostLoadEvent event) {
		
		if (event.getEntity() instanceof LoadLog) {
			LoadLog loadLog = (LoadLog) event.getEntity();
			loadLog.afterLoad();
        }
		
		super.onPostLoad(event);
	}

    
}