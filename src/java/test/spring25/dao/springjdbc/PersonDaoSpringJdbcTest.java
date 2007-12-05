package test.spring25.dao.springjdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.sql.DataSource;

import org.dbunit.database.DatabaseConnection;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.operation.DatabaseOperation;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import tutorial.spring25.dao.PersonDao;
import tutorial.spring25.model.Person;

public class PersonDaoSpringJdbcTest {

	private static ApplicationContext ctx;
	private static PersonDao personDao;
	
	@BeforeClass
	public static void init(){
		ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
		personDao = (PersonDao) ctx.getBean("personDao");
	}
	
	@Before
	public void resetDatabase() throws Exception {
		final DataSource ds = (DataSource) ctx.getBean("dataSource");
		final Connection conn = ds.getConnection();
		DatabaseOperation.CLEAN_INSERT.execute(new DatabaseConnection(conn), new FlatXmlDataSet(new FileInputStream("fixtures/person.xml")));
		conn.close();
	}
	
	@Test
	public void testGetAll() {
		List<Person> result = personDao.getAll();
		assertEquals(2, result.size());
		Person endy = result.get(0);
		assertEquals("Endy Muhardin", endy.getName());
		assertEquals("endy.muhardin@gmail.com", endy.getEmail());
		assertEquals(100L, endy.getId());
	}

	@Test
	public void testGetById() {
		Person endy = personDao.getById(100L);
		assertNotNull(endy);
		assertEquals("Endy Muhardin", endy.getName());
		assertEquals("endy.muhardin@gmail.com", endy.getEmail());
		assertEquals(100L, endy.getId());
	}
	
	@Test
	public void testGetByNonExistentId(){
		assertNull(personDao.getById(99L));
	}

	@Test
	public void testSaveNewObject() throws Exception {
		Person dhiku = new Person();
		dhiku.setName("Hadikusuma Wahab");
		dhiku.setEmail("dhiku@gmail.com");
		assertNull(dhiku.getId());
		personDao.save(dhiku);
		assertNotNull(dhiku.getId());
		
		final DataSource ds = (DataSource) ctx.getBean("dataSource");
		final Connection conn = ds.getConnection();
		final PreparedStatement ps = conn.prepareStatement("select * from T_PERSON where id=?");
		ps.setLong(1, dhiku.getId());
		final ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		
		assertEquals(dhiku.getName(), rs.getString("name"));
		assertEquals(dhiku.getEmail(), rs.getString("email"));
		
		ps.close();
		rs.close();
		conn.close();
	}
	
	@Test
	public void testSaveExistingObject() throws Exception {
		Person endy = personDao.getById(100L);
		endy.setEmail("endymuhardin@yahoo.com");
		personDao.save(endy);
		
		final DataSource ds = (DataSource) ctx.getBean("dataSource");
		final Connection conn = ds.getConnection();
		final PreparedStatement ps = conn.prepareStatement("select * from T_PERSON where id=?");
		ps.setLong(1, 100L);
		final ResultSet rs = ps.executeQuery();
		assertTrue(rs.next());
		assertEquals("endymuhardin@yahoo.com", rs.getString("email"));
		
		ps.close();
		rs.close();
		conn.close();
	}

}
