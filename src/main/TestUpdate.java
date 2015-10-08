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

public class TestUpdate {

    public static void main(String[] args) throws SQLException, IOException {

        String username = "root";
        String password = "";


        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        calendar.set(Calendar.DAY_OF_MONTH, 6);
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.DAY_OF_MONTH, 7);
        calendar.set(Calendar.MONTH, 10-1);
        Timestamp lasttime = new Timestamp(calendar.getTimeInMillis());

        calendar.set(Calendar.DAY_OF_MONTH, 7);
        calendar.set(Calendar.MONTH, 10-1);
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        Timestamp newtime = new Timestamp(calendar.getTimeInMillis());

        WebLayer wl = new WebLayer(username, password);
        String lurl[] = {"http://tuoitre.vn","http://vnexpress.net", "http://www.thanhnien.com.vn"
        };
        //String url = "http://vnexpress.net";
        //String url = "http://www.thanhnien.com.vn";
        // String url = "http://tuoitre.vn";
        //wl.update(url, 1);
        for (String url : lurl) {
        System.out.println("\nBắt đầu insert : " + url + "\n");
            wl.insert(url, newtime, lasttime);
        }

//        for (String url : lurl) {
//            System.out.println("\nBắt đầu update : " + url +"\n");
//            wl.update(url, 1);
//        }
        System.out.println("Finished");

    }
}
