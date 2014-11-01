

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import jdbc.JdbcUserDao;
import jdbc.PropertiesParser;
import jdbc.User;

import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Test;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;

public class JdbcUserDaoTest extends DBTestCase {
	private static JdbcConnectionPool connPool;
	JdbcUserDao userDao = new JdbcUserDao();

	public JdbcUserDaoTest() throws Exception {
		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_DRIVER_CLASS,
				PropertiesParser.getDriver());
		System.setProperty(
				PropertiesBasedJdbcDatabaseTester.DBUNIT_CONNECTION_URL,
				PropertiesParser.getUrl());
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_USERNAME,
				PropertiesParser.getLogin());
		System.setProperty(PropertiesBasedJdbcDatabaseTester.DBUNIT_PASSWORD,
				PropertiesParser.getPass());

		if (connPool == null) {
			Class.forName(PropertiesParser.getDriver());
			connPool = JdbcConnectionPool.create(PropertiesParser.getUrl(),
					PropertiesParser.getLogin(), PropertiesParser.getPass());
			Connection conn = connPool.getConnection();
			Statement sst = conn.createStatement();
			sst.executeUpdate("create table if not exists user(id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY, login VARCHAR NOT NULL UNIQUE,"
					+ " password VARCHAR, email VARCHAR NOT NULL UNIQUE, firstname VARCHAR, lastname VARCHAR, birthday DATE, roleid BIGINT); create table if not exists role (id BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY, name VARCHAR NOT NULL UNIQUE);");
			sst.close();
			conn.close();
		}
	}

	@Override
	protected IDataSet getDataSet() throws Exception {
		return new FlatXmlDataSetBuilder().build(new FileInputStream(
				"src/resources/dataset.xml"));
	}

	@Test
	public void testfindAll() throws Exception {
		List<User> res = userDao.findAll();
		assertTrue(res != null);
		assertEquals(2, res.size());
	}

	@Test
	public void testCreate() throws Exception {
		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("USER");
		int i = actualTable.getRowCount();
		User user4 = new User();
		user4.setId(5L);
		user4.setLogin("log5");
		user4.setPassword("pas5");
		user4.setEmail("email5");
		user4.setRole(5L);
		userDao.create(user4);
		databaseDataSet = getConnection().createDataSet();
		actualTable = databaseDataSet.getTable("USER");
		assertEquals(i + 1, actualTable.getRowCount());
	}

	@Test
	public void testRemove() throws Exception {
		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("USER");
		int i = actualTable.getRowCount();
		User user2 = new User();
		user2.setId(1L);
		user2.setLogin("log5");
		user2.setPassword("pas5");
		user2.setEmail("email5");
		user2.setRole(5L);
		userDao.remove(user2);
		databaseDataSet = getConnection().createDataSet();
		actualTable = databaseDataSet.getTable("USER");
		assertEquals(i - 1, actualTable.getRowCount());
	}

	@Test
	public void testfindByLogin() throws Exception {
		User res = userDao.findByLogin("log1");
		assertNotNull(res);
		assertEquals(res.getLogin(), "log1");
	}

	@Test
	public void testfindByEmail() throws Exception {
		User res = userDao.findByEmail("mail2");
		assertNotNull(res);
		assertEquals(res.getEmail(), "mail2");
	}

	@Test
	public void testUpdate() throws Exception {
		User user2 = new User();
		user2.setId(1L);
		user2.setLogin("log5");
		user2.setPassword("pas5");
		user2.setEmail("mail5");
		try {
			userDao.update(user2);
			IDataSet databaseDataSet = getConnection().createDataSet();
			ITable actualTable = databaseDataSet.getTable("USER");
			assertEquals("log5", actualTable.getValue(0, "LOGIN"));
		} catch (Exception e) {
			fail("Error! role input argument of the method update is null or Id = 0 ");
		}
	}

}
