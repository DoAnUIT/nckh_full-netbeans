/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebLayer;

import DTO.ArticleDTO;
import DTO.CategoryCommon;
import java.io.IOException;
import java.sql.SQLException;
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

    public ArticleTuoiTre(String username, String password) {
        this.username = username;
        this.password = password;
        this.parCmt = new ParentCmtTuoiTre();
        this.subCmt = new SubCmtTuoiTre();
        this.parseXML();
    }

    private ArticleDTO getArticleInformation1(String source_url, Boolean isServerPrevented) {

        // TODO Auto-generated method stub
        ArticleDTO art = new ArticleDTO();
        String tempt = null;
        Element meta = null;
        Timestamp time = null;
        Date d = null;
        Elements metas = null;
        Document doc = jsoupConnect(source_url);

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
        metas = doc.select("meta[property]");

        // date => chua lam
        meta = doc.select(".date").first();
        if (meta == null) {
            isServerPrevented = true;
            return null;
        }
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

        // set article url
        art.setUrl(source_url);

        // set magazine
        art.setIDTableMagazine(3);
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

    @Override
    public ArticleDTO getArticleInformation(String source_url) {
        Boolean isServerPrevented = false;
        ArticleDTO art = getArticleInformation1(source_url, isServerPrevented);
        while (art == null && isServerPrevented == true) {
            System.out.println("\nArticle Thanh nien return null");

            try {
                Thread.sleep(900000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ArticleTuoiTre.class.getName()).log(Level.SEVERE, null, ex);
            }
            isServerPrevented = false;
            art = getArticleInformation1(source_url, isServerPrevented);
        }
        return art;
    }

    // Get menu Web, ignore thể thao, nhịp sống số
    @Override
    public List<String> getMenuWeb(String source_url) {
        Document doc = null;
        List<String> arrayMenu = new ArrayList<String>();
        doc = jsoupConnect(source_url);
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
            String realCate = "Chính trị - Xã hội, Thế giới, Pháp luật, Kinh tế, Giáo dục, Văn hóa - Giải trí";

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
    public void setNewsOfEachMenuDependOnTime(String source_url, Timestamp newtime, Timestamp lasttime) {
        Document doc = null;
        ArticleDTO art = new ArticleDTO();
        // Document subDoc = null;
        String url = null;
        String menuUrl = null;

        // get menu
        List<String> arrayMenu = getMenuWeb(source_url);

        // get article each menu
        int pageCount = 1;
        Elements temptElements = null;
        Element temptElement = null;
        for (int i = 0; i < arrayMenu.size(); i++) {

            //<editor-fold defaultstate="collapsed" desc="get class="block-feature" and class="list-news">
            doc = jsoupConnect(arrayMenu.get(i));

            // block-feature
            temptElement = doc.select(".block-feature > a").first();
            url = temptElement.attr("href");
            art = getArticleInformation(url);
            if (art != null && isTheDayOfMonthValid(art, lasttime) != false) {
                if (isTimeValid(art, newtime, lasttime)) {
                    System.out.println(url);

                    try {
                        this.insertDatabase(art);
                    } catch (SQLException ex) {
                        Logger.getLogger(ArticleTuoiTre.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            // block list-news
            temptElements = doc.select(".list-news > li > a");
            for (int j = 0; j < temptElements.size(); j++) {
                temptElement = temptElements.get(j);
                url = temptElement.attr("href");
                art = getArticleInformation(url);
                if (art != null && isTheDayOfMonthValid(art, lasttime) != false) {
                    if (isTimeValid(art, newtime, lasttime)) {
                        System.out.println(url);

                        try {
                            this.insertDatabase(art);
                        } catch (SQLException ex) {
                            Logger.getLogger(ArticleTuoiTre.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
//</editor-fold>

            // id="newhot_most_content"
            menuUrl = apiTuoiTreMenu + arrayMenu.get(i).substring(arrayMenu.get(i).lastIndexOf('/') + 1);
            pageCount = 1;
            outLoop:
            while (true) {

                doc = jsoupConnectTuoiTrePost(menuUrl, pageCount);

                //<editor-fold defaultstate="collapsed" desc="class highligh">
                //temptElement = doc.select("#newhot_most_content").first();
                temptElements = doc.select(".block-left");
                // String subUrl;
                for (Element element : temptElements) {
                    temptElement = element.select("a[href]").first();
                    url = temptElement.attr("href");
                    // don't get info of article isn't in category
                    art = getArticleInformation(url);
                    if (art != null) {
                        if (isTheDayOfMonthValid(art, lasttime) == false) {
                            break outLoop;
                        }

                        if (isTimeValid(art, newtime, lasttime)) {
                            System.out.println(url);

                            try {
                                this.insertDatabase(art);
                            } catch (SQLException ex) {
                                Logger.getLogger(ArticleTuoiTre.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
//</editor-fold>

                pageCount++;
            } // end while loop

        }
    }

    // get article like
    @Override
    public int getArticleLike(int objectId) {
        String url = apiTuoiTreArticleLike + objectId;
        Document doc = jsoupConnect(url);

        Element ele = doc.select("span.sl").first();
        if (ele.text().trim().length() > 0) {
            return Integer.parseInt(ele.text().trim());
        } else {
            return 0;
        }
    }

}
