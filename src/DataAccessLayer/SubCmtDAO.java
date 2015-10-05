package DataAccessLayer;

import java.sql.SQLException;

import DTO.SubCmtDTO;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubCmtDAO extends OpenDBConnection {

    CallableStatement call = null;

    public SubCmtDAO() throws SQLException {
        super();
        // TODO Auto-generated constructor stub
    }
    
    public SubCmtDAO(String username, String password) throws SQLException{
        super(username, password);
    }

    public boolean insertSubCmt(String username, String password, SubCmtDTO sub) {
        try {
            if (connection.isClosed()) {
				// ds = (MysqlConnectionPoolDataSource)
                // context.lookup("jbdc/pool/nckhDB");
                openConnection(username, password);
                
            }
//IDTableSubCmt int, IDTableParentCmt int,  ChildID int, CmtLike int, Content
            call = connection.prepareCall("{call insertSubCmt(?,?,?,?,?)}");

            call.setInt(1, sub.getIDTableSubCmt());
            call.setInt(2, sub.getIDTableParentCmt());
            call.setInt(3, sub.getChildID());
            call.setInt(4, sub.getCmtLike());
            call.setString(5, sub.getContent());

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

    public boolean updateSubCmt(String username, String password, SubCmtDTO sub) {
        try {
            if (connection.isClosed()) {
				// ds = (MysqlConnectionPoolDataSource)
                // context.lookup("jbdc/pool/nckhDB");
                openConnection(username, password);
            }
            //IDTableParentCmt int, in ChildID int, in CmtLike int, Content 
            call = connection.prepareCall("{call updateSubCmt(?,?,?,?)}");

            call.setInt("IDTableParentCmt", sub.getIDTableParentCmt());
            call.setInt("ChildID", sub.getChildID());
            call.setInt("CmtLike", sub.getCmtLike());
            call.setString("Content", sub.getContent());

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
    
    public int getMaxIDTableSubCmt (String username, String password)    {
        try {
            if(connection.isClosed())
                openConnection(username, password);
            
            call = connection.prepareCall("{call getMaxIDTableSubCmt(?)}");
            
            call.registerOutParameter(1,Types.INTEGER );
            
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
    
    // 1 => exist
    public int isSubCmtExits(String username, String password, SubCmtDTO sub ){
        try {
            if(connection.isClosed())
                openConnection(username, password);
            //isSubCmntExits(IDTableArticle int, ParentID int, ChildID int, out Result int
            call = connection.prepareCall("{call isSubCmtExits(?,?,?,?)}");
            
            call.setInt(1, sub.getIDTableArticle());
            call.setInt(2, sub.getParentID());
            call.setInt(3, sub.getChildID());
            call.registerOutParameter("Result",Types.INTEGER );
            
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
    
    public int getIDTableParentCmtWithArgument(String username, String password, SubCmtDTO sub) {
        //getIDTableParentCmtWithArgument(IDTableArticle int, ParentID int, out IDTableParentCmt int)
        try {
            if (connection.isClosed()) {
                openConnection(username, password);
            }
             //IDTableArticle int, ParentID int, out IDTableParentCmt 
            call = connection.prepareCall("{call getIDTableParentCmtWithArgument(?,?,?)}");

            call.setInt("IDTableArticle", sub.getIDTableArticle());
            call.setInt("ParentID", sub.getParentID());
            call.registerOutParameter("IDTableParentCmt", Types.INTEGER);

            call.execute();

            return call.getInt("IDTableParentCmt");
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
