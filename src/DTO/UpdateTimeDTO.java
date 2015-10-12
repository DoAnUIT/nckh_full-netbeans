/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DTO;

/**
 *
 * @author ngthi
 */
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
