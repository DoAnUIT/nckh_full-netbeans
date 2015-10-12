package WebLayer;

import DTO.ArticleDTO;
import DTO.FacebookDTO;
import DataAccessLayer.*;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ThreadUpdate {
    Thread UpdateNewsThread;
    List<ArticleDTO> ListArtToUpdate;
    
    public ThreadUpdate(int _idType, String _user, String _pass)
    {
        ArticleDAO tempArt = new ArticleDAO(_user, _pass);
        ListArtToUpdate = new ArrayList<ArticleDTO>();
        UpdateTimeDAO udDAO = new UpdateTimeDAO(_user, _pass);
        UpdateNewsThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    ListArtToUpdate = tempArt.getArticleDTOByUpdateType(_idType);
                    for (int i = 0; i < ListArtToUpdate.size(); i++) {
                        
                        ArticleDTO tempDTO = GetArticle(_user, _pass, ListArtToUpdate.get(i));
                        System.out.println(tempDTO.getTitle());
                        tempDTO.setCountOfUpdate(tempDTO.getCountOfUpdate() + 1);
                        if(tempDTO.getCountOfUpdate() > udDAO.GetTimeUpdateByID(_idType).getMaxRepeat())
                        {
                            tempDTO.setCountOfUpdate(0);
                            if (tempDTO.getIDTableUpdateTime() + 1 < 6) {
                                tempDTO.setIDTableUpdateTime(tempDTO.getIDTableUpdateTime() + 1);
                            }
                        }
                        tempArt.updateArticle(tempDTO);
                    }
                    Timestamp ttTimestamp = udDAO.GetTimeUpdateByID(_idType).getQuantumTime();
                    long timeSleep = (ttTimestamp.getHours() * 60 * 60 + ttTimestamp.getMinutes() * 60  + ttTimestamp.getSeconds()) * 1000;
                    try {
                        Thread.sleep(timeSleep);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ThreadUpdate.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            
        });
        UpdateNewsThread.start();
    }
    
    
    public ArticleDTO GetArticle(String _user, String _pass, ArticleDTO _artUp)
    {
        ArticleDTO result = new ArticleDTO();
        int _idMaga = _artUp.getIDTableMagazine();
        ArticleObject _art = null;
        switch(_idMaga)
        {
            case 1:
            {
                _art = new ArticleThanhNien(_user, _pass);
            }
            break;
            case 2:
            {
                _art = new ArticleVnexpress(_user, _pass);
            }
            break;
            case 3:
            {
                _art = new ArticleTuoiTre(_user, _pass);
            }
            break;
        }
        
        int artLike = 0;
        FacebookDTO fbData = null;
        try {
            fbData = _art.getContentOfFacebook(result.getUrl());
        } catch (IOException ex) {
            System.out.println("Can not get fb");
        }
;
        artLike = _art.getArticleLike(result.getObjectID());
        result.setArticleLike(artLike);
        result.facebook = fbData;

        try {
            _art.updateDatabase(result);
        } catch (SQLException ex) {
            System.out.println("can not update article");
        }
        return result;
        
    }
}