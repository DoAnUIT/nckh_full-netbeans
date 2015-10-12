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

        String username = "root";
        String password = "";

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
//        calendar.set(Calendar.DAY_OF_MONTH, 10);
//        calendar.set(Calendar.MONTH, 7 - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 11);
        calendar.set(Calendar.MONTH, 10 - 1);
        Timestamp lasttime = new Timestamp(calendar.getTimeInMillis());

        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.DAY_OF_MONTH, 12);
        calendar.set(Calendar.MONTH, 10 - 1);
        Timestamp newtime = new Timestamp(calendar.getTimeInMillis());

        // 0h ngay 10.7 den 15h ngay 9.10
        WebLayer wl = new WebLayer(username, password);
        String lurl[] = {"http://www.thanhnien.com.vn", "http://vnexpress.net","http://tuoitre.vn"};
        //String url = "http://vnexpress.net";
        //String url = "http://www.thanhnien.com.vn";
        // String url = "http://tuoitre.vn";
        //wl.update(url, 1);
        
        //insert
//        List<ThreadInsert> listIns = new ArrayList<ThreadInsert>();
//        for (String url : lurl) {
//            System.out.println("\nBắt đầu insert : " + url + "\n");
//            listIns.add(new ThreadInsert(username, password, url, newtime, newtime));
//        }
        
        
        List<ThreadUpdate> listThreadUpdate = new ArrayList<ThreadUpdate>();
        UpdateTimeBUS upBUS = new UpdateTimeBUS(username, password);
        List<Integer> listUpdateType = upBUS.GetListTypeUpdate();
        for (int i = 0; i < listUpdateType.size(); i++) {
            System.out.println(i);
        }
        ThreadUpdate x = new ThreadUpdate(username, password, 1);
//        for (int idUpdate : listUpdateType) {
//             listThreadUpdate.add(new ThreadUpdate(username, password, idUpdate));
//             System.out.println("update " + idUpdate);
//        }
        System.out.println("Finished");

    }
}
