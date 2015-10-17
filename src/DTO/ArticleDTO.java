package DTO;

import java.sql.Timestamp;

public class ArticleDTO {

    private int IDTableArticle = 0; //set in BUS
    private int IDTableUpdateTime = 0; // need to update, set in BUS
    private int IDTableMagazine = 0;
    private int IDTableCategory = 0;
    private int countOfUpdate = 0; // need to update, set in BUS
    private Timestamp articleDate = null;
    private String title = null;
    private String urlPicture = null;
    private String url = null;
    private String description = null;
    private int objectID = 0;
    private int articleLike = 0;
    public FacebookDTO facebook = null; // need to update

    public ArticleDTO() {

    }

    // IDTableArticle This is auto_increment, but it increase in BUS layer
    public int getIDTableArticle() {
        return this.IDTableArticle;
    }

    public synchronized void setIDTableArticle(int a) {
        this.IDTableArticle = a;
    }

    // IDTableUpdateTime
    public int getIDTableUpdateTime() {
        return this.IDTableUpdateTime;
    }

    public void setIDTableUpdateTime(int a) {
        this.IDTableUpdateTime = a;
    }
    // IDTable magazine

    public int getIDTableMagazine() {
        return this.IDTableMagazine;
    }

    public void setIDTableMagazine(int a) {
        this.IDTableMagazine = a;
    }
    // IDTable category

    public int getIDTableCategory() {
        return this.IDTableCategory;
    }

    public void setIDTableCategory(int a) {
        this.IDTableCategory = a;
    }
    
    // count of update, if countofupdate = maxrepeated (in updatetime table) => next level update
    public int getCountOfUpdate(){
        return this.countOfUpdate;
    }
    
    public void setCountOfUpdate(int a){
        this.countOfUpdate = a;
    }

    // date

    public Timestamp getArticleDate() {
        return this.articleDate;
    }

    public void setArticleDate(Timestamp a) {
        this.articleDate = a;
    }

    // title
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String a) {
        this.title = a;
    }

    // url Picture
    public String geturlPicture() {
        return this.urlPicture;
    }

    public void setUrlPicture(String a) {
        this.urlPicture = a;
    }

    // url
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String a) {
        this.url = a;
    }

    // description
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String a) {
        this.description = a;
    }

    // object id
    public int getObjectID() {
        return this.objectID;
    }

    public void setObjectID(int a) {
        this.objectID = a;
    }

    // vne like
    public int getArticleLike() {
        return this.articleLike;
    }

    public void setArticleLike(int a) {
        this.articleLike = a;
    }

}
