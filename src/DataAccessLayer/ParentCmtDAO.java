package DataAccessLayer;

import java.sql.CallableStatement;
import java.sql.SQLException;

import javax.naming.NamingException;

import DTO.ParentCmtDTO;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ParentCmtDAO extends DataSource {

    CallableStatement call = null;
    int numberInsert = 0;
    int numberUpdate = 0;

    public ParentCmtDAO(String username, String password) {
        this.username = username;
        this.password = password;
        numberInsert = 0;
        numberUpdate = 0;
    }

    public synchronized boolean insertParentCmt(ParentCmtDTO par) {
        try {
            connection = DataSource.getInstance().getConnection();
            while (connection == null) {
                connection = DataSource.getInstance().getConnection();
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
            System.out.println("Insert Parent cmt thanh cong");
            return true;

        } catch (Exception e) {
            // TODO: handle exception
            if (numberInsert >= 5) {
                numberInsert = 0;
                System.out.println(e.getMessage());
                return false;
            }
            numberInsert++;
            par.setIDTableParentCmt(par.getIDTableParentCmt() + 1);
            insertParentCmt(par);
            //System.out.println(e.getMessage());
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

    public boolean updateParentCmt(ParentCmtDTO par) {
        try {
            connection = DataSource.getInstance().getConnection();
            while (connection == null) {
                connection = DataSource.getInstance().getConnection();
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
            if (numberUpdate >= 5) {
                numberUpdate = 0;
                System.out.println(e.getMessage());
                return false;
            }
            numberUpdate++;
            updateParentCmt(par);
            //System.out.println(e.getMessage());
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

    public int getMaxIDTableParentCmt() {
        try {
            connection = DataSource.getInstance().getConnection();
            while (connection == null) {
                connection = DataSource.getInstance().getConnection();
            }

            call = connection.prepareCall("{call getMaxIDTableParentCmt(?)}");

            call.registerOutParameter(1, Types.INTEGER);

            call.execute();

            return call.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
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
        return -1;
    }

    // exist => 1
    public int isParentCmtExists(ParentCmtDTO par) {
        try {
            connection = DataSource.getInstance().getConnection();
            while (connection == null) {
                connection = DataSource.getInstance().getConnection();
            }
            //IDTableArticle int, ParentID int, out Result int
            call = connection.prepareCall("{call isParentCmtExists(?,?,?)}");

            call.setInt("IDTableArticle", par.getIDTableArticle());
            call.setInt("ParentID", par.getParentID());
            call.registerOutParameter("Result", Types.INTEGER);

            call.execute();

            return call.getInt("Result");
        } catch (SQLException ex) {
            Logger.getLogger(ArticleDAO.class.getName()).log(Level.SEVERE, null, ex);
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
        return -1;
    }

}
