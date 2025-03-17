

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database_Connectivity {
	static final String URL = "jdbc:mysql://localhost:3306/CabManagement";
	static final String USERNAME = "root";
	static final String PASSWORD = "261226A";
	public static Connection createConnection(){
		Connection connection = null;
		if(connection == null) {
			try {
				connection=DriverManager.getConnection(URL,USERNAME,PASSWORD);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	
	return connection;
	}
}
