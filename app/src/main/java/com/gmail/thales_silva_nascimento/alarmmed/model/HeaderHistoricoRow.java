package com.gmail.thales_silva_nascimento.alarmmed;

import java.util.Calendar;

/**
 * Created by Thales on 25/02/2018.
 */

public class HeaderHistoricoRow extends ListItemHistorico {

    private String dataProg;
    public HeaderHistoricoRow(String data){
        this.dataProg = data;

    }

    public Calendar getDataProg(){
        return Utils.DataStringToCalendar(dataProg);
    }

    @Override
    public int getType() {
        return TYPE_HEADER;
    }

    @Override
    public String toString() {
        return dataProg;
    }
}
