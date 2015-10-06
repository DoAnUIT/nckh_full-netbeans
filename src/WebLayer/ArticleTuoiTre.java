/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebLayer;

import DTO.ArticleDTO;
import DTO.CategoryCommon;
import com.sun.org.apache.xalan.internal.lib.ExsltDatetime;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Minh Nhat
 */
public class ArticleTuoiTre extends ArticleObject {

    static List<String> linkList;
    static int MAX_ARTICLE = 1;
    static int MAX_MONTH_GET_NEWS = 1;
    static int MAX_DATE_GET_NEWS = 1;
    String apiTuoiTreArticleLike = "http://cm.tuoitre.vn/like/ttocreateiframe?app_id=6&object_id=";
    String apiTuoiTreMenu = "http://tuoitre.vn/page?ajax=169---tto_custom_list&slug=";

    @Override
    public ArticleDTO getArticleInformation(String source_url) {
        
        // TODO Auto-generated method stub
        ArticleDTO art = new ArticleDTO();
        String tempt = null;
        String url = null;

        Document doc = JsoupConnect(source_url);
        if(doc == null)
            return null;
        
         // get category id. and convert it to number
        //<editor-fold defaultstate="collapsed" desc="category id">
        tempt = source_url;
        tempt = tempt.substring(tempt.indexOf('/', tempt.indexOf('/') + 2) + 1);
        tempt = tempt.substring(tempt.indexOf('/') + 1);
        tempt = tempt.substring(0, tempt.indexOf('/'));
        
        // tempt = tempt.trim();
        // Chính trị - Xã hội, Quân sự , Thế giới, Kinh tế, Giáo dục, Thể thao,
        // Văn hóa - Giải trí, Công nghệ
        CategoryCommon cate;
        switch (tempt) {
            case "chinh-tri-xa-hoi":
            case "phap-luat":
                //case "doi-song":
                cate = CategoryCommon.THOI_SU;
                break;
            case "the-gioi":
                cate = CategoryCommon.THE_GIOI;
                break;
            case "kinh-te":
                cate = CategoryCommon.KINH_DOANH;
                break;
            case "giao-duc":
                cate = CategoryCommon.GIAO_DUC;
                break;
            case "the-thao": ///////////////////
                cate = CategoryCommon.THE_THAO;
                break;
            case "van-hoa-giai-tri":
                cate = CategoryCommon.GIAI_TRI;
                break;
            case "cong-nghe": ///////////////////////
                cate = CategoryCommon.KHOA_HOC_CONG_NGHE;
                break;
            default:
                cate = CategoryCommon.DEFAULT;
                break;
        }
        
        if (cate.getValue() == 0) {
            return null;
        }
        art.setIDTableCategory(cate.getValue());
//</editor-fold>

        // parse document
        Elements metas = doc.select("meta[property]");

        // set article url
        art.setUrl(source_url);

        // set magazine
        art.setIDTableMagazine(3);
        Date d = null;
        Timestamp time = null;
        // date => chua lam
        Element meta = doc.select("span.date").first();
        tempt = meta.text();
        tempt = tempt.substring(0, tempt.indexOf('G'));
        tempt = tempt.trim();
            //tempt = tempt.replace('T', ' ');
        // tempt = tempt + ".000";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            d = dateFormat.parse(tempt);
            time = new Timestamp(d.getTime());
        } catch (ParseException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        art.setArticleDate(time);

        // Description
        meta = metas.select("meta[property=og:description").first();
        art.setDescription(meta.attr("content")); // ok

        // ObjectID
        tempt = source_url.substring(0, source_url.lastIndexOf('.'));
        tempt = tempt.substring(tempt.lastIndexOf('/') + 1);
        art.setObjectID(Integer.parseInt(tempt));

       
        // URl Image
        meta = metas.select("meta[property=og:image").first(); // ok
        art.setUrlPicture(meta.attr("content"));

        // title
        meta = metas.select("meta[property=og:title]").first();
        tempt = meta.attr("content");
        tempt = tempt.substring(0, tempt.lastIndexOf('-'));
        art.setTitle(tempt);

        // article like
        art.setArticleLike(getArticleLike(art.getObjectID()));

        // facebook ok
        try {
            art.facebook = getContentOfFacebook(source_url);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        return art;
    }

    // Get menu Web, ignore thể thao, nhịp sống số
    @Override
    public List<String> getMenuWeb(String source_url) {
        Document doc = null;
        List<String> arrayMenu = new ArrayList<String>();
        doc = JsoupConnect(source_url);
        // get all category
        Elements categories = doc.select(".clearfix > a");
        // categories = categories.select("li.top-level");
        // System.out.println(categories);
        Element category = null;
        // i = 1 to remove the first element
        for (int i = 0; i < categories.size(); i++) {
            category = categories.get(i);
            if (category.text().length() == 0) {
                continue;
            }
            String realCate = "Chính trị - Xã hội, Thế giới, Pháp luật, Kinh tế, Giáo dục, Đời sống, Văn hóa - Giải trí";
            if (realCate.matches("(.*)" + category.text() + "(.*)") == false) {
                continue;
            }
            // writer.println("Content : " + category.text());
            String tempt = category.attr("href");
            if (tempt.charAt(tempt.length() - 1) == '/') {
                tempt = tempt.substring(0, tempt.length() - 1);
            }
            arrayMenu.add(tempt);

            // writer.println();
        }

        return arrayMenu;
    }

    // get article of each menu
    @Override
    public List<ArticleDTO> getNewsOfEachMenuDependOnTime(String source_url, Timestamp newtime, Timestamp lasttime) {
        Document doc = null;
        ArticleDTO art = new ArticleDTO();
        ArrayList<ArticleDTO> artArray = new ArrayList<ArticleDTO>();
        // Document subDoc = null;
        String url = null;
        String menuUrl = null;

        // get menu
        List<String> arrayMenu = getMenuWeb(source_url);

        // get article each menu
        int pageCount = 0;
        Elements temptElements = null;
        Element temptElement = null;
        for (int i = 0; i < arrayMenu.size(); i++) {

            //<editor-fold defaultstate="collapsed" desc="get class="block-feature" and class="list-news">
            doc = JsoupConnect(arrayMenu.get(i));

            // block-feature
            temptElement = doc.select(".block-feature > a").first();
            url = temptElement.attr("href");
            System.out.println(url);
            art = getArticleInformation(url);
            if (art != null && isTheDayOfMonthValid(art, lasttime) != false) {
                if (isTimeValid(art, newtime, lasttime)) {
                    artArray.add(art);
                }
            }

            // block list-news
            temptElements = doc.select(".list-news > li > a");
            for (int j = 0; j < temptElements.size(); j++) {
                temptElement = temptElements.get(j);
                url = temptElement.attr("href");
                System.out.println(url);
                art = getArticleInformation(url);
                if (art != null && isTheDayOfMonthValid(art, lasttime) != false) {
                    if (isTimeValid(art, newtime, lasttime)) {
                        artArray.add(art);
                    }
                }
            }
//</editor-fold>

            // id="newhot_most_content"
            menuUrl = apiTuoiTreMenu + arrayMenu.get(i).substring(arrayMenu.get(i).lastIndexOf('/') + 1);
            pageCount = 1;
            outLoop:
            while (true) {

                //<editor-fold defaultstate="collapsed" desc="jsoup connect">
                try {
                    // connect source
                    doc = Jsoup.connect(menuUrl)
                            .data("page_number", String.valueOf(pageCount))
                            .timeout(5000)
                            .userAgent(
                                    "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36")
                            .post();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//</editor-fold>

                //<editor-fold defaultstate="collapsed" desc="class highligh">
                //temptElement = doc.select("#newhot_most_content").first();
                temptElements = doc.select(".block-left");
                // String subUrl;
                for (Element element : temptElements) {
                    temptElement = element.select("a[href]").first();
                    url = temptElement.attr("href");
                    System.out.println(url);
                    // don't get info of article isn't in category
                    art = getArticleInformation(url);
                    if (art != null) {
                        if (isTheDayOfMonthValid(art, lasttime) == false) {
                            break outLoop;
                        }

                        if (isTimeValid(art, newtime, lasttime)) {
                            artArray.add(art);
                        }
                    }
                }
//</editor-fold>

                pageCount++;
            } // end while loop

        }
        return artArray;
    }

    // get article like
    @Override
    public int getArticleLike(int objectId) {
        String url = apiTuoiTreArticleLike + objectId;
        Document doc = JsoupConnect(url);

        Element ele = doc.select("span.sl").first();
        return Integer.parseInt(ele.text().trim());
    }

}
