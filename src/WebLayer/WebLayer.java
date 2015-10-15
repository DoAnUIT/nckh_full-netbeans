/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebLayer;

import BusinessLayer.ArticleBUS;
import BusinessLayer.ParentCmtBUS;
import BusinessLayer.SubCmtBUS;
import DTO.ArticleDTO;
import DTO.FacebookDTO;
import DTO.ParentCmtDTO;
import DTO.SubCmtDTO;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author Minh Nhat
 */
public class WebLayer {

    private String username = null;
    private String password = null;

    public WebLayer() {
    }

    public WebLayer(String username, String password) throws SQLException {
        this.username = username;
        this.password = password;

    }

//   insert to database with specific magazine
    public void insert(String magazineUrl, Timestamp newtime, Timestamp lasttime) throws SQLException {
        ArticleObject art = null;

        switch (magazineUrl) //<editor-fold defaultstate="collapsed" desc="comment">
        {
            case "http://vnexpress.net":
                art = new ArticleVnexpress(username,password);
                break;
            case "http://www.thanhnien.com.vn":
                art = new ArticleThanhNien(username,password);
                break;
            case "http://tuoitre.vn":
                art = new ArticleTuoiTre(username,password);
                
                break;
        }
//</editor-fold>

        // article, parentcmt, subcmt lấy vào insert ngay vào database
        // insert theo từng bài
        art.setNewsOfEachMenuDependOnTime(magazineUrl, newtime, lasttime);
        
    }

    // update to database with specific magazine
    public void update(String magazineUrl, int IDTableUpdate) throws SQLException, IOException {
        List<ArticleDTO> lOldArt = new ArrayList<ArticleDTO>();
        ArticleObject artObject = null;
        ArticleBUS artBUS = new ArticleBUS(username, password);

        // thanh nien :1, vnexpress : 2, tuoitre : 3
        //<editor-fold defaultstate="collapsed" desc="chon lấy những bài báo cần update đưa vào lOldArt">
        switch (magazineUrl) 
        {
            case "http://vnexpress.net":
                lOldArt = artBUS.getArticleToUpdate(IDTableUpdate, 2);
                artObject = new ArticleVnexpress(username,password);
                
                break;
            case "http://www.thanhnien.com.vn":
                lOldArt = artBUS.getArticleToUpdate(IDTableUpdate, 1);
                artObject = new ArticleThanhNien(username,password);
                break;
            case "http://tuoitre.vn/":
            case "http://tuoitre.vn":
                lOldArt = artBUS.getArticleToUpdate(IDTableUpdate, 3);
                artObject = new ArticleTuoiTre(username,password);
                
                break;
        }
//</editor-fold>

        // update article
        int articleLike = 0;
        FacebookDTO fb = null;
        // nếu không có bài nào cần update
        if(lOldArt == null)
            return;
        for (int i = 0; i < lOldArt.size(); i++) {
            System.out.println(lOldArt.get(i).getUrl());
            fb = artObject.getContentOfFacebook(lOldArt.get(i).getUrl());
            articleLike = artObject.getArticleLike(lOldArt.get(i).getObjectID());
           
            lOldArt.get(i).setArticleLike(articleLike);
            lOldArt.get(i).facebook = fb;
            lOldArt.get(i).setIDTableUpdateTime(IDTableUpdate);
            
            // cập nhật parent cmt và subcmt rồi lưu vào database
            artObject.updateDatabase(lOldArt.get(i));

        }
    }
}
