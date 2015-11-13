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
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public abstract class ArticleObject extends ConnectUrl {

    private String apiFBStart = "https://graph.facebook.com/fql?q=SELECT%20share_count,%20like_count,%20comment_count%20FROM%20link_stat%20where%20url=%27";
    private String apiFBEnd = "%27";
    private List<ParentCmtDTO> temptPar = null;
    private List<SubCmtDTO> temptSub = null;
    private List<Integer> parentIDHasSub = new ArrayList<Integer>();
    protected String username = null;
    protected String password = null;
    protected IParentCmt parCmt = null;
    protected ISubCmt subCmt = null;
    private int count = 0;
    private int article1 = 0;
    private int article200 = 0;
    private int article1000 = 0;
    
    

    // count
    public void setCount(int a){
        this.count = a;
    }
    
    public int getCount (){
        return this.count;
    }
    // Lấy thông tin của từng bài báo
    public abstract ArticleDTO getArticleInformation(String source_url);

    // Lấy link của từng menu web
    public abstract List<String> getMenuWeb(String source_url);

    // 
    public abstract void setNewsOfEachMenuDependOnTime(String source_url, Timestamp newtime, Timestamp lasttime);

    // get article like
    public abstract int getArticleLike(int objectID);

    // facebook
    public FacebookDTO getContentOfFacebook(String source_url) throws MalformedURLException, IOException {
        String url = apiFBStart + source_url + apiFBEnd;

        FacebookDTO fb = new FacebookDTO();
        String json = jsoupConnectJson(url);
        
        JsonParser parser = new JsonParser();

        JsonElement element = parser.parse(json);
        JsonObject root = element.getAsJsonObject();
        JsonArray datas = root.getAsJsonArray("data");
        JsonObject data = datas.get(0).getAsJsonObject();

        Gson gson = new Gson();
        fb = gson.fromJson(data, FacebookDTO.class);
//        try {
//            Thread.sleep(7000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(ArticleObject.class.getName()).log(Level.SEVERE, null, ex);
//        }
        //System.out.println("Parse fb successful");
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

    protected void insertDatabase(ArticleDTO art) throws SQLException {
        ArticleBUS artBUS =  ArticleBUS.getInstance(username, password);
        ParentCmtBUS parBUS =  ParentCmtBUS.getInstance(username, password);
        SubCmtBUS subBUS =  SubCmtBUS.getInstance(username, password);

        // insert art to database => art have idtablearticle
        // nếu đã tồn tại thì return
        count++;
        checkAmountArticleToSleep(count);
        if (!artBUS.insert(art)) {
            return;
        }
        temptPar = parCmt.getContentParentComment(art, parentIDHasSub);
        // nếu temptPar != null => insert vào database
        // có parentcmt thì mới có subcmt
        if (temptPar != null) {
            // insert that bai => return => tranh loi phia sau
            if (!parBUS.insert(temptPar)) {
                temptPar = null;
                parentIDHasSub.clear();
                return;
            }

            if (parentIDHasSub != null) {
                temptSub = subCmt.getContentSubComment(art, parentIDHasSub);
                if (temptSub != null) {
                    subBUS.insert(temptSub);
                }
            }
        }
        temptPar = null;
        temptSub = null;
        parentIDHasSub.clear();
          try {
            Thread.sleep(article1);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectUrl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected void updateDatabase(ArticleDTO art) throws SQLException {
        ArticleBUS artBUS =  ArticleBUS.getInstance(username, password);
        ParentCmtBUS parBUS =  ParentCmtBUS.getInstance(username, password);
        SubCmtBUS subBUS =  SubCmtBUS.getInstance(username, password);

        count++;
        checkAmountArticleToSleep(count);
        // update art to database 
        if (!artBUS.update(art)) {
            return;
        }
        temptPar = parCmt.getContentParentComment(art, parentIDHasSub);
        // nếu temptPar != null => insert vào database. Có parentcmt thì mới có subcmt
        if (temptPar != null) {
            if (!parBUS.update(temptPar)) {
                temptPar = null;
                parentIDHasSub.clear();
                return;
            }
            temptSub = subCmt.getContentSubComment(art, parentIDHasSub);
            if (temptSub != null) {
                subBUS.update(temptSub);
            }
        }
        temptPar = null;
        temptSub = null;
        parentIDHasSub.clear();
          try {
            Thread.sleep(article1);
        } catch (InterruptedException ex) {
            Logger.getLogger(ConnectUrl.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void checkAmountArticleToSleep(int count) {
        if (count % 200 == 0) {
            //count = 0;
            if (count % 1000 == 0) {
                count = 0;
                //<editor-fold defaultstate="collapsed" desc="ngu 20 phut">
                System.out.println("\nLay du 1000 bai, Thread ngu 20 minutes");
                try {
                    Thread.sleep(article1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ArticleObject.class.getName()).log(Level.SEVERE, null, ex);
                }
//</editor-fold>
            } else {
                //<editor-fold defaultstate="collapsed" desc="ngu 10 phut">
                System.out.println("\nLay du 200 bai, Thread insert ngu 10 minutes");
                try {
                    Thread.sleep(article200);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ConnectUrl.class.getName()).log(Level.SEVERE, null, ex);
                }
//</editor-fold>
            }
        }
    }

    protected void parseXML(){
        SAXBuilder builder = new SAXBuilder();
        File xmlFile = new File("config.xml");

        try {

            org.jdom.Document document = (org.jdom.Document) builder.build(xmlFile);
            Element node = document.getRootElement();

            article1 = Integer.parseInt(node.getChildText("article1"));
            article200 = Integer.parseInt(node.getChildText("article200"));
            article1000 = Integer.parseInt(node.getChildText("article1000"));

        } catch (IOException io) {
            System.out.println(io.getMessage());
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
        }
    }
}
