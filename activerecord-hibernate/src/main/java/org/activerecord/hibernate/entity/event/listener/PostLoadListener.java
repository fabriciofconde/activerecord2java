package org.activerecord.hibernate.entity.event.listener;

import org.activerecord.hibernate.entity.event.LoadLog;
import org.hibernate.event.PostLoadEvent;
import org.hibernate.event.def.DefaultPostLoadEventListener;

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