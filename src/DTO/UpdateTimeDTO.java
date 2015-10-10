package DTO;

import java.sql.Timestamp;

public class UpdateTimeDTO {
    int IDUpdateTime;
    int MaxRepeat;
    Timestamp QuantumTime;
    public UpdateTimeDTO()
    {
    }

    public int getIDUpdateTime() {
        return IDUpdateTime;
    }

    public void setIDUpdateTime(int IDUpdateTime) {
        this.IDUpdateTime = IDUpdateTime;
    }

    public int getMaxRepeat() {
        return MaxRepeat;
    }

    public void setMaxRepeat(int MaxRepeat) {
        this.MaxRepeat = MaxRepeat;
    }

    public Timestamp getQuantumTime() {
        return QuantumTime;
    }

    public void setQuantumTime(Timestamp QuantumTime) {
        this.QuantumTime = QuantumTime;
    }
    
}
