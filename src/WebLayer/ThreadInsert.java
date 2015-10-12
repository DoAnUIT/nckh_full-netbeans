package WebLayer;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ThreadInsert {
    public Thread ThreadInsertByTypeNews;
    String UserName;
    String Password;
    String URLMagazine;
    Timestamp StartTimestamp;
    Timestamp EndTimestamp;
    
    public ThreadInsert(String _userString, String _passString, String _url, Timestamp _start, Timestamp _end)
    {
        URLMagazine = _url;
        UserName = _userString;
        Password = _passString;
        StartTimestamp = _start;
        EndTimestamp = _end;
        ThreadInsertByTypeNews = new Thread(new Runnable() {
            public void run() {
                InsertNewsByURL();
            }
        });
        ThreadInsertByTypeNews.start();
    }
    
    public synchronized void InsertNewsByURL()
    {
        try {
            System.out.println("Start insert news in " + URLMagazine);
            WebLayer wl = new WebLayer(UserName, Password);
            wl.insert(URLMagazine, EndTimestamp, StartTimestamp);
        } catch (Exception e) {
            System.out.println("Can not insert news in " + URLMagazine);
        }
    }
}
