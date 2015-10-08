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
import WebLayer.IParentCmt;
import WebLayer.ISubCmt;
import WebLayer.ParentCmtTuoiTre;
import WebLayer.SubCmtTuoiTre;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author Minh Nhat
 */
public class test2 {
        public static void main(String[] args)  {
             // TODO Auto-generated method stub
        String username = "nhat";
        String password = "mysql!@3";
//        IArticle ar = new ArticleVnexpress();
//        IParentComment parComment = new ParentCmtVnexpress();
//        ISubComment subComment = new SubCmtVnexpress();

        ArticleObject ar = new ArticleTuoiTre(username,password);
        IParentCmt parComment = new ParentCmtTuoiTre();
        ISubCmt subComment = new SubCmtTuoiTre();

//        IArticle ar = new ArticleThanhNien();
//        IParentComment parComment = new ParentCmtThanhNien();
//        ISubComment subComment = new SubCmtThanhNien();
        

        List<Integer> parentIDHasSub = new ArrayList<Integer>();
        //String url = "http://vnexpress.net";
        //String url = "http://www.thanhnien.com.vn";
        String url = "http://tuoitre.vn/";

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        Timestamp newtime = new Timestamp(calendar.getTimeInMillis());

        //calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        Timestamp lasttime = new Timestamp(calendar.getTimeInMillis());
        //List<ArticleDTO> larticle = ar.getNewsOfEachMenuDependOnTime(url, newtime, lasttime);

//        for (int i = 0; i < larticle.size(); i++) {
//            System.out.println(
//                    "ObjectID : " + larticle.get(i).getObjectID() +
//                   "\nTitle : " + larticle.get(i).getTitle() + "\nURL : " + larticle.get(i).getUrl() + "\nCategory : "
//                    + larticle.get(i).getIDTableCategory() + "\nFb like : " + larticle.get(i).facebook.getFBLike());
//        }
        //String a = "http://vnexpress.net/photo/thoi-su/dan-dai-bac-truoc-gio-khai-hoa-mung-quoc-khanh-3272468.html";
        //String a = "http://vnexpress.net/tin-tuc/thoi-su/chanh-long-vi-biet-danh-xa-an-may-3274793.html";
        //String a = "http://tuoitre.vn/tin/phap-luat/20150510/vu-kien-neo-xe-ca-tiep-tuc-bi-toa-neo-ho-so/745056.html";
//        String a = "http://www.thanhnien.com.vn/giao-duc/con-bi-duoi-hoc-vi-me-len-facebook-che-dong-phuc-cua-truong-605950.html";
        String a = "http://thethao.tuoitre.vn/tin/20151006/doi-tuyen-iraq-den-de-gianh-chien-thang/980461.html";
        ArticleDTO article = ar.getArticleInformation(a);
        if (article != null) {
            System.out.println("Time : " + article.getArticleDate() + "\nObjectId : " + article.getObjectID() + "\nTitile :"
                    + article.getTitle() + "\nURL : " + article.getUrl() + "\nFacebook Like : "
                    + article.facebook.getFBLike() + "\nFacebook Comment : " + article.facebook.getFBCmt()
                    + "\nCategory : " + article.getIDTableCategory());
            System.out.println();

        }
       
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
       
        
        System.out.println("Finished");
        }

    
}
