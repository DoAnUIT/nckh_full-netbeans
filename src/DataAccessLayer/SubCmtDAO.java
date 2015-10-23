package DataAccessLayer;

import java.sql.SQLException;

import DTO.SubCmtDTO;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SubCmtDAO extends DataSource {

    CallableStatement call = null;
    int numberInsert = 0;
    int numberUpdate = 0;

    public SubCmtDAO() {
    }
    
    public SubCmtDAO(String username, String password) {
        this.username= username;
        this.password= password;
        numberInsert = 0;
        numberUpdate = 0;
    }

    public synchronized boolean insertSubCmt(SubCmtDTO sub) {
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
//IDTableSubCmt int, IDTableParentCmt int,  ChildID int, CmtLike int, Content
            call = connection.prepareCall("{call insertSubCmt(?,?,?,?,?)}");

            call.setInt(1, sub.getIDTableSubCmt());
            call.setInt(2, sub.getIDTableParentCmt());
            call.setInt(3, sub.getChildID());
            call.setInt(4, sub.getCmtLike());
            call.setString(5, sub.getContent());

            call.execute();
            //System.out.println("Insert Subcmt thanh cong");
            return true;

        } catch (Exception e) {
            // TODO: handle exception
//            if (numberInsert >= 5) {
//                numberInsert = 0;
//                System.out.println(e.getMessage());
//                return false;
//            }
//            numberInsert++;
//            sub.setIDTableSubCmt(sub.getIDTableSubCmt() + 1);
//            insertSubCmt(sub);
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

    public boolean updateSubCmt(SubCmtDTO sub) {
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
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
//            if (numberUpdate >= 5) {
//                numberUpdate = 0;
//                System.out.println(e.getMessage());
//                return false;
//            }
//            numberUpdate++;
//            updateSubCmt(sub);
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
    
    public int getMaxIDTableSubCmt ()    {
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
            
            call = connection.prepareCall("{call getMaxIDTableSubCmt(?)}");
            
            call.registerOutParameter(1,Types.INTEGER );
            
            call.execute();
            
            return call.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
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
    public int isSubCmtExists(SubCmtDTO sub ){
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
            //isSubCmntExits(IDTableArticle int, ParentID int, ChildID int, out Result int
            call = connection.prepareCall("{call isSubCmtExists(?,?,?,?)}");
            
            call.setInt(1, sub.getIDTableArticle());
            call.setInt(2, sub.getParentID());
            call.setInt(3, sub.getChildID());
            call.registerOutParameter("Result",Types.INTEGER );
            
            call.execute();
            
            return call.getInt("Result");
        } catch (SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally
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
    
    public int getIDTableParentCmtWithArgument(SubCmtDTO sub) {
        //getIDTableParentCmtWithArgument(IDTableArticle int, ParentID int, out IDTableParentCmt int)
        try {
            connection = DataSource.getInstance().getConnection();
            while(connection == null)
                connection = DataSource.getInstance().getConnection();
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
        finally
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
