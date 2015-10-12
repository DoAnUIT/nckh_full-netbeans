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
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import WebLayer.*;
import java.io.IOException;
import java.sql.Array;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestUpdate {

    public static void main(String[] args) throws SQLException, IOException {

        String username = "root";
        String password = "";


        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        //calendar.set(Calendar.DAY_OF_MONTH, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.DAY_OF_MONTH, 9);
        calendar.set(Calendar.MONTH, 10-1);
        Timestamp lasttime = new Timestamp(calendar.getTimeInMillis());
        // lasstime = startime
        calendar.set(Calendar.DAY_OF_MONTH, 11);
        calendar.set(Calendar.MONTH, 10-1);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        Timestamp newtime = new Timestamp(calendar.getTimeInMillis());
        // newtime = endtime
        String lurl[] = {"http://tuoitre.vn"};//"http://vnexpress.net", "http://www.thanhnien.com.vn"*/};
        
        
        //insert news
        List<ThreadInsert> listInsertMagazine = new ArrayList<ThreadInsert>();
        int i = 0;
        for (String url : lurl) {
            System.out.println("\nBắt đầu insert : " + url + "\n");
            ThreadInsert x = new ThreadInsert(username, password, url, lasttime, newtime);
            //x.ThreadInsertByTypeNews.setPriority(i);
            listInsertMagazine.add(x);
            i++;
        }
        //end insert news 
        //ThreadUpdate a = new ThreadUpdate(1, username, password);
        
//        for (int i = 1; i <= 1; i++) {
//            ThreadUpdate a = new ThreadUpdate(i, username, password);
//        }
        System.out.println("Finished");

    }
}
