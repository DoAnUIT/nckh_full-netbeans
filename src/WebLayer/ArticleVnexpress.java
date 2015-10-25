package WebLayer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import DTO.ArticleDTO;
import DTO.CategoryCommon;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticleVnexpress extends ArticleObject {

    private String apiVNELikeStart = "http://usi.saas.vnexpress.net/widget/index/?likeid=";
    private String apiVNELikeEnd = "-1";

    public ArticleVnexpress(String username, String password) {
        this.username = username;
        this.password = password;
        this.parCmt = new ParentCmtVnexpress();
        this.subCmt = new SubCmtVnexpress();
    }

    // get article Information, don't get information of video => remove link
    // video in code

    @Override
    public ArticleDTO getArticleInformation(String source_url) {
        if (source_url.matches("(.*)http://video.vnexpress.net(.*)") == true
                || source_url.matches("(.*)http://vnexpress.net/interactive(.*)") == true) {
            return null;
        }

        ArticleDTO art = new ArticleDTO();
        String url = null;

        String tempt = null;
        Document doc = jsoupConnect(source_url);
        

//</editor-fold>
        // Category ID
        //<editor-fold defaultstate="collapsed" desc="Category id">
        Element meta = doc.select("#menu_web > .active").first();
        tempt = meta.text();
        tempt = tempt.trim();
        CategoryCommon cate;
        switch (tempt) {
            case "Thời sự":
            case "Pháp luật":
                cate = CategoryCommon.THOI_SU;
                break;
            case "Thế giới":
                cate = CategoryCommon.THE_GIOI;
                break;
            case "Kinh doanh":
                cate = CategoryCommon.KINH_DOANH;
                break;
            case "Giáo dục":
                cate = CategoryCommon.GIAO_DUC;
                break;
            case "Thể thao":
                cate = CategoryCommon.THE_THAO;
                break;
            case "Giải trí":
                cate = CategoryCommon.GIAI_TRI;
                break;
            case "Khoa học":
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
        // remove all &nbsp
        // Elements metas = doc.select(":containsOwn(\u00a0)").remove();
        Elements metas = doc.select("meta[name]");

        // set article url
        art.setUrl(source_url);

        // set magazine
        art.setIDTableMagazine(2);
        // Date
        meta = doc.select(".block_timer_share > .block_timer").first();
        // System.out.println(source_url);
        tempt = meta.text();
        tempt = tempt.replace('|', ' ');
        tempt = tempt.substring(tempt.indexOf(',') + 1);
        tempt = tempt.trim();
        tempt = tempt.substring(0, tempt.lastIndexOf(' '));

        // remove &nbsp
        if (tempt.indexOf("\u00a0") > 0) {
            tempt = tempt.replace('\u00a0', ' ');
        }
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Timestamp daTimestamp = null;
        try {
            Date d = dateFormat.parse(tempt);
            daTimestamp = new Timestamp(d.getTime());
        } catch (ParseException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        art.setArticleDate(daTimestamp);

        // Description
        meta = metas.select("meta[name=description").first();
        art.setDescription(meta.attr("content"));
        art.setDescription(art.getDescription().split("-")[0]);

        // Article ID
        meta = metas.select("meta[name=tt_article_id").first();
        art.setObjectID(Integer.parseInt(meta.attr("content")));

        // url picture
        meta = doc.select("meta[property=og:image").first();
        art.setUrlPicture(meta.attr("content"));

        // title
        meta = doc.select("meta[property=og:title]").first();
        tempt = meta.attr("content");
        art.setTitle(tempt.substring(0, tempt.lastIndexOf('-')));

        // article like
        art.setArticleLike(getArticleLike(art.getObjectID()));

        // facebook
        try {
            art.facebook = getContentOfFacebook(source_url);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return art;
    }

    // get Like
    @Override
    public int getArticleLike(int objectID) {
        String url = apiVNELikeStart + objectID + apiVNELikeEnd;

        int count = 0;
        String tempt = "";
        String data = jsoupConnectJson(url);
//        try {
//            data = IOUtils.toString(new URL(url).openStream(), "UTF-8");
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
        int index = data.indexOf(";;");
        for (int i = index; i > 0; i--) {
            if (data.charAt(i) == ' ') {
                break;
            }
            if (data.charAt(i) >= '0' && data.charAt(i) <= '9') {
                tempt += data.charAt(i);
            }
        }
        for (int i = 0; i < tempt.length(); i++) {
            count += (tempt.charAt(i) - '0') * Math.pow(10, i);
        }
        return count;
    }

    // Get menu Web
    @Override
    public List<String> getMenuWeb(String source_url) {
        Document doc = jsoupConnect(source_url);
        List<String> arrayMenu = new ArrayList<String>();

        // get all category
        Elements categories = doc.select("ul#menu_web");
        // Elements categories = doc.select("ul#mainMenu");
        categories = categories.select("a");
        // System.out.println(categories);
        Element category = null;
        // i = 1 to remove the first element
        for (int i = 1; i < categories.size(); i++) {
            category = categories.get(i);
            if (category.text().length() == 0) {
                continue;
            }
            String realCate = "Thời sự, Thế giới, Kinh doanh, Giáo dục,Giải trí,Pháp luật, Thể thao,  Khoa học";
            // String realCate = "Khoa học";

            if (realCate.matches("(.*)" + category.text() + "(.*)") == false) {
                continue;
            }
            String tempt = category.attr("href");
            if (tempt.charAt(tempt.length() - 1) == '/') {
                tempt = tempt.substring(0, tempt.length() - 1);
            }
            if (tempt.matches("(.*)vnexpress(.*)") == true) {
                arrayMenu.add(tempt);
            } else {
                arrayMenu.add(source_url + tempt);
            }
        }

        return arrayMenu;
    }

    // get news of each menu depend on time
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
        for (int i = 0; i < arrayMenu.size(); i++) {
            // get page of each menu
            menuUrl = arrayMenu.get(i) + "/page/";
            pageCount = 1;
            outLoop:
            while (true) {
                doc = jsoupConnect(String.format(menuUrl + "%d.html", pageCount));
                
                Elements temptElements = null;
                Element temptElement = null;
                if (pageCount == 1) {

                    //<editor-fold defaultstate="collapsed" desc="div box_hot_news">
                    temptElements = doc.select(".box_hot_news");

                    temptElement = temptElements.select(".title_news").first();
                    temptElement = temptElement.select("a[href]").first();
                    url = temptElement.attr("href");

                    art = getArticleInformation(url);
                    if (art != null && isTheDayOfMonthValid(art, lasttime) != false) {
                        if (isTimeValid(art, newtime, lasttime)) {
                            System.out.println(url);

                            try {
                                this.insertDatabase(art);
                            } catch (SQLException ex) {
                                Logger.getLogger(ArticleVnexpress.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
//</editor-fold>

                    //<editor-fold defaultstate="collapsed" desc="div box_sub_hot_news">
                    temptElements = doc.select("div.box_sub_hot_news").select("div.title_news");

                    for (int j = 0; j < temptElements.size(); j++) {
                        temptElement = temptElements.get(j);
                        temptElement = temptElement.select("a[href]").first();
                        url = temptElement.attr("href");

                        art = getArticleInformation(url);
                        if (art != null) {

                            if (isTheDayOfMonthValid(art, lasttime) == false) {
                                break;
                            }
                            if (isTimeValid(art, newtime, lasttime)) {
                                System.out.println(url);

                                try {
                                    this.insertDatabase(art);
                                } catch (SQLException ex) {
                                    Logger.getLogger(ArticleVnexpress.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        }
                    }
                    //</editor-fold>

                }// end if (pageCount == 1) condition

                //<editor-fold defaultstate="collapsed" desc="id news_home">
                temptElements = doc.select("#news_home");
                temptElements = temptElements.select(".title_news");
                // String subUrl;
                for (Element element : temptElements) {
                    temptElement = element.select("a[href]").first();
                    url = temptElement.attr("href");
                    // don't get info of article isn't in category
                    art = getArticleInformation(url);
                    if (art != null) {
                        // if date of month of art - day of month of lasttime =
                        // -1 => break outloop
                        if (isTheDayOfMonthValid(art, lasttime) == false) {
                            break outLoop;
                        }
                        // if time gets art > lasttime => get art
                        if (isTimeValid(art, newtime, lasttime)) {
                            System.out.println(url);

                            try {
                                this.insertDatabase(art);
                            } catch (SQLException ex) {
                                Logger.getLogger(ArticleVnexpress.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
//</editor-fold>

                pageCount++;
            } // end while loop
        }
    }

}
