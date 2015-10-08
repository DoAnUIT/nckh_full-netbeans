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
        return artDAO.insertArticle(art);
    }

    public boolean updateArticle(ArticleDTO art) {
        int maxCount = updateList.get(art.getIDTableUpdateTime() - 1);
        if (art.getCountOfUpdate() < maxCount - 1) {
            art.setCountOfUpdate(art.getCountOfUpdate() + 1);
        } else {
            art.setCountOfUpdate(0);
            art.setIDTableUpdateTime(art.getIDTableUpdateTime() + 1);
        }
        return artDAO.updateArticle(art);
    }

    public int getMaxIDTableArticle() {
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
            return insertArticle(art);
        }
        return false;
    }

    public boolean update(ArticleDTO art) {

        if (isArticleExistsForUpdate(art) == 1) {
            return updateArticle(art);
             
        } else {
            return insertArticle(art);
        }

    }

}
