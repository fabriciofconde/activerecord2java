package org.activerecord.hibernate.entity.test;

import org.activerecord.hibernate.config.test.HibernateConfigImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;


/**
 * 
 * @author fabricio.conde
 *
 */
public class ModelBaseTest {

	private static HibernateConfigImpl hibernateConfigImpl = new HibernateConfigImpl();
	
	@BeforeClass
	public static void testSetup() throws Exception {
		hibernateConfigImpl.config();
	}
	
	@AfterClass
	public static void testCleanUp() {
		hibernateConfigImpl.destroy();
	}
	
	@Before
	public void setUp() {
	}
	
	@After
	public void setDown() {
	}
	
}
