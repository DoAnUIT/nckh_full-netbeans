package WebLayer;

import java.sql.Timestamp;
import java.util.List;

import DTO.ArticleDTO;
import DTO.FacebookDTO;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public abstract class ArticleObject {

    protected String apiFBStart = "https://graph.facebook.com/fql?q=SELECT%20share_count,%20like_count,%20comment_count%20FROM%20link_stat%20where%20url=%27";
    protected String apiFBEnd = "%27";
    // 
    public abstract ArticleDTO getArticleInformation(String source_url);

    public abstract List<String> getMenuWeb(String source_url);
    // trong day se goi ham 
    public abstract List<ArticleDTO> getNewsOfEachMenuDependOnTime(String source_url, Timestamp newtime, Timestamp lasttime);
    
    // get article like
     public abstract int getArticleLike(int objectID);
     
    // facebook
    protected FacebookDTO getContentOfFacebook(String source_url) throws MalformedURLException, IOException {
        String  url = apiFBStart + source_url + apiFBEnd;

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
//        if(cal1.get(Calendar.MONTH) > cal2.get(Calendar.MONTH))
//            return true;
//        if(cal1.get(Calendar.MONTH) < cal2.get(Calendar.MONTH))
//            return false;
//        if (cal1.get(Calendar.DAY_OF_MONTH) - cal2.get(Calendar.DAY_OF_MONTH) >= 0) {
//            return true;
//        }
        if(calArt.get(Calendar.DAY_OF_YEAR) - calLast.get((Calendar.DAY_OF_YEAR)) >= 0)
            return true;
        return false;
    }
    
    // check if date time valid, lasstime < art.getDate < newtime
    protected boolean isTimeValid(ArticleDTO art, Timestamp newtime, Timestamp lasttime){
        if(art.getArticleDate().getTime() >= lasttime.getTime()
                                && art.getArticleDate().getTime() < newtime.getTime())
            return true;
        return false;
    }
    
    // connect jsou
    protected Document JsoupConnect(String source_url){
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
        return doc;
    }
    
    

}
