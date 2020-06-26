package org.activerecord.hibernate.entity;

import org.hibernate.criterion.Example.PropertySelector;
import org.hibernate.type.Type;

/**
 * 
 * @author fabricio.conde
 *
 */
@SuppressWarnings("serial")
public class PropertySelectorImpl implements PropertySelector {

	@Override
	public boolean include(Object propertyValue, String propertyName, Type type) {
		if (propertyValue != null && propertyValue instanceof String) {
            return !((String) propertyValue).trim().isEmpty();
        }           
        return propertyValue != null;
	}

}
