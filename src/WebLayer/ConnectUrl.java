/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebLayer;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;

/**
 *
 * @author Minh Nhat
 */
public class ConnectUrl {

    // connect jsou
    //private String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36";
    private String userAgent = "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0";
   
    private Document doc = null;

    private Document jsoupConnect1(String source_url) {
        Document doc = null;
        Response response = null;
        try {
            response = Jsoup.connect(source_url).timeout(60000).followRedirects(true)
                    .userAgent(userAgent)
                    .execute();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        try {
            doc = response.parse();
        } catch (IOException ex) {
            Logger.getLogger(ArticleObject.class.getName()).log(Level.SEVERE, null, ex);
        }

        return doc;
    }

    protected Document jsoupConnect(String source_url) {
        doc = jsoupConnect1(source_url);
        while (doc == null) {
            try {
                Thread.sleep(360000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectUrl.class.getName()).log(Level.SEVERE, null, ex);
            }
            doc = jsoupConnect1(source_url);
        }
        return doc;

    }

    private String jsoupConnectJson1(String source_url) {
        Response response = null;
        try {
            response = Jsoup.connect(source_url).timeout(60000)
                    .userAgent(userAgent)
                    .ignoreContentType(true).followRedirects(true)
                    .execute();
             
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
        return response.body();
       
    }
    
    protected String jsoupConnectJson(String source_url){
        String json = jsoupConnectJson1(source_url);
        while (json == null) {
            System.out.println("\n" + source_url + " error");
            try {
                Thread.sleep(900000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectUrl.class.getName()).log(Level.SEVERE, null, ex);
            }
            json = jsoupConnectJson1(source_url);
        }
        return json;
    }

    private Document jsoupConnectTuoiTrePost1(String url, int pageCount) {
        Document doc = null;
        try {
            // connect source
            doc = Jsoup.connect(url)
                    .data("page_number", String.valueOf(pageCount))
                    .timeout(60000)
                    .userAgent(userAgent)
                    .post();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return doc;
    }
    
    protected Document jsoupConnectTuoiTrePost(String url, int pageCount){
        doc = jsoupConnectTuoiTrePost1(url, pageCount);
        while(doc == null){
            try {
                Thread.sleep(360000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectUrl.class.getName()).log(Level.SEVERE, null, ex);
            }
            doc = jsoupConnectTuoiTrePost1(url, pageCount);
        }
        return doc;
    }

  }
