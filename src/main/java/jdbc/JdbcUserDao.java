package jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class JdbcUserDao extends AbsctractJdbcDao implements UserDao {
	private static final Log log = LogFactory.getLog(JdbcUserDao.class);

	public JdbcUserDao() {
	}

	public void create(User user) {
		Connection conn = null;
		PreparedStatement prepStat = null;
		if (user == null) {
			log.error("Error! User input argument of the method update is null");
			throw new NullPointerException(
					"Error! User input argument of the method update is null");
		}
		try {
			log.debug("Try to create connection");
			conn = createConnection();
//			prepStat = conn.prepareStatement("SELECT * FROM USER WHERE ID=?");
//			prepStat.setLong(1, user.getId());
//			ResultSet rs = prepStat.executeQuery();
//			if (rs.next()) {
//				log.error("The User " + user.getId() + " already exist!");
//				throw new NullPointerException("The User " + user.getId()
//						+ " already exist!");
//			}
			log.debug("Try to create connection");
			prepStat = conn
					.prepareStatement("INSERT INTO USER VALUES(null, ?, ?, ?, ?, ?, ? , ?)");
//			prepStat.setLong(1, (Long) null);
			prepStat.setString(1, user.getLogin());
			prepStat.setString(2, user.getPassword());
			prepStat.setString(3, user.getEmail());
			prepStat.setString(4, user.getFirstName());
			prepStat.setString(5, user.getLastName());
			prepStat.setDate(6, user.getBirthday());
			prepStat.setLong(7, user.getRole());
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

	public void update(User user) {
		Connection conn = null;
		PreparedStatement pst = null;
		if (user == null) {
			log.error("Error! User input argument of the method update is null");
			throw new NullPointerException(
					"Error! User input argument of the method update is null");
		}
		try {
			log.debug("Try to create connection");
			conn = createConnection();
//			pst = conn.prepareStatement("SELECT * FROM USER WHERE ID=?");
//			pst.setLong(1, user.getId());
//			ResultSet prepStat = pst.executeQuery();
//			if (!prepStat.next()) {
//				log.error("User with the ID= " + user.getId()
//						+ " does not exist in the base");
//				this.create(user);
//				return;
//			}
			log.debug("Try to create connection");
			pst = conn
					.prepareStatement("UPDATE USER SET LOGIN=?, PASSWORD=?, EMAIL=?, "
							+ "FIRSTNAME=?, LASTNAME=?, BIRTHDAY=?, ROLEID=? WHERE ID=?");
			pst.setString(1, user.getLogin());
			pst.setString(2, user.getPassword());
			pst.setString(3, user.getEmail());
			pst.setString(4, user.getFirstName());
			pst.setString(5, user.getLastName());
			pst.setDate(6, (Date)user.getBirthday());
			pst.setLong(7, user.getRole());
			pst.setLong(8, user.getId());
			log.debug("executeUpdate prepareStatement to DB");
			pst.executeUpdate();
		} catch (Exception e) {
			log.error("Can not execute executeUpdate ", e);
			e.printStackTrace();
		} finally {
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void remove(User user) {
		Connection conn = null;
		PreparedStatement pst = null;
		if (user == null) {
			log.error("Error! User input argument of the method update is null");
			throw new NullPointerException(
					"Error! User input argument of the method update is null");
		}
		try {
			log.debug("Try to create connection");
			conn = createConnection();
			pst = conn.prepareStatement("DELETE FROM USER WHERE ID=?");
			pst.setLong(1, user.getId());
			log.debug("executeUpdate prepareStatement to DB");
			pst.executeUpdate();
		} catch (Exception e) {
			log.error("Can not execute executeUpdate ", e);
			e.printStackTrace();
		} finally {
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public List<User> findAll() {
		Connection conn = null;
		PreparedStatement pst = null;
		List<User> result = new ArrayList<User>();
		try {
			log.debug("Try to create connection");
			conn = createConnection();
			pst = conn.prepareStatement("SELECT * FROM USER");
			log.debug("executeQuery prepareStatement to DB");
			ResultSet res = pst.executeQuery();
			while (res.next()) {
				User user = new User();
				user.setId(res.getLong("id"));
				user.setLogin(res.getString("login"));
				user.setPassword(res.getString("password"));
				user.setBirthday(res.getDate("birthday"));
				user.setEmail(res.getString("email"));
				user.setFirstName(res.getString("firstname"));
				user.setLastName(res.getString("lastname"));
				user.setRole(res.getLong("roleid"));
				result.add(user);
			}
		} catch (Exception e) {
			log.error("Can not execute executeUpdate ", e);
			e.printStackTrace();
			return result;
		} finally {
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	public User findByLogin(String login) {
		Connection conn = null;
		PreparedStatement pst = null;
		User user = null;
		if (login == null) {
			log.error("Error! login input argument of the method update is null");
			throw new NullPointerException(
					"Error! login input argument of the method update is null");
		}
		try {
			log.debug("Try to create connection");
			conn = createConnection();
			pst = conn.prepareStatement("SELECT * FROM USER WHERE LOGIN=?");
			pst.setString(1, login);
			log.debug("executeQuery prepareStatement to DB");
			ResultSet rs = pst.executeQuery();
			if (!rs.next()) {
				return null;
			}
			user = new User();
			user.setId(rs.getLong("id"));
			user.setLogin(rs.getString("login"));
			user.setPassword(rs.getString("password"));
			user.setEmail(rs.getString("email"));
			user.setFirstName(rs.getString("firstname"));
			user.setLastName(rs.getString("lastname"));
			user.setBirthday(rs.getDate("birthday"));
			user.setRole(rs.getLong("roleid"));
		} catch (Exception e) {
			log.error("Can not execute executeUpdate ", e);
			e.printStackTrace();
		} finally {
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return user;
	}

	public User findByEmail(String email) {
		Connection conn = null;
		PreparedStatement pst = null;
		User user = null;
		if (email == null) {
			log.error("Error! email input argument of the method update is null");
			throw new NullPointerException(
					"Error! email input argument of the method update is null");
		}
		try {
			log.debug("Try to create connection");
			conn = createConnection();
			pst = conn.prepareStatement("SELECT * FROM USER WHERE EMAIL=?");
			pst.setString(1, email);
			log.debug("executeQuery prepareStatement to DB");
			ResultSet rs = pst.executeQuery();
			if (!rs.next()) {
				return null;
			}
			user = new User();
			user.setId(rs.getLong("id"));
			user.setLogin(rs.getString("login"));
			user.setPassword(rs.getString("password"));
			user.setEmail(rs.getString("email"));
			user.setFirstName(rs.getString("firstname"));
			user.setLastName(rs.getString("lastname"));
			user.setBirthday(rs.getDate("birthday"));
			user.setRole(rs.getLong("roleid"));
		} catch (Exception e) {
			log.error("Can not execute executeUpdate ", e);
			e.printStackTrace();
		} finally {
			try {
				pst.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return user;
	}

}
