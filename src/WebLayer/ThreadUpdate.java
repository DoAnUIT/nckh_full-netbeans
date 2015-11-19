package WebLayer;

import BusinessLayer.UpdateTimeBUS;
import DTO.ArticleDTO;
import DTO.FacebookDTO;
import DataAccessLayer.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadUpdate {

    Thread updateNewsThread;
    List<ArticleDTO> listArtToUpdate;
    ArticleObject _art = null;
    ArticleObject artVnExpress = null;
    ArticleObject artThanhNien = null;
    ArticleObject artTuoiTre = null;

    public ThreadUpdate(String _user, String _pass, int _idType, ArticleObject artThanhNien, ArticleObject artTuoiTre,
            ArticleObject artVnexpress) {
        ArticleDAO tempArt = new ArticleDAO(_user, _pass);
        listArtToUpdate = new ArrayList<ArticleDTO>();
        UpdateTimeBUS udBUS = new UpdateTimeBUS(_user, _pass);

//        this.artTuoiTre = new ArticleTuoiTre(_user, _pass);
//        this.artThanhNien = new ArticleThanhNien(_user, _pass);
//        this.artVnExpress = new ArticleVnexpress(_user, _pass);
        this.artTuoiTre = artTuoiTre;
        this.artThanhNien = artThanhNien;
        this.artVnExpress = artVnexpress;

        updateNewsThread = new Thread(new Runnable() {

            public void run() {
                while (true) {
                    listArtToUpdate = tempArt.getArticleDTOByUpdateType(_idType);
                    if (listArtToUpdate == null) {
                        continue;
                    }
                    for (int i = 0; i < listArtToUpdate.size(); i++) {
                        System.out.println(listArtToUpdate.get(i).getUrl());
                        updateArticle(listArtToUpdate.get(i));
                    }

                    // khi da lay xong tung loai, set bien dem so bai lay duoc count ve lai 0
                    System.out.println("Thread " + _idType + " TN :" + artThanhNien.getCount() + " VNE : " + artVnExpress.getCount()
                            + "  TT : " + artTuoiTre.getCount());
                    _art = null;
                    Timestamp ttTimestamp = udBUS.GetTimeUpdateByID(_idType).getQuantumTime();
                    long timeSleep = ttTimestamp.getTime() + TimeZone.getDefault().getRawOffset();
                    try {
                        System.out.println("Thread update bao loai " + _idType + " ngu ");
                        Thread.sleep(timeSleep);
                        System.out.println("Thread update bao loai " + _idType + " day ");

                    } catch (InterruptedException ex) {
                        Logger.getLogger(ThreadUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        });

        updateNewsThread.start();
        updateNewsThread.setName("Update loai " + _idType);
    }

    public void updateArticle(ArticleDTO _artUp) {
        //ArticleDTO result = new ArticleDTO();
        int _idMaga = _artUp.getIDTableMagazine();

        //<editor-fold defaultstate="collapsed" desc="switch de chon _art phu hop">
        switch (_idMaga) {
            case 1: {
                //_art = new ArticleThanhNien(_user, _pass);
                _art = artThanhNien;
            }
            break;
            case 2: {
                // _art = new ArticleVnexpress(_user, _pass);
                _art = artVnExpress;
            }
            break;
            case 3: {
                //_art = new ArticleTuoiTre(_user, _pass);
                _art = artTuoiTre;
            }
            break;
        }
//</editor-fold>

        synchronized (_art) {
            FacebookDTO fbData = null;
            try {
                fbData = _art.getContentOfFacebook(_artUp.getUrl());
            } catch (IOException ex) {
                System.out.println(ex.toString());
            }
            _artUp.setArticleLike(_art.getArticleLike(_artUp.getObjectID()));
            _artUp.facebook = fbData;

            try {
                _art.updateDatabase(_artUp);
            } catch (SQLException ex) {
                System.out.println(ex.toString());
                System.out.println("can not update article");
            }
        }
    }
}
