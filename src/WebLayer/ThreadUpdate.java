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
    
    public ThreadUpdate(String _user, String _pass, int _idType)
    {
        ArticleDAO tempArt = new ArticleDAO(_user, _pass);
        ListArtToUpdate = new ArrayList<ArticleDTO>();
        UpdateTimeDAO udDAO = new UpdateTimeDAO(_user, _pass);
        UpdateNewsThread = new Thread(new Runnable() {
            public void run() {
                while (true) {
                    ListArtToUpdate = tempArt.getArticleDTOByUpdateType(_idType);
                    if (ListArtToUpdate == null) {
                        continue;
                    }
                    for (int i = 0; i < ListArtToUpdate.size(); i++) {
                        
                        ArticleDTO tempDTO = GetArticle(_user, _pass, ListArtToUpdate.get(i));
                        System.out.println(tempDTO.getTitle());
                        tempDTO.setCountOfUpdate(tempDTO.getCountOfUpdate() + 1);
                        if(tempDTO.getCountOfUpdate() > udDAO.GetTimeUpdateByID(_idType).getMaxRepeat())
                        {
                            tempDTO.setCountOfUpdate(0);
                            int maxID = udDAO.GetMaxIdTableUpdateTime();
                            if(maxID <= 0)
                            {
                                System.out.println("can not update number for " + tempDTO.getTitle());
                                continue;
                            }
                            if (tempDTO.getIDTableUpdateTime() + 1 < maxID) {
                                tempDTO.setIDTableUpdateTime(tempDTO.getIDTableUpdateTime() + 1);
                            }
                        }
                        tempArt.updateArticle(tempDTO);
                        System.out.println("Update succesful for " + tempDTO.getTitle());
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
        
        FacebookDTO fbData = null;
        try {
            fbData = _art.getContentOfFacebook(_artUp.getUrl());
        } catch (IOException ex) {
            System.out.println("Can not get fb");
        }
        result.setArticleLike(_art.getArticleLike(_artUp.getObjectID()));
        result.facebook = fbData;
        _artUp = result;
        try {
            _art.updateDatabase(result);
        } catch (SQLException ex) {
            System.out.println(ex.toString());
            System.out.println("can not update article");
        }
        return result;
        
    }
}