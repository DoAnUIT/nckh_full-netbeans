package main;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.naming.NamingException;

import DTO.ArticleDTO;
import DTO.ParentCmtDTO;
import DTO.SubCmtDTO;
import WebLayer.ArticleObject;
import WebLayer.ArticleThanhNien;
import WebLayer.ArticleVnexpress;
import WebLayer.IParentCmt;
import WebLayer.ParentCmtThanhNien;
import WebLayer.ParentCmtVnexpress;
import WebLayer.ISubCmt;
import WebLayer.SubCmtThanhNien;
import WebLayer.SubCmtVnexpress;

import BusinessLayer.*;
import WebLayer.ArticleTuoiTre;
import WebLayer.ParentCmtTuoiTre;
import WebLayer.SubCmtTuoiTre;
import java.util.LinkedList;

public class main {

    public static void main(String[] args) throws IOException, NamingException, SQLException {
        // TODO Auto-generated method stub
//        String username = "nhat";
//        String password = "mysql!@3";
////        IArticle ar = new ArticleVnexpress();
////        IParentComment parComment = new ParentCmtVnexpress();
////        ISubComment subComment = new SubCmtVnexpress();
//
////        IArticle ar = new ArticleTuoiTre();
////        IParentComment parComment = new ParentCmtTuoiTre();
////        ISubComment subComment = new SubCmtTuoiTre();
//
//        ArticleObject ar = new ArticleThanhNien();
//        IParentComment parComment = new ParentCmtThanhNien();
//        ISubComment subComment = new SubCmtThanhNien();
//        List<ParentCmtDTO> arrayParentCmt = null;
//        List<SubCmtDTO> arraySubcmt = null;
//
//        List<Integer> parentIDHasSub = new ArrayList<Integer>();
//        //String url = "http://vnexpress.net";
//        String url = "http://www.thanhnien.com.vn";
//        //String url = "http://tuoitre.vn/";
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
//        List<ArticleDTO> larticle = ar.getNewsOfEachMenuDependOnTime(url, newtime, lasttime);
//
////        for (int i = 0; i < larticle.size(); i++) {
////            System.out.println(
////                    "ObjectID : " + larticle.get(i).getObjectID() +
////                   "\nTitle : " + larticle.get(i).getTitle() + "\nURL : " + larticle.get(i).getUrl() + "\nCategory : "
////                    + larticle.get(i).getIDTableCategory() + "\nFb like : " + larticle.get(i).facebook.getFBLike());
////        }
//        //String a = "http://vnexpress.net/photo/thoi-su/dan-dai-bac-truoc-gio-khai-hoa-mung-quoc-khanh-3272468.html";
//        //String a = "http://vnexpress.net/tin-tuc/thoi-su/chanh-long-vi-biet-danh-xa-an-may-3274793.html";
//        //String a = "http://tuoitre.vn/tin/phap-luat/20150510/vu-kien-neo-xe-ca-tiep-tuc-bi-toa-neo-ho-so/745056.html";
////        String a = "http://www.thanhnien.com.vn/giao-duc/con-bi-duoi-hoc-vi-me-len-facebook-che-dong-phuc-cua-truong-605950.html";
////        ArticleDTO article = ar.getArticleInformation(a);
////        if (article != null) {
////            System.out.println("Time : " + article.getArticleDate() + "\nObjectId : " + article.getObjectID() + "\nTitile :"
////                    + article.getTitle() + "\nURL : " + article.getUrl() + "\nFacebook Like : "
////                    + article.facebook.getFBLike() + "\nFacebook Comment : " + article.facebook.getFBCmt()
////                    + "\nCategory : " + article.getIDTableCategory());
////            System.out.println();
////
////        }
//        ArticleBUS artBUS = new ArticleBUS(username, password);
//        artBUS.insert(larticle);
//
//        System.out.println("Parent Comment : \n");
//
//        List<ParentCmtDTO> temptPar = null;
//        List<SubCmtDTO> temptSub = null;
//        
//
//        for (ArticleDTO article : larticle) {
//            temptPar = parComment.getContentParentComment(article, parentIDHasSub);
//            if (temptPar != null) {
//                if (arrayParentCmt != null) {
//                    arrayParentCmt.addAll(temptPar);
//                } else {
//                    arrayParentCmt = temptPar;
//                }
//
//                temptSub = subComment.getContentSubComment(article, parentIDHasSub);
//                if (temptSub != null) {
//                    if (arraySubcmt != null) {
//                        arraySubcmt.addAll(temptSub);
//                    } else {
//                        arraySubcmt = temptSub;
//                    }
//                }
//                parentIDHasSub.clear();
//            }
//        }
////        arrayParentCmt = parComment.getContentParentComment(article, parentIDHasSub);
////        for (int i = 0; i < arrayParentCmt.size(); i++) {
////            System.out.println("IDTableArticle : " + arrayParentCmt.get(i).getIDTableArticle() + "\nParentID : "
////                    + arrayParentCmt.get(i).getParentID() + "\nLike : " + arrayParentCmt.get(i).getCmtLike() + "\nContent : "
////                    + arrayParentCmt.get(i).getContent());
////            System.out.println();
////        }
//        ParentCmtBUS parBUS = new ParentCmtBUS(username, password);
//        parBUS.insert(arrayParentCmt);
//        System.out.println("\nSub Comment :\n");
//
////        arraySubcmt = subComment.getContentSubComment(article, parentIDHasSub);
////        for (int i = 0; i < arraySubcmt.size(); i++) {
////            System.out.println("ID : " + arraySubcmt.get(i).getChildID() + "\nParentID : " + arraySubcmt.get(i).getParentID()
////                    + "\nLike : " + " " + arraySubcmt.get(i).getCmtLike() + "\nContent : " + arraySubcmt.get(i).getContent());
////            System.out.println();
////        }
//        SubCmtBUS subBus = new SubCmtBUS(username, password);
//        subBus.insert(arraySubcmt);
        
        System.out.println("Finished");
    }

}
