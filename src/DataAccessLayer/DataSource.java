package DataAccessLayer;

import java.sql.Connection;
import java.sql.SQLException;

import com.mysql.jdbc.jdbc2.optional.MysqlConnectionPoolDataSource;
import java.sql.DriverManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.mchange.v2.c3p0.*;
import java.beans.PropertyVetoException;
import java.sql.Statement;

import javax.xml.ws.BindingProvider;

//dung connecton poolling
public class DataSource {

    protected Connection connection = null;
    private static DataSource dataSource = null;
    private ComboPooledDataSource cpds = null;
    protected static String username = null;
    protected static String password = null;
    
    public DataSource() {
    }

    private DataSource(String username, String password) throws SQLException {
        try {
            System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
            System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "WARNING");

            cpds = new ComboPooledDataSource();
            cpds.setDriverClass("com.mysql.jdbc.Driver");
            //cpds.setJdbcUrl("jdbc:mysql://localhost:3306/nckh?useUnicode=true");&characterEncoding=UTF-8
            cpds.setJdbcUrl("jdbc:mysql://localhost:3306/nckh_hot_news?characterEncoding=UTF-8");
            cpds.setUser(username);
            cpds.setPassword(password);

            // the settings below are optional -- c3p0 can work with defaults
            
            cpds.setAcquireIncrement(5);
            cpds.setMaxPoolSize(50);
            cpds.setMaxStatements(180);
            
        } catch (PropertyVetoException ex) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static DataSource getInstance() throws SQLException {
        if(dataSource == null)
            dataSource = new DataSource(username, password);
        return dataSource;
    }
    
    public Connection getConnection(){
        Connection a = null;
        try {
            a= this.cpds.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, null, ex);
        }
        return a;
    }
}
