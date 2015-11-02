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
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Bao thanh nien khong co luot like cho tung bao bao
 * */
public class ArticleThanhNien extends ArticleObject {
//    private String username = null;
//    private String password = null;

    public ArticleThanhNien(String username, String password) {
        this.username = username;
        this.password = password;
        this.parCmt = new ParentCmtThanhNien();
        this.subCmt = new SubCmtThanhNien();
    }

    public ArticleDTO getArticleInformation1(String source_url, Boolean isServerPrevented) {
        // TODO Auto-generated method stub
        ArticleDTO art = new ArticleDTO();
        String tempt = null;
        Date d = null;
        Timestamp time = null;

        // get category id. and convert it to number
        //<editor-fold defaultstate="collapsed" desc="category id">
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
            case "van-hoa-nghe-thuat":
                cate = CategoryCommon.GIAI_TRI;
                break;
            case "cong-nghe-thong-tin":
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
        Document doc = jsoupConnect(source_url);
        // set article url
        art.setUrl(source_url);

        // set magazine
        art.setIDTableMagazine(1);
        // parse document
        Elements metas = doc.select("meta[property]");

        if (metas == null) {
            isServerPrevented = true;
            return null;
        }
        // date
        Element meta = metas.select("meta[property=article:published_time").first();
        if (meta == null) {
            isServerPrevented = true;
            return null;
        }
        tempt = meta.attr("content");
        tempt = tempt.substring(0, tempt.lastIndexOf('T'));
        tempt = tempt.replace('T', ' ');
        // tempt = tempt + ".000";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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

    // Get menu Web

    @Override
    public List<String> getMenuWeb(String source_url) {

        List<String> arrayMenu = new ArrayList<String>();
        Document doc = jsoupConnect(source_url);

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
        }

        return arrayMenu;
    }

    // Get news of each menu depend on time
    @Override
    @SuppressWarnings("empty-statement")
    public void setNewsOfEachMenuDependOnTime(String source_url, Timestamp newtime, Timestamp lasttime) {
        Document doc = null;
        ArticleDTO art = new ArticleDTO();
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
                doc = jsoupConnect(String.format(menuUrl + "%d.html", pageCount));

                Elements temptElements = null;
                Element temptElement = null;

                //<editor-fold defaultstate="collapsed" desc="class lvkd-content id divtoptin">
                // parse html để lấy link
                temptElement = doc.select(".lvkd-content").first();
                temptElements = temptElement.select("#divtoptin");
                for (Element element : temptElements) {
                    temptElement = element.select("a[href]").first();
                    url = temptElement.attr("href");
                    url = source_url + url;

                    //System.out.println(url);
                    // lấy nội dung của tin tương ứng với url
                    art = getArticleInformation(url);
                    if (art != null) {
                        // if date of month of art - day of month of lasttime =
                        // -1 => break outloop
                        if (isTheDayOfMonthValid(art, lasttime) == false && pageCount != 0) {
                            break outLoop;
                        }
                        // if time gets art > lasttime => get art
                        if (isTimeValid(art, newtime, lasttime)) {
                            System.out.println(url);
                            try {
                                this.insertDatabase(art);
                            } catch (SQLException ex) {
                                Logger.getLogger(ArticleThanhNien.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
//</editor-fold>

                pageCount++;
            } // end while loop
        }
    }

    @Override
    public int getArticleLike(int objectID) {
        return 0;
    }

}
