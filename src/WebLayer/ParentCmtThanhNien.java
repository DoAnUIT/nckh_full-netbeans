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

public class ParentCmtThanhNien implements IParentCmt {

    private String source_url = "http://www.thanhnien.com.vn/ajax/comment.aspx?&order=like&cid=";

    // get parent comment and return the parent comment had sub comment 
    @Override
    public List<ParentCmtDTO> getContentParentComment(ArticleDTO article, List<Integer> parentIDHasSub) {
        // TODO Auto-generated method stub
        List<ParentCmtDTO> lpar = new ArrayList<ParentCmtDTO>();
        ParentCmtDTO parent = null;
        Document doc = null;

        try {
            doc = Jsoup.connect(article.getUrl()).timeout(5000).followRedirects(true)
                    .userAgent(
                            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36")
                    .get();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Element meta = doc.select("#posturl").first();
        String tempt = meta.attr("value");
        String url = source_url;
        url += tempt + "&page=";
        int count = 1;
        Elements elements = null;
        while (doc.text().length() > 1) {
            // url += count;
            try {
                doc = Jsoup.connect(String.format(url + "%d", count)).timeout(5000).followRedirects(true)
                        .userAgent(
                                "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.134 Safari/537.36")
                        .get();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
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
                if (meta.select(".Comments-item-replies").first().text().length() > 2) {
                    parentIDHasSub.add(parent.getParentID());
                }

                // get parent comment like
                tempt = meta.select(".comments-likes-number").first().text();
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
