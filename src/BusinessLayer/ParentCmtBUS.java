/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessLayer;

/**
 *
 * @author Minh Nhat
 */
import DataAccessLayer.*;
import DTO.*;
import java.sql.SQLException;
import java.util.List;

public class ParentCmtBUS {

    ParentCmtDAO pcmtDAO;
    private String username;
    private String password;

    public ParentCmtBUS(String username, String password) throws SQLException {
        this.username = username;
        this.password = password;
        pcmtDAO = new ParentCmtDAO(username, password);
    }

    public boolean insertParentCmt(ParentCmtDTO par) {
        // generated idtable parent cmt
        int maxid = getMaxIDTableParentCmt();
        par.setIDTableParentCmt(maxid + 1);
        return pcmtDAO.insertParentCmt(par);
    }

    public boolean updateParentCmt(ParentCmtDTO par) {
        return pcmtDAO.updateParentCmt(par);
    }

    public int getMaxIDTableParentCmt() {
        return pcmtDAO.getMaxIDTableParentCmt();
    }

    public int isParentCmtExists(ParentCmtDTO par) {
        return pcmtDAO.isParentCmtExists(par);
    }

    // working with list
    // kiem tra co ton tai hay khong roi moi them vao co so du lieu
    public boolean insert(List<ParentCmtDTO> lpar) {
        for (ParentCmtDTO par : lpar) {
            if (isParentCmtExists(par) == 0) {
                if (insertParentCmt(par) == false) {
                    System.out.println("insert parentcmt vao database that bai");
                    return false;
                }
            }
        }
        return true;
    }

    public boolean update(List<ParentCmtDTO> lpar) {
        for (ParentCmtDTO par : lpar) {
            if (isParentCmtExists(par) == 1) {
                if (updateParentCmt(par) == false) {
                    System.out.println("Cap nhat parentcmt vao database that bai");
                    return false;
                }
            } else if (insertParentCmt(par) == false) {
                System.out.println("Cap nhat parentcmt vao database that bai");
                return false;
            }
        }
        return true;
    }

}
