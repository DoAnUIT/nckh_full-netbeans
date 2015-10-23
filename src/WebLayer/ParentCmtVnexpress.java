package WebLayer;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import DTO.ArticleDTO;
import DTO.ParentCmtDTO;

public class ParentCmtVnexpress extends ConnectUrl implements IParentCmt {

    // limit = 0 => get all comments
    private String source_url = "http://usi.saas.vnexpress.net/index/get?&offset=0&limit=0";

    // function getContentVNEParentComment
    public List<ParentCmtDTO> getContentParentComment(ArticleDTO article, List<Integer> parentIDHasSub) {
        List<ParentCmtDTO> lpar = new ArrayList<ParentCmtDTO>();

        // Parse json
        String json = null;
        // parse url to get objecttype
        int objecttype = 1;
       while(objecttype < 6){
        // create url to get comment
        String url = source_url + "&objectid=" + article.getObjectID() + "&objecttype=" + objecttype + "&siteid=1" + "&categoryid="
                + article.getIDTableCategory();

        json = jsoupConnectJson(url);
        
       
        // parse json
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();
        JsonObject data = jsonObject.getAsJsonObject("data");
        JsonArray datas = data.getAsJsonArray("items");
        
        if(datas.size() == 0){
            objecttype++;
            continue;
        }
        for (int i = 0; i < datas.size(); i++) {
            data = datas.get(i).getAsJsonObject();

            // get content of parent comment
            ParentCmtDTO temptParentCmt = new ParentCmtDTO();
            temptParentCmt.setIDTableArticle(article.getIDTableArticle());
            temptParentCmt.setCmtLike(data.get("userlike").getAsInt());
            temptParentCmt.setParentID(data.get("comment_id").getAsInt());
            temptParentCmt.setContent(data.get("content").getAsString());
            // If parent comment has child comment => add parentID to List
            // parentIDHasSub
            if (data.getAsJsonObject("replys").toString().length() > 2) {
                parentIDHasSub.add(temptParentCmt.getParentID());
            }

            // add parent comment to List<ParentComentDTO>
            lpar.add(temptParentCmt);
        }
        break;
       }

        if (lpar.isEmpty()) {
            return null;
        } else {
            return lpar;
        }
    }
}
