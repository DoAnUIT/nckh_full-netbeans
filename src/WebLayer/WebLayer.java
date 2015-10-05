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
        IParentCmt parComment = null;
        ISubCmt subComment = null;

        switch (magazineUrl) //<editor-fold defaultstate="collapsed" desc="comment">
        {
            case "http://vnexpress.net":
                art = new ArticleVnexpress();
                parComment = new ParentCmtVnexpress();
                subComment = new SubCmtVnexpress();
                break;
            case "http://www.thanhnien.com.vn":
                art = new ArticleThanhNien();
                parComment = new ParentCmtThanhNien();
                subComment = new SubCmtThanhNien();
                break;
            case "http://tuoitre.vn":
                art = new ArticleTuoiTre();
                parComment = new ParentCmtTuoiTre();
                subComment = new SubCmtTuoiTre();
                break;
        }
//</editor-fold>

        List<ParentCmtDTO> lParentCmt = null;
        List<SubCmtDTO> lSubcmt = null;
        List<ArticleDTO> lArticle = null;
        List<ParentCmtDTO> temptPar = null;
        List<SubCmtDTO> temptSub = null;
        List<Integer> parentIDHasSub = new ArrayList<Integer>();

        lArticle = art.getNewsOfEachMenuDependOnTime(magazineUrl, newtime, lasttime);
        // finshish this step, IDTableArtile doesn't exist for particular article
        ArticleBUS artBUS = new ArticleBUS(username, password);
        if(lArticle != null)
        artBUS.insert(lArticle);
        // after insert, has IDTableArticle in database in lArticle list
        for (ArticleDTO article : lArticle) {
            temptPar = parComment.getContentParentComment(article, parentIDHasSub);
            if (temptPar != null) {
                if (lParentCmt != null) {
                    lParentCmt.addAll(temptPar);
                } else {
                    lParentCmt = temptPar;
                }

                temptSub = subComment.getContentSubComment(article, parentIDHasSub);
                if (temptSub != null) {
                    if (lSubcmt != null) {
                        lSubcmt.addAll(temptSub);
                    } else {
                        lSubcmt = temptSub;
                    }
                }
                parentIDHasSub.clear();
            }
        }

        ParentCmtBUS parBUS = new ParentCmtBUS(username, password);
        if (lParentCmt != null) {
            parBUS.insert(lParentCmt);
        }

        SubCmtBUS subBus = new SubCmtBUS(username, password);
        if (lSubcmt != null) {
            subBus.insert(lSubcmt);
        }
        lArticle = null;
        lParentCmt =null;
        lSubcmt = null;
    }

    // update to database with specific magazine
    public void update(String magazineUrl, int IDTableUpdate) throws SQLException, IOException {
        List<ArticleDTO> lOldArt = new ArrayList<ArticleDTO>();
        ArticleObject artObject = null;
        IParentCmt parComment = null;
        ISubCmt subComment = null;
        ArticleBUS artBUS = new ArticleBUS(username, password);

        // thanh nien :1, vnexpress : 2, tuoitre : 3
        switch (magazineUrl) //<editor-fold defaultstate="collapsed" desc="comment">
        {
            case "http://vnexpress.net":
                lOldArt = artBUS.getArticleToUpdate(IDTableUpdate, 2);
                artObject = new ArticleVnexpress();
                parComment = new ParentCmtVnexpress();
                subComment = new SubCmtVnexpress();
                break;
            case "http://www.thanhnien.com.vn":
                lOldArt = artBUS.getArticleToUpdate(IDTableUpdate, 1);
                artObject = new ArticleThanhNien();
                parComment = new ParentCmtThanhNien();
                subComment = new SubCmtThanhNien();
                break;
            case "http://tuoitre.vn/":
            case "http://tuoitre.vn":
                lOldArt = artBUS.getArticleToUpdate(IDTableUpdate, 3);
                artObject = new ArticleTuoiTre();
                parComment = new ParentCmtTuoiTre();
                subComment = new SubCmtTuoiTre();
                break;
        }
//</editor-fold>

        // update article
        int articleLike = 0;
        FacebookDTO fb = null;
        for (int i = 0; i < lOldArt.size(); i++) {
            // truyen thieu objectid de lay article like,
            // phải set lai idtablearticle, idtableupdate
            // nên tạo ra 1 hàm dành riêng cho việc update, lấy thêm thành phần object id nữa.
            // hàm này sẽ truyền vào article dto luôn
            fb = artObject.getContentOfFacebook(lOldArt.get(i).getUrl());
            articleLike = artObject.getArticleLike(lOldArt.get(i).getObjectID());
           
            lOldArt.get(i).setArticleLike(articleLike);
            lOldArt.get(i).facebook = fb;
            lOldArt.get(i).setIDTableUpdateTime(IDTableUpdate);

        }
        if(lOldArt != null)
        artBUS.update(lOldArt);

        // update parent and sub comment
        List<ParentCmtDTO> lParentCmt = null;
        List<SubCmtDTO> lSubCmt = null;
        List<ParentCmtDTO> ltemptPar = null;
        List<SubCmtDTO> ltemptSub = null;
        List<Integer> parentIDHasSub = new ArrayList<Integer>();

        for (ArticleDTO art : lOldArt) {
            ltemptPar = parComment.getContentParentComment(art, parentIDHasSub);
            if (ltemptPar != null) {
                if (lParentCmt != null) {
                    lParentCmt.addAll(ltemptPar);
                } else {
                    lParentCmt = ltemptPar;
                }

                ltemptSub = subComment.getContentSubComment(art, parentIDHasSub);
                if (ltemptSub != null) {
                    if (lSubCmt != null) {
                        lSubCmt.addAll(ltemptSub);
                    } else {
                        lSubCmt = ltemptSub;
                    }
                }
                
                parentIDHasSub.clear();
            }
        }

        ParentCmtBUS parBUS = new ParentCmtBUS(username, password);
        if(lParentCmt != null)
        parBUS.update(lParentCmt);

        SubCmtBUS subBus = new SubCmtBUS(username, password);
        if(lSubCmt != null)
        subBus.update(lSubCmt);
       
        lParentCmt =null;
        lSubCmt = null;
    }

}
