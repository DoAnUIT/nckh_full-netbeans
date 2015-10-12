package WebLayer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import DTO.ArticleDTO;
import DTO.SubCmtDTO;
import org.jsoup.Jsoup;

public class SubCmtVnexpress extends ConnectUrl implements ISubCmt {

    // url get subcomment of vnexpress
    private String source_url = "http://usi.saas.vnexpress.net/index/getreplay?&limit=0&offset=0";
	// &siteid=1000000&objectid=3259146&objecttype=1&id=12819274";

    // function get content of subcomment
    public List<SubCmtDTO> getContentSubComment(ArticleDTO article, List<Integer> parentcomment) {
        List<SubCmtDTO> object = new ArrayList<SubCmtDTO>();
        SubCmtDTO temtSubComment = null;
       
         // parse article url to get objecttype
//        int objecttype = 0;
//        if(article.getUrl().matches("(.*)/tin-tuc/(.*)"))
//            objecttype = 1;
//        if(article.getUrl().matches("(.*)/photo/(.*)"))
//            objecttype = 3;
//        if(article.getUrl().matches("(.*)/infographic/(.*)"))
//            objecttype = 4;
        // create url
        
        String url = source_url+  "&id=";
        for (int i = 0; i < parentcomment.size(); i++) {

            String json = jsoupConnectJson(String.format(url + "%d", parentcomment.get(i)));
            if(json == null)
                return null;
            

            // parse json
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(json);
            JsonObject root = element.getAsJsonObject();

            JsonObject data = root.getAsJsonObject("data");
            JsonArray datas = data.getAsJsonArray("items");
            for (int j = 0; j < datas.size(); j++) {
                data = datas.get(j).getAsJsonObject();

                // fill data to SubCommentDTO object
                temtSubComment = new SubCmtDTO();
                temtSubComment.setIDTableArticle(article.getIDTableArticle());
                temtSubComment.setParentID(data.get("parent_id").getAsInt());
                temtSubComment.setChildID(data.get("comment_id").getAsInt());
                temtSubComment.setCmtLike(data.get("userlike").getAsInt());
                temtSubComment.setContent(data.get("content").getAsString());

                // add to list
                object.add(temtSubComment);
            }
        }

        if(object.isEmpty())
            return null;
        else 
        return object;
    }
}
