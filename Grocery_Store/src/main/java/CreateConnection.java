import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class CreateConnection {
	
	public static Connection create() throws SQLException{
		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/GROCERY_STORE", "root", "password");
		return con;
	}

}
