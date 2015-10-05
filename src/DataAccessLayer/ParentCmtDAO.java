package DataAccessLayer;

import java.sql.CallableStatement;
import java.sql.SQLException;

import javax.naming.NamingException;

import DTO.ParentCmtDTO;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParentCmtDAO extends OpenDBConnection {

    CallableStatement call = null;

    // constructor
    public ParentCmtDAO() throws NamingException, SQLException {
        super();
    }

    public ParentCmtDAO(String username, String password) throws SQLException {
        super(username, password);
    }

    public boolean insertParentCmt(String username, String password, ParentCmtDTO par) {
        try {
            if (connection.isClosed()) {
                // ds = (MysqlConnectionPoolDataSource)
                // context.lookup("jbdc/pool/nckhDB");
                openConnection(username, password);
            }
            //IDTableParentCmt int, IDTableArticle int, parentID int, CmtLike int, Content
            call = connection.prepareCall("{call insertParentCmt(?,?,?,?,?)}");

            call.setInt(1, par.getIDTableParentCmt());
            call.setInt(2, par.getIDTableArticle());
            call.setInt(3, par.getParentID());
            call.setInt(4, par.getCmtLike());
            call.setString(5, par.getContent());
            //`   
            call.execute();
            return true;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                if (call != null) {
                    call.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean updateParentCmt(String username, String password, ParentCmtDTO par) {
        try {
            if (connection.isClosed()) {
                // ds = (MysqlConnectionPoolDataSource)
                // context.lookup("jbdc/pool/nckhDB");
                openConnection(username, password);
            }
            //IDTableArticle int,  parentID int,  CmtLike int, Content varchar
            call = connection.prepareCall("{call updateParentCmt(?,?,?,?)}");

            call.setInt(1, par.getIDTableArticle());
            call.setInt(2, par.getParentID());
            call.setInt(3, par.getCmtLike());
            call.setString(4, par.getContent());

            call.execute();
            return true;

        } catch (Exception e) {
            // TODO: handle exception
            System.out.println(e.getMessage());
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                if (call != null) {
                    call.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return false;
    }

    public int getMaxIDTableParentCmt(String username, String password) {
        try {
            if (connection.isClosed()) {
                openConnection(username, password);
            }

            call = connection.prepareCall("{call getMaxIDTableParentCmt(?)}");

            call.registerOutParameter(1, Types.INTEGER);

            call.execute();

            return call.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                if (call != null) {
                    call.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return -1;
    }

    // exist => 1
    public int isParentCmtExits(String username, String password, ParentCmtDTO par) {
        try {
            if (connection.isClosed()) {
                openConnection(username, password);
            }
            //IDTableArticle int, ParentID int, out Result int
            call = connection.prepareCall("{call isParentCmtExits(?,?,?)}");

            call.setInt("IDTableArticle", par.getIDTableArticle());
            call.setInt("ParentID", par.getParentID());
            call.registerOutParameter("Result", Types.INTEGER);

            call.execute();

            return call.getInt("Result");
        } catch (SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                if (call != null) {
                    call.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return -1;
    }
    
    
}
