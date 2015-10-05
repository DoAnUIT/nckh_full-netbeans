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

public class ParentCmtVnexpress implements IParentCmt {

    // limit = 0 => get all comments
    private String source_url = "http://usi.saas.vnexpress.net/index/get?&offset=0&limit=0";

    // function getContentVNEParentComment
    public List<ParentCmtDTO> getContentParentComment(ArticleDTO article, List<Integer> parentIDHasSub) {
        List<ParentCmtDTO> lpar = new ArrayList<ParentCmtDTO>();

        // parse url to get objecttype
        int objecttype = 0;
        if (article.getUrl().matches("(.*)/tin-tuc/(.*)")) {
            objecttype = 1;
        }
        if (article.getUrl().matches("(.*)/photo/(.*)")) {
            objecttype = 3;
        }
        if (article.getUrl().matches("(.*)/infographic/(.*)")) {
            objecttype = 4;
        }
        // create url to get comment
        String url = source_url + "&objectid=" + article.getObjectID() + "&objecttype=" + objecttype + "&siteid=1" + "&categoryid="
                + article.getIDTableCategory();

        // Parse json
        String json = null;
        try {
            json = IOUtils.toString(new URL(url).openStream(), "UTF-8");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();
        // JsonArray datas = jsonObject.getAsJsonObject("data");
        JsonObject data = jsonObject.getAsJsonObject("data");
        //article.setTotalComment(data.get("totalitem").getAsInt());

        JsonArray datas = data.getAsJsonArray("items");
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

        if (lpar.isEmpty()) {
            return null;
        } else {
            return lpar;
        }
    }
}
