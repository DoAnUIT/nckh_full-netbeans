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
import DTO.*;
import DataAccessLayer.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ArticleBUS {

    // article -> parent comment -> sub comment
    private String username = null;
    private String password = null;
    private ArticleDAO artDAO = null;
    private UpdateTimeDAO upDAO = null;
    //List<Integer> updateList = new ArrayList(Arrays.asList(144, 48, 120, 28, 28, -1));

    public ArticleBUS() {
    }

    // ok
    public ArticleBUS(String username, String password) throws SQLException {
        this.username = username;
        this.password = password;
        artDAO = new ArticleDAO(username, password);
        upDAO = new UpdateTimeDAO(username, password);

    }

    // ok
    public synchronized boolean insertArticle(ArticleDTO art) {
        int maxIDTableArticle = getMaxIDTableArticle();
        art.setIDTableArticle(maxIDTableArticle + 1);
        art.setIDTableUpdateTime(1);
        art.setCountOfUpdate(0);
        System.out.println(art.getIDTableArticle());
        return artDAO.insertArticle(art);
    }

    public boolean updateArticle(ArticleDTO tempDTO) {
//        int maxCount = updateList.get(art.getIDTableUpdateTime() - 1);
//        if (art.getCountOfUpdate() < maxCount - 1) {
//            art.setCountOfUpdate(art.getCountOfUpdate() + 1);
//        } else {
//            art.setCountOfUpdate(0);
//            art.setIDTableUpdateTime(art.getIDTableUpdateTime() + 1);
//        }
        tempDTO.setCountOfUpdate(tempDTO.getCountOfUpdate() + 1);
        if (tempDTO.getCountOfUpdate() > upDAO.GetTimeUpdateByID(tempDTO.getIDTableUpdateTime()).getMaxRepeat()) {
            tempDTO.setCountOfUpdate(0);
            int maxID = upDAO.GetMaxIdTableUpdateTime();
            if (maxID <= 0) {
                System.out.println("can not update number for " + tempDTO.getTitle());
                //continue;
                return false;
            }
            if (tempDTO.getIDTableUpdateTime() + 1 <= maxID) {
                tempDTO.setIDTableUpdateTime(tempDTO.getIDTableUpdateTime() + 1);
            }
        }
        return artDAO.updateArticle(tempDTO);
    }

    public synchronized int getMaxIDTableArticle() {
        return artDAO.getMaxIDTableArticle();
    }

    public List<ArticleDTO> getArticleToUpdate(int IDTableUpdateTime, int IDTableMagazine) {
        return artDAO.getArticleToUpdate(IDTableUpdateTime, IDTableMagazine);
    }

    public int isArticleExistsForInsert(ArticleDTO art) {
        return artDAO.isArticleExistsForInsert(art);
    }

    public int isArticleExistsForUpdate(ArticleDTO art) {
        return artDAO.isArticleExistsForUpdate(art);
    }

    // working with list
    public boolean insert(ArticleDTO art) {
        if (isArticleExistsForInsert(art) == 0) {
            if (!insertArticle(art)) {
                System.out.println("insert article vao database that bai");
                return false;
            }
        } else {
            System.out.println("article da ton tai");
            return false;
        }
        return true;
    }

    public boolean update(ArticleDTO art) {

        if (isArticleExistsForUpdate(art) == 1) {
            if (updateArticle(art) == false) {
                System.out.println("Cap nhat article vao database that bai");
                return false;
            } else {
                return true;
            }

        } else {
            if (!insertArticle(art)) {
                System.out.println("Cap nhat article vao database that bai");
                return false;
            } else {
                return true;
            }
        }
    }

}
