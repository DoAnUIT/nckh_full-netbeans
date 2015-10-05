/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package WebLayer;

import DTO.ArticleDTO;
import DTO.ParentCmtDTO;
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
public class ParentCmtTuoiTre implements IParentCmt {

    String source_url = "http://cm.tuoitre.vn/comment/createiframe?app_id=6&offset=0&layout=tto&object_id=";

    @Override
    public List<ParentCmtDTO> getContentParentComment(ArticleDTO article, List<Integer> parentIDHasSub) {
        List<ParentCmtDTO> lpar = new ArrayList<ParentCmtDTO>();
        Document doc = null;

        String url = source_url + article.getObjectID();
        //<editor-fold defaultstate="collapsed" desc="jsoup connect">
        try {
            doc = Jsoup.connect(url).timeout(5000).followRedirects(true)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36")
                    .get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //</editor-fold>

        Elements datas = doc.select(".lst-comment > ul > li"); // contain parent and sub
        Element data = null;
        for (int i = 0; i < datas.size(); i++) {
            // get content of parent comment
            data = datas.get(i).select("> dl > dd").first();
            ParentCmtDTO temptParentCmt = new ParentCmtDTO();
            temptParentCmt.setIDTableArticle(article.getIDTableArticle());
            temptParentCmt.setCmtLike(Integer.parseInt(data.select("span.like_number").text()));
            temptParentCmt.setParentID(Integer.parseInt(data.select(".like_comment_div > a.like_btn").attr("id").replaceAll("[^0-9]", "").trim()));
            temptParentCmt.setContent(data.select("p.cm-content").text());
            // If parent comment has child comment => add parentID to List
            // parentIDHasSub
            // if (datas.get(i).select("> ul").toString().length() > 2) {
            if (datas.get(i).select("> ul").text().toString().length() > 2) {
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
