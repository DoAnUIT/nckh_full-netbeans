/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebLayer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 *
 * @author Minh Nhat
 */
public class ConnectUrl {

    // connect jsou
    private int count = 0;
    private String userAgent1 = "Mozilla/5.0 (Windows NT 6.3; rv:36.0) Gecko/20100101 Firefox/36.0";
    private String userAgent2 = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
   // private String userAgent = userAgent1;

    private String userAgent = "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";
    private boolean flag = true;

    protected Document jsoupConnect(String source_url) {
//        // flag flase and count > 500 => default proxy
//        if (count > 500 && flag == false) {
//            //System.setProperty("java.net.useSystemProxies", "true");
////            System.getProperties().remove("http.proxyHost");
////            System.getProperties().remove("http.proxyPort");
//            userAgent = userAgent1;
//            count = 0;
//            flag = true;
//        }
//        
//        ////////////// co gang dua proxy vao file xml
//        if (count > 500 && flag == true) {
////            System.setProperty("http.proxyHost", "125.212.219.221");
////            System.setProperty("http.proxyPort", "3128");
//            userAgent = userAgent2;
//            count = 0;
//            flag = false;
//        }
//        count++;
        Document doc = null;
        Response response = null;
        try {
            response = Jsoup.connect(source_url).timeout(0).followRedirects(true)
                    .userAgent(userAgent)
                    .execute();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //<editor-fold defaultstate="collapsed" desc="comment">
        if (response.statusCode() >= 400) {
            System.out.println("Thread ngu 60s");
            try {
                Thread.sleep(60000);
                try {
                    response = Jsoup.connect(source_url).timeout(10000).followRedirects(true)
                            .userAgent(userAgent)
                            .execute();
                } catch (IOException ex) {
                    Logger.getLogger(ArticleObject.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ArticleObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//</editor-fold>

        try {
            doc = response.parse();
        } catch (IOException ex) {
            Logger.getLogger(ArticleObject.class.getName()).log(Level.SEVERE, null, ex);
        }

        return doc;
    }

    protected String jsoupConnectJson(String source_url) {
        Response response = null;
//        // flag flase and count > 500 => default proxy
//        if (count > 250 && flag == false) {
//            //System.setProperty("java.net.useSystemProxies", "true");
////            System.getProperties().remove("http.proxyHost");
////            System.getProperties().remove("http.proxyPort");
//            userAgent = userAgent1;
//            count = 0;
//            flag = true;
//        }
//        
//        ////////////// co gang dua proxy vao file xml
//        if (count > 250 && flag == true) {
////            System.setProperty("http.proxyHost", "125.212.219.221");
////            System.setProperty("http.proxyPort", "3128");
//            userAgent = userAgent2;
//            count = 0;
//            flag = false;
//        }
//        count++;

        try {
            //json = IOUtils.toString(new URL(String.format(url + "%d", parentcomment.get(i))).openStream(), "UTF-8");
            response = Jsoup.connect(source_url).timeout(0)
                    .userAgent(userAgent)
                    .ignoreContentType(true)
                    .execute();
            //return rp.body();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //<editor-fold defaultstate="collapsed" desc="comment">
        if (response.statusCode() > 399) {
            System.out.println("Thread ngu 60s");
            try {
                Thread.sleep(60000);
                try {
                    response = Jsoup.connect(source_url).timeout(10000).followRedirects(true)
                            .userAgent(userAgent)
                            .execute();
                } catch (IOException ex) {
                    Logger.getLogger(ArticleObject.class.getName()).log(Level.SEVERE, null, ex);
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(ArticleObject.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
//</editor-fold>

        return response.body();
    }

    protected Document jsoupConnectTuoiTrePost(String url, int pageCount) {
        Document doc = null;
        try {
            // connect source
            doc = Jsoup.connect(url)
                    .data("page_number", String.valueOf(pageCount))
                    .timeout(5000)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36")
                    .post();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return doc;
    }
}
