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

    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36";

    protected Document jsoupConnect(String source_url) {
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

        try {
            doc = response.parse();
        } catch (IOException ex) {
            Logger.getLogger(ArticleObject.class.getName()).log(Level.SEVERE, null, ex);
        }

        return doc;
    }

    protected String jsoupConnectJson(String source_url) {
        Response response = null;

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

        return response.body();
    }

    protected Document jsoupConnectTuoiTrePost(String url, int pageCount) {
        Document doc = null;
        try {
            // connect source
            doc = Jsoup.connect(url)
                    .data("page_number", String.valueOf(pageCount))
                    .timeout(0)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36")
                    .post();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return doc;
    }
}
