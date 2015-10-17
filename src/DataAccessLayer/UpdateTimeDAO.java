package DataAccessLayer;

import DTO.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UpdateTimeDAO extends DataSource{
    CallableStatement Call;
    
    public UpdateTimeDAO(String _user, String _pass)
    {
        this.username = _user;
        this.password = _pass;
    }
    
    public List<Integer> GetTypeUpdate()
    {
        List<Integer> result = new ArrayList<Integer>();
        try {
            connection = DataSource.getInstance().getConnection();
            while (connection == null) {                
                connection = DataSource.getInstance().getConnection();
            }
            Call = connection.prepareCall("select * from updatetime");
            ResultSet rs = Call.executeQuery();
            while (rs.next()) {                
                result.add(rs.getInt("idtableupdatetime"));
            }
            return result;
        } catch (Exception e) {
            System.out.println("Can not get all type update time");
            System.out.println(e.toString());
        }
        finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                if (Call != null) {
                    Call.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }
    
    public int GetMaxIdTableUpdateTime()
    {
        try {
            connection = DataSource.getInstance().getConnection();
            while (connection == null) {                
                connection = DataSource.getInstance().getConnection();
            }
            Call = connection.prepareCall("select max(idtableupdatetime) from updatetime");
            ResultSet rs = Call.executeQuery();
            while (rs.next()) {                
                return rs.getInt("max(idtableupdatetime)");
            }
        } catch (Exception e) {
            System.out.println("Can not get max id update time");
            System.out.println(e.toString());
        }
        finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                if (Call != null) {
                    Call.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return 0;
    }
    
    public UpdateTimeDTO GetTimeUpdateByID(int _idType)
    {
        UpdateTimeDTO utdto = new UpdateTimeDTO();
        try {
            connection = DataSource.getInstance().getConnection();
            while (connection == null) {                
                connection = DataSource.getInstance().getConnection();
            }
            Call = connection.prepareCall("{Call GetTableUpdateTime(?)}");
            Call.setInt(1, _idType);
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
            System.out.println(e.toString());
        }
        finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            try {
                if (Call != null) {
                    Call.close();
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;
    }
}
