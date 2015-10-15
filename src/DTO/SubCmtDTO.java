package DTO;

public class SubCmtDTO {

    private int IDTableSubCmt = 0;
    private int IDTableParentCmt = 0;
    private int childID  = 0;
    private int cmtLike  = 0;
    private String content = null;
 
    // add to variables to get IDTableParentCmt. these are parentID and IDTableArticle
    private int parentID = 0;
    private int IDTableArticle = 0;
    // IDTableSubCmt
    public int getIDTableSubCmt() {
        return this.IDTableSubCmt;
    }

    public synchronized void setIDTableSubCmt(int a) {
        this.IDTableSubCmt = a;
    }

    // IDTableParentCmt
    public int getIDTableParentCmt() {
        return this.IDTableParentCmt;
    }

    public void setIDTableParentCmt(int a) {
        this.IDTableParentCmt = a;
    }

    // Child ID
    public void setChildID(int ID) {
        if (ID >= 0) {
            this.childID = ID;
        }
    }

    public int getChildID() {
        return this.childID;
    }

    // like of child id
    public void setCmtLike(int like) {
        if (like >= 0) {
            this.cmtLike = like;
        }
    }

    public int getCmtLike() {
        return this.cmtLike;
    }

    // Content of comment
    public void setContent(String a) {
        this.content = a;
    }

    public String getContent() {
        return this.content;
    }
    
    // parent cmt
    public int getParentID(){
        return this.parentID;
    }
    
    public void setParentID(int a){
        this.parentID = a;
    }
    
    // IDTableArticle
    public int getIDTableArticle(){
        return this.IDTableArticle;
    }
    
    public void setIDTableArticle(int a){
        this.IDTableArticle = a;
    }
}
