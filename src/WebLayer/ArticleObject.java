package WebLayer;

import BusinessLayer.ArticleBUS;
import BusinessLayer.ParentCmtBUS;
import BusinessLayer.SubCmtBUS;
import java.sql.Timestamp;
import java.util.List;

import DTO.ArticleDTO;
import DTO.FacebookDTO;
import DTO.ParentCmtDTO;
import DTO.SubCmtDTO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class ArticleObject {

    private String apiFBStart = "https://graph.facebook.com/fql?q=SELECT%20share_count,%20like_count,%20comment_count%20FROM%20link_stat%20where%20url=%27";
    private String apiFBEnd = "%27";
    private List<ParentCmtDTO> temptPar = null;
    private List<SubCmtDTO> temptSub = null;
    private List<Integer> parentIDHasSub = new ArrayList<Integer>();
    protected String username = null;
    protected String password = null;
    protected IParentCmt parCmt = null;
    protected ISubCmt subCmt = null;

    // Lấy thông tin của từng bài báo
    public abstract ArticleDTO getArticleInformation(String source_url);

    // Lấy link của từng menu web
    public abstract List<String> getMenuWeb(String source_url);

    // 
    public abstract void setNewsOfEachMenuDependOnTime(String source_url, Timestamp newtime, Timestamp lasttime);

    // get article like
    public abstract int getArticleLike(int objectID);

    // facebook
    protected FacebookDTO getContentOfFacebook(String source_url) throws MalformedURLException, IOException {
        String url = apiFBStart + source_url + apiFBEnd;

        FacebookDTO fb = new FacebookDTO();
        String json = IOUtils.toString(new URL(url).openStream(), "UTF-8");
        JsonParser parser = new JsonParser();

        JsonElement element = parser.parse(json);
        JsonObject root = element.getAsJsonObject();
        JsonArray datas = root.getAsJsonArray("data");
        JsonObject data = datas.get(0).getAsJsonObject();

        Gson gson = new Gson();
        fb = gson.fromJson(data, FacebookDTO.class);

        return fb;
        // khong quan trong
    }

    // Check if date of month valid
    protected boolean isTheDayOfMonthValid(ArticleDTO art, Timestamp lasttime) {
        Calendar calArt = Calendar.getInstance();
        calArt.setTime(art.getArticleDate());

        Calendar calLast = Calendar.getInstance();
        calLast.setTime(lasttime);
        
        if (calArt.get(Calendar.DAY_OF_YEAR) - calLast.get((Calendar.DAY_OF_YEAR)) >= 0) {
            return true;
        }
        return false;
    }

    // check if date time valid, lasstime < art.getDate < newtime
    protected boolean isTimeValid(ArticleDTO art, Timestamp newtime, Timestamp lasttime) {
        if (art.getArticleDate().getTime() >= lasttime.getTime()
                && art.getArticleDate().getTime() < newtime.getTime()) {
            return true;
        }
        return false;
    }

    // connect jsou
    protected Document JsoupConnect(String source_url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(source_url).timeout(10000).followRedirects(true)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36")
                    .get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException ex) {
            Logger.getLogger(ArticleObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc;
    }

    protected void insertDatabase(ArticleDTO art) throws SQLException {
        ArticleBUS artBUS = new ArticleBUS(username, password);
        ParentCmtBUS parBUS = new ParentCmtBUS(username, password);
        SubCmtBUS subBUS = new SubCmtBUS(username, password);

        // insert art to database => art have idtablearticle
        if(!artBUS.insert(art))
            return;
        temptPar = parCmt.getContentParentComment(art, parentIDHasSub);
        // nếu temptPar != null => insert vào database
        // có parentcmt thì mới có subcmt
        if (temptPar != null) {
            parBUS.insert(temptPar);

            if(parentIDHasSub != null){
            temptSub = subCmt.getContentSubComment(art, parentIDHasSub);
            if (temptSub != null) {
                subBUS.insert(temptSub);
            }
            }
        }
        temptPar = null;
        temptSub = null;
        parentIDHasSub.clear();

    }

    protected void updateDatabase(ArticleDTO art) throws SQLException {
        ArticleBUS artBUS = new ArticleBUS(username, password);
        ParentCmtBUS parBUS = new ParentCmtBUS(username, password);
        SubCmtBUS subBUS = new SubCmtBUS(username, password);

        // update art to database 
        artBUS.update(art);
        temptPar = parCmt.getContentParentComment(art, parentIDHasSub);
        // nếu temptPar != null => insert vào database. Có parentcmt thì mới có subcmt
        if (temptPar != null) {
            parBUS.update(temptPar);

            temptSub = subCmt.getContentSubComment(art, parentIDHasSub);
            if (temptSub != null) {
                subBUS.update(temptSub);
            }
        }
        temptPar = null;
        temptSub = null;
        parentIDHasSub.clear();

    }

}
