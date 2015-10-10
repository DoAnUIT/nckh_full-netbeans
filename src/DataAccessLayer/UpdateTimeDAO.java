package DataAccessLayer;

import DTO.*;
import java.sql.*;

public class UpdateTimeDAO extends DataSource{
    CallableStatement Call;
    
    public UpdateTimeDAO(String _user, String _pass)
    {
        this.username = _user;
        this.password = _pass;
    }
    
    public UpdateTimeDTO GetTimeUpdateByID(int _idType)
    {
        UpdateTimeDTO utdto = new UpdateTimeDTO();
        try {
            connection = DataSource.getInstance().getConnection();
            while (connection == null) {                
                connection = DataSource.getInstance().getConnection();
            }
            Call = connection.prepareCall("{call GetTableUpdateTime(?)}");
            Call.setInt("_IDTableUpdateTime", _idType);
            ResultSet rs = Call.executeQuery();
            if (rs == null) {
                System.out.println("Query return empty result with id = " + _idType);
                return null;
            }
            rs.next();
            utdto.setIDUpdateTime(_idType);
            utdto.setMaxRepeat(rs.getInt("MaxRepeated"));
            utdto.setQuantumTime(rs.getTimestamp("QuantumTime"));
            return utdto;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
