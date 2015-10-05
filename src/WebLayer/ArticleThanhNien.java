package WebLayer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import DTO.ArticleDTO;
import DTO.CategoryCommon;

/*
 * Bao thanh nien khong co luot like cho tung bao bao
 * */
public class ArticleThanhNien extends ArticleObject {

    @Override
    public ArticleDTO getArticleInformation(String source_url) {
        // TODO Auto-generated method stub
        ArticleDTO art = new ArticleDTO();
        String tempt = null;
        Date d = null;
        Timestamp time = null;

        Document doc = null;
        try {
            doc = Jsoup.connect(source_url).timeout(5000).followRedirects(true)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36")
                    .get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // set article url
        art.setUrl(source_url);

        // set magazine
        art.setIDTableMagazine(1);
        // parse document
        Elements metas = doc.select("meta[property]");

        // date
        Element meta = metas.select("meta[property=article:published_time").first();
        tempt = meta.attr("content");
        tempt = tempt.substring(0, tempt.lastIndexOf('T'));
        tempt = tempt.replace('T', ' ');
        // tempt = tempt + ".000";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            d = dateFormat.parse(tempt);
            time = new Timestamp(d.getTime());
        } catch (ParseException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        art.setArticleDate(time);

        // Title
        meta = metas.select("meta[property=og:title]").first(); // ok
        art.setTitle(meta.attr("content"));

        // Description
        meta = metas.select("meta[property=og:description").first();
        art.setDescription(meta.attr("content"));

        // ObjectID
        tempt = source_url.substring(0, source_url.lastIndexOf('.'));
        int e = 0;
        int objectID = 0;
        for (int i = tempt.length() - 1; i > 0; i--) {
            if (tempt.charAt(i) >= '0' && tempt.charAt(i) <= '9') {
                objectID += (int) ((tempt.charAt(i) - '0') * Math.pow(10, e));
                e++;
            } else {
                break;
            }

        }
        art.setObjectID(objectID);

            // get category id. and convert it to number
        // meta = doc.select("#mainMenu .active > a").first();
        // if (meta == null)
        // return null;
        tempt = source_url;
        tempt = tempt.substring(tempt.indexOf('/', tempt.indexOf('/') + 2) + 1);
        tempt = tempt.substring(0, tempt.indexOf('/'));

            // tempt = tempt.trim();
        // Chính trị - Xã hội, Quân sự , Thế giới, Kinh tế, Giáo dục, Thể thao,
        // Văn hóa - Giải trí, Công nghệ
        CategoryCommon cate;
        switch (tempt) {
            case "chinh-tri-xa-hoi":
            case "quan-su":
            case "doi-song":
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
            case "the-thao":
                cate = CategoryCommon.THE_THAO;
                break;
            case "van-hoa-giai-tri":
                cate = CategoryCommon.GIAI_TRI;
                break;
            case "cong-nghe":
                cate = CategoryCommon.KHOA_HOC_CONG_NGHE;
                break;
            default:
                cate = CategoryCommon.DEFAULT;
                break;
        }
            // if (source_url.matches("http://thethao.thanhnien.com.vn(.*)"))
        // cate = CategoryCommon.THE_THAO;
        if (cate.getValue() == 0) {
            return null;
        }
        art.setIDTableCategory(cate.getValue());
        // URl Image
        meta = metas.select("meta[property=og:image").first(); // ok
        art.setUrlPicture(meta.attr("content"));

        // facebook ok
        try {
            art.facebook = getContentOfFacebook(source_url);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return art;
    }

    // Get menu Web
    @Override
    public List<String> getMenuWeb(String source_url) {
        Document doc = null;
        List<String> arrayMenu = new ArrayList<String>();
        try {
            // PrintWriter writer = new PrintWriter("vnexpress_menu_web.txt",
            // "UTF-8");
            // connect source
            doc = Jsoup.connect(source_url).timeout(5000)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36")
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // get all category
        Elements categories = doc.select("#mainMenu .top-level > a");
        // categories = categories.select("li.top-level");
        // System.out.println(categories);
        Element category = null;
        // i = 1 to remove the first element
        for (int i = 0; i < categories.size(); i++) {
            category = categories.get(i);
            if (category.text().length() == 0) {
                continue;
            }
            String realCate = "Chính trị - Xã hội, Quân sự , Thế giới, Kinh tế, Giáo dục, Đời sống, Văn hóa - Giải trí, Công nghệ";
            if (realCate.matches("(.*)" + category.text() + "(.*)") == false) {
                continue;
            }
            // writer.println("Content : " + category.text());
            String tempt = category.attr("href");
            if (tempt.charAt(tempt.length() - 1) == '/') {
                tempt = tempt.substring(0, tempt.length() - 1);
            }
            if (tempt.matches("(.*)thanhnien.com.vn(.*)") == true) {
                arrayMenu.add(tempt);
            } else {
                arrayMenu.add(source_url + tempt);
            }
            // writer.println();
        }

        return arrayMenu;
    }

    // Get news of each menu depend on time
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
        for (int i = 0; i < arrayMenu.size(); i++) {
            // get page of each menu
            menuUrl = arrayMenu.get(i) + "/trang-";
            pageCount = 0;
            outLoop:
            while (true) {

                //<editor-fold defaultstate="collapsed" desc="jsoup connect">
                try {
                    // connect source
                    doc = Jsoup.connect(String.format(menuUrl + "%d.html", pageCount)).timeout(5000)
                            .userAgent(
                                    "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36")
                            .get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
//</editor-fold>

                Elements temptElements = null;
                Element temptElement = null;

//                if (pageCount == 1) {
//
//                    //<editor-fold defaultstate="collapsed" desc="id divshowothers">
//                    temptElement = doc.select("#divshowothers").first();
//                    temptElements = temptElement.select(".bottom-tt-one");
//
//                    for (int j = 0; j < temptElements.size(); j++) {
//                        temptElement = temptElements.get(j);
//                        temptElement = temptElement.select("a").first();
//                        url = temptElement.attr("href");
//                        url = source_url + url;
//
//                        // don't get info of article isn't in category
//                        // if (url.matches(arrayMenu.get(i) + "(.*)") == false)
//                        // continue;
//                        art = getArticleInformation(url);
//                        if (art != null) {
//
//                            if (isTheDayOfMonthValid(art, lasttime) == false) {
//                                break;
//                            }
//                            if (art.getArticleDate().getTime() > lasttime.getTime()
//                                    && art.getArticleDate().getTime() < newtime.getTime()) {
//
//                                artArray.add(art);
//                            }
//                        }
//
//                    }
////</editor-fold>
//
//                } // end if (pageCount == 1) condition

                //<editor-fold defaultstate="collapsed" desc="class lvkd-content id divtoptin">
                temptElement = doc.select(".lvkd-content").first();
                temptElements = temptElement.select("#divtoptin");
                // String subUrl;
                for (Element element : temptElements) {
                    temptElement = element.select("a[href]").first();
                    url = temptElement.attr("href");
                    url = source_url + url;
                    // art = getAriticleInformationDependOnTime(url, lasttime);
                    // don't get info of article isn't in category
                    // if (url.matches(arrayMenu.get(i) + "(.*)") == false)
                    // break;
                    System.out.println(url);
                    art = getArticleInformation(url);
                    if (art != null) {
                        // if date of month of art - day of month of lasttime =
                        // -1 => break outloop
                        if (isTheDayOfMonthValid(art, lasttime) == false) {
                            break outLoop;
                        }
                        // if time gets art > lasttime => get art
                        if (isTimeValid(art, newtime, lasttime)) {
                            artArray.add(art);
                        }
                    }
                }
//</editor-fold>

                // writer.println("End page " + pageCount);
                pageCount++;
            } // end while loop
        }
        return artArray;
    }

    @Override
    public int getArticleLike(int objectID) {
        return 0;
    }

}
