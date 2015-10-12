/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebLayer;

import DTO.ArticleDTO;
import DTO.SubCmtDTO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Minh Nhat
 */
public class SubCmtTuoiTre extends ConnectUrl implements ISubCmt {

    String source_url = "http://cm.tuoitre.vn/comment/createiframe?app_id=6&offset=0&layout=tto&object_id=";

    @Override
    public List<SubCmtDTO> getContentSubComment(ArticleDTO article, List<Integer> parentcomment) {
        // TODO Auto-generated method stub
        List<SubCmtDTO> lSub = new ArrayList<SubCmtDTO>();

        String url = source_url + article.getObjectID();
        Document doc = jsoupConnect(url);
        if(doc == null)
            return null;        

        // select comment item parent
        Elements datas = doc.select(".lst-comment > ul > li"); // contain parent and sub
        Element data = null;
        Elements datasubs = null;
        String cssparentid = "like_comment_id-";
        int parentid = 0;
        for (int i = 0; i < datas.size(); i++) {

            for (int ii = 0; ii < parentcomment.size(); ii++) {
                data = datas.get(i).select(String.format("#" + cssparentid + "%d", parentcomment.get(ii))).first();
                if (data != null) {
                    parentid = parentcomment.get(ii);
                    //data = data.select("> ul").first();
                    parentcomment.remove(ii);
                    ii = 0;
                    break;
                }
            }

            if (data == null) {
                continue;
            }
            // get content of child comment
            datasubs = datas.get(i).select("> ul > li");//
            for (int j = 0; j < datasubs.size(); j++) {
                data = datasubs.get(j).select("> dl > dd").first();
                SubCmtDTO temptSubComment = new SubCmtDTO();
                temptSubComment.setIDTableArticle(article.getIDTableArticle());
                temptSubComment.setParentID(parentid);
                temptSubComment.setCmtLike(Integer.parseInt(data.select("span.like_number").text()));
                temptSubComment.setChildID(Integer.parseInt(data.select(" .like_btn").attr("id").replaceAll("[^0-9]", "").trim()));
                temptSubComment.setContent(data.select("p.cm-content").text());
            // If parent comment has child comment => add parentID to List
                // parentIDHasSub
                // if (datas.get(i).select("> ul").toString().length() > 2) {

                // add parent comment to List<ParentComentDTO>
                lSub.add(temptSubComment);
            }
        }
        if (lSub.isEmpty()) {
            return null;
        } else {
            return lSub;
        }
    }
}
