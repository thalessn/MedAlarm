package com.gmail.thales_silva_nascimento.alarmmed.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thales on 06/09/17.
 */

public class AlarmeInfo implements Parcelable {

    private String horario;
    private float qtd_tomar;
    private long idAlarme;

    public AlarmeInfo(String horario, float qtd_tomar){
        this.horario = horario;
        this.qtd_tomar = qtd_tomar;
    }

    public String getHorario() {
        return horario;
    }

    public float getQtd_tomar() {
        return qtd_tomar;
    }

    public long getIdAlarme(){
        return idAlarme;
    }

    public void setIdAlarme(long id){
        this.idAlarme = id;
    }

    @Override
    public String toString() {
        return "Alarme Info Horario: " +horario+ " Qtd_tomar: " +String.valueOf(qtd_tomar)+ " idAlarme: " +String.valueOf(idAlarme);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.horario);
        dest.writeFloat(this.qtd_tomar);
        dest.writeLong(this.idAlarme);
    }

    protected AlarmeInfo(Parcel in) {
        this.horario = in.readString();
        this.qtd_tomar = in.readFloat();
        this.idAlarme = in.readLong();
    }

    public static final Parcelable.Creator<AlarmeInfo> CREATOR = new Parcelable.Creator<AlarmeInfo>() {
        @Override
        public AlarmeInfo createFromParcel(Parcel source) {
            return new AlarmeInfo(source);
        }

        @Override
        public AlarmeInfo[] newArray(int size) {
            return new AlarmeInfo[size];
        }
    };
}
