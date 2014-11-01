package jdbc;

import java.sql.Connection;

import org.h2.jdbcx.JdbcConnectionPool;

public abstract class AbsctractJdbcDao {
	private static JdbcConnectionPool conPool;

	public Connection createConnection() {
		Connection connect = null;
		try {
			if (conPool == null) {
				Class.forName(PropertiesParser.getDriver());
				conPool = JdbcConnectionPool
						.create(PropertiesParser.getUrl(),
								PropertiesParser.getLogin(),
								PropertiesParser.getPass());
				conPool.setMaxConnections(10);
			}
			connect = conPool.getConnection();
		} catch (Exception e) {
			throw new RuntimeException("error connection", e);
		}
		return connect;
	}

}
