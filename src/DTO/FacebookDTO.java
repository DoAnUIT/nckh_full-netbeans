package DTO;

import com.google.gson.annotations.SerializedName;

public class FacebookDTO {

    @SerializedName("like_count")
    private int FBLike = 0;
    @SerializedName("comment_count")
    private int FBCmt = 0;
    @SerializedName("share_count")
    private int FBShare = 0;

    // fb like
    public int getFBLike() {
        return this.FBLike;
    }

    public void setFBLike(int a) {
        this.FBLike = a;
    }

    // fb comment
    public int getFBCmt() {
        return this.FBCmt;
    }

    public void setFBCmt(int a) {
        this.FBCmt = a;
    }

    // fb share
    public int getFBShare() {
        return this.FBShare;
    }

    public void setFBShare(int a) {
        this.FBShare = a;
    }
}
