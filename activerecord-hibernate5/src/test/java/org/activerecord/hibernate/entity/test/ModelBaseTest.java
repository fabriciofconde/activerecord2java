package org.activerecord.hibernate.entity.test;

import org.activerecord.hibernate.config.test.HibernateConfigImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;


/**
 * 
 * @author fabricio.conde
 *
 */
public class ModelBaseTest {

	private static HibernateConfigImpl hibernateConfigImpl = new HibernateConfigImpl();
	
	@Ignore
	@BeforeClass
	public static void testSetup() throws Exception {
		hibernateConfigImpl.config();
	}
	
	@Ignore
	@AfterClass
	public static void testCleanUp() {
		hibernateConfigImpl.destroy();
	}
	
	@Ignore
	@Before
	public void setUp() {
	}
	
	@Ignore
	@After
	public void setDown() {
	}
	
}
