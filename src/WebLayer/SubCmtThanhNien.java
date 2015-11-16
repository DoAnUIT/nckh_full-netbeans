package WebLayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import DTO.ArticleDTO;
import DTO.SubCmtDTO;

public class SubCmtThanhNien extends ConnectUrl implements ISubCmt {

    private String source_url = "http://thanhnien.vn/ajax/comment.aspx?&order=like&cid=";

    @Override
    public List<SubCmtDTO> getContentSubComment(ArticleDTO article, List<Integer> parentcomment) {
        // TODO Auto-generated method stub
        List<SubCmtDTO> arraySub = new ArrayList<SubCmtDTO>();
        SubCmtDTO temptSubComment = null;

        Document doc = jsoupConnect(article.getUrl());

        Element meta = doc.select("input[id^=posturl]").first();
        String tempt = meta.attr("value");
        String url = source_url;
        url += tempt + "&page=";
        int count = 1;
        int parentid = 0;
        while (doc.text().length() > 1) {
            // url += count;
            doc = jsoupConnect(String.format(url + "%d", count));

            // select comment item parent
            Elements elements = doc.select(".Comments-item-parent");
            for (int i = 0; i < elements.size(); i++) {
                String idparent = "comments_item_";
                Elements elesubcomment = null;

                meta = null;
                // create css id of parent comment has sub
                // search to see if in this page has parentcomment value
                for (int ii = 0; ii < parentcomment.size(); ii++) {
                    meta = elements.select("#" + String.format(idparent + "%d", parentcomment.get(ii))).first();
                    if (meta != null) {
                        parentid = parentcomment.get(ii);
                        parentcomment.remove(ii);
                        ii = 0;
                        break;
                    }
                }
                if (meta == null) {
                    break;
                }
                elesubcomment = meta.select(".reply");

                for (int j = 0; j < elesubcomment.size(); j++) {
                    meta = elesubcomment.get(j);

                    temptSubComment = new SubCmtDTO();
                    // set IDTableArticle
                    temptSubComment.setIDTableArticle(article.getIDTableArticle());
                    // set parent id
                    temptSubComment.setParentID(parentid);

                    // get id
                    tempt = meta.attr("id");
                    tempt = tempt.substring(tempt.lastIndexOf('_') + 1);
                    temptSubComment.setChildID(Integer.parseInt(tempt));

                    // get like
                    tempt = meta.select(".likebtn").first().text();
                    tempt = tempt.replaceAll("[^0-9]", "");
                    temptSubComment.setCmtLike(Integer.parseInt(tempt));

                    // get content of comment
                    tempt = meta.select(".Comments-item-Content").first().text();
                    temptSubComment.setContent(tempt);
                    // add list SubParentDTO
                    arraySub.add(temptSubComment);
                }

            }
            count++;
        }
        if (arraySub.isEmpty()) {
            return null;
        } else {
            return arraySub;
        }
    }

}
