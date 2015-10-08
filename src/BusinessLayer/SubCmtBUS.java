package BusinessLayer;

import DTO.ParentCmtDTO;
import java.sql.SQLException;

import javax.naming.NamingException;

import DTO.SubCmtDTO;
import DataAccessLayer.SubCmtDAO;
import java.util.List;

public class SubCmtBUS {

    SubCmtDAO scmtDAO;

    private String username;
    private String password;

    public SubCmtBUS(String username, String password) throws SQLException {
        this.username = username;
        this.password = password;
        scmtDAO = new SubCmtDAO(username, password);
    }

    public boolean insertSubCmt(SubCmtDTO child) {
        int maxIDTableSubCmt = getMaxIDTableSubCmt();
        child.setIDTableSubCmt(maxIDTableSubCmt + 1);

        int idTableParentCmt = getIDTableParentCmtWithArgument(child);
        child.setIDTableParentCmt(idTableParentCmt);
        return scmtDAO.insertSubCmt(child);
    }

    public boolean updateSubCmt(SubCmtDTO child) {
        return scmtDAO.updateSubCmt(child);
    }

    public int getMaxIDTableSubCmt() {
        return scmtDAO.getMaxIDTableSubCmt();
    }

    public int isSubCmtExits(SubCmtDTO child) {
        return scmtDAO.isSubCmtExists(child);
    }

    public int getIDTableParentCmtWithArgument(SubCmtDTO sub) {
        return scmtDAO.getIDTableParentCmtWithArgument(sub);
    }

    // Working with list
    public boolean insert(List<SubCmtDTO> lchild) {
        for (SubCmtDTO child : lchild) {
            if (isSubCmtExits(child) == 0) {
                if (insertSubCmt(child) == false) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean update(List<SubCmtDTO> lSub) {
        for (SubCmtDTO sub : lSub) {
            if (isSubCmtExits(sub) == 1) {
                if (updateSubCmt(sub) == false) {
                    return false;
                }
            } else if (insertSubCmt(sub) == false) {
                return false;
            }
        }
        return true;
    }

}
