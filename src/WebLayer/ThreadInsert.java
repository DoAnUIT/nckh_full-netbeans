package WebLayer;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ThreadInsert {
    public Thread ThreadInsertByTypeNews;
    String UserName;
    String Password;
    String URLMagazine;
    Timestamp newtime;
    Timestamp lasttime;
    
    public ThreadInsert(String _userString, String _passString, String _url, Timestamp newtime, Timestamp lasttime)
    {
        this.URLMagazine = _url;
        this.UserName = _userString;
        this.Password = _passString;
        this.newtime = newtime;
        this.lasttime = lasttime;
        ThreadInsertByTypeNews = new Thread(new Runnable() {
            public void run() {
                InsertNewsByURL();
            }
        });
        ThreadInsertByTypeNews.start();
    }
    
    public void InsertNewsByURL()
    {
        try {//em set cái request o file nao
            //System.out.println("Start insert news in " + URLMagazine);
            WebLayer wl = new WebLayer(UserName, Password);
            wl.insert(URLMagazine, newtime ,lasttime);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Can not insert news in " + URLMagazine);
        }
    }
}
