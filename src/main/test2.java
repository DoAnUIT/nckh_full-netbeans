/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import BusinessLayer.ArticleBUS;
import BusinessLayer.ParentCmtBUS;
import BusinessLayer.SubCmtBUS;
import DTO.ArticleDTO;
import DTO.ParentCmtDTO;
import DTO.SubCmtDTO;
import WebLayer.ArticleTuoiTre;
import WebLayer.ArticleObject;
import WebLayer.ArticleThanhNien;
import WebLayer.ArticleVnexpress;
import WebLayer.IParentCmt;
import WebLayer.ISubCmt;
import WebLayer.ParentCmtThanhNien;
import WebLayer.ParentCmtTuoiTre;
import WebLayer.ParentCmtVnexpress;
import WebLayer.SubCmtThanhNien;
import WebLayer.SubCmtTuoiTre;
import WebLayer.SubCmtVnexpress;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Minh Nhat
 */
public class test2 {//file nao chay on nhat?
    // file test2. chỉ test xem co lay thông tin về được hay không.
        public static void main(String[] args)  {
             // TODO Auto-generated method stub
        String username = "testNhon";
        String password = "nhonmysql!@3";
//        ArticleObject ar = new ArticleVnexpress(username,password);
//        IParentCmt parComment = new ParentCmtVnexpress();
//        ISubCmt subComment = new SubCmtVnexpress();
//<editor-fold defaultstate="collapsed" desc="comment">
//
//        ArticleObject ar = new ArticleTuoiTre(username,password);
//        IParentCmt parComment = new ParentCmtTuoiTre();
//        ISubCmt subComment = new SubCmtTuoiTre();
        
       ArticleObject ar = new ArticleThanhNien(username, password);
       IParentCmt parComment = new ParentCmtThanhNien();
       ISubCmt subComment = new SubCmtThanhNien();
//</editor-fold>
            //String test = "https://graph.facebook.com/fql?q=SELECT%20share_count,%20like_count,%20comment_count%20FROM%20link_stat%20where%20url=%27http://vnexpress.net/tin-tuc/khoa-hoc/hoi-dap/mat-nguoi-nhin-duoc-bao-nhieu-khung-hinh-giay-3292760.html%27";   
            
              
        

        List<Integer> parentIDHasSub = new ArrayList<Integer>();
        //String url = "http://vnexpress.net";
        //String url = "http://www.thanhnien.com.vn";
//        String url = "http://tuoitre.vn/";
//<editor-fold defaultstate="collapsed" desc="comment">
//
//        Calendar calendar = new GregorianCalendar();
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.HOUR_OF_DAY, 11);
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
//        Timestamp newtime = new Timestamp(calendar.getTimeInMillis());
//
//        //calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
//        calendar.set(Calendar.HOUR_OF_DAY, 1);
//        Timestamp lasttime = new Timestamp(calendar.getTimeInMillis());
        //List<ArticleDTO> larticle = ar.getNewsOfEachMenuDependOnTime(url, newtime, lasttime);
//</editor-fold>

        String a = "http://www.thanhnien.com.vn/chinh-tri-xa-hoi/vac-kiem-di-thanh-toan-doi-thu-bi-phat-3-trieu-dong-624212.html";
//        //String a = "http://tuoitre.vn/tin/chinh-tri-xa-hoi/20151009/nhieu-vu-bao-chi-noi-am-am-nhung-dai-bieu-im-thin-thit/982477.html";
//        //String a = "http://www.thanhnien.com.vn/chinh-tri-xa-hoi/clip-bi-don-vi-cai-csgt-toi-bi-danh-mieng-sung-vu-mau-chay-nhieu-605535.html";
//        try {
//                ar.getContentOfFacebook(a);
//            } catch (IOException ex) {
//                Logger.getLogger(test2.class.getName()).log(Level.SEVERE, null, ex);
//            }
        ArticleDTO article = ar.getArticleInformation(a);
        
        if (article != null) {
            System.out.println("Time : " + article.getArticleDate() + "\nObjectId : " + article.getObjectID() + "\nTitile :"
                    + article.getTitle() + "\nURL : " + article.getUrl() + "\nFacebook Like : "
                    + article.facebook.getFBLike() + "\nFacebook Comment : " + article.facebook.getFBCmt()
                    + "\nCategory : " + article.getIDTableCategory() +"\nArticleDate : " + article.getArticleDate());
            System.out.println();

        }
//       
        System.out.println("Parent Comment : \n");

        List<ParentCmtDTO> arrayParentCmt = null;
        List<SubCmtDTO> arraySubcmt = null;
        arrayParentCmt = parComment.getContentParentComment(article, parentIDHasSub);
        for (int i = 0; i < arrayParentCmt.size(); i++) {
            System.out.println("IDTableArticle : " + arrayParentCmt.get(i).getIDTableArticle() + "\nParentID : "
                    + arrayParentCmt.get(i).getParentID() + "\nLike : " + arrayParentCmt.get(i).getCmtLike() + "\nContent : "
                    + arrayParentCmt.get(i).getContent());
            System.out.println();
        }
      
        System.out.println("\nSub Comment :\n");

        arraySubcmt = subComment.getContentSubComment(article, parentIDHasSub);
        for (int i = 0; i < arraySubcmt.size(); i++) {
            System.out.println("ID : " + arraySubcmt.get(i).getChildID() + "\nParentID : " + arraySubcmt.get(i).getParentID()
                    + "\nLike : " + " " + arraySubcmt.get(i).getCmtLike() + "\nContent : " + arraySubcmt.get(i).getContent());
            System.out.println();
        }
//       
        
        System.out.println("Finished");
        }

    
}
