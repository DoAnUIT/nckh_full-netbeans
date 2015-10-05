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
    List<Integer> updateList = new ArrayList(Arrays.asList(144, 48, 120, 28, 28, -1));

    public ArticleBUS() {
    }

    // ok
    public ArticleBUS(String username, String password) throws SQLException {
        this.username = username;
        this.password = password;
        artDAO = new ArticleDAO(username, password);

    }

    // ok
    public boolean insertArticle(ArticleDTO art) {
        int maxIDTableArticle = getMaxIDTableArticle();
        art.setIDTableArticle(maxIDTableArticle + 1);
        art.setIDTableUpdateTime(1);
        art.setCountOfUpdate(0);
        return artDAO.insertArticle(username, password, art);
    }

    public boolean updateArticle(ArticleDTO art) {
        int maxCount = updateList.get(art.getIDTableUpdateTime() - 1);
        if (art.getCountOfUpdate() < maxCount - 1) {
            art.setCountOfUpdate(art.getCountOfUpdate() + 1);
        } else {
//            if (art.getCountOfUpdate() == 5) {
//                art.setCountOfUpdate(-1);
//            } else {
//                art.setCountOfUpdate(0);
//            }
//            art.setIDTableUpdateTime(art.getIDTableUpdateTime() + 1);
            //if (art.getIDTableUpdateTime() != 5) {
                art.setCountOfUpdate(0);
                art.setIDTableUpdateTime(art.getIDTableUpdateTime() + 1);
            //}
        }
        return artDAO.updateArticle(username, password, art);
    }

    public int getMaxIDTableArticle() {
        return artDAO.getMaxIDTableArticle(username, password);
    }

    public List<ArticleDTO> getArticleToUpdate(int IDTableUpdateTime, int IDTableMagazine) {
        return artDAO.getArticleToUpdate(username, password, IDTableUpdateTime, IDTableMagazine);
    }

    public int isArticleExistsForInsert(ArticleDTO art) {
        return artDAO.isArticleExistsForInsert(username, password, art);
    }
    
    public int isArticleExistsForUpdate(ArticleDTO art) {
        return artDAO.isArticleExistsForUpdate(username, password, art);
    }

    // working with list
    // if article don't exist => insert
    public boolean insert(List<ArticleDTO> lart) {
        for(int i = 0; i < lart.size(); i++) {
            if (isArticleExistsForInsert(lart.get(i)) == 0) { // phải gọi kiểm tra liên tục => truy suất database liên tục
                if (insertArticle(lart.get(i)) == false) {
                    return false;
                }
            }
            else{
                lart.remove(i);
                i--;
            }
        }
        return true;
    }

    public boolean update(List<ArticleDTO> lart) {
        for (ArticleDTO art : lart) {
            if (isArticleExistsForUpdate(art) == 1) {
                if (updateArticle(art) == false) {
                    return false;
                } }
            else {
                    if (insertArticle(art) == false) {
                        return false;
                    }
                }
            }

        return true;

    }

}
