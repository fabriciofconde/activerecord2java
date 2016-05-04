package org.activerecord.hibernate.entity.test;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activerecord.hibernate.entity.DummyModel;
import org.activerecord.hibernate.entity.Filter;
import org.activerecord.hibernate.entity.enums.Operator;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;


/**
 * 
 * @author fabricio.conde
 *
 */
@RunWith(JUnit4.class)
public class DummyModelTest extends ModelBaseTest {

	@Ignore
	@Test
	public void testSetAtributtes() {
		HashMap<String, Object> atributos = new HashMap<String, Object>();
		atributos.put("id", "id");
		atributos.put("column1", "column1");
		atributos.put("column2", "column2");
		atributos.put("column3", "column3");
		
		DummyModel dummyModel = new DummyModel();
		dummyModel.attributes(atributos);
		
		Assert.assertEquals(atributos.get("id"), dummyModel.getId());
		Assert.assertEquals(atributos.get("column1"), dummyModel.getColumn1());
		Assert.assertEquals(atributos.get("column2"), dummyModel.getColumn2());
		Assert.assertEquals(atributos.get("column3"), dummyModel.getColumn3());
	}
	
	@Ignore
	@Test
	public void testGetAtributtes() {
		DummyModel dummyModel = new DummyModel();
		dummyModel.setColumn1("column1");
		dummyModel.setColumn2("column2");
		dummyModel.setColumn3("column3");
		
		Map<String, Object> atributos = dummyModel.attributes();
		System.out.println(atributos);
		Assert.assertFalse(atributos.isEmpty());
	}
	
	@Ignore
	@Test
	public void shouldSave() {
		DummyModel dummyModel = new DummyModel();
		dummyModel.setId("shouldSave");
		dummyModel.setColumn1("Column1");
		dummyModel.setColumn2("Column2");
		dummyModel.setColumn3("Column3");
		Assert.assertTrue(dummyModel.save());
	}
	
	@Ignore
	@Test
	public void shouldUpdate() {
		DummyModel dummyModel = createDummyModel("shouldUpdate");
		String newVal = "update";
		dummyModel.setColumn1(newVal);
		Assert.assertTrue(dummyModel.update());
		Assert.assertEquals(newVal, dummyModel.getColumn1());
	}
	
	/*@Ignore
	@Test
	public void shouldSaveOrUpdate() {
		DummyModel dummyModel = new DummyModel();
		dummyModel.setId("shouldSaveOrUpdate");
		dummyModel.setColumn1("Column1");
		dummyModel.setColumn2("Column2");
		dummyModel.setColumn3("Column3");
		Assert.assertTrue(dummyModel.saveOrUpdate());
		dummyModel.setColumn3("Test");
		Assert.assertTrue(dummyModel.saveOrUpdate());
	}*/
	
	@Ignore
	@Test 
	public void testLoad() {
		String id = "testLoad";
		createDummyModel(id);
		DummyModel dummyModel = DummyModel.load(DummyModel.class, id);
		Assert.assertNotNull(dummyModel);
	}
	
	@Ignore
	@Test
	public void testGet() {
		String id = "testLoad";
		createDummyModel(id);
		DummyModel dummyModel = DummyModel.get(DummyModel.class, id);
		Assert.assertNotNull(dummyModel);
	}
	
	@Ignore
	@Test
	public void testFind() {
		String id = "testFind";
		createDummyModel(id);
		
		DummyModel dummyModelByParams = DummyModel.findBy(DummyModel.class, "id", id);
		Assert.assertNotNull(dummyModelByParams);
		
		Filter filter = Filter.Builder.select().build();
		filter.addCondition("id", id);
		DummyModel dummyModelByFilter = DummyModel.findBy(DummyModel.class, filter);
		Assert.assertNotNull(dummyModelByFilter);
	}
	
	@Ignore
	@Test
	public void testFindCondition() {
		Date date1 = new Date();
		String id = "testFindCondition";
		createDummyModel(id);
		Date date2 = new Date();
		
		DummyModel dummyModel = DummyModel.findByAttributeAndOperatorAndValue(DummyModel.class, "id", Operator.eq, id);
		Assert.assertNotNull(dummyModel);
		DummyModel dummyModel1 = DummyModel.findByAttributeAndOperatorAndValue(DummyModel.class, "date", Operator.between, date1, date2);
		Assert.assertNotNull(dummyModel1);
		DummyModel dummyModel2 = DummyModel.findByAttributeAndOperatorAndValue(DummyModel.class, "timestamp", Operator.between, date1, date2);
		Assert.assertNotNull(dummyModel2);
		DummyModel dummyModel3 = DummyModel.findByAttributeAndOperatorAndValue(DummyModel.class, "time", Operator.between, date1, date2);
		Assert.assertNotNull(dummyModel3);
		
		/*DummyModel dummyModel4 = DummyModel.findByAttributeAndOperatorAndValue(DummyModel.class, "column1", Operator.in, "Column1", "Column2");
		Assert.assertNotNull(dummyModel4);*/
	}
	
	@Ignore
	@Test
	public void testCount() {
		String id = "testCount";
		createDummyModel(id);
		
		Long count = DummyModel.count(DummyModel.class);
		Assert.assertNotNull(count);
		
		Long countByParams = DummyModel.countBy(DummyModel.class, "Column1", "Column1");
		Assert.assertNotNull(countByParams);
		
		Filter filter = Filter.Builder.count().build();
		filter.addCondition("Column1", "Column1");
		Long countByFilter = DummyModel.countBy(DummyModel.class, filter);
		Assert.assertNotNull(countByFilter);
	}
	
	@Ignore
	@Test
	public void testFindAll() {
		String id = "testFindAll";
		createDummyModel(id);
		
		List<DummyModel> listFindAll = DummyModel.findAll(DummyModel.class);
		Assert.assertFalse(listFindAll.isEmpty());
		
		List<DummyModel> listFindAllByParams = DummyModel.findAllBy(DummyModel.class, "Column1", "Column1");
		Assert.assertFalse(listFindAllByParams.isEmpty());
		
		Filter filter = Filter.Builder.select().build();
		filter.addCondition("Column1", "Column1");
		List<DummyModel> listFindAllByFilter = DummyModel.findAllBy(DummyModel.class, filter);
		Assert.assertFalse(listFindAllByFilter.isEmpty());
	}
	
	@Ignore
	@Test
	public void shouldExists() {
		String id = "FindAll";
		createDummyModel(id);
		Assert.assertTrue(DummyModel.exists(DummyModel.class, id));
	}
	
	@Ignore
	@Test
	public void shouldDelete() {
		createDummyModel("shouldDelete");
		Assert.assertTrue(DummyModel.delete(DummyModel.class, "shouldDelete"));
		
		createDummyModel("shouldDeleteParams");
		int amountByParams = DummyModel.deleteAllBy(DummyModel.class, "id", "shouldDeleteParams");
		Assert.assertEquals(1, amountByParams);
		
		createDummyModel("shouldDeleteFilter");
		Filter filter = Filter.Builder.delete().build();
		filter.addCondition("id", "shouldDeleteFilter");
		int amountByFilter = DummyModel.deleteAllBy(DummyModel.class, filter);
		Assert.assertEquals(1, amountByFilter);
		
		createDummyModel("shouldDeleteDomain");
		DummyModel dummyModel = DummyModel.load(DummyModel.class, "shouldDeleteDomain");
		Assert.assertTrue(dummyModel.delete());
	}
	
	@Ignore
	@Test
	public void testExample() {
		createDummyModel("testExample");
		
		DummyModel example = new DummyModel();
		example.setColumn3("Column3");
		
		DummyModel dummyModelExample = DummyModel.findOneByExample(DummyModel.class, example);
		Assert.assertNotNull(dummyModelExample);
		
		example.setId("testExample");
		List<DummyModel> listDummyModelExample = DummyModel.findAllByExample(DummyModel.class, example);
		Assert.assertFalse(listDummyModelExample.isEmpty());
		
		example.setColumn1("naoTem");
		List<DummyModel> listDummyModelExample2 = DummyModel.findAllByExample(DummyModel.class, example);
		Assert.assertTrue(listDummyModelExample2.isEmpty());
	}
	
	private DummyModel createDummyModel(final String id) {
		boolean exist = DummyModel.exists(DummyModel.class, id);
		if (!exist) {
			DummyModel dummyModel = new DummyModel();
			dummyModel.setId(id);
			dummyModel.setColumn1("Column1");
			dummyModel.setColumn2("Column2");
			dummyModel.setColumn3("Column3");
			dummyModel.save();
			DummyModel dummyParent = new DummyModel();
			dummyParent.setId(id.concat("Parent"));
			dummyParent.setColumn1("ParentColumn1");
			dummyParent.setColumn2("ParentColumn2");
			dummyParent.setColumn3("ParentColumn3");
			dummyParent.save();
			dummyModel.setParent(dummyParent);
			dummyModel.update();
			dummyModel.flush();
			
			return dummyModel;
		} 
		return DummyModel.get(DummyModel.class, id);
	}
	
}
