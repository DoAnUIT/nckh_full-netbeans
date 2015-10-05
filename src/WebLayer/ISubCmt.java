package WebLayer;

import java.util.List;

import DTO.ArticleDTO;
import DTO.SubCmtDTO;

public interface ISubCmt {

    /*
    argument : 
        article => pass article that we want to get sub comment
        parentcomment => pass list id of parent comment that had sub comment
    */
    public List<SubCmtDTO> getContentSubComment(ArticleDTO article, List<Integer> parentcomment);
}
