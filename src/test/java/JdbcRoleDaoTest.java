package tests;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import jdbc.JdbcRoleDao;
import jdbc.JdbcUserDao;
import jdbc.PropertiesParser;
import jdbc.Role;
import jdbc.User;

import org.dbunit.Assertion;
import org.dbunit.DBTestCase;
import org.dbunit.PropertiesBasedJdbcDatabaseTester;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.h2.jdbcx.JdbcConnectionPool;
import org.junit.Test;

public class JdbcRoleDaoTest extends DBTestCase {
	private static JdbcConnectionPool connPool;
	JdbcRoleDao roleDao = new JdbcRoleDao();

	public JdbcRoleDaoTest() throws Exception {
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
			connPool.setMaxConnections(5);
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
	public void testCreate() throws Exception {
		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("ROLE");
		int i = actualTable.getRowCount();
		Role role2 = new Role();
		role2.setId(5L);
		role2.setName("guest");
		roleDao.create(role2);
		databaseDataSet = getConnection().createDataSet();
		actualTable = databaseDataSet.getTable("ROLE");
		assertEquals(i + 1, actualTable.getRowCount());
	}

	@Test
	public void testfindName() throws Exception {
		Role res = roleDao.findByName("admin");
		assertNotNull(res);
		assertEquals(res.getName(), "admin");
		res = roleDao.findByName("guest");
		assertNull(res);
	}

	@Test
	public void testRemove() throws Exception {
		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("ROLE");
		int i = actualTable.getRowCount();
		Role role = new Role();
		role.setId(1L);
		roleDao.remove(role);
		databaseDataSet = getConnection().createDataSet();
		actualTable = databaseDataSet.getTable("ROLE");
		assertEquals(i - 1, actualTable.getRowCount());
	}

	@Test
	public void testUpdate() throws Exception {
		Role role2 = new Role();
		role2.setId(2L);
		role2.setName("guest");
		roleDao.update(role2);
		IDataSet databaseDataSet = getConnection().createDataSet();
		ITable actualTable = databaseDataSet.getTable("ROLE");
		assertEquals("guest", actualTable.getValue(1, "NAME"));
	}

}
