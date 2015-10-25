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
import WebLayer.ThreadInsert;
import WebLayer.ThreadUpdate;
import java.util.logging.Level;
import java.util.logging.Logger;

class Insert extends Thread {
//<editor-fold defaultstate="collapsed" desc="code inside">

    String lurl[] = {"http://www.thanhnien.com.vn", "http://vnexpress.net", "http://tuoitre.vn"};

    Calendar calendar = null;
    Timestamp lasttime = null;
    Timestamp newtime = null;

    String threadName = null;
    String username = null;
    String password = null;

    ArticleBUS artBUS = null;

    List<ThreadInsert> listIns = new ArrayList<ThreadInsert>();

    Thread t = null;

    Insert(String threadName, String username, String password) throws SQLException {
        this.threadName = threadName;
        this.username = username;
        this.password = password;
        artBUS = ArticleBUS.getInstance(username, password);

        this.setName("Thread " + threadName);
        System.out.println("Creating " + threadName);

    }

    @Override
    public void run() {

        System.out.println("Running " + threadName);
        calendar = new GregorianCalendar();

        while (true) {
            //calendar = new GregorianCalendar();
//            calendar.set(Calendar.MINUTE, 0);
//            calendar.set(Calendar.SECOND, 0);
//            calendar.set(Calendar.HOUR_OF_DAY, 0);

            // calendar.set(Calendar.DAY_OF_MONTH, 23);
            // calendar.set(Calendar.MONTH, 10 - 1);
            lasttime = artBUS.getMaxArticleTime();
            if (lasttime == null) {
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 90);
                lasttime = new Timestamp(calendar.getTimeInMillis());
                calendar = new GregorianCalendar();
            }

            // calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
            //  calendar.set(Calendar.MONTH, 10 - 1);
            newtime = new Timestamp(calendar.getTimeInMillis());
         // 0h ngay 10.7 den 15h ngay 9.10

            //insert
            for (String url : lurl) {
                System.out.println("Bắt đầu insert : " + url + "\n");
                listIns.add(new ThreadInsert(username, password, url, newtime,lasttime));
            }

            try {
                lasttime = newtime;
                calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + 8);
                newtime = new Timestamp(calendar.getTimeInMillis());
                Thread.sleep(8 * 3600000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Insert.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
//</editor-fold>
}

class Update extends Thread {

//<editor-fold defaultstate="collapsed" desc="code inside">
    private Thread t = null;
    private String threadName = null;
    private String username = null;
    private String password = null;

    List<ThreadUpdate> listThreadUpdate = new ArrayList<ThreadUpdate>();
    ArticleObject artTN = null;
    ArticleObject artTT = null;
    ArticleObject artVNE = null;

    UpdateTimeBUS upBUS = null;
    List<Integer> listUpdateType = null;

    Update(String threadName, String username, String password) {
        this.threadName = threadName;
        this.username = username;
        this.password = password;
        artTN = new ArticleThanhNien(username, password);
        artTT = new ArticleTuoiTre(username, password);
        artVNE = new ArticleVnexpress(username, password);
        upBUS = new UpdateTimeBUS(username, password);
        listUpdateType = upBUS.GetListTypeUpdate();
        this.setName("Thread " + threadName);
        System.out.println("Creating " + threadName);

    }

    public void run() {

        System.out.println("Running " + threadName);

        for (int i = 0; i < listUpdateType.size() - 1; i++) {
            listThreadUpdate.add(new ThreadUpdate(username, password, listUpdateType.get(i), artTN, artTT, artVNE));

            System.out.println("update " + listUpdateType.get(i));
        }
    }

    public void start() {
        System.out.println("Starting " + threadName);
        if (t == null) {
            t = new Thread(this, threadName);
            t.start();
        }
    }
//</editor-fold>
}

public class main {

    public static void main(String[] args) throws IOException, NamingException, SQLException {
        // TODO Auto-generated method stub
        String username = "nhat";
        String password = "mysql!@3";

        Insert insert = new Insert("Insert", username, password);
        insert.start();

        Update update = new Update("Update", username, password);
        //update.start();
    }

}
