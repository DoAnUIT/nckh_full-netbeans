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
        String username = "nhat";
        String password = "mysql!@3";

        Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, 7-1);
        Timestamp lasttime = new Timestamp(calendar.getTimeInMillis());

        calendar.set(Calendar.DAY_OF_MONTH, 4);
        calendar.set(Calendar.MONTH, 10-1);
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        Timestamp newtime = new Timestamp(calendar.getTimeInMillis());

        WebLayer wl = new WebLayer(username, password);
        String lurl[] = {"http://vnexpress.net", "http://www.thanhnien.com.vn",
         "http://tuoitre.vn"};
        //String url = "http://vnexpress.net";
        //String url = "http://www.thanhnien.com.vn";
        // String url = "http://tuoitre.vn";
        //wl.update(url, 1);
        for (String url : lurl) {
            wl.insert(url, newtime, lasttime);
        }

        System.out.println("Finished");

    }
}
