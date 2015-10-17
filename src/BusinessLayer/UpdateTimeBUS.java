package BusinessLayer;

import DTO.UpdateTimeDTO;
import java.util.*;
import DataAccessLayer.*;

public class UpdateTimeBUS {

    UpdateTimeDAO UpDAO;

    public UpdateTimeBUS(String _user, String _pass) {
        UpDAO = new UpdateTimeDAO(_user, _pass);
    }

    public List<Integer> GetListTypeUpdate() {
        return UpDAO.GetTypeUpdate();
    }

    public UpdateTimeDTO GetTimeUpdateByID(int _idType) {
        return UpDAO.GetTimeUpdateByID(_idType);
    }

    public int GetMaxIdTableUpdateTime(){
        return UpDAO.GetMaxIdTableUpdateTime();
    }

}
