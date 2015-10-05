package DTO;

import com.google.gson.annotations.SerializedName;

public class ParentCmtDTO {

    private int IDTableParentCmt = 0;
    private int IDTableArticle = 0;
    @SerializedName("comment_id")
    private int parentID = 0;
    @SerializedName("userlike")
    private int cmtLike = 0;
    private String content = null;

    // IDTableParentCmt
    public int getIDTableParentCmt(){
        return this.IDTableParentCmt;
    }
    
    public void setIDTableParentCmt(int a){
        this.IDTableParentCmt = a;
    }
    
    // IDTableArticle
    public int getIDTableArticle() {
        return this.IDTableArticle;
    }

    public void setIDTableArticle(int a) {
        this.IDTableArticle = a;
    }

    // parentID
    public int getParentID() {
        return this.parentID;
    }

    public void setParentID(int a) {
        this.parentID = a;
    }

    // like
    public int getCmtLike() {
        return this.cmtLike;
    }

    public void setCmtLike(int a) {
        this.cmtLike = a;
    }

    // content
    public String getContent() {
        return this.content;
    }

    public void setContent(String a) {
        this.content = a;
    }
}
