package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JdbcRoleDao extends AbsctractJdbcDao implements RoleDao {
	private static final Log log = LogFactory.getLog(JdbcUserDao.class);

	public JdbcRoleDao() {
	}

	public void create(Role role) {
		Connection conn = null;
		PreparedStatement prepStat = null;
		if (role == null || role.getId() == 0) {
			log.error("Error! role input argument of the method update is null or Id = 0 ");
			throw new IllegalArgumentException(
					"Error! role input argument of the method update is null or Id = 0 ");
		}
		try {
			log.debug("Try to create connection");
			conn = createConnection();
			prepStat = conn.prepareStatement("SELECT * FROM ROLE WHERE ID=?");
			prepStat.setLong(1, role.getId());
			ResultSet rs = prepStat.executeQuery();
			if (rs.next()) {
				log.error("The User " + role.getId() + " already exist!");
				throw new SQLException("The User " + role.getId()
						+ " already exist!");
			}
			prepStat = conn.prepareStatement("INSERT INTO ROLE VALUES(?, ?)");
			prepStat.setLong(1, role.getId());
			prepStat.setString(2, role.getName());
			log.debug("executeUpdate prepareStatement to DB");
			prepStat.executeUpdate();
		} catch (Exception e) {
			log.error("Can not execute executeUpdate ", e);
			e.printStackTrace();
		} finally {
			try {
				prepStat.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void update(Role role) {
		Connection conn = null;
		PreparedStatement prepStat = null;
		if (role == null) {
			log.error("Error! role input argument of the method update is null");
			throw new NullPointerException(
					"Error! role input argument of the method update is null");
		}
		try {
			log.debug("Try to create connection");
			conn = createConnection();
			prepStat = conn.prepareStatement("SELECT * FROM ROLE WHERE ID=?");
			prepStat.setLong(1, role.getId());
			ResultSet rs = prepStat.executeQuery();
			if (!rs.next()) {
				this.create(role);
				return;
			}
			prepStat = conn
					.prepareStatement("UPDATE ROLE SET NAME=? WHERE ID=?");
			prepStat.setString(1, role.getName());
			prepStat.setLong(2, role.getId());
			log.debug("executeUpdate prepareStatement to DB");
			prepStat.executeUpdate();
		} catch (Exception e) {
			log.error("Can not execute executeUpdate ", e);
			e.printStackTrace();
		} finally {
			try {
				prepStat.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void remove(Role role) {
		Connection conn = null;
		PreparedStatement prepStat = null;
		if (role == null) {
			log.error("Error! role input argument of the method update is null");
			throw new NullPointerException();
		}
		try {
			log.debug("Try to create connection");
			conn = createConnection();
			prepStat = conn.prepareStatement("DELETE FROM ROLE WHERE ID=?");
			prepStat.setLong(1, role.getId());
			log.debug("executeUpdate prepareStatement to DB");
			prepStat.executeUpdate();
		} catch (Exception e) {
			log.error("Can not execute executeUpdate ", e);
			e.printStackTrace();
		} finally {
			try {
				prepStat.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Role findByName(String string) {
		Connection conn = null;
		PreparedStatement prepStat = null;
		if (string == null) {
			log.error("Error! role input argument of the method update is null");
			throw new NullPointerException(
					"Error! role input argument of the method update is null");
		}
		try {
			log.debug("Try to create connection");
			conn = createConnection();
			prepStat = conn.prepareStatement("SELECT * FROM ROLE WHERE NAME=?");
			prepStat.setString(1, string);
			log.debug("executeQuery prepareStatement to DB");
			ResultSet rs = prepStat.executeQuery();
			if (rs.next()) {
				Role role = new Role();
				role.setId(rs.getLong("ID"));
				role.setName(rs.getString("NAME"));
				return role;
			}
		} catch (Exception e) {
			log.error("Can not execute executeUpdate ", e);
			e.printStackTrace();
		} finally {
			try {
				prepStat.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}
