package com.gmail.thales_silva_nascimento.alarmmed.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thales on 06/09/17.
 */

public class Horario implements Parcelable {

    private long id;
    private String horario;

    public Horario(String horario){
        this.horario = horario;
    }

    public Horario(long id, String horario){
        this.id = id;
        this.horario = horario;
    }

    public String getHorario(){
        return horario;
    }

    public void setHorario(String horario){
        this.horario = horario;
    }

    public long getId(){
        return id;
    }

    @Override
    public String toString() {
        return horario;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.horario);
    }

    protected Horario(Parcel in) {
        this.id = in.readLong();
        this.horario = in.readString();
    }

    public static final Parcelable.Creator<Horario> CREATOR = new Parcelable.Creator<Horario>() {
        @Override
        public Horario createFromParcel(Parcel source) {
            return new Horario(source);
        }

        @Override
        public Horario[] newArray(int size) {
            return new Horario[size];
        }
    };
}
