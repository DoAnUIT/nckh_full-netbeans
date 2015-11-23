package WebLayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import DTO.ArticleDTO;
import DTO.ParentCmtDTO;

public class ParentCmtThanhNien extends ConnectUrl implements IParentCmt {

    private String source_url = "http://thanhnien.vn/ajax/comment.aspx?&order=like&cid=";

    // get parent comment and return the parent comment had sub comment 
    @Override
    public List<ParentCmtDTO> getContentParentComment(ArticleDTO article, List<Integer> parentIDHasSub) {
        // TODO Auto-generated method stub
        List<ParentCmtDTO> lpar = new ArrayList<ParentCmtDTO>();
        ParentCmtDTO parent = null;
        Document doc = null;

        doc = jsoupConnect(article.getUrl());

        Element meta = doc.select("input[id^=posturl]").first();
        if(meta == null)
            return null;
        String tempt = meta.attr("value");
        String url = source_url;
        url += tempt + "&page=";
        int count = 1;
        Elements elements = null;
        while (doc.text().length() > 1) {
            // url += count;
            doc = jsoupConnect(String.format(url + "%d", count));
            elements = doc.select(".Comments-item-parent");

            for (int i = 0; i < elements.size(); i++) {
                parent = new ParentCmtDTO();
                // get ID Table Article
                parent.setIDTableArticle(article.getIDTableArticle());

                meta = elements.get(i);

                // get parent comment id
                tempt = meta.attr("id");
                tempt = tempt.substring(tempt.lastIndexOf('_') + 1);
                parent.setParentID(Integer.parseInt(tempt));

                // check to see if parentId has Sub
                if (meta.select(".reply").first() != null) {
                    parentIDHasSub.add(parent.getParentID());
                }

                // get parent comment like
                tempt = meta.select(".likebtn").first().text();
                tempt = tempt.replaceAll("[^0-9]", "");
                parent.setCmtLike(Integer.parseInt(tempt));

                // get content of comment
                tempt = meta.select(".Comments-item-Content").first().text();
                parent.setContent(tempt);

                // add to list parentcommentDTO
                lpar.add(parent);
            }
            count++;
        }
        if (lpar.isEmpty()) {
            return null;
        } else {
            return lpar;
        }
    }
}
