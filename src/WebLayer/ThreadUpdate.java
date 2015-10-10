package WebLayer;

import DTO.ArticleDTO;
import DataAccessLayer.*;
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
                        //then dua zo ham la then lay moi de update
                        
                        ArticleDTO tempDTO = tempArt.GetArticle(ListArtToUpdate.get(i).getIDTableArticle());
                        System.out.println(tempDTO.getTitle());
                        //tang bien count update len 1
                        tempDTO.setCountOfUpdate(tempDTO.getCountOfUpdate() + 1);
                        //neu bien count update lon hon MaxRepeat thi reset bien dem va tang loai update len 1
                        if(tempDTO.getCountOfUpdate() > udDAO.GetTimeUpdateByID(_idType).getMaxRepeat())
                        {
                            tempDTO.setCountOfUpdate(0);
                            if (tempDTO.getIDTableUpdateTime() + 1 < 6) {
                                //id table
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
}



//class ArticleThread
//{
//    ArticleDTO m_ArticleDTO;
//    public Thread m_ThreadArticle;
//    
//    public ArticleThread()
//    {
//        m_ArticleDTO = new ArticleDTO();
//    }
//    
//    public void UpdateArticle(String _user, String _pass, ArticleDAO _artDAO, ArticleDTO _art, double timeRepeat, int numberMaxRepeat)
//    {
//        m_ArticleDTO = _art;
//        m_ThreadArticle = new Thread(new Runnable()
//        {
//           public void run()
//           {
//               while(true)
//               {
//                Date date = new Date();
//                UpdateTime upt = new UpdateTime();
//                int maxIDUpdateTable = upt.GetMaxIdTableUpdateTime(_user, _pass);
//                //m_ArticleDTO = _artDAO.GetArticle(_user, _pass, listArticle.get(i).getIDTableArticle());
//                double secondsNow = date.getTime() / 60000;
//                double secondsNews = m_ArticleDTO.getArticleDate().getTime() / 60000;
//                double maxTimeSaveNews = UpdateTime.MAX_TIME_SAVE * 365 * 24 * 60;
//                if (secondsNow - secondsNews >= maxTimeSaveNews) {
//                    _artDAO.deleteArticle(_user, _pass, m_ArticleDTO.getIDTableArticle());
//                    return;
//                }
//                if (secondsNow - secondsNews - m_ArticleDTO.getCountOfUpdate() * timeRepeat >= timeRepeat ) 
//                {
//                    if (m_ArticleDTO.getCountOfUpdate() < numberMaxRepeat) 
//                    {
//                        m_ArticleDTO.setCountOfUpdate(m_ArticleDTO.getCountOfUpdate()+ 1);//set so lan update tang len 1
//                    }
//                    else if (m_ArticleDTO.getCountOfUpdate() >= numberMaxRepeat) 
//                    {
//                        if (m_ArticleDTO.getIDTableUpdateTime() < maxIDUpdateTable) {
//                            m_ArticleDTO.setIDTableUpdateTime(m_ArticleDTO.getIDTableUpdateTime() + 1);
//                            m_ArticleDTO.setCountOfUpdate(0);
//                        }
//                        else
//                        {
//                            return;
//                        }
//                    }
//                    System.out.println("update bai bao " + m_ArticleDTO.getTitle());
//                    _artDAO.updateArticle(_user, _pass, m_ArticleDTO);
//                }
//                else{
//                    try {
//                        long timeSleep = (long) ((secondsNow - secondsNews - (m_ArticleDTO.getCountOfUpdate() + 1) * timeRepeat ) * 1000);
//                        System.out.println(timeSleep);
//                        m_ThreadArticle.sleep(timeSleep);
//                    } catch (Exception e) {
//                        return;
//                    }
//                }
//               }
//           }
//        });
//        m_ThreadArticle.start();
//    }
//}