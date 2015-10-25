/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Minh Nhat
 */
import BusinessLayer.*;
import DTO.ArticleDTO;
import DTO.ParentCmtDTO;
import DTO.SubCmtDTO;
import WebLayer.ArticleTuoiTre;
import WebLayer.ArticleObject;
import WebLayer.IParentCmt;
import WebLayer.ISubCmt;
import WebLayer.ParentCmtTuoiTre;
import WebLayer.SubCmtTuoiTre;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import WebLayer.*;
import java.io.IOException;

public class TestUpdate {

    public static void main(String[] args) throws SQLException, IOException {

        String username = "nhat";
        String password = "mysql!@3";

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.DAY_OF_MONTH, 31);
        calendar.set(Calendar.MONTH, 10 - 1);

        Timestamp lasttime = new Timestamp(calendar.getTimeInMillis());

        //calendar.set(Calendar.HOUR_OF_DAY, 15);
        //calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
        //calendar.set(Calendar.MONTH, 10 - 1);
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 8);

        Timestamp newtime = new Timestamp(calendar.getTimeInMillis());

        System.out.println(lasttime);
        System.out.println(newtime);

        // 0h ngay 10.7 den 15h ngay 9.10
        String lurl[] = {"http://www.thanhnien.com.vn", "http://vnexpress.net", "http://tuoitre.vn"};
        //String url = "http://vnexpress.net";
        //String url = "http://www.thanhnien.com.vn";
        // String url = "http://tuoitre.vn";

//<editor-fold defaultstate="collapsed" desc="comment">
        WebLayer wl = new WebLayer(username, password);

//        for (String url : lurl) {
//            System.out.println("\nBắt đầu update : " + url +"\n");
//            wl.update(url, 1);
//        }
//</editor-fold>
        //insert
//        List<ThreadInsert> listIns = new ArrayList<ThreadInsert>();
//        for (String url : lurl) {
//            System.out.println("\nBắt đầu insert : " + url + "\n");
//            listIns.add(new ThreadInsert(username, password, url, lasttime, newtime));
//        }
//        UpdateTimeBUS upBUS = new UpdateTimeBUS(username, password);
//        List<Integer> listUpdateType = upBUS.GetListTypeUpdate();
////        for (int i = 0; i < listUpdateType.size(); i++) {
////            System.out.println(i);
////        }
//      
//        for (int i = 0; i< listUpdateType.size() - 1; i++) {
//             listThreadUpdate.add(new ThreadUpdate(username, password, listUpdateType.get(i),
//                     artThanhNien,artVnExpress,artTuoiTre));
//
//             System.out.println("update " + listUpdateType.get(i));
//        }
//        System.out.println("Finished");
//        List<ThreadUpdate> listThreadUpdate = new ArrayList<ThreadUpdate>();
//        ArticleObject artTN = new ArticleThanhNien(username, password);
//        ArticleObject artTT = new ArticleTuoiTre(username, password);
//        ArticleObject artVNE = new ArticleVnexpress(username, password);
//        
//        UpdateTimeBUS upBUS = new UpdateTimeBUS(username, password);
//        List<Integer> listUpdateType = upBUS.GetListTypeUpdate();
////        for (int i = 0; i < listUpdateType.size(); i++) {
////            System.out.println(i);
////        }
//      
//        for (int i = 0; i< listUpdateType.size() - 1; i++) {
//             listThreadUpdate.add(new ThreadUpdate(username, password, listUpdateType.get(i)
//             ,artTN,artTT,artVNE));
//             
//             System.out.println("update " + listUpdateType.get(i));
//        }
        System.out.println("Finished");

    }
}
