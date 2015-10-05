package WebLayer;

import java.util.List;

import DTO.ArticleDTO;
import DTO.ParentCmtDTO;

public interface IParentCmt {

        // get parent comment and return the parent comment had sub comment 

    public List<ParentCmtDTO> getContentParentComment(ArticleDTO article, List<Integer> parentIDHasSub);
}
