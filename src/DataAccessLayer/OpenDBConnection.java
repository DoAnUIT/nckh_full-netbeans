package DataAccessLayer;

import java.sql.Connection;
import java.sql.SQLException;


import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.xml.ws.BindingProvider;

// khong dung pool ling
public class OpenDBConnection {

    protected Connection connection = null;
    
    public OpenDBConnection(){}

    public OpenDBConnection(String username, String password) throws SQLException {
         try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(OpenDBConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        openConnection(username, password);
    }
    
    public void openConnection(String username, String password) throws SQLException {
       
       connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/nckh?useUnicode=true",username,password);
    }
}
